package org.sds.elevateconnect;

import org.sds.elevateconnect.mapper.UserMapper;
import org.sds.elevateconnect.model.PageResult;
import org.sds.elevateconnect.model.User;
import org.sds.elevateconnect.model.project.Project;
import org.sds.elevateconnect.model.project.ProjectStage;
import org.sds.elevateconnect.model.project.ProjectCategory;
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
    private ProjectService ProjectService;

    @Test
    public void testAddProject() {
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("This is a test project insertion");
        project.setCategory(ProjectCategory.CATEGORY0);
        project.setCreatorId(1);
        project.setCurrentStage(ProjectStage.EMPATHISE);
        project.setProjectImageId(1);
        project.setCommunityId(1);

        int rows = ProjectService.create(project);
        System.out.println("Affected rows: " + rows);
        assert rows > 0;
    }

    @Test
    public void testListAllProjectsWithPaginationAndSearch() {
        int page = 1;
        int size = 10;
        Integer searchType = 0;
        String searchValue = "Test";

        PageResult<Project> result = ProjectService.getPaginatedListOfAllProjects(page, size, searchType, searchValue);

        assert result != null;
        System.out.println("Total projects: " + result.getTotal());
        System.out.println("Projects returned:");
        result.getRecords().forEach(System.out::println);

        if (result.getRecords() != null) {
            for (Project p : result.getRecords()) {
                assert p.getName().toLowerCase().contains(searchValue.toLowerCase())
                        || p.getCategory().getStringValue().toLowerCase().contains(searchValue.toLowerCase())
                        || p.getDescription().toLowerCase().contains(searchValue.toLowerCase());
            }
        }
    }

    @Test
    public void testUpdateProject() {
        Project project = new Project();
        project.setId(1);
        project.setName("Updated Project Name");
        project.setDescription("Modified Description");
        project.setCategory(ProjectCategory.CATEGORY1);
        project.setProjectImageId(2);
        project.setCommunityId(1);
        project.setCurrentStage(ProjectStage.DEFINE);

        int rows = ProjectService.update(project);
        System.out.println("Update affected rows: " + rows);
        assert rows > 0;
    }

    @Test
    public void testDeleteProject() {
        int projectId = 1;
        int rows = ProjectService.delete(projectId);
        System.out.println("Delete affected rows: " + rows);
        assert rows > 0;
    }

    @Test
    public void testSearchProjects() {
        List<Project> results = ProjectService.searchProjects("AI", null, 1, 1);
        results.forEach(System.out::println);
    }

    @Test
    public void testGetMyProjects() {
        Integer userId = 1; // Replace with userId from database

        List<Project> myProjects = ProjectService.searchMyProjects(userId);
        System.out.println("Projects joined by user " + userId + ": " + myProjects.size());
        myProjects.forEach(System.out::println);

    }

    @Test
    public void testAddMemberToProject() {
        Integer projectId = 1; // Replace with actual existing project ID
        Integer userId = 2;    // Replace with actual existing user ID who hasn't joined this project yet

        boolean success = ProjectService.addMemberToProject(projectId, userId);

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

        boolean success = ProjectService.exitProject(projectId, userId);

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
        project.setCategory(ProjectCategory.CATEGORY2);
        project.setCurrentStage(ProjectStage.EMPATHISE);
        project.setProjectImageId(3);
        project.setCommunityId(1);

        Project newProject = ProjectService.createProject(project);

        System.out.println("New Project: " + newProject);
        assert newProject != null;
    }

    @Test
    public void trueTestDeleteProject() {
        Integer projectId = 5;
        Integer ownerUserId = 2;
        boolean success = ProjectService.deleteProject(projectId, ownerUserId);

        if (success) {
            System.out.println("Project " + projectId + " deleted successfully.");
        } else {
            System.out.println("Failed to delete project " + projectId + ".");
        }

        assert success;
    }
}
