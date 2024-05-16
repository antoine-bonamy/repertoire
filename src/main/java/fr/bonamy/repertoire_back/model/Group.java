package fr.bonamy.repertoire_back.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "`GROUP`")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "comment")
    private String comment;

    @Column(name = "is_public")
    private Boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "LINK_CONTACT_GROUP",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id"))
    private List<Contact> contactList;


    public Group() {
    }

    public Group(Long id, String name, List<Contact> contactList, String comment, Boolean isPublic, User user) {
        this.id = id;
        this.name = name;
        this.contactList = contactList;
        this.comment = comment;
        this.isPublic = isPublic;
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

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
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
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(name, group.name) && Objects.equals(contactList,
                group.contactList) && Objects.equals(comment, group.comment) && Objects.equals(isPublic,
                group.isPublic) && Objects.equals(user, group.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contactList, comment, isPublic, user);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactList=" + contactList +
                ", comment='" + comment + '\'' +
                ", isPublic=" + isPublic +
                ", user=" + user +
                '}';
    }

}
