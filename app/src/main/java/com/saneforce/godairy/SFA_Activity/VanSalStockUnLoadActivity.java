package com.saneforce.godairy.SFA_Activity;

import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;
import static com.saneforce.godairy.SFA_Activity.HAPApp.MRPCap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
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
import com.saneforce.godairy.Activity_Hap.AllowancCapture;
import com.saneforce.godairy.Activity_Hap.ProductImageView;
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

public class VanSalStockUnLoadActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI, Master_Interface {

    List<Category_Universe_Modal> Category_Modal = new ArrayList<>();
    List<Product_Details_Modal> Product_Modal;
    List<Product_Details_Modal> Product_ModalSetAdapter;
    List<Product_Details_Modal> Getorder_Array_List;
    List<Product_Details_Modal> freeQty_Array_List;
    List<Category_Universe_Modal> listt;
    Type userType;
    Gson gson;
    SharedPreferences UserDetails;
    CircularProgressButton takeorder;
    TextView Out_Let_Name, Category_Nametext,tvVehNo,
            tvOtherBrand, tvQPS, tvPOP, tvCoolerInfo, tvRetailorPhone, retaileAddress, tvHeader, tvVanSalPay, tvStockView;
    LinearLayout lin_orderrecyclerview, lin_gridcategory, rlAddProduct, llCalMob;
    Common_Class common_class;
    String Ukey;
    String[] strLoc;
    String Worktype_code = "", Route_Code = "", Dirtributor_Cod = "", Distributor_Name = "",vehNo="", UserInfo = "MyPrefs";
    Shared_Common_Pref sharedCommonPref;
    Prodct_Adapter mProdct_Adapter;
    String TAG = "VanSalesOrderActivity";
    DatabaseHandler db;
    RelativeLayout rlCategoryItemSearch;
    ImageView ivClose, attachedImage, endKmImg;
    EditText etCategoryItemSearch, edtEndKm;
    int cashDiscount;
    NumberFormat formatter = new DecimalFormat("##0.00");
    private RecyclerView recyclerView, categorygrid, Grpgrid, Brndgrid, freeRecyclerview;
    public int selectedPos = 0;
    private TextView tvTotalAmount;
    private double totalvalues, taxVal;
    private Integer totalQty;
    private TextView tvBillTotItem;
    final Handler handler = new Handler();
    private List<Product_Details_Modal> orderTotTax;
    private int uomPos;
    private ArrayList<Common_Model> uomList;

