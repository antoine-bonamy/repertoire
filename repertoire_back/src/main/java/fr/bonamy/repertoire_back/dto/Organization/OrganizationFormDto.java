package fr.bonamy.repertoire_back.dto.Organization;

import fr.bonamy.repertoire_back.dto.Contact.ContactDetailDto;
import fr.bonamy.repertoire_back.dto.User.UserDetailDto;
import fr.bonamy.repertoire_back.model.Organization;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class OrganizationFormDto {

    @NotNull(message = "Name cannot be null")
    @Length(min = 1, max = 64, message = "Name length must be between 1 and 64")
    private String name;

    @Length(max = 1000, message = "Comment length cannot be superior to 1000")
    private String note;

    @NotNull(message = "User cannot be null")
    private UserDetailDto user;

    private List<ContactDetailDto> contacts;

    public static OrganizationFormDto of(Organization organization) {
        OrganizationFormDto dto = new OrganizationFormDto();
        dto.setName(organization.getName());
        dto.setNote(organization.getNote());
        dto.setUser(UserDetailDto.of(organization.getUser()));
        dto.setContacts(organization.getContacts().stream().map(ContactDetailDto::of).toList());
        return dto;
    }

}
