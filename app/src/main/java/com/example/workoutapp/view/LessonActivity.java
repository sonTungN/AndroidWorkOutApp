package com.example.workoutapp.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workoutapp.R;
import com.example.workoutapp.adapter.ExerciseAdapter;
import com.example.workoutapp.adapter.LessonAdapter;
import com.example.workoutapp.databinding.ActivityLessonBinding;
import com.example.workoutapp.model.Exercise;
import com.example.workoutapp.model.Lesson;
import com.example.workoutapp.viewmodel.ExerciseViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LessonActivity extends AppCompatActivity {

    public static final String COLLECTION_PATH = "Exercises";

    private ActivityLessonBinding binding;
    private RecyclerView recyclerView;

    // Exercise
    private ExerciseViewModel exerciseViewModel;
    private ExerciseAdapter exerciseAdapter;

    // Lesson
    private LessonAdapter lessonAdapter;
    private List<Lesson> lessonList;

    // Widget
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson);

        String documentId = getIntent().getStringExtra("DOC_ID");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_lesson);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Exercise
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        fetchExerciseDataIntoView(documentId);

        // Lessons
        lessonList = new ArrayList<>();
        exerciseViewModel.getLessonListLiveData(documentId).observe(this, new Observer<List<Lesson>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Lesson> lessons) {
                lessonList.clear();
                lessonList.addAll(lessons);

                lessonAdapter = new LessonAdapter(getApplicationContext(), lessonList);
                recyclerView.setAdapter(lessonAdapter);
                lessonAdapter.notifyDataSetChanged();
            }
        });

        // Back Button
        backButton = binding.backButton;
        backButton.setOnClickListener(view -> {
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void fetchExerciseDataIntoView(String documentId) {
        DocumentReference documentReference
                = FirebaseFirestore.getInstance().collection(COLLECTION_PATH).document(documentId);
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    Exercise exercise = documentSnapshot.toObject(Exercise.class);

                    binding.setExercise(exercise);

                    assert exercise != null;
                    Glide.with(getApplicationContext())
                            .load(exercise.getImageUrl())
                            .into(binding.hero);
                });
    }
}