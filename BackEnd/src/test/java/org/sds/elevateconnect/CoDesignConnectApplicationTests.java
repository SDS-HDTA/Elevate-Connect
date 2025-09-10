package org.sds.elevateconnect;

import org.sds.elevateconnect.mapper.UserMapper;
import org.sds.elevateconnect.model.PageResult;
import org.sds.elevateconnect.model.User;
import org.sds.elevateconnect.model.Project;
import org.sds.elevateconnect.service.ProjectService;
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
        List<User> userList = userMapper.getAllUsers();
        userList.forEach(System.out::println);
    }
    @Autowired
    private ProjectService projectService;

    @Test
    public void testAddProject() {
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("This is a test project insertion");
        project.setCategory("Test");
        project.setCreatorId(1);
        project.setStatus(0);
        project.setImageUrl("");
        project.setChannelId(1);
        project.setTags("Unit Test");

        int rows = projectService.add(project);
        System.out.println("Affected rows: " + rows);
        assert rows > 0;
    }

    @Test
    public void testListAllProjectsWithPaginationAndSearch() {
        int page = 1;
        int size = 10;
        Integer searchType = 0;
        String searchValue = "Test";

        PageResult<Project> result = projectService.listAllProjects(page, size, searchType, searchValue);

        assert result != null;
        System.out.println("Total projects: " + result.getTotal());
        System.out.println("Projects returned:");
        result.getRecords().forEach(System.out::println);

        if (result.getRecords() != null) {
            for (Project p : result.getRecords()) {
                assert p.getName().toLowerCase().contains(searchValue.toLowerCase())
                        || p.getCategory().toLowerCase().contains(searchValue.toLowerCase())
                        || (p.getTags() != null && p.getTags().toLowerCase().contains(searchValue.toLowerCase()));
            }
        }
    }

    @Test
    public void testUpdateProject() {
        Project project = new Project();
        project.setId(1);
        project.setName("Updated Project Name");
        project.setDescription("Modified Description");
        project.setCategory("Updated Category");
        project.setImageUrl("https://example.com/image.jpg");
        project.setChannelId(1);
        project.setTags("Updated,Test");
        project.setStatus(1);

        int rows = projectService.update(project);
        System.out.println("Update affected rows: " + rows);
        assert rows > 0;
    }

    @Test
    public void testDeleteProject() {
        int projectId = 1;
        int rows = projectService.delete(projectId);
        System.out.println("Delete affected rows: " + rows);
        assert rows > 0;
    }

    @Test
    public void testSearchProjects() {
        List<Project> results = projectService.searchProjects("AI", null, 1, 1);
        results.forEach(System.out::println);
    }

    @Test
    public void testGetMyProjects() {
        Integer userId = 1; // Replace with userId from database

        List<Project> myProjects = projectService.getProjectsByUserId(userId);
        System.out.println("Projects joined by user " + userId + ": " + myProjects.size());
        myProjects.forEach(System.out::println);

    }

    @Test
    public void testAddMemberToProject() {
        Integer projectId = 1; // Replace with actual existing project ID
        Integer userId = 2;    // Replace with actual existing user ID who hasn't joined this project yet

        boolean success = projectService.addMemberToProject(projectId, userId);

        if (success) {
            System.out.println("User " + userId + " was successfully added to project " + projectId);
        } else {
            System.out.println("User " + userId + " is already a member of project " + projectId);
        }

        assert success;
    }

    @Test
    public void testExitProject() {
        Integer projectId = 1; // Replace with actual existing project ID
        Integer userId = 2;    // Replace with actual existing user ID who has joined this project

        boolean success = projectService.exitProject(projectId, userId);

        if (success) {
            System.out.println("User " + userId + " exited project " + projectId + " successfully.");
        } else {
            System.out.println("User " + userId + " failed to exit project " + projectId + ".");
        }

        assert success;
    }

    @Test
    public void testCreateProject() {
        Project project = new Project();
        project.setName("Unit Test Project");
        project.setDescription("This is a project created during unit testing.");
        project.setCategory("Testing");
        project.setStatus(0);
        project.setImageUrl("https://example.com/test.png");
        project.setChannelId(1);
        project.setTags("test,unit");

        Project newProject = projectService.createProject(project);

        System.out.println("New Project: " + newProject);
        assert newProject != null;
    }

    @Test
    public void trueTestDeleteProject() {
        Integer projectId = 5;
        Integer ownerUserId = 2;
        boolean success = projectService.deleteProject(projectId, ownerUserId);

        if (success) {
            System.out.println("Project " + projectId + " deleted successfully.");
        } else {
            System.out.println("Failed to delete project " + projectId + ".");
        }

        assert success;
    }
}
