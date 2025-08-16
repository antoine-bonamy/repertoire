package fr.bonamy.repertoire_back.dto.Contact;

import fr.bonamy.repertoire_back.dto.Group.GroupDetailDTO;
import fr.bonamy.repertoire_back.dto.Organization.OrganizationDetailDto;
import fr.bonamy.repertoire_back.dto.User.UserDetailDto;
import fr.bonamy.repertoire_back.model.Contact;
import fr.bonamy.repertoire_back.model.Group;
import lombok.Data;

import java.util.List;

@Data
public class ContactFormDto {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private String note;
    private OrganizationDetailDto organization;
    private UserDetailDto user;
    private List<GroupDetailDTO> groups;

    public static ContactFormDto of(Contact contact) {
        ContactFormDto dto = new ContactFormDto();
        dto.setFirstname(contact.getFirstname());
        dto.setLastname(contact.getLastname());
        dto.setEmail(contact.getEmail());
        dto.setPhone(contact.getPhone());
        dto.setAddress(contact.getAddress());
        dto.setOrganization(OrganizationDetailDto.of(contact.getOrganization()));
        dto.setUser(UserDetailDto.of(contact.getUser()));
        dto.setGroups(contact.getGroups());
        return dto;
    }
}
