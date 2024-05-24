package com.example.e_journal.model.group_table;

import androidx.annotation.NonNull;

import java.util.List;

public class Student {
    private String name;
    private List<String> gradesAndVisits;

    Student() {}

    public Student(String name, List<String> gradesAndVisits) {
        this.name = name;
        this.gradesAndVisits = gradesAndVisits;
    }

    public String getName() {
        return name;
    }

    public List<String> getGradesAndVisits() {
        return gradesAndVisits;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: " + name + " Grades: " + gradesAndVisits.toString();
    }
}
