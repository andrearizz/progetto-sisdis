package it.unical.progettosisdis.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Credentials {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @NotBlank
    private String url;

    @NotNull
    private String login;

    @NotBlank
    private String password;

    public Credentials(User user, String url, String login, String password) {
        this.user = user;
        this.url = url;
        this.login = login;
        this.password = password;
    }

    public Credentials() {

    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
