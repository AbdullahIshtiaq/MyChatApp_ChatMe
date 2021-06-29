package com.example.attendance_app_ezilinetest.admin.adapter;

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
import com.example.attendance_app_ezilinetest.dataModels.customAttendance;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerViewAdapter_GenerateStudentReport extends RecyclerView.Adapter<RecyclerViewAdapter_GenerateStudentReport.AttendanceReportViewHolder> {

    private static Context context;
    private ArrayList<customAttendance> recAttendanceList;

    public RecyclerViewAdapter_GenerateStudentReport(@NonNull @NotNull Context context, ArrayList<customAttendance> recAttendanceList) {
        this.context = context;
        this.recAttendanceList = recAttendanceList;
    }

    @NonNull
    @NotNull
    @Override
    public AttendanceReportViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_report_item, parent, false);
        return new RecyclerViewAdapter_GenerateStudentReport.AttendanceReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewAdapter_GenerateStudentReport
            .AttendanceReportViewHolder holder, int position) {
        holder.setDate(recAttendanceList.get(position).getDate());
        holder.setStatus(recAttendanceList.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return recAttendanceList.size();
    }

    public static class AttendanceReportViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView statusView, dateView;

        public AttendanceReportViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.wtf("-this", "134 ");
            mView = itemView;
            statusView = mView.findViewById(R.id.statusTV_StudentReportItem);
            dateView = mView.findViewById(R.id.dateTV_StudentReportItem);

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
