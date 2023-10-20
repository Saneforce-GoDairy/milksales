package com.milksales.godairy.SFA_Activity;

import static com.milksales.godairy.SFA_Activity.HAPApp.CurrencySymbol;
import static com.milksales.godairy.SFA_Activity.HAPApp.MRPCap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
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
import com.milksales.godairy.Activity_Hap.SFA_Activity;
import com.milksales.godairy.BuildConfig;
import com.milksales.godairy.Common_Class.AlertDialogBox;
import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Common_Model;
import com.milksales.godairy.Common_Class.Constants;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.AlertBox;
import com.milksales.godairy.Interface.ApiClient;
import com.milksales.godairy.Interface.ApiInterface;
import com.milksales.godairy.Interface.LocationEvents;
import com.milksales.godairy.Interface.Master_Interface;
import com.milksales.godairy.Interface.UpdateResponseUI;
import com.milksales.godairy.Interface.onListItemClick;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Adapter.RyclBrandListItemAdb;
import com.milksales.godairy.SFA_Adapter.RyclListItemAdb;
import com.milksales.godairy.SFA_Model_Class.Category_Universe_Modal;
import com.milksales.godairy.SFA_Model_Class.Product_Details_Modal;
import com.milksales.godairy.common.DatabaseHandler;
import com.milksales.godairy.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Grn_Category_Select extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI, Master_Interface {
    NumberFormat formatter = new DecimalFormat("##0.00");
    List<Category_Universe_Modal> Category_Modal = new ArrayList<>();
    List<Product_Details_Modal> Product_Modal;
    List<Product_Details_Modal> Product_ModalSetAdapter;
    List<Product_Details_Modal> Getorder_Array_List;
    List<Category_Universe_Modal> listt;
    Type userType;
    Gson gson;
    TextView Out_Let_Name, Category_Nametext;

    CircularProgressButton takeorder;

    private RecyclerView recyclerView, categorygrid, freeRecyclerview, Grpgrid, Brndgrid;
    LinearLayout lin_gridcategory;
    Common_Class common_class;

    String Ukey;
    String[] strLoc;
    String Worktype_code = "", Route_Code = "", Dirtributor_Cod = "", Distributor_Name = "", mDCRMode, id="",in="";
    Shared_Common_Pref sharedCommonPref;
    EditText cashdiscount, etOrderNo;
    Prodct_Adapter mProdct_Adapter;
    List<Product_Details_Modal> freeQty_Array_List;

    String TAG = "GRN_Category_Select";
    DatabaseHandler db;
    public int selectedPos = 0;

    RelativeLayout rlCategoryItemSearch;
    ImageView ivClose;
    EditText etCategoryItemSearch, etRecAmt;
    private TextView tvTotalAmount;
    private double totalvalues;
    double cashDiscount;

    private Integer totalQty;
    private TextView tvBillTotItem;


    String orderId = "";
    private LinearLayout rlAddProduct;

    final Handler handler = new Handler();
    List<Product_Details_Modal> orderTotTax;
    private int uomPos;
    ArrayList<Common_Model> uomList;
    Button btnGetOrder;
    private ArrayList<Product_Details_Modal> grn_product;
    private String filterId = "";
    private JSONArray ProdGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_grn_category__select);
            db = new DatabaseHandler(this);
            sharedCommonPref = new Shared_Common_Pref(Grn_Category_Select.this);
            common_class = new Common_Class(this);
            categorygrid = findViewById(R.id.category);
            Grpgrid = findViewById(R.id.PGroup);
            Brndgrid = findViewById(R.id.PBrnd);
            takeorder = findViewById(R.id.takeorder);
            btnGetOrder = findViewById(R.id.btn_get_order);

            etOrderNo = findViewById(R.id.edt_order_no);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            categorygrid.setLayoutManager(layoutManager);

            mDCRMode = sharedCommonPref.getvalue(Shared_Common_Pref.DCRMode);
            common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);
            cashdiscount = findViewById(R.id.cashdiscount);
            lin_gridcategory = findViewById(R.id.lin_gridcategory);
            Out_Let_Name = findViewById(R.id.outlet_name);
            Category_Nametext = findViewById(R.id.Category_Nametext);
            rlCategoryItemSearch = findViewById(R.id.rlCategoryItemSearch);
            ivClose = findViewById(R.id.ivClose);

            etCategoryItemSearch = findViewById(R.id.searchView);
            rlAddProduct = findViewById(R.id.rlAddProduct);


            Product_ModalSetAdapter = new ArrayList<>();
            gson = new Gson();
            takeorder.setOnClickListener(this);
            rlCategoryItemSearch.setOnClickListener(this);
            ivClose.setOnClickListener(this);
            rlAddProduct.setOnClickListener(this);
            Category_Nametext.setOnClickListener(this);
            btnGetOrder.setOnClickListener(this);


            Ukey = Common_Class.GetEkey();
            Out_Let_Name.setText("Hi! " + sharedCommonPref.getvalue(Constants.Distributor_name));
            recyclerView = findViewById(R.id.orderrecyclerview);
            freeRecyclerview = findViewById(R.id.freeRecyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            String OrdersTable = String.valueOf(db.getMasterData(Constants.POS_Product_List));

            userType = new TypeToken<ArrayList<Product_Details_Modal>>() {
            }.getType();
            Product_Modal = gson.fromJson(OrdersTable, userType);


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);

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


            GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "6", "");

            ProdGroups = db.getMasterData(Constants.POS_ProdGroups_List);
            LinearLayoutManager GrpgridlayManager = new LinearLayoutManager(this);
            GrpgridlayManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            Grpgrid.setLayoutManager(GrpgridlayManager);

            RyclListItemAdb grplistItems = new RyclListItemAdb(ProdGroups, this, new onListItemClick() {
                @Override
                public void onItemClick(JSONObject item) {

                    try {
                        FilterTypes(item.getString("id"));
                        common_class.brandPos = 0;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Grpgrid.setAdapter(grplistItems);


            common_class.getDataFromApi(Constants.GRN_ORDER_DATA, this, false);

            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.TAXList)))
                common_class.getDb_310Data(Constants.TAXList, this);
            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.TAXList)))
                common_class.getDb_310Data(Constants.FreeSchemeDiscList, this);


        } catch (Exception e) {

            Log.e(TAG, " invoice oncreate: " + e.getMessage());

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

                Grn_Category_Select.CategoryAdapter customAdapteravail = new Grn_Category_Select.CategoryAdapter(getApplicationContext(),
                        Category_Modal);
                categorygrid.setAdapter(customAdapteravail);

                //  showOrderItemList(selectedPos, "");
                showOrderList();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    void showOrderList() {

        Getorder_Array_List = new ArrayList<>();
        Getorder_Array_List.clear();


        for (int pm = 0; pm < grn_product.size(); pm++) {

            if (grn_product.get(pm).getRegularQty() != null) {
                if (grn_product.get(pm).getRegularQty() > 0) {
                    Getorder_Array_List.add(grn_product.get(pm));

                }
            }
        }

        if (Getorder_Array_List.size() == 0)
            Toast.makeText(getApplicationContext(), "GRN is empty", Toast.LENGTH_SHORT).show();
        else
            FilterProduct(Getorder_Array_List);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_order:
                if (Common_Class.isNullOrEmpty(etOrderNo.getText().toString()))
                    common_class.showMsg(this, "Please Enter Invoice Number");
                else {
                    Shared_Common_Pref.TransSlNo = etOrderNo.getText().toString();
                    common_class.getDataFromApi(Constants.GRN_ORDER_DATA, this, false);
                }
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
                if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
                    if (Getorder_Array_List != null && Getorder_Array_List.size() > 0) {
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
                    }
                } else {
                    showOrderList();
                }
                break;

            case R.id.orderbutton:
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
                break;
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                takeorder.stopAnimation();
                takeorder.revertAnimation();
            }
        }, dely);

    }

    private void SaveOrder() {
        if (common_class.isNetworkAvailable(this)) {

            AlertDialogBox.showDialog(Grn_Category_Select.this, HAPApp.Title, "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
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
                        HeadItem.put("orderValue", formatter.format(totalvalues));
                        HeadItem.put("DataSF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("AppVer", BuildConfig.VERSION_NAME);
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
                        OutletItem.put("TransSlNo", Shared_Common_Pref.TransSlNo);
                        OutletItem.put("doctor_code", Shared_Common_Pref.OutletCode);
                        OutletItem.put("doctor_name", Shared_Common_Pref.OutletName);
                        OutletItem.put("ordertype", "GRN");
                        OutletItem.put("orderId", etOrderNo.getText().toString());


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
                            ProdItem.put("Product_Qty", Getorder_Array_List.get(z).getQty());
                            ProdItem.put("Product_RegularQty", Getorder_Array_List.get(z).getRegularQty());
                            ProdItem.put("Product_InvQty", Getorder_Array_List.get(z).getRegularQty());
                            double cf = (Getorder_Array_List.get(z).getCnvQty());
                            ProdItem.put("Product_Total_Qty", cf > 0 ? Getorder_Array_List.get(z).getQty() *
                                    cf : Getorder_Array_List.get(z).getQty());
                            ProdItem.put("Product_Amount", Getorder_Array_List.get(z).getAmount());
                            ProdItem.put("Rate", String.format("%.2f", Getorder_Array_List.get(z).getRate()));
                            ProdItem.put("free", Getorder_Array_List.get(z).getFree());
                            ProdItem.put("dis", Getorder_Array_List.get(z).getDiscount());
                            ProdItem.put("dis_value", Getorder_Array_List.get(z).getDiscount_value());
                            ProdItem.put("Off_Pro_code", Getorder_Array_List.get(z).getOff_Pro_code());
                            ProdItem.put("Off_Pro_name", Getorder_Array_List.get(z).getOff_Pro_name());
                            ProdItem.put("Off_Pro_Unit", Getorder_Array_List.get(z).getOff_Pro_Unit());
                            ProdItem.put("Off_Scheme_Unit", Getorder_Array_List.get(z).getScheme());
                            ProdItem.put("discount_type", Getorder_Array_List.get(z).getDiscount_type());

                            ProdItem.put("ConversionFactor", Getorder_Array_List.get(z).getCnvQty());
                            ProdItem.put("UOM_Id", Getorder_Array_List.get(z).getUOM_Id());
                            ProdItem.put("UOM_Nm", Getorder_Array_List.get(z).getUOM_Nm());

                            ProdItem.put("mfg", Getorder_Array_List.get(z).getMfg() == null ? "" : Getorder_Array_List.get(z).getMfg());
                            ProdItem.put("exp", Getorder_Array_List.get(z).getExp() == null ? "" : Getorder_Array_List.get(z).getExp());
                            ProdItem.put("batch_no", Getorder_Array_List.get(z).getBatchNo() == null ? "" : Getorder_Array_List.get(z).getBatchNo());
                            ProdItem.put("remarks", Getorder_Array_List.get(z).getRemarks() == null ? "" : Getorder_Array_List.get(z).getRemarks());
                            ProdItem.put("deviation", (Getorder_Array_List.get(z).getRegularQty() - (cf > 0 ? Getorder_Array_List.get(z).getQty() *
                                    cf : Getorder_Array_List.get(z).getQty())));


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
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<JsonObject> responseBodyCall = apiInterface.saveGrn(Shared_Common_Pref.Div_Code, data.toString());
                        responseBodyCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        common_class.ProgressdialogShow(0, "");
                                        Log.e("JSON_VALUES", response.body().toString());
                                        JSONObject jsonObjects = new JSONObject(response.body().toString());

                                        ResetSubmitBtn(1);
                                        if (jsonObjects.getString("success").equals("true")) {
                                            // sharedCommonPref.clear_pref(Constants.LOC_INVOICE_DATA);
                                            common_class.CommonIntentwithFinish(SFA_Activity.class);
                                        }
                                        common_class.showMsg(Grn_Category_Select.this, jsonObjects.getString("Msg"));

                                    } catch (Exception e) {
                                        common_class.ProgressdialogShow(0, "");
                                        Log.e(TAG, "invcatch: " + e.getMessage());
                                        ResetSubmitBtn(2);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.e("SUBMIT_VALUE", "ERROR");
                                ResetSubmitBtn(2);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        ResetSubmitBtn(2);
                    }
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
        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.rlSearchParent).setVisibility(View.GONE);
        findViewById(R.id.llBillHeader).setVisibility(View.GONE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.GONE);
        //rlAddProduct.setVisibility(View.VISIBLE);
        lin_gridcategory.setVisibility(View.GONE);
        takeorder.setText("SUBMIT");

        mProdct_Adapter = new Prodct_Adapter(orderList, R.layout.grn_pay_recyclerview, getApplicationContext(), -1);
        recyclerView.setAdapter(mProdct_Adapter);
        showFreeQtyList();


    }

    void showFreeQtyList() {
        freeQty_Array_List = new ArrayList<>();
        freeQty_Array_List.clear();

        for (Product_Details_Modal pm : grn_product) {

            if (pm.getRegularQty() != null) {
                if (!Common_Class.isNullOrEmpty(pm.getFree()) && !pm.getFree().equals("0")) {
                    freeQty_Array_List.add(pm);

                }
            }
        }
        if (freeQty_Array_List != null && freeQty_Array_List.size() > 0) {
            findViewById(R.id.cdFreeQtyParent).setVisibility(View.VISIBLE);
            Free_Adapter mFreeAdapter = new Free_Adapter(freeQty_Array_List, R.layout.product_free_recyclerview, getApplicationContext(), -1);
            freeRecyclerview.setAdapter(mFreeAdapter);

        } else {
            findViewById(R.id.cdFreeQtyParent).setVisibility(View.GONE);

        }

    }

    public void updateToTALITEMUI() {
        TextView tvTotalItems = findViewById(R.id.tvTotalItems);
        TextView tvTotLabel = findViewById(R.id.tvTotLabel);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        TextView tvTax = findViewById(R.id.tvTaxVal);
        TextView tvBillSubTotal = findViewById(R.id.subtotal);
        TextView tvSaveAmt = findViewById(R.id.tvSaveAmt);
        tvBillTotItem = findViewById(R.id.totalitem);
        TextView tvBillTotQty = findViewById(R.id.tvtotalqty);
        TextView tvBillToPay = findViewById(R.id.tvnetamount);
        TextView tvCashDiscount = findViewById(R.id.tvcashdiscount);
        TextView tvTaxLabel = findViewById(R.id.tvTaxLabel);
        Getorder_Array_List = new ArrayList<>();
        Getorder_Array_List.clear();
        totalvalues = 0;
        totalQty = 0;
        cashDiscount = 0;


        for (int pm = 0; pm < grn_product.size(); pm++) {
            if (grn_product.get(pm).getRegularQty() != null) {
                if (grn_product.get(pm).getRegularQty() > 0) {
                    cashDiscount += grn_product.get(pm).getDiscount();
                    totalvalues += grn_product.get(pm).getAmount();
                    totalQty += grn_product.get(pm).getQty();


                    Getorder_Array_List.add(grn_product.get(pm));


                }
            }
        }

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
        //  tvTax.setText(CurrencySymbol+" " + formatter.format(taxVal));
        //  tvPayAmount.setText("" + (int) totalvalues);


        if (cashDiscount > 0) {
            tvSaveAmt.setVisibility(View.VISIBLE);
            tvSaveAmt.setText("You will save "+CurrencySymbol+" " + formatter.format(cashDiscount) + " on this order");
        } else
            tvSaveAmt.setVisibility(View.GONE);

        orderTotTax = new ArrayList<>();
        orderTotTax.clear();

        for (int l = 0; l < Getorder_Array_List.size(); l++) {
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

        String label = "", amt = "";
        for (int i = 0; i < orderTotTax.size(); i++) {
            label = label + orderTotTax.get(i).getTax_Type() + "\n";
            amt = amt + CurrencySymbol+" " + String.valueOf(formatter.format(orderTotTax.get(i).getTax_Amt())) + "\n";

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


    }

    public void showOrderItemList(int categoryPos, String filterString) {
        Product_ModalSetAdapter.clear();
        for (Product_Details_Modal personNpi : grn_product) {
            if (personNpi.getProductCatCode().toString().equals(listt.get(categoryPos).getId())) {
                if (Common_Class.isNullOrEmpty(filterString))
                    Product_ModalSetAdapter.add(personNpi);
                else {
                    if (personNpi.getName().toLowerCase().contains(filterString.toLowerCase()))
                        Product_ModalSetAdapter.add(personNpi);

                }
            }
        }

        Category_Nametext.setText(listt.get(categoryPos).getName());

        mProdct_Adapter = new Prodct_Adapter(Product_ModalSetAdapter, R.layout.product_grn_recyclerview, getApplicationContext(), categoryPos);
        recyclerView.setAdapter(mProdct_Adapter);

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {


            switch (key) {
                case Constants.FreeSchemeDiscList:
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

                        sharedCommonPref.save(Constants.FreeSchemeDiscList, gson.toJson(product_details_modalArrayList));


                    } else {
                        sharedCommonPref.clear_pref(Constants.FreeSchemeDiscList);

                    }
                    break;
                case Constants.TAXList:
                    JSONObject jsonObjectTax = new JSONObject(apiDataResponse);
                    Log.v("TAX_PRIMARY:", apiDataResponse);

                    if (jsonObjectTax.getBoolean("success")) {
                        sharedCommonPref.save(Constants.TAXList, apiDataResponse);

                    } else {
                        sharedCommonPref.clear_pref(Constants.TAXList);

                    }
                    break;
                case Constants.GRN_ORDER_DATA:
                    if (Common_Class.isNullOrEmpty(apiDataResponse) || apiDataResponse.equalsIgnoreCase("[]")) {
                        common_class.showMsg(this, "No Records Found");
                    } else {
                        JSONArray jsonArray1 = new JSONArray(apiDataResponse);


                        if (jsonArray1 != null && jsonArray1.length() > 0) {

                            grn_product = new ArrayList<>();
                            for (int pm = 0; pm < Product_Modal.size(); pm++) {
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                    if (Product_Modal.get(pm).getId().equals(jsonObject1.getString("Product_Code"))) {
                                        Product_Modal.get(pm).setRegularQty
                                                (jsonObject1.getInt("Quantity"));
                                        Product_Modal.get(pm).setQty(
                                                jsonObject1.getInt("Quantity"));
                                        Product_Modal.get(pm).setAmount(jsonObject1.getDouble("value"));
                                        Product_Modal.get(pm).setDiscount(jsonObject1.getInt("discount"));
//                                Product_Modal.get(pm).setFree(String.valueOf(jsonObject1.getInt("free")));
                                        Product_Modal.get(pm).setRate((jsonObject1.getDouble("Rate")));

                                        JSONArray taxArr = jsonObject1.getJSONArray("TAX_details");
                                        List<Product_Details_Modal> taxList = new ArrayList<>();
                                        double wholeTax = 0;
                                        for (int tax = 0; tax < taxArr.length(); tax++) {
                                            JSONObject taxObj = taxArr.getJSONObject(tax);
                                            taxList.add(new Product_Details_Modal(taxObj.getString("Tax_Code"), taxObj.getString("Tax_Name"), taxObj.getDouble("Tax_Val"),
                                                    taxObj.getDouble("Tax_Amt")));
                                            wholeTax += taxObj.getDouble("Tax_Amt");

                                        }

                                        Product_Modal.get(pm).setProductDetailsModal(taxList);
                                        Product_Modal.get(pm).setTax(Double.parseDouble(formatter.format(wholeTax)));


                                        grn_product.add(Product_Modal.get(pm));
                                    }


                                }
                            }
                            FilterTypes(ProdGroups.getJSONObject(0).getString("id"));

                            updateToTALITEMUI();
                        }
                    }
                    break;

            }
        } catch (Exception e) {

        }

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


    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        try {
            common_class.dismissCommonDialog(type);
            switch (type) {
                case 1:
                    int qty = (int) (Product_ModalSetAdapter.get(uomPos).getQty() * Double.parseDouble(myDataset.get(position).getPhone()));
                    if (Product_ModalSetAdapter.get(uomPos).getRegularQty() >= qty) {
                        Product_ModalSetAdapter.get(uomPos).setCnvQty(Double.parseDouble((myDataset.get(position).getPhone())));
                        Product_ModalSetAdapter.get(uomPos).setUOM_Id(myDataset.get(position).getId());
                        Product_ModalSetAdapter.get(uomPos).setUOM_Nm(myDataset.get(position).getName());
                        mProdct_Adapter.notify(Product_ModalSetAdapter, R.layout.product_grn_recyclerview, getApplicationContext(), 1);
                    } else {
                        common_class.showMsg(this, "Can't exceed Invoice Qty");
                    }
                    break;

            }
        } catch (Exception e) {
            Log.v("UOMSelect:", e.getMessage());
        }
    }

    public class CategoryAdapter extends RecyclerView.Adapter<Grn_Category_Select.CategoryAdapter.MyViewHolder> {

        Context context;
        Grn_Category_Select.CategoryAdapter.MyViewHolder pholder;

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


        public CategoryAdapter(Context applicationContext, List<Category_Universe_Modal> list) {
            this.context = applicationContext;
            listt = list;
        }

        @Override
        public Grn_Category_Select.CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.category_order_horizantal_universe_gridview, parent, false);
            return new Grn_Category_Select.CategoryAdapter.MyViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(Grn_Category_Select.CategoryAdapter.MyViewHolder holder, int position) {
            try {


                holder.icon.setText(listt.get(holder.getBindingAdapterPosition()).getName());
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
                        selectedPos = holder.getBindingAdapterPosition();
                        showOrderItemList(holder.getBindingAdapterPosition(), "");
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

            } catch (
                    Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return listt.size();
        }


    }

    public class Prodct_Adapter extends RecyclerView.Adapter<Prodct_Adapter.MyViewHolder> {
        private List<Product_Details_Modal> Product_Details_Modalitem;
        private int rowLayout;
        Context context;
        int CategoryType;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname, Rate, Amount, Disc, Free, RegularQty, lblRQty, productQty, regularAmt,
                    QtyAmt, totalQty, tvTaxLabel, tvMFG, tvEXP, tvUOM, tvInvQty, tvOrderQty,tvDamageQty,prodCode,etBatchNo,invoiceNo,invoiceDate;

            ImageView ImgVwProd, QtyPls, QtyMns, ivDel;
            EditText Qty,tvReceivedQty,  etRemarks;
            RelativeLayout rlUOM;


            public MyViewHolder(View view) {
                super(view);

                invoiceNo = view.findViewById(R.id.invoiceNo);
                invoiceDate=view.findViewById(R.id.invoiceDate);
                productname = view.findViewById(R.id.productname);
                prodCode=view.findViewById(R.id.prodCode);
//                QtyPls = view.findViewById(R.id.ivQtyPls);
//                QtyMns = view.findViewById(R.id.ivQtyMns);
//                Rate = view.findViewById(R.id.mrp);
                Qty = view.findViewById(R.id.Qty);
                RegularQty = view.findViewById(R.id.RegularQty);
                Amount = view.findViewById(R.id.mrp);
//                Free = view.findViewById(R.id.Free);
//                Disc = view.findViewById(R.id.Disc);
//                tvTaxLabel = view.findViewById(R.id.tvTaxTotAmt);
                tvUOM = view.findViewById(R.id.tvUOM);
//                tvInvQty = view.findViewById(R.id.tvInvQty);
                tvMFG = view.findViewById(R.id.manufactureDate);
                tvEXP = view.findViewById(R.id.expiryDate);
                etBatchNo = view.findViewById(R.id.batchNo);
                tvOrderQty = view.findViewById(R.id.tvOrderQty);
                tvReceivedQty = view.findViewById(R.id.receivedQnty);
                tvDamageQty = view.findViewById(R.id.damageQnty);
                ImgVwProd = view.findViewById(R.id.ivAddShoppingCart);


//                if (CategoryType >= 0) {
//                    rlUOM = view.findViewById(R.id.rlUOM);
//                    tvMFG = view.findViewById(R.id.tvMFG);
//                    tvEXP = view.findViewById(R.id.tvEXP);
//                    etBatchNo = view.findViewById(R.id.batchNo);
//                    etRemarks = view.findViewById(R.id.etRemarks);
//                    ImgVwProd = view.findViewById(R.id.ivAddShoppingCart);
//                    lblRQty = view.findViewById(R.id.status);
//                    regularAmt = view.findViewById(R.id.RegularAmt);
//                    QtyAmt = view.findViewById(R.id.qtyAmt);
//                    totalQty = view.findViewById(R.id.totalqty);
//                } else {
//                    tvOrderQty = view.findViewById(R.id.tvOrderQty);
//                    ivDel = view.findViewById(R.id.ivDel);
//
//                }


            }
        }

        public void notify(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context, int categoryType) {
            this.Product_Details_Modalitem = Product_Details_Modalitem;
            this.rowLayout = rowLayout;
            this.context = context;
            this.CategoryType = categoryType;
            notifyDataSetChanged();

        }


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

        void showDatePickerDialog(int pos, TextView tv, Product_Details_Modal pm) {
            Calendar newCalendar = Calendar.getInstance();
            DatePickerDialog fromDatePickerDialog = new DatePickerDialog(Grn_Category_Select.this, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int month = monthOfYear + 1;
                    String date = ("" + year + "-" + month + "-" + dayOfMonth);

                    //if (common_class.checkDates(pos == 0 ? date : holder.tvMFG.getText().toString(), pos == 1 ? date : holder.tvEXP.getText().toString(), Grn_Category_Select.this)) {
                    tv.setText(date);

                    if (pos == 0)
                        pm.setMfg(date);
                    else
                        pm.setExp(date);


//                    } else {
//                        common_class.showMsg(Grn_Category_Select.this, "Please select valid date");
//                    }
                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            fromDatePickerDialog.show();
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {
                Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(holder.getBindingAdapterPosition());

                in=getIntent().getStringExtra("Invoice_No");
                id=getIntent().getStringExtra("Invoice_Date");

                holder.invoiceNo.setText(in);
                holder.invoiceDate.setText(id);
                holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());
                holder.prodCode.setText("" + Product_Details_Modal.getERP_Code());
                holder.Amount.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(Product_Details_Modal.getAmount()));
                holder.tvEXP.setText(Product_Details_Modal.getExp());
                holder.tvMFG.setText(Product_Details_Modal.getMfg());
                holder.etBatchNo.setText(Product_Details_Modal.getBatchNo());
                holder.tvOrderQty.setText("" + Product_Details_Modal.getQty());
                holder.tvUOM.setText(""+Product_Details_Modal.getUOMList().get(position).getCnvQty());
//                holder.tvInvQty.setText("" + Product_Details_Modal.getRegularQty());

//                if (!Common_Class.isNullOrEmpty(Product_Details_Modal.getUOM_Nm()))
//                    holder.tvUOM.setText(Product_Details_Modal.getUOM_Nm());
//                else {
//                    holder.tvUOM.setText((int) Product_Details_Modal.getCnvQty());
//                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Nm(Product_Details_Modal.getDefault_UOM_Name());
//                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Id("" + Product_Details_Modal.getDefaultUOM());
//                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setCnvQty(Product_Details_Modal.getDefaultUOMQty());
//
//
//                }

                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        try {
                            if (!holder.tvOrderQty.getText().toString().equals("") && !holder.tvReceivedQty.getText().toString().equals("")) {
                                int temp1 = Integer.parseInt(holder.tvOrderQty.getText().toString());
                                int temp2 = Integer.parseInt(holder.tvReceivedQty.getText().toString());
                                holder.tvDamageQty.setText(String.valueOf(temp1 - temp2));
                            }
                        }catch (Exception ignored){
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                };

                holder.tvOrderQty.addTextChangedListener(textWatcher);
                holder.tvReceivedQty.addTextChangedListener(textWatcher);

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

/*

                if (!Common_Class.isNullOrEmpty(Product_Details_Modal.getUOM_Nm()))
                    holder.tvUOM.setText(Product_Details_Modal.getUOM_Nm());
                else {
                    holder.tvUOM.setText(Product_Details_Modal.getDefault_UOM_Name());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Nm(Product_Details_Modal.getDefault_UOM_Name());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Id("" + Product_Details_Modal.getDefaultUOM());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setCnvQty(Product_Details_Modal.getDefaultUOMQty());


                }

                Log.v("uomName:", Product_Details_Modal.getUOM_Nm());
                holder.Rate.setText(CurrencySymbol+" "+ formatter.format(Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty()));

                holder.RegularQty.setText("" + Product_Details_Modal.getRegularQty());
                holder.tvInvQty.setText("" + Product_Details_Modal.getRegularQty());

                if (CategoryType >= 0) {


                    if (Common_Class.isNullOrEmpty(Product_Details_Modal.getExp()))
                        Product_Details_Modal.setExp("");
                    if (Common_Class.isNullOrEmpty(Product_Details_Modal.getMfg()))
                        Product_Details_Modal.setMfg("");
                    if (Common_Class.isNullOrEmpty(Product_Details_Modal.getBatchNo()))
                        Product_Details_Modal.setBatchNo("");
                    if (Common_Class.isNullOrEmpty(Product_Details_Modal.getRemarks()))
                        Product_Details_Modal.setRemarks("");

                    holder.tvEXP.setText("" + Product_Details_Modal.getExp());
                    holder.tvMFG.setText("" + Product_Details_Modal.getMfg());
                    holder.etBatchNo.setText("" + Product_Details_Modal.getBatchNo());
                    holder.etRemarks.setText("" + Product_Details_Modal.getRemarks());

                   */
/* holder.etBatchNo.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setBatchNo(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    holder.etRemarks.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setRemarks(s.toString());

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


                    holder.rlUOM.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                uomPos = position;
                                uomList = new ArrayList<>();

                                if (Product_Details_Modal.getUOMList() != null && Product_Details_Modal.getUOMList().size() > 0) {
                                    for (int i = 0; i < Product_Details_Modal.getUOMList().size(); i++) {
                                        Product_Details_Modal.UOM uom = Product_Details_Modal.getUOMList().get(i);
                                        uomList.add(new Common_Model(uom.getUOM_Nm(), uom.getUOM_Id(), "", "", String.valueOf(uom.getCnvQty())));

                                    }
                                    common_class.showCommonDialog(uomList, 1, Grn_Category_Select.this);
                                } else {
                                    common_class.showMsg(Grn_Category_Select.this, "No Records Found.");
                                }
                            } catch (Exception e) {
                                Log.v(TAG, e.getMessage());
                            }
                        }
                    });

                    holder.tvEXP.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDatePickerDialog(1, holder.tvEXP, Product_Details_Modalitem.get(holder.getBindingAdapterPosition()));

                        }
                    });

                    holder.tvMFG.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDatePickerDialog(0, holder.tvMFG, Product_Details_Modalitem.get(holder.getBindingAdapterPosition()));
                        }
                    });*/
                /*


                    holder.totalQty.setText("Total Qty : " + ((int) (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty())));

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


                    holder.regularAmt.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(Product_Details_Modal.getRegularQty() *
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate() * Product_Details_Modal.getCnvQty()));

                    holder.QtyAmt.setText(CurrencySymbol+" "+ formatter.format(Product_Details_Modal.getRate() * Product_Details_Modal.getQty() * Product_Details_Modal.getCnvQty()));


                } else {
                    try {
                        if (Product_Details_Modal.getOrderQty() == null)
                            Product_Details_Modal.setOrderQty(0);
                        holder.tvOrderQty.setText("" + Product_Details_Modal.getOrderQty());
                    } catch (Exception e) {

                    }
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
//                        String sVal = holder.Qty.getText().toString();
//                        if (sVal.equalsIgnoreCase("")) sVal = "0";
//
//                        if (Product_Details_Modal.getRegularQty() >= ((Integer.parseInt(sVal) + 1)))
//                            holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) + 1));

                        String sVal = holder.Qty.getText().toString();
                        if (sVal.equalsIgnoreCase("")) sVal = "0";

                        int order = (int) ((Integer.parseInt(sVal) + 1) * Product_Details_Modal.getCnvQty());
                        int inv = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRegularQty();
                        if (inv >= order)
                            holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) + 1));
                        else {
                            common_class.showMsg(Grn_Category_Select.this, "Can't exceed Invoice Qty");
                        }
                    }
                });
                holder.QtyMns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sVal = holder.Qty.getText().toString();
                        if (sVal.equalsIgnoreCase("")) sVal = "0";
                        if (Integer.parseInt(sVal) > 0) {
                            holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) - 1));
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


                            if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRegularQty() < totQty) {
                                totQty = 0;
                                enterQty = 0;
                                //holder.Qty.setText("0");
                                common_class.showMsg(Grn_Category_Select.this, "Can't exceed Invoice Qty");
                            }
//                            double enterQty = 0;
//                            if (!charSequence.toString().equals(""))
//                                enterQty = Double.valueOf(charSequence.toString());
//
//                            double totQty = (enterQty * Product_Details_Modal.getCnvQty());


                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setQty((int) enterQty);
                            holder.Amount.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(totQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate()));
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(Double.valueOf(formatter.format(totQty *
                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate())));
                            if (CategoryType >= 0) {
                                holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(enterQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate() * Product_Details_Modal.getCnvQty()));
                                holder.totalQty.setText("Total Qty : " + (int) */
/*totQty*/
                /*
enterQty);
                            }


                            String strSchemeList = sharedCommonPref.getvalue(Constants.FreeSchemeDiscList);

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


                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(((discountVal)));

                                                    } else {
                                                        //Rs
                                                        if (product_details_modalArrayList.get(i).getPackage().equals("N")) {
                                                            double freePer = (totQty / highestScheme);

                                                            double freeVal = freePer * (product_details_modalArrayList.
                                                                    get(i).getDiscount());

                                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(((freeVal)));
                                                        } else {
                                                            int val = (int) (totQty / highestScheme);
                                                            double freeVal = (double) (val * (product_details_modalArrayList.get(i).getDiscount()));
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
                                holder.Disc.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));

                                holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));


                            }


                            String taxRes = sharedCommonPref.getvalue(Constants.TAXList);

                            if (!Common_Class.isNullOrEmpty(taxRes)) {
                                JSONObject jsonObject = new JSONObject(taxRes.toString());
                                List<Product_Details_Modal> taxList = new ArrayList<>();


                                JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                double wholeTax = 0;

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    if (jsonObject1.getString("Product_Detail_Code").equals(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getId())) {

                                        if (jsonObject1.getDouble("Tax_Val") > 0) {
                                            double taxCal = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount() *
                                                    ((jsonObject1.getDouble("Tax_Val") / 100));

                                            wholeTax += taxCal;

                                            taxList.add(new Product_Details_Modal(jsonObject1.getString("Tax_Id"),
                                                    jsonObject1.getString("Tax_Type"), jsonObject1.getDouble("Tax_Val"), taxCal));

                                        }
                                    }
                                }

                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setProductDetailsModal(taxList);

                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(Double.valueOf(formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()
                                        + wholeTax)));

                                Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setTax(Double.parseDouble(formatter.format(wholeTax)));
                                holder.Amount.setText(CurrencySymbol+" "+ formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));


                                holder.tvTaxLabel.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modal.getTax()));


                            }

                            updateToTALITEMUI();


//                            if (CategoryType == -1) {
//                                String amt = holder.Amount.getText().toString();
//                                if (amt.equals(CurrencySymbol+" 0.00")) {
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

                            AlertDialogBox.showDialog(Grn_Category_Select.this, HAPApp.Title,
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
*/

                updateToTALITEMUI();
            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }
        }


        private void showDialog(Product_Details_Modal product_details_modal) {
            try {


                LayoutInflater inflater = LayoutInflater.from(Grn_Category_Select.this);

                final View view = inflater.inflate(R.layout.edittext_price_dialog, null);
                AlertDialog alertDialog = new AlertDialog.Builder(Grn_Category_Select.this).create();
                alertDialog.setCancelable(false);

                final EditText etComments = (EditText) view.findViewById(R.id.et_addItem);
                Button btnSave = (Button) view.findViewById(R.id.btn_save);
                Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Common_Class.isNullOrEmpty(etComments.getText().toString())) {
                            common_class.showMsg(Grn_Category_Select.this, "Empty value is not allowed");
                        } else if (Double.valueOf(etComments.getText().toString()) > Double.valueOf(product_details_modal.getMRP())) {
                            common_class.showMsg(Grn_Category_Select.this, "Enter Rate is greater than "+MRPCap);

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

        @Override
        public int getItemCount() {
            return Product_Details_Modalitem.size();
        }


    }

    public class Free_Adapter extends RecyclerView.Adapter<Free_Adapter.MyViewHolder> {
        private List<Product_Details_Modal> Product_Details_Modalitem;
        private int rowLayout;

        Context context;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname, Rate, Amount, tvDisc, Free, RegularQty, productQty, totalQty, tvTax;


            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);

                Free = view.findViewById(R.id.Free);

            }
        }


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


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
//                moveProductScreen();
//            } else {
            common_class.commonDialog(this, GrnListActivity.class, "GRN?");

            // }
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
        rlAddProduct.setVisibility(View.GONE);
        findViewById(R.id.cdFreeQtyParent).setVisibility(View.GONE);
        takeorder.setText("PROCEED");
        showOrderItemList(selectedPos, "");


    }
}