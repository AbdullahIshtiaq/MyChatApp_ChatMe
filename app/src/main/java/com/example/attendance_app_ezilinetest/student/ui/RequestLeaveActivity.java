package com.example.attendance_app_ezilinetest.student.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.databinding.ActivityRequestLeaveBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RequestLeaveActivity extends AppCompatActivity {

    private ActivityRequestLeaveBinding binding;
    private View view;

    private Calendar myCalendar;
    private String calendarDate = null;
    String start_Date = null;
    String end_Date = null;
    String reason = null;
    String roll_number;
    String timeStamp;
    int check = 0;

    private DatabaseReference mLeaveDatabase;
    private FirebaseAuth mAuth;
    public SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_leave);

        binding = ActivityRequestLeaveBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        myCalendar = Calendar.getInstance();

        mLeaveDatabase = FirebaseDatabase.getInstance().getReference().child("leaves");

        prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        roll_number = prefs.getString("roll_number", "Empty");

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendarDate = getDate();
                if (check == 1) {
                    start_Date = calendarDate;
                    binding.leaveStartDate.setText(start_Date);
                } else if (check == 2) {
                    end_Date = calendarDate;
                    binding.leaveEndDate.setText(end_Date);
                }
            }

        };

        binding.leaveStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 1;
                // TODO Auto-generated method stub
                new DatePickerDialog(RequestLeaveActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.leaveEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 2;
                // TODO Auto-generated method stub
                new DatePickerDialog(RequestLeaveActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.ReqLeaveBtnReqLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(start_Date)) {
                    binding.leaveStartDate.setError("Required");
                    return;
                }
                binding.leaveStartDate.setError(null);
                if (TextUtils.isEmpty(end_Date)) {
                    binding.leaveEndDate.setError("Required");
                    return;
                }
                binding.leaveEndDate.setError(null);
                if (start_Date.compareTo(end_Date) > 0) {
                    binding.leaveStartDate.setError("Wrong Date");
                    return;
                }
                binding.leaveStartDate.setError(null);
                reason = binding.reasonReqLeave.getText().toString();
                if (TextUtils.isEmpty(reason)) {
                    binding.reasonReqLeave.setError("Required");
                    return;
                }
                binding.reasonReqLeave.setError(null);

                timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                HashMap<String, String> leaveMap = new HashMap<>();
                leaveMap.put("end_date", end_Date);
                leaveMap.put("reason", reason);
                leaveMap.put("roll_number", roll_number);
                leaveMap.put("start_date", start_Date);
                leaveMap.put("status", "Pending");

                mLeaveDatabase.child(timeStamp).setValue(leaveMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RequestLeaveActivity.this, "Request Submitted", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RequestLeaveActivity.this, StudentHomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(RequestLeaveActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        });

            }
        });

    }

    private String getDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        String formatedDate = sdf.format(myCalendar.getTime());
        return formatedDate;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(RequestLeaveActivity.this, StudentHomeActivity.class));
        finish();
    }
}