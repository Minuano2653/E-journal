package com.example.e_journal.model.groups;

import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;



public class FirebaseGroupsRepository {

    public interface GroupsLoadListener {
        void onSuccess(List<Group> groups);
        void onError(Exception e);
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void loadGroups(GroupsLoadListener groupsLoadListener) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference teacherRef = db.collection("maths teachers").document(uid);

        List<Group> groups = new ArrayList<>();

        teacherRef.get().addOnSuccessListener(documentSnapshot -> {
            List<DocumentReference> groupRefs = (List<DocumentReference>) documentSnapshot.get("classes");
            if (groupRefs != null) {
                for (DocumentReference groupRef : groupRefs) {
                    groupRef.get().addOnSuccessListener(groupDocument -> {
                        if (groupDocument.exists()) {
                            Group group = groupDocument.toObject(Group.class);
                            group.setUID(groupDocument.getId());
                            groups.add(group);
                            groupsLoadListener.onSuccess(groups);
                        }
                        else {
                            Log.d("RRRR", "DOESNT EXIST");
                        }
                    });
                }
            } else {
                Log.d("QQQQ", "EMPTY");
            }

        }).addOnFailureListener(e -> groupsLoadListener.onError(e));
    }
}
