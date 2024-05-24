package com.example.e_journal.screens.schedule;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_journal.R;
import com.example.e_journal.databinding.ItemLessonBinding;
import com.example.e_journal.model.schedule.Lesson;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.LessonViewHolder> implements View.OnClickListener {

    private List<Lesson> dailySchedule = new ArrayList<>();
    private LessonActionListener lessonActionListener;

    public ScheduleAdapter() {}

    public ScheduleAdapter(LessonActionListener listener) {
        this.lessonActionListener = listener;
    }

    public void setDailySchedule(List<Lesson> dailySchedule) {
        this.dailySchedule = dailySchedule;
        notifyDataSetChanged();
    }

    public void setLessonActionListener(LessonActionListener listener) {
        this.lessonActionListener = listener;
    }


    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLessonBinding binding = ItemLessonBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new LessonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Lesson lesson = dailySchedule.get(position);
        holder.itemView.setTag(lesson);
        holder.binding.lessonGroupNameTextView.setText(context.getString(R.string.group_name_text_view, lesson.getGroupName()));
        holder.binding.lessonNumberTextView.setText(context.getString(R.string.lesson_number_text_view, lesson.getNumber()));
        holder.binding.lessonTimeTextView.setText(context.getString(R.string.lesson_time_text_view, lesson.getTime()));

        holder.binding.getRoot().setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return dailySchedule.size();
    }

    @Override
    public void onClick(View v) {
        Lesson lesson = (Lesson) v.getTag();
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.setGetHomework) {
                lessonActionListener.onGoToHomeworkDialogFragmentClick(lesson.getGroupName());
                return true;
            } else if (item.getItemId() == R.id.goToGroupTable) {
                lessonActionListener.onGoToGroupTableClick(lesson.getGroupName());
            }
            return false;
        });

        popupMenu.show();
    }

    static class LessonViewHolder extends RecyclerView.ViewHolder {
        ItemLessonBinding binding;

        public LessonViewHolder(@NonNull ItemLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface LessonActionListener {
        void onGoToHomeworkDialogFragmentClick(String groupName);
        void onGoToGroupTableClick(String groupName);
    }

}
