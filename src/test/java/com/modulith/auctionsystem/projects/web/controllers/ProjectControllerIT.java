package com.modulith.auctionsystem.projects.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modulith.auctionsystem.BaseIntegrationTest;
import com.modulith.auctionsystem.projects.shared.dto.AddProjectMemberRequest;
import com.modulith.auctionsystem.projects.shared.dto.CreateProjectRequest;
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
import java.util.Map;

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

    @Autowired
    @Qualifier("userService")
    @MockitoBean
    private UserPublicApi userPublicApi;

    private final String userId = KeycloakTestUtils.KEYCLOAK_ID;

    private CreateProjectRequest defaultCreateRequest;

    private final String[] ROLES = new String[]{"client_admin", "client_user"};

    @BeforeEach
    void globalSetup() {
        defaultCreateRequest = new CreateProjectRequest(
                "Test Project",
                "Description",
                LocalDate.now(),
                LocalDate.now().plusDays(10)
        );

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
                    null,
                    null,
                    null,
                    null,
                    0L
            );
            when(userPublicApi.findByUserId(userId)).thenReturn(userResponse);
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
        @DisplayName("Should add member to project successfully")
        void shouldAddMemberToProject() throws Exception {
            Integer projectId = createProjectAndGetId(defaultCreateRequest);

            AddProjectMemberRequest addMemberRequest = new AddProjectMemberRequest(userId);

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
            Integer projectId = createProjectAndGetId(defaultCreateRequest);

            // Add member first
            AddProjectMemberRequest addMemberRequest = new AddProjectMemberRequest(userId);
            mockMvc.perform(post("/api/v1/projects/{projectId}/members", projectId)
                            .with(KeycloakTestUtils.getMockJwt(ROLES))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(addMemberRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Member added successfully"));

            // Now remove the member
            mockMvc.perform(delete("/api/v1/projects/{projectId}/members/{userId}", projectId, userId)
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
            Integer projectId = createProjectAndGetId(defaultCreateRequest);

            AddProjectMemberRequest addMemberRequest = new AddProjectMemberRequest("some-other-user-id");

            mockMvc.perform(post("/api/v1/projects/{projectId}/members", projectId)
                            .with(KeycloakTestUtils.getMockJwt("client_user")) // Not an admin or owner
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(addMemberRequest)))
                    .andExpect(status().isForbidden());

        }
    }

    private Integer createProjectAndGetId(CreateProjectRequest request) {
        var response = projectPublicApi.createProject(request);
        return response.projectId();
    }
}
