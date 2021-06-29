package com.example.attendance_app_ezilinetest.dataModels;

public class Attendance {
    public String isPresent;

    public Attendance() {
    }

    public Attendance(String isPresent) {
        this.isPresent = isPresent;
    }

    public String getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(String isPresent) {
        this.isPresent = isPresent;
    }
}
