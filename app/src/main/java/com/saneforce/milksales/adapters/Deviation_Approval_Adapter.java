package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Model_Class.Deviation_Entr_Modal;
import com.saneforce.milksales.R;

import java.util.List;

public class Deviation_Approval_Adapter extends RecyclerView.Adapter<Deviation_Approval_Adapter.MyViewHolder> {

    private List<Deviation_Entr_Modal> Deviation_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open, NoofHours;
        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.textviewname);
            textviewdate = view.findViewById(R.id.textviewdate);
            open = view.findViewById(R.id.open);

        }
    }


    public Deviation_Approval_Adapter(List<Deviation_Entr_Modal> Deviation_Approval_ModelsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Deviation_Approval_ModelsList = Deviation_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.mAdapterOnClick = mAdapterOnClick;
        this.context = context;
    }

    @Override
    public Deviation_Approval_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(dummy);
            }
        });*/
        return new Deviation_Approval_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Deviation_Approval_Adapter.MyViewHolder holder, int position) {
        Deviation_Entr_Modal Deviation_Entr_Modal = Deviation_Approval_ModelsList.get(position);
        holder.textviewname.setText(Deviation_Entr_Modal.getFieldForceName());
        holder.textviewdate.setText("" + Deviation_Entr_Modal.getApplieddate());
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Deviation_Approval_ModelsList.size();
    }
}