package org.sds.elevateconnect.service;

import org.sds.elevateconnect.mapper.UserMapper;
import org.sds.elevateconnect.model.InviteCode;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.model.auth.User;
import org.sds.elevateconnect.model.auth.UserRole;
import org.sds.elevateconnect.service.interfaces.IEmailService;
import org.sds.elevateconnect.utils.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService implements IEmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserMapper userMapper;

    private static final int LENGTH = 6;
    private static final int EXPIRATION_MINUTES = 10;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public Result sendVerificationCode(String email) {
        String code = CodeGenerator.generateVerificationCode(LENGTH);
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);

        User user = userMapper.getUserByEmail(email); // Check if user exists

        if (user == null) {
            return Result.error("User not found");
        }

        userMapper.deleteVerificationCode(user.getId());
        userMapper.saveVerificationCode(email, code, expireTime);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject("Elevate Connect - Verification code for password reset");
        message.setText("You are trying to reset your password. Your verification code is: " + code +
                ". The code is valid for " + EXPIRATION_MINUTES + " minutes.");
        javaMailSender.send(message);

        return Result.success();
    }

    @Override
    public void sendInviteCode(InviteCode inviteCode) {
        String email = inviteCode.getEmail();
        String code = inviteCode.getCode();
        UserRole role = inviteCode.getUserRole();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject("Your Invitation Code for Elevate Connect Platform Access");
        message.setText("""
        Dear Partner,

        You have been invited to join our platform as a %s.

        Please use the following invitation code to complete your registration:

        Invitation Code: %s

        If you have any questions or did not request this invitation, please contact us directly.

        Best regards,
        Elevate Connect Team.
        """.formatted(role.getStringValue(), code));
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
