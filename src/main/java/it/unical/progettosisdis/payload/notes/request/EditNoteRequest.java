package it.unical.progettosisdis.payload.notes.request;

public class EditNoteRequest {

    private Long id;

    private String title;
    private boolean isTitleModified;

    private String content;
    private boolean isContentModified;


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

    public boolean isTitleModified() {
        return isTitleModified;
    }

    public void setIsTitleModified(boolean isTitleModified) {
        this.isTitleModified = isTitleModified;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isContentModified() {
        return isContentModified;
    }

    public void setIsContentModified(boolean isContentModified) {
        this.isContentModified = isContentModified;
    }
}
