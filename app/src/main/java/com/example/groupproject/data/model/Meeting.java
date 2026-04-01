package com.example.groupproject.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meetings")
public class Meeting {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String meetingDate;
    private String notes;

    public Meeting(String title, String meetingDate, String notes) {
        this.title = title;
        this.meetingDate = meetingDate;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}