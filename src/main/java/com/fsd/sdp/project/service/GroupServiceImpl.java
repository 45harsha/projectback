package com.fsd.sdp.project.service;

import com.fsd.sdp.project.model.FileDTO;
import com.fsd.sdp.project.model.Group;
import com.fsd.sdp.project.model.Message;
import com.fsd.sdp.project.model.FileEntity;
import com.fsd.sdp.project.repository.GroupRepository;
import com.fsd.sdp.project.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // Added for WebSocket

    @Override
    public Group createGroup(String name, String password, String creatorUsername) {
        Group group = new Group();
        group.setName(name);
        group.setPassword(password);
        List<String> usernames = new ArrayList<>();
        usernames.add(creatorUsername);
        group.setUsernames(usernames);
        return groupRepository.save(group);
    }

    @Override
    public Group joinGroup(Long groupId, String password, String username) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (!groupOpt.isPresent()) {
            throw new RuntimeException("Group not found");
        }
        Group group = groupOpt.get();
        if (!group.getPassword().equals(password)) {
            throw new RuntimeException("Incorrect password");
        }
        List<String> usernames = group.getUsernames();
        if (!usernames.contains(username)) {
            usernames.add(username);
            group.setUsernames(usernames);
            groupRepository.save(group);
        }
        return group;
    }

    @Override
    public String leaveGroup(Long groupId, String username) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (!groupOpt.isPresent()) {
            throw new RuntimeException("Group not found");
        }
        Group group = groupOpt.get();
        List<String> usernames = group.getUsernames();
        if (!usernames.contains(username)) {
            throw new RuntimeException("User is not a member of this group");
        }
        usernames.remove(username);
        group.setUsernames(usernames);
        if (usernames.isEmpty()) {
            groupRepository.delete(group);
            return "Group deleted as it has no members";
        } else {
            groupRepository.save(group);
            return "Successfully left the group";
        }
    }

    @Override
    public List<Group> getUserGroups(String username) {
        List<Group> groups = groupRepository.findByUsernamesContaining(username);
        return groups != null ? groups : new ArrayList<>();
    }

    @Override
    public Message sendMessage(Long groupId, String senderUsername, String content, String type) {
        if (!groupRepository.existsById(groupId)) {
            throw new RuntimeException("Group not found");
        }
        Message message = new Message();
        message.setGroupId(groupId);
        message.setSenderUsername(senderUsername);
        message.setContent(content);
        message.setType(type);
        message.setTimestamp(new Date());
        Message savedMessage = messageRepository.save(message);
        broadcastMessage(groupId, savedMessage); // Broadcast after saving
        return savedMessage;
    }

    @Override
    public List<Message> getGroupMessages(Long groupId) {
        List<Message> messages = messageRepository.findByGroupId(groupId);
        return messages != null ? messages : new ArrayList<>();
    }

    @Override
    public List<FileDTO> getSharedFiles(String username) {
        List<Group> userGroups = getUserGroups(username);
        if (userGroups.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> groupIds = userGroups.stream()
                .map(Group::getId)
                .toList();
        List<Message> messages = messageRepository.findAll().stream()
                .filter(message -> groupIds.contains(message.getGroupId()))
                .filter(message -> "file".equals(message.getType()))
                .toList();
        List<FileDTO> sharedFiles = new ArrayList<>();
        for (Message message : messages) {
            try {
                Long fileId = Long.parseLong(message.getContent());
                FileEntity file = fileService.getFile(fileId);
                if (file != null) {
                    FileDTO fileDTO = new FileDTO(file);
                    Optional<Group> groupOpt = groupRepository.findById(message.getGroupId());
                    if (groupOpt.isPresent()) {
                        fileDTO.setGroupName(groupOpt.get().getName());
                    } else {
                        fileDTO.setGroupName("Unknown Group");
                    }
                    sharedFiles.add(fileDTO);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid file ID in message content: " + message.getContent());
            }
        }
        return sharedFiles;
    }

    @Override
    public void broadcastMessage(Long groupId, Message message) {
        messagingTemplate.convertAndSend("/topic/group/" + groupId, message);
    }
}