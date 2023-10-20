package com.milksales.godairy.SFA_Adapter;

import static com.milksales.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.AdapterOnClick;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Model_Class.OutletReport_View_Modal;
import com.milksales.godairy.SFA_Model_Class.Product_Details_Modal;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class HistoryInfoAdapter extends RecyclerView.Adapter<HistoryInfoAdapter.MyViewHolder> {
    Context context;
    List<OutletReport_View_Modal> mDate;
    AdapterOnClick mAdapterOnClick;
    private View listItem;
    int rowlayout;
    NumberFormat formatter = new DecimalFormat("##0.00");

    int tab;

    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;

    public HistoryInfoAdapter(Context context, List<OutletReport_View_Modal> mDate, int rowlayout, int tab, AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.rowlayout = rowlayout;
        this.tab = tab;
        this.mAdapterOnClick = mAdapterOnClick;
        common_class = new Common_Class(context);
        shared_common_pref = new Shared_Common_Pref(context);


    }


    @NonNull
    @Override
    public HistoryInfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        listItem = layoutInflater.inflate(rowlayout, null, false);
        return new HistoryInfoAdapter.MyViewHolder(listItem);
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
    public void onBindViewHolder(HistoryInfoAdapter.MyViewHolder holder, int position) {
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

            StringBuilder value = new StringBuilder();
            //  holder.tvName.setText("" + mDate.get(position).getNo_Of_items());
            if (mDate.get(position).getProduct_details_modal() != null && mDate.get(position).getProduct_details_modal().size() > 0) {
                for (int i = 0; i < mDate.get(position).getProduct_details_modal().size(); i++) {
                    Product_Details_Modal pm = mDate.get(position).getProduct_details_modal().get(i);
                    if((i+1)==mDate.get(position).getProduct_details_modal().size())
                        value.append(pm.getName() + " x " + pm.getQty() );
                    else
                    value.append(pm.getName() + " x " + pm.getQty() + ", ");

                }

                holder.tvName.setText("" + value);
            } else
                holder.tvName.setText("");


//            holder.btnReOrder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Gson gson = new Gson();
//
//                    shared_common_pref.save(Constants.PreOrderQtyList, gson.toJson(mDate.get(position).getProduct_details_modal()));
//
//
//                    if (tab == 1)
//                        context.startActivity(new Intent(context, Order_Category_Select.class));
//                    else
//                        context.startActivity(new Intent(context, Invoice_Category_Select.class));
//
//                }
//            });

        } catch (Exception e) {
            Log.e("History_Adapter:", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId, tvDate, tvStatus, tvAmount, tvAddress, tvOutletName, tvInvDate, tvInvStatus, tvInvId, tvInvAmt, tvInvProducts;
        ImageView ivStatus;
        Button btnReOrder;

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
            btnReOrder = itemView.findViewById(R.id.btnReOrder);


        }
    }
}