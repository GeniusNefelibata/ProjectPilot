package com.example.groupproject.ui.meetings;

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

import com.example.groupproject.R;
import com.example.groupproject.data.db.AppDatabase;
import com.example.groupproject.data.model.Meeting;

import java.util.ArrayList;
import java.util.List;

public class MeetingsFragment extends Fragment implements MeetingAdapter.OnMeetingClickListener {

    private RecyclerView recyclerView;
    private Button btnAddMeeting;
    private TextView tvEmptyMeetings;
    private MeetingAdapter meetingAdapter;
    private final List<Meeting> meetingList = new ArrayList<>();

    public MeetingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetings, container, false);

        recyclerView = view.findViewById(R.id.recycler_meetings);
        btnAddMeeting = view.findViewById(R.id.btn_add_meeting);
        tvEmptyMeetings = view.findViewById(R.id.tv_empty_meetings);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        meetingAdapter = new MeetingAdapter(meetingList, this);
        recyclerView.setAdapter(meetingAdapter);

        btnAddMeeting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddMeetingActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMeetings();
    }

    private void loadMeetings() {
        if (getContext() == null) return;

        List<Meeting> meetingsFromDb = AppDatabase.getInstance(getContext())
                .meetingDao()
                .getAllMeetings();

        meetingAdapter.setMeetingList(meetingsFromDb);

        if (meetingsFromDb.isEmpty()) {
            tvEmptyMeetings.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyMeetings.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMeetingClick(Meeting meeting) {
        Intent intent = new Intent(getActivity(), EditMeetingActivity.class);
        intent.putExtra("meeting_id", meeting.getId());
        startActivity(intent);
    }
}