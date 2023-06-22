package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.ViewReport;
import com.saneforce.milksales.Model_Class.DateResult;
import com.saneforce.milksales.R;

import java.util.List;


public class DateReportAdapter extends RecyclerView.Adapter<DateReportAdapter.MyViewHolder> {

    Context context;
    List<DateResult> mDate;
    ViewReport mViewReport;
    String produtId, productDate;

    public DateReportAdapter(Context context, List<DateResult> mDate) {
        this.context = context;
        this.mDate = mDate;
    }

    @NonNull
    @Override
    public DateReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_date_view_report, null, false);
        return new DateReportAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(DateReportAdapter.MyViewHolder holder, int position) {
        holder.txtName.setText(" " + mDate.get(position).getProductName());
        holder.txtQty.setText(" " + mDate.get(position).getCQty());
        holder.txtRate.setText(" " + mDate.get(position).getRate());
        holder.txtTotal.setText(" " + mDate.get(position).getValue());

    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtsNo;
        TextView txtName;
        TextView txtQty;
        TextView txtRate;
        TextView txtTotal;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtQty = (TextView) itemView.findViewById(R.id.txt_qty);
            txtRate = (TextView) itemView.findViewById(R.id.txt_rate);
            txtTotal = (TextView) itemView.findViewById(R.id.txt_total);


        }
    }
}
