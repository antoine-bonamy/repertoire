package fr.bonamy.repertoire_back.dto.User;

import fr.bonamy.repertoire_back.util.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserFormDTO(
        @NotNull(message = "Firstname cannot be null.")
        @NotEmpty(message = "Firstname cannot be empty.")
        @Length(max = 64, message = "Firstname length cannot be superior to 64.")
        String firstname,

        @NotNull(message = "Lastname cannot be null.")
        @NotEmpty(message = "Lastname cannot be empty.")
        @Length(max = 64, message = "Lastname length cannot be superior to 64.")
        String lastname,

        @Email(message = "Email must be valid.")
        @NotEmpty(message = "Email cannot be empty")
        String email,

        @NotNull(message = "Password cannot be null")
        @ValidPassword(message = "Invalid password.")
        String password
) {

}
