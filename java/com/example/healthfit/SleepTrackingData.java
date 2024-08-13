package com.example.healthfit;

public class SleepTrackingData {

    String timeSleep, wakeupTime, date, sleepNote, username;

    public String getTimeSleep() {
        return timeSleep;
    }

    public void setTimeSleep(String timeSleep) {
        this.timeSleep = timeSleep;
    }

    public String getWakeupTime() {
        return wakeupTime;
    }

    public void setWakeupTime(String wakeupTime) {
        this.wakeupTime = wakeupTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSleepNote() {
        return sleepNote;
    }

    public void setSleepNote(String sleepNote) {
        this.sleepNote = sleepNote;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SleepTrackingData(String timeSleep, String wakeupTime, String date, String sleepNote, String username) {
        this.timeSleep = timeSleep;
        this.wakeupTime = wakeupTime;
        this.date = date;
        this.sleepNote = sleepNote;
        this.username = username;
    }

    public SleepTrackingData() {
    }
}
