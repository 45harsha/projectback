package com.fsd.sdp.project.controller;

import com.fsd.sdp.project.model.User;
import com.fsd.sdp.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Register user
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        try {
            String result = userService.adduser(user);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            if ("Username already taken".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
            } else if ("Email already taken".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already taken");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An error occurred during registration");
            }
        }
    }

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            User loggedInUser = userService.loginUser(user);
            return ResponseEntity.ok(loggedInUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login");
        }
    }
}
