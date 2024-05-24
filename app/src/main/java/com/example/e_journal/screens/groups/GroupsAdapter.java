package com.example.e_journal.screens.groups;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_journal.R;
import com.example.e_journal.databinding.ItemGroupBinding;
import com.example.e_journal.model.groups.Group;

import java.util.ArrayList;
import java.util.List;


public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> implements View.OnClickListener {

    private List<Group> groups = new ArrayList<>();
    private List<Group> groupsCopy = new ArrayList<>();

    public void setGroups(List<Group> groups) {
        /*this.groups = groups;
        notifyDataSetChanged();*/
        this.groups = groups;
        groupsCopy.clear();
        groupsCopy.addAll(groups);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGroupBinding binding = ItemGroupBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new GroupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Group group = groups.get(position);
        holder.itemView.setTag(group);
        holder.binding.classNameTextView.setText(group.getName());
        holder.binding.studentsNumberTextView.setText(context.getString(R.string.students_number_text, group.getStudentsNumber()));
        holder.binding.getRoot().setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    @Override
    public void onClick(View v) {
        Group group = (Group) v.getTag();
    }

    public void filterByName(String query) {
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
        groups = filteredGroups;
        notifyDataSetChanged();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        ItemGroupBinding binding;

        public GroupViewHolder(ItemGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
