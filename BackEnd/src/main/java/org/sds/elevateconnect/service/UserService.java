package org.sds.elevateconnect.service;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.dto.SignupRequest;
import org.sds.elevateconnect.dto.UserDetail;
import org.sds.elevateconnect.model.InviteCode;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.mapper.UserMapper;
import org.sds.elevateconnect.model.User;
import org.sds.elevateconnect.service.interfaces.IUserService;
import org.sds.elevateconnect.config.security.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserService implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private InviteCodeService inviteCodeService;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public Result login(String email, String password, HttpSession session) {
        User user = userMapper.getUserByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            return Result.error("Invalid email or password");
        } else {
            Map<String, Object> claims = new HashMap<>();

            claims.put("id", user.getId());
            claims.put("username", user.getFirstName());

            String jwt = jwtUtils.generateJwt(claims);

            session.setAttribute("user", user.getFirstName());

            return Result.success(Map.of("id", user.getId(), "accessToken", jwt));
        }
    }

    @Override
    public Result signup(SignupRequest request) {
        InviteCode inviteCode = inviteCodeService.getInviteCodeByCode(request.getInviteCode());

        if (inviteCode == null || inviteCode.getIsUsed() || !inviteCode.getEmail().equals(request.getEmail()))
        {
            return Result.error("Invalid Invite Code");
        } else {
            try {
                User user = new User();

                user.setFirstName(request.getFirstName());
                user.setLastName(request.getLastName());
                user.setEmail(request.getEmail());
                user.setPassword(request.getPassword());

                user.setRole(inviteCode.getType());

                userMapper.addUser(user);
                inviteCodeService.deactivateCode(inviteCode);
                return Result.success(new UserDetail(user));
            } catch (Exception e) {
                log.error("e: ", e);
                return Result.error("Error creating new user. Please try again.");
            }
        }
    }

    @Override
    public Result resetPassword(String email, String verificationCode, String newPassword) {
        String code = userMapper.getVerificationCode(email);

        if (code == null || !code.equals(verificationCode)) {
            return Result.error("Invalid Verification Code");
        } else {
            userMapper.updatePassword(email, newPassword);
            userMapper.deleteVerificationCode(email);
            return Result.success();
        }
    }

    @Override
    public Result getUserInfo(Integer userId) {
        User user = userMapper.getUserById(userId);

        if (user == null) {
            return Result.error("No such user");
        } else {
            return Result.success(new UserDetail(userMapper.getUserById(userId)));
        }
    }

    @Override
    public String getFullNameById(Integer userId) {
        return userMapper.getFullNameById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }

    @Override
    public Result getUserRoleById(Integer id) {
        Integer role = userMapper.getUserRoleById(id);

        if (role == null) {
            return Result.error("No role found for user ID: " + id);
        } else {
            return Result.success(role);
        }
    }
}
