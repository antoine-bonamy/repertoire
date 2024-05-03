package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.front.UserFormDto;
import fr.bonamy.repertoire_back.dto.front.UserFrontDto;
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

import java.util.ArrayList;
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
        UserFrontDto expectedReturn = new UserFrontDto("1", "Jean", "Martin", "jean.martin@mail.com");
        User user = new User(1L, "Jean", "Martin", "jean.martin@mail.com", "password");
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toDto(user)).thenReturn(expectedReturn);
        UserFrontDto result = service.getUserById(1L);
        assertEquals(expectedReturn, result);
    }

    @Test
    public void getUserById_shouldThrowResourceNotFoundException_whenDoesntExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            service.getUserById(1L);
        });
    }

    @Test
    public void getAllUsers_shouldReturnAnEmptyListOfUserDto_whenNoUserExists() {
        when(repository.findAll()).thenReturn(List.of());
        List<UserFrontDto> result = service.getAllUsers();
        assertTrue(result.isEmpty());
    }

    @Test
    public void getAllUsers_shouldReturnListOfUserDto_whenUsersExists() {
        User user1 = new User(1L, "Jean", "Martin", "jean.martin@mail.com", "pass");
        User user2 = new User(2L, "Jean", "Claude", "jean.martin@mail.com", "pass");
        User user3 = new User(3L, "Claude", "Martin", "jean.martin@mail.com", "pass");
        List<User> users = new ArrayList<>(List.of(user1, user2, user3));
        UserFrontDto userDto1 = new UserFrontDto("1", "Jean", "Martin", "jean.martin@mail.com");
        UserFrontDto userDto2 = new UserFrontDto("2", "Jean", "Claude", "jean.martin@mail.com");
        UserFrontDto userDto3 = new UserFrontDto("3", "Claude", "Martin", "jean.martin@mail.com");
        List<UserFrontDto> userDtos = new ArrayList<>(List.of(userDto1, userDto2, userDto3));
        when(repository.findAll()).thenReturn(users);
        when(mapper.toDto(user1)).thenReturn(userDto1);
        when(mapper.toDto(user2)).thenReturn(userDto2);
        when(mapper.toDto(user3)).thenReturn(userDto3);
        List<UserFrontDto> result = service.getAllUsers();
        assert result != null;
        if (userDtos.size() != result.size()) fail();
        boolean equal = false;
        for (int k = 0; k < users.size(); k++) {
            equal = userDtos.get(k).equals(result.get(k));
        }
        assertTrue(equal);
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

        List<UserFrontDto> userFrontDtos = List.of(
                new UserFrontDto("1", "John", "Doe", "john@example.com"),
                new UserFrontDto("2", "Jane", "Doe", "jane@example.com")
        );
        when(mapper.toDto(users.get(0))).thenReturn(userFrontDtos.get(0));
        when(mapper.toDto(users.get(1))).thenReturn(userFrontDtos.get(1));
        Page<UserFrontDto> result = service.searchUsers("Doe", "id", "asc", 0, 10);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(userFrontDtos.get(0), result.getContent().get(0));
        assertEquals(userFrontDtos.get(1), result.getContent().get(1));
    }

    @Test
    public void createUser_shouldReturnCreatedUser_whenUserIsCreated() {
        UserFormDto userFormDto = new UserFormDto("Jean", "Martin", "jean.martin@mail.com", "password");
        UserFrontDto userFrontDto = new UserFrontDto("1", "Jean", "Martin", "jean.martin@mail.com");
        User user = new User(1L, "Jean", "Martin", "jean.martin@mail.com", "password");
        when(repository.save(user)).thenReturn(user);
        when(mapper.toEntity(userFormDto)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(userFrontDto);
        UserFrontDto result = service.createUser(userFormDto);
        assertEquals(userFrontDto, result);
    }

    @Test
    public void createUser_shouldThrowResourceAlreadyExist_whenUserAlreadyExist() {
        UserFormDto userFormDto = new UserFormDto("Jean", "Martin", "jean.martin@mail.com", "password");
        User user = new User();
        user.setEmail("email");
        when(repository.exists(Example.of(user))).thenReturn(true);
        when(mapper.toEntity(userFormDto)).thenReturn(user);
        assertThrows(ResourceAlreadyExist.class, () -> service.createUser(userFormDto));
    }

    @Test
    public void updateUser_shouldReturnUpdatedUser_whenUserIsUpdated() {
        UserFormDto userFormDto = new UserFormDto("Jean", "Martin", "jean.martin@mail.com", "password");
        UserFrontDto userFrontDto = new UserFrontDto("1", "Louis", "Martin", "jean.martin@mail.com");
        User user = new User(1L, "Jean", "Martin", "jean.martin@mail.com", "password");

        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.toEntity(userFormDto)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(userFrontDto);

        UserFrontDto result = service.updateUser(1L, userFormDto);
        assertEquals(userFrontDto, result);
    }

    @Test
    public void updateUser_shouldResourceNotFoundException_whenUserDoesntExist() {
        UserFormDto userFormDto = new UserFormDto("Jean", "Martin", "jean.martin@mail.com", "password");
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.updateUser(1L, userFormDto));
    }

    @Test
    public void deleteUser_shouldReturnTrue_whenUserIsDeleted() {
        when(repository.findById(1L)).thenReturn(Optional.of(new User(1L, "Jean", "Martin", "jean.martin@mail.com", "password")));
        assertTrue(service.deleteUser(1L));
    }

    @Test
    public void deleteUser_shouldResourceNotFoundException_whenUserDoesntExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deleteUser(1L));
    }

}
