package com.example.groupproject.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.groupproject.R;
import com.example.groupproject.data.db.AppDatabase;
import com.example.groupproject.data.model.Meeting;
import com.example.groupproject.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvProjectSubinfo;
    private TextView tvTasksHighlight;
    private TextView tvTasksSubinfo;
    private TextView tvMeetingsHighlight;
    private TextView tvMeetingsSubinfo;
    private TextView tvChecklistHighlight;
    private TextView tvChecklistSubinfo;
    private TextView tvRecentTaskTitle;
    private TextView tvRecentTaskMeta;
    private TextView tvRecentMeetingTitle;
    private TextView tvRecentMeetingMeta;

    private final List<Task> taskList = new ArrayList<>();
    private final List<Meeting> meetingList = new ArrayList<>();

    private static final String PREF_NAME = "checklist_prefs";
    private static final String KEY_REPORT = "report_completed";
    private static final String KEY_SOURCE = "source_code_ready";
    private static final String KEY_README = "readme_ready";
    private static final String KEY_SLIDES = "slides_ready";
    private static final String KEY_DEMO = "demo_ready";
    private static final String KEY_VIDEO = "video_ready";

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvProjectSubinfo = view.findViewById(R.id.tv_project_subinfo);
        tvTasksHighlight = view.findViewById(R.id.tv_tasks_highlight);
        tvTasksSubinfo = view.findViewById(R.id.tv_tasks_subinfo);
        tvMeetingsHighlight = view.findViewById(R.id.tv_meetings_highlight);
        tvMeetingsSubinfo = view.findViewById(R.id.tv_meetings_subinfo);
        tvChecklistHighlight = view.findViewById(R.id.tv_checklist_highlight);
        tvChecklistSubinfo = view.findViewById(R.id.tv_checklist_subinfo);
        tvRecentTaskTitle = view.findViewById(R.id.tv_recent_task_title);
        tvRecentTaskMeta = view.findViewById(R.id.tv_recent_task_meta);
        tvRecentMeetingTitle = view.findViewById(R.id.tv_recent_meeting_title);
        tvRecentMeetingMeta = view.findViewById(R.id.tv_recent_meeting_meta);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        if (getContext() == null) return;

        taskList.clear();
        taskList.addAll(
                AppDatabase.getInstance(getContext())
                        .taskDao()
                        .getAllTasks()
        );

        meetingList.clear();
        meetingList.addAll(
                AppDatabase.getInstance(getContext())
                        .meetingDao()
                        .getAllMeetings()
        );

        updateDashboard();
    }

    private void updateDashboard() {
        int totalTasks = taskList.size();
        int completedTasks = 0;

        for (Task task : taskList) {
            if ("Done".equals(task.getStatus())) {
                completedTasks++;
            }
        }

        int pendingTasks = totalTasks - completedTasks;

        tvProjectSubinfo.setText(
                "COMP7506B workspace\n" +
                        totalTasks + " task" + (totalTasks == 1 ? "" : "s") + " · " +
                        meetingList.size() + " meeting" + (meetingList.size() == 1 ? "" : "s")
        );

        tvTasksHighlight.setText(pendingTasks + " active");
        if (completedTasks == 0) {
            tvTasksSubinfo.setText("Nothing completed yet");
        } else {
            tvTasksSubinfo.setText(completedTasks + " completed so far");
        }

        if (meetingList.isEmpty()) {
            tvMeetingsHighlight.setText("No recent");
            tvMeetingsSubinfo.setText("Capture notes and decisions");
        } else {
            Meeting latestMeeting = meetingList.get(0);
            tvMeetingsHighlight.setText(truncateSingleLine(latestMeeting.getTitle(), 16));
            tvMeetingsSubinfo.setText("Latest meeting updated");
        }

        SharedPreferences sharedPreferences =
                requireContext().getSharedPreferences(PREF_NAME, 0);

        int checklistCompleted = 0;
        if (sharedPreferences.getBoolean(KEY_REPORT, false)) checklistCompleted++;
        if (sharedPreferences.getBoolean(KEY_SOURCE, false)) checklistCompleted++;
        if (sharedPreferences.getBoolean(KEY_README, false)) checklistCompleted++;
        if (sharedPreferences.getBoolean(KEY_SLIDES, false)) checklistCompleted++;
        if (sharedPreferences.getBoolean(KEY_DEMO, false)) checklistCompleted++;
        if (sharedPreferences.getBoolean(KEY_VIDEO, false)) checklistCompleted++;

        tvChecklistHighlight.setText(checklistCompleted + " / 6 ready");
        tvChecklistSubinfo.setText("Submission progress");

        if (taskList.isEmpty()) {
            tvRecentTaskTitle.setText("No tasks yet");
            tvRecentTaskMeta.setText("Create your first task");
        } else {
            Task latestTask = taskList.get(0);
            tvRecentTaskTitle.setText(truncateSingleLine(latestTask.getTitle(), 26));
            tvRecentTaskMeta.setText(
                    truncateSingleLine(latestTask.getAssignee(), 14) +
                            " · " +
                            truncateSingleLine(latestTask.getDueDate(), 14)
            );
        }

        if (meetingList.isEmpty()) {
            tvRecentMeetingTitle.setText("No meetings yet");
            tvRecentMeetingMeta.setText("Add a meeting note");
        } else {
            Meeting latestMeeting = meetingList.get(0);
            tvRecentMeetingTitle.setText(truncateSingleLine(latestMeeting.getTitle(), 26));
            tvRecentMeetingMeta.setText(
                    truncateMultiLine(latestMeeting.getNotes(), 28)
            );
        }
    }

    private String truncateSingleLine(String text, int maxLength) {
        if (text == null) return "";
        text = text.trim();
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }

    private String truncateMultiLine(String text, int maxLength) {
        if (text == null) return "";
        text = text.trim().replace("\n", " ");
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }
}