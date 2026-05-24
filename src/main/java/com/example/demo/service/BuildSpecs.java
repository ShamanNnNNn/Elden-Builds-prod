package com.example.demo.service;

import com.example.demo.model.Build;
import com.example.demo.model.User;
import org.springframework.data.jpa.domain.Specification;

public class BuildSpecs {

    public static Specification<Build> byOwner(User owner) {
        return (root, q, cb) -> cb.equal(root.get("owner"), owner);
    }

    public static Specification<Build> nameContains(String search) {
        return (root, q, cb) -> search == null ? null
                : cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%");
    }

    public static Specification<Build> byDamageCategory(Build.BuildDamageCategory damage) {
        return (root, q, cb) -> damage == null ? null
                : cb.equal(root.get("damageCategory"), damage);
    }

    public static Specification<Build> byWeaponClass(Build.BuildWeaponClass weaponClass) {
        return (root, q, cb) -> weaponClass == null ? null
                : cb.equal(root.get("weaponClass"), weaponClass);
    }

    public static Specification<Build> byStatus(Build.BuildStatus status) {
        return (root, q, cb) -> status == null ? null
                : cb.equal(root.get("status"), status);
    }
}