package com.saneforce.milksales.SFA_Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.AdapterOnClick;

import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.Dashboard_View_Model;


import java.util.List;

public class Dashboard_View_Adapter extends RecyclerView.Adapter<Dashboard_View_Adapter.MyViewHolder> {

    private List<Dashboard_View_Model> Dashboard_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewcount;

        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.textviewname);
            textviewcount = view.findViewById(R.id.textviewcount);

        }
    }


    public Dashboard_View_Adapter(List<Dashboard_View_Model> Dashboard_ModelsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Dashboard_ModelsList = Dashboard_ModelsList;
        this.rowLayout = rowLayout;
        this.mAdapterOnClick = mAdapterOnClick;
        this.context = context;
    }

    @Override
    public Dashboard_View_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new Dashboard_View_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Dashboard_View_Adapter.MyViewHolder holder, int position) {
        Dashboard_View_Model Dashboard_View_Model = Dashboard_ModelsList.get(position);
        holder.textviewname.setText(Dashboard_View_Model.getName());
        holder.textviewcount.setText("" + Dashboard_View_Model.getCountAll());
      
    }

    @Override
    public int getItemCount() {
        return Dashboard_ModelsList.size();
    }
}