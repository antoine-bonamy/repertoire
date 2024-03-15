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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserFrontDto getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(userMapper::toDto).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<UserFrontDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<UserFrontDto> searchUsers(String keyword, String sortBy, String sortOrder, int page, int size) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return userRepository.
                findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword,
                        keyword, keyword, pageable).map(userMapper::toDto);
    }

    public UserFrontDto createUser(UserFormDto userDTO) {
        User user = userMapper.toEntity(userDTO);
        User probe = new User();
        probe.setEmail("email");
        if (userRepository.exists(Example.of(probe))) {
            throw new ResourceAlreadyExist(user.getEmail());
        }
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserFrontDto updateUser(Long id, UserFormDto userDTO) {
        userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        User newUser = userMapper.toEntity(userDTO);
        newUser.setId(id);
        userRepository.save(newUser);
        return userMapper.toDto(newUser);
    }

    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
