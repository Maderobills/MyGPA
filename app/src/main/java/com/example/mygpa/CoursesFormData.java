package com.example.mygpa;

public class CoursesFormData {
    private String schoolName;
    private String programName;
    private String academicYear;
    private String semester;
    private int numberOfCourses;

    // Constructor
    public CoursesFormData(String schoolName, String programName, String academicYear, String semester, String numberOfCourses) {
        this.schoolName = schoolName;
        this.programName = programName;
        this.academicYear = academicYear;
        this.semester = semester;
        this.numberOfCourses = Integer.parseInt(numberOfCourses);
    }

    // Getters and setters for each field
    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getNumberOfCourses() {
        return numberOfCourses;
    }

    public void setNumberOfCourses(int numberOfCourses) {
        this.numberOfCourses = numberOfCourses;
    }
}
