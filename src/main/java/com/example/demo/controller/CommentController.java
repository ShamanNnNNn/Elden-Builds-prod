package com.example.demo.controller;

import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.model.User;
import com.example.demo.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/builds/{buildId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long buildId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal User currentUser) {

        CommentResponse response = commentService.createComment(buildId, request, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<CommentResponse>> getComments(
            @PathVariable Long buildId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "date") String sortBy) {

        Page<CommentResponse> comments = commentService.getCommentsByBuild(buildId, page, size, sortBy);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User currentUser) {

        commentService.deleteComment(commentId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal User currentUser) {

        CommentResponse response = commentService.updateComment(commentId, request, currentUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User currentUser) {

        commentService.likeComment(commentId, currentUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<Void> unlikeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User currentUser) {

        commentService.unlikeComment(commentId, currentUser);
        return ResponseEntity.ok().build();
    }
}