package com.example.groupproject.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.groupproject.data.model.Meeting;
import com.example.groupproject.data.model.Project;
import com.example.groupproject.data.model.ProjectChecklistState;
import com.example.groupproject.data.model.Task;

@Database(
        entities = {
                Task.class,
                Meeting.class,
                Project.class,
                ProjectChecklistState.class
        },
        version = 4,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();
    public abstract MeetingDao meetingDao();
    public abstract ProjectDao projectDao();
    public abstract ProjectChecklistDao projectChecklistDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "projectpilot_db"
                            )
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}