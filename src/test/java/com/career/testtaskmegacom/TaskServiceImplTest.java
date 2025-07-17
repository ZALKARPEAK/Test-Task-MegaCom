package com.career.testtaskmegacom;

import com.career.testtaskmegacom.dto.SimpleResponse;
import com.career.testtaskmegacom.dto.Task.TaskRequest;
import com.career.testtaskmegacom.dto.Task.TaskResponse;
import com.career.testtaskmegacom.exception.ForbiddenException;
import com.career.testtaskmegacom.exception.NotFoundException;
import com.career.testtaskmegacom.model.Task;
import com.career.testtaskmegacom.model.UserInfo;
import com.career.testtaskmegacom.repository.TaskRepository;
import com.career.testtaskmegacom.repository.UserInfoRepository;
import com.career.testtaskmegacom.service.EmailService;
import com.career.testtaskmegacom.service.impl.TaskServiceImpl;
import com.career.testtaskmegacom.util.enums.Priority;
import com.career.testtaskmegacom.util.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceImplTest {

    TaskServiceImpl taskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private UserInfoRepository userInfoRepository;

    @MockBean
    private EmailService emailService;

    @MockBean
    private Authentication authentication;

    @MockBean
    private SecurityContext securityContext;

    private UserInfo testUser;

    @BeforeEach
    void setup() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        testUser = new UserInfo();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setUserInfoUserName("testuser");

        when(authentication.getName()).thenReturn(testUser.getEmail());
        when(userInfoRepository.getUserAccountByEmail(testUser.getEmail()))
                .thenReturn(Optional.of(testUser));

        taskService = new TaskServiceImpl(taskRepository, userInfoRepository, emailService);
    }

    @Test
    void createTask_shouldSaveTaskAndSendEmail() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Test task");
        request.setDescription("Description");
        Priority priority = Priority.MEDIUM;
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(10L);
            return t;
        });

        var response = taskService.createTask(request, priority);

        verify(taskRepository, times(1)).save(captor.capture());
        verify(emailService, times(1)).sendTaskCreatedEmail(
                eq(testUser.getEmail()),
                anyString(),
                contains("Test task")
        );

        Task savedTask = captor.getValue();
        assertEquals(request.getTitle(), savedTask.getTitle());
        assertEquals(request.getDescription(), savedTask.getDescription());
        assertEquals(priority, savedTask.getPriority());
        assertEquals(testUser, savedTask.getCreator());
        assertEquals("Задача успешно создана", response.getMessageCode());
        assertEquals(201, response.getHttpStatus().value());
    }

    @Test
    void getTaskById_shouldReturnTaskResponse_whenTaskExists() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Title");
        task.setDescription("Desc");
        task.setPriority(Priority.HIGH);
        task.setStatus(Status.TODO);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskById(1L);
        assertEquals(task.getId(), response.getId());
        assertEquals(task.getTitle(), response.getTitle());
        assertEquals(task.getDescription(), response.getDescription());
        assertEquals(task.getPriority(), response.getPriority());
        assertEquals(task.getStatus(), response.getStatus());
    }

    @Test
    void getTaskById_shouldThrowNotFoundException_whenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> taskService.getTaskById(99L));
    }

    @Test
    void updateTask_shouldUpdateTask_whenUserAuthorized() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Updated title");
        request.setDescription("Updated description");
        Priority priority = Priority.LOW;

        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old title");
        existingTask.setDescription("Old description");
        existingTask.setPriority(Priority.HIGH);
        existingTask.setCreator(testUser);
        existingTask.setCreatedAt(LocalDateTime.now().minusDays(1));
        existingTask.setUpdatedAt(LocalDateTime.now().minusDays(1));
        testUser.setCreatedTasks(List.of(existingTask));

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = taskService.updateTask(1L, request, priority);
        assertEquals("Задача успешно обновлена", response.getMessageCode());
        assertEquals(200, response.getHttpStatus().value());
        assertEquals(request.getTitle(), existingTask.getTitle());
        assertEquals(request.getDescription(), existingTask.getDescription());
        assertEquals(priority, existingTask.getPriority());
        assertNotNull(existingTask.getUpdatedAt());
    }

    @Test
    void updateTask_shouldThrowForbiddenException_whenUserNotAuthorized() {
        TaskRequest request = new TaskRequest();
        Priority priority = Priority.LOW;

        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setCreator(new UserInfo());
        testUser.setCreatedTasks(List.of());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        assertThrows(ForbiddenException.class, () -> taskService.updateTask(1L, request, priority));
    }


    @Test
    void deleteTask_TaskAuthorized_ShouldDelete() {
        UserInfo user = new UserInfo();
        user.setId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setCreator(user);

        user.setCreatedTasks(List.of(task));

        when(userInfoRepository.getUserAccountByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        SimpleResponse response = taskService.deleteTask(1L);

        assertEquals("Задача успешно удалена", response.getMessageCode());
        assertEquals(HttpStatus.OK, response.getHttpStatus());

        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_TaskNotAuthorized_ShouldThrowForbidden() {
        UserInfo user = new UserInfo();
        user.setId(1L);
        user.setCreatedTasks(List.of());

        Task task = new Task();
        task.setId(1L);
        task.setCreator(new UserInfo());

        when(userInfoRepository.getUserAccountByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        ForbiddenException thrown = assertThrows(ForbiddenException.class, () -> taskService.deleteTask(1L));
        assertEquals("У вас нет прав на вполнение данной операции с задачей", thrown.getMessage());
    }
}