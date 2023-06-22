package com.saneforce.milksales.Status_Adapter;

import static com.saneforce.milksales.SFA_Activity.HAPApp.CurrencySymbol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.ComplementaryInvoiceModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ComplementaryInvoiceAdapter extends RecyclerView.Adapter<ComplementaryInvoiceAdapter.ViewHolder> {
    Context context;
    ArrayList<ComplementaryInvoiceModel> list;

    ItemChecked itemChecked;

    public void setItemChecked(ItemChecked itemChecked) {
        this.itemChecked = itemChecked;
    }

    public ComplementaryInvoiceAdapter(Context context, ArrayList<ComplementaryInvoiceModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ComplementaryInvoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComplementaryInvoiceAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.complementary_invoice_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComplementaryInvoiceAdapter.ViewHolder holder, int position) {
        ComplementaryInvoiceModel model = list.get(position);
        holder.invoice.setText(model.getInvoice());
        holder.date.setText(model.getDate());
        String amount = "Total: " + CurrencySymbol + " " + new DecimalFormat("0.00").format(Double.parseDouble(model.getValue()));
        holder.value.setText(amount);
        boolean statuss = model.getStatus();
        holder.checkBox.setChecked(statuss);
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (itemChecked != null) {
                itemChecked.onItemChecked(isChecked, position, model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView invoice, date, value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkBox_complementary);
            invoice = itemView.findViewById(R.id.invoice);
            date = itemView.findViewById(R.id.date);
            value = itemView.findViewById(R.id.value);

        }
    }

    public interface ItemChecked {
        void onItemChecked (boolean status, int position, ComplementaryInvoiceModel model);
    }
}
