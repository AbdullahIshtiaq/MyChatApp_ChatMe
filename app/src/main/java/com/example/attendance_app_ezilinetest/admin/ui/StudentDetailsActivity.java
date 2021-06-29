package com.example.attendance_app_ezilinetest.admin.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.admin.adapter.FireBaseAdapter_StudentDetailsAdmHome;
import com.example.attendance_app_ezilinetest.dataModels.Student;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class StudentDetailsActivity extends AppCompatActivity {

    private FireBaseAdapter_StudentDetailsAdmHome adapter;
    private RecyclerView recyclerView;
    private DatabaseReference mStudentDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        recyclerView = findViewById(R.id.horizontalRec_StudentDetails);

        mStudentDatabase = FirebaseDatabase.getInstance().getReference().child("Students");

        Query query = mStudentDatabase.orderByKey();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Student>().setQuery(
                        query, Student.class
                ).build();

        adapter = new FireBaseAdapter_StudentDetailsAdmHome(options, StudentDetailsActivity.this);

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
        startActivity(new Intent(StudentDetailsActivity.this, AdminHomeActivity.class));
        finish();
    }
}