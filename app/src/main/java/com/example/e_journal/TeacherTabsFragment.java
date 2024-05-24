package com.example.e_journal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.e_journal.databinding.FragmentTeacherTabsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherTabsFragment extends Fragment {
    FragmentTeacherTabsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_bar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //TODO: вот тут несоответствие MVVM
        if (item.getItemId() == R.id.logout) {

            NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

            if (navHostFragment != null) {
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_teacherTabsFragment_to_signInFragment);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                return true;
            }

        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTeacherTabsBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(binding.toolbar);

            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_bottom);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.scheduleFragment, R.id.groupsFragment
            ).build();

            NavigationUI.setupActionBarWithNavController(activity, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);
        }
    }
}
