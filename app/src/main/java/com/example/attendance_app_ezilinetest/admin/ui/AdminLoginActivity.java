package com.example.attendance_app_ezilinetest.admin.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance_app_ezilinetest.MainActivity;
import com.example.attendance_app_ezilinetest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button login;
    private ProgressDialog mProgress;

    private FirebaseAuth fAuth;
    private FirebaseUser current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        username = findViewById(R.id.username_AdminLogin);
        password = findViewById(R.id.password_AdminLogin);
        login = findViewById(R.id.loginBtn_AdminLogin);

        mProgress = new ProgressDialog(this);

        fAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = username.getText().toString();
                String passwordByUser = password.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    username.setError("Phone Number Required");
                    return;
                }
                username.setError(null);

                if (TextUtils.isEmpty(passwordByUser)) {
                    password.setError("Password is Required");
                    return;
                }
                password.setError(null);

                login.setEnabled(false);

                mProgress.setTitle("Verifying Credentials ");
                mProgress.setMessage("Please wait while we are verifying credentials");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();


                fAuth.signInWithEmailAndPassword(userName + "@adm.com", passwordByUser)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    current_user = fAuth.getCurrentUser();

                                    mProgress.dismiss();
                                    Intent mainIntent = new Intent(AdminLoginActivity.this, AdminHomeActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);

                                } else {
                                    mProgress.dismiss();
                                    login.setEnabled(true);
                                    Toast.makeText(AdminLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(AdminLoginActivity.this, MainActivity.class));
        finish();
    }
}