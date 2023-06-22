package com.saneforce.milksales.SFA_Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.Print_Invoice_Activity;
import com.saneforce.milksales.SFA_Model_Class.ComplementaryInvoiceHistoryModel;

import java.util.ArrayList;

public class ComplementaryInvoiceHistoryAdapter extends RecyclerView.Adapter<ComplementaryInvoiceHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<ComplementaryInvoiceHistoryModel> list;

    public ComplementaryInvoiceHistoryAdapter(Context context, ArrayList<ComplementaryInvoiceHistoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ComplementaryInvoiceHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComplementaryInvoiceHistoryAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.posorder_history_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComplementaryInvoiceHistoryAdapter.ViewHolder holder, int position) {
        ComplementaryInvoiceHistoryModel model = list.get(position);
        holder.txtOrderID.setText(model.getInvoice());
        holder.txtOrderDate.setText(model.getDateTime());
        holder.txtValue.setText(model.getValue());
        holder.Itemcountinvoice.setText(model.getType());

        holder.linearLayout.setOnClickListener(v -> {
            Shared_Common_Pref.TransSlNo = model.getInvoice();
            Intent intent = new Intent(context, Print_Invoice_Activity.class);
            Shared_Common_Pref sharedCommonPref = new Shared_Common_Pref(context);
            sharedCommonPref.save(Constants.FLAG, "COMPLEMENTARY INVOICE");
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.in, R.anim.out);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderDate, txtOrderID, txtValue, Itemcountinvoice;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderID = itemView.findViewById(R.id.txt_order);
            txtOrderDate = itemView.findViewById(R.id.txt_date);
            txtValue = itemView.findViewById(R.id.txt_total);
            linearLayout = itemView.findViewById(R.id.row_report);
            Itemcountinvoice = itemView.findViewById(R.id.Itemcountinvoice);
        }
    }
}
