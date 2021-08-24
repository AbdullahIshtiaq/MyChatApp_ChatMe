package com.example.attendance_app_ezilinetest.admin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.dataModels.Student;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

public class FireBaseAdapter_StudentDetailsAdmHome extends FirebaseRecyclerAdapter<Student,
        FireBaseAdapter_StudentDetailsAdmHome.StudentDetailsViewHolder> {

    public static Context context;

    public FireBaseAdapter_StudentDetailsAdmHome(@NonNull @NotNull FirebaseRecyclerOptions<Student> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull FireBaseAdapter_StudentDetailsAdmHome.StudentDetailsViewHolder holder,
                                    int position, @NonNull @NotNull Student model) {

        DatabaseReference mAttendanceDatabase = FirebaseDatabase.getInstance().getReference().child("Attendance");
        Query query = mAttendanceDatabase.orderByKey();

        Log.wtf("-this", " Roll Number: " + model.getRoll_number());

        final int[] presentCount = {0};
        final int[] absentCount = {0};

        holder.setRollNumberView(model.getRoll_number());
        holder.setNameView(model.getName());
        holder.setClassView(model.getClass_room());

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String date = snapshot.getKey().toString();
                Log.wtf("-this", "52 SnapShot Date: " + date);
                if (snapshot.hasChild(model.getRoll_number())) {
                    if (snapshot.child(model.getRoll_number()).child("isPresent").getValue().toString().contentEquals("Present")) {
                        presentCount[0] += 1;
                        Log.wtf("-this", "52 Present: " + presentCount[0]);
                    } else {
                        absentCount[0] += 1;
                        Log.wtf("-this", "55 Absent: " + absentCount[0]);
                    }

                    String present = Integer.toString(presentCount[0]);
                    String absent = Integer.toString(absentCount[0]);

                    // Log.wtf("-this", "67: "+presentCount[0]+" :: "+absentCount[0] + " = "+ ((1 / (1+4))*100));

                    int grade = (presentCount[0] * 100 / (presentCount[0] + absentCount[0]));


                    Log.wtf("-this", "Grade " + grade);
                    // Log.wtf("-this", "67: "+presentCount[0]+" :: "+absentCount[0]);

                    holder.setPresentView(Integer.toString(presentCount[0]));
                    holder.setAbsentView(Integer.toString(absentCount[0]));
                    holder.setGradeView(grade);

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

    @NonNull
    @NotNull
    @Override
    public StudentDetailsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_details_item, parent, false);
        return new FireBaseAdapter_StudentDetailsAdmHome.StudentDetailsViewHolder(view);
    }

    public static class StudentDetailsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        TextView gradeView, rollNumberView, nameView, presentView, absentView, classView;

        public StudentDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.wtf("-this", "134 ");
            mView = itemView;
            gradeView = mView.findViewById(R.id.attendanceGrade_StudentItem);
            rollNumberView = mView.findViewById(R.id.rollNumber_StudentItem);
            nameView = mView.findViewById(R.id.name_StudentItem);
            presentView = mView.findViewById(R.id.presents_StudentItem);
            absentView = mView.findViewById(R.id.absents_StudentItem);
            classView = mView.findViewById(R.id.classRoom_StudentItem);
        }

        public void setGradeView(int grade) {
            String Grade = Integer.toString(grade);
            if (grade > 79) {
                gradeView.setTextColor(ContextCompat.getColor(context, R.color.light_green));
                this.gradeView.setText(grade + "%");
            } else if (grade > 49) {
                gradeView.setTextColor(ContextCompat.getColor(context, R.color.orange));
                this.gradeView.setText(grade + "%");
            } else {
                gradeView.setTextColor(ContextCompat.getColor(context, R.color.red));
                this.gradeView.setText(grade + "%");
            }
        }

        public void setNameView(String name) {
            this.nameView.setText(name);
        }

        public void setRollNumberView(String rollNumber) {
            this.rollNumberView.setText(rollNumber);
        }

        public void setPresentView(String present) {
            this.presentView.setText(present);
        }

        public void setAbsentView(String absent) {
            this.absentView.setText(absent);
        }

        public void setClassView(String Class) {
            this.classView.setText("Class: " + Class);
        }
    }
}
