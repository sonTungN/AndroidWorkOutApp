package com.example.workoutapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workoutapp.R;
import com.example.workoutapp.databinding.WorkoutItemBinding;
import com.example.workoutapp.model.Exercise;
import com.example.workoutapp.view.LessonActivity;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private Context context;
    private List<Exercise> exerciseList;
    private List<Exercise> allStoredExercise;

    public ExerciseAdapter(Context context, List<Exercise> exerciseList) {
        this.context = context;
        this.exerciseList = exerciseList;
        this.allStoredExercise = new ArrayList<>(exerciseList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkoutItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.workout_item,
                parent,
                false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        String imageUrl = exercise.getImageUrl();

        holder.binding.setExercise(exercise);

        Glide.with(context)
                .load(imageUrl)
                .into(holder.binding.thumb);
    }

    @Override
    public int getItemCount() {
        return exerciseList != null ? exerciseList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final WorkoutItemBinding binding;

        public ViewHolder(WorkoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(view -> {
                int position = getAdapterPosition();

                Toast.makeText(context, "Clicked: " + position, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(context, LessonActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("DOC_ID", exerciseList.get(position).getDocumentId());
                context.startActivity(i);
            });
        }

        public WorkoutItemBinding getBinding() {
            return binding;
        }
    }
}
