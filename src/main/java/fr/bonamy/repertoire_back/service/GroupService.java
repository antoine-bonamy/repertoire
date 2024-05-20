package fr.bonamy.repertoire_back.service;

import fr.bonamy.repertoire_back.dto.front.GroupFormDto;
import fr.bonamy.repertoire_back.dto.front.GroupFrontDto;
import fr.bonamy.repertoire_back.exception.ResourceNotFoundException;
import fr.bonamy.repertoire_back.mapper.GroupMapper;
import fr.bonamy.repertoire_back.model.Group;
import fr.bonamy.repertoire_back.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private static final String GROUP_NOT_FOUND = "Group with id:%s not found.";

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final UserService userService;

    @Autowired
    public GroupService(GroupRepository groupRepository, GroupMapper groupMapper, UserService userService) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.userService = userService;
    }

    private Pageable initPageable(String sortBy, String sortOrder, int page, int size) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    public void exist(Long id) {
        groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(GROUP_NOT_FOUND, id)));
    }

    public List<GroupFrontDto> getAllGroups() {
        return groupRepository.findAll().stream().map(x -> groupMapper.toDto(x, GroupFrontDto.class))
                .collect(Collectors.toList());
    }


    public GroupFrontDto getGroupById(Long id) {
        return groupRepository.findById(id).map(x -> groupMapper.toDto(x, GroupFrontDto.class))
                .orElseThrow(() -> new ResourceNotFoundException(String.format(GROUP_NOT_FOUND, id)));
    }

    public Page<GroupFrontDto> getGroupByName(String keyword, String sortBy, String sortOrder, int page, int size) {
        return groupRepository.findByNameContainingIgnoreCase(keyword, initPageable(sortBy, sortOrder, page, size))
                .map(x -> groupMapper.toDto(x, GroupFrontDto.class));
    }

    public Page<GroupFrontDto> searchGroup(String keyword, String sortBy, String sortOrder, int page, int size) {
        return groupRepository.findByNameContainingIgnoreCaseOrCommentContainingIgnoreCase(
                        keyword, keyword, initPageable(sortBy, sortOrder, page, size))
                .map(x -> groupMapper.toDto(x, GroupFrontDto.class));
    }

    public Page<GroupFrontDto> getGroupByIsPublic(
            Boolean isPublic, String sortBy, String sortOrder, int page, int size) {
        return groupRepository.findByIsPublic(isPublic, initPageable(sortBy, sortOrder, page, size))
                .map(x -> groupMapper.toDto(x, GroupFrontDto.class));
    }

    public GroupFrontDto createGroup(GroupFormDto dto) {
        userService.exist(dto.user().id());
        return groupMapper.toDto(groupRepository.save(groupMapper.toEntity(dto)), GroupFrontDto.class);
    }

    public GroupFrontDto updateGroup(Long id, GroupFormDto dto) {
        exist(id);
        userService.getUserById(dto.user().id());
        Group newGroup = groupMapper.toEntity(dto);
        newGroup.setId(id);
        return groupMapper.toDto(groupRepository.save(newGroup), GroupFrontDto.class);
    }

    public GroupFrontDto updateIsPublic(Long id, Boolean isPublic) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(GROUP_NOT_FOUND, id)));
        groupRepository.updateIsPublic(id, isPublic);
        group.setPublic(isPublic);
        return groupMapper.toDto(group, GroupFrontDto.class);
    }

    public Boolean deleteGroup(Long id) {
        exist(id);
        groupRepository.deleteById(id);
        return true;
    }

    public void addContact(Long groupId, Long contactId) {
        exist(groupId);
        //TODO: ne pas ajouter si la liaison existe deja
        groupRepository.addContact(groupId, contactId);
    }

    public void removeContact(Long groupId, Long contactId) {
        exist(groupId);
        groupRepository.removeContact(groupId, contactId);
    }


}
