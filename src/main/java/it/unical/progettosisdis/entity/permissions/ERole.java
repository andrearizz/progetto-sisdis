package it.unical.progettosisdis.entity.permissions;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static it.unical.progettosisdis.entity.permissions.EPermission.*;

public enum ERole {
    ROLE_USER(Sets.newHashSet(USER_READ,USER_WRITE)),
    ROLE_GROUP_ADMIN(Sets.newHashSet(USER_READ,USER_WRITE, GROUP_READ, GROUP_WRITE, GROUP_MODIFY)),
    ROLE_GROUP_USER_BAS(Sets.newHashSet(USER_READ, USER_WRITE, GROUP_READ)),
    ROLE_GROUP_USER_ADV(Sets.newHashSet(USER_READ, USER_WRITE, GROUP_READ, GROUP_WRITE));


    private final Set<EPermission> permissions;

    ERole(Set<EPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<EPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority(this.name()));
        return permissions;
    }


}
