package com.saneforce.milksales.SFA_Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.MapDirectionActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyTeamMapAdapter extends RecyclerView.Adapter<MyTeamMapAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private JSONArray array;
    Context context;
    String laty, lngy,mType;
    JSONObject json;
    AdapterOnClick mAdapterOnClick;
    public static String TAG = "MyTeamMapAdapter";
    Common_Class common_class;


    public MyTeamMapAdapter(Activity context, JSONArray array, String laty, String lngy,String mType, AdapterOnClick mAdapterOnClick) {

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.array = array;
        this.laty = laty;
        this.lngy = lngy;
        this.mType=mType;
        this.mAdapterOnClick = mAdapterOnClick;
        common_class=new Common_Class(context);

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.team_loc_detail_layout, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            json = array.getJSONObject(position);
            holder.tvSfName.setText((mType.equalsIgnoreCase("ALL") ?
                    json.getString("Sf_Name") + " (" + json.getString("shortname") + ")" : json.getString("Sf_Name")) + " - " + json.getString("sf_emp_id"));
            //holder.txEMPId.setText(json.getString("sf_emp_id"));
            holder.txEMPId.setVisibility(View.GONE);
            holder.tvDesig.setText(json.getString("Designation_Name"));
            holder.tvMobile.setText(json.getString("SF_Mobile"));
            holder.txDtTm.setText(json.getString("dttm"));
            holder.txHQ.setText(""+json.getString("HQ_Name"));
            holder.tvMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterOnClick.CallMobile(holder.tvMobile.getText().toString().replaceAll(",", ""));
                }
            });

            holder.llMobile.setVisibility(View.VISIBLE);
            if(Common_Class.isNullOrEmpty(json.getString("SF_Mobile")))
                holder.llMobile.setVisibility(View.GONE);


        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.llDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    json = array.getJSONObject(position);
                    Intent intent = new Intent(context, MapDirectionActivity.class);
                    intent.putExtra(Constants.DEST_LAT, json.getString("Lat"));
                    intent.putExtra(Constants.DEST_LNG, json.getString("Lon"));
                    intent.putExtra(Constants.DEST_NAME, json.getString("HQ_Name"));
                    context.startActivity(intent);


                } catch (Exception e) {
                    Log.v(TAG, e.getMessage());
                }

            }
        });

        holder.llMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.showCalDialog(context, "Do you want to Call this number?", holder.tvMobile.getText().toString().replaceAll(",", ""));
            }
        });


    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvSfName, txEMPId, tvDesig, tvMobile, txDtTm, txHQ;
        LinearLayout llDir, llMobile;


        public ViewHolder(View itemView) {
            super(itemView);

            tvSfName = (TextView) itemView.findViewById(R.id.tvSfName);
            txEMPId = (TextView) itemView.findViewById(R.id.tvEMPId);
            tvDesig = (TextView) itemView.findViewById(R.id.tvDesig);
            tvMobile = (TextView) itemView.findViewById(R.id.txMobile);
            txDtTm = (TextView) itemView.findViewById(R.id.txDtTm);
            txHQ = (TextView) itemView.findViewById(R.id.tvHQ);
            llMobile = (LinearLayout) itemView.findViewById(R.id.btnCallMob);
            llDir = (LinearLayout) itemView.findViewById(R.id.llDirection);
        }
    }

}
