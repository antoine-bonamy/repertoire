package fr.bonamy.repertoire_back.dto;

import fr.bonamy.repertoire_back.model.Group;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class GroupDto {

    private Long id;

    @NotNull(message = "Name cannot be null")
    @Length(min = 1, max = 64, message = "Name length must be between 1 and 64")
    private String name;

    @Length(max = 1000, message = "Comment length cannot be superior to 1000")
    private String note;

    @NotNull(message = "UserId cannot be null")
    private UserDto user;

    public static GroupDto of(Group group) {
        GroupDto dto = new GroupDto();
        group.setId(group.getId());
        dto.setName(group.getName());
        dto.setNote(group.getNote());
        dto.setUser(UserDto.of(group.getUser()));
        return dto;
    }
}
