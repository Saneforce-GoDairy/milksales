package com.saneforce.milksales.Status_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;
import com.saneforce.milksales.Status_Model_Class.WeekOff_Status_Model;

import java.util.List;

public class WeekOff_Status_Adapter extends RecyclerView.Adapter<WeekOff_Status_Adapter.MyViewHolder> {

    private List<WeekOff_Status_Model> Onduty_Status_ModelsList;
    private int rowLayout;
    private Context context;

    String AMod;
    public WeekOff_Status_Adapter(List<WeekOff_Status_Model> Onduty_Status_ModelsList, int rowLayout, Context context,String AMod) {
        this.Onduty_Status_ModelsList = Onduty_Status_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
    }

    @Override
    public WeekOff_Status_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new WeekOff_Status_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeekOff_Status_Adapter.MyViewHolder holder, int position) {
        WeekOff_Status_Model Onduty_Status_Model = Onduty_Status_ModelsList.get(position);
        holder.onWorkType.setText("" + Onduty_Status_ModelsList.get(position).getWrkTyp());
        holder.onWorkDate.setText("" + Onduty_Status_ModelsList.get(position).getWkDate());
        holder.onWorkDay.setText(" - " + Onduty_Status_ModelsList.get(position).getDtNm());
        holder.onWorkSubmitDate.setText(" " + Onduty_Status_ModelsList.get(position).getSbmtOn());
    }

    @Override
    public int getItemCount() {
        return Onduty_Status_ModelsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView onWorkType, onWorkDate, onWorkDay, onWorkSubmitDate,SfName;
        RelativeLayout sf_namelayout;
        public MyViewHolder(View view) {
            super(view);
            onWorkType = view.findViewById(R.id.txt_wktype);
            onWorkDate = view.findViewById(R.id.txt_wkdate);
            onWorkDay = view.findViewById(R.id.txt_wkday);
            onWorkSubmitDate = view.findViewById(R.id.txt_submit_date);
            SfName = view.findViewById(R.id.SfName);
            sf_namelayout = view.findViewById(R.id.sf_namelayout);
        }
    }

}