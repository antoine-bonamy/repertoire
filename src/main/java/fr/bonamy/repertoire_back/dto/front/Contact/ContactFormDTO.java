package fr.bonamy.repertoire_back.dto.front.Contact;

import fr.bonamy.repertoire_back.dto.front.Organization.OrganizationIdDTO;
import fr.bonamy.repertoire_back.dto.front.User.UserIdDTO;

public record ContactFormDTO(
        String firstname,
        String lastname,
        String email,
        String phone,
        String address,
        String comment,
        Boolean isPublic,
        OrganizationIdDTO organization,
        UserIdDTO user
) {
}
