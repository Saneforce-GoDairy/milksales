package com.saneforce.godairy.Common_Class;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Shared_Common_Pref {
    public static String ORDER_TYPE = "orderType";
    public static String SFCutoff = "SFCutoff";
    public static String SFA_MENU = "";
    public static String CUSTOMER_CODE = "";
    public static String SALES_MODE = "";
    public static String LOGINTYPE = "";
    public static int Projection_Approval = 0;
    public static String ImageUKey = "";
    SharedPreferences Common_pref;
    SharedPreferences.Editor editor;
    Activity activity;
    Context _context;
    public static final String spName = "SP_LOGIN_DETAILS";
    public static String Sf_Code = "Sf_Code";
    public static String mOldPassword = "mOldPassword";
    public static String mNewPasswpord = "mNewPasswpord";
    public static String Reporting_Sf_Code = "Reporting_To_SF";
    public static String Profile = "Profile";
    public static String Div_Code = "Div_Code";
    public static String StateCode = "StateCode";
    public static String Sf_Name = "Sf_Name";
    public static String SF_Mobile = "SF_Mobile";
    public static String Dv_ID = "DvID";
    public static String SF_Type = "SF_Type";
    public static String CHECK_COUNT = "0";
    public static String SF_EMP_ID = "sf_emp_id";
    public static String SF_DESIG = "sf_Designation_Short_Name";
    public static String SF_DEPT = "DeptName";
    public static String DAMode = "DAMode";
    public static Double Outletlat;
    public static Double Outletlong;
    public static String OutletAddress;
    public static String Outler_AddFlag = "";
    public static String FromActivity = "";
    public static String OutletName = "OutletName";
    public static String OutletCode = "OutletCode";
    public static String SecOrdOutletType = "";
    public static String Freezer_Required = "";
    public static String DistributorGst = "GSTN";
    public static int TravelAllowance = 0;
    public static String Dept_Type = "Dept_Type";
    public static String Sync_Flag;
    public static String name = "name";
    public static String Status = "status";
    public static int TotalCountApproval = 0;
    public static String TransSlNo;
    public static String BillingID;
    public static String SalesID;
    public static String Outlet_Info_Flag;
    public static String Invoicetoorder;
    public static String OutletAvail = "OutletAvail";
    public static String OutletUniv = "OutletUniv";
    public static String Rout_List = "Rout_List";
    public static String Category_List = "Category_List";
    public static String Todaydayplanresult = "Todaydayplanresult";
    public static String Route_Code = "Route_Code";
    public static String Route_name = "Route_name";
    public static String Editoutletflag = "Editoutletflag";
    public static String Tp_Approvalflag = "Tp_Approvalflag";
    public static String Tp_Sf_name = "Tp_Sf_name";
    public static String Tp_Monthname = "Tp_Monthname";
    public static String Tp_SFCode = "Tp_SFCode";
    public static String DCRMode = "DCRMode";
    public static int MaxKm = 1000;
    public static String BASE_URL = "base_url";

    public Shared_Common_Pref(Activity Ac) {
        activity = Ac;
        if (activity != null) {
            Common_pref = activity.getSharedPreferences("Preference_values", Context.MODE_PRIVATE);
            editor = Common_pref.edit();
        }
    }

    public Shared_Common_Pref(Context cc) {
        this._context = cc;
        Common_pref = cc.getSharedPreferences("Preference_values", Context.MODE_PRIVATE);
        editor = Common_pref.edit();
    }

    public String save(String key, String value) {
        editor.putString(key, value);
        editor.commit();
        return key;
    }

    public void save(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void save(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }


    public String getvalue(String key, String defValue) {
        if (defValue == null) defValue = "";
        String text = Common_pref.getString(key, defValue);
        return text;
    }

    public String getvalue(String key) {
        String text = Common_pref.getString(key, "");
        return text;
    }

    public Boolean getBoolValue(String Key) {
        Boolean val = false;
        val = Common_pref.getBoolean(Key, false);
        return val;
    }

    public int getIntValue(String Key) {
        int val = Common_pref.getInt(Key, -1);
        return val;
    }

    public void clearAll() {
        Common_pref.edit().clear().apply();
    }

    public void clear_pref(String key) {
        try {
            Common_pref.edit().remove(key).apply();
        } catch (Exception e) {
            Log.d("Error Loading:",e.getMessage().toString());
        }
        //the good quality product by the end of the day worth od manual  developement in this quality regaurds minimum qu.
    }
}
