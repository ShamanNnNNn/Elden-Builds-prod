package com.example.demo.dto;

import com.example.demo.model.User;

public class BuildResponse {
    private Long id;
    private String preview;
    private String name;
    private String description;
    private String helmet;
    private String armor;
    private String trousers;
    private String gloves;
    private String left;
    private String right;
    private String leftAsh;
    private String rightAsh;
    private User owner;
    private String status;
    private String damageCategory;
    private String weaponClass;
    private String guideContent;
    private String buildScalingLevel;
    private String strengthScaling;
    private String dexterityScaling;
    private String intelligenceScaling;
    private String faithScaling;
    private String arcaneScaling;
    private int likesCount;
    private boolean likedByCurrentUser;
    private String createdAt;
    private long commentsCount;


    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPreview() {
        return preview;
    }

    public String getHelmet() {
        return helmet;
    }

    public String getTrousers() {
        return trousers;
    }

    public String getGloves() {
        return gloves;
    }

    public String getLeftAsh() {
        return leftAsh;
    }

    public String getRightAsh() {
        return rightAsh;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    public String getArmor() {
        return armor;
    }

    public String getStatus() {
        return status;
    }

    public String getDamageCategory() {
        return damageCategory;
    }

    public String getGuideContent() {
        return guideContent;
    }

    public String getWeaponClass() {
        return weaponClass;
    }

    public String getBuildScalingLevel() {
        return buildScalingLevel;
    }

    public String getStrengthScaling() {
        return strengthScaling;
    }

    public String getDexterityScaling() {
        return dexterityScaling;
    }

    public String getIntelligenceScaling() {
        return intelligenceScaling;
    }

    public String getFaithScaling() {
        return faithScaling;
    }

    public String getArcaneScaling() {
        return arcaneScaling;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {this.description = description;}

    public void setPreview(String preview) {this.preview = preview;}

    public void setHelmet(String helmet) {this.helmet = helmet;}

    public void setTrousers(String trousers) {this.trousers = trousers;}

    public void setGloves(String gloves) {this.gloves = gloves;}

    public void setLeft(String left) {this.left = left;}

    public void setRight(String right) {this.right = right;}

    public void setArmor(String armor) {this.armor = armor;}

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDamageCategory(String damageCategory) {this.damageCategory = damageCategory;}

    public void setLeftAsh(String leftAsh) {this.leftAsh = leftAsh;}

    public void setRightAsh(String rightAsh) {this.rightAsh = rightAsh;}

    public void setGuideContent(String guideContent) {
        this.guideContent = guideContent;
    }

    public void setWeaponClass(String weaponClass) {this.weaponClass = weaponClass;}

    public void setBuildScalingLevel(String buildScalingLevel) {this.buildScalingLevel = buildScalingLevel;}

    public void setStrengthScaling(String strengthScaling) {
        this.strengthScaling = strengthScaling;
    }

    public void setDexterityScaling(String dexterityScaling) {
        this.dexterityScaling = dexterityScaling;
    }

    public void setIntelligenceScaling(String intelligenceScaling) {
        this.intelligenceScaling = intelligenceScaling;
    }

    public void setFaithScaling(String faithScaling) {
        this.faithScaling = faithScaling;
    }

    public void setArcaneScaling(String arcaneScaling) {
        this.arcaneScaling = arcaneScaling;
    }

    public int getLikesCount() { return likesCount; }

    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }

    public boolean isLikedByCurrentUser() { return likedByCurrentUser; }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) { this.likedByCurrentUser = likedByCurrentUser; }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }
}
