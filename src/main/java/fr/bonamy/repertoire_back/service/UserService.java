package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.front.UserFormDto;
import fr.bonamy.repertoire_back.dto.front.UserFrontDto;
import fr.bonamy.repertoire_back.exception.ResourceAlreadyExist;
import fr.bonamy.repertoire_back.exception.ResourceNotFoundException;
import fr.bonamy.repertoire_back.mapper.UserMapper;
import fr.bonamy.repertoire_back.model.User;
import fr.bonamy.repertoire_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String USER_NOT_FOUND = "User with id : %s not found.";
    private static final String USER_ALREADY_EXIST = "User with email : %s already exist.";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    private Pageable initPageable(String sortBy, String sortOrder, int page, int size) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    public void exist(Long id) {
        userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, id)));
    }

    public UserFrontDto getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, id)));
    }

    public List<UserFrontDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<UserFrontDto> searchUsers(String keyword, String sortBy, String sortOrder, int page, int size) {
        return userRepository.
                findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword, initPageable(sortBy, sortOrder, page, size)).map(userMapper::toDto);
    }

    public UserFrontDto createUser(UserFormDto userDTO) {
        User user = userMapper.toEntity(userDTO);
        User probe = new User();
        probe.setEmail(user.getEmail());
        if (userRepository.exists(Example.of(probe))) {
            throw new ResourceAlreadyExist(String.format(USER_ALREADY_EXIST, user.getEmail()));
        }
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserFrontDto updateUser(Long id, UserFormDto userDTO) {
        exist(id);
        User newUser = userMapper.toEntity(userDTO);
        newUser.setId(id);
        userRepository.save(newUser);
        return userMapper.toDto(newUser);
    }

    public boolean deleteUser(Long id) {
        exist(id);
        userRepository.deleteById(id);
        return true;
    }
}
