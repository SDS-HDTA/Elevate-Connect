package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.dto.UserDetail;
import org.sds.elevateconnect.dto.auth.AuthenticationResponse;
import org.sds.elevateconnect.dto.auth.CheckCodeRequest;
import org.sds.elevateconnect.dto.auth.ConfirmPasswordCodeRequest;
import org.sds.elevateconnect.dto.auth.LoginRequest;
import org.sds.elevateconnect.dto.auth.SignupRequest;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.model.auth.UserRole;

import java.util.List;

public interface IUserService {
    AuthenticationResponse login(LoginRequest request);
    AuthenticationResponse signup(SignupRequest request);
    Result resetPassword(Integer userId, String newPassword);
    Result getUserInfo(Integer userId);
    String getFullNameById(Integer userId);
    List<UserDetail> getAllUsers();
    void deleteUser(Integer id);
    UserRole getUserRoleById(Integer id);
    void updateUserById(Integer id, String email, String firstName, String lastName, String phone);
    Result checkCode(CheckCodeRequest request);
    Result confirmPasswordCode(ConfirmPasswordCodeRequest request);
}
