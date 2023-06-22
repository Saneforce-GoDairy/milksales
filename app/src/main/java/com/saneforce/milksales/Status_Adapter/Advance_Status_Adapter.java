package com.saneforce.milksales.Status_Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.LeaveCancelReason;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Advance_Status_Adapter extends RecyclerView.Adapter<Advance_Status_Adapter.MyViewHolder> {
    private JSONArray AdvList;
    private int rowLayout;
    private Context context;
    LeaveCancelReason mLeaveCancelRea;
    String EditextReason = "";
    String AMod;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView fromdatetodate, Advtype, AdvAmt, advLoc,AdvPurpose, applieddate,AdvSettl, LStatus, SfName,txtApproved;
        LinearLayout sf_namelayout;
        LinearLayout linearCancel, linearReason;
        Button ButtonCancel, ReasonSend;
        EditText ReasonEntry;

        public MyViewHolder(View view) {
            super(view);
            sf_namelayout = view.findViewById(R.id.sf_namelayout);
            fromdatetodate = view.findViewById(R.id.fromdatetodate);
            Advtype = view.findViewById(R.id.Advtype);
            AdvAmt = view.findViewById(R.id.AdvAmt);
            AdvPurpose = view.findViewById(R.id.AdvPurpose);
            advLoc = view.findViewById(R.id.advLoc);
            applieddate = view.findViewById(R.id.applieddate);
            AdvSettl = view.findViewById(R.id.AdvSettl);
            LStatus = view.findViewById(R.id.LStatus);
            linearReason = view.findViewById(R.id.linear_reason);
            ReasonSend = view.findViewById(R.id.reason_send);
            ReasonEntry = view.findViewById(R.id.reason_permission);
            txtApproved = view.findViewById(R.id.approvedate);
            sf_namelayout = view.findViewById(R.id.sf_namelayout);
        }
    }


    public Advance_Status_Adapter(JSONArray AdvList, int rowLayout, Context context, String AMod, LeaveCancelReason mLeaveCancelRea) {
        this.AdvList = AdvList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
        this.mLeaveCancelRea = mLeaveCancelRea;
    }

    @Override
    public Advance_Status_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Advance_Status_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Advance_Status_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        JSONObject item = null;
        try {
            item = AdvList.getJSONObject(position);
            holder.fromdatetodate.setText(item.getString("From_Date") + " TO " + item.getString("To_Date"));

            holder.Advtype.setText("" + item.getString("AdvTyp"));
            holder.AdvAmt.setText("" + item.getString("AdvAmt"));
            holder.advLoc.setText("" + item.getString("AdvLoc"));
            holder.AdvPurpose.setText("" + item.getString("AdvPurp"));
            holder.applieddate.setText("Applied: " + item.getString("eDate"));
            holder.AdvSettl.setText(item.getString("AdvSettle"));
            holder.LStatus.setText(item.getString("LStatus"));
            String color = item.getString("StusClr").replace(" !important", "");
            //holder.LStatus.setBackgroundColor(Color.parseColor(color.trim()));
            GradientDrawable drawable = (GradientDrawable) holder.LStatus.getBackground();
            drawable.setColor(Color.parseColor(color.trim()));

            int flag=item.getInt("flag");
            holder.sf_namelayout.setVisibility(View.VISIBLE);
            if(flag==1) {
                holder.txtApproved.setText(item.getString("ApprDt"));
            }else if (AMod.equalsIgnoreCase("1") && flag==0){
                holder.sf_namelayout.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return AdvList.length();
    }
}