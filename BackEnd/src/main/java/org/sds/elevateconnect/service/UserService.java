package org.sds.elevateconnect.service;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.dto.SignupRequest;
import org.sds.elevateconnect.dto.UserDetail;
import org.sds.elevateconnect.exceptions.UserException;
import org.sds.elevateconnect.model.InviteCode;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.mapper.UserMapper;
import org.sds.elevateconnect.model.User;
import org.sds.elevateconnect.model.UserRole;
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

        if (inviteCode == null || !inviteCode.getEmail().equals(request.getEmail()))
        {
            return Result.error("Invalid Invite Code");
        } else {
            try {
                User user = new User();

                user.setFirstName(request.getFirstName());
                user.setLastName(request.getLastName());
                user.setEmail(request.getEmail());
                user.setPassword(request.getPassword());

                user.setRole(inviteCode.getUserRole());

                if (inviteCode.getUserRole() == UserRole.COUNTRY_COLLABORATION_PARTNER) {
                    if (inviteCode.getCountry() == null) {
                        return Result.error("Request is missing a country for " + UserRole.COUNTRY_COLLABORATION_PARTNER.getStringValue() + " role");
                    }

                    user.setCountry(inviteCode.getCountry());
                }

                userMapper.addUser(user);
                inviteCodeService.deleteCode(inviteCode);

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
            return Result.success(userMapper.getUserById(userId));
        }
    }

    @Override
    public String getFullNameById(Integer userId) {
        return userMapper.getFullNameById(userId);
    }

    @Override
    public List<UserDetail> getAllUsers() {
        List<User> users = userMapper.getAllUsers();

        return users.stream().map(UserDetail::new).toList();
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }

    @Override
    public UserRole getUserRoleById(Integer id) {
        Integer role = userMapper.getUserRoleById(id);

        if (role == null) {
            throw new UserException("No role found for user.");
        } else {
            return UserRole.fromInt(role);
        }
    }

    @Override
    public void updateUserById(Integer id, String email, String firstName, String lastName) {
        User user = userMapper.getUserById(id);
        if (user == null) {
            log.warn("No user found with ID: {}", id);
            return;
        }

        userMapper.updateUserById(id, email, firstName, lastName);
        return;
    }
}
