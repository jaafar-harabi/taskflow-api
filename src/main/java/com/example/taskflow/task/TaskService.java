package com.example.taskflow.task;

import com.example.taskflow.activity.ActivityService;
import com.example.taskflow.project.Project;
import com.example.taskflow.project.ProjectRepository;
import com.example.taskflow.user.User;
import com.example.taskflow.user.UserRepository;
import com.example.taskflow.user.UserRole;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;

    @Transactional
    public Task createTask(Long projectId,
                           String title,
                           String description,
                           LocalDate dueDate,
                           Long assigneeId) {

        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        User assignee = null;
        if (assigneeId != null) {
            assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
        }

        Task task = Task.builder()
                .project(project)
                .assignee(assignee)
                .title(title)
                .description(description)
                .status(TaskStatus.TODO)
                .dueDate(dueDate)
                .createdAt(Instant.now())
                .build();

        taskRepository.save(task);
        activityService.recordTaskCreated(task);
        return task;
    }

    @Transactional
    public Task changeStatus(Long taskId, TaskStatus newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (task.getStatus() == TaskStatus.DONE && newStatus != TaskStatus.DONE) {
            throw new IllegalStateException("Cannot move task back from DONE");
        }

        TaskStatus oldStatus = task.getStatus();
        task.setStatus(newStatus);
        activityService.recordTaskStatusChange(task, oldStatus, newStatus);
        return task;
    }

    public Page<Task> searchTasks(Long projectId,
                                  Long assigneeId,
                                  TaskStatus status,
                                  Pageable pageable) {
        return taskRepository.search(projectId, assigneeId, status, pageable);
    }

    @Transactional
    public Task reassignTask(Long taskId, Long newAssigneeId, User actingUser) {
        if (actingUser.getRole() != UserRole.ADMIN && actingUser.getRole() != UserRole.MANAGER) {
            throw new SecurityException("Only MANAGER or ADMIN can reassign tasks");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        User newAssignee = userRepository.findById(newAssigneeId)
                .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));

        task.setAssignee(newAssignee);
        activityService.recordTaskReassigned(task, actingUser, newAssignee);
        return task;
    }
}
