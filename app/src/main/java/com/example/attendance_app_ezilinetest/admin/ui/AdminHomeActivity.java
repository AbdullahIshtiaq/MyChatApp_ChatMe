package com.example.attendance_app_ezilinetest.admin.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.databinding.ActivityAdminHomeBinding;
import com.example.attendance_app_ezilinetest.student.ui.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdminHomeActivity extends AppCompatActivity {


    private ActivityAdminHomeBinding binding;
    private View view;

    private FirebaseAuth fAuth;
    private DatabaseReference mAttendance;

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        fAuth = FirebaseAuth.getInstance();

        mAttendance = FirebaseDatabase.getInstance().getReference();

        try {
            iterateDates();
            Log.wtf("-this", "Outside 57");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.threeDotsAdminHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.relativelayoutAdminHome.getVisibility() == View.GONE) {
                    binding.relativelayoutAdminHome.setVisibility(View.VISIBLE);
                    binding.signOutAdminHome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fAuth.signOut();
                            startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
                } else {
                    binding.relativelayoutAdminHome.setVisibility(View.GONE);
                }
            }
        });

        binding.studentDetailsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, StudentDetailsActivity.class));
                finish();
            }
        });

        binding.leaveRequestsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, LeaveRequestsActivity.class));
                finish();
            }
        });

        binding.generateReportAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, GenerateStudentReportActivity.class));
                finish();
            }
        });

        binding.attendanceRecordsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, ViewAllAttendanceActivity.class));
                finish();

            }
        });

        binding.editAttendanceAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHomeActivity.this, EditAttendanceActivity.class));
                finish();

            }
        });


    }

    private void iterateDates() throws ParseException {
        Log.wtf("-this", "127");

        Date startDate = convertToDate("2021-06-21");
        Date currentDate = convertToDate(getCurrentDate());

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(currentDate);

        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            Log.wtf("-this", "138 In For: " + date);
            fillDates(date);
        }
    }

    private void fillDates(Date date) {
        Log.wtf("-this", "144");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = df.format(date);
        flag = false;

        mAttendance.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String snapShotDate = snapshot.getKey().toString();
                Log.wtf("-this", "152 SnapShot Date: " + snapShotDate);

                if (!(snapshot.hasChild(dateString)) && snapShotDate.contentEquals("Attendance")) {
                    Log.wtf("-this", "156 In If");
                    mAttendance.child("Attendance").child(dateString).child("sp19-bse-005").child("isPresent").setValue("Absent")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.wtf("-this", "Task Complete for " + dateString);
                                    } else {
                                        Log.wtf("-this", "Error in Task " + task.getException());
                                    }
                                }
                            });
                }

//                if(snapShotDate.contentEquals(dateString)){
//                    Log.wtf("-this", "155 In If");
//                    flag = true;
//                    return;
//                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        if(!flag){
//            Log.wtf("-this", "183 In Flag: "+ date);
//            mAttendance.child(dateString).child("sp19-bse-005").child("isPresent").setValue("Absent")
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()){
//                        Log.wtf("-this", "Task Complete for "+dateString);
//                    }else{
//                        Log.wtf("-this", "Error in Task " + task.getException());
//                    }
//                }
//            });
//        }
    }

    private String getCurrentDate() {
        Date cd = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(cd);
    }

    private Date convertToDate(String date) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(date);
        return date1;
    }

    public void dummyClick(View view) {
    }
}