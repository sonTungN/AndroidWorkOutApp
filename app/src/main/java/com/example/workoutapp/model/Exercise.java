package com.example.workoutapp.model;

import java.util.List;


public class Exercise {
    private String documentId;
    private String title;
    private String totalDuration;
    private String totalCalories;
    private String imageUrl;

    private String description;
    private List<Lesson> lessonList;

    public Exercise() {
    }

    public Exercise(
            String documentId,
            String title,
            String totalDuration,
            String totalCalories,
            String imageUrl,
            String description,
            List<Lesson> lessonList
    ) {
        this.documentId = documentId;
        this.title = title;
        this.totalDuration = totalDuration;
        this.totalCalories = totalCalories;
        this.imageUrl = imageUrl;
        this.description = description;
        this.lessonList = lessonList;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(String totalCalories) {
        this.totalCalories = totalCalories;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Lesson> getLessonList() {
        return lessonList;
    }

    public void setLessonList(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }

    public String getLessonCount(){
        return String.valueOf(lessonList.size());
    }
}
