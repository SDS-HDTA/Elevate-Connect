package org.example.codesignconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.codesignconnect.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
    public List<User> findAllUsers();

    public User findByUsernameOrEmail(String loginInfo);
}
