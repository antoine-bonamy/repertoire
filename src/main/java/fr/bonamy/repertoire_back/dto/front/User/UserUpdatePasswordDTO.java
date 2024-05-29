package fr.bonamy.repertoire_back.dto.front.User;

import fr.bonamy.repertoire_back.util.ValidPassword;
import jakarta.validation.constraints.NotNull;

public record UserUpdatePasswordDTO(
        @NotNull(message = "Password cannot be null")
        @ValidPassword(message = "Invalid password.")
        String password
) {
}
