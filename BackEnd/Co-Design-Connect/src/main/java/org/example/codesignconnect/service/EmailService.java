package org.example.codesignconnect.service;

import org.example.codesignconnect.model.Result;

public interface EmailService {
    Result sendVerificationCode(String email);
}
