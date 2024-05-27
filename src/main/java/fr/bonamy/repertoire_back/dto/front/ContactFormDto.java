package fr.bonamy.repertoire_back.dto.front;

public record ContactFormDto(
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
