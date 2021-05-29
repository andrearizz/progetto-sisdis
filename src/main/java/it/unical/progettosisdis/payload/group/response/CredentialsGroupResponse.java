package it.unical.progettosisdis.payload.group.response;

import java.util.Date;

public class CredentialsGroupResponse {

    private String url;

    private String login;

    private String protocol;

    private Long id;

    private String lastChanger;


    private String dayCreation;
    private String hoursCreation;

    private String dayLastModify;
    private String hoursLastModify;


    private String creator;

    private String version;

    public CredentialsGroupResponse(String url, String login, String protocol, Long id, String lastChanger,
                                    String dayCreation, String hoursCreation, String dayLastModify,
                                    String hoursLastModify, String creator, String version) {
        this.url = url;
        this.login = login;
        this.protocol = protocol;
        this.id = id;
        this.lastChanger = lastChanger;
        this.dayCreation = dayCreation;
        this.hoursCreation = hoursCreation;
        this.dayLastModify = dayLastModify;
        this.hoursLastModify = hoursLastModify;
        this.creator = creator;
        this.version = version;
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

    public String getDayCreation() {
        return dayCreation;
    }

    public void setDayCreation(String dayCreation) {
        this.dayCreation = dayCreation;
    }

    public String getHoursCreation() {
        return hoursCreation;
    }

    public void setHoursCreation(String hoursCreation) {
        this.hoursCreation = hoursCreation;
    }

    public String getDayLastModify() {
        return dayLastModify;
    }

    public void setDayLastModify(String dayLastModify) {
        this.dayLastModify = dayLastModify;
    }

    public String getHoursLastModify() {
        return hoursLastModify;
    }

    public void setHoursLastModify(String hoursLastModify) {
        this.hoursLastModify = hoursLastModify;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
