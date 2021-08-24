package com.example.attendance_app_ezilinetest.admin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.admin.adapter.FireBaseAdminAdapter_ReqLeave;
import com.example.attendance_app_ezilinetest.dataModels.Leave;
import com.example.attendance_app_ezilinetest.databinding.ActivityLeaveRequestsBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class LeaveRequestsActivity extends AppCompatActivity {

    private ActivityLeaveRequestsBinding binding;
    private View view;

    private FireBaseAdminAdapter_ReqLeave adapter;
    private DatabaseReference mLeaveDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_requests);

        binding = ActivityLeaveRequestsBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        mLeaveDatabase = FirebaseDatabase.getInstance().getReference().child("leaves");

        Query query = mLeaveDatabase.orderByKey();

        binding.recViewReqLeaveAdmin.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Leave>().setQuery(
                        query, Leave.class
                ).build();

        adapter = new FireBaseAdminAdapter_ReqLeave(options, LeaveRequestsActivity.this);

        binding.recViewReqLeaveAdmin.setAdapter(adapter);
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
        startActivity(new Intent(LeaveRequestsActivity.this, AdminHomeActivity.class));
        finish();
    }
}