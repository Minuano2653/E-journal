package com.example.e_journal.screens.schedule;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_journal.model.groups.FirebaseGroupsRepository;
import com.example.e_journal.model.schedule.FirebaseScheduleRepository;
import com.example.e_journal.screens.groups.GroupsViewModel;

public class ScheduleViewModelFactory implements ViewModelProvider.Factory {
    private final FirebaseScheduleRepository scheduleRepository;

    public ScheduleViewModelFactory(FirebaseScheduleRepository groupsRepository) {
        this.scheduleRepository = groupsRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ScheduleViewModel.class)) {
            return (T) new ScheduleViewModel(scheduleRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
