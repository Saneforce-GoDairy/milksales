package com.saneforce.milksales.SFA_Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity_Hap.CustomListViewDialog;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.MVP.Main_Model;
import com.saneforce.milksales.Model_Class.Route_Master;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.Complete_Order_Adapter;
import com.saneforce.milksales.SFA_Adapter.Outlet_Orders_Alldays;
import com.saneforce.milksales.SFA_Model_Class.Retailer_Modal_List;
import com.saneforce.milksales.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Dashboard_Order_Reports extends AppCompatActivity implements Main_Model.MasterSyncView, View.OnClickListener, Master_Interface {
    List<Outlet_Orders_Alldays> Retailer_Modal_List;
    List<Outlet_Orders_Alldays> Retailer_Modal_ListFilter;
    List<Outlet_Orders_Alldays> FilterCompleteList;
    List<Outlet_Orders_Alldays> Retailer_Order_List;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    TextView headtext, textViewname, Alltextclick, Completeclick, Pendingclick, ReachedOutlet, distributor_text, route_text;
    View Alltextview, completeview, pendingview;
    Common_Model Model_Pojo;
    Shared_Common_Pref shared_common_pref;
    private Main_Model.presenter presenter;
    List<Common_Model> distributor_master = new ArrayList<>();
    List<Common_Model> Route_Masterlist = new ArrayList<>();
    CustomListViewDialog customDialog;
    List<Common_Model> FRoute_Master = new ArrayList<>();

    String Route_id, Distributor_Id;
    Shared_Common_Pref sharedCommonPref;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dashboard__order__reports);
            db = new DatabaseHandler(this);
            common_class = new Common_Class(this);
            common_class.getDataFromApi(Constants.Outlet_Total_AlldaysOrders, this, false);
            recyclerView = findViewById(R.id.leaverecyclerview);
            sharedCommonPref = new Shared_Common_Pref(Dashboard_Order_Reports.this);

