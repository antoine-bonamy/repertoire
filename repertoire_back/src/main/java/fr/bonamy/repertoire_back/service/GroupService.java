package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.Group.GroupDetailDto;
import fr.bonamy.repertoire_back.dto.Group.GroupFormDto;
import fr.bonamy.repertoire_back.exception.ResourceAlreadyExist;
import fr.bonamy.repertoire_back.exception.ResourceNotFoundException;
import fr.bonamy.repertoire_back.model.Group;
import fr.bonamy.repertoire_back.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static fr.bonamy.repertoire_back.util.InitPageable.initPageable;

@Service
public class GroupService {

    private static final String GROUP_NOT_FOUND = "Group with id=%s not found.";
    private static final String CONTACT_ALREADY_IN_GROUP = "Contact with id=%s is already in group with id=%s";

    private final GroupRepository groupRepository;
    private final UserService userService;
    private final ContactService contactService;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserService userService, ContactService contactService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
        this.contactService = contactService;
    }

    public void exist(Long id) {
        groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(GROUP_NOT_FOUND, id)));
    }

    public GroupDetailDto getById(Long id) {
        return groupRepository.findById(id).map(GroupDetailDto::of)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(GROUP_NOT_FOUND, id)));
    }

    public Page<GroupDetailDto> search(Long userId, String keyword, String sortBy, String sortOrder, int page,
                                       int size) {
        return groupRepository.search(
                        userId, keyword, initPageable(sortBy, sortOrder, page, size))
                .map(GroupDetailDto::of);
    }

    public GroupDetailDto create(GroupFormDto dto) {
        userService.exist(dto.getUser().getId());
        return GroupDetailDto.of(groupRepository.save(Group.of(dto)));
    }

    public GroupDetailDto update(Long id, GroupFormDto dto) {
        exist(id);
        userService.getById(dto.getUser().getId());
        Group newGroup = Group.of(dto);
        newGroup.setId(id);
        return GroupDetailDto.of(groupRepository.save(newGroup));
    }

    public Boolean delete(Long id) {
        exist(id);
        groupRepository.deleteById(id);
        return true;
    }

    public void addContact(Long groupId, Long contactId) {
        exist(groupId);
        contactService.exist(contactId);
        if (Boolean.parseBoolean(groupRepository.isContactInGroup(groupId, contactId).toString())) {
            throw new ResourceAlreadyExist(String.format(CONTACT_ALREADY_IN_GROUP, contactId, groupId));
        }
        groupRepository.addContact(groupId, contactId);
    }

    public void removeContact(Long groupId, Long contactId) {
        exist(groupId);
        groupRepository.removeContact(groupId, contactId);
    }

}
