package it.unical.progettosisdis.payload.response;

import java.util.List;

public class ListSecureNoteResponse {

    private List<SecureNotesResponse> secureNotes;

    public ListSecureNoteResponse(List<SecureNotesResponse> secureNotes) {
        this.secureNotes = secureNotes;
    }

    public List<SecureNotesResponse> getSecureNotes() {
        return secureNotes;
    }

    public void setSecureNotes(List<SecureNotesResponse> secureNotes) {
        this.secureNotes = secureNotes;
    }
}
