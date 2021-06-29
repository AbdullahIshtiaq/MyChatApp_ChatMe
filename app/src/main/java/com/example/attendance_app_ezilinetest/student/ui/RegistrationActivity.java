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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance_app_ezilinetest.MainActivity;
import com.example.attendance_app_ezilinetest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.installations.FirebaseInstallations;

import java.util.ArrayList;
import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {
    private EditText name, rollNumber, Class, password;
    private TextView loginText;
    private Button register;
    private ProgressDialog mProgress;

    private FirebaseAuth fAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser current_User;

    ArrayList<String> rollNumberList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.fullName_Reg);
        rollNumber = findViewById(R.id.rollNumber_Reg);
        Class = findViewById(R.id.class_Reg);
        password = findViewById(R.id.password_Reg);
        register = findViewById(R.id.registerBtn_Reg);
        loginText = findViewById(R.id.login_Reg);

        mProgress = new ProgressDialog(this);

        fAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        searchRollNumber();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stdRollNumber = rollNumber.getText().toString().trim();

                if (TextUtils.isEmpty(stdRollNumber)) {
                    rollNumber.setError("Roll Number is Required");
                    return;
                }
                rollNumber.setError(null);

                findInList(stdRollNumber);
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    public void findInList(String rollnumber) {
        if (rollNumberList != null && rollNumberList.size() > 0) {
            boolean foundName = false;
            for (int i = 0; i < rollNumberList.size(); i++) {
                if (rollnumber.equals(rollNumberList.get(i))) {
                    foundName = true;
                    break;
                }
            }
            if (foundName) {
                rollNumber.setError("Roll Number Already Registered");
            } else {
                register();
            }
        } else {
            register();
        }
    }

    public void register() {

        String stdName = name.getText().toString().trim();
        String stdRollNumber = rollNumber.getText().toString().trim();
        String stdClass = Class.getText().toString().trim();
        String stdPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(stdName)) {
            name.setError("Name is Required");
            return;
        }
        name.setError(null);
        if (TextUtils.isEmpty(stdRollNumber)) {
            rollNumber.setError("Roll Number is Required");
            return;
        }
        rollNumber.setError(null);
        if (TextUtils.isEmpty(stdClass)) {
            Class.setError("Class is Required");
            return;
        }
        Class.setError(null);
        if (TextUtils.isEmpty(stdPassword)) {
            password.setError("Password is Required");
            return;
        }
        password.setError(null);

        if (stdPassword.length() < 6) {
            password.setError("Password Must be >= 6 Characters");
            return;
        }
        password.setError(null);

        String reg_no = stdRollNumber;
        stdRollNumber += "@std.com";

        register.setEnabled(false);

        mProgress.setTitle("Registering Student");
        mProgress.setMessage("Please wait while we create user");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String finalStdRollNumber = stdRollNumber;
        fAuth.createUserWithEmailAndPassword(stdRollNumber, stdPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                    current_User = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_User.getUid();
                    String deviceToken = FirebaseInstallations.getInstance().getToken(true).toString();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Students").child(uid);

                    HashMap<String, String> studentMap = new HashMap<>();
                    studentMap.put("device_token", deviceToken);
                    studentMap.put("name", stdName);
                    studentMap.put("roll_number", reg_no);
                    studentMap.put("class_room", stdClass);
                    studentMap.put("image", "default");
                    studentMap.put("password", stdPassword);

                    mDatabase.setValue(studentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mProgress.dismiss();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegistrationActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    mProgress.dismiss();
                }
            }
        });
    }

    private void searchRollNumber() {
        final DatabaseReference userRef = mDatabase.child("Students");
        Query namesQuery = userRef.orderByKey();
        rollNumberList.clear();
        namesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.hasChild("roll_number")) {
                    String rollnumber = snapshot.child("roll_number").getValue().toString();
                    rollNumberList.add(rollnumber);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
        finish();
    }

}