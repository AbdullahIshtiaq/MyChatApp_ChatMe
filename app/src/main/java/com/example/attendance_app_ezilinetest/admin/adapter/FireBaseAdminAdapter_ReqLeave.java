package com.example.attendance_app_ezilinetest.admin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.dataModels.Leave;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class FireBaseAdminAdapter_ReqLeave extends FirebaseRecyclerAdapter<Leave, FireBaseAdminAdapter_ReqLeave.ReqLeaveAdminViewHolder> {
    private static Context context;

    public FireBaseAdminAdapter_ReqLeave(@NonNull @NotNull FirebaseRecyclerOptions<Leave> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull FireBaseAdminAdapter_ReqLeave.ReqLeaveAdminViewHolder holder,
                                    int position, @NonNull @NotNull Leave model) {
        String key = getRef(position).getKey().toString();

        DatabaseReference mStudentDatabase = FirebaseDatabase.getInstance().getReference().child("Students");
        DatabaseReference mLeaveDatabase = FirebaseDatabase.getInstance().getReference().child("leaves");

        mStudentDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.child("roll_number").getValue().toString().contentEquals(model.getRoll_number())) {
                    String name = snapshot.child("name").getValue().toString();
                    Log.wtf("-this", "47 Name " + name);
                    Log.wtf("-this", "47 Roll " + model.getRoll_number());

                    holder.setRollNumberView(model.getRoll_number());
                    holder.setNameView(name);
                    holder.setStartDateView(model.getStart_date());
                    holder.setEndDateView(model.getEnd_date());
                    holder.setDescriptionView(model.getReason());
                    holder.setStatusView(model.getStatus());

                    if (model.getStatus().contentEquals("Pending")) {
                        holder.acceptBtn.setVisibility(View.VISIBLE);
                        holder.rejectBtn.setVisibility(View.VISIBLE);

                        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mLeaveDatabase.child(key).child("status").setValue("Accepted");
                                holder.acceptBtn.setVisibility(View.GONE);
                                holder.rejectBtn.setVisibility(View.GONE);

                            }
                        });

                        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mLeaveDatabase.child(key).child("status").setValue("Rejected");
                                holder.acceptBtn.setVisibility(View.GONE);
                                holder.rejectBtn.setVisibility(View.GONE);

                            }
                        });

                    } else {
                        holder.acceptBtn.setVisibility(View.GONE);
                        holder.rejectBtn.setVisibility(View.GONE);
                    }
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
    public ReqLeaveAdminViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.req_leave_admin_item, parent, false);
        return new FireBaseAdminAdapter_ReqLeave.ReqLeaveAdminViewHolder(view);
    }

    public static class ReqLeaveAdminViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView nameView, rollNumberView, statusView, startDateView, endDateView, descriptionView;
        Button acceptBtn, rejectBtn;

        public ReqLeaveAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.wtf("-this", "134 ");
            mView = itemView;

            nameView = mView.findViewById(R.id.name_ReqItemAdmin);
            rollNumberView = mView.findViewById(R.id.rollNumber_ReqItemAdmin);
            statusView = mView.findViewById(R.id.status_ReqItemAdmin);
            startDateView = mView.findViewById(R.id.startDate_ReqItemAdmin);
            endDateView = mView.findViewById(R.id.endDate_ReqItemAdmin);
            descriptionView = mView.findViewById(R.id.description_ReqItemAdmin);
            acceptBtn = mView.findViewById(R.id.acceptBtn_ReqItemAdmin);
            rejectBtn = mView.findViewById(R.id.rejectBtn_ReqItemAdmin);
        }

        public void setStatusView(String status) {
            if (status.contentEquals("Accepted")) {
                statusView.setTextColor(ContextCompat.getColor(context, R.color.light_green));
                this.statusView.setText("Status: " + status);
            } else if (status.contentEquals("Rejected")) {
                statusView.setTextColor(ContextCompat.getColor(context, R.color.red));
                this.statusView.setText("Status: " + status);
            } else if (status.contentEquals("Pending")) {
                this.statusView.setText("Status: " + status);
            }
        }

        public void setStartDateView(String startDate) {
            this.startDateView.setText("Start Date: " + startDate);
        }

        public void setEndDateView(String endDate) {
            this.endDateView.setText("End Date: " + endDate);
        }

        public void setNameView(String name) {
            this.nameView.setText(name);
        }

        public void setDescriptionView(String description) {
            this.descriptionView.setText("Reason: " + description);
        }

        public void setRollNumberView(String rollNumber) {
            this.rollNumberView.setText(rollNumber);
        }
    }
}
