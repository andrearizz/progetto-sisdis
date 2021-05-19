package it.unical.progettosisdis.payload.credentials.response;

import it.unical.progettosisdis.payload.credentials.response.CredentialsResponse;

import java.util.List;

public class ListCredentianlsResponse {

    private List<CredentialsResponse> credentials;

    public ListCredentianlsResponse(List<CredentialsResponse> credentials) {
        this.credentials = credentials;
    }

    public List<CredentialsResponse> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<CredentialsResponse> credentials) {
        this.credentials = credentials;
    }
}
