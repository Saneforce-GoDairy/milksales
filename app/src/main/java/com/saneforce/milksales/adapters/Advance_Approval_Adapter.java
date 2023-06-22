package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Advance_Approval_Adapter extends RecyclerView.Adapter<Advance_Approval_Adapter.MyViewHolder> {

    private JSONArray Leave_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open, AdvAmt;

        public MyViewHolder(View view) {
            super(view);
            textviewname = view.findViewById(R.id.textviewname);
            textviewdate = view.findViewById(R.id.textviewdate);
            open = view.findViewById(R.id.open);
            AdvAmt = view.findViewById(R.id.AdvAmt);
        }
    }


    public Advance_Approval_Adapter(JSONArray Leave_Approval_ModelsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.Leave_Approval_ModelsList = Leave_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.mAdapterOnClick = mAdapterOnClick;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        JSONObject Leave_Approval_Model = null;
        try {
            Leave_Approval_Model = Leave_Approval_ModelsList.getJSONObject(position);
            holder.textviewname.setText(Leave_Approval_Model.getString("SF_Name"));
            holder.AdvAmt.setText("" + Leave_Approval_Model.getDouble("AdvAmt"));
            holder.textviewdate.setText(Leave_Approval_Model.getString("eDate"));
            holder.open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapterOnClick.onIntentClick(position);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Leave_Approval_ModelsList.length();
    }
}