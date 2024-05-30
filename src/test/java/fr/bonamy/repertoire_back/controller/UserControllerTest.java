package fr.bonamy.repertoire_back.controller;

import fr.bonamy.repertoire_back.dto.User.UserDetailDTO;
import fr.bonamy.repertoire_back.dto.User.UserFormDTO;
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

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    UserController controller;

    @Mock
    UserService service;


    @Test
    public void getUserById_shouldReturn200_whenUserExist() {
        UserDetailDTO user = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        when(service.getById(1L)).thenReturn(user);
        ResponseEntity<UserDetailDTO> result = controller.getById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getUserById_shouldReturnCorrespondingUser_whenUserExist() {
        UserDetailDTO user = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        when(service.getById(1L)).thenReturn(user);
        ResponseEntity<UserDetailDTO> result = controller.getById(1L);
        assertEquals(user, result.getBody());
    }

    @Test
    public void searchUsers_shouldReturn200_whenAPageIsReturned() {

        List<UserDetailDTO> userFrontDtos = List.of(
                new UserDetailDTO(1L, "John", "Doe", "john@example.com"),
                new UserDetailDTO(2L, "Jane", "Doe", "jane@example.com")
        );
        Page<UserDetailDTO> page = new PageImpl<>(userFrontDtos);

        when(service.search("Doe", "firstname", "asc", 0, 10)).thenReturn(page);
        ResponseEntity<Page<UserDetailDTO>> result = controller.search("Doe", "firstname", "asc", 0, 10);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void searchUsers_shouldReturnAPageOfUsers_whenAListIsReturned() {

        List<UserDetailDTO> userFrontDtos = List.of(
                new UserDetailDTO(1L, "John", "Doe", "john@example.com"),
                new UserDetailDTO(2L, "Jane", "Doe", "jane@example.com")
        );
        Page<UserDetailDTO> page = new PageImpl<>(userFrontDtos);

        when(service.search("Doe", "firstname", "asc", 0, 10)).thenReturn(page);
        ResponseEntity<Page<UserDetailDTO>> result = controller.search("Doe", "firstname", "asc", 0, 10);
        assertEquals(userFrontDtos.get(0), Objects.requireNonNull(result.getBody()).getContent().get(0));
        assertEquals(userFrontDtos.get(1), Objects.requireNonNull(result.getBody()).getContent().get(1));

    }


    @Test
    public void createUser_shouldReturn201_whenUserIsCreated() {
        UserFormDTO userFormDto = new UserFormDTO("Jean", "Martin", "jean.martin@mail.com", "password");
        UserDetailDTO userFrontDto = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        when(service.create(userFormDto)).thenReturn(userFrontDto);
        ResponseEntity<UserDetailDTO> result = controller.create(userFormDto);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    public void createUser_shouldReturnCreatedUser_whenUserIsCreated() {
        UserFormDTO userFormDto = new UserFormDTO("Jean", "Martin", "jean.martin@mail.com", "password");
        UserDetailDTO userFrontDto = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        when(service.create(userFormDto)).thenReturn(userFrontDto);
        ResponseEntity<UserDetailDTO> result = controller.create(userFormDto);
        assertEquals(userFrontDto, result.getBody());
    }

    @Test
    public void updateUser_shouldReturn201_whenUserIsUpdated() {
        UserFormDTO userFormDto = new UserFormDTO("Jean", "Martin", "jean.martin@mail.com", "password");
        UserDetailDTO userFrontDto = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        when(service.update(1L, userFormDto)).thenReturn(userFrontDto);
        ResponseEntity<UserDetailDTO> result = controller.update(1L, userFormDto);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    public void updateUser_shouldReturnUpdatedUser_whenUserIsUpdated() {
        UserFormDTO userFormDto = new UserFormDTO("Jean", "Martin", "jean.martin@mail.com", "password");
        UserDetailDTO userFrontDto = new UserDetailDTO(1L, "Jean", "Martin", "jean.martin@mail.com");
        when(service.update(1L, userFormDto)).thenReturn(userFrontDto);
        ResponseEntity<UserDetailDTO> result = controller.update(1L, userFormDto);
        assertEquals(userFrontDto, result.getBody());
    }

    @Test
    public void deleteUser_shouldReturn200_whenUserIsDeleted() {
        when(service.delete(1L)).thenReturn(true);
        ResponseEntity<Boolean> result = controller.delete(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void deleteUser_shouldReturnTrue_whenUserIsDeleted() {
        when(service.delete(1L)).thenReturn(true);
        ResponseEntity<Boolean> result = controller.delete(1L);
        assertEquals(Boolean.TRUE, result.getBody());
    }

}
