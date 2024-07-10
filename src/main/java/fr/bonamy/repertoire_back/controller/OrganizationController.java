package fr.bonamy.repertoire_back.controller;

import fr.bonamy.repertoire_back.dto.Organization.OrganizationDetailDTO;
import fr.bonamy.repertoire_back.dto.Organization.OrganizationFormDto;
import fr.bonamy.repertoire_back.dto.Organization.OrganizationMinimalDTO;
import fr.bonamy.repertoire_back.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/organizations")
@CrossOrigin("http://localhost:4200")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDetailDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.getById(id));
    }

    @GetMapping("/search/{userId}")
    public ResponseEntity<Page<OrganizationDetailDTO>> getByNameIgnoreCaseAndCommentIgnoreCase(
            @PathVariable Long userId,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                organizationService.getByNameAndComment(userId, keyword, sortBy, sortOrder, page, size));
    }

    @PostMapping
    public ResponseEntity<OrganizationMinimalDTO> create(
            @Valid @RequestBody OrganizationFormDto organizationFormDto) {
        return new ResponseEntity<>(organizationService.create(organizationFormDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationMinimalDTO> update(
            @PathVariable Long id, @Valid @RequestBody OrganizationFormDto organizationFormDto) {
        return new ResponseEntity<>(organizationService.update(id, organizationFormDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.delete(id));
    }

}
