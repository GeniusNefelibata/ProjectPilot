package com.example.groupproject.ui.tasks;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.groupproject.R;
import com.example.groupproject.data.db.AppDatabase;
import com.example.groupproject.data.model.Task;
//import com.example.groupproject.ui.MotionUtils;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    private EditText etTaskTitle;
    private EditText etTaskAssignee;
    private EditText etTaskDueDate;
    private Spinner spinnerTaskStatus;
    private Button btnUpdateTask;
    private Button btnDeleteTask;

    private Task currentTask;
    private int taskId = -1;

    private final String[] statusOptions = {"To Do", "Doing", "Done"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        etTaskTitle = findViewById(R.id.et_edit_task_title);
        etTaskAssignee = findViewById(R.id.et_edit_task_assignee);
        etTaskDueDate = findViewById(R.id.et_edit_task_due_date);
        spinnerTaskStatus = findViewById(R.id.spinner_edit_task_status);
        btnUpdateTask = findViewById(R.id.btn_update_task);
        btnDeleteTask = findViewById(R.id.btn_delete_task);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_selected_item,
                statusOptions
        );
        statusAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTaskStatus.setAdapter(statusAdapter);

        etTaskDueDate.setOnClickListener(v -> showDatePicker());

//        MotionUtils.applyPressAnimation(btnUpdateTask);
//        MotionUtils.applyPressAnimation(btnDeleteTask);

        taskId = getIntent().getIntExtra("task_id", -1);

        if (taskId == -1) {
            Toast.makeText(this, "Invalid task", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadTask();

        btnUpdateTask.setOnClickListener(v -> updateTask());
        btnDeleteTask.setOnClickListener(v -> confirmDeleteTask());
    }

    private void loadTask() {
        currentTask = AppDatabase.getInstance(this)
                .taskDao()
                .getTaskById(taskId);

        if (currentTask == null) {
            Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etTaskTitle.setText(currentTask.getTitle());
        etTaskAssignee.setText(currentTask.getAssignee());
        etTaskDueDate.setText(currentTask.getDueDate());

        int statusPosition = 0;
        String status = currentTask.getStatus();

        if ("Doing".equals(status)) {
            statusPosition = 1;
        } else if ("Done".equals(status)) {
            statusPosition = 2;
        }

        spinnerTaskStatus.setSelection(statusPosition);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = selectedYear + "-"
                            + String.format("%02d", selectedMonth + 1) + "-"
                            + String.format("%02d", selectedDay);
                    etTaskDueDate.setText(formattedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void updateTask() {
        String title = etTaskTitle.getText().toString().trim();
        String assignee = etTaskAssignee.getText().toString().trim();
        String dueDate = etTaskDueDate.getText().toString().trim();
        String status = spinnerTaskStatus.getSelectedItem().toString();

        if (TextUtils.isEmpty(title)) {
            etTaskTitle.setError("Task title is required");
            etTaskTitle.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(assignee)) {
            assignee = "Unassigned";
        }

        if (TextUtils.isEmpty(dueDate)) {
            dueDate = "No due date";
        }

        currentTask.setTitle(title);
        currentTask.setAssignee(assignee);
        currentTask.setDueDate(dueDate);
        currentTask.setStatus(status);

        AppDatabase.getInstance(this)
                .taskDao()
                .update(currentTask);

        Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmDeleteTask() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", (dialog, which) -> deleteTask())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteTask() {
        AppDatabase.getInstance(this)
                .taskDao()
                .delete(currentTask);

        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
}