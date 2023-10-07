package com.saneforce.milksales.SFA_Activity;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.saneforce.milksales.Activity_Hap.AllowancCapture;
import com.saneforce.milksales.Activity_Hap.ProductImageView;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.DownloadReceiver;
import com.saneforce.milksales.Common_Class.FileDownloader;
import com.saneforce.milksales.Common_Class.MyAlertDialog;
import com.saneforce.milksales.Common_Class.MyProgressDialog;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.OnImagePickListener;
import com.saneforce.milksales.Interface.UpdateResponseUI;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.CommonAdapterForDropdown;
import com.saneforce.milksales.SFA_Adapter.CommonAdapterForDropdownWithFilter;
import com.saneforce.milksales.SFA_Adapter.RegionAdapter;
import com.saneforce.milksales.SFA_Model_Class.CommonModelForDropDown;
import com.saneforce.milksales.SFA_Model_Class.CommonModelWithFourString;
import com.saneforce.milksales.SFA_Model_Class.CommonModelWithThreeString;
import com.saneforce.milksales.common.FileUploadService;
import com.saneforce.milksales.common.LocationFinder;
import com.saneforce.milksales.universal.UniversalDropDownAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewDistributor extends AppCompatActivity implements OnMapReadyCallback {

    TextView /*select_region,*/ select_sales_office_name, select_route_name, select_channel, select_state, date_of_creation, downloadGSTDeclarationForm, downloadTCSDeclarationForm, purchaseType,
            /*select_mode_of_payment,*/ submit, select_bank_details, select_agreement_copy, select_sub_channel, selectCusACGroup, select_dist_channel, select_sales_division, /*selectDistrict,*/ selectSalesRegion, selectBusinessType, selectCustomerGroup, selectSalesGroup, selectBusinessDivision, selectCustomerClass, selectCustomerType, selectReportingVerticals, selectSubMarket, downloadfssaiDeclarationForm, fssaiFromDate, fssaiToDate;
    ImageView refreshLocation, display_customer_photo, capture_customer_photo, display_shop_photo, capture_shop_photo, display_bank_details, capture_bank_details, display_fssai, capture_fssai,
            display_gst, capture_gst, display_agreement_copy, capture_agreement_copy, /*display_deposit,*/ /*capture_deposit,*/ home, display_aadhaar_number, capture_aadhaar_number,
            display_pan_number, capture_pan_number, gstInfo, previewGSTDeclaration, captureGSTDeclaration, tcsInfo, previewTCSDeclaration, captureTCSDeclaration, fssaiInfo, previewfssaiDeclaration, capturefssaiDeclaration;
    EditText type_city, type_pincode, type_name_of_the_customer, type_name_of_the_owner, /*type_address_of_the_shop,*/ /*type_residence_address,*/ type_mobile_number, type_email_id, type_pan_name, uidType,
            type_sales_executive_name, type_sales_executive_employee_id, type_aadhaar_number, type_pan_number, type_gst, /*type_deposit,*/ type_fssai, businessAddressNo, businessAddressCity, businessAddressPincode, ownerAddressNo, ownerAddressCity, ownerAddressPincode;
    LinearLayout gstDeclarationLL, gstLL, tcsDeclarationLL, fssaiDeclarationLL, fssaiLL;

    Context context = this;
    Common_Class common_class;
    Shared_Common_Pref pref;
    SharedPreferences UserDetails;

    CommonAdapterForDropdown adapter;
    RegionAdapter regionAdapter;
    CommonAdapterForDropdownWithFilter filterAdapter;
    ArrayList<CommonModelForDropDown> ChannelList, stateList, BankList, AgreementList, MOPList;
    ArrayList<CommonModelWithThreeString> regionList/*, regionFilteredList*/;
    ArrayList<CommonModelWithFourString> officeList, filteredOfficeList, tempOfficeList, routeList, filteredRouteList, tempRouteList;

    String customer_photo_url = "", customer_photo_name = "", shop_photo_url = "", shop_photo_name = "", /*regionStr = "", regionCodeStr = "",*/ officeCodeStr = "", officeNameStr = "", routeCodeStr = "",
            routeNameStr = "", channelStr = "", channelIDStr = "", cityStr = "", pincodeStr = "", stateCodeStr = "", stateNameStr = "", customerNameStr = "", ownerNameStr = "", /*shopAddressStr = "",*/
    /*residenceAddressStr = "",*/ mobileNumberStr = "", emailAddressStr = "", executiveNameStr = "", employeeIdStr = "", creationDateStr = "", aadhaarStr = "",
            PANStr = "", bankDetailsStr = "", bankImageName = "", bankImageFullPath = "", FSSAIDetailsStr = "", FSSAIImageName = "", FSSAIImageFullPath = "", FSSAIDeclarationImageName = "", FSSAIDeclarationImageFullPath = "",
            GSTDetailsStr = "", GSTImageName = "", GSTImageFullPath = "", agreementDetailsStr = "", agreementImageName = "", agreementImageFullPath = "", /*modeOfPaymentStr = "",*/
            /*depositDetailsStr = "",*/ /*depositImageName = "",*/ tcsDeclarationImageName = "", tcsDeclarationImageFullPath = "", gstDeclarationImageName = "", gstDeclarationImageFullPath = "", depositImageFullPath = "", aadhaarImageName = "", aadhaarImageFullPath = "", panImageName = "", panImageFullPath = "", /*DistrictID = "",*/
            SalesRegionID = "", BusinessTypeID = "", CustomerGroupID = "", SalesGroupID = "", BusinessDivisionID = "", CustomerClassID = "", CustomerTypeID = "", ReportingVerticalsID = "",
            SubMarketID = "", /*DistrictStr = "",*/ SalesRegionStr = "", BusinessTypeStr = "", CustomerGroupStr = "", SalesGroupStr = "", BusinessDivisionStr = "", CustomerClassStr = "", purchaseTypeID = "", purchaseTypeName = "",
            CustomerTypeStr = "", ReportingVerticalsStr = "", SubMarketStr = "", businessAddressNoStr = "", businessAddressCityStr = "", businessAddressPincodeStr = "", ownerAddressNoStr = "", ownerAddressCityStr = "", ownerAddressPincodeStr = "",
            fssaiFromStr = "", fssaitoStr = "", PANName = "", UIDType = "";

    double Lat = 0, Long = 0;

    GoogleMap googleMap;
    JSONArray subChannelResponse, filteredSubChannel, cusACGroupResponse, distChannelResponse, salesDivisionResponse, MasDistrictArray, filteredMasDistrictArray, MasCusSalRegionArray, MasSalesGroupArray, filteredMasSalesGroupArray, MasCusGroupArray, MasBusinessTypeArray, MasBusinessDivisionArray, MasCusClassArray, MasReportingVertArray, MasSubMarketArray, MasCusTypeArray, stateArray;
    String subChannelIDStr = "", subChannelNameStr = "", acGroupIDStr = "", acGroupNameStr = "", distChannelIDStr = "", distChannelNameStr = "", salesDivisionIDStr = "", salesDivisionNameStr = "";
    SwitchMaterial gstSwitch, tcsSwitch, fssaiSwitch;

    DownloadReceiver downloadReceiver;
    DatePickerDialog fromDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_distributor);

        //select_region = findViewById(R.id.select_region);
        select_sales_office_name = findViewById(R.id.select_sales_office_name);
        select_route_name = findViewById(R.id.select_route_name);
        select_channel = findViewById(R.id.select_channel);
        select_sub_channel = findViewById(R.id.select_sub_channel);
        selectCusACGroup = findViewById(R.id.selectCusACGroup);
        select_dist_channel = findViewById(R.id.select_dist_channel);
        select_sales_division = findViewById(R.id.select_sales_division);
        //selectDistrict = findViewById(R.id.selectDistrict);
        selectSalesRegion = findViewById(R.id.selectSalesRegion);
        selectBusinessType = findViewById(R.id.selectBusinessType);
        selectCustomerGroup = findViewById(R.id.selectCustomerGroup);
        selectSalesGroup = findViewById(R.id.selectSalesGroup);
        selectBusinessDivision = findViewById(R.id.selectBusinessDivision);
        selectCustomerClass = findViewById(R.id.selectCustomerClass);
        selectCustomerType = findViewById(R.id.selectCustomerType);
        selectReportingVerticals = findViewById(R.id.selectReportingVerticals);
        selectSubMarket = findViewById(R.id.selectSubMarket);
        select_state = findViewById(R.id.select_state);
        date_of_creation = findViewById(R.id.date_of_creation);
