package org.example.codesignconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.codesignconnect.model.InviteCode;
import org.example.codesignconnect.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findAllUsers();

    User findByEmail(String email);

    InviteCode checkCode(String code);

    void addUser(User user);

    void inactivateCode(InviteCode inviteCode);
}
