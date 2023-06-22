package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Model_Class.Holiday_Entry_Modal;
import com.saneforce.milksales.R;

import java.util.List;

public class Holiday_Approval_Adapter extends RecyclerView.Adapter<Holiday_Approval_Adapter.MyViewHolder> {

    private List<Holiday_Entry_Modal> Onduty_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open, NoofHours;
        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.textviewname);
            textviewdate = view.findViewById(R.id.textviewdate);
            open = view.findViewById(R.id.open);
        }
    }


    public Holiday_Approval_Adapter(List<Holiday_Entry_Modal> Onduty_Approval_ModelsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Onduty_Approval_ModelsList = Onduty_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.mAdapterOnClick = mAdapterOnClick;
        this.context = context;
    }

    @Override
    public Holiday_Approval_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(dummy);
            }
        });*/
        return new Holiday_Approval_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Holiday_Approval_Adapter.MyViewHolder holder, int position) {
        Holiday_Entry_Modal Holiday_Entry_Modal = Onduty_Approval_ModelsList.get(position);
        holder.textviewname.setText(Holiday_Entry_Modal.getFieldForceName());
        holder.textviewdate.setText("" + Holiday_Entry_Modal.getLoginDate());
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Onduty_Approval_ModelsList.size();
    }
}