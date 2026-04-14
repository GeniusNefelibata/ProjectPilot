package com.example.groupproject.ui.checklist;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.groupproject.CurrentProjectManager;
import com.example.groupproject.R;
import com.example.groupproject.data.db.AppDatabase;
import com.example.groupproject.data.model.ProjectChecklistState;

public class ChecklistFragment extends Fragment {

    private CheckBox cbReportCompleted;
    private CheckBox cbSourceCodeReady;
    private CheckBox cbReadmeReady;
    private CheckBox cbSlidesReady;
    private CheckBox cbDemoReady;
    private CheckBox cbVideoReady;
    private TextView tvChecklistSummary;

    private ProjectChecklistState currentChecklistState;
    private boolean isBindingState = false;

    public ChecklistFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist, container, false);

        cbReportCompleted = view.findViewById(R.id.cb_report_completed);
        cbSourceCodeReady = view.findViewById(R.id.cb_source_code_ready);
        cbReadmeReady = view.findViewById(R.id.cb_readme_ready);
        cbSlidesReady = view.findViewById(R.id.cb_slides_ready);
        cbDemoReady = view.findViewById(R.id.cb_demo_ready);
        cbVideoReady = view.findViewById(R.id.cb_video_ready);
        tvChecklistSummary = view.findViewById(R.id.tv_checklist_summary);

        setupListeners();
        loadChecklistState();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadChecklistState();
    }

    private void loadChecklistState() {
        if (getContext() == null) return;

        AppDatabase database = AppDatabase.getInstance(getContext());
        int currentProjectId = CurrentProjectManager.getCurrentProjectId(getContext());

        currentChecklistState = database.projectChecklistDao().getChecklistByProjectId(currentProjectId);

        if (currentChecklistState == null) {
            currentChecklistState = new ProjectChecklistState(
                    currentProjectId,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false
            );
            long insertedId = database.projectChecklistDao().insert(currentChecklistState);
            currentChecklistState.setId((int) insertedId);
        }

        bindChecklistStateToViews();
        updateSummary();
    }

    private void bindChecklistStateToViews() {
        if (currentChecklistState == null) return;

        isBindingState = true;

        cbReportCompleted.setChecked(currentChecklistState.isReportCompleted());
        cbSourceCodeReady.setChecked(currentChecklistState.isSourceCodeReady());
        cbReadmeReady.setChecked(currentChecklistState.isReadmeReady());
        cbSlidesReady.setChecked(currentChecklistState.isSlidesReady());
        cbDemoReady.setChecked(currentChecklistState.isDemoReady());
        cbVideoReady.setChecked(currentChecklistState.isVideoReady());

        isBindingState = false;
    }

    private void setupListeners() {
        cbReportCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isBindingState || currentChecklistState == null) return;
            currentChecklistState.setReportCompleted(isChecked);
            saveChecklistState();
        });

        cbSourceCodeReady.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isBindingState || currentChecklistState == null) return;
            currentChecklistState.setSourceCodeReady(isChecked);
            saveChecklistState();
        });

        cbReadmeReady.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isBindingState || currentChecklistState == null) return;
            currentChecklistState.setReadmeReady(isChecked);
            saveChecklistState();
        });

        cbSlidesReady.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isBindingState || currentChecklistState == null) return;
            currentChecklistState.setSlidesReady(isChecked);
            saveChecklistState();
        });

        cbDemoReady.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isBindingState || currentChecklistState == null) return;
            currentChecklistState.setDemoReady(isChecked);
            saveChecklistState();
        });

        cbVideoReady.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isBindingState || currentChecklistState == null) return;
            currentChecklistState.setVideoReady(isChecked);
            saveChecklistState();
        });
    }

    private void saveChecklistState() {
        if (getContext() == null || currentChecklistState == null) return;

        AppDatabase.getInstance(getContext())
                .projectChecklistDao()
                .update(currentChecklistState);

        updateSummary();
    }

    private void updateSummary() {
        int completedCount = 0;

        if (currentChecklistState != null) {
            if (currentChecklistState.isReportCompleted()) completedCount++;
            if (currentChecklistState.isSourceCodeReady()) completedCount++;
            if (currentChecklistState.isReadmeReady()) completedCount++;
            if (currentChecklistState.isSlidesReady()) completedCount++;
            if (currentChecklistState.isDemoReady()) completedCount++;
            if (currentChecklistState.isVideoReady()) completedCount++;
        }

        tvChecklistSummary.setText(completedCount + " of 6 completed");
    }
}