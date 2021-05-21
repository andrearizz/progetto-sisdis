package it.unical.progettosisdis.payload.group.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddCredentialGroupRequest {

    @NotBlank
    private String url;

    @NotNull
    private String login;

    @NotBlank
    private String password;

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
}
