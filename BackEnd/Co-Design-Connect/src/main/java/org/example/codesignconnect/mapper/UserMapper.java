package org.example.codesignconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.codesignconnect.model.InviteCode;
import org.example.codesignconnect.model.User;

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

    @Select("SELECT username FROM users WHERE id = #{userId}")
    String getUsernameById(@Param("userId") Integer userId);
}
