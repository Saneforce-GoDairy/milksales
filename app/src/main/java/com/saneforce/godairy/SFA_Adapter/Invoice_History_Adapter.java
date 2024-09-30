package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.Order_Category_Select;
import com.saneforce.godairy.SFA_Activity.Print_Invoice_Activity;
import com.saneforce.godairy.SFA_Model_Class.OutletReport_View_Modal;

import java.text.DecimalFormat;
import java.util.List;

public class Invoice_History_Adapter extends RecyclerView.Adapter<Invoice_History_Adapter.MyViewHolder> {

    Context context;
    List<OutletReport_View_Modal> mDate;
    AdapterOnClick mAdapterOnClick;
    Shared_Common_Pref sharedCommonPref;

    public Invoice_History_Adapter(Context context, List<OutletReport_View_Modal> mDate, AdapterOnClick mAdapterOnClick) {
        this.context = context;
        this.mDate = mDate;
        this.mAdapterOnClick = mAdapterOnClick;
        sharedCommonPref = new Shared_Common_Pref(context);
    }

    @NonNull
    @Override
    public Invoice_History_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.invoice_history_recyclerview, null, false);

        return new Invoice_History_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(Invoice_History_Adapter.MyViewHolder holder, int position) {
        //   holder.llReturnInv.setVisibility(View.GONE);

        holder.editOrder.setVisibility(View.GONE);
        if (mDate.get(position).getInvoice_Flag().equals("1")) {
            //  holder.llReturnInv.setVisibility(View.VISIBLE);
            holder.Statusinvoice.setText("Invoice Complete.");
            holder.Statusinvoice.setTextColor(context.getResources().getColor(R.color.green));
            holder.ivStatus.setImageResource(R.drawable.ic_round_done_outline_24);

            // holder.parent_layout.setBackgroundResource(R.color.white);
        } else {
            holder.Statusinvoice.setText("Order received.Pending for Invoice.");
            holder.editOrder.setVisibility(View.VISIBLE);
            holder.ivStatus.setImageResource(R.drawable.ic_baseline_fiber_manual_record_24);
            holder.Statusinvoice.setTextColor(context.getResources().getColor(R.color.checkout));

            //  holder.parent_layout.setBackgroundResource(R.color.greeninvoicecolor);
        }
        holder.txtOrderDate.setText("" + mDate.get(position).getOrderDate());
        holder.txtOrderID.setText(mDate.get(position).getTransSlNo());
        holder.txtValue.setText("" + new DecimalFormat("##0.00").format(mDate.get(position).getOrderValue()));
        holder.Itemcountinvoice.setText("" + mDate.get(position).getNo_Of_items());
        holder.txtType.setText("" + mDate.get(position).getStatus());

        if (mDate.get(position).getStatus().equalsIgnoreCase("SALES RETURN") ||
                mDate.get(position).getStatus().equalsIgnoreCase("STOCK ROTATION") ||
                mDate.get(position).getStatus().equalsIgnoreCase("INDENT")) {
            holder.Statusinvoice.setText(mDate.get(position).getStatus() + " Completed.");
            holder.Statusinvoice.setTextColor(context.getResources().getColor(R.color.txt_template_color));
            holder.ivStatus.setImageResource(R.drawable.ic_round_done_outline_24);
            holder.txtType.setTextColor(context.getResources().getColor(R.color.txt_template_color));

        }

        holder.editOrder.setOnClickListener(v -> {
            Intent intent = new Intent(context, Order_Category_Select.class);
            intent.putExtra("mode", "edit");
            intent.putExtra("orderId", mDate.get(position).getTransSlNo());
            context.startActivity(intent);
        });

        if (mDate.get(position).getStatus().equalsIgnoreCase("Completed")) {
            holder.Statusinvoice.setText("GRN Complete.");
            holder.Statusinvoice.setTextColor(context.getResources().getColor(R.color.green));
            holder.ivStatus.setImageResource(R.drawable.ic_round_done_outline_24);

        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(position);
            }
        });

        holder.llReturnInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TRANS_SLNO", mDate.get(position).getTransSlNo());
                Shared_Common_Pref.TransSlNo = mDate.get(position).getTransSlNo();
                Shared_Common_Pref.Invoicetoorder = "1";
                Intent intent = new Intent(context, Print_Invoice_Activity.class);
                sharedCommonPref.save(Constants.FLAG, "Return Invoice");
                intent.putExtra("Order_Values", mDate.get(position).getOrderValue() + "");
                intent.putExtra("Invoice_Values", mDate.get(position).getInvoicevalues());
                intent.putExtra("No_Of_Items", mDate.get(position).getNo_Of_items());
                intent.putExtra("Invoice_Date", mDate.get(position).getOrderDate());
                intent.putExtra("NetAmount", mDate.get(position).getNetAmount());
                intent.putExtra("Discount_Amount", mDate.get(position).getDiscount_Amount());
                context.startActivity(intent);
            }
        });


        if (Common_Class.isNullOrEmpty(mDate.get(position).getIndent())) {

            holder.llIndent.setVisibility(View.GONE);
        } else {
            holder.tvIndent.setText("" + mDate.get(position).getIndent());
        }

        holder.tvQty.setText(""+mDate.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Statusinvoice, txtOrderDate, txtOrderID, txtValue, Itemcountinvoice, txtType, tvIndent, tvQty;
        LinearLayout linearLayout, llReturnInv, llIndent;
        RelativeLayout parent_layout;
        ImageView ivStatus, editOrder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderID = itemView.findViewById(R.id.txt_order);
            Statusinvoice = itemView.findViewById(R.id.Statusinvoice);
            txtOrderDate = itemView.findViewById(R.id.txt_date);
            txtValue = itemView.findViewById(R.id.txt_total);
            linearLayout = itemView.findViewById(R.id.row_report);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            Itemcountinvoice = itemView.findViewById(R.id.Itemcountinvoice);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            txtType = itemView.findViewById(R.id.txt_type);
            llReturnInv = itemView.findViewById(R.id.llSalesReturn);
            llIndent = itemView.findViewById(R.id.llIndent);
            tvIndent = itemView.findViewById(R.id.txt_indent);
            tvQty = itemView.findViewById(R.id.tvQty);
            editOrder = itemView.findViewById(R.id.editOrder);
        }
    }
}
