package it.unical.progettosisdis.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class SecureNotesGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "groupId", referencedColumnName = "id")
    private Groups groups;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    public SecureNotesGroup() { }

    public SecureNotesGroup(Groups groups, String title, String content) {
        this.groups = groups;
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
