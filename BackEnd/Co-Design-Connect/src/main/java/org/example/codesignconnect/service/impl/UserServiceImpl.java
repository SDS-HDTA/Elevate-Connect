package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.dto.SignupRequest;
import org.example.codesignconnect.model.InviteCode;
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
    public Boolean comparePassword(String email, String password) {
        User user = userMapper.findByEmail(email);
        if(user == null) return false;
        else return user.getPassword().equals(password);
    }

    @Override
    public Boolean signup(SignupRequest request) {
        InviteCode inviteCode = userMapper.checkCode(request.getInviteCode());
        if(inviteCode == null || inviteCode.getIsUsed()) return false;
        else {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setType(inviteCode.getType());
            user.setInviteCodeId(inviteCode.getId());
            userMapper.addUser(user);
            userMapper.inactivateCode(inviteCode);
            return true;
        }
    }
}
