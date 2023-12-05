package com.example.mygpa;

public class SemCourseFormData {
    private String semHeading;
    private String courseName;
    private String courseCode;
    private String courseScore;

    public SemCourseFormData() {
        // Default constructor required for Firebase
    }

    // Constructor with semester heading and course details
    public SemCourseFormData(String semHeading, String courseName, String courseCode, String courseScore) {
        this.semHeading = semHeading;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseScore = courseScore;
    }

    // Getters and setters for each field
    public String getSemHeading() {
        return semHeading;
    }

    public void setSemHeading(String semHeading) {
        this.semHeading = semHeading;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseScore() {
        return courseScore;
    }

    public void setCourseScore(String courseScore) {
        this.courseScore = courseScore;
    }
}
