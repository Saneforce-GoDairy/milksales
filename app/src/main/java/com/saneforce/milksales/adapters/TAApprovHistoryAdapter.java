package com.saneforce.milksales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;


public class TAApprovHistoryAdapter extends RecyclerView.Adapter<TAApprovHistoryAdapter.MyViewHolder> {
    Context context;
    JsonArray taJsonArray;
    AdapterOnClick adapterOnClick;


    public TAApprovHistoryAdapter(Context context, JsonArray taJsonArray, AdapterOnClick adapterOnClick) {
        this.context = context;
        this.taJsonArray = taJsonArray;
        this.adapterOnClick = adapterOnClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View viewStatus = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_t_a_history, parent, false);
        return new TAApprovHistoryAdapter.MyViewHolder(viewStatus);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {

            JsonObject jsonObject = (JsonObject) taJsonArray.get(position);
            Log.v("TaAmount", jsonObject.get("Total_Amount").getAsString());

            holder.taDate.setText(jsonObject.get("EDT").getAsString());
            holder.taStatus.setText(jsonObject.get("ApSTatus").getAsString());
            holder.taTotalAmt.setText(jsonObject.get("Total_Amount").getAsString());
            holder.taDaAmt.setText(jsonObject.get("Boarding_Amt").getAsString());
            holder.taTLAmt.setText(jsonObject.get("Ta_totalAmt").getAsString());
            holder.taFaAmt.setText(jsonObject.get("trv_lc_amt").getAsString());
            holder.taLaAmt.setText(jsonObject.get("Ldg_totalAmt").getAsString());
            holder.taLcAmt.setText(jsonObject.get("Lc_totalAmt").getAsString());
            holder.taOeAmt.setText(jsonObject.get("Oe_totalAmt").getAsString());

            holder.taStatus.setPadding(20, 5, 20, 5);
            holder.taName.setText(""+jsonObject.get("SFName").getAsString());

            if (jsonObject.get("ApSTatus").getAsString().equalsIgnoreCase("Rejected")) {
                holder.taStatus.setBackgroundResource(R.drawable.button_red);
                holder.taName.setTextColor(Color.parseColor("#ff3700"));

            } else if (jsonObject.get("ApSTatus").getAsString().equalsIgnoreCase("Approved")) {
                holder.taStatus.setBackgroundResource(R.drawable.button_green);
                holder.taName.setTextColor(Color.parseColor("#008000"));

            } else {
                holder.taStatus.setBackgroundResource(R.drawable.button_yellows);
                holder.taName.setTextColor(Color.parseColor("#ff9819"));

            }

        } catch (Exception e) {

        }
    }


    @Override
    public int getItemCount() {
        return taJsonArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView taDate, taStatus, taTotalAmt, taDaAmt, taTLAmt, taFaAmt, taLaAmt, taLcAmt, taOeAmt,taName;
        CardView mCardView;
        Button btnCancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taDate = (TextView) itemView.findViewById(R.id.ta_date);
            taStatus = (TextView) itemView.findViewById(R.id.ta_status);
            taTotalAmt = (TextView) itemView.findViewById(R.id.ta_amount);
            taDaAmt = (TextView) itemView.findViewById(R.id.txt_da);
            taTLAmt = (TextView) itemView.findViewById(R.id.txt_tvrl_amt);
            taFaAmt = (TextView) itemView.findViewById(R.id.txt_tl);
            taLaAmt = (TextView) itemView.findViewById(R.id.txt_la);
            taLcAmt = (TextView) itemView.findViewById(R.id.txt_lc);
            taOeAmt = (TextView) itemView.findViewById(R.id.txt_oe);
            mCardView = itemView.findViewById(R.id.ta_row_item);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
            taName=itemView.findViewById(R.id.SfName);
        }
    }


}
