package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Model_Class.Tp_Approval_FF_Modal;
import com.saneforce.milksales.R;

import java.util.List;

public class New_TP_Approval_Adapter extends RecyclerView.Adapter<New_TP_Approval_Adapter.MyViewHolder> {
    private List<Tp_Approval_FF_Modal> Tp_Approval_FF_ModalsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id, name, designation, hq, open;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.tpEmpId);
            name = (TextView) view.findViewById(R.id.tpName);
            designation = (TextView) view.findViewById(R.id.tpDesignation);
            hq = (TextView) view.findViewById(R.id.tpHQ);
            open = (TextView) view.findViewById(R.id.openDetails);
        }
    }


    public New_TP_Approval_Adapter(List<Tp_Approval_FF_Modal> Tp_Approval_FF_ModalsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Tp_Approval_FF_ModalsList = Tp_Approval_FF_ModalsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(dummy);
            }
        });*/
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(New_TP_Approval_Adapter.MyViewHolder holder, int position) {
        Tp_Approval_FF_Modal Tp_Approval_FF_Modal = Tp_Approval_FF_ModalsList.get(position);
        holder.name.setText(Tp_Approval_FF_Modal.getFieldForceName());
        holder.designation.setText(Tp_Approval_FF_Modal.getDesignation());
        holder.id.setText(Tp_Approval_FF_Modal.getEmpCode());
        holder.hq.setText(Tp_Approval_FF_Modal.getHQName());
//        holder.textviewdate.setText(Tp_Approval_FF_Modal.getMonthnameexample()+"-"+Tp_Approval_FF_Modal.getTyear());
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Tp_Approval_FF_ModalsList.size();
    }
}