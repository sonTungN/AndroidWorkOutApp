package com.example.workoutapp.repository;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.workoutapp.model.User;
import com.example.workoutapp.view.DashboardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class UserRepository {
    public static final String USER_COLLECTION_PATH = "Users";
    private Context context;

    // Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    // Firebase FireStore
    private FirebaseFirestore db;

    // Live Data List
    private MutableLiveData<User> userMutableLiveData;

    public UserRepository(Context context) {
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();

        this.db = FirebaseFirestore.getInstance();

        this.userMutableLiveData = new MutableLiveData<>();
    }

    public void signUpUserWithEmailAndPassword(String email, String password, String displayName) {
        if(
            !TextUtils.isEmpty(email) &&
            !TextUtils.isEmpty(password) &&
            !TextUtils.isEmpty(displayName)
        ) {
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
                                        Log.d("REGISTER", "Register with display name failed!");
                                    }
                                });

                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Register Status: SUCCESS!", Toast.LENGTH_SHORT).show();

                            User userObject = new User(email, displayName, new ArrayList<>());
                            db.collection(USER_COLLECTION_PATH)
                                    .document(currentUser.getUid())
                                    .set(userObject);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.d("REGISTER", Objects.requireNonNull(e.getMessage()));
                        Toast.makeText(context, "Register Status: FAILED!", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public void signInUserWithEmailAndPassword(String email, String password) {
        if(
            !TextUtils.isEmpty(email) &&
            !TextUtils.isEmpty(password)
        ) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Auth Status: SUCCESS!", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(context, DashboardActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
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

    public String getCurrentUserId() {
        return Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    }

    public boolean isUserLoggedIn(){
        return firebaseAuth.getCurrentUser() != null;
    }

    public void signOut() {
        Toast.makeText(context, "LOG OUT", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
    }
}
