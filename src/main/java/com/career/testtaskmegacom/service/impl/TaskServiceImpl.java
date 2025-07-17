package com.career.testtaskmegacom.service.impl;

import com.career.testtaskmegacom.dto.SimpleResponse;
import com.career.testtaskmegacom.dto.Task.TaskRequest;
import com.career.testtaskmegacom.dto.Task.TaskResponse;
import com.career.testtaskmegacom.dto.Task.TaskResponseAll;
import com.career.testtaskmegacom.exception.ForbiddenException;
import com.career.testtaskmegacom.exception.NotFoundException;
import com.career.testtaskmegacom.model.Task;
import com.career.testtaskmegacom.model.UserInfo;
import com.career.testtaskmegacom.repository.TaskRepository;
import com.career.testtaskmegacom.repository.UserInfoRepository;
import com.career.testtaskmegacom.service.EmailService;
import com.career.testtaskmegacom.service.TaskService;
import com.career.testtaskmegacom.util.enums.Priority;
import com.career.testtaskmegacom.util.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserInfoRepository userInfoRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public SimpleResponse createTask(TaskRequest request, Priority priority) {
        UserInfo creator = getCurrentUser();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(priority);
        task.setStatus(Status.TODO);
        task.setCreator(creator);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);

        String subject = "Вы создали новую задачу";
        String body = String.format("""
                        Здравствуйте, %s!
                        
                        Вы только что создали новую задачу:
                        
                        Название: %s
                        Приоритет: %s
                        Статус: %s
                        Описание: %s
                        
                        Дата создания: %s
                        """,
                creator.getUserInfoUserName(),
                task.getTitle(),
                task.getPriority(),
                task.getStatus(),
                task.getDescription(),
                task.getCreatedAt()
        );

        emailService.sendTaskCreatedEmail(creator.getEmail(), subject, body);

        return new SimpleResponse("Задача успешно создана", HttpStatus.CREATED);
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Задача не найдена"));
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    @Override
    @Cacheable(value = "tasksCache", key = "#userInfo")
    public List<TaskResponseAll> getAllTasks(UserInfo userInfo) {
        List<Task> tasks = taskRepository.findAllByCreatorId(userInfo.getId());

        System.out.println("Кэш по ключу: " + userInfo.getId());

        return tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SimpleResponse updateTask(Long taskId, TaskRequest request, Priority priority) {
        UserInfo user = getCurrentUser();
        Task task = getTaskIfAuthorized(user, taskId);

        if (user.getCreatedTasks().stream().noneMatch(task1 -> task1.getId().equals(task.getId()))) {
            throw new ForbiddenException("У вас нет прав на удаление этой задачи");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(priority);
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
        return new SimpleResponse("Задача успешно обновлена", HttpStatus.OK);
    }

    @Override
    public SimpleResponse deleteTask(Long taskId) {
        UserInfo user = getCurrentUser();
        Task task = getTaskIfAuthorized(user, taskId);

        taskRepository.delete(task);

        return new SimpleResponse("Задача успешно удалена", HttpStatus.OK);
    }

    @Override
    public SimpleResponse changeStatus(Long taskId, Status status) {
        UserInfo admin = getCurrentUser();
        Task task = getTaskIfAuthorized(admin, taskId);

        if (admin.getCreatedTasks().stream().noneMatch(task1 -> task1.getId().equals(task.getId()))) {
            throw new ForbiddenException("У вас нет прав на удаление этой задачи");
        }

        task.setStatus(status);
        taskRepository.save(task);
        return new SimpleResponse("Статус задачи успешно изменен", HttpStatus.OK);
    }

    private TaskResponseAll convertToResponse(Task task) {
        TaskResponseAll response = new TaskResponseAll();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        return response;
    }

    private UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userInfoRepository.getUserAccountByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Task getTaskIfAuthorized(UserInfo user, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Задача не найдена"));

        if (user.getCreatedTasks().stream().noneMatch(task1 -> task1.getId().equals(task.getId()))) {
            throw new ForbiddenException("У вас нет прав на выполнение данной операции с задачей");
        }

        return task;
    }
}