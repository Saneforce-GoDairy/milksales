package com.saneforce.godairy.SFA_Activity;

import static android.Manifest.permission.CALL_PHONE;
import static com.saneforce.godairy.Common_Class.Constants.Retailer_OutletList;
import static com.saneforce.godairy.Common_Class.Constants.Rout_List;
import static com.saneforce.godairy.Common_Class.Constants.Route_Id;
import static com.saneforce.godairy.Common_Class.Constants.STOCK_LEDGER;
import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;
import static com.saneforce.godairy.SFA_Activity.HAPApp.getActiveActivity;

import android.content.Context;
import android.content.DialogInterface;
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
import com.saneforce.godairy.Activity_Hap.SFA_Activity;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Interface.OnLiveUpdateListener;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.MVP.Main_Model;
import com.saneforce.godairy.Model_Class.Route_Master;
import com.saneforce.godairy.PushNotification.MyFirebaseMessagingService;
import com.saneforce.godairy.R;
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
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VanSalesDashboardRoute extends AppCompatActivity implements Main_Model.MasterSyncView, View.OnClickListener, Master_Interface, UpdateResponseUI {
    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";
    public static VanSalesDashboardRoute dashboard_route;
    public static Common_Class common_class;
    public static TextView distributor_text;
    public static Shared_Common_Pref shared_common_pref;
    List<Retailer_Modal_List> Retailer_Modal_List = new ArrayList<>();
    List<Retailer_Modal_List> Retailer_Modal_ListFilter = new ArrayList<>();
    List<OutletReport_View_Modal> Retailer_Order_List;
    Gson gson;
    Type userTypeRetailor, userTypeReport;
    TextView headtext, textViewname, ReachedOutlet, route_text, txtOrdDate, OvrAll, tvStockLoad, tvStockUnload,tvVanSalPay,tvStockView,
            txTotUniOtlt, txTotUniOtltCnt, txSrvOtlt, txUniOtlt, txClsOtlt, txSrvOtltCnt, txUniOtltCnt, txClsOtltCnt, smryOrd, smryNOrd, smryNOOrd, smryInv, smryInvVal, tvDistributor;
    EditText txSearchRet;
    LinearLayout btnCmbRoute, btSrvOtlt, btUniOtlt, btClsOtlt, undrUni, undrCls, undrServ, underTotUni, btTotUniOtlt;
    Common_Model Model_Pojo;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    String DCRMode;
    String sDeptType, RetType = "";
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    DatabaseHandler db;

    ImageView ivToolbarHome, ivBtnRpt;
    LinearLayout llDistributor, llOrder, llNewOrder, llInvoice, llNoOrder;
    TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Switch swACOutlet, swOTHOutlet;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;
    int CountUR = 0, CountSR = 0, CountCls = 0, CountTotUni = 0;
    Boolean StopedUpdate;
    ApiInterface apiInterface;
    boolean updSale = true;
    String ViewDist;
    TextView tvStockTopUp;

    com.saneforce.godairy.Activity_Hap.Common_Class DT = new com.saneforce.godairy.Activity_Hap.Common_Class();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vansales_dashboard__route);

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
            txSearchRet = findViewById(R.id.txSearchRet);
            txSrvOtlt = findViewById(R.id.txSrvOtlt);
            txUniOtlt = findViewById(R.id.txUniOtlt);
            txClsOtlt = findViewById(R.id.txClsOtlt);
            txSrvOtltCnt = findViewById(R.id.txSrvOtltCnt);
            txUniOtltCnt = findViewById(R.id.txUniOtltCnt);
            txClsOtltCnt = findViewById(R.id.txClsOtltCnt);
            btSrvOtlt = findViewById(R.id.btSrvOtlt);
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
            undrUni = findViewById(R.id.undrUni);
            undrCls = findViewById(R.id.undrCls);

            swACOutlet = findViewById(R.id.swACOutlet);
            swOTHOutlet = findViewById(R.id.swOTHOutlet);

            viewPager = findViewById(R.id.viewpager);
            viewPager.setOffscreenPageLimit(2);
            tabLayout = findViewById(R.id.tabs);
            tvStockLoad = findViewById(R.id.tvStockLoad);
            tvStockUnload = findViewById(R.id.tvStockUnload);
            btTotUniOtlt = findViewById(R.id.btTotUnivOtlt);
            txTotUniOtltCnt = findViewById(R.id.txTotUnivOtltCnt);
            txTotUniOtlt = findViewById(R.id.txTotUnivOtlt);
            underTotUni = findViewById(R.id.undrTotUniv);
            tvVanSalPay = findViewById(R.id.tvVanSalPay);
            tvStockView=findViewById(R.id.tvStockView);
            tvStockTopUp=findViewById(R.id.tvStockTopUp);



            ReachedOutlet.setOnClickListener(this);
            distributor_text.setOnClickListener(this);
            route_text.setOnClickListener(this);
            ivToolbarHome.setOnClickListener(this);
            btnCmbRoute.setOnClickListener(this);
            llDistributor.setOnClickListener(this);
            llOrder.setOnClickListener(this);
            llNewOrder.setOnClickListener(this);
            llNoOrder.setOnClickListener(this);
            llInvoice.setOnClickListener(this);
            tvStockLoad.setOnClickListener(this);
            tvStockUnload.setOnClickListener(this);
            tvStockTopUp.setOnClickListener(this);

            tvVanSalPay.setOnClickListener(this);
            tvStockView.setOnClickListener(this);


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

            ivBtnRpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    common_class.CommonIntentwithoutFinish(HistoryInfoActivity.class);
                }
            });
            txtOrdDate.setText(DT.getDateWithFormat(new Date(), "dd-MMM-yyyy"));

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
            btSrvOtlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    // SearchRetailers();
                }
            });

            btTotUniOtlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

                }
            });
            btUniOtlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                }
            });
            btClsOtlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            // GetJsonData(shared_common_pref.getvalue(Shared_Common_Pref.Todaydayplanresult), "6");
            DCRMode = shared_common_pref.getvalue(Shared_Common_Pref.DCRMode);
            if (DCRMode.equalsIgnoreCase("SC")) {
                headtext.setText("SALES CALLS");
            }
            DCRMode = shared_common_pref.getvalue(Shared_Common_Pref.DCRMode);
            if (DCRMode.equalsIgnoreCase("VC")) {
                headtext.setText("VAN ROUTE SUPPLY");
            }

            Retailer_Modal_ListFilter = new ArrayList<>();
            Retailer_Modal_List = new ArrayList<>();
            if (!shared_common_pref.getvalue(Constants.Distributor_Id).equals("")) {
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

            common_class.getDb_310Data(Constants.Product_List,this);

        } catch (Exception e) {
            Log.e("Retailor List:ex ", e.getMessage());
            e.printStackTrace();
        }




    }


    public void getSalesCounts() {
        updSale = true;
        JSONObject jParam = new JSONObject();
        try {
            jParam.put("SF", UserDetails.getString("Sfcode", ""));
            jParam.put("Stk", ViewDist);
            jParam.put("div", UserDetails.getString("Divcode", ""));
            jParam.put(Constants.LOGIN_TYPE, shared_common_pref.getvalue(Constants.LOGIN_TYPE));

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

try{
        if  (shared_common_pref.getvalue(Constants.VAN_STOCK_LOADING)==null||shared_common_pref.getvalue(Constants.VAN_STOCK_LOADING).equals("")) {
            tvStockUnload.setTextColor(getResources().getColor(R.color.grey_500));
            tvStockUnload.setEnabled(false);
            tvStockLoad.setVisibility(View.VISIBLE);
            tvStockLoad.setTextColor(getResources().getColor(R.color.black));
            tvStockLoad.setEnabled(true);
            tvStockTopUp.setVisibility(View.GONE);

        }

        if  (shared_common_pref.getvalue(Constants.VAN_STOCK_LOADING)!=null&&!shared_common_pref.getvalue(Constants.VAN_STOCK_LOADING).equals("")&&shared_common_pref.getvalue(Constants.VAN_STOCK_LOADING_TIME).equals(Common_Class.GetDateOnly())  ) {
            tvStockLoad.setTextColor(getResources().getColor(R.color.grey_500));
            tvStockLoad.setEnabled(false);
            tvStockLoad.setVisibility(View.GONE);
            tvStockTopUp.setVisibility(View.VISIBLE);


            tvStockUnload.setTextColor(getResources().getColor(R.color.black));
            tvStockUnload.setEnabled(true);

        }

        if (!Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.Distributor_Id))) {
            common_class.getDb_310Data(Constants.VAN_RETAILER_STATUS, this);
        }
}catch(Exception e){
    Log.e("errordashboard1",e.getMessage());
}

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {

                }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

