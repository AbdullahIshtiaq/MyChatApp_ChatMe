package com.example.attendance_app_ezilinetest.dataModels;

public class customAttendance {
    private String date;
    private String rollNumber;
    private String status;

    public customAttendance() {
    }

    public customAttendance(String date, String rollNumber, String status) {
        this.date = date;
        this.rollNumber = rollNumber;
        this.status = status;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
