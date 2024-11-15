package com.example.workoutapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.credentials.exceptions.domerrors.EncodingError;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutapp.R;
import com.example.workoutapp.adapter.ExerciseAdapter;
import com.example.workoutapp.adapter.SearchAdapter;
import com.example.workoutapp.databinding.ActivitySearchBinding;
import com.example.workoutapp.model.Exercise;
import com.example.workoutapp.viewmodel.ExerciseViewModel;
import com.example.workoutapp.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;

    // User
    private UserViewModel userViewModel;

    // Exercise
    private ExerciseViewModel exerciseViewModel;
    private RecyclerView searchRecyclerView;

    // ALL
    private SearchAdapter searchExerciseAdapter;
    private List<Exercise> clientAllExercise;

    // COMPLETED
    private SearchAdapter searchDoneExerciseAdapter;
    private List<Exercise> clientCompletedExercise;

    // Widget
    private SearchView searchView;
    private Button allFilterButton;
    private Button completedFilterButton;
    private boolean isAllFilterButtonSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        setUpBottomNavigationBar();
        searchView = binding.searchView;
        allFilterButton = binding.allFilter;
        completedFilterButton = binding.completedFilter;

        // View Model
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        searchRecyclerView = binding.searchRecyclerView;
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        updateFilterButtonStyles();

        clientAllExercise = new ArrayList<>();
        exerciseViewModel.getExerciseLiveData().observe(this, this::setUpExerciseSearch);

        allFilterButton.setOnClickListener(view -> onFilterButtonClicked(true));
        completedFilterButton.setOnClickListener(view -> onFilterButtonClicked(false));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void onFilterButtonClicked(boolean isAllFilter) {
        if (isAllFilter != isAllFilterButtonSelected) {
            isAllFilterButtonSelected = isAllFilter;
            updateFilterButtonStyles();

            if(isAllFilterButtonSelected) {
                // Getting Exercise Lists
                clientAllExercise = new ArrayList<>();
                exerciseViewModel
                        .getExerciseLiveData()
                        .observe(this, this::setUpExerciseSearch);

            } else {
                // Getting Completed Exercise List
                clientCompletedExercise = new ArrayList<>();
                exerciseViewModel
                        .getDoneExerciseLiveData(userViewModel.getCurrentUserId())
                        .observe(this, this::setUpCompletedExerciseSearch);
            }
        }
    }

    private void setUpExerciseSearch(List<Exercise> exercises) {
        clientAllExercise.clear();
        clientAllExercise.addAll(exercises);

        searchExerciseAdapter = new SearchAdapter(getApplicationContext(), clientAllExercise);
        searchRecyclerView.setAdapter(searchExerciseAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String input) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String input) {
                searchExerciseAdapter.getFilter().filter(input);
                return false;
            }
        });
    }

    private void setUpCompletedExerciseSearch(List<Exercise> exercises) {
        clientCompletedExercise.clear();
        clientCompletedExercise.addAll(exercises);

        searchDoneExerciseAdapter = new SearchAdapter(getApplicationContext(), clientCompletedExercise);
        searchRecyclerView.setAdapter(searchDoneExerciseAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String input) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String input) {
                searchDoneExerciseAdapter.getFilter().filter(input);
                return false;
            }
        });
    }

    private void updateFilterButtonStyles() {
        if (isAllFilterButtonSelected) {
            allFilterButton.setBackgroundResource(R.drawable.active_searchtext_bg);
            completedFilterButton.setBackgroundResource(R.drawable.searchtext_bg);

        } else {
            completedFilterButton.setBackgroundResource(R.drawable.active_searchtext_bg);
            allFilterButton.setBackgroundResource(R.drawable.searchtext_bg);
        }
    }

    public void setUpBottomNavigationBar() {
        binding.navBar.setSelectedItemId(R.id.search);
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