package it.unical.progettosisdis.security;

import it.unical.progettosisdis.entity.Groups;
import it.unical.progettosisdis.entity.Role;
import it.unical.progettosisdis.entity.permissions.EPermission;
import it.unical.progettosisdis.repository.GroupRepository;
import it.unical.progettosisdis.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component("authorityChecker")
public class AuthorityChecker {

    @Autowired
    GroupRepository groupRepository;
    
    public boolean hasTheAuthority(Authentication authentication, Long id, String authority) {
        Groups groups = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        Role role = null;
        for(Groups g : user.getGroups()){
            if(g.getId().equals(groups.getId()))
                role = user.getGroupsRole().get(g);
        }
        assert role != null;
        Set<EPermission> x = role.getName().getPermissions();
        for (EPermission p : x) {
            if(p.getPermission().equals(authority))
                return true;
        }
        return false;
    }
}