try {
    if (shared_common_pref.getvalue(Constants.VAN_STOCK_LOADING) != null && !shared_common_pref.getvalue(Constants.VAN_STOCK_LOADING).equals("") && shared_common_pref.getvalue(Constants.VAN_STOCK_LOADING_TIME).equals(Common_Class.GetDateOnly())) {
        tvStockLoad.setTextColor(getResources().getColor(R.color.grey_500));
        tvStockLoad.setEnabled(false);
        tvStockLoad.setVisibility(View.GONE);
        tvStockTopUp.setVisibility(View.VISIBLE);
        tvStockUnload.setTextColor(getResources().getColor(R.color.black));
        tvStockUnload.setEnabled(true);

    }

    if (shared_common_pref.getvalue(Constants.VAN_STOCK_LOADING) == null || shared_common_pref.getvalue(Constants.VAN_STOCK_LOADING).equals("")) {
        tvStockUnload.setTextColor(getResources().getColor(R.color.grey_500));
        tvStockUnload.setEnabled(false);
        tvStockLoad.setVisibility(View.VISIBLE);
        tvStockLoad.setTextColor(getResources().getColor(R.color.black));
        tvStockLoad.setEnabled(true);
        tvStockTopUp.setVisibility(View.GONE);

    }

    if (!Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.Distributor_Id))) {
        common_class.getDb_310Data(Constants.VAN_RETAILER_STATUS, this);
    }
}catch(Exception e){
    Log.e("errordashboard",e.getMessage());
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
                common_class.showMsg(VanSalesDashboardRoute.dashboard_route, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }

    private void createTabFragment() {
        adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getSelectedTabPosition(), Retailer_Modal_ListFilter, RetType, this, "VanSalesDashboardRoute", "", "", "", "");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvVanSalPay:
                Intent payIntent=new Intent(VanSalesDashboardRoute.this, VanSalPaymentActivity.class);
                payIntent.putExtra("stkLoadAmt",-1.00);
                startActivity(payIntent);
                break;
            case R.id.tvStockView:
                startActivity(new Intent(VanSalesDashboardRoute.this, VanStockViewActivity.class));
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
            case R.id.tvStockLoad:
                common_class.getDb_310Data(Constants.STOCK_LEDGER, this);
                Intent load = new Intent(getApplicationContext(), VanSalStockLoadActivity.class);
                Shared_Common_Pref.SFA_MENU = "VanSalesDashboardRoute";
                Constants.VAN_SALES_MODE = Constants.VAN_STOCK_LOADING;
                startActivity(load);
                finish();
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.tvStockUnload:
                if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.VAN_STOCK_LOADING))) {
                    common_class.showMsg(this, "No Stock");
                } else {
                    Intent unload = new Intent(getApplicationContext(), VanSalStockUnLoadActivity.class);
                    Shared_Common_Pref.SFA_MENU = "VanSalesDashboardRoute";
                    Constants.VAN_SALES_MODE = Constants.VAN_STOCK_UNLOADING;
                    startActivity(unload);
                    overridePendingTransition(R.anim.in, R.anim.out);
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
            case R.id.toolbar_home:
                common_class.CommonIntentwithoutFinish(SFA_Activity.class);
                break;
            case R.id.tvStockTopUp:
                if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.VAN_STOCK_LOADING))) {
                    common_class.showMsg(this, "No Stock");
                } else {
                    Intent unload = new Intent(getApplicationContext(), VanSalStockTopUpActivity.class);
                    Shared_Common_Pref.SFA_MENU = "VanSalesDashboardRoute";
                    Constants.VAN_SALES_MODE = Constants.VAN_STOCK_TOPUP;
                    startActivity(unload);
                    overridePendingTransition(R.anim.in, R.anim.out);
                }
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
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {

        common_class.dismissCommonDialog(type);
        if (type == 2) {
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
            shared_common_pref.save(Constants.DistributorAdd, myDataset.get(position).getAddress());
            shared_common_pref.save(Constants.CusSubGrpErp, myDataset.get(position).getCusSubGrpErp());
            shared_common_pref.save(Constants.DistributorGst,myDataset.get(position).getDisGst());
            shared_common_pref.save(Constants.DistributorFSSAI,myDataset.get(position).getDisFssai());

            common_class.getDb_310Data(Constants.VAN_RETAILER_STATUS, this);
            getLastInvoiceData();
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

    @Override
    public void setDataToRouteObject(Object noticeArrayList, int position) {
    }

    @Override
    public void onResponseFailure(Throwable throwable) {


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
                    case STOCK_LEDGER:
                        Log.v(key + "bommu:", apiDataResponse);
                        break;
                    case Rout_List:
                        JSONArray routeArr = new JSONArray(apiDataResponse);
                        FRoute_Master.clear();
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
                        break;
                    case Constants.VAN_RETAILER_STATUS:
                        JSONObject jsonObject = new JSONObject(apiDataResponse);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            String outletCode = "";


                            for (int arr = 0; arr < jsonArray.length(); arr++) {
                                JSONObject arrObj = jsonArray.getJSONObject(arr);

                                int flag = arrObj.getInt("OrderFlg");
                                //  BTG=0,invoice-3,order-2,no order-1;
                                String sMode = flag == 0 ? "BTG" : flag == 3 ? "invoice" : flag == 2 ? "order" : flag == 4? "Van invoice " :"no order";

                                outletCode = outletCode + arrObj.getString("ListedDrCode") + sMode + ",";


                            }

                            shared_common_pref.save(Constants.VAN_RETAILER_STATUS, outletCode);

                            Log.v("statusList:", outletCode);

                            setPagerAdapter(false);
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
                adapter.notifyData(Retailer_Modal_ListFilter, tabLayout.getSelectedTabPosition(), txSearchRet.getText().toString(), RetType, "", "", "", "");
            } else {
                adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getSelectedTabPosition(), Retailer_Modal_ListFilter, RetType, this, "VanSalesDashboardRoute", "", "", "", "");
                viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        } catch (Exception e) {
            Log.v("DAshboard_Route:", e.getMessage());
        }
    }

    public static class AllDataFragment extends Fragment {
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
                        } /*else if (dashboard_route.route_text.getText().toString().equals("")) {
                            Toast.makeText(getActivity(), "Select The Route", Toast.LENGTH_SHORT).show();
                        }*/

