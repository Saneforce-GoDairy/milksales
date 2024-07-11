package com.saneforce.godairy.Activity_Hap;

import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;
import static com.saneforce.godairy.SFA_Activity.HAPApp.MRPCap;
import static com.saneforce.godairy.SFA_Activity.HAPApp.StockCheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saneforce.godairy.BuildConfig;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.CameraPermission;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.LocationEvents;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Interface.OnImagePickListener;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.Interface.onListItemClick;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.SFA_Activity.Invoice_History;
import com.saneforce.godairy.SFA_Adapter.RyclBrandListItemAdb;
import com.saneforce.godairy.SFA_Adapter.RyclListItemAdb;
import com.saneforce.godairy.SFA_Model_Class.Category_Universe_Modal;
import com.saneforce.godairy.SFA_Model_Class.Product_Details_Modal;
import com.saneforce.godairy.common.DatabaseHandler;
import com.saneforce.godairy.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Invoice_Vansales_Select extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI, Master_Interface {

    NumberFormat formatter = new DecimalFormat("##0.00");
    List<Category_Universe_Modal> Category_Modal = new ArrayList<>();
    List<Product_Details_Modal> Product_Modal;
    List<Product_Details_Modal> Product_ModalSetAdapter;
    List<Product_Details_Modal> Getorder_Array_List;
    List<Category_Universe_Modal> listt;
    Type userType;
    Gson gson;
    TextView Out_Let_Name, Category_Nametext;

    CircularProgressButton takeorder, btnRepeat;

    private RecyclerView recyclerView, categorygrid, freeRecyclerview, Grpgrid, Brndgrid;
    LinearLayout lin_gridcategory;
    Common_Class common_class;

    String Ukey;
    String[] strLoc;
    String Worktype_code = "", Route_Code = "", Dirtributor_Cod = "", Distributor_Name = "", mDCRMode;
    Shared_Common_Pref sharedCommonPref;
    EditText cashdiscount;
    Prodct_Adapter mProdct_Adapter;
    List<Product_Details_Modal> freeQty_Array_List;

    String TAG = "Invoice_Vansales_Select";
    DatabaseHandler db;
    public int selectedPos = 0;

    RelativeLayout rlCategoryItemSearch;
    ImageView ivClose;
    EditText etCategoryItemSearch, etRecAmt;
    private TextView tvTotalAmount;
    private double totalvalues;
    double cashDiscount;

    private Integer totalQty;

    private TextView tvBillTotItem, tvPayMode, tvDate, tvOutStanding, tvTotOutstanding, tvInvAmt, tvPayAmt,txPONo;
    private double taxVal;

    RelativeLayout rlPayment;

    CheckBox cbCredit, cbCash;
    LinearLayout llPayMode;

    private List<Common_Model> payList = new ArrayList<>();

    String orderId = "",OrderTypId="",OrderTypNm="";;
    private LinearLayout rlAddProduct, rlCredit, rlCash;
    private double outstandAmt;
    private double payAmt;

    final Handler handler = new Handler();
    List<Product_Details_Modal> orderTotTax;
    private ArrayList<Common_Model> uomList;
    private int uomPos;
    SharedPreferences UserDetails;
    String UserInfo = "MyPrefs";
    LinearLayout ll_startkm;
    TextView tvVehNo_label,tvstartkm_label;
    EditText edtVehicleNo,edtStartKm;
    LinearLayout ll_actual_total;
    TextView tv_no_match;
    String vehNo="";
    EditText etDiscPer,etDiscAmt;
    private double rDiscAmt,rDiscPer, InvAmt;
    TextView tvSaveAmt;
    TextView tvTotalDiscLabel;
    double totalMRP=0;
    ImageView attachedImage,img_lodg_atta;
    String imageSet="",imageServer = "",imageConvert = "";

    JSONArray CatFreeDetdata, FreeDetails,freeQtyNew;
    private double subtotalvalue;

    List<Product_Details_Modal> Product_FilterList=new ArrayList<>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_invoice_vansales_select);
            gson = new Gson();
            db = new DatabaseHandler(this);
            sharedCommonPref = new Shared_Common_Pref(Invoice_Vansales_Select.this);
            common_class = new Common_Class(this);
            UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
            initVariable();
            tvVehNo_label.setVisibility(View.GONE);
            tvstartkm_label.setVisibility(View.GONE);
            edtStartKm.setVisibility(View.GONE);
            edtVehicleNo.setVisibility(View.GONE);
            ll_startkm.setVisibility(View.GONE);
            btnRepeat.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            categorygrid.setLayoutManager(layoutManager);

            mDCRMode = sharedCommonPref.getvalue(Shared_Common_Pref.DCRMode);
            common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);

            Out_Let_Name.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
            Product_ModalSetAdapter = new ArrayList<>();

            takeorder.setOnClickListener(this);
            rlCategoryItemSearch.setOnClickListener(this);
            ivClose.setOnClickListener(this);
            rlPayment.setOnClickListener(this);
            rlCash.setOnClickListener(this);
            rlCredit.setOnClickListener(this);
            rlAddProduct.setOnClickListener(this);
            Category_Nametext.setOnClickListener(this);
            btnRepeat.setOnClickListener(this);
            Ukey = Common_Class.GetEkey();
            ll_actual_total=findViewById(R.id.ll_actual_total);
            tv_no_match=findViewById(R.id.tv_no_match);
            Out_Let_Name.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            StockCheck="1";

            String OrdersTable = String.valueOf(db.getMasterData(Constants.Product_List));
            userType = new TypeToken<ArrayList<Product_Details_Modal>>() {
            }.getType();
            Product_Modal = gson.fromJson(OrdersTable, userType);
            //  setProductSchemeAndTax(0);
            vehNo=sharedCommonPref.getvalue(Constants.Vansales_VehNo);
            Log.v("vehNo_ghj",vehNo);
            edtVehicleNo.setText(vehNo);
            etDiscPer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    etDiscAmt.setText("0");
                    rDiscPer =0;
                    if (!Common_Class.isNullOrEmpty(s.toString())) {
                        rDiscPer = Double.parseDouble(s.toString());
                    }

                    InvAmt= Double.parseDouble( tvInvAmt.getText().toString().replace(CurrencySymbol+" " , ""));
                    rDiscAmt=InvAmt*(rDiscPer/100);
                    totalvalues=InvAmt - rDiscAmt;
                    if (rDiscAmt == 0) {
                        etDiscAmt.setText("");
                    } else {
                        etDiscAmt.setText(new DecimalFormat("##0.00").format(rDiscAmt));
                    }

                    tvPayAmt.setText(CurrencySymbol+" " + formatter.format(totalvalues ));
                    tvTotOutstanding.setText(CurrencySymbol+" "+ formatter.format(outstandAmt + (totalvalues - payAmt)));
                    tvTotalDiscLabel.setText("(Total Discount "+CurrencySymbol+" " + formatter.format(cashDiscount+rDiscAmt)+")");
                    //tvSaveAmt.setText("Your Saving Amount is MRP "+formatter.format(totalMRP)+" - NetAmount "+formatter.format(totalvalues)+" = "+CurrencySymbol+" "  + formatter.format(totalMRP-totalvalues));
                    tvSaveAmt.setText("Total Profit "+CurrencySymbol+" "  + formatter.format(totalMRP-totalvalues));


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etDiscPer.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, 100)});
            etDiscAmt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus=true){
                        InvAmt= Double.parseDouble( tvInvAmt.getText().toString().replace(CurrencySymbol+" " , ""));
                        if(!etDiscPer.getText().toString().equalsIgnoreCase("")){
                            etDiscPer.setText("");
                            etDiscAmt.setText("");
                            rDiscPer=0;
                            rDiscAmt=InvAmt*(rDiscPer/100);
                            totalvalues=InvAmt - rDiscAmt;
                            tvPayAmt.setText(CurrencySymbol+" " + formatter.format(totalvalues ));
                            tvTotOutstanding.setText(CurrencySymbol+" "+ formatter.format(outstandAmt + (totalvalues - payAmt)));
                            tvTotalDiscLabel.setText("(Total Discount "+CurrencySymbol+" " + formatter.format(cashDiscount+rDiscAmt)+")");
                            //tvSaveAmt.setText("Your Saving Amount is MRP "+formatter.format(totalMRP)+" - NetAmount "+formatter.format(totalvalues)+" = "+CurrencySymbol+" "  + formatter.format(totalMRP-totalvalues));
                            tvSaveAmt.setText("Total Profit "+CurrencySymbol+" "  + formatter.format(totalMRP-totalvalues));
                        }
                        etDiscAmt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                        InputFilter limitFilter = new MinMaxInputFilter(0.0, InvAmt);
                        etDiscAmt.setFilters(new InputFilter[] { limitFilter });
                    }
                }
            });
            etDiscAmt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    rDiscAmt=0;
                    if (!Common_Class.isNullOrEmpty(s.toString())) {
                        rDiscAmt = Double.parseDouble(s.toString());
                    }
                    InvAmt= Double.parseDouble( tvInvAmt.getText().toString().replace(CurrencySymbol+" " , ""));
                    totalvalues=InvAmt - rDiscAmt;

                    tvPayAmt.setText(CurrencySymbol+" " + formatter.format(totalvalues ));
                    tvTotOutstanding.setText(CurrencySymbol+" "+ formatter.format(outstandAmt + (totalvalues - payAmt)));
                    tvTotalDiscLabel.setText("(Total Discount "+CurrencySymbol+" " + formatter.format(cashDiscount+rDiscAmt)+")");
                    //tvSaveAmt.setText("Your Saving Amount is MRP "+formatter.format(totalMRP)+" - NetAmount "+formatter.format(totalvalues)+" = "+CurrencySymbol+" "  + formatter.format(totalMRP-totalvalues));
                    tvSaveAmt.setText("Total Profit "+CurrencySymbol+" "  + formatter.format(totalMRP-totalvalues));

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);


            // showOrderItemList(0, "");


            etCategoryItemSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.e(TAG+"search","text");
                    showOrderItemList(selectedPos, s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            tvDate.setText("" + Common_Class.GetDatewothouttime());


            cbCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cbCredit.setChecked(false);
                        //  llPayMode.setVisibility(View.VISIBLE);


                    }
                }
            });


            cbCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cbCash.setChecked(false);
                        //llPayMode.setVisibility(View.GONE);
                    }
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

                        tvTotOutstanding.setText(CurrencySymbol+" " + formatter.format(outstandAmt + (totalvalues - payAmt)));

                    } catch (Exception e) {

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            attachedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                    intent.putExtra("ImageUrl", imageSet);
                    startActivity(intent);
                }
            });
            img_lodg_atta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CameraPermission cameraPermission = new CameraPermission(Invoice_Vansales_Select.this, getApplicationContext());

                    if (!cameraPermission.checkPermission()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            cameraPermission.requestPermission();
                        }
                    } else {

                        AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                            @Override
                            public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
//                            Photo_Name = FileName;
//                            imageConvert=fullPath;
//                            EndedImage="file://"+fullPath;
//                            EndedKmImage.setImageBitmap(image);

                                UploadPhoto(fullPath, UserDetails.getString("Sfcode", ""), FileName, "Travel", image);

                            }
                        });
                        Intent intent = new Intent(Invoice_Vansales_Select.this, AllowancCapture.class);
                        intent.putExtra("allowance", "Two");
                        startActivity(intent);

                    }

                }
            });



           /* txPONo.setEnabled(true);
            if (Shared_Common_Pref.Invoicetoorder.equals("4")) {
                orderId = Shared_Common_Pref.TransSlNo;
                Shared_Common_Pref.Invoicetoorder = "2";
                txPONo.setText(orderId);
                txPONo.setEnabled(false);
                String preOrderList = sharedCommonPref.getvalue(Constants.INVOICE_ORDERLIST);
                JSONArray jsonArray1 = new JSONArray(preOrderList);

                new Thread(() -> {
                    if (jsonArray1 != null && jsonArray1.length() > 0) {
                        for (int pm = 0; pm < Product_Modal.size(); pm++) {
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject jsonObject1 = null;
                                try {
                                    jsonObject1 = jsonArray1.getJSONObject(i);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    if (Product_Modal.get(pm).getId().equals(jsonObject1.getString("Product_Code"))) {
                                        //Product_Modal.get(pm).setRegularQty
                                        //        (jsonObject1.getInt("Quantity"));
                                        Product_Modal.get(pm).setQty(
                                                jsonObject1.getInt("Quantity"));
                                        Product_Modal.get(pm).setUOM_Nm(jsonObject1.getString("UOM"));
                                        Product_Modal.get(pm).setUOM_Id("" + jsonObject1.getString("umo_unit"));
                                        Product_Modal.get(pm).setCnvQty(jsonObject1.getDouble("Conf_Fac"));
                                        Product_Modal.get(pm).setDiscount(jsonObject1.getDouble("discount")*jsonObject1.getInt("Quantity")*jsonObject1.getDouble("Conf_Fac"));
                                        Product_Modal.get(pm).setFree(String.valueOf(jsonObject1.getInt("discount_price")));
                                        Product_Modal.get(pm).setOff_Pro_code(String.valueOf(jsonObject1.getString("Offer_ProductCd")));
                                        Product_Modal.get(pm).setOff_Pro_name(String.valueOf(jsonObject1.getString("Offer_ProductNm")));
                                        Product_Modal.get(pm).setRate((jsonObject1.getDouble("Rate")));
                                        double enterQty=jsonObject1.getInt("Quantity");
                                        double totQty = (enterQty + Product_Modal.get(pm).getRegularQty()) * (Product_Modal.get(pm).getCnvQty());
                                        if(Product_Modal.get(pm).getCnvQty()>1) {
                                            totQty = ((enterQty + Product_Modal.get(pm).getRegularQty()) * Product_Modal.get(pm).getCnvQty()) - Double.parseDouble(Product_Modal.get(pm).getFree());
                                        }
                                        int psc=(int)totQty;
                                        Product_Modal.get(pm).setOrderQty(psc);
                                        //double dMRPAmt =Double.valueOf(formatter.format((Product_Modal.get(pm).getCnvQty() * Product_Modal.get(pm).getQty()) *
                                        // Double.parseDouble(Product_Modal.get(pm).getMRP().toString())));
                                        //double dMrgn=dMRPAmt * (Product_Modal.get(pm).getMargin()/100);
                                        //double sellAmt=dMRPAmt-dMrgn;
                                        //double sellAmt= jsonObject1.getDouble("value"); //Double.valueOf(formatter.format((Product_Modal.get(pm).getCnvQty() * Product_Modal.get(pm).getQty()) * Double.parseDouble(Product_Modal.get(pm).getPTR())));


                                        //  double sellAmt= Double.valueOf(formatter.format((psc * Double.parseDouble(Product_Modal.get(pm).getPTR()))-Product_Modal.get(pm).getDiscount()));
                                        double sellAmt= Double.valueOf(formatter.format((psc * Product_Modal.get(pm).getRate())));
                                        Product_Modal.get(pm).setAmount(sellAmt);
                                        //Product_Modal.get(pm).setAmount(jsonObject1.getDouble("value"));

                                JSONArray taxArr = jsonObject1.getJSONArray("TAX_details");
                                List<Product_Details_Modal> taxList = new ArrayList<>();
                                double wholeTax = 0;
                                for (int tax = 0; tax < taxArr.length(); tax++) {
                                    JSONObject taxObj = taxArr.getJSONObject(tax);
                                    taxList.add(new Product_Details_Modal(taxObj.getString("Tax_Code"), taxObj.getString("Tax_Name"), taxObj.getDouble("Tax_Val"),
                                            taxObj.getDouble("Tax_Amt")));
                                    wholeTax += taxObj.getDouble("Tax_Amt");

                                }
                                        //  sumofTax(Product_Modal,pm);
                                        sumofTaxNew( Product_Modal.get(pm));
                                        //Product_Modal.get(pm).setProductDetailsModal(taxList);
                                        //Product_Modal.get(pm).setTax(Double.parseDouble(formatter.format(wholeTax)));
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }).start();

            } else if (!Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.LOC_INVOICE_DATA))) {
                Product_Modal = gson.fromJson(sharedCommonPref.getvalue(Constants.LOC_INVOICE_DATA), userType);

            }*/


            //   common_class.getDb_310Data(Constants.VAN_OUTSTANDING, this);

            GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "6", "");



