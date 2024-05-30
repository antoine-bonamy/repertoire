package fr.bonamy.repertoire_back.dto.Organization;

import fr.bonamy.repertoire_back.dto.User.UserDetailDTO;

public record OrganizationDetailDTO(
        Long id,
        String name,
        String comment,
        UserDetailDTO user) {
}
