package com.saneforce.milksales.MVP;

import android.util.Log;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Offline_SyncView implements Main_Model.GetRoutemastersyncResult {
    String QuerySTring;

    @Override
    public void GetRouteResult(Main_Model.GetRoutemastersyncResult.OnFinishedListenerroute onFinishedListener) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> QueryString = new HashMap<>();

        for (int i = 0; i < 12; i++) {
            String axnname = "table/list";
            switch (i) {
                case (0):
                    //Outlet_List
                    QuerySTring = "{\"tableName\":\"vwDoctor_Master_APP\",\"coloumns\":\"[\\\"doctor_code as id\\\", \\\"doctor_name as name\\\",\\\"Type\\\",  \\\"reason_category\\\", \\\"town_code\\\", \\\"ListedDr_Email\\\",\\\"cityname\\\",\\\"Owner_Name\\\",\\\"town_name\\\",\\\"lat\\\",\\\"long\\\", \\\"pin_code\\\", \\\"gst\\\",   \\\"Hatsanavail_Switch\\\"  , \\\"HatsanCategory_Switch\\\",\\\"addrs\\\",\\\"ListedDr_Address1\\\",\\\"ListedDr_Sl_No\\\",   \\\"Compititor_Id\\\", \\\"Compititor_Name\\\",  \\\"LastUpdt_Date\\\",    \\\"Mobile_Number\\\",\\\"Statusname\\\" ,\\\"Invoice_Flag\\\" , \\\"InvoiceValues\\\" , \\\"Valuesinv\\\" , \\\"InvoiceDate\\\", \\\"Category_Universe_Id\\\", \\\"Hatsun_AvailablityId\\\",   \\\"Doc_cat_code\\\",\\\"ContactPersion\\\",\\\"Doc_Special_Code\\\",\\\"Distributor_Code\\\"]\",\"where\":\"[\\\"isnull(Doctor_Active_flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    //commonworktype = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    break;
                case (1):
                    //Distributor_List
                    QuerySTring = "{\"tableName\":\"vwstockiest_Master_APP\",\"coloumns\":\"[\\\"distributor_code as id\\\", \\\"stockiest_name as name\\\",\\\"town_code\\\",\\\"town_name\\\",\\\"Addr1\\\",\\\"Addr2\\\",\\\"City\\\",\\\"Pincode\\\",\\\"GSTN\\\",\\\"lat\\\",\\\"long\\\",\\\"addrs\\\",\\\"Tcode\\\",\\\"Dis_Cat_Code\\\"]\",\"where\":\"[\\\"isnull(Stockist_Status,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

                    break;
                case (2):
                    //CategorySync
                    QuerySTring = "{\"tableName\":\"category_universe\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    break;
                case (3):
                    //Product Sync
                    QuerySTring = "{\"tableName\":\"getproduct_details\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    break;
                case (4):
                    //GetTodayOrder
                    QuerySTring = "{\"tableName\":\"gettotalorderbytoday\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    break;
                case (5):
                    //GetTodayOrder
                    QuerySTring = "{\"tableName\":\"GettotalOrderDetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    break;
                case (6):
                    axnname = "Get/dayplanresult";
                    QueryString.put("Date", Common_Class.GetDatewothouttime());
                    break;
                case (7):
                    QuerySTring = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    break;
                case (8):
                    QuerySTring = "{\"tableName\":\"gettotaloutletorders\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    break;
                case (9):
                    QuerySTring = "{\"tableName\":\"gettotalalldaysoutletorders\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    break;
                case (10):
                    QuerySTring = "{\"tableName\":\"gettodaysf_order_values\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    break;
                default:
                    QuerySTring = "{\"tableName\":\"get_compititordetails\"}";
            }
            int ii = i;
            Log.e("Print_REquest", QuerySTring);
            Log.e("SF_DETAILS", Shared_Common_Pref.Div_Code);
            QueryString.put("axn", axnname);
            QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
            QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
            QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
            QueryString.put("State_Code", Shared_Common_Pref.StateCode);
            Call<Object> call = service.GetRouteObject(QueryString, QuerySTring);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    String value = String.valueOf(response.body());
                    Log.e("test", value + "");

                 //   Log.e("MAsterSyncView_Result", response.body() + "");
                    //approvalList=response.body();
                    onFinishedListener.onFinishedrouteObject(response.body(), ii);
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    onFinishedListener.onFailure(t);
                }
            });
        }


