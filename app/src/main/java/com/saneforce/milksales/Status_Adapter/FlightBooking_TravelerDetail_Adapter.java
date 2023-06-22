package com.saneforce.milksales.Status_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class FlightBooking_TravelerDetail_Adapter extends RecyclerView.Adapter<FlightBooking_TravelerDetail_Adapter.MyViewHolder> {
    private JSONArray mArr;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvId, tvName, tvCompany, tvMobile, tvtype;


        public MyViewHolder(View view) {
            super(view);
            tvId = view.findViewById(R.id.tvEMPId);
            tvName = view.findViewById(R.id.tvName);
            tvCompany = view.findViewById(R.id.tvCompany);
            tvMobile = view.findViewById(R.id.txMobile);
            tvtype = view.findViewById(R.id.tvType);

        }
    }


    public FlightBooking_TravelerDetail_Adapter(JSONArray arr, Context context) {
        this.mArr = arr;

    }

    @Override
    public FlightBooking_TravelerDetail_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.flight_travelers_listitem, null, false);
        return new FlightBooking_TravelerDetail_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(FlightBooking_TravelerDetail_Adapter.MyViewHolder holder, int position) {
        try {
            JSONObject obj = mArr.getJSONObject(position);
            holder.tvId.setText(""+obj.getString("TrvEmpID"));
            holder.tvName.setText(""+obj.getString("TrvName"));
            holder.tvCompany.setText("" + obj.getString("TrvComp"));
            holder.tvMobile.setText("" + obj.getString("TrvMob"));
            holder.tvtype.setText("" + obj.getString("TrvType"));
        } catch (Exception e) {

        }


    }

    @Override
    public int getItemCount() {
        return mArr.length();
    }
}