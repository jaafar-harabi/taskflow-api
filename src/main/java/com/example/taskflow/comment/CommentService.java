package com.example.taskflow.comment;

import com.example.taskflow.task.Task;
import com.example.taskflow.task.TaskRepository;
import com.example.taskflow.user.User;
import com.example.taskflow.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment addComment(Long taskId, Long authorId, String content) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Comment comment = Comment.builder()
                .task(task)
                .author(author)
                .content(content)
                .createdAt(Instant.now())
                .build();

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsForTask(Long taskId) {
        return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId);
    }
}
