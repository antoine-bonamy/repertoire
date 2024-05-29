package fr.bonamy.repertoire_back.dto.front.Contact;

import fr.bonamy.repertoire_back.dto.front.Organization.OrganizationDetailDTO;
import fr.bonamy.repertoire_back.dto.front.User.UserDetailDTO;

public record ContactDetailDTO(
        Long id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String address,
        String comment,
        Boolean isPublic,
        OrganizationDetailDTO organization,
        UserDetailDTO user

) {
}
