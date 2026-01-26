package com.modulith.auctionsystem.projects.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modulith.auctionsystem.BaseIntegrationTest;
import com.modulith.auctionsystem.projects.shared.dto.AddProjectMemberRequest;
import com.modulith.auctionsystem.projects.shared.dto.CreateProjectRequest;
import com.modulith.auctionsystem.projects.shared.public_api.ProjectPublicApi;
import com.modulith.auctionsystem.util.KeycloakTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

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

    private CreateProjectRequest defaultCreateRequest;

    private final String[] ROLES = new String[]{"client_admin", "client_user"};

    @BeforeEach
    void setUp() {
        defaultCreateRequest = new CreateProjectRequest(
                "Test Project",
                "Description",
                LocalDate.now(),
                LocalDate.now().plusDays(10)
        );
    }

    @Test
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
    void shouldAddMemberToProject() throws Exception {
        Integer projectId = this.createProjectAndGetId(defaultCreateRequest);
        String userId = "user-123";

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
    void shouldRemoveMemberFromProject() throws Exception {
        Integer projectId = this.createProjectAndGetId(defaultCreateRequest);
        String userId = "user-to-remove";

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

    private Integer createProjectAndGetId(CreateProjectRequest request) {
        var response = projectPublicApi.createProject(request);
        return response.projectId();
    }
}
