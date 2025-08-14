package fr.bonamy.repertoire_back.dto.Group;

import fr.bonamy.repertoire_back.dto.Contact.ContactDetailDTO;
import fr.bonamy.repertoire_back.dto.User.UserDetailDto;

import java.util.List;

public record GroupDetailDTO(
        Long id,
        String name,
        String comment,
        List<ContactDetailDTO> contacts,
        UserDetailDto user
) {
}
