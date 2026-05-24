package com.example.demo.controller;

import com.example.demo.dto.BuildRequest;
import com.example.demo.dto.BuildResponse;
import com.example.demo.model.Build;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.service.BuildService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/builds")
public class BuildController {

    private final BuildService buildService;

    public BuildController(BuildService buildService) {
        this.buildService = buildService;
    }

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    public ResponseEntity<List<BuildResponse>> getApprovedBuilds() {
        System.out.println("=== getApprovedBuilds called ===");
        List<Build> builds = buildService.getApprovedBuilds();  // ← ИСПРАВЛЕНО
        List<BuildResponse> responses = builds.stream()
                .map(this::toResponse)
                .toList();
        System.out.println("Approved builds count: " + responses.size());
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BuildResponse> getBuildById(@PathVariable Long id) {
        Build build = buildService.getBuildById(id)
                .orElseThrow(() -> new RuntimeException("Build not found"));
        return ResponseEntity.ok(toResponse(build));
    }
    @GetMapping("/my")
    public ResponseEntity<List<BuildResponse>> getMyBuilds(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Build.BuildDamageCategory damage,
            @RequestParam(required = false) Build.BuildWeaponClass weaponClass,
            @RequestParam(required = false) Build.BuildStatus status,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        List<Build> builds = buildService.getMyBuilds(user, search, damage, weaponClass, status, sortBy, sortDir);
        return ResponseEntity.ok(builds.stream().map(this::toResponse).toList());
    }


    @PostMapping
    public ResponseEntity<BuildResponse> createBuild(@Valid @RequestBody BuildRequest request, @AuthenticationPrincipal User user) {
        Build build = toEntity(request);
        Build created = buildService.createBuild(build, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }
    @PutMapping("/{id}")
    public ResponseEntity<BuildResponse> updateBuild(@PathVariable Long id,
                                                     @Valid @RequestBody BuildRequest request,
                                                     @AuthenticationPrincipal User user) {
        System.out.println("=== UPDATE BUILD ===");
        System.out.println("Guide content from request: " + request.getGuideContent());

        Build build = toEntity(request);
        build.setId(id);
        Build updated = buildService.updateBuild(build, user);
        return ResponseEntity.ok(toResponse(updated));
    }
    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user) {
        try {
            if (user == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            File serverFile = new File(uploadDir + filename);
            file.transferTo(serverFile);

            String imageUrl = "/uploads/" + filename;
            return ResponseEntity.ok(Map.of("url", imageUrl));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BuildResponse>> getPendingBuilds() {
        List<Build> builds = buildService.getPendingBuilds();
        return ResponseEntity.ok(builds.stream().map(this::toResponse).toList());
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BuildResponse> approveBuild(@PathVariable Long id) {
        Build approved = buildService.approveBuild(id);
        return ResponseEntity.ok(toResponse(approved));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectBuild(@PathVariable Long id) {
        buildService.rejectBuild(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuild(@PathVariable Long id, @AuthenticationPrincipal User user) {
        buildService.deleteBuild(id, user);
        return ResponseEntity.noContent().build();
    }

    private BuildResponse toResponse(Build build) {
        BuildResponse response = new BuildResponse();
        response.setId(build.getId());
        response.setName(build.getName());
        response.setDescription(build.getDescription());
        response.setPreview(build.getPreview());
        response.setHelmet(build.getHelmet());
        response.setArmor(build.getArmor());
        response.setTrousers(build.getTrousers());
        response.setGloves(build.getGloves());
        response.setLeft(build.getLeft());
        response.setRight(build.getRight());
        response.setRightAsh(build.getRightAsh());
        response.setLeftAsh(build.getLeftAsh());
        response.setGuideContent(build.getGuideContent());
        response.setStatus(build.getStatus() != null ? build.getStatus().toString() : "PENDING");
        response.setDamageCategory(build.getDamageCategory() != null ? build.getDamageCategory().toString() : "NOT");
        response.setWeaponClass(build.getWeaponClass() != null ? build.getWeaponClass().toString() : "NOT");
        response.setBuildScalingLevel(build.getBuildScalingLevel() != null ? build.getBuildScalingLevel().toString() : "NOT");
        response.setStrengthScaling(build.getStrengthScaling() != null ? build.getStrengthScaling().toString() : "NOT");
        response.setDexterityScaling(build.getDexterityScaling() != null ? build.getDexterityScaling().toString() : "NOT");
        response.setIntelligenceScaling(build.getIntelligenceScaling() != null ? build.getIntelligenceScaling().toString() : "NOT");
        response.setFaithScaling(build.getFaithScaling() != null ? build.getFaithScaling().toString() : "NOT");
        response.setArcaneScaling(build.getArcaneScaling() != null ? build.getArcaneScaling().toString() : "NOT");
        response.setLikesCount(build.getLikesCount());
        long commentsCount = commentRepository.countByBuildId(build.getId());
        response.setCommentsCount(commentsCount);
        if (build.getOwner() != null) {
            response.setOwner(build.getOwner());
        }
        if (build.getCreatedAt() != null) {
            response.setCreatedAt(build.getCreatedAt().toString());
        }
        return response;
    }

    private Build toEntity(BuildRequest request) {
        Build build = new Build();
        build.setName(request.getName());
        build.setDescription(request.getDescription());
        build.setPreview(request.getPreview());
        build.setHelmet(request.getHelmet());
        build.setArmor(request.getArmor());
        build.setTrousers(request.getTrousers());
        build.setGloves(request.getGloves());
        build.setLeft(request.getLeft());
        build.setRight(request.getRight());
        build.setLeftAsh(request.getLeftAsh());
        build.setRightAsh(request.getRightAsh());
        build.setGuideContent(request.getGuideContent());
        build.setStrengthScaling(getScalingLevel(request.getStrengthScaling()));
        build.setDexterityScaling(getScalingLevel(request.getDexterityScaling()));
        build.setIntelligenceScaling(getScalingLevel(request.getIntelligenceScaling()));
        build.setFaithScaling(getScalingLevel(request.getFaithScaling()));
        build.setArcaneScaling(getScalingLevel(request.getArcaneScaling()));

        if (request.getDamageCategory() != null && !request.getDamageCategory().isEmpty()) {
            try {
                build.setDamageCategory(Build.BuildDamageCategory.valueOf(request.getDamageCategory()));
            } catch (IllegalArgumentException e) {
                build.setDamageCategory(Build.BuildDamageCategory.NOT);
            }
        } else {
            build.setDamageCategory(Build.BuildDamageCategory.NOT);
        }

        if (request.getWeaponClass() != null && !request.getWeaponClass().isEmpty()) {
            try {
                build.setWeaponClass(Build.BuildWeaponClass.valueOf(request.getWeaponClass()));
            } catch (IllegalArgumentException e) {
                build.setWeaponClass(Build.BuildWeaponClass.NOT);
            }
        } else {
            build.setWeaponClass(Build.BuildWeaponClass.NOT);
        }

        if (request.getBuildScalingLevel() != null && !request.getBuildScalingLevel().isEmpty()) {
            try {
                build.setBuildScalingLevel(Build.BuildScalingLevel.valueOf(request.getBuildScalingLevel()));
            } catch (IllegalArgumentException e) {
                build.setBuildScalingLevel(Build.BuildScalingLevel.NOT);
            }
        } else {
            build.setBuildScalingLevel(Build.BuildScalingLevel.NOT);
        }

        return build;
    }
    private Build.BuildScalingLevel getScalingLevel(String value) {
        if (value == null || value.isEmpty()) return Build.BuildScalingLevel.NOT;
        try {
            return Build.BuildScalingLevel.valueOf(value);
        } catch (IllegalArgumentException e) {
            return Build.BuildScalingLevel.NOT;
        }
    }
    private BuildResponse toResponseWithLikeStatus(Build build, User currentUser) {
        BuildResponse response = toResponse(build);
        if (currentUser != null) {
            boolean liked = likeRepository.existsByBuildAndUser(build, currentUser);
            response.setLikedByCurrentUser(liked);
        }
        return response;
    }
}