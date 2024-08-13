package com.example.healthfit;

import java.util.Date;

public class NutritionTrackingData {

    String username, date, time, totalCalories;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(String totalCalories) {
        this.totalCalories = totalCalories;
    }

    public NutritionTrackingData(String username, String date, String time, String totalCalories) {
        this.username = username;
        this.date = date;
        this.time = time;
        this.totalCalories = totalCalories;
    }

    public NutritionTrackingData() {

    }

}

