package it.unical.progettosisdis.payload.credentials.request;

public class EditRequest {

    private Long id;

    private String url;
    private boolean isUrlModified;

    private String login;
    private boolean isLoginModified;

    private String password;
    private boolean isPasswordModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUrlModified() {
        return isUrlModified;
    }

    public void setIsUrlModified(boolean isUrlModified) {
        this.isUrlModified = isUrlModified;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isLoginModified() {
        return isLoginModified;
    }

    public void setIsLoginModified(boolean isLoginModified) {
        this.isLoginModified = isLoginModified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPasswordModified() {
        return isPasswordModified;
    }

    public void setIsPasswordModified(boolean isPasswordModified) {
        this.isPasswordModified = isPasswordModified;
    }
}
