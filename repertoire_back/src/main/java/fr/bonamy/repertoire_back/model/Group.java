package fr.bonamy.repertoire_back.model;

import fr.bonamy.repertoire_back.dto.Group.GroupDetailDto;
import fr.bonamy.repertoire_back.dto.Group.GroupFormDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "contact_group")
@Data
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

    public static Group of(GroupFormDto dto) {
        Group group = new Group();
        if (dto instanceof GroupDetailDto) {
            group.setId(((GroupDetailDto) dto).getId());
        }
        group.setName(dto.getName());
        group.setNote(dto.getNote());
        group.setUser(User.of(dto.getUser()));
        return group;
    }

}
