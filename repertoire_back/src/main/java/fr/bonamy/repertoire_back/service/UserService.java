package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.User.UserDetailDTO;
import fr.bonamy.repertoire_back.dto.User.UserFormDTO;
import fr.bonamy.repertoire_back.dto.User.UserUpdatePasswordDTO;
import fr.bonamy.repertoire_back.exception.ResourceAlreadyExist;
import fr.bonamy.repertoire_back.exception.ResourceNotFoundException;
import fr.bonamy.repertoire_back.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void exist(Long id) {
        userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, id)));
    }

    public UserDetailDTO getById(Long id) {
        return userRepository.findById(id).map(user -> userMapper.toDto(user, UserDetailDTO.class)).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, id)));
    }

    public Page<UserDetailDTO> search(String keyword, String sortBy, String sortOrder, int page, int size) {
        return userRepository.
                findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword, initPageable(sortBy, sortOrder, page, size))
                .map(user -> userMapper.toDto(user, UserDetailDTO.class));
    }

    public UserDetailDTO create(UserFormDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User probe = new User();
        probe.setEmail(user.getEmail());
        if (userRepository.exists(Example.of(probe))) {
            throw new ResourceAlreadyExist(String.format(USER_ALREADY_EXIST, user.getEmail()));
        }
        user = userRepository.save(user);
        return userMapper.toDto(user, UserDetailDTO.class);
    }

    public UserDetailDTO update(Long id, UserFormDTO userDTO) {
        exist(id);
        User newUser = userMapper.toEntity(userDTO);
        newUser.setId(id);
        userRepository.save(newUser);
        return userMapper.toDto(newUser, UserDetailDTO.class);
    }

    public UserDetailDTO updatePassword(Long id, UserUpdatePasswordDTO newUser) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, id)));
        userRepository.updatePassword(id, newUser.password());
        user.setPassword(newUser.password());
        return userMapper.toDto(user, UserDetailDTO.class);

    }

    public Boolean delete(Long id) {
        exist(id);
        userRepository.deleteById(id);
        return true;
    }
}
