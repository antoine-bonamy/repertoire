package fr.bonamy.repertoire_back.controller;

import fr.bonamy.repertoire_back.dto.front.User.UserDetailDTO;
import fr.bonamy.repertoire_back.dto.front.User.UserFormDTO;
import fr.bonamy.repertoire_back.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    UserController controller;

    @Mock
    UserService service;


    @Test
    public void getAllUsers_shouldReturn200_whenNoExists() {
        when(service.getAllUsers()).thenReturn(List.of());
        ResponseEntity<List<UserDetailDTO>> result = controller.getAllUsers();
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getAllUsers_shouldReturn200_whenUsersExists() {
        when(service.getAllUsers()).thenReturn(List.of());
        ResponseEntity<List<UserDetailDTO>> result = controller.getAllUsers();
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getAllUsers_shouldReturnListOfUser_whenUsersExists() {
        UserDetailDTO user1 = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        UserDetailDTO user2 = new UserDetailDTO(2L, "Jean", "Claude", "jean.martin@mail.com");
        UserDetailDTO user3 = new UserDetailDTO(3L, "Claude", "Martin", "jean.martin@mail.com");
        List<UserDetailDTO> users = new ArrayList<>(List.of(user1, user2, user3));
        when(service.getAllUsers()).thenReturn(users);
        List<UserDetailDTO> result = controller.getAllUsers().getBody();
        assert result != null;
        if (users.size() != result.size()) fail();
        boolean equal = false;
        for (int k = 0; k < users.size(); k++) {
            equal = users.get(k).equals(result.get(k));
        }
        assertTrue(equal);
    }

    @Test
    public void getAllUsers_shouldReturnAnEmptyListOfUser_whenNoUserExists() {
        when(service.getAllUsers()).thenReturn(List.of());
        ResponseEntity<List<UserDetailDTO>> result = controller.getAllUsers();
        assertTrue(Objects.requireNonNull(result.getBody()).isEmpty());
    }

    @Test
    public void getUserById_shouldReturn200_whenUserExist() {
        UserDetailDTO user = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        when(service.getUserById(1L)).thenReturn(user);
        ResponseEntity<UserDetailDTO> result = controller.getUserById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getUserById_shouldReturnCorrespondingUser_whenUserExist() {
        UserDetailDTO user = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        when(service.getUserById(1L)).thenReturn(user);
        ResponseEntity<UserDetailDTO> result = controller.getUserById(1L);
        assertEquals(user, result.getBody());
    }

    @Test
    public void searchUsers_shouldReturn200_whenAPageIsReturned() {

        List<UserDetailDTO> userFrontDtos = List.of(
                new UserDetailDTO(1L, "John", "Doe", "john@example.com"),
                new UserDetailDTO(2L, "Jane", "Doe", "jane@example.com")
        );
        Page<UserDetailDTO> page = new PageImpl<>(userFrontDtos);

        when(service.searchUsers("Doe", "firstname", "asc", 0, 10)).thenReturn(page);
        ResponseEntity<Page<UserDetailDTO>> result = controller.searchUsers("Doe", "firstname", "asc", 0, 10);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void searchUsers_shouldReturnAPageOfUsers_whenAListIsReturned() {

        List<UserDetailDTO> userFrontDtos = List.of(
                new UserDetailDTO(1L, "John", "Doe", "john@example.com"),
                new UserDetailDTO(2L, "Jane", "Doe", "jane@example.com")
        );
        Page<UserDetailDTO> page = new PageImpl<>(userFrontDtos);

        when(service.searchUsers("Doe", "firstname", "asc", 0, 10)).thenReturn(page);
        ResponseEntity<Page<UserDetailDTO>> result = controller.searchUsers("Doe", "firstname", "asc", 0, 10);
        assertEquals(userFrontDtos.get(0), Objects.requireNonNull(result.getBody()).getContent().get(0));
        assertEquals(userFrontDtos.get(1), Objects.requireNonNull(result.getBody()).getContent().get(1));

    }


    @Test
    public void createUser_shouldReturn201_whenUserIsCreated() {
        UserFormDTO userFormDto = new UserFormDTO("Jean", "Martin", "jean.martin@mail.com", "password");
        UserDetailDTO userFrontDto = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        when(service.createUser(userFormDto)).thenReturn(userFrontDto);
        ResponseEntity<UserDetailDTO> result = controller.createUser(userFormDto);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    public void createUser_shouldReturnCreatedUser_whenUserIsCreated() {
        UserFormDTO userFormDto = new UserFormDTO("Jean", "Martin", "jean.martin@mail.com", "password");
        UserDetailDTO userFrontDto = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        when(service.createUser(userFormDto)).thenReturn(userFrontDto);
        ResponseEntity<UserDetailDTO> result = controller.createUser(userFormDto);
        assertEquals(userFrontDto, result.getBody());
    }

    @Test
    public void updateUser_shouldReturn201_whenUserIsUpdated() {
        UserFormDTO userFormDto = new UserFormDTO("Jean", "Martin", "jean.martin@mail.com", "password");
        UserDetailDTO userFrontDto = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        when(service.updateUser(1L, userFormDto)).thenReturn(userFrontDto);
        ResponseEntity<UserDetailDTO> result = controller.updateUser(1L, userFormDto);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    public void updateUser_shouldReturnUpdatedUser_whenUserIsUpdated() {
        UserFormDTO userFormDto = new UserFormDTO("Jean", "Martin", "jean.martin@mail.com", "password");
        UserDetailDTO userFrontDto = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        when(service.updateUser(1L, userFormDto)).thenReturn(userFrontDto);
        ResponseEntity<UserDetailDTO> result = controller.updateUser(1L, userFormDto);
        assertEquals(userFrontDto, result.getBody());
    }

    @Test
    public void deleteUser_shouldReturn200_whenUserIsDeleted() {
        when(service.deleteUser(1L)).thenReturn(true);
        ResponseEntity<Boolean> result = controller.deleteUser(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void deleteUser_shouldReturnTrue_whenUserIsDeleted() {
        when(service.deleteUser(1L)).thenReturn(true);
        ResponseEntity<Boolean> result = controller.deleteUser(1L);
        assertEquals(Boolean.TRUE, result.getBody());
    }

}
