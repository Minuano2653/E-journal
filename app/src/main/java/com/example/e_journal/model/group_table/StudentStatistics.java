package com.example.e_journal.model.group_table;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    public StudentStatistics(String studentName) {
        this.studentName = studentName;
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

    public void calculateStatistics(Map<Integer, Map<String, String>> months) {

        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DAY_OF_MONTH, 1);

        int presences = 0;
        int absences = 0;
        int excusedAbsences = 0;
        double totalScore = 0;
        int scoreCount = 0;

        for (Map.Entry<Integer, Map<String, String>> monthEntry : months.entrySet()) {
            int monthNumber = monthEntry.getKey();
            Map<String, String> monthMap = monthEntry.getValue();

            for (Map.Entry<String, String> entry : monthMap.entrySet()) {

                Calendar dateKey = Calendar.getInstance();
                dateKey.set(Calendar.MONTH, monthNumber);
                dateKey.set(Calendar.DAY_OF_MONTH, Integer.parseInt(entry.getKey()));
                String value = entry.getValue();

                if (dateKey.after(currentDate)) {
                    continue;
                }


                switch (value) {
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        presences++;
                        totalScore += Integer.parseInt(value);
                        scoreCount++;
                        break;
                    case "":
                        presences++;
                        break;
                    case "Н":
                        absences++;
                        break;
                    case "У":
                        excusedAbsences++;
                        break;
                }
            }
        }

        presenceCount = String.valueOf(presences);
        absenceCount = String.valueOf(absences);
        excusedAbsenceCount = String.valueOf(excusedAbsences);
        this.averageGrade = (scoreCount > 0) ? String.format("%.2f", totalScore / scoreCount) : "0.00";

    }
}
