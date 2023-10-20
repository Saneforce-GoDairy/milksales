package com.milksales.godairy.SFA_Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.Activity_Hap.ApproveOutletsDetailedActivity;
import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Model_Class.ModelApproveOutlets;

import java.util.ArrayList;

// Created by RAGU on 27/01/2023
public class Approve_Outlets_Adapter extends RecyclerView.Adapter<Approve_Outlets_Adapter.ViewHolder> {

    ArrayList<ModelApproveOutlets> list;
    Context context;

    OnApproveClicked approveClicked;
    OnRejectClicked rejectClicked;

    public Approve_Outlets_Adapter(ArrayList<ModelApproveOutlets> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setApproveClicked(OnApproveClicked approveClicked) {
        this.approveClicked = approveClicked;
    }

    public void setRejectClicked(OnRejectClicked rejectClicked) {
        this.rejectClicked = rejectClicked;
    }

    @NonNull
    @Override
    public Approve_Outlets_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Approve_Outlets_Adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.outlet_info_recyclerview_two, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Approve_Outlets_Adapter.ViewHolder holder, int position) {
        ModelApproveOutlets model = list.get(position);
        holder.name.setText(model.getCustomerName());
        holder.id.setText(model.getCustomerID());
        holder.mobile.setText(model.getCustomerMobile());
        holder.address.setText(model.getCustomerAddress());

        holder.mobile.setOnClickListener(v -> {
            Common_Class common_class = new Common_Class(context);
            common_class.makeCall(model.getCustomerMobile());
        });

        holder.view.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApproveOutletsDetailedActivity.class);
            Shared_Common_Pref.Outlet_Info_Flag = "1";
            Shared_Common_Pref.Editoutletflag = "1";
            Shared_Common_Pref.Outler_AddFlag = "0";
            Shared_Common_Pref.FromActivity = "Outlets";
            Shared_Common_Pref.OutletCode = String.valueOf(model.getListedDrCode());
            intent.putExtra("OutletCode", String.valueOf(model.getListedDrCode()));
            intent.putExtra("ListedDrCode", String.valueOf(model.getListedDrCode()));
            intent.putExtra("OutletName", model.getCustomerName());
            intent.putExtra("OutletAddress", model.getCustomerAddress());
            intent.putExtra("OutletMobile", model.getCustomerMobile());
            context.startActivity(intent);
        });

        holder.approve.setOnClickListener(v -> {
            if (approveClicked != null) {
                approveClicked.OnClick(model, "", "0", holder.getBindingAdapterPosition()); // 0 means approve, 1 means reject, 2 means pending
            }
        });

        holder.reject.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_get_reason, null);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            EditText editText = view.findViewById(R.id.inputMessage);
            TextView cancel = view.findViewById(R.id.cancel);
            TextView submit = view.findViewById(R.id.submit);
            cancel.setOnClickListener(v1 -> {
                dialog.dismiss();
            });
            submit.setOnClickListener(v2 -> {
                String message = editText.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(context, "Reason for Rejection Required", Toast.LENGTH_SHORT).show();
                } else {
                    if (rejectClicked != null) {
                        rejectClicked.OnClick(model, message, "1", holder.getBindingAdapterPosition()); // 0 means approve, 1 means reject, 2 means pending
                        dialog.dismiss();
                    }
                }
            });
            builder.setCancelable(true);
            dialog.show();
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnApproveClicked {
        void OnClick(ModelApproveOutlets model, String message, String flagType, int position);
    }

    public interface OnRejectClicked {
        void OnClick(ModelApproveOutlets model, String message, String flagType, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, id, mobile, address, view, approve, reject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.customerName_outletInfo);
            id = itemView.findViewById(R.id.customerId_outletInfo);
            mobile = itemView.findViewById(R.id.mobile_outletInfo);
            address = itemView.findViewById(R.id.address_outletInfo);
            view = itemView.findViewById(R.id.viewBtn);
            approve = itemView.findViewById(R.id.approveBtn);
            reject = itemView.findViewById(R.id.rejectBtn);
        }
    }
}