//        select_mode_of_payment = findViewById(R.id.select_mode_of_payment);
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
//        type_deposit = findViewById(R.id.type_deposit);
        display_agreement_copy = findViewById(R.id.display_agreement_copy);
        capture_agreement_copy = findViewById(R.id.capture_agreement_copy);
//        display_deposit = findViewById(R.id.display_deposit);
//        capture_deposit = findViewById(R.id.capture_deposit);
        home = findViewById(R.id.toolbar_home);
        type_city = findViewById(R.id.type_city);
        type_pincode = findViewById(R.id.type_pincode);
        type_name_of_the_customer = findViewById(R.id.type_name_of_the_customer);
        type_name_of_the_owner = findViewById(R.id.type_name_of_the_owner);
        businessAddressNo = findViewById(R.id.businessAddressNo);
        businessAddressCity = findViewById(R.id.businessAddressCity);
        businessAddressPincode = findViewById(R.id.businessAddressPincode);
//        type_residence_address = findViewById(R.id.type_residence_address);
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
                fssaiLL.setVisibility(View.VISIBLE);
                fssaiDeclarationLL.setVisibility(View.GONE);
            } else {
                fssaiLL.setVisibility(View.GONE);
                fssaiDeclarationLL.setVisibility(View.VISIBLE);
            }
        });

        gstSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                gstLL.setVisibility(View.VISIBLE);
                gstDeclarationLL.setVisibility(View.GONE);
            } else {
                gstLL.setVisibility(View.GONE);
                gstDeclarationLL.setVisibility(View.VISIBLE);
            }
        });

        fssaiFromDate.setOnClickListener(v -> {
            Calendar newCalendar = Calendar.getInstance();
            fromDatePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
                int month = monthOfYear + 1;
                String date = ("" + year + "-" + month + "-" + dayOfMonth);
                fssaiFromDate.setText(date);
                fssaiToDate.setEnabled(true);
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            fromDatePickerDialog.show();
        });

        fssaiToDate.setOnClickListener(v -> {
            Calendar newCalendar = Calendar.getInstance();
            fromDatePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
                int month = monthOfYear + 1;
                String date = ("" + year + "-" + month + "-" + dayOfMonth);
                fssaiToDate.setText(date);
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            fromDatePickerDialog.show();
        });

        tcsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tcsDeclarationLL.setVisibility(View.GONE);
            } else {
                tcsDeclarationLL.setVisibility(View.VISIBLE);
            }
        });

        downloadGSTDeclarationForm.setOnClickListener(v -> {
            downloadReceiver = new DownloadReceiver();
            Long downloadID = FileDownloader.downloadFile(context, "https://thirumala.salesjump.in/Downloads/THIRUMALA/GST%20Non-Regsitration%20Declaration.docx", "GST Non-Registration Declaration.docx");
            registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        });

        downloadTCSDeclarationForm.setOnClickListener(v -> {
            downloadReceiver = new DownloadReceiver();
            Long downloadID = FileDownloader.downloadFile(context, "https://thirumala.salesjump.in/Downloads/THIRUMALA/TCS%20Declaration.docx", "TCS Declaration.docx");
            registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        });

        downloadfssaiDeclarationForm.setOnClickListener(v -> {
            downloadReceiver = new DownloadReceiver();
            Long downloadID = FileDownloader.downloadFile(context, "https://thirumala.salesjump.in/Downloads/THIRUMALA/Undertaking%20FSSAI%20license.docx", "Undertaking FSSAI license.docx");
            registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        });

        regionList = new ArrayList<>();
