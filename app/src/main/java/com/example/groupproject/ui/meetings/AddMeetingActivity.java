package com.example.groupproject.ui.meetings;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groupproject.CurrentProjectManager;
import com.example.groupproject.R;
import com.example.groupproject.data.db.AppDatabase;
import com.example.groupproject.data.model.Meeting;

import java.util.Calendar;

public class AddMeetingActivity extends AppCompatActivity {

    private EditText etMeetingTitle;
    private EditText etMeetingDate;
    private EditText etMeetingNotes;
    private Button btnSaveMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        etMeetingTitle = findViewById(R.id.et_meeting_title);
        etMeetingDate = findViewById(R.id.et_meeting_date);
        etMeetingNotes = findViewById(R.id.et_meeting_notes);
        btnSaveMeeting = findViewById(R.id.btn_save_meeting);

        etMeetingDate.setOnClickListener(v -> showDatePicker());
        btnSaveMeeting.setOnClickListener(v -> saveMeeting());
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
                    etMeetingDate.setText(formattedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void saveMeeting() {
        String title = etMeetingTitle.getText().toString().trim();
        String meetingDate = etMeetingDate.getText().toString().trim();
        String notes = etMeetingNotes.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            etMeetingTitle.setError("Meeting title is required");
            etMeetingTitle.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(meetingDate)) {
            meetingDate = "No date selected";
        }

        if (TextUtils.isEmpty(notes)) {
            notes = "No notes";
        }

        int currentProjectId = CurrentProjectManager.getCurrentProjectId(this);

        Meeting meeting = new Meeting(currentProjectId, title, meetingDate, notes);

        AppDatabase.getInstance(this)
                .meetingDao()
                .insert(meeting);

        Toast.makeText(this, "Meeting saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(
                R.anim.activity_close_enter,
                R.anim.activity_close_exit
        );
    }
}