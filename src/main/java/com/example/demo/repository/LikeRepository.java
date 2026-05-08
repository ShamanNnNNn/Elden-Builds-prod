package com.example.demo.repository;

import com.example.demo.model.Build;
import com.example.demo.model.Like;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByBuildAndUser(Build build, User user);

    boolean existsByBuildAndUser(Build build, User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Like l WHERE l.build = :build AND l.user = :user")
    void deleteByBuildAndUser(Build build, User user);

    int countByBuild(Build build);
}