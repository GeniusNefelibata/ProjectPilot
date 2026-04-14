package com.example.groupproject.ui.meetings;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.groupproject.R;
import com.example.groupproject.data.db.AppDatabase;
import com.example.groupproject.data.model.Meeting;
//import com.example.groupproject.ui.MotionUtils;

import java.util.Calendar;

public class EditMeetingActivity extends AppCompatActivity {

    private EditText etMeetingTitle;
    private EditText etMeetingDate;
    private EditText etMeetingNotes;
    private Button btnUpdateMeeting;
    private Button btnDeleteMeeting;

    private Meeting currentMeeting;
    private int meetingId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meeting);

        etMeetingTitle = findViewById(R.id.et_edit_meeting_title);
        etMeetingDate = findViewById(R.id.et_edit_meeting_date);
        etMeetingNotes = findViewById(R.id.et_edit_meeting_notes);
        btnUpdateMeeting = findViewById(R.id.btn_update_meeting);
        btnDeleteMeeting = findViewById(R.id.btn_delete_meeting);

        etMeetingDate.setOnClickListener(v -> showDatePicker());

//        MotionUtils.applyPressAnimation(btnUpdateMeeting);
//        MotionUtils.applyPressAnimation(btnDeleteMeeting);

        meetingId = getIntent().getIntExtra("meeting_id", -1);

        if (meetingId == -1) {
            Toast.makeText(this, "Invalid meeting", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadMeeting();

        btnUpdateMeeting.setOnClickListener(v -> updateMeeting());
        btnDeleteMeeting.setOnClickListener(v -> confirmDeleteMeeting());
    }

    private void loadMeeting() {
        currentMeeting = AppDatabase.getInstance(this)
                .meetingDao()
                .getMeetingById(meetingId);

        if (currentMeeting == null) {
            Toast.makeText(this, "Meeting not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etMeetingTitle.setText(currentMeeting.getTitle());
        etMeetingDate.setText(currentMeeting.getMeetingDate());
        etMeetingNotes.setText(currentMeeting.getNotes());
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

    private void updateMeeting() {
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

        Meeting meeting = AppDatabase.getInstance(this)
                .meetingDao()
                .getMeetingById(meetingId);

        if (meeting == null) {
            Toast.makeText(this, "Meeting not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        meeting.setTitle(title);
        meeting.setMeetingDate(meetingDate);
        meeting.setNotes(notes);

        AppDatabase.getInstance(this)
                .meetingDao()
                .update(meeting);

        Toast.makeText(this, "Meeting updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmDeleteMeeting() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Meeting")
                .setMessage("Are you sure you want to delete this meeting?")
                .setPositiveButton("Delete", (dialog, which) -> deleteMeeting())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteMeeting() {
        AppDatabase.getInstance(this)
                .meetingDao()
                .delete(currentMeeting);

        Toast.makeText(this, "Meeting deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
}