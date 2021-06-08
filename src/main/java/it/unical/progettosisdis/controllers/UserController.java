package it.unical.progettosisdis.controllers;


import it.unical.progettosisdis.entity.Credentials;
import it.unical.progettosisdis.entity.User;
import it.unical.progettosisdis.payload.credentials.request.EditRequest;
import it.unical.progettosisdis.payload.credentials.request.SavePwRequest;
import it.unical.progettosisdis.payload.credentials.request.SeePwRequest;
import it.unical.progettosisdis.payload.credentials.response.CredentialsResponse;
import it.unical.progettosisdis.payload.credentials.response.ListCredentianlsResponse;
import it.unical.progettosisdis.payload.MessageResponse;
import it.unical.progettosisdis.repository.CredentialsRepository;
import it.unical.progettosisdis.repository.UserRepository;
import it.unical.progettosisdis.utils.Encrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CredentialsRepository credentialsRepository;

    @Autowired
    Encrypter encrypter;

    @Value("${app.andrea.secret.key}")
    private String secretKey;


    private static final Pattern patternUrl = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");

    @PostMapping("/savePw")
    public ResponseEntity<?> save(@Valid @RequestBody SavePwRequest savePwRequest) throws
            InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException, InvalidParameterSpecException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        String url = savePwRequest.getUrl();
        String protocol = "";
        Matcher matcher = patternUrl.matcher(url);
        if(matcher.find()) {
            protocol = matcher.group(2);
        }
        if(protocol == null) {
            protocol = "https";
            url = protocol + "://" + url;
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if(credentialsRepository.existsByUserAndUrlAndLogin(user, url, savePwRequest.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Credentials for site " + url + " alrady exists"));
        }


        String algorithm = "AES/GCM/NoPadding";
        String encryptedPassword = encrypter.encrypt(savePwRequest.getPassword(), secretKey);

        Credentials credentials = new Credentials(user, url,
                savePwRequest.getLogin(), encryptedPassword);

        credentialsRepository.save(credentials);
        return ResponseEntity.ok(new MessageResponse("Password save successfully!"));
    }

    @PostMapping("/seePw")
    public ResponseEntity<?> getPassword(@Valid @RequestBody SeePwRequest req) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Credentials credentials = credentialsRepository
                .findCredentialsByUrlAndUserAndLogin(req.getUrl(), user, req.getLogin()).
                orElseThrow(() -> new RuntimeException("Credential not found with url: " + req.getUrl() + " and login credential " + req.getLogin()
                        + " and username " + user.getUsername()));

        String password = encrypter.decrypt(credentials.getPassword(), secretKey);
        return ResponseEntity.ok(new MessageResponse(password));
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCredentials() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        /*
        List<Credentials> allCredentials = credentialsRepository.findAllByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
         */

        Set<Credentials> allCredentials = user.getCredentials();
        List<CredentialsResponse> credentials = new LinkedList<>();
        for(Credentials c : allCredentials) {
            String protocol = "";
            Matcher matcher = patternUrl.matcher(c.getUrl());
            if(matcher.find()) {
                protocol = matcher.group(2);
                System.out.println(protocol);
            }

            credentials.add(new CredentialsResponse(c.getUrl(), c.getLogin(), protocol, c.getId()));
        }
        return ResponseEntity.ok(new ListCredentianlsResponse(credentials));

    }

    @DeleteMapping("/{id}/delPw")
    public ResponseEntity<?> deletePw(@PathVariable Long id) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        //String url = protocol + "://" + site;

        Credentials credentials = credentialsRepository.findCredentialsByUserAndId(user ,id)
                .orElseThrow(() -> new RuntimeException("Credential not found with id " + id + " or user" + username));

        credentialsRepository.delete(credentials);
        return ResponseEntity.ok(new MessageResponse("Credential deleted successfully"));
    }


    @PutMapping("/edit")
    public ResponseEntity<?> edit(@Valid @RequestBody EditRequest req) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidParameterSpecException, InvalidKeyException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        long id = req.getId();
        Credentials credentials = credentialsRepository
                .findCredentialsById(id)
                .orElseThrow(() -> new RuntimeException("Credential not found with id " + id));


        if(req.isUrlModified()) {
            credentials.setUrl(req.getUrl());
        }

        if(req.isLoginModified()) {
            credentials.setLogin(req.getLogin());
        }

        if(req.isPasswordModified()) {
            String encryptedPassword = encrypter.encrypt(req.getPassword(), secretKey);
            credentials.setPassword(encryptedPassword);
        }

        credentialsRepository.save(credentials);
        return ResponseEntity.ok(new MessageResponse("Credential modified successfully!"));
    }
    /*

    @PutMapping("/putETag/{id}")
    public ResponseEntity<?> putETag(WebRequest request, @PathVariable Long id, @Valid @RequestBody EditRequest ereq) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidParameterSpecException, InvalidKeyException {
        Credentials credentials = credentialsRepository
                .findCredentialsById(id)
                .orElseThrow(() -> new RuntimeException("Credential not found with id " + id));

        String ifMatchValue = request.getHeader("If-Match");
        if(ifMatchValue == null) {
            return ResponseEntity.badRequest().build();
        }

        if(!ifMatchValue.equals("\"" + "provaVersion" + "\"")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        try {
            if (ereq.isUrlModified()) {
                credentials.setUrl(ereq.getUrl());
            }

            if (ereq.isLoginModified()) {
                credentials.setLogin(ereq.getLogin());
            }

            if (ereq.isPasswordModified()) {
                String encryptedPassword = encrypter.encrypt(ereq.getPassword(), secretKey);
                credentials.setPassword(encryptedPassword);
            }

            credentialsRepository.save(credentials);
            return ResponseEntity.ok()
                    .eTag("\"" + "provaVersion2" + "\"")
                    .body(new MessageResponse("Credentials update successfully"));
        } catch (OptimisticLockingFailureException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

     */

}
