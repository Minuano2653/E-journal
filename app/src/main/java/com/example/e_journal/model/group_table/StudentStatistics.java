package com.example.e_journal.model.group_table;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class StudentStatistics implements Parcelable {
    private String studentName;
    private String presenceCount;
    private String absenceCount;
    private String excusedAbsenceCount;
    private String averageGrade;

    public StudentStatistics(String studentName, String presenceCount, String absenceCount, String excusedAbsenceCount, String averageGrade) {
        this.studentName = studentName;
        this.presenceCount = presenceCount;
        this.absenceCount = absenceCount;
        this.excusedAbsenceCount = excusedAbsenceCount;
        this.averageGrade = averageGrade;
    }

    protected StudentStatistics(Parcel in) {
        studentName = in.readString();
        presenceCount = in.readString();
        absenceCount = in.readString();
        excusedAbsenceCount = in.readString();
        averageGrade = in.readString();
    }

    public static final Creator<StudentStatistics> CREATOR = new Creator<StudentStatistics>() {
        @Override
        public StudentStatistics createFromParcel(Parcel in) {
            return new StudentStatistics(in);
        }

        @Override
        public StudentStatistics[] newArray(int size) {
            return new StudentStatistics[size];
        }
    };

    public String getStudentName() {
        return studentName;
    }

    public String getPresenceCount() {
        return presenceCount;
    }

    public String getAbsenceCount() {
        return absenceCount;
    }

    public String getExcusedAbsenceCount() {
        return excusedAbsenceCount;
    }

    public String getAverageGrade() {
        return averageGrade;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(studentName);
        dest.writeString(presenceCount);
        dest.writeString(absenceCount);
        dest.writeString(excusedAbsenceCount);
        dest.writeString(averageGrade);
    }
}
