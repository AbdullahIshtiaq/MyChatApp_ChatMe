package com.example.attendance_app_ezilinetest.admin.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance_app_ezilinetest.MainActivity;
import com.example.attendance_app_ezilinetest.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHomeActivity extends AppCompatActivity {

    private TextView studentDetails, viewAttendance, leaveReq, generateReport, editAttendance;
    private ImageView threeDotsImageView;
    private RelativeLayout relativeLayout;
    private Button signOutBtn;

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        viewAttendance = findViewById(R.id.ViewAttendanceTV_AdminHome);
        leaveReq = findViewById(R.id.LeaveReqTV_AdminHome);
        generateReport = findViewById(R.id.GenerateReportTV_AdminHome);
        studentDetails = findViewById(R.id.studentDetailsTV_AdminHome);
        editAttendance = findViewById(R.id.EditAttendanceTV_AdminHome);
        threeDotsImageView = findViewById(R.id.threeDots_AdminHome);
        relativeLayout = findViewById(R.id.relativelayout_adminHome);
        signOutBtn = findViewById(R.id.signOut_adminHome);

        fAuth = FirebaseAuth.getInstance();

        threeDotsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (relativeLayout.getVisibility() == View.GONE) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    signOutBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fAuth.signOut();
                            startActivity(new Intent(AdminHomeActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                } else {
                    relativeLayout.setVisibility(View.GONE);
                }
            }
        });

        studentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, StudentDetailsActivity.class));
                finish();
            }
        });

        leaveReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, LeaveRequestsActivity.class));
                finish();
            }
        });

        generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, GenerateStudentReportActivity.class));
                finish();
            }
        });

        viewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, ViewAllAttendanceActivity.class));
                finish();

            }
        });

        editAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, EditAttendanceActivity.class));
                finish();

            }
        });
    }

    public void dummyClick(View view) {
    }
}