package com.example.groupproject.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "meetings",
        foreignKeys = @ForeignKey(
                entity = Project.class,
                parentColumns = "id",
                childColumns = "projectId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("projectId")}
)
public class Meeting {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int projectId;
    private String title;
    private String meetingDate;
    private String notes;

    public Meeting(int projectId, String title, String meetingDate, String notes) {
        this.projectId = projectId;
        this.title = title;
        this.meetingDate = meetingDate;
        this.notes = notes;
    }

    @Ignore
    public Meeting(String title, String meetingDate, String notes) {
        this.projectId = 1;
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

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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