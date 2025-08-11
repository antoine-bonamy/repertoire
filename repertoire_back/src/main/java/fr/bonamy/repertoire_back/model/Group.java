package fr.bonamy.repertoire_back.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "contact_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "link_contact_group_contact",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id"))
    private List<Contact> contacts;

    public Group() {
    }

    public Group(Long id, String name, List<Contact> contactList, String note, User user) {
        this.id = id;
        this.name = name;
        this.contacts = contactList;
        this.note = note;
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

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contactList) {
        this.contacts = contactList;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String comment) {
        this.note = comment;
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
        return Objects.equals(id, group.id)
                && Objects.equals(name, group.name)
                && Objects.equals(contacts, group.contacts)
                && Objects.equals(note, group.note)
                && Objects.equals(user, group.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contacts, note, user);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactList=" + contacts +
                ", comment='" + note + '\'' +
                ", user=" + user +
                '}';
    }

}
