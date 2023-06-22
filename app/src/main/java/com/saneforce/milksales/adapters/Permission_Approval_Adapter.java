package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Model_Class.Permission_Approval_Model;
import com.saneforce.milksales.R;

import java.util.List;

public class Permission_Approval_Adapter extends RecyclerView.Adapter<Permission_Approval_Adapter.MyViewHolder> {

    private List<Permission_Approval_Model> Permission_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    Integer dummy =0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open, NoofHours;

        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.textviewname);
            textviewdate = view.findViewById(R.id.textviewdate);
            open = view.findViewById(R.id.open);
            NoofHours = view.findViewById(R.id.noofhours);
        }
    }


    public Permission_Approval_Adapter(List<Permission_Approval_Model> Permission_Approval_ModelsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Permission_Approval_ModelsList = Permission_Approval_ModelsList;
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Permission_Approval_Model Permission_Approval_Model = Permission_Approval_ModelsList.get(position);
        holder.textviewname.setText(Permission_Approval_Model.getFieldForceName());
        holder.NoofHours.setText("" + Permission_Approval_Model.getNoofHours());
        holder.textviewdate.setText(Permission_Approval_Model.getApplieddate());
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAdapterOnClick.onIntentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Permission_Approval_ModelsList.size();
    }
}