package com.example.groupproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.groupproject.data.db.AppDatabase;
import com.example.groupproject.data.model.Project;
import com.example.groupproject.data.model.ProjectChecklistState;
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

        ensureDefaultProjectData();

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_tasks) {
                loadFragment(new TasksFragment());
                return true;
            } else if (itemId == R.id.nav_meetings) {
                loadFragment(new MeetingsFragment());
                return true;
            } else if (itemId == R.id.nav_checklist) {
                loadFragment(new ChecklistFragment());
                return true;
            }

            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void ensureDefaultProjectData() {
        AppDatabase database = AppDatabase.getInstance(this);

        if (database.projectDao().getProjectCount() == 0) {
            Project defaultProject = new Project(
                    "ProjectPilot",
                    "COMP7506B",
                    "2026-05-03",
                    "Default workspace project"
            );

            long insertedProjectId = database.projectDao().insert(defaultProject);
            int projectId = (int) insertedProjectId;

            ProjectChecklistState checklistState = new ProjectChecklistState(
                    projectId,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false
            );
            database.projectChecklistDao().insert(checklistState);

            CurrentProjectManager.setCurrentProjectId(this, projectId);
        } else {
            int currentProjectId = CurrentProjectManager.getCurrentProjectId(this);

            if (database.projectDao().getProjectById(currentProjectId) == null) {
                Project firstProject = database.projectDao().getAllProjects().get(0);
                CurrentProjectManager.setCurrentProjectId(this, firstProject.getId());

                if (database.projectChecklistDao().getChecklistByProjectId(firstProject.getId()) == null) {
                    ProjectChecklistState checklistState = new ProjectChecklistState(
                            firstProject.getId(),
                            false,
                            false,
                            false,
                            false,
                            false,
                            false
                    );
                    database.projectChecklistDao().insert(checklistState);
                }
            } else {
                if (database.projectChecklistDao().getChecklistByProjectId(currentProjectId) == null) {
                    ProjectChecklistState checklistState = new ProjectChecklistState(
                            currentProjectId,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false
                    );
                    database.projectChecklistDao().insert(checklistState);
                }
            }
        }
    }
}