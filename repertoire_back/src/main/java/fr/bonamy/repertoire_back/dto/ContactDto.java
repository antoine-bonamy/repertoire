package fr.bonamy.repertoire_back.dto;

import fr.bonamy.repertoire_back.model.Contact;
import lombok.Data;

import java.util.List;

@Data
public class ContactDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private String note;
    private OrganizationDto organization;
    private UserDto user;
    private List<GroupDto> groups;

    public static ContactDto of(Contact contact) {
        ContactDto dto = new ContactDto();
        dto.setId(contact.getId());
        dto.setFirstname(contact.getFirstname());
        dto.setLastname(contact.getLastname());
        dto.setEmail(contact.getEmail());
        dto.setPhone(contact.getPhone());
        dto.setAddress(contact.getAddress());
        dto.setOrganization(OrganizationDto.of(contact.getOrganization()));
        dto.setUser(UserDto.of(contact.getUser()));
        dto.setGroups(contact.getGroups().stream().map(GroupDto::of).toList());
        return dto;
    }
}
