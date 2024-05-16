package fr.bonamy.repertoire_back.mapper;

import fr.bonamy.repertoire_back.dto.front.ContactFrontDto;
import fr.bonamy.repertoire_back.model.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public ContactFrontDto toDto(Contact contact) {
        return new ContactFrontDto(
                contact.getId(),
                contact.getFirstname(),
                contact.getLastname(),
                contact.getEmail(),
                contact.getPhone(),
                contact.getPhone(),
                contact.getComment(),
                contact.getPublic(),
                new ContactFrontDto.ContactOrganizationFrontDto(
                        contact.getOrganization().getId(),
                        contact.getOrganization().getName()
                ),
                new ContactFrontDto.ContactUserFrontDto(
                        contact.getUser().getId(),
                        contact.getUser().getFirstname(),
                        contact.getUser().getLastname()
                )
        );
    }

}
