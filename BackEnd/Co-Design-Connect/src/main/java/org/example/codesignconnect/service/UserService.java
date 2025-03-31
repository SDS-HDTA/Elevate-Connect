package org.example.codesignconnect.service;

import org.example.codesignconnect.dto.SignupRequest;
import org.example.codesignconnect.model.Result;

public interface UserService {
    Result login(String email, String password);

    Result signup(SignupRequest request);

    Result generateCode(String email, Short type);

    Result resetPassword(String email, String verificationCode, String newPassword);
}
