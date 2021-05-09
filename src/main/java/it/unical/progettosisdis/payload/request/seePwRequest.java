package it.unical.progettosisdis.payload.request;

import javax.validation.constraints.NotBlank;

public class seePwRequest {

    @NotBlank
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
