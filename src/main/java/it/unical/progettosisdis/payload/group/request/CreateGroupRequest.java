package it.unical.progettosisdis.payload.group.request;

import it.unical.progettosisdis.entity.User;

public class CreateGroupRequest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
