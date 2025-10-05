package org.sds.elevateconnect;

import java.time.LocalDate;
import org.sds.elevateconnect.dto.CreateProjectRequest;
import org.sds.elevateconnect.dto.ProjectResponse;
import org.sds.elevateconnect.mapper.UserMapper;
import org.sds.elevateconnect.model.PageResult;
import org.sds.elevateconnect.model.auth.User;
import org.sds.elevateconnect.model.project.Project;
import org.sds.elevateconnect.model.project.ProjectStage;
import org.sds.elevateconnect.model.project.ProjectCategory;
import org.sds.elevateconnect.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
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
        // Use timestamp to ensure unique project name
        String uniqueName = "Test Project " + System.currentTimeMillis();
        CreateProjectRequest createRequest = new CreateProjectRequest(
            1, // creatorId
            1, // communityId
            uniqueName, // name
            "This is a test project insertion", // description
            0, // category (ProjectCategory.CATEGORY0)
            "2024-12-31" // targetDate
        );

        projectService.createProject(createRequest);
        System.out.println("Created project successfully");
    }

    @Test
    public void testListAllProjectsWithPaginationAndSearch() {
        int page = 1;
        int size = 10;
        Integer searchType = 0;
        String searchValue = "Test";

        PageResult<ProjectResponse> result = projectService.getPaginatedListOfAllProjects(page, size, searchType, searchValue);

        assert result != null;
        System.out.println("Total projects: " + result.getTotal());
        System.out.println("Projects returned:");
        result.getRecords().forEach(System.out::println);

        if (result.getRecords() != null) {
            for (ProjectResponse pr : result.getRecords()) {
                Project p = pr.getProject();
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
        project.setCategory(ProjectCategory.EDUCATION);
        project.setProjectImageId(2);
        project.setCommunityId(1);
        project.setCurrentStage(ProjectStage.DEFINE);
        // Set a valid target date instead of leaving it null
        project.setTargetDate(LocalDate.of(2025, 12, 31));

        try {
            projectService.update(project);
            System.out.println("Project updated successfully");
        } catch (Exception e) {
            System.err.println("Failed to update project: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testDeleteProject() {
        int projectId = 1;
        
        try {
            projectService.deleteProject(projectId);
            System.out.println("Project deleted successfully");
        } catch (Exception e) {
            System.err.println("Failed to delete project: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testSearchProjects() {
        List<ProjectResponse> results = projectService.searchProjects("AI", null, 1, 1);
        results.forEach(System.out::println);
    }

    @Test
    public void testGetMyProjects() {
        Integer userId = 1; // Replace with userId from database
        Integer searchType = null; // No specific search type
        String searchValue = null; // No search filter

        List<ProjectResponse> myProjects = projectService.searchMyProjects(userId, searchType, searchValue);
        System.out.println("Projects joined by user " + userId + ": " + myProjects.size());
        myProjects.forEach(System.out::println);

    }

    @Test
    public void testAddMemberToProject() {
        // Use an existing project ID from the available projects
        Integer projectId = 2; // Smart Traffic Management project which exists in the database
        Integer userId = 3;    // Use user ID 3 who is not currently a member

        try {
            // First try to remove the user if they're already a member (to make test repeatable)
            try {
                projectService.removeMemberFromProject(projectId, userId);
                System.out.println("User " + userId + " was removed from project " + projectId + " to prepare for the test");
            } catch (Exception e) {
                // Ignore error if user wasn't a member already
                System.out.println("User " + userId + " was not a member of project " + projectId + " (expected for first run)");
            }
            
            // Now try to add the user
            projectService.joinProject(projectId, userId);
            System.out.println("User " + userId + " was successfully added to project " + projectId);
        } catch (Exception e) {
            System.err.println("Failed to add user to project: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testExitProject() {
        Integer projectId = 1; // Replace with actual existing project ID
        Integer userId = 2;    // Replace with actual existing user ID who has joined this project

        try {
            projectService.removeMemberFromProject(projectId, userId);
            System.out.println("User " + userId + " exited project " + projectId + " successfully.");
        } catch (Exception e) {
            System.err.println("Failed to remove user from project: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testCreateProject() {
        // Use timestamp to ensure unique project name
        String uniqueName = "Unit Test Project " + System.currentTimeMillis();
        CreateProjectRequest createRequest = new CreateProjectRequest(
            1, // creatorId
            1, // communityId
            uniqueName, // name
            "This is a project created during unit testing.", // description
            2, // category (ProjectCategory.CATEGORY2)
            "2024-12-31" // targetDate
        );

        projectService.createProject(createRequest);

        System.out.println("New Project created successfully");
    }

    @Test
    public void trueTestDeleteProject() {
        Integer projectId = 5;
        
        try {
            projectService.deleteProject(projectId);
            System.out.println("Project " + projectId + " deleted successfully.");
        } catch (Exception e) {
            System.err.println("Failed to delete project " + projectId + ": " + e.getMessage());
            throw e;
        }
    }
}
