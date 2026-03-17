package com.tglobe.loadlinks.service;

import com.tglobe.loadlinks.model.PasswordResetToken;
import com.tglobe.loadlinks.model.User;
import com.tglobe.loadlinks.repository.PasswordResetTokenRepository;
import com.tglobe.loadlinks.repository.UserRepository;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public boolean createPasswordResetToken(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            return false; // User not found
        }
        
        User user = userOpt.get();
        
        // Delete any existing tokens for this user
        tokenRepository.deleteByUser(user);
        
        // Create new token
        PasswordResetToken token = new PasswordResetToken(user);
        tokenRepository.save(token);
        
        // Send email with reset link
        sendResetEmail(user, token.getToken());
        
        return true;
    }
    
    private void sendResetEmail(User user, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(user.getEmail());
            helper.setSubject("TGlobe - Password Reset Request");
            helper.setFrom("noreply@tglobe.com");
            
            String resetLink = baseUrl + "/reset-password?token=" + token;
            
            String emailContent = buildEmailContent(user, resetLink);
            helper.setText(emailContent, true);
            
            mailSender.send(message);
            
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
    
    private String buildEmailContent(User user, String resetLink) {
        return "<!DOCTYPE html>\n" +
               "<html>\n" +
               "<head>\n" +
               "    <style>\n" +
               "        body { font-family: Arial, sans-serif; line-height: 1.6; }\n" +
               "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
               "        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); \n" +
               "                  color: white; padding: 20px; text-align: center; border-radius: 10px 10px 0 0; }\n" +
               "        .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }\n" +
               "        .button { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
               "                  color: white; padding: 12px 30px; text-decoration: none; \n" +
               "                  border-radius: 25px; display: inline-block; margin: 20px 0; }\n" +
               "        .footer { text-align: center; color: #666; font-size: 12px; margin-top: 20px; }\n" +
               "    </style>\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div class='container'>\n" +
               "        <div class='header'>\n" +
               "            <h2>TGlobe Load Links</h2>\n" +
               "        </div>\n" +
               "        <div class='content'>\n" +
               "            <h3>Hello " + user.getFirstName() + "!</h3>\n" +
               "            <p>We received a request to reset your password. Click the button below to create a new password:</p>\n" +
               "            \n" +
               "            <div style='text-align: center;'>\n" +
               "                <a href='" + resetLink + "' class='button'>Reset Password</a>\n" +
               "            </div>\n" +
               "            \n" +
               "            <p><strong>This link will expire in 24 hours.</strong></p>\n" +
               "            \n" +
               "            <p>If you didn't request a password reset, please ignore this email or contact support.</p>\n" +
               "            \n" +
               "            <hr>\n" +
               "            \n" +
               "            <p><small>For security, never share this link with anyone.</small></p>\n" +
               "        </div>\n" +
               "        <div class='footer'>\n" +
               "            <p>&copy; 2026 TGlobe Load Links. All rights reserved.</p>\n" +
               "        </div>\n" +
               "    </div>\n" +
               "</body>\n" +
               "</html>";
    }
    
    @Transactional
    public boolean validateResetToken(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        
        if (tokenOpt.isEmpty()) {
            return false;
        }
        
        PasswordResetToken resetToken = tokenOpt.get();
        
        if (resetToken.isExpired() || resetToken.isUsed()) {
            return false;
        }
        
        return true;
    }
    
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        
        if (tokenOpt.isEmpty()) {
            return false;
        }
        
        PasswordResetToken resetToken = tokenOpt.get();
        
        if (resetToken.isExpired() || resetToken.isUsed()) {
            return false;
        }
        
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
        
        return true;
    }
    
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            return false;
        }
        
        User user = userOpt.get();
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false; // Old password doesn't match
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        return true;
    }
}