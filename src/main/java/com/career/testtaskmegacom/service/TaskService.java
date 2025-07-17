package com.career.testtaskmegacom.service;

import com.career.testtaskmegacom.dto.SimpleResponse;
import com.career.testtaskmegacom.dto.Task.TaskRequest;
import com.career.testtaskmegacom.dto.Task.TaskResponse;
import com.career.testtaskmegacom.dto.Task.TaskResponseAll;
import com.career.testtaskmegacom.model.UserInfo;
import com.career.testtaskmegacom.util.enums.Priority;
import com.career.testtaskmegacom.util.enums.Status;

import java.util.List;

public interface TaskService {
    SimpleResponse createTask(TaskRequest request, Priority priority);

    TaskResponse getTaskById(Long id);

    List<TaskResponseAll> getAllTasks(UserInfo userInfo);

    SimpleResponse updateTask(Long id, TaskRequest request, Priority priority);

    SimpleResponse deleteTask(Long id);

    SimpleResponse changeStatus(Long taskId, Status status);
}