    String axn = "",imageSet = "", imageServer = "",imageConvert = "";
    private double totStkAmt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_vansale_unload);
            db = new DatabaseHandler(this);

            UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

            sharedCommonPref = new Shared_Common_Pref(VanSalStockUnLoadActivity.this);
            common_class = new Common_Class(this);
            tvHeader = findViewById(R.id.tvHeader);
            tvVanSalPay = findViewById(R.id.tvVanSalPay);
            tvStockView = findViewById(R.id.tvStockView);
            Grpgrid = findViewById(R.id.PGroup);
            Brndgrid = findViewById(R.id.PBrnd);
            categorygrid = findViewById(R.id.category);
            takeorder = findViewById(R.id.takeorder);
            common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);
            lin_orderrecyclerview = findViewById(R.id.lin_orderrecyclerview);
            lin_gridcategory = findViewById(R.id.lin_gridcategory);
            Out_Let_Name = findViewById(R.id.outlet_name);
            Category_Nametext = findViewById(R.id.Category_Nametext);
            rlCategoryItemSearch = findViewById(R.id.rlCategoryItemSearch);
            rlAddProduct = findViewById(R.id.rlAddProduct);
            ivClose = findViewById(R.id.ivClose);
            tvVehNo = findViewById(R.id.tvUnloadVehNo);

            tvOtherBrand = (TextView) findViewById(R.id.tvOtherBrand);
            tvPOP = (TextView) findViewById(R.id.tvPOP);
            tvQPS = (TextView) findViewById(R.id.tvQPS);
            tvCoolerInfo = (TextView) findViewById(R.id.tvCoolerInfo);
            etCategoryItemSearch = findViewById(R.id.searchView);
            retaileAddress = findViewById(R.id.retaileAddress);
            tvRetailorPhone = findViewById(R.id.retailePhoneNum);
            edtEndKm=findViewById(R.id.edtEndKm);

            endKmImg=findViewById(R.id.endKm_attach);
            attachedImage=findViewById(R.id.attachedUnloadEndImage);

            llCalMob = findViewById(R.id.btnCallMob);
            llCalMob.setOnClickListener(this);
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

            vehNo=sharedCommonPref.getvalue(Constants.Vansales_VehNo);
            Log.v("vehNo_ghj",vehNo);
            tvVehNo.setText(vehNo);

            endKmImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CameraPermission cameraPermission = new CameraPermission(VanSalStockUnLoadActivity.this, getApplicationContext());

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
                        Intent intent = new Intent(VanSalStockUnLoadActivity.this, AllowancCapture.class);
                        intent.putExtra("allowance", "Two");
                        startActivity(intent);

                    }

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



            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Distributor_phone)))
                llCalMob.setVisibility(View.GONE);
            else
                tvRetailorPhone.setText(sharedCommonPref.getvalue(Constants.Distributor_phone));
            retaileAddress.setText(sharedCommonPref.getvalue(Constants.DistributorAdd));
            Out_Let_Name.setText(sharedCommonPref.getvalue(Constants.Distributor_name));

            //GetJsonData(String.valueOf(db.getMasterData(Constants.Category_List)), "1", "");
            String OrdersTable = String.valueOf(db.getMasterData(Constants.Product_List));
            userType = new TypeToken<ArrayList<Product_Details_Modal>>() {
            }.getType();

