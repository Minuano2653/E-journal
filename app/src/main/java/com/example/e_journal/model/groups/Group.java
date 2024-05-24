package com.example.e_journal.model.groups;

import androidx.annotation.NonNull;

public class Group {
    public Group() {}
    private String UID;

    private String name;
    private String studentsNumber;

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public String getUID() {
        return UID;
    }

    public String getStudentsNumber() {
        return studentsNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return "name: " + name + " studentsNumber: " + studentsNumber;
    }
}
