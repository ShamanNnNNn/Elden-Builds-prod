package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "builds")
@Data
@NoArgsConstructor
public class Build {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "preview")
    private String preview;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "helmet")
    private String helmet;
    @Column(name = "armor")
    private String armor;
    @Column(name = "trousers")
    private String trousers;
    @Column(name = "gloves")
    private String gloves;
    @Column(name = "left_hand")
    private String left;
    @Column(name = "right_hand")
    private String right;
    @Column(name = "right_ash")
    private String rightAsh;
    @Column(name = "left_ash")
    private String leftAsh;
    @Column(columnDefinition = "TEXT")
    private String guideContent;
    @Enumerated(EnumType.STRING)
    private BuildScalingLevel strengthScaling = BuildScalingLevel.NOT;

    @Enumerated(EnumType.STRING)
    private BuildScalingLevel dexterityScaling = BuildScalingLevel.NOT;

    @Enumerated(EnumType.STRING)
    private BuildScalingLevel intelligenceScaling = BuildScalingLevel.NOT;

    @Enumerated(EnumType.STRING)
    private BuildScalingLevel faithScaling = BuildScalingLevel.NOT;

    @Enumerated(EnumType.STRING)
    private BuildScalingLevel arcaneScaling = BuildScalingLevel.NOT;

    public String getGuideContent() {
        return guideContent;
    }

    public void setGuideContent(String guideContent) {
        this.guideContent = guideContent;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    private BuildStatus status = BuildStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private BuildDamageCategory damageCategory = BuildDamageCategory.NOT;

    @Enumerated(EnumType.STRING)
    private BuildWeaponClass weaponClass = BuildWeaponClass.NOT;

    @Enumerated(EnumType.STRING)
    private BuildScalingLevel buildScalingLevel = BuildScalingLevel.NOT;

    @OneToMany(mappedBy = "build", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();

    @Column(name = "likes_count")
    private Integer likesCount = 0;

    public Set<Like> getLikes() { return likes; }
    public void setLikes(Set<Like> likes) { this.likes = likes; }
    public int getLikesCount() {
        return likesCount != null ? likesCount : 0;
    }

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public enum BuildStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
    public enum BuildDamageCategory {
        BLOOD,
        FROST,
        POISON,
        RED_ROT,
        MAGIC,
        FIRE,
        PHYSICAL,
        NOT
    }
    public enum BuildWeaponClass {
        STRAIGHT_SWORD,
        GREAT_SWORD,
        COLOSSAL_SWORD,
        CURVED_SWORD,
        GREAT_CURVED_SWORD,
        THRUSTING_SWORD,
        HEAVY_THRUSTING_SWORD,
        KATANA,
        TWINBLADE,
        AXE,
        GREAT_AXE,
        HAMMER,
        GREAT_HAMMER,
        FLAIL,
        DAGGER,
        SPEAR,
        GREAT_SPEAR,
        HALBERD,
        WHIP,
        FIST,
        CLAW,
        REAPER,
        LIGHT_BOW,
        BOW,
        GREAT_BOW,
        CROSSBOW,
        BALLISTA,
        GLINTSTONE_STAFF,
        SACRED_SEAL,
        SMALL_SHIELD,
        MEDIUM_SHIELD,
        NOT,
        GREAT_SHIELD;
    }
    public enum BuildScalingLevel {
        NOT,
        A,
        B,
        C,
        D,
        S;
    }
}