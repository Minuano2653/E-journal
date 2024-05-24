package com.example.e_journal.screens.groups;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_journal.model.groups.FirebaseGroupsRepository;

public class GroupsViewModelFactory implements ViewModelProvider.Factory {
    private final FirebaseGroupsRepository groupsRepository;

    public GroupsViewModelFactory(FirebaseGroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GroupsViewModel.class)) {
            return (T) new GroupsViewModel(groupsRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
