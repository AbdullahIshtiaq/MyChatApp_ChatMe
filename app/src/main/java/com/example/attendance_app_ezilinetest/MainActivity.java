package com.example.attendance_app_ezilinetest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance_app_ezilinetest.admin.ui.AdminLoginActivity;
import com.example.attendance_app_ezilinetest.student.ui.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    private FirebaseUser currentUser;

    private Button stdPortalBtn, adminProtalBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stdPortalBtn = findViewById(R.id.stdPortalBtn_Main);
        adminProtalBtn = findViewById(R.id.adminPortalBtn_Main);

        Log.wtf("-this", "In Main");

        stdPortalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("-this", "Student");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        adminProtalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("-this", "Admin");
                startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
                finish();
            }
        });

    }
}