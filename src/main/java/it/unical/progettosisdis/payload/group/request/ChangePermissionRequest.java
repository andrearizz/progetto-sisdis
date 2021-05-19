package it.unical.progettosisdis.payload.group.request;

public class ChangePermissionRequest {

    private String usernameToChange;

    // true means add permission, false means remove permission
    private boolean modeAdd;

    private String permission;

    public String getUsernameToChange() {
        return usernameToChange;
    }

    public void setUsernameToChange(String usernameToChange) {
        this.usernameToChange = usernameToChange;
    }

    public boolean isModeAdd() {
        return modeAdd;
    }

    public void setMode(boolean modeAdd) {
        this.modeAdd = modeAdd;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
