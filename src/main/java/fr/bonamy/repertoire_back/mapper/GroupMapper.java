package fr.bonamy.repertoire_back.mapper;

import fr.bonamy.repertoire_back.dto.front.GroupFormDto;
import fr.bonamy.repertoire_back.dto.front.GroupFrontDto;
import fr.bonamy.repertoire_back.model.Group;
import fr.bonamy.repertoire_back.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GroupMapper {

    private final ContactMapper contactMapper;

    @Autowired
    public GroupMapper(ContactMapper contactMapper) {
        this.contactMapper = contactMapper;
    }

    public GroupFrontDto toDto(Group group) {
        return new GroupFrontDto(
                group.getId(),
                group.getName(),
                group.getComment(),
                group.getPublic(),
                group.getContactList().stream().map(contactMapper::toDto).toList(),
                new GroupFrontDto.GroupUserFrontDto(
                        group.getUser().getId(),
                        group.getUser().getFirstname(),
                        group.getUser().getLastname()
                )
        );

    }

    public Group toEntity(GroupFormDto dto) {
        Group group = new Group();
        group.setName(dto.name());
        group.setComment(dto.comment());
        group.setPublic(dto.isPublic());
        User user = new User();
        user.setId(dto.userId());
        group.setUser(user);
        group.setContactList(new ArrayList<>());
        return group;
    }

}
