package com.example.e_journal.screens.sign_in;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.e_journal.R;
import com.example.e_journal.Repositories;
import com.example.e_journal.databinding.FragmentSignInBinding;


public class SignInFragment extends Fragment {
    private FragmentSignInBinding binding;
    private SignInViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SignInViewModelFactory factory = new SignInViewModelFactory(Repositories.getAuthRepository());
        viewModel = new ViewModelProvider(this, factory).get(SignInViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.enterAsTeacherButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            viewModel.onSignIn(email, password);
        });

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.authStatus.observe(getViewLifecycleOwner(), authStatus -> {
            if (authStatus == AuthStatus.PENDING) {
                binding.enterAsTeacherButton.setEnabled(false);
            }
            else if (authStatus == AuthStatus.SUCCESS) {
                launchTeacherTabsFragment();
                binding.enterAsTeacherButton.setEnabled(true);
            } else if (authStatus == AuthStatus.INVALID_INPUT) {
                binding.enterAsTeacherButton.setEnabled(true);
                Toast.makeText(requireContext(), "Неверный логин или пароль.\nПопробуйте ещё раз!", Toast.LENGTH_SHORT).show();
            } else if (authStatus == AuthStatus.FAILURE){
                binding.enterAsTeacherButton.setEnabled(true);
                Toast.makeText(requireContext(), "Ошибка входа.\nПопробуйте ещё раз!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchTeacherTabsFragment() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_signInFragment_to_teacherTabsFragment);
    }
}