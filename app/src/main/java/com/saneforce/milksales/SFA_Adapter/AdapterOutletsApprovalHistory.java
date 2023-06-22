package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.ModelOutletsApprovalHistory;

import java.util.ArrayList;

public class AdapterOutletsApprovalHistory extends RecyclerView.Adapter<AdapterOutletsApprovalHistory.ViewHolder> {
    ArrayList<ModelOutletsApprovalHistory> list;
    Context context;

    public AdapterOutletsApprovalHistory(ArrayList<ModelOutletsApprovalHistory> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterOutletsApprovalHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterOutletsApprovalHistory.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approval_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOutletsApprovalHistory.ViewHolder holder, int position) {
        ModelOutletsApprovalHistory model = list.get(position);
        holder.Name.setText(model.getName());
        holder.Code.setText(model.getCode());
        holder.Mobile.setText(model.getMobile());
        holder.Address.setText(model.getAddress());

        holder.Mobile.setOnClickListener(v -> {
            Common_Class common_class = new Common_Class(context);
            common_class.makeCall(model.getMobile());
        });

        if (model.getStatus().equals("0")) {
            holder.Approve.setVisibility(View.VISIBLE);
            holder.Reject.setVisibility(View.GONE);
            holder.Remarks.setVisibility(View.GONE);
            holder.ApprovedBy.setText("Approved By: " + model.getApprovedBy());
            holder.ModifiedOn.setText("Approved On: " + model.getModifiedOn());
        } else if (model.getStatus().equals("1")) {
            holder.Approve.setVisibility(View.GONE);
            holder.Reject.setVisibility(View.VISIBLE);
            holder.ApprovedBy.setText("Rejected By: " + model.getApprovedBy());
            holder.ModifiedOn.setText("Rejected On: " + model.getModifiedOn());
            holder.Remarks.setText("Remarks: " + model.getRemarks());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Approve, Reject, Name, Code, Mobile, Address, ApprovedBy, ModifiedOn, Remarks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Approve = itemView.findViewById(R.id.approved);
            Reject = itemView.findViewById(R.id.rejected);
            Name = itemView.findViewById(R.id.customerName_outletInfo);
            Code = itemView.findViewById(R.id.customerId_outletInfo);
            Mobile = itemView.findViewById(R.id.mobile_outletInfo);
            Address = itemView.findViewById(R.id.address_outletInfo);
            ApprovedBy = itemView.findViewById(R.id.approvedBy);
            ModifiedOn = itemView.findViewById(R.id.approvedOn);
            Remarks = itemView.findViewById(R.id.remarks);
        }
    }
}
