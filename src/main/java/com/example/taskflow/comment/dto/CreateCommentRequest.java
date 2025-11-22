package com.example.taskflow.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCommentRequest(
        @NotNull Long taskId,
        @NotBlank String content
) {}
