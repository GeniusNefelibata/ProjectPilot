package com.example.groupproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.groupproject.ui.checklist.ChecklistFragment;
import com.example.groupproject.ui.home.HomeFragment;
import com.example.groupproject.ui.meetings.MeetingsFragment;
import com.example.groupproject.ui.tasks.TasksFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            switchFragment(new HomeFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                switchFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_tasks) {
                switchFragment(new TasksFragment());
                return true;
            } else if (itemId == R.id.nav_meetings) {
                switchFragment(new MeetingsFragment());
                return true;
            } else if (itemId == R.id.nav_checklist) {
                switchFragment(new ChecklistFragment());
                return true;
            }

            return false;
        });
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.fragment_enter,
                        R.anim.fragment_exit,
                        R.anim.fragment_enter,
                        R.anim.fragment_exit
                )
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}