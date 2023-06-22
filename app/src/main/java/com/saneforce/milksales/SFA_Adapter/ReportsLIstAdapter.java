package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReportsLIstAdapter extends RecyclerView.Adapter<ReportsLIstAdapter.MyViewHolder> {
    Context context;
    JSONArray mArr;
    private View listItem;
    int rowlayout;
    AdapterOnClick adapterOnClick;

    public ReportsLIstAdapter(Context context, JSONArray mArr, int rowlayout, AdapterOnClick adapterOnClick) {
        this.context = context;
        this.mArr = mArr;
        this.rowlayout = rowlayout;
        this.adapterOnClick = adapterOnClick;
    }


    @NonNull
    @Override
    public ReportsLIstAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        listItem = layoutInflater.inflate(rowlayout, null, false);
        return new ReportsLIstAdapter.MyViewHolder(listItem);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(ReportsLIstAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject obj = mArr.getJSONObject(position);
            holder.tvName.setText("" + obj.getString("name"));

            holder.layparent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        adapterOnClick.onIntentClick(mArr.getJSONObject(position), holder.getBindingAdapterPosition());
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {
            Log.e("OutletApprovalAdapter:", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mArr.length();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RelativeLayout layparent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            layparent = itemView.findViewById(R.id.parent_layout);
        }
    }
}