package com.example.workoutapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutapp.R;
import com.example.workoutapp.databinding.ActivityProfileBinding;
import com.example.workoutapp.model.User;
import com.example.workoutapp.viewmodel.UserViewModel;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setUpBottomNavigationBar();

        userViewModel.getUserMutableLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.setUser(user);
            }
        });

        binding.save.setOnClickListener(view -> {
            String newDisplayName = binding.profileDisplayName.getText().toString();
            userViewModel.updateUserDisplayName(newDisplayName);
        });

        binding.logout.setOnClickListener(view -> {
            userViewModel.signOut();
            navigateToActivity(WelcomeActivity.class);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void setUpBottomNavigationBar() {
        binding.navBar.setSelectedItemId(R.id.profile);
        binding.navBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    navigateToActivity(DashboardActivity.class);
                    return true;

                } else if (item.getItemId() == R.id.search) {
                    navigateToActivity(SearchActivity.class);
                    return true;

                }

                return false;
            }
        });
    }

    public void navigateToActivity(Class<?> targetActivity) {
        Intent i = new Intent(getApplicationContext(), targetActivity);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}