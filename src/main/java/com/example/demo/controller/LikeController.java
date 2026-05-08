package com.example.demo.controller;

import com.example.demo.model.Build;
import com.example.demo.model.Like;
import com.example.demo.model.User;
import com.example.demo.repository.BuildRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private BuildRepository buildRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String email = null;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        return userRepository.findByEmail(email).orElse(null);
    }

    @PostMapping("/{buildId}/toggle")
    public ResponseEntity<?> toggleLike(@PathVariable Long buildId) {
        User currentUser = getCurrentUser();

        if (currentUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Please login first"));
        }

        Build build = buildRepository.findById(buildId).orElse(null);
        if (build == null) {
            return ResponseEntity.notFound().build();
        }

        boolean liked = likeRepository.existsByBuildAndUser(build, currentUser);

        if (liked) {
            likeRepository.deleteByBuildAndUser(build, currentUser);
            build.setLikesCount(build.getLikesCount() - 1);
            buildRepository.save(build);
            return ResponseEntity.ok(Map.of(
                    "liked", false,
                    "likesCount", build.getLikesCount()
            ));
        } else {
            Like like = new Like();
            like.setBuild(build);
            like.setUser(currentUser);
            likeRepository.save(like);
            build.setLikesCount(build.getLikesCount() + 1);
            buildRepository.save(build);
            return ResponseEntity.ok(Map.of(
                    "liked", true,
                    "likesCount", build.getLikesCount()
            ));
        }
    }

    @GetMapping("/{buildId}/status")
    public ResponseEntity<?> getLikeStatus(@PathVariable Long buildId) {
        User currentUser = getCurrentUser();
        Build build = buildRepository.findById(buildId).orElse(null);

        if (build == null) {
            return ResponseEntity.notFound().build();
        }

        boolean liked = currentUser != null && likeRepository.existsByBuildAndUser(build, currentUser);

        return ResponseEntity.ok(Map.of(
                "liked", liked,
                "likesCount", build.getLikesCount()
        ));
    }
}