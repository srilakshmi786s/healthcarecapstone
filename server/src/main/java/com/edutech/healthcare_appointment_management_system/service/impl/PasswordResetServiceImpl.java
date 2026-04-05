package com.edutech.healthcare_appointment_management_system.service.impl;

import com.edutech.healthcare_appointment_management_system.entity.PasswordResetToken;
import com.edutech.healthcare_appointment_management_system.entity.User;
import com.edutech.healthcare_appointment_management_system.repository.PasswordResetTokenRepository;
import com.edutech.healthcare_appointment_management_system.repository.UserRepository;
import com.edutech.healthcare_appointment_management_system.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    public PasswordResetServiceImpl(UserRepository userRepository,
                                    PasswordResetTokenRepository tokenRepository,
                                    JavaMailSender mailSender,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void requestReset(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            // Return silently — never reveal whether an account exists
            return;
        }

        // Remove any existing (stale) tokens for this user
        tokenRepository.deleteByUsername(username);

        // Generate secure random token (UUID = 128-bit)
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);
        PasswordResetToken resetToken = new PasswordResetToken(token, username, expiresAt);
        tokenRepository.save(resetToken);

        // Build and send the HTML email
        String resetLink = frontendUrl + "/auth/reset-password?token=" + token;
        sendResetEmail(username, resetLink);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired password reset token."));

        if (resetToken.isUsed()) {
            throw new RuntimeException("This reset link has already been used.");
        }
        if (LocalDateTime.now().isAfter(resetToken.getExpiresAt())) {
            throw new RuntimeException("Password reset token has expired. Please request a new one.");
        }

        User user = userRepository.findByUsername(resetToken.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found.");
        }

        // Encode and persist the new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Mark token as used to prevent replay
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }

    // ── private helpers ────────────────────────────────────────────────────────

    private void sendResetEmail(String toEmail, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(toEmail);
            helper.setSubject("MediConnect — Password Reset Request");
            helper.setText(buildHtmlEmail(toEmail, resetLink), true /* isHtml */);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send reset email: " + e.getMessage(), e);
        }
    }

    private String buildHtmlEmail(String email, String resetLink) {
        return "<!DOCTYPE html>" +
               "<html><head><meta charset='UTF-8'>" +
               "<style>" +
               "  body{font-family:Arial,sans-serif;background:#f0f4f8;margin:0;padding:20px;}" +
               "  .card{background:#fff;max-width:520px;margin:40px auto;border-radius:12px;" +
               "        padding:36px;box-shadow:0 4px 20px rgba(0,0,0,.1);}" +
               "  h2{color:#2F7ECF;margin-top:0;}" +
               "  p{color:#444;line-height:1.6;}" +
               "  .btn{display:inline-block;margin:24px 0;padding:14px 32px;" +
               "        background:linear-gradient(90deg,#34A678,#2F7ECF);color:#fff;" +
               "        border-radius:8px;text-decoration:none;font-size:16px;font-weight:600;}" +
               "  .note{font-size:13px;color:#888;}" +
               "  .footer{margin-top:32px;font-size:12px;color:#aaa;text-align:center;}" +
               "</style></head><body>" +
               "<div class='card'>" +
               "  <h2>🔐 Password Reset</h2>" +
               "  <p>Hi <strong>" + email + "</strong>,</p>" +
               "  <p>We received a request to reset your <strong>MediConnect</strong> password. " +
               "     Click the button below to set a new password. The link is valid for <strong>30 minutes</strong>.</p>" +
               "  <a href='" + resetLink + "' class='btn'>Reset My Password</a>" +
               "  <p class='note'>If you didn't request this, you can safely ignore this email. " +
               "     Your password will not change.</p>" +
               "  <p class='note'>Or copy this link into your browser:<br>" +
               "     <code>" + resetLink + "</code></p>" +
               "</div>" +
               "<div class='footer'>© 2026 MediConnect Healthcare. All rights reserved.</div>" +
               "</body></html>";
    }
}
