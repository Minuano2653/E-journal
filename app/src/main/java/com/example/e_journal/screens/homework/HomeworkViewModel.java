package com.example.e_journal.screens.homework;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.e_journal.model.homework.FirebaseHomeworkRepository;
import com.example.e_journal.model.homework.Homework;
import com.example.e_journal.utlis.LoadingState;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HomeworkViewModel extends ViewModel {

    private final FirebaseHomeworkRepository homeworkRepository;

    private MutableLiveData<Homework> _homework = new MutableLiveData<>();
    public LiveData<Homework> homework = _homework;

    private MutableLiveData<Map<String, Integer>> _date = new MutableLiveData<>();
    public LiveData<Map<String, Integer>> date = _date;

    private MutableLiveData<LoadingState> _loadingState = new MutableLiveData<>();
    public LiveData<LoadingState> state = _loadingState;


    public HomeworkViewModel(FirebaseHomeworkRepository homeworkRepository) {
        this.homeworkRepository = homeworkRepository;
    }

    public void loadHomework(String groupName, String date) {
        _loadingState.setValue(LoadingState.PENDING);

        homeworkRepository.loadHomework(groupName, date, new FirebaseHomeworkRepository.HomeworkLoadListener() {
            @Override
            public void onSuccess(Homework homework) {
                if (homework == null) {
                    _loadingState.setValue(LoadingState.EMPTY);
                } else {
                    _homework.setValue(homework);
                    _loadingState.setValue(LoadingState.SUCCESS);
                    Log.d("FATAL", _homework.getValue().toString());
                    Log.d("FATAL", _loadingState.getValue().toString());
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("TAG", e.toString());
                _loadingState.setValue(LoadingState.ERROR);
            }
        });

    }

    public void saveHomework(Homework homework, Context context) {
        homeworkRepository.saveHomework(homework, task -> {
            Toast.makeText(context, "Домашнее задание успешно сохранено.", Toast.LENGTH_SHORT).show();
        }, task -> {
            Toast.makeText(context, "Ошибка сохранения.", Toast.LENGTH_SHORT).show();
        });

    }

    public void setDate(Map<String, Integer> date) {
        _date.setValue(date);
    }
}
