package it.unical.progettosisdis.payload.group.response;

import java.util.Date;

public class GetGroupResponse {

    private Long id;
    private String name;
    private String code;
    private String day;
    private String hours;
    private String creatorName;

    public GetGroupResponse(Long id, String name, String code, String day, String hours, String creatorName) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.day = day;
        this.hours = hours;
        this.creatorName = creatorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
