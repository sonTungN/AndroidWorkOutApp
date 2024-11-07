package com.example.workoutapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workoutapp.R;
import com.example.workoutapp.databinding.WorkoutItemBinding;
import com.example.workoutapp.model.Exercise;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private Context context;
    private List<Exercise> exerciseList;

    public ExerciseAdapter(Context context, List<Exercise> exerciseList) {
        this.context = context;
        this.exerciseList = exerciseList;
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
        private WorkoutItemBinding binding;

        public ViewHolder(WorkoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(view -> {
                int position = getAdapterPosition();

                Toast.makeText(context, "Clicked: " + position, Toast.LENGTH_SHORT).show();
            });
        }

        public WorkoutItemBinding getBinding() {
            return binding;
        }
    }
}
