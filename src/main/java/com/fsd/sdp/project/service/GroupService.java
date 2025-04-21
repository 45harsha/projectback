package com.fsd.sdp.project.service;

import com.fsd.sdp.project.model.FileDTO;
import com.fsd.sdp.project.model.Group;
import com.fsd.sdp.project.model.Message;

import java.util.List;

public interface GroupService {
    Group createGroup(String name, String password, String creatorUsername);
    Group joinGroup(Long groupId, String password, String username);
    String leaveGroup(Long groupId, String username);
    List<Group> getUserGroups(String username);
    Message sendMessage(Long groupId, String senderUsername, String content, String type);
    List<Message> getGroupMessages(Long groupId);
    List<FileDTO> getSharedFiles(String username);
    void broadcastMessage(Long groupId, Message message); // Added for WebSocket
}