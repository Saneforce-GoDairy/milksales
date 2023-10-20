package com.milksales.godairy.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CancelledOrderHistoryAdapter extends RecyclerView.Adapter<CancelledOrderHistoryAdapter.ViewHolder> {
    JSONArray array;
    Context context;
    Common_Class common_class;

    public CancelledOrderHistoryAdapter(JSONArray array, Context context) {
        this.array = array;
        this.context = context;
        common_class = new Common_Class(context);
    }

    @NonNull
    @Override
    public CancelledOrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CancelledOrderHistoryAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cancelled_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CancelledOrderHistoryAdapter.ViewHolder holder, int position) {
        try {
            JSONObject object = array.getJSONObject(position);
            holder.orderId.setText(object.optString("orderId"));
            holder.Order_Value.setText("Order value: " + common_class.formatCurrency(object.optDouble("Order_Value")));
            holder.Order_Date.setText("Ordered on:\n" + object.getJSONObject("Order_Date").optString("date"));
            holder.modifiedOn.setText("Cancelled on:\n" + object.optString("modifiedOn"));
            holder.remarks.setText("Reason: " + object.optString("remarks"));
        } catch (JSONException ignored) {

        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, Order_Value, Order_Date, modifiedOn, remarks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            Order_Value = itemView.findViewById(R.id.Order_Value);
            Order_Date = itemView.findViewById(R.id.Order_Date);
            modifiedOn = itemView.findViewById(R.id.modifiedOn);
            remarks = itemView.findViewById(R.id.remarks);
        }
    }
}
