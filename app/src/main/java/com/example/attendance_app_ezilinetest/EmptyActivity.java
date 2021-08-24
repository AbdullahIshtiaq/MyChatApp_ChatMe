package com.example.attendance_app_ezilinetest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance_app_ezilinetest.student.ui.LoginActivity;
import com.example.attendance_app_ezilinetest.student.ui.StudentHomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        Log.wtf("-this", "Empty Activity ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.wtf("-this", "First if");
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(EmptyActivity.this, LoginActivity.class));
            finish();
        } else if (currentUser.getEmail().contentEquals("admin@adm.com")) {
            Log.wtf("-this", "Its Admin");
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(EmptyActivity.this, LoginActivity.class));
            finish();
        } else {
            Log.wtf("-this", "Student: " + currentUser.getEmail());
            startActivity(new Intent(EmptyActivity.this, StudentHomeActivity.class));
            finish();
        }

    }

}