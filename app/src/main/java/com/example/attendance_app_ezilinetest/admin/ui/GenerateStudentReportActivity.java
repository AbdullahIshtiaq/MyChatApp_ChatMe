package com.example.attendance_app_ezilinetest.admin.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.admin.adapter.RecyclerViewAdapter_GenerateStudentReport;
import com.example.attendance_app_ezilinetest.dataModels.Student;
import com.example.attendance_app_ezilinetest.dataModels.customAttendance;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class GenerateStudentReportActivity extends AppCompatActivity {

    private RecyclerViewAdapter_GenerateStudentReport adapter;

    private TextView fromDateTV, toDateTV;
    public static TextView rollNumberTV_SR, nameTV_SR, classTV_SR, presentsTV_SR, absentsTV_SR, gradeTV_SR;
    private EditText rollNumberEditTxt;
    private Button generateBtn;
    private RecyclerView recyclerViewReport;
    private MaterialCardView cardView;

    private Calendar myCalendar;
    private String calendarDate = null;
    private DatabaseReference mStudentDatabase, mAttendanceDatabase;

    ArrayList<Student> studentList = new ArrayList<>();
    ArrayList<customAttendance> attendanceList = new ArrayList<>();
    ArrayList<customAttendance> myAttendanceList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    String from_Date = null;
    String to_Date = null;
    //String roll_number;
    //String timeStamp;
    boolean start = true;
    boolean end = true;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_student_report);
        mStudentDatabase = FirebaseDatabase.getInstance().getReference().child("Students");
        mAttendanceDatabase = FirebaseDatabase.getInstance().getReference().child("Attendance");

        Log.wtf("-this", "Start 74 ");
        searchRollNumber();
        gettingDates();
        Log.wtf("-this", "77 ");

        myCalendar = Calendar.getInstance();

        rollNumberEditTxt = findViewById(R.id.roll_NumberET_StudentReport);
        fromDateTV = findViewById(R.id.fromDate_StudentReport);
        toDateTV = findViewById(R.id.toDate_StudentReport);
        generateBtn = findViewById(R.id.generateReportBtn_StudentReport);
        rollNumberTV_SR = findViewById(R.id.rollNumberTV_StudentReport);
        nameTV_SR = findViewById(R.id.nameTV_StudentReport);
        classTV_SR = findViewById(R.id.classTV_StudentReport);
        presentsTV_SR = findViewById(R.id.presentsTV_StudentReport);
        absentsTV_SR = findViewById(R.id.absentsTV_StudentReport);
        gradeTV_SR = findViewById(R.id.gradeTV_StudentReport);
        recyclerViewReport = findViewById(R.id.recView_StudentReport);
        cardView = findViewById(R.id.cardView_StudentReport);

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
                    from_Date = calendarDate;
                    fromDateTV.setText(from_Date);
                } else if (check == 2) {
                    to_Date = calendarDate;
                    toDateTV.setText(to_Date);
                }
            }

        };

        fromDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 1;
                // TODO Auto-generated method stub
                new DatePickerDialog(GenerateStudentReportActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        toDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 2;
                // TODO Auto-generated method stub
                new DatePickerDialog(GenerateStudentReportActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        generateBtn.setOnClickListener(new View.OnClickListener() {
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

    public void generateReport(int index) {
        Log.wtf("-this", "Generate Report 152");

        if (TextUtils.isEmpty(from_Date)) {
            fromDateTV.setError("Required");
            return;
        }
        fromDateTV.setError(null);
        if (TextUtils.isEmpty(to_Date)) {
            toDateTV.setError("Required");
            return;
        }
        toDateTV.setError(null);

        if (from_Date.compareTo(to_Date) > 0) {
            fromDateTV.setError("Wrong Date");
            return;
        }
        fromDateTV.setError(null);

        Log.wtf("-this", "toDate: " + to_Date);
        Log.wtf("-this", "fromDate: " + from_Date);

        cardView.setVisibility(View.VISIBLE);

        rollNumberTV_SR.setText(studentList.get(index).getRoll_number());
        nameTV_SR.setText(studentList.get(index).getName());
        classTV_SR.setText("Class: " + studentList.get(index).getClass_room());

        String startDate_DataBase = dateList.get(0);
        String endDate_DataBase = dateList.get(dateList.size() - 1);

        Log.wtf("-this", "Start Date: " + startDate_DataBase);
        Log.wtf("-this", "End Date: " + endDate_DataBase);

        start = true;
        end = true;


        if (from_Date.compareTo(startDate_DataBase) > 0) { // from date is bigger then start date
            start = false;
        }

        if (to_Date.compareTo(endDate_DataBase) < 0) { // to is bigger then end date
            end = false;
        }
        Log.wtf("-this", "Generate Report 188");

        ArrayList<Integer> array = filterAttendanceList(studentList.get(index).getRoll_number());

        for (int i = 0; i < myAttendanceList.size(); i++) {
            Log.wtf("-this", "AttendanceList: " + myAttendanceList.get(i).getDate());
        }

        int presents = array.get(0);
        int absents = array.get(1);

        Log.wtf("-this", "Present: " + presents);
        Log.wtf("-this", "Absents: " + absents);

        String presentTV = Integer.toString(presents);
        String absentsTV = Integer.toString(absents);

        presentsTV_SR.setText("Present: " + presentTV);
        absentsTV_SR.setText("Absents: " + absentsTV);

        int grade = (presents * 100 / (presents + absents));
        String Grade = Integer.toString(grade);

        if (Grade.compareTo("79") > 0) {
            gradeTV_SR.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.light_green));
            gradeTV_SR.setText("Grade: " + grade + "%");
        } else if (Grade.compareTo("49") > 0) {
            gradeTV_SR.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
            gradeTV_SR.setText("Grade: " + grade + "%");
        } else {
            gradeTV_SR.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
            gradeTV_SR.setText("Grade: " + grade + "%");
        }

        recyclerViewReport.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter_GenerateStudentReport(this, myAttendanceList);
        recyclerViewReport.setAdapter(adapter);

    }

    private void searchRollNumber() {
        final DatabaseReference userRef = mStudentDatabase;
        Log.wtf("-this", "Search Roll Number ");
        Query namesQuery = userRef.orderByKey();
        studentList.clear();
        namesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.hasChild("roll_number")) {
                    String class_room = snapshot.child("class_room").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
                    String roll_number = snapshot.child("roll_number").getValue().toString();

                    Student std = new Student(class_room, "null", name, "null", roll_number);
                    studentList.add(std);
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
        Query datesQuery = userRef.orderByKey();
        dateList.clear();
        attendanceList.clear();
        Log.wtf("-this", "Getting Dates 257 ");
        datesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String date = snapshot.getKey().toString();
                dateList.add(date);
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

    public ArrayList<Integer> filterAttendanceList(String roll) {
        Log.wtf("-this", "Filter Attendance 321");

        ArrayList<Integer> array = new ArrayList<>();
        array.clear();

        myAttendanceList.clear();
        boolean check = false;
        boolean check1 = false;
        int presents = 0;
        int absents = 0;

        for (int i = 0; i < attendanceList.size(); i++) {

            if (start && end) {
                Log.wtf("-this", "Start|End");
                if (attendanceList.get(i).getRollNumber().contentEquals(roll)) {
                    customAttendance ca = new customAttendance(attendanceList.get(i).getDate(), roll, attendanceList.get(i).getStatus());
                    myAttendanceList.add(ca);
                    if (attendanceList.get(i).getStatus().contentEquals("Absent")) {
                        absents += 1;
                    } else {
                        presents += 1;
                    }
                }

            } else if (start == true && end == false) {
                Log.wtf("-this", "Start");
                if (attendanceList.get(i).getRollNumber().contentEquals(roll) && !(attendanceList.get(i).getDate().contentEquals(to_Date))) {
                    Log.wtf("-this", "371");
                    customAttendance ca = new customAttendance(attendanceList.get(i).getDate(), roll, attendanceList.get(i).getStatus());
                    myAttendanceList.add(ca);
                    if (attendanceList.get(i).getStatus().contentEquals("Absent")) {
                        absents += 1;
                    } else {
                        presents += 1;
                    }
                } else if (attendanceList.get(i).getRollNumber().contentEquals(roll) && attendanceList.get(i).getDate().contentEquals(to_Date)) {
                    Log.wtf("-this", "380");
                    customAttendance ca = new customAttendance(attendanceList.get(i).getDate(), roll, attendanceList.get(i).getStatus());
                    myAttendanceList.add(ca);
                    if (attendanceList.get(i).getStatus().contentEquals("Absent")) {
                        absents += 1;
                    } else {
                        presents += 1;
                    }
                    break;
                }

            } else if (start == false && end == true) {
                Log.wtf("-this", "End: " + i);
                if (check) {
                    if (attendanceList.get(i).getRollNumber().contentEquals(roll)) {
                        Log.wtf("-this", "389");
                        customAttendance ca = new customAttendance(attendanceList.get(i).getDate(), roll, attendanceList.get(i).getStatus());
                        myAttendanceList.add(ca);
                        if (attendanceList.get(i).getStatus().contentEquals("Absent")) {
                            absents += 1;
                        } else {
                            presents += 1;
                        }
                    }
                }
                if (attendanceList.get(i).getRollNumber().contentEquals(roll) && attendanceList.get(i).getDate().contentEquals(from_Date)) {
                    check = true;
                    Log.wtf("-this", "400");
                    customAttendance ca = new customAttendance(attendanceList.get(i).getDate(), roll, attendanceList.get(i).getStatus());
                    myAttendanceList.add(ca);
                    if (attendanceList.get(i).getStatus().contentEquals("Absent")) {
                        absents += 1;
                    } else {
                        presents += 1;
                    }
                }
            } else if (start == false && end == false) {
                Log.wtf("-this", "Both False: " + i);
                if (check1) {
                    Log.wtf("-this", "413");
                    if (attendanceList.get(i).getRollNumber().contentEquals(roll)) {
                        customAttendance ca = new customAttendance(attendanceList.get(i).getDate(), roll, attendanceList.get(i).getStatus());
                        myAttendanceList.add(ca);
                        if (attendanceList.get(i).getStatus().contentEquals("Absent")) {
                            absents += 1;
                        } else {
                            presents += 1;
                        }
                    }
                }
                if (attendanceList.get(i).getRollNumber().contentEquals(roll) && attendanceList.get(i).getDate().contentEquals(from_Date)) {
                    check1 = true;
                    Log.wtf("-this", "426");
                    customAttendance ca = new customAttendance(attendanceList.get(i).getDate(), roll, attendanceList.get(i).getStatus());
                    myAttendanceList.add(ca);
                    if (attendanceList.get(i).getStatus().contentEquals("Absent")) {
                        absents += 1;
                    } else {
                        presents += 1;
                    }
                }
                if (attendanceList.get(i).getRollNumber().contentEquals(roll) && attendanceList.get(i).getDate().contentEquals(to_Date)) {
                    Log.wtf("-this", "436");
//                    customAttendance ca = new customAttendance(attendanceList.get(i).getDate(), roll, attendanceList.get(i).getStatus());
//                    myAttendanceList.add(ca);
//                    if (attendanceList.get(i).getStatus().contentEquals("Absent")) {
//                        absents += 1;
//                    } else {
//                        presents += 1;
//                    }
                    break;
                }
            }
        }
        array.add(presents);
        array.add(absents);
        return array;
    }

    public void findInList(String rollnumber) {
        if (studentList != null && studentList.size() > 0) {
            boolean foundName = false;
            for (int i = 0; i < studentList.size(); i++) {
                if (rollnumber.equals(studentList.get(i).getRoll_number())) {
                    foundName = true;
                    generateReport(i);
                    break;
                }
            }
            if (!foundName) {
                rollNumberEditTxt.setError("Roll Number not Registered");
            }
        }
    }

    private String getDate() {

        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        String formatedDate = sdf.format(myCalendar.getTime());
        return formatedDate;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(GenerateStudentReportActivity.this, AdminHomeActivity.class));
        finish();
    }

}