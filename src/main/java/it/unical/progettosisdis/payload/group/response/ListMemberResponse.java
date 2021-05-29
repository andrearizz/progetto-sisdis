package it.unical.progettosisdis.payload.group.response;

import java.util.List;

public class ListMemberResponse {

    private List<MemberResponse> members;

    public ListMemberResponse(List<MemberResponse> members) {
        this.members = members;
    }

    public List<MemberResponse> getMembers() {
        return members;
    }

    public void setMembers(List<MemberResponse> members) {
        this.members = members;
    }
}
