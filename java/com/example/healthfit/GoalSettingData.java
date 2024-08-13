package com.example.healthfit;

public class GoalSettingData {

    String startDate, endDate, fitnessGoal, noteProgress;

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

    public String getFitnessGoal() {
        return fitnessGoal;
    }

    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }

    public String getNoteProgress() {
        return noteProgress;
    }

    public void setNoteProgress(String noteProgress) {
        this.noteProgress = noteProgress;
    }

    public GoalSettingData(String startDate, String endDate, String fitnessGoal, String noteProgress) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.fitnessGoal = fitnessGoal;
        this.noteProgress = noteProgress;
    }

    public GoalSettingData() {
    }
}
