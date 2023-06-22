package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Activity_Hap.Cumulative_Order_Model;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;

import java.util.List;

public class CumulativeInfoAdapter extends RecyclerView.Adapter<CumulativeInfoAdapter.MyViewHolder> {
    Context context;
    List<Cumulative_Order_Model> mDate;
    AdapterOnClick mAdapterOnClick;

    public CumulativeInfoAdapter(Context context, List<Cumulative_Order_Model> mDate, AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @NonNull
    @Override
    public CumulativeInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.cumulative_info_recyclerview1, null, false);
        return new CumulativeInfoAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(CumulativeInfoAdapter.MyViewHolder holder, int position) {
        holder.desc.setText("" + mDate.get(position).getDesc());
        holder.existing.setText("" + mDate.get(position).getExisting());
        holder.newCustomer.setText("" + mDate.get(position).getNewCustomer());
        holder.totalMilk.setText("" + mDate.get(position).getTotalLtrs());
        holder.newOrderMilk.setText("" + mDate.get(position).getNewOrderLtrs());
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView desc, existing, newCustomer, totalMilk, newOrderMilk;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.tvDesc);
            existing = itemView.findViewById(R.id.tvExisting);
            newCustomer = itemView.findViewById(R.id.tvNew);
            totalMilk = itemView.findViewById(R.id.tvTotalLtrs);
            newOrderMilk = itemView.findViewById(R.id.tvNewOrderLtrs);


        }
    }
}