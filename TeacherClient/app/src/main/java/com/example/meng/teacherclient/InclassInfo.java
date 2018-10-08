package com.example.meng.teacherclient;

public class InclassInfo {
    private int inclassId;
    private int courseId;
    private String inclassName;
    private boolean isActive;
    private boolean isVisible;

    public InclassInfo(int inclassId, int courseId, String inclassName, boolean isActive, boolean isVisible) {
        this.inclassId = inclassId;
        this.courseId = courseId;
        this.inclassName = inclassName;
        this.isActive = isActive;
        this.isVisible = isVisible;
    }

    public int getInclassId() {
        return inclassId;
    }

    public void setInclassId(int inclassId) {
        this.inclassId = inclassId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getInclassName() {
        return inclassName;
    }

    public void setInclassName(String inclassName) {
        this.inclassName = inclassName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public String toString() {
        return inclassName;
    }
}
