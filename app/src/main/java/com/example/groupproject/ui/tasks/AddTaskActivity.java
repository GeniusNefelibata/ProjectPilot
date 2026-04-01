package com.example.groupproject.ui.tasks;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groupproject.R;
import com.example.groupproject.data.db.AppDatabase;
import com.example.groupproject.data.model.Task;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    private EditText etTaskTitle;
    private EditText etTaskAssignee;
    private EditText etTaskDueDate;
    private Spinner spinnerTaskStatus;
    private Button btnSaveTask;

    private final String[] statusOptions = {"To Do", "Doing", "Done"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTaskTitle = findViewById(R.id.et_task_title);
        etTaskAssignee = findViewById(R.id.et_task_assignee);
        etTaskDueDate = findViewById(R.id.et_task_due_date);
        spinnerTaskStatus = findViewById(R.id.spinner_task_status);
        btnSaveTask = findViewById(R.id.btn_save_task);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_selected_item,
                statusOptions
        );
        statusAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTaskStatus.setAdapter(statusAdapter);

        etTaskDueDate.setOnClickListener(v -> showDatePicker());

        btnSaveTask.setOnClickListener(v -> {
            saveTask();
        });
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

    private void saveTask() {
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

        Task task = new Task(title, assignee, dueDate, status);

        AppDatabase.getInstance(this)
                .taskDao()
                .insert(task);

        Toast.makeText(this, "Task saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}