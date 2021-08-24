package com.example.attendance_app_ezilinetest.student.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.admin.ui.AdminHomeActivity;
import com.example.attendance_app_ezilinetest.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.installations.FirebaseInstallations;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private View view;
    private ProgressDialog mProgress;

    private FirebaseAuth fAuth;
    private DatabaseReference mUserDatabase;
    private FirebaseUser current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        mProgress = new ProgressDialog(this);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Students");
        fAuth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stdRollNumber = binding.rollNumberLogin.getText().toString();
                String passwordByUser = binding.passwordLogin.getText().toString();

                if (TextUtils.isEmpty(stdRollNumber)) {
                    binding.rollNumberLogin.setError("Required");
                    return;
                }
                binding.rollNumberLogin.setError(null);

                if (TextUtils.isEmpty(passwordByUser)) {
                    binding.passwordLogin.setError("Password is Required");
                    return;
                }
                binding.passwordLogin.setError(null);

                binding.btnLogin.setEnabled(false);

                mProgress.setTitle("Verifying Credentials ");
                mProgress.setMessage("Please wait while we are verifying credentials");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                if (stdRollNumber.contentEquals("admin") || stdRollNumber.contentEquals("Admin")) {
                    fAuth.signInWithEmailAndPassword(stdRollNumber + "@adm.com", passwordByUser)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        current_user = fAuth.getCurrentUser();

                                        mProgress.dismiss();
                                        Intent mainIntent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);

                                    } else {
                                        mProgress.dismiss();
                                        binding.btnLogin.setEnabled(true);
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else {
                    fAuth.signInWithEmailAndPassword(stdRollNumber + "@std.com", passwordByUser)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                                        binding.btnLogin.setEnabled(true);
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        binding.btnRegLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }
}