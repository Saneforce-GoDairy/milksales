package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RyclShortageListItemAdb extends RecyclerView.Adapter<RyclShortageListItemAdb.MyViewHolder> {

    Context context;
    JSONArray mArr;

    public RyclShortageListItemAdb(Context context, JSONArray arr) {
        this.context = context;
        this.mArr = arr;
    }

    @NonNull
    @Override
    public RyclShortageListItemAdb.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.replc_detail_adb, parent, false);
        return new RyclShortageListItemAdb.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RyclShortageListItemAdb.MyViewHolder holder, int position) {
        try {
            JSONObject item=mArr.getJSONObject(position);
            holder.txPName.setText(item.getString("Product_Detail_Name"));
            holder.txQty.setText(String.valueOf(item.getInt("Qty")));
            holder.rcylPrdDRplc.setAdapter(new RyclShortageDetAdb(context,item.getJSONArray("ReplcItems")));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mArr.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txPName,txQty;
        RecyclerView rcylPrdDRplc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txPName=itemView.findViewById(R.id.txPName);
            txQty=itemView.findViewById(R.id.txQty);
            rcylPrdDRplc=itemView.findViewById(R.id.rcylPrdDRplc);

        }
    }
}
