package com.example.e_journal.screens.group_table;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_journal.R;
import com.example.e_journal.Repositories;
import com.example.e_journal.databinding.FragmentGroupTableBinding;
import com.example.e_journal.model.group_table.GradeOrVisit;
import com.example.e_journal.model.group_table.Student;
import com.example.e_journal.model.group_table.StudentStatistics;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class GroupTableFragment extends Fragment {

    private FragmentGroupTableBinding binding;

    private GroupTableViewModel viewModel;

    private GradesAndVisitsAdapter gradesAndVisitsAdapter;
    private DatesAdapter datesAdapter;

    private String groupName;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroupTableViewModelFactory factory = new GroupTableViewModelFactory(Repositories.getGroupTableRepository());
        viewModel = new ViewModelProvider(this, factory).get(GroupTableViewModel.class);

        groupName = GroupTableFragmentArgs.fromBundle(requireArguments()).getGroupName();

        setHasOptionsMenu(true);

        datesAdapter = new DatesAdapter();
        gradesAndVisitsAdapter = new GradesAndVisitsAdapter(gradeOrVisit -> showDialogFragmentGradeChoice(gradeOrVisit));

        viewModel.loadGroupTable(groupName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGroupTableBinding.inflate(getLayoutInflater());

        setupDatesRecyclerView();
        setupGradesAndVisitsRecyclerView();

        observeStudents();
        observeMonth();
        observeState();
        observeSaveResult();
        observeStudentStatistics();

        setupDialogFragmentMonthChoiceListener();
        setupDialogFragmentGradeChoiceListener();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(binding.groupTableToolbar);

            NavController navController = Navigation.findNavController(activity, R.id.fragmentContainer);

            NavigationUI.setupActionBarWithNavController(activity, navController);
        }
    }

    private void observeStudents() {
        viewModel.students.observe(getViewLifecycleOwner(), students -> {
            if (!viewModel.areNamesLoaded.getValue()) {
                setupNamesTable(students.stream().map(Student::getName).collect(Collectors.toList()));
            }
            gradesAndVisitsAdapter.setGradesAndVisits(students);
        });
    }

    private void observeMonth() {
        viewModel.month.observe(getViewLifecycleOwner(), month -> datesAdapter.setDates(month));
    }

    private void observeSaveResult() {
        viewModel.saveResult.observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });

        viewModel.updatedGradeOrVisit.observe(getViewLifecycleOwner(), gradeOrVisit -> {
            if (gradeOrVisit != null) {
                gradesAndVisitsAdapter.updateGradeOrVisit(gradeOrVisit);
            }
        });
    }

    private void observeStudentStatistics() {
        viewModel.studentStatistics.observe(getViewLifecycleOwner(), studentStatistics -> {
            Log.d("RRRR", studentStatistics.getStudentName());
            Log.d("RRRR", studentStatistics.getPresenceCount());
            Log.d("RRRR", studentStatistics.getAbsenceCount());
            Log.d("RRRR", studentStatistics.getExcusedAbsenceCount());
            Log.d("RRRR", studentStatistics.getAverageGrade());
            showDialogFragmentStudentStatistics(studentStatistics);
        });

        viewModel.fetchingStudentsStatisticsResult.observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });
    }

    private void observeState() {
        viewModel.state.observe(getViewLifecycleOwner(), loadingState -> {
            hideAll();
            switch (loadingState) {
                case PENDING:
                    binding.frameProgressBar.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.groupTableNotFoundTextView.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    binding.frameProgressBar.setVisibility(View.GONE);
                    binding.tableView.setVisibility(View.VISIBLE);
                    break;
                case EMPTY:
                    binding.frameProgressBar.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.groupTableNotFoundTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), R.string.empty_load_response, Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    binding.frameProgressBar.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.groupTableNotFoundTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), R.string.error_load_response, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    // -----

    private void showDialogFragmentMonthChoice() {
        DialogFragmentMonthChoice.showDialogFragmentMonthChoice(getParentFragmentManager(), viewModel.month.getValue());
    }

    private void setupDialogFragmentMonthChoiceListener() {
        getParentFragmentManager().setFragmentResultListener(
                DialogFragmentMonthChoice.REQUEST_KEY,
                getViewLifecycleOwner(), (requestKey, result) -> {
                    int month = result.getInt(DialogFragmentMonthChoice.KEY_MONTH_RESPONSE);
                    viewModel.setMonth(month);
                    viewModel.setAreNamesLoaded(true);
                    viewModel.loadGroupTable(groupName);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(2024, month, 1);
                    ((GridLayoutManager) binding.gradesAndVisitsRecyclerView.getLayoutManager()).setSpanCount(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                });
    }

    // -----

    private void showDialogFragmentGradeChoice(GradeOrVisit gradeOrVisit) {
        DialogFragmentGradeChoice.showGradeChoiceDialogFragment(getParentFragmentManager(), gradeOrVisit);
    }

    private void setupDialogFragmentGradeChoiceListener() {
        getParentFragmentManager().setFragmentResultListener(DialogFragmentGradeChoice.REQUEST_KEY,
                getViewLifecycleOwner(), (requestKey, result) -> {
                    GradeOrVisit gradeOrVisit = result.getParcelable(DialogFragmentGradeChoice.KEY_GRADE_OR_VISIT_RESPONSE);
                    viewModel.saveGradeOrVisit(groupName, gradeOrVisit);
                });
    }

    // -----

    private void showDialogFragmentStudentStatistics(StudentStatistics studentStatistics) {
        DialogFragmentStudentStatistics.showDialogFragmentStudentStatistics(getParentFragmentManager(), studentStatistics);
    }

    // -----

    private void setupDatesRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.datesRecyclerView.setLayoutManager(layoutManager);
        binding.datesRecyclerView.setAdapter(datesAdapter);
    }

    private void setupGradesAndVisitsRecyclerView() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

        };
        binding.gradesAndVisitsRecyclerView.setLayoutManager(gridLayoutManager);
        binding.gradesAndVisitsRecyclerView.setAdapter(gradesAndVisitsAdapter);
    }

    private void setupNamesTable(List<String> names) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (int i = 0; i < names.size(); i++) {

            TableRow tableRow = new TableRow(requireContext());
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView textView = (TextView) inflater.inflate(R.layout.item_student, tableRow, false);
            textView.setText(names.get(i));
            textView.setId(View.generateViewId());
            textView.setTag(names.get(i));

            textView.setOnClickListener(v -> {
                String studentName = (String) v.getTag();
                viewModel.loadStudentStatistics(studentName, groupName);
            });

            tableRow.addView(textView);

            binding.namesTableLayout.addView(tableRow);
        }
    }


    private void hideAll() {
        binding.tableView.setVisibility(View.GONE);
        binding.frameProgressBar.setVisibility(View.GONE);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.group_table_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.changeMonth) {
            showDialogFragmentMonthChoice();
            return true;
        }
        return false;
    }

}
//TODO: хаха, летом работать не будет из-за того, что летние месяцы не указаны в strings.xml