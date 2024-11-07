package com.example.workoutapp.model;

public class Exercise {
    private String title;
    private String duration;
    private String numberOfExercise;
    private String caloriesBurn;

    private String imageUrl;

    public Exercise() {
    }

    public Exercise(String title, String duration, String numberOfExercise, String caloriesBurn, String imageUrl) {
        this.title = title;
        this.duration = duration;
        this.numberOfExercise = numberOfExercise;
        this.caloriesBurn = caloriesBurn;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNumberOfExercise() {
        return numberOfExercise;
    }

    public void setNumberOfExercise(String numberOfExercise) {
        this.numberOfExercise = numberOfExercise;
    }

    public String getCaloriesBurn() {
        return caloriesBurn;
    }

    public void setCaloriesBurn(String caloriesBurn) {
        this.caloriesBurn = caloriesBurn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
