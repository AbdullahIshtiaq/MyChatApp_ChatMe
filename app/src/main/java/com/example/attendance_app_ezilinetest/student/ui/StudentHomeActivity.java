package com.example.attendance_app_ezilinetest.student.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.student.fragments.MarkAttendanceFragment;
import com.example.attendance_app_ezilinetest.student.fragments.RequestLeaveFragment;
import com.example.attendance_app_ezilinetest.student.fragments.ViewAttendanceFragment;
import com.google.firebase.auth.FirebaseAuth;
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

public class StudentHomeActivity extends AppCompatActivity {

    private ImageView markAttendance, requestLeave, viewAttendance;
    private MarkAttendanceFragment currentFragment;
    private TextView title;
    private ImageView profile, threeDots;
    private RelativeLayout relativeLayout;
    private Button signOutBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mStudentDatabase;

    public static String mCurrent_user_id;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        markAttendance = findViewById(R.id.markAttendance_Home);
        requestLeave = findViewById(R.id.requestLeave_Home);
        viewAttendance = findViewById(R.id.viewAttendance_Home);
        title = findViewById(R.id.title_StudentHome);
        title = findViewById(R.id.title_StudentHome);
        threeDots = findViewById(R.id.threeDots_StudentHome);
        threeDots = findViewById(R.id.threeDots_StudentHome);
        relativeLayout = findViewById(R.id.relativelayout_studentHome);
        signOutBtn = findViewById(R.id.signOut_studentHome);
        profile = findViewById(R.id.profile_StudentHome);

        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mStudentDatabase = FirebaseDatabase.getInstance().getReference().child("Students");

        mStudentDatabase.child(mCurrent_user_id).child("roll_number")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String reg_no = snapshot.getValue().toString();

                        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                        editor.putString("roll_number", reg_no);
                        editor.commit();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentHomeActivity.this, ProfileActivity.class));
                finish();
            }
        });

        threeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (relativeLayout.getVisibility() == View.GONE) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    signOutBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mAuth.signOut();
                            startActivity(new Intent(StudentHomeActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
                } else {
                    relativeLayout.setVisibility(View.GONE);
                }
            }
        });

        MarkAttendanceFragment markAttendanceFragment = new MarkAttendanceFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, markAttendanceFragment).commit();
        currentFragment = markAttendanceFragment;

        markAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarkAttendanceFragment markAttendanceFragment = new MarkAttendanceFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, markAttendanceFragment).commit();
                currentFragment = markAttendanceFragment;
                title.setText("Mark Attendance");
            }
        });

        requestLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestLeaveFragment requestLeaveFragment = new RequestLeaveFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, requestLeaveFragment).commit();
                title.setText("View Leaves");
            }
        });

        viewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAttendanceFragment viewAttendanceFragment = new ViewAttendanceFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, viewAttendanceFragment).commit();
                title.setText("View Attendance");
            }
        });
    }

    private String getCurrentDate() {
        Date cd = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(cd);
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    public void dummyClick(View view) {
    }
}