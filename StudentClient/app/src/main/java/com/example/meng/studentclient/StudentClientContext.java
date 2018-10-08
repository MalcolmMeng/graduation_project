package com.example.meng.studentclient;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class StudentClientContext {
    private static StudentClientContext clientContext;
    //学生信息
    private StudentInfo studentInfo;
    //服务器地址
    private String url="";
    private OkHttpClient okHttpClient;
    //课程列表
    private ArrayList courseList;
    //当前课程
    private  CourseInfo currentCourse;
    //课堂列表
    private ArrayList inclassList;
    //当前课堂
    private InclassInfo currentInclass;
    //问题列表
    private ArrayList questionList;
    public void addQuestion(Question question){
        questionList.add(question);
    }
    public Question getQuestionAt(int i){
        return (Question) questionList.get(i);
    }

    public ArrayList getQuestionList() {
        return questionList;
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
    public void addInclass(InclassInfo inclassInfo){
        inclassList.add(inclassInfo);
    }

    public ArrayList getInclassList() {
        return inclassList;
    }
    public InclassInfo getInclassAt(int i){
        return (InclassInfo) inclassList.get(i);
    }
    public void clearInclassList(){
        inclassList.clear();
    }
    public CourseInfo getCurrentCourse() {
        return currentCourse;
    }

    public void setCurrentCourse(CourseInfo currentCourse) {
        this.currentCourse = currentCourse;
    }

    public InclassInfo getCurrentInclass() {
        return currentInclass;
    }

    public void setCurrentInclass(InclassInfo currentInclass) {
        this.currentInclass = currentInclass;
    }




    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public StudentInfo getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(StudentInfo studentInfo) {
        this.studentInfo = studentInfo;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    private StudentClientContext(){
        courseList=new ArrayList();
        inclassList=new ArrayList();
        questionList=new ArrayList();
        okHttpClient=new OkHttpClient();
    }

    public static StudentClientContext getClientContext() {
        if(clientContext==null){
            synchronized (StudentClientContext.class) {
                if (clientContext == null) {
                    clientContext=new StudentClientContext();
                }
            }
            return clientContext;
        }
        return clientContext;
    }
}
