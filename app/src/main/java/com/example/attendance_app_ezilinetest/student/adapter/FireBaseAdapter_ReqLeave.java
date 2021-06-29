package com.example.attendance_app_ezilinetest.student.adapter;

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
import com.example.attendance_app_ezilinetest.dataModels.Leave;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.jetbrains.annotations.NotNull;

public class FireBaseAdapter_ReqLeave extends FirebaseRecyclerAdapter<Leave, FireBaseAdapter_ReqLeave.ReqLeaveViewHolder> {

    private static Context context;

    public FireBaseAdapter_ReqLeave(@NonNull @NotNull FirebaseRecyclerOptions<Leave> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull FireBaseAdapter_ReqLeave.ReqLeaveViewHolder holder, int position, @NonNull @NotNull Leave model) {
        holder.setStartDateView(model.getStart_date());
        holder.setEndDateView(model.getEnd_date());
        holder.setStatusView(model.getStatus());
        holder.setDescriptionView(model.getReason());

    }

    @NonNull
    @NotNull
    @Override
    public ReqLeaveViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.req_leave_item, parent, false);
        return new FireBaseAdapter_ReqLeave.ReqLeaveViewHolder(view);

    }

    public static class ReqLeaveViewHolder extends RecyclerView.ViewHolder {

        View mView;

        TextView statusView, startDateView, endDateView, descriptionView;

        public ReqLeaveViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.wtf("-this", "134 ");
            mView = itemView;
            statusView = mView.findViewById(R.id.status_ReqItem);
            startDateView = mView.findViewById(R.id.startDate_ReqItem);
            endDateView = mView.findViewById(R.id.endDate_ReqItem);
            descriptionView = mView.findViewById(R.id.description_ReqItem);
        }

        public void setStatusView(String status) {
            if (status.contentEquals("Accepted")) {
                statusView.setTextColor(ContextCompat.getColor(context, R.color.light_green));
                this.statusView.setText("Status: " + status);
            } else if (status.contentEquals("Rejected")) {
                statusView.setTextColor(ContextCompat.getColor(context, R.color.red));
                this.statusView.setText("Status: " + status);
            } else {
                this.statusView.setText("Status: " + status);
            }
        }

        public void setStartDateView(String startDate) {
            this.startDateView.setText("Start Date: " + startDate);
        }

        public void setEndDateView(String endDate) {
            this.endDateView.setText("End Date: " + endDate);
        }

        public void setDescriptionView(String description) {
            this.descriptionView.setText("Reason: " + description);
        }
    }
}
