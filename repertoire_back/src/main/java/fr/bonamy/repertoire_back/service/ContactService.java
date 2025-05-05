package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.Contact.ContactDetailDTO;
import fr.bonamy.repertoire_back.dto.Contact.ContactFormDTO;
import fr.bonamy.repertoire_back.dto.Contact.ContactMinimalDTO;
import fr.bonamy.repertoire_back.exception.ResourceNotFoundException;
import fr.bonamy.repertoire_back.mapper.ContactMapper;
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
    private final ContactMapper contactMapper;

    @Autowired
    public ContactService(ContactRepository contactRepository,
                          UserService userService,
                          OrganizationService organizationService,
                          ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.userService = userService;
        this.organizationService = organizationService;
        this.contactMapper = contactMapper;
    }

    public void exist(Long id) {
        contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CONTACT_NOT_FOUND, id)));
    }

    public ContactDetailDTO getById(Long id) {
        return contactRepository.findById(id).map(x -> contactMapper.toDto(x, ContactDetailDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CONTACT_NOT_FOUND, id)));
    }

    public Page<ContactDetailDTO> search(Long userId, String keyword, String sortBy, String sortOrder, int page,
                                         int size) {
        return contactRepository.search(userId, keyword, initPageable(sortBy, sortOrder, page, size))
                .map(x -> contactMapper.toDto(x, ContactDetailDTO.class));
    }

    public Page<ContactDetailDTO> getByOrganization(
            Long userId, Long organizationId, String sortBy, String sortOrder, int page, int size) {
        return contactRepository.findByOrganizationId(userId, organizationId, initPageable(sortBy, sortOrder, page,
                        size))
                .map(x -> contactMapper.toDto(x, ContactDetailDTO.class));
    }

    public ContactMinimalDTO create(ContactFormDTO dto) {
        userService.exist(dto.user().id());
        return contactMapper.toDto(contactRepository.save(contactMapper.toEntity(dto)), ContactMinimalDTO.class);
    }

    public ContactMinimalDTO update(Long id, ContactFormDTO dto) {
        exist(id);
        userService.exist(id);
        organizationService.exist(dto.organization().id());
        Contact newContact = contactMapper.toEntity(dto);
        newContact.setId(id);
        return contactMapper.toDto(contactRepository.save(newContact), ContactMinimalDTO.class);
    }

    public Boolean deleteContact(Long id) {
        exist(id);
        contactRepository.deleteById(id);
        return true;
    }

}
