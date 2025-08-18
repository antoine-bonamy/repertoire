package fr.bonamy.repertoire_back.dto.Contact;

import fr.bonamy.repertoire_back.model.Contact;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContactDetailDto extends ContactFormDto {

    private Long id;

    public static ContactDetailDto of(Contact contact) {
        ContactDetailDto dto =(ContactDetailDto) ContactFormDto.of(contact);
        dto.setId(contact.getId());
        return dto;
    }

}
