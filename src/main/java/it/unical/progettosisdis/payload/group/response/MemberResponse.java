package it.unical.progettosisdis.payload.group.response;

public class MemberResponse {

    String username;
    String role;

    public MemberResponse(String username, String role) {
        this.username = username;
        this.role = role;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
