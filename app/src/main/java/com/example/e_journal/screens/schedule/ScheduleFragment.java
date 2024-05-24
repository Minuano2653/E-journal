package com.example.e_journal.screens.schedule;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_journal.R;
import com.example.e_journal.Repositories;
import com.example.e_journal.TeacherTabsFragment;
import com.example.e_journal.TeacherTabsFragmentDirections;
import com.example.e_journal.databinding.FragmentScheduleBinding;
import com.example.e_journal.screens.homework.HomeworkDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class ScheduleFragment extends Fragment {
    private FragmentScheduleBinding binding;
    private ScheduleViewModel viewModel;
    private ScheduleAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScheduleViewModelFactory factory = new ScheduleViewModelFactory(Repositories.getScheduleRepository());
        viewModel = new ViewModelProvider(this, factory).get(ScheduleViewModel.class);

        adapter = new ScheduleAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);

        setupRecyclerView();

        observeSchedule();
        observeState();
        observeDate();

        binding.changeDateButton.setOnClickListener(v -> {
            Map<String, Integer> date = viewModel.date.getValue();

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
                String selectedDate = sdf.format(calendar.getTime());

                date.put("year", calendar.get(Calendar.YEAR));
                date.put("month", calendar.get(Calendar.MONTH));
                date.put("dayOfMonth", calendar.get(Calendar.DAY_OF_MONTH));

                viewModel.loadDailySchedule(selectedDate);
                viewModel.setDate(date);
            }, date.get("year"), date.get("month"), date.get("dayOfMonth"));
            datePickerDialog.show();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter.setLessonActionListener(new ScheduleAdapter.LessonActionListener() {
            @Override
            public void onGoToHomeworkDialogFragmentClick(String groupName) {
                Map<String, Integer> dateMap = viewModel.date.getValue();
                Calendar calendar = Calendar.getInstance();
                calendar.set(dateMap.get("year"), dateMap.get("month"), dateMap.get("dayOfMonth"));
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
                String date = sdf.format(calendar.getTime());
                showHomeworkDialog(groupName, date);
            }

            @Override
            public void onGoToGroupTableClick(String groupName) {

                NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

                if (navHostFragment != null) {
                    NavController navController = navHostFragment.getNavController();
                    TeacherTabsFragmentDirections.ActionTeacherTabsFragmentToGroupTableFragment action =
                            TeacherTabsFragmentDirections.actionTeacherTabsFragmentToGroupTableFragment(groupName);
                    navController.navigate(action);
                }
            }
        });
    }

    private void observeSchedule() {
        viewModel.dailySchedule.observe(getViewLifecycleOwner(), dailySchedule -> {
            adapter.setDailySchedule(dailySchedule);
        });
    }

    private void observeDate() {
        viewModel.date.observe(getViewLifecycleOwner(), dateMap -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(dateMap.get("year"), dateMap.get("month"), dateMap.get("dayOfMonth"));
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
            String formattedDate = sdf.format(calendar.getTime());

            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("Расписание на "+ formattedDate);
        });
    }

    private void observeState() {
        viewModel.state.observe(getViewLifecycleOwner(), state -> {
            hideAll();
            switch (state) {
                case PENDING:
                    binding.scheduleProgressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.scheduleProgressBar.setVisibility(View.GONE);
                    binding.scheduleRecyclerView.setVisibility(View.VISIBLE);
                    break;
                case EMPTY:
                    binding.scheduleProgressBar.setVisibility(View.GONE);
                    binding.scheduleNotFoundTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), R.string.empty_load_response, Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    binding.scheduleNotFoundTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), R.string.error_load_response, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void showHomeworkDialog(String groupName, String date) {
        HomeworkDialogFragment.showDialog(getParentFragmentManager(), groupName, date);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.scheduleRecyclerView.setLayoutManager(layoutManager);
        binding.scheduleRecyclerView.setAdapter(adapter);
    }

    private void hideAll() {
        binding.scheduleRecyclerView.setVisibility(View.GONE);
        binding.scheduleProgressBar.setVisibility(View.GONE);
        binding.scheduleNotFoundTextView.setVisibility(View.GONE);
    }
}
