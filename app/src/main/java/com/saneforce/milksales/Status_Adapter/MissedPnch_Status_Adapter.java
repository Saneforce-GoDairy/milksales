package com.saneforce.milksales.Status_Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;
import com.saneforce.milksales.Status_Model_Class.MissedPunch_Status_Model;

import java.util.List;

public class MissedPnch_Status_Adapter extends RecyclerView.Adapter<MissedPnch_Status_Adapter.MyViewHolder> {

    private List<MissedPunch_Status_Model> missedPunchStatusModelList;
    private int rowLayout;
    private Context context;

    String AMod;

    public MissedPnch_Status_Adapter(List<MissedPunch_Status_Model> onduty_Status_ModelsList, int rowLayout, Context context, String AMod) {
        missedPunchStatusModelList = onduty_Status_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MissedPnch_Status_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MissedPunch_Status_Model Onduty_Status_Model = missedPunchStatusModelList.get(position);
        holder.ondutydate.setText("" + missedPunchStatusModelList.get(position).getMissedPunchDate());
        holder.intime.setText("" + missedPunchStatusModelList.get(position).getCheckinTime());
        holder.outtime.setText("" + missedPunchStatusModelList.get(position).getCheckoutTme());
        holder.POV.setText("" + missedPunchStatusModelList.get(position).getReason());
        holder.applieddate.setText("Applied : " + missedPunchStatusModelList.get(position).getSubmissionDate());
        holder.OStatus.setText(Onduty_Status_Model.getMPStatus());
        if (Onduty_Status_Model.getMissedPunchFlag() == 2) {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(Onduty_Status_Model.getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#008000"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.Papproved.setText("Approved : " + missedPunchStatusModelList.get(position).getRejectdate());
            holder.OStatus.setBackgroundResource(R.drawable.button_green);
            holder.OStatus.setPadding(20, 5, 20, 0);
        } else if (Onduty_Status_Model.getMissedPunchFlag() == 3) {

            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(Onduty_Status_Model.getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff3700"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.Papproved.setText("Reject : " + missedPunchStatusModelList.get(position).getRejectdate());
            holder.OStatus.setBackgroundResource(R.drawable.button_red);
            holder.OStatus.setPadding(20, 5, 20, 0);
        } else {
            holder.OStatus.setBackgroundResource(R.drawable.button_yellows);
            holder.OStatus.setPadding(20, 5, 20, 0);
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(Onduty_Status_Model.getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff9819"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
        }
        try {
            holder.type.setText("" + missedPunchStatusModelList.get(position).getShiftName().replaceAll("]", "").replaceAll("\\[", ""));
        } catch (Exception e) {
            Log.v("viewAllStatus:", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return missedPunchStatusModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ondutydate, type, shift, odlocation, POV, intime, outtime, geoin, geoout, applieddate, OStatus, Papproved, SfName;
        RelativeLayout sf_namelayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ondutydate = itemView.findViewById(R.id.ondutydate);
            type = itemView.findViewById(R.id.type);
            odlocation = itemView.findViewById(R.id.odlocation);
            POV = itemView.findViewById(R.id.text_reason);
            intime = itemView.findViewById(R.id.txt_in_time);
            outtime = itemView.findViewById(R.id.txt_out_time);
            geoin = itemView.findViewById(R.id.geoin);
            OStatus = itemView.findViewById(R.id.OStatus);
            geoout = itemView.findViewById(R.id.geoout);
            applieddate = itemView.findViewById(R.id.applieddate);
            Papproved = itemView.findViewById(R.id.Papproved);
            SfName = itemView.findViewById(R.id.SfName);
            sf_namelayout = itemView.findViewById(R.id.sf_namelayout);
        }
    }
}
