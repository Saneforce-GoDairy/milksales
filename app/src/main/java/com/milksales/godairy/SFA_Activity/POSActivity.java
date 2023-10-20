package com.milksales.godairy.SFA_Activity;

import static com.milksales.godairy.SFA_Activity.HAPApp.CurrencySymbol;
import static com.milksales.godairy.SFA_Activity.HAPApp.MRPCap;
import static com.milksales.godairy.SFA_Activity.HAPApp.ProductsLoaded;
import static com.milksales.godairy.SFA_Activity.HAPApp.StockCheck;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.milksales.godairy.Activity_Hap.QRCodeScanner;
import com.milksales.godairy.Activity_Hap.SFA_Activity;
import com.milksales.godairy.BuildConfig;
import com.milksales.godairy.Common_Class.AlertDialogBox;
import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Common_Model;
import com.milksales.godairy.Common_Class.Constants;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.AdapterOnClick;
import com.milksales.godairy.Interface.AlertBox;
import com.milksales.godairy.Interface.ApiClient;
import com.milksales.godairy.Interface.ApiInterface;
import com.milksales.godairy.Interface.LocationEvents;
import com.milksales.godairy.Interface.Master_Interface;
import com.milksales.godairy.Interface.OnLiveUpdateListener;
import com.milksales.godairy.Interface.UpdateResponseUI;
import com.milksales.godairy.Interface.onListItemClick;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Adapter.Dashboard_View_Adapter;
import com.milksales.godairy.SFA_Adapter.RyclBrandListItemAdb;
import com.milksales.godairy.SFA_Adapter.RyclListItemAdb;
import com.milksales.godairy.SFA_Model_Class.Category_Universe_Modal;
import com.milksales.godairy.SFA_Model_Class.Dashboard_View_Model;
import com.milksales.godairy.SFA_Model_Class.Product_Details_Modal;
import com.milksales.godairy.common.DatabaseHandler;
import com.milksales.godairy.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POSActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI, View.OnTouchListener, Master_Interface {
    final Handler handler = new Handler();
    public int selectedPos = 0, uomPos;
    //GridView categorygrid,Grpgrid,Brndgrid;
    List<Category_Universe_Modal> Category_Modal = new ArrayList<>();
    List<Product_Details_Modal> Product_Modal;
    List<Product_Details_Modal> Product_ModalSetAdapter;
    List<Product_Details_Modal> Getorder_Array_List;
    List<Product_Details_Modal> freeQty_Array_List;
    List<Category_Universe_Modal> listt;
    Type userType;
    Gson gson;
    CircularProgressButton takeorder;
    TextView Category_Nametext, tvName, tvMRP, lblName, lblPhone, lblAddress, tvPosOrders, tvPayMode, btnPosStockLoad, tvCounterEntrySales;
    LinearLayout lin_orderrecyclerview, lin_gridcategory, rlAddProduct, rlQtyParent;
    Common_Class common_class;
    String Ukey;
    String[] strLoc;
    String Worktype_code = "", Route_Code = "", Dirtributor_Cod = "", Distributor_Name = "";
    Shared_Common_Pref sharedCommonPref;
    Prodct_Adapter mProdct_Adapter;
    String TAG = "POSACTIVITY";
    DatabaseHandler db;
    RelativeLayout rlCategoryItemSearch;
    ImageView ivClose, ivScanner, ivMns, ivPlus, ImgVProd;
    EditText etCategoryItemSearch, etName, etPhone, etAddress, etQty, etRecAmt;
    int cashDiscount;
    NumberFormat formatter = new DecimalFormat("##0.00");
    private RecyclerView recyclerView, categorygrid, Grpgrid, Brndgrid, freeRecyclerview;
    private TextView tvTotalAmount, tvBalAmt, tvNetAmtTax, tvDate, tvDay;
    private double totalvalues, taxVal;
    private Integer totalQty;
    private TextView tvBillTotItem;
    private DatePickerDialog fromDatePickerDialog;
    private List<Product_Details_Modal> orderTotTax;
    private String scanProId = "",OrderTypId="",OrderTypNm="";
    private ArrayList<Common_Model> uomList;

    private final List<Common_Model> payList = new ArrayList<>();
    private double payAmt;
    private double totTax;

    RecyclerView rvCurrentStk;
    com.milksales.godairy.Activity_Hap.Common_Class DT = new com.milksales.godairy.Activity_Hap.Common_Class();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pos_layout);
            db = new DatabaseHandler(this);
            sharedCommonPref = new Shared_Common_Pref(this);
            common_class = new Common_Class(this);

            selectedPos = 0;
            rvCurrentStk = findViewById(R.id.rvCurntStk);
            Grpgrid = findViewById(R.id.PGroup);
            Brndgrid = findViewById(R.id.PBrnd);
            categorygrid = findViewById(R.id.category);
            takeorder = findViewById(R.id.takeorder);
            common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);
            lin_orderrecyclerview = findViewById(R.id.lin_orderrecyclerview);
            lin_gridcategory = findViewById(R.id.lin_gridcategory);
            Category_Nametext = findViewById(R.id.Category_Nametext);
            rlCategoryItemSearch = findViewById(R.id.rlCategoryItemSearch);
            rlQtyParent = findViewById(R.id.rlQtyParent);
            rlAddProduct = findViewById(R.id.rlAddProduct);
            ivClose = findViewById(R.id.ivClose);
            tvCounterEntrySales = findViewById(R.id.btnPosEntrySales);
            btnPosStockLoad = findViewById(R.id.btnPosStockLoad);

            tvCounterEntrySales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(POSActivity.this, POS_SalesEntryActivity.class));

                }
            });
            btnPosStockLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(POSActivity.this, POSStockLoadingActivity.class));

                }
            });


            etCategoryItemSearch = findViewById(R.id.searchView);
            ivScanner = findViewById(R.id.ivScanner);
            etName = findViewById(R.id.edt_name);
            etPhone = findViewById(R.id.edt_phone);
            etAddress = findViewById(R.id.edtAddress);
            lblName = findViewById(R.id.lbl_name);
            lblPhone = findViewById(R.id.lbl_phone);
            lblAddress = findViewById(R.id.lblAddress);

            tvName = findViewById(R.id.tvScanProName);
            tvMRP = findViewById(R.id.tvScanMRP);
            ivPlus = findViewById(R.id.ivScanQtyPls);
            ivMns = findViewById(R.id.ivScanQtyMns);
            etQty = findViewById(R.id.etScanQty);
            etRecAmt = findViewById(R.id.etRecAmt);
            ImgVProd = findViewById(R.id.ivAddShoppingCart);
            tvPosOrders = findViewById(R.id.tvPosOrders);
            tvPayMode = findViewById(R.id.tvPayMode);
            tvBalAmt = findViewById(R.id.tvBalance);
            tvDate = findViewById(R.id.tvDate);
            tvDay = findViewById(R.id.tvDay);


            ivScanner.setOnClickListener(this);
            rlQtyParent.setOnTouchListener(this);
            tvPayMode.setOnClickListener(this);


            Product_ModalSetAdapter = new ArrayList<>();
            gson = new Gson();
            takeorder.setOnClickListener(this);
            rlCategoryItemSearch.setOnClickListener(this);
            ivClose.setOnClickListener(this);
            rlAddProduct.setOnClickListener(this);
            tvPosOrders.setOnClickListener(this);
            Ukey = Common_Class.GetEkey();
            recyclerView = findViewById(R.id.orderrecyclerview);
            freeRecyclerview = findViewById(R.id.freeRecyclerview);

            tvDate.setText("" + DT.getDateWithFormat(new Date(), "dd-MMM-yyyy"));
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            tvDay.setText("" + sdf.format(new Date()));


            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            categorygrid.setLayoutManager(layoutManager);

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);

            Log.v(TAG, " order oncreate:h ");

            // showOrderItemList(0, "");

            Log.v(TAG, " order oncreate:i ");

            Category_Nametext.setOnClickListener(this);


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

            etRecAmt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        payAmt = 0;

                        if (!Common_Class.isNullOrEmpty(s.toString())) {
                            payAmt = Double.parseDouble(s.toString());
                        }

                        tvBalAmt.setText(formatter.format((payAmt - totalvalues)));

                    } catch (Exception e) {

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "6", "");


            JSONArray ProdGroups = db.getMasterData(Constants.POS_ProdGroups_List);
            LinearLayoutManager GrpgridlayManager = new LinearLayoutManager(this);
            GrpgridlayManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            Grpgrid.setLayoutManager(GrpgridlayManager);

            RyclListItemAdb grplistItems = new RyclListItemAdb(ProdGroups, this, new onListItemClick() {
                @Override
                public void onItemClick(JSONObject item) {

                    try {
                        FilterTypes(item.getString("id"));
                        OrderTypId=item.getString("id");
                        OrderTypNm=item.getString("name");
                        common_class.brandPos = 0;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Grpgrid.setAdapter(grplistItems);

            OrderTypId=ProdGroups.getJSONObject(0).getString("id");
            OrderTypNm=ProdGroups.getJSONObject(0).getString("name");
            FilterTypes(ProdGroups.getJSONObject(0).getString("id"));
            FilterTypes(ProdGroups.getJSONObject(0).getString("id"));
            common_class.getDb_310Data(Constants.POS_TAXList, this);
            common_class.getDb_310Data(Constants.POS_SCHEME, this);

            //common_class.getPOSProduct(this);
   /*    String preOrderList = sharedCommonPref.getvalue(Constants.PreOrderQtyList);

            if (!Common_Class.isNullOrEmpty(preOrderList)) {
                for (int pm = 0; pm < Product_Modal.size(); pm++) {
                    JSONObject jsonObjectPreOrder = new JSONObject(preOrderList);
                    JSONArray arr = jsonObjectPreOrder.getJSONArray("Data");

                    for (int k = 0; k < arr.length(); k++) {
                        JSONObject obj = arr.getJSONObject(k);

                        if (Product_Modal.get(pm).getId().equals(obj.getString("Product_Detail_Code"))) {

                            Product_Modal.get(pm).setRegularQty(obj.getInt("Qty"));

                            Product_Modal.get(pm).setAmount(Double.valueOf(formatter.format(Product_Modal.get(pm).getRegularQty() * Double.parseDouble(Product_Modal.get(pm).getMRP()))));


                            double enterQty = Product_Modal.get(pm).getRegularQty();
                            String strSchemeList = sharedCommonPref.getvalue(Constants.FreeSchemeDiscList);

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


                                                        Product_Modal.get(pm).setDiscount((Math.round(discountVal)));

                                                    } else {
                                                        //Rs
                                                        if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                            double freePer = (enterQty / highestScheme);

                                                            double freeVal = freePer * (product_details_modalArrayList.
                                                                    get(i).getDiscount());

                                                            Product_Modal.get(pm).setDiscount((Math.round(freeVal)));
                                                        } else {
                                                            int val = (int) (enterQty / highestScheme);
                                                            double freeVal = (double) (val * (product_details_modalArrayList.get(i).getDiscount()));
                                                            Product_Modal.get(pm).setDiscount((freeVal));
                                                        }
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
                            }


                            sumofTax(Product_Modal, pm);
                        }
                    }

                }
            }

            Log.v(TAG, " order oncreate:j " + preOrderList);*/

            initData();
            // common_class.getDb_310Data(Constants.STOCK_DATA, this);
            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.POS_NETAMT_TAX)))
                common_class.getDb_310Data(Constants.POS_NETAMT_TAX, this);

            common_class.getDb_310Data(Constants.CURRENT_STOCK, this);


        } catch (Exception e) {
            Log.v(TAG, " order oncreate: " + e.getMessage());

        }
    }

    public void initData()
    {
        ProductsLoaded=false;
        //GetJsonData(String.valueOf(db.getMasterData(Constants.Category_List)), "1", "");
        String OrdersTable = String.valueOf(db.getMasterData(Constants.POS_Product_List));
        userType = new TypeToken<ArrayList<Product_Details_Modal>>() {
        }.getType();

       // if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.LOC_POS_DATA)))
            Product_Modal = gson.fromJson(OrdersTable, userType);
        //else
          //  Product_Modal = gson.fromJson(sharedCommonPref.getvalue(Constants.LOC_POS_DATA), userType);
        showOrderItemList(selectedPos,"");

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

    public void sumofTax(List<Product_Details_Modal> Product_Details_Modalitem, int pos) {
        try {
            String taxRes = sharedCommonPref.getvalue(Constants.POS_TAXList);
            if (!Common_Class.isNullOrEmpty(taxRes)) {
                JSONObject jsonObject = new JSONObject(taxRes);
                JSONArray jsonArray = jsonObject.getJSONArray("Data");

                double wholeTax = 0;
                List<Product_Details_Modal> taxList = new ArrayList<>();

                double totTax = 0;

                JSONArray proTaxArr = new JSONArray();
                double rate = 0;

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if (jsonObject1.getString("Product_Detail_Code").equals(Product_Details_Modalitem.get(pos).getId())) {
                        if (jsonObject1.getDouble("Tax_Val") > 0) {
//                            double taxCal = Product_Details_Modalitem.get(pos).getAmount() *
//                                    ((jsonObject1.getDouble("Tax_Val") / 100));

                            totTax = totTax + (jsonObject1.getDouble("Tax_Val"));

                            JSONObject totTaxObj = new JSONObject();
                            totTaxObj.put("Tax_Id", jsonObject1.getString("Tax_Id"));

                            totTaxObj.put("Tax_Type", jsonObject1.getString("Tax_Type"));
                            totTaxObj.put("Tax_Val", jsonObject1.getDouble("Tax_Val"));
                            proTaxArr.put(totTaxObj);
//                            double taxCal =  ((Product_Details_Modalitem.get(pos).getQty() * Product_Details_Modalitem.get(pos).getCnvQty() *
//                                    Double.parseDouble(Product_Details_Modalitem.get(pos).getMRP()) * 100) /
//                                    ((jsonObject1.getDouble("Tax_Val") + 100)));
//                            Log.v("taxCalc:","val:"+taxCal);
//
//                            taxCal=(Product_Details_Modalitem.get(pos).getQty() * Product_Details_Modalitem.get(pos).getCnvQty() *
//                                    Double.parseDouble(Product_Details_Modalitem.get(pos).getMRP())) -taxCal;
//
//                            wholeTax += taxCal;
//
//                            taxList.add(new Product_Details_Modal(jsonObject1.getString("Tax_Id"),
//                                    jsonObject1.getString("Tax_Type"), jsonObject1.getDouble("Tax_Val"), taxCal));


                        }
                    }
                }


                //rate = Double.parseDouble(Product_Details_Modalitem.get(pos).getMRP());
                rate= ((Double.parseDouble(Product_Details_Modalitem.get(pos).getMRP()) * 100) / (totTax + 100));

                wholeTax = (Product_Details_Modalitem.get(pos).getQty() * Product_Details_Modalitem.get(pos).getCnvQty() *
                        Double.parseDouble(Product_Details_Modalitem.get(pos).getMRP())) - ((Product_Details_Modalitem.get(pos).getQty() * Product_Details_Modalitem.get(pos).getCnvQty() *
                        Double.parseDouble(Product_Details_Modalitem.get(pos).getMRP()) * 100) /
                        (totTax + 100));
                //wholeTax=(Product_Details_Modalitem.get(pos).getQty() * Product_Details_Modalitem.get(pos).getCnvQty() *
                //Double.parseDouble(Product_Details_Modalitem.get(pos).getMRP()))*totTax/100;
                Log.v("taxCalc:", "val:" + wholeTax + ":Rate:" + rate + ":totTax:" + totTax);

                for (int pTax = 0; pTax < proTaxArr.length(); pTax++) {
                    JSONObject jsonObject1 = proTaxArr.getJSONObject(pTax);
                    double taxCal = (rate * Product_Details_Modalitem.get(pos).getQty() * Product_Details_Modalitem.get(pos).getCnvQty()) *
                            ((jsonObject1.getDouble("Tax_Val") / 100));

                    taxList.add(new Product_Details_Modal(jsonObject1.getString("Tax_Id"),
                            jsonObject1.getString("Tax_Type"), jsonObject1.getDouble("Tax_Val"), taxCal));

                }

                Product_Details_Modalitem.get(pos).setProductDetailsModal(taxList);
                Product_Details_Modalitem.get(pos).setAmount(Double.valueOf(formatter.format(Product_Details_Modalitem.get(pos).getAmount())));
                Product_Details_Modalitem.get(pos).setTax(Double.parseDouble(formatter.format(wholeTax)));
            }
        } catch (Exception e) {
            Log.v("taxCalc:ex", e.getMessage());
        }
    }

    private void FilterTypes(String GrpID) {
        try {
            JSONArray TypGroups = new JSONArray();
            JSONArray tTypGroups = db.getMasterData(Constants.POS_ProdTypes_List);
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
            if (TypGroups.length() > 0)
                filterId = TypGroups.getJSONObject(0).getString("id");
            GetJsonData(String.valueOf(db.getMasterData(Constants.POS_Category_List)), "1", filterId);

            RyclBrandListItemAdb TyplistItems = new RyclBrandListItemAdb(TypGroups, this, new onListItemClick() {
                @Override
                public void onItemClick(JSONObject item) {
                    try {
                        GetJsonData(String.valueOf(db.getMasterData(Constants.POS_Category_List)), "1", item.getString("id"));
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
        //type =1 product category data values
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            Category_Modal.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (type.equals("1")) {
                    String id = String.valueOf(jsonObject1.optInt("id"));
                    String name = jsonObject1.optString("name");
                    String typeId = String.valueOf(jsonObject1.optInt("TypID"));
                    String Division_Code = jsonObject1.optString("Division_Code");
                    String Cat_Image = jsonObject1.optString("Cat_Image");
                    String sampleQty = jsonObject1.optString("sampleQty");
                    String colorflag = jsonObject1.optString("colorflag");

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


                POSActivity.CategoryAdapter customAdapteravail = new POSActivity.CategoryAdapter(getApplicationContext(),
                        Category_Modal);
                categorygrid.setAdapter(customAdapteravail);

                showOrderItemList(selectedPos, "");
            }

        } catch (JSONException e) {
            e.printStackTrace();
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

        /*if (Common_Class.isNullOrEmpty(etName.getText().toString()))
            common_class.showMsg(this, "Please Enter the name");
        else if (Common_Class.isNullOrEmpty(etPhone.getText().toString()))
            common_class.showMsg(this, "Please Enter the Phone No");
        else if (Common_Class.isNullOrEmpty(etAddress.getText().toString()))
            common_class.showMsg(this, "Please Enter the Address");
        else*/
        if (Getorder_Array_List.size() == 0)
            Toast.makeText(getApplicationContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
        else
            FilterProduct(Getorder_Array_List);

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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                takeorder.stopAnimation();
                takeorder.revertAnimation();
            }
        }, dely);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvPayMode:
                common_class.getDb_310Data(Constants.PAYMODES, this);
                break;
            case R.id.tvPosOrders:
                startActivity(new Intent(getApplicationContext(), PosHistoryActivity.class));
                break;
            case R.id.ivScanner:
                Intent intent = new Intent(this, QRCodeScanner.class);
                intent.putExtra("scan", "scan");
                startActivity(intent);
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

                    if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {


                        if (Getorder_Array_List != null
                                && Getorder_Array_List.size() > 0) {
                            Log.d("RepeatAni", String.valueOf(takeorder.isAnimating()));
                            if (takeorder.isAnimating()) return;
                            takeorder.startAnimation();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
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
                            }, 500);
                        } else {
                            common_class.showMsg(this, "Your Cart is empty...");
                            ResetSubmitBtn(0);
                        }
                    } else {
                        showOrderList();
                        ResetSubmitBtn(0);
                    }
                } catch (Exception e) {
                    Log.v(TAG, e.getMessage());
                }
                break;


        }
    }

    private void SaveOrder() {
        if (common_class.isNetworkAvailable(this)) {
            if(StockCheck.equalsIgnoreCase("1")) {
                for (int z = 0; z < Getorder_Array_List.size(); z++) {
                    double enterQty = Getorder_Array_List.get(z).getQty();
                    double totQty = (enterQty * Getorder_Array_List.get(z).getCnvQty());
                    if ((Getorder_Array_List.get(z).getBalance() - (int) totQty) < 0) {
                        Toast.makeText(this, "Low Stock", Toast.LENGTH_LONG).show();
                        ResetSubmitBtn(0);
                        return;
                    }
                }
            }

            AlertDialogBox.showDialog(POSActivity.this, HAPApp.Title, "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {
                    common_class.ProgressdialogShow(1, "");
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
                        HeadItem.put("AppVer", BuildConfig.VERSION_NAME);
                        ActivityData.put("Activity_Report_Head", HeadItem);
                        String cusName=etName.getText().toString();
                        if(cusName.equalsIgnoreCase("")){
                            cusName="Customer";
                        }
                        JSONObject OutletItem = new JSONObject();
                        OutletItem.put("stockist_code", sharedCommonPref.getvalue(Constants.Distributor_Id));
                        OutletItem.put("stockist_name", sharedCommonPref.getvalue(Constants.Distributor_name));
                        OutletItem.put("name", cusName);
                        OutletItem.put("phoneNo", etPhone.getText().toString());
                        OutletItem.put("address", etAddress.getText().toString());
                        OutletItem.put("CashDiscount", cashDiscount);
                        OutletItem.put("NetAmount", formatter.format(totalvalues));
                        OutletItem.put("No_Of_items", tvBillTotItem.getText().toString());
                        OutletItem.put("ordertype", "pos");
                        OutletItem.put("ordertypeid", OrderTypId);
                        OutletItem.put("ordertypenm", OrderTypNm);
                        OutletItem.put("payMode", tvPayMode.getText().toString());
                        OutletItem.put("totAmtTax", totTax);

                        sharedCommonPref.save(Constants.Retailor_Name_ERP_Code,cusName);
                        sharedCommonPref.save(Constants.Retailor_PHNo,etPhone.getText().toString());
                        OutletItem.put("RecAmt",
                                tvPayMode.getText().toString().equalsIgnoreCase("cash") ? etRecAmt.getText().toString() : "0");
                        OutletItem.put("Balance", tvPayMode.getText().toString().equalsIgnoreCase("cash") ?
                                tvBalAmt.getText().toString() : "0");

                        if (strLoc.length > 0) {
                            OutletItem.put("Lat", strLoc[0]);
                            OutletItem.put("Long", strLoc[1]);
                        } else {
                            OutletItem.put("Lat", "");
                            OutletItem.put("Long", "");
                        }
                        JSONArray Order_Details = new JSONArray();
                        JSONArray totTaxArr = new JSONArray();

                        for (int z = 0; z < Getorder_Array_List.size(); z++) {
                            JSONObject ProdItem = new JSONObject();
                            ProdItem.put("product_Name", Getorder_Array_List.get(z).getName());
                            ProdItem.put("product_code", Getorder_Array_List.get(z).getId());
                            ProdItem.put("ERPCode", Getorder_Array_List.get(z).getERP_Code());
                            ProdItem.put("Product_Qty", Getorder_Array_List.get(z).getQty());
                            ProdItem.put("Product_RegularQty", Getorder_Array_List.get(z).getRegularQty());
                            double cf = (Getorder_Array_List.get(z).getCnvQty());
                            ProdItem.put("Product_Total_Qty", cf > 0 ? Getorder_Array_List.get(z).getQty() *
                                    cf : Getorder_Array_List.get(z).getQty());
                            ProdItem.put("Product_Amount", Getorder_Array_List.get(z).getAmount());
                            ProdItem.put("Rate", formatter.format(Double.valueOf(Getorder_Array_List.get(z).getRate())));
                            ProdItem.put("MRP", formatter.format(Double.valueOf(Getorder_Array_List.get(z).getMRP())));

                            ProdItem.put("free", Getorder_Array_List.get(z).getFree());
                            ProdItem.put("dis", Getorder_Array_List.get(z).getDiscount());//calculation amount
                            ProdItem.put("dis_value", Getorder_Array_List.get(z).getDiscount_value());//api value
                            ProdItem.put("Off_Pro_code", Getorder_Array_List.get(z).getOff_Pro_code());
                            ProdItem.put("Off_Pro_name", Getorder_Array_List.get(z).getOff_Pro_name());
                            ProdItem.put("Off_Pro_Unit", Getorder_Array_List.get(z).getOff_Pro_Unit());
                            ProdItem.put("Off_Scheme_Unit", Getorder_Array_List.get(z).getScheme());
                            ProdItem.put("discount_type", Getorder_Array_List.get(z).getDiscount_type());

                            ProdItem.put("ConversionFactor", Getorder_Array_List.get(z).getCnvQty());
                            ProdItem.put("UOM_Id", Getorder_Array_List.get(z).getUOM_Id());
                            ProdItem.put("UOM_Nm", Getorder_Array_List.get(z).getUOM_Nm());

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
                        ActivityData.put("Activity_Doctor_Report", OutletItem);
                        ActivityData.put("Order_Details", Order_Details);
                        data.put(ActivityData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<JsonObject> responseBodyCall = apiInterface.savePOS(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
                    responseBodyCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                try {
                                    common_class.ProgressdialogShow(0, "");
                                    Log.e("JSON_VALUES", response.body().toString());
                                    JSONObject jsonObjects = new JSONObject(response.body().toString());
                                    ResetSubmitBtn(1);
                                    String succ=jsonObjects.getString("success");


                                    if (jsonObjects.getString("success").equals("true")) {
                                        sharedCommonPref.clear_pref(Constants.LOC_POS_DATA);
                                        Shared_Common_Pref.TransSlNo = jsonObjects.getString("OrderID");
                                        sharedCommonPref.save(Constants.FLAG, "POS INVOICE");ProductsLoaded=true;
                                        common_class.ProgressdialogShow(1, "Updating Matrial Details");
                                        common_class.getPOSProduct(POSActivity.this, new OnLiveUpdateListener() {
                                            @Override
                                            public void onUpdate(String mode) {
                                                common_class.CommonIntentwithFinish(Print_Invoice_Activity.class);
                                                common_class.ProgressdialogShow(0, "");
                                            }
                                        });
                                        //common_class.CommonIntentwithFinish(Print_Invoice_Activity.class);

                                    }
                                    common_class.showMsg(POSActivity.this, jsonObjects.getString("Msg"));

                                } catch (Exception e) {
                                    common_class.ProgressdialogShow(0, "");
                                    ResetSubmitBtn(2);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            common_class.ProgressdialogShow(0, "");
                            Log.e("SUBMIT_VALUE", "ERROR");
                            ResetSubmitBtn(2);
                        }
                    });

                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    ResetSubmitBtn(0);
                }
            });
        } else {
            Toast.makeText(this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
            ResetSubmitBtn(0);
        }
    }

    private void FilterProduct(List<Product_Details_Modal> orderList) {
        findViewById(R.id.llUserDetail).setVisibility(View.GONE);
        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.rlSearchParent).setVisibility(View.GONE);
        findViewById(R.id.llBillHeader).setVisibility(View.VISIBLE);
        findViewById(R.id.llLblUserDetail).setVisibility(View.VISIBLE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.VISIBLE);
        lin_gridcategory.setVisibility(View.GONE);
        lin_orderrecyclerview.setVisibility(View.VISIBLE);

        lblName.setText(etName.getText().toString());
        lblPhone.setText(etPhone.getText().toString());
        lblAddress.setText(etAddress.getText().toString());
        takeorder.setText("SUBMIT");

        mProdct_Adapter = new Prodct_Adapter(orderList, R.layout.product_pos_pay_recyclerview, getApplicationContext(), -1);
        recyclerView.setAdapter(mProdct_Adapter);
        showFreeQtyList();
    }

    void showFreeQtyList() {
        freeQty_Array_List = new ArrayList<>();
        freeQty_Array_List.clear();

        for (Product_Details_Modal pm : Product_Modal) {

            if (pm.getQty() > 0) {
                if (!Common_Class.isNullOrEmpty(pm.getFree()) && !pm.getFree().equals("0")) {
                    freeQty_Array_List.add(pm);

                }
            }
        }
        if (freeQty_Array_List != null && freeQty_Array_List.size() > 0) {
            findViewById(R.id.cdFreeQtyParent).setVisibility(View.VISIBLE);
            Free_Adapter mFreeAdapter = new Free_Adapter(freeQty_Array_List, R.layout.product_free_recyclerview, getApplicationContext());
            freeRecyclerview.setAdapter(mFreeAdapter);

        } else {
            findViewById(R.id.cdFreeQtyParent).setVisibility(View.GONE);

        }

    }


    public void updateToTALITEMUI() {
        try {
            TextView tvTotalItems = findViewById(R.id.tvTotalItems);
            TextView tvTotLabel = findViewById(R.id.tvTotLabel);
            tvTotalAmount = findViewById(R.id.tvTotalAmount);
            tvNetAmtTax = findViewById(R.id.tvNetAmtTax);
            TextView tvTax = findViewById(R.id.tvTaxVal);
            TextView tvTaxLabel = findViewById(R.id.tvTaxLabel);
            TextView tvBillSubTotal = findViewById(R.id.subtotal);
            TextView tvSaveAmt = findViewById(R.id.tvSaveAmt);
            tvBillTotItem = findViewById(R.id.totalitem);
            TextView tvBillTotQty = findViewById(R.id.tvtotalqty);
            TextView tvBillToPay = findViewById(R.id.tvnetamount);
            TextView tvCashDiscount = findViewById(R.id.tvcashdiscount);


            Getorder_Array_List = new ArrayList<>();
            Getorder_Array_List.clear();
            totalvalues = 0;
            totalQty = 0;
            cashDiscount = 0;
            taxVal = 0;


            for (int pm = 0; pm < Product_Modal.size(); pm++) {


                if (Product_Modal.get(pm).getQty() > 0) {

                    cashDiscount += (int) Product_Modal.get(pm).getDiscount();

                    totalvalues += Product_Modal.get(pm).getAmount();

                    totalQty += Product_Modal.get(pm).getQty();

                    if (Product_Modal.get(pm).getTax() > 0)
                        taxVal += Product_Modal.get(pm).getTax();


                    Getorder_Array_List.add(Product_Modal.get(pm));


                }

            }

//            totTax = 0;
//            try {
//                String totAmtTax = sharedCommonPref.getvalue(Constants.POS_NETAMT_TAX);
//                JSONObject obj = new JSONObject(totAmtTax);
//
//                if (obj.getBoolean("success")) {
//                    JSONArray arr = obj.getJSONArray("Data");
//                    for (int i = 0; i < arr.length(); i++) {
//                        JSONObject taxObj = arr.getJSONObject(i);
//                        double taxCal = (totalvalues) *
//                                ((taxObj.getDouble("Value") / 100));
//                        totTax = +totTax + taxCal;
//
//                    }
//                }
//            } catch (Exception e) {
//
//            }

           // totalvalues = totalvalues + totTax;

          //  tvNetAmtTax.setText(CurrencySymbol+" " + totTax);

            tvTotalAmount.setText(CurrencySymbol+" " + formatter.format(totalvalues));
            tvTotalItems.setText("Items : " + Getorder_Array_List.size() + "   Qty : " + totalQty);

            if (Getorder_Array_List.size() == 1)
                tvTotLabel.setText("Price (1 item)");
            else
                tvTotLabel.setText("Price (" + Getorder_Array_List.size() + " items)");

            tvBillSubTotal.setText(CurrencySymbol+" " + formatter.format(totalvalues));
            tvBillTotItem.setText("" + Getorder_Array_List.size());
            tvBillTotQty.setText("" + totalQty);
            tvBillToPay.setText(CurrencySymbol+" " + formatter.format(totalvalues));
            tvCashDiscount.setText(CurrencySymbol+" " + formatter.format(cashDiscount));
            // tvTax.setText(CurrencySymbol+" " + formatter.format(taxVal));


            if (cashDiscount > 0) {
                tvSaveAmt.setVisibility(View.VISIBLE);
                tvSaveAmt.setText("You will save "+CurrencySymbol+" " + formatter.format(cashDiscount) + " on this order");
            } else
                tvSaveAmt.setVisibility(View.GONE);
            orderTotTax = new ArrayList<>();
            orderTotTax.clear();

            for (int l = 0; l < Getorder_Array_List.size(); l++) {
                if (Getorder_Array_List.get(l).getProductDetailsModal() != null) {
                    for (int tax = 0; tax < Getorder_Array_List.get(l).getProductDetailsModal().size(); tax++) {
                        String label = Getorder_Array_List.get(l).getProductDetailsModal().get(tax).getTax_Type();
                        Double amt = Getorder_Array_List.get(l).getProductDetailsModal().get(tax).getTax_Amt();
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
                }
            }

            String label = "", amt = "";
            for (int i = 0; i < orderTotTax.size(); i++) {
                label = label + orderTotTax.get(i).getTax_Type() + "\n";
                amt = amt + CurrencySymbol+" " + formatter.format(orderTotTax.get(i).getTax_Amt()) + "\n";
            }
            tvTaxLabel.setText(label);
            tvTax.setText(amt);
            if (orderTotTax.size() == 0) {
                tvTaxLabel.setVisibility(View.INVISIBLE);
                tvTax.setVisibility(View.INVISIBLE);
            } else {
                tvTaxLabel.setVisibility(View.VISIBLE);
                tvTax.setVisibility(View.VISIBLE);
            }

            sharedCommonPref.save(Constants.LOC_POS_DATA, gson.toJson(Product_Modal));
        } catch (Exception e) {

        }

    }

    public void showOrderItemList(int categoryPos, String filterString) {
        categoryPos = selectedPos;
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
        Category_Nametext.setText(listt.get(categoryPos).getName() +" ( "+Product_ModalSetAdapter.size()+" )");

        mProdct_Adapter = new Prodct_Adapter(Product_ModalSetAdapter, R.layout.product_pos_recyclerview, getApplicationContext(), categoryPos);
        recyclerView.setAdapter(mProdct_Adapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            ResetSubmitBtn(0);
        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {


            switch (key) {
                case Constants.CURRENT_STOCK:
                    Log.v("POS_CURRENT_STK:", apiDataResponse);

                    JSONObject currStkObj = new JSONObject(apiDataResponse);
                    List<Dashboard_View_Model> approvalList = new ArrayList<>();

                    if (currStkObj.getBoolean("success")) {
                        JSONArray arr = currStkObj.getJSONArray("Data");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            String val = obj.toString();
                            String result = val.replaceAll("[(){}]", "");

                            List<String> list = Arrays.asList(result.split(","));

                            for (int l = 0; l < list.size(); l++) {
                                int split = list.get(l).indexOf(":");
                                String name = list.get(l).substring(1, split - 1);
                                String cnt = list.get(l).substring(split + 1, list.get(l).length());

                                if (name.equalsIgnoreCase("SoldValue"))
                                    cnt = CurrencySymbol+" " + cnt;
                                approvalList.add(new Dashboard_View_Model(name, cnt));
                            }

                        }
                    }


                    rvCurrentStk.setAdapter(new Dashboard_View_Adapter(approvalList, R.layout.adapter_current_stk_layout, getApplicationContext(), new AdapterOnClick() {
                        @Override
                        public void onIntentClick(int Name) {

                        }
                    }));
                    break;
                case Constants.POS_NETAMT_TAX:
                    Log.v("POS_NETAMT_TAX:", apiDataResponse);
                    break;
                case Constants.STOCK_DATA:
                    JSONObject stkObj = new JSONObject(apiDataResponse);
                    if (stkObj.getBoolean("success")) {
                        JSONArray arr = stkObj.getJSONArray("Data");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            for (int pm = 0; pm < Product_Modal.size(); pm++) {
                                if (obj.getString("ProdCode").equalsIgnoreCase(Product_Modal.get(pm).getId())) {
                                    Product_Modal.get(pm).setBalance(obj.getInt("Balance"));
                                    break;
                                }
                            }
                        }
                    }


                    break;
                case Constants.PAYMODES:
                    payList.clear();
                    JSONObject obj = new JSONObject(apiDataResponse);
                    if (obj.getBoolean("success")) {
                        JSONArray jsonArray = obj.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataObj = jsonArray.getJSONObject(i);
                            payList.add(new Common_Model(dataObj.getString("Name"), dataObj.getString("Code")));
                        }
                    } else {
                        common_class.showMsg(this, "No Records Found");
                    }
                    if (payList.size() > 0) {
                        common_class.showCommonDialog(payList, 20, this);
                    }

                    break;


                case Constants.POS_SCHEME:
                    Log.v(TAG + "scheme:", apiDataResponse);
                    JSONObject jsonObject = new JSONObject(apiDataResponse);

                    if (jsonObject.getBoolean("success")) {

                        Gson gson = new Gson();
                        List<Product_Details_Modal> product_details_modalArrayList = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("Data");

                        if (jsonArray != null && jsonArray.length() > 1) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                product_details_modalArrayList.add(new Product_Details_Modal(jsonObject1.getString("Product_Code"),
                                        jsonObject1.getString("Scheme"), jsonObject1.getString("Free"),
                                        Double.valueOf(jsonObject1.getString("Discount")), jsonObject1.getString("Discount_Type"),
                                        jsonObject1.getString("Package"), 0, jsonObject1.getString("Offer_Product"),
                                        jsonObject1.getString("Offer_Product_Name"), jsonObject1.getString("offer_product_unit")));


                            }
                        }

                        sharedCommonPref.save(Constants.POS_SCHEME, gson.toJson(product_details_modalArrayList));


                    } else {
                        sharedCommonPref.clear_pref(Constants.POS_SCHEME);

                    }
                    break;
                case Constants.POS_TAXList:
                    JSONObject jsonObjectTax = new JSONObject(apiDataResponse);
                    if (jsonObjectTax.getBoolean("success")) {
                        sharedCommonPref.save(Constants.POS_TAXList, apiDataResponse);

                    } else {
                        sharedCommonPref.clear_pref(Constants.POS_TAXList);

                    }
                    break;
            }
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.SCAN_DATA))) {
            loadScanData();
        }
        if(ProductsLoaded==true){
            initData();
        }

    }

    private void loadScanData() {
        scanProId = "";
        for (int pm = 0; pm < Product_Modal.size(); pm++) {

            if (!Common_Class.isNullOrEmpty(Product_Modal.get(pm).getBar_Code()) && !Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.SCAN_DATA)) && Product_Modal.get(pm).getBar_Code().equals(sharedCommonPref.getvalue(Constants.SCAN_DATA))) {
                scanProId = Product_Modal.get(pm).getId();
                etQty.setText("" + Product_Modal.get(pm).getQty());
                tvName.setText(Product_Modal.get(pm).getName());

                Glide.with(this)
                        .load(Product_Modal.get(pm).getPImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ImgVProd);
                tvMRP.setText(CurrencySymbol+" " + Product_Modal.get(pm).getMRP());
                break;
            }
        }

        sharedCommonPref.save(Constants.SCAN_DATA, "");
        if (scanProId.equals("")) {
            common_class.showMsg(this, "No Products Found");
        } else {
            findViewById(R.id.rlParent).setVisibility(View.VISIBLE);

        }
        ivMns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sVal = etQty.getText().toString();
                if (sVal.equalsIgnoreCase("")) sVal = "0";
                if (Integer.parseInt(sVal) > 0) {
                    etQty.setText(String.valueOf(Integer.parseInt(sVal) - 1));
                }
            }
        });
        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sVal = etQty.getText().toString();
                if (sVal.equalsIgnoreCase("")) sVal = "0";
                etQty.setText(String.valueOf(Integer.parseInt(sVal) + 1));
            }
        });

        etQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (findViewById(R.id.rlParent).getVisibility() == View.VISIBLE) {
                updateQtyScanData();
            } else {
                if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
                    moveProductScreen();
                } else {
                    common_class.commonDialog(this, SFA_Activity.class, "POS?");

                }
                return true;
            }
        }
        return false;
    }

    void moveProductScreen() {
        lin_gridcategory.setVisibility(View.VISIBLE);
        findViewById(R.id.llUserDetail).setVisibility(View.VISIBLE);
        findViewById(R.id.llLblUserDetail).setVisibility(View.GONE);
        findViewById(R.id.rlSearchParent).setVisibility(View.VISIBLE);
        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.llBillHeader).setVisibility(View.GONE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.GONE);
        findViewById(R.id.cdFreeQtyParent).setVisibility(View.GONE);
        takeorder.setText("PROCEED");
        showOrderItemList(selectedPos, "");
    }

    void updateQtyScanData() {
        sharedCommonPref.clear_pref(Constants.SCAN_DATA);
        findViewById(R.id.rlParent).setVisibility(View.GONE);
        int qty = Common_Class.isNullOrEmpty(etQty.getText().toString()) ? 0 : Integer.parseInt(etQty.getText().toString());
        for (int pm = 0; pm < Product_Modal.size(); pm++) {
            if (Product_Modal.get(pm).getId().equals(scanProId)) {
                Product_Modal.get(pm).setQty(qty);
                Product_Modal.get(pm).setAmount(qty * Double.parseDouble(Product_Modal.get(pm).getMRP()));

                sumofTax(Product_Modal, pm);
            }
        }

        if (takeorder.getText().toString().equals("PROCEED")) {
            mProdct_Adapter = new Prodct_Adapter(Product_ModalSetAdapter, R.layout.product_pos_recyclerview, getApplicationContext(), selectedPos);
            recyclerView.setAdapter(mProdct_Adapter);
        } else {

            Getorder_Array_List = new ArrayList<>();
            Getorder_Array_List.clear();

            for (int pm = 0; pm < Product_Modal.size(); pm++) {
                if (Product_Modal.get(pm).getQty() > 0) {
                    Getorder_Array_List.add(Product_Modal.get(pm));
                }
            }

            mProdct_Adapter = new Prodct_Adapter(Getorder_Array_List, R.layout.product_pos_pay_recyclerview, getApplicationContext(), -1);
            recyclerView.setAdapter(mProdct_Adapter);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.rlQtyParent:
                updateQtyScanData();
                break;
        }
        return false;
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        switch (type) {
            case 1:

                int qty = (int) (Product_ModalSetAdapter.get(uomPos).getQty() * Double.parseDouble((myDataset.get(position).getPhone())));
                if(StockCheck.equalsIgnoreCase("1") && qty > Product_ModalSetAdapter.get(uomPos).getBalance() ){
                    common_class.showMsg(this, "Can't exceed Stock");
                }else{
                //if (Product_ModalSetAdapter.get(uomPos).getBalance() >= qty || Product_ModalSetAdapter.get(uomPos).getCheckStock() == 0) {
                    Product_ModalSetAdapter.get(uomPos).setCnvQty(Double.parseDouble((myDataset.get(position).getPhone())));
                    Product_ModalSetAdapter.get(uomPos).setUOM_Id(myDataset.get(position).getId());
                    Product_ModalSetAdapter.get(uomPos).setUOM_Nm(myDataset.get(position).getName());
                    mProdct_Adapter.notify(Product_ModalSetAdapter, R.layout.product_pos_recyclerview, getApplicationContext(), 1);
                }
//                else {
//                    common_class.showMsg(this, "Can't exceed Stock");
//                }
                break;
            case 20:
                tvPayMode.setText("" + myDataset.get(position).getName());
                if (myDataset.get(position).getName().equalsIgnoreCase("cash"))
                    findViewById(R.id.llPayAmtDetail).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.llPayAmtDetail).setVisibility(View.GONE);
                break;

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
        public void onBindViewHolder(MyViewHolder holder, int position) {
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

                holder.gridcolor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                    }
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

        public void notify(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context, int categoryType) {
            this.Product_Details_Modalitem = Product_Details_Modalitem;
            this.rowLayout = rowLayout;
            this.context = context;
            this.CategoryType = categoryType;
            notifyDataSetChanged();

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

                Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(holder.getBindingAdapterPosition());

                holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());
                holder.erpCode.setText("" + Product_Details_Modal.getERP_Code().toUpperCase());
                holder.Amount.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(Product_Details_Modal.getAmount()));
                if (!Common_Class.isNullOrEmpty(Product_Details_Modal.getUOM_Nm()))
                    holder.tvUOM.setText(Product_Details_Modal.getUOM_Nm());
                else {
                    holder.tvUOM.setText(Product_Details_Modal.getDefault_UOM_Name());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Nm(Product_Details_Modal.getDefault_UOM_Name());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Id("" + Product_Details_Modal.getDefaultUOM());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setCnvQty(Product_Details_Modal.getDefaultUOMQty());
                }

                if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() == null)
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setBalance(0);


                holder.Rate.setText(CurrencySymbol+" " + formatter.format(Double.parseDouble(Product_Details_Modal.getMRP()) * Product_Details_Modal.getCnvQty()));

                //  holder.RegularQty.setText("" + Product_Details_Modal.getRegularQty());

                if (!Common_Class.isNullOrEmpty(Product_Details_Modal.getBar_Code()))
                    Log.v(TAG, "name:" + Product_Details_Modal.getName() + " :code:" + Product_Details_Modal.getBar_Code());
                double totQty= Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty();
                //holder.tvStock.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance());
                holder.tvStock.setText("" + String.format("%.2f", (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance()/Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty())).replaceAll(".00","") + " " + holder.tvUOM.getText());

                holder.tvTknStock.setText("" + ((int) totQty) + " EA");
                holder.tvCLStock.setText("" + (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty) + " EA");

                holder.tvTknStock.setTextColor(getResources().getColor(R.color.green));
                holder.tvCLStock.setTextColor(getResources().getColor(R.color.green));
                if((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty)<0 && StockCheck.equalsIgnoreCase("1")) {
                    holder.itemView.setBackgroundColor(getResources().getColor(R.color.color_red));
                    holder.tvTknStock.setTextColor(getResources().getColor(R.color.color_red));
                    holder.tvCLStock.setTextColor(getResources().getColor(R.color.color_red));
                }
                //holder.tvBatchNo.setText("Batch : "+Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBatchNo());
                holder.tvTknStock.setVisibility(View.GONE);
                holder.tvCLStock.setVisibility(View.GONE);

                if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() > 0)
                    holder.tvStock.setTextColor(getResources().getColor(R.color.green));
                else
                    holder.tvStock.setTextColor(getResources().getColor(R.color.color_red));

                if (CategoryType >= 0) {


                    holder.totalQty.setText("Total Qty : " + ((int)
                            (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty()/*
                             * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty()*/)));

                    if (!Product_Details_Modal.getPImage().equalsIgnoreCase("")) {
                        holder.ImgVwProd.clearColorFilter();
                        Glide.with(this.context)
                                .load(Product_Details_Modal.getPImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.ImgVwProd);
                    } else {
                        holder.ImgVwProd.setImageDrawable(getResources().getDrawable(R.drawable.product_logo));
                        holder.ImgVwProd.setColorFilter(getResources().getColor(R.color.grey_500));
                    }


                    holder.regularAmt.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty() * Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getMRP())));

                    holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(Double.parseDouble(Product_Details_Modal.getMRP()) * Product_Details_Modal.getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty()));


                    holder.rlUOM.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uomPos = position;
                            uomList = new ArrayList<>();

                            String uomids="";
                            if (Product_Details_Modal.getUOMList() != null && Product_Details_Modal.getUOMList().size() > 0) {
                                for (int i = 0; i < Product_Details_Modal.getUOMList().size(); i++) {
                                    com.milksales.godairy.SFA_Model_Class.Product_Details_Modal.UOM uom = Product_Details_Modal.getUOMList().get(i);

                                    if((";"+uomids).toLowerCase().indexOf(";"+uom.getUOM_Id().toLowerCase()+";")<0) {
                                        uomids += uom.getUOM_Id().toLowerCase() + ";";
                                        uomList.add(new Common_Model(uom.getUOM_Nm(), uom.getUOM_Id(), "", "", String.valueOf(uom.getCnvQty())));
                                    }
                                }
                                common_class.showCommonDialog(uomList, 1, POSActivity.this);
                            } else {
                                common_class.showMsg(POSActivity.this, "No Records Found.");
                            }
                        }
                    });

                }

                holder.tvTaxLabel.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modal.getTax()));
                if (Product_Details_Modal.getQty() > 0)
                    holder.Qty.setText("" + Product_Details_Modal.getQty());

                if (Common_Class.isNullOrEmpty(Product_Details_Modal.getFree()))
                    holder.Free.setText("0");
                else
                    holder.Free.setText("" + Product_Details_Modal.getFree());


                holder.Disc.setText(CurrencySymbol+" "+ formatter.format(Product_Details_Modal.getDiscount()));


                holder.QtyPls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sVal = holder.Qty.getText().toString();
                        if (sVal.equalsIgnoreCase("")) sVal = "0";

                        int order = (int) ((Integer.parseInt(sVal) + 1) * Product_Details_Modal.getCnvQty());
                        int balance = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance();
                        if(StockCheck.equalsIgnoreCase("1") && order > balance){
                            common_class.showMsg(POSActivity.this, "Can't exceed Stock");
                        }else{
                            //if ((balance >= order) || Product_Details_Modal.getCheckStock() == 0) {
                            if (Product_Details_Modal.getCheckStock() == 1)
                                holder.tvStock.setText("" + (int) (balance - order));
                            holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) + 1));
