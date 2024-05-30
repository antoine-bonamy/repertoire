package fr.bonamy.repertoire_back.controller;

import fr.bonamy.repertoire_back.dto.Contact.ContactDetailDTO;
import fr.bonamy.repertoire_back.dto.Contact.ContactFormDTO;
import fr.bonamy.repertoire_back.dto.Contact.ContactMinimalDTO;
import fr.bonamy.repertoire_back.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDetailDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getById(id));
    }

    @GetMapping("/search/{userId}")
    public ResponseEntity<Page<ContactDetailDTO>> search(
            @PathVariable Long userId,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(contactService.search(userId, keyword, sortBy, sortOrder, page, size));
    }

    @GetMapping("/organization/{organizationId}/{userId}")
    public ResponseEntity<Page<ContactDetailDTO>> getByOrganization(
            @PathVariable Long organizationId,
            @PathVariable Long userId,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                contactService.getByOrganization(userId, organizationId, sortBy, sortOrder, page, size));
    }

    @PostMapping
    public ResponseEntity<ContactMinimalDTO> create(@RequestBody ContactFormDTO contact) {
        return new ResponseEntity<>(contactService.create(contact), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactMinimalDTO> update(@PathVariable Long id, @RequestBody ContactFormDTO contact) {
        return new ResponseEntity<>(contactService.update(id, contact), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.deleteContact(id));
    }

}
