package com.example.workoutapp.view;

import static com.example.workoutapp.repository.ExerciseRepository.EXERCISE_COLLECTION_PATH;
import static com.example.workoutapp.repository.UserRepository.USER_COLLECTION_PATH;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

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

import com.bumptech.glide.Glide;
import com.example.workoutapp.R;
import com.example.workoutapp.adapter.ExerciseAdapter;
import com.example.workoutapp.adapter.LessonAdapter;
import com.example.workoutapp.databinding.ActivityLessonBinding;
import com.example.workoutapp.model.Exercise;
import com.example.workoutapp.model.Lesson;
import com.example.workoutapp.model.User;
import com.example.workoutapp.repository.ExerciseRepository;
import com.example.workoutapp.viewmodel.ExerciseViewModel;
import com.example.workoutapp.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LessonActivity extends AppCompatActivity {

    private ActivityLessonBinding binding;
    private RecyclerView recyclerView;

    // User
    private UserViewModel userViewModel;

    // Exercise
    private ExerciseViewModel exerciseViewModel;
    private ExerciseAdapter exerciseAdapter;

    // Lesson
    private LessonAdapter lessonAdapter;
    private List<Lesson> lessonList;

    // Widget
    private ImageView backButton;

    // Dialog
    private Dialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson);

        // Get the clicked document Id
        String documentId = getIntent().getStringExtra("DOC_ID");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_lesson);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // User
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

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

        // Done Workout
        binding.doneBtn.setOnClickListener(view -> {
            showConfirmDialog();
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
        DocumentReference documentReference =
                FirebaseFirestore.getInstance()
                        .collection(EXERCISE_COLLECTION_PATH)
                        .document(documentId);

        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    Exercise exercise = documentSnapshot.toObject(Exercise.class);
                    binding.setExercise(exercise);

                    assert exercise != null;
                    Glide.with(getApplicationContext())
                            .load(exercise.getImageUrl())
                            .into(binding.hero);
                });

        String currentUserId = userViewModel.getCurrentUserId();
        DocumentReference ref =
                FirebaseFirestore.getInstance()
                        .collection(USER_COLLECTION_PATH)
                        .document(currentUserId);

        ref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<Map<String, Object>> exerciseList =
                                (List<Map<String, Object>>) documentSnapshot.get("doneExercises");

                        assert exerciseList != null;
                        for(Map<String, Object> ex : exerciseList) {
                            if(!Objects.equals(ex.get("documentId"), documentId)) {
                                continue;
                            }

                            binding.doneBtn.setText("COMPLETED");
                            binding.doneBtn.setBackgroundDrawable(getDrawable(R.drawable.button_green_bg));
                            binding.doneBtn.setEnabled(false);
                        }
                    }
                });
    }

    private void showConfirmDialog() {
        String docId = getIntent().getStringExtra("DOC_ID");

        confirmDialog = new Dialog(this);
        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.confirm_dialog, null);

        confirmDialog.setContentView(view);
        confirmDialog.show();

        Button yesButton = view.findViewById(R.id.yesButton);
        Button noButton = view.findViewById(R.id.noButton);

        noButton.setOnClickListener(v -> {
            confirmDialog.dismiss();
        });

        yesButton.setOnClickListener(v -> {
            confirmDialog.dismiss();

            exerciseViewModel.markedExerciseAsDone(docId, () -> fetchExerciseDataIntoView(docId));
        });
    }

}