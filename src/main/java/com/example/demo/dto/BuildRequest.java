package com.example.demo.dto;

import com.example.demo.model.Build;

public class BuildRequest {

    private String preview;
    private String name;
    private String description;
    private String helmet;
    private String armor;
    private String trousers;
    private String gloves;
    private String left;
    private String leftAsh;
    private String right;
    private String rightAsh;
    private String damageCategory;
    private String weaponClass;
    private String guideContent;
    private String buildScalingLevel;
    private String strengthScaling;
    private String dexterityScaling;
    private String intelligenceScaling;
    private String faithScaling;
    private String arcaneScaling;

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

    public String getLeft() {
        return left;
    }

    public String getLeftAsh() {
        return leftAsh;
    }

    public String getRight() {
        return right;
    }

    public String getRightAsh() {
        return rightAsh;
    }

    public String getArmor() {
        return armor;
    }

    public String getDamageCategory() {
        return damageCategory;
    }

    public String getWeaponClass() {
        return weaponClass;
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

    public String getGuideContent() {
        return guideContent;
    }

    public String getBuildScalingLevel() {
        return buildScalingLevel;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public void setHelmet(String helmet) {
        this.helmet = helmet;
    }

    public void setTrousers(String trousers) {
        this.trousers = trousers;
    }

    public void setGloves(String gloves) {
        this.gloves = gloves;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public void setArmor(String armor) {
        this.armor = armor;
    }

    public void setDamageCategory(String damageCategory) {
        this.damageCategory = damageCategory;
    }

    public void setLeftAsh(String leftAsh) {this.leftAsh = leftAsh;}

    public void setRightAsh(String rightAsh) {this.rightAsh = rightAsh;}

    public void setWeaponClass(String weaponClass) {this.weaponClass = weaponClass;}

    public void setBuildScalingLevel(String buildScalingLevel) {this.buildScalingLevel = buildScalingLevel;}

    public void setGuideContent(String guideContent) {
        this.guideContent = guideContent;
    }

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
}