package com.example.workoutapp.repository;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.workoutapp.view.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UserRepository {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private Context context;

    public UserRepository(Context context) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public void signUpUserWithEmailAndPassword(String email, String password) {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Account is created successfully!", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(context, SignInActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
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

    public void signOut() {
        firebaseAuth.signOut();
    }
}
