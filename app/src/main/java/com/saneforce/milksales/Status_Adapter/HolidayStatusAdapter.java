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
import com.saneforce.milksales.Model_Class.HolidayEntryModel;
import com.saneforce.milksales.R;

import java.util.List;

public class HolidayStatusAdapter extends RecyclerView.Adapter<HolidayStatusAdapter.MyViewHolder> {
    private List<HolidayEntryModel> holiday_status_modelist;
    private int rowLayout;
    private Context context;
    String AMod;
    LeaveCancelReason mLeaveCancelRea;
    String EditextReason = "";
    Integer count = 0;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MyViewHolder(view);
    }

    public HolidayStatusAdapter(String AMod, List<HolidayEntryModel> holiday_status_modelist, int rowLayout, Context context) {
        this.holiday_status_modelist = holiday_status_modelist;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.HolidayDate.setText(holiday_status_modelist.get(position).getSubmissionDate());
        holder.HolidayStatus.setText(holiday_status_modelist.get(position).getOStatus());
        holder.HolidayShitTime.setText(holiday_status_modelist.get(position).getShiftName());
        holder.HolidayInTime.setText(holiday_status_modelist.get(position).getStartTime());
        holder.HolidayOutTime.setText(holiday_status_modelist.get(position).getEndTime());
        holder.HolidayGeoIN.setText(holiday_status_modelist.get(position).getCheckin());
        holder.HolidayGeoOut.setText(holiday_status_modelist.get(position).getCheckout());
        holder.HolidayApplied.setText("Holiday Entry : " + holiday_status_modelist.get(position).getSubmissionDate());
        if (holiday_status_modelist.get(position).getDutyMode() == 2) {
            holder.HolidayStatus.setBackgroundResource(R.drawable.button_green);
            holder.HolidayStatus.setPadding(20,5,20,0);
            holder.HolidayReject.setText("Approved : " + holiday_status_modelist.get(position).getApproveddate());
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(holiday_status_modelist.get(position).getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#008000"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }

        } else if (holiday_status_modelist.get(position).getDutyMode() == 3) {
            holder.HolidayStatus.setBackgroundResource(R.drawable.button_red);
            holder.HolidayStatus.setPadding(20,5,20,0);
            holder.HolidayReject.setText("Rejected : " + holiday_status_modelist.get(position).getApproveddate());
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(holiday_status_modelist.get(position).getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff9819"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
        } else {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(holiday_status_modelist.get(position).getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff3700"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.HolidayStatus.setPadding(20,5,20,0);
            holder.HolidayStatus.setBackgroundResource(R.drawable.button_yellows);
        }

    }

    @Override
    public int getItemCount() {
        return holiday_status_modelist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView HolidayApplied,HolidayReject, HolidayDate, HolidayStatus, SfName, HolidayShitTime, HolidayInTime, HolidayOutTime, HolidayGeoIN, HolidayGeoOut;
        RelativeLayout sf_namelayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            HolidayDate = itemView.findViewById(R.id.holiday_date);
            HolidayStatus = itemView.findViewById(R.id.holiday_status);
            HolidayShitTime = itemView.findViewById(R.id.shift_timing);
            HolidayInTime = itemView.findViewById(R.id.holiday_in_time);
            HolidayOutTime = itemView.findViewById(R.id.holiday_out_time);
            HolidayGeoIN = itemView.findViewById(R.id.geo_in);
            HolidayGeoOut = itemView.findViewById(R.id.geo_out);
            HolidayApplied = itemView.findViewById(R.id.applieddate);
            HolidayReject = itemView.findViewById(R.id.deviation_rejected);

            SfName = itemView.findViewById(R.id.SfName);
            sf_namelayout = itemView.findViewById(R.id.sf_namelayout);
        }
    }
}
