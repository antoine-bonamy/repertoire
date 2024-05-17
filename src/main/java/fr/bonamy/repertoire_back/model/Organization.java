package fr.bonamy.repertoire_back.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "ORGANIZATION")
public class Organization {

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

    public Organization() {
    }

    public Organization(Long id, String name, String comment, Boolean isPublic, User user) {
        this.id = id;
        this.name = name;
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
        Organization that = (Organization) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(comment, that.comment)
                && Objects.equals(isPublic, that.isPublic)
                && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, comment, isPublic, user);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", isPublic=" + isPublic +
                ", user=" + user +
                '}';
    }

}
