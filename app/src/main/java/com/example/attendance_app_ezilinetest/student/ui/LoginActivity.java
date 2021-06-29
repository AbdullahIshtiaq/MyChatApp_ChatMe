package com.example.attendance_app_ezilinetest.student.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.installations.FirebaseInstallations;

public class LoginActivity extends AppCompatActivity {

    private EditText rollNumber, password;
    private TextView registerText;
    private Button login;
    private ProgressDialog mProgress;

    private FirebaseAuth fAuth;
    private DatabaseReference mUserDatabase;
    private FirebaseUser current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rollNumber = findViewById(R.id.rollNumber_Login);
        password = findViewById(R.id.password_Login);
        login = findViewById(R.id.loginBtn_Login);
        registerText = findViewById(R.id.register_Login);

        mProgress = new ProgressDialog(this);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Students");
        fAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stdRollNumber = rollNumber.getText().toString();
                String passwordByUser = password.getText().toString();

                if (TextUtils.isEmpty(stdRollNumber)) {
                    rollNumber.setError("Phone Number Required");
                    return;
                }
                rollNumber.setError(null);

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


                fAuth.signInWithEmailAndPassword(stdRollNumber + "@std.com", passwordByUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            current_user = fAuth.getCurrentUser();

                            // if (current_user.isEmailVerified()) {

                            String uid = current_user.getUid();
                            String deviceToken = FirebaseInstallations.getInstance().getToken(true).toString();

                            mUserDatabase.child(uid).child("device_token").setValue(deviceToken)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mProgress.dismiss();
                                                Intent mainIntent = new Intent(LoginActivity.this, StudentHomeActivity.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(mainIntent);
                                            }
                                        }
                                    });
                            //}
                        } else {
                            mProgress.dismiss();
                            login.setEnabled(true);
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}