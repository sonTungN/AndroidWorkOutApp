package com.example.workoutapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.workoutapp.model.User;
import com.example.workoutapp.repository.UserRepository;
import com.google.firebase.auth.FirebaseUser;

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

    public MutableLiveData<User> getUserMutableLiveData() {
        return repository.getUserMutableLiveData();
    }

    public FirebaseUser getCurrentUser() {
        return repository.getCurrentUser();
    }

    public void signOut(){
        repository.signOut();
    }
}
