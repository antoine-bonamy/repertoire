package fr.bonamy.repertoire_back.dto.Organization;

import fr.bonamy.repertoire_back.dto.User.UserIdDTO;

public record OrganizationMinimalDTO(
        Long id,
        String name,
        String comment,
        UserIdDTO user
) {
}
