package com.example.demo.service;

import com.example.demo.controller.OrderNotFoundExeption;
import com.example.demo.model.Build;
import com.example.demo.model.User;
import com.example.demo.repository.BuildRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuildServiceImpl implements BuildService {
    private final BuildRepository buildRepository;

    public BuildServiceImpl(BuildRepository buildRepository) {
        this.buildRepository = buildRepository;
    }

    @Override
    public List<Build> getApprovedBuilds() {
        return buildRepository.findByStatus(Build.BuildStatus.APPROVED);
    }

    @Override
    public Build createBuild(Build build, User user) {
        build.setOwner(user);
        return buildRepository.save(build);
    }

    @Override
    public List<Build> getBuildsByUser(User user) {
        Pageable pageable = Pageable.unpaged();
        Page<Build> page = buildRepository.findAllByOwner(user, pageable);
        return page.getContent();
    }

    @Override
    public Optional<Build> getBuildByIdAndOwner(Long id, User user) {
        if (id == null || user == null) {
            return Optional.empty();
        }
        return buildRepository.findByIdAndOwner(id, user);
    }
    @Override
    public Optional<Build> getBuildById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return buildRepository.findById(id);
    }

    public List<Build> getMyBuilds(User user, String search,
                                   Build.BuildDamageCategory damage,
                                   Build.BuildWeaponClass weaponClass,
                                   Build.BuildStatus status,
                                   String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Specification<Build> spec = Specification
                .where(BuildSpecs.byOwner(user))
                .and(BuildSpecs.nameContains(search))
                .and(BuildSpecs.byDamageCategory(damage))
                .and(BuildSpecs.byWeaponClass(weaponClass))
                .and(BuildSpecs.byStatus(status));

        return buildRepository.findAll(spec, sort);
    }

    @Override
    public List<Build> getPendingBuilds() {
        return buildRepository.findByStatus(Build.BuildStatus.PENDING);
    }

    @Override
    public List<Build> getAllBuilds() {
        List<Build> build = buildRepository.findAll();
        return build;
    }

    @Override
    public Page<Build> getAllBuilds(Pageable pageable, User user) {
        return buildRepository.findAllByOwner(user, pageable);
    }

    @Override
    public Build approveBuild(Long id) {
        Build build = buildRepository.findById(id).orElseThrow(() -> new RuntimeException("Build not found"));

        build.setStatus(Build.BuildStatus.APPROVED);

        return buildRepository.save(build);
    }

    @Override
    public void rejectBuild(Long id) {
        Build build = buildRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Build not found"));
        buildRepository.delete(build);
    }

    @Override
    public Build updateBuild(Build build, User user) {
        Build existingBuild = buildRepository.findByIdAndOwner(build.getId(), user)
                .orElseThrow(() -> new OrderNotFoundExeption(build.getId()));

        if (build.getName() != null) {
            existingBuild.setName(build.getName());
        }
        if (build.getDescription() != null) {
            existingBuild.setDescription(build.getDescription());
        }
        if (build.getPreview() != null) {
            existingBuild.setPreview(build.getPreview());
        }
        if (build.getHelmet() != null) {
            existingBuild.setHelmet(build.getHelmet());
        }
        if (build.getArmor() != null) {
            existingBuild.setArmor(build.getArmor());
        }
        if (build.getTrousers() != null) {
            existingBuild.setTrousers(build.getTrousers());
        }
        if (build.getGloves() != null) {
            existingBuild.setGloves(build.getGloves());
        }
        if (build.getLeft() != null) {
            existingBuild.setLeft(build.getLeft());
        }
        if (build.getRight() != null) {
            existingBuild.setRight(build.getRight());
        }
        if (build.getLeftAsh() != null) {
            existingBuild.setLeftAsh(build.getLeftAsh());
        }
        if (build.getRightAsh() != null) {
            existingBuild.setRightAsh(build.getRightAsh());
        }
        if (build.getDamageCategory() != null) {
            existingBuild.setDamageCategory(build.getDamageCategory());
        }
        if (build.getGuideContent() != null) {
            existingBuild.setGuideContent(build.getGuideContent());
        }

        return buildRepository.save(existingBuild);
    }

    @Override
    public void deleteBuild(Long id, User user) {
        Build build = buildRepository.findByIdAndOwner(id, user)
                .orElseThrow(() -> new OrderNotFoundExeption(id));
        boolean isAdmin = user.getRole() == User.Role.ADMIN;
        boolean isOwner = build.getOwner().getId().equals(user.getId());

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Not your build");
        }
        buildRepository.delete(build);
    }
}