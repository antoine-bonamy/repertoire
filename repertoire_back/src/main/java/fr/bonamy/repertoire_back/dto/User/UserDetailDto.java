package fr.bonamy.repertoire_back.dto.User;

import fr.bonamy.repertoire_back.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDetailDto extends UserFormDto {

    private Long id;

    public static UserDetailDto of(User user) {
        UserDetailDto dto = (UserDetailDto) UserFormDto.of(user);
        dto.setId(user.getId());
        return dto;
    }

}