package com.example.e_journal.screens.group_table;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_journal.databinding.ItemDateBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.DateViewHolder> {

    private List<String> dates = new ArrayList<>();

    public void setDates(int month) {
        dates.clear();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, month, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < daysInMonth; i++) {
            dates.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        notifyDataSetChanged();
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        ItemDateBinding binding;

        public DateViewHolder(ItemDateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDateBinding binding = ItemDateBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new DateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        String date = dates.get(position);
        holder.binding.dateTextView.setText(date);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

}
