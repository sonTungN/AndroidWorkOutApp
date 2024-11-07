package com.example.workoutapp.view;

import android.annotation.SuppressLint;
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
import com.example.workoutapp.model.User;
import com.example.workoutapp.viewmodel.ExerciseViewModel;
import com.example.workoutapp.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;

    // User
    private UserViewModel userViewModel;

    // Exercise
    private ExerciseViewModel exerciseViewModel;
    private RecyclerView exerciseRecyclerView;
    private ExerciseAdapter exerciseAdapter;
    private List<Exercise> exerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        // User
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUserMutableLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.setUser(user);
            }
        });

        // Exercise
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        exerciseList = new ArrayList<>();

        exerciseRecyclerView = binding.exerciseRecyclerView;
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Exercise Example
//        Exercise ex1 = new Exercise("Daily workout", "30 min", "2 Exs", "180 Kcal", "null");
//        Exercise ex2 = new Exercise("Daily workout 2", "50 min", "5 Exs", "210 Kcal", "null");
//        Exercise ex3 = new Exercise("Daily workout 3", "40 min", "1 Exs", "190 Kcal", "null");
//
//        exerciseList = new ArrayList<>();
//        exerciseList.add(ex1);
//        exerciseList.add(ex2);
//        exerciseList.add(ex3);
//
//        exerciseAdapter = new ExerciseAdapter(this, exerciseList);
//        exerciseRecyclerView.setAdapter(exerciseAdapter);

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}