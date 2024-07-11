package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Model_Class.PaymentModel;
import com.saneforce.godairy.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder>{
    Context context;
    List<PaymentModel> modelList;
    double rec_amt=0;
    int collectPaymentId=0;
    NumberFormat formatter = new DecimalFormat("##0.00");

    public void setCollectPaymentId(int collectPaymentId) {this.collectPaymentId = collectPaymentId;}

    public void setPaymentList(List<PaymentModel>paymentModelList){
        modelList=paymentModelList;
        notifyDataSetChanged();
    }


    public PaymentAdapter(Context context, List<PaymentModel> paymentModelList){
        this.context=context;
        modelList=paymentModelList;

    }
    public double getRec_amt() {return rec_amt;}

    public void setRec_amt(double rec_amt) {this.rec_amt = rec_amt;}

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_payment,parent,false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PaymentModel paymentModel=modelList.get(position);

        if(collectPaymentId==1){
            holder.tv_amt_label.setVisibility(View.GONE);
            holder.et_amt.setVisibility(View.GONE);
            holder.tv_paid_amt_label.setVisibility(View.VISIBLE);
            holder.tv_paid_amt.setVisibility(View.VISIBLE);
            holder.tv_paid_amt.setText(String.valueOf(formatter.format(paymentModel.getAmt())));
        }else{
            holder.tv_amt_label.setVisibility(View.VISIBLE);
            holder.et_amt.setVisibility(View.VISIBLE);
            holder.tv_paid_amt_label.setVisibility(View.GONE);
            holder.tv_paid_amt.setVisibility(View.GONE);
        }

        holder.tv_bill_no.setText(paymentModel.getBillNo());
        holder.tv_bill_date.setText(paymentModel.getBillDate());
        holder.tv_billed_amt.setText(String.valueOf(paymentModel.getBilledAmt()));
        holder.tv_pending_amt.setText(String.valueOf(formatter.format(paymentModel.getPendingAmt())));
        holder.et_amt.setText(String.valueOf(formatter.format(paymentModel.getAmt())));

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_bill_no,tv_bill_date,tv_billed_amt,tv_pending_amt,tv_amt_label,tv_paid_amt,tv_paid_amt_label;
        EditText et_amt;
        LinearLayout layout;


        public MyViewHolder(View itemView) {
            super(itemView);


            tv_bill_no=itemView.findViewById(R.id.tv_bill_no);
            tv_bill_date=itemView.findViewById(R.id.tv_bill_date);
            tv_billed_amt=itemView.findViewById(R.id.tv_billed_Amt);
            tv_pending_amt=itemView.findViewById(R.id.tv_pending_Amt);
            et_amt=itemView.findViewById(R.id.et_amt);
            tv_amt_label=itemView.findViewById(R.id.tv_amt_label);
            tv_paid_amt=itemView.findViewById(R.id.tv_paid_Amt);
            tv_paid_amt_label=itemView.findViewById(R.id.tv_paid_amt_label);
        }
    }

    /*public void setRecAmt(Double recAmt){
        double rcamt=recAmt;
        printUsrLog("")
        for (int i=0;i<modelList.size();i++){
            if(modelList.get(i).getPendingAmt()<rcamt){
                double amt=rcamt-modelList.get(i).getPendingAmt();
                modelList.get(i).setAmt(modelList.get(i).getPendingAmt());
                rcamt=amt;
            }else{
                modelList.get(i).setAmt(recAmt);
            }
        }
    }*/
}
