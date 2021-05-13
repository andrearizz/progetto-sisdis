package it.unical.progettosisdis.payload.response;

public class CredentialsResponse {

    private String url;

    private String login;

    private String protocol;

    private Long id;

    public CredentialsResponse(String url, String login, String protocol, Long id) {
        this.url = url;
        this.login = login;
        this.protocol = protocol;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getLogin() {
        return login;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

