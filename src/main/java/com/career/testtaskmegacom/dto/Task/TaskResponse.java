package com.career.testtaskmegacom.dto.Task;

import com.career.testtaskmegacom.util.enums.Priority;
import com.career.testtaskmegacom.util.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskResponse {
    Long id;
    String title;
    String description;
    Status status;
    Priority priority;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}