package com.example.e_journal.screens.groups;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_journal.R;
import com.example.e_journal.Repositories;
import com.example.e_journal.databinding.FragmentGroupsBinding;
import com.example.e_journal.utlis.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class GroupsFragment extends Fragment {
    private FragmentGroupsBinding binding;
    private GroupsViewModel viewModel;
    private GroupsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroupsViewModelFactory factory = new GroupsViewModelFactory(Repositories.getGroupsRepository());
        viewModel = new ViewModelProvider(this, factory).get(GroupsViewModel.class);
        adapter = new GroupsAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGroupsBinding.inflate(inflater, container, false);

        setupRecyclerView();
        setupSearchView();

        observeGroups();
        observeViewModelState();

        return binding.getRoot();
    }

    private void observeGroups() {
        viewModel.groups.observe(getViewLifecycleOwner(), groups -> adapter.setGroups(groups));
    }

    private void observeViewModelState() {
        viewModel.state.observe(getViewLifecycleOwner(), state -> {
            hideAll();
            switch (state) {
                case PENDING:
                    binding.groupsProgressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.groupsProgressBar.setVisibility(View.GONE);
                    binding.groupsRecyclerView.setVisibility(View.VISIBLE);
                    break;
                case EMPTY:
                    binding.groupsProgressBar.setVisibility(View.GONE);
                    binding.groupsNotFoundTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), R.string.empty_load_response, Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    binding.groupsNotFoundTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), R.string.error_load_response, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.groupsRecyclerView.setLayoutManager(layoutManager);
        binding.groupsRecyclerView.setAdapter(adapter);
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filterByName(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filterByName(newText);
                return true;
            }
        });
    }

    private void hideAll() {
        binding.groupsRecyclerView.setVisibility(View.GONE);
        binding.groupsNotFoundTextView.setVisibility(View.GONE);
        binding.groupsProgressBar.setVisibility(View.GONE);
    }
}
