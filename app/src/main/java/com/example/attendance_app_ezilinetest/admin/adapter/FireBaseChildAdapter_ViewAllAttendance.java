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
import com.example.attendance_app_ezilinetest.dataModels.Attendance;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

public class FireBaseChildAdapter_ViewAllAttendance extends FirebaseRecyclerAdapter<Attendance,
        FireBaseChildAdapter_ViewAllAttendance.ViewAllAttendanceChildViewHolder> {
    public static Context context;


    public FireBaseChildAdapter_ViewAllAttendance(@NonNull @NotNull FirebaseRecyclerOptions<Attendance> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull FireBaseChildAdapter_ViewAllAttendance.ViewAllAttendanceChildViewHolder holder, int position, @NonNull @NotNull Attendance model) {
        String roll_number = getRef(position).getKey().toString();
        Log.wtf("-this", "RollNumber: " + roll_number);
        Log.wtf("-this", "isPresent: " + model.getIsPresent());


        DatabaseReference mStudentDatabase = FirebaseDatabase.getInstance().getReference().child("Students");
        Query query = mStudentDatabase;

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.child("roll_number").getValue().toString().contentEquals(roll_number)) {
                    String name = snapshot.child("name").getValue().toString();
                    holder.setRollNumberView(roll_number);
                    holder.setNameView(name);
                    holder.setStatusView(model.getIsPresent());
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
    public ViewAllAttendanceChildViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_attendance_child_item, parent, false);
        return new FireBaseChildAdapter_ViewAllAttendance.ViewAllAttendanceChildViewHolder(view);
    }

    public static class ViewAllAttendanceChildViewHolder extends RecyclerView.ViewHolder {

        View mView;

        TextView rollNumberView, nameView, statusView;

        public ViewAllAttendanceChildViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.wtf("-this", "Child View Holder");
            mView = itemView;
            rollNumberView = mView.findViewById(R.id.rollNumber_AllAttendance_Child_item);
            nameView = mView.findViewById(R.id.name_AllAttendance_Child_item);
            statusView = mView.findViewById(R.id.status_AllAttendance_Child_item);
        }

        public void setNameView(String name) {
            this.nameView.setText(name);
        }

        public void setRollNumberView(String rollNumber) {
            this.rollNumberView.setText(rollNumber);
        }

        public void setStatusView(String status) {

            if (status.contentEquals("Present")) {
                statusView.setTextColor(ContextCompat.getColor(context, R.color.light_green));
                this.statusView.setText("Status: " + status);
            } else if (status.contentEquals("Absent")) {
                statusView.setTextColor(ContextCompat.getColor(context, R.color.red));
                this.statusView.setText("Status: " + status);
            } else if (status.contentEquals("Leave")) {
                statusView.setTextColor(ContextCompat.getColor(context, R.color.orange));
                this.statusView.setText("Status: " + status);
            }


        }
    }

}
