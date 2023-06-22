package com.saneforce.milksales.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.R;

public class GateAdapter extends RecyclerView.Adapter<GateAdapter.MyViewHolder> {
    Context context;
    JsonArray jsonArray;

    public GateAdapter(Context context, JsonArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_gtein_gteout, null, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Log.v("GATE_DATA_ADAPTER", String.valueOf(jsonArray));

        JsonObject jsonObject = (JsonObject) jsonArray.get(position);
        {
            Log.v("GATE_DATA_ADAPTER", jsonObject.get("HQLoc").getAsString());

            holder.txtplace.setText(jsonObject.get("HQLoc").getAsString());
            holder.txtdate.setText(jsonObject.get("time").getAsString());
            holder.txtIntime.setText(jsonObject.get("Itime").getAsString());
            holder.txOtTime.setText(jsonObject.get("Otime").getAsString());
            holder.txtGeoIn.setText(jsonObject.get("latLng").getAsString());
            holder.txtGeoOt.setText(jsonObject.get("OlatLng").getAsString());
        }
    }

    @Override
    public int getItemCount() {
        if(jsonArray!=null)
        return jsonArray.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtplace;
        TextView txtdate;
        TextView txtIntime;
        TextView txOtTime;
        TextView txtGeoIn;
        TextView txtGeoOt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtplace = itemView.findViewById(R.id.txt_plce);
            txtdate = itemView.findViewById(R.id.txt_dte);
            txtIntime = itemView.findViewById(R.id.txt_intme);
            txOtTime = itemView.findViewById(R.id.txt_ottme);
            txtGeoIn = itemView.findViewById(R.id.txt_ingeo);
            txtGeoOt = itemView.findViewById(R.id.txt_otgeo);
        }
    }
}
