package com.saneforce.godairy.SFA_Adapter;


import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.content.Context;
import android.content.DialogInterface;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Activity_Hap.AddNewRetailer;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.SFA_Activity.MapDirectionActivity;
import com.saneforce.godairy.SFA_Model_Class.Retailer_Modal_List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Route_View_Adapter extends RecyclerView.Adapter<Route_View_Adapter.MyViewHolder> {

    private List<Retailer_Modal_List> Retailer_Modal_Listitem;

    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    JSONObject PreSales;
    Shared_Common_Pref shared_common_pref;
    com.saneforce.godairy.Activity_Hap.Common_Class DT = new com.saneforce.godairy.Activity_Hap.Common_Class();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, txTodayTotQty, txTodayTotVal, txPreTotQty, txPreTotVal,
                textviewdate, txAdd, txOwnerNm, txMobile, txDistName, txChannel, txRetNo,
                status, invoice, values, invoicedate, tvRetailorCode, tvFirstMonth, tvSecondMnth, tvThirdMnth;
        LinearLayout parent_layout, cdParent, linDistance, btnCallMob, linDirection, icAC, llDataParent, icFreezer;
        ImageView icMob, btnEditRet;
        RecyclerView lstTdyView, lstPreView;
        Button btnView;


        public MyViewHolder(View view) {
            super(view);
            try {
                btnView = view.findViewById(R.id.btn_View);
                textviewname = view.findViewById(R.id.retailername);
                parent_layout = view.findViewById(R.id.parent_layout);
                llDataParent = view.findViewById(R.id.llDataParent);
                icFreezer = view.findViewById(R.id.icFreezer);

                invoicedate = view.findViewById(R.id.invoicedate);
                tvRetailorCode = view.findViewById(R.id.retailorCode);
                txRetNo = view.findViewById(R.id.txRetNo);
                btnEditRet = view.findViewById(R.id.btnEditRet);
                tvFirstMonth = view.findViewById(R.id.tvLMFirst);
                tvSecondMnth = view.findViewById(R.id.tvLMSecond);
                tvThirdMnth = view.findViewById(R.id.tvLMThree);
                textviewdate = view.findViewById(R.id.tvDate);

                txTodayTotQty = view.findViewById(R.id.tvTodayTotQty);
                txTodayTotVal = view.findViewById(R.id.tvTodayTotVal);
                txPreTotQty = view.findViewById(R.id.tvPreTotQty);
                txPreTotVal = view.findViewById(R.id.tvPreTotVal);

                lstTdyView = view.findViewById(R.id.rvTodayData);
                lstPreView = view.findViewById(R.id.rvPreMnData);

                lstTdyView.setLayoutManager(new LinearLayoutManager(context));
                lstPreView.setLayoutManager(new LinearLayoutManager(context));
                cdParent = view.findViewById(R.id.cdParent);
                linDistance = view.findViewById(R.id.linDistance);
                linDirection = view.findViewById(R.id.linDirection);
                btnCallMob = view.findViewById(R.id.btnCallMob);
                txAdd = view.findViewById(R.id.txAdd);
                txOwnerNm = view.findViewById(R.id.txOwnerNm);
                txMobile = view.findViewById(R.id.txMobile);
                icMob = view.findViewById(R.id.icMob);

                txDistName = view.findViewById(R.id.txDistName);
                txChannel = view.findViewById(R.id.txChannel);
                icAC = view.findViewById(R.id.icAC);


                linDistance.setVisibility(View.GONE);
                txDistName.setVisibility(View.GONE);
                //txChannel.setVisibility(View.GONE);

                Calendar c = Calendar.getInstance();
                SimpleDateFormat dpln = new SimpleDateFormat("yyyy-MM-dd");
                String plantime = dpln.format(c.getTime());
                textviewdate.setText("" + plantime);


            } catch (Exception e) {
                Log.e("RouteAdapter:holder ", e.getMessage());
            }
        }


    }


    public Route_View_Adapter(List<Retailer_Modal_List> retailer_Modal_Listitem, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        try {
            this.Retailer_Modal_Listitem = retailer_Modal_Listitem;
            this.rowLayout = rowLayout;
            this.context = context;
            this.mAdapterOnClick = mAdapterOnClick;
            shared_common_pref = new Shared_Common_Pref(context);
            String todayorderliost = shared_common_pref.getvalue(Constants.RetailorTodayData);
            PreSales = new JSONObject(todayorderliost);

        } catch (Exception e) {
            Log.e("RouteAdapter:constr ", e.getMessage());
        }

    }

    public void notifyData(List<Retailer_Modal_List> retailer_Modal_Listitem) {
        this.Retailer_Modal_Listitem = retailer_Modal_Listitem;
        notifyDataSetChanged();

    }

    @Override
    public Route_View_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Route_View_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Route_View_Adapter.MyViewHolder holder, int pos) {
        try {
            Retailer_Modal_List mRetailer_Modal_List = Retailer_Modal_Listitem.get(holder.getBindingAdapterPosition());
            holder.textviewname.setText(mRetailer_Modal_List.getName().toUpperCase());
            holder.tvRetailorCode.setText(mRetailer_Modal_List.getERP_Code());
            holder.txOwnerNm.setText(mRetailer_Modal_List.getOwner_Name());
            holder.txMobile.setText(mRetailer_Modal_List.getPrimary_No());
            holder.txAdd.setText(mRetailer_Modal_List.getListedDrAddress1());
            holder.txRetNo.setText(mRetailer_Modal_List.getListedDrSlNo().toString());
            holder.txChannel.setText(mRetailer_Modal_List.getOutletClass()+" - "+mRetailer_Modal_List.getSpeciality());
            holder.icMob.setVisibility(View.VISIBLE);
            if (mRetailer_Modal_List.getPrimary_No().equalsIgnoreCase("")) {
                holder.icMob.setVisibility(View.GONE);
            }
            holder.icAC.setVisibility(View.INVISIBLE);
            if (mRetailer_Modal_List.getDelivType() != null && mRetailer_Modal_List.getDelivType().equalsIgnoreCase("AC")) {
                holder.icAC.setVisibility(View.VISIBLE);
            }


            if (!Common_Class.isNullOrEmpty(mRetailer_Modal_List.getFreezer_required()) && mRetailer_Modal_List.getFreezer_required().equalsIgnoreCase("yes")) {
                holder.icFreezer.setVisibility(View.VISIBLE);
            } else {
                holder.icFreezer.setVisibility(View.GONE);
            }

            SimpleDateFormat fdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String csDT = fdt.format(new Date());
            SimpleDateFormat f = new SimpleDateFormat("MMM");
            holder.tvFirstMonth.setText(f.format(new Date()));
            holder.tvSecondMnth.setText(f.format(DT.AddMonths(csDT, -1)));
            holder.tvThirdMnth.setText(f.format(DT.AddMonths(csDT, -2)));

            if (mRetailer_Modal_List.getInvoice_Flag().equals("0")) {
                holder.cdParent.setBackgroundResource(R.color.white);
            } else if (mRetailer_Modal_List.getInvoice_Flag().equals("1")) {
                holder.cdParent.setBackgroundResource(R.color.invoiceordercolor);
            } else {
                holder.cdParent.setBackgroundResource(R.color.greentrans);
            }
            holder.btnCallMob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialogBox.showDialog(context, HAPApp.Title, "Do you want to Call this Outlet?", "Yes", "No", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            mAdapterOnClick.CallMobile(holder.txMobile.getText().toString().replaceAll(",", ""));
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {

                        }
                    });
                }
            });
            holder.btnEditRet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddNewRetailer.class);
                    Shared_Common_Pref.Outlet_Info_Flag = "1";
                    Shared_Common_Pref.Editoutletflag = "1";
                    Shared_Common_Pref.Outler_AddFlag = "0";
                    Shared_Common_Pref.FromActivity = "Outlets";
                    Shared_Common_Pref.OutletCode = String.valueOf(mRetailer_Modal_List.getId());
                    intent.putExtra("OutletCode", String.valueOf(mRetailer_Modal_List.getId()));
                    intent.putExtra("OutletName", mRetailer_Modal_List.getName());
                    intent.putExtra("OutletAddress", mRetailer_Modal_List.getListedDrAddress1());
                    intent.putExtra("OutletMobile", mRetailer_Modal_List.getPrimary_No());
                    intent.putExtra("OutletRoute", mRetailer_Modal_List.getTownName());
                    context.startActivity(intent);
                }
            });
            holder.linDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sOutletName = mRetailer_Modal_List.getName();

                    drawRoute(sOutletName, mRetailer_Modal_List.getLat(), mRetailer_Modal_List.getLong());
                }
            });
            holder.parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterOnClick.onIntentClick(holder.getBindingAdapterPosition());
                    Shared_Common_Pref.OutletAddress = String.valueOf(mRetailer_Modal_List.getListedDrAddress1());
                }
            });

            holder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.btnView.getText().toString().equalsIgnoreCase("VIEW")) {
                        holder.btnView.setText("HIDE");
                        holder.llDataParent.setVisibility(View.VISIBLE);
                    } else {
                        holder.btnView.setText("VIEW");
                        holder.llDataParent.setVisibility(View.GONE);
                    }
                }
            });

            holder.txTodayTotQty.setText("0");
            holder.txTodayTotVal.setText(CurrencySymbol+" 0.00");
            holder.txPreTotQty.setText("0");
            holder.txPreTotVal.setText(CurrencySymbol+" 0.00");
            //PreSales
            JSONArray TodaySales = PreSales.getJSONArray("todaydata");
            Boolean DtaBnd = false;
            if (TodaySales.length() > 0) {
                for (int i = 0; i < TodaySales.length(); i++) {
                    JSONObject item = TodaySales.getJSONObject(i);
                    if (item.getString("Cust_Code").equals(mRetailer_Modal_List.getId())) {
                        JSONArray BindArry = item.getJSONArray("Items");
                        holder.lstTdyView.setAdapter(new CatewiseSalesaAdapter(BindArry, R.layout.categorywise_sales_adp, context));
                        DtaBnd = true;

                        int iQty = 0;
                        double iVal = 0.0;
                        for (int il = 0; il < BindArry.length(); il++) {
                            JSONObject itm = BindArry.getJSONObject(il);
                            if (itm.has("Vals")) {
                                JSONArray itmv = itm.getJSONArray("Vals");
                                if (itmv.length() > 0) {
                                    iQty += itmv.getJSONObject(0).getInt("Qty");
                                    iVal += itmv.getJSONObject(0).getDouble("Val");
                                }
                            }
                        }
                        holder.txTodayTotQty.setText(String.valueOf(iQty));
                        holder.txTodayTotVal.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(iVal));
                    }
                }
            }
            if (DtaBnd == false) {
                JSONArray BindArry = PreSales.getJSONArray("Group");
                holder.lstTdyView.setAdapter(new CatewiseSalesaAdapter(BindArry, R.layout.categorywise_sales_adp, context));
            }

            getMnthlyDta(holder, mRetailer_Modal_List.getId(), holder.tvFirstMonth.getText().toString());
            holder.tvFirstMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tvFirstMonth.setTypeface(null, Typeface.BOLD);
                    holder.tvFirstMonth.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

                    holder.tvSecondMnth.setTypeface(null, Typeface.NORMAL);
                    holder.tvSecondMnth.setTextColor(context.getResources().getColor(R.color.black_80));

                    holder.tvThirdMnth.setTypeface(null, Typeface.NORMAL);
                    holder.tvThirdMnth.setTextColor(context.getResources().getColor(R.color.black_80));

                    getMnthlyDta(holder, mRetailer_Modal_List.getId(), holder.tvFirstMonth.getText().toString());
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

                    getMnthlyDta(holder, mRetailer_Modal_List.getId(), holder.tvSecondMnth.getText().toString());
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

                    getMnthlyDta(holder, mRetailer_Modal_List.getId(), holder.tvThirdMnth.getText().toString());
                }
            });


        } catch (Exception e) {
            Log.v("RouteAdapter: ", e.getMessage());
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

    public void getMnthlyDta(Route_View_Adapter.MyViewHolder holder, String CusId, String Mnth) {

        JSONArray PrevSales = null;
        try {
            PrevSales = PreSales.getJSONArray("data");
            boolean DtaBnd = false;
            JSONArray BindArry = new JSONArray();
            if (PrevSales.length() > 0) {
                for (int i = 0; i < PrevSales.length(); i++) {
                    JSONObject item = PrevSales.getJSONObject(i);
                    String str = item.getString("Mnth").substring(0, 3);
                    if (item.getString("Cust_Code").equals(CusId) &&
                            Mnth.equals(str)) {
                        BindArry = item.getJSONArray("Items");
                        if (BindArry.length() > 0) {
                            holder.lstPreView.setAdapter(new CatewiseSalesaAdapter(BindArry, R.layout.categorywise_sales_mnth_adp, context));
                            DtaBnd = true;
                        }
                    }
                }
            }
            if (DtaBnd == false) {
                BindArry = PreSales.getJSONArray("Group");
                holder.lstPreView.setAdapter(new CatewiseSalesaAdapter(BindArry, R.layout.categorywise_sales_mnth_adp, context));
            }

            sumOfTotal(BindArry, holder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sumOfTotal(JSONArray AryDta, Route_View_Adapter.MyViewHolder holder) {
        try {
            int iQty = 0;
            double iVal = 0.0;
            for (int il = 0; il < AryDta.length(); il++) {
                JSONObject itm = AryDta.getJSONObject(il);
                if (itm.has("Vals")) {
                    JSONArray itmv = itm.getJSONArray("Vals");
                    if (itmv.length() > 0) {
                        iQty += itmv.getJSONObject(0).getInt("Qty");
                        iVal += itmv.getJSONObject(0).getDouble("Val");
                    }
                }
            }
            holder.txPreTotQty.setText(String.valueOf(iQty));
            holder.txPreTotVal.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(iVal));

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public int getItemCount() {
        if (Retailer_Modal_Listitem != null)
            return Retailer_Modal_Listitem.size();
        else
            return 0;
    }


}