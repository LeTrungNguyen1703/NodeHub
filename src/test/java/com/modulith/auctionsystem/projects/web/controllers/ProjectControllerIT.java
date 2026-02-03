package com.modulith.auctionsystem.projects.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modulith.auctionsystem.BaseIntegrationTest;
import com.modulith.auctionsystem.projects.shared.dto.AddProjectMemberRequest;
import com.modulith.auctionsystem.projects.shared.dto.CreateProjectRequest;
import com.modulith.auctionsystem.projects.shared.dto.UpdateProjectRequest;
import com.modulith.auctionsystem.projects.shared.public_api.ProjectPublicApi;
import com.modulith.auctionsystem.users.shared.dto.UserResponse;
import com.modulith.auctionsystem.users.shared.public_api.UserPublicApi;
import com.modulith.auctionsystem.util.KeycloakTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("projectService")
    private ProjectPublicApi projectPublicApi;

    @MockitoBean
    @Qualifier("userService")
    private UserPublicApi userPublicApi;

    private final String userId = KeycloakTestUtils.KEYCLOAK_ID;
    private final String otherUserId = "other-user-id";

    private CreateProjectRequest defaultCreateRequest;

    private final String[] ROLES = new String[] { "client_admin", "client_user" };

    @BeforeEach
    void globalSetup() {
        defaultCreateRequest = new CreateProjectRequest(
                "Test Project",
                "Description",
                LocalDate.now(),
                LocalDate.now().plusDays(10));

    }

    @Nested
    class ProjectCRUDTests {

        @BeforeEach
        void setup() {
            var userResponse = new UserResponse(
                    userId,
                    "testuser@gmail.com",
                    "test",
                    "Test User",
                    null,
                    null);
            when(userPublicApi.findByUserId(userId)).thenReturn(userResponse);

            var otherUserResponse = new UserResponse(
                    otherUserId,
                    "otheruser@gmail.com",
                    "other",
                    "Other User",
                    null,
                    null);
            when(userPublicApi.findByUserId(otherUserId)).thenReturn(otherUserResponse);
        }

        @Test
        @DisplayName("Should create a new project successfully")
        void shouldCreateProject() throws Exception {
            mockMvc.perform(post("/api/v1/projects")
                    .with(KeycloakTestUtils.getMockJwt(ROLES))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(defaultCreateRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Project created successfully"))
                    .andExpect(jsonPath("$.data.name").value("Test Project"))
                    .andExpect(jsonPath("$.data.projectId").exists());
        }

        @Test
        @DisplayName("Should get project by ID successfully")
        void shouldGetProjectById() throws Exception {
            Integer projectId = createProjectAndGetId(defaultCreateRequest, userId);

            mockMvc.perform(get("/api/v1/projects/{projectId}", projectId)
                    .with(KeycloakTestUtils.getMockJwt("client_user")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.projectId").value(projectId))
                    .andExpect(jsonPath("$.data.name").value("Test Project"));
        }

        @Test
        @DisplayName("Should update project successfully")
        void shouldUpdateProject() throws Exception {
            Integer projectId = createProjectAndGetId(defaultCreateRequest, userId);

            UpdateProjectRequest updateRequest = new UpdateProjectRequest(
                    "Updated Project Name",
                    "Updated Description",
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(20));

            mockMvc.perform(put("/api/v1/projects/{projectId}", projectId)
                    .with(KeycloakTestUtils.getMockJwt(ROLES))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.name").value("Updated Project Name"))
                    .andExpect(jsonPath("$.data.description").value("Updated Description"));
        }

        @Test
        @DisplayName("Should delete project successfully")
        void shouldDeleteProject() throws Exception {
            Integer projectId = createProjectAndGetId(defaultCreateRequest, userId);

            mockMvc.perform(delete("/api/v1/projects/{projectId}", projectId)
                    .with(KeycloakTestUtils.getMockJwt(ROLES)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));

            // Verify it's deleted (should return 404 or empty? Service throws
            // ProjectNotFoundException)
            // But getProject checks deletedAt.
            mockMvc.perform(get("/api/v1/projects/{projectId}", projectId)
                    .with(KeycloakTestUtils.getMockJwt(ROLES)))
                    .andExpect(status().isNotFound()); // Assuming GlobalExceptionHandler maps ProjectNotFoundException
                                                       // to 404
        }

        @Test
        @DisplayName("Should add member to project successfully")
        void shouldAddMemberToProject() throws Exception {
            Integer projectId = createProjectAndGetId(defaultCreateRequest, userId);

            AddProjectMemberRequest addMemberRequest = new AddProjectMemberRequest(otherUserId);

            mockMvc.perform(post("/api/v1/projects/{projectId}/members", projectId)
                    .with(KeycloakTestUtils.getMockJwt(ROLES))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addMemberRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Member added successfully"));
        }

        @Test
        @DisplayName("Should remove member from project successfully")
        void shouldRemoveMemberFromProject() throws Exception {
            Integer projectId = createProjectAndGetId(defaultCreateRequest, userId);

            // Add member first
            AddProjectMemberRequest addMemberRequest = new AddProjectMemberRequest(otherUserId);
            mockMvc.perform(post("/api/v1/projects/{projectId}/members", projectId)
                    .with(KeycloakTestUtils.getMockJwt(ROLES))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addMemberRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Member added successfully"));

            // Now remove the member
            mockMvc.perform(delete("/api/v1/projects/{projectId}/members/{userId}", projectId, otherUserId)
                    .with(KeycloakTestUtils.getMockJwt(ROLES)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Member removed successfully"));
        }
    }

    @Nested
    class ProjectSecurityTests {

        @Test
        @DisplayName("Should return unauthorized when creating project without auth")
        void shouldUnauthorizedCreateProject() throws Exception {
            mockMvc.perform(post("/api/v1/projects")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(defaultCreateRequest)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return forbidden when user who is not project owner or admin tries to add member")
        void shouldForbiddenAddMemberNotOwnerOrAdmin() throws Exception {
            Integer projectId = createProjectAndGetId(defaultCreateRequest, "test-owner-id");

            AddProjectMemberRequest addMemberRequest = new AddProjectMemberRequest("some-other-user-id");

            mockMvc.perform(post("/api/v1/projects/{projectId}/members", projectId)
                    .with(KeycloakTestUtils.getMockJwt("client_user")) // Not an admin or owner
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addMemberRequest)))
                    .andExpect(status().isForbidden());

        }

        @Test
        @DisplayName("Should return forbidden when user who is not project member or admin tries to get specific project")
        void shouldForbiddenGetProjectNotMemberOrAdmin() throws Exception {
            Integer projectId = createProjectAndGetId(defaultCreateRequest, "test-owner-id");
            mockMvc.perform(get("/api/v1/projects/{projectId}", projectId)
                    .with(KeycloakTestUtils.getMockJwt("client_user"))) // Not an admin or member
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return project not found")
        void shouldProjectNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/projects/{projectId}", 9999)
                    .with(KeycloakTestUtils.getMockJwt(ROLES)))
                    .andExpect(status().isNotFound());
        }
    }

    private Integer createProjectAndGetId(CreateProjectRequest request, String ownerId) {
        var response = projectPublicApi.createProject(request, ownerId);
        return response.projectId();
    }
}
