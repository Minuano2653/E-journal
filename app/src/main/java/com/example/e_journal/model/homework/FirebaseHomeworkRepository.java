package com.example.e_journal.model.homework;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class FirebaseHomeworkRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface HomeworkLoadListener {
        void onSuccess(Homework homework);
        void onError(Exception e);
    }

    public void saveHomework(Homework homework, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("maths teachers")
                .document(uid)
                .get().addOnSuccessListener(documentSnapshot -> {
                    String subject = documentSnapshot.getString("position");
                    homework.setSubject(subject);

                    Map<String, Object> homeworkData = new HashMap<>();
                    homeworkData.put("date", homework.getDate());
                    homeworkData.put("description", homework.getDescription());
                    homeworkData.put("subject", subject);

                    CollectionReference homeworkCollection = db.collection("classes")
                            .document(homework.getGroupName())
                            .collection("homework");

                    homeworkCollection.whereEqualTo("date", homework.getDate())
                            .whereEqualTo("subject", homework.getSubject())
                            .limit(1)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    DocumentSnapshot homeworkDocument = queryDocumentSnapshots.getDocuments().get(0);
                                    DocumentReference homeworkDocumentRef = homeworkDocument.getReference();

                                    homeworkDocumentRef.set(homeworkData, SetOptions.merge())
                                            .addOnSuccessListener(onSuccessListener)
                                            .addOnFailureListener(onFailureListener);

                                } else {
                                    homeworkCollection.document().set(homeworkData)
                                            .addOnSuccessListener(onSuccessListener)
                                            .addOnFailureListener(onFailureListener);
                                }
                            }).addOnFailureListener(onFailureListener);

                }).addOnFailureListener(onFailureListener);


    }

    public void loadHomework(String groupName, String date, HomeworkLoadListener listener) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("maths teachers")
                .document(uid)
                .get().addOnSuccessListener(documentSnapshot -> {
                    String subject = documentSnapshot.getString("position");

                    db.collection("classes")
                            .document(groupName)
                            .collection("homework")
                            .whereEqualTo("date", date)
                            .whereEqualTo("subject", subject)
                            .limit(1)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                                    Homework homework = document.toObject(Homework.class);
                                    homework.setGroupName(groupName);
                                    listener.onSuccess(homework);
                                } else {
                                    listener.onSuccess(null);
                                }
                            }).addOnFailureListener(e -> {
                                listener.onError(e);
                            });
        }).addOnFailureListener(e -> {
            listener.onError(e);
        });
    }
}