//                            } else {
//                                common_class.showMsg(POSActivity.this, "Can't exceed stock");
//                            }
                        }
                    }
                });
                holder.QtyMns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String sVal = holder.Qty.getText().toString();
                            if (sVal.equalsIgnoreCase("")) sVal = "0";
                            if (Integer.parseInt(sVal) > 0) {
                                holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) - 1));

                                int order = (int) ((Integer.parseInt(sVal) - 1) * Product_Details_Modal.getCnvQty());
                                int balance = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance();
                                if (Product_Details_Modal.getCheckStock() == 1)
                                    holder.tvStock.setText("" + (int) (balance - order));
                            }

                        } catch (Exception e) {
                            Log.v(TAG + "QtyMns:", e.getMessage());
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
                                enterQty = Double.valueOf(charSequence.toString());

                            double totQty = (enterQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty());


                            if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() < totQty  && StockCheck.equalsIgnoreCase("1")) {
//                                totQty = 0;
//                                enterQty = 0;
//                                holder.Qty.setText("0");
                                // common_class.showMsg(POSActivity.this, "No stock");
                                //totQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty();
                                //enterQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty();

                                common_class.showMsg(POSActivity.this, "Can't exceed stock");
                                //holder.Qty.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty());

                            }
                            //if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCheckStock() > 0)
                            //    holder.tvStock.setText("" + (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance()) + " EA");
                            holder.tvStock.setText("" + String.format("%.2f", (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance()/Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty())).replaceAll(".00","") + " " + holder.tvUOM.getText());

                            holder.tvTknStock.setText("" + ((int) totQty) + " EA");
                            holder.tvCLStock.setText("" + (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty) + " EA");
                            holder.tvTknStock.setVisibility(View.GONE);
                            holder.tvCLStock.setVisibility(View.GONE);
                                // holder.tvTknStock.setTextColor(getResources().getColor(R.color.green));
                            //holder.tvCLStock.setTextColor(getResources().getColor(R.color.green));
                            holder.itemView.setBackgroundColor(getResources().getColor(R.color.white));
                            if((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty)<0 && StockCheck.equalsIgnoreCase("1")) {
                            holder.itemView.setBackgroundColor(getResources().getColor(R.color.color_red));
                             //   holder.tvTknStock.setTextColor(getResources().getColor(R.color.color_red));
                                // holder.tvCLStock.setTextColor(getResources().getColor(R.color.color_red));
                            }

                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setQty((int) enterQty);
                            holder.Amount.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(totQty * Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getMRP())));
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(Double.valueOf(formatter.format(totQty *
                                    Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getMRP()))));
                            if (CategoryType >= 0) {
                                holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(enterQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty() * Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getMRP())));
                                holder.totalQty.setText("Total Qty : " + (int) /*totQty*/enterQty);
                            }


                            String strSchemeList = sharedCommonPref.getvalue(Constants.POS_SCHEME);

                            Type type = new TypeToken<ArrayList<Product_Details_Modal>>() {
                            }.getType();
                            List<Product_Details_Modal> product_details_modalArrayList = gson.fromJson(strSchemeList, type);

                            double highestScheme = 0;
                            boolean haveVal = false;
                            if (totQty > 0 && product_details_modalArrayList != null && product_details_modalArrayList.size() > 0) {

                                for (int i = 0; i < product_details_modalArrayList.size(); i++) {

                                    if (Product_Details_Modal.getId().equals(product_details_modalArrayList.get(i).getId())) {

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


                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount((Math.round(discountVal)));

                                                    } else {
                                                        //Rs
                                                        if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                            double freePer = (totQty / highestScheme);

                                                            double freeVal = freePer * (product_details_modalArrayList.
                                                                    get(i).getDiscount());

                                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount((Math.round(freeVal)));
                                                        } else {
                                                            int val = (int) (totQty / highestScheme);
                                                            double freeVal = val * (product_details_modalArrayList.get(i).getDiscount());
                                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount((freeVal));
                                                        }
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

                            if (!haveVal) {
                                holder.Free.setText("0");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree("0");

                                holder.Disc.setText(CurrencySymbol+" 0.00");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(0.00);

                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_code("");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_name("");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_Unit("");

                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_value("0.00");
                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_type("");


                            } else {

                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()) -
                                        (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));
                                holder.Free.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getFree());
                                holder.Disc.setText(CurrencySymbol+" "+ formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));
                                holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
                            }
                            sumofTax(Product_Details_Modalitem, holder.getBindingAdapterPosition());
                            holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
                            holder.tvTaxLabel.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTax()));
                            updateToTALITEMUI();

                            //hide code for del also unwanted edit scenario
