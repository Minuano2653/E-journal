package com.example.e_journal.model.auth;

import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthRepository implements AuthRepository {
        private FirebaseAuth auth = FirebaseAuth.getInstance();

        @Override
        public void SignInWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
        }

        @Override
        public boolean isSignedIn() {
            return auth.getCurrentUser() != null;
        }


        @Override
        public void logout() {
            auth.signOut();
        }
    }
