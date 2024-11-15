package com.example.workoutapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.workoutapp.model.Exercise;
import com.example.workoutapp.model.Lesson;
import com.example.workoutapp.repository.ExerciseRepository;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {
    private final ExerciseRepository repository;

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ExerciseRepository(getApplication());
    }

    public MutableLiveData<List<Exercise>> getExerciseLiveData() {
        return repository.getExerciseMutableLiveData();
    }

    public MutableLiveData<List<Exercise>> getDoneExerciseLiveData(String userId) {
        return repository.getDoneExerciseMutableLiveData(userId);
    }

    public void markedExerciseAsDone(String docId, Runnable fetchData) {
        repository.markedWorkoutStatusAsDone(docId, fetchData);
    }

    public MutableLiveData<List<Lesson>> getLessonListLiveData(String docId) {
        return repository.getLessonListMutableLiveData(docId);
    }
}
