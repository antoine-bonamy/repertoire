package fr.bonamy.repertoire_back.controller;

import fr.bonamy.repertoire_back.dto.front.ContactFormDto;
import fr.bonamy.repertoire_back.dto.front.ContactFrontDto;
import fr.bonamy.repertoire_back.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContactFrontDto>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactFrontDto> getContactById(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getContactById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ContactFrontDto>> search(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(contactService.search(keyword, sortBy, sortOrder, page, size));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ContactFrontDto>> getContactByUser(
            @PathVariable Long userId,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(contactService.getContactByUser(userId, sortBy, sortOrder, page, size));
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<Page<ContactFrontDto>> getContactByOrganization(
            @PathVariable Long organizationId,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(contactService.getContactByOrganization(organizationId, sortBy, sortOrder, page,
                size));
    }

    @GetMapping("isPublic")
    public ResponseEntity<Page<ContactFrontDto>> getContactByIsPublic(
            @RequestParam(name = "isPublic") Boolean isPublic,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                contactService.getContactByPublic(isPublic, sortBy, sortOrder, page, size));
    }

    @PostMapping
    public ResponseEntity<ContactFrontDto> createContact(@RequestBody ContactFormDto contact) {
        return new ResponseEntity<>(contactService.createContact(contact), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactFrontDto> updateContact(@PathVariable Long id, @RequestBody ContactFormDto contact) {
        return new ResponseEntity<>(contactService.updateContact(id, contact), HttpStatus.CREATED);
    }

    @PutMapping("/isPublic/{id}")
    public ResponseEntity<ContactFrontDto> updateIsPublic(@PathVariable Long id, @RequestBody Boolean isPublic) {
        return new ResponseEntity<>(contactService.updateIsPublic(id, isPublic), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteContact(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.deleteContact(id));
    }


}
