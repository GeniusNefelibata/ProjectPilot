package com.example.groupproject.ui.home;
import android.app.DatePickerDialog;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.groupproject.CurrentProjectManager;
import com.example.groupproject.R;
import com.example.groupproject.data.db.AppDatabase;
import com.example.groupproject.data.model.Meeting;
import com.example.groupproject.data.model.Project;
import com.example.groupproject.data.model.ProjectChecklistState;
import com.example.groupproject.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvProjectName;
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

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvProjectName = view.findViewById(R.id.tv_project_name);
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

        View.OnClickListener switchProjectClickListener = v -> showProjectSwitcher();
        tvProjectName.setOnClickListener(switchProjectClickListener);
        tvProjectSubinfo.setOnClickListener(switchProjectClickListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        if (getContext() == null) return;

        AppDatabase database = AppDatabase.getInstance(getContext());
        int currentProjectId = CurrentProjectManager.getCurrentProjectId(getContext());

        taskList.clear();
        taskList.addAll(database.taskDao().getTasksByProjectId(currentProjectId));

        meetingList.clear();
        meetingList.addAll(database.meetingDao().getMeetingsByProjectId(currentProjectId));

        Project currentProject = database.projectDao().getProjectById(currentProjectId);
        ProjectChecklistState checklistState =
                database.projectChecklistDao().getChecklistByProjectId(currentProjectId);

        updateDashboard(currentProject, checklistState);
    }

    private void updateDashboard(Project currentProject, ProjectChecklistState checklistState) {
        int totalTasks = taskList.size();
        int completedTasks = 0;

        for (Task task : taskList) {
            if ("Done".equalsIgnoreCase(task.getStatus())) {
                completedTasks++;
            }
        }

        int pendingTasks = totalTasks - completedTasks;

        tvProjectName.setText(formatProjectName(currentProject));

        String courseText = "Workspace";
        if (currentProject != null
                && currentProject.getCourse() != null
                && !currentProject.getCourse().trim().isEmpty()) {
            courseText = currentProject.getCourse().trim() + " workspace";
        }

        String deadlineRaw = "";
        if (currentProject != null && currentProject.getDeadline() != null) {
            deadlineRaw = currentProject.getDeadline().trim();
        }

        String formattedDeadline = formatDeadlineForDisplay(deadlineRaw);

        String secondLine;
        if (!formattedDeadline.isEmpty()) {
            secondLine = "Due " + formattedDeadline + " · "
                    + totalTasks + " task" + (totalTasks == 1 ? "" : "s") + " · "
                    + meetingList.size() + " meeting" + (meetingList.size() == 1 ? "" : "s");
        } else {
            secondLine = totalTasks + " task" + (totalTasks == 1 ? "" : "s") + " · "
                    + meetingList.size() + " meeting" + (meetingList.size() == 1 ? "" : "s");
        }

        tvProjectSubinfo.setText(courseText + "\n" + secondLine);

        tvTasksHighlight.setText(pendingTasks + " active");
        if (taskList.isEmpty()) {
            tvTasksSubinfo.setText("Nothing completed yet");
        } else if (completedTasks == 0) {
            tvTasksSubinfo.setText("Nothing completed yet");
        } else if (pendingTasks == 0) {
            tvTasksSubinfo.setText("Everything completed");
        } else {
            tvTasksSubinfo.setText(completedTasks + " completed so far");
        }

        if (meetingList.isEmpty()) {
            tvMeetingsHighlight.setText("No recent");
            tvMeetingsSubinfo.setText("Capture notes and decisions");
        } else {
            Meeting latestMeeting = meetingList.get(0);
            String title = latestMeeting.getTitle();
            if (title == null || title.trim().isEmpty()) {
                tvMeetingsHighlight.setText("Untitled");
            } else {
                tvMeetingsHighlight.setText(truncateSingleLine(title, 16));
            }
            tvMeetingsSubinfo.setText("Latest meeting updated");
        }

        int checklistCompleted = countChecklistCompleted(checklistState);
        tvChecklistHighlight.setText(checklistCompleted + " / 6 ready");

        if (checklistCompleted == 0) {
            tvChecklistSubinfo.setText("Submission progress");
        } else if (checklistCompleted == 6) {
            tvChecklistSubinfo.setText("All deliverables prepared");
        } else {
            tvChecklistSubinfo.setText("Keep going");
        }

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

    private String formatDeadlineForDisplay(String rawDate) {
        if (rawDate == null) return "";

        rawDate = rawDate.trim();
        if (rawDate.isEmpty() || "No deadline".equalsIgnoreCase(rawDate)) {
            return "";
        }

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
            return outputFormat.format(inputFormat.parse(rawDate));
        } catch (ParseException e) {
            return rawDate;
        }
    }

    private void showProjectSwitcher() {
        if (getContext() == null) return;

        AppDatabase database = AppDatabase.getInstance(getContext());
        List<Project> projects = database.projectDao().getAllProjects();

        if (projects == null || projects.isEmpty()) {
            Toast.makeText(getContext(), "No projects available", Toast.LENGTH_SHORT).show();
            return;
        }

        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_project_switcher, null, false);
        TextView tvDialogSubtitle = dialogView.findViewById(R.id.tv_dialog_subtitle);
        int projectCount = projects.size();
        tvDialogSubtitle.setText(
                projectCount + " workspace" + (projectCount == 1 ? "" : "s") + " · Tap one to switch"
        );

        LinearLayout layoutProjectList = dialogView.findViewById(R.id.layout_project_list);
        View btnNewProject = dialogView.findViewById(R.id.btn_new_project);
        View btnEditProject = dialogView.findViewById(R.id.btn_edit_project);

        int currentProjectId = CurrentProjectManager.getCurrentProjectId(requireContext());

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        for (Project project : projects) {
            View itemView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_project_option, layoutProjectList, false);

            LinearLayout layoutProjectItem = itemView.findViewById(R.id.layout_project_item);
            TextView tvProjectItemName = itemView.findViewById(R.id.tv_project_item_name);
            TextView tvProjectItemMeta = itemView.findViewById(R.id.tv_project_item_meta);
            TextView tvProjectItemCurrent = itemView.findViewById(R.id.tv_project_item_current);

            String projectName = safeDisplayName(project);
            String course = safeText(project.getCourse());
            String deadline = formatDeadlineForDisplay(safeText(project.getDeadline()));

            tvProjectItemName.setText(projectName);

            String metaText;
            if (!course.isEmpty() && !deadline.isEmpty()) {
                metaText = course + " · Due " + deadline;
            } else if (!course.isEmpty()) {
                metaText = course;
            } else if (!deadline.isEmpty()) {
                metaText = "Due " + deadline;
            } else {
                metaText = "Project workspace";
            }
            tvProjectItemMeta.setText(metaText);

            if (project.getId() == currentProjectId) {
                layoutProjectItem.setBackgroundResource(R.drawable.bg_project_option_selected);
                tvProjectItemCurrent.setVisibility(View.VISIBLE);
            } else {
                layoutProjectItem.setBackgroundResource(R.drawable.bg_project_option);
                tvProjectItemCurrent.setVisibility(View.GONE);
            }

            layoutProjectItem.setOnClickListener(v -> {
                CurrentProjectManager.setCurrentProjectId(requireContext(), project.getId());
                loadDashboardData();
                dialog.dismiss();
                Toast.makeText(
                        requireContext(),
                        "Switched to " + projectName,
                        Toast.LENGTH_SHORT
                ).show();
            });

            layoutProjectList.addView(itemView);
        }

        btnNewProject.setOnClickListener(v -> {
            dialog.dismiss();
            showCreateProjectDialog();
        });

        btnEditProject.setOnClickListener(v -> {
            dialog.dismiss();
            showEditCurrentProjectDialog();
        });

        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    private void showProjectManageDialog() {
        if (getContext() == null) return;

        String[] options = {"New project", "Edit current project"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Manage projects")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showCreateProjectDialog();
                    } else if (which == 1) {
                        showEditCurrentProjectDialog();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditCurrentProjectDialog() {
        if (getContext() == null) return;

        AppDatabase database = AppDatabase.getInstance(getContext());
        int currentProjectId = CurrentProjectManager.getCurrentProjectId(getContext());
        Project currentProject = database.projectDao().getProjectById(currentProjectId);

        if (currentProject == null) {
            Toast.makeText(requireContext(), "Current project not found", Toast.LENGTH_SHORT).show();
            return;
        }

        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_project_form, null, false);

        TextView tvTitle = dialogView.findViewById(R.id.tv_project_form_title);
        TextView tvSubtitle = dialogView.findViewById(R.id.tv_project_form_subtitle);
        EditText etProjectName = dialogView.findViewById(R.id.et_project_form_name);
        EditText etCourse = dialogView.findViewById(R.id.et_project_form_course);
        EditText etDeadline = dialogView.findViewById(R.id.et_project_form_deadline);
        Button btnCancel = dialogView.findViewById(R.id.btn_project_form_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_project_form_confirm);

        tvTitle.setText("Edit current project");
        tvSubtitle.setText("Update your workspace details");
        btnConfirm.setText("Save");

        etProjectName.setText(safeText(currentProject.getName()));
        etCourse.setText(safeText(currentProject.getCourse()));
        etDeadline.setText(safeText(currentProject.getDeadline()));

        etDeadline.setOnClickListener(v -> showProjectDeadlinePicker(etDeadline));

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            String name = etProjectName.getText().toString().trim();
            String course = etCourse.getText().toString().trim();
            String deadline = etDeadline.getText().toString().trim();

            if (name.isEmpty()) {
                etProjectName.setError("Project name is required");
                etProjectName.requestFocus();
                return;
            }

            if (course.isEmpty()) {
                course = "Untitled Course";
            }

            if (deadline.isEmpty()) {
                deadline = "No deadline";
            }

            currentProject.setName(name);
            currentProject.setCourse(course);
            currentProject.setDeadline(deadline);

            database.projectDao().update(currentProject);
            loadDashboardData();

            Toast.makeText(requireContext(), "Project updated", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    private void showCreateProjectDialog() {
        if (getContext() == null) return;

        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_project_form, null, false);

        TextView tvTitle = dialogView.findViewById(R.id.tv_project_form_title);
        TextView tvSubtitle = dialogView.findViewById(R.id.tv_project_form_subtitle);
        EditText etProjectName = dialogView.findViewById(R.id.et_project_form_name);
        EditText etCourse = dialogView.findViewById(R.id.et_project_form_course);
        EditText etDeadline = dialogView.findViewById(R.id.et_project_form_deadline);
        Button btnCancel = dialogView.findViewById(R.id.btn_project_form_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_project_form_confirm);

        tvTitle.setText("New project");
        tvSubtitle.setText("Set up your workspace details");
        btnConfirm.setText("Create");

        etDeadline.setOnClickListener(v -> showProjectDeadlinePicker(etDeadline));

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            String name = etProjectName.getText().toString().trim();
            String course = etCourse.getText().toString().trim();
            String deadline = etDeadline.getText().toString().trim();

            if (name.isEmpty()) {
                etProjectName.setError("Project name is required");
                etProjectName.requestFocus();
                return;
            }

            if (course.isEmpty()) {
                course = "Untitled Course";
            }

            if (deadline.isEmpty()) {
                deadline = "No deadline";
            }

            createProjectAndSwitch(name, course, deadline);
            dialog.dismiss();
        });

        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    private void showProjectDeadlinePicker(EditText etDeadline) {
        if (getContext() == null) return;

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = selectedYear + "-"
                            + String.format("%02d", selectedMonth + 1) + "-"
                            + String.format("%02d", selectedDay);
                    etDeadline.setText(formattedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void createProjectAndSwitch(String name, String course, String deadline) {
        if (getContext() == null) return;

        AppDatabase database = AppDatabase.getInstance(getContext());

        Project newProject = new Project(
                name,
                course,
                deadline,
                "Created from home switcher"
        );

        long insertedProjectId = database.projectDao().insert(newProject);
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

        CurrentProjectManager.setCurrentProjectId(requireContext(), projectId);
        loadDashboardData();

        Toast.makeText(
                requireContext(),
                "Created and switched to " + name,
                Toast.LENGTH_SHORT
        ).show();
    }

    private int countChecklistCompleted(ProjectChecklistState checklistState) {
        if (checklistState == null) return 0;

        int count = 0;
        if (checklistState.isReportCompleted()) count++;
        if (checklistState.isSourceCodeReady()) count++;
        if (checklistState.isReadmeReady()) count++;
        if (checklistState.isSlidesReady()) count++;
        if (checklistState.isDemoReady()) count++;
        if (checklistState.isVideoReady()) count++;
        return count;
    }

    private String formatProjectName(Project project) {
        if (project == null) return "PROJECTPILOT";

        String name = safeText(project.getName());
        if (name.isEmpty()) return "PROJECTPILOT";

        return name.toUpperCase();
    }

    private String safeDisplayName(Project project) {
        if (project == null) return "project";

        String name = safeText(project.getName());
        return name.isEmpty() ? "project" : name;
    }

    private String safeText(String text) {
        return text == null ? "" : text.trim();
    }

    private int dpToPx(int dp) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
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