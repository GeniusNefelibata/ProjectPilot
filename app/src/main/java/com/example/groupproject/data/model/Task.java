package com.example.groupproject.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "tasks",
        foreignKeys = @ForeignKey(
                entity = Project.class,
                parentColumns = "id",
                childColumns = "projectId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("projectId")}
)
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int projectId;
    private String title;
    private String assignee;
    private String dueDate;
    private String status;

    public Task(int projectId, String title, String assignee, String dueDate, String status) {
        this.projectId = projectId;
        this.title = title;
        this.assignee = assignee;
        this.dueDate = dueDate;
        this.status = status;
    }

    @Ignore
    public Task(String title, String assignee, String dueDate, String status) {
        this.projectId = 1;
        this.title = title;
        this.assignee = assignee;
        this.dueDate = dueDate;
        this.status = status;
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

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}