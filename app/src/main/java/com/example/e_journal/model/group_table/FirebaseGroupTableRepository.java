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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FirebaseGroupTableRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface GroupTableLoadListener {
        void onSuccess(List<Student> students);
        void onError(Exception e);
    }

    public void saveGradeOrVisit(String groupName, GradeOrVisit gradeOrVisit, String month, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("maths teachers")
                .document(uid)
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

    public void loadStudents(String groupName, String month, GroupTableLoadListener listener) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("maths teachers")
                .document(uid)
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