//                        else if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.VAN_STOCK_LOADING))) {
//                            common_class.showMsg(getActivity(), "No Stock");
//                        }

                        else {

                            if (!Shared_Common_Pref.OutletCode.equalsIgnoreCase(mRetailer_Modal_ListFilter.get(position).getId())) {
                                shared_common_pref.clear_pref(Constants.LOC_SECONDARY_DATA);
                                shared_common_pref.clear_pref(Constants.LOC_INVOICE_DATA);

                            }

                            Shared_Common_Pref.Outler_AddFlag = "0";
                            Shared_Common_Pref.OutletName = mRetailer_Modal_ListFilter.get(position).getName().toUpperCase();
                            Shared_Common_Pref.OutletCode = mRetailer_Modal_ListFilter.get(position).getId();

                            shared_common_pref.save(Constants.Retailor_Address, mRetailer_Modal_ListFilter.get(position).getListedDrAddress1());
                            shared_common_pref.save(Constants.Retailor_ERP_Code, mRetailer_Modal_ListFilter.get(position).getERP_Code());
                            shared_common_pref.save(Constants.Retailor_Name_ERP_Code, mRetailer_Modal_ListFilter.get(position).getName().toUpperCase() /*+ "~"
                                    + mRetailer_Modal_ListFilter.get(position).getERP_Code()*/);

                            shared_common_pref.save(Constants.Retailor_PHNo, mRetailer_Modal_ListFilter.get(position).getPrimary_No());
                            Shared_Common_Pref.Freezer_Required = mRetailer_Modal_ListFilter.get(position).getFreezer_required();

                            Shared_Common_Pref.SFA_MENU = "VanSalesDashboardRoute";
                            //  common_class.CommonIntentwithoutFinish(Invoice_History.class);
                            //  getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                            LoadingMaterials();


                        }
                    } catch (Exception e) {

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
                public void LoadingMaterials(){
                    common_class.ProgressdialogShow(1, "Loading Material Details");
                    common_class.getProductDetails(getActiveActivity(), new OnLiveUpdateListener() {
                        @Override
                        public void onUpdate(String mode) {
                            common_class.CommonIntentwithoutFinish(Invoice_History.class);
                            //getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                            common_class.ProgressdialogShow(0, "");
                        }

                        @Override
                        public void onError(String msg) {
                            RetryLoadingProds();
                            common_class.ProgressdialogShow(0, "");
                        }
                    });
                }
                public void RetryLoadingProds(){
                    AlertDialogBox.showDialog(getContext(), "HAP SFA", "Product Loading Failed. Do you want to Retry ?", "Retry", "Cancel", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            if (common_class.isNetworkAvailable(getContext())) {
                                LoadingMaterials();
                                dialog.dismiss();
                            }
                        }
                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                }
            }));

        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            this.context = getContext();
            recyclerView = view.findViewById(R.id.recyclerView);
            updateData();
        }
    }



}
