package fr.bonamy.repertoire_back.dto.Contact;

import fr.bonamy.repertoire_back.dto.Organization.OrganizationDetailDto;
import fr.bonamy.repertoire_back.dto.User.UserDetailDto;

public record ContactDetailDTO(
        Long id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String address,
        String comment,
        OrganizationDetailDto organization,
        UserDetailDto user

) {
}
