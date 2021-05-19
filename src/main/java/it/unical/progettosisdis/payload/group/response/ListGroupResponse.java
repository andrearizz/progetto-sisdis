package it.unical.progettosisdis.payload.group.response;

import it.unical.progettosisdis.payload.group.response.GetGroupResponse;

import java.util.List;

public class ListGroupResponse {

    List<GetGroupResponse> listResponse;

    public ListGroupResponse(List<GetGroupResponse> listResponse) {
        this.listResponse = listResponse;
    }

    public List<GetGroupResponse> getListResponse() {
        return listResponse;
    }

    public void setListResponse(List<GetGroupResponse> listResponse) {
        this.listResponse = listResponse;
    }
}

