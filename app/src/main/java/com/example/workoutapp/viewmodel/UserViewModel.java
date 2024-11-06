package com.example.workoutapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.workoutapp.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.repository = new UserRepository(getApplication());
    }

    public void signUpUser(String email, String password) {
        repository.signUpUserWithEmailAndPassword(email, password);
    }

    public void signInUser(String email, String password) {
        repository.signInUserWithEmailAndPassword(email, password);
    }

    public void signOut(){
        repository.signOut();
    }
}
