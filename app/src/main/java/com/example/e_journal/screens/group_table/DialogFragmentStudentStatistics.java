package com.example.e_journal.screens.group_table;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.e_journal.R;
import com.example.e_journal.databinding.DialogFragmentStudentStatisticsBinding;
import com.example.e_journal.model.group_table.StudentStatistics;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DialogFragmentStudentStatistics extends DialogFragment {
    private DialogFragmentStudentStatisticsBinding binding;
    private static final String ARG_STUDENT_STATISTICS = "ARG_STUDENT_STATISTICS";
    private static final String TAG = DialogFragmentStudentStatistics.class.getSimpleName();
    public static final String REQUEST_KEY = TAG + ":defaultRequestKey";
    private StudentStatistics studentStatistics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentStatistics = requireArguments().getParcelable(ARG_STUDENT_STATISTICS);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogFragmentStudentStatisticsBinding.inflate(getLayoutInflater());

        Log.d("RRRR", studentStatistics.getStudentName());
        Log.d("RRRR", studentStatistics.getPresenceCount());
        Log.d("RRRR", studentStatistics.getAbsenceCount());
        Log.d("RRRR", studentStatistics.getExcusedAbsenceCount());
        Log.d("RRRR", studentStatistics.getAverageGrade());

        binding.studentNameTextView.setText(getString(R.string.student_fio, studentStatistics.getStudentName()));
        binding.presenceCountTextView.setText(getString(R.string.presence_count, studentStatistics.getPresenceCount()));
        binding.absenceCountTextView.setText(getString(R.string.absence_count, studentStatistics.getAbsenceCount()));
        binding.excusedAbsenceCountTextView.setText(getString(R.string.excusedAbsence, studentStatistics.getExcusedAbsenceCount()));
        binding.averageGradeTextView.setText(getString(R.string.average_grade, studentStatistics.getAverageGrade()));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        String currentDate = dateFormat.format(calendar.getTime());

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .setTitle("Успеваемость на " + currentDate)
                .setPositiveButton("Закрыть", null);

        setCancelable(false);

        return alertDialog.create();
    }

    public static void showDialogFragmentStudentStatistics(FragmentManager manager, StudentStatistics studentStatistics) {
        DialogFragmentStudentStatistics dialogFragment = new DialogFragmentStudentStatistics();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STUDENT_STATISTICS, studentStatistics);
        dialogFragment.setArguments(args);
        dialogFragment.show(manager, TAG);
    }
}
