package fr.bonamy.repertoire_back.dto.Organization;

import fr.bonamy.repertoire_back.model.Organization;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizationDetailDto extends OrganizationFormDto {

    private Long id;

    public static OrganizationDetailDto of(Organization organization) {
        OrganizationDetailDto dto = (OrganizationDetailDto) OrganizationFormDto.of(organization);
        dto.setId(organization.getId());
        return dto;
    }

}
