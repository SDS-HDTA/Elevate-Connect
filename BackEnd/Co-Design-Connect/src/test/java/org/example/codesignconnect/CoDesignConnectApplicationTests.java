package org.example.codesignconnect;

import org.example.codesignconnect.mapper.UserMapper;
import org.example.codesignconnect.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CoDesignConnectApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void setupTest(){
        List<User> userList = userMapper.findAllUsers();
        userList.forEach(System.out::println);
    }

}
