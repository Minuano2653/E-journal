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

public class DialogFragmentMonthChoice extends DialogFragment {
    private static final String ARG_MONTH = "ARG_MONTH";
    public static final String KEY_MONTH_RESPONSE = "MONTH_KEY_RESPONSE";

    private static final String TAG = DialogFragmentMonthChoice.class.getSimpleName();
    public static final String REQUEST_KEY = TAG + ":defaultRequestKey";

    private int month;

    private String[] months;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int monthArg = requireArguments().getInt(ARG_MONTH);
        month = (monthArg > 4) ? monthArg - 3 : monthArg ;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle("Выбор месяца") //TODO: номера месцев не совпадают
                .setSingleChoiceItems(R.array.months, month, (dialog, which) -> {
                    Bundle result = new Bundle();
                    if (which > 4) {
                        result.putInt(KEY_MONTH_RESPONSE, which + 3);
                    } else {
                        result.putInt(KEY_MONTH_RESPONSE, which);
                    }
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
                    dismiss();
                });

        return builder.create();
    }

    public static void showDialogFragmentMonthChoice(FragmentManager manager, int month) {
        DialogFragmentMonthChoice dialogFragment = new DialogFragmentMonthChoice();
        Bundle args = new Bundle();
        args.putInt(ARG_MONTH, month);
        dialogFragment.setArguments(args);
        dialogFragment.show(manager, TAG);
    }

}
