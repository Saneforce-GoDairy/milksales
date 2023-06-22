package com.saneforce.milksales.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Interface.Joint_Work_Listner;
import com.saneforce.milksales.R;


import java.util.List;

public class Joint_Work_Adapter extends RecyclerView.Adapter<Joint_Work_Adapter.MyViewHolder> {

    private List<Common_Model> extendedShift_status_models;
    private int rowLayout;
    private Context context;
    String AMod;
    Joint_Work_Listner updateUi;

    public Joint_Work_Adapter(List<Common_Model> onduty_Status_ModelsList, int rowLayout, Context context, String AMod, Joint_Work_Listner listner) {
        extendedShift_status_models = onduty_Status_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
        this.updateUi = listner;
        Log.e("ONDUTY_DATA", String.valueOf(onduty_Status_ModelsList));
    }

    @NonNull
    @Override
    public Joint_Work_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);


        return new Joint_Work_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Joint_Work_Adapter.MyViewHolder holder, int position) {
        Common_Model Onduty_Status_Model = extendedShift_status_models.get(position);
        holder.jointworkname.setText(extendedShift_status_models.get(position).getName());
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                updateUi.onIntentClick(position,false);


            }
        });

    }

    @Override
    public int getItemCount() {
        return extendedShift_status_models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView jointworkname, type, shift, odlocation, POV, intime, outtime, geoin, geoout, applieddate, OStatus, Papproved, SfName;
        RelativeLayout sf_namelayout;
        ImageView check;

        public MyViewHolder(View view) {
            super(view);
            jointworkname = (TextView) view.findViewById(R.id.textView);
            check = view.findViewById(R.id.checkBox_select);
            geoout = (TextView) view.findViewById(R.id.txt_geo_out_time);
            applieddate = (TextView) view.findViewById(R.id.applieddate);
            Papproved = (TextView) view.findViewById(R.id.applieddate);
            SfName = view.findViewById(R.id.SfName);
            sf_namelayout = view.findViewById(R.id.sf_namelayout);
        }

        @Override
        public void onClick(View v) {

        }
    }


}