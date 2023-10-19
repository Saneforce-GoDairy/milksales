package com.saneforce.godairy.Activity_Hap;

import static com.saneforce.godairy.Activity_Hap.Leave_Request.CheckInfo;
import static com.saneforce.godairy.Common_Class.Constants.CUSTOMER_DATA;
import static com.saneforce.godairy.Common_Class.Constants.Freezer_Status;
import static com.saneforce.godairy.Common_Class.Constants.Freezer_capacity;
import static com.saneforce.godairy.Common_Class.Constants.OUTLET_CATEGORY;
import static com.saneforce.godairy.Common_Class.Constants.Rout_List;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
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
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.ApproveOutletsActivity;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.SFA_Adapter.FilesAdapter;
import com.saneforce.godairy.SFA_Adapter.QPS_Modal;
import com.saneforce.godairy.SFA_Model_Class.Retailer_Modal_List;
import com.saneforce.godairy.common.DatabaseHandler;
import com.saneforce.godairy.common.FileUploadService;
import com.saneforce.godairy.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Created by RAGU on 27/01/2023
public class ApproveOutletsDetailedActivity extends AppCompatActivity implements Master_Interface, View.OnClickListener, OnMapReadyCallback, UpdateResponseUI {
    public static ApproveOutletsDetailedActivity mAddNewRetailer;
    public static Context context;
    final Handler handler = new Handler();
    public String categoryType = "";
    TextView toolHeader, editDetails;
    ImageView imgBack;
    EditText toolSearch;
    GoogleMap mGoogleMap;
    ApiInterface service;
    RelativeLayout linReatilerRoute, rlDistributor, rlDelvryType, rlOutletType, rlState, linReatilerChannel, rlSubCategory, linServiceType, rlFreezerCapacity, rlFreezerSta;
    LinearLayout linReatilerClass, CurrentLocLin, retailercodevisible, linClsRmks;
    TextView txtRetailerRoute, txtRetailerClass, txtRetailerChannel, CurrentLocationsAddress, headtext, distributor_text,
            txDelvryType, txOutletType, tvStateName, retailercode, tvServiceType, tvFreezerCapacity, tvFreezerSta, tvSubCategory;
    Type userType;
    List<Common_Model> modelRetailClass = new ArrayList<>();
    List<Common_Model> modelRetailChannel = new ArrayList<>();
    List<Common_Model> categoryList = new ArrayList<>();
    Common_Model mCommon_model_spinner;
    Gson gson;
    EditText addRetailerName, owner_name, addRetailerAddress, addRetailerCity, etDistrict, addRetailerPhone, addRetailerEmail, edt_sub_category, edtDepositAmt, edtExpcSalVal,
            edt_pin_codeedit, edt_gst, etPhoneNo2, edt_outstanding, edtClsRetRmk, edtFSSAI, edtPAN, edtFreezerMake, edtFreezerTag, edtDistCode;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    JSONArray mainArray;
    JSONObject docMasterObject;
    String keyEk = "N", KeyDate, KeyHyp = "-", keyCodeValue, imageConvert = "", imageServer = "";
    Integer classId, channelID, iOutletTyp, stateCode;
    String routeId, Compititor_Id, Compititor_Name, CatUniverSelectId, AvailUniverSelectId, reason_category_remarks = "", HatsunAvailswitch = "", categoryuniverseswitch = "";
    Shared_Common_Pref shared_common_pref;
    SharedPreferences UserDetails, CheckInDetails;
    Common_Class common_class;
    List<com.saneforce.godairy.SFA_Model_Class.Retailer_Modal_List> Retailer_Modal_List;
    String outletCode = "";
    ImageView copypaste, ivCapture, ivFreezerCapture;
    String TAG = "AddNewRetailer: ", UserInfo = "MyPrefs";
    DatabaseHandler db;
    ImageView ivPhotoShop, ivProfilePreview;
    String filePath;
    File file;
    Common_Model Model_Pojo;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    List<Common_Model> freezerCapcityList = new ArrayList<>();
    List<Common_Model> freezerStaList = new ArrayList<>();
    CircularProgressButton btnRefLoc;
    double RetLat = 0.0, RetLng = 0.0;
    List<Common_Model> deliveryTypeList, outletTypeList;
    RecyclerView rvFiles, rvFreezerFiles, rvCategoryTypes;
    List<QPS_Modal> mData = new ArrayList<>();
    List<QPS_Modal> mFreezerData = new ArrayList<>();
    String divERP = "", freezerStaId = "", freezerCapId = "", distributorERP = "";
    Button btnDistCode;
    Boolean isValidCode = false;
    CheckBox cbFranchise, cbFreezerYes, cbFreezerNo;
    boolean isFlag = false;
    String customer_code = "";
    Button approveBtn, rejectBtn, updateButton;
    LinearLayout llUpdate, llApprove;
    boolean editMode = false;
    private Uri outputFileUri;
    private String finalPath = "";
    private String place_id = "";
    private ArrayList<Common_Model> stateList = new ArrayList<>();
    private ArrayList<Common_Model> serviceTypeList;
    private String name = "";
    private FilesAdapter filesAdapter;
    private String categoryId = "", approval = "", distGrpERP = "";
    private int typeUpdatePos = -1, freezerStaApproval;
    private ApproveOutletsDetailedActivity.Category_Adapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_approve_outlets_detailed);
            mAddNewRetailer = this;
            context = this;

            CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
            UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

            Shared_Common_Pref.Outletlat = 0.0;
            Shared_Common_Pref.Outletlong = 0.0;

            db = new DatabaseHandler(this);
            linReatilerRoute = findViewById(R.id.rl_route);
            rlDistributor = findViewById(R.id.rl_Distributor);
            txtRetailerRoute = findViewById(R.id.retailer_type);
            distributor_text = findViewById(R.id.distributor_text);
            retailercode = findViewById(R.id.retailercode);
            common_class = new Common_Class(this);
            shared_common_pref = new Shared_Common_Pref(this);
            CurrentLocLin = findViewById(R.id.CurrentLocLin);
            retailercodevisible = findViewById(R.id.retailercodevisible);
            CurrentLocationsAddress = findViewById(R.id.CurrentLocationsAddress);
            copypaste = findViewById(R.id.copypaste);
            ivCapture = findViewById(R.id.ivRetailCapture);
            ivFreezerCapture = findViewById(R.id.ivFreezerCapture);
            rvFiles = findViewById(R.id.rvFiles);
            rvFreezerFiles = findViewById(R.id.rvFreezerFiles);
            rvCategoryTypes = findViewById(R.id.rvCategoryTypes);
            edt_gst = findViewById(R.id.edt_gst);
            headtext = findViewById(R.id.headtext);
            addRetailerName = findViewById(R.id.edt_new_name);
            owner_name = findViewById(R.id.owner_name);
            addRetailerAddress = findViewById(R.id.edt_new_address);
            addRetailerCity = findViewById(R.id.edt_new_city);
            addRetailerPhone = findViewById(R.id.edt_new_phone);
            addRetailerEmail = findViewById(R.id.edt_new_email);
            etDistrict = findViewById(R.id.edt_district);
            edt_pin_codeedit = findViewById(R.id.edt_pin_code);
            edtDistCode = findViewById(R.id.edt_dist_code);
            edtDepositAmt = findViewById(R.id.edt_depositAmt);

            editDetails = findViewById(R.id.edit_details);

            llUpdate = findViewById(R.id.llUpdateParent);
            llUpdate.setVisibility(View.GONE);

            llApprove = findViewById(R.id.llApprovParent);

            updateButton = findViewById(R.id.updateBtn);

            linClsRmks = findViewById(R.id.linClsRmks);
            edtClsRetRmk = findViewById(R.id.edtClsRetRmk);
            edtFSSAI = findViewById(R.id.edt_retailer_fssai);
            edtPAN = findViewById(R.id.edt_retailer_pan);
            edtFreezerMake = findViewById(R.id.edt_retailer_freezerMake);
            edtFreezerTag = findViewById(R.id.edt_retailer_freezerTagNo);
            tvFreezerCapacity = findViewById(R.id.txFreezerCapacity);
            tvFreezerSta = findViewById(R.id.txFreezerStatus);
            tvSubCategory = findViewById(R.id.tvSubCategory);

            rlDelvryType = findViewById(R.id.rlDelvryType);
            txDelvryType = findViewById(R.id.txDelvryType);
            rlOutletType = findViewById(R.id.rlOutletType);
            txOutletType = findViewById(R.id.txOutletType);
            rlState = findViewById(R.id.rl_state);
            tvStateName = findViewById(R.id.tvState);
            linReatilerChannel = findViewById(R.id.linear_retailer_channel);
            txtRetailerChannel = findViewById(R.id.txt_retailer_channel);
            rlSubCategory = findViewById(R.id.linear_retailer_subCategory);

            ivPhotoShop = findViewById(R.id.ivShopPhoto);
            ivProfilePreview = findViewById(R.id.ivProfileView);
            //mSubmit = findViewById(R.id.submit_button);
            etPhoneNo2 = findViewById(R.id.edt_new_phone2);
            edt_outstanding = findViewById(R.id.edt_retailer_outstanding);
            btnRefLoc = findViewById(R.id.btnRefLoc);
            linServiceType = findViewById(R.id.linear_service_type);
            rlFreezerCapacity = findViewById(R.id.rlFreezerCapacity);
            rlFreezerSta = findViewById(R.id.rlFreezerStatus);
            tvServiceType = findViewById(R.id.txt_service_type);
            btnDistCode = findViewById(R.id.btn_dist_enter);
            edtExpcSalVal = findViewById(R.id.edt_expectSaleVal);
            cbFranchise = findViewById(R.id.cbFranchise);
            cbFreezerYes = findViewById(R.id.cbFreezerYes);
            cbFreezerNo = findViewById(R.id.cbFreezerNo);
            linReatilerClass = findViewById(R.id.linear_retailer_class);
            txtRetailerClass = findViewById(R.id.txt_retailer_class);

            linServiceType.setOnClickListener(this);
            ivCapture.setOnClickListener(this);
            ivFreezerCapture.setOnClickListener(this);
            btnDistCode.setOnClickListener(this);
            rlFreezerCapacity.setOnClickListener(this);
            rlFreezerSta.setOnClickListener(this);
            linReatilerClass.setOnClickListener(this);
            linReatilerChannel.setOnClickListener(this);
            rlSubCategory.setOnClickListener(this);
            ivProfilePreview.setOnClickListener(this);

            approveBtn = findViewById(R.id.approveBtn);
            rejectBtn = findViewById(R.id.rejectBtn);

            MakeEditable();

            editDetails.setOnClickListener(v -> {
                if (editMode) {
                    editMode = false;
                    editDetails.setText("Edit");
                    llApprove.setVisibility(View.VISIBLE);
                    llUpdate.setVisibility(View.GONE);
                    MakeEditable();

                    distributor_text.setTextColor(getResources().getColor(R.color.disabled));
                    txOutletType.setTextColor(getResources().getColor(R.color.disabled));
                    retailercode.setTextColor(getResources().getColor(R.color.disabled));
                    txtRetailerRoute.setTextColor(getResources().getColor(R.color.disabled));
                    tvStateName.setTextColor(getResources().getColor(R.color.disabled));
                    txDelvryType.setTextColor(getResources().getColor(R.color.disabled));
                    tvFreezerSta.setTextColor(getResources().getColor(R.color.disabled));
                    tvFreezerCapacity.setTextColor(getResources().getColor(R.color.disabled));

                } else {
                    editMode = true;
                    editDetails.setText("Cancel");
                    llApprove.setVisibility(View.GONE);
                    llUpdate.setVisibility(View.VISIBLE);
                    MakeEditable();

                    distributor_text.setTextColor(getResources().getColor(R.color.black));
                    txOutletType.setTextColor(getResources().getColor(R.color.black));
                    retailercode.setTextColor(getResources().getColor(R.color.black));
                    txtRetailerRoute.setTextColor(getResources().getColor(R.color.black));
                    tvStateName.setTextColor(getResources().getColor(R.color.black));
                    txDelvryType.setTextColor(getResources().getColor(R.color.black));
                    tvFreezerSta.setTextColor(getResources().getColor(R.color.black));
                    tvFreezerCapacity.setTextColor(getResources().getColor(R.color.black));
                }
            });

            approveBtn.setOnClickListener(v -> {
                UpdateData("", "0");
            });

            rejectBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(ApproveOutletsDetailedActivity.this);
                View view = LayoutInflater.from(ApproveOutletsDetailedActivity.this).inflate(R.layout.layout_get_reason, null);
                builder.setView(view);
                AlertDialog dialog = builder.create();
                EditText editText = view.findViewById(R.id.inputMessage);
                TextView cancel = view.findViewById(R.id.cancel);
                TextView submit = view.findViewById(R.id.submit);
                cancel.setOnClickListener(v1 -> {
                    dialog.dismiss();
                });
                submit.setOnClickListener(v2 -> {
                    String message = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(message)) {
                        Toast.makeText(context, "Reason for Rejection Required", Toast.LENGTH_SHORT).show();
                    } else {
                        UpdateData(message, "1");
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);
                dialog.show();

            });

            getDataFromDatabase();

            findViewById(R.id.ivFreezReqMandatory).setVisibility(View.INVISIBLE);

            if (shared_common_pref.getIntValue(Constants.Freezer_Mandatory) == 1)
                findViewById(R.id.ivFreezReqMandatory).setVisibility(View.VISIBLE);


            distGrpERP = shared_common_pref.getvalue(Constants.CusSubGrpErp);


            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.route_map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

            deliveryTypeList = new ArrayList<>();
            mCommon_model_spinner = new Common_Model("AC", "AC", "flag");
            deliveryTypeList.add(mCommon_model_spinner);
            mCommon_model_spinner = new Common_Model("OT", "Others", "flag");
            deliveryTypeList.add(mCommon_model_spinner);

            rlDelvryType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    common_class.showCommonDialog(deliveryTypeList, 11, ApproveOutletsDetailedActivity.this);
                }
            });

            outletTypeList = new ArrayList<>();
            mCommon_model_spinner = new Common_Model("1", "Service", "flag");
            outletTypeList.add(mCommon_model_spinner);
            mCommon_model_spinner = new Common_Model("0", "Non Service", "flag");
            outletTypeList.add(mCommon_model_spinner);
            mCommon_model_spinner = new Common_Model("2", "Closed", "flag");
            outletTypeList.add(mCommon_model_spinner);
            mCommon_model_spinner = new Common_Model("3", "Duplicate", "flag");
            outletTypeList.add(mCommon_model_spinner);

