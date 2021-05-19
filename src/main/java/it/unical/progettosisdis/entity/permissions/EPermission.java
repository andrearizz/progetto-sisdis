package it.unical.progettosisdis.entity.permissions;

public enum EPermission {
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    GROUP_READ("group:read"),
    GROUP_WRITE("group:write"),
    GROUP_MODIFY("group:modify");


    private final String permission;

    EPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
