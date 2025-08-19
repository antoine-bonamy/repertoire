package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.UserDto;
import fr.bonamy.repertoire_back.exception.ResourceAlreadyExist;
import fr.bonamy.repertoire_back.exception.ResourceNotFoundException;
import fr.bonamy.repertoire_back.model.User;
import fr.bonamy.repertoire_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static fr.bonamy.repertoire_back.util.InitPageable.initPageable;

@Service
public class UserService {

    private static final String USER_NOT_FOUND = "User with id : %s not found.";
    private static final String USER_ALREADY_EXIST = "User with email : %s already exist.";

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void exist(Long id) {
        userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, id)));
    }

    public UserDto getById(Long id) {
        return userRepository.findById(id).map(UserDto::of).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, id)));
    }

    public Page<UserDto> search(String keyword, String sortBy, String sortOrder, int page, int size) {
        return userRepository.
                findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword, initPageable(sortBy, sortOrder, page, size))
                .map(UserDto::of);
    }

    public UserDto create(UserDto dto) {
        User user = User.of(dto);
        User probe = new User();
        probe.setEmail(user.getEmail());
        if (userRepository.exists(Example.of(probe))) {
            throw new ResourceAlreadyExist(String.format(USER_ALREADY_EXIST, user.getEmail()));
        }
        user = userRepository.save(user);
        return UserDto.of(user);
    }

    public UserDto update(Long id, UserDto dto) {
        exist(id);
        User newUser = User.of(dto);
        newUser.setId(id);
        userRepository.save(newUser);
        return UserDto.of(newUser);
    }

    public UserDto updatePassword(Long id, UserDto newUser) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, id)));
        userRepository.updatePassword(id, newUser.getPassword());
        user.setPassword(newUser.getPassword());
        return UserDto.of(user);
    }

    public Boolean delete(Long id) {
        exist(id);
        userRepository.deleteById(id);
        return true;
    }

}
