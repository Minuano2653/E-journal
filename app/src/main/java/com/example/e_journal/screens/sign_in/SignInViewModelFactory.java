package com.example.e_journal.screens.sign_in;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_journal.model.auth.AuthRepository;

public class SignInViewModelFactory implements ViewModelProvider.Factory {
    private final AuthRepository authRepository;

    public SignInViewModelFactory(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignInViewModel.class)) {
            return (T) new SignInViewModel(authRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
