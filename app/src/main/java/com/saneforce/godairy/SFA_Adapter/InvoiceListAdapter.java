package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Activity.ViewInvoiceActivity;
import com.saneforce.godairy.R;

import org.json.JSONArray;

public class InvoiceListAdapter extends RecyclerView.Adapter<InvoiceListAdapter.ViewHolder> {
    Context context;
    JSONArray array;

    public InvoiceListAdapter(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public InvoiceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvoiceListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_invoice_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceListAdapter.ViewHolder holder, int position) {
        holder.invoiceNumber.setText(array.optJSONObject(holder.getBindingAdapterPosition()).optString("VBELN"));
        holder.view.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewInvoiceActivity.class);
            intent.putExtra("OrderNo", array.optJSONObject(holder.getBindingAdapterPosition()).optString("VBELV"));
            intent.putExtra("InvNo", array.optJSONObject(holder.getBindingAdapterPosition()).optString("VBELN"));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView invoiceNumber;
        TextView view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            invoiceNumber = itemView.findViewById(R.id.invoiceNumber);
            view = itemView.findViewById(R.id.view);
        }
    }
}
