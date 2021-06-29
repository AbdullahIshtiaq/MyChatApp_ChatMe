package com.example.attendance_app_ezilinetest.dataModels;

public class Student {
    private String class_room;
    private String device_token;
    private String name;
    private String password;
    private String roll_number;

    public Student() {

    }

    public Student(String class_room, String device_token, String name, String password, String roll_number) {
        this.class_room = class_room;
        this.device_token = device_token;
        this.name = name;
        this.password = password;
        this.roll_number = roll_number;
    }

    public String getClass_room() {
        return class_room;
    }

    public void setClass_room(String class_room) {
        this.class_room = class_room;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoll_number() {
        return roll_number;
    }

    public void setRoll_number(String roll_number) {
        this.roll_number = roll_number;
    }
}
