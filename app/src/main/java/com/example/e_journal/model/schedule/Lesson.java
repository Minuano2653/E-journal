package com.example.e_journal.model.schedule;

public class Lesson {

    private String groupName;
    private String number;

    private String time;

    public Lesson() {}


    public String getGroupName() {
        return groupName;
    }

    public String getNumber() {
        return number;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Lesson { group: " + groupName + " number: " + number + " time: " + time + " }";
    }
}
