package com.example.workoutapp.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.workoutapp.R;
import com.example.workoutapp.databinding.LessonItemBinding;
import com.example.workoutapp.model.Lesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder>{

    private final Context context;
    private final List<Lesson> lessonList;

    public LessonAdapter(Context context, List<Lesson> lessonList) {
        this.context = context;
        this.lessonList = lessonList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LessonItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.lesson_item,
                parent,
                false
        );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lesson lesson = lessonList.get(position);

        holder.getBinding().setLesson(lesson);

        Glide.with(context)
                .load(lesson.getImageUrl())
                .into(holder.binding.lessonThumb);

        holder.binding.playButton.setOnClickListener(view -> {
            Intent appIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                            "vnd.youtube:" +
                                    lessonList.get(position).getLink()
                    )
            );

            Intent webIntent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                            "https://www.youtube.com/watch?v=" +
                                    lessonList.get(position).getLink()
                    )
            );

            try {
                appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(appIntent);

            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Error opening Youtube in app!", Toast.LENGTH_SHORT).show();

                webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(webIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessonList != null ? lessonList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LessonItemBinding binding;

        public ViewHolder(LessonItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public LessonItemBinding getBinding() {
            return binding;
        }
    }
}