//        for (int i = 0; i < 12; i++) {
//            String axnname = "table/list";
//            if (i == 0) {
//                //Outlet_List
//                QuerySTring = "{\"tableName\":\"vwDoctor_Master_APP\",\"coloumns\":\"[\\\"doctor_code as id\\\", \\\"doctor_name as name\\\",  \\\"reason_category\\\", \\\"town_code\\\", \\\"ListedDr_Email\\\",\\\"cityname\\\",\\\"Owner_Name\\\",\\\"town_name\\\",\\\"lat\\\",\\\"long\\\", \\\"pin_code\\\", \\\"gst\\\",   \\\"Hatsanavail_Switch\\\"  , \\\"HatsanCategory_Switch\\\",\\\"addrs\\\",\\\"ListedDr_Address1\\\",\\\"ListedDr_Sl_No\\\",   \\\"Compititor_Id\\\", \\\"Compititor_Name\\\",  \\\"LastUpdt_Date\\\",    \\\"Mobile_Number\\\",\\\"Statusname\\\" ,\\\"Invoice_Flag\\\" , \\\"InvoiceValues\\\" , \\\"Valuesinv\\\" , \\\"InvoiceDate\\\", \\\"Category_Universe_Id\\\", \\\"Hatsun_AvailablityId\\\",   \\\"Doc_cat_code\\\",\\\"ContactPersion\\\",\\\"Doc_Special_Code\\\",\\\"Distributor_Code\\\"]\",\"where\":\"[\\\"isnull(Doctor_Active_flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//                //commonworktype = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//            } else if (i == 1) {
//                //Distributor_List
//                QuerySTring = "{\"tableName\":\"vwstockiest_Master_APP\",\"coloumns\":\"[\\\"distributor_code as id\\\", \\\"stockiest_name as name\\\",\\\"town_code\\\",\\\"town_name\\\",\\\"Addr1\\\",\\\"Addr2\\\",\\\"City\\\",\\\"Pincode\\\",\\\"GSTN\\\",\\\"lat\\\",\\\"long\\\",\\\"addrs\\\",\\\"Tcode\\\",\\\"Dis_Cat_Code\\\"]\",\"where\":\"[\\\"isnull(Stockist_Status,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//            } else if (i == 2) {
//                //CategorySync
//                QuerySTring = "{\"tableName\":\"category_universe\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//            } else if (i == 3) {
//                //Product Sync
//                QuerySTring = "{\"tableName\":\"getproduct_details\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//            } else if (i == 4) {
//                //GetTodayOrder
//                QuerySTring = "{\"tableName\":\"gettotalorderbytoday\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//                QueryString.put("fromdate", Common_Class.GetDatewothouttime());
//                QueryString.put("todate", Common_Class.GetDatewothouttime());
//            } else if (i == 5) {
//                //GetTodayOrder
//                QuerySTring = "{\"tableName\":\"GettotalOrderDetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//                QueryString.put("fromdate", Common_Class.GetDatewothouttime());
//                QueryString.put("todate", Common_Class.GetDatewothouttime());
//            } else if (i == 6) {
//                axnname = "Get/dayplanresult";
//                QueryString.put("Date", Common_Class.GetDatewothouttime());
//            } else if (i == 7) {
//                QuerySTring = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//            } else if (i == 8) {
//                QuerySTring = "{\"tableName\":\"gettotaloutletorders\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//                QueryString.put("fromdate", Common_Class.GetDatewothouttime());
//                QueryString.put("todate", Common_Class.GetDatewothouttime());
//            } else if (i == 9) {
//                QuerySTring = "{\"tableName\":\"gettotalalldaysoutletorders\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//                QueryString.put("fromdate", Common_Class.GetDatewothouttime());
//                QueryString.put("todate", Common_Class.GetDatewothouttime());
//            } else if (i == 10) {
//                QuerySTring = "{\"tableName\":\"gettodaysf_order_values\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//                QueryString.put("fromdate", Common_Class.GetDatewothouttime());
//                QueryString.put("todate", Common_Class.GetDatewothouttime());
//            } else {
//                QuerySTring = "{\"tableName\":\"get_compititordetails\"}";
//            }
//            int ii = i;
//            Log.e("Print_REquest", QuerySTring);
//            Log.e("SF_DETAILS", Shared_Common_Pref.Div_Code);
//            QueryString.put("axn", axnname);
//            QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
//            QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
//            QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
//            QueryString.put("State_Code", Shared_Common_Pref.StateCode);
//            Call<Object> call = service.GetRouteObject(QueryString, QuerySTring);
//            call.enqueue(new Callback<Object>() {
//                @Override
//                public void onResponse(Call<Object> call, Response<Object> response) {
//
//                    String value = String.valueOf(response.body());
//                    Log.e("test", value + "");
//
//                    Log.e("MAsterSyncView_Result", response.body() + "");
//                    //approvalList=response.body();
//                    onFinishedListener.onFinishedrouteObject(response.body(), ii);
//                }
//
//                @Override
//                public void onFailure(Call<Object> call, Throwable t) {
//                    onFinishedListener.onFailure(t);
//                }
//            });
//        }
    }
}

