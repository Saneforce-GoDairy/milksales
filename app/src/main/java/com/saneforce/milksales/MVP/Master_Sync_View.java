package com.saneforce.milksales.MVP;

import android.util.Log;

import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Master_Sync_View implements Main_Model.GetRoutemastersyncResult {

    String commonworktype;
    Shared_Common_Pref shared_common_pref;

    @Override
    public void GetRouteResult(Main_Model.GetRoutemastersyncResult.OnFinishedListenerroute onFinishedListener) {
        /** Create handle for the RetrofitInstance interface*/

        ///SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

// 0-retailor,1=distributor,2=category list,3=product list,4=rout list
        for (int i = 0; i < 7; i++) {
            String axnname = "table/list";
            if (i == 0) {
                commonworktype = "{\"tableName\":\"mas_worktype\",\"coloumns\":\"[\\\"type_code as id\\\", \\\"Wtype as name\\\"]\",\"where\":\"[\\\"isnull(Active_flag,0)=0\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                //commonworktype = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            } else if (i == 1) {
                commonworktype = "{\"tableName\":\"vwstockiest_Master_APP\",\"coloumns\":\"[\\\"distributor_code as id\\\", \\\"stockiest_name as name\\\",\\\"town_code\\\",\\\"town_name\\\",\\\"Addr1\\\",\\\"Addr2\\\",\\\"City\\\",\\\"Pincode\\\",\\\"GSTN\\\",\\\"lat\\\",\\\"long\\\",\\\"addrs\\\",\\\"Tcode\\\",\\\"Dis_Cat_Code\\\"]\",\"where\":\"[\\\"isnull(Stockist_Status,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            } else if (i == 2) {
                commonworktype = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            } else if (i == 3) {
                commonworktype = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                //commonworktype = "{\"tableName\":\"salesforce_master\",\"coloumns\":\"[\\\"sf_code as id\\\", \\\"sf_name as name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            } else if (i == 4) {
                axnname = "get/FieldForce_HQ";
                commonworktype = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            } else if (i == 5) {
                axnname = "get/Shift_type";
                commonworktype = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            } else if (i == 6) {
                axnname = "get/Chilling_Centre";
                commonworktype = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            }

            int ii = i;
            Log.e("Print_REquest", commonworktype);
            Log.e("SF_DETAILS", Shared_Common_Pref.Div_Code);
            Map<String, String> QueryString = new HashMap<>();
            QueryString.put("axn", axnname);
            QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
            QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
            QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
            QueryString.put("State_Code", Shared_Common_Pref.StateCode);

            Call<Object> call = service.GetRouteObject(QueryString, commonworktype);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    //approvalList=response.body();
                  //  Log.e("MAsterSyncView_Result", response.body() + "");
                    onFinishedListener.onFinishedrouteObject(response.body(), ii);
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    onFinishedListener.onFailure(t);
                }
            });
        }

    }
}

