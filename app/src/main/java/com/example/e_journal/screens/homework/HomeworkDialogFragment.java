package com.example.e_journal.screens.homework;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_journal.R;
import com.example.e_journal.Repositories;
import com.example.e_journal.databinding.DialogFragmentHomeworkBinding;
import com.example.e_journal.model.homework.Homework;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class HomeworkDialogFragment extends DialogFragment {
    private DialogFragmentHomeworkBinding binding;
    private HomeworkViewModel viewModel;


    private String groupName;
    private String date;

    public static final String ARG_GROUP_NAME = "ARG_GROUP_NAME";
    public static final String ARG_DATE = "ARG_DATE";

    public static final String KEY_GROUP_NAME_RESPONSE = "KEY_GROUP_NAME_RESPONSE";
    public static final String KEY_DESCRIPTION_RESPONSE = "KEY_DESCRIPTION_RESPONSE";

    public static final String TAG = HomeworkDialogFragment.class.getSimpleName();
    public static final String REQUEST_KEY = TAG + "$TAG:defaultRequestKey";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupName = requireArguments().getString(ARG_GROUP_NAME);
        date = requireArguments().getString(ARG_DATE);

        ViewModelProvider.Factory factory = new HomeworkViewModelFactory(Repositories.getHomeworkRepository());
        viewModel = new ViewModelProvider(this, factory).get(HomeworkViewModel.class);

        viewModel.loadHomework(groupName, date);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogFragmentHomeworkBinding.inflate(getLayoutInflater());

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .setTitle("Класс: " + groupName + "\nДомашнее задание на " + date)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    viewModel.saveHomework(new Homework(groupName, date, binding.editTextHomework.getText().toString()), requireContext());
                    dismiss();
                }).setNegativeButton("Отмена", null);
        setCancelable(false);

        observeHomework();
        observeLoadingState();

        return alertDialog.create();
    }

    private void observeHomework() {
        viewModel.homework.observe(this, homework -> {
            binding.editTextHomework.setText(homework.getDescription());
        });
    }

    private void observeLoadingState() {
        viewModel.state.observe(this, loadingState -> {
            switch (loadingState) {
                case PENDING:
                    binding.homeworkProgressBar.setVisibility(View.VISIBLE);
                    break;
                case EMPTY:
                    binding.homeworkProgressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), R.string.empty_load_response, Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    binding.homeworkProgressBar.setVisibility(View.GONE);
                    break;
                case ERROR:
                    binding.homeworkProgressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), R.string.error_load_response, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    public static void showDialog(FragmentManager fragmentManager, String groupName, String date) {
        HomeworkDialogFragment dialogFragment = new HomeworkDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ARG_GROUP_NAME, groupName);
        bundle.putString(ARG_DATE, date);

        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentManager, TAG);
    }
}
