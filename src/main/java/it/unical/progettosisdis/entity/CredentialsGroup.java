package it.unical.progettosisdis.entity;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class CredentialsGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "grupoId", referencedColumnName = "id")
    private Groups groups;

    @NotBlank
    private String url;

    @NotNull
    private String login;

    @NotBlank
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;

    @NotBlank
    private String userLastChange;

    @NotBlank
    private String userCreator;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLastModify;

    public CredentialsGroup() { }

    public CredentialsGroup(Groups groups, String url, String login, String password, Date creationDateTime) {
        this.groups = groups;
        this.url = url;
        this.login = login;
        this.password = password;
        this.creationDateTime = creationDateTime;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
