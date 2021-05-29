package it.unical.progettosisdis.payload.group.response;

import java.util.Date;

public class SecureNotesGroupResponse {

    private Long id;

    private String title;

    private String lastChanger;

    private String dayCreation;
    private String hoursCreation;

    private String dayLastModify;
    private String hoursLastModify;

    private String creator;

    private String version;
    private String content;

    public SecureNotesGroupResponse(Long id, String title, String lastChanger, String dayCreation,
                                    String hoursCreation, String dayLastModify, String hoursLastModify,
                                    String creator, String version, String content) {
        this.id = id;
        this.title = title;
        this.lastChanger = lastChanger;
        this.dayCreation = dayCreation;
        this.hoursCreation = hoursCreation;
        this.dayLastModify = dayLastModify;
        this.hoursLastModify = hoursLastModify;
        this.creator = creator;
        this.version = version;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
