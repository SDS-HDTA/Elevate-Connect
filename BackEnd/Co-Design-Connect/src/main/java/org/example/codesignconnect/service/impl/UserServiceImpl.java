package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.service.UserService;
import org.example.codesignconnect.mapper.UserMapper;
import org.example.codesignconnect.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean comparePassword(String loginInfo, String password) {
        User user = userMapper.findByUsernameOrEmail(loginInfo);
        return user.getPassword().equals(password);
    }
}
