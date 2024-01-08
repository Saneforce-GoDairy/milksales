package com.saneforce.godairy.Activity_Hap;

import static com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE;
import static com.saneforce.godairy.Common_Class.Constants.GroupFilter;
import static com.saneforce.godairy.Common_Class.Constants.VAN_STOCK;
import static com.saneforce.godairy.SFA_Activity.HAPApp.printUsrLog;
import static com.saneforce.godairy.SFA_Activity.HAPApp.setAppLogos;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Activity.ProcPrimaryOrderActivity;
import com.saneforce.godairy.Activity.Util.ListModel;
import com.saneforce.godairy.Activity.ViewActivity;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.Interface.onListItemClick;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.Dashboard_Order_Reports;
import com.saneforce.godairy.SFA_Activity.Dashboard_Route;
import com.saneforce.godairy.SFA_Activity.FeedbackActivitySFA;
import com.saneforce.godairy.SFA_Activity.GrnListActivity;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.SFA_Activity.InshopActivity;
import com.saneforce.godairy.SFA_Activity.Lead_Activity;
import com.saneforce.godairy.SFA_Activity.MyTeamActivity;
import com.saneforce.godairy.SFA_Activity.Nearby_Outlets;
import com.saneforce.godairy.SFA_Activity.Offline_Sync_Activity;
import com.saneforce.godairy.SFA_Activity.Outlet_Info_Activity;
import com.saneforce.godairy.SFA_Activity.POSActivity;
import com.saneforce.godairy.SFA_Activity.PaymentCollection;
import com.saneforce.godairy.SFA_Activity.PendingOutletsCategory;
import com.saneforce.godairy.SFA_Activity.PrimaryOrderActivity;
import com.saneforce.godairy.SFA_Activity.ProjectionCategorySelectActivity;
import com.saneforce.godairy.SFA_Activity.ReportsListActivity;
import com.saneforce.godairy.SFA_Activity.Reports_Distributor_Name;
import com.saneforce.godairy.SFA_Activity.SFA_Dashboard;
import com.saneforce.godairy.SFA_Activity.StockAuditCategorySelectActivity;
import com.saneforce.godairy.SFA_Activity.VanSalesDashboardRoute;
import com.saneforce.godairy.SFA_Activity.vwAllPrimaryOrders;
import com.saneforce.godairy.SFA_Adapter.RyclBrandListItemAdb;
import com.saneforce.godairy.SFA_Model_Class.Product_Details_Modal;
import com.saneforce.godairy.adapters.OffersAdapter;
import com.saneforce.godairy.common.DatabaseHandler;
import com.saneforce.godairy.common.LocationReceiver;
import com.saneforce.godairy.common.SANGPSTracker;
import com.saneforce.godairy.databinding.ActivitySfactivityBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SFA_Activity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI /*,Main_Model.MasterSyncView*/ {
    private ActivitySfactivityBinding binding;
    LinearLayout Lin_Route, Lin_Lead, Lin_Dashboard, Logout, SyncButon, linorders;
    Gson gson;
    private SANGPSTracker mLUService;
    private LocationReceiver myReceiver;
    private boolean mBound = false;
    public static final String UserDetail = "MyPrefs";
    private Common_Class common_class;
    private Shared_Common_Pref sharedCommonPref;
    private SharedPreferences UserDetails;
    private SharedPreferences CheckInDetails;
    public static final String CheckInDetail = "CheckInDetail";
    private DatabaseHandler db;
    private ImageView ivLogout, ivCalendar, ivProcureSync,btnCloseOffer;
    private LinearLayout llGridParent,linOffer;
    private OutletDashboardInfoAdapter cumulativeInfoAdapter;
    private List<Cumulative_Order_Model> cumulative_order_modelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView tvServiceOutlet, tvUniverseOutlet, tvNewSerOutlet, tvTotSerOutlet, tvExistSerOutlet, tvDate, tvTodayCalls, tvProCalls,
            tvCumTodayCalls, tvNewTodayCalls, tvCumProCalls, tvNewProCalls, tvAvgNewCalls, tvAvgTodayCalls, tvAvgCumCalls, tvUserName,
            tvPrimOrder, tvNoOrder, tvTotalValOrder, tvUpdTime, lblSlideNo,txview_all;
    private DatePickerDialog fromDatePickerDialog;
    public static String sfa_date = "";
    private MenuAdapter menuAdapter;
    private RecyclerView rvMenu, rvPrimOrd, ryclOffers;
    private List<ListModel> menuList = new ArrayList<>();
    private NumberFormat formatter = new DecimalFormat("##0.00");
    public static String updateTime = "";
    private ApiInterface apiService;
    int menuItem = 0;
    private Shared_Common_Pref shared_common_pref;

    TextView update_text;
    LinearLayout lnupdate_text;
    private AppUpdateManager appUpdateManager;
    public  static final int APP_UPDATE=100;
    int update_available = 0;
    int version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySfactivityBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);

        db = new DatabaseHandler(this);
        sharedCommonPref = new Shared_Common_Pref(SFA_Activity.this);
        CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(SFA_Activity.this);
        gson = new Gson();
        apiService = ApiClient.getClient().create(ApiInterface.class);

        linOffer=findViewById(R.id.linOffer);
        linOffer.setVisibility(View.GONE);
        ryclOffers= findViewById(R.id.ryclOffers);
        lblSlideNo =findViewById(R.id.lblSlideNo);
        btnCloseOffer =findViewById(R.id.btnCloseOffer);
        txview_all=findViewById(R.id.txview_all);
        init();
        setOnClickListener();
        txview_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vwall=new Intent(SFA_Activity.this, vwAllPrimaryOrders.class);
                startActivity(vwall);
            }
        });

        String sOffShown=sharedCommonPref.getvalue(Constants.DB_OfferShownOn,"");

        if (!Common_Class.GetDatewothouttime().equalsIgnoreCase(sOffShown)){
            sharedCommonPref.clear_pref(Constants.DB_Offer_NOTIFY);
        }

        btnCloseOffer.setOnClickListener(view -> linOffer.setVisibility(View.GONE));
        ivLogout.setImageResource(R.drawable.ic_baseline_logout_24);

        tvDate.setText("" + Common_Class.GetDatewothouttime());

        sfa_date = tvDate.getText().toString();
        String sUName = UserDetails.getString("SfName", "");
        String SFDesig = UserDetails.getString("SFDesig", "");
        String mProfileUrl = sharedCommonPref.getvalue("mProfile");

        Log.e("hgfhg", mProfileUrl);
        if (!Common_Class.isNullOrEmpty(mProfileUrl)) {
            String[] image = mProfileUrl.split("/"); // https://lactalisindia.salesjump.in/SalesForce_Profile_Img/TRMUMGR0009_1697262524.jpg
            if (image.length > 0 && image[(image.length - 1)].contains(".")) {
                loadImage(mProfileUrl);
            }
        }
        tvUserName.setText(sUName);
        binding.userName.setText(sUName);
        binding.designation.setText(SFDesig );
        tvUpdTime.setText("Last Updated On : " + updateTime);

        common_class.getProductDetails(this);
        getNoOrderRemarks();
        showDashboardData();
        getOfferNotify();
        getPrimaryData("All");
        common_class.getDb_310Data(Constants.GroupFilter, this);

        if (UserDetails.getString("DeptType", "").equalsIgnoreCase("1")) {
            ivProcureSync.setVisibility(View.VISIBLE);
            findViewById(R.id.cvOutletSummary).setVisibility(View.GONE);
            findViewById(R.id.cvTodayOrders).setVisibility(View.GONE);
            findViewById(R.id.cvSalesData).setVisibility(View.GONE);
            findViewById(R.id.cvCalls).setVisibility(View.GONE);
            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Distributor_Id)))
                common_class.getDb_310Data(Constants.Distributor_List, this);
            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.PROCUR_MENU)))
                callDynamicmenu();
            else {
                menuList.clear();
                Type userType = new TypeToken<ArrayList<ListModel>>() {
                }.getType();
                menuList = gson.fromJson(sharedCommonPref.getvalue(Constants.PROCUR_MENU), userType);
                setMenuAdapter();
            }
        }
        else {
            ivProcureSync.setVisibility(View.GONE);
            switch (sharedCommonPref.getvalue(Constants.LOGIN_TYPE)) {
                case Constants.CHECKIN_TYPE:
                    menuList.add(new ListModel("", "Primary Order", "", "", "", R.drawable.ic_primary_order_sf));
                    menuList.add(new ListModel("", "Secondary Order", "", "", "", R.drawable.ic_secondary_order_sf));
                    // menuList.add(new ListModel("", "Van Sales", "", "", "", R.drawable.ic_outline_local_shipping_24));
                    menuList.add(new ListModel("", "Outlets", "", "", "", R.drawable.ic_outlets_sf));
                    if (UserDetails.getString("Sfcode", "").equals("MGR8171")) {
                        menuList.add(new ListModel("", "Approve Outlets", "", "", "", R.drawable.ic_approve_outlets));
                    }
                    menuList.add(new ListModel("", "Nearby Outlets", "", "", "", R.drawable.ic_near_outlets_sf));
                    menuList.add(new ListModel("", "Reports", "", "", "", R.drawable.ic_reports_sf));
                    menuList.add(new ListModel("", "Distributor", "", "", "", R.drawable.ic_distributor_sf));
                    menuList.add(new ListModel("", "My Team", "", "", "", R.drawable.ic_my_team_sf));
                    //menuList.add(new ListModel("", "Projection", "", "", "", R.drawable.ic_projection_sf));
                    // menuList.add(new ListModel("", "Stock Audit", "", "", "", R.drawable.ic_stock_audit));
                   // menuList.add(new ListModel("", "Inshop", "", "", "", R.drawable.ic_inshop_sf));
                    menuList.add(new ListModel("", "Feedback", "", "", "", R.drawable.ic_feedback_sf));
                    if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Distributor_Id)))
                        common_class.getDb_310Data(Constants.Distributor_List, this);
                    break;

                case Constants.DISTRIBUTER_TYPE:
                    menuList.add(new ListModel("", "Primary Order", "", "", "", R.drawable.ic_projection_sf));
                    menuList.add(new ListModel("", "Secondary Order", "", "", "", R.drawable.ic_secondary_order_sf));
                    // menuList.add(new ListModel("", "Van Sales", "", "", "", R.drawable.ic_outline_local_shipping_24));
                    //menuList.add(new ListModel("", "Pay Bills", "", "", "", R.drawable.ic_secondary_order_sf));
                    menuList.add(new ListModel("", "Outlets", "", "", "", R.drawable.ic_outlets_sf));
                    menuList.add(new ListModel("", "Nearby Outlets", "", "", "", R.drawable.ic_near_outlets_sf));
                    menuList.add(new ListModel("", "Reports", "", "", "", R.drawable.ic_reports_sf));
                    //menuList.add(new ListModel("", "Counter Sales", "", "", "", R.drawable.ic_outline_assignment_48));
                    //menuList.add(new ListModel("", "GRN", "", "", "", R.drawable.ic_outline_assignment_turned_in_24));
                    menuList.add(new ListModel("", "Feedback", "", "", "", R.drawable.ic_feedback_sf));
