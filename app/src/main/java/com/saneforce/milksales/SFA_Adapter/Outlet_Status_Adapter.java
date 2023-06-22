package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.LeaveCancelReason;
import com.saneforce.milksales.R;
import com.saneforce.milksales.Status_Model_Class.Leave_Status_Model;

import java.util.List;

public class Outlet_Status_Adapter extends RecyclerView.Adapter<Outlet_Status_Adapter.MyViewHolder> {
    private List<Leave_Status_Model> Leave_Status_ModelsList;
    private int rowLayout;
    private Context context;
    LeaveCancelReason mLeaveCancelRea;
    String EditextReason = "";
    String AMod;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fromdatetodate, leavetype, leavedays, leavereason, applieddate, LStatus, SfName,txtApproved;
        RelativeLayout sf_namelayout;
        LinearLayout linearCancel, linearReason;
        Button ButtonCancel, ReasonSend;
        EditText ReasonEntry;

        public MyViewHolder(View view) {
            super(view);
            fromdatetodate = view.findViewById(R.id.fromdatetodate);
            leavetype = view.findViewById(R.id.leavetype);
            leavedays = view.findViewById(R.id.leavedays);
            leavereason = view.findViewById(R.id.leavereason);
            applieddate = view.findViewById(R.id.applieddate);
            LStatus = view.findViewById(R.id.LStatus);
            SfName = view.findViewById(R.id.SfName);
            sf_namelayout = view.findViewById(R.id.sf_namelayout);
            linearCancel = view.findViewById(R.id.linear_cancel);
            linearReason = view.findViewById(R.id.linear_reason);
            ButtonCancel = view.findViewById(R.id.button_cancel);
            ReasonSend = view.findViewById(R.id.reason_send);
            ReasonEntry = view.findViewById(R.id.reason_permission);
            txtApproved = view.findViewById(R.id.approvedate);
        }
    }


    public Outlet_Status_Adapter(List<Leave_Status_Model> Leave_Status_ModelsList, int rowLayout, Context context, String AMod, LeaveCancelReason mLeaveCancelRea) {
        this.Leave_Status_ModelsList = Leave_Status_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
        this.mLeaveCancelRea = mLeaveCancelRea;
    }

    @Override
    public Outlet_Status_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Outlet_Status_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Outlet_Status_Adapter.MyViewHolder holder, int position) {
//        Leave_Status_Model Leave_Status_Model = Leave_Status_ModelsList.get(position);
//        holder.fromdatetodate.setText(Leave_Status_ModelsList.get(position).getFromDate() + " TO " + Leave_Status_ModelsList.get(position).getToDate());
//        holder.leavetype.setText("" + Leave_Status_ModelsList.get(position).getLeaveType());
//        holder.leavedays.setText("" + Leave_Status_ModelsList.get(position).getNoOfDays());
//        holder.leavereason.setText("" + Leave_Status_ModelsList.get(position).getReason());
//        holder.applieddate.setText("Applied: " + Leave_Status_ModelsList.get(position).getCreatedDate());
//        holder.LStatus.setText(Leave_Status_ModelsList.get(position).getLStatus());
//
//        Log.v("FLAG_LEAVE", String.valueOf(Leave_Status_ModelsList.get(position).getShowFlag()));
//        Log.v("FLAG_LEAVE_ACTIVE", String.valueOf(Leave_Status_ModelsList.get(position).getLeaveActiveFlag()));
//
//        if ((!AMod.equalsIgnoreCase("1")) && Leave_Status_ModelsList.get(position).getShowFlag().equalsIgnoreCase("1") && Leave_Status_ModelsList.get(position).getLeaveActiveFlag() != 3) {
//            holder.linearCancel.setVisibility(View.VISIBLE);
//
//        } else {
//            holder.linearCancel.setVisibility(View.GONE);
//        }
//
//
//        holder.ButtonCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mLeaveCancelRea.onCancelReason(Leave_Status_ModelsList.get(position).getLeaveId());
//                Log.v("ONCLICK_REASON_LEAVE",Leave_Status_ModelsList.get(position).getLeaveId());
//            }
//        });
//
//        if (Leave_Status_ModelsList.get(position).getLeaveActiveFlag() == 0) {
            holder.LStatus.setBackgroundResource(R.drawable.button_green);
            holder.LStatus.setPadding(20,5,20,0);
//            if (AMod.equals("1")) {
//                holder.sf_namelayout.setVisibility(View.VISIBLE);
//                holder.SfName.setText(Leave_Status_Model.getSFNm());
//                holder.SfName.setTextColor(Color.parseColor("#008000"));
//            } else {
//                holder.sf_namelayout.setVisibility(View.GONE);
//            }
//            holder.txtApproved.setText("Appproved : "+Leave_Status_ModelsList.get(position).getLastUpdtDate());
//        } else if (Leave_Status_ModelsList.get(position).getLeaveActiveFlag() == 2) {
//            holder.LStatus.setBackgroundResource(R.drawable.button_yellows);
//            holder.LStatus.setPadding(20,5,20,0);
//            if (AMod.equals("1")) {
//                holder.sf_namelayout.setVisibility(View.VISIBLE);
//                holder.SfName.setText(Leave_Status_Model.getSFNm());
//                holder.SfName.setTextColor(Color.parseColor("#ff9819"));
//            } else {
//                holder.sf_namelayout.setVisibility(View.GONE);
//            }
//        } else {
//            if (AMod.equals("1")) {
//                holder.sf_namelayout.setVisibility(View.VISIBLE);
//                holder.SfName.setText(Leave_Status_Model.getSFNm());
//                holder.SfName.setTextColor(Color.parseColor("#ff3700"));
//            } else {
//                holder.sf_namelayout.setVisibility(View.GONE);
//            }
//            holder.LStatus.setPadding(20,5,20,0);
//            holder.txtApproved.setText("Rejected : "+Leave_Status_ModelsList.get(position).getLastUpdtDate());
//            holder.LStatus.setBackgroundResource(R.drawable.button_red);
//        }
    }

    @Override
    public int getItemCount() {
        return Leave_Status_ModelsList.size();
    }
}