package com.example.attendance_app_ezilinetest.admin.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.admin.ui.EditAttendanceActivity;
import com.example.attendance_app_ezilinetest.dataModels.customAttendance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerViewAdapter_EditAttendance extends
        RecyclerView.Adapter<RecyclerViewAdapter_EditAttendance.EditAttendanceViewHolder> {

    private static Context context;
    private Activity activity;
    private ArrayList<customAttendance> recAttendanceList;


    public RecyclerViewAdapter_EditAttendance(Context context, ArrayList<customAttendance> recAttendanceList, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.recAttendanceList = recAttendanceList;
    }

    @NonNull
    @NotNull
    @Override
    public EditAttendanceViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_attendance_item, parent,
                false);
        return new RecyclerViewAdapter_EditAttendance.EditAttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewAdapter_EditAttendance.EditAttendanceViewHolder holder,
                                 int position) {
        DatabaseReference mAttendance = FirebaseDatabase.getInstance().getReference().child("Attendance");
        holder.setDate(recAttendanceList.get(position).getDate());
        holder.setStatus(recAttendanceList.get(position).getStatus());
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditAttendanceActivity.showRelativeView();

                EditAttendanceActivity.presentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAttendance.child(recAttendanceList.get(position).getDate())
                                .child(recAttendanceList.get(position).getRollNumber())
                                .child("isPresent").setValue("Present")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            holder.setStatus("Present");
                                            EditAttendanceActivity.hideRelativeView();
                                        }
                                    }
                                });
                    }
                });

                EditAttendanceActivity.absentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAttendance.child(recAttendanceList.get(position).getDate())
                                .child(recAttendanceList.get(position).getRollNumber())
                                .child("isPresent").setValue("Absent")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            holder.setStatus("Absent");
                                            EditAttendanceActivity.hideRelativeView();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return recAttendanceList.size();
    }

    public static class EditAttendanceViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView statusView, dateView, editBtn;

        public EditAttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.wtf("-this", "134 ");
            mView = itemView;
            statusView = mView.findViewById(R.id.statusTV_EditAttendance);
            dateView = mView.findViewById(R.id.dateTV_EditAttendance);
            editBtn = mView.findViewById(R.id.editBtn_EditAttendance);

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