//            serviceTypeList = new ArrayList<>();
//            serviceTypeList.add(new Common_Model("-18", "1", false, "", "", "", ""));
//            serviceTypeList.add(new Common_Model("+4", "2", false, "", "", "", ""));
//            serviceTypeList.add(new Common_Model("Ambient", "3", false, "", "", "", ""));
//            //serviceTypeList.add(new Common_Model("B&C", "4", false, "", "", "", ""));
//
//
//            categoryAdapter = new Category_Adapter(serviceTypeList, R.layout.adapter_retailer_category_types, AddNewRetailer.this);
//            rvCategoryTypes.setAdapter(categoryAdapter);


            rlOutletType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    common_class.showCommonDialog(outletTypeList, 13, ApproveOutletsDetailedActivity.this);
                }
            });
            copypaste.setOnClickListener(this);
            ivPhotoShop.setOnClickListener(this);
            rlState.setOnClickListener(this);
            btnRefLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                new LocationFinder(getApplication(), new LocationEvents() {
                                    @Override
                                    public void OnLocationRecived(Location location) {
                                        try {
                                            if (location == null) {
                                                Toast.makeText(ApproveOutletsDetailedActivity.this, "Location Can't Getting Location. Try Again.", Toast.LENGTH_LONG).show();
                                                //btnRefLoc.doneLoadingAnimation(getResources().getColor(R.color.color_red), BitmapFactory.decodeResource(getResources(), R.drawable.ic_wrong));
                                                return;
                                            } else {
                                                refreshLocation(location);
                                            }
                                        } catch (Exception e) {
                                            Log.v(TAG, "LOC1:" + e.getMessage());
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                Log.v(TAG, "LOC3:" + e.getMessage());
                            }
                        }
                    }, 100);

                }

            });


            gson = new Gson();
            outletCode = Shared_Common_Pref.OutletCode;


            divERP = shared_common_pref.getvalue(Constants.DivERP);

            service = ApiClient.getClient().create(ApiInterface.class);


            String OrdersTable = shared_common_pref.getvalue(Constants.Retailer_OutletList);

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryType = "";
                    for (int i = 0; i < serviceTypeList.size(); i++) {
                        if (serviceTypeList.get(i).isSelected())
                            categoryType = categoryType + serviceTypeList.get(i).getName() + ",";
                    }
                    if (cbFranchise.isChecked() && !isValidCode) {
                        if (btnDistCode.getText().toString().equalsIgnoreCase("Check Validity"))
                            common_class.showMsg(ApproveOutletsDetailedActivity.this, "Please Check validity for the Franchise Code");
                        else
                            common_class.showMsg(ApproveOutletsDetailedActivity.this, "Please given valid Franchise Code or Uncheck the " + HAPApp.Title + " Franchise");
                    } else if (Common_Class.isNullOrEmpty(txOutletType.getText().toString())) {
                        common_class.showMsg(ApproveOutletsDetailedActivity.this, "Select Outlet Type");
                    } else if (iOutletTyp == 2) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                addNewRetailers();
                            }
                        }, 500);
                    } else if (txtRetailerRoute.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Select route", Toast.LENGTH_SHORT).show();
                    } else if (addRetailerName.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Enter Outlet Name", Toast.LENGTH_SHORT).show();
                    } else if (owner_name.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Enter the owner Name", Toast.LENGTH_SHORT).show();
                    } else if (String.valueOf(Shared_Common_Pref.Outletlat).matches("")
                            || String.valueOf(Shared_Common_Pref.Outletlat).matches("0")
                            || String.valueOf(Shared_Common_Pref.Outletlat).matches("0.0")) {
                        Toast.makeText(getApplicationContext(), "Refresh the Location Lat & Lng", Toast.LENGTH_SHORT).show();
                    } else if (addRetailerAddress.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Enter Address", Toast.LENGTH_SHORT).show();
                    } else if (tvStateName.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Select the State", Toast.LENGTH_SHORT).show();
                    } else if (addRetailerCity.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Enter Location", Toast.LENGTH_SHORT).show();
                    } else if (etDistrict.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Enter District", Toast.LENGTH_SHORT).show();
                    } else if (addRetailerPhone.getText().toString().length() != 10) {
                        Toast.makeText(getApplicationContext(), "Enter 10 digit phone number", Toast.LENGTH_SHORT).show();
                    }
//                    else if (txtRetailerClass.getText().toString().matches("")) {
//                        Toast.makeText(getApplicationContext(), "Select the Outlet Type", Toast.LENGTH_SHORT).show();
//                    }
//                    else if (!divERP.equalsIgnoreCase("21") && txtRetailerChannel.getText().toString().equalsIgnoreCase("")) {
//                        Toast.makeText(getApplicationContext(), "Select the Outlet Category", Toast.LENGTH_SHORT).show();
//                    } else if (!divERP.equalsIgnoreCase("21") && tvSubCategory.getText().toString().equalsIgnoreCase("")) {
//                        Toast.makeText(getApplicationContext(), "Select the Sub Category", Toast.LENGTH_SHORT).show();
//                    }


                    else if (txDelvryType.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Select the Delivery Type", Toast.LENGTH_SHORT).show();
                    } else if (iOutletTyp == 2 && edtClsRetRmk.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Enter the reason for close outlet", Toast.LENGTH_SHORT).show();
                        linClsRmks.requestFocus();
                    } else if (imageConvert.equals("") && name.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please take picture", Toast.LENGTH_SHORT).show();

                    } else if (/*divERP.equalsIgnoreCase("21") &&*/ categoryType.equals("")) {
                        common_class.showMsg(ApproveOutletsDetailedActivity.this, "Select the Category Type");
                    } else if (shared_common_pref.getIntValue(Constants.Freezer_Mandatory) == 1 && !cbFreezerYes.isChecked() && !cbFreezerNo.isChecked()) {
                        common_class.showMsg(ApproveOutletsDetailedActivity.this, "Check the Freezer/Cooler Required");

                    } else if (/*divERP.equalsIgnoreCase("21") && */cbFreezerYes.isChecked()) {
                        if (tvFreezerSta.getText().toString().equalsIgnoreCase("")) {
                            common_class.showMsg(ApproveOutletsDetailedActivity.this, "Selet the Freezer/Cooler Status");
                        } else if (edtFreezerMake.getText().toString().equalsIgnoreCase(""))
                            common_class.showMsg(ApproveOutletsDetailedActivity.this, "Enter the Freezer/Cooler make");

                        else if (!tvFreezerSta.getText().toString().equalsIgnoreCase("Own Freezer") && edtFreezerTag.getText().toString().length() != 13) {
                            common_class.showMsg(ApproveOutletsDetailedActivity.this, "Enter the 13 digits Freezer/Cooler Tag Number");
                        } else if (tvFreezerCapacity.getText().toString().equalsIgnoreCase("")) {
                            common_class.showMsg(ApproveOutletsDetailedActivity.this, "Select the Freezer/Cooler Capacity");
                        } else if (!tvFreezerSta.getText().toString().equalsIgnoreCase("Own Freezer") && (mFreezerData == null || mFreezerData.size() == 0 || mFreezerData.get(0).getFileUrls() == null || mFreezerData.get(0).getFileUrls().size() == 0))
                            common_class.showMsg(ApproveOutletsDetailedActivity.this, "Please take Freezer/Cooler Photo");
                        else {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    addNewRetailers();
                                }
                            }, 500);
                        }
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                addNewRetailers();
                            }
                        }, 500);
                    }

                }
            });

        } catch (Exception e) {
            Log.e(TAG + "catch:", e.getMessage());

        }


    }

    private void MakeEditable() {
        distributor_text.setEnabled(editMode);
        cbFranchise.setEnabled(editMode);
        edtDistCode.setEnabled(editMode);
        addRetailerName.setEnabled(editMode);
        txOutletType.setEnabled(editMode);
        retailercode.setEnabled(editMode);
        txtRetailerRoute.setEnabled(editMode);
        owner_name.setEnabled(editMode);
        btnRefLoc.setEnabled(editMode);
        addRetailerAddress.setEnabled(editMode);
        tvStateName.setEnabled(editMode);
        addRetailerCity.setEnabled(editMode);
        etDistrict.setEnabled(editMode);
        edt_pin_codeedit.setEnabled(editMode);
        edt_gst.setEnabled(editMode);
        addRetailerPhone.setEnabled(editMode);
        etPhoneNo2.setEnabled(editMode);
        addRetailerEmail.setEnabled(editMode);
        txDelvryType.setEnabled(editMode);
        cbFreezerYes.setEnabled(editMode);
        cbFreezerNo.setEnabled(editMode);
        tvFreezerSta.setEnabled(editMode);
        edtExpcSalVal.setEnabled(editMode);
        edtDepositAmt.setEnabled(editMode);
        edtFreezerMake.setEnabled(editMode);
        edtFreezerTag.setEnabled(editMode);
        tvFreezerCapacity.setEnabled(editMode);
        edtFSSAI.setEnabled(editMode);
        edtPAN.setEnabled(editMode);
        edt_outstanding.setEnabled(editMode);
        edtClsRetRmk.setEnabled(editMode);
        btnDistCode.setEnabled(editMode);

        rlDelvryType.setEnabled(editMode);
        linReatilerClass.setEnabled(editMode);
        linReatilerChannel.setEnabled(editMode);
        rlSubCategory.setEnabled(editMode);
        linServiceType.setEnabled(editMode);
        rlFreezerCapacity.setEnabled(editMode);
        rlFreezerSta.setEnabled(editMode);
        rlState.setEnabled(editMode);
        linReatilerRoute.setEnabled(editMode);
        rlDistributor.setEnabled(editMode);
        rlOutletType.setEnabled(editMode);

        ivPhotoShop.setEnabled(editMode);
        ivFreezerCapture.setEnabled(editMode);

    }

    private void UpdateData(String message, String type) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String listedDrCode = getIntent().getStringExtra("ListedDrCode");
        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(context);
        String sfCode = shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code, "");
        Map<String, String> params = new HashMap<>();
        params.put("type", type); // 0 means approve, 1 means reject, 2 means pending
        params.put("listedDrCode", listedDrCode);
        params.put("ModifiedBy", sfCode);
        params.put("remarks", message);
        params.put("ModifiedOn", new Common_Class(context).GetDatemonthyearTimeformat());
        Log.e("status", params.toString());
        Call<ResponseBody> call = apiInterface.setOutletStatus("set_outlet_status", params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject js = new JSONObject(response.body().string());
                        if (js.getBoolean("success")) {
                            if (type.equals("0")) {
                                Toast.makeText(context, "Outlet Approved Successfully", Toast.LENGTH_SHORT).show();
                            } else if (type.equals("1")) {
                                Toast.makeText(context, "Outlet Rejected Successfully", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                            ApproveOutletsActivity.refresh = true;
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Error: Response not successfull", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void getDataFromDatabase() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getDataPendingOutlets("get_data_of_pending_outlet", getIntent().getStringExtra("ListedDrCode"));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();

                        Log.e("status", result);

                        userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
                        }.getType();
                        Retailer_Modal_List = gson.fromJson(result, userType);

                        distributor_text.setText(shared_common_pref.getvalue(Constants.Distributor_name));
                        txtRetailerRoute.setText(shared_common_pref.getvalue(Constants.Route_name));
                        routeId = shared_common_pref.getvalue(Constants.Route_Id);

                        if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                            //mSubmit.setVisibility(View.VISIBLE);
                            CurrentLocLin.setVisibility(View.GONE);
                            retailercodevisible.setVisibility(View.GONE);
                            CurrentLocationsAddress.setVisibility(View.GONE);
                            //   routeId = shared_common_pref.getvalue("RouteSelect");
                            CurrentLocationsAddress.setText("" + Shared_Common_Pref.OutletAddress);
                            // getCompleteAddressString(Shared_Common_Pref.Outletlat, Shared_Common_Pref.Outletlong);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        new LocationFinder(getApplication(), new LocationEvents() {
                                            @Override
                                            public void OnLocationRecived(Location location) {
                                                try {
                                                    if (location == null) {
                                                        Toast.makeText(ApproveOutletsDetailedActivity.this, "Location Can't Getting Location. Try Again.", Toast.LENGTH_LONG).show();
                                                        //btnRefLoc.doneLoadingAnimation(getResources().getColor(R.color.color_red), BitmapFactory.decodeResource(getResources(), R.drawable.ic_wrong));
                                                        return;
                                                    } else {
                                                        refreshLocation(location);
                                                    }
                                                } catch (Exception e) {
                                                    Log.v(TAG, "LOC1:" + e.getMessage());
                                                }
                                            }
                                        });
                                    } catch (Exception e) {
                                        Log.v(TAG, "LOC3:" + e.getMessage());
                                    }
                                }
                            }, 100);

                            headtext.setText("Create Outlet");
                        } else {
                            retailercodevisible.setVisibility(View.VISIBLE);
                            CurrentLocLin.setVisibility(View.GONE);
                            CurrentLocationsAddress.setVisibility(View.GONE);
                            Shared_Common_Pref.Outler_AddFlag = "0";
                        }
                        if (Shared_Common_Pref.Outlet_Info_Flag != null && Shared_Common_Pref.Outlet_Info_Flag.equals("1")) {
                            //mSubmit.setVisibility(View.GONE);
                            headtext.setText("Approve/Reject Outlet");
                        }
                        //  getRouteDetails();
