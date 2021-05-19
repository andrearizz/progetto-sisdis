package it.unical.progettosisdis.payload.notes.response;

import it.unical.progettosisdis.payload.notes.response.SecureNotesResponse;

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
