package com.example.healthfit;

import java.text.DateFormat;

public class WaterTrackingData {

        String waterValue, date, time, username;

    public String getWaterValue() {
        return waterValue;
    }

    public void setWaterValue(String waterValue) {
        this.waterValue = waterValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public WaterTrackingData(String waterValue, String date, String time, String username) {
        this.waterValue = waterValue;
        this.date = date;
        this.time = time;
        this.username = username;
    }

    public WaterTrackingData() {
    }
}
