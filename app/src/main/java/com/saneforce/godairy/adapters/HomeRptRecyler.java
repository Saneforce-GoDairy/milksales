package com.saneforce.godairy.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Activity_Hap.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.MapDirectionActivity;
import com.saneforce.godairy.Status_Activity.View_All_Status_Activity;

public class HomeRptRecyler extends RecyclerView.Adapter<HomeRptRecyler.ViewHolder> {
    private static final String TAG = "Home Dashboard reports";
    private JsonArray mArrayList = new JsonArray();
    private Context mContext;
    private String latLong = "";

    public HomeRptRecyler(JsonArray arrayList, Context mContext, String latLong) {
        this.mArrayList = arrayList;
        this.mContext = mContext;
        this.latLong = latLong;
    }

    public HomeRptRecyler(JsonArray arrayList, Context mContext) {
        this.mArrayList = arrayList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public HomeRptRecyler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homerptrecyler, parent, false);
        HomeRptRecyler.ViewHolder holder = new HomeRptRecyler.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.e("adaptor__", "Working");
        try {
            Log.v("VALUE_HOME_REPORT", mArrayList.toString());
            Log.v("Lat_Long_Vi", latLong);
            JsonObject itm = mArrayList.get(position).getAsJsonObject();
            holder.txtLable.setText(itm.get("name").getAsString());
            holder.txtValue.setText("" + Html.fromHtml(itm.get("value").getAsString()).toString().trim());
            Log.d(TAG, "onBindViewHolder: ColorCd " + itm.get("color").getAsString());
            if (!itm.get("color").getAsString().equalsIgnoreCase(""))
                holder.txtValue.setTextColor(Color.parseColor(itm.get("color").getAsString()));
            holder.txtValue.setMovementMethod(LinkMovementMethod.getInstance());
            if (itm.get("Link") != null) {
                if (itm.get("Link").getAsBoolean() == true) {
                    holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, View_All_Status_Activity.class);
                            intent.putExtra("Priod", itm.get("Priod").getAsString());
                            intent.putExtra("Status", holder.txtLable.getText());
                            intent.putExtra("name", "View " + itm.get("name").getAsString() + " Status");
                            mContext.startActivity(intent);
                        }
                    });
                }
            }
            try {
                if (itm.get("type").getAsString().equalsIgnoreCase("geo")) {
                    holder.mapImage.setVisibility(View.VISIBLE);
                    holder.mapImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            try {
                                if (itm.get("name").getAsString().equalsIgnoreCase("Geo In") || itm.get("name").getAsString().equalsIgnoreCase("Geo Out") || itm.get("name").getAsString().equalsIgnoreCase("Ext.Geo In") || itm.get("name").getAsString().equalsIgnoreCase("Ext.Geo Out")) {
                                    navigateMapDir(itm.get("value").getAsString(), itm.get("name").getAsString());

//                            Intent intent = new Intent(mContext, Webview_Activity.class);
//                            intent.putExtra("Locations", itm.get("value").getAsString());
//                            mContext.startActivity(intent);

                                }else
                                {
                                    navigateMapDir(latLong,"ON Duty - Locations");
                                  /*  Intent intent = new Intent(mContext, MapDirectionActivity.class);
                                    intent.putExtra(latLong,"ON Duty - Locations");
                                    mContext.startActivity(intent);*/
                                }
                            } catch (Exception e) {

                            }
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=" + latLong));
//                            mContext.startActivity(browserIntent);
//                            Log.v("Lat_Long", latLong);

//

//                            Intent intent = new Intent(mContext, MapDirectionActivity.class);
//                            intent.putExtra("Locations", latLong);
//                            mContext.startActivity(intent);

                        }
                    });
                }
            } catch (Exception e) {

            }

            holder.txtValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (itm.get("name").getAsString().equalsIgnoreCase("Geo In") ||
                                itm.get("name").getAsString().equalsIgnoreCase("Geo Out")) {
                            navigateMapDir(itm.get("value").getAsString(), itm.get("name").getAsString());

//                            Intent intent = new Intent(mContext, Webview_Activity.class);
//                            intent.putExtra("Locations", itm.get("value").getAsString());
//                            mContext.startActivity(intent);

                        }else if (itm.get("type").getAsString().equalsIgnoreCase("geo")) {
                            navigateMapDir(latLong,"ON Duty - Locations");
                            /*Intent intent = new Intent(mContext, MapDirectionActivity.class);
                            intent.putExtra(latLong,"ON Duty - Locations" );
                            mContext.startActivity(intent);*/
                        }
                    } catch (Exception e) {

                    }
                }
            });


        } catch (Exception e) {

        }

    }


    void navigateMapDir(String value, String tag) {
        if (!Common_Class.isNullOrEmpty(value)) {
            String[] latlongs = value.split(",");
            Intent intent = new Intent(mContext, MapDirectionActivity.class);
            intent.putExtra(Constants.DEST_LAT, latlongs[0]);
            intent.putExtra(Constants.DEST_LNG, latlongs[1]);

            intent.putExtra(Constants.DEST_NAME, tag);
            intent.putExtra(Constants.NEW_OUTLET, "GEO");
            mContext.startActivity(intent);

        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtLable, txtValue;
        LinearLayout parentLayout;
        ImageView mapImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLable = itemView.findViewById(R.id.txtlable);
            txtValue = itemView.findViewById(R.id.txtval);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            mapImage = itemView.findViewById(R.id.image_map);
        }
    }
}