package it.unical.progettosisdis.payload.group.response;

import java.util.Date;

public class SecureNotesGroupResponse {

    private Long id;

    private String title;

    private String lastChanger;

    private Date changeTamestamp;

    private Date creationTime;

    private String creator;

    public SecureNotesGroupResponse(Long id, String title, String lastChanger, Date changeTamestamp,
                                    Date creationTime, String creator) {
        this.id = id;
        this.title = title;
        this.lastChanger = lastChanger;
        this.changeTamestamp = changeTamestamp;
        this.creationTime = creationTime;
        this.creator = creator;
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
