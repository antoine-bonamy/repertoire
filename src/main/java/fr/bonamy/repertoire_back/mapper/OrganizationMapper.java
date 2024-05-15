package fr.bonamy.repertoire_back.mapper;

import fr.bonamy.repertoire_back.dto.front.OrganizationFormDto;
import fr.bonamy.repertoire_back.dto.front.OrganizationFrontDto;
import fr.bonamy.repertoire_back.model.Organization;
import fr.bonamy.repertoire_back.model.User;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {

    public OrganizationFrontDto toDto(Organization organization) {
        return new OrganizationFrontDto(organization.getId(),
                organization.getName(),
                organization.getComment(),
                organization.getPublic(),
                new OrganizationFrontDto.UserOrganizationDto(
                        organization.getUser().getId(),
                        organization.getUser().getFirstname(),
                        organization.getUser().getLastname()
                )
        );
    }

    public Organization toEntity(OrganizationFormDto dto) {
        Organization organization = new Organization();
        organization.setComment(dto.comment());
        organization.setName(dto.name());
        organization.setPublic(dto.isPublic());
        User user = new User();
        user.setId(dto.userId());
        organization.setUser(user);
        return organization;
    }


}