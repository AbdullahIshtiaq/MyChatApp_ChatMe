package com.example.attendance_app_ezilinetest.admin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendance_app_ezilinetest.R;
import com.example.attendance_app_ezilinetest.dataModels.Attendance;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

public class FireBaseAdapter_ViewAllAttendance extends FirebaseRecyclerAdapter<Attendance,
        FireBaseAdapter_ViewAllAttendance.ViewAllAttendanceViewHolder> {

    public static Context context;
    private FireBaseChildAdapter_ViewAllAttendance fireBaseChildAdapter_viewAllAttendance;

    public FireBaseAdapter_ViewAllAttendance(@NonNull @NotNull FirebaseRecyclerOptions<Attendance> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull FireBaseAdapter_ViewAllAttendance.ViewAllAttendanceViewHolder holder,
                                    int position, @NonNull @NotNull Attendance model) {

        String date = getRef(position).getKey().toString();
        Log.wtf("-this", "Date: " + date);

        DatabaseReference mAttendanceDatabase = FirebaseDatabase.getInstance().getReference().child("Attendance").child(date);
        Query query = mAttendanceDatabase;

        holder.setDateView(date);

        holder.linearLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.childRecyclerView.getVisibility() == View.GONE) {
                    holder.childRecyclerView.setVisibility(View.VISIBLE);
                    holder.CE_TV.setText("Collapse");
                    holder.CE_IV.setImageDrawable(null);
                    holder.CE_IV.setBackgroundResource(R.drawable.collapse_arrow);
                } else {
                    holder.childRecyclerView.setVisibility(View.GONE);
                    holder.CE_TV.setText("Expand");
                    holder.CE_IV.setImageDrawable(null);
                    holder.CE_IV.setBackgroundResource(R.drawable.expand_arrow);
                }
            }
        });

        holder.childRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Attendance>().setQuery(
                        query, Attendance.class
                ).build();

        fireBaseChildAdapter_viewAllAttendance = new FireBaseChildAdapter_ViewAllAttendance(options, context);

        holder.childRecyclerView.setAdapter(fireBaseChildAdapter_viewAllAttendance);

        fireBaseChildAdapter_viewAllAttendance.startListening();

    }

    @NonNull
    @NotNull
    @Override
    public ViewAllAttendanceViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_attendance_item, parent, false);
        return new FireBaseAdapter_ViewAllAttendance.ViewAllAttendanceViewHolder(view);
    }

    public static class ViewAllAttendanceViewHolder extends RecyclerView.ViewHolder {

        View mView;

        TextView dateView, CE_TV;
        ImageView CE_IV;
        RecyclerView childRecyclerView;
        LinearLayout linearLayoutBtn;


        public ViewAllAttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.wtf("-this", "134 ");
            mView = itemView;
            dateView = mView.findViewById(R.id.date_AllAttendance_item);
            childRecyclerView = mView.findViewById(R.id.childRec_ViewAllAttendance);
            linearLayoutBtn = mView.findViewById(R.id.linear_AllAttendance_item);
            CE_IV = mView.findViewById(R.id.collapse_ExpandArrows_AllAttendance_item);
            CE_TV = mView.findViewById(R.id.collapse_Expand_AllAttendance_item);
        }

        public void setDateView(String date) {

            this.dateView.setText(date);
        }
    }
}
