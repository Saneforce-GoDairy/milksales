package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.ProjectionCategorySelectActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProjectionApprovListAdapter extends RecyclerView.Adapter<ProjectionApprovListAdapter.MyViewHolder> {
    Context context;
    JSONArray mArr;

    private View listItem;
    int rowlayout;


    public ProjectionApprovListAdapter(Context context, JSONArray mArr, int rowlayout) {
        this.context = context;
        this.mArr = mArr;
        this.rowlayout = rowlayout;


    }


    @NonNull
    @Override
    public ProjectionApprovListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        listItem = layoutInflater.inflate(rowlayout, null, false);
        return new ProjectionApprovListAdapter.MyViewHolder(listItem);
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
    public void onBindViewHolder(ProjectionApprovListAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject obj = mArr.getJSONObject(position);
            holder.tvDate.setText("" + obj.getString("date"));
            holder.tvName.setText("" + obj.getString("name"));
            holder.tvQty.setText("" + obj.getString("qty"));

            holder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProjectionCategorySelectActivity.class);
                    Shared_Common_Pref.Projection_Approval=1;
                    intent.putExtra("approval", "status");
//                    if (!Shared_Common_Pref.OutletCode.equalsIgnoreCase("OutletCode")) {
//                        Shared_Common_Pref.Editoutletflag = "1";
//                        //  Shared_Common_Pref.OutletCode = String.valueOf(Retailer_Modal_ListFilter.get(position).getId());
//                        intent.putExtra("OutletCode", Shared_Common_Pref.OutletCode);
//                        intent.putExtra("OutletName", Shared_Common_Pref.OutletName);
//                        intent.putExtra("OutletAddress", Shared_Common_Pref.OutletAddress);
//                        intent.putExtra("OutletMobile", "9876543210");
//                        intent.putExtra("OutletRoute", Shared_Common_Pref.Route_name);
//                    }

                    context.startActivity(intent);
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
        TextView tvDate, tvName, tvQty, tvView;
        Button btnView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvView = itemView.findViewById(R.id.tvView);
            btnView = itemView.findViewById(R.id.btn_View);

        }
    }
}