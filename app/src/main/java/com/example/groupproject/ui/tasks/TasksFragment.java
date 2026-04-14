package com.example.groupproject.ui.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupproject.CurrentProjectManager;
import com.example.groupproject.R;
import com.example.groupproject.data.db.AppDatabase;
import com.example.groupproject.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment implements TaskAdapter.OnTaskClickListener {

    private RecyclerView recyclerView;
    private Button btnAddTask;
    private TextView tvEmptyTasks;
    private TaskAdapter taskAdapter;
    private List<Task> taskList = new ArrayList<>();

    public TasksFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        recyclerView = view.findViewById(R.id.recycler_tasks);
        btnAddTask = view.findViewById(R.id.btn_add_task);
        tvEmptyTasks = view.findViewById(R.id.tv_empty_tasks);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);

        btnAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTaskActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        if (getContext() == null) return;

        int currentProjectId = CurrentProjectManager.getCurrentProjectId(getContext());

        List<Task> tasksFromDb = AppDatabase.getInstance(getContext())
                .taskDao()
                .getTasksByProjectId(currentProjectId);

        taskAdapter.setTaskList(tasksFromDb);

        if (tasksFromDb.isEmpty()) {
            tvEmptyTasks.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyTasks.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTaskClick(Task task) {
        Intent intent = new Intent(getActivity(), EditTaskActivity.class);
        intent.putExtra("task_id", task.getId());
        startActivity(intent);
    }
}