//            getRetailerClass();
//            getRetailerChannel();
                        // getServiceTypes(shared_common_pref.getvalue(Constants.Distributor_Id));

                        if (Shared_Common_Pref.Editoutletflag != null && Shared_Common_Pref.Editoutletflag.equals("1") || (Shared_Common_Pref.Outlet_Info_Flag != null && Shared_Common_Pref.Outlet_Info_Flag.equals("1"))) {

                            iOutletTyp = Retailer_Modal_List.get(getOutletPosition()).getType() == null ? 0 : Integer.valueOf(Retailer_Modal_List.get(getOutletPosition()).getType());
                            switch (iOutletTyp) {
                                case 0:
                                    txOutletType.setText("Non Service");
                                    break;
                                case 2:
                                    txOutletType.setText("Closed");
                                    break;
                                case 3:
                                    txOutletType.setText("Duplicate");
                                    break;
                                default:
                                    txOutletType.setText("Service");
                                    break;
                            }

                            txDelvryType.setText(Retailer_Modal_List.get(getOutletPosition()).getDelivType());
                        }

                        TextView txtHelp = findViewById(R.id.toolbar_help);
                        ImageView imgHome = findViewById(R.id.toolbar_home);
                        txtHelp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
                            }
                        });
                        TextView txtErt = findViewById(R.id.toolbar_ert);
                        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);
                        txtErt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getApplicationContext(), ERT.class));
                            }
                        });
                        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        ObjectAnimator textColorAnim;
                        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
                        textColorAnim.setDuration(500);
                        textColorAnim.setEvaluator(new ArgbEvaluator());
                        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
                        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
                        textColorAnim.start();
                        imgHome.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                                if (CheckIn == true) {
                                    common_class.CommonIntentwithoutFinish(SFA_Activity.class);
                                } else
                                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            }
                        });

                        addRetailerName.clearFocus();
                        Type commonType = new TypeToken<ArrayList<Common_Model>>() {
                        }.getType();

                        if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.STATE_LIST)))
                            common_class.getDb_310Data(Constants.STATE_LIST, ApproveOutletsDetailedActivity.this);
                        else {
                            stateList = gson.fromJson(shared_common_pref.getvalue(Constants.STATE_LIST), commonType);
                        }

                        Intent i = getIntent();
                        Log.e(TAG + "1:", Shared_Common_Pref.Outler_AddFlag);
                        if (i != null && i.getExtras() != null) {

                            if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                                Compititor_Id = i.getExtras().getString("Compititor_Id");
                                Compititor_Name = i.getExtras().getString("Compititor_Name");
                                CatUniverSelectId = i.getExtras().getString("CatUniverSelectId");
                                AvailUniverSelectId = i.getExtras().getString("AvailUniverSelectId");
                                reason_category_remarks = i.getExtras().getString("reason_category");
                                HatsunAvailswitch = i.getExtras().getString("HatsunAvailswitch");
                                categoryuniverseswitch = i.getExtras().getString("categoryuniverseswitch");
                                Log.e("HatsunAvailswitch", "" + HatsunAvailswitch);
                                Log.e("categoryuniverseswitch", "" + categoryuniverseswitch);
                                Log.e("reason_category", "" + reason_category_remarks);
                                Log.e("CatUniverSelectId", "" + CatUniverSelectId);
                                Log.e("AvailUniverSelectId", "" + AvailUniverSelectId);
                                Log.e("Compititor_Name", "" + Compititor_Name);
                                //The key argument here must match that used in the other activity
                            } else {

                                if (getOutletPosition() >= 0)
                                    assignData();
                            }

                        }
                        Log.e(TAG + "1:1", Shared_Common_Pref.Outler_AddFlag);

                        getFreezerData(divERP);
                        if (Shared_Common_Pref.Editoutletflag != null && Shared_Common_Pref.Editoutletflag.equals("1")) {
                            //mSubmit.setVisibility(View.VISIBLE);
                            addRetailerName.setText("" + Retailer_Modal_List.get(getOutletPosition()).getName());
                            addRetailerAddress.setText("" + Retailer_Modal_List.get(getOutletPosition()).getListedDrAddress1());
                            txtRetailerRoute.setText("" + Retailer_Modal_List.get(getOutletPosition()).getTownName());
                            addRetailerPhone.setText("" + Retailer_Modal_List.get(getOutletPosition()).getPrimary_No());
                            retailercode.setText("" + Retailer_Modal_List.get(getOutletPosition()).getERP_Code());
                            routeId = Retailer_Modal_List.get(getOutletPosition()).getTownCode();

                            if (!Common_Class.isNullOrEmpty(Retailer_Modal_List.get(getOutletPosition()).getCustomerCode())) {
                                cbFranchise.setChecked(true);
                                edtDistCode.setText("" + Retailer_Modal_List.get(getOutletPosition()).getCustomerCode());
                                btnDistCode.setText("Valid Code");
                                isValidCode = true;
                                customer_code = Retailer_Modal_List.get(getOutletPosition()).getCustomerCode();
                                findViewById(R.id.llFranchiseCode).setVisibility(View.VISIBLE);
                                findViewById(R.id.llFranchiseCode).setEnabled(false);
                            }
                            ArrayList<Retailer_Modal_List.CateSpecList> CatSubList = Retailer_Modal_List.get(getOutletPosition()).getCategoryList();
                            for (int ik = 0; ik < serviceTypeList.size(); ik++) {
                                for (int ij = 0; ij < CatSubList.size(); ij++) {
                                    if (CatSubList.get(ij).OutletCat_Type.equalsIgnoreCase(serviceTypeList.get(ik).getName())) {
                                        serviceTypeList.get(ik).setSelected(true);
                                        serviceTypeList.get(ik).setCatId(CatSubList.get(ij).Category_Code);
                                        serviceTypeList.get(ik).setSubCatId(CatSubList.get(ij).Sub_Category_Code);
                                        serviceTypeList.get(ik).setCatName(CatSubList.get(ij).Category_Name);
                                        serviceTypeList.get(ik).setSubCatName(CatSubList.get(ij).Sub_Category_Name);

                                    }
                                }
                            }

                            shared_common_pref.save(Constants.SERVICETYPE_LIST, gson.toJson(serviceTypeList));
                            categoryAdapter.notifyData(serviceTypeList, ApproveOutletsDetailedActivity.this);
                            edtExpcSalVal.setText(Retailer_Modal_List.get(getOutletPosition()).getExpected_sales_value());
                            edtDepositAmt.setText(Retailer_Modal_List.get(getOutletPosition()).getDeposit_amount());
                            edtFSSAI.setText(Retailer_Modal_List.get(getOutletPosition()).getFssiNo());
                            edtExpcSalVal.setText(Retailer_Modal_List.get(getOutletPosition()).getExpected_sales_value());
                            edtPAN.setText(Retailer_Modal_List.get(getOutletPosition()).getPan_No());

                            edtFreezerMake.setText(Retailer_Modal_List.get(getOutletPosition()).getFreezer_make());
                            edtFreezerTag.setText(Retailer_Modal_List.get(getOutletPosition()).getFreezer_Tag_no());
                            tvFreezerSta.setText(Retailer_Modal_List.get(getOutletPosition()).getFreezer_status());
                            tvFreezerCapacity.setText(Retailer_Modal_List.get(getOutletPosition()).getFreezer_capacity());
                            //freezerStaId
                            String FreReq = Retailer_Modal_List.get(getOutletPosition()).getFreezer_required();
                            cbFreezerYes.setChecked(false);
                            if (FreReq.equalsIgnoreCase("yes")) {
                                cbFreezerNo.setChecked(false);
                                cbFreezerYes.setChecked(true);
                                findViewById(R.id.llFreezer).setVisibility(View.VISIBLE);
                            }
                            updateView("", false);
                            if (!Common_Class.isNullOrEmpty(Retailer_Modal_List.get(getOutletPosition()).getLat()))
                                RetLat = Double.parseDouble(Retailer_Modal_List.get(getOutletPosition()).getLat());
                            if (!Common_Class.isNullOrEmpty(Retailer_Modal_List.get(getOutletPosition()).getLong()))
                                RetLng = Double.parseDouble(Retailer_Modal_List.get(getOutletPosition()).getLong());

                            Shared_Common_Pref.Outletlat = RetLat;
                            Shared_Common_Pref.Outletlong = RetLng;
                            centreMapOnLocation("Your Location");
                            String[] filelst = Retailer_Modal_List.get(getOutletPosition()).getFreezer_attachments().split(",");

                            mFreezerData.clear();
                            mFreezerData = new ArrayList<>();
                            mFreezerData.add(new QPS_Modal("", "", ""));
                            List<String> jAryDta = new ArrayList<>();
                            for (int il = 0; il < filelst.length; il++) {
                                if (!filelst[il].equalsIgnoreCase("")) {
                                    String sname = ApiClient.BASE_URL + "FreezerImages/" + filelst[il];
                                    sname = sname.replaceAll("server/", "");
                                    jAryDta.add(sname);

                                }

                            }

                            mFreezerData.get(0).setFileUrls(jAryDta);

                            if (Retailer_Modal_List.get(getOutletPosition()).getFreezer_status().equalsIgnoreCase("Company Provided"))
                                findViewById(R.id.llExpecSalVal).setVisibility(View.VISIBLE);
                            else
                                findViewById(R.id.llExpecSalVal).setVisibility(View.GONE);
                            filesAdapter = new FilesAdapter(jAryDta, R.layout.adapter_local_files_layout, ApproveOutletsDetailedActivity.this);
                            rvFreezerFiles.setAdapter(filesAdapter);
                            if (Retailer_Modal_List.get(getOutletPosition()).getCityname() != null)
                                addRetailerCity.setText("" + Retailer_Modal_List.get(getOutletPosition()).getCityname());
                            if (Retailer_Modal_List.get(getOutletPosition()).getListedDr_Email() != null)
                                addRetailerEmail.setText("" + Retailer_Modal_List.get(getOutletPosition()).getListedDr_Email());
                            if (Retailer_Modal_List.get(getOutletPosition()).getOwner_Name() != null)
                                owner_name.setText("" + Retailer_Modal_List.get(getOutletPosition()).getOwner_Name());
                            if (Retailer_Modal_List.get(getOutletPosition()).getDistrictname() != null)
                                etDistrict.setText("" + Retailer_Modal_List.get(getOutletPosition()).getDistrictname());
                            edt_pin_codeedit.setText("" + Retailer_Modal_List.get(getOutletPosition()).getPin_code());
                            edt_gst.setText("" + Retailer_Modal_List.get(getOutletPosition()).getGst());
                            //  txtRetailerClass.setText("" + Retailer_Modal_List.get(getOutletPosition()).getClass());

                            if (i != null && i.getExtras() != null) {
                                if (i.getExtras().getString("Compititor_Id") != null)
                                    Compititor_Id = i.getExtras().getString("Compititor_Id");
                                if (i.getExtras().getString("Compititor_Name") != null)
                                    Compititor_Name = i.getExtras().getString("Compititor_Name");
                                if (i.getExtras().getString("CatUniverSelectId") != null)
                                    CatUniverSelectId = i.getExtras().getString("CatUniverSelectId");
                                if (i.getExtras().getString("AvailUniverSelectId") != null)
                                    AvailUniverSelectId = i.getExtras().getString("AvailUniverSelectId");
                                if (i.getExtras().getString("reason_category") != null)
                                    reason_category_remarks = i.getExtras().getString("reason_category");
                                if (i.getExtras().getString("HatsunAvailswitch") != null)
                                    HatsunAvailswitch = i.getExtras().getString("HatsunAvailswitch");
                                if (i.getExtras().getString("categoryuniverseswitch") != null)
                                    categoryuniverseswitch = i.getExtras().getString("categoryuniverseswitch");
                            }


                            if (Retailer_Modal_List.get(getOutletPosition()).getSpeciality() != null) {
                                tvSubCategory.setText("" + Retailer_Modal_List.get(getOutletPosition()).getSpeciality());
                                categoryId = "" + Retailer_Modal_List.get(getOutletPosition()).getDocSpecialCode();
                                Log.v("categoryId:", categoryId + ":" + Retailer_Modal_List.get(getOutletPosition()).getSpeciality());
                            }


                            if (Retailer_Modal_List.get(getOutletPosition()).getOutletClass() != null && !Retailer_Modal_List.get(getOutletPosition()).getOutletClass().equalsIgnoreCase("B")) {
                                txtRetailerChannel.setText("" + Retailer_Modal_List.get(getOutletPosition()).getOutletClass());
                                if (Retailer_Modal_List.get(getOutletPosition()).getDocCatCode() != null)
                                    channelID = Retailer_Modal_List.get(getOutletPosition()).getDocCatCode();

                                Log.v("categorySubId:", "" + channelID + ": " + Retailer_Modal_List.get(getOutletPosition()).getOutletClass());

                            }


                        }
                        Log.e(TAG + "2:", Shared_Common_Pref.Outler_AddFlag);

                        /**/
                        Log.e(TAG + "3:", Shared_Common_Pref.Outler_AddFlag);

                        String placeIdData = getIntent().getStringExtra(Constants.PLACE_ID);
                        if (placeIdData != null) {
                            //  Nearby_Outlets.

                            JSONObject jsonObject = new JSONObject(placeIdData);

                            JSONObject jsonResult = jsonObject.getJSONObject("result");
                            addRetailerPhone.setText("" + jsonResult.optString("formatted_phone_number"));
                            addRetailerAddress.setText("" + jsonResult.optString("vicinity"));
                            addRetailerName.setText("" + jsonResult.getString("name"));

                            place_id = jsonResult.getString("place_id");

                            Log.e(TAG, "Address:" + jsonObject.optString("formatted_address"));

                            JSONArray addressJsonArray = jsonResult.getJSONArray("address_components");
                            for (int addressIdex = 0; addressIdex < addressJsonArray.length(); addressIdex++) {

                                JSONObject jsonAddressObj = addressJsonArray.getJSONObject(addressIdex);

                                JSONArray typesArray = addressJsonArray.getJSONObject(addressIdex).getJSONArray("types");

                                for (int typesIndex = 0; typesIndex < typesArray.length(); typesIndex++) {

                                    if (typesArray.get(typesIndex).equals("postal_code")) {
                                        edt_pin_codeedit.setText("" + jsonAddressObj.optString("long_name"));
                                    }

                                    if (typesArray.get(typesIndex).equals("locality")) {

                                        addRetailerCity.setText("" + jsonAddressObj.optString("long_name"));
                                    }
                                }
                            }
                        }
                        Log.e(TAG + "4:", Shared_Common_Pref.Outler_AddFlag);

                        shared_common_pref.save(Constants.Retailor_FilePath, "");

                        linReatilerRoute.setOnClickListener(ApproveOutletsDetailedActivity.this);
                        rlDistributor.setOnClickListener(ApproveOutletsDetailedActivity.this);

                        if (Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                            btnDistCode.setVisibility(View.VISIBLE);
                            common_class.getDb_310Data(Rout_List, ApproveOutletsDetailedActivity.this);

                        }

