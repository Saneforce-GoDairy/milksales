package com.saneforce.godairy.SFA_Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.saneforce.godairy.Activity_Hap.AllowancCapture;
import com.saneforce.godairy.Activity_Hap.ProductImageView;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.DownloadReceiver;
import com.saneforce.godairy.Common_Class.FileDownloader;
import com.saneforce.godairy.Common_Class.MyAlertDialog;
import com.saneforce.godairy.Common_Class.MyProgressDialog;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.OnImagePickListener;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.AdapterShowMultipleImages;
import com.saneforce.godairy.SFA_Adapter.CommonAdapterForDropdown;
import com.saneforce.godairy.SFA_Adapter.CommonAdapterForDropdownWithFilter;
import com.saneforce.godairy.SFA_Model_Class.CommonModelForDropDown;
import com.saneforce.godairy.SFA_Model_Class.CommonModelWithFourString;
import com.saneforce.godairy.SFA_Model_Class.CommonModelWithThreeString;
import com.saneforce.godairy.common.LocationFinder;
import com.saneforce.godairy.databinding.ActivityAddNewDistributorBinding;
import com.saneforce.godairy.universal.UniversalDropDownAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewDistributor extends AppCompatActivity implements OnMapReadyCallback {
    ActivityAddNewDistributorBinding binding;

    TextView select_sales_office_name, select_route_name, select_channel, select_state, date_of_creation, downloadGSTDeclarationForm, uidType, downloadTCSDeclarationForm, purchaseType, submit, select_bank_details, select_agreement_copy, select_sub_channel, selectReportingVerticals, downloadfssaiDeclarationForm, fssaiFromDate, fssaiToDate;
    ImageView refreshLocation, display_customer_photo, capture_customer_photo, display_shop_photo, capture_shop_photo, display_bank_details, capture_bank_details, display_fssai, capture_fssai, display_gst, capture_gst, display_agreement_copy, capture_agreement_copy, home, display_aadhaar_number, capture_aadhaar_number, display_pan_number, capture_pan_number, gstInfo, previewGSTDeclaration, captureGSTDeclaration, tcsInfo, previewTCSDeclaration, captureTCSDeclaration, fssaiInfo, previewfssaiDeclaration, capturefssaiDeclaration, capture_customer_application, display_customer_application;
    EditText type_city, type_pincode, type_name_of_the_customer, type_name_of_the_owner, type_mobile_number, type_email_id, type_pan_name, type_sales_executive_name, type_sales_executive_employee_id, type_aadhaar_number, type_pan_number, type_gst, type_fssai, businessAddressNo, businessAddressCity, businessAddressPincode, ownerAddressNo, ownerAddressCity, ownerAddressPincode;
    LinearLayout gstDeclarationLL, gstLL, tcsDeclarationLL, fssaiDeclarationLL, fssaiLL;
    SwitchMaterial gstSwitch, tcsSwitch, fssaiSwitch;

    Context context = this;
    Common_Class common_class;
    Shared_Common_Pref pref;
    SharedPreferences UserDetails;

    CommonAdapterForDropdown adapter;
    CommonAdapterForDropdownWithFilter filterAdapter;
    ArrayList<CommonModelForDropDown> ChannelList, stateList, BankList, AgreementList, MOPList;
    ArrayList<CommonModelWithThreeString> regionList;
    ArrayList<CommonModelWithFourString> officeList, filteredOfficeList, tempOfficeList, routeList, filteredRouteList, tempRouteList;

    GoogleMap googleMap;
    JSONArray subChannelResponse, filteredSubChannel, cusACGroupResponse, distChannelResponse, salesDivisionResponse, MasDistrictArray, filteredMasDistrictArray, MasCusSalRegionArray, MasSalesGroupArray, filteredMasSalesGroupArray, MasCusGroupArray, MasBusinessTypeArray, MasBusinessDivisionArray, MasCusClassArray, MasReportingVertArray, uidTypeArray, MasSubMarketArray, MasCusTypeArray, stateArray;

    DownloadReceiver downloadReceiver;
    DatePickerDialog fromDatePickerDialog;

    double Lat = 0, Long = 0;
    String customer_photo_name = "", shop_photo_name = "", customerApplicationImageName = "", stateCodeStr = "", stateNameStr = "", officeCodeStr = "", officeNameStr = "", routeCodeStr = "", routeNameStr = "", channelIDStr = "", channelStr = "", subChannelNameStr = "", ReportingVerticalsID = "", ReportingVerticalsStr = "", cityStr = "", customerNameStr = "", ownerNameStr = "", businessAddressNoStr = "", businessAddressCityStr = "", businessAddressPincodeStr = "", pincodeStr = "", ownerAddressNoStr = "", ownerAddressCityStr = "", ownerAddressPincodeStr = "", mobileNumberStr = "", emailAddressStr = "", executiveNameStr = "", employeeIdStr = "", UIDType = "", aadhaarStr = "", aadhaarImageName = "", PANStr = "", panImageName = "", PANName = "", bankDetailsStr = "", bankImageName = "", FSSAIDetailsStr = "", fssaiFromStr = "", fssaitoStr = "", FSSAIDeclarationImageName = "", GSTDetailsStr = "", gstDeclarationImageName = "", tcsDeclarationImageName = "", agreementDetailsStr = "", agreementImageName = "", purchaseTypeID = "", purchaseTypeName = "", FSSAIDeclarationImageFullPath = "", gstDeclarationImageFullPath = "", tcsDeclarationImageFullPath = "", stockistCode = "", customer_photo_url = "", shop_photo_url = "", aadhaarImageFullPath = "", panImageFullPath = "", bankImageFullPath = "", agreementImageFullPath = "", customerApplicationImageFullPath = "", subChannelIDStr = "", doorNo = "", street = "", city = "", district = "", state = "", country = "", pincode = "", feature = "";
    boolean isEditMode = false;
    ArrayList<String> FSSAIList, GSTList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewDistributorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        select_sales_office_name = findViewById(R.id.select_sales_office_name);
        select_route_name = findViewById(R.id.select_route_name);
        select_channel = findViewById(R.id.select_channel);
        select_sub_channel = findViewById(R.id.select_sub_channel);
        selectReportingVerticals = findViewById(R.id.selectReportingVerticals);
        select_state = findViewById(R.id.select_state);
        date_of_creation = findViewById(R.id.date_of_creation);
        submit = findViewById(R.id.submit);
        display_customer_photo = findViewById(R.id.display_customer_photo);
        capture_customer_photo = findViewById(R.id.capture_customer_photo);
        display_shop_photo = findViewById(R.id.display_shop_photo);
        capture_shop_photo = findViewById(R.id.capture_shop_photo);
        display_bank_details = findViewById(R.id.display_bank_details);
        capture_bank_details = findViewById(R.id.capture_bank_details);
        display_fssai = findViewById(R.id.display_fssai);
        capture_fssai = findViewById(R.id.capture_fssai);
        display_gst = findViewById(R.id.display_gst);
        capture_gst = findViewById(R.id.capture_gst);
        type_gst = findViewById(R.id.type_gst);
        display_agreement_copy = findViewById(R.id.display_agreement_copy);
        capture_agreement_copy = findViewById(R.id.capture_agreement_copy);
        home = findViewById(R.id.toolbar_home);
        type_city = findViewById(R.id.type_city);
        type_pincode = findViewById(R.id.type_pincode);
        type_name_of_the_customer = findViewById(R.id.type_name_of_the_customer);
        type_name_of_the_owner = findViewById(R.id.type_name_of_the_owner);
        businessAddressNo = findViewById(R.id.businessAddressNo);
        businessAddressCity = findViewById(R.id.businessAddressCity);
        businessAddressPincode = findViewById(R.id.businessAddressPincode);
        type_mobile_number = findViewById(R.id.type_mobile_number);
        type_email_id = findViewById(R.id.type_email_id);
        type_sales_executive_name = findViewById(R.id.type_sales_executive_name);
        type_sales_executive_employee_id = findViewById(R.id.type_sales_executive_employee_id);
        type_aadhaar_number = findViewById(R.id.type_aadhaar_number);
        type_pan_number = findViewById(R.id.type_pan_number);
        select_bank_details = findViewById(R.id.select_bank_details);
        select_agreement_copy = findViewById(R.id.select_agreement_copy);
        type_fssai = findViewById(R.id.type_fssai);
        display_aadhaar_number = findViewById(R.id.display_aadhaar_number);
        capture_aadhaar_number = findViewById(R.id.capture_aadhaar_number);
        display_pan_number = findViewById(R.id.display_pan_number);
        capture_pan_number = findViewById(R.id.capture_pan_number);
        refreshLocation = findViewById(R.id.refreshLocation);
        gstSwitch = findViewById(R.id.gstSwitch);
        tcsSwitch = findViewById(R.id.tcsSwitch);
        gstDeclarationLL = findViewById(R.id.gstDeclarationLL);
        gstLL = findViewById(R.id.gstLL);
        downloadGSTDeclarationForm = findViewById(R.id.downloadGSTDeclarationForm);
        gstInfo = findViewById(R.id.gstInfo);
        previewGSTDeclaration = findViewById(R.id.previewGSTDeclaration);
        captureGSTDeclaration = findViewById(R.id.captureGSTDeclaration);
        downloadTCSDeclarationForm = findViewById(R.id.downloadTCSDeclarationForm);
        tcsInfo = findViewById(R.id.tcsInfo);
        previewTCSDeclaration = findViewById(R.id.previewTCSDeclaration);
        captureTCSDeclaration = findViewById(R.id.captureTCSDeclaration);
        tcsDeclarationLL = findViewById(R.id.tcsDeclarationLL);
        fssaiDeclarationLL = findViewById(R.id.fssaiDeclarationLL);
        fssaiLL = findViewById(R.id.fssaiLL);
        fssaiSwitch = findViewById(R.id.fssaiSwitch);
        downloadfssaiDeclarationForm = findViewById(R.id.downloadfssaiDeclarationForm);
        fssaiInfo = findViewById(R.id.fssaiInfo);
        previewfssaiDeclaration = findViewById(R.id.previewfssaiDeclaration);
        capturefssaiDeclaration = findViewById(R.id.capturefssaiDeclaration);
        capture_customer_application = findViewById(R.id.capture_customer_application);
        display_customer_application = findViewById(R.id.display_customer_application);
        fssaiFromDate = findViewById(R.id.fssaiFromDate);
        fssaiToDate = findViewById(R.id.fssaiToDate);
        ownerAddressNo = findViewById(R.id.ownerAddressNo);
        ownerAddressCity = findViewById(R.id.ownerAddressCity);
        ownerAddressPincode = findViewById(R.id.ownerAddressPincode);
        purchaseType = findViewById(R.id.purchaseType);
        type_pan_name = findViewById(R.id.type_pan_name);
        uidType = findViewById(R.id.uidType);

        common_class = new Common_Class(this);
        pref = new Shared_Common_Pref(this);
        UserDetails = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        common_class.gotoHomeScreen(context, home);

        fssaiSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                previewfssaiDeclaration.setVisibility(View.GONE);
                fssaiDeclarationLL.setVisibility(View.GONE);
                FSSAIDeclarationImageName = "";
                FSSAIDeclarationImageFullPath = "";
                fssaiLL.setVisibility(View.VISIBLE);
            } else {
                fssaiFromDate.setText("");
                fssaiFromStr = "";
                fssaiToDate.setText("");
                fssaitoStr = "";
                type_fssai.setText("");
                FSSAIDetailsStr = "";
                FSSAIList.clear();
                fssaiLL.setVisibility(View.GONE);
                fssaiDeclarationLL.setVisibility(View.VISIBLE);
            }
        });

        gstSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                gstDeclarationImageName = "";
                gstDeclarationImageFullPath = "";
                previewGSTDeclaration.setVisibility(View.GONE);
                gstDeclarationLL.setVisibility(View.GONE);
                gstLL.setVisibility(View.VISIBLE);
            } else {
                type_gst.setText("");
                GSTDetailsStr = "";
                GSTList.clear();
                gstLL.setVisibility(View.GONE);
                gstDeclarationLL.setVisibility(View.VISIBLE);
            }
        });

        fssaiFromDate.setOnClickListener(v -> {
            Calendar newCalendar = Calendar.getInstance();
            fromDatePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
                Calendar fromCal = Calendar.getInstance();
                fromCal.set(year, monthOfYear, dayOfMonth);
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(fromCal.getTime());
                fssaiFromDate.setText(date);
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            fromDatePickerDialog.show();
        });

        fssaiToDate.setOnClickListener(v -> {
            String fromDate = fssaiFromDate.getText().toString();
            if (fromDate.isEmpty()) {
                Toast.makeText(context, "Please select from date", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Date date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(fromDate);
                    Calendar toCal = Calendar.getInstance();
                    toCal.setTime(date);
                    fromDatePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
                        Calendar refCal = Calendar.getInstance();
                        refCal.set(year, monthOfYear, dayOfMonth);
                        String dates = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(refCal.getTime());
                        fssaiToDate.setText(dates);
                    }, toCal.get(Calendar.YEAR), toCal.get(Calendar.MONTH), toCal.get(Calendar.DAY_OF_MONTH));
                    fromDatePickerDialog.getDatePicker().setMinDate(toCal.getTimeInMillis());
                    fromDatePickerDialog.show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        tcsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                previewTCSDeclaration.setVisibility(View.GONE);
                tcsDeclarationImageName = "";
                tcsDeclarationImageFullPath = "";
                tcsDeclarationLL.setVisibility(View.GONE);
            } else {
                tcsDeclarationLL.setVisibility(View.VISIBLE);
            }
        });

        downloadGSTDeclarationForm.setOnClickListener(v -> {
            Long downloadID = FileDownloader.downloadFile(context, "https://thirumala.salesjump.in/Downloads/THIRUMALA/GST%20Non-Regsitration%20Declaration.docx", "GST Non-Registration Declaration.docx");

        });

        downloadTCSDeclarationForm.setOnClickListener(v -> {
            Long downloadID = FileDownloader.downloadFile(context, "https://thirumala.salesjump.in/Downloads/THIRUMALA/TCS%20Declaration.docx", "TCS Declaration.docx");
        });

        downloadfssaiDeclarationForm.setOnClickListener(v -> {
            Long downloadID = FileDownloader.downloadFile(context, "https://thirumala.salesjump.in/Downloads/THIRUMALA/Undertaking%20FSSAI%20license.docx", "Undertaking FSSAI license.docx");
        });

        if (getIntent().hasExtra("id")) {
            binding.headtext.setText("View Distributor");
            stockistCode = getIntent().getStringExtra("id");
            Log.e("stockistCode", stockistCode);
            getStockistInfo();
        } else {
            getLocation();
            type_sales_executive_name.setText(UserDetails.getString("SfName", ""));
            type_sales_executive_employee_id.setText(UserDetails.getString("EmpId", ""));
        }

        if (getIntent().hasExtra("flag")) {
            int flag = getIntent().getIntExtra("flag", 0);
            if (flag == 3) {
                isEditMode = true;
                binding.headtext.setText("Edit Distributor");
                submit.setVisibility(View.VISIBLE);
                submit.setText("Re-Submit");
            } else {
                isEditMode = false;
                MakeFieldsEnabled(false);
                submit.setVisibility(View.GONE);
            }
        }

        regionList = new ArrayList<>();
        ChannelList = new ArrayList<>();
        stateList = new ArrayList<>();
        BankList = new ArrayList<>();
        AgreementList = new ArrayList<>();
        MOPList = new ArrayList<>();
        officeList = new ArrayList<>();
        filteredOfficeList = new ArrayList<>();
        tempOfficeList = new ArrayList<>();
        routeList = new ArrayList<>();
        filteredRouteList = new ArrayList<>();
        tempRouteList = new ArrayList<>();
        FSSAIList = new ArrayList<>();
        GSTList = new ArrayList<>();

        subChannelResponse = new JSONArray();
        filteredSubChannel = new JSONArray();
        cusACGroupResponse = new JSONArray();
        distChannelResponse = new JSONArray();
        salesDivisionResponse = new JSONArray();
        MasDistrictArray = new JSONArray();
        filteredMasDistrictArray = new JSONArray();
        MasCusSalRegionArray = new JSONArray();
        MasSalesGroupArray = new JSONArray();
        filteredMasSalesGroupArray = new JSONArray();
        MasCusGroupArray = new JSONArray();
        MasBusinessTypeArray = new JSONArray();
        MasBusinessDivisionArray = new JSONArray();
        MasCusClassArray = new JSONArray();
        MasReportingVertArray = new JSONArray();
        uidTypeArray = new JSONArray();
        MasSubMarketArray = new JSONArray();
        MasCusTypeArray = new JSONArray();
        stateArray = new JSONArray();

        capture_customer_photo.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    customer_photo_name = FileName;
                    customer_photo_url = fullPath;
                    display_customer_photo.setImageBitmap(image);
                    display_customer_photo.setVisibility(View.VISIBLE);
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info");
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_shop_photo.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    shop_photo_name = FileName;
                    shop_photo_url = fullPath;
                    display_shop_photo.setImageBitmap(image);
                    display_shop_photo.setVisibility(View.VISIBLE);
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info");
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_aadhaar_number.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    aadhaarImageName = FileName;
                    aadhaarImageFullPath = fullPath;
                    display_aadhaar_number.setImageBitmap(image);
                    display_aadhaar_number.setVisibility(View.VISIBLE);
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info");
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_pan_number.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    panImageName = FileName;
                    panImageFullPath = fullPath;
                    display_pan_number.setImageBitmap(image);
                    display_pan_number.setVisibility(View.VISIBLE);
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info");
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_bank_details.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    bankImageName = FileName;
                    bankImageFullPath = fullPath;
                    display_bank_details.setImageBitmap(image);
                    display_bank_details.setVisibility(View.VISIBLE);
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info");
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_fssai.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    FSSAIList.add(FileName);
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info");
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_gst.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info");
                    GSTList.add(FileName);
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_agreement_copy.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    agreementImageName = FileName;
                    agreementImageFullPath = fullPath;
                    display_agreement_copy.setImageBitmap(image);
                    display_agreement_copy.setVisibility(View.VISIBLE);
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info");
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        captureGSTDeclaration.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    gstDeclarationImageName = FileName;
                    gstDeclarationImageFullPath = fullPath;
                    previewGSTDeclaration.setImageBitmap(image);
                    previewGSTDeclaration.setVisibility(View.VISIBLE);
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info");
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        captureTCSDeclaration.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    tcsDeclarationImageName = FileName;
                    tcsDeclarationImageFullPath = fullPath;
                    previewTCSDeclaration.setImageBitmap(image);
                    previewTCSDeclaration.setVisibility(View.VISIBLE);
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info");
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capturefssaiDeclaration.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    FSSAIDeclarationImageName = FileName;
                    FSSAIDeclarationImageFullPath = fullPath;
                    previewfssaiDeclaration.setImageBitmap(image);
                    previewfssaiDeclaration.setVisibility(View.VISIBLE);
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info");
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_customer_application.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    customerApplicationImageName = FileName;
                    customerApplicationImageFullPath = fullPath;
                    display_customer_application.setImageBitmap(image);
                    display_customer_application.setVisibility(View.VISIBLE);
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info");
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });

        gstInfo.setOnClickListener(v -> {
            MyAlertDialog.show(context, "", "Download the GST Declaration form.", true, "Close", "", new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        });

        tcsInfo.setOnClickListener(v -> {
            MyAlertDialog.show(context, "", "Download the TCS Declaration form.", true, "Close", "", new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        });

        fssaiInfo.setOnClickListener(v -> {
            MyAlertDialog.show(context, "", "Download the FSSAI Declaration form.", true, "Close", "", new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        });

        previewGSTDeclaration.setOnClickListener(v -> {
            previewGSTDeclaration.setEnabled(false);
            new Handler().postDelayed(() -> previewGSTDeclaration.setEnabled(true), 1500);
            showImage(gstDeclarationImageFullPath);
        });
        previewTCSDeclaration.setOnClickListener(v -> {
            previewTCSDeclaration.setEnabled(false);
            new Handler().postDelayed(() -> previewTCSDeclaration.setEnabled(true), 1500);
            showImage(tcsDeclarationImageFullPath);
        });
        previewfssaiDeclaration.setOnClickListener(v -> {
            previewfssaiDeclaration.setEnabled(false);
            new Handler().postDelayed(() -> previewfssaiDeclaration.setEnabled(true), 1500);
            showImage(FSSAIDeclarationImageFullPath);

        });
        display_customer_application.setOnClickListener(v -> {
            display_customer_application.setEnabled(false);
            new Handler().postDelayed(() -> display_customer_application.setEnabled(true), 1500);
            showImage(customerApplicationImageFullPath);

        });
        display_customer_photo.setOnClickListener(v -> {
            display_customer_photo.setEnabled(false);
            new Handler().postDelayed(() -> display_customer_photo.setEnabled(true), 1500);
            showImage(customer_photo_url);
        });
        display_shop_photo.setOnClickListener(v -> {
            display_shop_photo.setEnabled(false);
            new Handler().postDelayed(() -> display_shop_photo.setEnabled(true), 1500);
            showImage(shop_photo_url);
        });
        display_bank_details.setOnClickListener(v -> {
            display_bank_details.setEnabled(false);
            new Handler().postDelayed(() -> display_bank_details.setEnabled(true), 1500);
            showImage(bankImageFullPath);
        });
        display_fssai.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_show_multiple_images, null, false);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            TextView title = view.findViewById(R.id.title);
            ImageView close = view.findViewById(R.id.close);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            title.setText("FSSAI Images");
            close.setOnClickListener(v1 -> dialog.dismiss());
            recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
            recyclerView.setAdapter(new AdapterShowMultipleImages(context, FSSAIList));
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
        });
        display_gst.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_show_multiple_images, null, false);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            TextView title = view.findViewById(R.id.title);
            ImageView close = view.findViewById(R.id.close);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            title.setText("GST Images");
            close.setOnClickListener(v1 -> dialog.dismiss());
            recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
            recyclerView.setAdapter(new AdapterShowMultipleImages(context, GSTList));
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
        });
        display_agreement_copy.setOnClickListener(v -> {
            display_agreement_copy.setEnabled(false);
            new Handler().postDelayed(() -> display_agreement_copy.setEnabled(true), 1500);
            showImage(agreementImageFullPath);
        });
        display_aadhaar_number.setOnClickListener(v -> {
            display_aadhaar_number.setEnabled(false);
            new Handler().postDelayed(() -> display_aadhaar_number.setEnabled(true), 1500);
            showImage(aadhaarImageFullPath);
        });
        display_pan_number.setOnClickListener(v -> {
            display_pan_number.setEnabled(false);
            new Handler().postDelayed(() -> display_pan_number.setEnabled(true), 1500);
            showImage(panImageFullPath);
        });
        select_state.setOnClickListener(v -> {
            if (stateArray.length() == 0) {
                Toast.makeText(context, "No states found!", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select State");
            AlertDialog dialog = builder.create();
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, stateArray);
            adapter.setOnItemClick(position -> {
                try {
                    select_state.setText(stateArray.getJSONObject(position).getString("title"));
                    stateCodeStr = stateArray.getJSONObject(position).getString("id");
                    MyProgressDialog.dismiss();
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            recyclerView1.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        select_sales_office_name.setOnClickListener(v -> {
            if (officeList.isEmpty()) {
                Toast.makeText(context, "No offices found for the selected state", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv_and_filter, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Sales Office");
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
                    tempOfficeList.clear();
                    for (CommonModelWithFourString modelWithThreeString : officeList) {
                        if (modelWithThreeString.getTitle().toLowerCase().contains(s.toString().toLowerCase().trim()) || modelWithThreeString.getId().toLowerCase().contains(s.toString().toLowerCase().trim())) {
                            tempOfficeList.add(modelWithThreeString);
                        }
                    }
                    setAdapterForOffice(tempOfficeList, dialog, recyclerView1);
                }
            });
            close.setOnClickListener(v1 -> dialog.dismiss());
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            setAdapterForOffice(officeList, dialog, recyclerView1);
            dialog.show();
        });
        select_route_name.setOnClickListener(v -> {
            if (TextUtils.isEmpty(select_sales_office_name.getText().toString().trim())) {
                Toast.makeText(context, "Please Select Sales Office Name", Toast.LENGTH_SHORT).show();
                return;
            } else if (filteredRouteList.isEmpty()) {
                Toast.makeText(context, "No Routes found for the selected Office", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv_and_filter, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Route");
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
                    tempRouteList.clear();
                    for (CommonModelWithFourString modelWithThreeString : filteredRouteList) {
                        if (modelWithThreeString.getTitle().toLowerCase().contains(s.toString().toLowerCase().trim()) || modelWithThreeString.getId().toLowerCase().contains(s.toString().toLowerCase().trim())) {
                            tempRouteList.add(modelWithThreeString);
                        }
                    }
                    setAdapterForRoute(tempRouteList, dialog, recyclerView1);
                }
            });
            close.setOnClickListener(v1 -> dialog.dismiss());
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            setAdapterForRoute(filteredRouteList, dialog, recyclerView1);
            dialog.show();


        });
        select_channel.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Channel");
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            adapter = new CommonAdapterForDropdown(ChannelList, context);
            recyclerView1.setAdapter(adapter);
            AlertDialog dialog = builder.create();
            adapter.setSelectItem((model, position) -> {
                channelIDStr = model.getId();
                select_channel.setText(model.getTitle());
                filteredSubChannel = new JSONArray();
                subChannelIDStr = "";
                subChannelNameStr = "";
                select_sub_channel.setText("");
                for (int i = 0; i < subChannelResponse.length(); i++) {
                    try {
                        if (subChannelResponse.getJSONObject(i).getString("CateCode").equals(model.getId())) {
                            JSONObject object = new JSONObject();
                            object.put("id", subChannelResponse.getJSONObject(i).getString("id"));
                            object.put("title", subChannelResponse.getJSONObject(i).getString("title"));
                            filteredSubChannel.put(object);
                        }
                    } catch (JSONException ignored) {
                    }
                }
                dialog.dismiss();
            });
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        select_sub_channel.setOnClickListener(v -> {
            if (select_channel.getText().toString().isEmpty()) {
                Toast.makeText(context, "Please select channel", Toast.LENGTH_SHORT).show();
                return;
            }
            if (filteredSubChannel.length() == 0) {
                Toast.makeText(context, "No sub channel found for the selected channel", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Sub Channel");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, filteredSubChannel);
            adapter.setOnItemClick(position -> {
                try {
                    subChannelIDStr = filteredSubChannel.getJSONObject(position).getString("id");
                    subChannelNameStr = filteredSubChannel.getJSONObject(position).getString("title");
                    select_sub_channel.setText(filteredSubChannel.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        selectReportingVerticals.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Verticals");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, MasReportingVertArray);
            adapter.setOnItemClick(position -> {
                try {
                    ReportingVerticalsID = MasReportingVertArray.getJSONObject(position).getString("id");
                    ReportingVerticalsStr = MasReportingVertArray.getJSONObject(position).getString("title");
                    selectReportingVerticals.setText(MasReportingVertArray.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        try {
            uidTypeArray.put(new JSONObject().put("title", "Aadhaar"));
            uidTypeArray.put(new JSONObject().put("title", "Without Aadhaar"));
        } catch (Exception ignored) {

        }
        uidType.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select UID Type");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, uidTypeArray);
            adapter.setOnItemClick(position -> {
                try {
                    UIDType = uidTypeArray.getJSONObject(position).getString("title");
                    uidType.setText(UIDType);
                    if (!UIDType.equals("Aadhaar")) {
                        binding.aadhaarLL.setVisibility(View.GONE);
                        aadhaarStr = "";
                        aadhaarImageName = "";
                        aadhaarImageFullPath = "";
                        binding.typeAadhaarNumber.setText("");
                        binding.displayAadhaarNumber.setVisibility(View.GONE);
                    } else {
                        binding.aadhaarLL.setVisibility(View.VISIBLE);
                    }
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        JSONArray purchaseTypeArray = new JSONArray();
        JSONObject object = new JSONObject();
        try {
            object.put("id", "1");
            object.put("title", "Advance Amount");
            purchaseTypeArray.put(object);
            object = new JSONObject();
            object.put("id", "2");
            object.put("title", "Credit Limit");
            purchaseTypeArray.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        purchaseType.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Purchase Type");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, purchaseTypeArray);
            adapter.setOnItemClick(position -> {
                try {
                    purchaseTypeID = purchaseTypeArray.getJSONObject(position).getString("id");
                    purchaseTypeName = purchaseTypeArray.getJSONObject(position).getString("title");
                    purchaseType.setText(purchaseTypeArray.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        select_bank_details.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Bank Details");
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            adapter = new CommonAdapterForDropdown(BankList, context);
            recyclerView1.setAdapter(adapter);
            AlertDialog dialog = builder.create();
            adapter.setSelectItem((model, position) -> {
                select_bank_details.setText(model.getTitle());
                dialog.dismiss();
            });
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        select_agreement_copy.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Agreement Copy");
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            adapter = new CommonAdapterForDropdown(AgreementList, context);
            recyclerView1.setAdapter(adapter);
            AlertDialog dialog = builder.create();
            adapter.setSelectItem((model, position) -> {
                select_agreement_copy.setText(model.getTitle());
                dialog.dismiss();
            });
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });

        select_agreement_copy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    binding.clearAgreement.setVisibility(View.GONE);
                } else {
                    binding.clearAgreement.setVisibility(View.VISIBLE);
                }
            }
        });

        select_bank_details.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    binding.clearBank.setVisibility(View.GONE);
                } else {
                    binding.clearBank.setVisibility(View.VISIBLE);
                }
            }
        });

        refreshLocation.setOnClickListener(v -> getLocation());
        submit.setOnClickListener(v -> ValidateFields());

        type_sales_executive_name.setEnabled(false);
        type_sales_executive_employee_id.setEnabled(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        PrepareDropdownLists();

        downloadReceiver = new DownloadReceiver();
        registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        binding.clearBank.setOnClickListener(v -> {
            bankDetailsStr = "";
            bankImageName = "";
            bankImageFullPath = "";
            select_bank_details.setText("");
            display_bank_details.setVisibility(View.GONE);
        });

        binding.clearAgreement.setOnClickListener(v -> {
            agreementDetailsStr = "";
            agreementImageName = "";
            aadhaarImageFullPath = "";
            select_agreement_copy.setText("");
            display_agreement_copy.setVisibility(View.GONE);
        });
    }

    private void MakeFieldsEnabled(boolean isEnabled) {
        capture_customer_photo.setEnabled(isEnabled);
        capture_shop_photo.setEnabled(isEnabled);
        capture_customer_application.setEnabled(isEnabled);
        select_state.setEnabled(isEnabled);
        select_sales_office_name.setEnabled(isEnabled);
        select_route_name.setEnabled(isEnabled);
        select_channel.setEnabled(isEnabled);
        select_sub_channel.setEnabled(isEnabled);
        selectReportingVerticals.setEnabled(isEnabled);
        type_city.setEnabled(isEnabled);
        type_name_of_the_customer.setEnabled(isEnabled);
        type_name_of_the_owner.setEnabled(isEnabled);
        businessAddressNo.setEnabled(isEnabled);
        businessAddressCity.setEnabled(isEnabled);
        businessAddressPincode.setEnabled(isEnabled);
        type_pincode.setEnabled(isEnabled);
        ownerAddressNo.setEnabled(isEnabled);
        ownerAddressCity.setEnabled(isEnabled);
        ownerAddressPincode.setEnabled(isEnabled);
        type_mobile_number.setEnabled(isEnabled);
        type_email_id.setEnabled(isEnabled);
        uidType.setEnabled(isEnabled);
        type_aadhaar_number.setEnabled(isEnabled);
        capture_aadhaar_number.setEnabled(isEnabled);
        type_pan_number.setEnabled(isEnabled);
        capture_pan_number.setEnabled(isEnabled);
        type_pan_name.setEnabled(isEnabled);
        select_bank_details.setEnabled(isEnabled);
        capture_bank_details.setEnabled(isEnabled);
        fssaiSwitch.setEnabled(isEnabled);
        type_fssai.setEnabled(isEnabled);
        capture_fssai.setEnabled(isEnabled);
        fssaiFromDate.setEnabled(isEnabled);
        fssaiToDate.setEnabled(isEnabled);
        capturefssaiDeclaration.setEnabled(isEnabled);
        gstSwitch.setEnabled(isEnabled);
        type_gst.setEnabled(isEnabled);
        capture_gst.setEnabled(isEnabled);
        captureGSTDeclaration.setEnabled(isEnabled);
        tcsSwitch.setEnabled(isEnabled);
        captureTCSDeclaration.setEnabled(isEnabled);
        select_agreement_copy.setEnabled(isEnabled);
        capture_agreement_copy.setEnabled(isEnabled);
        purchaseType.setEnabled(isEnabled);

        binding.refreshLocation.setVisibility(View.GONE);
    }

    private void getStockistInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_stockist_info");
        params.put("stockistCode", stockistCode);
        Common_Class.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    AssignStockistInfo(jsonObject.getJSONObject("response"));
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(String error) {
                Log.e("getStockistInfo", error);
            }
        });
    }

    private void AssignStockistInfo(JSONObject jsonObject) {
        Log.e("AssignStockistInfo", jsonObject.toString());

        try {
            customer_photo_name = jsonObject.optString("Cust_Photo");
            common_class.getImageFromS3Bucket(context, customer_photo_name, "stockist_info", (bmp, path) -> {
                display_customer_photo.setImageBitmap(bmp);
                display_customer_photo.setVisibility(View.VISIBLE);
                customer_photo_url = path;
            });

            shop_photo_name = jsonObject.optString("Shop_Photo");
            common_class.getImageFromS3Bucket(context, shop_photo_name, "stockist_info", (bmp, path) -> {
                display_shop_photo.setImageBitmap(bmp);
                display_shop_photo.setVisibility(View.VISIBLE);
                shop_photo_url = path;
            });

            customerApplicationImageName = jsonObject.optString("custAppImg");
            common_class.getImageFromS3Bucket(context, customerApplicationImageName, "stockist_info", (bmp, path) -> {
                display_customer_application.setImageBitmap(bmp);
                display_customer_application.setVisibility(View.VISIBLE);
                customerApplicationImageFullPath = path;
            });

            stateCodeStr = jsonObject.optString("state_Code");
            stateNameStr = jsonObject.optString("state_Name");
            select_state.setText(stateNameStr);

            officeCodeStr = jsonObject.optString("Plant_id");
            officeNameStr = jsonObject.optString("Sales_Offc_Name");
            select_sales_office_name.setText(officeNameStr);

            routeCodeStr = jsonObject.optString("RouteCode");
            routeNameStr = jsonObject.optString("Route_Name");
            select_route_name.setText(routeNameStr);

            channelIDStr = jsonObject.optString("Dis_Cat_Code");
            channelStr = jsonObject.optString("Dis_Cat_Name");
            select_channel.setText(channelStr);

            subChannelIDStr = jsonObject.optString("SubCat_channel");
            subChannelNameStr = jsonObject.optString("CateNm");
            select_sub_channel.setText(subChannelNameStr);

            ReportingVerticalsID = jsonObject.optString("RepVertID");
            ReportingVerticalsStr = jsonObject.optString("RepVertName");
            selectReportingVerticals.setText(ReportingVerticalsStr);

            cityStr = jsonObject.optString("city");
            type_city.setText(cityStr);

            customerNameStr = jsonObject.optString("Stockist_Name");
            type_name_of_the_customer.setText(customerNameStr);

            ownerNameStr = jsonObject.optString("Stockist_ContactPerson");
            type_name_of_the_owner.setText(ownerNameStr);

            Lat = jsonObject.optDouble("Lat");
            Long = jsonObject.optDouble("Lng");
            SetMap();

            businessAddressNoStr = jsonObject.optString("Bus_Add1");
            businessAddressNo.setText(businessAddressNoStr);
            businessAddressCityStr = jsonObject.optString("Bus_Add2");
            businessAddressCity.setText(businessAddressCityStr);
            businessAddressPincodeStr = jsonObject.optString("Bus_Add3");
            businessAddressPincode.setText(businessAddressPincodeStr);
            pincodeStr = jsonObject.optString("pincode");
            type_pincode.setText(pincodeStr);

            ownerAddressNoStr = jsonObject.optString("Own_Add1");
            ownerAddressNo.setText(ownerAddressNoStr);
            ownerAddressCityStr = jsonObject.optString("Own_Add2");
            ownerAddressCity.setText(ownerAddressCityStr);
            ownerAddressPincodeStr = jsonObject.optString("Own_Add3");
            ownerAddressPincode.setText(ownerAddressPincodeStr);

            mobileNumberStr = jsonObject.optString("mobile");
            type_mobile_number.setText(mobileNumberStr);

            emailAddressStr = jsonObject.optString("email");
            type_email_id.setText(emailAddressStr);

            executiveNameStr = jsonObject.optString("Field_Name");
            type_sales_executive_name.setText(executiveNameStr);

            String Field_Code = jsonObject.optString("Field_Code");

            employeeIdStr = jsonObject.optString("Emp_ID");
            type_sales_executive_employee_id.setText(employeeIdStr);

            UIDType = jsonObject.optString("UID_Type");
            uidType.setText(UIDType);
            if (!UIDType.equals("Aadhaar")) {
                binding.aadhaarLL.setVisibility(View.GONE);
            } else {
                aadhaarStr = jsonObject.optString("Aadhaar");
                type_aadhaar_number.setText(aadhaarStr);

                aadhaarImageName = jsonObject.optString("AadhaarImg");
                common_class.getImageFromS3Bucket(context, aadhaarImageName, "stockist_info", (bmp, path) -> {
                    display_aadhaar_number.setImageBitmap(bmp);
                    display_aadhaar_number.setVisibility(View.VISIBLE);
                    aadhaarImageFullPath = path;
                });
            }

            PANStr = jsonObject.optString("Pan");
            type_pan_number.setText(PANStr);

            panImageName = jsonObject.optString("PanImg");
            common_class.getImageFromS3Bucket(context, panImageName, "stockist_info", (bmp, path) -> {
                display_pan_number.setImageBitmap(bmp);
                display_pan_number.setVisibility(View.VISIBLE);
                panImageFullPath = path;
            });

            PANName = jsonObject.optString("Pan_Name");
            type_pan_name.setText(PANName);

            bankDetailsStr = jsonObject.optString("BankAccNo");
            select_bank_details.setText(bankDetailsStr);

            bankImageName = jsonObject.optString("BankAccImg");
            common_class.getImageFromS3Bucket(context, bankImageName, "stockist_info", (bmp, path) -> {
                display_bank_details.setImageBitmap(bmp);
                display_bank_details.setVisibility(View.VISIBLE);
                bankImageFullPath = path;
            });

            String have_fssai = jsonObject.optString("have_fssai");
            fssaiSwitch.setChecked(have_fssai.equals("1"));

            FSSAIDetailsStr = jsonObject.optString("Fssai");
            type_fssai.setText(FSSAIDetailsStr);

            String FSSAIImageName = jsonObject.optString("FssaiImg");
            FSSAIList.clear();
            FSSAIList.addAll(Arrays.asList(FSSAIImageName.split(",")));

            fssaiFromStr = jsonObject.optString("fssaiFrom");
            fssaiFromDate.setText(fssaiFromStr);

            fssaitoStr = jsonObject.optString("fssaiTo");
            fssaiToDate.setText(fssaitoStr);

            FSSAIDeclarationImageName = jsonObject.optString("fssaiDecImg");
            common_class.getImageFromS3Bucket(context, FSSAIDeclarationImageName, "stockist_info", (bmp, path) -> {
                previewfssaiDeclaration.setImageBitmap(bmp);
                previewfssaiDeclaration.setVisibility(View.VISIBLE);
                FSSAIDeclarationImageFullPath = path;
            });

            String GST_type = jsonObject.optString("GST_type");
            gstSwitch.setChecked(GST_type.equals("1"));

            GSTDetailsStr = jsonObject.optString("Gst");
            type_gst.setText(GSTDetailsStr);

            String GSTImageName = jsonObject.optString("GstImg");
            GSTList.clear();
            GSTList.addAll(Arrays.asList(GSTImageName.split(",")));

            gstDeclarationImageName = jsonObject.optString("gstDecImg");
            common_class.getImageFromS3Bucket(context, gstDeclarationImageName, "stockist_info", (bmp, path) -> {
                previewGSTDeclaration.setImageBitmap(bmp);
                previewGSTDeclaration.setVisibility(View.VISIBLE);
                gstDeclarationImageFullPath = path;
            });

            String have_tcs = jsonObject.optString("have_tcs");
            tcsSwitch.setChecked(have_tcs.equals("0"));

            tcsDeclarationImageName = jsonObject.optString("tcsDecImg");
            common_class.getImageFromS3Bucket(context, tcsDeclarationImageName, "stockist_info", (bmp, path) -> {
                previewTCSDeclaration.setImageBitmap(bmp);
                previewTCSDeclaration.setVisibility(View.VISIBLE);
                tcsDeclarationImageFullPath = path;
            });

            agreementDetailsStr = jsonObject.optString("Agreement");
            select_agreement_copy.setText(agreementDetailsStr);

            agreementImageName = jsonObject.optString("AgreementImg");
            common_class.getImageFromS3Bucket(context, agreementImageName, "stockist_info", (bmp, path) -> {
                display_agreement_copy.setImageBitmap(bmp);
                display_agreement_copy.setVisibility(View.VISIBLE);
                agreementImageFullPath = path;
            });

            purchaseTypeID = jsonObject.optString("purchaseTypeId");
            purchaseTypeName = jsonObject.optString("purchaseType");
            purchaseType.setText(purchaseTypeName);

            binding.clearBank.setVisibility(View.GONE);
            binding.clearAgreement.setVisibility(View.GONE);

        } catch (Exception e) {
            Toast.makeText(context, "Json parsing error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocation() {
        new LocationFinder(this, location -> {
            try {
                Lat = location.getLatitude();
                Long = location.getLongitude();
                getCompleteAddressString(Lat, Long);
                SetMap();
            } catch (Exception ignored) {
            }
        });
    }

    private void setAdapterForRoute(ArrayList<CommonModelWithFourString> filteredRouteList, AlertDialog dialog, RecyclerView recyclerView1) {
        filterAdapter = new CommonAdapterForDropdownWithFilter(filteredRouteList, context);
        recyclerView1.setAdapter(filterAdapter);
        filterAdapter.setSelectItem((model, position) -> {
            dialog.dismiss();
            routeCodeStr = model.getId();
            select_route_name.setText(model.getTitle());
        });
    }

    private void setAdapterForOffice(ArrayList<CommonModelWithFourString> list, AlertDialog dialog, RecyclerView recyclerView1) {
        filterAdapter = new CommonAdapterForDropdownWithFilter(list, context);
        recyclerView1.setAdapter(filterAdapter);
        filterAdapter.setSelectItem((model, position) -> {
            dialog.dismiss();
            officeCodeStr = model.getId();
            select_sales_office_name.setText(model.getTitle());
            routeCodeStr = "";
            String routeReference = model.getRouteReference();
            select_route_name.setText("");
            filteredRouteList.clear();
            for (CommonModelWithFourString modelWithThreeString : routeList) {
                if (modelWithThreeString.getRouteReference().equalsIgnoreCase(routeReference)) {
                    filteredRouteList.add(modelWithThreeString);
                }
            }
            filteredMasSalesGroupArray = new JSONArray();
            for (int i = 0; i < MasSalesGroupArray.length(); i++) {
                try {
                    if (MasSalesGroupArray.getJSONObject(i).getString("PlantCode").equals(officeCodeStr)) {
                        filteredMasSalesGroupArray.put(MasSalesGroupArray.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void PrepareDropdownLists() {
        BankList.add(new CommonModelForDropDown("1", "Passbook"));
        BankList.add(new CommonModelForDropDown("2", "Cheque"));

        AgreementList.add(new CommonModelForDropDown("1", "TDC Agreement"));
        AgreementList.add(new CommonModelForDropDown("2", "TOT"));

        MOPList.add(new CommonModelForDropDown("1", "App"));

        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_dist_dropdown_lists");
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = service.universalAPIRequest(params, "");
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() & response.body() != null) {
                        String result = response.body().string();
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("success")) {
                            new Thread(() -> {
                                try {
                                    officeList.clear();
                                    routeList.clear();
                                    ChannelList.clear();
                                    JSONArray officeResponse = object.getJSONArray("officeResponse");
                                    for (int i = 0; i < officeResponse.length(); i++) {
                                        String id = officeResponse.getJSONObject(i).getString("sOffCode");
                                        String title = officeResponse.getJSONObject(i).getString("sOffName");
                                        String regionReference = officeResponse.getJSONObject(i).getString("StateCode");
                                        String officeReference = officeResponse.getJSONObject(i).getString("PlantId");
                                        officeList.add(new CommonModelWithFourString(id, title, regionReference, officeReference));
                                    }
                                    JSONArray routeResponse = object.getJSONArray("routeResponse");
                                    for (int i = 0; i < routeResponse.length(); i++) {
                                        String id = routeResponse.getJSONObject(i).getString("Route_ID");
                                        String title = routeResponse.getJSONObject(i).getString("Route_Name");
                                        String officeReference = routeResponse.getJSONObject(i).getString("Plant_Code");
                                        routeList.add(new CommonModelWithFourString(id, title, "", officeReference));
                                    }
                                    JSONArray channelResponse = object.getJSONArray("channelResponse");
                                    for (int i = 0; i < channelResponse.length(); i++) {
                                        String id = channelResponse.getJSONObject(i).getString("CateId");
                                        String title = channelResponse.getJSONObject(i).getString("CateNm");
                                        ChannelList.add(new CommonModelForDropDown(id, title));
                                    }
                                    subChannelResponse = object.getJSONArray("subChannelResponse");
                                    stateArray = object.getJSONArray("MasState");
                                    MasReportingVertArray = object.getJSONArray("MasReportingVert");
                                } catch (JSONException ignored) {
                                }
                            }).start();
                        } else {
                            Toast.makeText(context, "Response is false", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Response is null", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showImage(String url) {
        Intent show = new Intent(context, ProductImageView.class);
        show.putExtra("ImageUrl", Uri.fromFile(new File(url)).toString());
        startActivity(show);
    }

    private void ValidateFields() {
        stateNameStr = select_state.getText().toString().trim();
        stateNameStr = Common_Class.validateField(stateNameStr);

        officeNameStr = select_sales_office_name.getText().toString().trim();
        officeNameStr = Common_Class.validateField(officeNameStr);

        routeNameStr = select_route_name.getText().toString().trim();
        routeNameStr = Common_Class.validateField(routeNameStr);

        channelStr = select_channel.getText().toString().trim();
        channelStr = Common_Class.validateField(channelStr);

        subChannelNameStr = select_sub_channel.getText().toString().trim();
        subChannelNameStr = Common_Class.validateField(subChannelNameStr);

        ReportingVerticalsStr = selectReportingVerticals.getText().toString().trim();
        ReportingVerticalsStr = Common_Class.validateField(ReportingVerticalsStr);

        cityStr = type_city.getText().toString().trim();
        cityStr = Common_Class.validateField(cityStr);

        customerNameStr = type_name_of_the_customer.getText().toString().trim();
        customerNameStr = Common_Class.validateField(customerNameStr);

        ownerNameStr = type_name_of_the_owner.getText().toString().trim();
        ownerNameStr = Common_Class.validateField(ownerNameStr);

        businessAddressNoStr = businessAddressNo.getText().toString().trim();
        businessAddressNoStr = Common_Class.validateField(businessAddressNoStr);

        businessAddressCityStr = businessAddressCity.getText().toString().trim();
        businessAddressCityStr = Common_Class.validateField(businessAddressCityStr);

        businessAddressPincodeStr = businessAddressPincode.getText().toString().trim();
        businessAddressPincodeStr = Common_Class.validateField(businessAddressPincodeStr);

        pincodeStr = type_pincode.getText().toString().trim();
        pincodeStr = Common_Class.validateField(pincodeStr);

        ownerAddressNoStr = ownerAddressNo.getText().toString().trim();
        ownerAddressNoStr = Common_Class.validateField(ownerAddressNoStr);

        ownerAddressCityStr = ownerAddressCity.getText().toString().trim();
        ownerAddressCityStr = Common_Class.validateField(ownerAddressCityStr);

        ownerAddressPincodeStr = ownerAddressPincode.getText().toString().trim();
        ownerAddressPincodeStr = Common_Class.validateField(ownerAddressPincodeStr);

        mobileNumberStr = type_mobile_number.getText().toString().trim();
        mobileNumberStr = Common_Class.validateField(mobileNumberStr);

        emailAddressStr = type_email_id.getText().toString().trim();
        emailAddressStr = Common_Class.validateField(emailAddressStr);

        executiveNameStr = type_sales_executive_name.getText().toString().trim();
        executiveNameStr = Common_Class.validateField(executiveNameStr);

        employeeIdStr = type_sales_executive_employee_id.getText().toString().trim();
        employeeIdStr = Common_Class.validateField(employeeIdStr);

        UIDType = uidType.getText().toString().trim();
        UIDType = Common_Class.validateField(UIDType);

        aadhaarStr = type_aadhaar_number.getText().toString().trim();
        aadhaarStr = Common_Class.validateField(aadhaarStr);

        PANStr = type_pan_number.getText().toString().trim();
        PANStr = Common_Class.validateField(PANStr);

        PANName = type_pan_name.getText().toString().trim();
        PANName = Common_Class.validateField(PANName);

        bankDetailsStr = select_bank_details.getText().toString().trim();
        bankDetailsStr = Common_Class.validateField(bankDetailsStr);

        fssaiFromStr = fssaiFromDate.getText().toString().trim();
        fssaiFromStr = Common_Class.validateField(fssaiFromStr);

        fssaitoStr = fssaiToDate.getText().toString().trim();
        fssaitoStr = Common_Class.validateField(fssaitoStr);

        FSSAIDetailsStr = type_fssai.getText().toString().trim();
        FSSAIDetailsStr = Common_Class.validateField(FSSAIDetailsStr);

        GSTDetailsStr = type_gst.getText().toString().trim();
        GSTDetailsStr = Common_Class.validateField(GSTDetailsStr);

        agreementDetailsStr = select_agreement_copy.getText().toString().trim();
        agreementDetailsStr = Common_Class.validateField(agreementDetailsStr);

        purchaseTypeName = purchaseType.getText().toString().trim();
        purchaseTypeName = Common_Class.validateField(purchaseTypeName);

        if (TextUtils.isEmpty(customer_photo_name) || TextUtils.isEmpty(customer_photo_url)) {
            Toast.makeText(context, "Please Capture Customer Photo", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(shop_photo_name) || TextUtils.isEmpty(shop_photo_url)) {
            Toast.makeText(context, "Please Capture Shop Photo", Toast.LENGTH_SHORT).show();
        } else if (customerApplicationImageName.isEmpty() || customerApplicationImageFullPath.isEmpty()) {
            Toast.makeText(context, "Please capture customer application photo", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(stateCodeStr) || TextUtils.isEmpty(stateNameStr)) {
            Toast.makeText(context, "Please Select the State", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(officeNameStr) || TextUtils.isEmpty(officeCodeStr)) {
            Toast.makeText(context, "Please Select the Sales Office", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(routeNameStr) || TextUtils.isEmpty(routeCodeStr)) {
            Toast.makeText(context, "Please Select the Route Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(channelStr) || TextUtils.isEmpty(channelIDStr)) {
            Toast.makeText(context, "Please Select the Channel", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(subChannelIDStr) || TextUtils.isEmpty(subChannelNameStr)) {
            Toast.makeText(context, "Please Select Sub Channel", Toast.LENGTH_SHORT).show();
        } else if (ReportingVerticalsID.isEmpty() || ReportingVerticalsStr.isEmpty()) {
            Toast.makeText(context, "Please Select Verticals", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cityStr)) {
            Toast.makeText(context, "Please Select the City", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(customerNameStr)) {
            Toast.makeText(context, "Please Enter the Business Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ownerNameStr)) {
            Toast.makeText(context, "Please Enter the Owner Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(businessAddressNoStr) || TextUtils.isEmpty(businessAddressCityStr) || TextUtils.isEmpty(businessAddressPincodeStr)) {
            Toast.makeText(context, "Please Enter the Business Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pincodeStr) || pincodeStr.length() != 6) {
            Toast.makeText(context, "Please Enter 6 digit Pincode", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ownerAddressNoStr) || TextUtils.isEmpty(ownerAddressCityStr) || TextUtils.isEmpty(ownerAddressPincodeStr)) {
            Toast.makeText(context, "Please Enter the Owner Address", Toast.LENGTH_SHORT).show();
        } else if (ownerAddressPincodeStr.length() != 6) {
            Toast.makeText(context, "Please Enter 6 digit pincode", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mobileNumberStr) || mobileNumberStr.length() != 10) {
            Toast.makeText(context, "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(executiveNameStr)) {
            Toast.makeText(context, "Please Enter the Sales Executive Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(employeeIdStr)) {
            Toast.makeText(context, "Please Enter the Sales Executive - Employee ID", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(UIDType)) {
            Toast.makeText(context, "Please select UID type", Toast.LENGTH_SHORT).show();
        } else if (UIDType.equals("Aadhaar") && TextUtils.isEmpty(aadhaarStr)) {
            Toast.makeText(context, "Please Enter the Aadhaar Number", Toast.LENGTH_SHORT).show();
        } else if (UIDType.equals("Aadhaar") && aadhaarStr.length() != 12) {
            Toast.makeText(context, "Please Enter 12 digit Aadhaar Number", Toast.LENGTH_SHORT).show();
        } else if (UIDType.equals("Aadhaar") && (TextUtils.isEmpty(aadhaarImageName) || TextUtils.isEmpty(aadhaarImageFullPath))) {
            Toast.makeText(context, "Please Capture the Aadhaar Image", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(PANStr)) {
            Toast.makeText(context, "Please Enter the PAN Number", Toast.LENGTH_SHORT).show();
        } else if (PANStr.length() != 10) {
            Toast.makeText(context, "Please Enter 10 digit PAN Number", Toast.LENGTH_SHORT).show();
        } else if (!PANStr.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
            Toast.makeText(context, "Please Enter valid PAN Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(panImageName) || TextUtils.isEmpty(panImageFullPath)) {
            Toast.makeText(context, "Please Capture the PAN Image", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(PANName)) {
            Toast.makeText(context, "Please enter PAN Name", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(bankDetailsStr) && bankImageName.isEmpty()) {
            Toast.makeText(context, "Please Capture the Bank Details Image", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(bankDetailsStr) && !bankImageName.isEmpty()) {
            Toast.makeText(context, "Please Enter Bank Details", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(FSSAIDetailsStr) && FSSAIDetailsStr.length() != 14 && fssaiSwitch.isChecked()) {
            Toast.makeText(context, "Please Enter 14 digit FSSAI Number", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(FSSAIDetailsStr) && FSSAIDetailsStr.length() == 14 && fssaiSwitch.isChecked() && FSSAIList.isEmpty()) {
            Toast.makeText(context, "Please capture FSSAI License Certificate", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(FSSAIDetailsStr) && FSSAIDetailsStr.length() == 14 && fssaiSwitch.isChecked() && (fssaiFromStr.isEmpty() || fssaitoStr.isEmpty())) {
            Toast.makeText(context, "Please select FSSAI valid dates", Toast.LENGTH_SHORT).show();
        } else if (!fssaiSwitch.isChecked() && FSSAIDeclarationImageName.isEmpty()) {
            Toast.makeText(context, "Please capture FSSAI declaration", Toast.LENGTH_SHORT).show();
        } else if (gstSwitch.isChecked() && GSTDetailsStr.length() != 15) {
            Toast.makeText(context, "Please Enter 15 digit GST Number", Toast.LENGTH_SHORT).show();
        } else if (gstSwitch.isChecked() && (!GSTDetailsStr.matches("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[0-9]{1}[A-Z]{1}[0-9A-Z]{1}$"))) {
            Toast.makeText(context, "Please Enter valid GST Number", Toast.LENGTH_SHORT).show();
        } else if (gstSwitch.isChecked() && (GSTList.isEmpty())) {
            Toast.makeText(context, "Please Capture the GST Certificate", Toast.LENGTH_SHORT).show();
        } else if (!gstSwitch.isChecked() && (TextUtils.isEmpty(gstDeclarationImageName) || TextUtils.isEmpty(gstDeclarationImageFullPath))) {
            Toast.makeText(context, "Please Capture the GST Declaration Certificate", Toast.LENGTH_SHORT).show();
        } else if ((Lat == 0) || (Long == 0)) {
            Toast.makeText(context, "Location can't be fetched", Toast.LENGTH_SHORT).show();
        } else if (!tcsSwitch.isChecked() && tcsDeclarationImageName.isEmpty()) {
            Toast.makeText(context, "Please capture the TCS Declaration", Toast.LENGTH_SHORT).show();
        } else if (agreementDetailsStr.isEmpty() && !agreementImageName.isEmpty()) {
            Toast.makeText(context, "Please select agreement copy", Toast.LENGTH_SHORT).show();
        } else if (!agreementDetailsStr.isEmpty() && agreementImageName.isEmpty()) {
            Toast.makeText(context, "Please capture the agreement copy", Toast.LENGTH_SHORT).show();
        } else if (purchaseTypeID.isEmpty() || purchaseTypeName.isEmpty()) {
            Toast.makeText(context, "Please select purchase type", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (isEditMode) {
                builder.setMessage("Are you sure you want to re-submit?");
            } else {
                builder.setMessage("Are you sure you want to submit?");
            }
            builder.setCancelable(false);
            builder.setPositiveButton("YES", (dialog, which) -> SubmitForm());
            builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        }
    }

    private void SubmitForm() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        if (isEditMode) {
            progressDialog.setMessage("Re-Submitting Distributor...");
        } else {
            progressDialog.setMessage("Creating Distributor...");
        }
        progressDialog.setCancelable(false);
        progressDialog.show();

        JSONArray data = new JSONArray();
        JSONObject object = new JSONObject();

        try {
            object.put("sfCode", UserDetails.getString("Sfcode", ""));

            object.put("customer_photo_name", customer_photo_name);

            object.put("shop_photo_name", shop_photo_name);

            object.put("stateCodeStr", stateCodeStr);
            object.put("stateNameStr", stateNameStr);

            object.put("officeCodeStr", officeCodeStr);
            object.put("officeNameStr", officeNameStr);

            object.put("routeCodeStr", routeCodeStr);
            object.put("routeNameStr", routeNameStr);

            object.put("channelIDStr", channelIDStr);
            object.put("channelStr", channelStr);

            object.put("subChannelID", subChannelIDStr);
            object.put("subChannelName", subChannelNameStr);

            object.put("ReportingVerticalsID", ReportingVerticalsID);
            object.put("ReportingVerticalsStr", ReportingVerticalsStr);

            object.put("cityStr", cityStr);

            object.put("customerNameStr", customerNameStr);

            object.put("ownerNameStr", ownerNameStr);

            object.put("lat", Lat);
            object.put("long", Long);

            object.put("businessAddressNoStr", businessAddressNoStr);
            object.put("businessAddressCityStr", businessAddressCityStr);
            object.put("businessAddressPincodeStr", businessAddressPincodeStr);
            object.put("pincodeStr", pincodeStr);

            object.put("ownerAddressNoStr", ownerAddressNoStr);
            object.put("ownerAddressCityStr", ownerAddressCityStr);
            object.put("ownerAddressPincodeStr", ownerAddressPincodeStr);

            object.put("mobileNumberStr", mobileNumberStr);

            object.put("emailAddressStr", emailAddressStr);

            object.put("executiveNameStr", executiveNameStr);

            object.put("employeeIdStr", employeeIdStr);

            object.put("creationDateStr", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime()));

            object.put("UIDType", UIDType);

            object.put("aadhaarStr", aadhaarStr);
            object.put("aadhaarImage", aadhaarImageName);

            object.put("PANStr", PANStr);
            object.put("panImage", panImageName);

            object.put("PANName", PANName);

            object.put("bankDetailsStr", bankDetailsStr);
            object.put("bankImageName", bankImageName);

            object.put("fssaiStatus", fssaiSwitch.isChecked() ? "1" : "0");
            object.put("FSSAIDetailsStr", FSSAIDetailsStr);

            StringBuilder FSSAIImageName = new StringBuilder();
            for (String name : FSSAIList) {
                FSSAIImageName.append(",").append(name);
            }
            String fssaiImageName = FSSAIImageName.toString();
            if (fssaiImageName.length() > 0) {
                fssaiImageName = fssaiImageName.substring(1);
            }
            object.put("FSSAIImageName", fssaiImageName);
            object.put("fssaiFromStr", fssaiFromStr);
            object.put("fssaitoStr", fssaitoStr);
            object.put("FSSAIDeclarationImageName", FSSAIDeclarationImageName);
            object.put("gstStatus", gstSwitch.isChecked() ? "1" : "0");
            object.put("GSTDetailsStr", GSTDetailsStr);
            StringBuilder GSTImageName = new StringBuilder();
            for (String name : GSTList) {
                GSTImageName.append(",").append(name);
            }
            String gstImageName = GSTImageName.toString();
            if (gstImageName.length() > 0) {
                gstImageName = gstImageName.substring(1);
            }
            object.put("GSTImageName", gstImageName);
            object.put("gstDeclarationImageName", gstDeclarationImageName);
            object.put("tcsStatus", tcsSwitch.isChecked() ? "0" : "1");
            object.put("tcsDeclarationImageName", tcsDeclarationImageName);

            object.put("agreementDetailsStr", agreementDetailsStr);
            object.put("agreementImageName", agreementImageName);

            object.put("purchaseTypeId", purchaseTypeID);
            object.put("purchaseTypeName", purchaseTypeName);

            object.put("customerApplicationImageName", customerApplicationImageName);

        } catch (JSONException e) {
            progressDialog.dismiss();
            Toast.makeText(context, "Json Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        data.put(object);
        Log.e("JSONData", data.toString());
        Map<String, String> params = new HashMap<>();
        if (isEditMode) {
            params.put("axn", "update_distributor");
            params.put("stockistCode", stockistCode);
        } else {
            params.put("axn", "save/new_distributor");
        }
        Log.e("JSONData", params.toString());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.universalAPIRequest(params, data.toString());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() == null) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Response is Null", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String result = response.body().string();
                        Log.e("API Response", result);
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("success")) {
                            progressDialog.dismiss();
                            MyAlertDialog.show(context, "", jsonObject.getString("response"), false, "Close", "", new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    Reports_Distributor_Name.refresh = true;
                                    finish();
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, jsonObject.getString("response"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Error while parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Response Not Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
                if (addresses != null && addresses.size() > 0) {
                    Address returnedAddress = addresses.get(0);
                    Log.e("returnedAddress", returnedAddress.toString());
                    doorNo = returnedAddress.getSubThoroughfare();
                    street = returnedAddress.getThoroughfare();
                    feature = returnedAddress.getFeatureName();
                    city = returnedAddress.getLocality();
                    district = returnedAddress.getSubAdminArea();
                    state = returnedAddress.getAdminArea();
                    country = returnedAddress.getCountryName();
                    pincode = returnedAddress.getPostalCode();

                    runOnUiThread(() -> {
                        if (!Common_Class.isNullOrEmpty(city)) {
                            type_city.setText(city);
                        }
                        if (!Common_Class.isNullOrEmpty(pincode)) {
                            type_pincode.setText(pincode);
                        }

                        if (Common_Class.isNullOrEmpty(doorNo)) doorNo = "";
                        if (Common_Class.isNullOrEmpty(street)) street = "";
                        if (Common_Class.isNullOrEmpty(feature)) feature = "";

                        String line1 = doorNo + ", " + street + ", " + feature;
                        line1 = line1.replace(", , ", ", ");
                        if (line1.startsWith(", ")) line1 = line1.substring(2);
                        if (line1.endsWith(", ")) line1 = line1.substring(0, line1.length()- 3);
                        businessAddressNo.setText(line1);

                        if (!Common_Class.isNullOrEmpty(city) && !Common_Class.isNullOrEmpty(district)) {
                            businessAddressCity.setText(city + ", " + district);
                        } else if (!Common_Class.isNullOrEmpty(city)) {
                            businessAddressCity.setText(city);
                        } else if (!Common_Class.isNullOrEmpty(district)) {
                            businessAddressCity.setText(district);
                        }

                        if (!Common_Class.isNullOrEmpty(state) && !Common_Class.isNullOrEmpty(country)) {
                            businessAddressPincode.setText(state + ", " + country);
                        } else if (!Common_Class.isNullOrEmpty(state)) {
                            businessAddressPincode.setText(state);
                        } else if (!Common_Class.isNullOrEmpty(country)) {
                            businessAddressPincode.setText(country);
                        }
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(context, "Map Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        SetMap();
    }

    private void SetMap() {
        LatLng userLocation = new LatLng(Lat, Long);
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(userLocation).title("Your are here"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadReceiver != null) {
            unregisterReceiver(downloadReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        MyAlertDialog.show(context, "", "Are you sure you want to go back?", true, "Yes", "No", new AlertBox() {
            @Override
            public void PositiveMethod(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }

            @Override
            public void NegativeMethod(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }
}