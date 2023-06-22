package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Model_Class.Missed_Punch_Model;
import com.saneforce.milksales.R;

import java.util.List;

public class Missed_Punch_Adapter extends RecyclerView.Adapter<Missed_Punch_Adapter.MyViewHolder> {

    private List<Missed_Punch_Model> Missed_Punch_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open;

        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.textviewname);
            textviewdate = view.findViewById(R.id.textviewdate);
            open = view.findViewById(R.id.open);

        }
    }


    public Missed_Punch_Adapter(List<Missed_Punch_Model> Missed_Punch_ModelsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Missed_Punch_ModelsList = Missed_Punch_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;
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
        Missed_Punch_Model Missed_Punch_Model = Missed_Punch_ModelsList.get(position);
        holder.textviewname.setText(Missed_Punch_Model.getSfName());

        holder.textviewdate.setText(Missed_Punch_Model.getAppliedDate());
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapterOnClick.onIntentClick(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return Missed_Punch_ModelsList.size();
    }
}