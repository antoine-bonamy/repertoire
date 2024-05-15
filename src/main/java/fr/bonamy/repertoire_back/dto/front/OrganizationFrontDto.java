package fr.bonamy.repertoire_back.dto.front;

public record OrganizationFrontDto(
        Long id,
        String name,
        String comment,
        Boolean isPublic,
        UserOrganizationDto user) {
    public record UserOrganizationDto(
            Long id,
            String firstname,
            String lastname) {
    }
}
