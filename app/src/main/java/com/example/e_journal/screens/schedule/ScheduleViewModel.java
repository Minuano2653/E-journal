package com.example.e_journal.screens.schedule;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.e_journal.model.homework.Homework;
import com.example.e_journal.model.schedule.FirebaseScheduleRepository;
import com.example.e_journal.model.schedule.Lesson;
import com.example.e_journal.utlis.Event;
import com.example.e_journal.utlis.LoadingState;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleViewModel extends ViewModel {
    private final FirebaseScheduleRepository scheduleRepository;

    private MutableLiveData<List<Lesson>> _dailySchedule = new MutableLiveData<>();
    public LiveData<List<Lesson>> dailySchedule = _dailySchedule;

    private MutableLiveData<Map<String, Integer>> _date = new MutableLiveData<>();
    public LiveData<Map<String, Integer>> date = _date;

    private MutableLiveData<LoadingState> _state = new MutableLiveData<>();
    public LiveData<LoadingState> state = _state;

    public ScheduleViewModel(FirebaseScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        Calendar calendar = Calendar.getInstance();
        String currentDate = sdf.format(calendar.getTime());

        Map<String, Integer> date = new HashMap<>();
        date.put("year", calendar.get(Calendar.YEAR));
        date.put("month", calendar.get(Calendar.MONTH));
        date.put("dayOfMonth", calendar.get(Calendar.DAY_OF_MONTH));

        _date.setValue(date);

        loadDailySchedule(currentDate);
    }

    public void setDate(Map<String, Integer> date) {
        _date.setValue(date);
    }

    public void loadDailySchedule(String date) {
        _state.setValue(LoadingState.PENDING);
        scheduleRepository.loadDailySchedule(date, new FirebaseScheduleRepository.ScheduleLoadListener() {
            @Override
            public void onSuccess(List<Lesson> dailySchedule) {
                if (dailySchedule.isEmpty()) {
                    _state.setValue(LoadingState.EMPTY);
                } else {
                    _dailySchedule.setValue(dailySchedule);
                    _state.setValue(LoadingState.SUCCESS);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("TAG", e.toString());
                _state.setValue(LoadingState.ERROR);
            }
        });
    }
}
