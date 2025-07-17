package com.career.testtaskmegacom.api;

import com.career.testtaskmegacom.dto.SimpleResponse;
import com.career.testtaskmegacom.dto.Task.TaskRequest;
import com.career.testtaskmegacom.dto.Task.TaskResponse;
import com.career.testtaskmegacom.dto.Task.TaskResponseAll;
import com.career.testtaskmegacom.exception.NotFoundException;
import com.career.testtaskmegacom.model.UserInfo;
import com.career.testtaskmegacom.repository.UserInfoRepository;
import com.career.testtaskmegacom.service.TaskService;
import com.career.testtaskmegacom.util.enums.Priority;
import com.career.testtaskmegacom.util.enums.Status;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskApi {

    private final TaskService taskService;
    private final UserInfoRepository userInfoRepository;

    @PostMapping("/create-task")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Создает новую задачу",
            description = "Создает новую задачу на основании предоставленных данных в запросе."
    )
    public SimpleResponse createTask(
            @RequestBody TaskRequest request,
            @RequestParam Priority priority
    ) {
        return taskService.createTask(request, priority);
    }

    @GetMapping("/get-task-by/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Получает задачу по ID",
            description = "Получает задачу по уникальному идентификатору."
    )
    public TaskResponse getTaskById(
            @PathVariable Long id
    ) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/get-all-tasks")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Получает список всех задач",
            description = "Возвращает список всех задач."
    )
    public List<TaskResponseAll> getAllTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserInfo userInfo = userInfoRepository.getUserAccountByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return taskService.getAllTasks(userInfo);
    }

    @PutMapping("/update-task/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Обновляет существующую задачу",
            description = "Обновляет существующую задачу на основании предоставленных данных."
    )
    public SimpleResponse updateTask(
            @PathVariable Long id,
            @RequestBody TaskRequest request,
            @RequestParam Priority priority
    ) {
        return taskService.updateTask(id, request, priority);
    }

    @DeleteMapping("/delete-task/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Удаляет задачу по ID",
            description = "Удаляет задачу по уникальному идентификатору."
    )
    public SimpleResponse deleteTask(
            @PathVariable Long id
    ) {
        return taskService.deleteTask(id);
    }

    @PutMapping("/{taskId}/change-status")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Изменяет статус задачи",
            description = "Изменяет статус задачи."
    )
    public SimpleResponse changeStatus(
            @PathVariable Long taskId,
            @RequestParam Status status
    ) {
        return taskService.changeStatus(taskId, status);
    }
}