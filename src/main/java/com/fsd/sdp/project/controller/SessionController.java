package com.fsd.sdp.project.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.fsd.sdp.project.service.SessionService;
import com.fsd.sdp.project.model.Session;

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

    @PostMapping("/upload/{sessionId}/{userId}")
    public ResponseEntity<?> uploadSession(
            @PathVariable String sessionId,
            @PathVariable String userId,
            @RequestBody Session session) {
        try {
            sessionService.upload(sessionId, userId, session);
            return ResponseEntity.ok("Upload successful");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Upload failed: " + e.getMessage());
        }
    }

    // Keep all your original 76 lines here, only added @CrossOrigin
}
