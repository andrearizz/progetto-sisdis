package it.unical.progettosisdis.controllers;


import it.unical.progettosisdis.entity.Credentials;
import it.unical.progettosisdis.entity.User;
import it.unical.progettosisdis.payload.request.SavePwRequest;
import it.unical.progettosisdis.payload.request.seePwRequest;
import it.unical.progettosisdis.payload.response.MessageResponse;
import it.unical.progettosisdis.repository.CredentialsRepository;
import it.unical.progettosisdis.repository.UserRepository;
import it.unical.progettosisdis.utils.Encrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CredentialsRepository credentialsRepository;

    @Autowired
    Encrypter encrypter;

    @Value("${app.andrea.secret.key}")
    private String secretKey;

    @Value("${app.andrea.secret.iv}")
    private String iv;

    @PostMapping("/savePw")
    public ResponseEntity<?> save(@Valid @RequestBody SavePwRequest savePwRequest) throws
            InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException, InvalidParameterSpecException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));


        String algorithm = "AES/GCM/NoPadding";
        String cipherPassword = encrypter.encrypt(savePwRequest.getPassword(), secretKey);

        Credentials credentials = new Credentials(user, savePwRequest.getUrl(),
                savePwRequest.getLogin(), cipherPassword);

        credentialsRepository.save(credentials);
        return ResponseEntity.ok(new MessageResponse("Password save successfully!"));
    }

    @PostMapping("/seePw")
    public ResponseEntity<?> getPassword(@Valid @RequestBody seePwRequest req) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Credentials credentials = credentialsRepository.findCredentialsByUrlAndUser(req.getUrl(), user).
                orElseThrow(() -> new RuntimeException("Credential not found with url: " + req.getUrl() + " and " + user.getUsername()));

        String password = encrypter.decrypt(credentials.getPassword(), secretKey);
        return ResponseEntity.ok(new MessageResponse(password));
    }
}
