package com.example.attendance_app_ezilinetest.admin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.admin.adapter.RecyclerViewAdapter_EditAttendance;
import com.example.attendance_app_ezilinetest.dataModels.customAttendance;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EditAttendanceActivity extends AppCompatActivity {

    private RecyclerViewAdapter_EditAttendance adapter;

    public static RelativeLayout relativeLayout, relativeLayout1;
    public static Button presentBtn, absentBtn;

    private EditText rollNumberEditTxt;
    private Button searchBtn;
    private RecyclerView recyclerViewEditAttendance;
    private MaterialCardView cardView;

    private DatabaseReference mStudentDatabase, mAttendanceDatabase;

    ArrayList<String> rollNumberList = new ArrayList<>();
    ArrayList<customAttendance> attendanceList = new ArrayList<>();
    ArrayList<customAttendance> myAttendanceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_attendance);
        mStudentDatabase = FirebaseDatabase.getInstance().getReference().child("Students");
        mAttendanceDatabase = FirebaseDatabase.getInstance().getReference().child("Attendance");

        Log.wtf("-this", "Start 74 ");
        searchRollNumber();
        gettingDates();
        Log.wtf("-this", "77 ");

        rollNumberEditTxt = findViewById(R.id.roll_NumberET_EditAttendance);
        searchBtn = findViewById(R.id.searchBtn_EditAttendance);
        recyclerViewEditAttendance = findViewById(R.id.recView_EditAttendance);
        cardView = findViewById(R.id.cardView_EditAttendance);

        presentBtn = findViewById(R.id.markPresent_EditAttendance);
        absentBtn = findViewById(R.id.markAbsent_EditAttendance);


        relativeLayout = findViewById(R.id.relativeView_EditAttendance);
        relativeLayout1 = findViewById(R.id.relativeView1_EditAttendance);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roll_number = rollNumberEditTxt.getText().toString().trim();

                if (TextUtils.isEmpty(roll_number)) {
                    rollNumberEditTxt.setError("Roll Number Required");
                    return;
                }
                rollNumberEditTxt.setError(null);
                findInList(roll_number);
            }
        });


    }

    public void populateRecyclerView(int index) {
        Log.wtf("-this", "populateRecyclerView 152");
        cardView.setVisibility(View.VISIBLE);

        filterAttendanceList(rollNumberList.get(index));

        recyclerViewEditAttendance.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter_EditAttendance(this, myAttendanceList);
        recyclerViewEditAttendance.setAdapter(adapter);

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
                rollNumberEditTxt.setError("Roll Number not Registered");
                cardView.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(EditAttendanceActivity.this, AdminHomeActivity.class));
        finish();
    }
}