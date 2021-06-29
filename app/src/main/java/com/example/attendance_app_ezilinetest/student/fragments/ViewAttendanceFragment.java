package com.example.attendance_app_ezilinetest.student.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.dataModels.Attendance;
import com.example.attendance_app_ezilinetest.student.adapter.FireBaseAdapter_ViewAttendance;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ViewAttendanceFragment extends Fragment {

    private FireBaseAdapter_ViewAttendance adapter;
    private RecyclerView mAttendanceList;
    private TextView mAttendanceLabel;

    private DatabaseReference mAttendanceDatabase;

    private FirebaseAuth mAuth;
    public static String mCurrent_user_id;

    private String currentDate = getCurrentDate();

    public ViewAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_attendance, container, false);

        mAttendanceList = view.findViewById(R.id.recyclerView_ViewAttendance);
        mAttendanceLabel = view.findViewById(R.id.attendance_label);

        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mAttendanceDatabase = FirebaseDatabase.getInstance().getReference().child("Attendance");

        mAttendanceList.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Attendance>().setQuery(
                        mAttendanceDatabase, Attendance.class
                ).build();

        adapter = new FireBaseAdapter_ViewAttendance(options, getContext());
        mAttendanceList.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private String getCurrentDate() {
        Date cd = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return df.format(cd);
    }
}