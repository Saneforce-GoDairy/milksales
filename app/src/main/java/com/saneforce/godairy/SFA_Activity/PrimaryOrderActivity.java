package com.saneforce.godairy.SFA_Activity;

import static com.saneforce.godairy.Activity_Hap.Leave_Request.CheckInfo;
import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atom.atompaynetzsdk.PayActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Activity_Hap.Dashboard;
import com.saneforce.godairy.Activity_Hap.MainActivity;
import com.saneforce.godairy.Activity_Hap.SFA_Activity;
import com.saneforce.godairy.BuildConfig;
import com.saneforce.godairy.CCAvenue.InitiatePaymentActivity;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.LocationEvents;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.Interface.onListItemClick;
import com.saneforce.godairy.JioMoney.PaymentWebView;
import com.saneforce.godairy.Model_Class.Datum;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.RyclBrandListItemAdb;
import com.saneforce.godairy.SFA_Adapter.RyclGrpListItemAdb;
import com.saneforce.godairy.SFA_Adapter.RyclShortageListItemAdb;
import com.saneforce.godairy.SFA_Model_Class.Category_Universe_Modal;
import com.saneforce.godairy.SFA_Model_Class.Product_Details_Modal;
import com.saneforce.godairy.common.DatabaseHandler;
import com.saneforce.godairy.common.LocationFinder;
import com.saneforce.godairy.databinding.ActivityPrimaryOrderLayoutBinding;
import com.saneforce.godairy.universal.UniversalDropDownAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrimaryOrderActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI, Master_Interface {
    private ActivityPrimaryOrderLayoutBinding binding;
    private final Context context = this;
    List<Category_Universe_Modal> Category_Modal = new ArrayList<>();
    List<Product_Details_Modal> Product_Modal;
    List<Product_Details_Modal> Product_ModalSetAdapter;
    List<Product_Details_Modal> Getorder_Array_List;
    List<Product_Details_Modal> freeQty_Array_List;
    List<Category_Universe_Modal> listt;
    Type userType;
    Gson gson;
    CircularProgressButton takeorder, btnRepeat;
    TextView Category_Nametext, selectDeliveryAddress,
            tvTimer, txBalAmt, txAmtWalt, txAvBal, tvDistId, tvDate, tvGrpName;
    LinearLayout lin_orderrecyclerview, lin_gridcategory, rlAddProduct, llTdPriOrd, btnRefACBal, vwRplcDetail, llProdRplc;
    Common_Class common_class;
    String Ukey;
    String[] strLoc;
    String Worktype_code = "", Route_Code = "", Dirtributor_Cod = "", Distributor_Name = "";
    Shared_Common_Pref sharedCommonPref;
    Prodct_Adapter mProdct_Adapter;
    String TAG = "PRIMARY_ORDER";
    DatabaseHandler db;
    RelativeLayout rlCategoryItemSearch, balDetwin;
    ImageView ivClose, btnClose,btnRplcClose, ivToolbarHome;
    EditText etCategoryItemSearch;
    double cashDiscount;
    boolean bRmRow = false,ACBalanceChk=true;
    NumberFormat formatter = new DecimalFormat("##0.00");
    private static final DecimalFormat qtyFormat = new DecimalFormat("#.##");
    private RecyclerView recyclerView, categorygrid, freeRecyclerview, Grpgrid, Brndgrid,rvShortageData;
    private int selectedPos = 0;
    private TextView tvTotalAmount, tvACBal, tvNetAmtTax, tvTotalItems, distributor_text;
    private double totalvalues;
    private double editTotValues;
    private Integer totalQty;
    private TextView tvBillTotItem;
    private TextView tvTotUOM;
    double ACBalance = 0.0;
    final Handler handler = new Handler();
    com.saneforce.godairy.Activity_Hap.Common_Class DT = new com.saneforce.godairy.Activity_Hap.Common_Class();
    public static final String UserDetail = "MyPrefs";
    SharedPreferences UserDetails;
    private ArrayList<Product_Details_Modal> orderTotTax;
    private ArrayList<Product_Details_Modal> orderTotUOM;
    List<Product_Details_Modal> multiList;
    String orderId = "",edOrderId = "", orderType = "";
    private boolean isEditOrder = false;
    private int inValidQty = -1;
    private double totTax;
    private JSONArray ProdGroups, ShortageData;
    private RyclGrpListItemAdb grplistItems;
    @SuppressLint("StaticFieldLeak")
    public static PrimaryOrderActivity primaryOrderActivity;
    public static int selPOS = 0;
    String grpName = "", grpCode = "";
    LinearLayout llDistributor;
    boolean isSubmit = false;
    int lastOrderedQty = 0;
    double lastOrderedAmount = 0;
    private Toolbar mToolbar;
    private DistributerAdapter distributerAdapter;
    private String ordersViewMode = "";

    String _id = "", _title = "", _erpCode = "", _stateCode = "", _pincode = "";
    JSONArray addressArray;
    private RyclShortageListItemAdb ShortagelistItems;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityPrimaryOrderLayoutBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

        // SearchView
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Search");

        ordersViewMode = getIntent().getStringExtra("Mode");

        // return to order view screen
        if (ordersViewMode != null){
            if ("order_view".equals(ordersViewMode)){
                binding.newPrimaryListLayout.setVisibility(View.GONE);
                binding.oldPrimaryLayout.setVisibility(View.VISIBLE);
            }
        }

        primaryOrderActivity = this;
        selPOS = 0;
        db = new DatabaseHandler(this);
        sharedCommonPref = new Shared_Common_Pref(PrimaryOrderActivity.this);
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        common_class = new Common_Class(this);

            iniVariable();
            initOnClickListner();


        if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equalsIgnoreCase(Constants.DISTRIBUTER_TYPE)) {
            distributor_text.setText("HI! " + sharedCommonPref.getvalue(Constants.Distributor_name, ""));
            distributor_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            findViewById(R.id.ivDistSpinner).setVisibility(View.GONE);
            tvTimer.setVisibility(View.VISIBLE);
        } else {
            distributor_text.setText(sharedCommonPref.getvalue(Constants.Distributor_name, ""));
            distributor_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_round_arrow_drop_down_24, 0);
            findViewById(R.id.ivDistSpinner).setVisibility(View.GONE);
            tvTimer.setVisibility(View.VISIBLE);
        }

        Product_ModalSetAdapter = new ArrayList<>();
        gson = new Gson();
        userType = new TypeToken<ArrayList<Product_Details_Modal>>() {
        }.getType();

        Ukey = Common_Class.GetEkey();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categorygrid.setLayoutManager(layoutManager);

        getStockistAddress();
        addressArray = new JSONArray();
        binding.selectDeliveryAddress.setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv_and_filter, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            android.app.AlertDialog dialog = builder.create();
            TextView title = view.findViewById(R.id.title);
            title.setText("Select Address");
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, addressArray);
            TextView close = view.findViewById(R.id.close);
            close.setOnClickListener(v1 -> dialog.dismiss());
            EditText eT_Filter = view.findViewById(R.id.eT_Filter);
            eT_Filter.setImeOptions(EditorInfo.IME_ACTION_DONE);
            eT_Filter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        adapter.getFilter().filter(s.toString());
                    }
                });
                adapter.setOnItemClick((position, arrayList) -> {
                    _id = arrayList.optJSONObject(position).optString("id");
                    _title = arrayList.optJSONObject(position).optString("title");
                    _erpCode = arrayList.optJSONObject(position).optString("erpCode");
                    _stateCode = arrayList.optJSONObject(position).optString("stateCode");
                    _pincode = arrayList.optJSONObject(position).optString("pincode");
                    selectDeliveryAddress.setText(_title);
                    dialog.dismiss();
                });
                recyclerView.setAdapter(adapter);
                dialog.show();
            });

            GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "6", "");

            common_class.getProductDetails(this);
            common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);

            getACBalance(0);

            common_class.getDb_310Data(Constants.Primary_Shortage_List, this);
            common_class.getDb_310Data(Constants.PaymentMethod, this);
            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.LOC_PRIMARY_DATA))){
                common_class.ProgressdialogShow(1, "Loading Matrial Details");
                common_class.getDb_310Data(Constants.Primary_Product_List, this);
            }
            else {
                Product_Modal = gson.fromJson(sharedCommonPref.getvalue(Constants.LOC_PRIMARY_DATA), userType);
                boolean isHave = false;
                for (int i = 0; i < Product_Modal.size(); i++) {
                    if (Product_Modal.get(i).getQty() > 0) {
                        isHave = true;
                        loadCategoryData("SAVE", "" + Product_Modal.get(i).getProduct_Grp_Code());
                        break;
                    }
                }
                if (!isHave) {
                    loadCategoryData("NEW", "");
                }
            }

            common_class.getDb_310Data(Constants.PRIMARY_SCHEME, this);
            handler.postDelayed(new Runnable() {
                public void run() {
                    findNearCutOfftime();
                    tvTimer.setText(Common_Class.GetTime() + "   /   " + sharedCommonPref.getvalue(Constants.CUTOFF_TIME));
                    handler.postDelayed(this, 1000);
                }
            }, 1000);

            tvDistId.setText("" + sharedCommonPref.getvalue(Constants.DistributorERP));
            tvDate.setText(DT.GetDateTime(getApplicationContext(), "dd-MMM-yyyy"));
            orderId = getIntent().getStringExtra(Constants.ORDER_ID);
            if(orderId==null) orderId = "";

            edOrderId=orderId;
            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.POS_NETAMT_TAX)))
                common_class.getDb_310Data(Constants.POS_NETAMT_TAX, this);

        getLastOrderedQty();

        loadDistributer(common_class.getDistList(), 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_toolbar, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                distributerAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initOnClickListner() {
        llDistributor.setOnClickListener(this);
        takeorder.setOnClickListener(this);
        rlCategoryItemSearch.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        rlAddProduct.setOnClickListener(this);
        llTdPriOrd.setOnClickListener(this);
        llProdRplc.setOnClickListener(this);
        Category_Nametext.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);
        ivToolbarHome.setOnClickListener(this);

        tvACBal.setOnClickListener(v -> balDetwin.setVisibility(View.VISIBLE));
        btnClose.setOnClickListener(v -> balDetwin.setVisibility(View.GONE));
        btnRplcClose.setOnClickListener(v -> balDetwin.setVisibility(View.GONE));
        btnRplcClose.setOnClickListener(v -> vwRplcDetail.setVisibility(View.GONE));
        btnRefACBal.setOnClickListener(v -> getACBalance(0));
        etCategoryItemSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showOrderItemList(selectedPos, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void iniVariable() {
        categorygrid = findViewById(R.id.category);
        Grpgrid = findViewById(R.id.PGroup);
        Brndgrid = findViewById(R.id.PBrnd);
        rvShortageData = findViewById(R.id.rcylPrdRplc);
        takeorder = findViewById(R.id.takeorder);
        lin_orderrecyclerview = findViewById(R.id.lin_orderrecyclerview);
        lin_gridcategory = findViewById(R.id.lin_gridcategory);
        distributor_text = findViewById(R.id.outlet_name);
        Category_Nametext = findViewById(R.id.Category_Nametext);
        rlCategoryItemSearch = findViewById(R.id.rlCategoryItemSearch);
        rlAddProduct = findViewById(R.id.rlAddProduct);
        ivClose = findViewById(R.id.ivClose);
        llTdPriOrd = findViewById(R.id.llTodayPriOrd);
        llProdRplc = findViewById(R.id.llProdRplc);
        tvACBal = findViewById(R.id.tvACBal);
        txAvBal = findViewById(R.id.txAvBal);
        txAmtWalt = findViewById(R.id.txAmtWalt);
        txBalAmt = findViewById(R.id.txBalAmt);
        btnRefACBal = findViewById(R.id.btnRefACBal);
        vwRplcDetail = findViewById(R.id.vwRplcDetail);
        balDetwin = findViewById(R.id.balDetwin);
        btnClose = findViewById(R.id.btnClose);
        btnRplcClose = findViewById(R.id.btnRplcClose);
        tvDistId = findViewById(R.id.tvDistId);
        tvDate = findViewById(R.id.tvDate);
        btnRepeat = findViewById(R.id.btnRepeat);
        tvGrpName = findViewById(R.id.tvGrpName);
        tvTotUOM = findViewById(R.id.tvTotUom);
        tvNetAmtTax = findViewById(R.id.tvNetAmtTax);
        tvTotalItems = findViewById(R.id.tvTotalItems);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        llDistributor = findViewById(R.id.llDistributor);
        etCategoryItemSearch = findViewById(R.id.searchView);
        tvTimer = findViewById(R.id.tvTimer);
        recyclerView = findViewById(R.id.orderrecyclerview);
        freeRecyclerview = findViewById(R.id.freeRecyclerview);
        ivToolbarHome = findViewById(R.id.toolbar_home);
        selectDeliveryAddress = findViewById(R.id.selectDeliveryAddress);

    }

    private void getStockistAddress() {
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_stockist_address");
        Common_Class.makeApiCall(context, params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                addressArray = new JSONArray();
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        addressArray = jsonObject.getJSONArray("response");
                    } catch (Exception ignored) {
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                addressArray = new JSONArray();
            }
        });
    }

    private void getLastOrderedQty() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_last_order_qty");
        params.put("stockistCode", sharedCommonPref.getvalue(Constants.Distributor_Id));
        Call<ResponseBody> call = apiInterface.universalAPIRequest(params, "");
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() == null) {
                            return;
                        }
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray array = jsonObject.getJSONArray("response");
                            lastOrderedQty = array.getJSONObject(0).getInt("LastOrderQty");
                            lastOrderedAmount = array.getJSONObject(0).optDouble("LastOrderAmt");
                        }
                    } catch (Exception e) {
                        Log.e("KnownError", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("KnownError", t.getMessage());
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @SuppressLint("SetTextI18n")
    private void getACBalance(int Mode) {
        JSONObject jParam = new JSONObject();
        try {
            jParam.put("StkERP", sharedCommonPref.getvalue(Constants.DistributorERP));
            tvACBal.setText(CurrencySymbol+" 0.00");
            txBalAmt.setText(CurrencySymbol+" 0.00");
            txAmtWalt.setText(CurrencySymbol+" 0.00");
            txAvBal.setText(CurrencySymbol+" 0.00");

            ApiClient.getClient().create(ApiInterface.class)
                    .getDataArrayList("get/custbalance", jParam.toString())
                    .enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                            try {
                                JsonArray res = response.body();
                                try {
                                    JsonObject jItem = res.get(0).getAsJsonObject();
                                    double ActBAL = jItem.get("LC_BAL").getAsDouble();
                                    ACBalance = jItem.get("Balance").getAsDouble();
                                    ACBalanceChk = jItem.get("BalanceChk").getAsBoolean();
//                                    if (ActBAL <= 0) ActBAL = Math.abs(ActBAL);
//                                    else ActBAL = 0 - ActBAL;
                                    NumberFormat format1 = NumberFormat.getCurrencyInstance(new Locale("en", "in"));

                                    // tvACBal.setText("₹" + new DecimalFormat("##0.00").format(ACBalance));
                                    tvACBal.setText(format1.format(ACBalance));
                                    txBalAmt.setText(CurrencySymbol + " " + new DecimalFormat("##0.00").format(ACBalance));
                                    txAmtWalt.setText(CurrencySymbol + " " + new DecimalFormat("##0.00").format(jItem.get("Pending").getAsDouble()));
                                    txAvBal.setText(CurrencySymbol + " " + new DecimalFormat("##0.00").format(ActBAL));
                                } catch (Exception ignored) {
                                }
                                if (Mode == 1) {

//                                    for (int i = 0; i < Getorder_Array_List.size(); i++) {
//                                        double val = Double.valueOf(Getorder_Array_List.get(i).getQty()) / Double.valueOf(Getorder_Array_List.get(i).getMultiple_Qty());
//                                        int cVal = (int) val;
//                                        if (val - cVal > 0) {
//                                            int finalI = i;
//                                            AlertDialogBox.showDialog(PrimaryOrderActivity.this, HAPApp.Title,
//                                                    "Enter Order Qty Multiple of : " + Getorder_Array_List.get(i).getMultiple_Qty() + " for " + Getorder_Array_List.get(i).getName().toUpperCase()
//                                                    , "", "Close", false, new AlertBox() {
//                                                        @Override
//                                                        public void PositiveMethod(DialogInterface dialog, int id) {
//                                                            dialog.dismiss();
//                                                            //  mProdct_Adapter.focusQty(i);
//                                                            inValidQty = finalI;
//                                                            mProdct_Adapter.notifyDataSetChanged();
//                                                        }
//
//                                                        @Override
//                                                        public void NegativeMethod(DialogInterface dialog, int id) {
//                                                            dialog.dismiss();
//                                                            inValidQty = finalI;
//                                                            mProdct_Adapter.notifyDataSetChanged();
//
//                                                        }
//                                                    });
//                                            ResetSubmitBtn(0);
//                                            return;
//                                        }
//
//                                    }
                                    SubmitPrimaryOrder();
                                }
                            } catch (Exception e) {
                                common_class.showMsg(PrimaryOrderActivity.this, e.getMessage());
                                ResetSubmitBtn(0);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
                            common_class.showMsg(PrimaryOrderActivity.this, t.getMessage());
                            ResetSubmitBtn(0);
                            Log.d("InvHistory", String.valueOf(t));
                        }
                    });
        } catch (JSONException e) {
            ResetSubmitBtn(0);
        }
    }

    private void FilterTypes(String GrpID) {
        try {
            JSONArray TypGroups = new JSONArray();
            JSONArray tTypGroups = db.getMasterData(Constants.ProdTypes_List);
            LinearLayoutManager TypgridlayManager = new LinearLayoutManager(this);
            TypgridlayManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            Brndgrid.setLayoutManager(TypgridlayManager);
            for (int i = 0; i < tTypGroups.length(); i++) {

                JSONObject ritm = tTypGroups.getJSONObject(i);
                if (ritm.getString("GroupId").equalsIgnoreCase(GrpID)) {
                    TypGroups.put(ritm);
                }
            }

            String filterId = "";
            if (TypGroups.length() > 0) {
                filterId = TypGroups.getJSONObject(0).getString("id");
                GetJsonData(String.valueOf(db.getMasterData(Constants.Category_List)), "1", filterId);
            }
            RyclBrandListItemAdb TyplistItems = new RyclBrandListItemAdb(TypGroups, this, new onListItemClick() {
                @Override
                public void onItemClick(JSONObject item) {
                    try {
                        GetJsonData(String.valueOf(db.getMasterData(Constants.Category_List)), "1", item.getString("id"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Brndgrid.setAdapter(TyplistItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetJsonData(String jsonResponse, String type, String filter) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            if (type.equals("1"))
                Category_Modal.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (type.equals("1")) {
                    String id = String.valueOf(jsonObject1.optInt("id"));
                    String name = jsonObject1.optString("name");
                    String Division_Code = jsonObject1.optString("Division_Code");
                    String Cat_Image = jsonObject1.optString("Cat_Image");
                    String sampleQty = jsonObject1.optString("sampleQty");
                    String colorflag = jsonObject1.optString("colorflag");
                    String typeId = String.valueOf(jsonObject1.optInt("TypID"));
                    if (filter.equalsIgnoreCase(typeId))
                        Category_Modal.add(new Category_Universe_Modal(id, name, Division_Code, Cat_Image, sampleQty, colorflag));
                } else {
                    Route_Code = jsonObject1.optString("cluster");
                    Dirtributor_Cod = jsonObject1.optString("stockist");
                    Worktype_code = jsonObject1.optString("wtype");
                    Distributor_Name = jsonObject1.optString("StkName");
                }
            }

            if (type.equals("1")) {
                selectedPos = 0;
                PrimaryOrderActivity.CategoryAdapter customAdapteravail = new PrimaryOrderActivity.CategoryAdapter(getApplicationContext(),
                        Category_Modal);
                categorygrid.setAdapter(customAdapteravail);
                customAdapteravail.notifyDataSetChanged();
                if (orderId.equalsIgnoreCase("")) {
                    showOrderItemList(selectedPos, "");
                } else {
                    edOrderId=orderId;
                    orderId = "";
                    common_class.getDataFromApi(Constants.PRIMARY_ORDER_EDIT, this, false);
                }
            }
        } catch (Exception e) {
            Log.v(TAG + "cat:", e.getMessage());
        }
    }

    void showOrderList() {

        Getorder_Array_List = new ArrayList<>();
        Getorder_Array_List.clear();

        for (int pm = 0; pm < Product_Modal.size(); pm++) {

            if (Product_Modal.get(pm).getQty() > 0) {
                Getorder_Array_List.add(Product_Modal.get(pm));
            }
        }

        if (Getorder_Array_List.size() == 0)
            Toast.makeText(getApplicationContext(), "Order is empty", Toast.LENGTH_SHORT).show();
        else
            FilterProduct();
    }

    void findNearCutOfftime() {
        try {
            Type type = new TypeToken<ArrayList<Datum>>() {
            }.getType();
            List<Datum> slotList = new ArrayList<>();
            if (!Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.SlotTime))) {
                slotList = gson.fromJson(sharedCommonPref.getvalue(Constants.SlotTime), type);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            long val = 0;
            String time = "";
            for (int i = 0; i < slotList.size(); i++) {
                Date d1 = sdf.parse(Common_Class.GetTime());
                Date d2 = sdf.parse(slotList.get(i).getTm());
                long elapsed = d2.getTime() - d1.getTime();
                ///Log.v(TAG + "time:" + slotList.get(i).getTm(), "Elapse:" + elapsed + ":val" + val);
                if ((val == 0 && elapsed > 0) || (elapsed < val && elapsed > 0)) {
                    val = elapsed;
                    time = slotList.get(i).getTm();
                }
            }

            if (slotList != null && slotList.size() > 0 && time.equals("")) {
                time = slotList.get(0).getTm();
            }

            if (!Common_Class.isNullOrEmpty(time))
                sharedCommonPref.save(Constants.CUTOFF_TIME, time);
            else {
                sharedCommonPref.save(Constants.CUTOFF_TIME, "23:59:00");
            }

        } catch (Exception e) {

        }
    }

    public void SubmitPrimaryOrder() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date d1 = sdf.parse(Common_Class.GetTime());
            Date d2 = sdf.parse(sharedCommonPref.getvalue(Constants.CUTOFF_TIME));
            long elapsed = d2.getTime() - d1.getTime();
            double currentOrderVal = totalvalues;
            if(edOrderId != null) currentOrderVal = totalvalues - editTotValues;
            if (ACBalance < currentOrderVal && ACBalanceChk) {
                ResetSubmitBtn(0);
                double ddif=  currentOrderVal - ACBalance;
                String sMsg="Low A/C Balance."+ " You need to pay the amount of <b><span style='color:#FF0000'>"+CurrencySymbol+" " + formatter.format(ddif) +"</span></b>";
                AlertDialogBox.showDialog(PrimaryOrderActivity.this, HAPApp.Title, sMsg, "OK", null, false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {

                    }
                });
            } else if (elapsed < 0 || Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.CUTOFF_TIME)) ||
                    sharedCommonPref.getvalue(Constants.CUTOFF_TIME).equals("--:--:--")) {
                ResetSubmitBtn(0);
                common_class.showMsg(this, "Time UP...");
            }
            else {
                String sLoc = sharedCommonPref.getvalue("CurrLoc");
                if (sLoc.equalsIgnoreCase("")) {
                    new LocationFinder(getApplication(), new LocationEvents() {
                        @Override
                        public void OnLocationRecived(Location location) {
                            strLoc = (location.getLatitude() + ":" + location.getLongitude()).split(":");
                            SaveOrder();
                        }
                    });
                } else {
                    strLoc = sLoc.split(":");
                    SaveOrder();
                }

            }
        } catch (Exception e) {
            common_class.showMsg(this, e.getMessage());
            ResetSubmitBtn(0);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedCommonPref.clear_pref(Constants.PRIMARY_ORDER_EDIT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_home:
                if (!orderId.equalsIgnoreCase("")) {
                    SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                    Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                    if (CheckIn == true) {
                        common_class.commonDialog(this, SFA_Activity.class, "Primary Order?");

                    } else
                        common_class.commonDialog(this, Dashboard.class, "Primary Order?");

                } else {
                    common_class.gotoHomeScreen(this, ivToolbarHome);
                }
                break;
            case R.id.llDistributor:
                common_class.showCommonDialog(common_class.getDistList(), 2, this);
                break;
            case R.id.llTodayPriOrd:
                if (!orderId.equalsIgnoreCase(""))
                    common_class.commonDialog(this, TodayPrimOrdActivity.class, "Primary Order?");
                else
                    startActivity(new Intent(getApplicationContext(), TodayPrimOrdActivity.class));
                break;
            case R.id.llProdRplc:
                vwRplcDetail.setVisibility(View.VISIBLE);
                break;
            case R.id.rlAddProduct:
                moveProductScreen();
                break;

            case R.id.Category_Nametext:
                findViewById(R.id.rlSearchParent).setVisibility(View.GONE);
                findViewById(R.id.rlCategoryItemSearch).setVisibility(View.VISIBLE);
                break;
            case R.id.ivClose:
                findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
                findViewById(R.id.rlSearchParent).setVisibility(View.VISIBLE);
                etCategoryItemSearch.setText("");
                showOrderItemList(selectedPos, "");
                break;

            case R.id.takeorder:
                try {
                    bRmRow = true;
                    if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
                        if (Getorder_Array_List != null
                                && Getorder_Array_List.size() > 0) {
                            if (takeorder.isAnimating()) return;
                            takeorder.startAnimation();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (lastOrderedQty > totalQty) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(PrimaryOrderActivity.this);
                                        builder.setMessage(String.format("Last order quantity: %s\n\nYour current order quantity is lesser than the last ordered quantity. Do you want to proceed?", lastOrderedQty));
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("PROCEED", (dialog, which) -> {
                                            if (lastOrderedAmount > totalvalues) {
                                                AlertDialog.Builder builders = new AlertDialog.Builder(PrimaryOrderActivity.this);
                                                String mMessage = String.format("<span style=\"color:#CC2311\">Last order amount: %s<br><br>Your current order amount is lesser than the last ordered amount. Do you want to proceed?</span>", common_class.formatCurrency(lastOrderedAmount));
                                                builders.setMessage(Html.fromHtml(mMessage));

                                                builders.setCancelable(false);
                                                builders.setPositiveButton("PROCEED", (dialog1, which1) -> {
                                                    dialog1.dismiss();
                                                    dialog.dismiss();
                                                    getACBalance(1);
                                                });
                                                builders.setNegativeButton("BACK", (dialog1, which1) -> {
                                                    dialog1.dismiss();
                                                    dialog.dismiss();
                                                    ResetSubmitBtn(0);
                                                });
                                                builders.create().show();
                                            } else {
                                                dialog.dismiss();
                                                getACBalance(1);
                                            }
                                        });
                                        builder.setNegativeButton("BACK", (dialog, which) -> {
                                            dialog.dismiss();
                                            ResetSubmitBtn(0);
                                        });
                                        builder.create().show();
                                    } else {
                                        getACBalance(1);
                                    }
                                }
                            }, 500);

                        } else {
                            common_class.showMsg(this, "Your Cart is empty...");
                        }
                    } else {
                        showOrderList();
                    }
                } catch (Exception e) {

                }
                break;
            case R.id.btnRepeat:
                if (btnRepeat.isAnimating()) return;
                btnRepeat.startAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        common_class.getDataFromApi(Constants.REPEAT_PRIMARY_ORDER, PrimaryOrderActivity.this, false);
                    }
                }, 500);
                break;

        }
    }

    private void loadDistributer(List<Common_Model> distList, int i) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemViewCacheSize(20);
        distributerAdapter = new DistributerAdapter(context, distList, 2);
        binding.recyclerView.setAdapter(distributerAdapter);
    }

    public class DistributerAdapter extends RecyclerView.Adapter<DistributerAdapter.ViewHolder> implements Filterable {
        Context context;
        List<Common_Model> distList;
        int mType;
        Activity activity;
        private final List<Common_Model> mFilteredList;
        public MyFilter mFilter;

        public DistributerAdapter(Context context, List<Common_Model> distList, int i) {
            this.context = context;
            this.distList = distList;
            this.mType = i;
            this.mFilteredList = new ArrayList< >();
        }

        @Override
        public Filter getFilter() {
            if (mFilter == null){
                mFilteredList.clear();
                mFilteredList.addAll(this.distList);
                mFilter = new DistributerAdapter.MyFilter(this,mFilteredList);
            }
            return mFilter;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Common_Model contact = distList.get(holder.getBindingAdapterPosition());
            holder.text.setText(contact.getName());
            holder.phoneText.setText(contact.getPhone());

            String distributerName = contact.getName();

            holder.firstLetterText.setText(distributerName.substring(0,1).toUpperCase());
            holder.mainLayout.setOnClickListener(v -> {

                sharedCommonPref.save(Constants.MAP_KEY, distList.get(position).getName());
                OnclickMasterType(distList, position, mType);

                if (!orderId.equalsIgnoreCase(""))
                    common_class.commonDialog(activity, TodayPrimOrdActivity.class, "Primary Order?");
                else
                    startActivity(new Intent(getApplicationContext(), TodayPrimOrdActivity.class));
            });
        }

        @Override
        public int getItemCount() {
            if (distList == null) return 0;
            return distList.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView text, firstLetterText, phoneText;
            LinearLayout mainLayout;

            public ViewHolder(View view) {
                super(view);
                text = view.findViewById(R.id.txt_name);
                firstLetterText = view.findViewById(R.id.first_letter);
                phoneText = view.findViewById(R.id.txt_phone);
                mainLayout = view.findViewById(R.id.layout);
            }
        }

        private class MyFilter extends Filter {

            private final DistributerAdapter myAdapter;
            private final List<Common_Model> originalList;
            private final List<Common_Model> filteredList;

            private MyFilter(DistributerAdapter myAdapter, List<Common_Model> originalList) {
                this.myAdapter = myAdapter;
                this.originalList = originalList;
                this.filteredList = new ArrayList<>();
            }

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                filteredList.clear();
                final FilterResults results = new FilterResults();
                if (charSequence.length() == 0){
                    filteredList.addAll(originalList);
                }else {
                    final String filterPattern = charSequence.toString().toLowerCase().trim();
                    for ( Common_Model user : originalList){
                        if (user.getName().toLowerCase().contains(filterPattern)){
                            filteredList.add(user);
                        }
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                myAdapter.distList.clear();
                myAdapter.distList.addAll((ArrayList<Common_Model>)filterResults.values);
                myAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", "requestCode: " + requestCode);
        Log.e("resultCode", "resultCode: " + resultCode);
        if (requestCode == 1000) {
            String sLoc = sharedCommonPref.getvalue("CurrLoc");
            if (sLoc.equalsIgnoreCase("")) {
                new LocationFinder(getApplication(), new LocationEvents() {
                    @Override
                    public void OnLocationRecived(Location location) {
                        strLoc = (location.getLatitude() + ":" + location.getLongitude()).split(":");
                        SaveOrder();
                    }
                });
            } else {
                strLoc = sLoc.split(":");
                SaveOrder();
            }
        }
    }

    private void SaveOrder() {
        if (common_class.isNetworkAvailable(this)) {
            AlertDialogBox.showDialog(PrimaryOrderActivity.this, HAPApp.Title, "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {

                    if (!isSubmit) {
                        isSubmit = true;

                        JSONArray data = new JSONArray();
                        JSONObject ActivityData = new JSONObject();
                        try {
                            JSONObject HeadItem = new JSONObject();
                            HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
                            HeadItem.put("Worktype_code", Worktype_code);
                            HeadItem.put("Town_code", sharedCommonPref.getvalue(Constants.Route_Id));
                            HeadItem.put("dcr_activity_date", Common_Class.GetDate());
                            HeadItem.put("Daywise_Remarks", "");
                            HeadItem.put("UKey", Ukey);
                            HeadItem.put("orderValue", formatter.format(totalvalues));
                            HeadItem.put("lastOrderedAmount", formatter.format(lastOrderedAmount));
                            HeadItem.put("DataSF", Shared_Common_Pref.Sf_Code);
                            HeadItem.put("AppVer", BuildConfig.VERSION_NAME);
                            HeadItem.put("addressId", _id);
                            HeadItem.put("title", _title);
                            HeadItem.put("erpCode", _erpCode);
                            HeadItem.put("stateCode", _stateCode);
                            HeadItem.put("pincode", _pincode);
                            ActivityData.put("Activity_Report_Head", HeadItem);

                            JSONObject OutletItem = new JSONObject();
                            OutletItem.put("Doc_Meet_Time", Common_Class.GetDate());
                            OutletItem.put("modified_time", Common_Class.GetDate());
                            OutletItem.put("stockist_code", sharedCommonPref.getvalue(Constants.Distributor_Id));
                            OutletItem.put("stockist_name", sharedCommonPref.getvalue(Constants.Distributor_name));
                            OutletItem.put("orderValue", formatter.format(totalvalues));
                            OutletItem.put("CashDiscount", cashDiscount);
                            OutletItem.put("NetAmount", formatter.format(totalvalues));
                            OutletItem.put("No_Of_items", tvBillTotItem.getText().toString());
                            OutletItem.put("Invoice_Flag", Shared_Common_Pref.Invoicetoorder);
                            OutletItem.put("ordertype", "order");
                            OutletItem.put("orderId", edOrderId);
                            OutletItem.put("mode", edOrderId.equalsIgnoreCase("") ? "new" : "edit");
                            OutletItem.put("cutoff_time", sharedCommonPref.getvalue(Constants.CUTOFF_TIME));
                            OutletItem.put("totAmtTax", formatter.format(totTax));
                            OutletItem.put("groupCode", grpCode);
                            OutletItem.put("groupName", grpName);

                            if (strLoc.length > 0) {
                                OutletItem.put("Lat", strLoc[0]);
                                OutletItem.put("Long", strLoc[1]);
                            } else {
                                OutletItem.put("Lat", "");
                                OutletItem.put("Long", "");
                            }
                            JSONArray Order_Details = new JSONArray();
                            JSONArray totTaxArr = new JSONArray();
                            JSONArray multipleArr = new JSONArray();
                            JSONArray uomArr = new JSONArray();

                            for (int z = 0; z < Getorder_Array_List.size(); z++) {
                                JSONObject ProdItem = new JSONObject();
                                // ProdItem.put("product_Name", Getorder_Array_List.get(z).getName());
                                ProdItem.put("product_code", Getorder_Array_List.get(z).getId());
                                ProdItem.put("Product_ERP", Getorder_Array_List.get(z).getERP_Code());
                                ProdItem.put("Product_Qty", Getorder_Array_List.get(z).getQty());
                                ProdItem.put("Product_RegularQty", 0);
                                ProdItem.put("Product_Total_Qty", Getorder_Array_List.get(z).getQty()
                                );
                                ProdItem.put("Product_Amount", Getorder_Array_List.get(z).getAmount());
                                ProdItem.put("MRP", Getorder_Array_List.get(z).getMRP());
                                ProdItem.put("Rate", String.format("%.2f", Getorder_Array_List.get(z).getSBRate()));

                                ProdItem.put("free", Getorder_Array_List.get(z).getFree());
                                ProdItem.put("dis", Getorder_Array_List.get(z).getDiscount());
                                ProdItem.put("dis_value", Getorder_Array_List.get(z).getDiscount_value());
                                ProdItem.put("Off_Pro_code", Getorder_Array_List.get(z).getOff_Pro_code());
                                ProdItem.put("Off_Pro_name", Getorder_Array_List.get(z).getOff_Pro_name());
                                ProdItem.put("Off_Pro_Unit", Getorder_Array_List.get(z).getOff_Pro_Unit());
                                ProdItem.put("Off_Scheme_Unit", Getorder_Array_List.get(z).getScheme());
                                ProdItem.put("discount_type", Getorder_Array_List.get(z).getDiscount_type());
                                ProdItem.put("ConversionFactor", Getorder_Array_List.get(z).getConversionFactor());

                                JSONArray tax_Details = new JSONArray();

                                if (Getorder_Array_List.get(z).getProductDetailsModal() != null &&
                                        Getorder_Array_List.get(z).getProductDetailsModal().size() > 0) {

                                    for (int i = 0; i < Getorder_Array_List.get(z).getProductDetailsModal().size(); i++) {
                                        JSONObject taxData = new JSONObject();

                                        String label = Getorder_Array_List.get(z).getProductDetailsModal().get(i).getTax_Type();
                                        Double amt = Getorder_Array_List.get(z).getProductDetailsModal().get(i).getTax_Amt();
                                        taxData.put("Tax_Id", Getorder_Array_List.get(z).getProductDetailsModal().get(i).getTax_Id());
                                        taxData.put("Tax_Val", Getorder_Array_List.get(z).getProductDetailsModal().get(i).getTax_Val());
                                        taxData.put("Tax_Type", label);
                                        taxData.put("Tax_Amt", formatter.format(amt));
                                        tax_Details.put(taxData);
                                    }
                                }
                                ProdItem.put("TAX_details", tax_Details);
                                Order_Details.put(ProdItem);
                            }

                            for (int i = 0; i < orderTotTax.size(); i++) {
                                JSONObject totTaxObj = new JSONObject();

                                totTaxObj.put("Tax_Type", orderTotTax.get(i).getTax_Type());
                                totTaxObj.put("Tax_Amt", formatter.format(orderTotTax.get(i).getTax_Amt()));
                                totTaxArr.put(totTaxObj);
                            }
                            OutletItem.put("TOT_TAX_details", totTaxArr);


//                            for (int i = 0; i < multiList.size(); i++) {
//                                JSONObject mulObj = new JSONObject();
//
//                                mulObj.put("multiple_name", multiList.get(i).getUOM_Nm());
//                                mulObj.put("multiple_tot_qty", multiList.get(i).getQty());
//                                mulObj.put("multiple_val", multiList.get(i).getMultiple_Qty());
//                                multipleArr.put(mulObj);
//
//                            }
//
//                            OutletItem.put("multiple_details", multipleArr);

                            for (int i = 0; i < orderTotUOM.size(); i++) {
                                JSONObject mulObj = new JSONObject();

                                mulObj.put("uom_name", orderTotUOM.get(i).getUOM_Nm());
                                mulObj.put("uom_qty", orderTotUOM.get(i).getCnvQty());
                                uomArr.put(mulObj);
                            }

                            OutletItem.put("uom_details", uomArr);
                            ActivityData.put("Activity_Doctor_Report", OutletItem);
                            ActivityData.put("Order_Details", Order_Details);
                            data.put(ActivityData);
                        } catch (JSONException e) {
                            isSubmit = false;
                            e.printStackTrace();
                        }
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<JsonObject> responseBodyCall = apiInterface.savePrimaryOrder(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
                        responseBodyCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        Log.e("JSON_VALUES", response.body().toString());
                                        JSONObject jsonObjects = new JSONObject(response.body().toString());
                                        ResetSubmitBtn(1);
                                        common_class.showMsg(PrimaryOrderActivity.this, jsonObjects.getString("Msg"));
                                        if (jsonObjects.getString("success").equals("true")) {
                                            sharedCommonPref.clear_pref(Constants.LOC_PRIMARY_DATA);
                                            // common_class.CommonIntentwithFinish(SFA_Activity.class);
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), TodayPrimOrdActivity.class));
                                        }
                                        isSubmit = false;
                                    } catch (Exception e) {
                                        ResetSubmitBtn(2);
                                        isSubmit = false;
                                    }
                                } else {
                                    isSubmit = false;
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.e("SUBMIT_VALUE", "ERROR");
                                ResetSubmitBtn(2);
                                isSubmit = false;
                            }
                        });
                    }
                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    ResetSubmitBtn(0);
                }
            });
        } else {
            ResetSubmitBtn(0);
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void ResetSubmitBtn(int resetMode) {
        common_class.ProgressdialogShow(0, "");
        long dely = 10;
        if (resetMode != 0) dely = 1000;
        if (resetMode == 1) {
            takeorder.doneLoadingAnimation(getResources().getColor(R.color.green), BitmapFactory.decodeResource(getResources(), R.drawable.done));
        } else {
            takeorder.doneLoadingAnimation(getResources().getColor(R.color.color_red), BitmapFactory.decodeResource(getResources(), R.drawable.ic_wrong));
        }
        handler.postDelayed(() -> {
            takeorder.stopAnimation();
            takeorder.revertAnimation();
            btnRepeat.stopAnimation();
            btnRepeat.revertAnimation();
        }, dely);
    }

    private void FilterProduct() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ignored) {}

        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.rlSearchParent).setVisibility(View.GONE);
        findViewById(R.id.llBillHeader).setVisibility(View.VISIBLE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.VISIBLE);
        lin_gridcategory.setVisibility(View.GONE);
        lin_orderrecyclerview.setVisibility(View.VISIBLE);
        takeorder.setText("SUBMIT");

        if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equalsIgnoreCase(Constants.CHECKIN_TYPE))
            takeorder.setVisibility(View.VISIBLE);
        btnRepeat.setVisibility(View.GONE);
        //  sumofTax();
        mProdct_Adapter = new Prodct_Adapter(Getorder_Array_List, R.layout.adapter_primary_pay_layout, getApplicationContext(), -1);
        recyclerView.setAdapter(mProdct_Adapter);
        showFreeQtyList();
    }

    void showFreeQtyList() {
        freeQty_Array_List = new ArrayList<>();
        freeQty_Array_List.clear();

        for (Product_Details_Modal pm : Product_Modal) {
            if (!Common_Class.isNullOrEmpty(pm.getFree()) && !pm.getFree().equals("0")) {
                freeQty_Array_List.add(pm);
            }
        }
        if (freeQty_Array_List != null && freeQty_Array_List.size() > 0) {
            findViewById(R.id.cdFreeQtyParent).setVisibility(View.VISIBLE);
            Free_Adapter mFreeAdapter = new Free_Adapter(freeQty_Array_List, R.layout.product_free_recyclerview, getApplicationContext(), -1);
            freeRecyclerview.setAdapter(mFreeAdapter);
        } else {
            findViewById(R.id.cdFreeQtyParent).setVisibility(View.GONE);
        }
        findViewById(R.id.delAddressLL).setVisibility(View.VISIBLE);

    }

    public void updateToTALITEMUI() {
        try {
            TextView tvTotLabel = findViewById(R.id.tvTotLabel);
            TextView tvTax = findViewById(R.id.tvTaxVal);
            TextView tvTaxableAmt = findViewById(R.id.tvTaxableAmt);
            TextView tvTaxLabel = findViewById(R.id.tvTaxLabel);
            TextView tvBillSubTotal = findViewById(R.id.subtotal);
            TextView tvSaveAmt = findViewById(R.id.tvSaveAmt);

            tvBillTotItem = findViewById(R.id.totalitem);
            TextView tvBillTotQty = findViewById(R.id.tvtotalqty);
            TextView tvTotQtyLabel = findViewById(R.id.tvTotQtyLabel);
            TextView tvBillToPay = findViewById(R.id.tvnetamount);
            TextView tvCashDiscount = findViewById(R.id.tvcashdiscount);

            Getorder_Array_List = new ArrayList<>();
            Getorder_Array_List.clear();
            totalvalues = 0;
            totalQty = 0;
            cashDiscount = 0;
            double taxVal = 0;

            orderTotTax = new ArrayList<>();
            orderTotUOM = new ArrayList<>();
            multiList = new ArrayList<>();

            for (int pm = 0; pm < Product_Modal.size(); pm++) {
                if (Product_Modal.get(pm).getQty() > 0) {
                    cashDiscount += (double) Product_Modal.get(pm).getDiscount();
                    totalvalues += Product_Modal.get(pm).getAmount();
                    totalQty += Product_Modal.get(pm).getQty();
                    if (Product_Modal.get(pm).getTax() > 0)
                        taxVal += Product_Modal.get(pm).getTax();

                    Getorder_Array_List.add(Product_Modal.get(pm));
                }
            }

            totTax = 0;
            try {
                String totAmtTax = sharedCommonPref.getvalue(Constants.POS_NETAMT_TAX);
                JSONObject obj = new JSONObject(totAmtTax);

                if (obj.getBoolean("success")) {
                    JSONArray arr = obj.getJSONArray("Data");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject taxObj = arr.getJSONObject(i);
                        double taxCal = (totalvalues) *
                                ((taxObj.getDouble("Value") / 100));
                        totTax = +totTax + taxCal;
                    }
                }
            } catch (Exception ignored) {

            }

            totalvalues = totalvalues + totTax;
            tvNetAmtTax.setText(CurrencySymbol+" " + formatter.format(totTax));
            tvTotalAmount.setText(CurrencySymbol+" " + formatter.format(totalvalues));

            if (isEditOrder) {
                isEditOrder = false;
                editTotValues = totalvalues;
            }

            tvTotalItems.setText("Items : " + Getorder_Array_List.size() + "   Qty : " + totalQty);

            if (Getorder_Array_List.size() == 1)
                tvTotLabel.setText("Price (1 item)");
            else
                tvTotLabel.setText("Price (" + Getorder_Array_List.size() + " items)");
            tvBillTotQty.setText("" + totalQty);

            tvBillSubTotal.setText(CurrencySymbol+" " + formatter.format(totalvalues));
            tvBillTotItem.setText("" + Getorder_Array_List.size());
            tvBillToPay.setText(CurrencySymbol+" " + formatter.format(totalvalues));
            tvCashDiscount.setText(CurrencySymbol+" " + formatter.format(cashDiscount));
            tvTax.setText(CurrencySymbol+" "+ formatter.format(taxVal));

            if (cashDiscount > 0) {
                tvSaveAmt.setVisibility(View.VISIBLE);
                tvSaveAmt.setText("You will save "+CurrencySymbol+" " + formatter.format(cashDiscount) + " on this order.");
            } else
                tvSaveAmt.setVisibility(View.GONE);

            int crtVal = 0;
            double txblamt = 0.0;
            for (int l = 0; l < Getorder_Array_List.size(); l++) {
                grpCode = "" + Getorder_Array_List.get(0).getProduct_Grp_Code();

                if (Getorder_Array_List.get(l).getProductDetailsModal() != null) {
                    txblamt = txblamt + Getorder_Array_List.get(l).getTaxableAmt();
                    for (int tax = 0; tax < Getorder_Array_List.get(l).getProductDetailsModal().size(); tax++) {
                        String label = Getorder_Array_List.get(l).getProductDetailsModal().get(tax).getTax_Type();
                        double amt = Getorder_Array_List.get(l).getProductDetailsModal().get(tax).getTax_Amt();
                        if (orderTotTax.size() == 0) {
                            orderTotTax.add(new Product_Details_Modal(label, amt));
                        } else {
                            boolean isDuplicate = false;
                            for (int totTax = 0; totTax < orderTotTax.size(); totTax++) {
                                if (orderTotTax.get(totTax).getTax_Type().equals(label)) {
                                    double oldAmt = orderTotTax.get(totTax).getTax_Amt();
                                    isDuplicate = true;
                                    orderTotTax.set(totTax, new Product_Details_Modal(label, oldAmt + amt));
                                }
                            }

                            if (!isDuplicate) {
                                orderTotTax.add(new Product_Details_Modal(label, amt));
                            }
                        }
                    }

                    String label = Getorder_Array_List.get(l).getProductUnit();
                    if (/*label.equalsIgnoreCase("CRT") ||*/ (label.equalsIgnoreCase("UNT"))) {
                        int qty = Getorder_Array_List.get(l).getQty();
                        if (orderTotUOM.size() == 0) {
                            orderTotUOM.add(new Product_Details_Modal(qty, label));
                        } else {
                            boolean isDuplicate = false;
                            for (int totUom = 0; totUom < orderTotUOM.size(); totUom++) {
                                if (orderTotUOM.get(totUom).getUOM_Nm().equals(label)) {
                                    double oldQty = orderTotUOM.get(totUom).getCnvQty();
                                    isDuplicate = true;
                                    orderTotUOM.set(totUom, new Product_Details_Modal(oldQty + qty, label));
                                }
                            }

                            if (!isDuplicate) {
                                orderTotUOM.add(new Product_Details_Modal(qty, label));
                            }
                        }
                    }

                    for (int uom = 0; uom < Getorder_Array_List.get(l).getUOMList().size(); uom++) {
                        String label1 = Getorder_Array_List.get(l).getUOMList().get(uom).getUOM_Nm();
                        if (/*label1.equalsIgnoreCase("CRT") ||*/ (label1.equalsIgnoreCase("UNT"))) {
                            int qty1 = (int) ((Integer.parseInt(Getorder_Array_List.get(l).getConversionFactor()) * Getorder_Array_List.get(l).getQty())
                                    / Getorder_Array_List.get(l).getUOMList().get(uom).getCnvQty());
                            if (orderTotUOM.size() == 0) {
                                orderTotUOM.add(new Product_Details_Modal(qty1, label1));
                            } else {
                                boolean isDuplicate = false;
                                for (int totUom = 0; totUom < orderTotUOM.size(); totUom++) {
                                    if (orderTotUOM.get(totUom).getUOM_Nm().equals(label1)) {
                                        double oldQty = orderTotUOM.get(totUom).getCnvQty();
                                        isDuplicate = true;
                                        orderTotUOM.set(totUom, new Product_Details_Modal(oldQty + qty1, label1));
                                    }
                                }

                                if (!isDuplicate) {
                                    orderTotUOM.add(new Product_Details_Modal(qty1, label1));
                                }
                            }
                        }
                    }

                    String multiple = "" + Getorder_Array_List.get(l).getMultiple_Qty();
                    int qty = Getorder_Array_List.get(l).getQty();
                    if (multiList.size() == 0) {
                        multiList.add(new Product_Details_Modal(multiple, qty, 1));
                    } else {

                        boolean isDuplicate = false;
                        for (int m = 0; m < multiList.size(); m++) {
                            if (multiList.get(m).getUOM_Nm().equals(multiple)) {
                                int oldQty = multiList.get(m).getQty();
                                int t = multiList.get(m).getMultiple_Qty();

                                isDuplicate = true;
                                multiList.set(m, new Product_Details_Modal(multiple, oldQty + qty, t + 1));
                            }
                        }
                        if (!isDuplicate) {
                            multiList.add(new Product_Details_Modal(multiple, qty, 1));

                        }
                    }
                }
            }

            String label = "";
            String amt = "";
            for (int i = 0; i < orderTotTax.size(); i++) {
                label = label + orderTotTax.get(i).getTax_Type() + "\n";
                amt = amt + CurrencySymbol+" " + String.valueOf(formatter.format(orderTotTax.get(i).getTax_Amt())) + "\n";
            }

            String uomName = "";

            String qtyLabel = "Total Qty" + "\n";
            String qtyVal = "" + totalQty + "\n";

            for (int i = 0; i < multiList.size(); i++) {
                //  qtyLabel = qtyLabel + multiList.get(i).getUOM_Nm() + "\n";
                double dVal = Double.valueOf(multiList.get(i).getQty()) /
                        Double.valueOf(multiList.get(i).getUOM_Nm());

                int iVal = (int) dVal;

                if (dVal > iVal)
                    iVal = iVal + 1;

                crtVal += iVal;

                multiList.set(i, new Product_Details_Modal(multiList.get(i).getUOM_Nm(), multiList.get(i).getQty(), iVal));
            }

            for (int i = 0; i < orderTotUOM.size(); i++) {
                qtyLabel = qtyLabel + orderTotUOM.get(i).getUOM_Nm() + "\n";
                qtyVal = qtyVal + ((int) orderTotUOM.get(i).getCnvQty()) + "\n";
                uomName = uomName + orderTotUOM.get(i).getUOM_Nm() + " : " + ((int) orderTotUOM.get(i).getCnvQty()) + "  ";
            }

            if (crtVal > 0) {
                qtyLabel = qtyLabel + "CRT" + "\n";
                qtyVal = qtyVal + (crtVal) + "\n";
                uomName = uomName + "CRT : " + (crtVal) + "  ";

                orderTotUOM.add(new Product_Details_Modal(crtVal, "CRT"));
            }
            tvTotQtyLabel.setText("" + qtyLabel);
            tvBillTotQty.setText("" + qtyVal);

            tvTotUOM.setText(uomName);
            tvTotUOM.setMovementMethod(new ScrollingMovementMethod());

            tvTaxLabel.setText(label);
            tvTax.setText(amt);
            if (orderTotTax.size() == 0) {
                tvTaxLabel.setVisibility(View.INVISIBLE);
                tvTax.setVisibility(View.INVISIBLE);
            } else {
                tvTaxLabel.setVisibility(View.VISIBLE);
                tvTax.setVisibility(View.VISIBLE);

            }

            tvTaxableAmt.setText(CurrencySymbol+" " + String.valueOf(formatter.format(txblamt)));
            if (sharedCommonPref.getvalue(Constants.DivERP).equalsIgnoreCase("21") && !isEditOrder) {
                if (Getorder_Array_List.size() == 0)
                    grplistItems.notify(ProdGroups, this, "", new onListItemClick() {
                        @Override
                        public void onItemClick(JSONObject item) {
                            try {
                                grpName = "";
                                grpCode = "";
                                FilterTypes(item.getString("id"));
                                common_class.brandPos = 0;

                                tvGrpName.setText("" + item.getString("name"));
                                getSlotTimes(item.getString("id"));
                            } catch (Exception ignored) {}
                        }
                    });
                else {
                    grpCode = "" + Getorder_Array_List.get(0).getProduct_Grp_Code();
                    for (int i = 0; i < ProdGroups.length(); i++) {
                        if (grpCode.equalsIgnoreCase(ProdGroups.getJSONObject(i).getString("id"))) {
                            grpName = ProdGroups.getJSONObject(i).getString("name");
                        }
                    }
                    grplistItems.notify(ProdGroups, this, "" + grpCode, new onListItemClick() {
                        @Override
                        public void onItemClick(JSONObject item) {

                        }
                    });
                }
            }
            if (orderId.equalsIgnoreCase("")) {
                String data = gson.toJson(Product_Modal);
                sharedCommonPref.save(Constants.LOC_PRIMARY_DATA, data);
            }
        } catch (Exception e) {
            Log.v(TAG + " updateUI:", e.getMessage());
        }
    }

    public void sumofTax(List<Product_Details_Modal> Product_Details_Modalitem, int pos) {
        try {
            String taxRes = sharedCommonPref.getvalue(Constants.PrimaryTAXList);
            if (!Common_Class.isNullOrEmpty(taxRes)) {
                JSONObject jsonObject = new JSONObject(taxRes);
                JSONArray jsonArray = jsonObject.getJSONArray("Data");

                double wholeTax = 0;
                List<Product_Details_Modal> taxList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    if (jsonObject1.getString("Product_Detail_Code").equals(Product_Details_Modalitem.get(pos).getId())) {

                        if (jsonObject1.getDouble("Tax_Val") > 0) {
                            double taxCal = Product_Details_Modalitem.get(pos).getTaxableAmt() *
                                    ((jsonObject1.getDouble("Tax_Val") / 100));

                            wholeTax += taxCal;

                            taxList.add(new Product_Details_Modal(jsonObject1.getString("Tax_Id"),
                                    jsonObject1.getString("Tax_Type"), jsonObject1.getDouble("Tax_Val"), taxCal));
                        }
                    }
                }

                Product_Details_Modalitem.get(pos).setProductDetailsModal(taxList);


                // Product_Details_Modalitem.get(pos).setAmount(Double.valueOf(formatter.format(Product_Details_Modalitem.get(pos).getAmount()
                //         + wholeTax)));

                Product_Details_Modalitem.get(pos).setTax(Double.parseDouble(formatter.format(wholeTax)));
            }

        } catch (Exception ignored) {

        }
    }

    public void showOrderItemList(int categoryPos, String filterString) {
        try {
            Product_ModalSetAdapter.clear();
            for (Product_Details_Modal personNpi : Product_Modal) {
                if (personNpi.getProductCatCode().toString().equals(listt.get(categoryPos).getId())) {
                    if (Common_Class.isNullOrEmpty(filterString))
                        Product_ModalSetAdapter.add(personNpi);
                    else if (personNpi.getName().toLowerCase().contains(filterString.toLowerCase()))
                        Product_ModalSetAdapter.add(personNpi);

                }
            }
            lin_orderrecyclerview.setVisibility(View.VISIBLE);
            Category_Nametext.setVisibility(View.VISIBLE);
            Category_Nametext.setText(listt.get(categoryPos).getName()+" ( " + String.valueOf(Product_ModalSetAdapter.size()) + " )");

            mProdct_Adapter = new Prodct_Adapter(Product_ModalSetAdapter, R.layout.adapter_primary_product, getApplicationContext(), categoryPos);
            recyclerView.setAdapter(mProdct_Adapter);
        } catch (Exception e) {
            Log.v(TAG + ":showOrdList:", e.getMessage());
        }
    }

    @Override
    public void onErrorData(String msg) {
        ResetSubmitBtn(2);
    }

    void loadCategoryData(String mode, String id) {
        try {
            ProdGroups = db.getMasterData(Constants.ProdGroups_List);
            LinearLayoutManager GrpgridlayManager = new LinearLayoutManager(this);
            GrpgridlayManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            Grpgrid.setLayoutManager(GrpgridlayManager);

            String saveProductname = "";


            if (sharedCommonPref.getvalue(Constants.DivERP).equalsIgnoreCase("21")) {
                for (int i = 0; i < ProdGroups.length(); i++) {
                    JSONObject obj = ProdGroups.getJSONObject(i);
                    if (mode.equalsIgnoreCase("save")) {
                        if (obj.getString("id").equalsIgnoreCase(id)) {
                            selPOS = i;
                            saveProductname = obj.getString("name");
                            break;
                        }
                    } else if (mode.equalsIgnoreCase("edit")) {
                        JSONArray arr = new JSONArray();
                        if (obj.getString("name").equalsIgnoreCase(orderType)) {
                            arr.put(obj);
                            ProdGroups = arr;
                            break;
                        }
                    }
                }
            }

            grplistItems = new RyclGrpListItemAdb(ProdGroups, this, new onListItemClick() {
                @Override
                public void onItemClick(JSONObject item) {

                    try {
                        FilterTypes(item.getString("id"));
                        common_class.brandPos = 0;
                        tvGrpName.setText("" + item.getString("name"));
                        getSlotTimes(item.getString("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Grpgrid.setAdapter(grplistItems);

            FilterTypes(saveProductname.equalsIgnoreCase("") ? ProdGroups.getJSONObject(0).getString("id") : "" + id);
            tvGrpName.setText(saveProductname.equalsIgnoreCase("") ? "" + ProdGroups.getJSONObject(0).getString("name") : saveProductname);
            getSlotTimes(saveProductname.equalsIgnoreCase("") ? ProdGroups.getJSONObject(0).getString("id") : "" + id);

        } catch (Exception e) {
            Log.v(TAG + "loadData:", e.getMessage());
        }
    }

    void getSlotTimes(String grpCode) {
        try {
            JsonObject data = new JsonObject();
            data.addProperty("GrpCode", grpCode);
            common_class.getDb_310Data(Constants.SlotTime, this, data);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        if (type == 2) {
            // route_text.setText("");
            selPOS = 0;
            sharedCommonPref.clear_pref(Constants.LOC_PRIMARY_DATA);
            distributor_text.setText(myDataset.get(position).getName());
            tvTotalAmount.setText(CurrencySymbol + " 0.00");
            tvTotalItems.setText("Items : 0");
            sharedCommonPref.save(Constants.Route_name, "");
            sharedCommonPref.save(Constants.Route_Id, "");
            // btnCmbRoute.setVisibility(View.VISIBLE);
            sharedCommonPref.save(Constants.Distributor_name, myDataset.get(position).getName());
            sharedCommonPref.save(Constants.DistributorGst, myDataset.get(position).getDisGst());
            sharedCommonPref.save(Constants.DistributorFSSAI, myDataset.get(position).getDisFssai());
            sharedCommonPref.save(Constants.Distributor_Id, myDataset.get(position).getId());
            sharedCommonPref.save(Constants.DistributorERP, myDataset.get(position).getCont());
            sharedCommonPref.save(Constants.TEMP_DISTRIBUTOR_ID, myDataset.get(position).getId());
            sharedCommonPref.save(Constants.Distributor_phone, myDataset.get(position).getPhone());
            sharedCommonPref.save(Constants.DivERP, myDataset.get(position).getDivERP());
            sharedCommonPref.save(Constants.CusSubGrpErp, myDataset.get(position).getCusSubGrpErp());

            getProductDetails();
            getLastOrderedQty();
            // common_class.getProductDetails(this);

            // common_class.getDb_310Data(Constants.Primary_Product_List, this);
            getACBalance(0);  // common_class.getDb_310Data(Rout_List, this);

            //common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);

        }

//        else if (type == 3) {
//            route_text.setText(myDataset.get(position).getName());
//            sharedCommonPref.save(Constants.Route_name, myDataset.get(position).getName());
//            sharedCommonPref.save(Constants.Route_Id, myDataset.get(position).getId());
//            common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);
//
//        }
    }

    public void getProductDetails() {

        if (common_class.isNetworkAvailable(this)) {
            UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

            DatabaseHandler db = new DatabaseHandler(this);
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("SF", UserDetails.getString("Sfcode", ""));
                jParam.put("Stk", sharedCommonPref.getvalue(Constants.Distributor_Id));
                jParam.put("outletId", Shared_Common_Pref.OutletCode);
                jParam.put("div", UserDetails.getString("Divcode", ""));
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                service.getDataArrayList("get/prodGroup", jParam.toString()).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        db.deleteMasterData(Constants.ProdGroups_List);
                        db.addMasterData(Constants.ProdGroups_List, response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {

                    }
                });
                service.getDataArrayList("get/prodTypes", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        db.deleteMasterData(Constants.ProdTypes_List);
                        db.addMasterData(Constants.ProdTypes_List, response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {

                    }
                });
                service.getDataArrayList("get/prodCate", jParam.toString()).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        try {
                            db.deleteMasterData(Constants.Category_List);
                            db.addMasterData(Constants.Category_List, response.body());

                            common_class.getDb_310Data(Constants.Primary_Product_List, PrimaryOrderActivity.this);

                        } catch (Exception ignored) {

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            switch (key) {
                case Constants.SlotTime:
                    JSONObject obj = new JSONObject(apiDataResponse);
                    if (obj.getBoolean("success")) {
                        JSONArray arr = obj.getJSONArray("Data");
                        List<Datum> slotList = new ArrayList<>();

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject dataObj = arr.getJSONObject(i);
                            slotList.add(new Datum(dataObj.getString("Tm")));

                        }
                        sharedCommonPref.save(Constants.SlotTime, gson.toJson(slotList));
                    } else {
                        sharedCommonPref.clear_pref(Constants.SlotTime);

                    }
                    break;

                case Constants.REPEAT_PRIMARY_ORDER:
                    if (Common_Class.isNullOrEmpty(apiDataResponse) || apiDataResponse.equals("[]")) {
                        ResetSubmitBtn(0);
                        common_class.showMsg(PrimaryOrderActivity.this, "No Records Found.");
                    } else
                        loadData(apiDataResponse);
                    break;

                case Constants.PRIMARY_ORDER_EDIT:
                    sharedCommonPref.clear_pref(Constants.PRIMARY_ORDER);
                    orderType = getIntent().getStringExtra(Constants.CATEGORY_TYPE);
                    isEditOrder = true;
                    loadCategoryData("EDIT", "");
                    loadData(apiDataResponse);
                    break;

                case Constants.Primary_Product_List:
                    Product_Modal = gson.fromJson(apiDataResponse, userType);
                    common_class.ProgressdialogShow(0, "");
                    loadCategoryData("NEW", "");
                    break;

                case Constants.Primary_Shortage_List:
                    ShortageData= new JSONArray(apiDataResponse);
                    LinearLayoutManager shrtgridlayManager = new LinearLayoutManager(this);
                    shrtgridlayManager.setOrientation(LinearLayoutManager.VERTICAL);
                    rvShortageData.setLayoutManager(shrtgridlayManager);
                    RyclShortageListItemAdb shortagelistItems = new RyclShortageListItemAdb(this, ShortageData);
                    rvShortageData.setAdapter(shortagelistItems);
                    break;

                case Constants.PRIMARY_SCHEME:
                    JSONObject jsonObject = new JSONObject(apiDataResponse);
                    if (jsonObject.getBoolean("success")) {
                        Gson gson = new Gson();
                        List<Product_Details_Modal> product_details_modalArrayList = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("Data");

                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                product_details_modalArrayList.add(new Product_Details_Modal(jsonObject1.getString("Product_Code"),
                                        jsonObject1.getString("Scheme"), jsonObject1.getString("Free"),
                                        Double.valueOf(jsonObject1.getString("Discount")), jsonObject1.getString("Discount_Type"),
                                        jsonObject1.getString("Package"), 0, jsonObject1.getString("Offer_Product"),
                                        jsonObject1.getString("Offer_Product_Name"), jsonObject1.getString("offer_product_unit")));
                            }
                        }
                        sharedCommonPref.save(Constants.PRIMARY_SCHEME, gson.toJson(product_details_modalArrayList));

                    } else {
                        sharedCommonPref.clear_pref(Constants.PRIMARY_SCHEME);
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
                moveProductScreen();

            } else {
                common_class.commonDialog(this, SFA_Activity.class, "Primary Order?");

            }
            return true;
        }
        return false;
    }

    void moveProductScreen() {
        lin_gridcategory.setVisibility(View.VISIBLE);
        findViewById(R.id.rlSearchParent).setVisibility(View.VISIBLE);
        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.llBillHeader).setVisibility(View.GONE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.GONE);
        findViewById(R.id.cdFreeQtyParent).setVisibility(View.GONE);
        findViewById(R.id.delAddressLL).setVisibility(View.GONE);
        btnRepeat.setVisibility(View.VISIBLE);
        takeorder.setText("PROCEED");
        takeorder.setVisibility(View.VISIBLE);
        showOrderItemList(selectedPos, "");
    }

    void loadData(String apiDataResponse) {
        try {
            Product_Modal = gson.fromJson(sharedCommonPref.getvalue(Constants.Primary_Product_List), userType);

            JSONArray jsonArray1 = new JSONArray(apiDataResponse);
            if (jsonArray1 != null && jsonArray1.length() > 0) {
                for (int pm = 0; pm < Product_Modal.size(); pm++) {
                    for (int q = 0; q < jsonArray1.length(); q++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(q);
                        if (Product_Modal.get(pm).getId().equals(jsonObject1.getString("Product_Code"))) {
                            Product_Modal.get(pm).setQty(
                                    jsonObject1.getInt("Quantity"));

                            Product_Modal.get(pm).setAmount(Double.valueOf(formatter.format((Product_Modal.get(pm).getQty() * Double.parseDouble( Product_Modal.get(pm).getConversionFactor()) )*
                                    Product_Modal.get(pm).getSBRate())));
                            Product_Modal.get(pm).setTaxableAmt(Double.valueOf(formatter.format((Product_Modal.get(pm).getQty()  * Double.parseDouble( Product_Modal.get(pm).getConversionFactor()) )*
                                    Product_Modal.get(pm).getBillRate())));

                            double enterQty = Product_Modal.get(pm).getQty();
                            String strSchemeList = sharedCommonPref.getvalue(Constants.PRIMARY_SCHEME);

                            Type type1 = new TypeToken<ArrayList<Product_Details_Modal>>() {
                            }.getType();
                            List<Product_Details_Modal> product_details_modalArrayList = gson.fromJson(strSchemeList, type1);

                            double highestScheme = 0;
                            boolean haveVal = false;
                            if (product_details_modalArrayList != null && product_details_modalArrayList.size() > 0) {

                                for (int i = 0; i < product_details_modalArrayList.size(); i++) {

                                    if (Product_Modal.get(pm).getId().equals(product_details_modalArrayList.get(i).getId())) {
                                        haveVal = true;
                                        double schemeVal = Double.parseDouble(product_details_modalArrayList.get(i).getScheme());

                                        if (enterQty >= schemeVal) {
                                            if (schemeVal > highestScheme) {
                                                highestScheme = schemeVal;

                                                if (!product_details_modalArrayList.get(i).getFree().equals("0")) {
                                                    if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                        double freePer = (enterQty / highestScheme);

                                                        double freeVal = freePer * Double.parseDouble(product_details_modalArrayList.
                                                                get(i).getFree());

                                                        Product_Modal.get(pm).setFree(String.valueOf(Math.round(freeVal)));
                                                    } else {
                                                        int val = (int) (enterQty / highestScheme);
                                                        int freeVal = val * Integer.parseInt(product_details_modalArrayList.get(i).getFree());
                                                        Product_Modal.get(pm).setFree(String.valueOf(freeVal));
                                                    }
                                                } else {
                                                    Product_Modal.get(pm).setFree("0");
                                                }

                                                if (product_details_modalArrayList.get(i).getDiscount() != 0) {
                                                    if (product_details_modalArrayList.get(i).getDiscount_type().equals("%")) {
                                                        double discountVal = enterQty * (((product_details_modalArrayList.get(i).getDiscount()
                                                        )) / 100);
                                                        Product_Modal.get(pm).setDiscount(discountVal);

                                                    } else {
                                                        double freeVal;
                                                        if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                            double freePer = (enterQty / highestScheme);
                                                            freeVal = freePer * (product_details_modalArrayList.
                                                                    get(i).getDiscount());
                                                        } else {
                                                            int val = (int) (enterQty / highestScheme);
                                                            freeVal = (double) (val * (product_details_modalArrayList.get(i).getDiscount()));
                                                        }
                                                        Product_Modal.get(pm).setDiscount(freeVal);
                                                    }
                                                } else {
                                                    Product_Modal.get(pm).setDiscount(0.00);
                                                }
                                            }
                                        } else {
                                            Product_Modal.get(pm).setFree("0");
                                            Product_Modal.get(pm).setDiscount(0.00);
                                        }
                                    }
                                }
                            }

                            if (!haveVal) {
                                Product_Modal.get(pm).setFree("0");

                                Product_Modal.get(pm).setDiscount(0.00);

                            } else {
                                Product_Modal.get(pm).setAmount((Product_Modal.get(pm).getAmount()) -
                                        Double.valueOf(Product_Modal.get(pm).getDiscount()));
                                Product_Modal.get(pm).setTaxableAmt((Product_Modal.get(pm).getTaxableAmt()) -
                                        Double.valueOf(Product_Modal.get(pm).getDiscount()));
                            }

                            sumofTax(Product_Modal, pm);
                        }

                    }
                }


            }
            ResetSubmitBtn(0);
            showOrderList();
        } catch (Exception e) {
            Log.v(TAG + ":loadData:", e.getMessage());
        }

    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
        Context context;
        MyViewHolder pholder;

        public CategoryAdapter(Context applicationContext, List<Category_Universe_Modal> list) {
            this.context = applicationContext;
            listt = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.category_order_horizantal_universe_gridview, parent, false);
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
        public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                holder.icon.setText(listt.get(position).getName());
                if (!listt.get(position).getCatImage().equalsIgnoreCase("")) {
                    holder.ivCategoryIcon.clearColorFilter();
                    Glide.with(this.context)
                            .load(listt.get(position).getCatImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.ivCategoryIcon);
                } else {
                    holder.ivCategoryIcon.setImageDrawable(getResources().getDrawable(R.drawable.product_logo));
                    holder.ivCategoryIcon.setColorFilter(getResources().getColor(R.color.grey_500));
                }

                holder.gridcolor.setOnClickListener(v -> {
                    if (pholder != null) {
                        pholder.gridcolor.setBackground(getResources().getDrawable(R.drawable.cardbutton));
                        pholder.icon.setTextColor(getResources().getColor(R.color.black));
                        pholder.icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        pholder.undrCate.setVisibility(View.GONE);

                    }
                    pholder = holder;
                    selectedPos = position;
                    showOrderItemList(position, "");
                    holder.gridcolor.setBackground(getResources().getDrawable(R.drawable.cardbtnprimary));
                    holder.icon.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    holder.icon.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.undrCate.setVisibility(View.VISIBLE);
                });


                if (position == selectedPos) {
                    holder.gridcolor.setBackground(getResources().getDrawable(R.drawable.cardbtnprimary));
                    holder.icon.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    holder.icon.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.undrCate.setVisibility(View.VISIBLE);
                    pholder = holder;
                } else {
                    holder.gridcolor.setBackground(getResources().getDrawable(R.drawable.cardbutton));
                    holder.icon.setTextColor(getResources().getColor(R.color.black));
                    holder.icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                }


            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return listt.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public LinearLayout gridcolor, undrCate;
            TextView icon;
            ImageView ivCategoryIcon;


            public MyViewHolder(View view) {
                super(view);

                icon = view.findViewById(R.id.textView);
                gridcolor = view.findViewById(R.id.gridcolor);
                ivCategoryIcon = view.findViewById(R.id.ivCategoryIcon);
                undrCate = view.findViewById(R.id.undrCate);

            }
        }


    }

    public class Prodct_Adapter extends RecyclerView.Adapter<Prodct_Adapter.MyViewHolder> {
        Context context;
        int CategoryType;
        private List<Product_Details_Modal> Product_Details_Modalitem;
        private int rowLayout;


        public Prodct_Adapter(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context, int categoryType) {
            this.Product_Details_Modalitem = Product_Details_Modalitem;
            this.rowLayout = rowLayout;
            this.context = context;
            this.CategoryType = categoryType;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
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
        public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {


                Product_Details_Modal ProductItem = Product_Details_Modalitem.get(holder.getBindingAdapterPosition());
                holder.productname.setText("" + ProductItem.getName().toUpperCase());
                holder.Rate.setText(CurrencySymbol+" " + formatter.format(ProductItem.getSBRate() * (Integer.parseInt(ProductItem.getConversionFactor()))));// * (Integer.parseInt(Product_Details_Modal.getConversionFactor()))));
                holder.Amount.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(ProductItem.getAmount()));
                if(ProductItem.getTaxableAmt()==null) ProductItem.setTaxableAmt(0.0);
                if (CategoryType >= 0) holder.QtyAmt.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(ProductItem.getTaxableAmt()));

                int oQty = ProductItem.getQty();
                int eQty = ProductItem.getQty() * Integer.parseInt(ProductItem.getConversionFactor());
                String sQty = ProductItem.getQty().toString();
                if (oQty <= 0) sQty = "";
                holder.Qty.setText(sQty);

                holder.tvDefUOM.setText("" + ProductItem.getProductUnit());

//                holder.tvUomName

                holder.llFreeProd.setVisibility(View.GONE);
                if (ProductItem.getOff_Pro_name()!=null) {
                    if (!(ProductItem.getOff_Pro_name().equalsIgnoreCase(ProductItem.getName())))
                        holder.llFreeProd.setVisibility(View.VISIBLE);
                }
                if (CategoryType >= 0) {
                    holder.tvProERPCode.setText("" + ProductItem.getERP_Code());
                    holder.tvMRP.setText(CurrencySymbol+" "+ (Double.parseDouble( ProductItem.getMRP()) * (Integer.parseInt(ProductItem.getConversionFactor()))));
                    holder.totalQty.setText("Total Qty : " + (int) oQty);//((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() * (Integer.parseInt(Product_Details_Modal.getConversionFactor())))));

                    if (!ProductItem.getPImage().equalsIgnoreCase("")) {
                        holder.ImgVwProd.clearColorFilter();
                        Glide.with(this.context)
                                .load(ProductItem.getPImage())
                                .placeholder(getResources().getDrawable(R.drawable.delivery_svg_low))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.ImgVwProd);
                    } else {
                        holder.ImgVwProd.setImageDrawable(getResources().getDrawable(R.drawable.delivery_svg_low));
                        //holder.ImgVwProd.setColorFilter(getResources().getColor(R.color.grey_500));
                    }


                   ///// holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(eQty * ProductItem.getBillRate())); //* (Integer.parseInt(Product_Details_Modal.getConversionFactor())) * Product_Details_Modal.getQty()));


                    String name = "";
                    String uomQty = "";
                    for (int i = 0; i < Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getUOMList().size(); i++) {
                        name = name + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getUOMList().get(i).getUOM_Nm() + "\n";
                        uomQty = uomQty + "" + qtyFormat.format((Double.parseDouble(ProductItem.getConversionFactor()) * ProductItem.getQty()) / (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getUOMList().get(i).getCnvQty())) + "\n";

                    }

                    holder.tvUomName.setText(name);
                    holder.tvUomQty.setText(uomQty);

                    holder.tvMultiple.setText("Order Qty Multiple of : " + (int) (ProductItem.getMultiple_Qty()));

                }

                holder.tvTaxLabel.setText(CurrencySymbol+" " + formatter.format(ProductItem.getTax()));


                if (Common_Class.isNullOrEmpty(ProductItem.getFree()))
                    holder.Free.setText("0");
                else
                    holder.Free.setText("" + ProductItem.getFree());


                holder.Disc.setText(CurrencySymbol+" " + formatter.format(ProductItem.getDiscount()));


                holder.QtyPls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
//                        String sVal = holder.Qty.getText().toString();
//                        if (sVal.equalsIgnoreCase("")) sVal = "0";
//                        int iQty = Integer.parseInt(sVal) + 1;
//                        sVal = "";
//                        if (iQty > 0) sVal = String.valueOf(iQty);
//                        holder.Qty.setText(sVal);

                            String sVal = holder.Qty.getText().toString();
                            if (sVal.equalsIgnoreCase("")) sVal = "0";
                            holder.Qty.setText("" + (Integer.parseInt(sVal) + ProductItem.getMultiple_Qty()));

                            double val = Double.valueOf(sVal) / Double.valueOf(ProductItem.getMultiple_Qty());
                            int cVal = (int) (val);

                            if (val - cVal > 0) {
                                holder.Qty.setText("" + (Math.round(val + 1) * ProductItem.getMultiple_Qty()));

                            }
                        } catch (Exception e) {
                            Log.v(TAG + "plus:", e.getMessage());
                        }

                    }
                });
                holder.QtyMns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String sVal = holder.Qty.getText().toString();
