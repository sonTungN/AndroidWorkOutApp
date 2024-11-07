package com.example.workoutapp.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutapp.R;
import com.example.workoutapp.databinding.ActivitySignUpBinding;
import com.example.workoutapp.viewmodel.UserViewModel;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.signupBtn.setOnClickListener(view -> {
            String email = binding.signupEmail.getText().toString();
            String password = binding.signupPassword.getText().toString();

            userViewModel.signUpUser(email, password);

            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
        });

        binding.signinCta.setOnClickListener(view -> {
            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}