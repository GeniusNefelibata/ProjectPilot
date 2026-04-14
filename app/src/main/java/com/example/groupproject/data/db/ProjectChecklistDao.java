package com.example.groupproject.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.groupproject.data.model.ProjectChecklistState;

@Dao
public interface ProjectChecklistDao {

    @Insert
    long insert(ProjectChecklistState state);

    @Update
    void update(ProjectChecklistState state);

    @Query("SELECT * FROM project_checklist_states WHERE projectId = :projectId LIMIT 1")
    ProjectChecklistState getChecklistByProjectId(int projectId);
}