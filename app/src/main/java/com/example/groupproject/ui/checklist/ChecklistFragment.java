package com.example.groupproject.ui.checklist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.groupproject.R;

public class ChecklistFragment extends Fragment {

    private CheckBox cbReportCompleted;
    private CheckBox cbSourceCodeReady;
    private CheckBox cbReadmeReady;
    private CheckBox cbSlidesReady;
    private CheckBox cbDemoReady;
    private CheckBox cbVideoReady;
    private TextView tvChecklistSummary;

    private SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "checklist_prefs";
    private static final String KEY_REPORT = "report_completed";
    private static final String KEY_SOURCE = "source_code_ready";
    private static final String KEY_README = "readme_ready";
    private static final String KEY_SLIDES = "slides_ready";
    private static final String KEY_DEMO = "demo_ready";
    private static final String KEY_VIDEO = "video_ready";

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

        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, 0);

        loadChecklistState();
        setupListeners();
        updateSummary();

        return view;
    }

    private void loadChecklistState() {
        cbReportCompleted.setChecked(sharedPreferences.getBoolean(KEY_REPORT, false));
        cbSourceCodeReady.setChecked(sharedPreferences.getBoolean(KEY_SOURCE, false));
        cbReadmeReady.setChecked(sharedPreferences.getBoolean(KEY_README, false));
        cbSlidesReady.setChecked(sharedPreferences.getBoolean(KEY_SLIDES, false));
        cbDemoReady.setChecked(sharedPreferences.getBoolean(KEY_DEMO, false));
        cbVideoReady.setChecked(sharedPreferences.getBoolean(KEY_VIDEO, false));
    }

    private void setupListeners() {
        cbReportCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveBoolean(KEY_REPORT, isChecked);
            updateSummary();
        });

        cbSourceCodeReady.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveBoolean(KEY_SOURCE, isChecked);
            updateSummary();
        });

        cbReadmeReady.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveBoolean(KEY_README, isChecked);
            updateSummary();
        });

        cbSlidesReady.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveBoolean(KEY_SLIDES, isChecked);
            updateSummary();
        });

        cbDemoReady.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveBoolean(KEY_DEMO, isChecked);
            updateSummary();
        });

        cbVideoReady.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveBoolean(KEY_VIDEO, isChecked);
            updateSummary();
        });
    }

    private void saveBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    private void updateSummary() {
        int completedCount = 0;

        if (cbReportCompleted.isChecked()) completedCount++;
        if (cbSourceCodeReady.isChecked()) completedCount++;
        if (cbReadmeReady.isChecked()) completedCount++;
        if (cbSlidesReady.isChecked()) completedCount++;
        if (cbDemoReady.isChecked()) completedCount++;
        if (cbVideoReady.isChecked()) completedCount++;

        tvChecklistSummary.setText(completedCount + " of 6 completed");
    }
}