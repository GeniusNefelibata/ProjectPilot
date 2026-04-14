package com.example.groupproject.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "project_checklist_states",
        foreignKeys = @ForeignKey(
                entity = Project.class,
                parentColumns = "id",
                childColumns = "projectId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = "projectId", unique = true)}
)
public class ProjectChecklistState {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int projectId;

    private boolean reportCompleted;
    private boolean sourceCodeReady;
    private boolean readmeReady;
    private boolean slidesReady;
    private boolean demoReady;
    private boolean videoReady;

    public ProjectChecklistState(int projectId,
                                 boolean reportCompleted,
                                 boolean sourceCodeReady,
                                 boolean readmeReady,
                                 boolean slidesReady,
                                 boolean demoReady,
                                 boolean videoReady) {
        this.projectId = projectId;
        this.reportCompleted = reportCompleted;
        this.sourceCodeReady = sourceCodeReady;
        this.readmeReady = readmeReady;
        this.slidesReady = slidesReady;
        this.demoReady = demoReady;
        this.videoReady = videoReady;
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

    public boolean isReportCompleted() {
        return reportCompleted;
    }

    public void setReportCompleted(boolean reportCompleted) {
        this.reportCompleted = reportCompleted;
    }

    public boolean isSourceCodeReady() {
        return sourceCodeReady;
    }

    public void setSourceCodeReady(boolean sourceCodeReady) {
        this.sourceCodeReady = sourceCodeReady;
    }

    public boolean isReadmeReady() {
        return readmeReady;
    }

    public void setReadmeReady(boolean readmeReady) {
        this.readmeReady = readmeReady;
    }

    public boolean isSlidesReady() {
        return slidesReady;
    }

    public void setSlidesReady(boolean slidesReady) {
        this.slidesReady = slidesReady;
    }

    public boolean isDemoReady() {
        return demoReady;
    }

    public void setDemoReady(boolean demoReady) {
        this.demoReady = demoReady;
    }

    public boolean isVideoReady() {
        return videoReady;
    }

    public void setVideoReady(boolean videoReady) {
        this.videoReady = videoReady;
    }
}