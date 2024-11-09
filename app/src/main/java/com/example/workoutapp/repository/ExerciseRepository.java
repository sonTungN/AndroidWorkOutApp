package com.example.workoutapp.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.workoutapp.model.Exercise;
import com.example.workoutapp.model.Lesson;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExerciseRepository {

    private Context context;

    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    private MutableLiveData<List<Exercise>> exerciseMutableLiveData;

    private MutableLiveData<List<Lesson>> lessonListMutableLiveData;

    public ExerciseRepository(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.collectionReference = db.collection("Exercises");

        this.exerciseMutableLiveData = new MutableLiveData<>();
        this.lessonListMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Exercise>> getExerciseMutableLiveData() {
        List<Exercise> exerciseList = new ArrayList<>();

        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Exercise exercise = snapshot.toObject(Exercise.class);

                            List<Map<String, Object>> lessonMaps =
                                    (List<Map<String, Object>>) snapshot.get("lessonList");

                            List<Lesson> lessons = new ArrayList<>();

                            if (lessonMaps != null) {
                                for (Map<String, Object> lessonMap : lessonMaps) {
                                    Lesson lesson = new Lesson();
                                    lesson.setTitle((String) lessonMap.get("title"));
                                    lesson.setDuration((String) lessonMap.get("duration"));
                                    lesson.setImageUrl((String) lessonMap.get("imageUrl"));
                                    lesson.setLink((String) lessonMap.get("link"));
                                    lessons.add(lesson);
                                }
                            }

                            exercise.setLessonList(lessons);
                            exerciseList.add(exercise);
                        }

                        exerciseMutableLiveData.postValue(exerciseList);
                    }
                });

        return exerciseMutableLiveData;
    }

    public MutableLiveData<List<Lesson>> getLessonListMutableLiveData(String docId) {
        DocumentReference ref = db.collection("Exercises").document(docId);
        ref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<Lesson> lessonList = new ArrayList<>();

                        if(documentSnapshot.exists()) {
                            List<Map<String, Object>> lessonsData
                                    = (List<Map<String, Object>>) documentSnapshot.get("lessonList");


                            if (lessonsData != null) {
                                for (Map<String, Object> lessonData : lessonsData) {
                                    Lesson lesson = new Lesson();
                                    lesson.setTitle((String) lessonData.get("title"));
                                    lesson.setDuration((String) lessonData.get("duration"));
                                    lesson.setImageUrl((String) lessonData.get("imageUrl"));
                                    lesson.setLink((String) lessonData.get("link"));

                                    lessonList.add(lesson);
                                }
                            }
                        }

                        lessonListMutableLiveData.postValue(lessonList);
                    }
                });

        return lessonListMutableLiveData;
    }
}
