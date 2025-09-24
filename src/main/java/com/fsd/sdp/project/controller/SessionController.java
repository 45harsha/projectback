package com.fsd.sdp.project.controller;

import com.fsd.sdp.project.model.FileEntity;
import com.fsd.sdp.project.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = {
        "http://localhost:8084",
        "http://localhost:3000",
        "https://reactfrontend-orcin.vercel.app"
}, allowedHeaders = "*", allowCredentials = "true")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    // Upload a file to a session
    @PostMapping("/upload/{sessionId}/{userId}")
    public ResponseEntity<?> uploadSession(
            @PathVariable String sessionId,
            @PathVariable int userId,
            @RequestParam("file") MultipartFile file) {
        try {
            FileEntity uploadedFile = sessionService.uploadFile(userId, sessionId, file);
            return ResponseEntity.ok(uploadedFile);
        } catch (IOException e) {
            return ResponseEntity.status(400).body("Upload failed: " + e.getMessage());
        }
    }

    // Get all files in a session
    @GetMapping("/{sessionId}/files")
    public ResponseEntity<List<FileEntity>> getSessionFiles(@PathVariable String sessionId) {
        List<FileEntity> files = sessionService.getSessionFiles(sessionId);
        return ResponseEntity.ok(files);
    }

    // Create a new session
    @PostMapping("/create")
    public ResponseEntity<Session> createSession(
            @RequestParam String passkey,
            @RequestParam String creatorUsername) {
        Session session = sessionService.createSession(passkey, creatorUsername);
        return ResponseEntity.ok(session);
    }

    // Join an existing session
    @PostMapping("/join")
    public ResponseEntity<Session> joinSession(
            @RequestParam String passkey,
            @RequestParam String username) {
        Session session = sessionService.joinSession(passkey, username);
        return ResponseEntity.ok(session);
    }
}
