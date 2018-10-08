package com.example.meng.teacherclient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class TeacherClientContext {
    private String url="";//服务器地址
    private static TeacherClientContext clientContext;
    //保存当前教师信息
    private TeacherInfo teacherInfo;
    //保存当前教师的课程信息
    private ArrayList courseList;
    //保存教师进入的课程
    private CourseInfo currentCourse;
    //保存教师的课堂列表
    private ArrayList inClassList;
    //保存教师进入的课堂
    private InclassInfo currentInclassInfo;
    private OkHttpClient okHttpClient;

    public InclassInfo getCurrentInclassInfo() {
        return currentInclassInfo;
    }

    public void setCurrentInclassInfo(InclassInfo currentInclassInfo) {
        this.currentInclassInfo = currentInclassInfo;
    }
    public ArrayList getInClassList(){
        return inClassList;
    }
    public void clearInClassList(){
        inClassList.clear();
    }
    public void addCourse(CourseInfo courseInfo){
        courseList.add(courseInfo);
    }
    public CourseInfo getCourseInfoAt(int i){
        return (CourseInfo) courseList.get(i);
    }
    public ArrayList getCourseList() {
        return courseList;
    }
    public CourseInfo getCurrentCourse() {
        return currentCourse;
    }

    public void setCurrentCourse(CourseInfo currentCourse) {
        this.currentCourse = currentCourse;
    }

    public void addInclassInfo(InclassInfo inclassInfo){
        inClassList.add(inclassInfo);
    }
    public InclassInfo getInclassInfo(int i){
        return (InclassInfo) inClassList.get(i);
    }
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }


    private TeacherClientContext(){
        okHttpClient=new OkHttpClient();
        courseList=new ArrayList();
        inClassList=new ArrayList();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TeacherInfo getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(TeacherInfo teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public static TeacherClientContext getClientContextInstance(){
        if(clientContext==null){
            synchronized (TeacherClientContext.class) {
                if (clientContext == null) {
                    clientContext=new TeacherClientContext();
                }
            }
            return clientContext;
        }
        return clientContext;
    }
}
