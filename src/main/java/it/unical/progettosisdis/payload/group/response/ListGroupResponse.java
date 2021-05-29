package it.unical.progettosisdis.payload.group.response;

import it.unical.progettosisdis.payload.group.response.GetGroupResponse;

import java.util.List;

public class ListGroupResponse {

    private List<GetGroupResponse> groups;

    public ListGroupResponse(List<GetGroupResponse> groups) {
        this.groups = groups;
    }

    public List<GetGroupResponse> getListResponse() {
        return groups;
    }

    public void setListResponse(List<GetGroupResponse> listResponse) {
        this.groups = groups;
    }
}

