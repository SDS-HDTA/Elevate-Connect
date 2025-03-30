package org.example.codesignconnect.service;

import org.example.codesignconnect.dto.SignupRequest;

public interface UserService {
    Boolean comparePassword(String email, String password);

    Boolean signup(SignupRequest request);
}
