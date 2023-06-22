package com.saneforce.milksales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.saneforce.milksales.Interface.LeaveCancelReason;
import com.saneforce.milksales.Model_Class.DaExceptionStatusModel;
import com.saneforce.milksales.R;

import java.util.List;

public class DaExceptionStatusAdapt extends RecyclerView.Adapter<DaExceptionStatusAdapt.MyViewHolder> {
    private List<DaExceptionStatusModel> holiday_status_modelist;
    private int rowLayout;
    private Context context;
    String AMod;
    LeaveCancelReason mLeaveCancelRea;
    String EditextReason = "";
    Integer count = 0;

    @NonNull
    @Override
    public DaExceptionStatusAdapt.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new DaExceptionStatusAdapt.MyViewHolder(view);
    }

    public DaExceptionStatusAdapt(String AMod, List<DaExceptionStatusModel> holiday_status_modelist, int rowLayout, Context context) {
        this.holiday_status_modelist = holiday_status_modelist;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
    }

    @Override
    public void onBindViewHolder(@NonNull DaExceptionStatusAdapt.MyViewHolder holder, int position) {
        holder.HolidayDate.setText(holiday_status_modelist.get(position).getAppliedDate());
        holder.HolidayStatus.setText(holiday_status_modelist.get(position).getStatus());
        holder.HolidayEntry.setText(holiday_status_modelist.get(position).getDA_Type());
        holder.Actutalcheckin.setText(holiday_status_modelist.get(position).getActualTime());
        holder.EarlyCheckin.setText(holiday_status_modelist.get(position).getEarlyLateTime());
        holder.ActualLateCheck.setText(holiday_status_modelist.get(position).getActualTime());
        holder.LateCheckout.setText(holiday_status_modelist.get(position).getEarlyLateTime());
        holder.FromType.setText(holiday_status_modelist.get(position).getFMOTName());
        holder.ToType.setText(holiday_status_modelist.get(position).getTMOTName());
        holder.amount.setText("" + holiday_status_modelist.get(position).getDAAmt());
        holder.HolidayApplied.setText(holiday_status_modelist.get(position).getAppliedDate().toString());


        if (holiday_status_modelist.get(position).getDA_Type().equalsIgnoreCase("EARLY CHECK-IN")) {

            holder.LinEarly.setVisibility(View.VISIBLE);
            holder.LinLate.setVisibility(View.GONE);
            holder.LinMode.setVisibility(View.GONE);
        } else if (holiday_status_modelist.get(position).getDA_Type().equalsIgnoreCase("LATE CHECK-OUT")) {
            holder.LinEarly.setVisibility(View.GONE);
            holder.LinLate.setVisibility(View.VISIBLE);
            holder.LinMode.setVisibility(View.GONE);
        } else {
            holder.LinEarly.setVisibility(View.GONE);
            holder.LinLate.setVisibility(View.GONE);
            holder.LinMode.setVisibility(View.VISIBLE);
        }

        if (holiday_status_modelist.get(position).getApproval_Flag().equalsIgnoreCase("2")) {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(holiday_status_modelist.get(position).getName());
                holder.SfName.setTextColor(Color.parseColor("#ff3700"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.HolidayStatus.setPadding(20, 5, 20, 0);
            //  holder.HolidayReject.setText("Reject : " + holiday_status_modelist.get(position).getLastUpdtDate());
            holder.HolidayStatus.setBackgroundResource(R.drawable.button_red);
        } else if (holiday_status_modelist.get(position).getApproval_Flag().equalsIgnoreCase("1")) {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(holiday_status_modelist.get(position).getName());
                holder.SfName.setTextColor(Color.parseColor("#009688"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.HolidayStatus.setPadding(20, 5, 20, 0);
            //     holder.HolidayReject.setText("Approved : " + holiday_status_modelist.get(position).getLastUpdtDate());
            holder.HolidayStatus.setBackgroundResource(R.drawable.button_green);
        } else {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(holiday_status_modelist.get(position).getName());
                holder.SfName.setTextColor(Color.parseColor("#ff9819"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.HolidayStatus.setPadding(20, 5, 20, 0);
            holder.HolidayStatus.setBackgroundResource(R.drawable.button_yellows);
        }

        try {
            Glide.with(context)
                    .load(holiday_status_modelist.get(position).getDA_Url())
                    .into(holder.ivUrl);
        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        return holiday_status_modelist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView HolidayDate, HolidayStatus, HolidayEntry, SfName, HolidayReason, HolidayApplied, HolidayReject, HolidayGeoIN, HolidayGeoOut, HolidayEntryDate;
        RelativeLayout sf_namelayout;

        TextView Actutalcheckin, EarlyCheckin, ActualLateCheck, LateCheckout, FromType, ToType, amount;

        LinearLayout Approvereject, rejectonly, LinEarly, LinLate, LinMode;
        ImageView ivUrl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            HolidayDate = itemView.findViewById(R.id.deviation_date);
            HolidayStatus = itemView.findViewById(R.id.devitaion_status);
            HolidayEntry = itemView.findViewById(R.id.deviation_entry);
            HolidayReason = itemView.findViewById(R.id.deviation_reason);
            HolidayApplied = itemView.findViewById(R.id.deviation_applied);
            HolidayReject = itemView.findViewById(R.id.deviation_rejected);
            sf_namelayout = itemView.findViewById(R.id.sf_namelayout);
            SfName = itemView.findViewById(R.id.SfName);


            Actutalcheckin = itemView.findViewById(R.id.workinghours);
            EarlyCheckin = itemView.findViewById(R.id.shiftdate);
            amount = itemView.findViewById(R.id.amnt_txt);
            ActualLateCheck = itemView.findViewById(R.id.lateactualout);
            LateCheckout = itemView.findViewById(R.id.latecheckout);
            FromType = itemView.findViewById(R.id.from_type);
            ToType = itemView.findViewById(R.id.to_type);


            LinEarly = itemView.findViewById(R.id.lin_actual);
            LinLate = itemView.findViewById(R.id.lin_late);
            LinMode = itemView.findViewById(R.id.lin_daType);
            ivUrl = itemView.findViewById(R.id.ivExp);
        }
    }
}