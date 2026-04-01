package com.example.groupproject.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.groupproject.data.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    Task getTaskById(int taskId);

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    List<Task> getAllTasks();

    @Query("DELETE FROM tasks")
    void deleteAllTasks();
}