//            if (!Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.LOC_VANSALES_DATA)) && Constants.VAN_SALES_MODE.equalsIgnoreCase(Constants.VAN_STOCK_UNLOADING))
//                Product_Modal = gson.fromJson(sharedCommonPref.getvalue(Constants.LOC_VANSALES_DATA), userType);
//            else {
            Product_Modal = gson.fromJson(OrdersTable, userType);

            // }

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);

            Log.v(TAG, " order oncreate:h ");

            // showOrderItemList(0, "");

            Log.v(TAG, " order oncreate:i ");
            tvOtherBrand.setOnClickListener(this);
            tvQPS.setOnClickListener(this);
            tvPOP.setOnClickListener(this);
            tvCoolerInfo.setOnClickListener(this);
            Category_Nametext.setOnClickListener(this);
            tvVanSalPay.setOnClickListener(this);
            tvStockView.setOnClickListener(this);

            findViewById(R.id.tvOrder).setVisibility(View.GONE);


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

          /*  String preOrderList = sharedCommonPref.getvalue(Constants.PreOrderQtyList);

            if (!Common_Class.isNullOrEmpty(preOrderList) && Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.LOC_VANSALES_DATA))) {
                for (int pm = 0; pm < Product_Modal.size(); pm++) {
                    JSONObject jsonObjectPreOrder = new JSONObject(preOrderList);
                    JSONArray arr = jsonObjectPreOrder.getJSONArray("Data");

                    for (int k = 0; k < arr.length(); k++) {
                        JSONObject obj = arr.getJSONObject(k);

                        if (Product_Modal.get(pm).getId().equals(obj.getString("Product_Detail_Code"))) {

                            Product_Modal.get(pm).setRegularQty(obj.getInt("Qty"));

                            Product_Modal.get(pm).setAmount(Double.valueOf(formatter.format(Product_Modal.get(pm).getRegularQty() * Product_Modal.get(pm).getRate())));


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

            if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE))
                findViewById(R.id.orderTypesLayout).setVisibility(View.GONE);

            GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "6", "");


            JSONArray ProdGroups = db.getMasterData(Constants.ProdGroups_List);
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

            FilterTypes(ProdGroups.getJSONObject(0).getString("id"));

            tvHeader.setText(Constants.VAN_SALES_MODE);


//            switch (Constants.VAN_SALES_MODE) {
//                case Constants.VAN_STOCK_LOADING:
//                    axn = "save/stockloading";
//                    break;
//                case Constants.VAN_STOCK_UNLOADING:
            axn = "save/stockunloading";
//                    break;
//                case Constants.VAN_SALES_ORDER:
//                    axn = "save/vansales";
//                    break;
//            }


            tvCoolerInfo.setVisibility(View.GONE);

            if (Shared_Common_Pref.Freezer_Required.equalsIgnoreCase("yes"))
                tvCoolerInfo.setVisibility(View.VISIBLE);


            common_class.getDb_310Data(Constants.VAN_STOCK, this);
        } catch (Exception e) {
            Log.v(TAG, " order oncreate: " + e.getMessage());

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

                                common_class.showMsg(VanSalStockUnLoadActivity.this, "File uploading successful ");
                            } else {
                                common_class.ProgressdialogShow(0, "");
                                common_class.showMsg(VanSalStockUnLoadActivity.this, "Failed.Try Again...");
                            }
                        } else {

                            common_class.ProgressdialogShow(0, "");
                            common_class.showMsg(VanSalStockUnLoadActivity.this, "Failed.Try Again...");

                        }

                    } catch (Exception e) {
                        common_class.ProgressdialogShow(0, "");
                        common_class.showMsg(VanSalStockUnLoadActivity.this, "Failed.Try Again...");

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    common_class.ProgressdialogShow(0, "");
                    common_class.showMsg(VanSalStockUnLoadActivity.this, "Failed.Try Again...");

                    Log.e("SEND_IMAGE_Response", "ERROR");
                }
            });


        } catch (Exception e) {
            Log.e("TAClaim:", e.getMessage());
        }
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
            if (type.equals("1")) Category_Modal.clear();
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

                selectedPos = 0;

                VanSalStockUnLoadActivity.CategoryAdapter customAdapteravail = new VanSalStockUnLoadActivity.CategoryAdapter(getApplicationContext(),
                        Category_Modal);
                categorygrid.setAdapter(customAdapteravail);

//                if (Constants.VAN_SALES_MODE.equalsIgnoreCase(Constants.VAN_STOCK_UNLOADING))
//                    showOrderList();
//                else
//                    showOrderItemList(selectedPos, "");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            case R.id.tvVanSalPay:
                Intent intent = new Intent(VanSalStockUnLoadActivity.this, VanSalPaymentActivity.class);
                intent.putExtra("stkLoadAmt", totStkAmt);
                startActivity(intent);
                break;
            case R.id.tvStockView:
                startActivity(new Intent(VanSalStockUnLoadActivity.this, VanStockViewActivity.class));
                break;

            case R.id.btnCallMob:
                common_class.showCalDialog(VanSalStockUnLoadActivity.this, "Do you want to Call this Distributor?",
                        tvRetailorPhone.getText().toString().replaceAll(",", ""));

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

            case R.id.tvOtherBrand:
                common_class.commonDialog(this, OtherBrandActivity.class, "Van Sales?");
                break;
            case R.id.tvQPS:
                common_class.commonDialog(this, QPSActivity.class, "Van Sales?");
                break;
            case R.id.tvPOP:
                common_class.commonDialog(this, POPActivity.class, "Van Sales?");
                break;
            case R.id.tvCoolerInfo:
                common_class.commonDialog(this, CoolerInfoActivity.class, "Van Sales?");
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

    private void SaveOrder(String axn) {
        if (common_class.isNetworkAvailable(this)) {

            AlertDialogBox.showDialog(VanSalStockUnLoadActivity.this, HAPApp.Title, "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {
                    common_class.ProgressdialogShow(1, "");
                    JSONArray data = new JSONArray();
                    JSONObject ActivityData = new JSONObject();
                    try {
                        JSONObject HeadItem = new JSONObject();
                        HeadItem.put("id", sharedCommonPref.getvalue(Constants
                                .VAN_ID));

                        HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("Worktype_code", Worktype_code);
                        HeadItem.put("Town_code", sharedCommonPref.getvalue(Constants.Route_Id));
                        HeadItem.put("dcr_activity_date", Common_Class.GetDate());
                        // HeadItem.put("Daywise_Remarks", "");
                        HeadItem.put("UKey", Ukey);
                        //  HeadItem.put("orderValue", formatter.format(totalvalues));
                        HeadItem.put("DataSF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("AppVer", BuildConfig.VERSION_NAME);

                        HeadItem.put("Vansales_VehNo",vehNo);
                        HeadItem.put("Vansales_EndKm",edtEndKm.getText().toString());
                        HeadItem.put("Vansales_Endkm_Image",imageSet);

                        Log.v("vansalesUnload",HeadItem.toString());

                        ActivityData.put("Activity_Report_Head", HeadItem);

                        JSONObject OutletItem = new JSONObject();
//                        OutletItem.put("Doc_Meet_Time", Common_Class.GetDate());
//                        OutletItem.put("modified_time", Common_Class.GetDate());
                        OutletItem.put("stockist_code", sharedCommonPref.getvalue(Constants.Distributor_Id));
                        OutletItem.put("stockist_name", sharedCommonPref.getvalue(Constants.Distributor_name));
                        OutletItem.put("orderValue", formatter.format(totalvalues));
                        OutletItem.put("CashDiscount", cashDiscount);
                        OutletItem.put("NetAmount", formatter.format(totalvalues));
                        OutletItem.put("No_Of_items", tvBillTotItem.getText().toString());
                        //  OutletItem.put("Invoice_Flag", Shared_Common_Pref.Invoicetoorder);
                        OutletItem.put("TransSlNo", Shared_Common_Pref.TransSlNo);
                        OutletItem.put("doctor_code", Shared_Common_Pref.OutletCode);
                        OutletItem.put("doctor_name", Shared_Common_Pref.OutletName);
                        OutletItem.put("ordertype", "Van Stock UnLoading");

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
                            ProdItem.put("Stock_Qty", Getorder_Array_List.get(z).getQty());

                            ProdItem.put("Product_Qty", Getorder_Array_List.get(z).getQty());
                            //   ProdItem.put("Product_RegularQty", Getorder_Array_List.get(z).getRegularQty());
//                            ProdItem.put("Product_Total_Qty", Getorder_Array_List.get(z).getQty() +
//                                    Getorder_Array_List.get(z).getRegularQty());
                            ProdItem.put("Product_Amount", Getorder_Array_List.get(z).getAmount());
                            ProdItem.put("Rate", String.format("%.2f", Getorder_Array_List.get(z).getRate()));

//                            ProdItem.put("free", Getorder_Array_List.get(z).getFree());
//                            ProdItem.put("dis", Getorder_Array_List.get(z).getDiscount());
//                            ProdItem.put("dis_value", Getorder_Array_List.get(z).getDiscount_value());
//                            ProdItem.put("Off_Pro_code", Getorder_Array_List.get(z).getOff_Pro_code());
//                            ProdItem.put("Off_Pro_name", Getorder_Array_List.get(z).getOff_Pro_name());
//                            ProdItem.put("Off_Pro_Unit", Getorder_Array_List.get(z).getOff_Pro_Unit());
//                            ProdItem.put("Off_Scheme_Unit", Getorder_Array_List.get(z).getScheme());
//                            ProdItem.put("discount_type", Getorder_Array_List.get(z).getDiscount_type());

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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<JsonObject> responseBodyCall = apiInterface.saveVanSales(axn, Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
                    responseBodyCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                try {
                                    common_class.ProgressdialogShow(0, "");
                                    Log.e("JSON_VALUES", response.body().toString());
                                    JSONObject jsonObjects = new JSONObject(response.body().toString());
                                    String san = jsonObjects.getString("success");
                                    Log.e("Success_Message", san);
                                    ResetSubmitBtn(1);
                                    if (san.equals("true")) {
                                        sharedCommonPref.clear_pref(Constants.LOC_VANSALES_DATA);
                                        sharedCommonPref.clear_pref(Constants.VAN_STOCK_LOADING);
                                        //  common_class.CommonIntentwithFinish(Invoice_History.class);
                                        finish();
                                    }
                                    common_class.showMsg(VanSalStockUnLoadActivity.this, jsonObjects.getString("Msg"));

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
        findViewById(R.id.orderTypesLayout).setVisibility(View.GONE);
        findViewById(R.id.rlCategoryItemSearch).setVisibility(View.GONE);
        findViewById(R.id.rlSearchParent).setVisibility(View.GONE);
        //for testing
        //  findViewById(R.id.llBillHeader).setVisibility(View.VISIBLE);
        findViewById(R.id.llPayNetAmountDetail).setVisibility(View.VISIBLE);
        lin_gridcategory.setVisibility(View.GONE);
        lin_orderrecyclerview.setVisibility(View.VISIBLE);
        takeorder.setText("SUBMIT");

        mProdct_Adapter = new Prodct_Adapter(orderList, R.layout.vansales_unload_pay_recyclerview, getApplicationContext(), -1);
        recyclerView.setAdapter(mProdct_Adapter);
        showFreeQtyList();
    }

    void showFreeQtyList() {
        freeQty_Array_List = new ArrayList<>();
        freeQty_Array_List.clear();

        for (Product_Details_Modal pm : Product_Modal) {

            if (pm.getRegularQty() != null) {
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
        cashDiscount = 0;
        taxVal = 0;


        for (int pm = 0; pm < Product_Modal.size(); pm++) {

            if (Product_Modal.get(pm).getRegularQty() != null) {
                if (Product_Modal.get(pm).getQty() > 0 || Product_Modal.get(pm).getRegularQty() > 0) {

                    cashDiscount += (int) Product_Modal.get(pm).getDiscount();

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

        tvBillSubTotal.setText(CurrencySymbol+" " + formatter.format(totalvalues));
        tvBillTotItem.setText("" + Getorder_Array_List.size());
        tvBillTotQty.setText("" + totalQty);
        tvBillToPay.setText(CurrencySymbol+" " + formatter.format(totalvalues));
        tvCashDiscount.setText(CurrencySymbol+" " + formatter.format(cashDiscount));
        // tvTax.setText(CurrencySymbol+" "+ formatter.format(taxVal));


        if (cashDiscount > 0) {
            tvSaveAmt.setVisibility(View.VISIBLE);
            tvSaveAmt.setText("You will save " +CurrencySymbol+" "+ formatter.format(cashDiscount) + " on this order");
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
        Category_Nametext.setText(listt.get(categoryPos).getName());

        mProdct_Adapter = new Prodct_Adapter(Product_ModalSetAdapter, R.layout.vansales_product_order_recyclerview, getApplicationContext(), categoryPos);
        recyclerView.setAdapter(mProdct_Adapter);

    }


    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            switch (key) {

                case Constants.VAN_STOCK:
                    JSONObject stkObj = new JSONObject(apiDataResponse);


                    totStkAmt = 0;
                    if (stkObj.getBoolean("success")) {
                        JSONArray arr = stkObj.getJSONArray("Data");
                        List<Product_Details_Modal> stkList = new ArrayList<>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            //  Getorder_Array_List.clear();


                            for (int pm = 0; pm < Product_Modal.size(); pm++) {
                                if (obj.getString("PCode").equalsIgnoreCase(Product_Modal.get(pm).getId())) {
                                    Product_Modal.get(pm).setQty(obj.getInt("Cr"));
                                    Product_Modal.get(pm).setCr(obj.getInt("Cr"));
                                    Product_Modal.get(pm).setDr(obj.getInt("Dr"));
                                    Product_Modal.get(pm).setBalance(obj.getInt("Bal"));


                                    try {
                                        Product_Modal.get(pm).setCrAmt(obj.getDouble("CrAmt"));
                                        Product_Modal.get(pm).setDrAmt(obj.getDouble("DrAmt"));
                                        Product_Modal.get(pm).setAmount(obj.getDouble("CrAmt"));
                                        Product_Modal.get(pm).setBalAmt(obj.getDouble("BalAmt"));

                                        totStkAmt += obj.getDouble("CrAmt");


                                    } catch (Exception e) {

                                    }

                                    //  Getorder_Array_List.add(Product_Modal.get(pm));

                                }

                            }

                            showOrderList();

                            // mProdct_Adapter.notify(Getorder_Array_List,R.layout.vansales_unload_pay_recyclerview,VanSalStockUnLoadActivity.this,-1);
                        }


                    }

                    break;
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
        findViewById(R.id.orderTypesLayout).setVisibility(View.VISIBLE);
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
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        switch (type) {
            case 1:
                Product_ModalSetAdapter.get(uomPos).setCnvQty(Double.parseDouble((myDataset.get(position).getPhone())));
                Product_ModalSetAdapter.get(uomPos).setUOM_Id(myDataset.get(position).getId());
                Product_ModalSetAdapter.get(uomPos).setUOM_Nm(myDataset.get(position).getName());
                mProdct_Adapter.notify(Product_ModalSetAdapter, R.layout.vansales_product_order_recyclerview, getApplicationContext(), 1);
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
        public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
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

                if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() == null)
                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setBalance(0);


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


                if (CategoryType >= 0) {

                    holder.tvStock.setText("" + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance());

                    if (Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getBalance() > 0)
                        holder.tvStock.setTextColor(getResources().getColor(R.color.green));
                    else
                        holder.tvStock.setTextColor(getResources().getColor(R.color.color_red));


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
                                    com.saneforce.godairy.SFA_Model_Class.Product_Details_Modal.UOM uom = Product_Details_Modal.getUOMList().get(i);

                                    if((";"+uomids).toLowerCase().indexOf(";"+uom.getUOM_Id().toLowerCase()+";")<0) {
                                        uomids += uom.getUOM_Id().toLowerCase() + ";";
                                        uomList.add(new Common_Model(uom.getUOM_Nm(), uom.getUOM_Id(), "", "", String.valueOf(uom.getCnvQty())));
                                    }
                                }
                                common_class.showCommonDialog(uomList, 1, VanSalStockUnLoadActivity.this);
                            } else {
                                common_class.showMsg(VanSalStockUnLoadActivity.this, "No Records Found.");
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
                        holder.Qty.setText(String.valueOf(Integer.parseInt(sVal) + 1));
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

                            double totQty = ((enterQty + Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRegularQty()) * Product_Details_Modal.getCnvQty());


                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setQty((int) enterQty);
                            holder.Amount.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(totQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate()));
                            Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).setAmount(Double.valueOf(formatter.format(totQty *
                                    Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate())));
                            if (CategoryType >= 0) {
                                holder.QtyAmt.setText(CurrencySymbol+" " + formatter.format(enterQty * Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getRate() * Product_Details_Modal.getCnvQty()));
                                holder.totalQty.setText("Total Qty : " + (int)/* totQty*/enterQty);
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
                            sumofTax(Product_Details_Modalitem, holder.getBindingAdapterPosition());
                            holder.Amount.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getAmount()));
                            holder.tvTaxLabel.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modalitem.get(holder.getBindingAdapterPosition()).getTax()));

                            updateToTALITEMUI();

