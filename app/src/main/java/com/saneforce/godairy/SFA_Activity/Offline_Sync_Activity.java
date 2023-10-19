package com.saneforce.godairy.SFA_Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.saneforce.godairy.Activity_Hap.SFA_Activity;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.MVP.Main_Model;
import com.saneforce.godairy.R;
import com.saneforce.godairy.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class Offline_Sync_Activity extends Activity implements View.OnClickListener/*, Main_Model.MasterSyncView*/ {
    private Main_Model.presenter presenter;
    Shared_Common_Pref sharedCommonPref;
    Type userType;
    Gson gson;
    private ProgressDialog progress;
    Common_Class common_class;
    TextView Backbuttontextview, lastsuccessync, Sf_UserId, distributornametext, SyncButton, totaloutlets, todayoutlet, invoicevalues, ordervalues;
    List<com.saneforce.godairy.SFA_Model_Class.Retailer_Modal_List> Retailer_Modal_List;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline__sync_);
        db = new DatabaseHandler(this);

        sharedCommonPref = new Shared_Common_Pref(Offline_Sync_Activity.this);
        Backbuttontextview = findViewById(R.id.Backbuttontextview);
        lastsuccessync = findViewById(R.id.lastsuccessync);
        distributornametext = findViewById(R.id.distributornametext);
        SyncButton = findViewById(R.id.SyncButton);
        Sf_UserId = findViewById(R.id.Sf_UserId);
        invoicevalues = findViewById(R.id.invoicevalues);
        ordervalues = findViewById(R.id.ordervalues);
        totaloutlets = findViewById(R.id.totaloutlets);
        todayoutlet = findViewById(R.id.todayoutlet);
        //   presenter = new MasterSync_Implementations(this, new Offline_SyncView());
        Backbuttontextview.setOnClickListener(this);
        gson = new Gson();
        common_class = new Common_Class(this);
