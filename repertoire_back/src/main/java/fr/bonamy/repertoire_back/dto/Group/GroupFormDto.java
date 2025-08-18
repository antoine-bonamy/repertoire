package fr.bonamy.repertoire_back.dto.Group;

import fr.bonamy.repertoire_back.dto.User.UserDetailDto;
import fr.bonamy.repertoire_back.model.Group;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class GroupFormDto {

    @NotNull(message = "Name cannot be null")
    @Length(min = 1, max = 64, message = "Name length must be between 1 and 64")
    private String name;

    @Length(max = 1000, message = "Comment length cannot be superior to 1000")
    private String note;

    @NotNull(message = "UserId cannot be null")
    private UserDetailDto user;

    public static GroupFormDto of(Group group) {
        GroupFormDto dto = new GroupFormDto();
        dto.setName(group.getName());
        dto.setNote(group.getNote());
        dto.setUser(fr.bonamy.repertoire_back.dto.User.UserDetailDto.of(group.getUser()));
        return dto;
    }
}
