package com.example.e_journal.model.group_table;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.e_journal.model.groups.FirebaseGroupsRepository;
import com.example.e_journal.model.homework.Homework;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FirebaseGroupTableRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String teacherUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public interface GroupTableLoadListener {
        void onSuccess(List<Student> students);
        void onError(Exception e);
    }

    public interface StudentStatisticsLoadListener {
        void onSuccess(StudentStatistics studentStatistics);
        void onError(Exception e);
    }

    public void saveGradeOrVisit(String groupName, GradeOrVisit gradeOrVisit, String month, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {

        db.collection("maths teachers")
                .document(teacherUID)
                .get().addOnSuccessListener(teacherSnapshot -> {
                    String subject = teacherSnapshot.getString("position");

                    db.collection("classes")
                            .document(groupName)
                            .collection("students")
                            .whereEqualTo("name", gradeOrVisit.getStudentName())
                            .limit(1)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    DocumentSnapshot studentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                    DocumentReference studentRef = studentSnapshot.getReference();

                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put(month + "." + gradeOrVisit.getDate(), gradeOrVisit.getValue());

                                    studentRef.collection("subjects")
                                            .document(subject).update(updates)
                                            .addOnSuccessListener(onSuccessListener)
                                            .addOnFailureListener(onFailureListener);
                                }
                            }).addOnFailureListener(onFailureListener);

                }).addOnFailureListener(onFailureListener);
    }

    public void getStudentStatistics(String studentName, String groupName, int month, StudentStatisticsLoadListener listener) {

        db.collection("maths teachers")
                .document(teacherUID)
                .get().addOnSuccessListener(teacherSnapshot -> {
                    String subject = teacherSnapshot.getString("position");

                    db.collection("classes")
                            .document(groupName)
                            .collection("students")
                            .whereEqualTo("name", studentName)
                            .limit(1)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    DocumentSnapshot studentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                    DocumentReference studentRef = studentSnapshot.getReference();

                                    studentRef.collection("subjects")
                                            .document(subject)
                                            .get().addOnSuccessListener(documentSnapshot -> {
                                                Map<Integer,Map<String, String>> months = new HashMap<>();
                                                if (month == 8 || month == 9) {
                                                    Map<String, String> septemberMap = (Map<String, String>) documentSnapshot.get("september");
                                                    Map<String, String> octoberMap = (Map<String, String>) documentSnapshot.get("october");
                                                    months.put(8, septemberMap);
                                                    months.put(9, octoberMap);
                                                } else if (month == 10 || month == 11) {
                                                    Map<String, String> novemberMap = (Map<String, String>) documentSnapshot.get("november");
                                                    Map<String, String> decemberMap = (Map<String, String>) documentSnapshot.get("december");
                                                    months.put(10, novemberMap);
                                                    months.put(11, decemberMap);
                                                } else if (month == 0 || month == 1 || month == 2) {
                                                    Map<String, String> januaryMap = (Map<String, String>) documentSnapshot.get("january");
                                                    Map<String, String> februaryMap = (Map<String, String>) documentSnapshot.get("february");
                                                    Map<String, String> marchMap = (Map<String, String>) documentSnapshot.get("march");
                                                    months.put(0, januaryMap);
                                                    months.put(1, februaryMap);
                                                    months.put(2, marchMap);
                                                } else if (month == 3 || month == 4) {
                                                    Map<String, String> aprilMap = (Map<String, String>) documentSnapshot.get("april");
                                                    Map<String, String> mayMap = (Map<String, String>) documentSnapshot.get("may");
                                                    months.put(3, aprilMap);
                                                    months.put(4, mayMap);
                                                } else {
                                                    throw new IllegalArgumentException("No matching month");
                                                }
                                                Map<String, String> studentStatistics = calculateStatistics(months);

                                                studentRef.collection("subjects")
                                                        .document(subject)
                                                        .set(studentStatistics, SetOptions.merge())
                                                        .addOnSuccessListener(unused -> {
                                                            listener.onSuccess(new StudentStatistics(
                                                                    studentName,
                                                                    studentStatistics.get("presenceCount"),
                                                                    studentStatistics.get("absenceCount"),
                                                                    studentStatistics.get("excusedAbsenceCount"),
                                                                    studentStatistics.get("averageGrade")
                                                                    ));
                                                        })
                                                        .addOnFailureListener(listener::onError);
                                            }).addOnFailureListener(listener::onError);
                                }
                            }).addOnFailureListener(listener::onError);

                }).addOnFailureListener(listener::onError);

    }

    private Map<String, String> calculateStatistics(Map<Integer, Map<String, String>> months) {
        Calendar currentDate = Calendar.getInstance();
        int presences = 0;
        int absences = 0;
        int excusedAbsences = 0;
        double totalScore = 0;
        int scoreCount = 0;
        String averageGrade;

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
        averageGrade = (scoreCount > 0) ? String.format("%.2f", totalScore / scoreCount) : "0.00";
        Map<String, String> studentStatistics = new HashMap<>();
        studentStatistics.put("presenceCount", String.valueOf(presences));
        studentStatistics.put("absenceCount", String.valueOf(absences));
        studentStatistics.put("excusedAbsenceCount", String.valueOf(excusedAbsences));
        studentStatistics.put("averageGrade", averageGrade);

        return studentStatistics;
    }

    public void loadStudents(String groupName, String month, GroupTableLoadListener listener) {

        db.collection("maths teachers")
                .document(teacherUID)
                .get().addOnSuccessListener(teacherSnapshot -> {
                    String subject = teacherSnapshot.getString("position");

                    List<Student> students = new ArrayList<>();

                    db.collection("classes")
                            .document(groupName)
                            .collection("students")
                            .orderBy("name", Query.Direction.ASCENDING)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    int expectedSize = queryDocumentSnapshots.size();
                                    AtomicInteger loadedSize = new AtomicInteger(0);

                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        String studentName = (String) documentSnapshot.get("name");

                                        DocumentReference studentRef = documentSnapshot.getReference();

                                        studentRef.collection("subjects")
                                                .document(subject)
                                                .get()
                                                .addOnSuccessListener(subjectSnapshot -> {

                                                    Map<String, String> gradesAndVisitsMap = (Map<String, String>) subjectSnapshot.get(month);
                                                    List<String> gradesAndVisitsList = new ArrayList<>();

                                                    for (int i = 1; i <= gradesAndVisitsMap.size(); i++) {
                                                        String date = String.format("%02d", i);
                                                        gradesAndVisitsList.add(gradesAndVisitsMap.get(date));
                                                    }
                                                    students.add(new Student(studentName, gradesAndVisitsList));

                                                    if (loadedSize.incrementAndGet() == expectedSize) {
                                                        students.sort(Comparator.comparing(Student::getName));
                                                        listener.onSuccess(students);
                                                    }
                                                }).addOnFailureListener(e -> listener.onError(e));

                                    }

                                } else {
                                    listener.onSuccess(null);
                                }
                            }).addOnFailureListener(e -> listener.onError(e));

                }).addOnFailureListener(e -> listener.onError(e));


    }
}