//            }).start();



            //common_class.getDb_310Data(Constants.STOCK_DATA, this);
            common_class.getDb_310Data(Constants.VAN_STOCK, this);
        } catch (Exception e) {

            Log.e(TAG, " invoice oncreate: " + e.getMessage());

        }
    }

    private void initVariable() {
        categorygrid = findViewById(R.id.category);
        Grpgrid = findViewById(R.id.PGroup);
        Brndgrid = findViewById(R.id.PBrnd);
        takeorder = findViewById(R.id.takeorder);
        btnRepeat = findViewById(R.id.btnRepeat);
        cashdiscount = findViewById(R.id.cashdiscount);
        lin_gridcategory = findViewById(R.id.lin_gridcategory);
        Out_Let_Name = findViewById(R.id.outlet_name);
        Category_Nametext = findViewById(R.id.Category_Nametext);
        rlCategoryItemSearch = findViewById(R.id.rlCategoryItemSearch);
        ivClose = findViewById(R.id.ivClose);
        etCategoryItemSearch = findViewById(R.id.searchView);
        rlPayment = findViewById(R.id.rlPayMode);
        rlCredit = findViewById(R.id.rlPayTypeCredit);
        rlCash = findViewById(R.id.rlPayTypeCash);
        tvDate = findViewById(R.id.tvDate);
        tvPayMode = findViewById(R.id.tvPayMode);
        etRecAmt = findViewById(R.id.etRecAmt);
        cbCash = findViewById(R.id.cbCash);
        cbCredit = findViewById(R.id.cbCredit);
        llPayMode = findViewById(R.id.llPayMode);
        rlAddProduct = findViewById(R.id.rlAddProduct);
        tvOutStanding = findViewById(R.id.tvOutstanding);
        tvTotOutstanding = findViewById(R.id.tvTotOutstanding);
        tvInvAmt = findViewById(R.id.tvInvAMt);
        tvPayAmt = findViewById(R.id.tvPayAmt);
        txPONo = findViewById(R.id.txPONum);
        recyclerView = findViewById(R.id.orderrecyclerview);
        freeRecyclerview = findViewById(R.id.freeRecyclerview);
        etDiscPer=findViewById(R.id.etdiscountPer);
        etDiscAmt=findViewById(R.id.etdiscountAmt);

        attachedImage = findViewById(R.id.attachedImage);
        edtVehicleNo = findViewById(R.id.edtVehNo);
        edtStartKm = findViewById(R.id.edtStartKm);
        img_lodg_atta = findViewById(R.id.startkm_attach);
        tvVehNo_label=findViewById(R.id.tvVehNo_label);
        tvstartkm_label=findViewById(R.id.tvstartkm_label);
        ll_startkm=findViewById(R.id.ll_startkm);
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

                CategoryAdapter customAdapteravail = new CategoryAdapter(getApplicationContext(),
                        Category_Modal);
                categorygrid.setAdapter(customAdapteravail);

                showOrderItemList(selectedPos, "");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private int getCatePos(Integer CId) throws JSONException {
        int po=-1;
        for(int il=0;il<CatFreeDetdata.length();il++){
            if( CatFreeDetdata.getJSONObject(il).getInt("CatId")==CId){
                po=il;
            }
        }
        return po;
    }
    private int getFProdPos(String fPcode) {
        int po=-1;
        for(int il=0;il<freeQtyNew.length();il++){
            try {
                if( freeQtyNew.getJSONObject(il).getString("FPCode").equalsIgnoreCase(fPcode)){
                    po=il;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return po;
    }
    void showOrderList() {

        Getorder_Array_List = new ArrayList<>();
        Getorder_Array_List.clear();
        CatFreeDetdata=new JSONArray();
        for (int pm = 0; pm < Product_Modal.size(); pm++) {
            if (Product_Modal.get(pm).getQty() > 0) {
                Product_Details_Modal itm=Product_Modal.get(pm);
                Getorder_Array_List.add(itm);
                try {
                    int ipo=getCatePos(itm.getpCatCode());
                    /*if(ipo>-1){
                        JSONObject oitm=CatFreeDetdata.getJSONObject(ipo);
                        CatFreeDetdata.getJSONObject(ipo).put("Qty",oitm.getInt("Qty")+itm.getOrderQty());
                        CatFreeDetdata.getJSONObject(ipo).put("Value",oitm.getDouble("Value")+itm.getAmount());
                    }else{
                        JSONObject nItm=new JSONObject();
                        nItm.put("CatId",itm.getpCatCode());
                        nItm.put("Qty",itm.getOrderQty());
                        nItm.put("Value",itm.getAmount());
                        CatFreeDetdata.put(nItm);
                    }*/
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (Getorder_Array_List.size() == 0)
            Toast.makeText(getApplicationContext(), "Invoice is empty", Toast.LENGTH_SHORT).show();
        else
            FilterProduct();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          /*  case R.id.btnRepeat:
                if (btnRepeat.isAnimating()) return;
                btnRepeat.startAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        common_class.getDataFromApi(Constants.PreInvOrderQty, Invoice_Vansales_Select.this, false);
                    }
                }, 500);
                break;*/
            case R.id.rlAddProduct:
                moveProductScreen();
                break;

            case R.id.rlPayTypeCash:
                cbCash.setChecked(false);
                break;
            case R.id.rlPayTypeCredit:
                cbCredit.setChecked(false);
                break;

            case R.id.rlPayMode:
                common_class.getDb_310Data(Constants.PAYMODES, this);
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
                    int count=0;
                    if(StockCheck.equalsIgnoreCase("1")) {
                        for (int z = 0; z < Product_Modal.size(); z++) {
                            double enterQty = Product_Modal.get(z).getQty();
                            double totQty = (enterQty * Product_Modal.get(z).getCnvQty());
                            if ((Product_Modal.get(z).getBalance() - (int) totQty) < 0) {
                                count+=1;
                            }
                        }
                    }
                    if(count==0){
                        showOrderList();
                    }else{
                        Toast.makeText(this, "Low Stock", Toast.LENGTH_SHORT).show();
                    }

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
                btnRepeat.stopAnimation();
                btnRepeat.revertAnimation();
            }
        }, dely);

    }

    private void SaveOrder() {
        if (common_class.isNetworkAvailable(this)) {

            if(StockCheck.equalsIgnoreCase("1")) {
                for (int z = 0; z < Getorder_Array_List.size(); z++) {
                    double enterQty = Getorder_Array_List.get(z).getQty();
                    double totQty = (enterQty * Getorder_Array_List.get(z).getCnvQty());
                    if ((Getorder_Array_List.get(z).getBalance() - (int) totQty) < 0) {
                        Toast.makeText(this, "Low Stock", Toast.LENGTH_SHORT).show();
                        ResetSubmitBtn(0);
                        return;
                    }
                }
            }
            AlertDialogBox.showDialog(Invoice_Vansales_Select.this, "HAP SFA", "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
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
                        HeadItem.put("Vansales_VehNo",edtVehicleNo.getText().toString());
                        HeadItem.put("Vansales_Km",edtStartKm.getText().toString());
                        HeadItem.put("Vansales_Km_Image",imageSet);
                        //sharedCommonPref.save(Constants.Vansales_VehNo,edtVehicleNo.getText().toString());
                        ActivityData.put("Activity_Report_Head", HeadItem);

                        JSONObject OutletItem = new JSONObject();
                        OutletItem.put("Doc_Meet_Time", Common_Class.GetDate());
                        OutletItem.put("modified_time", Common_Class.GetDate());
                        OutletItem.put("stockist_code", sharedCommonPref.getvalue(Constants.Distributor_Id));
                        OutletItem.put("stockist_name", sharedCommonPref.getvalue(Constants.Distributor_name));
                        OutletItem.put("orderValue", formatter.format(totalvalues));

                        orderId= txPONo.getText().toString();
                        OutletItem.put("PONo", txPONo.getText().toString());
                        OutletItem.put("InvValue", formatter.format(InvAmt));
                        OutletItem.put("DiscPer", etDiscPer.getText().toString());
                        OutletItem.put("DiscAmt", rDiscAmt);
                        OutletItem.put("prodDisAmt",cashDiscount);
                        cashDiscount=cashDiscount+rDiscAmt;
                        OutletItem.put("CashDiscount", cashDiscount);

                        OutletItem.put("NetAmount", formatter.format(totalvalues));

                        OutletItem.put("No_Of_items", tvBillTotItem.getText().toString());
                        OutletItem.put("Invoice_Flag", Shared_Common_Pref.Invoicetoorder);
                        OutletItem.put("TransSlNo", Shared_Common_Pref.TransSlNo);
                        OutletItem.put("doctor_code", Shared_Common_Pref.OutletCode);
                        OutletItem.put("doctor_name", Shared_Common_Pref.OutletName);
                        OutletItem.put("ordertype", "invoice");
                        OutletItem.put("ordertypeid", OrderTypId);
                        OutletItem.put("ordertypenm", OrderTypNm);
                        OutletItem.put("category_type", Shared_Common_Pref.SecOrdOutletType);

                        // OutletItem.put("outstandAmt", outstandAmt);

                        OutletItem.put("PAYAmount", etRecAmt.getText().toString());

                        if (cbCredit.isChecked())
                            OutletItem.put("payType", "Credit");
                        else
                            OutletItem.put("payType", "Cash");

                        // OutletItem.put("payType", tvPayMode.getText().toString());
                        OutletItem.put("orderId", orderId);


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
                            ProdItem.put("BatchNo", Getorder_Array_List.get(z).getBatchNo());

                            if(Getorder_Array_List.get(z).getQty()<1 || Getorder_Array_List.get(z).getOrderQty()<1){
                                Toast.makeText(Invoice_Vansales_Select.this,"Zero Qty Found. Kindly Check Or Contact Admin",Toast.LENGTH_LONG).show();
                                return;
                            }
                            ProdItem.put("Product_Qty", Getorder_Array_List.get(z).getQty());
                            ProdItem.put("Product_RegularQty", Getorder_Array_List.get(z).getRegularQty());
                            double cf = (Getorder_Array_List.get(z).getCnvQty());
                            // ProdItem.put("Product_Total_Qty", cf > 0 ? (Getorder_Array_List.get(z).getQty()) *
                            //         cf : Getorder_Array_List.get(z).getQty());
                            ProdItem.put("Product_Cnv_Qty", cf > 0 ? (Getorder_Array_List.get(z).getQty()) *
                                    cf : Getorder_Array_List.get(z).getQty());
                            ProdItem.put("Product_Total_Qty", Getorder_Array_List.get(z).getOrderQty());
                            ProdItem.put("Product_Amount", Getorder_Array_List.get(z).getAmount());
                            ProdItem.put("MRP", String.valueOf(Getorder_Array_List.get(z).getMRP()));
                            ProdItem.put("Margin", String.format("%.2f", Getorder_Array_List.get(z).getMargin()));
                            ProdItem.put("MarginTyp", String.format("%.2f", Getorder_Array_List.get(z).getMarginTyp()));
                            ProdItem.put("RateTyp", String.format("%.2f", Getorder_Array_List.get(z).getRateTyp()));
                            ProdItem.put("RTEd", Getorder_Array_List.get(z).getRateEdited());
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
                        ActivityData.put("FreeDetail", FreeDetails);
                        data.put(ActivityData);

                        Log.e("vaninvoice","data:"+data.toString());
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<JsonObject> responseBodyCall = apiInterface.saveVanInvoice(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code,
                                sharedCommonPref.getvalue(Constants.LOGIN_TYPE), data.toString());
                        responseBodyCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                sharedCommonPref.clear_pref(Constants.LOC_INVOICE_DATA);
                                if (response.isSuccessful()) {
                                    try {
                                        common_class.ProgressdialogShow(0, "");
                                        Log.e("JSON_VALUES", response.body().toString());
                                        JSONObject jsonObjects = new JSONObject(response.body().toString());
                                        ResetSubmitBtn(1);
                                        if (jsonObjects.getString("success").equals("true")) {
                                            common_class.CommonIntentwithFinish(Invoice_History.class);
                                        }
                                        common_class.showMsg(Invoice_Vansales_Select.this, jsonObjects.getString("Msg"));

                                    } catch (Exception e) {
                                        common_class.ProgressdialogShow(0, "");
                                        Log.e(TAG, "invcatch: " + e.getMessage());
                                        ResetSubmitBtn(2);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                call.cancel();
                                Log.e("SUBMIT_VALUE", "ERROR");
                                ResetSubmitBtn(2);
                            }
                        });

                    } catch (JSONException e) {
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

    private void FilterProduct() {
        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.rlSearchParent).setVisibility(View.GONE);
        findViewById(R.id.llBillHeader).setVisibility(View.VISIBLE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.VISIBLE);
        rlAddProduct.setVisibility(View.VISIBLE);
        btnRepeat.setVisibility(View.GONE);
        lin_gridcategory.setVisibility(View.GONE);
        takeorder.setText("SUBMIT");

        mProdct_Adapter = new Prodct_Adapter(Getorder_Array_List, R.layout.invoice_pay_recyclerview_edit, getApplicationContext(), -1);
        recyclerView.setAdapter(mProdct_Adapter);
        showFreeQtyList();
        updateToTALITEMUI(1);
    }

    void showFreeQtyList() {
        freeQty_Array_List = new ArrayList<>();
        freeQty_Array_List.clear();

        freeQtyNew=new JSONArray();
        for (Product_Details_Modal pm : Product_Modal) {

            if (pm.getRegularQty() != null) {
                if (!Common_Class.isNullOrEmpty(pm.getFree()) && !pm.getFree().equals("0")) {
                    int ik=getFProdPos(pm.getOff_Pro_code());
                    try {
                        if(ik>-1){
                            JSONObject itm= null;
                            itm = freeQtyNew.getJSONObject(ik);

                            int f=itm.getInt("FPQty");
                            f+=Integer.parseInt( pm.getFree());
                            freeQtyNew.getJSONObject(ik).put("FPQty",f);
                        }else {
                            JSONObject itm=new JSONObject();

                            itm.put("FPCode",pm.getOff_Pro_code());
                            itm.put("FPName",pm.getOff_Pro_name());
                            itm.put("FPQty",pm.getFree());
                            freeQtyNew.put(itm);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    freeQty_Array_List.add(pm);

                }
            }
        }

        String strSchemeList = sharedCommonPref.getvalue(Constants.FreeSchemeDiscList);

        Type type = new TypeToken<ArrayList<Product_Details_Modal>>() {
        }.getType();
        List<Product_Details_Modal> catScheme = gson.fromJson(strSchemeList, type);

        for(int il=0;il<CatFreeDetdata.length();il++){
            JSONObject itm= null;
            try {
                itm = CatFreeDetdata.getJSONObject(il);
                FreeDetails=new JSONArray();
                if(catScheme!= null && catScheme.size()>0){
                    for(int ij=0;ij<catScheme.size();ij++) {
                        double schemeVal = Double.parseDouble(catScheme.get(ij).getScheme());
                        if (String.valueOf(itm.getInt("CatId")).equalsIgnoreCase(catScheme.get(ij).getId()) &&
                                itm.getDouble("Value") >= schemeVal
                        ) {
                            Product_Details_Modal nItm= new Product_Details_Modal(catScheme.get(ij).getOff_Pro_code(),catScheme.get(ij).getOff_Pro_name());
                            nItm.setFree(catScheme.get(ij).getFree());
                            freeQty_Array_List.add(nItm);
                            int ik=getFProdPos(nItm.getOff_Pro_code());
                            if(ik>-1){
                                JSONObject fitm=freeQtyNew.getJSONObject(ik);
                                int f=fitm.getInt("FPQty");
                                f+=Integer.parseInt( catScheme.get(ij).getFree());
                                freeQtyNew.getJSONObject(ik).put("FPQty",f);
                            }else {
                                JSONObject fitm=new JSONObject();

                                itm.put("FPCode",catScheme.get(ij).getOff_Pro_code());
                                itm.put("FPName",catScheme.get(ij).getOff_Pro_name());
                                itm.put("FPQty",catScheme.get(ij).getFree());
                                freeQtyNew.put(fitm);
                            }
                            JSONObject nItem=new JSONObject();
                            nItem.put("CatId",itm.getString("CatId"));
                            nItem.put("Qty",itm.getString("Qty"));
                            nItem.put("Value",itm.getString("Value"));
                            nItem.put("FPCode",catScheme.get(ij).getOff_Pro_code());
                            nItem.put("FPName",catScheme.get(ij).getOff_Pro_name());
                            nItem.put("FPQty",catScheme.get(ij).getFree());
                            FreeDetails.put(nItem);
                            Log.d(TAG, "showFreeQtyList: "+ itm.getString("Value"));
                        }
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        if (freeQty_Array_List != null && freeQty_Array_List.size() > 0) {
            findViewById(R.id.cdFreeQtyParent).setVisibility(View.VISIBLE);
            findViewById(R.id.lblfrdet).setVisibility(View.VISIBLE);
            Free_Adapter mFreeAdapter = new Free_Adapter(freeQtyNew, R.layout.product_free_recyclerview, getApplicationContext());
            freeRecyclerview.setAdapter(mFreeAdapter);

        } else {
            findViewById(R.id.cdFreeQtyParent).setVisibility(View.GONE);
            findViewById(R.id.lblfrdet).setVisibility(View.GONE);

        }

    }

    public void updateToTALITEMUI(int flag) {
        TextView tvTotalItems = findViewById(R.id.tvTotalItems);
        TextView tvTotLabel = findViewById(R.id.tvTotLabel);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        TextView tvTax = findViewById(R.id.tvTaxVal);
        TextView tvBillSubTotal = findViewById(R.id.subtotal);
        tvBillTotItem = findViewById(R.id.totalitem);
        TextView tvBillTotQty = findViewById(R.id.tvtotalqty);
        TextView tvBillToPay = findViewById(R.id.tvnetamount);
        TextView tvCashDiscount = findViewById(R.id.tvcashdiscount);
        TextView tvTaxLabel = findViewById(R.id.tvTaxLabel);
        tvTotalDiscLabel=findViewById(R.id.tvTotalDiscLabel);
        tvSaveAmt = findViewById(R.id.tvSaveAmt);
        Getorder_Array_List = new ArrayList<>();
        Getorder_Array_List.clear();
        totalvalues = 0;
        totalQty = 0;
        cashDiscount = 0;
        taxVal = 0;
        int totQty=0;
        totalMRP=0;
        subtotalvalue=0;

        for (int pm = 0; pm < Product_Modal.size(); pm++) {
            if (Product_Modal.get(pm).getRegularQty() != null) {
                if (Product_Modal.get(pm).getQty() > 0 /*|| Product_Modal.get(pm).getRegularQty() > 0*/) {
                    cashDiscount += Product_Modal.get(pm).getDiscount();
                    totalvalues += Product_Modal.get(pm).getAmount();
                    totalQty += Product_Modal.get(pm).getQty()/* + Product_Modal.get(pm).getRegularQty()*/;
                    subtotalvalue+=Product_Modal.get(pm).getQty()*Product_Modal.get(pm).getRate()*Product_Modal.get(pm).getCnvQty();
                    totQty += Product_Modal.get(pm).getQty()*Product_Modal.get(pm).getCnvQty();
                    totalMRP+=Product_Modal.get(pm).getCnvQty()*Product_Modal.get(pm).getQty()*Double.parseDouble(Product_Modal.get(pm).getMRP());

                    if (Product_Modal.get(pm).getTax() > 0)
                        taxVal += Product_Modal.get(pm).getTax();

                    Getorder_Array_List.add(Product_Modal.get(pm));


                }
            }
        }

        tvTotalAmount.setText(CurrencySymbol+" " + formatter.format(totalvalues));
        tvTotalItems.setText("Items : " + Getorder_Array_List.size() + "   Qty : " + totalQty);

        /*  if (Getorder_Array_List.size() == 1)
            tvTotLabel.setText("Price (1 item)");
        else
            tvTotLabel.setText("Price (" + Getorder_Array_List.size() + " items)");*/
        if(flag==1) {
            tvBillSubTotal.setText(CurrencySymbol + " " + formatter.format(subtotalvalue));
            tvBillTotItem.setText("" + Getorder_Array_List.size());
            tvBillTotQty.setText("" + totQty);
            tvBillToPay.setText(CurrencySymbol + " " + formatter.format(totalvalues));
            tvCashDiscount.setText("- " + CurrencySymbol + " " + formatter.format(cashDiscount));
            //  tvTax.setText(CurrencySymbol+" " + formatter.format(taxVal));
            //  tvPayAmount.setText("" + (int) totalvalues);

            tvInvAmt.setText(CurrencySymbol + " " + formatter.format(totalvalues));

            InvAmt = Double.parseDouble(tvInvAmt.getText().toString().replace(CurrencySymbol + " ", ""));
            rDiscAmt = InvAmt * (rDiscPer / 100);
            totalvalues = InvAmt - rDiscAmt;

            if (rDiscAmt == 0) {
                etDiscAmt.setText("");
            } else {
                etDiscAmt.setText(new DecimalFormat("##0.00").format(rDiscAmt));
            }

            tvPayAmt.setText(CurrencySymbol + " " + formatter.format(totalvalues));

            tvTotOutstanding.setText(CurrencySymbol + " " + formatter.format(outstandAmt + (totalvalues - payAmt)));

            tvTotalDiscLabel.setText("(Total Discount " + CurrencySymbol + " " + formatter.format(cashDiscount) + ")");
            //tvSaveAmt.setText("Your Saving Amount is MRP " + formatter.format(totalMRP) + " - NetAmount " + formatter.format(totalvalues) + " = " + CurrencySymbol + " " + formatter.format(totalMRP - totalvalues));
            tvSaveAmt.setText("Total Profit "+CurrencySymbol+" "  + formatter.format(totalMRP-totalvalues));
            //  tvSaveAmt.setText("Total Scheme Discount "+CurrencySymbol+" "  + formatter.format(totalMRP-totalvalues));

            // tvTax.setText(CurrencySymbol+" " + formatter.format(taxVal));
            ll_actual_total.setVisibility(View.GONE);
       /* if (cashDiscount > 0) {
            ll_actual_total.setVisibility(View.VISIBLE);
          tvSaveAmt.setVisibility(View.VISIBLE);
            tvSaveAmt.setText("You will save "+CurrencySymbol+" "  + formatter.format(cashDiscount) + " on this order");
            tvSaveAmt.setText("Total Scheme Discount "+CurrencySymbol+" "  + formatter.format(totalMRP-totalvalues));
        }else{
            tvSaveAmt.setVisibility(View.GONE);
           ll_actual_total.setVisibility(View.GONE);
       }*/
        /*if (cashDiscount > 0) {
            tvSaveAmt.setVisibility(View.VISIBLE);
            tvSaveAmt.setText("You will save "+CurrencySymbol+" " + formatter.format(cashDiscount) + " on this order");
        } else
            tvSaveAmt.setVisibility(View.GONE);*/


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
                amt = amt + CurrencySymbol + " " + String.valueOf(formatter.format(orderTotTax.get(i).getTax_Amt())) + "\n";

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

            sharedCommonPref.save(Constants.LOC_INVOICE_DATA, gson.toJson(Product_Modal));
        }

    }

    public void showOrderItemList(int categoryPos, String filterString) {
        try {
            Product_ModalSetAdapter.clear();
            int il = 0;
            for (Product_Details_Modal personNpi : Product_Modal) {
                if (personNpi.getProductCatCode().toString().equals(listt.get(categoryPos).getId())) {
                    if (Common_Class.isNullOrEmpty(filterString))
                        Product_ModalSetAdapter.add(personNpi);
                    else {
                        if (personNpi.getName().toLowerCase().contains(filterString.toLowerCase()) || personNpi.getERP_Code().toLowerCase().contains(filterString.toLowerCase()))
                            Product_ModalSetAdapter.add(personNpi);

                    }
                    il++;
                }
            }
            if (Product_ModalSetAdapter.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                tv_no_match.setVisibility(View.GONE);
            } else {
                if (!filterString.equalsIgnoreCase("")) {
                    tv_no_match.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_no_match.setVisibility(View.GONE);
                }
            }

            Category_Nametext.setText(listt.get(categoryPos).getName() + " ( " + String.valueOf(il) + " )");

            mProdct_Adapter = new Prodct_Adapter(Product_ModalSetAdapter, R.layout.product_invoice_recyclerview, getApplicationContext(), categoryPos);
            recyclerView.setAdapter(mProdct_Adapter);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

    }

   /* void loadData(String apiDataResponse) {
        try {
            Product_Modal = gson.fromJson(String.valueOf(db.getMasterData(Constants.Product_List)), userType);
            setProductSchemeAndTax(1);
            JSONArray jsonArray1 = new JSONArray(apiDataResponse);
            if (jsonArray1 != null && jsonArray1.length() > 0) {
                for (int pm = 0; pm < Product_Modal.size(); pm++) {
                    for (int q = 0; q < jsonArray1.length(); q++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(q);
                        if (Product_Modal.get(pm).getId().equals(jsonObject1.getString("Product_Code"))) {

                            Product_Modal.get(pm).setUOM_Nm(jsonObject1.getString("UOM"));
                            Product_Modal.get(pm).setUOM_Id("" + jsonObject1.getString("umo_unit"));
                            Product_Modal.get(pm).setCnvQty(jsonObject1.getDouble("Conf_Fac"));

                            Product_Modal.get(pm).setQty(
                                    jsonObject1.getInt("Quantity"));

                            // Product_Modal.get(pm).setAmount(Double.valueOf(formatter.format(Product_Modal.get(pm).getCnvQty() * Product_Modal.get(pm).getQty() *
                            //         Product_Modal.get(pm).getRate())));

//                            double dMRPAmt =Double.valueOf(formatter.format((Product_Modal.get(pm).getCnvQty() * Product_Modal.get(pm).getQty()) *
//                                    Double.parseDouble(Product_Modal.get(pm).getMRP().toString())));
//                            double dMrgn=dMRPAmt * (Product_Modal.get(pm).getMargin()/100);
//                            double sellAmt=dMRPAmt-dMrgn;

                            //  double sellAmt=Double.valueOf(formatter.format((Product_Modal.get(pm).getCnvQty() * Product_Modal.get(pm).getQty()) *Double.parseDouble(Product_Modal.get(pm).getPTR())));
                            double sellAmt=Double.valueOf(formatter.format((Product_Modal.get(pm).getCnvQty() * Product_Modal.get(pm).getQty()) *Product_Modal.get(pm).getRate()));
                            Product_Modal.get(pm).setAmount(sellAmt);

                            double enterQty = Product_Modal.get(pm).getQty() * Product_Modal.get(pm).getCnvQty();
                            String strSchemeList = sharedCommonPref.getvalue(Constants.FreeSchemeDiscList);

                           // Type type1 = new TypeToken<ArrayList<Product_Details_Modal>>() {
                           // }.getType();
                          //  List<Product_Details_Modal> product_details_modalArrayList = gson.fromJson(strSchemeList, type1);

                            double highestScheme = 0;
                            boolean haveVal = false;
                            List<Product_Details_Modal.Scheme> schemeList = Product_Modal.get(pm).getSchemeList();
                            if (schemeList != null && schemeList.size() > 0) {

                                for (int i = 0; i < schemeList.size(); i++) {

                                    if (Product_Modal.get(pm).getId().equals(schemeList.get(i).getPCode())) {

                                        haveVal = true;
                                        double schemeVal = schemeList.get(i).getScheme();

                                        if (enterQty >= schemeVal) {

                                            if (schemeVal > highestScheme) {
                                                highestScheme = schemeVal;


                                                if (!String.valueOf(schemeList.get(i).getFree()).equals("0")) {
                                                    if (schemeList.get(i).getPackages().equals("N")) {
                                                        double freePer = (enterQty / highestScheme);

                                                        double freeVal = freePer * Double.parseDouble(String.valueOf(schemeList.
                                                                get(i).getFree()));

                                                        Product_Modal.get(pm).setFree(String.valueOf(Math.round(freeVal)));
                                                    } else {
                                                        int val = (int) (enterQty / highestScheme);
                                                        int freeVal = val * Integer.parseInt(String.valueOf(schemeList.get(i).getFree()));
                                                        Product_Modal.get(pm).setFree(String.valueOf(freeVal));
                                                    }
                                                } else {

                                                    Product_Modal.get(pm).setFree("0");

                                                }


                                                if (schemeList.get(i).getDisc() != 0) {

                                                    if (schemeList.get(i).getDiscountType().equals("%")) {
                                                        double discountVal = enterQty * (((schemeList.get(i).getDisc()
                                                        )) / 100);
                                                        Product_Modal.get(pm).setDiscount(discountVal);

                                                    } else {
                                                        //Rs
                                                        double freeVal;
                                                        if (schemeList.get(i).getPackages().equals("N")) {
                                                            double freePer = (enterQty / highestScheme);
                                                            freeVal = freePer * (schemeList.
                                                                    get(i).getDisc());

                                                        } else {
                                                            int val = (int) (enterQty / highestScheme);
                                                            freeVal = (double) (val * (schemeList.get(i).getDisc()));
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
                                // Product_Modal.get(pm).setAmount((Product_Modal.get(pm).getAmount()) -
                                // Double.valueOf(Product_Modal.get(pm).getDiscount()));
                            }

                            // sumofTax(Product_Modal, pm);
                            sumofTaxNew(Product_Modal.get(pm));
                        }

                    }
                }


            }
            ResetSubmitBtn(0);
            showOrderList();
        } catch (Exception e) {
            Log.v(TAG + ":loadData:", e.getMessage());
        }

    }*/

   /* public void sumofTax(List<Product_Details_Modal> Product_Details_Modalitem, int pos) {
        try {
            String taxRes = sharedCommonPref.getvalue(Constants.TAXList);
            if (!Common_Class.isNullOrEmpty(taxRes)) {
                JSONObject jsonObject = new JSONObject(taxRes);
                JSONArray jsonArray = jsonObject.getJSONArray("Data");

                double TotalTax=getTotTax(Product_Details_Modalitem,pos);
             //   Log.e("taxc",""+Product_Details_Modalitem.get(pos).getTax());
               // Log.e("totamountc",""+Product_Details_Modalitem.get(pos).getAmount());
                //Log.e("totamountc",""+Product_Details_Modalitem.get(pos).getDiscount());
                if(Product_Details_Modalitem.get(pos).getDiscount()>0) {
                    double val = (100 + (TotalTax)) / 100;
                    double finDisc = Product_Details_Modalitem.get(pos).getDiscount() / val;
                    Product_Details_Modalitem.get(pos).setDiscount(finDisc);
                }
                Product_Details_Modalitem.get(pos).setAmount(Product_Details_Modalitem.get(pos).getAmount()-Product_Details_Modalitem.get(pos).getDiscount());
                double sellAmt=Product_Details_Modalitem.get(pos).getAmount();
                //sellAmt=sellAmt/((100+(TotalTax))/100);

                double wholeTax = 0;
                List<Product_Details_Modal> taxList = new ArrayList<>();
                double totTax=0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    if (jsonObject1.getString("Product_Detail_Code").equals(Product_Details_Modalitem.get(pos).getId())) {
                        if (jsonObject1.getDouble("Tax_Val") > 0) {

                            double taxCal = sellAmt *
                                    ((jsonObject1.getDouble("Tax_Val") / 100));
                            wholeTax += taxCal;

                            taxList.add(new Product_Details_Modal(jsonObject1.getString("Tax_Id"),
                                    jsonObject1.getString("Tax_Type"), jsonObject1.getDouble("Tax_Val"), taxCal));


                        }
                    }
                }
              //  Log.e("taxa",""+wholeTax);
                //Log.e("totamounta",""+Product_Details_Modalitem.get(pos).getAmount());
                //Product_Details_Modalitem.get(pos).setAmount(Double.valueOf(formatter.format(Product_Details_Modalitem.get(pos).getAmount()     )));
                Product_Details_Modalitem.get(pos).setProductDetailsModal(taxList);
                Product_Details_Modalitem.get(pos).setAmount(Double.valueOf(formatter.format(Product_Details_Modalitem.get(pos).getAmount()
                        + wholeTax)));
                Product_Details_Modalitem.get(pos).setTax(Double.parseDouble(formatter.format(wholeTax)));
                //Log.e("taxb",""+Product_Details_Modalitem.get(pos).getTax());
                //Log.e("totamountb",""+Product_Details_Modalitem.get(pos).getAmount());
                //Log.e("totamountb",""+Product_Details_Modalitem.get(pos).getDiscount());

            }
        } catch (Exception e) {
            Log.d("st","dd");
        }
    }*/

   /* public double getTotTax(List<Product_Details_Modal> Product_Details_Modalitem, int pos) {
        double totTax=0;
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
                            totTax+=jsonObject1.getDouble("Tax_Val");
                        }
                    }
                }

            }
        } catch (Exception e) {
            Log.d("gd","sd");
        }
        return totTax;
    }*/



    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {


            switch (key) {
              /*  case Constants.STOCK_DATA:
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

                        mProdct_Adapter.notifyDataSetChanged();
                    }


                    break;*/
                case Constants.VAN_STOCK:
                    Log.e("eq","van1");
                    JSONObject stkObj = new JSONObject(apiDataResponse);
                    if (stkObj.getBoolean("success")) {
                        Log.e("eq","van2");
                        JSONArray arr = stkObj.getJSONArray("Data");
                        List<Product_Details_Modal> stkList = new ArrayList<>();
                        Product_FilterList.clear();

                        for (int pm = 0; pm < Product_Modal.size(); pm++) {
                            Product_Modal.get(pm).setBalance(0);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                if (obj.getString("PCode").equalsIgnoreCase(Product_Modal.get(pm).getId())) {
                                    Product_Modal.get(pm).setBalance(obj.getInt("Bal"));
                                    Product_FilterList.add(Product_Modal.get(pm));


                                }
                            }
                        }

                        if(Product_FilterList.size()>0) {
                            Product_Modal.clear();
                            Product_Modal.addAll(Product_FilterList);
                        }
                        setGroupList();
                        // mProdct_Adapter.notifyDataSetChanged();



                    }else{
                        Log.e("eq","van3");
                        for (int pm = 0; pm < Product_Modal.size(); pm++) {
                            Product_Modal.get(pm).setBalance(0);

                        }
                        if(Product_FilterList.size()>0) {
                            Product_Modal.clear();
                            Product_Modal.addAll(Product_FilterList);
                        }
                        setGroupList();
                    }

                    break;
               /* case Constants.PreInvOrderQty:
                    if (Common_Class.isNullOrEmpty(apiDataResponse) || apiDataResponse.equals("[]")) {
                        ResetSubmitBtn(0);
                        common_class.showMsg(Invoice_Vansales_Select.this, "No Records Found.");
                    } else {


                    //    loadData(apiDataResponse);
                    }
                    break;*/
              /*  case Constants.VAN_OUTSTANDING:

                    JSONObject jsonObject = new JSONObject(apiDataResponse);

                    if (jsonObject.getBoolean("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            outstandAmt = jsonArray.getJSONObject(i).getDouble("Outstanding");
                            if (outstandAmt < 0) outstandAmt = Math.abs(outstandAmt);
                            else outstandAmt = 0 - outstandAmt;
                            tvOutStanding.setText(CurrencySymbol+" " + formatter.format(outstandAmt));
                        }

                    } else {

                        outstandAmt = 0;
                        tvOutStanding.setText(CurrencySymbol+" " + 0.00);
                    }

                    tvTotOutstanding.setText(CurrencySymbol+" " + tvOutStanding.getText().toString());
                    break;*/
                case Constants.PAYMODES:
                    JSONObject payObj = new JSONObject(apiDataResponse);

                    payList.clear();

                    if (payObj.getBoolean("success")) {

                        JSONArray jsonArray = payObj.getJSONArray("Data");
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

            }
        } catch (Exception e) {

            Log.d("gd","sd1");
        }

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
                    Product_ModalSetAdapter.get(uomPos).setCnvQty(Double.parseDouble((myDataset.get(position).getPhone())));
                    Product_ModalSetAdapter.get(uomPos).setUOM_Id(myDataset.get(position).getId());
                    Product_ModalSetAdapter.get(uomPos).setUOM_Nm(myDataset.get(position).getName());
                    mProdct_Adapter.notify(Product_ModalSetAdapter, R.layout.product_invoice_recyclerview, getApplicationContext(), 1);
                }
                //if (Product_ModalSetAdapter.get(uomPos).getBalance() == null
                //        || Product_ModalSetAdapter.get(uomPos).getBalance() >= qty
                //       || Product_ModalSetAdapter.get(uomPos).getCheckStock() == null
                //       || Product_ModalSetAdapter.get(uomPos).getCheckStock() == 0) {
                break;
            case 20:
                tvPayMode.setText("" + myDataset.get(position).getName());

                break;

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

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
        Context context;
        CategoryAdapter.MyViewHolder pholder;

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

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(CategoryAdapter.MyViewHolder holder, int position) {
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
        private Context context;
        int CategoryType;
        boolean isLoad = true;
        int uomClickCnt=0;
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
            if(Product_Details_Modalitem.size()>0)  notifyItemRangeChanged(0,Product_Details_Modalitem.size());
        }


        @Override
        public Prodct_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(Prodct_Adapter.MyViewHolder holder, int position) {
            try {
                int index=position;
                Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(holder.getBindingAdapterPosition());
                holder.productname.setText(Product_Details_Modal.getName().toUpperCase());
              //  holder.erpCode.setText(Product_Details_Modal.getERP_Code().toUpperCase());
                //   Log.e("taxc",""+Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTax());
                //   Log.e("totamountc",""+Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount());
                //   Log.e("totamount",""+Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount());
                if (!Common_Class.isNullOrEmpty(Product_Details_Modal.getUOM_Nm()))
                    holder.tvUOM.setText(Product_Details_Modal.getUOM_Nm());
                else {
                    holder.tvUOM.setText(Product_Details_Modal.getDefault_UOM_Name());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Nm(Product_Details_Modal.getDefault_UOM_Name());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setUOM_Id("" + Product_Details_Modal.getDefaultUOM());
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setCnvQty(Product_Details_Modal.getDefaultUOMQty());
                }

                holder.Rate.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty()));
                holder.Amount.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(Product_Details_Modal.getAmount()));
                //  holder.RegularQty.setText("" + Product_Details_Modal.getRegularQty());

               // holder.ActualTotal.setText(CurrencySymbol+" "+formatter.format(Product_Details_Modal.getAmount()+Product_Details_Modal.getDiscount()));
                //holder.Rate.setVisibility(View.GONE);
                if (Product_Details_Modal.getRateEdit() == 1)
                    holder.Rate.setVisibility(View.VISIBLE);
//                if (Product_Details_Modal.getRateEdit() == 1) {
//                    holder.Rate.setEnabled(true);
//                    holder.Rate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.edit_small, 0);
//                } else {
//                    holder.Rate.setEnabled(false);
//                    holder.Rate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                }


                if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() == null)
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setBalance(0);
                //if (Product_Details_Modal.getERP_Code().toUpperCase().equalsIgnoreCase("4000002046")){
                   // Log.d("hi","gh");
              //  }

                double totQty= Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty();
                holder.tvStock.setText("" + String.format("%.2f", (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance()/Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty())).replaceAll(".00","") + " " + holder.tvUOM.getText());

                holder.tvTknStock.setText("" + ((int) totQty) + " EA");
                holder.tvCLStock.setText("" + (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty) + " EA");

                holder.tvTknStock.setTextColor(getResources().getColor(R.color.green));
                holder.tvCLStock.setTextColor(getResources().getColor(R.color.green));
                if((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty)<0) {
                    holder.tvTknStock.setTextColor(getResources().getColor(R.color.color_red));
                    holder.tvCLStock.setTextColor(getResources().getColor(R.color.color_red));
                }

                if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() > 0)
                    holder.tvStock.setTextColor(getResources().getColor(R.color.green));
                else
                    holder.tvStock.setTextColor(getResources().getColor(R.color.color_red));

                holder.itemView.setBackgroundColor(getResources().getColor(R.color.white));
                if((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty)<0 && StockCheck.equalsIgnoreCase("1")) {
                    holder.itemView.setBackgroundColor(getResources().getColor(R.color.color_red));
                    //   holder.tvTknStock.setTextColor(getResources().getColor(R.color.color_red));
                    // holder.tvCLStock.setTextColor(getResources().getColor(R.color.color_red));
                }
                //holder.tvMRP.setText(CurrencySymbol+" " + Product_Details_Modal.getMRP());
               // holder.tvMRPLabel.setText(MRPCap);
                holder.tvMRP.setText(CurrencySymbol+" "  + formatter.format(Double.parseDouble(Product_Details_Modal.getMRP()) * Product_Details_Modal.getCnvQty()));

                if (CategoryType >= 0){
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
                   // holder.ActualTotal.setText(CurrencySymbol+" "+formatter.format(Product_Details_Modal.getAmount()+Product_Details_Modal.getDiscount()));
                    if(uomClickCnt==1){
                        uomClickCnt++;
                        loadUomFirstData(Product_Details_Modalitem,holder,CategoryType);
                    }
                    holder.rlUOM.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uomClickCnt++;
                            uomPos = position;
                            uomList = new ArrayList<>();
                            String uomids="";
                            if (Product_Details_Modal.getUOMList() != null && Product_Details_Modal.getUOMList().size() > 0) {
                                for (int i = 0; i < Product_Details_Modal.getUOMList().size(); i++) {
                                    com.saneforce.godairy.SFA_Model_Class.Product_Details_Modal.UOM uom = Product_Details_Modal.getUOMList().get(i);

                                    if((";"+uomids).toLowerCase().indexOf(";"+uom.getUOM_Id().toLowerCase()+";")<0) {
                                        uomids += uom.getUOM_Id().toLowerCase() + ";";
                                        uomList.add(new Common_Model(uom.getUOM_Nm(), uom.getUOM_Id(), "", "", String.valueOf(uom.getCnvQty())));
                                    }
                                }
                                common_class.showCommonDialog(uomList, 1, Invoice_Vansales_Select.this);
                            } else {
                                common_class.showMsg(Invoice_Vansales_Select.this, "No Records Found.");
                            }
                        }
                    });

                }

                // final double[] sellAmt = new double[1];
                // final double[] TotalTax = new double[1];
                if (CategoryType >= 0) {
                    new Thread(() -> {
                        holder.totalQty.setText("Total Qty : " + ((int) (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() /**
                         Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty()*/)));

                        //  Integer indx= holder.getBindingAdapterPosition();
                        //  sellAmt[0] = Product_Details_Modalitem.get(position).getAmount();
                        //  TotalTax[0] = getTotTaxNew(Product_Details_Modalitem.get(position));
                        //  sellAmt[0] = sellAmt[0] /((100+(TotalTax[0]))/100);
                    }).start();

                    // sellAmt[0] = sellAmt[0] /((100+(TotalTax[0]))/100);
                    // holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(sellAmt[0]));
                    //  holder.QtyAmt.setText(CurrencySymbol+" "  + formatter.format( (Product_Details_Modal.getQty() * Product_Details_Modal.getCnvQty())*Double.parseDouble( Product_Details_Modal.getPTR())));

                    //     holder.regularAmt.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(Product_Details_Modal.getRegularQty() *
                    //  Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate() * Product_Details_Modal.getCnvQty()));
                }

                runOnUiThread(() -> holder.tvTaxLabel.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modal.getTax())));

                if (Product_Details_Modal.getQty() > 0)
                    holder.Qty.setText("" + Product_Details_Modal.getQty());

                if (Common_Class.isNullOrEmpty(Product_Details_Modal.getFree()))
                    holder.Free.setText("0");
                else
                    holder.Free.setText("" + Product_Details_Modal.getFree());

                runOnUiThread(() -> holder.Disc.setText(CurrencySymbol+" "+ formatter.format(Product_Details_Modal.getDiscount())));
                //holder.tvDiscBasePrice.setText(CurrencySymbol+" "+formatter.format((Product_Details_Modal.getQty()*Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty())-Product_Details_Modal.getDiscount()));

                holder.QtyPls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Thread thread = new Thread(){
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                super.run();
                                String sVal = holder.Qty.getText().toString();
                                if (sVal.equalsIgnoreCase("")) sVal = "0";

                                int order = (int) ((Integer.parseInt(sVal) + 1) * Product_Details_Modal.getCnvQty());
                                int balance = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance();
                                if(StockCheck.equalsIgnoreCase("1") && order > balance ){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            common_class.showMsg(Invoice_Vansales_Select.this, "Can't exceed Stock");
                                        }
                                    });
                                }else{
                                    //if ((balance >= order) || Product_Details_Modal.getCheckStock() == null || Product_Details_Modal.getCheckStock() == 0) {
                                    if (Product_Details_Modal.getCheckStock() != null && Product_Details_Modal.getCheckStock() == 1)
                                        Log.e("txt_", "loop works");
                                    String finalSVal = sVal;
                                    runOnUiThread(() -> {
                                        holder.tvStock.setText("" + (int) (balance - order));
                                        holder.Qty.setText(String.valueOf(Integer.parseInt(finalSVal) + 1));
                                    });
                                }
                            }
                        };
                        thread.start();

                    }
                });

                holder.QtyMns.setOnClickListener(v -> {
                    try {
                        String sVal = holder.Qty.getText().toString();
                        if (sVal.equalsIgnoreCase("")) sVal = "0";
                        if (Integer.parseInt(sVal) > 0) {
                            holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) - 1));

                            int order = (int) ((Integer.parseInt(sVal) - 1) * Product_Details_Modal.getCnvQty());
                            int balance = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance();
                            if (Product_Details_Modal.getCheckStock() != null && Product_Details_Modal.getCheckStock() == 1)
                                holder.tvStock.setText("" + (int) (balance - order));
                        }

                    } catch (Exception e) {
                        Log.v(TAG + "QtyMns:", e.getMessage());
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


                            if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() < totQty && StockCheck.equalsIgnoreCase("1") //&&  Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCheckStock() > 0
                            ) {
//                                totQty = 0;
//                                enterQty = 0;
//                                holder.Qty.setText("0");
                                // common_class.showMsg(POSActivity.this, "No stock");
                                //totQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty();
                                //enterQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty();

                                //common_class.showMsg(Invoice_Vansales_Select.this, "Can't exceed stock");
                                //return;
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

                            /*

                            double totQty = (enterQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty());


                            if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCheckStock() != null && Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCheckStock() > 0 && Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() < totQty) {
                                totQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty();
                                enterQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty();
                                String pName=Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getName();
                                //holder.Qty.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty());
                                common_class.showMsg(Invoice_Vansales_Select.this, "Can't exceed stock - " + pName);
                            }

                            if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCheckStock() != null && Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCheckStock() > 0)
                                holder.tvStock.setText("" + (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty));

*/
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setQty((int) enterQty);
                            holder.Amount.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(totQty *  Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate()));
                            //Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(Double.valueOf(formatter.format(totQty *
                            //        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate())));
                            Integer intdx=holder.getBindingAdapterPosition();
