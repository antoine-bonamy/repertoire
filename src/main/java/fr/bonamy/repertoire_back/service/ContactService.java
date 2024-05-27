package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.front.ContactFormDto;
import fr.bonamy.repertoire_back.dto.front.ContactFrontDto;
import fr.bonamy.repertoire_back.exception.ResourceNotFoundException;
import fr.bonamy.repertoire_back.mapper.ContactMapper;
import fr.bonamy.repertoire_back.model.Contact;
import fr.bonamy.repertoire_back.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    private Pageable initPageable(String sortBy, String sortOrder, int page, int size) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    public void exist(Long id) {
        contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CONTACT_NOT_FOUND, id)));
    }

    public List<ContactFrontDto> getAllContacts() {
        return contactRepository.findAll().stream().map(x -> contactMapper.toDto(x, ContactFrontDto.class))
                .collect(Collectors.toList());
    }

    public ContactFrontDto getContactById(Long id) {
        return contactRepository.findById(id).map(x -> contactMapper.toDto(x, ContactFrontDto.class))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CONTACT_NOT_FOUND, id)));
    }

    public Page<ContactFrontDto> search(String keyword, String sortBy, String sortOrder, int page, int size) {
        return contactRepository.search(keyword, initPageable(sortBy, sortOrder, page, size))
                .map(x -> contactMapper.toDto(x, ContactFrontDto.class));
    }

    public Page<ContactFrontDto> getContactByUser(Long id, String sortBy, String sortOrder, int page, int size) {
        return contactRepository.findByUserId(id, initPageable(sortBy, sortOrder, page, size))
                .map(x -> contactMapper.toDto(x, ContactFrontDto.class));
    }

    public Page<ContactFrontDto> getContactByOrganization(
            Long id, String sortBy, String sortOrder, int page, int size) {
        return contactRepository.findByOrganizationId(id, initPageable(sortBy, sortOrder, page, size))
                .map(x -> contactMapper.toDto(x, ContactFrontDto.class));
    }

    public Page<ContactFrontDto> getContactByPublic(
            Boolean isPublic, String sortBy, String sortOrder, int page, int size) {
        return contactRepository.findByIsPublic(isPublic, initPageable(sortBy, sortOrder, page, size))
                .map(x -> contactMapper.toDto(x, ContactFrontDto.class));
    }

    public ContactFrontDto createContact(ContactFormDto dto) {
        userService.exist(dto.user().id());
        return contactMapper.toDto(contactRepository.save(contactMapper.toEntity(dto)), ContactFrontDto.class);
    }

    public ContactFrontDto updateContact(Long id, ContactFormDto dto) {
        exist(id);
        userService.exist(id);
        organizationService.exist(dto.organization().id());
        Contact newContact = contactMapper.toEntity(dto);
        newContact.setId(id);
        return contactMapper.toDto(contactRepository.save(newContact), ContactFrontDto.class);
    }

    public ContactFrontDto updateIsPublic(Long id, Boolean isPublic) {
        Contact contact = contactRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(CONTACT_NOT_FOUND, id)));
        contactRepository.updateIsPublic(id, isPublic);
        contact.setPublic(isPublic);
        return contactMapper.toDto(contact, ContactFrontDto.class);
    }


    public Boolean deleteContact(Long id) {
        exist(id);
        contactRepository.deleteById(id);
        return true;
    }


}
