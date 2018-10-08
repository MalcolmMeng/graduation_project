package com.example.meng.teacherclient;

public class TeacherInfo {
    private int teacherId;
    private String teacherName;
    public TeacherInfo(int teacherId,String teacherName){
        this.teacherId=teacherId;
        this.teacherName=teacherName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
