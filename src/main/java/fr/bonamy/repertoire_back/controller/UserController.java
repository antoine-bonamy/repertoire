package fr.bonamy.repertoire_back.controller;

import fr.bonamy.repertoire_back.dto.User.UserDetailDTO;
import fr.bonamy.repertoire_back.dto.User.UserFormDTO;
import fr.bonamy.repertoire_back.dto.User.UserUpdatePasswordDTO;
import fr.bonamy.repertoire_back.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserDetailDTO>> search(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.search(keyword, sortBy, sortOrder, page, size));
    }

    @PostMapping
    public ResponseEntity<UserDetailDTO> create(@Valid @RequestBody UserFormDTO userDTO) {
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailDTO> update(@PathVariable Long id, @Valid @RequestBody UserFormDTO userDTO) {
        return new ResponseEntity<>(userService.update(id, userDTO), HttpStatus.CREATED);
    }

    @PutMapping("password/{id}")
    public ResponseEntity<UserDetailDTO> updatePassword(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdatePasswordDTO user) {
        return new ResponseEntity<>(userService.updatePassword(id, user), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.delete(id));
    }

}
