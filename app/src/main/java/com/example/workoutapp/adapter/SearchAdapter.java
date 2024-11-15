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
import com.example.workoutapp.databinding.SearchResultItemBinding;
import com.example.workoutapp.model.Exercise;
import com.example.workoutapp.view.LessonActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private List<Exercise> exerciseList;
    private List<Exercise> allStoredExercise;

    public SearchAdapter(Context context, List<Exercise> exerciseList) {
        this.context = context;
        this.exerciseList = exerciseList;
        this.allStoredExercise = new ArrayList<>(exerciseList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchResultItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.search_result_item,
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

    // Override the filtering method
    public Filter getFilter() {
        return exerciseFilteredList;
    }

    private final Filter exerciseFilteredList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Exercise> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(allStoredExercise);
            } else {
                String pattern = charSequence.toString().toLowerCase().trim();

                for(Exercise e : allStoredExercise) {
                    if(e.getTitle().toLowerCase().contains(pattern)) {
                        filteredList.add(e);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            exerciseList.clear();
            exerciseList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final SearchResultItemBinding binding;

        public ViewHolder(SearchResultItemBinding binding) {
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

        public SearchResultItemBinding getBinding() {
            return binding;
        }
    }
}
