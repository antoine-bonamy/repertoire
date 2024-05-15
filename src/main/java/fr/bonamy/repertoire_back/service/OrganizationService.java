package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.front.OrganizationFormDto;
import fr.bonamy.repertoire_back.dto.front.OrganizationFrontDto;
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

    public List<OrganizationFrontDto> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(organizationMapper::toDto)
                .collect(Collectors.toList());
    }

    public OrganizationFrontDto getOrganizationById(Long id) {
        return organizationRepository.findById(id).map(organizationMapper::toDto).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
    }

    public Page<OrganizationFrontDto> getOrganizationByUserFirstNameAndUserLastName(
            String name, String sortBy, String sortOrder, int page, int size) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return organizationRepository.findByUserFirstNameAndUserLastName(name, pageable).map(organizationMapper::toDto);
    }

    public Page<OrganizationFrontDto> findByNameIgnoreCaseAndCommentIgnoreCase(
            String keyword, String sortBy, String sortOrder, int page, int size) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return organizationRepository.
                findByNameContainingIgnoreCaseOrCommentContainingIgnoreCase(keyword, keyword, pageable)
                .map(organizationMapper::toDto);
    }

    public Page<OrganizationFrontDto> findByIsPublic(
            Boolean isPublic, String sortBy, String sortOrder, int page, int size) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return organizationRepository.findByIsPublic(isPublic, pageable)
                .map(organizationMapper::toDto);
    }

    public OrganizationFrontDto createOrganization(OrganizationFormDto dto) {
        userService.getUserById(dto.userId());
        Organization organization = organizationMapper.toEntity(dto);
        //TODO: Verifier l'existence de l'organisation
        return organizationMapper.toDto(organizationRepository.save(organization));
    }

    public OrganizationFrontDto updateUser(Long id, OrganizationFormDto dto) {
        organizationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
        userService.getUserById(dto.userId());
        Organization newOrganization = organizationMapper.toEntity(dto);
        newOrganization.setId(id);
        return organizationMapper.toDto(organizationRepository.save(newOrganization));
    }

    public OrganizationFrontDto updateIsPublic(Long id, Boolean isPublic) {
        Organization organization = organizationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
        organizationRepository.updateIsPublic(id, isPublic);
        organization.setPublic(isPublic);
        return organizationMapper.toDto(organization);
    }

    public boolean deleteOrganization(Long id) {
        organizationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ORGANISATION_NOT_FOUND, id)));
        organizationRepository.deleteById(id);
        return true;
    }


}
