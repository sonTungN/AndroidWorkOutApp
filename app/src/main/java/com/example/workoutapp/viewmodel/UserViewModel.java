package com.example.workoutapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.workoutapp.model.User;
import com.example.workoutapp.repository.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.repository = new UserRepository(getApplication());
    }

    public void signUpUser(String email, String password, String displayName) {
        repository.signUpUserWithEmailAndPassword(email, password, displayName);
    }

    public void signInUser(String email, String password) {
        repository.signInUserWithEmailAndPassword(email, password);
    }

    public void updateUserDisplayName(String displayName){
        repository.updateUserDisplayName(displayName);
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return repository.getUserMutableLiveData();
    }

    public String getCurrentUserId() {
        return repository.getCurrentUserId();
    }

    public boolean isUserLoggedIn() {
        return repository.isUserLoggedIn();
    }

    public void signOut(){
        repository.signOut();
    }
}
