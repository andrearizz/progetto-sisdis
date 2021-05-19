package it.unical.progettosisdis.entity;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String joinCode;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
    private Set<User> users = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "groups", targetEntity = CredentialsGroup.class, cascade = CascadeType.REMOVE)
    private Set<CredentialsGroup> credentialsGroups;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "groups", targetEntity = SecureNotesGroup.class, cascade = CascadeType.REMOVE)
    private Set<SecureNotesGroup> secureNotesGroups;

    public Groups() { }

    public Groups(String name, String joinCode) {
        this.name = name;
        this.joinCode = joinCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }


    public Set<CredentialsGroup> getCredentialsGroups() {
        return credentialsGroups;
    }

    public void setCredentialsGroups(Set<CredentialsGroup> credentialsGroups) {
        this.credentialsGroups = credentialsGroups;
    }

    public Set<SecureNotesGroup> getSecureNotesGroups() {
        return secureNotesGroups;
    }

    public void setSecureNotesGroups(Set<SecureNotesGroup> secureNotesGroups) {
        this.secureNotesGroups = secureNotesGroups;
    }

    @PreRemove
    private void removeGroupsFromUser() {
        for (User u : users) {
            u.getGroup_user_role().remove(this);
            u.getGroups().remove(this);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Groups)) return false;

        Groups groups = (Groups) o;

        if (id != null ? !id.equals(groups.id) : groups.id != null) return false;
        if (name != null ? !name.equals(groups.name) : groups.name != null) return false;
        if (joinCode != null ? !joinCode.equals(groups.joinCode) : groups.joinCode != null) return false;
        if (users != null ? !users.equals(groups.users) : groups.users != null) return false;
        if (credentialsGroups != null ? !credentialsGroups.equals(groups.credentialsGroups) : groups.credentialsGroups != null)
            return false;
        return secureNotesGroups != null ? secureNotesGroups.equals(groups.secureNotesGroups) : groups.secureNotesGroups == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (joinCode != null ? joinCode.hashCode() : 0);
        result = 31 * result + (users != null ? users.hashCode() : 0);
        result = 31 * result + (credentialsGroups != null ? credentialsGroups.hashCode() : 0);
        result = 31 * result + (secureNotesGroups != null ? secureNotesGroups.hashCode() : 0);
        return result;
    }
}
