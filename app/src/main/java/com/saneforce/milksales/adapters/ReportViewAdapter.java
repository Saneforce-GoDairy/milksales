package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.ViewReport;
import com.saneforce.milksales.Model_Class.ReportModel;
import com.saneforce.milksales.R;

import java.util.List;


public class ReportViewAdapter extends RecyclerView.Adapter<ReportViewAdapter.MyViewHolder> {

    Context context;
    List<ReportModel> mDate;
    ViewReport mViewReport;
    String produtId, productDate;

    public ReportViewAdapter(Context context, List<ReportModel> mDate, ViewReport mViewReport) {
        this.context = context;
        this.mDate = mDate;
        this.mViewReport = mViewReport;
    }

    @NonNull
    @Override
    public ReportViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_report_list, null, false);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewReport.reportCliick(produtId, productDate);
            }
        });
        return new ReportViewAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ReportViewAdapter.MyViewHolder holder, int position) {

        holder.txtsNo.setText(mDate.get(position).getSlno());
        holder.txtOrderDate.setText(mDate.get(position).getOrderDate());
        holder.txtOrderID.setText(mDate.get(position).getOrderNo());
        holder.txtValue.setText(""+mDate.get(position).getOrderValue());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewReport.reportCliick(mDate.get(position).getOrderNo(), mDate.get(position).getOrderDate());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtsNo;
        TextView txtOrderDate;
        TextView txtOrderID;
        TextView txtValue;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtsNo = (TextView) itemView.findViewById(R.id.txt_serial);
            txtOrderID = (TextView) itemView.findViewById(R.id.txt_order);
            txtOrderDate = (TextView) itemView.findViewById(R.id.txt_date);
            txtValue = (TextView) itemView.findViewById(R.id.txt_total);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.row_report);
        }
    }
}
