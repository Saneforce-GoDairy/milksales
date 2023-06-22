package com.saneforce.milksales.Status_Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.MapDirectionActivity;
import com.saneforce.milksales.Status_Model_Class.View_All_Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAll_Status_Adapter extends RecyclerView.Adapter<ViewAll_Status_Adapter.MyViewHolder> {

    private List<View_All_Model> View_Status_ModelsList;
    private int rowLayout;
    private Context context;
    Shared_Common_Pref shared_common_pref;
    String AMod;
    Common_Class common_class;

    public ViewAll_Status_Adapter(List<View_All_Model> View_Status_ModelsList, int rowLayout, Context context, String AMod) {
        this.View_Status_ModelsList = View_Status_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.AMod = AMod;
        shared_common_pref = new Shared_Common_Pref(context);
        common_class=new Common_Class(context);
    }

    @Override
    public ViewAll_Status_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewAll_Status_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewAll_Status_Adapter.MyViewHolder holder, int position) {
        View_All_Model View_Status_Model = View_Status_ModelsList.get(position);
        GradientDrawable drawable = (GradientDrawable) holder.wkstatus.getBackground();
        holder.weekofdate.setText(View_Status_ModelsList.get(position).getWrkDate());
        holder.wkstatus.setText(View_Status_ModelsList.get(position).getDayStatus());
        holder.shifttime.setText(View_Status_ModelsList.get(position).getSFTName().toString());
        holder.txt_in_time.setText(View_Status_ModelsList.get(position).getAttTm().toString());
        holder.txt_out_time.setText(View_Status_ModelsList.get(position).getET().toString());

        holder.txt_in_geo.setText(View_Status_ModelsList.get(position).getGeoin().toString());
        holder.txt_out_geo.setText(View_Status_ModelsList.get(position).getGeoout().toString());
        holder.txt_Loc.setText(View_Status_ModelsList.get(position).getLoc().toString());

        String color = View_Status_Model.getStusClr().replace("!important", "");
        drawable.setColor(Color.parseColor(color.trim()));
        holder.llOnDuty.setVisibility(View.GONE);
        try {
            if (View_Status_Model.getDayStatus().equalsIgnoreCase("On-Duty") && View_Status_Model.getFlag() != 2) {
                holder.llOnDuty.setVisibility(View.VISIBLE);
                if (View_Status_Model.getFlag() == 1)
                    holder.cbOnDuty.setChecked(true);
                else
                    holder.cbOnDuty.setChecked(false);
            }
        } catch (Exception e) {

        }

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendStatusUpdate(holder.cbOnDuty.isChecked() ? 1 : 0, View_Status_ModelsList.get(position));
            }
        });

        holder.txt_in_geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateMapDir(View_Status_ModelsList.get(position).getGeoin().toString(),"In Geo");
//                Intent intent = new Intent(context, Webview_Activity.class);
//                intent.putExtra("Locations", View_Status_Model.getGeoin());
//                context.startActivity(intent);

            }
        });

        holder.txt_out_geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateMapDir(View_Status_ModelsList.get(position).getGeoout().toString(),"Out Geo");
//                Intent intent = new Intent(context, Webview_Activity.class);
//                intent.putExtra("Locations", View_Status_Model.getGeoout());
//                context.startActivity(intent);

            }
        });


    }

    void navigateMapDir(String value,String tag) {
        try {
            if (!com.saneforce.milksales.Activity_Hap.Common_Class.isNullOrEmpty(value)) {
                String[] latlongs = value.split(",");
                Intent intent = new Intent(context, MapDirectionActivity.class);
                intent.putExtra(Constants.DEST_LAT, latlongs[0]);
                intent.putExtra(Constants.DEST_LNG, latlongs[1]);
                intent.putExtra(Constants.DEST_NAME, tag);
                intent.putExtra(Constants.NEW_OUTLET, "GEO");
                context.startActivity(intent);

            }
        } catch (Exception e) {
            Log.v("ViewAllStatus:", e.getMessage());
        }
    }

    private void sendStatusUpdate(int flag, View_All_Model data) {
        JSONObject taReq = new JSONObject();

        try {
            taReq.put("login_sfCode", shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            taReq.put("Flag", flag);
            taReq.put("current_date", Common_Class.GetDate());
            taReq.put("shift_date", data.getAttndt());


            Log.v("TA_REQ", taReq.toString());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> mCall = apiInterface.viewStatusUpdate(taReq.toString());

            mCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    // locationList=response.body();
                    try {
                        Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        Toast.makeText(context.getApplicationContext(), jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return View_Status_ModelsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView weekofdate, wkstatus, shifttime, txt_in_time, txt_out_time, txt_in_geo, txt_out_geo, txt_Loc;
        LinearLayout llOnDuty;
        Button btnUpdate;
        CheckBox cbOnDuty;

        public MyViewHolder(View view) {
            super(view);
            weekofdate = view.findViewById(R.id.weekofdate);
            wkstatus = view.findViewById(R.id.wkstatus);
            shifttime = view.findViewById(R.id.shifttime);
            txt_in_time = view.findViewById(R.id.txt_in_time);
            txt_out_time = view.findViewById(R.id.txt_out_time);
            txt_in_geo = view.findViewById(R.id.txt_in_geo);
            txt_out_geo = view.findViewById(R.id.txt_out_geo);
            txt_Loc = view.findViewById(R.id.txt_loc);
            llOnDuty = view.findViewById(R.id.llOnDuty);
            btnUpdate = view.findViewById(R.id.btnUpdate);
            cbOnDuty = view.findViewById(R.id.cbOnDuty);

        }
    }

}