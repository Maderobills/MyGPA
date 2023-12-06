package com.example.mygpa;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class SchoolFormData {
    private String schoolName, program, startDate, endDate;
    private String numberOfSemesters;
    private String gpaScale; // Use float to store GPA scale (e.g., 4.0 or 5.0)
    private boolean visibility;

    // Default (no-argument) constructor
    public SchoolFormData() {
        // Default constructor with no arguments
    }

    public SchoolFormData(String schoolName, String program, String startDate, String endDate, String numberOfSemesters, String gpaScale) {
        this.schoolName = schoolName;
        this.program = program;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfSemesters = numberOfSemesters;
        this.gpaScale = gpaScale;
        this.visibility = false;
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

    public String getNumberOfSemesters() {
        return numberOfSemesters;
    }

    public void setNumberOfSemesters(String numberOfSemesters) {
        this.numberOfSemesters = numberOfSemesters;
    }

    public String getGpaScale() {
        return gpaScale;
    }

    public void setGpaScale(String gpaScale) {
        this.gpaScale = gpaScale;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

}
