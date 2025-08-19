package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.ContactDto;
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

    public ContactDto getById(Long id) {
        return contactRepository.findById(id).map(ContactDto::of)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CONTACT_NOT_FOUND, id)));
    }

    public Page<ContactDto> search(Long userId, String keyword, String sortBy, String sortOrder, int page, int size) {
        return contactRepository.search(userId, keyword, initPageable(sortBy, sortOrder, page, size))
                .map(ContactDto::of);
    }

    public Page<ContactDto> getByOrganization(
            Long userId, Long organizationId, String sortBy, String sortOrder, int page, int size) {
        return contactRepository.findByOrganizationId(userId, organizationId, initPageable(sortBy, sortOrder, page, size))
                .map(ContactDto::of);
    }

    public ContactDto create(ContactDto dto) {
        userService.exist(dto.getUser().getId());
        return ContactDto.of(contactRepository.save(Contact.of(dto)));
    }

    public ContactDto update(Long id, ContactDto dto) {
        exist(id);
        userService.exist(id);
        organizationService.exist(dto.getOrganization().getId());
        Contact newContact = Contact.of(dto);
        newContact.setId(id);
        return ContactDto.of(contactRepository.save(newContact));
    }

    public Boolean deleteContact(Long id) {
        exist(id);
        contactRepository.deleteById(id);
        return true;
    }

}
