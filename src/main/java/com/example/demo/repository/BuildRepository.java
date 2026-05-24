package com.example.demo.repository;

import com.example.demo.model.Build;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuildRepository extends JpaRepository<Build, Long> {
    Optional<Build> findByIdAndOwner(Long id, User owner);
    Optional<Build> findById(Long id);

    List<Build> findByNameContainingIgnoreCase(String name);
    Optional<Build> findByName(String name);

    Page<Build> getBuildsByOwner(User owner, Pageable pageable);
    List<Build> findAll();
    List<Build> findByStatus(Build.BuildStatus status);
    Page<Build> findAllByOwner(User owner, Pageable pageable);

    List<Build> findAll(Specification<Build> spec, Sort sort);
    //Page<Order> findByStatusAndPriorityAndOwner(User user, Pageable pageable);

}