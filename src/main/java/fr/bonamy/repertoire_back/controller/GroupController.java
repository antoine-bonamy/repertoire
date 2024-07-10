package fr.bonamy.repertoire_back.controller;

import fr.bonamy.repertoire_back.dto.AddContactToGroupDTO;
import fr.bonamy.repertoire_back.dto.Group.GroupDetailDTO;
import fr.bonamy.repertoire_back.dto.Group.GroupFormDTO;
import fr.bonamy.repertoire_back.dto.Group.GroupMinimalDTO;
import fr.bonamy.repertoire_back.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
@CrossOrigin("http://localhost:4200")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDetailDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(groupService.getById(id));
    }

    @GetMapping("/search/{userId}")
    public ResponseEntity<Page<GroupDetailDTO>> search(
            @PathVariable Long userId,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(groupService.search(userId, keyword, sortBy, sortOrder, page, size));
    }

    @PostMapping
    public ResponseEntity<GroupMinimalDTO> creatE(@RequestBody GroupFormDTO dto) {
        return new ResponseEntity<>(groupService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupMinimalDTO> update(@PathVariable Long id, @RequestBody GroupFormDTO dto) {
        return new ResponseEntity<>(groupService.update(id, dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.delete(id));
    }

    @PostMapping("/addContact")
    public ResponseEntity<Boolean> addContact(@RequestBody AddContactToGroupDTO dto) {
        groupService.addContact(dto.groupId(), dto.contactId());
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
    }

    @DeleteMapping("/removeContact")
    public ResponseEntity<Boolean> removeContact(@RequestBody AddContactToGroupDTO dto) {
        groupService.removeContact(dto.groupId(), dto.contactId());
        return ResponseEntity.ok(Boolean.TRUE);
    }

}