//                  menuList.add(new ListModel("", "Inshop", "", "", "", R.drawable.ic_inshop));
                    common_class.getPOSProduct(this);
                    common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);
                    break;

                default:
                    menuList.add(new ListModel("", "Secondary Order", "", "", "", R.drawable.ic_secondary_order_sf));
                    menuList.add(new ListModel("", "Van Sales", "", "", "", R.drawable.ic_outline_local_shipping_24));
                    menuList.add(new ListModel("", "Outlets", "", "", "", R.drawable.ic_outlets_sf));
                    menuList.add(new ListModel("", "Nearby Outlets", "", "", "", R.drawable.ic_near_outlets_sf));
                    menuList.add(new ListModel("", "Reports", "", "", "", R.drawable.ic_reports_sf));
                    menuList.add(new ListModel("", "POS", "", "", "", R.drawable.ic_outline_assignment_48));
                    menuList.add(new ListModel("", "GRN", "", "", "", R.drawable.ic_outline_assignment_turned_in_24));
                    menuList.add(new ListModel("", "Feedback", "", "", "", R.drawable.ic_feedback_sf));
//                  menuList.add(new ListModel("", "Inshop", "", "", "", R.drawable.ic_inshop));
                    break;
            }
            setMenuAdapter();
        }
        getNotify();

        update_text = findViewById(R.id.update_available);
        lnupdate_text = findViewById(R.id.updateAvailable);
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        checkUpdates();
        update_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo result) {

                        if (result.updateAvailability() == UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                            try {
                                appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, SFA_Activity.this, APP_UPDATE);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }else
                            lnupdate_text.setVisibility(View.GONE);
                    }
                });
            }
        });
        binding.logout.setOnClickListener(v -> {
            db.deleteAllMasterData();
            shared_common_pref.clearAll();
            UserDetails.edit().clear().apply();
            finishAffinity();
            Intent Dashboard = new Intent(this, Login.class);
            startActivity(Dashboard);
        });
    }

    private void checkUpdates() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {

                if (result.updateAvailability() == UPDATE_AVAILABLE ){

                    version = result.availableVersionCode();
                    printUsrLog("Version", String.valueOf(version));

                    lnupdate_text.setVisibility(View.VISIBLE);

                    /*if (Constant.getInstance().getSetup(StringConstants.IS_FORCE_UPDATE, 0, new DBController(MainActivity.this)) == version) {
                        try {
                            if(result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                                appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, Dashboard.this, APP_UPDATE);
                            }else{
                                appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.FLEXIBLE, Dashboard.this, APP_UPDATE);
                            }
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    } else {
                        update_available = 1;
                    }
                    */
                }else
                    lnupdate_text.setVisibility(View.GONE);
            }
        });
    }
    private void loadImage(String mProfileImage){
        Glide.with(this)
                .load(mProfileImage)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.profileImg);
    }

    private void getNotify() {
        Shared_Common_Pref mShared_common_pref = new Shared_Common_Pref(this);
        if (com.saneforce.godairy.Activity_Hap.Common_Class.isNullOrEmpty(mShared_common_pref.getvalue(Constants.DB_TWO_GET_NOTIFY))) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonArray> rptCall = apiInterface.getDataArrayListDist("get/dist_notify",
                    UserDetails.getString("Divcode", ""),
                    UserDetails.getString("Sfcode", ""), sharedCommonPref.getvalue(Constants.Distributor_Id), "", null);
            rptCall.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    try {
                        JsonArray res = response.body();
                        assignGetNotify(res);
                        mShared_common_pref.save(Constants.DB_TWO_GET_NOTIFY, gson.toJson(response.body()));
                    } catch (Exception e) {}
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {}
            });
        } else {
            Type userType = new com.google.common.reflect.TypeToken<JsonArray>() {}.getType();
            JsonArray arr = (gson.fromJson(mShared_common_pref.getvalue(Constants.DB_TWO_GET_NOTIFY), userType));
            assignGetNotify(arr);
        }
    }

    void assignGetNotify(JsonArray res) {
        TextView txt = findViewById(R.id.MRQtxt);
        txt.setText("");
        txt.setVisibility(View.GONE);
        String sMsg = "";
        for (int il = 0; il < res.size(); il++) {
            JsonObject Itm = res.get(il).getAsJsonObject();
            sMsg += Itm.get("NtfyMsg").getAsString();
        }
        if (!sMsg.equalsIgnoreCase("")) {
            txt.setText(Html.fromHtml(sMsg));
            txt.setVisibility(View.VISIBLE);
            txt.setSelected(true);
        }
    }

    void setMenuAdapter() {
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 3);
        rvMenu.setLayoutManager(manager);

        menuAdapter = new MenuAdapter(this, menuList, new AdapterOnClick() {
            @Override
            public void onIntentClick(int pos) {
                switch (menuList.get(pos).getFormName()) {
                    case "Nearby Outlets":
                        Intent intent = new Intent(SFA_Activity.this, Nearby_Outlets.class);
                        intent.putExtra("menu", "menu");
                        startActivity(intent);
                        break;

                    case "GRN":
                        common_class.CommonIntentwithNEwTask(GrnListActivity.class);
                        break;

                    case "Counter Sales":
                        common_class.CommonIntentwithNEwTask(POSActivity.class);
                        break;

                    case "Primary Order":
                        common_class.getDb_310Data(Constants.PrimaryTAXList, SFA_Activity.this);
                        break;

                    case "Pay Bills":
                        startActivity(new Intent(SFA_Activity.this, PaymentCollection.class));
                        break;
                    case "Secondary Order":
                        sharedCommonPref.save(Shared_Common_Pref.DCRMode, "SC");
                        startActivity(new Intent(SFA_Activity.this, Dashboard_Route.class));
                        break;

                    case "Sales Return":
                        sharedCommonPref.save(Shared_Common_Pref.DCRMode, "SR");
                        startActivity(new Intent(SFA_Activity.this, Dashboard_Route.class));
                        break;

                    case "Van Sales":
                        if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.VAN_STOCK_LOADING)))
                            common_class.getDb_310Data(Constants.VAN_STOCK, SFA_Activity.this);
                        sharedCommonPref.save(Shared_Common_Pref.DCRMode, "Van Sales");
                        startActivity(new Intent(SFA_Activity.this, VanSalesDashboardRoute.class));

                        break;

                    case "Outlets":
                        common_class.CommonIntentwithNEwTask(Outlet_Info_Activity.class);
                        break;

                    case "Distributor":
                        common_class.CommonIntentwithNEwTask(Reports_Distributor_Name.class);
                        break;

                    case "Approve Outlets":
                        common_class.CommonIntentwithNEwTask(PendingOutletsCategory.class);
                        break;

                    case "Franchise":
                        common_class.CommonIntentwithNEwTask(Reports_Distributor_Name.class);
                        break;

                    case "Reports":
                        common_class.CommonIntentwithNEwTask(ReportsListActivity.class);
                        // common_class.CommonIntentwithNEwTask(Reports_Outler_Name.class);
                        break;

                    case "My Team":
                        common_class.CommonIntentwithNEwTask(MyTeamActivity.class);
                        break;

                    case "Projection":
                        getProjectionProductDetails(SFA_Activity.this);
                        break;

                    case "Stock Audit":
                        getStockAuditDetails(SFA_Activity.this);
                        break;

                    case "Sync":
                        saveFormData(-1);
                        common_class.ProgressdialogShow(1, "");
                        break;

                    case "Inshop":
                        startActivity(new Intent(SFA_Activity.this, InshopActivity.class));
                        break;

                    case "Feedback":
                        startActivity(new Intent(SFA_Activity.this, FeedbackActivitySFA.class));
                        break;

                    default:
                        if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(menuList.get(pos).getFormName())))
                            saveFormData(pos);
                        else
                            navigateFormScreen(pos);
                }
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
        rvMenu.setAdapter(menuAdapter);
    }

    void navigateFormScreen(int pos) {
        Intent ii = new Intent(SFA_Activity.this, ViewActivity.class);
        ii.putExtra("btn_need", menuList.get(pos).getTargetForm());
        ii.putExtra("frmid", menuList.get(pos).getFormid());
        ii.putExtra("frmname", menuList.get(pos).getFormName());
        startActivity(ii);
    }

    private void saveFormData(int pos) {
        try {
            ArrayList<Common_Model> formList = new ArrayList<>();

            for (int i = 0; i < menuList.size(); i++) {
                if (!menuList.get(i).getFormid().equalsIgnoreCase(""))
                    formList.add(new Common_Model(menuList.get(i).getFormName(), menuList.get(i).getFormid()));
            }

            formList.add(new Common_Model("New Farmer", "2"));
            formList.add(new Common_Model("Existing Farmer", "3"));
            formList.add(new Common_Model("Competitor Activity", "12"));
            formList.add(new Common_Model("General Activities", "8"));

            if (formList.size() == menuItem) {
                common_class.ProgressdialogShow(0, "");
                menuItem = 0;
                if (pos >= 0) {
                    navigateFormScreen(pos);
                }
                return;
            } else {
                String formid = formList.get(menuItem).getName();
                JSONObject json = new JSONObject();
                json.put("slno", formList.get(menuItem).getId());
                String formname = formList.get(menuItem).getName();
                Log.v("printing_sf_code", json.toString());
                Call<ResponseBody> approval = apiService.getView(json.toString());

                approval.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Log.v("procure:" + formname + ":" + formid + ":", response.body().byteStream() + "");
                            InputStreamReader ip = null;
                            StringBuilder is = new StringBuilder();
                            String line = null;
                            try {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);

                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                }
                                sharedCommonPref.save(formname, is.toString());
                                menuItem = menuItem + 1;
                                saveFormData(pos);
                                Log.v("sizeCheck:", "" + formList.size() + ":current:" + menuItem);
                            } catch (Exception e) {
                                common_class.ProgressdialogShow(0, "");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        common_class.ProgressdialogShow(0, "");
                    }
                });
            }
        } catch (Exception e) {
        }
    }

    public void getProjectionProductDetails(Activity activity) {

        if (common_class.isNetworkAvailable(activity)) {
            UserDetails = activity.getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

            DatabaseHandler db = new DatabaseHandler(activity);
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("SF", UserDetails.getString("Sfcode", ""));
                jParam.put("Stk", sharedCommonPref.getvalue(Constants.Distributor_Id));
                // jParam.put("outletId", Shared_Common_Pref.OutletCode);
                jParam.put("div", UserDetails.getString("Divcode", ""));
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                service.getDataArrayList("get/projectionprodgroup", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("Projec_grp_List", response.body().toString());
                        db.deleteMasterData(Constants.ProjectionProdGroups_List);
                        db.addMasterData(Constants.ProjectionProdGroups_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.v("Projec_Product_List_ex", t.getMessage());
                        t.printStackTrace();
                    }
                });
                service.getDataArrayList("get/projectionprodtypes", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("Projec_type_List", response.body().toString());
                        db.deleteMasterData(Constants.ProjectionProdTypes_List);
                        db.addMasterData(Constants.ProjectionProdTypes_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.v("Projec_Product_List_ex", t.getMessage());
                        t.printStackTrace();
                    }
                });
                service.getDataArrayList("get/projectionprodcate", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("Projec_cat_List", response.body().toString());
                        db.deleteMasterData(Constants.Projection_Category_List);
                        db.addMasterData(Constants.Projection_Category_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.v("Projec_Product_List_ex", t.getMessage());
                        t.printStackTrace();
                    }
                });
                service.getDataArrayList("get/projectionproddets", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("Projec_Product_List", response.body().toString());
                        db.deleteMasterData(Constants.Projection_Product_List);
                        db.addMasterData(Constants.Projection_Product_List, response.body());
                        common_class.CommonIntentwithNEwTask(ProjectionCategorySelectActivity.class);
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.v("Projec_Product_fail", t.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.v("Projec_Product_List_ex", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void getStockAuditDetails(Activity activity) {
        if (common_class.isNetworkAvailable(activity)) {
            UserDetails = activity.getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
            DatabaseHandler db = new DatabaseHandler(activity);
            try {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                service.getStockAudit("get/auditprodgroup", UserDetails.getString("Divcode", "")).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try {
                            Log.v("stockAudit_grp_List", response.body().toString());
                            db.deleteMasterData(Constants.StockAudit_GroupsList);
                            db.addMasterData(Constants.StockAudit_GroupsList, response.body().toString());
                        } catch (Exception e) {
                            Log.v("StockAudit:Ex:catch", e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.v("StockAudit:Ex", t.getMessage());
                    }
                });
                service.getStockAudit("get/auditprodtypes", UserDetails.getString("Divcode", "")).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("stockAudit_type_List", response.body().toString());
                        db.deleteMasterData(Constants.StockAudit_Types_List);
                        db.addMasterData(Constants.StockAudit_Types_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                    }
                });
                service.getStockAudit("get/auditprodcate", UserDetails.getString("Divcode", "")).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("stockAudit_cat_List", response.body().toString());
                        db.deleteMasterData(Constants.StockAudit_Category_List);
                        db.addMasterData(Constants.StockAudit_Category_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                service.getStockAudit("get/auditproddets", UserDetails.getString("Divcode", "")).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v("stockAudit_Product_List", response.body().toString());
                        db.deleteMasterData(Constants.StockAudit_Product_List);
                        db.addMasterData(Constants.StockAudit_Product_List, response.body());

                        common_class.CommonIntentwithNEwTask(StockAuditCategorySelectActivity.class);
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.v("Projec_Product_fail", t.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.v("Projec_Product_List_ex", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void setOnClickListener() {
        ivCalendar.setOnClickListener(this);
        SyncButon.setOnClickListener(this);
        Lin_Route.setOnClickListener(this);
        Lin_Lead.setOnClickListener(this);
        Lin_Dashboard.setOnClickListener(this);
        linorders.setOnClickListener(this);
        Logout.setOnClickListener(this);
        ivLogout.setOnClickListener(this);
        ivProcureSync.setOnClickListener(this);
    }

    private void getCumulativeDataFromAPI(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("success")) {
                JSONArray jsonArray = jsonObject.getJSONArray("Data");
                int todayCall = 0, cumTodayCall = 0, newTodayCall = 0, proCall = 0, cumProCall = 0, newProCall = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    todayCall = jsonObject1.getInt("TC");
                    cumTodayCall = jsonObject1.getInt("CTC");
                    newTodayCall = jsonObject1.getInt("NTC");
                    proCall = jsonObject1.getInt("PC");
                    cumProCall = jsonObject1.getInt("CPC");
                    newProCall = jsonObject1.getInt("NPC");

                    tvTodayCalls.setText("" + todayCall);
                    tvCumTodayCalls.setText("" + cumTodayCall);
                    tvNewTodayCalls.setText("" + newTodayCall);
                    tvProCalls.setText("" + proCall);
                    tvCumProCalls.setText("" + cumProCall);
                    tvNewProCalls.setText("" + newProCall);
                }

                if (todayCall > 0 || proCall > 0)
                    tvAvgTodayCalls.setText("" + (todayCall + proCall) / 2);
                if (cumTodayCall > 0 || cumProCall > 0)
                    tvAvgCumCalls.setText("" + (cumTodayCall + cumProCall) / 2);
                if (newTodayCall > 0 || newProCall > 0)
                    tvAvgNewCalls.setText("" + (newTodayCall + newProCall) / 2);
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());

        }
    }

    private void getNoOrderRemarks() {
        try {
            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                JSONObject HeadItem = new JSONObject();
                HeadItem.put("Div", Shared_Common_Pref.Div_Code);
                service.getDataArrayList("get/noordrmks", HeadItem.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        db.deleteMasterData("HAPNoOrdRmks");
                        db.addMasterData("HAPNoOrdRmks", response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
            } else {
                common_class.showMsg(this, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());
        }
    }

    public void init() {
        tvTodayCalls = findViewById(R.id.tvTodayCalls);
        tvCumTodayCalls = findViewById(R.id.tvCumTodayCalls);
        tvNewTodayCalls = findViewById(R.id.tvNewTodayCalls);

        tvProCalls = findViewById(R.id.tvProCalls);
        tvCumProCalls = findViewById(R.id.tvCumProCalls);
        tvNewProCalls = findViewById(R.id.tvNewProCalls);

        tvAvgTodayCalls = findViewById(R.id.tvAvgTodayCalls);
        tvAvgCumCalls = findViewById(R.id.tvAvgCumCalls);
        tvAvgNewCalls = findViewById(R.id.tvAvgNewCalls);

        ivCalendar = (ImageView) findViewById(R.id.ivSFACalendar);
        tvDate = (TextView) findViewById(R.id.tvSFADate);
        ivProcureSync = (ImageView) findViewById(R.id.ivProcureSync);

        tvServiceOutlet = (TextView) findViewById(R.id.tvServiceOutlet);
        tvUniverseOutlet = (TextView) findViewById(R.id.tvUniverseOutlet);

        tvNewSerOutlet = (TextView) findViewById(R.id.tvNewServiceOutlet);
        tvTotSerOutlet = (TextView) findViewById(R.id.tvTotalServiceOutlet);
        tvExistSerOutlet = (TextView) findViewById(R.id.tvExistServiceOutlet);

        recyclerView = findViewById(R.id.gvOutlet);

        llGridParent = findViewById(R.id.lin_gridOutlet);
        tvUserName = findViewById(R.id.tvUserName);

        ivLogout = findViewById(R.id.toolbar_home);
        Lin_Route = findViewById(R.id.Lin_Route);
        SyncButon = findViewById(R.id.SyncButon);
        Lin_Lead = findViewById(R.id.Lin_Lead);
        Lin_Dashboard = findViewById(R.id.Lin_Dashboard);
        linorders = findViewById(R.id.linorders);
        Logout = findViewById(R.id.Logout);
        rvMenu = findViewById(R.id.rvMenu);
        tvTotalValOrder = findViewById(R.id.tvTotValOrd);
        tvNoOrder = findViewById(R.id.tvNoOrd);
        tvPrimOrder = findViewById(R.id.tvPrimOrd);
        tvUpdTime = findViewById(R.id.tvUpdTime);
        rvPrimOrd = findViewById(R.id.rvPrimOrder);

        Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
        Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivProcureSync:
                saveFormData(-1);
                common_class.ProgressdialogShow(1, "");
                break;

            case R.id.linorders:
                common_class.CommonIntentwithNEwTask(Dashboard_Order_Reports.class);
                break;

            case R.id.ivSFACalendar:
                Calendar newCalendar = Calendar.getInstance();
                fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        tvDate.setText("" + year + "-" + month + "-" + dayOfMonth);
                        sfa_date = tvDate.getText().toString();
                        showDashboardData();
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();
                break;

            case R.id.Lin_Dashboard:
                common_class.CommonIntentwithNEwTask(SFA_Dashboard.class);
                break;

            case R.id.Lin_Lead:
                common_class.CommonIntentwithNEwTask(Lead_Activity.class);
                break;

            case R.id.toolbar_home:
                AlertDialogBox.showDialog(SFA_Activity.this, HAPApp.Title, "Are You Sure Want to Logout?", "OK", "Cancel", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        sharedCommonPref.save("ActivityStart", "false");
                        boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                        sharedCommonPref.clear_pref(Constants.Distributor_name);
                        sharedCommonPref.clear_pref(Constants.Distributor_Id);

                        if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.CHECKIN_TYPE) && CheckIn) {
                            Intent intent = new Intent(SFA_Activity.this, Dashboard_Two.class);
                            intent.putExtra("Mode", "CIN");
                            startActivity(intent);
                            finish();
                        } else {
                            SharedPreferences CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = UserDetails.edit();
                            editor.putBoolean("Login", false);
                            editor.apply();
                            CheckInDetails.edit().clear().commit();setAppLogos();
                            finishAffinity();
                        }
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                break;

            case R.id.Lin_Route:
                sharedCommonPref.save(sharedCommonPref.DCRMode, "");
                common_class.CommonIntentwithNEwTask(Dashboard_Route.class);
                break;

            case R.id.SyncButon:
                Shared_Common_Pref.Sync_Flag = "10";
                common_class.CommonIntentwithNEwTask(Offline_Sync_Activity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equalsIgnoreCase(Constants.CHECKIN_TYPE)) {
            finish();
        }
    }

    private void getOfferNotify() {
        if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.DB_Offer_NOTIFY))) {
            Map<String, String> QueryString = new HashMap<>();
            QueryString.put("axn", "get/offernotify");
            QueryString.put("CusCode", UserDetails.getString("Sfcode", ""));
            QueryString.put("divisionCode", UserDetails.getString("Divcode", ""));
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonArray> rptCall = apiInterface.getDataArrayList(QueryString, null);
            rptCall.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    try {
                        JsonArray res = response.body();
                        Log.d("getOfferNotify", String.valueOf(response.body()));
                        //  Log.d("NotifyMsg", response.body().toString());
                        JSONArray sArr=new JSONArray(String.valueOf(response.body()));
                        assignOffGetNotify(sArr);
                        sharedCommonPref.save(Constants.DB_Offer_NOTIFY, gson.toJson(response.body()));
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Log.d("Tag", String.valueOf(t));
                }
            });

        } else {
            try {
                JSONArray sArr=new JSONArray(String.valueOf(sharedCommonPref.getvalue(Constants.DB_Offer_NOTIFY)));
                //assignOffGetNotify(sArr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void assignOffGetNotify(JSONArray res) {
        JSONArray fRes= res;
        if (fRes.length()>0){
            LinearLayoutManager TypgridlayManager = new LinearLayoutManager(this);
            TypgridlayManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            ryclOffers.setLayoutManager(TypgridlayManager);
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(ryclOffers);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ryclOffers.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                        LinearLayoutManager layoutManager = ((LinearLayoutManager)ryclOffers.getLayoutManager());
                        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                        lblSlideNo.setText((firstVisiblePosition+1)+"/"+fRes.length());
                    }
                });
            }else{
                ryclOffers.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        LinearLayoutManager layoutManager = ((LinearLayoutManager)ryclOffers.getLayoutManager());
                        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                        lblSlideNo.setText((firstVisiblePosition+1)+"/"+fRes.length());
                    }
                });
            }
            OffersAdapter TyplistItems = new OffersAdapter(fRes, this, new onListItemClick() {
                @Override
                public void onItemClick(JSONObject item) {
                    try {
                        //GetJsonData(String.valueOf(db.getMasterData(Constants.Category_List)), "1", item.getString("id"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            ryclOffers.setAdapter(TyplistItems);
            linOffer.setVisibility(View.VISIBLE);
            sharedCommonPref.save(Constants.DB_OfferShownOn,Common_Class.GetDatewothouttime());
        }
    }
    void showDashboardData() {
        common_class.getDb_310Data(Constants.CUMULATIVEDATA, this);
        //common_class.getDb_310Data(Constants.SERVICEOUTLET, this);
        common_class.getDb_310Data(Constants.OUTLET_SUMMARY, this);
        common_class.getDb_310Data(Constants.SFA_DASHBOARD, this);
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {
                switch (key) {
                    case VAN_STOCK:
                        JSONObject stkObj = new JSONObject(apiDataResponse);

                        if (stkObj.getBoolean("success")) {
                            JSONArray arr = stkObj.getJSONArray("Data");
                            List<Product_Details_Modal> stkList = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                stkList.add(new Product_Details_Modal(obj.getString("PCode"), obj.getInt("Cr"), obj.getInt("Dr"), (obj.getInt("Bal"))));
                            }
                            sharedCommonPref.save(Constants.VAN_STOCK_LOADING, gson.toJson(stkList));
                        }
                        Log.v(key, apiDataResponse);
                        break;

                    case GroupFilter:
                        Log.v(key, apiDataResponse);

                        JSONObject filterObj = new JSONObject(apiDataResponse);
                        JSONObject obj1 = new JSONObject();
                        obj1.put("name", "All");
                        obj1.put("GroupCode", "All");
                        JSONArray arr1 = new JSONArray();
                        arr1.put(obj1);

                        if (filterObj.getBoolean("success")) {
                            for (int i = 0; i < filterObj.getJSONArray("Data").length(); i++)
                                arr1.put(filterObj.getJSONArray("Data").getJSONObject(i));
                        }

                        rvPrimOrd.setAdapter(new RyclBrandListItemAdb(arr1, this, new onListItemClick() {
                            @Override
                            public void onItemClick(JSONObject item) {
                                try {
                                    getPrimaryData(item.getString("GroupCode"));
                                } catch (Exception e) {
                                    Log.v("primHist:", e.getMessage());
                                }
                            }
                        }));
                        break;

                    case Constants.PRIMARY_DASHBOARD:
                        Log.v(key + ":", apiDataResponse);
                        JSONObject obj = new JSONObject(apiDataResponse);
                        if (obj.getBoolean("success")) {
                            updateTime = Common_Class.GetDate();
                            tvUpdTime.setText("Last Updated On : " + updateTime);
                            JSONArray arr = obj.getJSONArray("Data");
                            tvPrimOrder.setText("" + arr.getJSONObject(0).getInt("Ordr"));
                            tvNoOrder.setText("" + arr.getJSONObject(0).getInt("NoOrdr"));
                            tvTotalValOrder.setText("" + formatter.format(arr.getJSONObject(0).getDouble("Val")));
                        }
                        break;

                    case Constants.PrimaryTAXList:
                        sharedCommonPref.save(Constants.PrimaryTAXList, apiDataResponse);
                        if (UserDetails.getString("DeptType", "").equalsIgnoreCase("1"))
                            common_class.CommonIntentwithoutFinish(ProcPrimaryOrderActivity.class);
                        else{
                            Intent intent = new Intent(SFA_Activity.this, PrimaryOrderActivity.class);
                            if(sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equalsIgnoreCase(Constants.DISTRIBUTER_TYPE)){
                                intent.putExtra("Mode", "order_view");
                            }
                            startActivity(intent);
                            //common_class.CommonIntentwithoutFinish(PrimaryOrderActivity.class);
                        }
                        overridePendingTransition(R.anim.in, R.anim.out);
                        break;

                    case Constants.CUMULATIVEDATA:
                        getCumulativeDataFromAPI(apiDataResponse);
                        break;

                    case Constants.SERVICEOUTLET:
                        JSONObject jsonObject = new JSONObject(apiDataResponse);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tvTotSerOutlet.setText("" + jsonArray.getJSONObject(i).getInt("totalcnt"));
                                tvNewSerOutlet.setText("" + jsonArray.getJSONObject(i).getInt("newcnt"));
                                tvExistSerOutlet.setText("" +
                                        (jsonArray.getJSONObject(i).getInt("totalcnt") - jsonArray.getJSONObject(i).getInt("newcnt")));
                            }
                        }
                        break;

                    case Constants.OUTLET_SUMMARY:
                        JSONObject outletObj = new JSONObject(apiDataResponse);
                        if (outletObj.getBoolean("success")) {
                            JSONArray jsonArray = outletObj.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tvServiceOutlet.setText("" + jsonArray.getJSONObject(i).getInt("ServiceOutlets"));
                                tvUniverseOutlet.setText("" + jsonArray.getJSONObject(i).getInt("UniverseOutlets"));
                            }
                        }
                        break;

                    case Constants.SFA_DASHBOARD:
                        JSONObject sfaObj = new JSONObject(apiDataResponse);
                        if (sfaObj.getBoolean("success")) {
                            JSONArray jsonArray = sfaObj.getJSONArray("Data");
                            cumulative_order_modelList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                cumulative_order_modelList.add(new Cumulative_Order_Model(jsonObject1.getString("Doc_Special_SName"),
                                        jsonObject1.getInt("cnt")));
                            }

                            cumulativeInfoAdapter = new OutletDashboardInfoAdapter(SFA_Activity.this,
                                    cumulative_order_modelList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(cumulativeInfoAdapter);
                        }
                        break;
                }
            }
        } catch (Exception e) {
            Log.v(key + "Ex:", e.getMessage());
        }
    }

    void getPrimaryData(String groupType) {
        JsonObject data = new JsonObject();
        data.addProperty("Grpcode", groupType);
        common_class.getDb_310Data(Constants.PRIMARY_DASHBOARD, SFA_Activity.this, data);
    }

    public void callDynamicmenu() {
        JSONObject json = new JSONObject();
        try {
            json.put("div", UserDetails.getString("Divcode", ""));
            Log.v("printing_sf_code", json.toString());
            Call<ResponseBody> approval = apiService.getMenu(json.toString());

            approval.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track", response.body().byteStream() + "");
                        JSONObject jsonObject = null;
                        String jsonData = null;

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }
                            menuList.clear();
                            menuList.add(new ListModel("", "Primary Order", "", "", "", R.drawable.ic_outline_add_chart_48));
                            Log.v("printing_dynamic_menu", is.toString());
                            JSONArray js = new JSONArray(is.toString());
                            for (int i = 0; i < js.length(); i++) {
                                JSONObject jj = js.getJSONObject(i);
                                menuList.add(new ListModel(jj.getString("Frm_ID"), jj.getString("Frm_Name"), jj.getString("Frm_Table"), jj.getString("Targt_Frm"), jj.getString("Frm_Type"), R.drawable.ic_outline_assignment_48));
                            }
                            //  menuList.add(new ListModel("", "Sync", "", "", "", R.drawable.ic_round_sync_24));
                            sharedCommonPref.save(Constants.PROCUR_MENU, gson.toJson(menuList));
                            setMenuAdapter();
                        } catch (Exception ignored) {
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });

        } catch (Exception ignored) {
        }
    }


    public class OutletDashboardInfoAdapter extends RecyclerView.Adapter<OutletDashboardInfoAdapter.MyViewHolder> {
        Context context;
        private List<Cumulative_Order_Model> listt;

        public OutletDashboardInfoAdapter(Context applicationContext, List<Cumulative_Order_Model> list) {
            this.context = applicationContext;
            this.listt = list;
        }

        @Override
        public OutletDashboardInfoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.outlet_dashboardinfo_recyclerview, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(OutletDashboardInfoAdapter.MyViewHolder holder, int position) {
            try {
                try {
                    holder.tvDesc.setText("" + listt.get(position).getDesc());
                    holder.tvValue.setText("" + listt.get(position).getValue());
//                    holder.pbVisitCount.setMax(listt.get(position).getValue());
//                    holder.pbVisitCount.setProgress(position);
                } catch (Exception e) {
                    Log.e("adaptergetView: ", e.getMessage());
                }
            } catch (Exception e) {
            }
        }

        @Override
        public int getItemCount() {
            return listt.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvDesc, tvValue;
            ProgressBar pbVisitCount;
            public MyViewHolder(View view) {
                super(view);
                tvDesc = view.findViewById(R.id.tvDesc);
                tvValue = view.findViewById(R.id.tvValue);
                pbVisitCount = view.findViewById(R.id.pbVisitCount);
            }
        }
    }

    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
        Context context;
        private List<ListModel> listt;
        AdapterOnClick adapterOnClick;

        public MenuAdapter(Context applicationContext, List<ListModel> list, AdapterOnClick adapterOnClick) {
            this.context = applicationContext;
            this.listt = list;
            this.adapterOnClick = adapterOnClick;
        }

        @Override
        public MenuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.sfa_menu_layout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(MenuAdapter.MyViewHolder holder, int position) {
            try {
                try {
                    holder.tvName.setText("" + listt.get(position).getFormName());
                    holder.ivIcon.setImageResource(listt.get(position).getIcon());

                    holder.llParent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapterOnClick.onIntentClick(position);
                        }
                    });

                } catch (Exception e) {
                    Log.e("adaptergetView: ", e.getMessage());
                }


            } catch (Exception ignored) {
            }
        }

        @Override
        public int getItemCount() {
            return listt.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvName;
            ImageView ivIcon;
            LinearLayout llParent;
            public MyViewHolder(View view) {
                super(view);
                tvName = view.findViewById(R.id.tvMenuName);
                ivIcon = view.findViewById(R.id.ivMenuIcon);
                llParent = view.findViewById(R.id.llMenu);
            }
        }
    }


    private final ServiceConnection mServiceConection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLUService = ((SANGPSTracker.LocationBinder) service).getLocationUpdateService(getApplicationContext());
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLUService = null;
            mBound = false;
        }
    };
}

  /*  private void getDashboardDataFromAPI() {
        try {
            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);


                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calobj = Calendar.getInstance();
                String dateTime = df.format(calobj.getTime());


                JSONObject HeadItem = new JSONObject();
                HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
                HeadItem.put("divCode", Shared_Common_Pref.Div_Code);
                HeadItem.put("dt", dateTime);


                Call<ResponseBody> call = service.getDashboardValues(HeadItem.toString());
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


                                JSONObject jsonObject = new JSONObject(is.toString());


                                //   {"success":true,"Data":[{"CTC":31,"CPC":28,"TC":0,"PC":0,"NTC":0,"NPC":0}]}

                                if (jsonObject.getBoolean("success")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                    }


                                }


//                            popMaterialList.clear();
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                                popMaterialList.add(new Common_Model(jsonObject1.getString("POP_Code"), jsonObject1.getString("POP_Name"),
//                                        jsonObject1.getString("POP_UOM")));
//                            }


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
                common_class.showMsg(this, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }*/
