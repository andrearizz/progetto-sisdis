package it.unical.progettosisdis.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.unical.progettosisdis.entity.Groups;
import it.unical.progettosisdis.entity.Role;
import it.unical.progettosisdis.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Map<Groups, Role> groupsRole;

    private Set<Groups> groups;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String password, Map<Groups, Role> groupsRole,
                           Collection<? extends GrantedAuthority> authorities, Set<Groups> groups) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.groupsRole = groupsRole;
        this.authorities = authorities;
        this.groups = groups;

    }


    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> role.getName().getGrantedAuthorities())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(),user.getGroup_user_role(), authorities, user.getGroups());
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Map<Groups, Role> getGroupsRole() {
        return groupsRole;
    }

    public void setGroupsRole(Map<Groups, Role> groupsRole) {
        this.groupsRole = groupsRole;
    }

    public Set<Groups> getGroups() {
        return groups;
    }

    public void setGroups(Set<Groups> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDetailsImpl)) return false;

        UserDetailsImpl that = (UserDetailsImpl) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        return password != null ? password.equals(that.password) : that.password == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
