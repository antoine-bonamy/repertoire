package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.Contact.ContactDetailDto;
import fr.bonamy.repertoire_back.dto.Contact.ContactFormDto;
import fr.bonamy.repertoire_back.exception.ResourceNotFoundException;
import fr.bonamy.repertoire_back.model.Contact;
import fr.bonamy.repertoire_back.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static fr.bonamy.repertoire_back.util.InitPageable.initPageable;

@Service
public class ContactService {

    private static final String CONTACT_NOT_FOUND = "Contact with id:%s not found.";

    private final ContactRepository contactRepository;
    private final UserService userService;
    private final OrganizationService organizationService;

    @Autowired
    public ContactService(ContactRepository contactRepository, UserService userService, OrganizationService organizationService) {
        this.contactRepository = contactRepository;
        this.userService = userService;
        this.organizationService = organizationService;
    }

    public void exist(Long id) {
        contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CONTACT_NOT_FOUND, id)));
    }

    public ContactDetailDto getById(Long id) {
        return contactRepository.findById(id).map(ContactDetailDto::of)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CONTACT_NOT_FOUND, id)));
    }

    public Page<ContactDetailDto> search(Long userId, String keyword, String sortBy, String sortOrder, int page, int size) {
        return contactRepository.search(userId, keyword, initPageable(sortBy, sortOrder, page, size))
                .map(ContactDetailDto::of);
    }

    public Page<ContactDetailDto> getByOrganization(
            Long userId, Long organizationId, String sortBy, String sortOrder, int page, int size) {
        return contactRepository.findByOrganizationId(userId, organizationId, initPageable(sortBy, sortOrder, page, size))
                .map(ContactDetailDto::of);
    }

    public ContactDetailDto create(ContactFormDto dto) {
        userService.exist(dto.getUser().getId());
        return ContactDetailDto.of(contactRepository.save(Contact.of(dto)));
    }

    public ContactDetailDto update(Long id, ContactFormDto dto) {
        exist(id);
        userService.exist(id);
        organizationService.exist(dto.getOrganization().getId());
        Contact newContact = Contact.of(dto);
        newContact.setId(id);
        return ContactDetailDto.of(contactRepository.save(newContact));
    }

    public Boolean deleteContact(Long id) {
        exist(id);
        contactRepository.deleteById(id);
        return true;
    }

}
