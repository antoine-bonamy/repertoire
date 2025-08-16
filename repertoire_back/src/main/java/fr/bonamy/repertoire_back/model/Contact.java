package fr.bonamy.repertoire_back.model;


import fr.bonamy.repertoire_back.dto.Contact.ContactDetailDto;
import fr.bonamy.repertoire_back.dto.Contact.ContactFormDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "contact")
@Data
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "link_contact_group_contact",
            joinColumns = @JoinColumn(name = "contact_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<Group> groups;

    public static Contact of(ContactFormDto dto) {
        Contact contact =  new Contact();
        if (dto instanceof ContactDetailDto) {
            contact.setId(((ContactDetailDto) dto).getId());
        }
        contact.setFirstname(dto.getFirstname());
        contact.setLastname(dto.getLastname());
        contact.setEmail(dto.getEmail());
        contact.setPhone(dto.getPhone());
        contact.setAddress(dto.getAddress());
        contact.setNote(dto.getNote());
        contact.setOrganization(Organization.of(dto.getOrganization()));
        contact.setUser(User.of(dto.getUser()));
        contact.setGroups(dto.getGroups());
        return contact;
    }

}