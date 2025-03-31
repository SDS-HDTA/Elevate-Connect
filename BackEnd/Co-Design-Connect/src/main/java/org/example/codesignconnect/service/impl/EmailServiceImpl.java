package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.mapper.UserMapper;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.EmailService;
import org.example.codesignconnect.utils.CodeGenerator;
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
        message.setText("Your verification code: " + code + ", expires in " + EXPIRATION_MINUTES + " minutes.");
        javaMailSender.send(message);
        return Result.success();
    }
}
