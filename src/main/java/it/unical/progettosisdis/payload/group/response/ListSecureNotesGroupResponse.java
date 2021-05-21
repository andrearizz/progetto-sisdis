package it.unical.progettosisdis.payload.group.response;

import java.util.List;

public class ListSecureNotesGroupResponse {

    private List<SecureNotesGroupResponse> secureNotesGroupResponses;

    public ListSecureNotesGroupResponse(List<SecureNotesGroupResponse> secureNotesGroupResponses) {
        this.secureNotesGroupResponses = secureNotesGroupResponses;
    }

    public List<SecureNotesGroupResponse> getSecureNotesGroupResponses() {
        return secureNotesGroupResponses;
    }

    public void setSecureNotesGroupResponses(List<SecureNotesGroupResponse> secureNotesGroupResponses) {
        this.secureNotesGroupResponses = secureNotesGroupResponses;
    }
}
