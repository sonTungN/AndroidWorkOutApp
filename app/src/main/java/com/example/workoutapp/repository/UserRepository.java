package com.example.workoutapp.repository;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.workoutapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UserRepository {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private Context context;

    private MutableLiveData<User> userMutableLiveData;

    public UserRepository(Context context) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
        this.userMutableLiveData = new MutableLiveData<>();
    }

    public void signUpUserWithEmailAndPassword(String email, String password) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Account is created successfully!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.d("REGISTER FAILED", Objects.requireNonNull(e.getMessage()));
                        Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public void signInUserWithEmailAndPassword(String email, String password) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Login Successfully!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.d("LOGIN FAILED", Objects.requireNonNull(e.getMessage()));
                        Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null) {
            User user = new User(
                    currentUser.getEmail(),
                    currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Anonymous"
            );

            userMutableLiveData.postValue(user);
        }
        return userMutableLiveData;
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void signOut() {
        firebaseAuth.signOut();
    }
}
