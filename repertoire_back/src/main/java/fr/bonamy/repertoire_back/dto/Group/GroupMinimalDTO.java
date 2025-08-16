package fr.bonamy.repertoire_back.dto.Group;

import fr.bonamy.repertoire_back.dto.Contact.ContactDetailDto;
import fr.bonamy.repertoire_back.dto.User.UserIdDTO;

import java.util.List;

public record GroupMinimalDTO(
        Long id,
        String name,
        String comment,
        List<ContactDetailDto> contacts,
        UserIdDTO user
) {
}
