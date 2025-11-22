package com.example.taskflow.task;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
           SELECT t FROM Task t
           WHERE (:projectId IS NULL OR t.project.id = :projectId)
             AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId)
             AND (:status IS NULL OR t.status = :status)
           """)
    Page<Task> search(@Param("projectId") Long projectId,
                      @Param("assigneeId") Long assigneeId,
                      @Param("status") TaskStatus status,
                      Pageable pageable);

    long countByProjectIdAndStatusNot(Long projectId, TaskStatus status);
}
