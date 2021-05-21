package it.unical.progettosisdis.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", targetEntity = Credentials.class, cascade = CascadeType.REMOVE)
    private Set<Credentials> credentials;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", targetEntity = SecureNotes.class, cascade = CascadeType.REMOVE)
    private Set<SecureNotes> secureNotes;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<Groups> groups = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    @MapKeyJoinColumn(name = "group_id")
    private Map<Groups, Role> group_user_role = new HashMap<>();

    public User() { }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Credentials> getCredentials() {
        return credentials;
    }

    public void setCredentials(Set<Credentials> credentials) {
        this.credentials = credentials;
    }

    public Set<SecureNotes> getSecureNotes() {
        return secureNotes;
    }

    public void setSecureNotes(Set<SecureNotes> secureNotes) {
        this.secureNotes = secureNotes;
    }

    public Map<Groups, Role> getGroup_user_role() {
        return group_user_role;
    }

    public void setGroup_user_role(Map<Groups, Role> group_user_role) {
        this.group_user_role = group_user_role;
    }

    public Set<Groups> getGroups() {
        return groups;
    }

    public void setGroups(Set<Groups> groups) {
        this.groups = groups;
    }

    @PreRemove
    private void removeGroupFromUser() {
        for(Groups g : group_user_role.keySet()) {
            g.getUsers().remove(this);
        }
        roles.clear();
    }

    public boolean addGroup(Groups group) {
        return groups.add(group);
    }


}
