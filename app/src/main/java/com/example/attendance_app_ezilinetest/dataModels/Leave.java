package com.example.attendance_app_ezilinetest.dataModels;

public class Leave {
    public String end_date;
    public String reason;
    public String roll_number;
    public String start_date;
    public String status;

    public Leave() {

    }

    public Leave(String end_date, String reason, String roll_number, String start_date, String status) {
        this.end_date = end_date;
        this.reason = reason;
        this.roll_number = roll_number;
        this.start_date = start_date;
        this.status = status;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRoll_number() {
        return roll_number;
    }

    public void setRoll_number(String roll_number) {
        this.roll_number = roll_number;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
