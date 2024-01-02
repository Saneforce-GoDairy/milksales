package com.saneforce.godairy.SFA_Activity;

import static android.Manifest.permission.CALL_PHONE;
import static com.saneforce.godairy.Common_Class.Constants.Retailer_OutletList;
import static com.saneforce.godairy.Common_Class.Constants.Rout_List;
import static com.saneforce.godairy.Common_Class.Constants.Route_Id;
import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;
import static com.saneforce.godairy.SFA_Activity.HAPApp.getActiveActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Interface.OnLiveUpdateListener;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.PushNotification.MyFirebaseMessagingService;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.OutletCategoryFilterAdapter;
import com.saneforce.godairy.SFA_Adapter.OutletMasterCategoryFilterAdapter;
import com.saneforce.godairy.SFA_Adapter.Route_View_Adapter;
import com.saneforce.godairy.SFA_Model_Class.OutletReport_View_Modal;
import com.saneforce.godairy.SFA_Model_Class.Retailer_Modal_List;
import com.saneforce.godairy.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard_Route extends AppCompatActivity implements View.OnClickListener, Master_Interface, UpdateResponseUI {
    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;
    public static Dashboard_Route dashboard_route;
    public static Common_Class common_class;
    public static Shared_Common_Pref shared_common_pref;
    public TextView distributor_text;
    List<Retailer_Modal_List> Retailer_Modal_List = new ArrayList<>();
    List<Retailer_Modal_List> Retailer_Modal_ListFilter = new ArrayList<>();
    List<OutletReport_View_Modal> Retailer_Order_List;
    Gson gson;
    Type userTypeRetailor, userTypeReport;
    TextView headtext, textViewname, ReachedOutlet, route_text, txtOrdDate, OvrAll,
            txSrvOtlt, txTotUniOtlt, txUniOtlt, txClsOtlt, txSrvOtltCnt, txTotUniOtltCnt,
            txUniOtltCnt, txClsOtltCnt, smryOrd, smryNOrd, smryNOOrd, smryInv, smryInvVal,
            tvDistributor, PendingCount, PendingText;
    EditText txSearchRet;
    LinearLayout btnCmbRoute, btTotUniOtlt, btSrvOtlt, btUniOtlt, btClsOtlt, undrUni, undrCls, undrServ, underTotUni, llPen;
    Common_Model Model_Pojo;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    String sDeptType, RetType = "";
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    DatabaseHandler db;
    ImageView ivToolbarHome, ivBtnRpt,btnFilter;
    LinearLayout llDistributor, llOrder, llNewOrder, llInvoice, llNoOrder,fltrView;
    TabAdapter adapter;
    Switch swACOutlet, swOTHOutlet, swPlus4, swMinus18, swAmbient, swBandC, swFreezerOutlet, swNoFreezerOutlet;
    int CountUR = 0, CountSR = 0, CountCls = 0, CountTotUni = 0;
    Boolean StopedUpdate;
    ApiInterface apiInterface;
    boolean updSale = true;
    String ViewDist;
    RecyclerView rvOutletCategory, rvMasterCategory;;
    com.saneforce.godairy.Activity_Hap.Common_Class DT = new com.saneforce.godairy.Activity_Hap.Common_Class();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String mCategoryName = "ALL", mSubCategoryName = "ALL";
    private final ArrayList<Common_Model> modelRetailChannel = new ArrayList<>();

    String categoryType = "", freezerFilter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard__route);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        dashboard_route = this;
        StopedUpdate = false;

        ViewDist = "";

        db = new DatabaseHandler(this);


        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);
        CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

        getSalesCounts();
        JSONObject jParam = new JSONObject();
        try {
            jParam.put("SF", UserDetails.getString("Sfcode", ""));
            jParam.put("div", UserDetails.getString("Divcode", ""));
            apiInterface.getDataArrayList("get/prodgroup", jParam.toString()).enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    db.deleteMasterData("PGroup");
                    db.addMasterData("PGroup", response.body());
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new MyFirebaseMessagingService().setOnLiveUpdateListener(new OnLiveUpdateListener() {
            @Override
            public void onUpdate(String mode) {
                Log.d("LiveEvent", "reloadList");
                if (mode.equalsIgnoreCase("reloadSale") && updSale == false) {
                    getSalesCounts();
                    getLastInvoiceData();
                }
            }
        });
        
        getPendingCounts();

        common_class.getDataFromApi(Constants.Outlet_Total_Orders, this, false);
        try {
            headtext = findViewById(R.id.headtext);
            route_text = findViewById(R.id.route_text);
            distributor_text = findViewById(R.id.distributor_text);
            tvDistributor = findViewById(R.id.tvDistributer);
            OvrAll = findViewById(R.id.OvrAll);
            textViewname = findViewById(R.id.textViewname);
            ReachedOutlet = findViewById(R.id.ReachedOutlet);
            btnCmbRoute = findViewById(R.id.btnCmbRoute);
            ivToolbarHome = findViewById(R.id.toolbar_home);
            llDistributor = findViewById(R.id.llDistributor);
            llOrder = findViewById(R.id.llOrder);
            llNewOrder = findViewById(R.id.llNewOrder);
            llNoOrder = findViewById(R.id.llNoOrder);
            llInvoice = findViewById(R.id.llInv);
            llPen = findViewById(R.id.llPen);
            PendingCount = findViewById(R.id.smryPen);
            txSearchRet = findViewById(R.id.txSearchRet);
            txSrvOtlt = findViewById(R.id.txSrvOtlt);
            txTotUniOtlt = findViewById(R.id.txTotUnivOtlt);
            txUniOtlt = findViewById(R.id.txUniOtlt);
            txClsOtlt = findViewById(R.id.txClsOtlt);
            txSrvOtltCnt = findViewById(R.id.txSrvOtltCnt);
            txUniOtltCnt = findViewById(R.id.txUniOtltCnt);
            txTotUniOtltCnt = findViewById(R.id.txTotUnivOtltCnt);
            txClsOtltCnt = findViewById(R.id.txClsOtltCnt);
            btSrvOtlt = findViewById(R.id.btSrvOtlt);
            btTotUniOtlt = findViewById(R.id.btTotUnivOtlt);
            btUniOtlt = findViewById(R.id.btUniOtlt);
            btClsOtlt = findViewById(R.id.btClsOtlt);
            ivBtnRpt = findViewById(R.id.ivBtnRpt);
            txtOrdDate = findViewById(R.id.txtOrdDate);

            smryOrd = findViewById(R.id.smryOrd);
            smryNOrd = findViewById(R.id.smryNOrd);
            smryNOOrd = findViewById(R.id.smryNOOrd);
            smryInv = findViewById(R.id.smryInv);
            smryInvVal = findViewById(R.id.smryInvVal);

            undrServ = findViewById(R.id.undrServ);
            underTotUni = findViewById(R.id.undrTotUniv);
            undrUni = findViewById(R.id.undrUni);
            undrCls = findViewById(R.id.undrCls);

            swACOutlet = findViewById(R.id.swACOutlet);
            swOTHOutlet = findViewById(R.id.swOTHOutlet);
            swPlus4 = findViewById(R.id.swPlus4);
            swAmbient = findViewById(R.id.swAmbient);
            swMinus18 = findViewById(R.id.swMinus18);
            swBandC = findViewById(R.id.swBandC);
            swFreezerOutlet = findViewById(R.id.swFreezerOutlet);
            swNoFreezerOutlet = findViewById(R.id.swNofreezerOutlet);

            viewPager = findViewById(R.id.viewpager);
            viewPager.setOffscreenPageLimit(4);
            tabLayout = findViewById(R.id.tabs);
            rvOutletCategory = findViewById(R.id.rvOutletCategory);
            rvMasterCategory = findViewById(R.id.rvOutletMasterCategory);

            ReachedOutlet.setOnClickListener(this);
            distributor_text.setOnClickListener(this);
            route_text.setOnClickListener(this);
            btnCmbRoute.setOnClickListener(this);
            llDistributor.setOnClickListener(this);
            llOrder.setOnClickListener(this);
            llNewOrder.setOnClickListener(this);
            llNoOrder.setOnClickListener(this);
            llInvoice.setOnClickListener(this);
            ivBtnRpt.setOnClickListener(this);
            btSrvOtlt.setOnClickListener(this);
            btTotUniOtlt.setOnClickListener(this);
            btUniOtlt.setOnClickListener(this);
            btClsOtlt.setOnClickListener(this);
            llPen.setOnClickListener(this);

            llNewOrder.setVisibility(View.GONE);
            llNoOrder.setVisibility(View.GONE);

            txTotUniOtlt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txTotUniOtlt.setTypeface(null, Typeface.BOLD);
            underTotUni.setVisibility(View.VISIBLE);

            undrServ.setVisibility(View.INVISIBLE);
            undrUni.setVisibility(View.INVISIBLE);
            undrCls.setVisibility(View.INVISIBLE);
            txSrvOtlt.setTypeface(null, Typeface.NORMAL);
            txSrvOtlt.setTextColor(getResources().getColor(R.color.grey_900));
            txUniOtlt.setTypeface(null, Typeface.NORMAL);
            txUniOtlt.setTextColor(getResources().getColor(R.color.grey_900));
            txClsOtlt.setTypeface(null, Typeface.NORMAL);
            txClsOtlt.setTextColor(getResources().getColor(R.color.grey_900));
            txtOrdDate.setText(DT.getDateWithFormat(new Date(), "dd-MMM-yyyy"));
            btnFilter=findViewById(R.id.btnFilter);
            fltrView=findViewById(R.id.fltrView);

            common_class.gotoHomeScreen(this, ivToolbarHome);

            btnFilter.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onClick(View v) {
                    if(fltrView.getVisibility()==View.GONE) {
                        fltrView.setVisibility(View.VISIBLE);
                        btnFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_btnfilter_off));
                    }else{
                        fltrView.setVisibility(View.GONE);
                        btnFilter.setImageDrawable(getResources().getDrawable(R.drawable.ic_btnfilter));
                    }
                }
            });
            swACOutlet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swOTHOutlet.setChecked(false);
                }
            });
            swACOutlet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setPagerAdapter(false);
                }
            });
            swOTHOutlet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swACOutlet.setChecked(false);
                }
            });
            swOTHOutlet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setPagerAdapter(false);
                }
            });


            swFreezerOutlet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        swNoFreezerOutlet.setChecked(false);


                    }
                    getFilterType(swFreezerOutlet.isChecked(), swNoFreezerOutlet.isChecked());
                }
            });

            swNoFreezerOutlet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        swFreezerOutlet.setChecked(false);
                    }

                    getFilterType(swFreezerOutlet.isChecked(), swNoFreezerOutlet.isChecked());
                }
            });


            swMinus18.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        swPlus4.setChecked(false);
                        swAmbient.setChecked(false);
                        swBandC.setChecked(false);


                    }

                    getCategoryType(swMinus18.isChecked(), swPlus4.isChecked(), swAmbient.isChecked(), swBandC.isChecked());

                    //  setPagerAdapter(false);
                }
            });
            swPlus4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        swMinus18.setChecked(false);
                        swAmbient.setChecked(false);
                        swBandC.setChecked(false);

                    }
                    getCategoryType(swMinus18.isChecked(), swPlus4.isChecked(), swAmbient.isChecked(), swBandC.isChecked());

                }
            });

            swAmbient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        swPlus4.setChecked(false);
                        swMinus18.setChecked(false);
                        swBandC.setChecked(false);
                    }
                    getCategoryType(swMinus18.isChecked(), swPlus4.isChecked(), swAmbient.isChecked(), swBandC.isChecked());

                }
            });

            swBandC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        swPlus4.setChecked(false);
                        swMinus18.setChecked(false);
                        swAmbient.setChecked(false);
                    }
                    getCategoryType(swMinus18.isChecked(), swPlus4.isChecked(), swAmbient.isChecked(), swBandC.isChecked());

                }
            });

            txSearchRet.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    setPagerAdapter(true);


                }
            });
            gson = new Gson();


            tvDistributor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewDist = shared_common_pref.getvalue(Constants.Distributor_Id);
                    Constants.View_SUMMARY_MODE = ViewDist;
                    getSalesCounts();
                    tvDistributor.setBackground(getResources().getDrawable(R.drawable.cardbtnprimary));
                    tvDistributor.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    OvrAll.setBackground(getResources().getDrawable(R.drawable.cardbutton));
                    OvrAll.setTextColor(getResources().getColor(R.color.black));

                }
            });
            OvrAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewDist = "";
                    getSalesCounts();
                    Constants.View_SUMMARY_MODE = "";
                    OvrAll.setBackground(getResources().getDrawable(R.drawable.cardbtnprimary));
                    OvrAll.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    tvDistributor.setBackground(getResources().getDrawable(R.drawable.cardbutton));
                    tvDistributor.setTextColor(getResources().getColor(R.color.black));

                }
            });
            if (shared_common_pref.getvalue(Constants.LOGIN_TYPE).equals(Constants.CHECKIN_TYPE)) {
                tvDistributor.setVisibility(View.GONE);
            }
            userTypeRetailor = new TypeToken<ArrayList<Retailer_Modal_List>>() {
            }.getType();

            if (shared_common_pref.getvalue(Shared_Common_Pref.DCRMode).equalsIgnoreCase("SR")) {
                headtext.setText("SALES RETURN");
            }

            Retailer_Modal_ListFilter = new ArrayList<>();
            Retailer_Modal_List = new ArrayList<>();
            if (!shared_common_pref.getvalue(Constants.Distributor_Id).equals("")) {

                if (shared_common_pref.getvalue(Constants.DivERP).equalsIgnoreCase("21")) {
                    categoryType = "-18";
                    findViewById(R.id.cvCatTypeParent).setVisibility(View.VISIBLE);
                } else
                    findViewById(R.id.cvCatTypeParent).setVisibility(View.GONE);

                common_class.getDb_310Data(Constants.RETAILER_STATUS, this);
                common_class.getDb_310Data(Rout_List, this);
                getLastInvoiceData();
                String outletserializableob = shared_common_pref.getvalue(Constants.Retailer_OutletList);
                Retailer_Modal_List = gson.fromJson(outletserializableob, userTypeRetailor);
                distributor_text.setText(shared_common_pref.getvalue(Constants.Distributor_name));
                tvDistributor.setText(shared_common_pref.getvalue(Constants.Distributor_name));
                loadroute();


                if (!shared_common_pref.getvalue(Route_Id).equals("")) {
                    route_text.setText(shared_common_pref.getvalue(Constants.Route_name));
                }
                if (Retailer_Modal_List != null) {

                    String todayorderliost = String.valueOf(db.getMasterData(Constants.Outlet_Total_Orders));

                    userTypeReport = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
                    }.getType();
                    Retailer_Order_List = gson.fromJson(todayorderliost, userTypeReport);
                    if (Retailer_Modal_List != null && Retailer_Modal_List.size() > 0) {
                        for (int i = 0; Retailer_Modal_List.size() > i; i++) {
                            for (int j = 0; Retailer_Order_List.size() > j; j++) {
                                if (Retailer_Modal_List.get(i).getId().equals(Retailer_Order_List.get(j).getOutletCode())) {
                                    Log.e("Invoice_Flag", Retailer_Order_List.get(j).getInvoice_Flag());
                                    if (Retailer_Order_List.get(j).getInvoice_Flag().equals("2")) {
                                        Retailer_Modal_List.get(i).setInvoiceDate(Retailer_Order_List.get(j).getOrderDate());
                                        Retailer_Modal_List.get(i).setInvoiceValues(String.valueOf(Retailer_Order_List.get(j).getInvoicevalues()));
                                        Retailer_Modal_List.get(i).setStatusname(String.valueOf(Retailer_Order_List.get(j).getStatus()));
                                        Retailer_Modal_List.get(i).setInvoice_Flag(Retailer_Order_List.get(j).getInvoice_Flag());
                                        Retailer_Modal_List.get(i).setValuesinv("" + Retailer_Order_List.get(j).getOrderValue());
                                    } else {
                                        Retailer_Modal_List.get(i).setInvoice_Flag(Retailer_Order_List.get(j).getInvoice_Flag());
                                    }
                                }
                            }
                        }
                    }
                    Retailer_Modal_ListFilter.clear();
                    sDeptType = UserDetails.getString("DeptType", "");
                    sDeptType = "1";
                    btnCmbRoute.setVisibility(View.VISIBLE);

                }


            } else {
                btnCmbRoute.setVisibility(View.GONE);
            }

            setPagerAdapter(false);
            createTabFragment();

            if (shared_common_pref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
                distributor_text.setEnabled(false);
                findViewById(R.id.ivDistSpinner).setVisibility(View.GONE);
            }

            Type commonType = new TypeToken<ArrayList<Common_Model>>() {
            }.getType();

            //  if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.RETAIL_CHANNEL)))//subCategory
            //  getRetailerChannel();

