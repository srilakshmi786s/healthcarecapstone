package com.edutech.progressive.controller;

import com.edutech.progressive.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Public (no-auth) endpoints for the forgot-password / reset-password flow.
 *
 * POST /api/auth/forgot-password  { "username": "user@example.com" }
 * POST /api/auth/reset-password   { "token": "uuid", "newPassword": "new!" }
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    /**
     * Step 1: User submits their email.
     * Always returns 200 (even for unknown emails) to prevent account enumeration.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Username (email) is required."));
        }
        passwordResetService.requestReset(username.trim());
        return ResponseEntity.ok(
                Map.of("message", "If that email is registered, a reset link has been sent."));
    }

    /**
     * Step 2: User submits the token + new password from the email link.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");

        if (token == null || token.isBlank() || newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Token and new password are required."));
        }
        if (newPassword.length() < 6) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Password must be at least 6 characters."));
        }

        try {
            passwordResetService.resetPassword(token.trim(), newPassword);
            return ResponseEntity.ok(Map.of("message", "Password updated successfully. You can now log in."));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }
}
