package com.example.taskflow.activity;

import com.example.taskflow.task.Task;
import com.example.taskflow.task.TaskStatus;
import com.example.taskflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public void recordTaskCreated(Task task) {
        Activity activity = Activity.builder()
                .entityType("TASK")
                .entityId(task.getId())
                .action("CREATED")
                .details("Task created with title: " + task.getTitle())
                .createdAt(Instant.now())
                .build();
        activityRepository.save(activity);
    }

    public void recordTaskStatusChange(Task task, TaskStatus oldStatus, TaskStatus newStatus) {
        Activity activity = Activity.builder()
                .entityType("TASK")
                .entityId(task.getId())
                .action("STATUS_CHANGED")
                .details("Status changed from " + oldStatus + " to " + newStatus)
                .createdAt(Instant.now())
                .build();
        activityRepository.save(activity);
    }

    public void recordTaskReassigned(Task task, User actingUser, User newAssignee) {
        Activity activity = Activity.builder()
                .entityType("TASK")
                .entityId(task.getId())
                .action("REASSIGNED")
                .actor(actingUser)
                .details("Task reassigned to " + newAssignee.getEmail())
                .createdAt(Instant.now())
                .build();
        activityRepository.save(activity);
    }
}
