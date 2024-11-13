package com.example.workoutapp.model;

import android.util.Log;

import androidx.databinding.Bindable;

import java.util.List;
import java.util.Objects;

public class User {
    private String email;
    private String displayName;

    private List<Exercise> doneExercises;

    public User() {
    }

    public User(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
    }

    public User(
            String email,
            String displayName,
            List<Exercise> doneExercises
    ) {
        this.email = email;
        this.displayName = displayName;
        this.doneExercises = doneExercises;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Exercise> getDoneExercises() {
        return doneExercises;
    }

    public void setDoneExercises(List<Exercise> doneExercises) {
        this.doneExercises = doneExercises;
    }

    public int getDoneTotalExercises() {
        return doneExercises != null ? doneExercises.size() : 0;
    }
}
