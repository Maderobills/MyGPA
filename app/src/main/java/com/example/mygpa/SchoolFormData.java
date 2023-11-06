package com.example.mygpa;

public class SchoolFormData {
    private String schoolName,program,startDate,endDate;
    private int numberOfSemesters;
    private float gpaScale; // Use float to store GPA scale (e.g., 4.0 or 5.0)

    public SchoolFormData(String schoolName, String program, String startDate, String endDate, int numberOfSemesters, float gpaScale) {
        this.schoolName = schoolName;
        this.program = program;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfSemesters = numberOfSemesters;
        this.gpaScale = gpaScale;
    }

    // Getters and setters for the fields
    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfSemesters() {
        return numberOfSemesters;
    }

    public void setNumberOfSemesters(int numberOfSemesters) {
        this.numberOfSemesters = numberOfSemesters;
    }

    public float getGpaScale() {
        return gpaScale;
    }

    public void setGpaScale(float gpaScale) {
        this.gpaScale = gpaScale;
    }
}
