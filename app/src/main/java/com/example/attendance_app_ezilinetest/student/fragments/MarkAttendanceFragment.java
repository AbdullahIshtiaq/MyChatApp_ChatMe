package com.example.attendance_app_ezilinetest.student.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attendance_app_ezilinetest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MarkAttendanceFragment extends Fragment {

    private Button markAttendance;

    private DatabaseReference mAttendanceDatabase;
    private DatabaseReference mStudentDatabase;

    private SharedPreferences prefs;

    private FirebaseAuth mAuth;
    private String mCurrent_user_id;

    public MarkAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mark_attendance, container, false);
        markAttendance = view.findViewById(R.id.markAttendanceBtn_MA);

        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        String currentDate = getCurrentDate();

        mAttendanceDatabase = FirebaseDatabase.getInstance().getReference().child("Attendance");
        mStudentDatabase = FirebaseDatabase.getInstance().getReference().child("Students");

        checkAttendance();
        Log.wtf("-this", "About to Start");
        markAbsent();

        markAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", getContext().MODE_PRIVATE);
                String roll_number = prefs.getString("roll_number", "Empty");

                mAttendanceDatabase.child(currentDate).child(roll_number).child("isPresent").setValue("Present")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Attendance Marked", Toast.LENGTH_SHORT).show();
                                    markAttendance.setEnabled(false);
                                    markAttendance.setText("Attendance Marked");
                                } else {
                                    Toast.makeText(getActivity(), "Attendance Not Marked", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        return view;
    }

    private void checkAttendance() {
        String currentDate = getCurrentDate();
        //prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", getActivity().MODE_PRIVATE);
        //String roll_number = prefs.getString("roll_number", "Empty");

        mStudentDatabase.child(mCurrent_user_id).child("roll_number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String rollNumber = snapshot.getValue().toString();
                mAttendanceDatabase.child(currentDate).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(rollNumber)) {
                            markAttendance.setEnabled(false);
                            markAttendance.setText("Attendance Marked");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void markAbsent() {
        String currentDate = getCurrentDate();
        //prefs = getActivity().getSharedPreferences("MY_PREFS_NAME", getContext().MODE_PRIVATE);
        //String roll_number = prefs.getString("roll_number", "Empty");


        DatabaseReference mAttendanceDatabase = FirebaseDatabase.getInstance().getReference().child("Attendance");

        mStudentDatabase.child(mCurrent_user_id).child("roll_number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String roll_number = snapshot.getValue().toString();
                Log.wtf("-this", "RollNumber: " + roll_number);

                mAttendanceDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String date = snapshot.getKey().toString();
                        Log.wtf("-this", "Date: " + date);

                        if (date.contentEquals(currentDate)) {
                            Log.wtf("-this", "Returning: ");
                            return;
                        } else if (!(snapshot.hasChild(roll_number))) {
                            Log.wtf("-this", "125 RollNumber: " + roll_number);
                            mAttendanceDatabase.child(date).child(roll_number).child("isPresent").setValue("Absent");
                        }

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
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private String getCurrentDate() {
        Date cd = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return df.format(cd);
    }
}