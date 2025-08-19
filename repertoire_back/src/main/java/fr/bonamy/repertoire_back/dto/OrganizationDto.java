package fr.bonamy.repertoire_back.dto;

import fr.bonamy.repertoire_back.model.Organization;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class OrganizationDto {

    private Long id;

    @NotNull(message = "Name cannot be null")
    @Length(min = 1, max = 64, message = "Name length must be between 1 and 64")
    private String name;

    @Length(max = 1000, message = "Comment length cannot be superior to 1000")
    private String note;

    @NotNull(message = "User cannot be null")
    private UserDto user;

    private List<ContactDto> contacts;

    public static OrganizationDto of(Organization organization) {
        OrganizationDto dto = new OrganizationDto();
        dto.setId(organization.getId());
        dto.setName(organization.getName());
        dto.setNote(organization.getNote());
        dto.setUser(UserDto.of(organization.getUser()));
        dto.setContacts(organization.getContacts().stream().map(ContactDto::of).toList());
        return dto;
    }

}
