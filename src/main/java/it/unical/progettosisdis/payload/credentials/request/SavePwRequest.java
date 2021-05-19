package it.unical.progettosisdis.payload.credentials.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SavePwRequest {

    @NotBlank
    private String url;

    @NotNull
    private String login;

    @NotBlank
    private String password;

    public String getUrl() {
        return url;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
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
