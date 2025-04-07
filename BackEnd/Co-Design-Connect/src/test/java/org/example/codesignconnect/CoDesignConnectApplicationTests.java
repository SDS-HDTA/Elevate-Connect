package org.example.codesignconnect;

import org.example.codesignconnect.mapper.UserMapper;
import org.example.codesignconnect.model.User;
import org.example.codesignconnect.model.Project;
import org.example.codesignconnect.service.ProjectService;
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
    @Autowired
    private ProjectService projectService;

    @Test
    public void testAddProject() {
        Project project = new Project();
        project.setName("测试项目");
        project.setDescription("这是一个测试插入的项目");
        project.setCategory("测试");
        project.setCreatorId(1);  // 确保数据库里有 ID=1 的用户
        project.setStatus(0);
        project.setImageUrl("");
        project.setChannelId(1);
        project.setTags("单元测试");

        int rows = projectService.add(project);
        System.out.println("影响行数: " + rows);
        assert rows > 0; // 简单断言：必须成功插入
    }

}