//                        if (sVal.equalsIgnoreCase("")) sVal = "0";
//                        int iQty = Integer.parseInt(sVal) - 1;
//                        sVal = "";
//                        if (iQty > 0) sVal = String.valueOf(iQty);
//                        holder.Qty.setText(sVal);

                        String sVal = holder.Qty.getText().toString();
                        if (sVal.equalsIgnoreCase("")) sVal = "0";
                        if (Integer.parseInt(sVal) > 0) {
                            if (Integer.parseInt(sVal) - ProductItem.getMultiple_Qty() > 0) {
                                int minVal = (Integer.parseInt(sVal) - ProductItem.getMultiple_Qty());
                                holder.Qty.setText("" + minVal);
                                double val = Double.valueOf(minVal) / Double.valueOf(ProductItem.getMultiple_Qty());
                                int cVal = (int) (val);
                                if (val - cVal > 0) {
                                    holder.Qty.setText("" + (Math.round(val) * ProductItem.getMultiple_Qty()));
                                }
                            } else
                                holder.Qty.setText("0");
                        }
                    }
                });
                holder.Qty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence charSequence, int start,
                                              int before, int count) {
                        try {

                            double enterQty = 0;
                            if (!charSequence.toString().equals(""))
                                enterQty = Double.parseDouble(charSequence.toString());

                            double totQty = (enterQty*Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getConversionFactor()));
                            //double ProdAmt = totQty * (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getSBRate()*Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getConversionFactor()));
                            double ProdAmt = totQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getSBRate();
                            double tProdAmt = totQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBillRate();
                            double EARate = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getSBRate() / Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getConversionFactor());
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setQty((int) enterQty);
                            holder.Amount.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(ProdAmt));
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(ProdAmt);
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setTaxableAmt(tProdAmt);
                            if (CategoryType >= 0) {

                                //holder.QtyAmt.setText(CurrencySymbol+" "+ formatter.format(tProdAmt));
                                holder.QtyAmt.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(tProdAmt));
                                holder.totalQty.setText("Total Qty : " + (int) totQty);

                                String name = "";
                                String uomQty = "";
                                for (int i = 0; i < Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getUOMList().size(); i++) {
                                    name = name + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getUOMList().get(i).getUOM_Nm() + "\n";
                                    uomQty = uomQty + "" + qtyFormat.format((Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getConversionFactor()) * enterQty) /
                                            (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getUOMList().get(i).getCnvQty())) + "\n";

                                }

                                holder.tvUomName.setText(name);
                                holder.tvUomQty.setText(uomQty);

                                holder.tvMultiple.setText("Order Qty Multiple of : " + (int) (ProductItem.getMultiple_Qty()));
                            }

                            String strSchemeList = sharedCommonPref.getvalue(Constants.PRIMARY_SCHEME);

                            Type type = new TypeToken<ArrayList<Product_Details_Modal>>() {
                            }.getType();
                            List<Product_Details_Modal> product_details_modalArrayList = gson.fromJson(strSchemeList, type);

                            double highestScheme = 0;
                            boolean haveVal = false;
                            if (totQty > 0 && product_details_modalArrayList != null && product_details_modalArrayList.size() > 0) {

                                for (int i = 0; i < product_details_modalArrayList.size(); i++) {

                                    if (ProductItem.getId().equals(product_details_modalArrayList.get(i).getId())) {

                                        haveVal = true;
                                        double schemeVal = Double.parseDouble(product_details_modalArrayList.get(i).getScheme());

                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_code(product_details_modalArrayList.get(i).getOff_Pro_code());
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_name(product_details_modalArrayList.get(i).getOff_Pro_name());
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_Unit(product_details_modalArrayList.get(i).getOff_Pro_Unit());
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree_val(product_details_modalArrayList.get(i).getFree());

                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_value(String.valueOf(product_details_modalArrayList.get(i).getDiscount()));
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_type(product_details_modalArrayList.get(i).getDiscount_type());


                                        if (totQty >= schemeVal) {

                                            if (schemeVal > highestScheme) {
                                                highestScheme = schemeVal;
                                                if (!product_details_modalArrayList.get(i).getFree().equals("0")) {
                                                    if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                        double freePer = (totQty / highestScheme);

                                                        double freeVal = freePer * Double.parseDouble(product_details_modalArrayList.
                                                                get(i).getFree());

                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree(String.valueOf(Math.round(freeVal)));
                                                    } else {
                                                        int val = (int) (totQty / highestScheme);
                                                        int freeVal = val * Integer.parseInt(product_details_modalArrayList.get(i).getFree());
                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree(String.valueOf(freeVal));
                                                    }
                                                } else {

                                                    holder.Free.setText("0");
                                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree("0");

                                                }


                                                if (product_details_modalArrayList.get(i).getDiscount() != 0) {

                                                    if (product_details_modalArrayList.get(i).getDiscount_type().equals("%")) {
                                                        double discountVal = totQty * (((product_details_modalArrayList.get(i).getDiscount()
                                                        )) / 100);


                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(discountVal);

                                                    } else {
                                                        //Rs
                                                        double freeVal;
                                                        if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                            double freePer = (totQty / highestScheme);
                                                            freeVal = freePer * (product_details_modalArrayList.
                                                                    get(i).getDiscount());
                                                        } else {
                                                            int val = (int) (totQty / highestScheme);
                                                            freeVal = (double) (val * (product_details_modalArrayList.get(i).getDiscount()));
                                                        }
                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(freeVal);

                                                    }


                                                } else {
                                                    holder.Disc.setText(CurrencySymbol+" 0.00");
                                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(0.00);

                                                }


                                            }

                                        } else {
                                            holder.Free.setText("0");
                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree("0");

                                            holder.Disc.setText(CurrencySymbol+" 0.00");
                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(0.00);


                                        }


                                    }

                                }


                            }

                            holder.llFreeProd.setVisibility(View.GONE);
                            if (!haveVal) {
                                holder.Free.setText("0");
                                holder.Disc.setText(CurrencySymbol+" 0.00");
                                holder.lblFreeNm.setText("");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree("0");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(0.00);
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_code("");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_name("");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_Unit("");

                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_value("0.00");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_type("");


                            } else {
                                String pna=Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getName().toString();
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()) -
                                        (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setTaxableAmt((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTaxableAmt()) -
                                        (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));

                                holder.Free.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getFree() +" EA");
                                holder.lblFreeNm.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getOff_Pro_name());
                                holder.Disc.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));
                                if (!(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getOff_Pro_name().equalsIgnoreCase(pna)))
                                holder.llFreeProd.setVisibility(View.VISIBLE);
                                holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
                                if (CategoryType >= 0) holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTaxableAmt()));

                            }
                            double EAAmt=0.0;
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setReplace_qty("0");
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setReplace_value("0.00");
                            for(int ish=0;ish<ShortageData.length();ish++){
                              JSONObject itm=ShortageData.getJSONObject(ish);
                                if (itm.getString("Product_code").equalsIgnoreCase(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getId())) {
                                    holder.tvReplcQty.setText(String.valueOf(itm.getInt("Qty")));
                                    EAAmt=itm.getInt("Qty") * EARate;

                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()) -
                                            EAAmt);
                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setTaxableAmt((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTaxableAmt()) -
                                            EAAmt);
                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setReplace_qty(String.valueOf(itm.getInt("Qty")));
                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setReplace_value(String.valueOf(EAAmt));
                                }
                            }

                            sumofTax(Product_Details_Modalitem, holder.getBindingAdapterPosition());
                            holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
                            if (CategoryType >= 0) holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTaxableAmt()));
                            holder.tvTaxLabel.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTax()));
                            // holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()-Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTax()));
                            updateToTALITEMUI();


                            //hide for remove unwanted action (product also remove edit scenario)
