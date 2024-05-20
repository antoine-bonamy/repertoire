package fr.bonamy.repertoire_back.dto.front;

public record ContactFrontDto(
        Long id,
        String firstname,
        String lastname,
        String email,
        String phone,
        String address,
        String comment,
        Boolean isPublic,
        OrganizationFrontDto organization,
        UserFrontDto user

) {
}
