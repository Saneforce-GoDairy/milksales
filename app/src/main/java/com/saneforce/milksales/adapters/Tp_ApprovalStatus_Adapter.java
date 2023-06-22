package com.saneforce.milksales.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Model_Class.Tp_Approval_FF_Modal;
import com.saneforce.milksales.R;

import java.util.List;

public class Tp_ApprovalStatus_Adapter extends RecyclerView.Adapter<Tp_ApprovalStatus_Adapter.MyViewHolder> {

    private List<Tp_Approval_FF_Modal> Tp_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    String name="";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView month, date, worktype, remarks, approve, reject;
        public LinearLayout open;

        public MyViewHolder(View view) {
            super(view);
           // month = (TextView) view.findViewById(R.id.tpApprovalMonth);
            date = (TextView) view.findViewById(R.id.sdate);
//            remarks = (TextView) view.findViewById(R.id.tpApprovalRemarks);
//            worktype = (TextView) view.findViewById(R.id.tpApprovalWorkType);
//            approve =(TextView) view.findViewById(R.id.approve);
//            reject =(TextView) view.findViewById(R.id.reject);
        }
    }


    public Tp_ApprovalStatus_Adapter(List<Tp_Approval_FF_Modal> Tp_Approval_ModelsList, int rowLayout, Context context, AdapterOnClick adapterOnClick) {
        this.Tp_Approval_ModelsList = Tp_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tp_Approval_FF_Modal Tp_Approval_Model = Tp_Approval_ModelsList.get(position);

//        Intent intent=new Intent();
//        String date=intent.getStringExtra("date");
//        String month=intent.getStringExtra("month");
//        String remarks=intent.getStringExtra("remarks");
//        String workType=intent.getStringExtra("Work_Type");

//        holder.month.setText(month);
//        holder.date.setText(date);
//        holder.remarks.setText(remarks);
//        holder.worktype.setText(workType);

//        holder.month.setText(Tp_Approval_Model.getMonthnameexample());
        holder.date.setText(Tp_Approval_Model.getDate());
        Log.v("datecfvg",holder.date.toString());
//        holder.remarks.setText(Tp_Approval_Model.getRemarks());
//        holder.worktype.setText(Tp_Approval_Model.getWorktypeName());
    }

    @Override
    public int getItemCount() {
        return Tp_Approval_ModelsList.size();
    }
}