package fr.bonamy.repertoire_back.model;

import fr.bonamy.repertoire_back.dto.Organization.OrganizationDetailDto;
import fr.bonamy.repertoire_back.dto.Organization.OrganizationFormDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "organization")
@Data
public class Organization {

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

    @OneToMany(mappedBy = "organization")
    private List<Contact> contacts;

    public static Organization of(OrganizationFormDto dto) {
        Organization organization = new Organization();
        if (dto instanceof OrganizationDetailDto) {
            organization.setId(((OrganizationDetailDto) dto).getId());
        }
        organization.setName(dto.getName());
        organization.setNote(dto.getNote());
        organization.setUser(User.of(dto.getUser()));
        organization.setContacts(List.of());// TODO: Liste des contacts
        return organization;
    }

}
