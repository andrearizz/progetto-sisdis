package it.unical.progettosisdis.payload.group.response;

import java.util.Date;

public class CredentialsGroupResponse {

    private String url;

    private String login;

    private String protocol;

    private Long id;

    private String lastChanger;

    private Date changeTamestamp;

    private Date creationTime;

    private String creator;

    public CredentialsGroupResponse(String url, String login, String protocol, Long id, String lastChanger,
                                    Date changeTamestamp, Date creationTime, String creator) {
        this.url = url;
        this.login = login;
        this.protocol = protocol;
        this.id = id;
        this.lastChanger = lastChanger;
        this.changeTamestamp = changeTamestamp;
        this.creationTime = creationTime;
        this.creator = creator;
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

    public String getProtocol() {
        return protocol;
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

    public String getLastChanger() {
        return lastChanger;
    }

    public void setLastChanger(String lastChanger) {
        this.lastChanger = lastChanger;
    }

    public Date getChangeTamestamp() {
        return changeTamestamp;
    }

    public void setChangeTamestamp(Date changeTamestamp) {
        this.changeTamestamp = changeTamestamp;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
