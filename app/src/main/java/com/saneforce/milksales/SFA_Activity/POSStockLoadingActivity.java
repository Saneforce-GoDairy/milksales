package com.saneforce.milksales.SFA_Activity;

import static com.saneforce.milksales.SFA_Activity.HAPApp.CurrencySymbol;
import static com.saneforce.milksales.SFA_Activity.HAPApp.MRPCap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.saneforce.milksales.BuildConfig;
import com.saneforce.milksales.Common_Class.AlertDialogBox;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.LocationEvents;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.Interface.OnLiveUpdateListener;
import com.saneforce.milksales.Interface.onListItemClick;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.RyclBrandListItemAdb;
import com.saneforce.milksales.SFA_Adapter.RyclListItemAdb;
import com.saneforce.milksales.SFA_Model_Class.Category_Universe_Modal;
import com.saneforce.milksales.SFA_Model_Class.Product_Details_Modal;
import com.saneforce.milksales.common.DatabaseHandler;
import com.saneforce.milksales.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POSStockLoadingActivity extends AppCompatActivity  implements View.OnClickListener, Master_Interface {
    String TAG = "VanSalesOrderActivity",UserInfo = "MyPrefs",Ukey="", axn = "save/posstock",ImageUKey = "",keyEk = "EK",modeId = "",imageSet = "", imageServer = "",imageConvert = "";

    String[] strLoc;
    DatabaseHandler db;
    SharedPreferences UserDetails;
    Shared_Common_Pref sharedCommonPref;

    List<Category_Universe_Modal> Category_Modal = new ArrayList<>();
    List<Product_Details_Modal> Product_ModalSetAdapter;
    List<Product_Details_Modal> Product_Modal;
    List<Product_Details_Modal> Getorder_Array_List;
    List<Category_Universe_Modal> listt;

    Type userType;
    Prodct_Adapter mProdct_Adapter;

    public static final String MyPREFERENCES = "MyPrefs";
    CircularProgressButton takeorder;
    private RecyclerView recyclerView, categorygrid, Grpgrid, Brndgrid, freeRecyclerview;
    TextView Out_Let_Name, Category_Nametext,
            tvOtherBrand, tvQPS, tvPOP, tvCoolerInfo, tvRetailorPhone, retaileAddress, tvHeader, tvDistName;
    EditText etCategoryItemSearch;
    ImageView ivClose,img_lodg_atta,attachedImage;

    RelativeLayout rlCategoryItemSearch;
    LinearLayout lin_orderrecyclerview, lin_gridcategory, rlAddProduct, llCalMob;

    Gson gson;Common_Class common_class;
    public int selectedPos = 0;
    private int uomPos;
    private ArrayList<Common_Model> uomList;
    NumberFormat formatter = new DecimalFormat("##0.00");

    private TextView tvTotalAmount;
    private double totalvalues, taxVal;
    private Integer totalQty;
    private TextView tvBillTotItem;
    private List<Product_Details_Modal> orderTotTax;

    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_posstock_loading);

        try {
            db = new DatabaseHandler(this);
            sharedCommonPref = new Shared_Common_Pref(POSStockLoadingActivity.this);
            common_class = new Common_Class(this);

            UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

            //common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);

            tvHeader = findViewById(R.id.tvHeader);
            Grpgrid = findViewById(R.id.PGroup);
            Brndgrid = findViewById(R.id.PBrnd);
            categorygrid = findViewById(R.id.category);
            takeorder = findViewById(R.id.takeorder);
            lin_orderrecyclerview = findViewById(R.id.lin_orderrecyclerview);
            lin_gridcategory = findViewById(R.id.lin_gridcategory);
            Category_Nametext = findViewById(R.id.Category_Nametext);
            rlCategoryItemSearch = findViewById(R.id.rlCategoryItemSearch);
            rlAddProduct = findViewById(R.id.rlAddProduct);
            ivClose = findViewById(R.id.ivClose);
            tvDistName = findViewById(R.id.tvDistName);

            etCategoryItemSearch = findViewById(R.id.searchView);

            Product_ModalSetAdapter = new ArrayList<>();
            gson = new Gson();
            takeorder.setOnClickListener(this);
            rlCategoryItemSearch.setOnClickListener(this);
            ivClose.setOnClickListener(this);
            rlAddProduct.setOnClickListener(this);
            Ukey = Common_Class.GetEkey();
            recyclerView = findViewById(R.id.orderrecyclerview);
            freeRecyclerview = findViewById(R.id.freeRecyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            categorygrid.setLayoutManager(layoutManager);


            //GetJsonData(String.valueOf(db.getMasterData(Constants.Category_List)), "1", "");
            getProductList();


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);


            Category_Nametext.setOnClickListener(this);

            tvDistName.setOnClickListener(v -> {
                startActivity(new Intent(this, LedgerHistoryActivity.class));
            });


            //findViewById(R.id.tvOrder).setVisibility(View.GONE);

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

        } catch (Exception e) {
            Log.v(TAG, " order oncreate: " + e.getMessage());

        }
    }
    private void getProductList(){
        common_class.ProgressdialogShow(1, "Getting Matrial Details");

        common_class.getPOSStockProduct(this, new OnLiveUpdateListener() {
            @Override
            public void onUpdate(String mode) {

                String OrdersTable = String.valueOf(db.getMasterData(Constants.ProductStock_List));
                userType = new TypeToken<ArrayList<Product_Details_Modal>>() {
                }.getType();

                Product_Modal = gson.fromJson(OrdersTable, userType);

                JSONArray ProdGroups = db.getMasterData(Constants.ProdGroups_List);
                LinearLayoutManager GrpgridlayManager = new LinearLayoutManager(POSStockLoadingActivity.this);
                GrpgridlayManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                Grpgrid.setLayoutManager(GrpgridlayManager);

                RyclListItemAdb grplistItems = new RyclListItemAdb(ProdGroups, POSStockLoadingActivity.this, new onListItemClick() {
                    @Override
                    public void onItemClick(JSONObject item) {

                        try {
                            FilterTypes(item.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Grpgrid.setAdapter(grplistItems);
                try {
                    FilterTypes(ProdGroups.getJSONObject(0).getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //selectedPos=0;
                //showOrderItemList(selectedPos, "");
                common_class.ProgressdialogShow(0, "");
            }
        });
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
            if (TypGroups.length() > 0)
                filterId = TypGroups.getJSONObject(0).getString("id");
            GetJsonData(String.valueOf(db.getMasterData(Constants.Category_List)), "1", filterId);

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

        //type =1 product category data values
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            Category_Modal.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String id = String.valueOf(jsonObject1.optInt("id"));
                    String name = jsonObject1.optString("name");
                    String typeId = String.valueOf(jsonObject1.optInt("TypID"));
                    String Division_Code = jsonObject1.optString("Division_Code");
                    String Cat_Image = jsonObject1.optString("Cat_Image");
                    String sampleQty = jsonObject1.optString("sampleQty");
                    String colorflag = jsonObject1.optString("colorflag");

                    if (filter.equalsIgnoreCase(typeId))
                        Category_Modal.add(new Category_Universe_Modal(id, name, Division_Code, Cat_Image, sampleQty, colorflag));

            }

            if (type.equals("1")) {

                selectedPos = 0;

                CategoryAdapter customAdapteravail = new CategoryAdapter(getApplicationContext(),
                        Category_Modal);
                categorygrid.setAdapter(customAdapteravail);

//                if (Constants.VAN_SALES_MODE.equalsIgnoreCase(Constants.VAN_STOCK_UNLOADING))
//                    showOrderList();
//                else
                showOrderItemList(selectedPos, "");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateToTALITEMUI() {
        TextView tvTotalItems = findViewById(R.id.tvTotalItems);
        TextView tvTotLabel = findViewById(R.id.tvTotLabel);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
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
        taxVal = 0;


        for (int pm = 0; pm < Product_Modal.size(); pm++) {

            if (Product_Modal.get(pm).getRegularQty() != null) {
                if (Product_Modal.get(pm).getQty() > 0 || Product_Modal.get(pm).getRegularQty() > 0) {


                    totalvalues += Product_Modal.get(pm).getAmount();

                    totalQty += Product_Modal.get(pm).getQty() + Product_Modal.get(pm).getRegularQty();

                    if (Product_Modal.get(pm).getTax() > 0)
                        taxVal += Product_Modal.get(pm).getTax();


                    Getorder_Array_List.add(Product_Modal.get(pm));


                }
            }
        }

        tvTotalAmount.setText(CurrencySymbol+" " + formatter.format(totalvalues));
        tvTotalItems.setText("Items : " + Getorder_Array_List.size() + "   Qty : " + totalQty);

        if (Getorder_Array_List.size() == 1)
            tvTotLabel.setText("Price (1 item)");
        else
            tvTotLabel.setText("Price (" + Getorder_Array_List.size() + " items)");

        tvBillSubTotal.setText(CurrencySymbol+" "+ formatter.format(totalvalues));
        tvBillTotItem.setText("" + Getorder_Array_List.size());
        tvBillTotQty.setText("" + totalQty);
        tvBillToPay.setText(CurrencySymbol+" " + formatter.format(totalvalues));
        ////tvCashDiscount.setText(CurrencySymbol+" " + formatter.format(cashDiscount));
        // tvTax.setText(CurrencySymbol+" " + formatter.format(taxVal));


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
        Category_Nametext.setText(listt.get(categoryPos).getName()+" ("+Product_ModalSetAdapter.size()+")");

        mProdct_Adapter = new Prodct_Adapter(Product_ModalSetAdapter, R.layout.vansales_product_order_recyclerview, getApplicationContext(), categoryPos);
        recyclerView.setAdapter(mProdct_Adapter);

    }

    void showOrderList() {
        Getorder_Array_List = new ArrayList<>();
        Getorder_Array_List.clear();

        for (int pm = 0; pm < Product_Modal.size(); pm++) {
            if (Product_Modal.get(pm).getQty() > 0 || Product_Modal.get(pm).getRegularQty() > 0) {
                Getorder_Array_List.add(Product_Modal.get(pm));
            }
        }

        if (Getorder_Array_List.size() == 0)
            Toast.makeText(getApplicationContext(), "Order is empty", Toast.LENGTH_SHORT).show();
        else
            FilterProduct(Getorder_Array_List);


    }
    private void SaveOrder(String axn) {
        for (int z = 0; z < Getorder_Array_List.size(); z++) {
            double totQty = (Getorder_Array_List.get(z).getQty() * Getorder_Array_List.get(z).getCnvQty());
            if(Getorder_Array_List.get(z).getBalance()<totQty) {
                Toast.makeText(getApplicationContext(), "Stock Not Available!", Toast.LENGTH_SHORT).show();
                ResetSubmitBtn(0);
                return;
            }
        }
        AlertDialogBox.showDialog(this, HAPApp.Title, "Are You Sure Want to Tranfert Stock for POS?", "OK", "Cancel", false, new AlertBox() {
            @Override
            public void PositiveMethod(DialogInterface dialog, int id) {
                common_class.ProgressdialogShow(1, "");
                JSONArray data = new JSONArray();
                JSONObject ActivityData = new JSONObject();

                try {
                    JSONObject HeadItem = new JSONObject();
                    HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
                    HeadItem.put("UKey", Ukey);
                    HeadItem.put("edate", Common_Class.GetDate());
                    HeadItem.put("AppVer", BuildConfig.VERSION_NAME);
                    if (strLoc.length > 0) {
                        HeadItem.put("Lat", strLoc[0]);
                        HeadItem.put("Long", strLoc[1]);
                    } else {
                        HeadItem.put("Lat", "");
                        HeadItem.put("Long", "");
                    }
                    ActivityData.put("POSStockHead", HeadItem);
                    JSONArray Order_Details = new JSONArray();
                    for (int z = 0; z < Getorder_Array_List.size(); z++) {
                        JSONObject ProdItem = new JSONObject();

                        double totQty = (Getorder_Array_List.get(z).getQty() * Getorder_Array_List.get(z).getCnvQty());

                        if(Getorder_Array_List.get(z).getBalance()<totQty) {
                            Toast.makeText(getApplicationContext(), "Stock Not Available!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ProdItem.put("product_code", Getorder_Array_List.get(z).getId());
                        ProdItem.put("ERPCode", Getorder_Array_List.get(z).getERP_Code());
                        ProdItem.put("product_Name", Getorder_Array_List.get(z).getName());
                        ProdItem.put("Batch", Getorder_Array_List.get(z).getBatchNo());
                        ProdItem.put("Stock_Qty", Getorder_Array_List.get(z).getQty());
                        ProdItem.put("EAQty", totQty);
                        ProdItem.put("MRP", Getorder_Array_List.get(z).getMRP());
                        ProdItem.put("Rate", String.format("%.2f", Getorder_Array_List.get(z).getRate()));
                        ProdItem.put("ConversionFactor", Getorder_Array_List.get(z).getCnvQty());
                        ProdItem.put("UOM_Id", Getorder_Array_List.get(z).getUOM_Id());
                        ProdItem.put("UOM_Nm", Getorder_Array_List.get(z).getUOM_Nm());

                        Order_Details.put(ProdItem);

                    }
                    ActivityData.put("POSStockDetail", Order_Details);
                    data.put(ActivityData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<JsonObject> responseBodyCall = apiInterface.savePOSStock(axn, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
                responseBodyCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            try {
                                common_class.ProgressdialogShow(0, "");
                                common_class.ProgressdialogShow(1, "Updating Matrial Details");
                                common_class.getPOSProduct(POSStockLoadingActivity.this, new OnLiveUpdateListener() {
                                    @Override
                                    public void onUpdate(String mode) {
                                        finish();
                                        common_class.ProgressdialogShow(0, "");
                                    }
                                });
                                Log.e("JSON_VALUES", response.body().toString());
                                JSONObject jsonObjects = new JSONObject(response.body().toString());
                                String san = jsonObjects.getString("success");

                                ResetSubmitBtn(1);
                                common_class.showMsg(POSStockLoadingActivity.this, jsonObjects.getString("Msg"));

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
    }

    void updateStockBal() {
        for (int pm = 0; pm < Getorder_Array_List.size(); pm++) {
            Getorder_Array_List.get(pm).setBalance((int) (Getorder_Array_List.get(pm).getQty() * Getorder_Array_List.get(pm).getCnvQty()));
        }
        sharedCommonPref.save(Constants.POS_STOCK_LOADING, gson.toJson(Getorder_Array_List));

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

    private void FilterProduct(List<Product_Details_Modal> orderList) {
        //findViewById(R.id.orderTypesLayout).setVisibility(View.GONE);
        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.rlSearchParent).setVisibility(View.GONE);
        //for testing
        //  findViewById(R.id.llBillHeader).setVisibility(View.VISIBLE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.VISIBLE);
        lin_gridcategory.setVisibility(View.GONE);
        lin_orderrecyclerview.setVisibility(View.VISIBLE);
        takeorder.setText("SUBMIT");

        mProdct_Adapter = new Prodct_Adapter(orderList, R.layout.vansales_product_pay_recyclerview, getApplicationContext(), -1);
        recyclerView.setAdapter(mProdct_Adapter);

    }

    public void sumofTax(List<Product_Details_Modal> Product_Details_Modalitem, int pos) {
        try {
            String taxRes = sharedCommonPref.getvalue(Constants.TAXList);
            if (!Common_Class.isNullOrEmpty(taxRes)) {
                JSONObject jsonObject = new JSONObject(taxRes);
                JSONArray jsonArray = jsonObject.getJSONArray("Data");

                double wholeTax = 0;
                List<Product_Details_Modal> taxList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if (jsonObject1.getString("Product_Detail_Code").equals(Product_Details_Modalitem.get(pos).getId())) {
                        if (jsonObject1.getDouble("Tax_Val") > 0) {
                            double taxCal = Product_Details_Modalitem.get(pos).getAmount() *
                                    ((jsonObject1.getDouble("Tax_Val") / 100));

                            wholeTax += taxCal;

                            taxList.add(new Product_Details_Modal(jsonObject1.getString("Tax_Id"),
                                    jsonObject1.getString("Tax_Type"), jsonObject1.getDouble("Tax_Val"), taxCal));


                        }
                    }
                }

                Product_Details_Modalitem.get(pos).setProductDetailsModal(taxList);
                Product_Details_Modalitem.get(pos).setAmount(Double.valueOf(formatter.format(Product_Details_Modalitem.get(pos).getAmount()
                        + wholeTax)));
                Product_Details_Modalitem.get(pos).setTax(Double.parseDouble(formatter.format(wholeTax)));
            }
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT") &&
                    !Constants.VAN_SALES_MODE.equalsIgnoreCase(Constants.VAN_STOCK_UNLOADING)) {
                moveProductScreen();
            } else {
                finish();
                //common_class.commonDialog(this, Invoice_History.class, "Van Sales?");
            }
            return true;
        }
        return false;
    }

    void moveProductScreen() {
        lin_gridcategory.setVisibility(View.VISIBLE);
//        findViewById(R.id.orderTypesLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.rlSearchParent).setVisibility(View.VISIBLE);
        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.llBillHeader).setVisibility(View.GONE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.GONE);
        findViewById(R.id.cdFreeQtyParent).setVisibility(View.GONE);
        takeorder.setText("PROCEED");
        showOrderItemList(selectedPos, "");
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
    public void onClick(View v) {
        switch (v.getId()) {
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
                                                SaveOrder(axn);
                                            }
                                        });
                                    } else {
                                        strLoc = sLoc.split(":");
                                        SaveOrder(axn);
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


        }
    }
    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        switch (type) {
            case 1:
//                int qty = (int) (Product_ModalSetAdapter.get(uomPos).getQty() * Double.parseDouble((myDataset.get(position).getPhone())));
//                if (Product_ModalSetAdapter.get(uomPos).getBalance() == null || Product_ModalSetAdapter.get(uomPos).getBalance() >= qty
                //    /*|| Product_ModalSetAdapter.get(uomPos).getCheckStock() == null || Product_ModalSetAdapter.get(uomPos).getCheckStock() == 0*/) {
                Product_ModalSetAdapter.get(uomPos).setCnvQty(Double.parseDouble((myDataset.get(position).getPhone())));
                Product_ModalSetAdapter.get(uomPos).setUOM_Id(myDataset.get(position).getId());
                Product_ModalSetAdapter.get(uomPos).setUOM_Nm(myDataset.get(position).getName());
                mProdct_Adapter.notify(Product_ModalSetAdapter, R.layout.vansales_product_order_recyclerview, getApplicationContext(), 1);
//                } else {
//                    common_class.showMsg(this, "Can't exceed Stock");
//                }



                break;
        }
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
        Context context;
        CategoryAdapter.MyViewHolder pholder;

        public CategoryAdapter(Context applicationContext, List<Category_Universe_Modal> list) {
            this.context = applicationContext;
            listt = list;
        }

        @Override
        public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.category_order_horizantal_universe_gridview, parent, false);
            return new CategoryAdapter.MyViewHolder(view);
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
        public void onBindViewHolder(CategoryAdapter.MyViewHolder holder, int position) {
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
        public Prodct_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new Prodct_Adapter.MyViewHolder(view);
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
        public void onBindViewHolder(Prodct_Adapter.MyViewHolder holder, int position) {
            try {
                Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(holder.getBindingAdapterPosition());

                holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());
                holder.Amount.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(Product_Details_Modal.getAmount()));
                holder.RegularQty.setText("" + Product_Details_Modal.getRegularQty());


                if (!Common_Class.isNullOrEmpty(Product_Details_Modal.getUOM_Nm()))
                    holder.tvUOM.setText(Product_Details_Modal.getUOM_Nm());
                else {
                    holder.tvUOM.setText(Product_Details_Modal.getDefault_UOM_Name());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Nm(Product_Details_Modal.getDefault_UOM_Name());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Id("" + Product_Details_Modal.getDefaultUOM());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setCnvQty(Product_Details_Modal.getDefaultUOMQty());


                }

                holder.Rate.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty()));

//                if (Product_Details_Modal.getRateEdit() == 1) {
//                    holder.Rate.setEnabled(true);
//                    holder.Rate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_small, 0);
//                } else {
//                    holder.Rate.setEnabled(false);
//                    holder.Rate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                }

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

                if (Constants.VAN_SALES_MODE.equalsIgnoreCase(Constants.VAN_STOCK_UNLOADING)) {
                    holder.QtyPls.setVisibility(View.INVISIBLE);
                    holder.QtyMns.setVisibility(View.INVISIBLE);
                    holder.Qty.setEnabled(false);
                }



                double totQty = (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty());

                holder.tvStock.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance());
                holder.tvTknStock.setText("" + (int)totQty);
                holder.tvTknStock.setTextColor(getResources().getColor(R.color.green));
                if(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance()<totQty){
                    holder.itemView.setBackgroundColor(getResources().getColor(R.color.color_red));
                    holder.tvTknStock.setTextColor(getResources().getColor(R.color.color_red));

                }
               // holder.tvBatchNo.setText("Batch : "+Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBatchNo());

//
//                if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() > 0)
//                    holder.tvStock.setTextColor(getResources().getColor(R.color.green));
//                else
//                    holder.tvStock.setTextColor(getResources().getColor(R.color.color_red));

                if (CategoryType >= 0) {

                    if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() == null)
                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setBalance(0);

                    holder.tvMRP.setText(CurrencySymbol+" " + Product_Details_Modal.getMRP());

                    holder.totalQty.setText("Total Qty : " + (int) ((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty()/* +
                            (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRegularQty())) * Product_Details_Modal.getCnvQty()*/)));


                    holder.regularAmt.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(Product_Details_Modal.getRegularQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate()));

                    holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modal.getRate() * Product_Details_Modal.getQty() * Product_Details_Modal.getCnvQty()));


                    holder.rlUOM.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uomPos = position;
                            uomList = new ArrayList<>();
                            String uomids="";
                            if (Product_Details_Modal.getUOMList() != null && Product_Details_Modal.getUOMList().size() > 0) {
                                for (int i = 0; i < Product_Details_Modal.getUOMList().size(); i++) {
                                    com.saneforce.milksales.SFA_Model_Class.Product_Details_Modal.UOM uom = Product_Details_Modal.getUOMList().get(i);

                                    if((";"+uomids).toLowerCase().indexOf(";"+uom.getUOM_Id().toLowerCase()+";")<0) {
                                        uomids += uom.getUOM_Id().toLowerCase() + ";";
                                        uomList.add(new Common_Model(uom.getUOM_Nm(), uom.getUOM_Id(), "", "", String.valueOf(uom.getCnvQty())));
                                    }
                                }
                                common_class.showCommonDialog(uomList, 1, POSStockLoadingActivity.this);
                            } else {
                                common_class.showMsg(POSStockLoadingActivity.this, "No Records Found.");
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


                holder.Disc.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modal.getDiscount()));


                holder.QtyPls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sVal = holder.Qty.getText().toString();
                        if (sVal.equalsIgnoreCase("")) sVal = "0";

//                        int order = (int) ((Integer.parseInt(sVal) + 1) * Product_Details_Modal.getCnvQty());
//                        int balance = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance();
//                        if ((balance >= order) /*|| Product_Details_Modal.getCheckStock() == null || Product_Details_Modal.getCheckStock() == 0*/) {
                        //  if (Product_Details_Modal.getCheckStock() != null && Product_Details_Modal.getCheckStock() == 1)
                        //holder.tvStock.setText("" + (int) (balance - order));
                        holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) + 1));

//                        } else {
//                            common_class.showMsg(VanSalesOrderActivity.this, "Can't exceed stock");
//                        }
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

//                                int order = (int) ((Integer.parseInt(sVal) - 1) * Product_Details_Modal.getCnvQty());
//                                int balance = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance();
                                //  if (Product_Details_Modal.getCheckStock() != null && Product_Details_Modal.getCheckStock() == 1)
                                //  holder.tvStock.setText("" + (int) (balance - order));
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


                            if(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance()<totQty) {
                                common_class.showMsg(POSStockLoadingActivity.this, "Can't exceed stock");
                               // return;
                            }
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setQty((int) enterQty);
                            holder.Amount.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(totQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate()));
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(Double.valueOf(formatter.format(totQty *
                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate())));
                            if (CategoryType >= 0) {
                                holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(enterQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty()));
                                holder.totalQty.setText("Total Qty : " + (int) /*totQty*/enterQty);
                            }

                            holder.tvTknStock.setText("" + (int)totQty);
                            holder.tvTknStock.setTextColor(getResources().getColor(R.color.green));
                            if(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance()<totQty){

                                holder.itemView.setBackgroundColor(getResources().getColor(R.color.color_red));
                                holder.tvTknStock.setTextColor(getResources().getColor(R.color.color_red));

                            }

                            sumofTax(Product_Details_Modalitem, holder.getBindingAdapterPosition());
                            holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
                            holder.tvTaxLabel.setText(CurrencySymbol+" "+ formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTax()));

                            updateToTALITEMUI();



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

                    if (Constants.VAN_SALES_MODE.equalsIgnoreCase(Constants.VAN_STOCK_UNLOADING))
                        holder.ivDel.setVisibility(View.INVISIBLE);

                    holder.ivDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialogBox.showDialog(POSStockLoadingActivity.this, HAPApp.Title,
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
                holder.Rate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(Product_Details_Modal);
                    }
                });

                //updateToTALITEMUI();
            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return Product_Details_Modalitem.size();
        }

        private void showDialog(Product_Details_Modal product_details_modal) {
            try {


                LayoutInflater inflater = LayoutInflater.from(POSStockLoadingActivity.this);

                final View view = inflater.inflate(R.layout.edittext_price_dialog, null);
                AlertDialog alertDialog = new AlertDialog.Builder(POSStockLoadingActivity.this).create();
                alertDialog.setCancelable(false);

                final EditText etComments = (EditText) view.findViewById(R.id.et_addItem);
                Button btnSave = (Button) view.findViewById(R.id.btn_save);
                Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Common_Class.isNullOrEmpty(etComments.getText().toString())) {
                            common_class.showMsg(POSStockLoadingActivity.this, "Empty value is not allowed");
                        } else if (Double.valueOf(etComments.getText().toString()) > Double.valueOf(product_details_modal.getRate())) {
                            common_class.showMsg(POSStockLoadingActivity.this, "Enter Rate is greater than "+MRPCap);

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
            public TextView productname, Rate, Amount, Disc, Free, RegularQty, lblRQty, productQty, regularAmt, tvUOM,
                    QtyAmt, totalQty, tvTaxLabel, tvMRP, tvStock,tvBatchNo,tvTknStock;
            ImageView ImgVwProd, QtyPls, QtyMns, ivDel;
            EditText Qty;

            LinearLayout llRegular, rlUOM;


            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);
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
                ImgVwProd = view.findViewById(R.id.ivAddShoppingCart);
                tvStock = view.findViewById(R.id.tvStockBal);
                tvTknStock = view.findViewById(R.id.tvTknStock);
                tvBatchNo= view.findViewById(R.id.tvBatchNo);

                if (CategoryType >= 0) {
                    tvMRP = view.findViewById(R.id.MrpRate);
                    lblRQty = view.findViewById(R.id.status);
                    regularAmt = view.findViewById(R.id.RegularAmt);
                    QtyAmt = view.findViewById(R.id.qtyAmt);
                    totalQty = view.findViewById(R.id.totalqty);
                    rlUOM = view.findViewById(R.id.rlUOM);


                } else {
                    ivDel = view.findViewById(R.id.ivDel);
                }


            }
        }


    }

}