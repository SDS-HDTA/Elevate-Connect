package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sds.elevateconnect.model.InviteCode;
import org.sds.elevateconnect.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findAllUsers();

    User findByEmail(String email);

    InviteCode checkCode(String code);

    void addUser(User user);

    void inactivateCode(InviteCode inviteCode);

    void addCode(InviteCode inviteCode);

    void deleteVerificationCode(String email);

    void saveVerificationCode(String email, String code, LocalDateTime expireTime);

    String findVerificationCode(String email);

    void updatePassword(String email, String newPassword);

    User getUserById(Integer userId);

    String getUsernameById(Integer userId);

    List<User> selectAllUsers();

    void deleteUser(Integer id);
}
