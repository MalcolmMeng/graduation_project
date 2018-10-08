package com.example.meng.studentclient;

public class CourseInfo {
    private int courseId;
    private String courseName;

    public CourseInfo(int courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "课程编号:" + courseId +"\n"+
                "课程名字:" + courseName ;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
