package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.User.UserDetailDto;
import fr.bonamy.repertoire_back.dto.User.UserFormDto;
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

    public UserDetailDto getById(Long id) {
        return userRepository.findById(id).map(UserDetailDto::of).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, id)));
    }

    public Page<UserDetailDto> search(String keyword, String sortBy, String sortOrder, int page, int size) {
        return userRepository.
                findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword, initPageable(sortBy, sortOrder, page, size))
                .map(UserDetailDto::of);
    }

    public UserDetailDto create(UserFormDto dto) {
        User user = User.of(dto);
        User probe = new User();
        probe.setEmail(user.getEmail());
        if (userRepository.exists(Example.of(probe))) {
            throw new ResourceAlreadyExist(String.format(USER_ALREADY_EXIST, user.getEmail()));
        }
        user = userRepository.save(user);
        return UserDetailDto.of(user);
    }

    public UserDetailDto update(Long id, UserFormDto dto) {
        exist(id);
        User newUser = User.of(dto);
        newUser.setId(id);
        userRepository.save(newUser);
        return UserDetailDto.of(newUser);
    }

    public UserDetailDto updatePassword(Long id, UserFormDto newUser) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, id)));
        userRepository.updatePassword(id, newUser.getPassword());
        user.setPassword(newUser.getPassword());
        return UserDetailDto.of(user);
    }

    public Boolean delete(Long id) {
        exist(id);
        userRepository.deleteById(id);
        return true;
    }

}
