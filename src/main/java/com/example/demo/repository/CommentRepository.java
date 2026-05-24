package com.example.demo.repository;

import com.example.demo.model.Build;
import com.example.demo.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByBuildOrderByCreatedAtDesc(Build build, Pageable pageable);

    Page<Comment> findByBuildOrderByLikesCountDesc(Build build, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.build.id = :buildId")
    long countByBuildId(@Param("buildId") Long buildId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.build.id = :buildId")
    void deleteByBuildId(@Param("buildId") Long buildId);
}