//            if (Shared_Common_Pref.Outler_AddFlag.equals("1") || divERP.equalsIgnoreCase("21") || divERP.equalsIgnoreCase("62")) {
//                linReatilerRoute.setEnabled(true);
//            } else {
//                linReatilerRoute.setEnabled(false);
//            }


                        // if (shared_common_pref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
//                if (Shared_Common_Pref.Outler_AddFlag != null && !Shared_Common_Pref.Outler_AddFlag.equals("1"))
//                    mSubmit.setVisibility(View.GONE);

//                rlDistributor.setEnabled(false);
//                findViewById(R.id.ivDistSpinner).setVisibility(View.GONE);
//            }


                        mData.add(new QPS_Modal("", "", ""));
                        mFreezerData.add(new QPS_Modal("", "", ""));

                        Log.e(TAG + "5:", Shared_Common_Pref.Outler_AddFlag);

                        if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Freezer_capacity)))
                            common_class.getDb_310Data(Freezer_capacity, ApproveOutletsDetailedActivity.this);
                        else {
                            JSONObject capObj = new JSONObject(shared_common_pref.getvalue(Freezer_capacity));
                            if (capObj.getBoolean("success")) {
                                JSONArray arr = capObj.getJSONArray("Data");
                                for (int c = 0; c < arr.length(); c++) {
                                    JSONObject obj = arr.getJSONObject(c);
                                    freezerCapcityList.add(new Common_Model(obj.getString("FCapacity"), obj.getString("ID")));
                                }
                            }
                        }
                        Log.e(TAG + "6:", Shared_Common_Pref.Outler_AddFlag);

                        if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Freezer_Status)))
                            common_class.getDb_310Data(Freezer_Status, ApproveOutletsDetailedActivity.this);
                        else {
                            JSONObject staObj = new JSONObject(shared_common_pref.getvalue(Freezer_Status));
                            if (staObj.getBoolean("success")) {
                                JSONArray arr = staObj.getJSONArray("Data");
                                for (int s = 0; s < arr.length(); s++) {
                                    JSONObject obj = arr.getJSONObject(s);
                                    freezerStaList.add(new Common_Model(obj.getString("FStatus"), obj.getString("ID"),
                                            obj.getInt("ApprovalNeed")));
                                }
                            }
                        }
                        Log.e(TAG + "7:", Shared_Common_Pref.Outler_AddFlag);


                        if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.RETAIL_CLASS)))
                            getRetailerClass();
                        else {
                            modelRetailClass = gson.fromJson(shared_common_pref.getvalue(Constants.RETAIL_CLASS), commonType);

                        }
                        Log.e(TAG + "8:", Shared_Common_Pref.Outler_AddFlag);

                        if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.RETAIL_CHANNEL)))//subCategory
                        {
                            getRetailerChannel();

                        } else {
                            modelRetailChannel = gson.fromJson(shared_common_pref.getvalue(Constants.RETAIL_CHANNEL), commonType);
                            String val = shared_common_pref.getvalue(Constants.RETAIL_CHANNEL);
                            Log.v("subcat:", val);
                        }


                        if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.OUTLET_CATEGORY)))
                            common_class.getDb_310Data(Constants.OUTLET_CATEGORY, ApproveOutletsDetailedActivity.this);
                        else {
                            getCategoryList(shared_common_pref.getvalue(OUTLET_CATEGORY));
                        }


                        Log.e(TAG + "9:", Shared_Common_Pref.Outler_AddFlag);


                        Log.e(TAG + "10:", Shared_Common_Pref.Outler_AddFlag);

                        distributorERP = shared_common_pref.getvalue(Constants.DistributorERP);

                        edtDistCode.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                // if (btnDistCode.getText().toString().equalsIgnoreCase("Valid Code")) {
                                btnDistCode.setText("Check Validity");
                                isValidCode = false;
                                // }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });


                        cbFreezerYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    cbFreezerNo.setChecked(false);
                                    findViewById(R.id.llFreezer).setVisibility(View.VISIBLE);


                                } else {
                                    // cbFreezerNo.setChecked(true);
                                    getFreezerData("");

                                    findViewById(R.id.llFreezer).setVisibility(View.GONE);
                                }

                            }
                        });

                        cbFreezerNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    cbFreezerYes.setChecked(false);
                                    findViewById(R.id.llFreezer).setVisibility(View.GONE);

                                    getFreezerData("");
                                } else {
                                    // cbFreezerYes.setChecked(true);
                                    findViewById(R.id.llFreezer).setVisibility(View.VISIBLE);
                                }


                            }
                        });
                        cbFranchise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked)
                                    findViewById(R.id.llFranchiseCode).setVisibility(View.VISIBLE);
                                else
                                    findViewById(R.id.llFranchiseCode).setVisibility(View.GONE);


                            }
                        });

                        String val = getIntent().getStringExtra("approval");
                        Log.v(TAG, "screenname:" + val);
                        if (val != null && val.equalsIgnoreCase("status")) {
                            findViewById(R.id.llApprovParent).setVisibility(View.VISIBLE);
                            //mSubmit.setVisibility(View.GONE);
                            headtext.setText("Outlet Approval");

                        }
                        common_class.getDb_310Data(Constants.Rout_List, ApproveOutletsDetailedActivity.this);
                        shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, shared_common_pref.getvalue(Constants.Distributor_Id));

                    } catch (Exception e) {
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Error: Response Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    void refreshLocation(Location location) {

        btnRefLoc.startAnimation();
        RetLat = location.getLatitude();
        RetLng = location.getLongitude();
        Shared_Common_Pref.Outletlat = RetLat;
        Shared_Common_Pref.Outletlong = RetLng;
        getCompleteAddressString(RetLat, RetLng);

        centreMapOnLocation("Your Location");
        btnRefLoc.doneLoadingAnimation(getResources().getColor(R.color.green), BitmapFactory.decodeResource(getResources(), R.drawable.done));


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    btnRefLoc.stopAnimation();
                    btnRefLoc.revertAnimation();
                    btnRefLoc.setBackgroundResource((R.drawable.button_blueg));
                } catch (Exception e) {
                    Log.v(TAG, "LOC2:" + e.getMessage());

                }
            }
        }, 1000);
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                addRetailerAddress.setText(strReturnedAddress.toString());
                addRetailerCity.setText(returnedAddress.getLocality());
                edt_pin_codeedit.setText(returnedAddress.getPostalCode());
                strAdd = strReturnedAddress.toString();
                //Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                // Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //  Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    public void getRetailerClass() {
        try {
            String routeMap = "{\"tableName\":\"Mas_Doc_Class\",\"coloumns\":\"[\\\"Doc_ClsCode as id\\\"," +
                    " \\\"Doc_ClsSName as name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonArray> call = apiInterface.retailerClass(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code),
                    shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24", routeMap);
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    JsonArray jsonArray = response.body();
                    Log.e("RESPONSE_VALUE:Sub:", String.valueOf(jsonArray));
                    for (int a = 0; a < jsonArray.size(); a++) {
                        JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                        String className = String.valueOf(jsonObject.get("name"));
                        String id = String.valueOf(jsonObject.get("id"));
                        String retailerClass = String.valueOf(className.subSequence(1, className.length() - 1));
                        Log.e("RETAILER_CLASS_NAME", retailerClass);
                        if (Shared_Common_Pref.Editoutletflag != null && Shared_Common_Pref.Editoutletflag.equals("1") || (Shared_Common_Pref.Outlet_Info_Flag != null && Shared_Common_Pref.Outlet_Info_Flag.equals("1"))) {
                            /*if (id.equals(String.valueOf(Retailer_Modal_List.get(getOutletPosition()).getDocCatCode()))) {
                                txtRetailerClass.setText(className.replace('"', ' '));
                                classId = Integer.valueOf(String.valueOf(jsonObject.get("id")));
                            }*/
                        }
                        mCommon_model_spinner = new Common_Model(id, retailerClass, "flag");
                        Log.e("LeaveType_Request", retailerClass);
                        modelRetailClass.add(mCommon_model_spinner);
                    }


                    if (modelRetailClass != null && modelRetailClass.size() > 0)
                        shared_common_pref.save(Constants.RETAIL_CLASS, gson.toJson(modelRetailClass));
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                }
            });
        } catch (Exception e) {

        }
    }

    public int getOutletPosition() {
        for (int i = 0; Retailer_Modal_List.size() > i; i++) {
            if (Retailer_Modal_List.get(i).getId().equals(outletCode)) {
                return i;
            }
        }
        return -1;
    }

    /*Retailer Channel */
    public void getRetailerChannel() {
        String routeMap = "{\"tableName\":\"Doctor_Specialty\",\"coloumns\":\"[\\\"DivErp\\\",\\\"NeedApproval\\\",\\\"CategoryCode\\\",\\\"Specialty_Code as id\\\", \\\"Specialty_Name as name\\\"]\"," +
                "\"where\":\"[\\\"isnull(Deactivate_flag,0)=0\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.retailerClass(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code),
                shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24",
                routeMap);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {

                    JsonArray jsonArray = response.body();
                    Log.e("RESPONSE_VALUE:SubCAT:", String.valueOf(jsonArray));
                    for (int a = 0; a < jsonArray.size(); a++) {
                        JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                        String className = String.valueOf(jsonObject.get("name"));
                        String id = String.valueOf(jsonObject.get("id"));
                        if (Shared_Common_Pref.Editoutletflag != null && Shared_Common_Pref.Editoutletflag.equals("1") || (Shared_Common_Pref.Outlet_Info_Flag != null && Shared_Common_Pref.Outlet_Info_Flag.equals("1"))) {
                            if (id.equals(String.valueOf(Retailer_Modal_List.get(getOutletPosition()).getDocSpecialCode()))) {
                                className = className == null ? "" : className.replace('"', ' ');
                                txtRetailerChannel.setText(className);
                                channelID = Integer.valueOf(String.valueOf(jsonObject.get("id")));
                            }
                        }
                        String retailerClass = String.valueOf(className.subSequence(1, className.length() - 1));
                        Log.e("RETAILER_Channel_NAME", retailerClass);
                        String approval = String.valueOf(jsonObject.get("NeedApproval"));
                        String code = jsonObject.get("CategoryCode").getAsString();
                        String divERP = jsonObject.get("DivErp").getAsString();

                        // mCommon_model_spinner = new Common_Model(id, retailerClass, approval, String.valueOf(jsonObject.get("CategoryCode")));

                        mCommon_model_spinner = new Common_Model(retailerClass, id, approval, code, divERP);

                        Log.e("LeaveType_Request", retailerClass + ":code:" + code + ":erp:" + divERP);
                        modelRetailChannel.add(mCommon_model_spinner);
                    }

                    if (modelRetailChannel != null && modelRetailChannel.size() > 0) {
                        shared_common_pref.save(Constants.RETAIL_CHANNEL, gson.toJson(modelRetailChannel));
                    }
                } catch (Exception e) {
                    Log.v(TAG + " getRetailerChannel: ", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    public void addNewRetailers() {
        try {

            String cat = "";
            String subCat = "";
            String catSubCat = "";
            for (int i = 0; i < serviceTypeList.size(); i++) {
                if (serviceTypeList.get(i).isSelected()) {
                    if (Common_Class.isNullOrEmpty(serviceTypeList.get(i).getCatName())) {
                        common_class.showMsg(this, "Select the " + serviceTypeList.get(i).getName() + " Category");
                        ResetSubmitBtn(0);
                        return;
                    } else if (Common_Class.isNullOrEmpty(serviceTypeList.get(i).getSubCatName())) {
                        common_class.showMsg(this, "Select the " + serviceTypeList.get(i).getName() + " Sub Category");
                        ResetSubmitBtn(0);
                        return;
                    }
                    cat = cat + serviceTypeList.get(i).getCatName() + ",";
                    subCat = subCat + serviceTypeList.get(i).getSubCatName() + ",";
                    catSubCat = catSubCat + serviceTypeList.get(i).getCatName() + "~~" + serviceTypeList.get(i).getSubCatName() + ",";
                }
            }
            if (!imageServer.equalsIgnoreCase("")) {
                Intent mIntent = new Intent(ApproveOutletsDetailedActivity.this, FileUploadService.class);
                mIntent.putExtra("mFilePath", imageConvert);
                mIntent.putExtra("SF", UserDetails.getString("Sfcode", ""));
                mIntent.putExtra("FileName", imageServer);
                mIntent.putExtra("Mode", "Outlet");
                FileUploadService.enqueueWork(this, mIntent);
            }
            if (txOutletType.getText().toString().equalsIgnoreCase("Closed") && mData.get(0).getFileUrls() != null && mData.get(0).getFileUrls().size() > 0) {
                for (int j = 0; j < mData.get(0).getFileUrls().size(); j++) {
                    String filePath = mData.get(0).getFileUrls().get(j).replaceAll("file:/", "");
                    File file = new File(filePath);
                    Intent mIntent = new Intent(ApproveOutletsDetailedActivity.this, FileUploadService.class);
                    mIntent.putExtra("mFilePath", filePath);
                    mIntent.putExtra("SF", UserDetails.getString("Sfcode", ""));
                    mIntent.putExtra("FileName", file.getName());
                    mIntent.putExtra("Mode", "Outlet_Close");
                    FileUploadService.enqueueWork(ApproveOutletsDetailedActivity.this, mIntent);
                }

            }

            if (mFreezerData.get(0).getFileUrls() != null && mFreezerData.get(0).getFileUrls().size() > 0) {
                for (int j = 0; j < mFreezerData.get(0).getFileUrls().size(); j++) {
                    String filePath = mFreezerData.get(0).getFileUrls().get(j).replaceAll("file:/", "");
                    File file = new File(filePath);
                    Intent mIntent = new Intent(ApproveOutletsDetailedActivity.this, FileUploadService.class);
                    mIntent.putExtra("mFilePath", filePath);
                    mIntent.putExtra("SF", UserDetails.getString("Sfcode", ""));
                    mIntent.putExtra("FileName", file.getName());
                    mIntent.putExtra("Mode", "freezer");
                    FileUploadService.enqueueWork(ApproveOutletsDetailedActivity.this, mIntent);
                }

            }

            DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar calobjw = Calendar.getInstance();
            KeyDate = shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code);
            keyCodeValue = keyEk + KeyHyp + KeyDate + dfw.format(calobjw.getTime()).hashCode();
            Log.e("KEY_CODE_HASH", keyCodeValue);
            JSONObject reportObject = new JSONObject();
            docMasterObject = new JSONObject();

            reportObject.put("town_code", "'" + routeId + "'");
            reportObject.put("wlkg_sequence", "null");
            reportObject.put("unlisted_doctor_name", "'" + addRetailerName.getText().toString() + "'");
            reportObject.put("unlisted_Owner_name", "'" + owner_name.getText().toString() + "'");
            reportObject.put("unlisted_doctor_pincode", "'" + edt_pin_codeedit.getText().toString() + "'");
            reportObject.put("unlisted_doctor_gst", "'" + edt_gst.getText().toString() + "'");
            reportObject.put("unlisted_doctor_address", "'" + addRetailerAddress.getText().toString().replace("\n", "") + "'");
            reportObject.put("unlisted_doctor_phone", "'" + addRetailerPhone.getText().toString() + "'");
            reportObject.put("unlisted_doctor_secondphone", "'" + etPhoneNo2.getText().toString() + "'");

            reportObject.put("CategoryType", "'" + categoryType + "'");
            if (Common_Class.isNullOrEmpty(edt_outstanding.getText().toString()) || edt_outstanding.getText().toString().equalsIgnoreCase("."))
                reportObject.put("outstanding_amount", 0);

            else
                reportObject.put("outstanding_amount", "'" + edt_outstanding.getText().toString());
            reportObject.put("unlisted_doctor_cityname", "'" + addRetailerCity.getText().toString() + "'");
            reportObject.put("districtname", "'" + etDistrict.getText().toString() + "'");

            reportObject.put("State_Code", "'" + stateCode + "'");
            reportObject.put("unlisted_doctor_landmark", "''");
            reportObject.put("unlisted_doctor_mobiledate", common_class.addquote(Common_Class.GetDatewothouttime()));
            reportObject.put("reason_category", common_class.addquote(reason_category_remarks));
            reportObject.put("Compititor_Id", common_class.addquote(Compititor_Id));
            reportObject.put("Compititor_Name", common_class.addquote(Compititor_Name));
            reportObject.put("CatUniverSelectId", common_class.addquote(CatUniverSelectId));
            reportObject.put("AvailUniverSelectId", common_class.addquote(AvailUniverSelectId));
            reportObject.put("HatsunAvailswitch", common_class.addquote(HatsunAvailswitch));
            reportObject.put("categoryuniverseswitch", common_class.addquote(categoryuniverseswitch));
            reportObject.put("lat", common_class.addquote(String.valueOf(Shared_Common_Pref.Outletlat)));
            reportObject.put("long", common_class.addquote(String.valueOf(Shared_Common_Pref.Outletlong)));
            reportObject.put("VechType", txDelvryType.getText().toString());
            reportObject.put("OutletTypeNm", txOutletType.getText().toString());
            reportObject.put("OutletTypeCd", iOutletTyp);
            reportObject.put("OutletTypeRmks", edtClsRetRmk.getText().toString());

            reportObject.put("unlisted_doctor_areaname", "''");
            reportObject.put("unlisted_doctor_Email", common_class.addquote(addRetailerEmail.getText().toString()));
            reportObject.put("unlisted_doctor_contactperson", "''");
            reportObject.put("unlisted_doctor_designation", "''");
            reportObject.put("unlisted_doctor_phone2", "''");
            reportObject.put("unlisted_doctor_phone3", "''");
            reportObject.put("unlisted_doctor_contactperson2", "''");
            reportObject.put("unlisted_doctor_contactperson3", "''");
            reportObject.put("unlisted_doctor_designation2", "''");
            reportObject.put("unlisted_cat_code", "null");
            reportObject.put("unlisted_specialty_name", Common_Class.isNullOrEmpty(tvSubCategory.getText().toString()) ? subCat : tvSubCategory.getText().toString());
            reportObject.put("unlisted_specialty_code", categoryId);
            reportObject.put("unlisted_qulifi", "'samp'");
            reportObject.put("unlisted_class", classId);
            reportObject.put("id", common_class.addquote(outletCode));

            reportObject.put("customer_code", cbFranchise.isChecked() ? customer_code : "");

            reportObject.put("DrKeyId", "'" + keyCodeValue + "'");

            //for marked option in explore screen
            reportObject.put("place_id", "'" + place_id + "'");
//
//            String imgName = filePath.substring(filePath.indexOf("/"));
            reportObject.put("img_name", "'" + imageServer + "'");
            reportObject.put("sub_category", Common_Class.isNullOrEmpty(txtRetailerChannel.getText().toString()) ? "'" + cat + "'" :
                    "'" + txtRetailerChannel.getText().toString() + "'");
            reportObject.put("category_type", catSubCat);
            reportObject.put("sub_categoryId", "'" + channelID + "'");
            reportObject.put("expected_sales_value", "'" + edtExpcSalVal.getText().toString() + "'");
            reportObject.put("deposit_amount", "'" + edtDepositAmt.getText().toString() + "'");
            reportObject.put("fssai_number", "'" + edtFSSAI.getText().toString() + "'");
            reportObject.put("pan_number", "'" + edtPAN.getText().toString() + "'");

            reportObject.put("freezer_make", "'" + edtFreezerMake.getText().toString() + "'");
            reportObject.put("freezer_tagno", "'" + edtFreezerTag.getText().toString() + "'");
            reportObject.put("freezer_status", "'" + tvFreezerSta.getText().toString() + "'");
            reportObject.put("freezer_capacity", "'" + tvFreezerCapacity.getText().toString() + "'");
            reportObject.put("freezer_statusId", "'" + freezerStaId + "'");
            reportObject.put("freezer_capacityId", "'" + freezerCapId + "'");

            reportObject.put("freezer_required", cbFreezerYes.isChecked() ? "'Yes" : "'No" + "'");


            reportObject.put("active_flag", "'" + (txOutletType.getText().toString().equalsIgnoreCase("Duplicate") ? 1 : 0 + "'"));


            boolean isApproval = false;
            try {
                Type userType = new TypeToken<ArrayList<Common_Model>>() {
                }.getType();

                String serviceType = shared_common_pref.getvalue(Constants.SERVICETYPE_LIST);
                ArrayList<Common_Model> old_list = gson.fromJson(serviceType, userType);

                //  not match== need approval 1=3
                if (serviceTypeList.equals(old_list))
                    isApproval = true;
            } catch (Exception e) {

            }


            ///   reportObject.put("flag", "'" + (isApproval ? 0 : approval.equalsIgnoreCase("1") ? 3 : 0) + "'");

            reportObject.put("flag", "'" + ((cbFreezerYes.isChecked() || approval.equalsIgnoreCase("1")) ? 3 : 0) + "'");

            reportObject.put(Constants.LOGIN_TYPE, "'" + shared_common_pref.getvalue(Constants.LOGIN_TYPE) + "'");


            JSONArray fileArr = new JSONArray();
            if (txOutletType.getText().toString().equalsIgnoreCase("Closed") && mData.get(0).getFileUrls() != null && mData.get(0).getFileUrls().size() > 0) {
                for (int i = 0; i < mData.get(0).getFileUrls().size(); i++) {
                    JSONObject fileData = new JSONObject();
                    File file = new File(mData.get(0).getFileUrls().get(i));
                    fileData.put("outlet_close_filename", file.getName());
                    fileArr.put(fileData);
                }
            }
            reportObject.put("file_Details", fileArr);


            JSONArray freezerFileArr = new JSONArray();
            if (mFreezerData.get(0).getFileUrls() != null && mFreezerData.get(0).getFileUrls().size() > 0) {
                for (int i = 0; i < mFreezerData.get(0).getFileUrls().size(); i++) {
                    JSONObject fileData = new JSONObject();
                    File file = new File(mFreezerData.get(0).getFileUrls().get(i));
                    fileData.put("freezer_filename", file.getName());
                    freezerFileArr.put(fileData);
                }
            }
            reportObject.put("freezer_file_Details", freezerFileArr);

            JSONArray outletTypeArr = new JSONArray();

            //if (divERP.equalsIgnoreCase("21")) {
            for (int i = 0; i < serviceTypeList.size(); i++) {
                if (serviceTypeList.get(i).isSelected()) {
                    JSONObject typeData = new JSONObject();

                    double catId = Double.parseDouble((serviceTypeList.get(i).getCatId()));

                    double subCatId = Double.parseDouble((serviceTypeList.get(i).getSubCatId()));
                    typeData.put("type_name", serviceTypeList.get(i).getName());
                    typeData.put("cat_name", serviceTypeList.get(i).getCatName());
                    typeData.put("cat_id", (int) catId);
                    typeData.put("subcat_id", (int) subCatId);
                    typeData.put("subcat_name", serviceTypeList.get(i).getSubCatName());

                    outletTypeArr.put(typeData);

                    if (Common_Class.isNullOrEmpty(serviceTypeList.get(i).getSubCatName())) {
                        common_class.showMsg(ApproveOutletsDetailedActivity.this, "Select the " + serviceTypeList.get(i).getName() + " Sub Category");
                        return;
                    } else if (Common_Class.isNullOrEmpty(serviceTypeList.get(i).getCatName())) {
                        common_class.showMsg(ApproveOutletsDetailedActivity.this, "Select the " + serviceTypeList.get(i).getName() + " Category");
                        return;
                    }
                }
            }
//            } else {
//                JSONObject typeData = new JSONObject();
//                typeData.put("type_name", "");
//                typeData.put("cat_name", txtRetailerChannel.getText().toString());
//                typeData.put("cat_id", channelID);
//                typeData.put("subcat_id", tvSubCategory.getText().toString());
//                typeData.put("subcat_name", categoryId);
//                outletTypeArr.put(typeData);
//
//            }

            reportObject.put("outlet_type_Details", outletTypeArr);


            // docMasterObject.put("unlisted_doctor_master", reportObject);
            docMasterObject.put("newunlisted_doctor_master", reportObject);


            mainArray = new JSONArray();
            mainArray.put(docMasterObject);

            String totalValueString = "";
            Map<String, String> QueryString = new HashMap<>();
            if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                QueryString.put("axn", "dcr/save");
                totalValueString = mainArray.toString();
            } else {
                QueryString.put("axn", "upd/newretailer2");
                totalValueString = reportObject.toString();
            }
            QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
            QueryString.put("State_Code", Shared_Common_Pref.StateCode);
            QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
            QueryString.put("activeFlag", "2");
            QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
            QueryString.put(Constants.Distributor_Id, shared_common_pref.getvalue(Constants.Distributor_Id));
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            // addNewRetailer
            Log.e("QueryString", totalValueString);
            Log.e("QueryString", QueryString.toString());

            Call<JsonObject> call = apiInterface.addNewRetailer(QueryString, totalValueString);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject jsonObject = response.body();
                    // Log.e("Add_Retailer_details", String.valueOf(jsonObject));
                    String success = String.valueOf(jsonObject.get("success"));

                    common_class.getDataFromApi(Constants.Retailer_OutletList, ApproveOutletsDetailedActivity.this, false);

                    if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                        Toast.makeText(ApproveOutletsDetailedActivity.this, "Outlet Added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ApproveOutletsDetailedActivity.this, "Outlet Updated successfully", Toast.LENGTH_SHORT).show();
                    }

                    ResetSubmitBtn(1);
                    /*
                    if (cbFreezerYes.isChecked() || approval.equalsIgnoreCase("1")) {
                        common_class.CommonIntentwithFinish(OutletApprovListActivity.class);
                        overridePendingTransition(R.anim.in, R.anim.out);
                    } else
//                    if (Shared_Common_Pref.FromActivity == "Outlets") {
//                        Shared_Common_Pref.FromActivity = "";
//                        common_class.CommonIntentwithFinish(Outlet_Info_Activity.class);
//                    } else if ((success.equalsIgnoreCase("true") && Shared_Common_Pref.Outler_AddFlag.equals("1")) || (success.equalsIgnoreCase("true") && Shared_Common_Pref.Editoutletflag.equals("1"))) {
//                        Shared_Common_Pref.Outler_AddFlag = "0";
//                        Shared_Common_Pref.Sync_Flag = "1";
//                        //startActivity(new Intent(getApplicationContext(), Dashboard_Route.class));
//                        common_class.CommonIntentwithFinish(Dashboard_Route.class);
//                        // startActivity(new Intent(getApplicationContext(), Offline_Sync_Activity.class));
//                    }*/


                    shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, shared_common_pref.getvalue(Constants.Distributor_Id));
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    ResetSubmitBtn(0);
                    common_class.showMsg(ApproveOutletsDetailedActivity.this, t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            common_class.showMsg(this, e.getMessage());
            ResetSubmitBtn(2);
        }
    }

    public void ResetSubmitBtn(int resetMode) {
        common_class.ProgressdialogShow(0, "");
        long dely = 10;
        if (resetMode != 0) dely = 1000;
        if (resetMode == 1) {
            //mSubmit.doneLoadingAnimation(getResources().getColor(R.color.green), BitmapFactory.decodeResource(getResources(), R.drawable.done));
        } else {
            //mSubmit.doneLoadingAnimation(getResources().getColor(R.color.color_red), BitmapFactory.decodeResource(getResources(), R.drawable.ic_wrong));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //mSubmit.stopAnimation();
                //mSubmit.revertAnimation();
            }
        }, dely);

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        switch (type) {
            case 5:

                serviceTypeList.get(typeUpdatePos).setCatId(myDataset.get(position).getId());
                serviceTypeList.get(typeUpdatePos).setCatName(myDataset.get(position).getName());

                serviceTypeList.get(typeUpdatePos).setSubCatId("");
                serviceTypeList.get(typeUpdatePos).setSubCatName("");

                categoryAdapter.notifyData(serviceTypeList, this);

                break;
            case 6:
                serviceTypeList.get(typeUpdatePos).setSubCatId(myDataset.get(position).getId());
                serviceTypeList.get(typeUpdatePos).setSubCatName(myDataset.get(position).getName());
                categoryAdapter.notifyData(serviceTypeList, this);

                break;
            case 1:
                tvStateName.setText(myDataset.get(position).getName());
                stateCode = Integer.valueOf(myDataset.get(position).getId());
                break;
            case 4:
                tvServiceType.setText(myDataset.get(position).getName());
                break;
            case 2:
                txtRetailerRoute.setText("");
                routeId = "";
                distGrpERP = myDataset.get(position).getCusSubGrpErp();
                distributor_text.setText(myDataset.get(position).getName());
                findViewById(R.id.rl_route).setVisibility(View.VISIBLE);
                shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, myDataset.get(position).getId());


                divERP = myDataset.get(position).getDivERP();
                distributorERP = myDataset.get(position).getCont();

                getFreezerData(divERP);

                common_class.getDb_310Data(Constants.Rout_List, this);


                // getServiceTypes(myDataset.get(position).getId());
                break;
            case 3:
                routeId = myDataset.get(position).getId();
                txtRetailerRoute.setText(myDataset.get(position).getName());
                break;

            case 8:
                txtRetailerChannel.setText(myDataset.get(position).getName());
                channelID = Integer.valueOf(myDataset.get(position).getId());
                break;
            case 9:
                txtRetailerClass.setText(myDataset.get(position).getName());
                classId = Integer.valueOf(myDataset.get(position).getId());
                break;

            case 10:
                tvSubCategory.setText(myDataset.get(position).getName());
                categoryId = myDataset.get(position).getId();
                approval = myDataset.get(position).getFlag();
                break;

            case 11:
                txDelvryType.setText(myDataset.get(position).getName());
                break;
            case 13:
                txOutletType.setText(myDataset.get(position).getName());
                iOutletTyp = Integer.valueOf(myDataset.get(position).getId());
                linClsRmks.setVisibility(View.GONE);
                if (iOutletTyp == 2) {
                    linClsRmks.setVisibility(View.VISIBLE);
                }
                break;
            case 14:
                freezerCapId = myDataset.get(position).getId();
                tvFreezerCapacity.setText(myDataset.get(position).getName());
                break;
            case 15:
                freezerStaId = myDataset.get(position).getId();
                tvFreezerSta.setText(myDataset.get(position).getName());
                freezerStaApproval = myDataset.get(position).getCnvQty();
                if (myDataset.get(position).getName().equalsIgnoreCase("Company Provided"))
                    findViewById(R.id.llExpecSalVal).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.llExpecSalVal).setVisibility(View.GONE);

                break;
        }
    }

    private void getFreezerData(String divERP) {

//        if (divERP.equalsIgnoreCase("62"))
//            linReatilerRoute.setEnabled(true);
        if (divERP.equals("21")) {
            //  linReatilerRoute.setEnabled(true);
            findViewById(R.id.llCategoryType).setVisibility(View.VISIBLE);
            findViewById(R.id.rvCategoryTypes).setVisibility(View.VISIBLE);
            findViewById(R.id.llSubCategory).setVisibility(View.GONE);
            findViewById(R.id.llCategory).setVisibility(View.GONE);

            serviceTypeList = new ArrayList<>();
            serviceTypeList.add(new Common_Model("-18", "1", false, "", "", "", ""));
            serviceTypeList.add(new Common_Model("+4", "2", false, "", "", "", ""));
            serviceTypeList.add(new Common_Model("Ambient", "3", false, "", "", "", ""));
            //  serviceTypeList.add(new Common_Model("B&C", "4", false, "", "", "", ""));


            categoryAdapter = new ApproveOutletsDetailedActivity.Category_Adapter(serviceTypeList, R.layout.adapter_retailer_category_types_two, ApproveOutletsDetailedActivity.this);
            rvCategoryTypes.setAdapter(categoryAdapter);


            // findViewById(R.id.llFreezer).setVisibility(View.VISIBLE);
        } else {
//            if (!Shared_Common_Pref.Outler_AddFlag.equals("1"))
//                linReatilerRoute.setEnabled(false);
            if (!Common_Class.isNullOrEmpty(divERP)) {

//                findViewById(R.id.llCategoryType).setVisibility(View.GONE);
//                findViewById(R.id.rvCategoryTypes).setVisibility(View.GONE);
//                findViewById(R.id.llSubCategory).setVisibility(View.VISIBLE);
//                findViewById(R.id.llCategory).setVisibility(View.VISIBLE);
                //  serviceTypeList.clear();

                serviceTypeList = new ArrayList<>();

                serviceTypeList.add(new Common_Model("+4", "2", false, "", "", "", ""));
                categoryAdapter = new ApproveOutletsDetailedActivity.Category_Adapter(serviceTypeList, R.layout.adapter_retailer_category_types, ApproveOutletsDetailedActivity.this);
                rvCategoryTypes.setAdapter(categoryAdapter);

            }
            //findViewById(R.id.llFreezer).setVisibility(View.GONE);
            mFreezerData.clear();
            mFreezerData = new ArrayList<>();
            mFreezerData.add(new QPS_Modal("", "", ""));

            edtFreezerMake.setText("");
            edtFreezerTag.setText("");
            tvFreezerSta.setText("");
            tvFreezerCapacity.setText("");
            freezerCapId = "";
            freezerStaId = "";
            findViewById(R.id.llExpecSalVal).setVisibility(View.GONE);

        }


    }

    public void loadroute(String id) {
        if (Common_Class.isNullOrEmpty(String.valueOf(id))) {
            Toast.makeText(this, "Select Franchise", Toast.LENGTH_SHORT).show();
        }

        if (FRoute_Master.size() == 1) {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.INVISIBLE);
            txtRetailerRoute.setText(FRoute_Master.get(0).getName());
            routeId = FRoute_Master.get(0).getId();
        } else {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, shared_common_pref.getvalue(Constants.Distributor_Id));

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {


            new LocationFinder(this, new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {
                    try {
                        refreshLocation(location);
                    } catch (Exception e) {
                    }
                }
            });
            if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
                finalPath = "/storage/emulated/0";
                filePath = outputFileUri.getPath();
                filePath = filePath.substring(1);
                filePath = finalPath + filePath.substring(filePath.indexOf("/"));

                file = new File(filePath);

                ivPhotoShop.setImageURI(Uri.fromFile(file));


            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.ivProfileView:

                    if (imageConvert.equalsIgnoreCase("") && name.equalsIgnoreCase("")) {
                        common_class.showMsg(this, "Please take picture");
                    } else {
                        Intent intentProfile = new Intent(this, ProductImageView.class);
                        intentProfile.putExtra("ImageUrl", imageConvert.equalsIgnoreCase("") ? name :
                                Uri.fromFile(new File(imageConvert)).toString());
                        startActivity(intentProfile);
                    }
                    break;
                case R.id.linear_retailer_class:
                    common_class.showCommonDialog(modelRetailClass, 9, ApproveOutletsDetailedActivity.this);
                    break;
                case R.id.linear_retailer_channel:
                    common_class.showCommonDialog(categoryList, 8, ApproveOutletsDetailedActivity.this);
                    break;
                case R.id.linear_retailer_subCategory:
                    common_class.showCommonDialog(modelRetailChannel, 10, ApproveOutletsDetailedActivity.this);
                    break;
                case R.id.btn_dist_enter:
                    if (Shared_Common_Pref.Outler_AddFlag != null && !Shared_Common_Pref.Outler_AddFlag.equals("1")) {

                        AlertDialogBox.showDialog(ApproveOutletsDetailedActivity.this, HAPApp.Title, "Are You Sure Want to Update the Franchise Code?", "OK", "Cancel", false, new AlertBox() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                checkValidity();
                            }

                            @Override
                            public void NegativeMethod(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });
                    } else {
                        checkValidity();
                    }
                    break;
                case R.id.ivRetailCapture:
                    captureImg(mData, rvFiles);
                    break;
                case R.id.ivFreezerCapture:
                    captureImg(mFreezerData, rvFreezerFiles);
                    break;
                case R.id.linear_service_type:
                    common_class.showCommonDialog(serviceTypeList, 4, this);

                    break;
                case R.id.rlFreezerCapacity:
                    common_class.showCommonDialog(freezerCapcityList, 14, this);
                    break;
                case R.id.rlFreezerStatus:
                    common_class.showCommonDialog(freezerStaList, 15, this);

                    break;
                case R.id.rl_state:
                    common_class.showCommonDialog(stateList, 1, this);
                    break;

                case R.id.rl_route:
                    if (FRoute_Master != null && FRoute_Master.size() > 1) {
                        common_class.showCommonDialog(FRoute_Master, 3, this);
                    }
                    break;
                case R.id.rl_Distributor:
                    common_class.showCommonDialog(common_class.getDistList(), 2, this);
                    break;
                case R.id.copypaste:
                    addRetailerAddress.setText(CurrentLocationsAddress.getText().toString());
                    break;

                case R.id.ivShopPhoto:
                    try {
                        AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                            @Override
                            public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                                imageServer = FileName;
                                imageConvert = fullPath;
                                ivPhotoShop.setImageBitmap(image);
                            }
                        });
                        Intent intent = new Intent(ApproveOutletsDetailedActivity.this, AllowancCapture.class);
                        intent.putExtra("allowance", "One");
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.v(TAG, ":imageClk:" + e.getMessage());
                    }
                    break;
            }
        } catch (Exception e) {
            Log.v(TAG + "profileView:", e.getMessage());
        }
    }

    void checkValidity() {
        if (Common_Class.isNullOrEmpty(edtDistCode.getText().toString()))
            common_class.showMsg(this, "Enter Customer Code");
        else {
            JsonObject data = new JsonObject();
            data.addProperty("customer_code", edtDistCode.getText().toString().trim());
            data.addProperty("ERP_Code", distributorERP);
            common_class.getDb_310Data(Constants.CUSTOMER_DATA, this, data);
        }
    }

    void assignData() {
        if (!Common_Class.isNullOrEmpty(Retailer_Modal_List.get(getOutletPosition()).getImagename())) {
            name = ApiClient.BASE_URL + Retailer_Modal_List.get(getOutletPosition()).getImagename();
            name = name.replaceAll("server/", "");
            name = name.replaceAll(",", "");
            Glide.with(ApproveOutletsDetailedActivity.this)
                    .load(name)
                    .error(R.drawable.profile_img)
                    .into(ivPhotoShop);
        }
        addRetailerName.setText("" + Retailer_Modal_List.get(getOutletPosition()).getName());
        addRetailerAddress.setText("" + Retailer_Modal_List.get(getOutletPosition()).getListedDrAddress1());
        txtRetailerRoute.setText("" + Retailer_Modal_List.get(getOutletPosition()).getTownName());
        addRetailerPhone.setText("" + Retailer_Modal_List.get(getOutletPosition()).getPrimary_No());
        retailercode.setText("" + Retailer_Modal_List.get(getOutletPosition()).getERP_Code());
        if (Retailer_Modal_List.get(getOutletPosition()).getSecondary_No() != null)
            etPhoneNo2.setText("" + Retailer_Modal_List.get(getOutletPosition()).getSecondary_No());
        if (Retailer_Modal_List.get(getOutletPosition()).getCityname() != null)
            addRetailerCity.setText("" + Retailer_Modal_List.get(getOutletPosition()).getCityname());
        if (Retailer_Modal_List.get(getOutletPosition()).getListedDr_Email() != null)
            addRetailerEmail.setText("" + Retailer_Modal_List.get(getOutletPosition()).getListedDr_Email());
        if (Retailer_Modal_List.get(getOutletPosition()).getOwner_Name() != null)
            owner_name.setText("" + Retailer_Modal_List.get(getOutletPosition()).getOwner_Name());
        if (Retailer_Modal_List.get(getOutletPosition()).getDistrictname() != null)
            etDistrict.setText("" + Retailer_Modal_List.get(getOutletPosition()).getDistrictname());


        edt_pin_codeedit.setText("" + (Retailer_Modal_List.get(getOutletPosition()).getPin_code()));
        edt_gst.setText("" + (Retailer_Modal_List.get(getOutletPosition()).getGst()));
        // txtRetailerClass.setText("" + Retailer_Modal_List.get(getOutletPosition()).getClass());

        if (stateList.size() > 0) {
            for (int i = 0; i < stateList.size(); i++) {
                if (stateList.get(i).getId().equalsIgnoreCase(Retailer_Modal_List.get(getOutletPosition()).getStateCode())) {
                    tvStateName.setText(stateList.get(i).getName());
                    stateCode = Integer.valueOf(stateList.get(i).getId());

                }
            }
        }


        if (getIntent().getExtras().getString("Compititor_Id") != null)
            Compititor_Id = getIntent().getExtras().getString("Compititor_Id");
        if (getIntent().getExtras().getString("Compititor_Name") != null)
            Compititor_Name = getIntent().getExtras().getString("Compititor_Name");
        if (getIntent().getExtras().getString("CatUniverSelectId") != null)
            CatUniverSelectId = getIntent().getExtras().getString("CatUniverSelectId");
        if (getIntent().getExtras().getString("AvailUniverSelectId") != null)
            AvailUniverSelectId = getIntent().getExtras().getString("AvailUniverSelectId");
        if (getIntent().getExtras().getString("reason_category") != null)
            reason_category_remarks = getIntent().getExtras().getString("reason_category");
    }

    private void captureImg(List<QPS_Modal> mModal, RecyclerView rv) {
        AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
            @Override
            public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                try {


                    List<String> list = new ArrayList<>();
                    File file = new File(fullPath);
                    Uri contentUri = Uri.fromFile(file);

                    if (mModal.get(0).getFileUrls() != null && mModal.get(0).getFileUrls().size() > 0)
                        list = (mModal.get(0).getFileUrls());
                    list.add(contentUri.toString());
                    mModal.get(0).setFileUrls(list);

                    filesAdapter = new FilesAdapter(mModal.get(0).getFileUrls(), R.layout.adapter_local_files_layout, ApproveOutletsDetailedActivity.this);
                    rv.setAdapter(filesAdapter);

                } catch (Exception e) {
                    Log.v(TAG + ":capture:", e.getMessage());
                }

            }
        });
        Intent intent = new Intent(this, AllowancCapture.class);
        intent.putExtra("allowance", "TAClaim");
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        centreMapOnLocation("Your Location");
    }

    public void centreMapOnLocation(String title) {

        LatLng userLocation = new LatLng(RetLat, RetLng);
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions().position(userLocation).title(title));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));

    }

    void getCategoryList(String apiDataResponse) {
        try {
            JSONObject catObj = new JSONObject(apiDataResponse);
            Log.e("RESPONSE_VALUE:CAT:", apiDataResponse);

            if (catObj.getBoolean("success")) {

                JSONArray arr = catObj.getJSONArray("Data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    categoryList.add(new Common_Model(obj.getString("CategoryCode"), obj.getString("CategoryName"),
                            obj.getString("OutletCategory"), obj.getString("DivErp")));
                }

            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {

                switch (key) {
                    case OUTLET_CATEGORY:
                        getCategoryList(apiDataResponse);
                        break;
                    case Freezer_Status:

                        JSONObject staObj = new JSONObject(apiDataResponse);
                        if (staObj.getBoolean("success")) {
                            JSONArray arr = staObj.getJSONArray("Data");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                freezerStaList.add(new Common_Model(obj.getString("FStatus"), obj.getString("ID")));
                            }
                        } else {
                            freezerStaList.clear();
                        }

                        break;
                    case Freezer_capacity:
                        freezerCapcityList.clear();
                        JSONObject capObj = new JSONObject(apiDataResponse);
                        if (capObj.getBoolean("success")) {
                            JSONArray arr = capObj.getJSONArray("Data");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                freezerCapcityList.add(new Common_Model(obj.getString("FCapacity"), obj.getString("ID")));
                            }
                        } else {
                            freezerCapcityList.clear();
                        }


                        break;
                    case CUSTOMER_DATA:
                        JSONObject cusObj = new JSONObject(apiDataResponse);
                        if (cusObj.getBoolean("success")) {
                            JSONArray arr = cusObj.getJSONArray("Data");
                            JSONObject obj = arr.getJSONObject(0);

                            edtFSSAI.setText("" + obj.getString("Fssai_No"));
                            addRetailerPhone.setText("" + obj.getString("Mobile"));
                            addRetailerName.setText("" + obj.getString("Name"));
                            addRetailerAddress.setText(obj.getString("Address"));
                            edt_gst.setText("" + obj.getString("gstn"));

                            btnDistCode.setText("Valid Code");
                            isValidCode = true;
                            customer_code = edtDistCode.getText().toString();
                        } else {
                            btnDistCode.setText("Invalid Code");
                            common_class.showMsg(this, cusObj.getString("Msg"));
                        }
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
                        //loadroute(shared_common_pref.getvalue(Constants.TEMP_DISTRIBUTOR_ID));
                        break;
                    case Constants.STATE_LIST:
                        Log.v(TAG, "state:" + apiDataResponse);
                        JSONObject stateObj = new JSONObject(apiDataResponse);
                        if (stateObj.getBoolean("success")) {
                            stateList = new ArrayList<>();
                            stateList.clear();

                            JSONArray array = stateObj.getJSONArray("Data");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                stateList.add(new Common_Model(obj.getString("StateName"), obj.getString("State_Code")));

                                try {
                                    if (!Shared_Common_Pref.Outler_AddFlag.equals("1") && getOutletPosition() >= 0 && (Retailer_Modal_List.get(getOutletPosition()).getStateCode() != null && obj.getString("State_Code").equals
                                            (Retailer_Modal_List.get(getOutletPosition()).getStateCode()))) {

                                        tvStateName.setText("" + obj.getString("StateName"));
                                        stateCode = Integer.valueOf(obj.getString("State_Code"));
                                    }
                                } catch (Exception e) {

                                }
                            }


                        }
                        if (stateList.size() > 0)
                            shared_common_pref.save(Constants.STATE_LIST, gson.toJson(stateList));
                        break;
                }

            }

        } catch (Exception e) {
            Log.v(TAG + "stateRes:", e.getMessage());
        }

    }

    private void updateView(String name, boolean isChecked) {
        for (int i = 0; i < serviceTypeList.size(); i++) {
            if (serviceTypeList.get(i).getName().equalsIgnoreCase("-18") && serviceTypeList.get(i).isSelected()) {
                cbFreezerYes.setEnabled(false);
                cbFreezerYes.setChecked(true);
                cbFreezerNo.setChecked(false);
                cbFreezerNo.setEnabled(false);


                break;
            } else if (serviceTypeList.get(i).getName().equalsIgnoreCase("+4") && serviceTypeList.get(i).isSelected()) {
                cbFreezerYes.setEnabled(false);
                cbFreezerNo.setEnabled(false);
                break;
            } else if ((serviceTypeList.get(i).getName().equalsIgnoreCase("Ambient") && serviceTypeList.get(i).isSelected()) || (serviceTypeList.get(i).getName().equalsIgnoreCase("B&C") && serviceTypeList.get(i).isSelected())) {
                cbFreezerNo.setChecked(true);
                cbFreezerNo.setEnabled(false);
                cbFreezerYes.setChecked(false);
                cbFreezerYes.setEnabled(false);
                break;
            }

        }

        if (cbFreezerYes.isChecked())
            findViewById(R.id.llFreezer).setVisibility(View.VISIBLE);


        if (cbFreezerNo.isChecked())
            findViewById(R.id.llFreezer).setVisibility(View.GONE);

        // cbFreezerYes.setChecked(isChecked);

    }


    public class Category_Adapter extends RecyclerView.Adapter<ApproveOutletsDetailedActivity.Category_Adapter.MyViewHolder> {
        Context context;
        private List<Common_Model> list;
        private int rowLayout;

        public Category_Adapter(List<Common_Model> list, int rowLayout, Context context) {
            this.list = list;
            this.rowLayout = rowLayout;
            this.context = context;
        }

        public void notifyData(List<Common_Model> list, Context context) {
            this.list = list;
            this.context = context;
            notifyDataSetChanged();
        }

        @Override
        public ApproveOutletsDetailedActivity.Category_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new ApproveOutletsDetailedActivity.Category_Adapter.MyViewHolder(view);
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
        public void onBindViewHolder(ApproveOutletsDetailedActivity.Category_Adapter.MyViewHolder holder, int position) {
            try {

                holder.type.setText(list.get(position).getName());
                holder.category.setText(list.get(position).getCatName());
                holder.subCategory.setText(list.get(position).getSubCatName());
                if (list.get(position).isSelected() == false) holder.cbType.setChecked(false);
                if (list.get(position).isSelected() == true) holder.cbType.setChecked(true);
                holder.cbType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try {

                            list.get(position).setSelected(isChecked);
                            // if (list.get(position).getName().equalsIgnoreCase("-18"))
                            ApproveOutletsDetailedActivity.mAddNewRetailer.updateView(list.get(position).getName(), isChecked);

                        } catch (Exception e) {
                            Log.e(TAG, "adapterProductEx: " + e.getMessage());

                        }
                    }
                });

                holder.category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        typeUpdatePos = position;
                        if (list.get(position).isSelected()) {
                            ArrayList<Common_Model> typeCatList = new ArrayList<>();

//                            for (int i = 0; i < categoryList.size(); i++) {
//                                if (categoryList.get(i).getFlag().contains(list.get(position).getName()))
//                                    typeCatList.add(categoryList.get(i));
//                            }

                            for (int i = 0; i < categoryList.size(); i++) {
                                String ERP = categoryList.get(i).getCheckouttime() + ",";
                                Log.v(TAG + "cat:", "pos" + i + ":" + categoryList.get(i).getFlag() + ":ERP:" + ERP + ":grpERP:" + distGrpERP);
                                if (categoryList.get(i).getFlag().contains(list.get(position).getName()) && ERP.contains(distGrpERP + ","))
                                    typeCatList.add(categoryList.get(i));
                            }
                            common_class.showCommonDialog(typeCatList, 5, ApproveOutletsDetailedActivity.this);
                        }
                    }
                });

                holder.subCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            typeUpdatePos = position;
                            if (list.get(position).isSelected() && !Common_Class.isNullOrEmpty(list.get(position).getCatId())) {
                                ArrayList<Common_Model> typeSubCatList = new ArrayList<>();

//                                for (int i = 0; i < modelRetailChannel.size(); i++) {
//
//                                    // String code = modelRetailChannel.get(i).getCheckouttime();
//                                    String code = modelRetailChannel.get(i).getAddress();
//
//                                    if (code.contains(list.get(position).getCatId() + ","))
//                                        typeSubCatList.add(modelRetailChannel.get(i));
//                                }


                                for (int i = 0; i < modelRetailChannel.size(); i++) {

                                    // String code = modelRetailChannel.get(i).getCheckouttime();
                                    String code = modelRetailChannel.get(i).getAddress();
                                    String ERP = modelRetailChannel.get(i).getPhone() + ",";

                                    if (code.contains(list.get(position).getCatId() + ",") && (ERP.contains(distGrpERP + ",")))
                                        typeSubCatList.add(modelRetailChannel.get(i));
                                }


                                common_class.showCommonDialog(typeSubCatList, 6, ApproveOutletsDetailedActivity.this);
                            }
                        } catch (Exception e) {
                            Log.v(TAG, "subCate: " + e.getMessage());

                        }
                    }
                });


            } catch (Exception e) {
                Log.e(TAG, "adapterProduct: " + e.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView category, type, subCategory;
            CheckBox cbType;

            public MyViewHolder(View view) {
                super(view);
                category = view.findViewById(R.id.tvCategory);
                type = view.findViewById(R.id.tvCategoryType);
                subCategory = view.findViewById(R.id.tvSubCategory);
                cbType = view.findViewById(R.id.cbCategory);

            }
        }
    }
}
