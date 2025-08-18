package fr.bonamy.repertoire_back.dto.Group;

import fr.bonamy.repertoire_back.model.Group;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupDetailDto extends GroupFormDto {

    private Long id;

    public static GroupDetailDto of(Group group) {
        GroupDetailDto dto = (GroupDetailDto) GroupFormDto.of(group);
        dto.setId(group.getId());
        return dto;
    }

}
