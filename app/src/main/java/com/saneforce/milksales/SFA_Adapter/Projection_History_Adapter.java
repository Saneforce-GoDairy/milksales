package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class Projection_History_Adapter extends RecyclerView.Adapter<Projection_History_Adapter.MyViewHolder> {

    Context context;
    JSONArray mDate;
    AdapterOnClick mAdapterOnClick;
    String mResponse;

    public Projection_History_Adapter(Context context, JSONArray mDate, String mResponse, AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.mAdapterOnClick = mAdapterOnClick;
        this.mResponse = mResponse;
    }

    @NonNull
    @Override
    public Projection_History_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.posorder_history_recyclerview, null, false);

        return new Projection_History_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(Projection_History_Adapter.MyViewHolder holder, int position) {
        try {
            JSONObject obj = mDate.getJSONObject(position);
            holder.txtOrderDate.setText("" + obj.getString("Order_Date"));
            holder.txtOrderID.setText(obj.getString("OrderNo"));
            holder.txtValue.setText("" + obj.getInt("TotalQty"));
            holder.Itemcountinvoice.setText(obj.getString("Status"));

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterOnClick.onIntentClick(position);
                }
            });


        } catch (Exception e) {
            Log.v("primAdapter:", e.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return mDate.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderDate, txtOrderID, txtValue, Itemcountinvoice;
        LinearLayout linearLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderID = itemView.findViewById(R.id.txt_order);
            txtOrderDate = itemView.findViewById(R.id.txt_date);
            txtValue = itemView.findViewById(R.id.txt_total);
            linearLayout = itemView.findViewById(R.id.row_report);
            Itemcountinvoice = itemView.findViewById(R.id.Itemcountinvoice);


        }
    }
}
