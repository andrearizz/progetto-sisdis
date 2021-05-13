package it.unical.progettosisdis.payload.request;

import javax.validation.constraints.NotBlank;

public class seePwRequest {

    @NotBlank
    private String url;

    @NotBlank
    private String login;

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
}
