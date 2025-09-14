package org.sds.elevateconnect.service.interfaces;

import jakarta.servlet.http.HttpSession;
import org.sds.elevateconnect.dto.SignupRequest;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.model.User;

import java.util.List;

public interface IUserService {
    Result login(String email, String password, HttpSession session);
    Result signup(SignupRequest request);
    Result resetPassword(String email, String verificationCode, String newPassword);
    Result getUserInfo(Integer userId);
    String getFullNameById(Integer userId);
    List<User> getAllUsers();
    void deleteUser(Integer id);
    Result getUserRoleById(Integer id);
}
