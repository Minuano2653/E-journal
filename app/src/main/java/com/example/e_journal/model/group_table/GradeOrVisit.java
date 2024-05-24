package com.example.e_journal.model.group_table;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class GradeOrVisit implements Parcelable {
    private String studentName;
    private String value;
    private String date;

    protected GradeOrVisit(Parcel in) {
        studentName = in.readString();
        value = in.readString();
        date = in.readString();
    }

    public GradeOrVisit() {}

    public GradeOrVisit(String value, String date, String studentName) {
        this.date = date;
        this.value = value;
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(studentName);
        dest.writeString(value);
        dest.writeString(date);
    }

    public static final Creator<GradeOrVisit> CREATOR = new Creator<GradeOrVisit>() {
        @Override
        public GradeOrVisit createFromParcel(Parcel in) {
            return new GradeOrVisit(in);
        }

        @Override
        public GradeOrVisit[] newArray(int size) {
            return new GradeOrVisit[size];
        }
    };

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
