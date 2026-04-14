package com.example.groupproject;

import android.content.Context;
import android.content.SharedPreferences;

public class CurrentProjectManager {

    private static final String PREF_NAME = "projectpilot_prefs";
    private static final String KEY_CURRENT_PROJECT_ID = "current_project_id";

    public static int getCurrentProjectId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(KEY_CURRENT_PROJECT_ID, 1);
    }

    public static void setCurrentProjectId(Context context, int projectId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(KEY_CURRENT_PROJECT_ID, projectId).apply();
    }
}