//                            double dMRPAmt =Double.valueOf(formatter.format((Product_Details_Modalitem.get(intdx).getCnvQty() * Product_Details_Modalitem.get(intdx).getQty()) *
//                                    Double.parseDouble(Product_Details_Modalitem.get(intdx).getMRP().toString())));
//                            double dMrgn=dMRPAmt * (Product_Modal.get(intdx).getMargin()/100);
//                            double sellAmt=dMRPAmt-dMrgn;

                            // double sellAmt=Double.valueOf(formatter.format((Product_Details_Modalitem.get(intdx).getCnvQty() * Product_Details_Modalitem.get(intdx).getQty()) *
                            //  Double.parseDouble(Product_Details_Modalitem.get(intdx).getPTR())));
                            double sellAmt=Double.valueOf(formatter.format(((Product_Details_Modalitem.get(intdx).getCnvQty() * Product_Details_Modalitem.get(intdx).getQty()) *
                                    Product_Details_Modalitem.get(intdx).getRate())));
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(sellAmt);
                            holder.Amount.setText(CurrencySymbol+" "+formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));

                            //  double TotalTax=getTotTax(Product_Details_Modalitem,intdx);
                            // sellAmt=sellAmt/((100+(TotalTax))/100);

                            if (CategoryType >= 0) {
                                //    holder.QtyAmt.setText(CurrencySymbol+" "+ formatter.format((Product_Details_Modalitem.get(intdx).getCnvQty() * Product_Details_Modalitem.get(intdx).getQty()) *
                                //  Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getPTR())));
                                holder.totalQty.setText("Total Qty : " + (int) /*totQty*/enterQty);


                            }


                          /*  String strSchemeList = sharedCommonPref.getvalue(Constants.FreeSchemeDiscList);

                            Type type = new TypeToken<ArrayList<Product_Details_Modal>>() {
                            }.getType();
                            List<Product_Details_Modal> product_details_modalArrayList = gson.fromJson(strSchemeList, type);*/
                            List<Product_Details_Modal.Scheme> schemeList = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getSchemeList();

                            double highestScheme = 0;
                            boolean haveVal = false;
                            if (totQty > 0 && schemeList != null && schemeList.size() > 0) {

                                for (int i = 0; i < schemeList.size(); i++) {

                                    if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getId().equals(schemeList.get(i).getPCode())) {

                                        haveVal = true;
                                        double schemeVal = Double.parseDouble(String.valueOf(schemeList.get(i).getScheme()));

                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_code(schemeList.get(i).getOffProd());
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_name(schemeList.get(i).getOffProdNm());
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_Unit(schemeList.get(i).getOffProdUnit());
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree_val(String.valueOf(schemeList.get(i).getFree()));

                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_value(String.valueOf(schemeList.get(i).getDisc()));
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_type(schemeList.get(i).getDiscountType());


                                        if (totQty >= schemeVal) {

                                            if (schemeVal > highestScheme) {
                                                highestScheme = schemeVal;
                                                if (!String.valueOf(schemeList.get(i).getFree()).equals("0")) {
                                                    int freeq=0;
                                                    if(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty()>1) {
                                                        totQty = (enterQty + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRegularQty()) * (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty() - Double.parseDouble(String.valueOf(schemeList.
                                                                get(i).getFree())));
                                                    }
                                                    if (schemeList.get(i).getPackages().equals("N")) {
                                                        double freePer = (totQty / highestScheme);

                                                        double freeVal = freePer * Double.parseDouble(String.valueOf(schemeList.
                                                                get(i).getFree()));

                                                        freeq=Integer.parseInt(String.valueOf( Math.round(freeVal)));
                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree(String.valueOf(Math.round(freeVal)));
                                                    } else {
                                                        int val = (int) (totQty / highestScheme);
                                                        int freeVal = val * Integer.parseInt(String.valueOf(schemeList.get(i).getFree()));
                                                        freeq=freeVal;
                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree(String.valueOf(freeVal));
                                                    }
                                                    if(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty()>1) {
                                                        // totQty=totQty-freeq;
                                                        //  holder.Amount.setText(CurrencySymbol + " " + new DecimalFormat("##0.00").format(totQty * Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getPTR())));
                                                        //  Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(Double.valueOf(formatter.format(totQty *
                                                        //  Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getPTR()))));

                                                       // holder.tvDiscBasePrice.setText(CurrencySymbol+" "+formatter.format((Product_Details_Modal.getQty()*(Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty()))-Product_Details_Modal.getDiscount()));
                                                        //  holder.ActualTotal.setText(CurrencySymbol+" "+formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()-Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));

                                                    }
                                                } else {

                                                    holder.Free.setText("0");
                                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree("0");

                                                }


                                                if (schemeList.get(i).getDisc() != 0) {

                                                    if (schemeList.get(i).getDiscountType().equals("%")) {
                                                        double discountVal = totQty * (((schemeList.get(i).getDisc()
                                                        )) / 100);


                                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(((discountVal)));

                                                    } else {
                                                        //Rs
                                                        if (schemeList.get(i).getPackages().equals("N")) {
                                                            double freePer = (totQty / highestScheme);

                                                            double freeVal = freePer * (schemeList.
                                                                    get(i).getDisc());

                                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(((freeVal)));
                                                        } else {
                                                            int val = (int) (totQty / highestScheme);
                                                            double freeVal = (double) (val * (schemeList.get(i).getDisc()));
                                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount((freeVal));
                                                        }
                                                    }


                                                } else {
                                                    holder.Disc.setText(CurrencySymbol+" 0.00");
                                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(0.00);
                                                  //  holder.tvDiscBasePrice.setText(CurrencySymbol+" "+formatter.format((Product_Details_Modal.getQty()*(Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty()))-Product_Details_Modal.getDiscount()));

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
                               // holder.tvDiscBasePrice.setText(CurrencySymbol+" "+formatter.format((Product_Details_Modal.getQty()*(Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty()))-Product_Details_Modal.getDiscount()));


                            } else {

                                //  Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()) -
                                //   (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));

                                holder.Free.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getFree());
                                holder.Disc.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));

                                holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
                              //  holder.tvDiscBasePrice.setText(CurrencySymbol+" "+formatter.format((Product_Details_Modal.getQty()*(Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty()))-Product_Details_Modal.getDiscount()));
                              //  holder.ActualTotal.setText(CurrencySymbol+" "+formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()+Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));


                            }


                            int psc=(int)totQty;
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOrderQty(psc);
                            //sumofTax(Product_Details_Modalitem, holder.getBindingAdapterPosition());
                            sumofTaxNew(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()));
                            holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
                            holder.tvTaxLabel.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTax()));
                           // holder.tvDiscBasePrice.setText(CurrencySymbol+" "+formatter.format((Product_Details_Modal.getQty()*(Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty()))-Product_Details_Modal.getDiscount()));
                           // holder.ActualTotal.setText(CurrencySymbol+" "+formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()+Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));
                            holder.Disc.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));



                            if (CategoryType == -1) {
                                //  String amt = holder.Amount.getText().toString();
//                                if (amt.equals(CurrencySymbol+" 0.00")) {
//                                    Product_Details_Modalitem.remove(position);
//                                    notifyDataSetChanged();
//                                }
                                updateToTALITEMUI(1);
                                showFreeQtyList();
                            }else{
                                updateToTALITEMUI(0);
                            }

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
                            AlertDialogBox.showDialog(Invoice_Vansales_Select.this, "HAP SFA",
                                    "Do you want to remove " + Product_Details_Modalitem.get(position).getName().toUpperCase() + " from your cart?"
                                    , "OK", "Cancel", false, new AlertBox() {
                                        @Override
                                        public void PositiveMethod(DialogInterface dialog, int id) {
                                            Product_Details_Modalitem.get(position).setQty(0);
                                            Product_Details_Modalitem.remove(position);
                                            //notifyDataSetChanged();
                                            //notifyItemChanged(position);
                                            notifyItemRemoved(position);
                                            //notifyItemRangeChanged(position, Product_Details_Modalitem.size());
                                            updateToTALITEMUI(1);
                                            showFreeQtyList();
                                        }

                                        @Override
                                        public void NegativeMethod(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    });

                    //  if(Product_Details_Modal.getDiscount()==0){
                   // holder.ll_ActualTotal.setVisibility(View.GONE);
                   // holder.ActualTotal.setPaintFlags( holder.ActualTotal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                   // holder.ActualTotal.setText(CurrencySymbol+" "+formatter.format(Product_Details_Modal.getAmount()+Product_Details_Modal.getDiscount()));
                    ///  }else{
                    // holder.ll_ActualTotal.setVisibility(View.GONE);
                    //  }

                }
                holder.Rate.setOnClickListener(v -> showDialog(Product_Details_Modal));

                // using boolean function for single time load
                if (isLoad){
                    Log.e("isLoad_", "Loaded");
                    updateToTALITEMUI(0);
                    if(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTax()==0)
                        sumofTaxNew(Product_Details_Modalitem.get(position));
                    isLoad = false;
                }
            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }
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
        public int getItemCount() {
//            if(Product_Details_Modalitem.size()<=0){
//                updateToTALITEMUI();
//            }
//            return Product_Details_Modalitem.size();
            return Product_Details_Modalitem.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView erpCode,productname, Rate, Amount, Disc, Free, RegularQty, lblRQty, productQty, regularAmt;
            TextView  QtyAmt, totalQty, tvTaxLabel, tvUOM, tvStock,tvTknStock,tvCLStock,tvBatchNo, tvMRP, tvDiscBasePrice,ActualTotal,tvMRPLabel;
            ImageView ImgVwProd, QtyPls, QtyMns, ivDel;
            EditText Qty;
            LinearLayout rlUOM,ll_ActualTotal;

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
                tvUOM = view.findViewById(R.id.tvUOM);
                tvStock = view.findViewById(R.id.tvStockBal);
                tvTknStock = view.findViewById(R.id.tvTknStock);
                tvCLStock = view.findViewById(R.id.tvCLStock);
                tvBatchNo= view.findViewById(R.id.tvBatchNo);
                tvMRP = view.findViewById(R.id.MrpRate);
                tvDiscBasePrice =view.findViewById(R.id.tvDiscBasePrice);
                ll_ActualTotal=view.findViewById(R.id.ll_ActualTotal);
                ActualTotal=view.findViewById(R.id.ActualTotal);
                tvMRPLabel=view.findViewById(R.id.MrpLabel);

                if (CategoryType >= 0) {
                    ImgVwProd = view.findViewById(R.id.ivAddShoppingCart);
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

        private void showDialog(Product_Details_Modal product_details_modal) {
            try {
                LayoutInflater inflater = LayoutInflater.from(Invoice_Vansales_Select.this);

                final View view = inflater.inflate(R.layout.edittext_price_dialog, null);
                AlertDialog alertDialog = new AlertDialog.Builder(Invoice_Vansales_Select.this).create();
                alertDialog.setCancelable(false);

                final EditText etComments = (EditText) view.findViewById(R.id.et_addItem);
                Button btnSave = (Button) view.findViewById(R.id.btn_save);
                Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

                btnSave.setOnClickListener(v -> {
                    if (Common_Class.isNullOrEmpty(etComments.getText().toString())) {
                        common_class.showMsg(Invoice_Vansales_Select.this, "Empty value is not allowed");
                    } else if (Double.valueOf(etComments.getText().toString()) > Double.valueOf(product_details_modal.getMRP())) {
                        common_class.showMsg(Invoice_Vansales_Select.this, "Enter Rate is greater than "+MRPCap);
                    } else {
                        alertDialog.dismiss();
                        product_details_modal.setRate(Double.valueOf(etComments.getText().toString()));
                        etComments.setText("");
                        notifyDataSetChanged();

                    }
                });
                btnCancel.setOnClickListener(v -> alertDialog.dismiss());

                alertDialog.setView(view);
                alertDialog.show();
            } catch (Exception e) {
                Log.e("OrderAdapter:dialog ", e.getMessage());
            }
        }
    }


    public class Free_Adapter extends RecyclerView.Adapter<Free_Adapter.MyViewHolder> {
        Context context;
        private final JSONArray jFree;
        private final int rowLayout;


        public Free_Adapter(JSONArray FreeDet, int rowLayout, Context context) {
            this.jFree = FreeDet;
            this.rowLayout = rowLayout;
            this.context = context;


        }

        @Override
        public Free_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new Free_Adapter.MyViewHolder(view);
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
        public void onBindViewHolder(Free_Adapter.MyViewHolder holder, int position) {
            try {


                JSONObject nItm = jFree.getJSONObject(position);


                holder.productname.setText("" + nItm.getString("FPName").toUpperCase());

                holder.Free.setText(String.valueOf( nItm.getString("FPQty")));


                // updateToTALITEMUI();
            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return jFree.length();
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

 /*   @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (takeorder.getText().toString().equalsIgnoreCase("SUBMIT")) {
                moveProductScreen();
            } else if (PendingOrdersActivity.CometoPending){
                finish();
            } else {
                common_class.commonDialog(this, Invoice_History.class, "Invoice?");
            }
            return true;
        }
        return false;
    }*/

    void moveProductScreen() {
        lin_gridcategory.setVisibility(View.VISIBLE);
        findViewById(R.id.rlSearchParent).setVisibility(View.VISIBLE);
        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.llBillHeader).setVisibility(View.GONE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.GONE);
        rlAddProduct.setVisibility(View.GONE);
        // btnRepeat.setVisibility(View.VISIBLE);
        findViewById(R.id.cdFreeQtyParent).setVisibility(View.GONE);
        takeorder.setText("PROCEED");
        showOrderItemList(selectedPos, "");


    }
    public class MinMaxInputFilter implements InputFilter {
        private double mMinValue;
        private double mMaxValue;

        private static final double MIN_VALUE_DEFAULT = Double.MIN_VALUE;
        private static final double MAX_VALUE_DEFAULT = Double.MAX_VALUE;

        public MinMaxInputFilter(Double min, Double max) {
            this.mMinValue = (min != null ? min : MIN_VALUE_DEFAULT);
            this.mMaxValue = (max != null ? max : MAX_VALUE_DEFAULT);
        }

        public MinMaxInputFilter(Integer min, Integer max) {
            this.mMinValue = (min != null ? min : MIN_VALUE_DEFAULT);
            this.mMaxValue = (max != null ? max : MAX_VALUE_DEFAULT);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                String replacement = source.subSequence(start, end).toString();
                String newVal = dest.subSequence(0, dstart).toString() + replacement
                        + dest.subSequence(dend, dest.length()).toString();

                // check if there are leading zeros
                if (newVal.matches("0\\d+.*"))
                    if (TextUtils.isEmpty(source))
                        return dest.subSequence(dstart, dend);
                    else
                        return "";

                // check range
                double input = Double.parseDouble(newVal);
                if (!isInRange(mMinValue, mMaxValue, input))
                    if (TextUtils.isEmpty(source))
                        return dest.subSequence(dstart, dend);
                    else
                        return "";

                return null;
            } catch (NumberFormatException nfe) {
                Log.e("inputfilter", "parse");
            }
            return "";
        }

        private boolean isInRange(double a, double b, double c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

    public void setBalanceStockZero(){

    }

    public void setProductSchemeAndTax(int flag) {
        try {

            String strSchemeList = sharedCommonPref.getvalue(Constants.FreeSchemeDiscList);
            Type type = new TypeToken<ArrayList<Product_Details_Modal>>() {
            }.getType();
            List<Product_Details_Modal> product_details_modalArrayList = gson.fromJson(strSchemeList, type);
            String taxRes = sharedCommonPref.getvalue(Constants.TAXList);
            JSONArray jsonArray=null;
            if (!Common_Class.isNullOrEmpty(taxRes)) {
                JSONObject jsonObject = new JSONObject(taxRes);
                jsonArray = jsonObject.getJSONArray("Data");
            }
            if (Product_Modal.size() > 0) {
                for (int a = 0; a < Product_Modal.size(); a++) {

                    String productId = Product_Modal.get(a).getId();
                    try {
                        if (product_details_modalArrayList != null && product_details_modalArrayList.size() > 0) {
                            List<Product_Details_Modal.Scheme> schemeList = new ArrayList<>();
                            for (int b = 0; b < product_details_modalArrayList.size(); b++) {
                                String schemeProductId = product_details_modalArrayList.get(b).getId();
                                if (productId.equalsIgnoreCase(schemeProductId)) {
                                    Product_Details_Modal.Scheme productScheme = new Product_Details_Modal.Scheme();
                                    productScheme.setPCode(product_details_modalArrayList.get(b).getId());
                                    productScheme.setScheme(Integer.parseInt(product_details_modalArrayList.get(b).getScheme()));
                                    productScheme.setOffProd(product_details_modalArrayList.get(b).getOff_Pro_code());
                                    productScheme.setOffProdNm(product_details_modalArrayList.get(b).getOff_Pro_name());
                                    productScheme.setOffProdUnit(product_details_modalArrayList.get(b).getOff_Pro_Unit());
                                    productScheme.setFreeUnit(product_details_modalArrayList.get(b).getFree());
                                    productScheme.setFree(Integer.parseInt(product_details_modalArrayList.get(b).getFree()));
                                    productScheme.setDiscountType(product_details_modalArrayList.get(b).getDiscount_type());
                                    productScheme.setPackages(product_details_modalArrayList.get(b).getPackage());
                                    productScheme.setDiscountValue(product_details_modalArrayList.get(b).getDiscount_value());
                                    productScheme.setDisc(product_details_modalArrayList.get(b).getDiscount());
                                    schemeList.add(productScheme);
                                }

                            }
                            Product_Modal.get(a).setSchemeList(schemeList);
                        }
                    }catch (Exception e){
                        Log.e("scheme error:",e.toString());
                    }


                    try {
                        if (!Common_Class.isNullOrEmpty(taxRes)) {
                            List<Product_Details_Modal> taxList = new ArrayList<>();
                            double totTax = 0;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if (jsonObject1.getString("Product_Detail_Code").equals(productId)) {
                                    taxList.add(new Product_Details_Modal(jsonObject1.getString("Tax_Id"), jsonObject1.getString("Tax_Type"), jsonObject1.getDouble("Tax_Val"), 0));
                                }

                            }
                            Product_Modal.get(a).setProductDetailsModal(taxList);
                        }
                    }catch (Exception e){
                        Log.e("tax error:",e.toString());
                    }

                }

                if (flag == 0) {
                    JSONArray ProdGroups = db.getMasterData(Constants.Van_ProdGroups_List);

                    JSONArray filterArr = new JSONArray();

                    //   new Thread(() -> {

                    for (int i = 0; i < ProdGroups.length(); i++) {
                        JSONObject obj = null;
                        try {
                            obj = ProdGroups.getJSONObject(i);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            if (Common_Class.isNullOrEmpty(Shared_Common_Pref.SecOrdOutletType) || (Shared_Common_Pref.SecOrdOutletType.contains(obj.getString("name"))))
                                filterArr.put(obj);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //    }).start();

                    LinearLayoutManager GrpgridlayManager = new LinearLayoutManager(this);
                    GrpgridlayManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    Grpgrid.setLayoutManager(GrpgridlayManager);

                    RyclListItemAdb grplistItems = new RyclListItemAdb(filterArr, this, new onListItemClick() {
                        @Override
                        public void onItemClick(JSONObject item) {

                            try {
                                FilterTypes(item.getString("id"));
                                OrderTypId = item.getString("id");
                                OrderTypNm = item.getString("name");
                                common_class.brandPos = 0;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Grpgrid.setAdapter(grplistItems);

                    FilterTypes(filterArr.getJSONObject(0).getString("id"));

                    OrderTypId = filterArr.getJSONObject(0).getString("id");
                    OrderTypNm = filterArr.getJSONObject(0).getString("name");
                }
            }
        } catch (JSONException e) {
            Log.e("schemeandtax error", e.getMessage());
        }

    }

    public void sumofTaxNew(Product_Details_Modal Product_Details_Modalitem) {
        try {
            if(Product_Details_Modalitem.getProductDetailsModal()!=null) {
                if (Product_Details_Modalitem.getProductDetailsModal().size() > 0 || Product_Details_Modalitem.getDiscount() > 0) {

              /*  double TotalTax = getTotTaxNew(Product_Details_Modalitem);
                //Log.e("sellAmt", "" + Product_Details_Modalitem.getDiscount());
                if (Product_Details_Modalitem.getDiscount() > 0) {
                    double val = (100 + (TotalTax)) / 100;
                    double finDisc = Product_Details_Modalitem.getDiscount() / val;
                    Product_Details_Modalitem.setDiscount(finDisc);
                }*/
                    Product_Details_Modalitem.setAmount(Product_Details_Modalitem.getAmount() - Product_Details_Modalitem.getDiscount());
                    double sellAmt = Product_Details_Modalitem.getAmount();
                    //   sellAmt=sellAmt/((100+(TotalTax))/100);
                    // Log.e("sellAmt", "" + sellAmt + "TotalTax:" + TotalTax + "getamount:" + Product_Details_Modalitem.getAmount());
                    if (Product_Details_Modalitem.getProductDetailsModal().size() > 0) {
                        List<Product_Details_Modal> productTaxList = Product_Details_Modalitem.getProductDetailsModal();
                        double wholeTax = 0;
                        List<Product_Details_Modal> taxList = new ArrayList<>();
                        for (int i = 0; i < productTaxList.size(); i++) {
                            if (productTaxList.get(i).getTax_Val() > 0) {
                                double taxCal = sellAmt * (productTaxList.get(i).getTax_Val() / 100);
                                wholeTax += taxCal;

                                taxList.add(new Product_Details_Modal(productTaxList.get(i).getTax_Id(), productTaxList.get(i).getTax_Type(), productTaxList.get(i).getTax_Val(), taxCal));
                            }
                        }


                        Product_Details_Modalitem.setProductDetailsModal(taxList);
                        //   Log.e("taxa", "" + wholeTax);
                        //  Log.e("totamounta", "" + Product_Details_Modalitem.getAmount());
                        Product_Details_Modalitem.setAmount(Double.valueOf(formatter.format(Product_Details_Modalitem.getAmount() + wholeTax)));
                        Product_Details_Modalitem.setTax(Double.parseDouble(formatter.format(wholeTax)));

                    }
                }
            }
        } catch (Exception e) {
            Log.d("sumoftax error",e.getMessage());
        }
    }
    private void UploadPhoto(String path, String SF, String FileName, String Mode, Bitmap image) {
        try {
            common_class.ProgressdialogShow(1, "");

            MultipartBody.Part imgg;
            if (path != null && (path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg"))) {
                imgg = convertimg("file", path);

            } else {
                common_class.ProgressdialogShow(0, "");
                common_class.showMsg(this, "Image file only supported");
                return;
            }


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseBody> mCall = apiInterface.onTAFileUpload(SF, FileName, Mode, imgg);

            Log.e("SEND_IMAGE_SERVER", mCall.request().toString());

            mCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful()) {


                            JSONObject js = new JSONObject(response.body().string());
                            Log.v("Res", js.toString());

                            if (js.getBoolean("success")) {

                                if (image != null) {

                                    imageServer=FileName;
                                    imageConvert = path;
                                    imageSet = "file://" + path;
                                    attachedImage.setImageBitmap(image);
                                    attachedImage.setVisibility(View.VISIBLE);

                                }


                                common_class.ProgressdialogShow(0, "");

                                common_class.showMsg(Invoice_Vansales_Select.this, "File uploading successful ");
                            } else {
                                common_class.ProgressdialogShow(0, "");
                                common_class.showMsg(Invoice_Vansales_Select.this, "Failed.Try Again...");
                            }
                        } else {

                            common_class.ProgressdialogShow(0, "");
                            common_class.showMsg(Invoice_Vansales_Select.this, "Failed.Try Again...");

                        }

                    } catch (Exception e) {
                        common_class.ProgressdialogShow(0, "");
                        common_class.showMsg(Invoice_Vansales_Select.this, "Failed.Try Again...");

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    common_class.ProgressdialogShow(0, "");
                    common_class.showMsg(Invoice_Vansales_Select.this, "Failed.Try Again...");

                    Log.e("SEND_IMAGE_Response", "ERROR");
                }
            });


        } catch (Exception e) {
            Log.e("TAClaim:", e.getMessage());
        }
    }
    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
        try {
            if (!TextUtils.isEmpty(path)) {

                File file = new File(path);
                if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
                    file = new Compressor(getApplicationContext()).compressToFile(new File(path));
                else
                    file = new File(path);
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
                yy = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);
            }
        } catch (Exception e) {
        }
        return yy;
    }
    public double getTotTaxNew(Product_Details_Modal Product_Details_Modalitem) {
        double totTax=0;
        try {
            if (Product_Details_Modalitem.getProductDetailsModal().size()>0) {
                List<Product_Details_Modal> productTaxList=Product_Details_Modalitem.getProductDetailsModal();
                for (int i = 0; i < productTaxList.size(); i++) {
                    if (productTaxList.get(i).getTax_Val()> 0) {
                        totTax+=productTaxList.get(i).getTax_Val();
                    }

                }

            }
        } catch (Exception e) {
            Log.d("gd","sd");
        }
        return totTax;
    }

    private void loadUomFirstData(List<Product_Details_Modal> Product_Details_Modalitem, Prodct_Adapter.MyViewHolder holder, int CategoryType) {
        try {
            Product_Details_Modal Product_Details_Modal=Product_Details_Modalitem.get(holder.getBindingAdapterPosition());
            double enterQty = 0;
            enterQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty();


            double totQty = (enterQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty());


            if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() < totQty && StockCheck.equalsIgnoreCase("1") //&&  Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCheckStock() > 0
            ) {
//                                totQty = 0;
//                                enterQty = 0;
//                                holder.Qty.setText("0");
                // common_class.showMsg(POSActivity.this, "No stock");
                //totQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty();
                //enterQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty();

                //common_class.showMsg(Invoice_Vansales_Select.this, "Can't exceed stock");
                //return;
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

                            /*

                            double totQty = (enterQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty());


                            if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCheckStock() != null && Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCheckStock() > 0 && Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() < totQty) {
                                totQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty() * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty();
                                enterQty = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty();
                                String pName=Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getName();
                                //holder.Qty.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getQty());
                                common_class.showMsg(Invoice_Vansales_Select.this, "Can't exceed stock - " + pName);
                            }

                            if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCheckStock() != null && Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCheckStock() > 0)
                                holder.tvStock.setText("" + (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() - (int) totQty));

*/
            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setQty((int) enterQty);
            holder.Amount.setText(CurrencySymbol+" "+ new DecimalFormat("##0.00").format(totQty *  Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate()));
            //Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(Double.valueOf(formatter.format(totQty *
            //        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate())));
            Integer intdx=holder.getBindingAdapterPosition();
//                            double dMRPAmt =Double.valueOf(formatter.format((Product_Details_Modalitem.get(intdx).getCnvQty() * Product_Details_Modalitem.get(intdx).getQty()) *
//                                    Double.parseDouble(Product_Details_Modalitem.get(intdx).getMRP().toString())));
//                            double dMrgn=dMRPAmt * (Product_Modal.get(intdx).getMargin()/100);
//                            double sellAmt=dMRPAmt-dMrgn;

            // double sellAmt=Double.valueOf(formatter.format((Product_Details_Modalitem.get(intdx).getCnvQty() * Product_Details_Modalitem.get(intdx).getQty()) *
            //  Double.parseDouble(Product_Details_Modalitem.get(intdx).getPTR())));
            double sellAmt=Double.valueOf(formatter.format(((Product_Details_Modalitem.get(intdx).getCnvQty() * Product_Details_Modalitem.get(intdx).getQty()) *
                    Product_Details_Modalitem.get(intdx).getRate())));
            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(sellAmt);
            holder.Amount.setText(CurrencySymbol+" "+formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));

            //  double TotalTax=getTotTax(Product_Details_Modalitem,intdx);
            // sellAmt=sellAmt/((100+(TotalTax))/100);

            if (CategoryType >= 0) {
                //    holder.QtyAmt.setText(CurrencySymbol+" "+ formatter.format((Product_Details_Modalitem.get(intdx).getCnvQty() * Product_Details_Modalitem.get(intdx).getQty()) *
                //  Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getPTR())));
                holder.totalQty.setText("Total Qty : " + (int) /*totQty*/enterQty);


            }


                          /*  String strSchemeList = sharedCommonPref.getvalue(Constants.FreeSchemeDiscList);

                            Type type = new TypeToken<ArrayList<Product_Details_Modal>>() {
                            }.getType();
                            List<Product_Details_Modal> product_details_modalArrayList = gson.fromJson(strSchemeList, type);*/
            List<Product_Details_Modal.Scheme> schemeList = Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getSchemeList();

            double highestScheme = 0;
            boolean haveVal = false;
            if (totQty > 0 && schemeList != null && schemeList.size() > 0) {

                for (int i = 0; i < schemeList.size(); i++) {

                    if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getId().equals(schemeList.get(i).getPCode())) {

                        haveVal = true;
                        double schemeVal = Double.parseDouble(String.valueOf(schemeList.get(i).getScheme()));

                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_code(schemeList.get(i).getOffProd());
                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_name(schemeList.get(i).getOffProdNm());
                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOff_Pro_Unit(schemeList.get(i).getOffProdUnit());
                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree_val(String.valueOf(schemeList.get(i).getFree()));

                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_value(String.valueOf(schemeList.get(i).getDisc()));
                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount_type(schemeList.get(i).getDiscountType());


                        if (totQty >= schemeVal) {

                            if (schemeVal > highestScheme) {
                                highestScheme = schemeVal;
                                if (!String.valueOf(schemeList.get(i).getFree()).equals("0")) {
                                    int freeq=0;
                                    if(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty()>1) {
                                        totQty = (enterQty + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRegularQty()) * (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty() - Double.parseDouble(String.valueOf(schemeList.
                                                get(i).getFree())));
                                    }
                                    if (schemeList.get(i).getPackages().equals("N")) {
                                        double freePer = (totQty / highestScheme);

                                        double freeVal = freePer * Double.parseDouble(String.valueOf(schemeList.
                                                get(i).getFree()));

                                        freeq=Integer.parseInt(String.valueOf( Math.round(freeVal)));
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree(String.valueOf(Math.round(freeVal)));
                                    } else {
                                        int val = (int) (totQty / highestScheme);
                                        int freeVal = val * Integer.parseInt(String.valueOf(schemeList.get(i).getFree()));
                                        freeq=freeVal;
                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree(String.valueOf(freeVal));
                                    }
                                    if(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getCnvQty()>1) {
                                        // totQty=totQty-freeq;
                                        //  holder.Amount.setText(CurrencySymbol + " " + new DecimalFormat("##0.00").format(totQty * Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getPTR())));
                                        //  Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(Double.valueOf(formatter.format(totQty *
                                        //  Double.parseDouble(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getPTR()))));

                                        //holder.tvDiscBasePrice.setText(CurrencySymbol+" "+formatter.format((Product_Details_Modal.getQty()*(Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty()))-Product_Details_Modal.getDiscount()));
                                        //  holder.ActualTotal.setText(CurrencySymbol+" "+formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()-Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));

                                    }
                                } else {

                                    holder.Free.setText("0");
                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setFree("0");

                                }


                                if (schemeList.get(i).getDisc() != 0) {

                                    if (schemeList.get(i).getDiscountType().equals("%")) {
                                        double discountVal = totQty * (((schemeList.get(i).getDisc()
                                        )) / 100);


                                        Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(((discountVal)));

                                    } else {
                                        //Rs
                                        if (schemeList.get(i).getPackages().equals("N")) {
                                            double freePer = (totQty / highestScheme);

                                            double freeVal = freePer * (schemeList.
                                                    get(i).getDisc());

                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(((freeVal)));
                                        } else {
                                            int val = (int) (totQty / highestScheme);
                                            double freeVal = (double) (val * (schemeList.get(i).getDisc()));
                                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount((freeVal));
                                        }
                                    }


                                } else {
                                    holder.Disc.setText(CurrencySymbol+" 0.00");
                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setDiscount(0.00);
                                   // holder.tvDiscBasePrice.setText(CurrencySymbol+" "+formatter.format((Product_Details_Modal.getQty()*(Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty()))-Product_Details_Modal.getDiscount()));

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
                holder.tvDiscBasePrice.setText(CurrencySymbol+" "+formatter.format((Product_Details_Modal.getQty()*(Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty()))-Product_Details_Modal.getDiscount()));


            } else {

                //  Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount((Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()) -
                //   (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));

                holder.Free.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getFree());
                holder.Disc.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));

                holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
                //holder.tvDiscBasePrice.setText(CurrencySymbol+" "+formatter.format((Product_Details_Modal.getQty()*(Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty()))-Product_Details_Modal.getDiscount()));
               // holder.ActualTotal.setText(CurrencySymbol+" "+formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()+Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));


            }


            int psc=(int)totQty;
            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setOrderQty(psc);
            //sumofTax(Product_Details_Modalitem, holder.getBindingAdapterPosition());
            sumofTaxNew(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()));
            holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
            holder.tvTaxLabel.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTax()));
           // holder.tvDiscBasePrice.setText(CurrencySymbol+" "+formatter.format((Product_Details_Modal.getQty()*(Product_Details_Modal.getRate() * Product_Details_Modal.getCnvQty()))-Product_Details_Modal.getDiscount()));
           // holder.ActualTotal.setText(CurrencySymbol+" "+formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()+Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));
            holder.Disc.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getDiscount()));



            if (CategoryType == -1) {
                //  String amt = holder.Amount.getText().toString();
//                                if (amt.equals(CurrencySymbol+" 0.00")) {
//                                    Product_Details_Modalitem.remove(position);
//                                    notifyDataSetChanged();
//                                }
                updateToTALITEMUI(1);
                showFreeQtyList();
            }else{
                updateToTALITEMUI(0);
            }

        } catch (Exception e) {
            Log.v(TAG, " orderAdapter:qty " + e.getMessage());
        }

    }

    private  void setGroupList(){
        Log.e("eq","van4");
        try {
            JSONArray ProdGroups = db.getMasterData(Constants.Van_ProdGroups_List);

            JSONArray filterArr = new JSONArray();

            //new Thread(() -> {
            Log.e("eq","van5:"+ProdGroups.length());
            for (int i = 0; i < ProdGroups.length(); i++) {
                JSONObject obj = null;
                try {
                    obj = ProdGroups.getJSONObject(i);
                    filterArr.put(obj);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

               /* try {
                    if ("+4,Ambient".contains(obj.getString("name")))
                        filterArr.put(obj);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }*/
            }
            LinearLayoutManager GrpgridlayManager = new LinearLayoutManager(this);
            GrpgridlayManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            Grpgrid.setLayoutManager(GrpgridlayManager);

            RyclListItemAdb grplistItems = new RyclListItemAdb(filterArr, this, new onListItemClick() {
                @Override
                public void onItemClick(JSONObject item) {

                    try {
                        FilterTypes(item.getString("id"));
                        OrderTypId = item.getString("id");
                        OrderTypNm = item.getString("name");
                        common_class.brandPos = 0;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Grpgrid.setAdapter(grplistItems);

            FilterTypes(filterArr.getJSONObject(0).getString("id"));

            OrderTypId = filterArr.getJSONObject(0).getString("id");
            OrderTypNm = filterArr.getJSONObject(0).getString("name");
        }catch(Exception e){
            Toast.makeText (getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),"Try after sometimes",Toast.LENGTH_SHORT).show();
        }
    }

}