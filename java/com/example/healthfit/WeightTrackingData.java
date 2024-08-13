package com.example.healthfit;

public class WeightTrackingData {
    String weightStart, weightCurrent, weightGoal, progressNote;

    public String getWeightStart() {
        return weightStart;
    }

    public void setWeightStart(String weightStart) {
        this.weightStart = weightStart;
    }

    public String getWeightCurrent() {
        return weightCurrent;
    }

    public void setWeightCurrent(String weightCurrent) {
        this.weightCurrent = weightCurrent;
    }

    public String getWeightGoal() {
        return weightGoal;
    }

    public void setWeightGoal(String weightGoal) {
        this.weightGoal = weightGoal;
    }

    public String getProgressNote() {
        return progressNote;
    }

    public void setProgressNote(String progressNote) {
        this.progressNote = progressNote;
    }

    public WeightTrackingData(String weightStart, String weightCurrent, String weightGoal, String progressNote) {
        this.weightStart = weightStart;
        this.weightCurrent = weightCurrent;
        this.weightGoal = weightGoal;
        this.progressNote = progressNote;
    }

    public WeightTrackingData(){

    }
}
