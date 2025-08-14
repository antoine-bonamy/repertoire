package fr.bonamy.repertoire_back.dto.User;

import fr.bonamy.repertoire_back.model.User;
import fr.bonamy.repertoire_back.util.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserFormDto {

    @NotNull(message = "Firstname cannot be null.")
    @Length(max = 64, message = "Firstname length cannot be superior to 64.")
    private String firstname;

    @NotNull(message = "Lastname cannot be null.")
    @Length(max = 64, message = "Lastname length cannot be superior to 64.")
    private String lastname;

    @Email(message = "Email must be valid.")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Password cannot be null")
    @ValidPassword(message = "Invalid password.")
    private String password;

    public static UserFormDto of(User user) {
        UserFormDto dto = new UserFormDto();
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        return dto;
    }

}
