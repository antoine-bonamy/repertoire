package fr.bonamy.repertoire_back.mapper;

import fr.bonamy.repertoire_back.dto.front.UserFormDto;
import fr.bonamy.repertoire_back.dto.front.UserFrontDto;
import fr.bonamy.repertoire_back.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserFrontDto toDto(User user) {
        return new UserFrontDto(user.getId().toString(), user.getFirstname(), user.getLastname(),user.getEmail());
    }

    public User toEntity(UserFormDto dto) {
        User user = new User();
        user.setFirstname(dto.firstname());
        user.setLastname(dto.lastname());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        return user;
    }

}
