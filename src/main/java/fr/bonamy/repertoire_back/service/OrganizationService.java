package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.front.Organization.OrganizationDetailDTO;
import fr.bonamy.repertoire_back.dto.front.Organization.OrganizationFormDto;
import fr.bonamy.repertoire_back.dto.front.Organization.OrganizationMinimalDTO;
import fr.bonamy.repertoire_back.exception.ResourceNotFoundException;
import fr.bonamy.repertoire_back.mapper.OrganizationMapper;
import fr.bonamy.repertoire_back.model.Organization;
import fr.bonamy.repertoire_back.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrganizationService {

    //TODO: la fonction de recherche ne doit renvoyer que les résultat dont "isPublic" vaut TRUE
    //TODO: ajouter une fonction de recherche en tenant compte de userID
    //TODO: ajouter une fonction pour recupérer toute les organization liée à un utilisateur

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

    private Pageable initPageable(String sortBy, String sortOrder, int page, int size) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    public void exist(Long id) {
        organizationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
    }

    public List<OrganizationDetailDTO> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(x -> organizationMapper.toDto(x, OrganizationDetailDTO.class))
                .collect(Collectors.toList());
    }

    public OrganizationDetailDTO getOrganizationById(Long id) {
        return organizationRepository.findById(id).map(x -> organizationMapper.toDto(x, OrganizationDetailDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
    }

    public Page<OrganizationDetailDTO> getOrganizationByUserFirstNameAndUserLastName(
            String name, String sortBy, String sortOrder, int page, int size) {
        return organizationRepository.findByUserFirstNameAndUserLastName(
                        name, initPageable(sortBy, sortOrder, page, size))
                .map(x -> organizationMapper.toDto(x, OrganizationDetailDTO.class));
    }

    public Page<OrganizationDetailDTO> findByNameIgnoreCaseAndCommentIgnoreCase(
            String keyword, String sortBy, String sortOrder, int page, int size) {
        return organizationRepository.findByNameContainingIgnoreCaseOrCommentContainingIgnoreCase(
                        keyword, keyword, initPageable(sortBy, sortOrder, page, size))
                .map(x -> organizationMapper.toDto(x, OrganizationDetailDTO.class));
    }

    public Page<OrganizationDetailDTO> findByIsPublic(
            Boolean isPublic, String sortBy, String sortOrder, int page, int size) {
        return organizationRepository.findByIsPublic(isPublic, initPageable(sortBy, sortOrder, page, size))
                .map(x -> organizationMapper.toDto(x, OrganizationDetailDTO.class));
    }

    public OrganizationMinimalDTO createOrganization(OrganizationFormDto dto) {
        Organization organization = organizationMapper.toEntity(dto);
        userService.exist(dto.user().id());
        //TODO: Verifier l'existence de l'organisation
        return organizationMapper.toDto(organizationRepository.save(organization), OrganizationMinimalDTO.class);
    }

    public OrganizationMinimalDTO updateUser(Long id, OrganizationFormDto dto) {
        exist(id);
        userService.exist(dto.user().id());
        Organization newOrganization = organizationMapper.toEntity(dto);
        newOrganization.setId(id);
        return organizationMapper.toDto(organizationRepository.save(newOrganization), OrganizationMinimalDTO.class);
    }

    public OrganizationMinimalDTO updateIsPublic(Long id, Boolean isPublic) {
        Organization organization = organizationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
        organizationRepository.updateIsPublic(id, isPublic);
        organization.setPublic(isPublic);
        return organizationMapper.toDto(organization, OrganizationMinimalDTO.class);
    }

    public boolean deleteOrganization(Long id) {
        organizationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
        organizationRepository.deleteById(id);
        return true;
    }


}
