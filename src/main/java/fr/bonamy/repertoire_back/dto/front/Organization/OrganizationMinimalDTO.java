package fr.bonamy.repertoire_back.dto.front.Organization;

import fr.bonamy.repertoire_back.dto.front.User.UserIdDTO;

public record OrganizationMinimalDTO(
        Long id,
        String name,
        String comment,
        Boolean isPublic,
        UserIdDTO user
) {
}