//                            if (CategoryType == -1) {
//                                if (holder.Amount.getText().toString().equals("₹0.00")) {
//                                    Product_Details_Modalitem.remove(position);
//                                    notifyDataSetChanged();
//                                }
//                                showFreeQtyList();
//                            }

                        } catch (Exception e) {
                            Log.v(TAG, " orderAdapter:qty " + e.getMessage());
                        }


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

                            AlertDialogBox.showDialog(POSActivity.this, HAPApp.Title,
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


//                holder.Rate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showDialog(Product_Details_Modal);
//                    }
//                });

                updateToTALITEMUI();
            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }


        }

        private void showUOMDialog() {

            LayoutInflater inflater = LayoutInflater.from(POSActivity.this);

            final View view = inflater.inflate(R.layout.fruit_item, null);
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(POSActivity.this).create();
//            alertDialog.setTitle(HAPApp.Title);
//            alertDialog.setMessage(Html.fromHtml(mMessage));
//            alertDialog.setCancelable(false);

//            TextView btnOthers = (TextView) view.findViewById(R.id.tvOthers);
//            TextView btnWeekOFF = (TextView) view.findViewById(R.id.tvWeekOff);
//            TextView btnDeviation = (TextView) view.findViewById(R.id.tvDeviation);
            alertDialog.setView(view);
            alertDialog.show();

        }

        @Override
        public int getItemCount() {
            return Product_Details_Modalitem.size();
        }

        private void showDialog(Product_Details_Modal product_details_modal) {
            try {


                LayoutInflater inflater = LayoutInflater.from(POSActivity.this);

                final View view = inflater.inflate(R.layout.edittext_price_dialog, null);
                AlertDialog alertDialog = new AlertDialog.Builder(POSActivity.this).create();
                alertDialog.setCancelable(false);

                final EditText etComments = view.findViewById(R.id.et_addItem);
                Button btnSave = view.findViewById(R.id.btn_save);
                Button btnCancel = view.findViewById(R.id.btn_cancel);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Common_Class.isNullOrEmpty(etComments.getText().toString())) {
                            common_class.showMsg(POSActivity.this, "Empty value is not allowed");
                        } else if (Double.valueOf(etComments.getText().toString()) > Double.valueOf(product_details_modal.getMRP())) {
                            common_class.showMsg(POSActivity.this, "Enter Rate is greater than "+MRPCap);

                        } else {
                            alertDialog.dismiss();
                            product_details_modal.setRate(Double.valueOf(etComments.getText().toString()));
                            etComments.setText("");
                            notifyDataSetChanged();

                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


                alertDialog.setView(view);
                alertDialog.show();
            } catch (Exception e) {
                Log.e("OrderAdapter:dialog ", e.getMessage());
            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname,erpCode, Rate, Amount, Disc, Free, RegularQty, lblRQty, productQty, regularAmt,
                    QtyAmt, totalQty, tvTaxLabel, tvUOM, tvStock,tvTknStock,tvCLStock,tvBatchNo;
            ImageView ImgVwProd, QtyPls, QtyMns,ivDel;
            EditText Qty;

            LinearLayout llRegular;
            LinearLayout rlUOM;

            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);
                erpCode = view.findViewById(R.id.erpCode);
                QtyPls = view.findViewById(R.id.ivQtyPls);
                QtyMns = view.findViewById(R.id.ivQtyMns);
                Rate = view.findViewById(R.id.Rate);
                Qty = view.findViewById(R.id.Qty);
                RegularQty = view.findViewById(R.id.RegularQty);
                Amount = view.findViewById(R.id.Amount);
                Free = view.findViewById(R.id.Free);
                Disc = view.findViewById(R.id.Disc);
                tvTaxLabel = view.findViewById(R.id.tvTaxTotAmt);
                llRegular = view.findViewById(R.id.llRegular);
                tvUOM = view.findViewById(R.id.tvUOM);
                tvStock = view.findViewById(R.id.tvStockBal);
                tvTknStock = view.findViewById(R.id.tvTknStock);
                tvCLStock = view.findViewById(R.id.tvCLStock);
                tvBatchNo= view.findViewById(R.id.tvBatchNo);


                if (CategoryType >= 0) {
                    ImgVwProd = view.findViewById(R.id.ivAddShoppingCart);
                    lblRQty = view.findViewById(R.id.status);
                    regularAmt = view.findViewById(R.id.RegularAmt);
                    QtyAmt = view.findViewById(R.id.qtyAmt);
                    totalQty = view.findViewById(R.id.totalqty);
                    rlUOM = view.findViewById(R.id.rlUOM);
                }
                else {
                    ivDel = view.findViewById(R.id.ivDel);
                }


            }
        }


    }

    public class Free_Adapter extends RecyclerView.Adapter<Free_Adapter.MyViewHolder> {
        Context context;
        private final List<Product_Details_Modal> Product_Details_Modalitem;
        private final int rowLayout;


        public Free_Adapter(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context) {
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


                holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());

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