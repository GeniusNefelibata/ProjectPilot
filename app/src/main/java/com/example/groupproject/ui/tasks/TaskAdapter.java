package com.example.groupproject.ui.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupproject.R;
import com.example.groupproject.data.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    private List<Task> taskList;
    private final OnTaskClickListener listener;

    public TaskAdapter(List<Task> taskList, OnTaskClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.tvTitle.setText(task.getTitle());
        holder.tvAssignee.setText("Assignee: " + task.getAssignee());
        holder.tvDueDate.setText("Due: " + task.getDueDate());
        holder.tvStatus.setText(task.getStatus());

        String status = task.getStatus();

        if ("To Do".equals(status)) {
            holder.tvStatus.setBackground(
                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_status_todo)
            );
            holder.tvStatus.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.status_todo_text)
            );
        } else if ("Doing".equals(status)) {
            holder.tvStatus.setBackground(
                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_status_doing)
            );
            holder.tvStatus.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.status_doing_text)
            );
        } else if ("Done".equals(status)) {
            holder.tvStatus.setBackground(
                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_status_done)
            );
            holder.tvStatus.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.status_done_text)
            );
        } else {
            holder.tvStatus.setBackground(null);
            holder.tvStatus.setTextColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.text_secondary)
            );
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskClick(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList == null ? 0 : taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvAssignee, tvDueDate, tvStatus;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_task_title);
            tvAssignee = itemView.findViewById(R.id.tv_task_assignee);
            tvDueDate = itemView.findViewById(R.id.tv_task_due_date);
            tvStatus = itemView.findViewById(R.id.tv_task_status);
        }
    }
}