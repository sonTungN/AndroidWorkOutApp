package com.example.workoutapp.repository;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.workoutapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class UserRepository {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private Context context;

    private MutableLiveData<User> userMutableLiveData;

    public UserRepository(Context context) {
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userMutableLiveData = new MutableLiveData<>();
    }

    public void signUpUserWithEmailAndPassword(String email, String password, String displayName) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(displayName)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        currentUser = firebaseAuth.getCurrentUser();

                        UserProfileChangeRequest customizedProfile =
                                new UserProfileChangeRequest.Builder()
                                .setDisplayName(displayName)
                                .build();

                        currentUser.updateProfile(customizedProfile)
                                .addOnCompleteListener(profileTask -> {
                                    if (profileTask.isCanceled()) {
                                        Toast.makeText(context, "Register with display name failed!", Toast.LENGTH_SHORT).show();
                                        Log.d("REGISTER", "Register with display name failed!");
                                    }
                                });

                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Register Status: SUCCESS!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.d("REGISTER ACC FAILED", Objects.requireNonNull(e.getMessage()));
                        Toast.makeText(context, "Register Status: FAILED!", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public void signInUserWithEmailAndPassword(String email, String password) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Auth Status: SUCCESS!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Auth Status: FAILED!", Toast.LENGTH_SHORT).show();
                        Log.d("LOGIN FAILED", Objects.requireNonNull(e.getMessage()));
                    });
        }
    }

    public void updateUserDisplayName(String displayName){
        currentUser = firebaseAuth.getCurrentUser();

        UserProfileChangeRequest changeRequest =
                new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build();

        currentUser.updateProfile(changeRequest)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Update Status: SAVE!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Update Status: FAILED!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            User user = new User(
                    currentUser.getEmail(),
                    currentUser.getDisplayName()
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
