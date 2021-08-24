package com.example.attendance_app_ezilinetest.admin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.admin.adapter.FireBaseAdapter_ViewAllAttendance;
import com.example.attendance_app_ezilinetest.dataModels.Attendance;
import com.example.attendance_app_ezilinetest.databinding.ActivityViewAllAttendanceBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ViewAllAttendanceActivity extends AppCompatActivity {

    private ActivityViewAllAttendanceBinding binding;
    private View view;
    private FireBaseAdapter_ViewAllAttendance adapter;
    private DatabaseReference mAttendanceDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_attendance);

        binding = ActivityViewAllAttendanceBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        Log.wtf("-this", "48 Line");

        mAttendanceDatabase = FirebaseDatabase.getInstance().getReference().child("Attendance");

        Query query = mAttendanceDatabase.orderByKey();

        binding.recViewViewAllAttendance.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Attendance>().setQuery(
                        query, Attendance.class
                ).build();

        adapter = new FireBaseAdapter_ViewAllAttendance(options, ViewAllAttendanceActivity.this);

        binding.recViewViewAllAttendance.setAdapter(adapter);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ViewAllAttendanceActivity.this, AdminHomeActivity.class));
        finish();
    }
}