package com.example.attendance_app_ezilinetest.student.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.attendance_app_ezilinetest.dataModels.Attendance;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class FireBaseAdapter_ViewAttendance extends FirebaseRecyclerAdapter<Attendance, FireBaseAdapter_ViewAttendance.AttendanceViewHolder> {

    private DatabaseReference mAttendanceDatabase;
    private SharedPreferences prefs;
    private static Context context;

    public FireBaseAdapter_ViewAttendance(@NonNull @NotNull FirebaseRecyclerOptions<Attendance> options, Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull FireBaseAdapter_ViewAttendance.AttendanceViewHolder holder,
                                    int position, @NonNull @NotNull Attendance model) {

        String date = getRef(position).getKey().toString();

        prefs = context.getSharedPreferences("MY_PREFS_NAME", context.MODE_PRIVATE);
        String roll_number1 = prefs.getString("roll_number", "Empty");

        mAttendanceDatabase = FirebaseDatabase.getInstance().getReference().child("Attendance");

        mAttendanceDatabase.child(date).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String roll_number = snapshot.getKey().toString();

                Log.wtf("-this", "57: " + roll_number);

                if (roll_number.contentEquals(roll_number1)) {
                    String status = snapshot.child("isPresent").getValue().toString();
                    Log.wtf("-this", "61 Status: " + status);
                    holder.setStatus(status);
                    holder.setDate(date);
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
    public AttendanceViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_attendance_item, parent, false);
        return new AttendanceViewHolder(view);
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView statusView, dateView;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.wtf("-this", "134 ");
            mView = itemView;
            statusView = mView.findViewById(R.id.statusViewAttendance);
            dateView = mView.findViewById(R.id.dateViewAttendance);

        }

        public void setStatus(String status) {

            if (status.contentEquals("Absent")) {
                statusView.setTextColor(ContextCompat.getColor(context, R.color.red));
                statusView.setText("Status: " + status);
            } else if (status.contentEquals("Present")) {
                statusView.setTextColor(ContextCompat.getColor(context, R.color.light_green));
                statusView.setText("Status: " + status);
            } else if (status.contentEquals("Leave")) {
                statusView.setTextColor(ContextCompat.getColor(context, R.color.orange));
                statusView.setText("Status: " + status);
            }
        }

        public void setDate(String date) {
            dateView.setText("Date: " + date);
        }
    }
}
