package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.Organization.OrganizationDetailDto;
import fr.bonamy.repertoire_back.dto.Organization.OrganizationFormDto;
import fr.bonamy.repertoire_back.exception.ResourceNotFoundException;
import fr.bonamy.repertoire_back.model.Organization;
import fr.bonamy.repertoire_back.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static fr.bonamy.repertoire_back.util.InitPageable.initPageable;


@Service
public class OrganizationService {

    private static final String ORGANISATION_NOT_FOUND = "Organization with id:%s not found.";

    private final OrganizationRepository organizationRepository;
    private final UserService userService;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, UserService userService) {
        this.organizationRepository = organizationRepository;
        this.userService = userService;
    }

    public void exist(Long id) {
        organizationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
    }

    public OrganizationDetailDto getById(Long id) {
        return organizationRepository.findById(id).map(OrganizationDetailDto::of)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
    }

    public Page<OrganizationDetailDto> getByNameAndComment(
            Long userId, String keyword, String sortBy, String sortOrder, int page, int size) {
        return organizationRepository.search(
                        userId, keyword, initPageable(sortBy, sortOrder, page, size))
                .map(OrganizationDetailDto::of);
    }

    public OrganizationDetailDto create(OrganizationFormDto dto) {
        Organization organization = Organization.of(dto);
        userService.exist(dto.getUser().getId());
        //TODO: Verifier l'existence de l'organisation
        return OrganizationDetailDto.of(organizationRepository.save(organization));
    }

    public OrganizationDetailDto update(Long id, OrganizationFormDto dto) {
        exist(id);
        userService.exist(dto.getUser().getId());
        Organization newOrganization = Organization.of(dto);
        newOrganization.setId(id);
        return OrganizationDetailDto.of(organizationRepository.save(newOrganization));
    }

    public boolean delete(Long id) {
        organizationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
        organizationRepository.deleteById(id);
        return true;
    }


}
