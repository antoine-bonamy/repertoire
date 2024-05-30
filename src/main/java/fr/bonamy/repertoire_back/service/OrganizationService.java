package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.Organization.OrganizationDetailDTO;
import fr.bonamy.repertoire_back.dto.Organization.OrganizationFormDto;
import fr.bonamy.repertoire_back.dto.Organization.OrganizationMinimalDTO;
import fr.bonamy.repertoire_back.exception.ResourceNotFoundException;
import fr.bonamy.repertoire_back.mapper.OrganizationMapper;
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
    private final OrganizationMapper organizationMapper;
    private final UserService userService;

    @Autowired
    public OrganizationService(
            OrganizationRepository organizationRepository,
            OrganizationMapper organizationMapper,
            UserService userService) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
        this.userService = userService;
    }

    public void exist(Long id) {
        organizationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
    }

    public OrganizationDetailDTO getById(Long id) {
        return organizationRepository.findById(id).map(x -> organizationMapper.toDto(x, OrganizationDetailDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
    }

    public Page<OrganizationDetailDTO> getByNameAndComment(
            Long userId, String keyword, String sortBy, String sortOrder, int page, int size) {
        return organizationRepository.search(
                        userId, keyword, initPageable(sortBy, sortOrder, page, size))
                .map(x -> organizationMapper.toDto(x, OrganizationDetailDTO.class));
    }

    public OrganizationMinimalDTO create(OrganizationFormDto dto) {
        Organization organization = organizationMapper.toEntity(dto);
        userService.exist(dto.user().id());
        //TODO: Verifier l'existence de l'organisation
        return organizationMapper.toDto(organizationRepository.save(organization), OrganizationMinimalDTO.class);
    }

    public OrganizationMinimalDTO update(Long id, OrganizationFormDto dto) {
        exist(id);
        userService.exist(dto.user().id());
        Organization newOrganization = organizationMapper.toEntity(dto);
        newOrganization.setId(id);
        return organizationMapper.toDto(organizationRepository.save(newOrganization), OrganizationMinimalDTO.class);
    }

    public boolean delete(Long id) {
        organizationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
        organizationRepository.deleteById(id);
        return true;
    }


}
