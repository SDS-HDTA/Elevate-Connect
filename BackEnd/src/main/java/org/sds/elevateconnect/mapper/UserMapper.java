package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sds.elevateconnect.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {
    void addUser(User user);
    void updatePassword(String email, String newPassword);
    void deleteUser(Integer id);
    List<User> getAllUsers();
    User getUserByEmail(String email);
    User getUserById(Integer userId);
    String getUsernameById(Integer userId);
    void saveVerificationCode(String email, String code, LocalDateTime expireTime);
    void deleteVerificationCode(String email);
    String getVerificationCode(String email);
}
