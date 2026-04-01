package com.example.groupproject.ui.meetings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupproject.R;
import com.example.groupproject.data.model.Meeting;

import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    public interface OnMeetingClickListener {
        void onMeetingClick(Meeting meeting);
    }

    private List<Meeting> meetingList;
    private final OnMeetingClickListener listener;

    public MeetingAdapter(List<Meeting> meetingList, OnMeetingClickListener listener) {
        this.meetingList = meetingList;
        this.listener = listener;
    }

    public void setMeetingList(List<Meeting> meetingList) {
        this.meetingList = meetingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting, parent, false);
        return new MeetingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        Meeting meeting = meetingList.get(position);

        holder.tvTitle.setText(meeting.getTitle());
        holder.tvDate.setText("Date: " + meeting.getMeetingDate());
        holder.tvNotes.setText("Notes: " + meeting.getNotes());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMeetingClick(meeting);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meetingList == null ? 0 : meetingList.size();
    }

    static class MeetingViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDate, tvNotes;

        public MeetingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_meeting_title);
            tvDate = itemView.findViewById(R.id.tv_meeting_date);
            tvNotes = itemView.findViewById(R.id.tv_meeting_notes);
        }
    }
}