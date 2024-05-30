package fr.bonamy.repertoire_back.dto.Contact;

import fr.bonamy.repertoire_back.dto.Organization.OrganizationDetailDTO;
import fr.bonamy.repertoire_back.dto.User.UserDetailDTO;

public record ContactDetailDTO(
        Long id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String address,
        String comment,
        OrganizationDetailDTO organization,
        UserDetailDTO user

) {
}
