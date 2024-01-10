package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class AdapterPaymentHistory extends RecyclerView.Adapter<AdapterPaymentHistory.ViewHolder> {
    Context context;
    JSONArray array;

    public AdapterPaymentHistory(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public AdapterPaymentHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterPaymentHistory.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_payment_status, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPaymentHistory.ViewHolder holder, int position) {
        JSONObject object = array.optJSONObject(holder.getBindingAdapterPosition());
        if (object != null) {
            holder.createdOn.setText(object.optString("createdOn"));
            holder.invoiceNumber.setText(object.optString("invoiceNumber"));
            holder.invoiceAmount.setText(new DecimalFormat("0.00").format(object.optDouble("invoiceAmount")));
            holder.paymentMethod.setText(object.optString("paymentMethod"));
            holder.paymentStatus.setText(object.optString("paymentStatus"));
            holder.transactionId.setText(object.optString("transactionId"));
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView createdOn, invoiceNumber, invoiceAmount, paymentMethod, paymentStatus, transactionId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            createdOn = itemView.findViewById(R.id.createdOn);
            invoiceNumber = itemView.findViewById(R.id.invoiceNumber);
            invoiceAmount = itemView.findViewById(R.id.invoiceAmount);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            paymentStatus = itemView.findViewById(R.id.paymentStatus);
            transactionId = itemView.findViewById(R.id.transactionId);
        }
    }
}
