package com.modulith.auctionsystem.tasks.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modulith.auctionsystem.BaseIntegrationTest;
import com.modulith.auctionsystem.tasks.domain.enums.TaskPriority;
import com.modulith.auctionsystem.tasks.domain.enums.TaskStatus;
import com.modulith.auctionsystem.tasks.shared.dto.*;
import com.modulith.auctionsystem.tasks.shared.public_api.KanbanPublicAPI;
import com.modulith.auctionsystem.tasks.shared.public_api.TaskPublicAPI;
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
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class KanbanControllerIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("kanbanService")
    private KanbanPublicAPI kanbanPublicAPI;

    @Autowired
    @Qualifier("projectService")
    private ProjectPublicApi projectPublicApi;

    @Autowired
    @Qualifier("taskService")
    private TaskPublicAPI taskPublicAPI;

    @MockitoBean
    @Qualifier("userService")
    private UserPublicApi userPublicApi;

    private final String userId = KeycloakTestUtils.KEYCLOAK_ID;
    private final String otherUserId = "other-user-id";

    private CreateKanbanRequest defaultCreateRequest;
    private Integer testProjectId;

    private final String[] ROLES = new String[]{"client_admin", "client_user"};

    @BeforeEach
    void globalSetup() {
        // Setup user mocks
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

        // Create a mock project
        var projectRequest = new CreateProjectRequest(
                "Test Project",
                "Project for Kanban tests",
                LocalDate.now(),
                LocalDate.now().plusDays(30));
        testProjectId = projectPublicApi.createProject(projectRequest, userId).projectId();

        defaultCreateRequest = new CreateKanbanRequest(
                testProjectId,
                "Test Kanban Board",
                "Main development board");
    }

    @Nested
    class KanbanCRUDTests {

        @Test
        @DisplayName("Should create a new Kanban board successfully")
        void shouldCreateKanbanBoard() throws Exception {
            mockMvc.perform(post("/api/v1/kanbans")
                            .with(KeycloakTestUtils.getMockJwt(ROLES))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(defaultCreateRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Kanban board created successfully"))
                    .andExpect(jsonPath("$.data.name").value("Test Kanban Board"))
                    .andExpect(jsonPath("$.data.kanbanId").exists())
                    .andExpect(jsonPath("$.data.projectId").value(testProjectId));
        }

        @Test
        @DisplayName("Should get Kanban board by ID successfully")
        void shouldGetKanbanBoardById() throws Exception {
            Integer kanbanId = createKanbanBoardAndGetId(defaultCreateRequest, userId);

            mockMvc.perform(get("/api/v1/kanbans/{kanbanId}", kanbanId)
                            .with(KeycloakTestUtils.getMockJwt("client_user")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.kanbanId").value(kanbanId))
                    .andExpect(jsonPath("$.data.name").value("Test Kanban Board"))
                    .andExpect(jsonPath("$.data.projectId").value(testProjectId));
        }

        @Test
        @DisplayName("Should get all Kanban boards successfully")
        void shouldGetAllKanbanBoards() throws Exception {
            createKanbanBoardAndGetId(defaultCreateRequest, userId);

            mockMvc.perform(get("/api/v1/kanbans")
                            .with(KeycloakTestUtils.getMockJwt("client_user")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.content").isArray());
        }

        @Test
        @DisplayName("Should get Kanban boards by project ID successfully")
        void shouldGetKanbanBoardsByProjectId() throws Exception {
            createKanbanBoardAndGetId(defaultCreateRequest, userId);

            mockMvc.perform(get("/api/v1/kanbans/project/{projectId}", testProjectId)
                            .with(KeycloakTestUtils.getMockJwt("client_user")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.content").isArray());
        }

        @Test
        @DisplayName("Should update Kanban board successfully")
        void shouldUpdateKanbanBoard() throws Exception {
            Integer kanbanId = createKanbanBoardAndGetId(defaultCreateRequest, userId);

            UpdateKanbanRequest updateRequest = new UpdateKanbanRequest(
                    "Updated Kanban Board Name",
                    "Updated Description");

            mockMvc.perform(put("/api/v1/kanbans/{kanbanId}", kanbanId)
                            .with(KeycloakTestUtils.getMockJwt(ROLES))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.name").value("Updated Kanban Board Name"))
                    .andExpect(jsonPath("$.data.description").value("Updated Description"));
        }

        @Test
        @DisplayName("Should delete Kanban board successfully")
        void shouldDeleteKanbanBoard() throws Exception {
            Integer kanbanId = createKanbanBoardAndGetId(defaultCreateRequest, userId);

            mockMvc.perform(delete("/api/v1/kanbans/{kanbanId}", kanbanId)
                            .with(KeycloakTestUtils.getMockJwt(ROLES)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Kanban board deleted successfully"));

            // Verify it's deleted (should return 404)
            mockMvc.perform(get("/api/v1/kanbans/{kanbanId}", kanbanId)
                            .with(KeycloakTestUtils.getMockJwt(ROLES)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class KanbanTasksTests {

//        @Test
//        @DisplayName("Should get all tasks for a kanban board")
//        void shouldGetTasksByKanbanBoard() throws Exception {
//            // Create kanban board
//            Integer kanbanId = createKanbanBoardAndGetId(defaultCreateRequest, userId);
//
//            // Create tasks linked to the kanban board
//            CreateTaskRequest task1 = new CreateTaskRequest(
//                    testProjectId,
//                    "Task 1",
//                    "First task",
//                    LocalDateTime.now(),
//                    LocalDateTime.now().plusDays(5),
//                    TaskStatus.TO_DO,
//                    TaskPriority.HIGH,
//                    userId,
//                    kanbanId);
//
//            CreateTaskRequest task2 = new CreateTaskRequest(
//                    testProjectId,
//                    "Task 2",
//                    "Second task",
//                    LocalDateTime.now(),
//                    LocalDateTime.now().plusDays(10),
//                    TaskStatus.IN_PROGRESS,
//                    TaskPriority.MEDIUM,
//                    userId,
//                    kanbanId);
//
//            taskPublicAPI.createTask(testProjectId, task1, userId);
//            taskPublicAPI.createTask(testProjectId, task2, userId);
//
//            // Get tasks by kanban board
//            mockMvc.perform(get("/api/v1/kanbans/{kanbanId}/tasks", kanbanId)
//                            .with(KeycloakTestUtils.getMockJwt(ROLES)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.message").value("Tasks retrieved successfully"))
//                    .andExpect(jsonPath("$.data.content", hasSize(2)))
//                    .andExpect(jsonPath("$.data.content[0].title").value("Task 1"))
//                    .andExpect(jsonPath("$.data.content[1].title").value("Task 2"));
//        }

        @Test
        @DisplayName("Should return empty list for kanban board with no tasks")
        void shouldReturnEmptyListForKanbanBoardWithNoTasks() throws Exception {
            // Create kanban board without tasks
            Integer kanbanId = createKanbanBoardAndGetId(defaultCreateRequest, userId);

            mockMvc.perform(get("/api/v1/kanbans/{kanbanId}/tasks", kanbanId)
                            .with(KeycloakTestUtils.getMockJwt(ROLES)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.content", hasSize(0)));
        }

        @Test
        @DisplayName("Should return 404 when getting tasks for non-existent kanban board")
        void shouldReturn404ForNonExistentKanbanBoard() throws Exception {
            mockMvc.perform(get("/api/v1/kanbans/{kanbanId}/tasks", 99999)
                            .with(KeycloakTestUtils.getMockJwt(ROLES)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return unauthorized when getting tasks without auth")
        void shouldReturnUnauthorizedWhenGettingTasksWithoutAuth() throws Exception {
            Integer kanbanId = createKanbanBoardAndGetId(defaultCreateRequest, userId);

            mockMvc.perform(get("/api/v1/kanbans/{kanbanId}/tasks", kanbanId))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class KanbanValidationTests {

        @Test
        @DisplayName("Should fail to create Kanban board with invalid data")
        void shouldFailCreateKanbanBoardWithInvalidData() throws Exception {
            CreateKanbanRequest invalidRequest = new CreateKanbanRequest(
                    null,  // Invalid: null project ID
                    "",    // Invalid: empty name
                    "Description");

            mockMvc.perform(post("/api/v1/kanbans")
                            .with(KeycloakTestUtils.getMockJwt(ROLES))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should fail to create Kanban board with name exceeding max length")
        void shouldFailCreateKanbanBoardWithLongName() throws Exception {
            String longName = "a".repeat(101); // Exceeds 100 character limit
            CreateKanbanRequest invalidRequest = new CreateKanbanRequest(
                    testProjectId,
                    longName,
                    "Description");

            mockMvc.perform(post("/api/v1/kanbans")
                            .with(KeycloakTestUtils.getMockJwt(ROLES))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class KanbanSecurityTests {

        @Test
        @DisplayName("Should return unauthorized when creating Kanban board without auth")
        void shouldUnauthorizedCreateKanbanBoard() throws Exception {
            mockMvc.perform(post("/api/v1/kanbans")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(defaultCreateRequest)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return unauthorized when getting Kanban board without auth")
        void shouldUnauthorizedGetKanbanBoard() throws Exception {
            Integer kanbanId = createKanbanBoardAndGetId(defaultCreateRequest, userId);

            mockMvc.perform(get("/api/v1/kanbans/{kanbanId}", kanbanId))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return not found for non-existent Kanban board")
        void shouldKanbanBoardNotFound() throws Exception {
            mockMvc.perform(get("/api/v1/kanbans/{kanbanId}", 99999)
                            .with(KeycloakTestUtils.getMockJwt(ROLES)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return forbidden when non-member tries to get Kanban boards by project")
        void shouldForbiddenGetKanbanBoardsByProjectNotMember() throws Exception {
            // Create a project with different owner
            var otherProjectRequest = new CreateProjectRequest(
                    "Other Project",
                    "Project owned by other user",
                    LocalDate.now(),
                    LocalDate.now().plusDays(30));
            Integer otherProjectId = projectPublicApi.createProject(otherProjectRequest, otherUserId).projectId();

            mockMvc.perform(get("/api/v1/kanbans/project/{projectId}", otherProjectId)
                            .with(KeycloakTestUtils.getMockJwt("client_user"))) // Not a member
                    .andExpect(status().isForbidden());
        }
    }

    private Integer createKanbanBoardAndGetId(CreateKanbanRequest request, String ownerId) {
        KanbanResponse response = kanbanPublicAPI.createKanbanBoard(request, ownerId);
        return response.kanbanId();
    }
}