//                            if (CategoryType == -1) {
//                                String amt = holder.Amount.getText().toString();
//                                Log.v(TAG + position, ":OUT:amt:" + amt);
//                                if (amt.equals(CurrencySymbol+" 0.00")) {
//                                    Log.v(TAG + position, ":IN:amt:" + amt);
//                                    Product_Details_Modalitem.remove(position);
//                                    notifyDataSetChanged();
//                                }
//
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

                    if (Constants.VAN_SALES_MODE.equalsIgnoreCase(Constants.VAN_STOCK_UNLOADING))
                        holder.ivDel.setVisibility(View.INVISIBLE);

                    //  holder.Qty.setText("" + Product_Details_Modal.getQty());
                    // holder.Amount.setText(CurrencySymbol+" "+ formatter.format(Product_Details_Modal.getCrAmt()));


                    holder.tvSalesQty.setText("" + Product_Details_Modal.getDr());
                    holder.tvSalesAmt.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modal.getDrAmt()));


                    holder.tvunloadQty.setText("" + (Product_Details_Modal.getCr() - Product_Details_Modal.getDr()));
                    holder.tvUnloadAmt.setText(CurrencySymbol+" " + formatter.format(Product_Details_Modal.getCrAmt() - Product_Details_Modal.getDrAmt()));


                    holder.ivDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialogBox.showDialog(VanSalStockUnLoadActivity.this, HAPApp.Title,
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

                updateToTALITEMUI();
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


                LayoutInflater inflater = LayoutInflater.from(VanSalStockUnLoadActivity.this);

                final View view = inflater.inflate(R.layout.edittext_price_dialog, null);
                AlertDialog alertDialog = new AlertDialog.Builder(VanSalStockUnLoadActivity.this).create();
                alertDialog.setCancelable(false);

                final EditText etComments = (EditText) view.findViewById(R.id.et_addItem);
                Button btnSave = (Button) view.findViewById(R.id.btn_save);
                Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Common_Class.isNullOrEmpty(etComments.getText().toString())) {
                            common_class.showMsg(VanSalStockUnLoadActivity.this, "Empty value is not allowed");
                        } else if (Double.valueOf(etComments.getText().toString()) > Double.valueOf(product_details_modal.getRate())) {
                            common_class.showMsg(VanSalStockUnLoadActivity.this, "Enter Rate is greater than "+MRPCap);

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
                    QtyAmt, totalQty, tvTaxLabel, tvMRP, tvStock, tvSalesQty, tvSalesAmt, tvunloadQty, tvUnloadAmt;
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


                if (CategoryType >= 0) {
                    tvMRP = view.findViewById(R.id.MrpRate);
                    lblRQty = view.findViewById(R.id.status);
                    regularAmt = view.findViewById(R.id.RegularAmt);
                    QtyAmt = view.findViewById(R.id.qtyAmt);
                    totalQty = view.findViewById(R.id.totalqty);
                    rlUOM = view.findViewById(R.id.rlUOM);
                    tvStock = view.findViewById(R.id.tvStockBal);


                } else {
                    ivDel = view.findViewById(R.id.ivDel);
                    tvSalesQty = view.findViewById(R.id.salesQty);
                    tvSalesAmt = view.findViewById(R.id.salesAmount);
                    tvunloadQty = view.findViewById(R.id.unLoadQty);
                    tvUnloadAmt = view.findViewById(R.id.tvUnLoadAmount);
                }


            }
        }


    }

    public class Free_Adapter extends RecyclerView.Adapter<Free_Adapter.MyViewHolder> {
        Context context;
        private List<Product_Details_Modal> Product_Details_Modalitem;
        private int rowLayout;


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