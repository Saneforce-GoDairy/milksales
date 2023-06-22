package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.PaymentActivity;

import java.util.ArrayList;
import java.util.List;

public class PayModeAdapter extends RecyclerView.Adapter<PayModeAdapter.MyViewHolder> {
    List<Common_Model> payList = new ArrayList<>();
    private Context context;
    int salRowDetailLayout;
    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    public PayModeAdapter(List<Common_Model> mPayList, int rowLayout, Context mContext) {
        payList = mPayList;
        context = mContext;
        salRowDetailLayout = rowLayout;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(salRowDetailLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {

            holder.tvPayMode.setText(payList.get(position).getName());

            holder.cbPayMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CheckBox cb = (CheckBox) buttonView;
                    int clickedPos = position;


                    if (cb.isChecked()) {
                        if (lastChecked != null) {
                            lastChecked.setChecked(false);
                            payList.get(lastCheckedPos).setSelected(false);
                        }

                        lastChecked = cb;
                        lastCheckedPos = clickedPos;

                        payList.get(clickedPos).setSelected(cb.isSelected());
                        PaymentActivity.paymentActivity.payModeLabel = payList.get(position).getName();
                    } else {
                        lastChecked = null;
                        PaymentActivity.paymentActivity.payModeLabel = "cash";

                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return payList.size();
    }

    public boolean isModeSelected() {
        if (lastChecked != null && lastChecked.isChecked())
            return true;
        return false;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbPayMode;
        TextView tvPayMode;

        public MyViewHolder(View view) {
            super(view);
            cbPayMode = view.findViewById(R.id.cbPayMode);
            tvPayMode = view.findViewById(R.id.tvPayMode);

        }
    }
}
