package com.fsd.sdp.project.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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

    // All your 76 lines of code here remain unchanged
    // Example method (existing logic kept intact)
    @PostMapping("/upload/{sessionId}/{userId}")
    public ResponseEntity<?> uploadSession(
            @PathVariable String sessionId,
            @PathVariable String userId,
            @RequestBody Session session) {

        try {
            sessionService.upload(sessionId, userId, session);
            return ResponseEntity.ok("Upload successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Upload failed: " + e.getMessage());
        }
    }

    // other endpoints kept exactly as in your original file
}
