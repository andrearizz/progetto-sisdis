package it.unical.progettosisdis.controllers;


import it.unical.progettosisdis.entity.SecureNotes;
import it.unical.progettosisdis.entity.User;
import it.unical.progettosisdis.payload.request.EditNoteRequest;
import it.unical.progettosisdis.payload.request.SecureNoteRequest;
import it.unical.progettosisdis.payload.request.ContentRequest;
import it.unical.progettosisdis.payload.response.ListSecureNoteResponse;
import it.unical.progettosisdis.payload.response.MessageResponse;
import it.unical.progettosisdis.payload.response.SecureContentResponse;
import it.unical.progettosisdis.payload.response.SecureNotesResponse;
import it.unical.progettosisdis.repository.SecureNotesRepository;
import it.unical.progettosisdis.repository.UserRepository;
import it.unical.progettosisdis.utils.Encrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.LinkedList;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/secure-notes")
public class SecureNotesController {


    @Autowired
    private SecureNotesRepository secureNotesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    Encrypter encrypter;

    @Value("${app.andrea.secret.key}")
    private String secretKey;

    @PostMapping("/saveNote")
    public ResponseEntity<?> saveNote(@Valid @RequestBody SecureNoteRequest req) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidParameterSpecException, InvalidKeyException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        String title = req.getTitle();
        if(secureNotesRepository.existsByTitleAndUser(title, user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Note with title: " + title + " already exits"));
        }

        String encryptedNote = encrypter.encrypt(req.getContent(), secretKey);

        SecureNotes secureNote = new SecureNotes(user, title, encryptedNote);

        secureNotesRepository.save(secureNote);
        return ResponseEntity.ok(new MessageResponse("Note saved successfully"));
    }

    @GetMapping("/getAllNotes")
    public ResponseEntity<?> getAllNotes() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));


        List<SecureNotes> notes = secureNotesRepository.getAllNotesByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<SecureNotesResponse> secureNotes = new LinkedList<>();
        for(SecureNotes sn: notes) {
            secureNotes.add(new SecureNotesResponse(sn.getId(), sn.getTitle()));
        }

        return ResponseEntity.ok(new ListSecureNoteResponse(secureNotes));
    }


    @PostMapping("/getContent")
    public ResponseEntity<?> getContent(@Valid @RequestBody ContentRequest req) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {

        Long id = req.getId();

        SecureNotes notes = secureNotesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note searched not found"));

        System.out.println(notes.getContent());

        String plainText = encrypter.decrypt(notes.getContent(), secretKey);

        System.out.println(plainText);
       return ResponseEntity.ok(new SecureContentResponse(plainText));
    }

    @DeleteMapping("/{id}/deleteNote")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {

        /*

        SecureNotes secureNotes = secureNotesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note searched not found"));


         */
        secureNotesRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Note deleted successfully"));
    }

    @PostMapping("/editNote")
    public ResponseEntity<?> editNote(@Valid @RequestBody EditNoteRequest req) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, InvalidParameterSpecException {

        long id = req.getId();

        System.out.println(id + " " + req.getTitle() + " " + req.isTitleModified()+ " " + req.getContent() + " " +
                req.isContentModified());

        SecureNotes secureNotes = secureNotesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note searched not found"));

        if(req.isTitleModified()) {
            secureNotes.setTitle(req.getTitle());
        }

        if(req.isContentModified()) {
            String encryptedContent = encrypter.encrypt(req.getContent(), secretKey);
            secureNotes.setContent(encryptedContent);
        }

        secureNotesRepository.save(secureNotes);
        return ResponseEntity.ok(new MessageResponse("Note updated successfully"));
    }
}
