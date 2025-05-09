package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.User.UserDetailDTO;
import fr.bonamy.repertoire_back.dto.User.UserFormDTO;
import fr.bonamy.repertoire_back.exception.ResourceAlreadyExist;
import fr.bonamy.repertoire_back.exception.ResourceNotFoundException;
import fr.bonamy.repertoire_back.mapper.UserMapper;
import fr.bonamy.repertoire_back.model.User;
import fr.bonamy.repertoire_back.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService service;

    @Mock
    UserRepository repository;

    @Mock
    UserMapper mapper;

    @Test
    public void getUserById_shouldReturnCorrespondingUser_whenUserExist() {
        UserDetailDTO expectedReturn = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        User user = new User(1L, "Jean", "Martin", "jean.martin@mail.com", "password");
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toDto(user, UserDetailDTO.class)).thenReturn(expectedReturn);
        UserDetailDTO result = service.getById(1L);
        assertEquals(expectedReturn, result);
    }

    @Test
    public void getUserById_shouldThrowResourceNotFoundException_whenDoesntExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            service.getById(1L);
        });
    }

    @Test
    public void searchUsers_shouldReturnAPageOfUserDtos_whenAListIsReturned() {
        List<User> users = List.of(
                new User(1L, "John", "Doe", "john@example.com", "password"),
                new User(2L, "Jane", "Doe", "jane@example.com", "password")
        );
        Page<User> page = new PageImpl<>(users);
        when(repository.findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                "Doe", "Doe", "Doe", PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"))))
                .thenReturn(page);

        List<UserDetailDTO> userFrontDtos = List.of(
                new UserDetailDTO(1L, "John", "Doe", "john@example.com"),
                new UserDetailDTO(2L, "Jane", "Doe", "jane@example.com")
        );
        when(mapper.toDto(users.get(0), UserDetailDTO.class)).thenReturn(userFrontDtos.get(0));
        when(mapper.toDto(users.get(1), UserDetailDTO.class)).thenReturn(userFrontDtos.get(1));
        Page<UserDetailDTO> result = service.search("Doe", "id", "asc", 0, 10);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(userFrontDtos.get(0), result.getContent().get(0));
        assertEquals(userFrontDtos.get(1), result.getContent().get(1));
    }

    @Test
    public void createUser_shouldReturnCreatedUser_whenUserIsCreated() {
        UserFormDTO userFormDto = new UserFormDTO("Jean", "Martin", "jean.martin@mail.com", "password");
        UserDetailDTO userFrontDto = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        User user = new User(1L, "Jean", "Martin", "jean.martin@mail.com", "password");
        when(repository.save(user)).thenReturn(user);
        when(mapper.toEntity(userFormDto)).thenReturn(user);
        when(mapper.toDto(user, UserDetailDTO.class)).thenReturn(userFrontDto);
        UserDetailDTO result = service.create(userFormDto);
        assertEquals(userFrontDto, result);
    }

    @Test
    public void createUser_shouldThrowResourceAlreadyExist_whenUserAlreadyExist() {
        UserFormDTO userFormDto = new UserFormDTO("Jean", "Martin", "jean.martin@mail.com", "password");
        User user = new User();
        user.setEmail("email");
        when(repository.exists(Example.of(user))).thenReturn(true);
        when(mapper.toEntity(userFormDto)).thenReturn(user);
        assertThrows(ResourceAlreadyExist.class, () -> service.create(userFormDto));
    }

    @Test
    public void updateUser_shouldReturnUpdatedUser_whenUserIsUpdated() {
        UserFormDTO userFormDto = new UserFormDTO("Jean", "Martin", "jean.martin@mail.com", "password");
        UserDetailDTO userFrontDto = new UserDetailDTO(1L, "Louis", "Martin", "jean.martin@mail.com");
        User user = new User(1L, "Jean", "Martin", "jean.martin@mail.com", "password");

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toEntity(userFormDto)).thenReturn(user);
        when(mapper.toDto(user, UserDetailDTO.class)).thenReturn(userFrontDto);

        UserDetailDTO result = service.update(1L, userFormDto);
        assertEquals(userFrontDto, result);
    }

    @Test
    public void updateUser_shouldResourceNotFoundException_whenUserDoesntExist() {
        UserFormDTO userFormDto = new UserFormDTO("Jean", "Martin", "jean.martin@mail.com", "password");
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, userFormDto));
    }

    @Test
    public void deleteUser_shouldReturnTrue_whenUserIsDeleted() {
        when(repository.findById(1L)).thenReturn(Optional.of(new User(1L, "Jean", "Martin", "jean.martin@mail.com",
                "password")));
        assertTrue(service.delete(1L));
    }

    @Test
    public void deleteUser_shouldResourceNotFoundException_whenUserDoesntExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(1L));
    }

}