//            else {
//                modelRetailChannel = gson.fromJson(shared_common_pref.getvalue(Constants.RETAIL_CHANNEL), commonType);
//                rvMasterCategory.setAdapter(new OutletCategoryFilterAdapter(modelRetailChannel, this, new AdapterOnClick() {
//                    @Override
//                    public void CallMobile(String categoryName) {
//                        setOutletCategoryAdapter();
//                    }
//                }));

            // }


            setOutletCategoryAdapter();
            getGroupOutletCategory();


        } catch (Exception e) {
            Log.e("Retailor List:ex ", e.getMessage());
            e.printStackTrace();
        }


    }

    private void getPendingCounts() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String StockistCode = shared_common_pref.getvalue(Constants.Distributor_Id);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_pending_orders_count");
        params.put("distCode", StockistCode);
        Log.e("status", "GET REQUEST: " + params.toString());
        Call<ResponseBody> call = apiInterface.getPendingOrdersCount(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        Log.e("status", "API Result: " + result);
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("success")) {
                            JSONArray array = object.getJSONArray("response");
                            for (int i = 0; i < array.length(); i++) {
                                String count = array.getJSONObject(i).getString("PendingCount");
                                PendingCount.setText(count);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("error", "Failed1: " + e.getMessage());
                    }
                } else {
                    Log.e("error", "Error: Response Failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error", "Failed2: " + t.getMessage());
            }
        });
    }

    private void getFilterType(boolean isFreezerOutlet, boolean isNoFreezerOutlet) {
        freezerFilter = isFreezerOutlet ? "Yes" : isNoFreezerOutlet ? "No" : "";
        setPagerAdapter(true);
    }

    private void getCategoryType(boolean isMinus18, boolean isPlus4, boolean isAmbient, boolean isBandC) {
        categoryType = isMinus18 ? "-18" : isPlus4 ? "+4" : isAmbient ? "Ambient" : isBandC ? "B&C" : "";
        Log.v("categoryTypes:DR", categoryType);
        setPagerAdapter(true);
    }

    public void getGroupOutletCategory() {
        String routeMap = "{\"tableName\":\"Doctor_Specialty\",\"coloumns\":\"[\\\"NeedApproval\\\",\\\"Specialty_Code as id\\\", \\\"Specialty_SName as group\\\"]\"," +
                "\"where\":\"[\\\"isnull(Deactivate_flag,0)=0\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"group asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.retailerClass(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code),
                shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24",
                routeMap);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    modelRetailChannel.clear();
                    JsonArray jsonArray = response.body();
                    Log.e("RESPONSE_VALUE_GROUP", String.valueOf(jsonArray));
                    for (int a = 0; a < jsonArray.size(); a++) {
                        JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                        String className = String.valueOf(jsonObject.get("name"));
                        String id = String.valueOf(jsonObject.get("id"));
                        String retailerClass = String.valueOf(className.subSequence(1, className.length() - 1));
                        String approval = String.valueOf(jsonObject.get("NeedApproval"));
                        Common_Model mCommon_model_spinner = new Common_Model(id, retailerClass, approval);
                        Log.e("LeaveType_Request", retailerClass);
                        modelRetailChannel.add(mCommon_model_spinner);
                    }

