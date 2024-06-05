package com.example.e_journal.screens.group_table;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_journal.R;
import com.example.e_journal.databinding.ItemGradeOrVisitBinding;
import com.example.e_journal.model.group_table.GradeOrVisit;
import com.example.e_journal.model.group_table.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GradesAndVisitsAdapter extends  RecyclerView.Adapter<GradesAndVisitsAdapter.GradeViewHolder>{

    public interface ItemActionListener {
        void showGradeChoice(GradeOrVisit gradeOrVisit);
    }
    private ItemActionListener itemActionListener;

    List<GradeOrVisit> gradesAndVisits = new ArrayList<>();

    public GradesAndVisitsAdapter(ItemActionListener listener) {
        this.itemActionListener = listener;
    }

    static class GradeViewHolder extends RecyclerView.ViewHolder {
        ItemGradeOrVisitBinding binding;

        GradeViewHolder(ItemGradeOrVisitBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public void updateGradeOrVisit(GradeOrVisit newGradeOrVisit) {
        for (int i = 0; i < gradesAndVisits.size(); i++) {
            GradeOrVisit gradeOrVisit = gradesAndVisits.get(i);
            if (Objects.equals(newGradeOrVisit.getStudentName(), gradeOrVisit.getStudentName())
                    && Objects.equals(newGradeOrVisit.getDate(), gradeOrVisit.getDate())
            ) {
                gradesAndVisits.set(i, newGradeOrVisit);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void setGradesAndVisits(List<Student> students) {
        this.gradesAndVisits.clear();
        for (Student student: students) {
            List<String> stringGradesAndVisits = student.getGradesAndVisits();
            for (int i = 0; i < stringGradesAndVisits.size(); i++) {
                this.gradesAndVisits.add(new GradeOrVisit(
                        stringGradesAndVisits.get(i),
                        String.format("%02d", i+1),
                        student.getName()));
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GradesAndVisitsAdapter.GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGradeOrVisitBinding binding = ItemGradeOrVisitBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new GradesAndVisitsAdapter.GradeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GradesAndVisitsAdapter.GradeViewHolder holder, int position) {
        GradeOrVisit gradeOrVisit = gradesAndVisits.get(position);

        String gradeValue = gradeOrVisit.getValue();

        holder.binding.gradeTextView.setText(gradeValue);

        int colorResId;

        switch (gradeValue) {
            case "2":
                colorResId = R.color.grade_2;
                break;
            case "3":
                colorResId = R.color.grade_3;
                break;
            case "4":
                colorResId = R.color.grade_4;
                break;
            case "5":
                colorResId = R.color.grade_5;
                break;
            default:
                colorResId = R.color.black;
                break;
        }

        holder.binding.gradeTextView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), colorResId));
        holder.binding.gradeTextView.setTag(gradeOrVisit);
        holder.binding.gradeTextView.setOnClickListener(v -> itemActionListener.showGradeChoice(gradeOrVisit));
    }

    @Override
    public int getItemCount() {
        return gradesAndVisits.size();
    }
}
