package it.unical.progettosisdis.controllers;

import it.unical.progettosisdis.entity.*;
import it.unical.progettosisdis.entity.permissions.ERole;
import it.unical.progettosisdis.payload.group.request.ChangePermissionRequest;
import it.unical.progettosisdis.payload.group.request.CreateGroupRequest;
import it.unical.progettosisdis.payload.group.request.JoinRequest;
import it.unical.progettosisdis.payload.group.response.GetGroupResponse;
import it.unical.progettosisdis.payload.group.response.ListGroupResponse;
import it.unical.progettosisdis.payload.MessageResponse;
import it.unical.progettosisdis.repository.GroupRepository;
import it.unical.progettosisdis.repository.RoleRepository;
import it.unical.progettosisdis.repository.UserRepository;
import it.unical.progettosisdis.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


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


    @PostMapping("/createGroup")
    public ResponseEntity<?> createGroup(@Valid @RequestBody CreateGroupRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        String code = generator.passwordGenerator(6, true, true, false);


        Groups group = new Groups(request.getName(), code);


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

    @GetMapping("/getAll/{userId}")
    @PreAuthorize("@authorityChecker.hasTheAuthority(authentication, #userId, 'group:read')")
    public ResponseEntity<?> getAll(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

       Set<Groups> groups = user.getGroups();

       List<GetGroupResponse> groupsResponse = new LinkedList<>();
       for(Groups g : groups) {
           groupsResponse.add(new GetGroupResponse(g.getId(), g.getName(), g.getJoinCode()));
       }

        return ResponseEntity.ok(new ListGroupResponse(groupsResponse));
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
        return ResponseEntity.ok(new MessageResponse("Role changed successfully"));
    }


    @PostMapping("/join")
    public ResponseEntity<?> joinGroup( @Valid @RequestBody JoinRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Groups groups = groupRepository.findGroupsByJoinCode(request.getCode())
                .orElseThrow(() -> new RuntimeException("Group not found with Join Code " + request.getCode()));

        Role role = roleRepository.findByName(ERole.ROLE_GROUP_USER_BAS)
                .orElseThrow(() -> new RuntimeException("No role found"));

        if(!user.addGroup(groups)) {
            ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Cannot add user " + user.getUsername() + " to group"));
        }

        Map<Groups, Role> groupsRoleMap = user.getGroup_user_role();
        groupsRoleMap.put(groups, role);
        user.setGroup_user_role(groupsRoleMap);
        groupRepository.save(groups);
        return ResponseEntity.ok(new MessageResponse("You have join the group"));
    }



}