//                    if (modelRetailChannel != null && modelRetailChannel.size() > 0) {
//                        rvMasterCategory.setAdapter(new OutletCategoryFilterAdapter(modelRetailChannel, Dashboard_Route.this, new AdapterOnClick() {
//                            @Override
//                            public void CallMobile(String categoryName) {
//                                setOutletCategoryAdapter();
//                            }
//                        }));
//
//                        shared_common_pref.save(Constants.RETAIL_CHANNEL, gson.toJson(modelRetailChannel));
//                    }

                } catch (Exception e) {
                    Log.v(" getRetailerChannel: ", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }


    public void getRetailerChannel() {
        String routeMap = "{\"tableName\":\"Doctor_Specialty\",\"coloumns\":\"[\\\"NeedApproval\\\",\\\"Specialty_Code as id\\\", \\\"Specialty_Name as name\\\"]\"," +
                "\"where\":\"[\\\"isnull(Deactivate_flag,0)=0\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.retailerClass(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code),
                shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24",
                routeMap);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    modelRetailChannel.clear();
                    JsonArray jsonArray = response.body();
                    Log.e("RESPONSE_VALUE", String.valueOf(jsonArray));
                    for (int a = 0; a < jsonArray.size(); a++) {
                        JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                        String className = String.valueOf(jsonObject.get("name"));
                        String id = String.valueOf(jsonObject.get("id"));
                        String retailerClass = String.valueOf(className.subSequence(1, className.length() - 1));
                        String approval = String.valueOf(jsonObject.get("NeedApproval"));
                        Common_Model mCommon_model_spinner = new Common_Model(id, retailerClass, approval);
                        Log.e("LeaveType_Request", retailerClass);
                        modelRetailChannel.add(mCommon_model_spinner);
                    }

//                    if (modelRetailChannel != null && modelRetailChannel.size() > 0) {
//                        rvMasterCategory.setAdapter(new OutletCategoryFilterAdapter(modelRetailChannel, Dashboard_Route.this, new AdapterOnClick() {
//                            @Override
//                            public void CallMobile(String categoryName) {
//                                setOutletCategoryAdapter();
//                            }
//                        }));
//
//                        shared_common_pref.save(Constants.RETAIL_CHANNEL, gson.toJson(modelRetailChannel));
//                    }

                } catch (Exception e) {
                    Log.v(" getRetailerChannel: ", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }


    void setOutletCategoryAdapter() {
        try {
            ArrayList<String> list = new ArrayList<>();
            list.add("ALL");
            for (int i = 0; i < Retailer_Modal_ListFilter.size(); i++) {
                if (!Common_Class.isNullOrEmpty(Retailer_Modal_ListFilter.get(i).getOutletClass()) && !Retailer_Modal_ListFilter.get(i).getOutletClass().equalsIgnoreCase("B")) {
                    list.add(Retailer_Modal_ListFilter.get(i).getOutletClass());
                }
            }
            HashSet hs = new HashSet();
            hs.addAll(list);
            list.clear();
            list.addAll(hs);

            rvMasterCategory.setAdapter(new OutletMasterCategoryFilterAdapter(list, this, new AdapterOnClick() {
                @Override
                public void CallMobile(String categoryName) {
                    common_class.brandPos = 0;
                    mCategoryName = categoryName;
                    setSubCatAdapter();
                    setPagerAdapter(true);
                }
            }));


            setSubCatAdapter();

        } catch (Exception e) {
            Log.v("Retailor List:cat ex", e.getMessage());
        }

    }

    void setSubCatAdapter() {
        ArrayList<String> subList = new ArrayList<>();
        subList.add("ALL");

        for (int i = 0; i < Retailer_Modal_ListFilter.size(); i++) {
            if (!Common_Class.isNullOrEmpty(Retailer_Modal_ListFilter.get(i).getSpeciality()) && ((mCategoryName.equalsIgnoreCase("ALL") ||
                    mCategoryName.equalsIgnoreCase(Retailer_Modal_ListFilter.get(i).getOutletClass()))))
                subList.add(Retailer_Modal_ListFilter.get(i).getSpeciality());
        }
        HashSet sub = new HashSet();
        sub.addAll(subList);
        subList.clear();
        subList.addAll(sub);

        rvOutletCategory.setAdapter(new OutletCategoryFilterAdapter(subList, this, new AdapterOnClick() {
            @Override
            public void CallMobile(String categoryName) {
                mSubCategoryName = categoryName;
                setPagerAdapter(true);
            }
        }));


    }

    public void getSalesCounts() {
        getPendingCounts();
        updSale = true;
        JSONObject jParam = new JSONObject();
        try {
            jParam.put("SF", UserDetails.getString("Sfcode", ""));
            jParam.put("Stk", ViewDist);
            jParam.put("div", UserDetails.getString("Divcode", ""));
            jParam.put(Constants.LOGIN_TYPE, shared_common_pref.getvalue(Constants.LOGIN_TYPE));

            Log.e("status", jParam.toString());

            apiInterface.getDataArrayList("get/salessumry", jParam.toString()).enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    try {
                        JsonArray jRes = response.body();
                        Log.v("Salessumry:", response.body().toString());
                        if (jRes.size() > 0) {
                            JsonObject jItm = jRes.get(0).getAsJsonObject();
                            double invVal = jItm.get("InvVal").getAsDouble();
                            smryOrd.setText(jItm.get("Orders").getAsString());
                            smryNOrd.setText(jItm.get("NOrders").getAsString());
                            smryNOOrd.setText(jItm.get("NoOrder").getAsString());
                            smryInv.setText(jItm.get("InvCnt").getAsString());
                            smryInvVal.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(invVal));
                        } else {
                            smryOrd.setText("0");
                            smryNOrd.setText("0");
                            smryNOOrd.setText("0");
                            smryInv.setText("0");
                            smryInvVal.setText(CurrencySymbol+" 0.00");
                        }

                        updSale = false;
                        //if(StopedUpdate==false) updateSales();
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    updSale = false;
                    //if(StopedUpdate==false) updateSales();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StopedUpdate = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.Distributor_Id)) && !distributor_text.getText().toString().equalsIgnoreCase(shared_common_pref.getvalue(Constants.Distributor_name))) {
            route_text.setText(shared_common_pref.getvalue(Constants.Route_name));
            distributor_text.setText(shared_common_pref.getvalue(Constants.Distributor_name));
            tvDistributor.setText(shared_common_pref.getvalue(Constants.Distributor_name));
            common_class.getDb_310Data(Rout_List, this);
            common_class.getDb_310Data(Constants.RETAILER_STATUS, this);

        }

    }

    private void getLastInvoiceData() {
        try {

            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                JSONObject HeadItem = new JSONObject();
                HeadItem.put("distributorCode", shared_common_pref.getvalue(Constants.Distributor_Id));

                String div_code = Shared_Common_Pref.Div_Code.replaceAll(",", "");
                HeadItem.put("divisionCode", div_code);


                Call<ResponseBody> call = service.getLastThreeMnthsData(HeadItem.toString());
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
                                    Log.v("Res>>", is.toString());
                                }

                                shared_common_pref.save(Constants.RetailorTodayData, is.toString());

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
                common_class.showMsg(Dashboard_Route.dashboard_route, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }

    private void createTabFragment() {
        adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getSelectedTabPosition(), Retailer_Modal_ListFilter, RetType, this, "Dashboard_Route", mCategoryName, categoryType, mSubCategoryName, freezerFilter);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btClsOtlt:
                RetType = "2";
                txClsOtlt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txClsOtlt.setTypeface(null, Typeface.BOLD);
                undrCls.setVisibility(View.VISIBLE);
                undrUni.setVisibility(View.INVISIBLE);
                undrServ.setVisibility(View.INVISIBLE);
                underTotUni.setVisibility(View.INVISIBLE);

                txSrvOtlt.setTypeface(null, Typeface.NORMAL);
                txSrvOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                txUniOtlt.setTypeface(null, Typeface.NORMAL);
                txUniOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                txTotUniOtlt.setTypeface(null, Typeface.NORMAL);
                txTotUniOtlt.setTextColor(getResources().getColor(R.color.grey_900));

                setPagerAdapter(false);
                setOutletCategoryAdapter();
                break;
            case R.id.btSrvOtlt:
                RetType = "1";
                txSrvOtlt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txSrvOtlt.setTypeface(null, Typeface.BOLD);

                undrServ.setVisibility(View.VISIBLE);
                undrUni.setVisibility(View.INVISIBLE);
                undrCls.setVisibility(View.INVISIBLE);
                underTotUni.setVisibility(View.INVISIBLE);

                txUniOtlt.setTypeface(null, Typeface.NORMAL);
                txUniOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                txClsOtlt.setTypeface(null, Typeface.NORMAL);
                txClsOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                txTotUniOtlt.setTypeface(null, Typeface.NORMAL);
                txTotUniOtlt.setTextColor(getResources().getColor(R.color.grey_900));

                setPagerAdapter(false);
                setOutletCategoryAdapter();
                break;
            case R.id.btUniOtlt:
                RetType = "0";
                txUniOtlt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txUniOtlt.setTypeface(null, Typeface.BOLD);
                undrUni.setVisibility(View.VISIBLE);
                undrServ.setVisibility(View.INVISIBLE);
                undrCls.setVisibility(View.INVISIBLE);
                underTotUni.setVisibility(View.INVISIBLE);
                txSrvOtlt.setTypeface(null, Typeface.NORMAL);
                txSrvOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                txClsOtlt.setTypeface(null, Typeface.NORMAL);
                txClsOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                txTotUniOtlt.setTypeface(null, Typeface.NORMAL);
                txTotUniOtlt.setTextColor(getResources().getColor(R.color.grey_900));

                setPagerAdapter(false);
                setOutletCategoryAdapter();
                break;
            case R.id.btTotUnivOtlt:
                RetType = "";
                txTotUniOtlt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txTotUniOtlt.setTypeface(null, Typeface.BOLD);

                underTotUni.setVisibility(View.VISIBLE);
                undrServ.setVisibility(View.INVISIBLE);
                undrUni.setVisibility(View.INVISIBLE);
                undrCls.setVisibility(View.INVISIBLE);

                txSrvOtlt.setTypeface(null, Typeface.NORMAL);
                txSrvOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                txUniOtlt.setTypeface(null, Typeface.NORMAL);
                txUniOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                txClsOtlt.setTypeface(null, Typeface.NORMAL);
                txClsOtlt.setTextColor(getResources().getColor(R.color.grey_900));

                setPagerAdapter(false);
                setOutletCategoryAdapter();
                break;
            case R.id.ivBtnRpt:
                common_class.CommonIntentwithoutFinish(HistoryInfoActivity.class);
                break;

            case R.id.llOrder:
                if (smryOrd.getText().toString().equals("0"))
                    common_class.showMsg(this, "No Records Found");
                else {
                    Intent intent = new Intent(getApplicationContext(), DashboardInfoActivity.class);
                    Shared_Common_Pref.SALES_MODE = "order";
                    intent.putExtra("type", "Orders");
                    startActivity(intent);
                }
                break;
            case R.id.llNewOrder:
                if (smryNOrd.getText().toString().equals("0"))
                    common_class.showMsg(this, "No Records Found");
                else {
                    Shared_Common_Pref.SALES_MODE = "neworder";
                    Intent intentNew = new Intent(getApplicationContext(), DashboardInfoActivity.class);
                    intentNew.putExtra("type", "New Order");
                    startActivity(intentNew);
                }
                break;
            case R.id.llNoOrder:
                if (smryNOOrd.getText().toString().equals("0"))
                    common_class.showMsg(this, "No Records Found");
                else {
                    Shared_Common_Pref.SALES_MODE = "noorder";
                    Intent intentNO = new Intent(getApplicationContext(), DashboardInfoActivity.class);
                    intentNO.putExtra("type", "Orders");
                    intentNO.putExtra("status", "No Order");
                    startActivity(intentNO);
                }
                break;
            case R.id.llInv:
                if (smryInv.getText().toString().equals("0"))
                    common_class.showMsg(this, "No Records Found");
                else {
                    Shared_Common_Pref.SALES_MODE = "invoice";
                    Intent intentInv = new Intent(getApplicationContext(), DashboardInfoActivity.class);
                    intentInv.putExtra("type", "Invoice");
                    startActivity(intentInv);
                }
                break;
            case R.id.llPen:
                if (PendingCount.getText().toString().equals("0"))
                    common_class.showMsg(this, "No Records Found");
                else {
                    Shared_Common_Pref.SALES_MODE = "pending";
                    Intent intentInv = new Intent(getApplicationContext(), PendingOrdersActivity.class);
                    startActivity(intentInv);
                }
                break;

            case R.id.ReachedOutlet:
                common_class.CommonIntentwithoutFinish(Nearby_Outlets.class);
                overridePendingTransition(R.anim.in, R.anim.out);

                break;
            case R.id.distributor_text:
                common_class.showCommonDialog(common_class.getDistList(), 2, this);
                break;
            case R.id.route_text:
                if (FRoute_Master != null && FRoute_Master.size() > 1) {
                    common_class.showCommonDialog(FRoute_Master, 3, this);
                }
                break;
        }
    }


    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {

        common_class.dismissCommonDialog(type);
        if (type == 2) {
            mCategoryName = "ALL";
            mSubCategoryName = "ALL";
            route_text.setText("");
            shared_common_pref.save(Constants.Route_name, "");
            shared_common_pref.save(Constants.Route_Id, "");
            btnCmbRoute.setVisibility(View.VISIBLE);
            distributor_text.setText(myDataset.get(position).getName());
            tvDistributor.setText(myDataset.get(position).getName());

            shared_common_pref.save(Constants.Distributor_name, myDataset.get(position).getName());
            shared_common_pref.save(Constants.Distributor_Id, myDataset.get(position).getId());
            shared_common_pref.save(Constants.DistributorERP, myDataset.get(position).getCont());
            shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, myDataset.get(position).getId());
            shared_common_pref.save(Constants.Distributor_phone, myDataset.get(position).getPhone());
            shared_common_pref.save(Constants.DistributorGst, myDataset.get(position).getDisGst());
            shared_common_pref.save(Constants.DistributorFSSAI, myDataset.get(position).getDisFssai());
            shared_common_pref.save(Constants.CusSubGrpErp, myDataset.get(position).getCusSubGrpErp());

            if (myDataset.get(position).getDivERP().equalsIgnoreCase("21")) {
                findViewById(R.id.cvCatTypeParent).setVisibility(View.VISIBLE);
                categoryType = "-18";
            } else {
                categoryType = "";
                findViewById(R.id.cvCatTypeParent).setVisibility(View.GONE);
            }


            common_class.getDb_310Data(Constants.RETAILER_STATUS, this);
            getLastInvoiceData();
            getPendingCounts();
            common_class.getDataFromApi(Retailer_OutletList, this, false);
            common_class.getDb_310Data(Rout_List, this);
            shared_common_pref.save(Constants.DivERP, myDataset.get(position).getDivERP());
        } else if (type == 3) {
            route_text.setText(myDataset.get(position).getName());
            shared_common_pref.save(Constants.Route_name, myDataset.get(position).getName());
            shared_common_pref.save(Constants.Route_Id, myDataset.get(position).getId());
            setPagerAdapter(false);
        }
    }


    public void loadroute() {

        if (FRoute_Master.size() == 1) {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.INVISIBLE);
            route_text.setText(FRoute_Master.get(0).getName());
            shared_common_pref.save(Constants.Route_name, FRoute_Master.get(0).getName());
            shared_common_pref.save(Constants.Route_Id, FRoute_Master.get(0).getId());

        } else {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {
                switch (key) {
                    case Constants.POS_NETAMT_TAX:
                        Log.v("POS_NETAMT_TAX:", apiDataResponse);
                        break;
                    case Rout_List:
                        JSONArray routeArr = new JSONArray(apiDataResponse);
                        FRoute_Master.clear();
                        if (routeArr.length() > 1) {
                            Model_Pojo = new Common_Model("", "All", routeArr.getJSONObject(0).optString("stockist_code"));
                            FRoute_Master.add(Model_Pojo);
                        }
                        for (int i = 0; i < routeArr.length(); i++) {
                            JSONObject jsonObject1 = routeArr.getJSONObject(i);
                            String id = String.valueOf(jsonObject1.optInt("id"));
                            String name = jsonObject1.optString("name");
                            String flag = jsonObject1.optString("FWFlg");
                            Model_Pojo = new Common_Model(id, name, flag);
                            Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                            FRoute_Master.add(Model_Pojo);

                        }
                        loadroute();
                        break;
                    case Retailer_OutletList:
                        setPagerAdapter(false);
                        setOutletCategoryAdapter();
                        break;
                    case Constants.RETAILER_STATUS:
                        JSONObject jsonObject = new JSONObject(apiDataResponse);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            String outletCode = "";


                            for (int arr = 0; arr < jsonArray.length(); arr++) {
                                JSONObject arrObj = jsonArray.getJSONObject(arr);

                                int flag = arrObj.getInt("OrderFlg");
                                //  BTG=0,invoice-3,order-2,no order-1;
                                String sMode = flag == 0 ? "BTG" : flag == 3 ? "invoice" : flag == 2 ? "order" : "no order";

                                outletCode = outletCode + arrObj.getString("ListedDrCode") + sMode + ",";


                            }

                            txUniOtltCnt.setText("" + jsonArray.length());
                            shared_common_pref.save(Constants.RETAILER_STATUS, outletCode);

                            Log.v("statusList:", outletCode);

                            setPagerAdapter(false);
                            setOutletCategoryAdapter();
                        }
                        break;
                }
            }
        } catch (Exception e) {

        }

    }

    void setPagerAdapter(boolean isFilter) {
        try {

            if (shared_common_pref.getvalue(Constants.Distributor_Id).equals("")) {
                Toast.makeText(this, "Select Franchise", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!shared_common_pref.getvalue(Constants.Distributor_Id).equals("")) {
                String outletserializableob = shared_common_pref.getvalue(Constants.Retailer_OutletList);
                Retailer_Modal_List = gson.fromJson(outletserializableob, userTypeRetailor);
            }

            Retailer_Modal_ListFilter.clear();
            CountUR = 0;
            CountSR = 0;
            CountCls = 0;
            CountTotUni = 0;


            for (int i = 0; i < Retailer_Modal_List.size(); i++) {
                boolean ACTrue = false;


                if (swACOutlet.isChecked()) {
                    if (Retailer_Modal_List.get(i).getDelivType() != null && Retailer_Modal_List.get(i).getDelivType().equalsIgnoreCase("AC"))
                        ACTrue = true;
                } else if (swOTHOutlet.isChecked()) {
                    if (!(Retailer_Modal_List.get(i).getDelivType() != null && Retailer_Modal_List.get(i).getDelivType().equalsIgnoreCase("AC")))
                        ACTrue = true;
                } else {
                    ACTrue = true;
                }

                if (shared_common_pref.getvalue(Shared_Common_Pref.DCRMode).equalsIgnoreCase("SR") && Common_Class.isNullOrEmpty(Retailer_Modal_List.get(i).getCustomerCode()))
                    ACTrue = false;

                if (Retailer_Modal_List.get(i).getType() == null)
                    Retailer_Modal_List.get(i).setType("0");
                if (Retailer_Modal_List.get(i).getType().equalsIgnoreCase("0") && ACTrue) CountUR++;
                if (Retailer_Modal_List.get(i).getType().equalsIgnoreCase("1") && ACTrue) CountSR++;
                if (Retailer_Modal_List.get(i).getType().equalsIgnoreCase("2") && ACTrue)
                    CountCls++;

                if ((Retailer_Modal_List.get(i).getType().equalsIgnoreCase("0") ||
                        Retailer_Modal_List.get(i).getType().equalsIgnoreCase("1")) && ACTrue)
                    CountTotUni++;
                if (ACTrue) {
                    Retailer_Modal_ListFilter.add(Retailer_Modal_List.get(i));
                }
            }
            txUniOtltCnt.setText(String.valueOf(CountUR));
            txSrvOtltCnt.setText(String.valueOf(CountSR));
            txClsOtltCnt.setText(String.valueOf(CountCls));
            txTotUniOtltCnt.setText(String.valueOf(CountTotUni));

            if (isFilter) {
                Log.v("categoryTypes:", categoryType + " :freezer:" + freezerFilter);
                adapter.notifyData(Retailer_Modal_ListFilter, tabLayout.getSelectedTabPosition(), txSearchRet.getText().toString(), RetType, mCategoryName, categoryType, mSubCategoryName, freezerFilter);
            } else {
                adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getSelectedTabPosition(), Retailer_Modal_ListFilter, RetType, this, "Dashboard_Route", mCategoryName, categoryType, mSubCategoryName, freezerFilter);
                viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
            }


        } catch (Exception e) {
            Log.v("DAshboard_Route:", e.getMessage());
        }
    }

    public static class AllDataFragment extends Fragment {
        View mView;
        String tabPosition = "";
        List<Retailer_Modal_List> mRetailer_Modal_ListFilter;
        private Context context;
        private RecyclerView recyclerView;


        public AllDataFragment(List<Retailer_Modal_List> retailer_Modal_ListFilter, int position) {
            this.mRetailer_Modal_ListFilter = retailer_Modal_ListFilter;
            this.tabPosition = String.valueOf(position);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_tab_outlet, container, false);
        }


        public void updateData() {
            recyclerView.setAdapter(new Route_View_Adapter(mRetailer_Modal_ListFilter, R.layout.route_dashboard_recyclerview, getActivity(), new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    try {

                        if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.Distributor_Id))) {
                            Toast.makeText(getActivity(), "Select Franchise", Toast.LENGTH_SHORT).show();
                        } //else if (dashboard_route.route_text.getText().toString().equals("")) {
                        //    Toast.makeText(getActivity(), "Select The Route", Toast.LENGTH_SHORT).show();
                       // }
                        else if (Common_Class.isNullOrEmpty(dashboard_route.categoryType) && shared_common_pref.getvalue(Constants.DivERP).equalsIgnoreCase("21")) {
                            common_class.showMsg(getActivity(), "Select the Category Type");
                        } else {

                            if (!Shared_Common_Pref.OutletCode.equalsIgnoreCase(mRetailer_Modal_ListFilter.get(position).getId())) {
                                shared_common_pref.clear_pref(Constants.LOC_SECONDARY_DATA);
                                shared_common_pref.clear_pref(Constants.LOC_VANSALES_DATA);
                                shared_common_pref.clear_pref(Constants.LOC_INVOICE_DATA);

                            }

                            Shared_Common_Pref.Outler_AddFlag = "0";
                            Shared_Common_Pref.OutletName = mRetailer_Modal_ListFilter.get(position).getName().toUpperCase();
                            Shared_Common_Pref.OutletCode = mRetailer_Modal_ListFilter.get(position).getId();
                            Shared_Common_Pref.Freezer_Required = mRetailer_Modal_ListFilter.get(position).getFreezer_required();

                            //  Shared_Common_Pref.DistributorCode = shared_common_pref.getvalue(Constants.Distributor_Id);
                            //  Shared_Common_Pref.DistributorName = distributor_text.getText().toString();
                            // Shared_Common_Pref.Route_Code = dashboard_route.Route_id;
                            // common_class.CommonIntentwithoutFinish(Route_Product_Info.class);
                            shared_common_pref.save(Constants.Retailor_Address, mRetailer_Modal_ListFilter.get(position).getListedDrAddress1());
                            shared_common_pref.save(Constants.Retailor_ERP_Code, mRetailer_Modal_ListFilter.get(position).getERP_Code());
                            shared_common_pref.save(Constants.Retailor_Name_ERP_Code, mRetailer_Modal_ListFilter.get(position).getName().toUpperCase() /*+ "~"
                                    + mRetailer_Modal_ListFilter.get(position).getERP_Code()*/);
//                            if (mRetailer_Modal_ListFilter.get(position).getMobileNumber().equalsIgnoreCase("")
//                                    || mRetailer_Modal_ListFilter.get(position).getOwner_Name().equalsIgnoreCase("")) {
//
//                                Intent intent = new Intent(context, AddNewRetailer.class);
//                                Shared_Common_Pref.Outlet_Info_Flag = "0";
//                                Shared_Common_Pref.Editoutletflag = "1";
//                                Shared_Common_Pref.Outler_AddFlag = "0";
//                                Shared_Common_Pref.OutletCode = String.valueOf(mRetailer_Modal_ListFilter.get(position).getId());
//                                intent.putExtra("OutletCode", String.valueOf(mRetailer_Modal_ListFilter.get(position).getId()));
//                                intent.putExtra("OutletName", mRetailer_Modal_ListFilter.get(position).getName());
//                                intent.putExtra("OutletAddress", mRetailer_Modal_ListFilter.get(position).getListedDrAddress1());
//                                intent.putExtra("OutletMobile", mRetailer_Modal_ListFilter.get(position).getMobileNumber());
//                                intent.putExtra("OutletRoute", mRetailer_Modal_ListFilter.get(position).getTownName());
//                                startActivity(intent);
//                                getActivity().finish();
//
//                            } else {
                            //common_class.CommonIntentwithoutFinish(Route_Product_Info.class);
                            Shared_Common_Pref.SFA_MENU = "Dashboard_Route";
                            if (dashboard_route.categoryType.equalsIgnoreCase("Ambient"))
                                Shared_Common_Pref.SecOrdOutletType = "+4," + dashboard_route.categoryType;
                            else if (dashboard_route.categoryType.equalsIgnoreCase("+4"))
                                Shared_Common_Pref.SecOrdOutletType = dashboard_route.categoryType + ",Ambient";
                            else
                                Shared_Common_Pref.SecOrdOutletType = dashboard_route.categoryType;
                            Shared_Common_Pref.CUSTOMER_CODE = mRetailer_Modal_ListFilter.get(position).getCustomerCode();
                            shared_common_pref.save(Constants.Retailor_PHNo, mRetailer_Modal_ListFilter.get(position).getPrimary_No());

                            common_class.getDb_310Data(Constants.TAXList, getActiveActivity());
                            common_class.ProgressdialogShow(1, "Loading Matrial Details");
                            //Toast.makeText(context, "Reached", Toast.LENGTH_SHORT).show();
                            common_class.getProductDetails(getActiveActivity(), new OnLiveUpdateListener() {
                                @Override
                                public void onUpdate(String mode) {
                                    common_class.CommonIntentwithoutFinish(Invoice_History.class);
                                    getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                                    common_class.ProgressdialogShow(0, "");
                                }

                                @Override
                                public void onError(String msg) {
                                    Toast.makeText(getContext(), "Product Loading Failed...\n"+msg , Toast.LENGTH_SHORT).show();
                                    common_class.ProgressdialogShow(0, "");
                                }
                            });
                            //}

                        }
                    } catch (Exception e) {
                        Log.v("AllDataFragment:", e.getMessage());
                    }
                }

                @Override
                public void CallMobile(String MobileNo) {
                    Log.d("Event", "CAll Mobile");
                    int readReq = ContextCompat.checkSelfPermission(context, CALL_PHONE);
                    if (readReq != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(HAPApp.activeActivity, new String[]{CALL_PHONE}, REQUEST_PERMISSIONS_REQUEST_CODE);
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + MobileNo));//change the number
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callIntent);
                    }
                }
            }));
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            this.context = getContext();
            mView = view;
            recyclerView = view.findViewById(R.id.recyclerView);
            updateData();
        }
    }
}
