package com.example.workoutapp.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.workoutapp.model.Exercise;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExerciseRepository {

    private Context context;

    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    private MutableLiveData<List<Exercise>> exerciseMutableLiveData;

    public ExerciseRepository(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.collectionReference = db.collection("Exercises");

        this.exerciseMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Exercise>> getExerciseMutableLiveData() {
        List<Exercise> exerciseList = new ArrayList<>();

        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Exercise exercise = snapshot.toObject(Exercise.class);
                            exerciseList.add(exercise);
                        }

                        exerciseMutableLiveData.postValue(exerciseList);
                    }
                });

        return exerciseMutableLiveData;
    }
}
