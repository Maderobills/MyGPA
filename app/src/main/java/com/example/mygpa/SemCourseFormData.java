package com.example.mygpa;

public class SemCourseFormData {
    private String semHeading;
    private String courseName;
    private String gradePoint;
    private String courseCode;
    private String courseScore;

    private String creditHours;

    public SemCourseFormData() {
        // Default constructor required for Firebase
    }

    // Constructor with semester heading and course details
    public SemCourseFormData(String semHeading, String courseName, String gradePoint, String courseCode, String courseScore, String creditHours) {
        this.semHeading = semHeading;
        this.courseName = courseName;
        this.gradePoint = gradePoint;
        this.courseCode = courseCode;
        this.courseScore = courseScore;
        this.creditHours = creditHours;
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
    public String getGradePoint() {
        return gradePoint;
    }
    public void setGradePoint(String gradePoint) {
        this.gradePoint = gradePoint;
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

    public String getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(String creditHours) {
        this.creditHours = creditHours;
    }
}
