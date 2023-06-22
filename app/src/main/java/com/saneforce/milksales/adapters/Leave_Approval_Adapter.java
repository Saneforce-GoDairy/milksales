package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Model_Class.Leave_Approval_Model;
import com.saneforce.milksales.R;

import java.util.List;

public class Leave_Approval_Adapter extends RecyclerView.Adapter<Leave_Approval_Adapter.MyViewHolder> {

    private List<Leave_Approval_Model> Leave_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open, leavedays;

        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.textviewname);
            textviewdate = view.findViewById(R.id.textviewdate);
            open = view.findViewById(R.id.open);
            leavedays = view.findViewById(R.id.leavedays);
        }
    }


    public Leave_Approval_Adapter(List<Leave_Approval_Model> Leave_Approval_ModelsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Leave_Approval_ModelsList = Leave_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.mAdapterOnClick = mAdapterOnClick;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(dummy);
            }
        });*/

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Leave_Approval_Model Leave_Approval_Model = Leave_Approval_ModelsList.get(position);
        holder.textviewname.setText(Leave_Approval_Model.getFieldForceName());
        holder.leavedays.setText("" + Leave_Approval_Model.getLeaveDays());
        holder.textviewdate.setText(Leave_Approval_Model.getApplieddate());
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Leave_Approval_ModelsList.size();
    }
}