package fr.bonamy.repertoire_back.controller;

import fr.bonamy.repertoire_back.dto.front.Organization.OrganizationDetailDTO;
import fr.bonamy.repertoire_back.dto.front.Organization.OrganizationFormDto;
import fr.bonamy.repertoire_back.dto.front.Organization.OrganizationMinimalDTO;
import fr.bonamy.repertoire_back.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity<List<OrganizationDetailDTO>> getAll() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDetailDTO> getOrganizationById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.getOrganizationById(id));
    }

    @GetMapping("/byUser")
    public ResponseEntity<Page<OrganizationDetailDTO>> getOrganizationByUserFirstNameAndUserLastName(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                organizationService.getOrganizationByUserFirstNameAndUserLastName(name, sortBy, sortOrder, page, size));
    }

    @GetMapping("/byNameComment")
    public ResponseEntity<Page<OrganizationDetailDTO>> getByNameIgnoreCaseAndCommentIgnoreCase(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                organizationService.findByNameIgnoreCaseAndCommentIgnoreCase(keyword, sortBy, sortOrder, page, size));
    }

    @GetMapping("/byIsPublic")
    public ResponseEntity<Page<OrganizationDetailDTO>> getByIsPublic(
            @RequestParam(name = "isPublic") Boolean isPublic,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                organizationService.findByIsPublic(isPublic, sortBy, sortOrder, page, size));
    }

    @PostMapping
    public ResponseEntity<OrganizationMinimalDTO> createOrganization(
            @Valid @RequestBody OrganizationFormDto organizationFormDto) {
        return new ResponseEntity<>(organizationService.createOrganization(organizationFormDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationMinimalDTO> updateUser(
            @PathVariable Long id, @Valid @RequestBody OrganizationFormDto organizationFormDto) {
        return new ResponseEntity<>(organizationService.updateUser(id, organizationFormDto), HttpStatus.CREATED);
    }

    @PutMapping("/isPublic/{id}")
    public ResponseEntity<OrganizationMinimalDTO> updateIsPublic(@PathVariable Long id, @RequestBody Boolean isPublic) {
        return new ResponseEntity<>(organizationService.updateIsPublic(id, isPublic), HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteOrganization(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.deleteOrganization(id));
    }

}