//                            if (CategoryType == -1) {
//                                if (holder.Amount.getText().toString().equals(CurrencySymbol+" 0.00")) {
//
//                                    Product_Details_Modalitem.remove(position);
//                                    notifyDataSetChanged();
//
//                                }
//
//                                showFreeQtyList();
//                            }

                        } catch (Exception e) {
                            Log.v(TAG, " orderAdapter:qty " + e.getMessage());
                        }

                        bRmRow = false;

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                if (CategoryType == -1) {
                    holder.ivDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialogBox.showDialog(PrimaryOrderActivity.this, HAPApp.Title,
                                    "Do you want to remove " + Product_Details_Modalitem.get(position).getName().toUpperCase() + " from your cart?"
                                    , "OK", "Cancel", false, new AlertBox() {
                                        @Override
                                        public void PositiveMethod(DialogInterface dialog, int id) {
                                            Product_Details_Modalitem.get(position).setQty(0);
                                            Product_Details_Modalitem.remove(position);
                                            notifyDataSetChanged();
                                            updateToTALITEMUI();
                                        }

                                        @Override
                                        public void NegativeMethod(DialogInterface dialog, int id) {
                                            dialog.dismiss();

                                        }
                                    });

                        }
                    });
                }

                updateToTALITEMUI();
                //holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(ProductItem.getAmount()-ProductItem.getTax()));

                if (inValidQty >= 0) {

                    if (position == inValidQty) {
                        holder.Qty.requestFocus();
                        holder.Qty.setSelection(holder.Qty.getText().length());

                        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.showSoftInput(holder.Qty, InputMethodManager.SHOW_IMPLICIT);
                        inValidQty = -1;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return Product_Details_Modalitem.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname, Rate, Amount, Disc, Free, lblRQty, productQty, lblFreeNm,
                    QtyAmt, totalQty, tvTaxLabel, tvMRP, tvDefUOM, tvUomName, tvUomQty, tvMultiple, tvProERPCode, tvReplcQty;
            ImageView ImgVwProd, QtyPls, QtyMns, ivDel;
            EditText Qty;
            LinearLayout llFreeProd;

            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);
                tvReplcQty = view.findViewById(R.id.tvReplcQty);
                QtyPls = view.findViewById(R.id.ivQtyPls);
                QtyMns = view.findViewById(R.id.ivQtyMns);
                Rate = view.findViewById(R.id.Rate);
                Qty = view.findViewById(R.id.Qty);
                Amount = view.findViewById(R.id.Amount);
                Free = view.findViewById(R.id.Free);
                llFreeProd = view.findViewById(R.id.llFreeProd);
                Disc = view.findViewById(R.id.Disc);
                tvTaxLabel = view.findViewById(R.id.tvTaxTotAmt);
                tvDefUOM = view.findViewById(R.id.tvUOM);
                lblFreeNm = view.findViewById(R.id.tvFreeProd);

                if (CategoryType >= 0) {
                    tvMultiple = view.findViewById(R.id.tvMultiple);
                    ImgVwProd = view.findViewById(R.id.ivAddShoppingCart);
                    lblRQty = view.findViewById(R.id.status);
                    totalQty = view.findViewById(R.id.totalqty);
                    QtyAmt = view.findViewById(R.id.qtyAmt);
                    tvMRP = view.findViewById(R.id.MrpRate);
                    tvUomName = view.findViewById(R.id.tvUomName);
                    tvUomQty = view.findViewById(R.id.tvUomQty);
                    tvProERPCode = view.findViewById(R.id.tvProERPCode);

                } else {
                    ivDel = view.findViewById(R.id.ivDel);
                }


            }
        }
    }

    public class Free_Adapter extends RecyclerView.Adapter<Free_Adapter.MyViewHolder> {
        private final List<Product_Details_Modal> Product_Details_Modalitem;
        private final int rowLayout;
        Context context;


        public Free_Adapter(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context, int Categorycolor) {
            this.Product_Details_Modalitem = Product_Details_Modalitem;
            this.rowLayout = rowLayout;
            this.context = context;


        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
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
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {


                Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(position);
                holder.productname.setText("" + Product_Details_Modal.getOff_Pro_name().toUpperCase());
                holder.Free.setText("" + Product_Details_Modal.getFree());
                updateToTALITEMUI();
            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return Product_Details_Modalitem.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname, Free;


            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);
                Free = view.findViewById(R.id.Free);

            }
        }
    }
}