package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.OutletReport_View_Modal;

import java.util.List;

public class HistorySalesInfoAdapter extends RecyclerView.Adapter<HistorySalesInfoAdapter.MyViewHolder> {
    Context context;
    List<OutletReport_View_Modal> mDate;
    AdapterOnClick mAdapterOnClick;
    private View listItem;
    int rowlayout;

    int tab;

    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;

    public HistorySalesInfoAdapter(Context context, List<OutletReport_View_Modal> mDate, int rowlayout, int tab, AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.rowlayout = rowlayout;
        this.tab = tab;
        this.mAdapterOnClick = mAdapterOnClick;
        common_class = new Common_Class(context);
        shared_common_pref = new Shared_Common_Pref(context);


    }


    @NonNull
    @Override
    public HistorySalesInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        listItem = layoutInflater.inflate(rowlayout, null, false);
        return new HistorySalesInfoAdapter.MyViewHolder(listItem);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(HistorySalesInfoAdapter.MyViewHolder holder, int position) {
        try {
            holder.tvDate.setText("" + mDate.get(position).getOrderDate());
            holder.tvOutletName.setText("" + mDate.get(position).getOutletCode());
        } catch (Exception e) {
            Log.e("History_Adapter:", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvOutletName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOutletName = itemView.findViewById(R.id.retailername);
          tvDate = itemView.findViewById(R.id.tvDate);

        }
    }
}