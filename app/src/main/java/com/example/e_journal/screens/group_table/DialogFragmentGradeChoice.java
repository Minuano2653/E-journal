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
import com.example.e_journal.model.group_table.GradeOrVisit;

import java.util.Arrays;
import java.util.List;

public class DialogFragmentGradeChoice extends DialogFragment {
    private static final String ARG_GRADE_OR_VISIT = "ARG_GRADE_OR_VISIT";
    private static final String TAG = DialogFragmentGradeChoice.class.getSimpleName();
    public static final String REQUEST_KEY = TAG + ":defaultRequestKey";
    public static final String KEY_GRADE_OR_VISIT_RESPONSE = "KEY_GRADE_OR_VISIT_RESPONSE";
    private GradeOrVisit gradeOrVisit;
    private List<String> gradeTypes;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gradeTypes = Arrays.asList(requireContext().getResources().getStringArray(R.array.grade_types));
        gradeOrVisit = requireArguments().getParcelable(ARG_GRADE_OR_VISIT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle result = new Bundle();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle("Выбор оценки для " + gradeOrVisit.getStudentName())
                .setSingleChoiceItems(R.array.grade_types, gradeTypes.indexOf(gradeOrVisit.getValue()), (dialog, which) -> {
                    gradeOrVisit.setValue(gradeTypes.get(which));
                })
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    result.putParcelable(KEY_GRADE_OR_VISIT_RESPONSE, gradeOrVisit);
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
                    dismiss();
                })
                .setNegativeButton("Отмена", null);

        return builder.create();
    }

    public static void showGradeChoiceDialogFragment(FragmentManager manager, GradeOrVisit gradeOrVisit) {
        DialogFragmentGradeChoice dialogFragment = new DialogFragmentGradeChoice();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GRADE_OR_VISIT, gradeOrVisit);
        dialogFragment.setArguments(args);
        dialogFragment.show(manager, TAG);
    }
}
