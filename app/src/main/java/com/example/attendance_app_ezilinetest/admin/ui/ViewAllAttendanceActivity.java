package com.example.attendance_app_ezilinetest.admin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.admin.adapter.FireBaseAdapter_ViewAllAttendance;
import com.example.attendance_app_ezilinetest.dataModels.Attendance;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ViewAllAttendanceActivity extends AppCompatActivity {

    private FireBaseAdapter_ViewAllAttendance adapter;
    private RecyclerView recyclerView;

    private DatabaseReference mAttendanceDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_attendance);

        recyclerView = findViewById(R.id.rec_ViewAllAttendance);

        Log.wtf("-this", "48 Line");

        mAttendanceDatabase = FirebaseDatabase.getInstance().getReference().child("Attendance");

        Query query = mAttendanceDatabase.orderByKey();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Attendance>().setQuery(
                        query, Attendance.class
                ).build();

        adapter = new FireBaseAdapter_ViewAllAttendance(options, ViewAllAttendanceActivity.this);

        recyclerView.setAdapter(adapter);
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