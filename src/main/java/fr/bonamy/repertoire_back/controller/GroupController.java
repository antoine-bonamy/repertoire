package fr.bonamy.repertoire_back.controller;

import fr.bonamy.repertoire_back.dto.front.AddContactToGroupDTO;
import fr.bonamy.repertoire_back.dto.front.Group.GroupDetailDTO;
import fr.bonamy.repertoire_back.dto.front.Group.GroupFormDTO;
import fr.bonamy.repertoire_back.dto.front.Group.GroupMinimalDTO;
import fr.bonamy.repertoire_back.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;


    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<GroupDetailDTO>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDetailDTO> getGroupById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @GetMapping("/byName")
    public ResponseEntity<Page<GroupDetailDTO>> getGroupByName(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(groupService.getGroupByName(keyword, sortBy, sortOrder, page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<GroupDetailDTO>> searchGroup(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(groupService.searchGroup(keyword, sortBy, sortOrder, page, size));
    }

    @GetMapping("/isPublic")
    public ResponseEntity<Page<GroupDetailDTO>> getGroupByIsPublic(
            @RequestParam(name = "isPublic") Boolean isPublic,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(groupService.getGroupByIsPublic(isPublic, sortBy, sortOrder, page, size));
    }

    @PostMapping
    public ResponseEntity<GroupMinimalDTO> createUser(@RequestBody GroupFormDTO dto) {
        return new ResponseEntity<>(groupService.createGroup(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupMinimalDTO> updateGroup(@PathVariable Long id, @RequestBody GroupFormDTO dto) {
        return new ResponseEntity<>(groupService.updateGroup(id, dto), HttpStatus.CREATED);
    }

    @PutMapping("/isPublic/{id}")
    public ResponseEntity<GroupMinimalDTO> updateIsPublic(@PathVariable Long id, @RequestBody Boolean isPublic) {
        return new ResponseEntity<>(groupService.updateIsPublic(id, isPublic), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteGroup(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.deleteGroup(id));
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
