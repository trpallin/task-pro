package com.taskpro.backend.repository;

import com.taskpro.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByCreatedBy_Id(Long userId);
    Optional<Task> findByIdAndCreatedBy_Id(Long taskId, Long userId);
}
