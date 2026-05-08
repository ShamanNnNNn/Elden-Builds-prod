package com.example.demo.service;

import com.example.demo.model.Build;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface BuildService {
    List<Build> getApprovedBuilds();  // ← ИСПРАВЛЕНО
    Build createBuild(Build build, User user);
    List<Build> getBuildsByUser(User user);
    Optional<Build> getBuildByIdAndOwner(Long id, User user);
    Optional<Build> getBuildById(Long id);

    List<Build> getPendingBuilds();
    List<Build> getAllBuilds();
    Page<Build> getAllBuilds(Pageable pageable, User user);
    Build approveBuild(Long id);
    void rejectBuild(Long id);
    Build updateBuild(Build task, User user);

    void deleteBuild(Long id, User user);
}
