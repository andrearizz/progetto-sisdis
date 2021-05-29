package it.unical.progettosisdis.payload.group.request;

public class ChangePermissionRequest {

    private String usernameToChange;

    private String permission;

    public String getUsernameToChange() {
        return usernameToChange;
    }

    public void setUsernameToChange(String usernameToChange) {
        this.usernameToChange = usernameToChange;
    }


    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
