package com.saneforce.milksales.SFA_Adapter;

import static com.saneforce.milksales.SFA_Activity.HAPApp.CurrencySymbol;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.OutletReport_View_Modal;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class HistoryOrderInvInfoAdapter extends RecyclerView.Adapter<HistoryOrderInvInfoAdapter.MyViewHolder> {
    Context context;
    List<OutletReport_View_Modal> mDate;
    private View listItem;
    int rowlayout;
    NumberFormat formatter = new DecimalFormat("##0.00");

    int tab;

    public HistoryOrderInvInfoAdapter(Context context, List<OutletReport_View_Modal> mDate, int rowlayout, int tab) {
        this.context = context;
        this.mDate = mDate;
        this.rowlayout = rowlayout;
        this.tab = tab;

    }

    @NonNull
    @Override
    public HistoryOrderInvInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        listItem = layoutInflater.inflate(rowlayout, null, false);
        return new HistoryOrderInvInfoAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(HistoryOrderInvInfoAdapter.MyViewHolder holder, int position) {
        try {
            holder.tvId.setText("" + mDate.get(position).getTransSlNo());
            holder.tvDate.setText("" + mDate.get(position).getOrderDate());
            holder.tvOutletName.setText("" + mDate.get(position).getOutletCode());
            holder.tvStatus.setText(mDate.get(position).getStatus());


            if (mDate.get(position).getStatus().equals("Completed")) {

                holder.ivStatus.setImageResource(R.drawable.ic_round_done_outline_24);

            } else {
                holder.ivStatus.setImageResource(R.drawable.ic_round_pending_24);
            }
            holder.tvAmount.setText(CurrencySymbol+" " + formatter.format(mDate.get(position).getOrderValue()));

            holder.tvName.setText("" + mDate.get(position).getName());


            holder.tvInvDate.setText("" + mDate.get(position).getInvoiceDate());
            holder.tvInvStatus.setText("" + mDate.get(position).getInvoiceStatus());
            holder.tvInvId.setText("" + mDate.get(position).getInvoiceID());

            if (!Common_Class.isNullOrEmpty(mDate.get(position).getInvoiceAmount()))
                holder.tvInvAmt.setText(CurrencySymbol+" " + formatter.format(Double.parseDouble(mDate.get(position).getInvoiceAmount())));
            holder.tvInvProducts.setText("" + mDate.get(position).getInvoiceItems());

            if (mDate.get(position).getInvoiceStatus().equals("Completed")) {

                holder.ivInvStatus.setImageResource(R.drawable.ic_round_done_outline_24);

                holder.rlInvDateParent.setVisibility(View.VISIBLE);
            } else {

                holder.tvInvStatus.setText("Pending");
                holder.rlInvDateParent.setVisibility(View.GONE);

                holder.ivInvStatus.setImageResource(R.drawable.ic_round_pending_24);
            }


        } catch (Exception e) {
            Log.e("OrderInvAdapter: ", e.getMessage());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId, tvDate, tvStatus, tvAmount, tvAddress, tvOutletName, tvInvDate, tvInvStatus, tvInvId, tvInvAmt, tvInvProducts;
        ImageView ivStatus, ivInvStatus;
        RelativeLayout rlInvDateParent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOutletName = itemView.findViewById(R.id.retailername);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvId = itemView.findViewById(R.id.tvOrderId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            tvDate = itemView.findViewById(R.id.tvDate);


            tvInvDate = itemView.findViewById(R.id.tvDateInv);
            tvInvStatus = itemView.findViewById(R.id.tvStatusInv);
            tvInvId = itemView.findViewById(R.id.tvInvoiceId);
            tvInvAmt = itemView.findViewById(R.id.tvAmountInv);
            tvInvProducts = itemView.findViewById(R.id.tvProductNameInv);
            ivInvStatus = itemView.findViewById(R.id.ivStatusInv);

            rlInvDateParent = itemView.findViewById(R.id.rlInvDateParent);


        }
    }
}