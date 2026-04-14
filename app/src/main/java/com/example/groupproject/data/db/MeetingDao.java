package com.example.groupproject.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.groupproject.data.model.Meeting;

import java.util.List;

@Dao
public interface MeetingDao {

    @Insert
    long insert(Meeting meeting);

    @Update
    void update(Meeting meeting);

    @Delete
    void delete(Meeting meeting);

    @Query("SELECT * FROM meetings ORDER BY id DESC")
    List<Meeting> getAllMeetings();

    @Query("SELECT * FROM meetings WHERE projectId = :projectId ORDER BY id DESC")
    List<Meeting> getMeetingsByProjectId(int projectId);

    @Query("SELECT * FROM meetings WHERE id = :meetingId LIMIT 1")
    Meeting getMeetingById(int meetingId);
}