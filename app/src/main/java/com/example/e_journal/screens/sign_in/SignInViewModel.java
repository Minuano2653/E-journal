package com.example.e_journal.screens.sign_in;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.e_journal.model.auth.AuthRepository;

public class SignInViewModel extends ViewModel {

    private final AuthRepository authRepository;

    SignInViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
        if (authRepository.isSignedIn()) _authStatus.setValue(AuthStatus.SUCCESS);
    }

    private MutableLiveData<AuthStatus> _authStatus = new MutableLiveData<>();
    public LiveData<AuthStatus> authStatus = _authStatus;

    public void onSignIn(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            _authStatus.setValue(AuthStatus.INVALID_INPUT);
            return;
        }
        _authStatus.setValue(AuthStatus.PENDING);
        authRepository.SignInWithEmailAndPassword(email, password, task -> {
            if (task.isSuccessful()) {
                _authStatus.postValue(AuthStatus.SUCCESS);
            } else {
                _authStatus.postValue(AuthStatus.FAILURE);
            }
        });
    }
}

enum AuthStatus {
    PENDING,
    SUCCESS,
    INVALID_INPUT,
    FAILURE,
}
