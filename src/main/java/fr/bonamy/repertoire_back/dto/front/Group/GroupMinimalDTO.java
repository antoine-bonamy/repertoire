package fr.bonamy.repertoire_back.dto.front.Group;

import fr.bonamy.repertoire_back.dto.front.Contact.ContactDetailDTO;
import fr.bonamy.repertoire_back.dto.front.User.UserIdDTO;

import java.util.List;

public record GroupMinimalDTO(
        Long id,
        String name,
        String comment,
        Boolean isPublic,
        List<ContactDetailDTO> contacts,
        UserIdDTO user
) {
}
