package com.career.testtaskmegacom.repository;

import com.career.testtaskmegacom.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByCreatorId(Long creatorId);
}