package com.example.taskflow.comment;

import com.example.taskflow.comment.dto.CreateCommentRequest;
import com.example.taskflow.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public Comment addComment(@Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        Long currentUserId = 1L;
        return commentService.addComment(request.taskId(), currentUserId, request.content());
    }

    @GetMapping("/task/{taskId}")
    public List<Comment> getComments(@PathVariable Long taskId) {
        return commentService.getCommentsForTask(taskId);
    }
}
