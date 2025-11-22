package com.example.taskflow.task;

import com.example.taskflow.activity.ActivityService;
import com.example.taskflow.project.Project;
import com.example.taskflow.project.ProjectRepository;
import com.example.taskflow.user.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private ActivityService activityService;
    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskRepository = mock(TaskRepository.class);
        projectRepository = mock(ProjectRepository.class);
        userRepository = mock(UserRepository.class);
        activityService = mock(ActivityService.class);

        taskService = new TaskService(taskRepository, projectRepository, userRepository, activityService);
    }

    @Test
    void createTask_shouldRejectPastDueDate() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        assertThatThrownBy(() ->
                taskService.createTask(1L, "Title", "Desc", yesterday, null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createTask_shouldPersistTaskAndRecordActivity() {
        Project project = Project.builder().id(1L).name("P1").build();
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(10L);
            return t;
        });

        Task task = taskService.createTask(1L, "Title", "Desc", null, null);

        assertThat(task.getId()).isEqualTo(10L);
        verify(activityService).recordTaskCreated(task);
    }
}
