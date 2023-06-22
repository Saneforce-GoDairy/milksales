package com.saneforce.milksales.Status_Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.MapDirectionActivity;
import com.saneforce.milksales.Status_Model_Class.Onduty_Status_Model;

import java.util.List;

public class Onduty_Status_Adapter extends RecyclerView.Adapter<Onduty_Status_Adapter.MyViewHolder> {

    private List<Onduty_Status_Model> Onduty_Status_ModelsList;
    private int rowLayout;
    private Context context;
    String AMod;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ondutydate, type, shift, odlocation, POV, intime, outtime, geoin, geoout, applieddate, OStatus, Papproved, SfName;
        RelativeLayout sf_namelayout;

        public MyViewHolder(View view) {
            super(view);
            ondutydate = view.findViewById(R.id.ondutydate);
            type = view.findViewById(R.id.type);
            shift = view.findViewById(R.id.shift);
            odlocation = view.findViewById(R.id.odlocation);
            POV = view.findViewById(R.id.POV);
            intime = view.findViewById(R.id.intime);
            outtime = view.findViewById(R.id.outtime);
            geoin = view.findViewById(R.id.geoin);
            OStatus = view.findViewById(R.id.OStatus);
            geoout = view.findViewById(R.id.geoout);
            applieddate = view.findViewById(R.id.applieddate);
            Papproved = view.findViewById(R.id.Papproved);
            SfName = view.findViewById(R.id.SfName);
            sf_namelayout = view.findViewById(R.id.sf_namelayout);
        }
    }


    public Onduty_Status_Adapter(List<Onduty_Status_Model> Onduty_Status_ModelsList, int rowLayout, Context context, String AMod) {
        this.Onduty_Status_ModelsList = Onduty_Status_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
    }

    @Override
    public Onduty_Status_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Onduty_Status_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Onduty_Status_Adapter.MyViewHolder holder, int position) {
        Onduty_Status_Model Onduty_Status_Model = Onduty_Status_ModelsList.get(position);
        // holder.sf_name.setText(""+Onduty_Status_Model.getSFNm());
        holder.ondutydate.setText("" + Onduty_Status_Model.getLoginDate());
        holder.type.setText("" + Onduty_Status_Model.getOndutytype());
        holder.shift.setText("" + Onduty_Status_Model.getShiftName());
        holder.odlocation.setText(Onduty_Status_Model.getODLocName());
        holder.POV.setText("" + Onduty_Status_Model.getReason());
        holder.intime.setText(Onduty_Status_Model.getStartTime());
        holder.outtime.setText(Onduty_Status_Model.getEndTime());
        holder.geoin.setText(Onduty_Status_Model.getCheckin());
        holder.geoout.setText(Onduty_Status_Model.getCheckout());
        holder.OStatus.setText(Onduty_Status_Model.getOStatus());
        holder.applieddate.setText("Applied : " + Onduty_Status_Model.getSubmissionDate());
 /*       if (Onduty_Status_Model.getWrkType() == 0) {
            holder.Papproved.setVisibility(View.VISIBLE);
            holder.Papproved.setText("Reject:" + Onduty_Status_Model.getApproveddate());
        } else if (Onduty_Status_Model.getWrkType() == 2) {
            holder.Papproved.setVisibility(View.VISIBLE);
            holder.Papproved.setText("Approved:" + Onduty_Status_Model.getApproveddate());
        } else {
            holder.Papproved.setVisibility(View.GONE);
        }*/
        holder.OStatus.setText(Onduty_Status_Model.getOStatus());
        if (Onduty_Status_Model.getWrkType() == 1) {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(Onduty_Status_Model.getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff9819"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.OStatus.setPadding(20, 5, 20, 0);
            holder.OStatus.setBackgroundResource(R.drawable.button_yellows);

            holder.Papproved.setText("");
        } else if (Onduty_Status_Model.getWrkType() == 2) {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(Onduty_Status_Model.getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#008000"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.Papproved.setText("Approved : " + Onduty_Status_Model.getApproveddate());
            holder.OStatus.setBackgroundResource(R.drawable.button_green);
            holder.OStatus.setPadding(20, 5, 20, 0);
        } else {
            if (AMod.equals("1")) {
                holder.sf_namelayout.setVisibility(View.VISIBLE);
                holder.SfName.setText(Onduty_Status_Model.getSFNm());
                holder.SfName.setTextColor(Color.parseColor("#ff3700"));
            } else {
                holder.sf_namelayout.setVisibility(View.GONE);
            }
            holder.Papproved.setText("Rejected : " + Onduty_Status_Model.getApproveddate());
            holder.OStatus.setBackgroundResource(R.drawable.button_red);
            holder.OStatus.setPadding(20, 5, 20, 0);
        }

        holder.geoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateMapDir(Onduty_Status_Model.getCheckin(),"Geo In");
//                Intent intent = new Intent(context, MapDirectionActivity.class);
//                intent.putExtra("Locations", Onduty_Status_Model.getCheckin());
//                context.startActivity(intent);

            }
        });

        holder.geoout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateMapDir(Onduty_Status_Model.getCheckout(),"Geo Out");
//                Intent intent = new Intent(context, MapDirectionActivity.class);
//                intent.putExtra("Locations", Onduty_Status_Model.getCheckout());
//                context.startActivity(intent);

            }
        });

    }

    void navigateMapDir(String value,String tag) {
        try {
            if (!com.saneforce.milksales.Activity_Hap.Common_Class.isNullOrEmpty(value)) {
                String[] latlongs = value.split(",");
                Intent intent = new Intent(context, MapDirectionActivity.class);
                intent.putExtra(Constants.DEST_LAT, latlongs[0]);
                intent.putExtra(Constants.DEST_LNG, latlongs[1]);
                intent.putExtra(Constants.DEST_NAME, tag);
                intent.putExtra(Constants.NEW_OUTLET, "GEO");
                context.startActivity(intent);

            }
        } catch (Exception e) {
            Log.v("ViewAllStatus:", e.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return Onduty_Status_ModelsList.size();
    }
}