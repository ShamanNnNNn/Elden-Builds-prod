package com.example.demo.service;

import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.model.Build;
import com.example.demo.model.Comment;
import com.example.demo.model.User;
import com.example.demo.repository.BuildRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BuildRepository buildRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse createComment(Long buildId, CommentRequest request, User currentUser) {
        Build build = buildRepository.findById(buildId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Build not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setBuild(build);
        comment.setAuthor(currentUser);

        Comment savedComment = commentRepository.save(comment);

        return toResponse(savedComment, currentUser);
    }

    public Page<CommentResponse> getCommentsByBuild(Long buildId, int page, int size, String sortBy) {
        Build build = buildRepository.findById(buildId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Build not found"));

        Sort sort = sortBy.equals("likes")
                ? Sort.by(Sort.Direction.DESC, "likesCount")
                : Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, size, sort);

        return commentRepository.findByBuildOrderByCreatedAtDesc(build, pageable)
                .map(comment -> toResponse(comment, null));
    }

    @Transactional
    public void deleteComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!comment.getAuthor().getId().equals(currentUser.getId()) &&
                !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest request, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only edit your own comments");
        }

        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return toResponse(updatedComment, currentUser);
    }

    @Transactional
    public void likeComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));


        comment.setLikesCount(comment.getLikesCount() + 1);
        commentRepository.save(comment);
    }

    @Transactional
    public void unlikeComment(Long commentId, User currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (comment.getLikesCount() > 0) {
            comment.setLikesCount(comment.getLikesCount() - 1);
            commentRepository.save(comment);
        }
    }

    private CommentResponse toResponse(Comment comment, User currentUser) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setBuildId(comment.getBuild().getId());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        response.setLikesCount(comment.getLikesCount());

        CommentResponse.CommentAuthor author = new CommentResponse.CommentAuthor();
        author.setId(comment.getAuthor().getId());
        author.setEmail(comment.getAuthor().getEmail());
        response.setAuthor(author);

        if (currentUser != null) {
            response.setLikedByCurrentUser(false);
        }

        return response;
    }
}