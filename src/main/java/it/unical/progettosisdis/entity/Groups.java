package it.unical.progettosisdis.entity;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

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
    private List<User> users = new LinkedList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "groups", targetEntity = CredentialsGroup.class, cascade = CascadeType.REMOVE)
    private List<CredentialsGroup> credentialsGroups;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "groups", targetEntity = SecureNotesGroup.class, cascade = CascadeType.REMOVE)
    private List<SecureNotesGroup> secureNotesGroups;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;

    @NotBlank
    private String creatorUsername;

    public Groups() { }

    public Groups(String name, String joinCode, Date creationDateTime, String creatorUsername) {
        this.name = name;
        this.joinCode = joinCode;
        this.creationDateTime = creationDateTime;
        this.creatorUsername = creatorUsername;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    public List<CredentialsGroup> getCredentialsGroups() {
        return credentialsGroups;
    }

    public void setCredentialsGroups(List<CredentialsGroup> credentialsGroups) {
        this.credentialsGroups = credentialsGroups;
    }

    public List<SecureNotesGroup> getSecureNotesGroups() {
        return secureNotesGroups;
    }

    public void setSecureNotesGroups(List<SecureNotesGroup> secureNotesGroups) {
        this.secureNotesGroups = secureNotesGroups;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    @PreRemove
    private void removeGroupsFromUser() {
        for (User u : users) {
            u.getGroup_user_role().remove(this);
            u.getGroups().remove(this);
        }
    }
}
