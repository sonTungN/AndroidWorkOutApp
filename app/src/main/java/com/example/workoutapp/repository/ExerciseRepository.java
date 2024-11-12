package com.example.workoutapp.repository;

import static com.example.workoutapp.repository.UserRepository.USER_COLLECTION_PATH;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.workoutapp.model.Exercise;
import com.example.workoutapp.model.Lesson;
import com.example.workoutapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExerciseRepository {
    public static final String EXERCISE_COLLECTION_PATH = "Exercises";
    private Context context;

    // Firebase FireStore
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    // Live Data List
    private MutableLiveData<List<Exercise>> exerciseMutableLiveData;
    private MutableLiveData<List<Exercise>> doneExerciseMutableLiveData;

    private MutableLiveData<List<Lesson>> lessonListMutableLiveData;

    public ExerciseRepository(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.collectionReference = db.collection(EXERCISE_COLLECTION_PATH);
        this.exerciseMutableLiveData = new MutableLiveData<>();
        this.doneExerciseMutableLiveData = new MutableLiveData<>();
        this.lessonListMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Exercise>> getExerciseMutableLiveData() {
        List<Exercise> exerciseList = new ArrayList<>();

        collectionReference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
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
                });

        return exerciseMutableLiveData;
    }

    public MutableLiveData<List<Exercise>> getDoneExerciseMutableLiveData(String userId) {
        db.collection(USER_COLLECTION_PATH)
                .document(userId)
                .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        User user = documentSnapshot.toObject(User.class);

                        assert user != null;
                        doneExerciseMutableLiveData.postValue(user.getDoneExercise());
                    });

        return doneExerciseMutableLiveData;
    }

    public void markedWorkoutStatusAsDone(String docId, Runnable fetchData) {
        String currentUserId
                = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DocumentReference exerciseRef = db.collection(EXERCISE_COLLECTION_PATH).document(docId);
        DocumentReference userRef = db.collection(USER_COLLECTION_PATH).document(currentUserId);

        exerciseRef
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Exercise exercise = documentSnapshot.toObject(Exercise.class);

                    userRef
                        .update("doneExercises", FieldValue.arrayUnion(exercise))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Workout Status: DONE", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(
                                    e -> Toast.makeText(context, "Workout Status: FAILED", Toast.LENGTH_SHORT).show()
                            );

                    if(fetchData != null) {
                        fetchData.run();
                    }
                });
    }

    public MutableLiveData<List<Lesson>> getLessonListMutableLiveData(String docId) {
        DocumentReference ref = db.collection(EXERCISE_COLLECTION_PATH).document(docId);
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
