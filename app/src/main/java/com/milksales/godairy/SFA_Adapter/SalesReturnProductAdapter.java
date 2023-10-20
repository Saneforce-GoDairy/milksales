package com.milksales.godairy.SFA_Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Activity.HAPApp;
import com.milksales.godairy.SFA_Model_Class.SalesReturnProductModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SalesReturnProductAdapter extends RecyclerView.Adapter<SalesReturnProductAdapter.ViewHolder> {
    Context context;
    ArrayList<SalesReturnProductModel> list;
    Common_Class common_class;
    CalculateTotal calculateTotal;

    boolean isUpdating;

    public SalesReturnProductAdapter(Context context, ArrayList<SalesReturnProductModel> list) {
        this.context = context;
        this.list = list;
        this.isUpdating = false;
        this.common_class = new Common_Class(context);
    }

    public void setCalculateTotal(CalculateTotal calculateTotal) {
        this.calculateTotal = calculateTotal;
    }

    @NonNull
    @Override
    public SalesReturnProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalesReturnProductAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.sales_return_product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesReturnProductAdapter.ViewHolder holder, int pos) {
        holder.productName.setText(list.get(holder.getBindingAdapterPosition()).getProduct_Name());
        String matCode = "Mat Code: " + list.get(holder.getBindingAdapterPosition()).getSale_Erp_Code();
        holder.materialCode.setText(matCode);
        String mRp = "MRP: " + HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(list.get(holder.getBindingAdapterPosition()).getMRP());
        holder.MRP.setText(mRp);
        String raTe = "Rate: " + HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(list.get(holder.getBindingAdapterPosition()).getPrice());
        holder.rate.setText(raTe);
        holder.invUOM.setText(list.get(holder.getBindingAdapterPosition()).getUOM());
        holder.invQty.setText(String.valueOf(list.get(holder.getBindingAdapterPosition()).getInvQty()));
        holder.retUOM.setText(list.get(holder.getBindingAdapterPosition()).getUOM());
        holder.retQty.setText(String.valueOf(list.get(holder.getBindingAdapterPosition()).getRetQty()));
        String value = "Return Amount: " + HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(list.get(holder.getBindingAdapterPosition()).getRetAmount());
        holder.retAmount.setText(value);

        holder.retQtyMinus.setOnClickListener(v -> {
            int retQtyCount = list.get(holder.getBindingAdapterPosition()).getRetQty();
            if (retQtyCount > 0) {
                int newQty = retQtyCount - 1;
                holder.retQty.setText(String.valueOf(newQty));
            }
        });

        holder.retQtyPlus.setOnClickListener(v -> {
            int newQty = list.get(holder.getBindingAdapterPosition()).getRetQty() + 1;
            if (list.get(holder.getBindingAdapterPosition()).getInvQty() >= newQty) {
                holder.retQty.setText(String.valueOf(newQty));
            } else {
                Toast.makeText(context, "Return quantity can't exceed the invoice quantity", Toast.LENGTH_SHORT).show();
            }
        });

        holder.retQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isUpdating) {
                    isUpdating = true;
                    String Qty = "0";
                    if (!s.toString().equals("")) {
                        Qty = s.toString();
                    }
                    int newQty = Integer.parseInt(Qty);
                    if (newQty != list.get(holder.getBindingAdapterPosition()).getRetQty()) {
                        int invQty = list.get(holder.getBindingAdapterPosition()).getInvQty();
                        if (invQty >= newQty) {
                            double totalAmt = common_class.formatDecimalToTwoDecimal((list.get(holder.getBindingAdapterPosition()).getInvAmount() / invQty * newQty));
                            double totalTax = common_class.formatDecimalToTwoDecimal((list.get(holder.getBindingAdapterPosition()).getInvTax() / invQty * newQty));
                            list.get(holder.getBindingAdapterPosition()).setRetQty(newQty);
                            list.get(holder.getBindingAdapterPosition()).setRetAmount(totalAmt);
                            list.get(holder.getBindingAdapterPosition()).setRetTax(totalTax);
                            String currency = "Return Amount: " + common_class.formatCurrency(totalAmt);
                            holder.retAmount.setText(currency);
                            if (calculateTotal != null) {
                                calculateTotal.onClick();
                            }
                            holder.retQty.setText(String.valueOf(newQty));
                        } else {
                            holder.retQty.setText(String.valueOf(list.get(holder.getBindingAdapterPosition()).getRetQty()));
                        }
                    }
                    isUpdating = false;
                    holder.retQty.setSelection(holder.retQty.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface CalculateTotal {
        void onClick();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, materialCode, MRP, rate, invUOM, invQty, retUOM, retType, retAmount;
        EditText retQty;
        ImageView retQtyMinus, retQtyPlus;
        RelativeLayout rl_retType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.ProductName);
            materialCode = itemView.findViewById(R.id.MatCode);
            MRP = itemView.findViewById(R.id.MRP);
            rate = itemView.findViewById(R.id.Rate);
            invUOM = itemView.findViewById(R.id.UOM);
            invQty = itemView.findViewById(R.id.invQty);
            retUOM = itemView.findViewById(R.id.retUOM);
            retQty = itemView.findViewById(R.id.retQty);
            retType = itemView.findViewById(R.id.tv_retType);
            retAmount = itemView.findViewById(R.id.retAmount);
            retQtyMinus = itemView.findViewById(R.id.retQtyMns);
            retQtyPlus = itemView.findViewById(R.id.retQtyPlus);
            rl_retType = itemView.findViewById(R.id.rl_retType);
        }
    }
}
