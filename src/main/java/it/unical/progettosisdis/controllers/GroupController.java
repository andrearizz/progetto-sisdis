package it.unical.progettosisdis.controllers;

import it.unical.progettosisdis.entity.*;
import it.unical.progettosisdis.entity.permissions.ERole;
import it.unical.progettosisdis.payload.group.request.*;
import it.unical.progettosisdis.payload.group.response.*;
import it.unical.progettosisdis.payload.MessageResponse;
import it.unical.progettosisdis.repository.*;
import it.unical.progettosisdis.utils.Encrypter;
import it.unical.progettosisdis.utils.Generator;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    private Generator generator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SecureNotesGroupRepository secureNotesGroupRepository;

    @Autowired
    private CredentialsGroupRepository credentialsGroupRepository;

    @Autowired
    Encrypter encrypter;

    @Value("${app.andrea.secret.key}")
    private String secretKey;


    private static final Pattern patternUrl = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");


    @PostMapping("/createGroup")
    public ResponseEntity<?> createGroup(@Valid @RequestBody CreateGroupRequest request) throws ParseException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        String code = generator.passwordGenerator(6, true, true, false);


        Date creationTimestamp = new Date();

        Groups group = new Groups(request.getName(), code, creationTimestamp, username);


        Map<Groups, Role> groupsRoleMap = user.getGroup_user_role();

        Role adminRole = roleRepository.findByName(ERole.ROLE_GROUP_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Default role is not found"));

        if(!user.addGroup(group)) {
            ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Cannot add user " + user.getUsername() + " to group"));
        }

        groupsRoleMap.put(group, adminRole);
        user.setGroup_user_role(groupsRoleMap);
        groupRepository.save(group);
        return ResponseEntity.ok(new MessageResponse("Group creation successfully"));
    }

    @GetMapping("/getAll")
    //@PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #userId, 'group:read')")
    public ResponseEntity<?> getAll() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        Set<Groups> groups = user.getGroups();

        List<GetGroupResponse> groupsResponse = new LinkedList<>();
        for(Groups g : groups) {
           Date date = g.getCreationDateTime();
           DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           String strDate = dateFormat.format(date);
           String day = strDate.substring(0,10);
           String hours = strDate.substring(11);
           groupsResponse.add(new GetGroupResponse(g.getId(), g.getName(), g.getJoinCode(), day, hours,
                   g.getCreatorUsername()));
       }

        return ResponseEntity.ok(new ListGroupResponse(groupsResponse));
    }

    @GetMapping("/getMembers/{groupId}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #groupId, 'group:read')")
    public ResponseEntity<?> getGroupMembers(@PathVariable Long groupId){

        Groups group = groupRepository
                .findById(groupId)
                .orElseThrow(() -> new RuntimeException("Credential not found with id " + groupId));

        List<User> users = group.getUsers();
        List<MemberResponse> members = new LinkedList<>();
        for(User u : users) {
            members.add(new MemberResponse(u.getUsername(), u.getGroup_user_role().get(group).getName().name()));
        }
        return ResponseEntity.ok(new ListMemberResponse(members));
    }

    @DeleteMapping("/deleteGroup/{id}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #id, 'group:modify')")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {

        Groups group = groupRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Credential not found with id " + id));

        groupRepository.delete(group);
        return ResponseEntity.ok(new MessageResponse("Group deleted successfully"));
    }


    @PutMapping("/changePermisison/{groupId}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #groupId, 'group:modify')")
    public ResponseEntity<?> changePermission(@PathVariable Long groupId,
                                              @Valid @RequestBody ChangePermissionRequest request) {
        User user = userRepository.findByUsername(request.getUsernameToChange())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + request.getUsernameToChange()));

        Groups groups = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id " + groupId));

        Role role = roleRepository.findByName(ERole.valueOf(request.getPermission()))
                .orElseThrow(() -> new RuntimeException("No role found"));

        user.getGroup_user_role().put(groups, role);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Role changed successfully"));
    }


    @PostMapping("/join")
    public ResponseEntity<?> joinGroup( @Valid @RequestBody JoinRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Groups groups = groupRepository.findGroupsByJoinCode(request.getCode())
                .orElseThrow(() -> new RuntimeException("Group not found with Join Code " + request.getCode()));

        if(groups.getUsers().contains(user)) {
            return ResponseEntity.badRequest().body(new MessageResponse("You are already in the group"));
        }

        Role role = roleRepository.findByName(ERole.ROLE_GROUP_USER_BAS)
                .orElseThrow(() -> new RuntimeException("No role found"));

        if(!user.addGroup(groups)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Cannot add user " + user.getUsername() + " to group"));
        }


        Map<Groups, Role> groupsRoleMap = user.getGroup_user_role();
        groupsRoleMap.put(groups, role);
        user.setGroup_user_role(groupsRoleMap);
        groupRepository.save(groups);
        return ResponseEntity.ok(new MessageResponse("You have join the group"));
    }


    @PostMapping("/addCredential/{groupId}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #groupId, 'group:write')")
    public ResponseEntity<?> addCredential(@PathVariable Long groupId,
                                           @Valid @RequestBody AddCredentialGroupRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidParameterSpecException, InvalidKeyException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Groups groups = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id " + groupId));

        String url = request.getUrl();
        String protocol = "";
        Matcher matcher = patternUrl.matcher(url);
        if(matcher.find()) {
            protocol = matcher.group(2);
        }
        if(protocol == null) {
            protocol = "https";
            url = protocol + "://" + url;
        }

        if(credentialsGroupRepository.existsByGroupsAndUrlAndLogin(groups, url, request.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Credentials for site " + url + " alrady exists"));
        }

        Date currentDate = new Date();
        String encryptedPassword = encrypter.encrypt(request.getPassword(), secretKey);
        CredentialsGroup credentialsGroup = new CredentialsGroup(groups, url, request.getLogin(),
                encryptedPassword, currentDate);
        credentialsGroup.setUserLastChange(username);
        credentialsGroup.setUserCreator(username);
        credentialsGroup.setDateLastModify(currentDate);

        credentialsGroupRepository.save(credentialsGroup);
        return ResponseEntity.ok(new MessageResponse("Password saved successfully"));
    }

    @CrossOrigin(exposedHeaders = {"ETag"})
    @GetMapping("/getCredential/{groupId}/{credentialId}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #groupId, 'group:read')")
    public ResponseEntity<?> getPassword(WebRequest webRequest, @PathVariable Long groupId, @PathVariable Long credentialId) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {

        CredentialsGroup credentialsGroup = credentialsGroupRepository.findById(credentialId)
                .orElseThrow(() -> new RuntimeException("Credential not found"));

        String ifMatchValue = webRequest.getHeader("If-Match");
        if(ifMatchValue == null) {
            return ResponseEntity.badRequest().build();
        }

        if(!ifMatchValue.equals("\"" + credentialsGroup.getVersion() + "\"")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }


        String password = encrypter.decrypt(credentialsGroup.getPassword(), secretKey);
        return ResponseEntity.ok()
                .eTag("\"" + credentialsGroup.getVersion() + "\"")
                .body(new MessageResponse(password));
    }

    @GetMapping("/getAllCredentials/{groupId}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #groupId, 'group:read')")
    public ResponseEntity<?> getAllCredentials(@PathVariable Long groupId) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {

        Groups groups = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<CredentialsGroup> allCredentials = groups.getCredentialsGroups();
        List<CredentialsGroupResponse> credentials = new LinkedList<>();
        for (CredentialsGroup c: allCredentials) {
            String protocol = "";
            Matcher matcher = patternUrl.matcher(c.getUrl());
            if(matcher.find()) {
                protocol = matcher.group(2);
            }
            String version = "\"" + c.getVersion() + "\"";

            Date dateCreation = c.getCreationDateTime();
            Date dateLastModify = c.getDateLastModify();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDateCreation = dateFormat.format(dateCreation);
            String strDateLastModify = dateFormat.format(dateLastModify);
            String dayCreation = strDateCreation.substring(0,10);
            String hoursCreation = strDateCreation.substring(11);
            String dayLastModify = strDateLastModify.substring(0,10);
            String hoursLastModify = strDateLastModify.substring(11);


            credentials.add(new CredentialsGroupResponse(c.getUrl(), c.getLogin(), protocol, c.getId(),
                    c.getUserLastChange(), dayCreation, hoursCreation, dayLastModify, hoursLastModify,
                    c.getUserCreator(), version));
        }

        return ResponseEntity.ok(new ListCredentialsGroupResponse(credentials));
    }

    @DeleteMapping("/deleteGroupCredential/{groupId}/{credentialId}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #groupId, 'group:write')")
    public ResponseEntity<?> deleteGroupCredential(@PathVariable Long groupId, @PathVariable Long credentialId) {


        CredentialsGroup credentialsGroup = credentialsGroupRepository.findById(credentialId)
                .orElseThrow(() -> new RuntimeException("Credential not found"));

        credentialsGroupRepository.delete(credentialsGroup);
        return ResponseEntity.ok(new MessageResponse("Credential delete successfully"));
    }

    @PutMapping("/editGroupCredential/{groupId}/{credentialId}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #groupId, 'group:write')")
    public ResponseEntity<?> updateCredential(WebRequest webRequest, @PathVariable Long groupId,
                                              @PathVariable Long credentialId,@Valid @RequestBody EditCredentialGroup request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidParameterSpecException, InvalidKeyException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        CredentialsGroup credentialsGroup = credentialsGroupRepository.findById(credentialId)
                .orElseThrow(() -> new RuntimeException("Credential not found"));

        String ifMatchValue = webRequest.getHeader("If-Match");
        if(ifMatchValue == null) {
            return ResponseEntity.badRequest().build();
        }

        if(!ifMatchValue.equals("\"" + credentialsGroup.getVersion() + "\"")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        try {
            credentialsGroup.setUrl(request.getUrl());
            credentialsGroup.setLogin(request.getLogin());
            String password = encrypter.encrypt(request.getPassword(), secretKey);
            credentialsGroup.setPassword(password);
            credentialsGroup.setUserLastChange(username);
            credentialsGroup.setDateLastModify(new Date());
            credentialsGroupRepository.save(credentialsGroup);
            return ResponseEntity.ok()
                    .eTag("\"" + credentialsGroup.getVersion() + "\"")
                    .body(new MessageResponse("Credentials update successfully"));
        } catch (OptimisticLockingFailureException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/saveGroupNote/{groupId}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #groupId, 'group:write')")
    public ResponseEntity<?> saveGroupNote(@PathVariable Long groupId,
                                           @Valid @RequestBody SecureNoteGroupRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidParameterSpecException, InvalidKeyException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Groups groups = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        String title = request.getTitle();
        if(secureNotesGroupRepository.existsSecureNotesGroupByTitleAndGroups(title, groups)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Note with title: " + title + " already exits"));
        }

        Date currentDate = new Date();
        String encryptedNote = encrypter.encrypt(request.getContent(), secretKey);
        SecureNotesGroup note = new SecureNotesGroup(groups, title, encryptedNote, currentDate, username);
        note.setDateLastModify(currentDate);
        note.setUserLastChange(username);
        secureNotesGroupRepository.save(note);
        return ResponseEntity.ok(new MessageResponse("Note created successfully"));
    }


    @GetMapping("/getAllNotes/{groupId}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #groupId, 'group:read')")
    public ResponseEntity<?> getAllNotes(@PathVariable Long groupId) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        Groups groups = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        List<SecureNotesGroup> notes = groups.getSecureNotesGroups();
        List<SecureNotesGroupResponse> secureNotesGroupResponses = new LinkedList<>();
        for (SecureNotesGroup s: notes) {

            String version = "\"" + s.getVersion() + "\"";

            Date dateCreation = s.getCreationDateTime();
            Date dateLastModify = s.getDateLastModify();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDateCreation = dateFormat.format(dateCreation);
            String strDateLastModify = dateFormat.format(dateLastModify);
            String dayCreation = strDateCreation.substring(0,10);
            String hoursCreation = strDateCreation.substring(11);
            String dayLastModify = strDateLastModify.substring(0,10);
            String hoursLastModify = strDateLastModify.substring(11);

            String plainText = encrypter.decrypt(s.getContent(), secretKey);

            secureNotesGroupResponses.add(new SecureNotesGroupResponse(s.getId(), s.getTitle(), s.getUserLastChange(),
                    dayCreation, hoursCreation, dayLastModify, hoursLastModify, s.getUserCreator(), version, plainText));
        }


        return ResponseEntity.ok(new ListSecureNotesGroupResponse(secureNotesGroupResponses));
    }

    @CrossOrigin(exposedHeaders = {"ETag"})
    @GetMapping("/getContent/{groupId}/{noteId}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #groupId, 'group:read')")
    public ResponseEntity<?> getContent(@PathVariable Long groupId, @PathVariable Long noteId) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {

        SecureNotesGroup secureNotesGroup = secureNotesGroupRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Secure Note not found"));

        String plainText = encrypter.decrypt(secureNotesGroup.getContent(), secretKey);

        return ResponseEntity.ok()
                .eTag("\"" + secureNotesGroup.getVersion() + "\"")
                .body(new MessageResponse(plainText));
    }

    @DeleteMapping("/deleteNote/{groupId}/{noteId}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #groupId, 'group:write')")
    public ResponseEntity<?> deleteNote(@PathVariable Long groupId, @PathVariable Long noteId) {

        SecureNotesGroup secureNotesGroup = secureNotesGroupRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Secure Note not found"));

        secureNotesGroupRepository.delete(secureNotesGroup);
        return ResponseEntity.ok(new MessageResponse("Note delete successfully"));
    }


    @PutMapping("/editNote/{groupId}/{noteId}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #groupId, 'group:write')")
    public ResponseEntity<?> updateNote(WebRequest webRequest, @PathVariable Long groupId,
                                        @PathVariable Long noteId,@Valid @RequestBody EditoNoteGroupRequest request) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeySpecException, InvalidParameterSpecException, InvalidKeyException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        SecureNotesGroup secureNotesGroup = secureNotesGroupRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        String ifMatchValue = webRequest.getHeader("If-Match");
        if(ifMatchValue == null) {
            return ResponseEntity.badRequest().build();
        }

        if(!ifMatchValue.equals("\"" + secureNotesGroup.getVersion() + "\"")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        try {
            secureNotesGroup.setTitle(request.getTitle());
            String encryptedContent = encrypter.encrypt(request.getContent(), secretKey);
            secureNotesGroup.setContent(encryptedContent);
            secureNotesGroup.setDateLastModify(new Date());
            secureNotesGroup.setUserLastChange(username);
            secureNotesGroupRepository.save(secureNotesGroup);
            return ResponseEntity.ok()
                    .eTag("\"" + secureNotesGroup.getVersion() + "\"")
                    .body(new MessageResponse("Note update successfully"));
        } catch (OptimisticLockingFailureException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
