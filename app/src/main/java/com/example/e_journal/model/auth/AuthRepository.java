package com.example.e_journal.model.auth;

import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

public interface AuthRepository {
    void SignInWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> onCompleteListener);

    boolean isSignedIn();

    void logout();
}
