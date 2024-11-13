package com.example.workoutapp.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.adapter.ExerciseAdapter;
import com.example.workoutapp.databinding.ActivityDashboardBinding;
import com.example.workoutapp.model.Exercise;
import com.example.workoutapp.viewmodel.ExerciseViewModel;
import com.example.workoutapp.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;

    // User
    private UserViewModel userViewModel;

    // Exercise
    private ExerciseViewModel exerciseViewModel;

    private ExerciseAdapter exerciseAdapter;
    private RecyclerView exerciseRecyclerView;
    private List<Exercise> exerciseList;

    private ExerciseAdapter doneExerciseAdapter;
    private RecyclerView doneExerciseRecyclerView;
    private List<Exercise> doneExerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        setUpBottomNavigationBar();

        // View Model
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        // User
        userViewModel.getUserMutableLiveData().observe(this, user -> binding.setUser(user));

        // All Exercise
        exerciseList = new ArrayList<>();
        exerciseRecyclerView = binding.exerciseRecyclerView;
        exerciseRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );
        exerciseRecyclerView.hasFixedSize();

        exerciseViewModel.getExerciseLiveData().observe(this, new Observer<List<Exercise>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Exercise> exercises) {
                exerciseList.clear();
                exerciseList.addAll(exercises);

                exerciseAdapter = new ExerciseAdapter(getApplicationContext(), exerciseList);
                exerciseRecyclerView.setAdapter(exerciseAdapter);
                exerciseAdapter.notifyDataSetChanged();
            }
        });

        // Done Exercise
        fetchDoneExercise();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchDoneExercise();
    }

    public void fetchDoneExercise() {
        doneExerciseList = new ArrayList<>();
        doneExerciseRecyclerView = binding.doneRecyclerView;
        doneExerciseRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false)
        );
        doneExerciseRecyclerView.hasFixedSize();

        exerciseViewModel.getDoneExerciseLiveData(userViewModel.getCurrentUserId()).observe(this, new Observer<List<Exercise>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Exercise> exercises) {
                doneExerciseList.clear();
                doneExerciseList.addAll(exercises);

                doneExerciseAdapter = new ExerciseAdapter(getApplicationContext(), doneExerciseList);
                doneExerciseRecyclerView.setAdapter(doneExerciseAdapter);
                doneExerciseAdapter.notifyDataSetChanged();

                fetchDataIntoFieldValue(exercises);
            }
        });
    }

    public void fetchDataIntoFieldValue(List<Exercise> exercises) {
        int doneTime = 0, doneExercise = 0, doneCalories = 0;

        for(Exercise e : exercises) {
            try {
                doneTime += Integer.parseInt(e.getTotalDuration());
                doneExercise++;
                doneCalories += Integer.parseInt(e.getTotalCalories());

            } catch (Exception ignored) {
                Log.d("FIELD", Objects.requireNonNull(ignored.getMessage()));
            }
        }

        binding.doneTime.setText(String.valueOf(doneTime));
        binding.doneExercises.setText(String.valueOf(doneExercise));
        binding.doneCalories.setText(String.valueOf(doneCalories));
    }

    public void setUpBottomNavigationBar() {
        binding.navBar.setSelectedItemId(R.id.home);
        binding.navBar.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                navigateToActivity(DashboardActivity.class);
                return true;

            } else if (item.getItemId() == R.id.profile) {
               navigateToActivity(ProfileActivity.class);
               return true;
            }

            return false;
        });
    }

    public void navigateToActivity(Class<?> targetActivity) {
        Intent i = new Intent(getApplicationContext(), targetActivity);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}