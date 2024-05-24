package com.example.e_journal.screens.groups;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.e_journal.model.groups.FirebaseGroupsRepository;
import com.example.e_journal.model.groups.Group;
import com.example.e_journal.utlis.Event;
import com.example.e_journal.utlis.LoadingState;

import java.util.ArrayList;
import java.util.List;

public class GroupsViewModel extends ViewModel {

    private FirebaseGroupsRepository groupsRepository;

    private MutableLiveData<List<Group>> _groups = new MutableLiveData<>();
    public LiveData<List<Group>> groups = _groups;

    private MutableLiveData<LoadingState> _state = new MutableLiveData<>();
    public LiveData<LoadingState> state = _state;

    private List<Group> groupsCopy = new ArrayList<>();

    public GroupsViewModel(FirebaseGroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
        loadGroups();
    }

    public void loadGroups() {
        _state.setValue(LoadingState.PENDING);
        groupsRepository.loadGroups(new FirebaseGroupsRepository.GroupsLoadListener() {
            @Override
            public void onSuccess(List<Group> groups) {
                if (groups.isEmpty()) {
                    _state.setValue(LoadingState.EMPTY);
                } else {
                    _groups.setValue(groups); //
                    _state.setValue(LoadingState.SUCCESS);
                    groupsCopy.addAll(groups);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("TAG", e.toString());
                _state.setValue(LoadingState.ERROR);
            }
        });
    }

    public void filterGroups(String query) {
        List<Group> filteredGroups = new ArrayList<>();
        if (query.isEmpty()) {
            filteredGroups.addAll(groupsCopy);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Group group : groupsCopy) {
                if (group.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredGroups.add(group);
                }
            }
        }
        _groups.setValue(filteredGroups);
    }
}
