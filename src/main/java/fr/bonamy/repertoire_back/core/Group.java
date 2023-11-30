package fr.bonamy.repertoire_back.core;

import java.util.List;
import java.util.Objects;

public class Group {

    private Long id;
    private String name;
    private List<Contact> contactList;
    private String comment;
    private User user;

    public Group() {
    }

    public Group(Long id, String name, List<Contact> contactList, String comment, User user) {
        this.id = id;
        this.name = name;
        this.contactList = contactList;
        this.comment = comment;
        this.user = user;
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

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group that = (Group) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(contactList, that.contactList) && Objects.equals(comment, that.comment) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contactList, comment, user);
    }

    @Override
    public String toString() {
        return "ContactGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactList=" + contactList +
                ", comment='" + comment + '\'' +
                ", user=" + user +
                '}';
    }

}
