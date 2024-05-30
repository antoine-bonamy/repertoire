package fr.bonamy.repertoire_back.dto.Contact;

import fr.bonamy.repertoire_back.dto.Organization.OrganizationIdDTO;
import fr.bonamy.repertoire_back.dto.User.UserIdDTO;

public record ContactMinimalDTO(
        Long id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String address,
        String comment,
        OrganizationIdDTO organization,
        UserIdDTO user
) {
}
