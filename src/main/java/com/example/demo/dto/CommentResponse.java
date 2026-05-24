package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private Long buildId;
    private CommentAuthor author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likesCount;
    private boolean likedByCurrentUser;

    @Data
    public static class CommentAuthor {
        private Long id;
        private String email;
    }
}