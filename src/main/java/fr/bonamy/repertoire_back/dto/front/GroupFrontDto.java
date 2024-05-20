package fr.bonamy.repertoire_back.dto.front;

import java.util.List;

public record GroupFrontDto(
        Long id,
        String name,
        String comment,
        Boolean isPublic,
        List<ContactFrontDto> contacts,
        UserFrontDto user
) {
}
