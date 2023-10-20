package com.milksales.godairy.SFA_Adapter;

import static com.milksales.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Model_Class.SalesReturnInvoiceModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SalesReturnInvoiceAdapter extends RecyclerView.Adapter<SalesReturnInvoiceAdapter.ViewHolder> {
    Context context;
    ArrayList<SalesReturnInvoiceModel> list;

    ItemSelect itemSelect;

    public void setItemSelect(ItemSelect itemSelect) {
        this.itemSelect = itemSelect;
    }

    public SalesReturnInvoiceAdapter(Context context, ArrayList<SalesReturnInvoiceModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SalesReturnInvoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesReturnInvoiceAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.sales_return_invoice_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesReturnInvoiceAdapter.ViewHolder holder, int position) {
        SalesReturnInvoiceModel model = list.get(position);

        holder.invoice.setText(model.getInvoice());
        holder.date.setText(model.getDate());
        String amount = "Total: " + CurrencySymbol + " " + new DecimalFormat("0.00").format(Double.parseDouble(model.getValue()));
        holder.value.setText(amount);

        holder.itemView.setOnClickListener(v -> {
            if (itemSelect != null) {
                itemSelect.onItemSelected(model.getInvoice());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView invoice, date, value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            invoice = itemView.findViewById(R.id.invoice);
            date = itemView.findViewById(R.id.date);
            value = itemView.findViewById(R.id.value);
        }
    }

    public interface ItemSelect {
        void onItemSelected (String invoiceNumber);
    }
}