//            presenter = new MasterSync_Implementations(this, new Master_Sync_View());
//            presenter.requestDataFromServer();

            shared_common_pref = new Shared_Common_Pref(this);
            headtext = findViewById(R.id.headtext);
            route_text = findViewById(R.id.route_text);
            distributor_text = findViewById(R.id.distributor_text);
            textViewname = findViewById(R.id.textViewname);
            Alltextclick = findViewById(R.id.Alltextclick);
            Completeclick = findViewById(R.id.Completeclick);
            Pendingclick = findViewById(R.id.Pendingclick);
            Alltextview = findViewById(R.id.Alltextview);
            completeview = findViewById(R.id.completeview);
            pendingview = findViewById(R.id.pendingview);
            Alltextview.setVisibility(View.VISIBLE);
            completeview.setVisibility(View.INVISIBLE);
            pendingview.setVisibility(View.INVISIBLE);
            Alltextclick.setOnClickListener(this);
            Completeclick.setOnClickListener(this);
            Pendingclick.setOnClickListener(this);

            distributor_text.setOnClickListener(this);
            route_text.setOnClickListener(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            gson = new Gson();
            userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
            }.getType();
            // String todayorderliost = sharedCommonPref.getvalue(Shared_Common_Pref.Outlet_Total_AlldaysOrders);
            String todayorderliost = String.valueOf(db.getMasterData(Constants.Outlet_Total_AlldaysOrders));
            userType = new TypeToken<ArrayList<Outlet_Orders_Alldays>>() {
            }.getType();
            Retailer_Order_List = gson.fromJson(todayorderliost, userType);
            FilterCompleteList = new ArrayList<>();
            Retailer_Modal_ListFilter = new ArrayList<>();
            int index = 0;
            Retailer_Modal_ListFilter.addAll(Retailer_Order_List);
            FilterCompleteList.addAll(Retailer_Order_List);
            recyclerView.setAdapter(new Complete_Order_Adapter(Retailer_Modal_ListFilter, R.layout.complete_orders_recyclerview, getApplicationContext(), new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    Shared_Common_Pref.Outler_AddFlag = "0";
                    Log.e("Route_Outlet_Info", Shared_Common_Pref.Outler_AddFlag);
                /*Shared_Common_Pref.OutletName = Retailer_Modal_List.get(position).getName().toUpperCase() + "~" + Retailer_Modal_List.get(position).getId();
                Shared_Common_Pref.OutletCode = Retailer_Modal_List.get(position).getId();
                common_class.CommonIntentwithoutFinish(Route_Product_Info.class);*/

                }
            }));

            ImageView ivToolbarHome=findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this,ivToolbarHome);
        } catch (Exception e) {

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Alltextclick:
                OutletFilter("t", "1");
                Alltextview.setVisibility(View.VISIBLE);
                completeview.setVisibility(View.INVISIBLE);
                pendingview.setVisibility(View.INVISIBLE);
                break;
            case R.id.Completeclick:
                OutletFilter("t", "2");
                Alltextview.setVisibility(View.INVISIBLE);
                completeview.setVisibility(View.VISIBLE);
                pendingview.setVisibility(View.INVISIBLE);
                break;
            case R.id.Pendingclick:
                OutletFilter("t", "3");
                Alltextview.setVisibility(View.INVISIBLE);
                completeview.setVisibility(View.INVISIBLE);
                pendingview.setVisibility(View.VISIBLE);
                break;

            case R.id.distributor_text:
                customDialog = new CustomListViewDialog(Dashboard_Order_Reports.this, distributor_master, 2);
                Window windoww = customDialog.getWindow();
                windoww.setGravity(Gravity.CENTER);
                windoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;
            case R.id.route_text:
                customDialog = new CustomListViewDialog(Dashboard_Order_Reports.this, FRoute_Master, 3);
                Window windowww = customDialog.getWindow();
                windowww.setGravity(Gravity.CENTER);
                windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void setDataToRoute(ArrayList<Route_Master> noticeArrayList) {
        Log.e("ROUTE_MASTER", String.valueOf(noticeArrayList.size()));
    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 2) {
            route_text.setText("");
            Distributor_Id = myDataset.get(position).getId();
            distributor_text.setText(myDataset.get(position).getName());
            loadroute(myDataset.get(position).getId());
        } else if (type == 3) {
            Route_id = myDataset.get(position).getId();
            route_text.setText(myDataset.get(position).getName());
            //   OutletFilter(myDataset.get(position).getId(), "0");
        }

    }

    private void OutletFilter(String id, String flag) {
        Retailer_Modal_ListFilter.clear();
        for (int i = 0; i < FilterCompleteList.size(); i++) {
            if (FilterCompleteList.get(i).getTerritory_Code().toLowerCase().trim().replaceAll("\\s", "").contains(id.toLowerCase().trim().replaceAll("\\s", ""))) {
                Retailer_Modal_ListFilter.add(Retailer_Modal_List.get(i));
            }
        }
        recyclerView.setAdapter(new Complete_Order_Adapter(Retailer_Modal_ListFilter, R.layout.complete_orders_recyclerview, getApplicationContext(), new AdapterOnClick() {
            @Override
            public void onIntentClick(int position) {
                Shared_Common_Pref.OutletName = Retailer_Modal_ListFilter.get(position).getOutletName().toUpperCase()
                        + "~" + Retailer_Modal_ListFilter.get(position).getOutletCode();
                Shared_Common_Pref.OutletCode = Retailer_Modal_ListFilter.get(position).getOutletCode();
                common_class.CommonIntentwithFinish(Route_Product_Info.class);

            }
        }));
    }

    @Override
    public void setDataToRouteObject(Object noticeArrayList, int position) {
        Log.e("Calling Position", String.valueOf(position));
        Log.e("ROUTE_MASTER_Object", String.valueOf(noticeArrayList));
        if (position == 0) {
            Log.e("SharedprefrenceVALUES", new Gson().toJson(noticeArrayList));
            GetJsonData(new Gson().toJson(noticeArrayList), "0");
        } else if (position == 1) {
            GetJsonData(new Gson().toJson(noticeArrayList), "1");
        } else if (position == 2) {
            GetJsonData(new Gson().toJson(noticeArrayList), "2");
        } else if (position == 3) {
            GetJsonData(new Gson().toJson(noticeArrayList), "3");
        } else if (position == 4) {
            GetJsonData(new Gson().toJson(noticeArrayList), "4");
        } else if (position == 5) {
            GetJsonData(new Gson().toJson(noticeArrayList), "5");
        } else {

        }

    }

    @Override
    public void onResponseFailure(Throwable throwable) {


    }

    public void loadroute(String id) {

        if (common_class.isNullOrEmpty(String.valueOf(id))) {
            Toast.makeText(this, "Select Franchise", Toast.LENGTH_SHORT).show();
        }
        FRoute_Master.clear();
        for (int i = 0; i < Route_Masterlist.size(); i++) {
            if (Route_Masterlist.get(i).getFlag().toLowerCase().trim().replaceAll("\\s", "").contains(id.toLowerCase().trim().replaceAll("\\s", ""))) {
                Log.e("Route_Masterlist", String.valueOf(id) + "STOCKIST" + Route_Masterlist.get(i).getFlag());
                FRoute_Master.add(new Common_Model(Route_Masterlist.get(i).getId(), Route_Masterlist.get(i).getName(), Route_Masterlist.get(i).getFlag()));
            }

        }

    }

    private void GetJsonData(String jsonResponse, String type) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("FWFlg");
                String ETabs = jsonObject1.optString("ETabs");
                Model_Pojo = new Common_Model(id, name, flag);
                if (type.equals("1")) {
                    distributor_master.add(Model_Pojo);
                } else if (type.equals("2")) {
                    Log.e("STOCKIST_CODE", jsonObject1.optString("stockist_code"));
                    Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                    FRoute_Master.add(Model_Pojo);
                    Route_Masterlist.add(Model_Pojo);
                }

            }

            //spinner.setSelection(adapter.getPosition("select worktype"));
            //            parseJsonData_cluster(clustspin_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
