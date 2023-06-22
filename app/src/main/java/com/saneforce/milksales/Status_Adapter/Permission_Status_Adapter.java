package com.saneforce.milksales.Status_Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saneforce.milksales.R;
import com.saneforce.milksales.Status_Model_Class.Permission_Status_Model;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Permission_Status_Adapter extends RecyclerView.Adapter<Permission_Status_Adapter.MyViewHolder> {
    private List<Permission_Status_Model> Permission_Status_ModelsList;
    private int rowLayout;
    private Context context;
    String AMod;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sf_name, permission_date, Ptime, Preason, Papplieddate, PStatus, Papproved, SfName,txtHours;
        RelativeLayout sf_namelayout;

        public MyViewHolder(View view) {
            super(view);
            sf_name = view.findViewById(R.id.SfName);
            permission_date = view.findViewById(R.id.permission_date);
            Ptime = view.findViewById(R.id.Ptime);
            Preason = view.findViewById(R.id.Preason);
            Papplieddate = view.findViewById(R.id.Papplieddate);
            PStatus = view.findViewById(R.id.PStatus);
            Papproved = view.findViewById(R.id.Papproved);
            SfName = view.findViewById(R.id.SfName);
            txtHours=view.findViewById(R.id.leavedays);
            sf_namelayout = view.findViewById(R.id.sf_namelayout);
        }
    }


    public Permission_Status_Adapter(List<Permission_Status_Model> Permission_Status_ModelsList, int rowLayout, Context context, String AMod) {
        this.Permission_Status_ModelsList = Permission_Status_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
    }

    @Override
    public Permission_Status_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Permission_Status_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Permission_Status_Adapter.MyViewHolder holder, int position) {
        Permission_Status_Model Permission_Status_Model = Permission_Status_ModelsList.get(position);
        //Log.e("SF_NAME", "" + Permission_Status_Model.getSFNm());
        // holder.sf_name.setText(""+Permission_Status_Model.getSFNm());
        holder.permission_date.setText("" + Permission_Status_Model.getPermissiondate());
        holder.Ptime.setText("" + Permission_Status_Model.getFromTime() + " to " + Permission_Status_Model.getToTime());
        holder.Preason.setText(Permission_Status_Model.getReason());
        holder.Papplieddate.setText("Applied: " + Permission_Status_Model.getCreatedDate());
        holder.PStatus.setText(Permission_Status_Model.getPStatus());
        holder.txtHours.setText(Permission_Status_Model.getNoofHours());
        if (Permission_Status_Model.getApprovalFlag() == 0) {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(Permission_Status_Model.getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#008000"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.PStatus.setBackgroundResource(R.drawable.button_green);
            holder.PStatus.setPadding(20,5,20,0);
        } else if (Permission_Status_Model.getApprovalFlag() == 2) {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(Permission_Status_Model.getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff9819"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.PStatus.setBackgroundResource(R.drawable.button_yellows);
            holder.PStatus.setPadding(20,5,20,0);
        } else {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(Permission_Status_Model.getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff3700"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.PStatus.setBackgroundResource(R.drawable.button_red);
            holder.PStatus.setPadding(20,5,20,0);
        }

        if (Permission_Status_Model.getApprovalFlag() == 1) {
            holder.Papproved.setVisibility(View.VISIBLE);
            holder.Papproved.setText("Reject:" + Permission_Status_Model.getApproveddate());
        } else if (Permission_Status_Model.getApprovalFlag() == 0) {
            holder.Papproved.setVisibility(View.VISIBLE);
            holder.Papproved.setText("Approved:" + Permission_Status_Model.getApproveddate());
        } else {
            holder.Papproved.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return Permission_Status_ModelsList.size();
    }
}