//        presenter.requestDataFromServer();
        lastsuccessync.setText(Common_Class.GetDatemonthyearformat());
        Sf_UserId.setText(Shared_Common_Pref.Sf_Code);




       /* if (Shared_Common_Pref.Todaydayplanresult != null) {
            GetJsonData(sharedCommonPref.getvalue(Shared_Common_Pref.Todaydayplanresult), "dayplan");
        }
        if (Shared_Common_Pref.TodaySfOrdervalues != null) {
            GetJsonData(sharedCommonPref.getvalue(Shared_Common_Pref.TodaySfOrdervalues), "order");
        }*/
       /* String OrdersTable = sharedCommonPref.getvalue(Shared_Common_Pref.Outlet_List);
        Retailer_Modal_List = new ArrayList<>();
        if (OrdersTable != null && !OrdersTable.equals("Outlet_List")) {
            Retailer_Modal_List = gson.fromJson(OrdersTable, userType);
            totaloutlets.setText("" + Retailer_Modal_List.size());
            int todaycount = 0;
            for (Retailer_Modal_List lm : Retailer_Modal_List) {
                if (lm.getLastUpdt_Date().equals(Common_Class.GetDatewothouttime())) {
                    todaycount++;
                }
            }
            todayoutlet.setText("" + todaycount);
        }*/
        SyncButton.setOnClickListener(this);
        //Log.e("SYNC_FLAG", Shared_Common_Pref.Sync_Flag);
        if (Shared_Common_Pref.Sync_Flag.equals("0")) {
            progress = new ProgressDialog(this);
            progress.setMessage("Data Is Syncing");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
            final int totalProgressTime = 100;
            final Thread t = new Thread() {
                @Override
                public void run() {
                    int jumpTime = 0;
                    while (jumpTime < totalProgressTime) {
                        try {
                            sleep(200);
                            jumpTime += 5;
                            progress.setProgress(jumpTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            t.start();
        }


        ImageView ivToolbarHome=findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this,ivToolbarHome);
    }

   /* @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setDataToRoute(ArrayList<Route_Master> noticeArrayList) {
    }

    @Override
    public void setDataToRouteObject(Object responsebody, int position) {
        Log.e("Calling Position", String.valueOf(position));
        // Toast.makeText(this, "Position" + position, Toast.LENGTH_SHORT).show();
        String serializedData = gson.toJson(responsebody);


        switch (position) {

            case (0):
                //Outlet_List
                System.out.println("GetOutlet_All" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.Outlet_List, serializedData);
                System.out.println("OUTLETLIST" + sharedCommonPref.getvalue(Shared_Common_Pref.Outlet_List));
                getResponseFromserver(Constants.Retailer_OutletList, serializedData);
                break;
            case (1):
                //Distributor_List
                // System.out.println("Distributor_List" + serializedData);
                // sharedCommonPref.save(Shared_Common_Pref.Distributor_List, serializedData);
                // getResponseFromserver(Constants.Distributor_List, serializedData);
                break;
            case (2):
                //Category_List
                sharedCommonPref.save(Shared_Common_Pref.Category_List, serializedData);
                System.out.println("Category_List" + serializedData);

                break;
            case (3):
                //Product_List
                System.out.println("Product_List" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.Product_List, serializedData);
                break;
            case (4):
                //GetTodayOrder_List
                System.out.println("GetTodayOrder_List" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.GetTodayOrder_List, serializedData);
                break;
            case (5):
                //GetTodayOrderDetails
                System.out.println("GetTodayOrderDetails_List" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.TodayOrderDetails_List, serializedData);
                break;
            case (6):
                System.out.println("Todaydayplanresult" + serializedData);
                GetJsonData(serializedData, "dayplan");
                sharedCommonPref.save(Shared_Common_Pref.Todaydayplanresult, serializedData);
                break;
            case (7):
                // System.out.println("Town_List" + serializedData);
                //sharedCommonPref.save(Shared_Common_Pref.Rout_List, serializedData);
                //  getResponseFromserver(Constants.Rout_List, serializedData);
                break;
            case (8):
                System.out.println("Route_Dashboars_Orders" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.Outlet_Total_Orders, serializedData);
                break;
            case (9):
                System.out.println("Route_Dashboars_AllDays" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.Outlet_Total_AlldaysOrders, serializedData);
                break;
            case (10):
                sharedCommonPref.save(Shared_Common_Pref.TodaySfOrdervalues, serializedData);
                GetJsonData(serializedData, "order");
                break;
            default:
                if (progress != null)
                    progress.dismiss();
                System.out.println("Compititor_List" + serializedData);
                System.out.println("Compititor_SYncFlag" + sharedCommonPref.Sync_Flag);
                sharedCommonPref.save(Shared_Common_Pref.Compititor_List, serializedData);
                if (sharedCommonPref.Sync_Flag != null && sharedCommonPref.Sync_Flag.equals("1")) {
                    common_class.CommonIntentwithNEwTask(Dashboard_Route.class);
                } else if (sharedCommonPref.Sync_Flag != null && sharedCommonPref.Sync_Flag.equals("2")) {
                    startActivity(new Intent(getApplicationContext(), Invoice_History.class));
                    finish();
                } else if (sharedCommonPref.Sync_Flag != null && sharedCommonPref.Sync_Flag.equals("0")) {
                    common_class.CommonIntentwithFinish(SFA_Activity.class);
                }

        }
    }

    @Override
    public void onResponseFailure(Throwable throwable) {

    }

    */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Backbuttontextview:
                common_class.CommonIntentwithFinish(SFA_Activity.class);
                break;
            case R.id.SyncButton:
//                Shared_Common_Pref.Sync_Flag = "0";
//                common_class.CommonIntentwithFinish(Offline_Sync_Activity.class);
                common_class.getDataFromApi(Constants.Retailer_OutletList, this, true);
                break;
        }


    }

    public void GetJsonData(String jsonResponse, String Name) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (Name.equals("dayplan")) {
                    distributornametext.setText(jsonObject1.optString("StkName"));
                } else {
                    invoicevalues.setText("" + jsonObject1.optString("Invoicevalues"));
                    ordervalues.setText("" + jsonObject1.optString("Order_Value"));
                    todayoutlet.setText("" + jsonObject1.optString("Outlet_Fortheday"));
                    totaloutlets.setText("" + jsonObject1.optString("Orderoutlet_Count") + "/" + jsonObject1.optString("Totaloutlet"));
                }
            }
            //spinner.setSelection(adapter.getPosition("select worktype"));
            //            parseJsonData_cluster(clustspin_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //156
    //Store Distributor_List values in local db
    public void getResponseFromserver(String key, String jsonResponse) {
        db.deleteMasterData(key);
        db.addMasterData(key, jsonResponse);
    }

    //156

}