package com.example.e_journal.screens.group_table;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_journal.model.group_table.FirebaseGroupTableRepository;
import com.example.e_journal.screens.groups.GroupsViewModel;

public class GroupTableViewModelFactory implements ViewModelProvider.Factory {
    private final FirebaseGroupTableRepository groupTableRepository;

    public GroupTableViewModelFactory(FirebaseGroupTableRepository groupTableRepository) {
        this.groupTableRepository = groupTableRepository;
    }



    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GroupTableViewModel.class)) {
            return (T) new GroupTableViewModel(groupTableRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
