package com.saneforce.milksales.Status_Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.LeaveCancelReason;
import com.saneforce.milksales.Model_Class.LeaveCancelStatusModel;
import com.saneforce.milksales.R;

import java.util.List;

public class LeaveCancelStatusAdapter extends RecyclerView.Adapter<LeaveCancelStatusAdapter.MyViewHolder> {
    private List<LeaveCancelStatusModel> holiday_status_modelist;
    private int rowLayout;
    private Context context;
    String AMod;
    LeaveCancelReason mLeaveCancelRea;
    String EditextReason = "";
    Integer count = 0;


    @NonNull
    @Override
    public LeaveCancelStatusAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new LeaveCancelStatusAdapter.MyViewHolder(view);
    }

    public LeaveCancelStatusAdapter(String AMod,List<LeaveCancelStatusModel> holiday_status_modelist, int rowLayout, Context context) {
        this.holiday_status_modelist = holiday_status_modelist;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveCancelStatusAdapter.MyViewHolder holder, int position) {

        holder.HolidayDate.setText(holiday_status_modelist.get(position).getCreatedDate());
        holder.HolidayStatus.setText(holiday_status_modelist.get(position).getLStatus());
        holder.HolidayEntry.setText(holiday_status_modelist.get(position).getLeaveType());
        holder.HolidayReason.setText(holiday_status_modelist.get(position).getReason());
        holder.HolidayApplied.setText(holiday_status_modelist.get(position).getCreatedDate());
        if (holiday_status_modelist.get(position).getLeavecancelId() == 1) {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(holiday_status_modelist.get(position).getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff3700"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.HolidayStatus.setPadding(20,5,20,0);
            holder.HolidayReject.setText("Rejected : " + holiday_status_modelist.get(position).getLastUpdtDate());
            holder.HolidayStatus.setBackgroundResource(R.drawable.button_red);
        } else if (holiday_status_modelist.get(position).getLeavecancelId() == 0) {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(holiday_status_modelist.get(position).getSFNm());

                holder.SfName.setTextColor(Color.parseColor("#009688"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.HolidayStatus.setPadding(20,5,20,0);
            holder.HolidayReject.setText("Approved : " + holiday_status_modelist.get(position).getLastUpdtDate());
            holder.HolidayStatus.setBackgroundResource(R.drawable.button_green);
        } else {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(holiday_status_modelist.get(position).getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff9819"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.HolidayStatus.setBackgroundResource(R.drawable.button_yellows);
            holder.HolidayStatus.setPadding(20,5,20,0);
        }

    }

    @Override
    public int getItemCount() {
        return holiday_status_modelist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView HolidayDate, HolidayStatus, HolidayEntry, HolidayReason, HolidayApplied, HolidayReject, SfName, HolidayEntryDate;

        RelativeLayout sf_namelayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            HolidayDate = itemView.findViewById(R.id.leave_date);
            HolidayStatus = itemView.findViewById(R.id.leave_status);
            HolidayEntry = itemView.findViewById(R.id.leave_type);
            sf_namelayout = itemView.findViewById(R.id.sf_namelayout);
            HolidayReason = itemView.findViewById(R.id.leave_reason);
            HolidayApplied = itemView.findViewById(R.id.leave_applied);
            HolidayReject = itemView.findViewById(R.id.leave_rejected);
            SfName = itemView.findViewById(R.id.SfName);
        }
    }
}