package fr.bonamy.repertoire_back.dto.Group;

import fr.bonamy.repertoire_back.dto.User.UserIdDTO;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record GroupFormDTO(
        @NotNull(message = "Name cannot be null")
        @Length(min = 1, max = 64, message = "Name length must be between 1 and 64")
        String name,

        @Length(max = 1000, message = "Comment length cannot be superior to 1000")
        String comment,

        @NotNull(message = "UserId cannot be null")
        UserIdDTO user
) {
}
