package com.milksales.godairy.Status_Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.Interface.LeaveCancelReason;
import com.milksales.godairy.Model_Class.DeviationEntryStatusModel;
import com.milksales.godairy.R;

import java.util.List;

public class DeviationEntryStatusAdapter extends RecyclerView.Adapter<DeviationEntryStatusAdapter.MyViewHolder> {
    private List<DeviationEntryStatusModel> holiday_status_modelist;
    private int rowLayout;
    private Context context;
    String AMod;
    LeaveCancelReason mLeaveCancelRea;
    String EditextReason = "";
    Integer count = 0;

    @NonNull
    @Override
    public DeviationEntryStatusAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new DeviationEntryStatusAdapter.MyViewHolder(view);
    }

    public DeviationEntryStatusAdapter(String AMod, List<DeviationEntryStatusModel> holiday_status_modelist, int rowLayout, Context context) {
        this.holiday_status_modelist = holiday_status_modelist;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviationEntryStatusAdapter.MyViewHolder holder, int position) {
        holder.HolidayDate.setText(holiday_status_modelist.get(holder.getAbsoluteAdapterPosition()).getDeviationDate());
        holder.HolidayStatus.setText(holiday_status_modelist.get(holder.getAbsoluteAdapterPosition()).getDStatus());
        holder.HolidayEntry.setText(holiday_status_modelist.get(holder.getAbsoluteAdapterPosition()).getDeviationType());
        holder.HolidayReason.setText(holiday_status_modelist.get(holder.getAbsoluteAdapterPosition()).getReason());
        holder.HolidayApplied.setText(holiday_status_modelist.get(holder.getAbsoluteAdapterPosition()).getCreatedDate());
        if (holiday_status_modelist.get(holder.getAbsoluteAdapterPosition()).getDeviActiveFlag() == 1) {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(holiday_status_modelist.get(holder.getAbsoluteAdapterPosition()).getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff3700"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }

            holder.HolidayStatus.setPadding(20,5,20,0);
            String hgfv = "Reject : " + holiday_status_modelist.get(holder.getAbsoluteAdapterPosition()).getLastUpdtDate();
            holder.HolidayReject.setText(hgfv);
            holder.HolidayStatus.setBackgroundResource(R.drawable.button_red);

            holder.rejectReason.setVisibility(View.VISIBLE);
            String rejectRea = "Reject Reason: " + holiday_status_modelist.get(holder.getAbsoluteAdapterPosition()).getRejectedReason();
            holder.rejectReason.setText(rejectRea);

        } else if (holiday_status_modelist.get(holder.getAbsoluteAdapterPosition()).getDeviActiveFlag() == 0) {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(holiday_status_modelist.get(holder.getAbsoluteAdapterPosition()).getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#009688"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.HolidayStatus.setPadding(20,5,20,0);
            holder.HolidayReject.setText("Approved : " + holiday_status_modelist.get(holder.getAbsoluteAdapterPosition()).getLastUpdtDate());
            holder.HolidayStatus.setBackgroundResource(R.drawable.button_green);
        } else {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(holiday_status_modelist.get(holder.getAbsoluteAdapterPosition()).getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff9819"));
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

        TextView HolidayDate, HolidayStatus, HolidayEntry, SfName, HolidayReason, HolidayApplied, HolidayReject, HolidayGeoIN, HolidayGeoOut, HolidayEntryDate, rejectReason;
        RelativeLayout sf_namelayout;

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
            rejectReason = itemView.findViewById(R.id.rejectReason);
        }
    }
}