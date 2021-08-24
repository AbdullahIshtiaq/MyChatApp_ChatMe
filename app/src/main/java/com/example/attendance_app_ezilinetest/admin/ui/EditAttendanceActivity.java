package com.example.attendance_app_ezilinetest.admin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.admin.adapter.RecyclerViewAdapter_EditAttendance;
import com.example.attendance_app_ezilinetest.dataModels.customAttendance;
import com.example.attendance_app_ezilinetest.databinding.ActivityEditAttendanceBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EditAttendanceActivity extends AppCompatActivity {

    private static ActivityEditAttendanceBinding binding;
    private View view;

    private RecyclerViewAdapter_EditAttendance adapter;

    public static Button presentBtn, absentBtn;

    private DatabaseReference mStudentDatabase, mAttendanceDatabase;

    ArrayList<String> rollNumberList = new ArrayList<>();
    ArrayList<customAttendance> attendanceList = new ArrayList<>();
    ArrayList<customAttendance> myAttendanceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_attendance);

        binding = ActivityEditAttendanceBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        mStudentDatabase = FirebaseDatabase.getInstance().getReference().child("Students");
        mAttendanceDatabase = FirebaseDatabase.getInstance().getReference().child("Attendance");

        presentBtn = findViewById(R.id.markPresent_EditAttendance);
        absentBtn = findViewById(R.id.markAbsent_EditAttendance);

        Log.wtf("-this", "Start 74 ");
        searchRollNumber();
        gettingDates();
        Log.wtf("-this", "77 ");

        binding.searchBtnEditAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roll_number = binding.rollNumberETEditAttendance.getText().toString().trim();

                if (TextUtils.isEmpty(roll_number)) {
                    binding.rollNumberETEditAttendance.setError("Roll Number Required");
                    return;
                }
                binding.rollNumberETEditAttendance.setError(null);
                findInList(roll_number);
            }
        });

    }

    public void populateRecyclerView(int index) {
        Log.wtf("-this", "populateRecyclerView 152");
        binding.cardViewEditAttendance.setVisibility(View.VISIBLE);

        filterAttendanceList(rollNumberList.get(index));

        binding.recViewEditAttendance.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter_EditAttendance(this, myAttendanceList, EditAttendanceActivity.this);
        binding.recViewEditAttendance.setAdapter(adapter);
    }

    private void searchRollNumber() {
        final DatabaseReference userRef = mStudentDatabase;
        Log.wtf("-this", "Search Roll Number ");
        Query rollNumberQuery = userRef.orderByKey();
        rollNumberList.clear();
        rollNumberQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.hasChild("roll_number")) {
                    String roll_number = snapshot.child("roll_number").getValue().toString();
                    rollNumberList.add(roll_number);
                }
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
    }

    private void gettingDates() {
        final DatabaseReference userRef = mAttendanceDatabase;
        Query attendanceQuery = userRef.orderByKey();
        attendanceList.clear();
        Log.wtf("-this", "Getting Dates 257 ");
        attendanceQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String date = snapshot.getKey().toString();
                Log.wtf("-this", "Date 263: " + date);

                mAttendanceDatabase.child(date).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String roll = snapshot.getKey().toString();
                        String status = snapshot.child("isPresent").getValue().toString();
                        Log.wtf("-this", "Roll: " + roll);

                        customAttendance cAttendance = new customAttendance(date, roll, status);
                        attendanceList.add(cAttendance);
                    }

                    @Override
                    public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
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
    }

    public void findInList(String rollnumber) {
        if (rollNumberList != null && rollNumberList.size() > 0) {
            boolean foundName = false;
            for (int i = 0; i < rollNumberList.size(); i++) {
                if (rollnumber.equals(rollNumberList.get(i))) {
                    foundName = true;
                    populateRecyclerView(i);
                    break;
                }
            }
            if (!foundName) {
                binding.rollNumberETEditAttendance.setError("Roll Number not Registered");
                binding.cardViewEditAttendance.setVisibility(View.GONE);
            }
        }
    }

    public void filterAttendanceList(String roll) {
        Log.wtf("-this", "Filter Attendance 321");
        myAttendanceList.clear();
        for (int i = 0; i < attendanceList.size(); i++) {
            if (attendanceList.get(i).getRollNumber().contentEquals(roll)) {
                customAttendance ca = new customAttendance(attendanceList.get(i).getDate(), roll, attendanceList.get(i).getStatus());
                myAttendanceList.add(ca);
            }
        }
    }

    public static void showRelativeView() {
        binding.relativeViewEditAttendance.setVisibility(View.VISIBLE);
        binding.relativeView1EditAttendance.setVisibility(View.VISIBLE);
    }

    public static void hideRelativeView() {
        binding.relativeViewEditAttendance.setVisibility(View.GONE);
        binding.relativeView1EditAttendance.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(EditAttendanceActivity.this, AdminHomeActivity.class));
        finish();
    }
}