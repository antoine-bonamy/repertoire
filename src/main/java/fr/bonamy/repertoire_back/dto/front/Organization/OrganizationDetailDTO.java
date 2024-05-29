package fr.bonamy.repertoire_back.dto.front.Organization;

import fr.bonamy.repertoire_back.dto.front.User.UserDetailDTO;

public record OrganizationDetailDTO(
        Long id,
        String name,
        String comment,
        Boolean isPublic,
        UserDetailDTO user) {
}
