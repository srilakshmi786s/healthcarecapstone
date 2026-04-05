package com.edutech.healthcare_appointment_management_system.service;

public interface PasswordResetService {

    /**
     * Generate a reset token for the given username, persist it,
     * and send an HTML reset-password email via SMTP.
     *
     * @param username the user's registered username (= email)
     * @throws RuntimeException if the username is not found
     */
    void requestReset(String username);

    /**
     * Validate the token and, if valid, update the user's password.
     *
     * @param token       UUID token from the reset email
     * @param newPassword plain-text new password (will be BCrypt-encoded)
     * @throws RuntimeException if token is invalid, expired, or already used
     */
    void resetPassword(String token, String newPassword);
}
