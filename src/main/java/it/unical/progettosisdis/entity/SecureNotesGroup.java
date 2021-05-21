package it.unical.progettosisdis.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
public class SecureNotesGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "groupId", referencedColumnName = "id")
    private Groups groups;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;

    @NotBlank
    private String userLastChange;

    @NotBlank
    private String userCreator;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLastModify;

    public SecureNotesGroup() { }

    public SecureNotesGroup(Groups groups, String title, String content, Date creationDateTime, String userCreator) {
        this.groups = groups;
        this.title = title;
        this.content = content;
        this.creationDateTime = creationDateTime;
        this.userCreator = userCreator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getUserLastChange() {
        return userLastChange;
    }

    public void setUserLastChange(String userLastChange) {
        this.userLastChange = userLastChange;
    }

    public String getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(String userCreator) {
        this.userCreator = userCreator;
    }

    public Date getDateLastModify() {
        return dateLastModify;
    }

    public void setDateLastModify(Date dateLastModify) {
        this.dateLastModify = dateLastModify;
    }
}