//        regionFilteredList = new ArrayList<>();
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

        capture_customer_photo.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    customer_photo_name = FileName;
                    customer_photo_url = fullPath;
                    display_customer_photo.setImageBitmap(image);
                    display_customer_photo.setVisibility(View.VISIBLE);
                    uploadImage(customer_photo_name, customer_photo_url);

                    // Todo: Upload Image to S3
                    // com.saneforce.milksales.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "milk_selfie");
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
                    uploadImage(shop_photo_name, shop_photo_url);
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
                    uploadImage(aadhaarImageName, aadhaarImageFullPath);
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
                    uploadImage(panImageName, panImageFullPath);
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
                    uploadImage(bankImageName, bankImageFullPath);
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_fssai.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    FSSAIImageName = FileName;
                    FSSAIImageFullPath = fullPath;
                    display_fssai.setImageBitmap(image);
                    display_fssai.setVisibility(View.VISIBLE);
                    uploadImage(FSSAIImageName, FSSAIImageFullPath);
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_gst.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    GSTImageName = FileName;
                    GSTImageFullPath = fullPath;
                    display_gst.setImageBitmap(image);
                    display_gst.setVisibility(View.VISIBLE);
                    uploadImage(GSTImageName, GSTImageFullPath);
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
                    uploadImage(agreementImageName, agreementImageFullPath);
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        /*capture_deposit.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    depositImageName = FileName;
                    depositImageFullPath = fullPath;
                    display_deposit.setImageBitmap(image);
                    display_deposit.setVisibility(View.VISIBLE);
                    uploadImage(depositImageName, depositImageFullPath);
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });*/
        captureGSTDeclaration.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    gstDeclarationImageName = FileName;
                    gstDeclarationImageFullPath = fullPath;
                    previewGSTDeclaration.setImageBitmap(image);
                    previewGSTDeclaration.setVisibility(View.VISIBLE);
                    uploadImage(gstDeclarationImageName, gstDeclarationImageFullPath);
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
                    uploadImage(tcsDeclarationImageName, tcsDeclarationImageFullPath);
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
                    uploadImage(FSSAIDeclarationImageName, FSSAIDeclarationImageFullPath);
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
            display_fssai.setEnabled(false);
            new Handler().postDelayed(() -> display_fssai.setEnabled(true), 1500);
            showImage(FSSAIImageFullPath);
        });
        display_gst.setOnClickListener(v -> {
            display_gst.setEnabled(false);
            new Handler().postDelayed(() -> display_gst.setEnabled(true), 1500);
            showImage(GSTImageFullPath);
        });
        display_agreement_copy.setOnClickListener(v -> {
            display_agreement_copy.setEnabled(false);
            new Handler().postDelayed(() -> display_agreement_copy.setEnabled(true), 1500);
            showImage(agreementImageFullPath);
        });
        /*display_deposit.setOnClickListener(v -> {
            display_deposit.setEnabled(false);
            new Handler().postDelayed(() -> display_deposit.setEnabled(true), 1500);
            showImage(depositImageFullPath);
        });*/
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
                    /*regionCodeStr = "";
                    regionFilteredList.clear();*/
                    filteredMasDistrictArray = new JSONArray();

                    officeCodeStr = "";
                    select_sales_office_name.setText("");
                    filteredOfficeList.clear();

                    for (CommonModelWithFourString modelWithThreeString : officeList) {
                        if (modelWithThreeString.getRegionReference().equalsIgnoreCase(stateCodeStr)) {
                            filteredOfficeList.add(modelWithThreeString);
                        }
                    }
                    MyProgressDialog.show(context, "", "Filtering Districts...", true);
                    for (int i = 0; i < MasDistrictArray.length(); i++) {
                        try {
                            if (MasDistrictArray.getJSONObject(i).getString("State_Code").equals(stateCodeStr)) {
                                filteredMasDistrictArray.put(MasDistrictArray.getJSONObject(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
        /*select_region.setOnClickListener(v -> {
            if (TextUtils.isEmpty(select_state.getText().toString().trim())) {
                Toast.makeText(context, "Please Select State", Toast.LENGTH_SHORT).show();
                return;
            } else if (regionFilteredList.isEmpty()) {
                Toast.makeText(context, "No Region found for the selected State", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Region");
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            regionAdapter = new RegionAdapter(regionFilteredList, context);
            recyclerView1.setAdapter(regionAdapter);
            AlertDialog dialog = builder.create();
            regionAdapter.setSelectItem((model, position) -> {
                regionCodeStr = model.getId();
                select_region.setText(model.getTitle());
                officeCodeStr = "";
                select_sales_office_name.setText("");
                filteredOfficeList.clear();

                for (CommonModelWithFourString modelWithThreeString : officeList) {
                    if (modelWithThreeString.getRegionReference().equalsIgnoreCase(regionCodeStr)) {
                        filteredOfficeList.add(modelWithThreeString);
                    }
                }
                dialog.dismiss();
            });
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });*/
        select_sales_office_name.setOnClickListener(v -> {
            if (TextUtils.isEmpty(select_state.getText().toString().trim())) {
                Toast.makeText(context, "Please Select State", Toast.LENGTH_SHORT).show();
                return;
            } else if (filteredOfficeList.isEmpty()) {
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
                    for (CommonModelWithFourString modelWithThreeString : filteredOfficeList) {
                        if (modelWithThreeString.getTitle().toLowerCase().contains(s.toString().toLowerCase().trim())) {
                            tempOfficeList.add(modelWithThreeString);
                        }
                    }
                    setAdapterForOffice(tempOfficeList, dialog, recyclerView1);
                }
            });
            close.setOnClickListener(v1 -> dialog.dismiss());
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            setAdapterForOffice(filteredOfficeList, dialog, recyclerView1);
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
                        if (modelWithThreeString.getTitle().toLowerCase().contains(s.toString().toLowerCase().trim())) {
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
        selectCusACGroup.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Account Group");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, cusACGroupResponse);
            adapter.setOnItemClick(position -> {
                try {
                    acGroupIDStr = cusACGroupResponse.getJSONObject(position).getString("id");
                    acGroupNameStr = cusACGroupResponse.getJSONObject(position).getString("title");
                    selectCusACGroup.setText(cusACGroupResponse.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        select_dist_channel.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Distributor Channel");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, distChannelResponse);
            adapter.setOnItemClick(position -> {
                try {
                    distChannelIDStr = distChannelResponse.getJSONObject(position).getString("id");
                    distChannelNameStr = distChannelResponse.getJSONObject(position).getString("title");
                    select_dist_channel.setText(distChannelResponse.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        select_sales_division.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Sales Division");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, salesDivisionResponse);
            adapter.setOnItemClick(position -> {
                try {
                    salesDivisionIDStr = salesDivisionResponse.getJSONObject(position).getString("id");
                    salesDivisionNameStr = salesDivisionResponse.getJSONObject(position).getString("title");
                    select_sales_division.setText(salesDivisionResponse.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        selectSalesRegion.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Sales Region");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, MasCusSalRegionArray);
            adapter.setOnItemClick(position -> {
                try {
                    SalesRegionID = MasCusSalRegionArray.getJSONObject(position).getString("id");
                    SalesRegionStr = MasCusSalRegionArray.getJSONObject(position).getString("title");
                    selectSalesRegion.setText(MasCusSalRegionArray.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        selectBusinessType.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Business Type");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, MasBusinessTypeArray);
            adapter.setOnItemClick(position -> {
                try {
                    BusinessTypeID = MasBusinessTypeArray.getJSONObject(position).getString("id");
                    BusinessTypeStr = MasBusinessTypeArray.getJSONObject(position).getString("title");
                    selectBusinessType.setText(MasBusinessTypeArray.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        selectCustomerGroup.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Customer Group");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, MasCusGroupArray);
            adapter.setOnItemClick(position -> {
                try {
                    CustomerGroupID = MasCusGroupArray.getJSONObject(position).getString("id");
                    CustomerGroupStr = MasCusGroupArray.getJSONObject(position).getString("title");
                    selectCustomerGroup.setText(MasCusGroupArray.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        selectSalesGroup.setOnClickListener(v -> {
            if (select_sales_office_name.getText().toString().isEmpty()) {
                Toast.makeText(context, "Please select sales office", Toast.LENGTH_SHORT).show();
                return;
            }
            if (filteredMasSalesGroupArray.length() == 0) {
                Toast.makeText(context, "No sales group found for the selected sales office", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Sales Group");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, filteredMasSalesGroupArray);
            adapter.setOnItemClick(position -> {
                try {
                    SalesGroupID = filteredMasSalesGroupArray.getJSONObject(position).getString("id");
                    SalesGroupStr = filteredMasSalesGroupArray.getJSONObject(position).getString("title");
                    selectSalesGroup.setText(filteredMasSalesGroupArray.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        selectBusinessDivision.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Business Division");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, MasBusinessDivisionArray);
            adapter.setOnItemClick(position -> {
                try {
                    BusinessDivisionID = MasBusinessDivisionArray.getJSONObject(position).getString("id");
                    BusinessDivisionStr = MasBusinessDivisionArray.getJSONObject(position).getString("title");
                    selectBusinessDivision.setText(MasBusinessDivisionArray.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        selectCustomerClass.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Customer Cluster");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, MasCusClassArray);
            adapter.setOnItemClick(position -> {
                try {
                    CustomerClassID = MasCusClassArray.getJSONObject(position).getString("id");
                    CustomerClassStr = MasCusClassArray.getJSONObject(position).getString("title");
                    selectCustomerClass.setText(MasCusClassArray.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        selectCustomerType.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Customer Type");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, MasCusTypeArray);
            adapter.setOnItemClick(position -> {
                try {
                    CustomerTypeID = MasCusTypeArray.getJSONObject(position).getString("id");
                    CustomerTypeStr = MasCusTypeArray.getJSONObject(position).getString("title");
                    selectCustomerType.setText(MasCusTypeArray.getJSONObject(position).getString("title"));
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
        selectSubMarket.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Sub Market");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, MasSubMarketArray);
            adapter.setOnItemClick(position -> {
                try {
                    SubMarketID = MasSubMarketArray.getJSONObject(position).getString("id");
                    SubMarketStr = MasSubMarketArray.getJSONObject(position).getString("title");
                    selectSubMarket.setText(MasSubMarketArray.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        /*selectDistrict.setOnClickListener(v -> {
            if (select_state.getText().toString().isEmpty()) {
                Toast.makeText(context, "Please select state", Toast.LENGTH_SHORT).show();
                return;
            }
            if (filteredMasDistrictArray.length() == 0) {
                Toast.makeText(context, "No District found", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select District");
            AlertDialog dialog = builder.create();
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, filteredMasDistrictArray);
            adapter.setOnItemClick(position -> {
                try {
                    DistrictID = filteredMasDistrictArray.getJSONObject(position).getString("id");
                    DistrictStr = filteredMasDistrictArray.getJSONObject(position).getString("title");
                    selectDistrict.setText(filteredMasDistrictArray.getJSONObject(position).getString("title"));
                    dialog.dismiss();
                } catch (JSONException ignored) {
                }
            });
            recyclerView.setAdapter(adapter);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });*/
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
        /*select_mode_of_payment.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            TextView title = view.findViewById(R.id.title);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView);
            TextView close = view.findViewById(R.id.close);
            title.setText("Select Mode of Payment");
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            adapter = new CommonAdapterForDropdown(MOPList, context);
            recyclerView1.setAdapter(adapter);
            AlertDialog dialog = builder.create();
            adapter.setSelectItem((model, position) -> {
                select_mode_of_payment.setText(model.getTitle());
                dialog.dismiss();
            });
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });*/
        refreshLocation.setOnClickListener(v -> getLocation());
        submit.setOnClickListener(v -> ValidateFields());

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
        MasSubMarketArray = new JSONArray();
        MasCusTypeArray = new JSONArray();
        stateArray = new JSONArray();

        getLocation();

        type_sales_executive_name.setText(UserDetails.getString("SfName", ""));
        type_sales_executive_name.setEnabled(false);
        type_sales_executive_employee_id.setText(UserDetails.getString("EmpId", ""));
        type_sales_executive_employee_id.setEnabled(false);
        creationDateStr = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        date_of_creation.setText(creationDateStr);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        PrepareDropdownLists();

        // Todo: Get Image From S3
        /*common_class.getImageFromS3Bucket(context, "key", "MGR23_1694523049.jpg", "milk_selfie");
        common_class.setOnDownloadImage((key, bmp) -> {
            display_customer_photo.setImageBitmap(bmp);
            display_customer_photo.setVisibility(View.VISIBLE);
        });*/
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
            selectSalesGroup.setText("");
            SalesGroupID = "";
            SalesGroupStr = "";
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
                                    regionList.clear();
                                    officeList.clear();
                                    routeList.clear();
                                    ChannelList.clear();
                                    JSONArray array = object.getJSONArray("regionResponse");
                                    for (int i = 0; i < array.length(); i++) {
                                        String id = array.getJSONObject(i).getString("Area_code");
                                        String title = array.getJSONObject(i).getString("Area_name");
                                        String stateCode = array.getJSONObject(i).getString("State_Code");
                                        Log.e("ksjdhksd", "regionResponse: " + id + ", " + title + ", state code: " + stateCode);
                                        regionList.add(new CommonModelWithThreeString(id, title, stateCode));
                                    }
                                    JSONArray officeResponse = object.getJSONArray("officeResponse");
                                    for (int i = 0; i < officeResponse.length(); i++) {
                                        String id = officeResponse.getJSONObject(i).getString("sOffCode");
                                        String title = officeResponse.getJSONObject(i).getString("sOffName");
                                        String regionReference = officeResponse.getJSONObject(i).getString("StateCode");
                                        String officeReference = officeResponse.getJSONObject(i).getString("PlantId");
                                        Log.e("ksjdhksd", "officeResponse: " + id + ", " + title + ", " + regionReference + ", " + officeReference);
                                        officeList.add(new CommonModelWithFourString(id, title, regionReference, officeReference));
                                    }
                                    JSONArray routeResponse = object.getJSONArray("routeResponse");
                                    for (int i = 0; i < routeResponse.length(); i++) {
                                        String id = routeResponse.getJSONObject(i).getString("Route_ID");
                                        String title = routeResponse.getJSONObject(i).getString("Route_Name");
                                        String officeReference = routeResponse.getJSONObject(i).getString("Plant_Code");
                                        Log.e("ksjdhksd", "routeResponse: " + id + ", " + title + ", " + officeReference);
                                        routeList.add(new CommonModelWithFourString(id, title, "", officeReference));
                                    }
                                    JSONArray channelResponse = object.getJSONArray("channelResponse");
                                    for (int i = 0; i < channelResponse.length(); i++) {
                                        String id = channelResponse.getJSONObject(i).getString("CateId");
                                        String title = channelResponse.getJSONObject(i).getString("CateNm");
                                        Log.e("ksjdhksd", "channelResponse: " + id + ", " + title);
                                        ChannelList.add(new CommonModelForDropDown(id, title));
                                    }
                                    subChannelResponse = object.getJSONArray("subChannelResponse");
                                    cusACGroupResponse = object.getJSONArray("cusACGroupResponse");
                                    distChannelResponse = object.getJSONArray("distChannelResponse");
                                    salesDivisionResponse = object.getJSONArray("salesDivisionResponse");

                                    stateArray = object.getJSONArray("MasState");
                                    MasDistrictArray = object.getJSONArray("MasDistrict");
                                    MasCusSalRegionArray = object.getJSONArray("MasCusSalRegion");
                                    MasSalesGroupArray = object.getJSONArray("MasSalesGroup");
                                    MasCusGroupArray = object.getJSONArray("MasCusGroup");
                                    MasBusinessTypeArray = object.getJSONArray("MasBusinessType");
                                    MasBusinessDivisionArray = object.getJSONArray("MasBusinessDivision");
                                    MasCusClassArray = object.getJSONArray("MasCusClass");
                                    MasReportingVertArray = object.getJSONArray("MasReportingVert");
                                    MasSubMarketArray = object.getJSONArray("MasSubMarket");
                                    MasCusTypeArray = object.getJSONArray("MasCusType");
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

    private void uploadImage(String fileName, String fullPath) {
        Intent mIntent = new Intent(context, FileUploadService.class);
        mIntent.putExtra("FileName", fileName);
        mIntent.putExtra("mFilePath", fullPath);
        mIntent.putExtra("SF", UserDetails.getString("Sfcode", ""));
        mIntent.putExtra("Mode", "AddNewDistributor");
        FileUploadService.enqueueWork(this, mIntent);
    }

    private void ValidateFields() {
        stateNameStr = select_state.getText().toString().trim();
        officeNameStr = select_sales_office_name.getText().toString().trim();
        routeNameStr = select_route_name.getText().toString().trim();
        channelStr = select_channel.getText().toString().trim();
        subChannelNameStr = select_sub_channel.getText().toString().trim();

        cityStr = type_city.getText().toString().trim();
        pincodeStr = type_pincode.getText().toString().trim();
        customerNameStr = type_name_of_the_customer.getText().toString().trim();
        ownerNameStr = type_name_of_the_owner.getText().toString().trim();
        businessAddressNoStr = businessAddressNo.getText().toString().trim();
        businessAddressCityStr = businessAddressCity.getText().toString().trim();
        businessAddressPincodeStr = businessAddressPincode.getText().toString().trim();
        mobileNumberStr = type_mobile_number.getText().toString().trim();
        emailAddressStr = type_email_id.getText().toString().trim();
        executiveNameStr = type_sales_executive_name.getText().toString().trim();
        employeeIdStr = type_sales_executive_employee_id.getText().toString().trim();
        creationDateStr = date_of_creation.getText().toString().trim();
        aadhaarStr = type_aadhaar_number.getText().toString().trim();
        PANStr = type_pan_number.getText().toString().trim();
        bankDetailsStr = select_bank_details.getText().toString().trim();
        FSSAIDetailsStr = type_fssai.getText().toString().trim();
        GSTDetailsStr = type_gst.getText().toString().trim();
        agreementDetailsStr = select_agreement_copy.getText().toString().trim();
//        modeOfPaymentStr = select_mode_of_payment.getText().toString().trim();
//        depositDetailsStr = type_deposit.getText().toString().trim();

        ownerAddressNoStr = ownerAddressNo.getText().toString().trim();
        ownerAddressCityStr = ownerAddressCity.getText().toString().trim();
        ownerAddressPincodeStr = ownerAddressPincode.getText().toString().trim();
        businessAddressNoStr = businessAddressNo.getText().toString().trim();
        businessAddressCityStr = businessAddressCity.getText().toString().trim();
        businessAddressPincodeStr = businessAddressPincode.getText().toString().trim();

        fssaiFromStr = fssaiFromDate.getText().toString().trim();
        fssaitoStr = fssaiToDate.getText().toString().trim();
        FSSAIDetailsStr = type_fssai.getText().toString().trim();
        PANName = type_pan_name.getText().toString().trim();
        UIDType = uidType.getText().toString().trim();

        if (TextUtils.isEmpty(customer_photo_name) || TextUtils.isEmpty(customer_photo_url)) {
            Toast.makeText(context, "Please Capture Customer Photo", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(shop_photo_name) || TextUtils.isEmpty(shop_photo_url)) {
            Toast.makeText(context, "Please Capture Shop Photo", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(officeNameStr) || TextUtils.isEmpty(officeCodeStr)) {
            Toast.makeText(context, "Please Select the Sales Office Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(routeNameStr) || TextUtils.isEmpty(routeCodeStr)) {
            Toast.makeText(context, "Please Select the Route Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(channelStr) || TextUtils.isEmpty(channelIDStr)) {
            Toast.makeText(context, "Please Select the Channel", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(subChannelIDStr) || TextUtils.isEmpty(subChannelNameStr)) {
            Toast.makeText(context, "Please Select Sub Channel", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(acGroupIDStr) || TextUtils.isEmpty(acGroupNameStr)) {
            Toast.makeText(context, "Please Select Account Group", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(distChannelIDStr) || TextUtils.isEmpty(distChannelNameStr)) {
            Toast.makeText(context, "Please Select Distributor Channel", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(salesDivisionIDStr) || TextUtils.isEmpty(salesDivisionNameStr)) {
            Toast.makeText(context, "Please Select Sales Division", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cityStr)) {
            Toast.makeText(context, "Please Select the City", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pincodeStr)) {
            Toast.makeText(context, "Please Enter the Pincode", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(stateCodeStr) || TextUtils.isEmpty(stateNameStr)) {
            Toast.makeText(context, "Please Select the State", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(customerNameStr)) {
            Toast.makeText(context, "Please Enter the Customer Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ownerNameStr)) {
            Toast.makeText(context, "Please Enter the Owner Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(businessAddressNoStr) || TextUtils.isEmpty(businessAddressCityStr) || TextUtils.isEmpty(businessAddressPincodeStr)) {
            Toast.makeText(context, "Please Enter the Business Address", Toast.LENGTH_SHORT).show();
        } else if (businessAddressPincodeStr.length() != 6) {
            Toast.makeText(context, "Please Enter 6 digit pincode", Toast.LENGTH_SHORT).show();
        } else if (ownerAddressPincodeStr.length() != 6) {
            Toast.makeText(context, "Please Enter 6 digit pincode", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ownerAddressNoStr) || TextUtils.isEmpty(ownerAddressCityStr) || TextUtils.isEmpty(ownerAddressPincodeStr)) {
            Toast.makeText(context, "Please Enter the Owner Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mobileNumberStr) || mobileNumberStr.length() != 10) { // Todo: Need to discuss
            Toast.makeText(context, "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(executiveNameStr)) {
            Toast.makeText(context, "Please Enter the Sales Executive Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(employeeIdStr)) {
            Toast.makeText(context, "Please Enter the Sales Executive - Employee ID", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(creationDateStr)) {
            Toast.makeText(context, "Creation Date Can't be Fetched", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(aadhaarStr)) {
            Toast.makeText(context, "Please Enter the Aadhaar Number", Toast.LENGTH_SHORT).show();
        } else if (aadhaarStr.length() != 12) {
            Toast.makeText(context, "Please Enter 12 digit Aadhaar Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(aadhaarImageName) || TextUtils.isEmpty(aadhaarImageFullPath)) {
            Toast.makeText(context, "Please Capture the Aadhaar Image", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(PANStr)) {
            Toast.makeText(context, "Please Enter the PAN Number", Toast.LENGTH_SHORT).show();
        } else if (PANStr.length() != 10) {
            Toast.makeText(context, "Please Enter 10 digit PAN Number", Toast.LENGTH_SHORT).show();
        } else if (!PANStr.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
            Toast.makeText(context, "Please Enter valid PAN Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(panImageName) || TextUtils.isEmpty(panImageFullPath)) {
            Toast.makeText(context, "Please Capture the PAN Image", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(GSTDetailsStr)) {
            Toast.makeText(context, "Please Enter the GST Number", Toast.LENGTH_SHORT).show();
        } else if (GSTDetailsStr.length() != 15 && gstSwitch.isChecked()) {
            Toast.makeText(context, "Please Enter 15 digit GST Number", Toast.LENGTH_SHORT).show();
        } else if (!GSTDetailsStr.matches("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[0-9]{1}[A-Z]{1}[0-9A-Z]{1}$") && gstSwitch.isChecked()) {
            Toast.makeText(context, "Please Enter valid GST Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(GSTImageName) || TextUtils.isEmpty(GSTImageFullPath)) {
            Toast.makeText(context, "Please Capture the GST Certificate", Toast.LENGTH_SHORT).show();
        } else if ((Lat == 0) || (Long == 0)) {
            Toast.makeText(context, "Location can't be fetched", Toast.LENGTH_SHORT).show();
        } else if (SalesRegionID.isEmpty() || SalesRegionStr.isEmpty()) {
            Toast.makeText(context, "Please Select Sales Region", Toast.LENGTH_SHORT).show();
        } else if (BusinessTypeID.isEmpty() || BusinessTypeStr.isEmpty()) {
            Toast.makeText(context, "Please Select Business Type", Toast.LENGTH_SHORT).show();
        } else if (CustomerGroupID.isEmpty() || CustomerGroupStr.isEmpty()) {
            Toast.makeText(context, "Please Select Customer Group", Toast.LENGTH_SHORT).show();
        } else if (SalesGroupID.isEmpty() || SalesGroupStr.isEmpty()) {
            Toast.makeText(context, "Please Select Sales Group", Toast.LENGTH_SHORT).show();
        } else if (BusinessDivisionID.isEmpty() || BusinessDivisionStr.isEmpty()) {
            Toast.makeText(context, "Please Select Business Division", Toast.LENGTH_SHORT).show();
        } else if (CustomerClassID.isEmpty() || CustomerClassStr.isEmpty()) {
            Toast.makeText(context, "Please Select Customer Class", Toast.LENGTH_SHORT).show();
        } else if (CustomerTypeID.isEmpty() || CustomerTypeStr.isEmpty()) {
            Toast.makeText(context, "Please Select Customer Type", Toast.LENGTH_SHORT).show();
        } else if (ReportingVerticalsID.isEmpty() || ReportingVerticalsStr.isEmpty()) {
            Toast.makeText(context, "Please Select Reporting Verticals", Toast.LENGTH_SHORT).show();
        } else if (SubMarketID.isEmpty() || SubMarketStr.isEmpty()) {
            Toast.makeText(context, "Please Select Sub Market", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure want to submit?");
            builder.setCancelable(false);
            builder.setPositiveButton("YES", (dialog, which) -> SubmitForm());
            builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        }
    }

    private void SubmitForm() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Creating New Distributor");
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

            object.put("acGroupID", acGroupIDStr);
            object.put("acGroupName", acGroupNameStr);

            object.put("distChannelID", distChannelIDStr);
            object.put("distChannelName", distChannelNameStr);

            object.put("salesDivisionID", salesDivisionIDStr);
            object.put("salesDivisionName", salesDivisionNameStr);

            object.put("SalesRegionID", SalesRegionID);
            object.put("SalesRegionStr", SalesRegionStr);

            object.put("BusinessTypeID", BusinessTypeID);
            object.put("BusinessTypeStr", BusinessTypeStr);

            object.put("CustomerGroupID", CustomerGroupID);
            object.put("CustomerGroupStr", CustomerGroupStr);

            object.put("SalesGroupID", SalesGroupID);
            object.put("SalesGroupStr", SalesGroupStr);

            object.put("BusinessDivisionID", BusinessDivisionID);
            object.put("BusinessDivisionStr", BusinessDivisionStr);

            object.put("CustomerClassID", CustomerClassID);
            object.put("CustomerClassStr", CustomerClassStr);

            object.put("CustomerTypeID", CustomerTypeID);
            object.put("CustomerTypeStr", CustomerTypeStr);

            object.put("ReportingVerticalsID", ReportingVerticalsID);
            object.put("ReportingVerticalsStr", ReportingVerticalsStr);

            object.put("SubMarketID", SubMarketID);
            object.put("SubMarketStr", SubMarketStr);

            object.put("cityStr", cityStr);

            object.put("pincodeStr", pincodeStr);

            object.put("customerNameStr", customerNameStr);

            object.put("ownerNameStr", ownerNameStr);

            object.put("lat", Lat);
            object.put("long", Long);

            object.put("businessAddressNoStr", businessAddressNoStr);
            object.put("businessAddressCityStr", businessAddressCityStr);
            object.put("businessAddressPincodeStr", businessAddressPincodeStr);

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
            object.put("FSSAIImageName", FSSAIImageName);
            object.put("fssaiFromStr", fssaiFromStr);
            object.put("fssaitoStr", fssaitoStr);
            object.put("FSSAIDeclarationImageName", FSSAIDeclarationImageName);

            object.put("gstStatus", gstSwitch.isChecked() ? "1" : "0");
            object.put("GSTDetailsStr", GSTDetailsStr);
            object.put("GSTImageName", GSTImageName);
            object.put("gstDeclarationImageName", gstDeclarationImageName);

            object.put("tcsStatus", tcsSwitch.isChecked() ? "0" : "1");
            object.put("tcsDeclarationImageName", tcsDeclarationImageName);

            object.put("agreementDetailsStr", agreementDetailsStr);
            object.put("agreementImageName", agreementImageName);

            object.put("purchaseTypeId", purchaseTypeID);
            object.put("purchaseTypeName", purchaseTypeName);

        } catch (JSONException e) {
            progressDialog.dismiss();
            Toast.makeText(context, "Json Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        data.put(object);

        Map<String, String> params = new HashMap<>();
        params.put("axn", "save/new_distributor");

        Log.e("JSONData", data.toString());
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
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                // Todo: Address
                type_city.setText(returnedAddress.getLocality());
                type_pincode.setText(returnedAddress.getPostalCode());
                businessAddressNo.setText(returnedAddress.getSubThoroughfare() + ", " + returnedAddress.getThoroughfare());
                businessAddressCity.setText(returnedAddress.getLocality() + ", " + returnedAddress.getAdminArea());
                businessAddressPincode.setText(returnedAddress.getPostalCode());
            }
        } catch (Exception e) {
            Toast.makeText(context, "Map Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
}