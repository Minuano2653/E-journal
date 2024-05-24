package com.example.e_journal.model.schedule;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FirebaseScheduleRepository {

    public interface ScheduleLoadListener {
        void onSuccess(List<Lesson> dailySchedule);
        void onError(Exception e);
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void loadDailySchedule(String date, ScheduleLoadListener scheduleLoadListener) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        List<Lesson> dailySchedule = new ArrayList<>();

        db.collection("maths teachers")
                .document(uid)
                .collection("schedule")
                .document(date)
                .collection("lessons").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.exists()) {
                            Lesson lesson = document.toObject(Lesson.class);
                            dailySchedule.add(lesson);
                        }
                    }
                    scheduleLoadListener.onSuccess(dailySchedule);

                }).addOnFailureListener(e -> scheduleLoadListener.onError(e));

    }
}
