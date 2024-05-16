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
        ContactOrganizationFrontDto organizationFrontDto,
        ContactUserFrontDto userFrontDto

) {

    public record ContactOrganizationFrontDto(
            Long id,
            String name
    ) {
    }

    public record ContactUserFrontDto(
            Long id,
            String firstname,
            String lastname
    ) {
    }

}
