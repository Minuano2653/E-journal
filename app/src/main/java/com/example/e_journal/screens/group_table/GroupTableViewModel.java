package com.example.e_journal.screens.group_table;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.e_journal.model.group_table.FirebaseGroupTableRepository;
import com.example.e_journal.model.group_table.GradeOrVisit;
import com.example.e_journal.model.group_table.Student;
import com.example.e_journal.model.group_table.StudentStatistics;
import com.example.e_journal.utlis.LoadingState;
import com.github.javafaker.Bool;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupTableViewModel extends ViewModel {

    private final FirebaseGroupTableRepository groupTableRepository;

    private MutableLiveData<List<Student>> _students = new MutableLiveData<>();
    public LiveData<List<Student>> students = _students;

    private MutableLiveData<Integer> _month = new MutableLiveData<>();
    public LiveData<Integer> month = _month;

    private MutableLiveData<Boolean> _areNamesLoaded = new MutableLiveData<>();
    public LiveData<Boolean> areNamesLoaded = _areNamesLoaded;

    private MutableLiveData<String> _saveResult = new MutableLiveData<>();
    public LiveData<String> saveResult = _saveResult;
    private MutableLiveData<GradeOrVisit> _updatedGradeOrVisit = new MutableLiveData<>();
    public LiveData<GradeOrVisit> updatedGradeOrVisit = _updatedGradeOrVisit;

    private MutableLiveData<StudentStatistics> _studentStatistics = new MutableLiveData<>();
    public LiveData<StudentStatistics> studentStatistics = _studentStatistics;
    private MutableLiveData<String> _fetchingStudentsStatisticsResult = new MutableLiveData<>();
    public LiveData<String> fetchingStudentsStatisticsResult = _fetchingStudentsStatisticsResult;

    private MutableLiveData<LoadingState> _state = new MutableLiveData<>();
    public LiveData<LoadingState> state = _state;

    public GroupTableViewModel(FirebaseGroupTableRepository groupTableRepository) {
        this.groupTableRepository = groupTableRepository;
        int month = Calendar.getInstance().get(Calendar.MONTH);
        _month.setValue(month);
        _areNamesLoaded.setValue(false);
    }

    public void setAreNamesLoaded(Boolean areNamesLoaded) {
        this._areNamesLoaded.setValue(areNamesLoaded);
    }

    public void setMonth(int month) {
        _month.setValue(month);
    }

    public void saveGradeOrVisit(String groupName, GradeOrVisit gradeOrVisit) {
        groupTableRepository.saveGradeOrVisit(groupName, gradeOrVisit, monthMapper(_month.getValue()),
                task -> {
                    _updatedGradeOrVisit.postValue(gradeOrVisit);
                    _saveResult.postValue("Оценка успешно сохранена для " + gradeOrVisit.getStudentName());
                },
                task -> _saveResult.postValue("Ошибка сохранения оценки для\nученика: " + gradeOrVisit.getStudentName())
        );
    }

    public void loadGroupTable(String groupName) {
        _state.setValue(LoadingState.PENDING);
        groupTableRepository.loadStudents(groupName, monthMapper(_month.getValue()), new FirebaseGroupTableRepository.GroupTableLoadListener() {
            @Override
            public void onSuccess(List<Student> students) {
                if (students.isEmpty()) {
                    _state.setValue(LoadingState.EMPTY);
                } else {
                    _state.setValue(LoadingState.SUCCESS);
                    _students.setValue(students);
                }
            }

            @Override
            public void onError(Exception e) {
                _state.setValue(LoadingState.ERROR);
                Log.d("TAG", e.toString());
            }
        });
    }

    public void loadStudentStatistics(String studentName, String groupName) {
        groupTableRepository.getStudentStatistics(studentName, groupName, _month.getValue(), new FirebaseGroupTableRepository.StudentStatisticsLoadListener() {
            @Override
            public void onSuccess(StudentStatistics studentStatistics) {
                _studentStatistics.postValue(studentStatistics);
            }

            @Override
            public void onError(Exception e) {
                _fetchingStudentsStatisticsResult.postValue("Ошибка получения данных студента");
                Log.d("TAG", e.toString());
            }
        });
    }

    private String monthMapper(int month) {
        Map<Integer, String> monthMap = new HashMap<>();
        monthMap.put(0, "january");
        monthMap.put(1, "february");
        monthMap.put(2, "march");
        monthMap.put(3, "april");
        monthMap.put(4, "may");
        monthMap.put(5, "june");
        monthMap.put(6, "july");
        monthMap.put(7, "august");
        monthMap.put(8, "september");
        monthMap.put(9, "october");
        monthMap.put(10, "november");
        monthMap.put(11, "december");

        return monthMap.get(month);
    }
}
