package com.life.dto.goals;

public class AchievementDTO {

    private String id;
    private String icon;
    private String title;
    private String description;
    private boolean unlocked;
    private int current;
    private int target;
    private String criteria;

    public AchievementDTO() {
    }

    public AchievementDTO(String id, String icon, String title, String description, boolean unlocked, int current, int target, String criteria) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.unlocked = unlocked;
        this.current = current;
        this.target = target;
        this.criteria = criteria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }
}
