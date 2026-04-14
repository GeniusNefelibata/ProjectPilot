package com.example.groupproject.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.groupproject.data.model.Project;

import java.util.List;

@Dao
public interface ProjectDao {

    @Insert
    long insert(Project project);

    @Update
    void update(Project project);

    @Query("SELECT * FROM projects ORDER BY id ASC")
    List<Project> getAllProjects();

    @Query("SELECT * FROM projects WHERE id = :projectId LIMIT 1")
    Project getProjectById(int projectId);

    @Query("SELECT COUNT(*) FROM projects")
    int getProjectCount();
}