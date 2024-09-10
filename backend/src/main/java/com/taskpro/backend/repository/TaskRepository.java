package com.taskpro.backend.repository;

import com.taskpro.backend.entity.Task;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Task> findByIdForUpdate(Long taskId);
    List<Task> findAllByCreatedBy_Id(Long userId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Task> findByIdAndCreatedBy_IdForUpdate(Long taskId, Long userId);
}
