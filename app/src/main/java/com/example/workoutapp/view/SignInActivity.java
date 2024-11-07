package com.example.workoutapp.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutapp.R;
import com.example.workoutapp.databinding.ActivitySignInBinding;
import com.example.workoutapp.viewmodel.UserViewModel;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.signinBtn.setOnClickListener(view -> {
            String email = binding.signinEmail.getText().toString();
            String password = binding.signinPassword.getText().toString();

            userViewModel.signInUser(email, password);

            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
        });

        binding.signupCta.setOnClickListener(view -> {
            Intent i = new Intent(this, SignUpActivity.class);
            startActivity(i);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}