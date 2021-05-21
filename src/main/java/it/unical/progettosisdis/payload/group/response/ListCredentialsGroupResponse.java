package it.unical.progettosisdis.payload.group.response;

import it.unical.progettosisdis.payload.group.response.CredentialsGroupResponse;

import java.util.List;

public class ListCredentialsGroupResponse {

    private List<CredentialsGroupResponse> credentials;

    public ListCredentialsGroupResponse(List<CredentialsGroupResponse> credentials) {
        this.credentials = credentials;
    }

    public List<CredentialsGroupResponse> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<CredentialsGroupResponse> credentials) {
        this.credentials = credentials;
    }
}
