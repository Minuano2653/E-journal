package com.example.e_journal.screens.homework;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_journal.model.homework.FirebaseHomeworkRepository;

public class HomeworkViewModelFactory implements ViewModelProvider.Factory {
    private final FirebaseHomeworkRepository homeworkRepository;

    public HomeworkViewModelFactory(FirebaseHomeworkRepository homeworkRepository) {
        this.homeworkRepository = homeworkRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeworkViewModel.class)) {
            return (T) new HomeworkViewModel(homeworkRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
