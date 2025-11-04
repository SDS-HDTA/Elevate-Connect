package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sds.elevateconnect.model.auth.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {
    void addUser(User user);
    void updatePassword(Integer id, String newPassword);
    void deleteUser(Integer id);
    List<User> getAllUsers();
    User getUserByEmail(String email);
    User getUserById(Integer userId);
    String getFullNameById(Integer userId);
    void saveVerificationCode(String email, String code, LocalDateTime expireTime);
    void deleteVerificationCode(String email);
    String getVerificationCode(String email);
    Integer getUserRoleById(Integer id);
    void updateUserById(Integer id, String email, String firstName, String lastName, String phone);
}
