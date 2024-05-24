package com.example.e_journal.model.homework;

public class Homework {
    private String groupName;
    private String date;
    private String description;
    private String subject;

    public Homework() {}

    public Homework(String groupName, String date, String description) {
        this.groupName = groupName;
        this.date = date;
        this.description = description;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
