package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.MapDirectionActivity;
import com.saneforce.godairy.SFA_Activity.Nearby_Outlets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RetailerNearByADP extends RecyclerView.Adapter<RetailerNearByADP.MyViewHolder> {
    JsonArray jLists;
    int RowLayout;
    Context context;
    AdapterOnClick mAdapterOnClick;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;

    public RetailerNearByADP(JsonArray jList, int rowLayout, Context mcontext, AdapterOnClick adapterOnClick) {

        jLists = jList;
        RowLayout = rowLayout;
        context = mcontext;
        mAdapterOnClick = adapterOnClick;
        shared_common_pref = new Shared_Common_Pref(context);
        common_class = new Common_Class(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(RowLayout, parent, false);
        return new RetailerNearByADP.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            JsonObject jItem = jLists.get(position).getAsJsonObject();
            String OutletId = jItem.get("Code").getAsString();
            holder.txRetailName.setText(jItem.get("Name").getAsString().toUpperCase());
            holder.txRetailCode.setText(OutletId);
            holder.txOwnerNm.setText(jItem.get("Owner_Name").getAsString().toUpperCase());
            holder.txMobile.setText(jItem.get("Mobile").getAsString().toUpperCase());
            holder.txAdd.setText(jItem.get("Add1").getAsString().toUpperCase());
            holder.txDistName.setText(jItem.get("Distributor").getAsString() + " - " + jItem.get("DistCode").getAsString());
            holder.txChannel.setText(jItem.get("Channel").getAsString());
            holder.txDistance.setText(jItem.get("Distance").getAsString());
            String InvFlag = jItem.get("InvFlag").getAsString();
            holder.txRetNo.setText(String.valueOf(position + 1));
            holder.icMob.setVisibility(View.VISIBLE);
            if (jItem.get("Mobile").getAsString().equalsIgnoreCase("")) {
                holder.icMob.setVisibility(View.GONE);
            }
            if (InvFlag.equalsIgnoreCase("0")) {
                holder.parent_layout.setBackgroundResource(R.color.white);
            } else if (InvFlag.equalsIgnoreCase("1")) {
                holder.parent_layout.setBackgroundResource(R.color.invoiceordercolor);
            } else {
                holder.parent_layout.setBackgroundResource(R.color.greeninvoicecolor);
            }
            holder.icAC.setVisibility(View.GONE);
            if (jItem.get("DelivType").getAsString().equalsIgnoreCase("AC")) {
                holder.icAC.setVisibility(View.VISIBLE);
            }
            holder.icFreezer.setVisibility(View.GONE);
            if (jItem.get("freezer_required").getAsString().equalsIgnoreCase("yes")) {
                holder.icFreezer.setVisibility(View.VISIBLE);
            }


            holder.parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posi = holder.getBindingAdapterPosition();
                    mAdapterOnClick.onIntentClick(jLists.get(posi).getAsJsonObject(), posi);
                    mAdapterOnClick.onIntentClick(posi);
                }
            });


            holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JsonObject jItem = jLists.get(position).getAsJsonObject();


                        if (!shared_common_pref.getvalue(Constants.Distributor_Id).equalsIgnoreCase(jItem.get("DistCode").getAsString())) {
                            Nearby_Outlets.nearby_outlets.navigateEditRetailerScreen(jItem, false);
                        } else {
                            Nearby_Outlets.nearby_outlets.navigateEditRetailerScreen(jItem, true);
                        }


                    } catch (Exception e) {
                        Log.v("NearByADP:", e.getMessage());
                    }
                }
            });


            holder.txTodayTotQty.setText("0");
            holder.txTodayTotVal.setText("₹0.00");
            holder.txPreTotQty.setText("0");
            holder.txPreTotVal.setText("₹0.00");
            //PreSales
            Boolean DtaBnd = false;
            String jData = jItem.get("todaydata").getAsJsonArray().toString();
            JSONArray BindArry = new JSONArray(jData);
            holder.lstTdyView.setAdapter(new CatewiseSalesaAdapter(BindArry, R.layout.categorywise_sales_adp, context));
            DtaBnd = true;

            int iQty = 0;
            double iVal = 0.0;
            for (int il = 0; il < BindArry.length(); il++) {
                JSONObject itm = BindArry.getJSONObject(il);
                if (itm.length() > 0) {
                    iQty += itm.getInt("Qty");
                    iVal += itm.getDouble("Val");
                }
            }
            holder.txTodayTotQty.setText(String.valueOf(iQty));
            holder.txTodayTotVal.setText("₹" + new DecimalFormat("##0.00").format(iVal));


            getMnthlyDta(holder, jItem, holder.tvFirstMonth.getText().toString());
            holder.tvFirstMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tvFirstMonth.setTypeface(null, Typeface.BOLD);
                    holder.tvFirstMonth.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

                    holder.tvSecondMnth.setTypeface(null, Typeface.NORMAL);
                    holder.tvSecondMnth.setTextColor(context.getResources().getColor(R.color.black_80));

                    holder.tvThirdMnth.setTypeface(null, Typeface.NORMAL);
                    holder.tvThirdMnth.setTextColor(context.getResources().getColor(R.color.black_80));

                    getMnthlyDta(holder, jItem, holder.tvFirstMonth.getText().toString());
                }
            });
            holder.tvSecondMnth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tvFirstMonth.setTypeface(null, Typeface.NORMAL);
                    holder.tvFirstMonth.setTextColor(context.getResources().getColor(R.color.black_80));

                    holder.tvSecondMnth.setTypeface(null, Typeface.BOLD);
                    holder.tvSecondMnth.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

                    holder.tvThirdMnth.setTypeface(null, Typeface.NORMAL);
                    holder.tvThirdMnth.setTextColor(context.getResources().getColor(R.color.black_80));

                    getMnthlyDta(holder, jItem, holder.tvSecondMnth.getText().toString());
                }
            });
            holder.tvThirdMnth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tvFirstMonth.setTypeface(null, Typeface.NORMAL);
                    holder.tvFirstMonth.setTextColor(context.getResources().getColor(R.color.black_80));

                    holder.tvSecondMnth.setTypeface(null, Typeface.NORMAL);
                    holder.tvSecondMnth.setTextColor(context.getResources().getColor(R.color.black_80));

                    holder.tvThirdMnth.setTypeface(null, Typeface.BOLD);
                    holder.tvThirdMnth.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

                    getMnthlyDta(holder, jItem, holder.tvThirdMnth.getText().toString());
                }
            });

            holder.linDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JsonObject jItem = jLists.get(position).getAsJsonObject();
                        if (Common_Class.isNullOrEmpty(jItem.get("lat").getAsString()) || Common_Class.isNullOrEmpty(jItem.get("long").getAsString())) {
                            common_class.showMsg(Nearby_Outlets.nearby_outlets, "No route is found");
                        } else {

                            Intent intent = new Intent(context, MapDirectionActivity.class);
                            intent.putExtra(Constants.NEW_OUTLET, "new");
                            intent.putExtra(Constants.DEST_LAT, jItem.get("lat").getAsString());
                            intent.putExtra(Constants.DEST_LNG, jItem.get("long").getAsString());
                            intent.putExtra(Constants.DEST_NAME, jItem.get("Name").getAsString());
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {

                        Log.v("NearbyOutlet:Dir: ", e.getMessage());
                    }
                }
            });

            holder.llDistparent.setVisibility(View.VISIBLE);

            holder.llDistcal.setVisibility(View.GONE);
            if (!Common_Class.isNullOrEmpty(jItem.get("DistMobile").getAsString())) {
                holder.llDistcal.setVisibility(View.VISIBLE)
                ;
                holder.tvDistMobile.setText(jItem.get("DistMobile").getAsString());
            }

            holder.tvDistAdd.setText(jItem.get("DistAddress").getAsString());


            holder.llOutletCal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    common_class.showCalDialog(context, "Do you want to Call this Outlet?",
                            holder.txMobile.getText().toString().replaceAll(",", ""));

                }
            });


            holder.llDistcal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    common_class.showCalDialog(context, "Do you want to Call this Franchise?",
                            holder.tvDistMobile.getText().toString().replaceAll(",", ""));

                }
            });


        } catch (Exception e) {
            Log.v("RouteAdapter: ", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return jLists.size();
    }

    public void getMnthlyDta(RetailerNearByADP.MyViewHolder holder, JsonObject jItem, String Mnth) {

        JSONArray PrevSales = null;
        try {
            PrevSales = new JSONArray(jItem.get("monthdata").getAsJsonArray().toString());
            boolean DtaBnd = false;
            JSONArray BindArry = new JSONArray();
            for (int i = 0; i < PrevSales.length(); i++) {
                JSONObject item = PrevSales.getJSONObject(i);
                String str = item.getString("Mnth").substring(0, 3);
                if (Mnth.equals(str)) {
                    BindArry.put(item);
                }

            }
            holder.lstPreView.setAdapter(new CatewiseSalesaAdapter(BindArry, R.layout.categorywise_sales_mnth_adp, context));

            sumOfTotal(BindArry, holder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sumOfTotal(JSONArray AryDta, RetailerNearByADP.MyViewHolder holder) {
        try {
            Log.v("NEARBY_OUTLETS:", AryDta.toString());
            int iQty = 0;
            double iVal = 0.0;
            for (int il = 0; il < AryDta.length(); il++) {
                JSONObject itm = AryDta.getJSONObject(il);
                iQty += itm.getInt("Qty");
                iVal += itm.getDouble("Val");
            }
            holder.txPreTotQty.setText(String.valueOf(iQty));
            holder.txPreTotVal.setText("₹" + new DecimalFormat("##0.00").format(iVal));

//            holder.linDirection.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    String sOutletName = mRetailer_Modal_List.getName();
////
////                    drawRoute(sOutletName, mRetailer_Modal_List.getLat(), mRetailer_Modal_List.getLong());
//                }
//            });

            holder.btnView.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void drawRoute(String OutletName, String sLat, String sLng) {
        Intent intent = new Intent(context.getApplicationContext(), MapDirectionActivity.class);
        intent.putExtra(Constants.DEST_LAT, sLat);
        intent.putExtra(Constants.DEST_LNG, sLng);
        intent.putExtra(Constants.DEST_NAME, OutletName);
        intent.putExtra(Constants.NEW_OUTLET, "");
        context.startActivity(intent);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txRetailName, txRetailCode, txAdd, txOwnerNm, txMobile, txDistName, txChannel, txDistance, txTdyDt, txTodayTotQty, txTodayTotVal, txPreTotQty, txPreTotVal,
                tvFirstMonth, tvSecondMnth, tvThirdMnth, txRetNo, tvDistAdd, tvDistMobile;
        LinearLayout parent_layout, icAC, linDirection, icFreezer, llDistparent, llDistcal,llOutletCal;
        RecyclerView lstTdyView, lstPreView;
        ImageView icMob;
        ImageView ivEdit;
        Button btnView;

        public MyViewHolder(View view) {
            super(view);
            try {
                parent_layout = view.findViewById(R.id.parent_layout);

                txRetailName = view.findViewById(R.id.retailername);
                txRetailCode = view.findViewById(R.id.retailorCode);
                llOutletCal=view.findViewById(R.id.btnCallMob);
                txRetNo = view.findViewById(R.id.txRetNo);
                txAdd = view.findViewById(R.id.txAdd);
                txOwnerNm = view.findViewById(R.id.txOwnerNm);
                txMobile = view.findViewById(R.id.txMobile);
                icMob = view.findViewById(R.id.icMob);
                icFreezer = view.findViewById(R.id.icFreezer);
                txDistName = view.findViewById(R.id.txDistName);
                txChannel = view.findViewById(R.id.txChannel);
                txDistance = view.findViewById(R.id.txDistance);
                tvFirstMonth = view.findViewById(R.id.tvLMFirst);
                tvSecondMnth = view.findViewById(R.id.tvLMSecond);
                tvThirdMnth = view.findViewById(R.id.tvLMThree);
                txTdyDt = view.findViewById(R.id.tvDate);
                linDirection = view.findViewById(R.id.linDirection);
                txTodayTotQty = view.findViewById(R.id.tvTodayTotQty);
                txTodayTotVal = view.findViewById(R.id.tvTodayTotVal);
                txPreTotQty = view.findViewById(R.id.tvPreTotQty);
                txPreTotVal = view.findViewById(R.id.tvPreTotVal);

                lstTdyView = view.findViewById(R.id.rvTodayData);
                lstPreView = view.findViewById(R.id.rvPreMnData);

                lstTdyView.setLayoutManager(new LinearLayoutManager(context));
                lstPreView.setLayoutManager(new LinearLayoutManager(context));
                ivEdit = view.findViewById(R.id.btnEditRet);
                icAC = view.findViewById(R.id.icAC);
                llDistparent = view.findViewById(R.id.llDistParent);
                llDistcal = view.findViewById(R.id.llCallMob);
                tvDistAdd = view.findViewById(R.id.tvDistAdd);
                tvDistMobile = view.findViewById(R.id.tvDistPhone);
                btnView=view.findViewById(R.id.btn_View);

                // ivEdit.setVisibility(View.GONE);
                //txRetNo.setVisibility(View.GONE);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dpln = new SimpleDateFormat("yyyy-MM-dd");
                String plantime = dpln.format(c.getTime());
                txTdyDt.setText("" + plantime);
            } catch (Exception e) {
                Log.e("RouteAdapter:holder ", e.getMessage());
            }
        }
    }
}
