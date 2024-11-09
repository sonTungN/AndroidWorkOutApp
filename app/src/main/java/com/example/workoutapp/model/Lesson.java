package com.example.workoutapp.model;

public class Lesson {
    private String title;
    private String duration;
    private String imageUrl;
    private String link;

    public Lesson() {
    }

    public Lesson(String title, String duration, String imageUrl, String link) {
        this.title = title;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.link = link;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
