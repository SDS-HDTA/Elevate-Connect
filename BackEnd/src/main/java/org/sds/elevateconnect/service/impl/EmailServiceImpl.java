package org.sds.elevateconnect.service.impl;

import org.sds.elevateconnect.mapper.UserMapper;
import org.sds.elevateconnect.model.InviteCode;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.model.UserRole;
import org.sds.elevateconnect.service.EmailService;
import org.sds.elevateconnect.utils.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailServiceImpl implements EmailService {
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
        userMapper.deleteVerificationCode(email);
        userMapper.saveVerificationCode(email, code, expireTime);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject("Verification code for password reset");
        message.setText("You are trying to reset your password. Your verification code is: " + code +
                ". The code is valid for " + EXPIRATION_MINUTES + " minutes.");
        javaMailSender.send(message);
        return Result.success();
    }

    @Override
    public void sendInviteCode(InviteCode inviteCode) {
        String email = inviteCode.getEmail();
        String code = inviteCode.getCode();
        UserRole type = inviteCode.getType();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject("Your Invitation Code for Co-Design Connect Platform Access");
        message.setText("""
        Dear Partner,

        You have been invited to join our platform as a %s.

        Please use the following invitation code to complete your registration:

        Invitation Code: %s

        If you have any questions or did not request this invitation, please contact us directly.

        Best regards,
        Develop Team
        """.formatted(type.getStringValue(), code));
        javaMailSender.send(message);
    }
}
