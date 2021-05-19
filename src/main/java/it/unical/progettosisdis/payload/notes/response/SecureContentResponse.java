package it.unical.progettosisdis.payload.notes.response;

public class SecureContentResponse {

    private String content;

    public SecureContentResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
