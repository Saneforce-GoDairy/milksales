package com.saneforce.milksales.Common_Class;


import static android.Manifest.permission.CALL_PHONE;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;
import static com.saneforce.milksales.Activity_Hap.SFA_Activity.sfa_date;
import static com.saneforce.milksales.Common_Class.Constants.Retailer_OutletList;
import static com.saneforce.milksales.Common_Class.Constants.Rout_List;
import static com.saneforce.milksales.SFA_Activity.HAPApp.ProductsLoaded;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Activity.ProcurementDashboardActivity;
import com.saneforce.milksales.Activity_Hap.CustomListViewDialog;
import com.saneforce.milksales.Activity_Hap.Dashboard;
import com.saneforce.milksales.Activity_Hap.Dashboard_Two;
import com.saneforce.milksales.Activity_Hap.SFA_Activity;
import com.saneforce.milksales.Interface.APIResult;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.OnLiveUpdateListener;
import com.saneforce.milksales.Interface.UpdateResponseUI;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.GrnHistory;
import com.saneforce.milksales.SFA_Activity.GrnListActivity;
import com.saneforce.milksales.SFA_Activity.HAPApp;
import com.saneforce.milksales.SFA_Activity.HistoryInfoActivity;
import com.saneforce.milksales.SFA_Activity.Invoice_History;
//import com.hap.checkinproc.SFA_Activity.POSViewEntryActivity;
import com.saneforce.milksales.SFA_Activity.PosHistoryActivity;
import com.saneforce.milksales.SFA_Activity.ProjectionHistoryActivity;
import com.saneforce.milksales.SFA_Activity.TodayPrimOrdActivity;
import com.saneforce.milksales.SFA_Activity.VanStockViewActivity;
import com.saneforce.milksales.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Common_Class {

    Intent intent;
    Activity activity;
    Dialog dialog_invitation = null;
    public Context context;
    Shared_Common_Pref shared_common_pref;
    ProgressDialog nDialog;

    Gson gson;

    // Gson gson;
    String Result = "false";
    public static String Work_Type = "0";
    public static int count;
    private UpdateResponseUI updateUi;
    private DatePickerDialog fromDatePickerDialog;

    String pickDate = "";
    private CustomListViewDialog customDialog;
    SharedPreferences UserDetails;
    public static final String UserDetail = "MyPrefs";
    public int brandPos = 0, grpPos = 0, categoryPos = 0;

    public void CommonIntentwithFinish(Class classname) {
        intent = new Intent(activity, classname);

        activity.startActivity(intent);
        activity.finish();
    }

    public String getDateWithFormat(String dateInString, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sdf = new SimpleDateFormat(pattern);
        Date resultdate = new Date(c.getTimeInMillis());
        dateInString = sdf.format(resultdate);
        return dateInString;
    }

    public Common_Class(Context context) {
        this.context = context;
        shared_common_pref = new Shared_Common_Pref(context);

    }


    public static String GetDatemonthyearformat() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dpln = new SimpleDateFormat("dd-MM-yyyy");
        String plantime = dpln.format(c.getTime());
        return plantime;
    }

    public static String GetDatemonthyearTimeformat() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dpln = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String plantime = dpln.format(c.getTime());
        return plantime;
    }

    public void CommonIntentwithoutFinish(Class classname) {
        intent = new Intent(activity, classname);
        activity.startActivity(intent);
    }

    public Common_Class(Activity activity) {
        this.activity = activity;
        nDialog = new ProgressDialog(activity);
        shared_common_pref = new Shared_Common_Pref(activity);
    }

    public void ProgressdialogShow(int flag, String message) {

        if (flag == 1) {
            nDialog.setMessage("Loading.......");
            if (message.length() > 1) {
                nDialog.setTitle(message);
                nDialog.setCancelable(false);
            }
            nDialog.setIndeterminate(false);
            nDialog.show();

            if (message.equals("")) {
                nDialog.setCancelable(false);
                nDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                nDialog.setContentView(R.layout.loading_progress_bottom);
            }


        } else {
            nDialog.dismiss();
        }
    }

    public static String GetDateOnly() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dpln = new SimpleDateFormat("yyyy-MM-dd");
        String plantime = dpln.format(c.getTime());
        return plantime;
    }

    public static String GetDay() {
        Date dd = new Date();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");

        return simpleDateformat.format(dd);
    }

    public boolean isNetworkAvailable(final Context context) {
        this.context = context;

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static String GetDatewothouttime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dpln = new SimpleDateFormat("yyyy-MM-dd");
        String plantime = dpln.format(c.getTime());
        return plantime;
    }


    public void makeCall(int mobilenumber) {
        final int REQUEST_PHONE_CALL = 1;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mobilenumber));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            activity.startActivity(callIntent);
        }


    }

    public void makeCall(String mobilenumber) {
        final int REQUEST_PHONE_CALL = 1;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mobilenumber));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            context.startActivity(callIntent);
        }


    }

    public static double ParseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch (Exception e) {
                return -1;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        } else return 0;
    }

    public static double Check_Distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public List<Common_Model> getfilterList(java.util.List<Common_Model> Jointworklistview) {
        List<Common_Model> Jointworklistviewsave = new ArrayList<>();
        for (int i = 0; i < Jointworklistview.size(); i++) {
            if (Jointworklistview.get(i).isSelected()) {
                Log.e("SELECTED", String.valueOf(Jointworklistview.get(i).isSelected()));
                Jointworklistviewsave.add(new Common_Model(Jointworklistview.get(i).getName(), Jointworklistview.get(i).getId(), true));
            }

        }

        return Jointworklistviewsave;
    }

    ;


    public JsonArray FilterGson(final Iterable<JsonObject> SrcArray, String colName, String searchVal) {
        JsonArray ResArray = new JsonArray();
        for (JsonObject jObj : SrcArray) {
            if (jObj.get(colName).getAsString().equalsIgnoreCase(searchVal)) {
                ResArray.add(jObj);
            }
        }
        return ResArray;
    }


    public void getDataFromApi(String key, Activity activity, Boolean boolRefresh) {

        updateUi = ((UpdateResponseUI) activity);
        if (isNetworkAvailable(activity)) {
            String QuerySTring1 = "";
            Map<String, String> QueryString = new HashMap<>();
            String axnname = "table/list";

            switch (key) {

                case (Retailer_OutletList):
                    QuerySTring1 = "{\"tableName\":\"vwDoctor_Master_APP\",\"coloumns\":\"[\\\"doctor_code as id\\\", \\\"doctor_name as name\\\",\\\"Type\\\",\\\"DelivType\\\"," +
                            " \\\"reason_category\\\", \\\"StateCode\\\",\\\"Tcs\\\",\\\"Tds\\\",\\\"OrderFlg\\\",\\\"Outlet_Type\\\",\\\"town_code\\\", \\\"ListedDr_Email\\\",\\\"cityname\\\",\\\"CustomerCode\\\",\\\"Owner_Name\\\",\\\"Category\\\",\\\"Speciality\\\",\\\"Class\\\",\\\"ERP_Code\\\",\\\"town_name\\\"," +
                            "\\\"lat\\\",\\\"long\\\", \\\"pin_code\\\", \\\"gst\\\",   \\\"Hatsanavail_Switch\\\"  , \\\"HatsanCategory_Switch\\\"," +
                            "\\\"addrs\\\",\\\"ListedDr_Address1\\\",\\\"ListedDr_Sl_No\\\",   \\\"Compititor_Id\\\", \\\"Compititor_Name\\\", " +
                            " \\\"LastUpdt_Date\\\",\\\"Primary_No\\\",\\\"Secondary_No\\\",\\\"Mobile_Number\\\",\\\"Imagename\\\",\\\"Statusname\\\" ,\\\"Invoice_Flag\\\" , \\\"InvoiceValues\\\" ," +
                            " \\\"Valuesinv\\\" , \\\"InvoiceDate\\\", \\\"Category_Universe_Id\\\", \\\"Hatsun_AvailablityId\\\", \\\"ClosedRmks\\\",   " +
                            "\\\"Doc_cat_code\\\",\\\"ContactPersion\\\",\\\"Doc_Special_Code\\\",\\\"Distributor_Code\\\"]\",\"where\":\"" +
                            "[\\\"isnull(Doctor_Active_flag,0)=0\\\"]\",\"orderBy\":\"[\\\"OutletOrder asc\\\",\\\"doctor_name asc\\\"]\",\"desig\":\"stockist\"}";


                    break;
                case (Constants.Distributor_List):
                    ProgressdialogShow(1, "Data Syncing");
                    QuerySTring1 = "{\"tableName\":\"vwstockiest_Master_APP\",\"coloumns\":\"[\\\"distributor_code as id\\\",\\\"StateCode \\\", \\\"stockiest_name as name\\\",\\\"town_code\\\",\\\"town_name\\\",\\\"Addr1\\\",\\\"Addr2\\\",\\\"City\\\",\\\"Pincode\\\",\\\"GSTN\\\",\\\"lat\\\",\\\"long\\\",\\\"addrs\\\",\\\"Tcode\\\",\\\"Dis_Cat_Code\\\"]\",\"where\":\"[\\\"isnull(Stockist_Status,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    break;
                case (Constants.Category_List):
                    QuerySTring1 = "{\"tableName\":\"category_universe\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    break;
                case (Constants.Product_List):
                    QuerySTring1 = "{\"tableName\":\"getproduct_details\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0," +
                            "\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    break;
                case (Constants.Rout_List):
                    QuerySTring1 = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    break;

                case Constants.GetTodayOrder_List:
                    QuerySTring1 = "{\"tableName\":\"gettotalorderbytoday\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Invoice_History.tvStartDate.getText().toString());
                    QueryString.put("todate", Invoice_History.tvEndDate.getText().toString());
                    QueryString.put("RetailerID", Shared_Common_Pref.OutletCode);

                    break;

                case Constants.VanSalOrderList:
                    QuerySTring1 = "{\"tableName\":\"gettotalorderbytoday\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    QueryString.put("RetailerID", Shared_Common_Pref.OutletCode);
                    break;


                case Constants.SR_GetTodayOrder_List:
                    QuerySTring1 = "{\"tableName\":\"getsalesandstockreturn\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Invoice_History.tvStartDate.getText().toString());
                    QueryString.put("todate", Invoice_History.tvEndDate.getText().toString());
                    break;
                case Constants.GetGrn_History:
                    QuerySTring1 = "{\"tableName\":\"getindentdetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put(Constants.SF_Code, Shared_Common_Pref.Sf_Code);
                    QueryString.put("divCode", Shared_Common_Pref.Div_Code);
                    QueryString.put("fromdate", GrnHistory.tvStartDate.getText().toString());
                    QueryString.put("todate", GrnHistory.tvEndDate.getText().toString());
                    break;

                case Constants.GetGrn_Pending_List:
                    QuerySTring1 = "{\"tableName\":\"getgrnindentdetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put(Constants.SF_Code, Shared_Common_Pref.Sf_Code);
                    QueryString.put("billing_doc", Shared_Common_Pref.BillingID);
                    QueryString.put("sales_doc", Shared_Common_Pref.SalesID);
                    break;

                case Constants.GetGrn_List:
                    QuerySTring1 = "{\"tableName\":\"getgrndetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//                    QueryString.put(Constants.SF_Code, Shared_Common_Pref.Sf_Code);
//                    QueryString.put("divCode", Shared_Common_Pref.Div_Code);
                    QueryString.put("fromdate", GrnListActivity.tvStartDate.getText().toString());
                    QueryString.put("todate", GrnListActivity.tvEndDate.getText().toString());
                    QueryString.put("distributorERP",shared_common_pref.getvalue(Constants.DistributorERP));
                    break;

                case Constants.GetGrn_OrderDetails:
                    QuerySTring1 = "{\"tableName\":\"getgrnorderdetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("orderID",Shared_Common_Pref.TransSlNo);
                    QueryString.put(Constants.SF_Code, Shared_Common_Pref.Sf_Code);
                    QueryString.put("divCode", Shared_Common_Pref.Div_Code);
                    break;

                case Constants.GetTodayPrimaryOrder_List:
                    QuerySTring1 = "{\"tableName\":\"gettotalprimaryorderbytoday\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//                    QueryString.put("fromdate", com.hap.checkinproc.Common_Class.Common_Class.GetDatewothouttime());
//                    QueryString.put("todate", com.hap.checkinproc.Common_Class.Common_Class.GetDatewothouttime());
                    QueryString.put("fromdate", TodayPrimOrdActivity.stDate);
                    QueryString.put("todate", TodayPrimOrdActivity.endDate);

                    break;


                case Constants.GetPosOrderHistory:
                    QuerySTring1 = "{\"tableName\":\"gettotalposorderbytoday\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//                    QueryString.put("fromdate", com.hap.checkinproc.Common_Class.Common_Class.GetDatewothouttime());
//                    QueryString.put("todate", com.hap.checkinproc.Common_Class.Common_Class.GetDatewothouttime());
                    QueryString.put("fromdate", PosHistoryActivity.stDate);
                    QueryString.put("todate", PosHistoryActivity.endDate);
                    break;

                case Constants.GetProjectionOrderHistory:
                    QuerySTring1 = "{\"tableName\":\"getprojectionhistory\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", ProjectionHistoryActivity.stDate);
                    QueryString.put("todate", ProjectionHistoryActivity.endDate);
                    break;


                case Constants.Outlet_Total_Orders:
                    QuerySTring1 = "{\"tableName\":\"gettotaloutletorders\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", com.saneforce.milksales.Common_Class.Common_Class.GetDatewothouttime());
                    QueryString.put("todate", com.saneforce.milksales.Common_Class.Common_Class.GetDatewothouttime());
                    break;
                case Constants.TodayOrderDetails_List:
                    QuerySTring1 = "{\"tableName\":\"GettotalOrderDetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    QueryString.put("orderID", Shared_Common_Pref.TransSlNo);
                    break;

                case Constants.GRN_ORDER_DATA:
                    QuerySTring1 = "{\"tableName\":\"getorderdetailsforgrn\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    QueryString.put("orderID", Shared_Common_Pref.TransSlNo);
                    break;
                case Constants.PrePrimaryOrderQty:
                    QuerySTring1 = "{\"tableName\":\"getpreviousorder\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    break;
                case Constants.REPEAT_PRIMARY_ORDER:
                    QuerySTring1 = "{\"tableName\":\"getpreviousordernew\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("GrpCode", "" + Shared_Common_Pref.ORDER_TYPE);

                    break;
                case Constants.PreInvOrderQty:
                    QuerySTring1 = "{\"tableName\":\"getpreviousinvoice\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("RetailerID", Shared_Common_Pref.OutletCode);
                    //  axnname = "getpreviousinvoice";

                    break;
                case Constants.TodayPrimaryOrderDetails_List:
                    //  QuerySTring1 = "{\"tableName\":\"gettotalprimaryorderdetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QuerySTring1 = "{\"tableName\":\"gettotalprimaryorderdetailsnew\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    QueryString.put("orderID", Shared_Common_Pref.TransSlNo);
                    break;

                case Constants.PRIMARY_ORDER_EDIT:
                    QuerySTring1 = "{\"tableName\":\"gettotalprimaryorderdetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    QueryString.put("orderID", Shared_Common_Pref.TransSlNo);
                    break;

                case Constants.ProjectionOrderDetails_List:
                    QuerySTring1 = "{\"tableName\":\"getprojectiondetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    QueryString.put("orderID", Shared_Common_Pref.TransSlNo);
                    break;
                case Constants.PosOrderDetails_List:
                    QuerySTring1 = "{\"tableName\":\"gettotalposorderdetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    QueryString.put("orderID", Shared_Common_Pref.TransSlNo);
                    break;

                case Constants.ComplementaryOrderDetails_List:
                    QuerySTring1 = "{\"tableName\":\"gettotalcomplementaryorderdetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    QueryString.put("orderID", Shared_Common_Pref.TransSlNo);
                    break;

                case Constants.SalesReturnDetailsList:
                    QuerySTring1 = "{\"tableName\":\"get_sales_return_details\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    QueryString.put("orderID", Shared_Common_Pref.TransSlNo);
                    break;

                case Constants.Competitor_List:
                    QuerySTring1 = "{\"tableName\":\"get_compititordetails\"}";
                    break;
                case Constants.Todaydayplanresult:
                    axnname = "Get/dayplanresult";
                    QueryString.put("Date", Common_Class.GetDatewothouttime());
                    break;
                case Constants.Outlet_Total_AlldaysOrders:
                    QuerySTring1 = "{\"tableName\":\"gettotalalldaysoutletorders\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    break;
            }

            QueryString.put("axn", axnname);
            QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
            QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
            QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
            QueryString.put("State_Code", Shared_Common_Pref.StateCode);
            QueryString.put("desig", "stockist");
            QueryString.put(Constants.Distributor_Id, shared_common_pref.getvalue(Constants.Distributor_Id));

            callAPI(QuerySTring1, QueryString, key, activity);
        } else {
            updateUi.onErrorData("Please check your internet connection.");
            Toast.makeText(activity, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }


    }


    void callAPI(String QuerySTring1, Map<String, String> QueryString, String key, Activity activity) {
        try {
            updateUi = ((UpdateResponseUI) activity);
            DatabaseHandler db = new DatabaseHandler(activity);
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            Call<Object> call = service.GetRouteObject(QueryString, QuerySTring1);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        Gson gson = new Gson();

                        if (shared_common_pref == null) shared_common_pref = new Shared_Common_Pref(activity);

                        if (key.equals(Retailer_OutletList)) {
                            shared_common_pref.save(key, gson.toJson(response.body()));
                        } else {
                            db.deleteMasterData(key);
                            db.addMasterData(key, gson.toJson(response.body()));
                        }

                        updateUi = ((UpdateResponseUI) activity);
                        updateUi.onLoadDataUpdateUI(gson.toJson(response.body()), key);

                        String res = response.body().toString();
                        Log.v("Res>>", "" + res);

                    } catch (Exception e) {
                        Log.e("Common class:", key + " response: " + e.getMessage());
                        updateUi = ((UpdateResponseUI) activity);
                        updateUi.onLoadDataUpdateUI(gson.toJson(response.body()), key);

                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    updateUi.onErrorData(t.getMessage());
                    Log.e("api response ex:", t.getMessage());
                }
            });
        } catch (Exception e) {
            updateUi.onErrorData(e.getMessage());
            Log.e("api response ex:", e.getMessage());
        }
    }


    public void getDb_310Data(String key, Activity activity) {
        getDb_310Data(key, activity, null);
    }

    public void getDb_310Data(String key, Activity activity, JsonObject jparam) {
        try {
            if (isNetworkAvailable(activity)) {
                Map<String, String> QueryString = new HashMap<>();
                String axnname = "";
                JSONObject data = new JSONObject();

                UserDetails = activity.getSharedPreferences(UserDetail, Context.MODE_PRIVATE);


                switch (key) {
                    case Constants.PRIMARY_DASHBOARD:
                        axnname = "get/primarydashboardvalues";
                        data.put("login_sfCode", UserDetails.getString("Sfcode", ""));
                        data.put("Dt", Common_Class.GetDatewothouttime());
                        data.put("Grpcode", jparam.get("Grpcode").getAsString());
                        data.put(Constants.LOGIN_TYPE, shared_common_pref.getvalue(Constants.LOGIN_TYPE));
                        break;

                    case Constants.AUDIT_STOCK_ONHAND:
                        axnname = "get/getauditstock";
                        data.put("plant", jparam.get("plant").getAsString());
                        data.put("loc", jparam.get("loc").getAsString());
                        break;

                    case Constants.GroupFilter:
                        axnname = "get/groupfilter";
                        data.put("distributorid", shared_common_pref.getvalue(Constants.LOGIN_TYPE).equalsIgnoreCase(Constants.DISTRIBUTER_TYPE) ?
                                shared_common_pref.getvalue(Constants.Distributor_Id) : "");
                        break;

                    case Constants.WEEKLY_EXPENSE:
                        axnname = "get/weeklyexpense";
                        QueryString.put("sfCode", UserDetails.getString("Sfcode", ""));
                        break;

                    case Constants.SlotTime:
                        axnname = "get/slottimes";
                        data.put("distributorCode", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("GrpCode", jparam.get("GrpCode").getAsString());
                        break;
                    case Constants.COOLER_INFO:
                        axnname = "get/coolerinfo";
                        data.put("retailerCode", Shared_Common_Pref.OutletCode);
                        data.put("dt", Common_Class.GetDatewothouttime());
                        break;
                    case Constants.PLANT_MASTER:
                        axnname = "get/plantmaster";
                        QueryString.put("login_sfCode", UserDetails.getString("Sfcode", ""));
                        QueryString.put("divisionCode", UserDetails.getString("Divcode", "").replaceAll(",", ""));
                        break;
                    case Constants.STOCK_AUDIT_PLANT:
                        axnname = "get/planttype";
//                        QueryString.put("login_sfCode", UserDetails.getString("Sfcode", ""));
//                        QueryString.put("divisionCode", UserDetails.getString("Divcode", "").replaceAll(",", ""));
                        break;
                    case Constants.STOCK_AUDIT_MFSCFA:
                        axnname = "get/mfscfa";
                        QueryString.put("divisionCode", UserDetails.getString("Divcode", ""));
                        QueryString.put("Type", jparam.get("Type").getAsString());
                        break;

                    case Constants.SALES_RETURN:
                        // {"Stk":"","Dt":"","RetID":"","CustomerCode":""}
                        axnname = "get/stockreturn";
                        data.put("Stk", jparam.get("Stk").getAsString());
                        data.put("Dt", jparam.get("Dt").getAsString());
                        data.put("RetID", jparam.get("RetID").getAsString());
                        data.put("CustomerCode", jparam.get("CustomerCode").getAsString());

                        break;
                    case Constants.POS_NETAMT_TAX:
                        axnname = "get/tcstax";
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        break;

                    case Constants.CURRENT_STOCK:
                        axnname = "get/currentstock";
                        QueryString.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        QueryString.put("Dt", Common_Class.GetDatewothouttime());
                        break;


                    case Constants.STOCK_DATA:
                        axnname = "get/stockistledger";
                        data.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        break;
                    case Constants.OUTLET_CATEGORY:
                        axnname = "get/outletcategory";
                        QueryString.put("divisionCode", UserDetails.getString("Divcode", ""));
                        //  data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        break;
                    case Constants.CUSTOMER_DATA:
                        axnname = "get/customerdetails";
                        data.put("customer_code", jparam.get("customer_code").getAsString());
                        data.put("ERP_Code", jparam.get("ERP_Code").getAsString());
                        break;
                    case Constants.UOM:
                        axnname = "get/productuom";
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        break;
                    case Constants.MYTEAM_LOCATION:
                        axnname = "get/newmyteamlocation";
                        data.put("sfcode", jparam.get("sfcode").getAsString());
                        data.put("date", jparam.get("date").getAsString());
                        data.put("lat", jparam.get("lat").getAsString());
                        data.put("lng", jparam.get("lng").getAsString());
                        // data.put("date", "2021-09-09");
                        data.put("type", jparam.get("type").getAsString());
                        data.put(Constants.LOGIN_TYPE, shared_common_pref.getvalue(Constants.LOGIN_TYPE));
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));

                        break;

                    case Constants.DELIVERY_SEQUENCE:
                        axnname = "save/deliverysequence";
                        data.put("RetailerID", jparam.get("RetailerID").getAsString());
                        data.put("SlNo", jparam.get("SlNo").getAsString());
                        break;
                    case Constants.PrimaryTAXList:
                        axnname = "get/primaryproducttaxdetails";
                        data.put("distributorid", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        break;
                    case Constants.SALES_SUMMARY:
                        axnname = "get/salessummarydetails";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("mode", Shared_Common_Pref.SALES_MODE);
                        data.put(Constants.LOGIN_TYPE, shared_common_pref.getvalue(Constants.LOGIN_TYPE));
                        Log.e("status", "SF" + UserDetails.getString("Sfcode", ""));
                        Log.e("status", "Stk" + shared_common_pref.getvalue(Constants.Distributor_Id));
                        Log.e("status", "mode" + Shared_Common_Pref.SALES_MODE);
                        Log.e("status", Constants.LOGIN_TYPE + shared_common_pref.getvalue(Constants.LOGIN_TYPE));
                        break;
                    case Constants.Distributor_List:
                        axnname = "get/distributor";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("div", UserDetails.getString("Divcode", ""));
                        break;
                    case Constants.Freezer_Status:
                        axnname = "get/freezerstatus";
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        break;
                    case Constants.Freezer_capacity:
                        axnname = "get/freezercapacity";
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        break;
                    case Constants.LEDGER:
                        axnname = "get/outletwiseledger";
                        data.put("SF", Shared_Common_Pref.Sf_Code);
                        data.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("FDT", jparam.get("FDate").getAsString());
                        data.put("TDT", jparam.get("TDate").getAsString());
                        break;
                    case Constants.QPS_STATUS:
                        axnname = "get/qpsentrystatus";
                        data.put("retailerCode", Shared_Common_Pref.OutletCode);
                        break;
                    case Constants.QPS_HAPBRAND:
                        data.put("retailorCode", Shared_Common_Pref.OutletCode);
                        axnname = "get/qpshaplitres";
                        break;
                    case Constants.QPS_COMBO:
                        axnname = "get/qpsallocation";
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        data.put("sfCode", Shared_Common_Pref.Sf_Code);
                        data.put("retailorCode", Shared_Common_Pref.OutletCode);
                        data.put("distributorcode", shared_common_pref.getvalue(Constants.Distributor_Id));
                        break;
                    case Rout_List:
                        data.put("Stk", shared_common_pref.getvalue(Constants.TEMP_DISTRIBUTOR_ID));
                        axnname = "get/routelist";
                        break;
                    case Constants.HistoryData:
                        axnname = "get/orderandinvoice";
                        data.put("distributorid", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("fdt", HistoryInfoActivity.stDate);
                        data.put("tdt", HistoryInfoActivity.endDate);
                        break;
                    case Constants.FlightBookingStatus:
                        axnname = "get/flightbookings";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("FDT", jparam.get("FDT").getAsString());
                        data.put("TDT", jparam.get("TDT").getAsString());
                        break;
                    case Constants.FlightBookingPending:
                        axnname = "get/flightpbookings";
                        data.put("SF", UserDetails.getString("Sfcode", ""));

                        break;
                    case Constants.FlightBookingApprovalHistory:
                        axnname = "get/fbapprhist";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("FDT", jparam.get("FDT").getAsString());
                        data.put("TDT", jparam.get("TDT").getAsString());

                        break;
                    case Constants.DASHBOARD_TYPE_INFO:
                        axnname = "get/orderandinvoice";
                        data.put("distributorid", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("fdt", Common_Class.GetDatewothouttime());
                        data.put("tdt", Common_Class.GetDatewothouttime());
                        break;
                    case Constants.RETAILER_STATUS:
                        //axnname = "get/retailerorderstatus";
                        axnname = "get/retailerorderstatusch";
                        data.put("distname", shared_common_pref.getvalue(Constants.Distributor_Id));
                        break;

//                    case Constants.DIST_STOCK:
//                        axnname = "get/diststock";
//                        data.put("distid", shared_common_pref.getvalue(Constants.Distributor_Id));
//                        data.put("dt", Common_Class.GetDatewothouttime());
//                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
//                        data.put("sfCode", UserDetails.getString("Sfcode", ""));
//                        break;
                    case Constants.PAYMODES:
                        axnname = "get/paymenttype";
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        break;

                    case Constants.POP_MATERIAL:
                        axnname = "get/popmaster";
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        data.put("sfCode", UserDetails.getString("Sfcode", ""));
                        data.put("retailorCode", Shared_Common_Pref.OutletCode);
                        data.put("distributorcode", shared_common_pref.getvalue(Constants.Distributor_Id));
                        break;

                    case Constants.OUTSTANDING:
                        axnname = "get/customeroutstanding";
                        data.put("retailerCode", Shared_Common_Pref.OutletCode);
                        data.put("distributorcode", shared_common_pref.getvalue(Constants.Distributor_Id));
                        break;

                    case Constants.POP_ENTRY_STATUS:
                        axnname = "get/popentrystatus";
                        data.put("retailerCode", Shared_Common_Pref.OutletCode);
                        break;

                    case Constants.TAXList:
                        axnname = "get/producttaxdetails";
                        data.put("distributorid", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        data.put("retailorId", Shared_Common_Pref.OutletCode);
                        break;

                    case Constants.POS_TAXList:
                        axnname = "get/posproducttaxdetails";
                        data.put("distributorid", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        data.put("retailorId", Shared_Common_Pref.OutletCode);
                        break;


                    case Constants.FreeSchemeDiscList:
                        axnname = "get/secondaryscheme";
                        data.put("sfCode", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        data.put("outletId", Shared_Common_Pref.OutletCode);
                        break;
                    case Constants.POS_SCHEME:
                        axnname = "get/posscheme";
                        data.put("sfCode", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        break;
                    case Constants.PRIMARY_SCHEME:
                        axnname = "get/primaryscheme";
                        data.put("sfCode", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("divisionCode", UserDetails.getString("Divcode", ""));
                        break;
                    case Constants.PreOrderQtyList:
                        axnname = "get/prevorderqty";
                        data.put("retailorCode", Shared_Common_Pref.OutletCode);
                        data.put("sfCode", Shared_Common_Pref.Sf_Code);
                        break;
//                    case Constants.PrePrimaryOrderQtyList:
//                        axnname = "get/prevorderqty";
//                        data.put("distributorid", Shared_Common_Pref.OutletCode);
//                        data.put("sfCode", Shared_Common_Pref.Sf_Code);
//                        break;
                    case Constants.CUMULATIVEDATA:
                        axnname = "get/cumulativevalues";
                        data.put("sfCode", Shared_Common_Pref.Sf_Code);
                        data.put("divCode", UserDetails.getString("Divcode", ""));
                        data.put("dt", sfa_date);
                        data.put(Constants.LOGIN_TYPE, shared_common_pref.getvalue(Constants.LOGIN_TYPE));
                        break;
                    case Constants.SERVICEOUTLET:
                        axnname = "get/serviceoutletsummary";
                        data.put("sfCode", Shared_Common_Pref.Sf_Code);
                        data.put("divCode", UserDetails.getString("Divcode", ""));
                        data.put("dt", sfa_date);
                        data.put(Constants.LOGIN_TYPE, shared_common_pref.getvalue(Constants.LOGIN_TYPE));
                        break;
                    case Constants.OUTLET_SUMMARY:
                        axnname = "get/outletsummary";
                        data.put("sfCode", Shared_Common_Pref.Sf_Code);
                        data.put("divCode", UserDetails.getString("Divcode", ""));
                        data.put("dt", sfa_date);
                        data.put(Constants.LOGIN_TYPE, shared_common_pref.getvalue(Constants.LOGIN_TYPE));
                        break;
                    case Constants.SFA_DASHBOARD:
                        axnname = "get/channelwiseoutletsummary";
                        data.put("sfCode", Shared_Common_Pref.Sf_Code);
                        data.put("divCode", UserDetails.getString("Divcode", ""));
                        data.put("dt", sfa_date);
                        data.put(Constants.LOGIN_TYPE, shared_common_pref.getvalue(Constants.LOGIN_TYPE));
                        break;
                    case Constants.STATE_LIST:
                        axnname = "get/states";
                        data.put("sfCode", Shared_Common_Pref.Sf_Code);
                        break;
                    case Constants.Category_List:
                        axnname = "get/prodCate";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("div", UserDetails.getString("Divcode", ""));
                        break;
                    case Constants.Product_List:
                        axnname = "get/prodDets";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("div", UserDetails.getString("Divcode", ""));
                        data.put("outletId", "");
                        break;
                    case Constants.Primary_Shortage_List:
                        axnname = "get/prodprishortage";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("div", UserDetails.getString("Divcode", ""));
                        break;
                    case Constants.PaymentMethod:
                        axnname = "get_payment_method";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("div", UserDetails.getString("Divcode", ""));
                        break;
                    case Constants.Primary_Product_List:
                        axnname = "get/prodprimarydets";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("div", UserDetails.getString("Divcode", ""));
                        break;
                    case Constants.POS_Product_List:
                        axnname = "get/posproddets";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("div", UserDetails.getString("Divcode", ""));
                        break;

                    case Constants.STOCK_LEDGER:
                        //dist stock for stock loading
                        axnname = "get/stockistledger";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("div", UserDetails.getString("Divcode", ""));
                        data.put("dt", Common_Class.GetDatewothouttime());
                        break;

                    case Constants.VAN_STOCK:
                        //dist stock for stock loading
                        axnname = "get/vanstock";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("div", UserDetails.getString("Divcode", ""));
                        data.put("dt", Common_Class.GetDatewothouttime());
                        data.put("fromdate", VanStockViewActivity.stDate);
                        data.put("todate", VanStockViewActivity.endDate);
                        break;
                    case Constants.VAN_STOCK_DTWS:
                        //dist stock for stock loading
                        axnname = "get/vanstockleg";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("div", UserDetails.getString("Divcode", ""));
                        data.put("dt", Common_Class.GetDatewothouttime());
                        data.put("fromdate", VanStockViewActivity.stDate);
                        data.put("todate", VanStockViewActivity.endDate);
                        break;

                    case Constants.POS_ENTRY_LIST:
                        //dist stock for stock loading
                        axnname = "get/posEntryList";
                        data.put("SF", UserDetails.getString("Sfcode", ""));
                        data.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("div", UserDetails.getString("Divcode", ""));
                        data.put("dt", Common_Class.GetDatewothouttime());
                      /*  data.put("fromdate", POSViewEntryActivity.stDate);
                        data.put("todate", POSViewEntryActivity.endDate);*/
                        break;

                    case Constants.POS_Category_EntryList:
                        axnname = "get/poscat";
                        data.put("fromdate", VanStockViewActivity.stDate);
                        data.put("todate", VanStockViewActivity.endDate);
                        break;

                }

                QueryString.put("axn", axnname);
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                Call<ResponseBody> call = service.GetRouteObject310(QueryString, data.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>" + key + ": ", is.toString());
                                }


                                shared_common_pref.save(key, is.toString());
                                updateUi = ((UpdateResponseUI) activity);
                                updateUi.onLoadDataUpdateUI(is.toString(), key);

                                if (key.equals(Constants.Distributor_List)) {
                                    setDefDist();
                                }

                                if (key.equals(Constants.Product_List)) {
                                    DatabaseHandler db = new DatabaseHandler(activity);

                                    db.deleteMasterData(Constants.Product_List);
                                    db.addMasterData(Constants.Product_List, is.toString());
                                }
                            }

                        } catch (Exception e) {

                            Log.v("fail>>1", e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>2", t.toString());


                    }
                });
            } else {
                showMsg(activity, "Please check your internet connection.");
            }
        } catch (Exception e) {
            Log.v("common_api:310:EX:", e.getMessage());
        }

    }

    public void setDefDist() {
        try {
            if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.Distributor_Id))) {
                JSONArray jsonArray = new JSONArray(shared_common_pref.getvalue(Constants.Distributor_List));
                Log.v("distList:", jsonArray.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    shared_common_pref.save(Constants.Distributor_name, jsonObject1.optString("name"));
                    shared_common_pref.save(Constants.Distributor_Id, String.valueOf(jsonObject1.optInt("id")));
                    shared_common_pref.save(Constants.DistributorERP, jsonObject1.optString("ERP_Code"));
                    shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, String.valueOf(jsonObject1.optInt("id")));
                    shared_common_pref.save(Constants.Distributor_phone, jsonObject1.optString("Mobile"));
                    shared_common_pref.save(Constants.DivERP, jsonObject1.optString("DivERP"));
                    shared_common_pref.save(Constants.CusSubGrpErp, jsonObject1.getString("CusSubGrpErp"));
                    shared_common_pref.save(Constants.DistributorGst, jsonObject1.optString("GSTN"));
                    shared_common_pref.save(Constants.DistributorFSSAI, jsonObject1.optString("FSSAI"));
                    shared_common_pref.save(Constants.RSM_CUTOFF_TIME,jsonObject1.optString("SFCutoff"));
                    getDataFromApi(Retailer_OutletList, activity, false);
                    break;
                }


            }
        } catch (Exception e) {

        }

    }


    public void getProductDetails(Activity activity) {
        getProductDetails(activity,null);
    }
    public void getProductDetails(Activity activity, OnLiveUpdateListener liveUpdateListener) {

        if (isNetworkAvailable(activity)) {
            UserDetails = activity.getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

            DatabaseHandler db = new DatabaseHandler(activity);
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("SF", UserDetails.getString("Sfcode", ""));
                jParam.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                jParam.put("outletId", Shared_Common_Pref.OutletCode);
                jParam.put("div", UserDetails.getString("Divcode", ""));
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                service.getDataArrayList("get/prodGroup", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        // Log.v(TAG, response.toString());
                        db.deleteMasterData(Constants.ProdGroups_List);
                        db.addMasterData(Constants.ProdGroups_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                service.getDataArrayList("get/prodTypes", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        db.deleteMasterData(Constants.ProdTypes_List);
                        db.addMasterData(Constants.ProdTypes_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                service.getDataArrayList("get/prodCate", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        db.deleteMasterData(Constants.Category_List);
                        db.addMasterData(Constants.Category_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                if (!Shared_Common_Pref.SFA_MENU.equalsIgnoreCase("VanSalesDashboardRoute")) {
                    ProductsLoaded=false;
                    service.getDataArrayList("get/prodDets", jParam.toString()).enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                            Log.v("SEC_Product_List", response.body().toString());
                            db.deleteMasterData(Constants.Product_List);
                            db.addMasterData(Constants.Product_List, response.body());
                            ProductsLoaded=true;
                            if(liveUpdateListener!=null) liveUpdateListener.onUpdate("");
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            if(liveUpdateListener!=null) liveUpdateListener.onError(t.getLocalizedMessage());
                        }
                    });
                }else{
                    if(liveUpdateListener!=null) liveUpdateListener.onError("Product Loading Failed");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if(liveUpdateListener!=null) liveUpdateListener.onError(e.getLocalizedMessage());
            }
        }else{
            if(liveUpdateListener!=null) liveUpdateListener.onError("Internet Not Available");
        }

    }
    public void getPOSStockProduct(Activity activity, OnLiveUpdateListener liveUpdateListener) {

        if (isNetworkAvailable(activity)) {
            UserDetails = activity.getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

            DatabaseHandler db = new DatabaseHandler(activity);
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("SF", UserDetails.getString("Sfcode", ""));
                jParam.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                jParam.put("outletId", Shared_Common_Pref.OutletCode);
                jParam.put("div", UserDetails.getString("Divcode", ""));
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                ProductsLoaded=false;
                service.getDataArrayList("get/prodstockdets", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                            Log.v("SEC_Product_List", response.body().toString());
                        db.deleteMasterData(Constants.ProductStock_List);
                        db.addMasterData(Constants.ProductStock_List, response.body());
                        ProductsLoaded=true;
                        if(liveUpdateListener!=null) liveUpdateListener.onUpdate("");
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        if(liveUpdateListener!=null) liveUpdateListener.onUpdate("");
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
                if(liveUpdateListener!=null) liveUpdateListener.onUpdate("");
            }
        }else{
            if(liveUpdateListener!=null) liveUpdateListener.onUpdate("");
        }

    }


    public void getPOSProduct(Activity activity) {
        getPOSProduct(activity, null);
    }
    public void getPOSProduct(Activity activity, OnLiveUpdateListener liveUpdateListener) {

        if (isNetworkAvailable(activity)) {

            UserDetails = activity.getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

            DatabaseHandler db = new DatabaseHandler(activity);
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("SF", UserDetails.getString("Sfcode", ""));
                jParam.put("Stk", shared_common_pref.getvalue(Constants.Distributor_Id));
                jParam.put("div", UserDetails.getString("Divcode", ""));
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                service.getDataArrayList("get/posprodgroup", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        // Log.v(TAG, response.toString());
                        db.deleteMasterData(Constants.POS_ProdGroups_List);
                        db.addMasterData(Constants.POS_ProdGroups_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                service.getDataArrayList("get/posprodtypes", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        db.deleteMasterData(Constants.POS_ProdTypes_List);
                        db.addMasterData(Constants.POS_ProdTypes_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                service.getDataArrayList("get/posprodcate", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        db.deleteMasterData(Constants.POS_Category_List);
                        db.addMasterData(Constants.POS_Category_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });

                ProductsLoaded=false;
                service.getDataArrayList("get/posproddets", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("POS:", response.body().toString());
                        db.deleteMasterData(Constants.POS_Product_List);
                        db.addMasterData(Constants.POS_Product_List, response.body());
                        ProductsLoaded=true;
                        if(liveUpdateListener!=null) liveUpdateListener.onUpdate("");
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void showMsg(Activity activity, String msg) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public boolean checkDates(String stDate, String endDate, Activity activity) {
        boolean b = false;
        try {
            SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = dfDate.parse(stDate);
            Date date2 = dfDate.parse(endDate);
            long diff = date2.getTime() - date1.getTime();
            System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) <= 90) {
                if (dfDate.parse(stDate).before(dfDate.parse(endDate))) {
                    b = true;//If start date is before end date
                } else if (dfDate.parse(stDate).equals(dfDate.parse(endDate))) {
                    b = true;//If two dates are equal
                } else {
                    b = false; //If start date is after the end date
                }

            } else {
                Toast.makeText(activity, "You can see only minimum 3 Months records", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return b;
    }

    public void showCalDialog(Activity activity, String msg, String num) {
        AlertDialogBox.showDialog(activity, HAPApp.Title, msg, "Yes", "No", false, new AlertBox() {
            @Override
            public void PositiveMethod(DialogInterface dialog, int id) {
                int readReq = ContextCompat.checkSelfPermission(activity, CALL_PHONE);
                if (readReq != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HAPApp.activeActivity, new String[]{CALL_PHONE}, 1001);
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + num));//change the number
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(callIntent);
                }
            }

            @Override
            public void NegativeMethod(DialogInterface dialog, int id) {

            }
        });
    }

    public void showCalDialog(Context activity, String msg, String num) {
        AlertDialogBox.showDialog(activity, HAPApp.Title, msg, "Yes", "No", false, new AlertBox() {
            @Override
            public void PositiveMethod(DialogInterface dialog, int id) {
                //callMob(activity, num);
                int readReq = ContextCompat.checkSelfPermission(activity, CALL_PHONE);
                if (readReq != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HAPApp.activeActivity, new String[]{CALL_PHONE}, 1001);
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + num));//change the number
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(callIntent);
                }
            }

            @Override
            public void NegativeMethod(DialogInterface dialog, int id) {

            }
        });
    }


    public String datePicker(Activity activity, TextView view) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                pickDate = ("" + dayOfMonth + "/" + month + "/" + year);


            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();

        return pickDate;
    }

    public void commonDialog(Activity activity, Class moveActivity, String name) {
        AlertDialogBox.showDialog(activity, HAPApp.Title, "Do you confirm to cancel " + name,
                "Yes", "No", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        if (name.equalsIgnoreCase("GRN?"))
                            activity.finish();
                        else if (name.equalsIgnoreCase("Complementary Invoice?")) {
                            shared_common_pref.clear_pref(Constants.LOC_INVOICE_DATA);
                            activity.finish();
                        } else {
                            CommonIntentwithFinish(moveActivity);
                        }


                        switch (name) {
                            case "Primary Order?":
                                shared_common_pref.clear_pref(Constants.LOC_PRIMARY_DATA);
                                break;
                            case "Order?":
                                shared_common_pref.clear_pref(Constants.LOC_SECONDARY_DATA);
                                break;
                            case "POS?":
                                shared_common_pref.clear_pref(Constants.LOC_POS_DATA);
                                break;
                            case "Invoice?":
                                shared_common_pref.clear_pref(Constants.LOC_INVOICE_DATA);
                                break;
                            case "Van Sales?":
                                shared_common_pref.clear_pref(Constants.LOC_VANSALES_DATA);
                                break;
                        }
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {

                    }
                });
    }

    public void showCommonDialog(List<Common_Model> dataList, int type, Activity activity) {
        customDialog = new CustomListViewDialog(activity, dataList, type);
        Window windowww = customDialog.getWindow();
        windowww.setGravity(Gravity.CENTER);
        windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }

    public void dismissCommonDialog(int type) {
        if (customDialog != null) {
            customDialog.dismiss();

            if (type == 2) {
                shared_common_pref.clear_pref(Constants.LOC_PRIMARY_DATA);
                shared_common_pref.clear_pref(Constants.LOC_SECONDARY_DATA);
                shared_common_pref.clear_pref(Constants.LOC_POS_DATA);
                shared_common_pref.clear_pref(Constants.LOC_INVOICE_DATA);
            }
        }

    }

    public String getDirectionsUrl(String dest, Activity activity) {
        // Origin of route
        String str_origin = "origin=" + Shared_Common_Pref.Outletlat + "," + Shared_Common_Pref.Outletlong;
        // Destination of route
        String str_dest = "destination=" + dest;
        // Key
        String key = "key=" + activity.getString(R.string.map_api_key);
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key;
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
        return url;
    }

//    public boolean checkValueStore(Activity activity, String key) {
//        DatabaseHandler db = new DatabaseHandler(activity);
//
//        try {
//            JSONArray storeData = db.getMasterData(key);
//            if (storeData != null && storeData.length() > 0)
//                return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
   /* public void Reurnypeface(class cl,){
        userType = new TypeToken<ArrayList<Work_Type_Model>>() {
        }.getType();
        worktypelist = gson.fromJson(new Gson().toJson(noticeArrayList), userType);

    }*/


//    public void CustomerMe(final Context context_) {
//        this.context = context_;
//        shared_common_pref = new Shared_Common_Pref(activity);
//        gson = new Gson();
//        apiService = ApiClient.getClient().create(ApiInterface.class);
//        Type type = new TypeToken<List<CustomerMe>>() {
//        }.getType();
//        System.out.println("TYPETOKEN_LIST" + type);
//        CustomerMeList = gson.fromJson(shared_common_pref.getvalue(Shared_Common_Pref.cards_pref), type);
//        JSONObject paramObject = new JSONObject();
//        try {
//            paramObject.put("name","dd");
//            paramObject.put("password","sddfdf");
//
//            Call<JsonObject> Callto = apiService.LoginJSON(paramObject.toString());
//            Callto.enqueue(CheckUser);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            System.out.println("JSON Expections" + paramObject.toString());
//
//        }
//
//
//    }


    /* public void showToastMSG(Activity Ac, String MSg, int s) {
         TastyToast.makeText(Ac, MSg,
                 TastyToast.LENGTH_SHORT, s);
     }*/
    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

    public void CommonIntentwithNEwTask(Class classname) {
        intent = new Intent(activity, classname);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static String GetEkey() {
        DateFormat dateformet = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calander = Calendar.getInstance();
        return "EK" + Shared_Common_Pref.Sf_Code + dateformet.format(calander.getTime()).hashCode();

    }

    public void hideKeybaord(View v, Context context) {
        this.context = context;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    public List<Common_Model> getDistList() {
        try {
            List<Common_Model> distributor_master = new ArrayList<>();
            Common_Model Model_Pojo;
            JSONArray jsonArray = new JSONArray(shared_common_pref.getvalue(Constants.Distributor_List));
            Log.v("distList:", jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("FWFlg");
                String Add2 = jsonObject1.optString("Addr2");
                String Mob = jsonObject1.optString("Mobile");
                String ERP_Code = jsonObject1.optString("ERP_Code");
                String DivERP = jsonObject1.optString("DivERP");
                String DisGst = jsonObject1.optString("GSTN");
                String DisFssai = jsonObject1.optString("FSSAI");
                Model_Pojo = new Common_Model(name, id, flag, Add2, Mob, ERP_Code, DivERP,DisGst,DisFssai, jsonObject1.optString("Latlong"), jsonObject1.getString("CusSubGrpErp"));
                distributor_master.add(Model_Pojo);

            }
            return distributor_master;
        } catch (Exception e) {

        }
        return null;
    }

    public static String addquote(String s) {
        return new StringBuilder()
                .append('\'')
                .append(s)
                .append('\'')
                .toString();
    }

    public String GetMonthname(int s) {
        String[] montharray = activity.getResources().getStringArray(R.array.MonthArray);
        Calendar cal = Calendar.getInstance();
        if (s == 12) {
            s = 0;

        }
        String currrentmonth = montharray[s];
        return currrentmonth;
    }

    public void CommonIntentwithoutFinishputextra(Class classname, String key, String value) {
        intent = new Intent(activity, classname);
        intent.putExtra(key, value);
        Log.e("commanclasstitle", value);
        activity.startActivity(intent);
    }

    public void CommonIntentwithoutFinishputextratwo(Class classname, String key, String
            value, String key2, String value2) {
        intent = new Intent(activity, classname);
        intent.putExtra(key, value);
        intent.putExtra(key2, value2);
        Log.e("commanclasstitle", value);
        activity.startActivity(intent);
    }

    public String getintentValues(String name) {
        Intent intent = activity.getIntent();
        return intent.getStringExtra(name);
    }

    public static String GetDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dpln = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String plantime = dpln.format(c.getTime());
        return plantime;
    }

    public static String GetTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dpln = new SimpleDateFormat("HH:mm:ss");
        String plantime = dpln.format(c.getTime());
        return plantime;
    }

    public static String GetRunTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dpln = new SimpleDateFormat("HH:mm:ss aaa");
        String plantime = dpln.format(c.getTime());
        return plantime;
    }

    public void GetTP_Result(String name, String values, int Month, int year) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        try {
            jsonObject.put(name, sp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.GetResponseBody(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, String.valueOf(Month), String.valueOf(year), jsonArray.toString());
        Log.e("Log_Tp_SELECT", jsonArray.toString());
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    Result = jsonObject.getString("success");
                    Toast.makeText(activity, "Send to Approval", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void GetTP_Result(String name, String values, String Month, String year) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        try {
            jsonObject.put(name, sp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.GetResponseBody(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, String.valueOf(Month), String.valueOf(year), jsonArray.toString());

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    Result = jsonObject.getString("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void clearLocData(Activity activity) {
        Shared_Common_Pref sharedCommonPref = new Shared_Common_Pref(activity);
        sharedCommonPref.clear_pref(Constants.STATE_LIST);
        sharedCommonPref.clear_pref(Constants.RETAIL_CHANNEL);
        sharedCommonPref.clear_pref(Constants.RETAIL_CLASS);
        sharedCommonPref.clear_pref(Constants.Freezer_Status);
        sharedCommonPref.clear_pref(Constants.Freezer_capacity);
    }


    public static class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }


        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }

    }


    public void gotoHomeScreen(Context context, View ivToolbarHome) {
        ivToolbarHome.setOnClickListener(v -> {
            SharedPreferences CheckInDetails = context.getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
            boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
            Intent intent;
            if (shared_common_pref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
                intent = new Intent(context, SFA_Activity.class);
            } else if (CheckIn) {
                intent = new Intent(context, Dashboard_Two.class);
                intent.putExtra("Mode", "CIN");
            } else {
                intent = new Intent(context, Dashboard.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    public void gotoProcurementDashboardScreen(Context context, View ivToolbarHome) {
        ivToolbarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences CheckInDetails = context.getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                if (CheckIn == true) {
                    CommonIntentwithoutFinish(ProcurementDashboardActivity.class);
                } else
                    context.startActivity(new Intent(context, Dashboard.class));

            }
        });


    }

    public String formatCurrency(double amount) {
        return (HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(amount));
    }

    public double formatDecimalToSingleDecimal(double amount) {
        return Double.parseDouble(new DecimalFormat("0.0").format(amount));
    }

    public double formatDecimalToTwoDecimal(double amount) {
        return Double.parseDouble(new DecimalFormat("0.00").format(amount));
    }

    public static Address getAddressFromLatLong(Context context, double LATITUDE, double LONGITUDE) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                return addresses.get(0);
            }
        } catch (Exception e) {
            Toast.makeText(context, "Map Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public static void uploadToS3Bucket(Context context, String mFilePath, String FileName, String companyCode, String Mode) {
        try{
            Util util = new Util();
            TransferUtility transferUtility = util.getTransferUtility(context);
            File file;
            if (mFilePath.contains(".png") || mFilePath.contains(".jpg") || mFilePath.contains(".jpeg")) {
                file = new Compressor(context).compressToFile(new File(mFilePath));
            } else {
                file = new File(mFilePath);
            }
            TransferObserver uploadObserver = transferUtility.upload("godairy",companyCode + "/" + Mode + "/" + FileName , file);
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            uploadObserver.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                    } else if (TransferState.FAILED == state) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int) percentDonef;
                    progressDialog.setMessage("Uploading... (" + percentDone + "%)");
                }

                @Override
                public void onError(int id, Exception ex) {
                    ex.printStackTrace();
                }

            });
        }
        catch (Exception e){
            Log.e("exjhbdbf",e.getMessage());
        }
    }

    public void getImageFromS3Bucket(Context context, String companyCode, String FileName,String Mode, OnDownloadImage onDownloadImage) {
        try{
            String[] names = FileName.split("\\.");
            String extension = names[names.length - 1];
            final File file = File.createTempFile(FileName, extension);
            Util util = new Util();
            TransferUtility transferUtility = util.getTransferUtility(context);
            TransferObserver downloadObserver = transferUtility.download("godairy",companyCode + "/" + Mode + "/" + FileName, file);
            downloadObserver.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        Bitmap bmp= BitmapFactory.decodeFile(file.getAbsolutePath());
                        if (onDownloadImage != null) {
                            onDownloadImage.onDownload(bmp, file.getAbsolutePath());
                        }
                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int) percentDonef;
                }

                @Override
                public void onError(int id, Exception ex) {

                }
            });
        }
        catch (Exception e){

        }
    }

    public interface OnDownloadImage {
        void onDownload(Bitmap bmp, String path);
    }

    @SuppressLint("ResourceType")
    public static void ShowNoDataFound(Activity activity) {
        ViewGroup rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        try {
            LinearLayout ll = rootView.findViewById(987654);
            if (ll.getVisibility() == View.VISIBLE) {
                rootView.removeView(ll);
            }
        } catch (Exception ignored) { }
        TextView noDataTextView = new TextView(activity);
        noDataTextView.setText("No Data Found");
        noDataTextView.setTextSize(14);
        noDataTextView.setTextColor(ContextCompat.getColor(activity, android.R.color.black));
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setId(987654);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(noDataTextView);
        rootView.addView(linearLayout);
    }

    @SuppressLint("ResourceType")
    public static void hideNoDataFound(Activity activity) {
        ViewGroup rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        try {
            LinearLayout ll = rootView.findViewById(987654);
            if (ll.getVisibility() == View.VISIBLE) {
                rootView.removeView(ll);
            }
        } catch (Exception ignored) { }
    }

    public static void makeApiCall(Map<String, String> params, String data, APIResult listener) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getUniversalData(params, data);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("success")) {
                            listener.onSuccess(jsonObject);
                        } else {
                            listener.onFailure(jsonObject.getString("msg"));
                        }
                    } catch (Exception e) {
                        listener.onFailure(e.getLocalizedMessage());
                    }
                } else {
                    listener.onFailure("Something went wrong");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                listener.onFailure(t.getLocalizedMessage());
            }
        });
    }
}

