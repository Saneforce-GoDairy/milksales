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
import android.text.Html;
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
import com.saneforce.godairy.Interface.LocationResponse;
import com.saneforce.godairy.Interface.OnImagePickListener;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.AdapterShowMultipleImages;
import com.saneforce.godairy.SFA_Adapter.CommonAdapterForDropdown;
import com.saneforce.godairy.SFA_Adapter.CommonAdapterForDropdownWithFilter;
import com.saneforce.godairy.SFA_Model_Class.CommonModelForDropDown;
import com.saneforce.godairy.SFA_Model_Class.CommonModelWithFourString;
import com.saneforce.godairy.SFA_Model_Class.CommonModelWithThreeString;
import com.saneforce.godairy.assistantClass.AssistantClass;
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

public class CustomerOnBoarding extends AppCompatActivity implements OnMapReadyCallback {
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
    AssistantClass assistantClass;

    CommonAdapterForDropdown adapter;
    CommonAdapterForDropdownWithFilter filterAdapter;
    ArrayList<CommonModelForDropDown> ChannelList, stateList, BankList, AgreementList, MOPList;
    ArrayList<CommonModelWithThreeString> regionList;
    ArrayList<CommonModelWithFourString> officeList, filteredOfficeList, tempOfficeList, routeList, filteredRouteList, tempRouteList;

    GoogleMap googleMap;
    JSONArray subChannelResponse, filteredSubChannel, cusACGroupResponse, distChannelResponse, salesDivisionResponse, MasDistrictArray, filteredMasDistrictArray, MasCusSalRegionArray, MasSalesGroupArray, filteredMasSalesGroupArray, MasCusGroupArray, MasBusinessTypeArray, MasBusinessDivisionArray, MasCusClassArray, MasReportingVertArray, uidTypeArray, MasSubMarketArray, MasCusTypeArray, stateArray, fieldDetails;

    DownloadReceiver downloadReceiver;
    DatePickerDialog fromDatePickerDialog;

    double Lat = 0, Long = 0;
    String customer_photo_name = "", shop_photo_name = "", customerApplicationImageName = "", stateCodeStr = "", stateNameStr = "", officeCodeStr = "", officeNameStr = "", routeCodeStr = "", routeNameStr = "", channelIDStr = "", channelStr = "", subChannelNameStr = "", ReportingVerticalsID = "", ReportingVerticalsStr = "", cityStr = "", customerNameStr = "", ownerNameStr = "", businessAddressNoStr = "", businessAddressCityStr = "", businessAddressPincodeStr = "", pincodeStr = "", ownerAddressNoStr = "", ownerAddressCityStr = "", ownerAddressPincodeStr = "", mobileNumberStr = "", emailAddressStr = "", executiveNameStr = "", employeeIdStr = "", UIDType = "", aadhaarStr = "", PANStr = "", PANName = "", bankDetailsStr = "", FSSAIDetailsStr = "", fssaiFromStr = "", fssaitoStr = "", GSTDetailsStr = "", agreementDetailsStr = "", purchaseTypeID = "", purchaseTypeName = "", stockistCode = "", customer_photo_url = "", shop_photo_url = "", customer_application_url = "", subChannelIDStr = "", doorNo = "", street = "", city = "", district = "", state = "", country = "", pincode = "", feature = "";
    ArrayList<String> FSSAIList, GSTList, AadhaarImgList, PanImgList, GSTDeclarationImgList, FSSAIDeclarationImgList, TCSDeclarationImgList, BankImgList, AgreementImgList;
    private String mUkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewDistributorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mUkey = Common_Class.GetEkey();

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

        assistantClass = new AssistantClass(context);
        common_class = new Common_Class(this);
        pref = new Shared_Common_Pref(this);
        UserDetails = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        common_class.gotoHomeScreen(context, home);

        binding.headtext.setText("Customer On Boarding");

        fssaiSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                fssaiDeclarationLL.setVisibility(View.GONE);
                fssaiLL.setVisibility(View.VISIBLE);
                FSSAIDeclarationImgList.clear();
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
                GSTDeclarationImgList.clear();
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
                TCSDeclarationImgList.clear();
                tcsDeclarationLL.setVisibility(View.GONE);
            } else {
                tcsDeclarationLL.setVisibility(View.VISIBLE);
            }
        });

        downloadGSTDeclarationForm.setOnClickListener(v -> {
            Long downloadID = FileDownloader.downloadFile(context, "https://admin.godairy.in/Downloads/THIRUMALA/GST%20Non-Regsitration%20Declaration.docx", "GST Non-Registration Declaration.docx");

        });

        downloadTCSDeclarationForm.setOnClickListener(v -> {
            Long downloadID = FileDownloader.downloadFile(context, "https://admin.godairy.in/Downloads/THIRUMALA/TCS%20Declaration.docx", "TCS Declaration.docx");
        });

        downloadfssaiDeclarationForm.setOnClickListener(v -> {
            Long downloadID = FileDownloader.downloadFile(context, "https://admin.godairy.in/Downloads/THIRUMALA/Undertaking%20FSSAI%20license.docx", "Undertaking FSSAI license.docx");
        });

        getLocation();
        type_sales_executive_name.setText(UserDetails.getString("SfName", ""));
        type_sales_executive_employee_id.setText(UserDetails.getString("EmpId", ""));

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
        AadhaarImgList = new ArrayList<>();
        PanImgList = new ArrayList<>();
        GSTDeclarationImgList = new ArrayList<>();
        FSSAIDeclarationImgList = new ArrayList<>();
        TCSDeclarationImgList = new ArrayList<>();
        BankImgList = new ArrayList<>();
        AgreementImgList = new ArrayList<>();

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
        fieldDetails = new JSONArray();

        capture_customer_photo.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info", new Common_Class.ImageUploadListener() {
                        @Override
                        public void onSuccess() {
                            customer_photo_name = FileName;
                            customer_photo_url = fullPath;
                            display_customer_photo.setImageBitmap(image);
                            display_customer_photo.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_shop_photo.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info", new Common_Class.ImageUploadListener() {
                        @Override
                        public void onSuccess() {
                            shop_photo_name = FileName;
                            shop_photo_url = fullPath;
                            display_shop_photo.setImageBitmap(image);
                            display_shop_photo.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_aadhaar_number.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info", new Common_Class.ImageUploadListener() {
                        @Override
                        public void onSuccess() {
                            AadhaarImgList.add(FileName);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_pan_number.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info", new Common_Class.ImageUploadListener() {
                        @Override
                        public void onSuccess() {
                            PanImgList.add(FileName);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_bank_details.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info", new Common_Class.ImageUploadListener() {
                        @Override
                        public void onSuccess() {
                            BankImgList.add(FileName);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_fssai.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info", new Common_Class.ImageUploadListener() {
                        @Override
                        public void onSuccess() {
                            FSSAIList.add(FileName);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_gst.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info", new Common_Class.ImageUploadListener() {
                        @Override
                        public void onSuccess() {
                            GSTList.add(FileName);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_agreement_copy.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info", new Common_Class.ImageUploadListener() {
                        @Override
                        public void onSuccess() {
                            AgreementImgList.add(FileName);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        captureGSTDeclaration.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info", new Common_Class.ImageUploadListener() {
                        @Override
                        public void onSuccess() {
                            GSTDeclarationImgList.add(FileName);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        captureTCSDeclaration.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info", new Common_Class.ImageUploadListener() {
                        @Override
                        public void onSuccess() {
                            TCSDeclarationImgList.add(FileName);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capturefssaiDeclaration.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info", new Common_Class.ImageUploadListener() {
                        @Override
                        public void onSuccess() {
                            FSSAIDeclarationImgList.add(FileName);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Intent intent = new Intent(context, AllowancCapture.class);
            startActivity(intent);
        });
        capture_customer_application.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    Common_Class.uploadToS3Bucket(context, fullPath, FileName, "stockist_info", new Common_Class.ImageUploadListener() {
                        @Override
                        public void onSuccess() {
                            customerApplicationImageName = FileName;
                            customer_application_url = fullPath;
                            display_customer_application.setImageBitmap(image);
                            display_customer_application.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
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
            ShowImages("GST Declaration Images", GSTDeclarationImgList);
        });
        previewTCSDeclaration.setOnClickListener(v -> {
            previewTCSDeclaration.setEnabled(false);
            new Handler().postDelayed(() -> previewTCSDeclaration.setEnabled(true), 1500);
            ShowImages("TCS Declaration Images", TCSDeclarationImgList);
        });
        previewfssaiDeclaration.setOnClickListener(v -> {
            previewfssaiDeclaration.setEnabled(false);
            new Handler().postDelayed(() -> previewfssaiDeclaration.setEnabled(true), 1500);
            ShowImages("FSSAI Declaration Images", FSSAIDeclarationImgList);

        });
        display_customer_application.setOnClickListener(v -> {
            display_customer_application.setEnabled(false);
            new Handler().postDelayed(() -> display_customer_application.setEnabled(true), 1500);
            showImage(customer_application_url);

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
            ShowImages("Bank Details Images", BankImgList);
        });
        display_fssai.setOnClickListener(v -> {
            ShowImages("FSSAI Images", FSSAIList);
        });
        display_gst.setOnClickListener(v -> {
            ShowImages("GST Images", GSTList);
        });
        display_agreement_copy.setOnClickListener(v -> {
            display_agreement_copy.setEnabled(false);
            new Handler().postDelayed(() -> display_agreement_copy.setEnabled(true), 1500);
            ShowImages("Agreement Copy Images", AgreementImgList);
        });
        display_aadhaar_number.setOnClickListener(v -> {
            display_aadhaar_number.setEnabled(false);
            new Handler().postDelayed(() -> display_aadhaar_number.setEnabled(true), 1500);
            ShowImages("Aadhaar Images", AadhaarImgList);
        });
        display_pan_number.setOnClickListener(v -> {
            display_pan_number.setEnabled(false);
            new Handler().postDelayed(() -> display_pan_number.setEnabled(true), 1500);
            ShowImages("PAN Images", PanImgList);
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
            adapter.setOnItemClick((position, arrayList) -> {
                try {
                    select_state.setText(arrayList.getJSONObject(position).getString("title"));
                    stateCodeStr = arrayList.getJSONObject(position).getString("id");
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
            adapter.setOnItemClick((position, arrayList) -> {
                try {
                    subChannelIDStr = arrayList.getJSONObject(position).getString("id");
                    subChannelNameStr = arrayList.getJSONObject(position).getString("title");
                    select_sub_channel.setText(arrayList.getJSONObject(position).getString("title"));
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
            adapter.setOnItemClick((position, arrayList) -> {
                try {
                    ReportingVerticalsID = arrayList.getJSONObject(position).getString("id");
                    ReportingVerticalsStr = arrayList.getJSONObject(position).getString("title");
                    selectReportingVerticals.setText(arrayList.getJSONObject(position).getString("title"));
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
            adapter.setOnItemClick((position, arrayList) -> {
                try {
                    UIDType = arrayList.getJSONObject(position).getString("title");
                    uidType.setText(UIDType);
                    if (!UIDType.equals("Aadhaar")) {
                        binding.aadhaarLL.setVisibility(View.GONE);
                        aadhaarStr = "";
                        AadhaarImgList.clear();
                        binding.typeAadhaarNumber.setText("");
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
            adapter.setOnItemClick((position, arrayList) -> {
                try {
                    purchaseTypeID = arrayList.getJSONObject(position).getString("id");
                    purchaseTypeName = arrayList.getJSONObject(position).getString("title");
                    purchaseType.setText(arrayList.getJSONObject(position).getString("title"));
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
            BankImgList.clear();
            select_bank_details.setText("");
        });

        binding.clearAgreement.setOnClickListener(v -> {
            agreementDetailsStr = "";
            AgreementImgList.clear();
            select_agreement_copy.setText("");
        });
    }

    private void ShowImages(String TITLE, ArrayList<String> LIST) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_show_multiple_images, null, false);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        TextView title = view.findViewById(R.id.title);
        ImageView close = view.findViewById(R.id.close);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        title.setText(TITLE);
        close.setOnClickListener(v1 -> dialog.dismiss());
        recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        recyclerView.setAdapter(new AdapterShowMultipleImages(context, LIST));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
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
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Getting stockist info...");
        progressDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_stockist_info");
        params.put("stockistCode", stockistCode);
        Common_Class.makeApiCall(context, params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    AssignStockistInfo(jsonObject.getJSONObject("response"));
                } catch (Exception e) {
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(String error) {
                progressDialog.dismiss();
                assistantClass.showAlertDialogWithDismiss(error);
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
                customer_application_url = path;
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

                String aadhaarImageName = jsonObject.optString("AadhaarImg");
                AadhaarImgList.clear();
                AadhaarImgList.addAll(Arrays.asList(aadhaarImageName.split(",")));
            }

            PANStr = jsonObject.optString("Pan");
            type_pan_number.setText(PANStr);

            String panImageName = jsonObject.optString("PanImg");
            PanImgList.clear();
            PanImgList.addAll(Arrays.asList(panImageName.split(",")));

            PANName = jsonObject.optString("Pan_Name");
            type_pan_name.setText(PANName);

            bankDetailsStr = jsonObject.optString("BankAccNo");
            select_bank_details.setText(bankDetailsStr);

            String bankImageName = jsonObject.optString("BankAccImg");
            BankImgList.clear();
            BankImgList.addAll(Arrays.asList(bankImageName.split(",")));

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

            String FSSAIDeclarationImageName = jsonObject.optString("fssaiDecImg");
            FSSAIDeclarationImgList.clear();
            FSSAIDeclarationImgList.addAll(Arrays.asList(FSSAIDeclarationImageName.split(",")));

            String GST_type = jsonObject.optString("GST_type");
            gstSwitch.setChecked(GST_type.equals("1"));

            GSTDetailsStr = jsonObject.optString("Gst");
            type_gst.setText(GSTDetailsStr);

            String GSTImageName = jsonObject.optString("GstImg");
            GSTList.clear();
            GSTList.addAll(Arrays.asList(GSTImageName.split(",")));

            String gstDeclarationImageName = jsonObject.optString("gstDecImg");
            GSTDeclarationImgList.clear();
            GSTDeclarationImgList.addAll(Arrays.asList(gstDeclarationImageName.split(",")));

            String have_tcs = jsonObject.optString("have_tcs");
            tcsSwitch.setChecked(have_tcs.equals("0"));

            String tcsDeclarationImageName = jsonObject.optString("tcsDecImg");
            TCSDeclarationImgList.clear();
            TCSDeclarationImgList.addAll(Arrays.asList(tcsDeclarationImageName.split(",")));

            agreementDetailsStr = jsonObject.optString("Agreement");
            select_agreement_copy.setText(agreementDetailsStr);

            String agreementImageName = jsonObject.optString("AgreementImg");
            AgreementImgList.clear();
            AgreementImgList.addAll(Arrays.asList(agreementImageName.split(",")));

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
        assistantClass.getLocation(new LocationResponse() {
            @Override
            public void onSuccess(double lat, double lng) {
                Lat = lat;
                Long = lng;
                getCompleteAddressString(Lat, Long);
                SetMap();
            }

            @Override
            public void onFailure() {
                assistantClass.showAlertDialogWithDismiss("Can't fetch your location...");
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
        assistantClass.showProgressDialog("Preparing...", false);

        BankList.add(new CommonModelForDropDown("1", "Passbook"));
        BankList.add(new CommonModelForDropDown("2", "Cheque"));

        AgreementList.add(new CommonModelForDropDown("1", "TDC Agreement"));
        AgreementList.add(new CommonModelForDropDown("2", "TOT"));

        MOPList.add(new CommonModelForDropDown("1", "App"));

        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_dist_dropdown_lists_for_onboarding");
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
                                    fieldDetails = object.optJSONArray("fieldDetails");
                                    runOnUiThread(() -> prepareViews());
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
                assistantClass.dismissProgressDialog();
                if (!stockistCode.isEmpty()) {
                    getStockistInfo();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                assistantClass.dismissProgressDialog();
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareViews() {
        if (fieldDetails == null) {
            return;
        }
        for (int i = 0; i < fieldDetails.length(); i++) {
            try {
                JSONObject object = fieldDetails.getJSONObject(i);
                String fieldCode = object.optString("fieldCode");
                String fieldName = object.optString("fieldName");
                int mandatory = object.optInt("mandatory");
                int visibility = object.optInt("visibility");
                switch (fieldCode) {
                    case "customerPhoto":
                        if (visibility == 1) {
                            binding.customerPhotoLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.customerPhotoLL.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.customerPhotoTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.customerPhotoTitle.setText(fieldName);
                        }
                        break;
                    case "shopPhoto":
                        if (visibility == 1) {
                            binding.shopPhotoLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.shopPhotoLL.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.shopPhotoTITLE.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.shopPhotoTITLE.setText(fieldName);
                        }
                        break;
                    case "applicationPhoto":
                        if (visibility == 1) {
                            binding.applicationPhotoLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.applicationPhotoLL.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.applicationPhotoTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.applicationPhotoTitle.setText(fieldName);
                        }
                        break;
                    case "state":
                        if (visibility == 1) {
                            binding.stateTitle.setVisibility(View.VISIBLE);
                            binding.selectState.setVisibility(View.VISIBLE);
                        } else {
                            binding.stateTitle.setVisibility(View.GONE);
                            binding.selectState.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.stateTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.stateTitle.setText(fieldName);
                        }
                        break;
                    case "salesOffice":
                        if (visibility == 1) {
                            binding.salesOfficeTitle.setVisibility(View.VISIBLE);
                            binding.selectSalesOfficeName.setVisibility(View.VISIBLE);
                        } else {
                            binding.salesOfficeTitle.setVisibility(View.GONE);
                            binding.selectSalesOfficeName.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.salesOfficeTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.salesOfficeTitle.setText(fieldName);
                        }
                        break;
                    case "route":
                        if (visibility == 1) {
                            binding.routeTitle.setVisibility(View.VISIBLE);
                            binding.selectRouteName.setVisibility(View.VISIBLE);
                        } else {
                            binding.routeTitle.setVisibility(View.GONE);
                            binding.selectRouteName.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.routeTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.routeTitle.setText(fieldName);
                        }
                        break;
                    case "channel":
                        if (visibility == 1) {
                            binding.channelTitle.setVisibility(View.VISIBLE);
                            binding.selectChannel.setVisibility(View.VISIBLE);
                        } else {
                            binding.channelTitle.setVisibility(View.GONE);
                            binding.selectChannel.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.channelTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.channelTitle.setText(fieldName);
                        }
                        break;
                    case "subChannel":
                        if (visibility == 1) {
                            binding.subChannelTitle.setVisibility(View.VISIBLE);
                            binding.selectSubChannel.setVisibility(View.VISIBLE);
                        } else {
                            binding.subChannelTitle.setVisibility(View.GONE);
                            binding.selectSubChannel.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.subChannelTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.subChannelTitle.setText(fieldName);
                        }
                        break;
                    case "verticals":
                        if (visibility == 1) {
                            binding.verticalsTitle.setVisibility(View.VISIBLE);
                            binding.selectReportingVerticals.setVisibility(View.VISIBLE);
                        } else {
                            binding.verticalsTitle.setVisibility(View.GONE);
                            binding.selectReportingVerticals.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.verticalsTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.verticalsTitle.setText(fieldName);
                        }
                        break;
                    case "city":
                        if (visibility == 1) {
                            binding.cityTitle.setVisibility(View.VISIBLE);
                            binding.typeCity.setVisibility(View.VISIBLE);
                        } else {
                            binding.cityTitle.setVisibility(View.GONE);
                            binding.typeCity.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.cityTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.cityTitle.setText(fieldName);
                        }
                        break;
                    case "businessName":
                        if (visibility == 1) {
                            binding.businessNameTitle.setVisibility(View.VISIBLE);
                            binding.typeNameOfTheCustomer.setVisibility(View.VISIBLE);
                        } else {
                            binding.businessNameTitle.setVisibility(View.GONE);
                            binding.typeNameOfTheCustomer.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.businessNameTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.businessNameTitle.setText(fieldName);
                        }
                        break;
                    case "ownerName":
                        if (visibility == 1) {
                            binding.ownerNameTitle.setVisibility(View.VISIBLE);
                            binding.typeNameOfTheOwner.setVisibility(View.VISIBLE);
                        } else {
                            binding.ownerNameTitle.setVisibility(View.GONE);
                            binding.typeNameOfTheOwner.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.ownerNameTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.ownerNameTitle.setText(fieldName);
                        }
                        break;
                    case "businessAddress":
                        if (visibility == 1) {
                            binding.businessAddressLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.businessAddressLL.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.businessAddressTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.businessAddressTitle.setText(fieldName);
                        }
                        break;
                    case "ownerAddress":
                        if (visibility == 1) {
                            binding.ownerAddressLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.ownerAddressLL.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.ownerAddressTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.ownerAddressTitle.setText(fieldName);
                        }
                        break;
                    case "mobile":
                        if (visibility == 1) {
                            binding.mobileTitle.setVisibility(View.VISIBLE);
                            binding.typeMobileNumber.setVisibility(View.VISIBLE);
                        } else {
                            binding.mobileTitle.setVisibility(View.GONE);
                            binding.typeMobileNumber.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.mobileTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.mobileTitle.setText(fieldName);
                        }
                        break;
                    case "email":
                        if (visibility == 1) {
                            binding.emailTitle.setVisibility(View.VISIBLE);
                            binding.typeEmailId.setVisibility(View.VISIBLE);
                        } else {
                            binding.emailTitle.setVisibility(View.GONE);
                            binding.typeEmailId.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.emailTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.emailTitle.setText(fieldName);
                        }
                        break;
                    case "salesExecutiveName":
                        if (visibility == 1) {
                            binding.salesExecutiveNameTitle.setVisibility(View.VISIBLE);
                            binding.typeSalesExecutiveName.setVisibility(View.VISIBLE);
                        } else {
                            binding.salesExecutiveNameTitle.setVisibility(View.GONE);
                            binding.typeSalesExecutiveName.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.salesExecutiveNameTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.salesExecutiveNameTitle.setText(fieldName);
                        }
                        break;
                    case "salesExecutiveCode":
                        if (visibility == 1) {
                            binding.salesExecutiveIDTitle.setVisibility(View.VISIBLE);
                            binding.typeSalesExecutiveEmployeeId.setVisibility(View.VISIBLE);
                        } else {
                            binding.salesExecutiveIDTitle.setVisibility(View.GONE);
                            binding.typeSalesExecutiveEmployeeId.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.salesExecutiveIDTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.salesExecutiveIDTitle.setText(fieldName);
                        }
                        break;
                    case "uidType":
                        if (visibility == 1) {
                            binding.uidTypeTitle.setVisibility(View.VISIBLE);
                            binding.uidType.setVisibility(View.VISIBLE);
                        } else {
                            binding.uidTypeTitle.setVisibility(View.GONE);
                            binding.uidType.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.uidTypeTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.uidTypeTitle.setText(fieldName);
                        }
                        break;
                    case "aadhaar":
                        if (visibility == 1) {
                            binding.aadhaarLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.aadhaarLL.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.aadhaarTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.aadhaarTitle.setText(fieldName);
                        }
                        break;
                    case "pan":
                        if (visibility == 1) {
                            binding.panNumberLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.panNumberLL.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.panNumberTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.panNumberTitle.setText(fieldName);
                        }
                        break;
                    case "panName":
                        if (visibility == 1) {
                            binding.panNameTitle.setVisibility(View.VISIBLE);
                            binding.typePanName.setVisibility(View.VISIBLE);
                        } else {
                            binding.panNameTitle.setVisibility(View.GONE);
                            binding.typePanName.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.panNameTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.panNameTitle.setText(fieldName);
                        }
                        break;
                    case "bank":
                        if (visibility == 1) {
                            binding.bankLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.bankLL.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.bankTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.bankTitle.setText(fieldName);
                        }
                        break;
                    case "fssai":
                        if (visibility == 1) {
                            binding.fssaiMasterLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.fssaiMasterLL.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.fssaiNumberTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.fssaiNumberTitle.setText(fieldName);
                        }
                        break;
                    case "fssaiDeclaration":
                        break;
                    case "gst":
                        if (visibility == 1) {
                            binding.gstMasterLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.gstMasterLL.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.gstTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.gstTitle.setText(fieldName);
                        }
                        break;
                    case "gstDeclaration":
                        break;
                    case "tcs":
                        if (visibility == 1) {
                            binding.tcsMasterLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.tcsMasterLL.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.tcsTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.tcsTitle.setText(fieldName);
                        }
                        break;
                    case "tcsDeclaration":
                        break;
                    case "agrementCopy":
                        if (visibility == 1) {
                            binding.agreementLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.agreementLL.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.agreementTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.agreementTitle.setText(fieldName);
                        }
                        break;
                    case "purchaseType":
                        if (visibility == 1) {
                            binding.purchaseType.setVisibility(View.VISIBLE);
                            binding.purchaseTypeTitle.setVisibility(View.VISIBLE);
                        } else {
                            binding.purchaseType.setVisibility(View.GONE);
                            binding.purchaseTypeTitle.setVisibility(View.GONE);
                        }
                        if (mandatory == 1) {
                            binding.purchaseTypeTitle.setText(Html.fromHtml(fieldName + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.purchaseTypeTitle.setText(fieldName);
                        }
                        break;
                }
            } catch (JSONException ignored) {
            }
        }
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

        FSSAIDetailsStr = type_fssai.getText().toString().trim();
        FSSAIDetailsStr = Common_Class.validateField(FSSAIDetailsStr);
        fssaiFromStr = fssaiFromDate.getText().toString().trim();
        fssaiFromStr = Common_Class.validateField(fssaiFromStr);
        fssaitoStr = fssaiToDate.getText().toString().trim();
        fssaitoStr = Common_Class.validateField(fssaitoStr);

        GSTDetailsStr = type_gst.getText().toString().trim();
        GSTDetailsStr = Common_Class.validateField(GSTDetailsStr);

        agreementDetailsStr = select_agreement_copy.getText().toString().trim();
        agreementDetailsStr = Common_Class.validateField(agreementDetailsStr);

        purchaseTypeName = purchaseType.getText().toString().trim();
        purchaseTypeName = Common_Class.validateField(purchaseTypeName);

        if (binding.customerPhotoLL.getVisibility() == View.VISIBLE && binding.customerPhotoTitle.getText().toString().contains("*") && TextUtils.isEmpty(customer_photo_name)) {
            Toast.makeText(context, "Please capture customer photo", Toast.LENGTH_SHORT).show();
        } else if (binding.shopPhotoLL.getVisibility() == View.VISIBLE && binding.shopPhotoTITLE.getText().toString().contains("*") && TextUtils.isEmpty(shop_photo_name)) {
            Toast.makeText(context, "Please capture shop photo", Toast.LENGTH_SHORT).show();
        } else if (binding.applicationPhotoLL.getVisibility() == View.VISIBLE && binding.applicationPhotoTitle.getText().toString().contains("*") && customerApplicationImageName.isEmpty()) {
            Toast.makeText(context, "Please capture customer application photo", Toast.LENGTH_SHORT).show();
        } else if (binding.stateTitle.getVisibility() == View.VISIBLE && binding.stateTitle.getText().toString().contains("*") && (TextUtils.isEmpty(stateCodeStr) || TextUtils.isEmpty(stateNameStr))) {
            Toast.makeText(context, "Please select the state", Toast.LENGTH_SHORT).show();
        } else if (binding.salesOfficeTitle.getVisibility() == View.VISIBLE && binding.salesOfficeTitle.getText().toString().contains("*") && (TextUtils.isEmpty(officeNameStr) || TextUtils.isEmpty(officeCodeStr))) {
            Toast.makeText(context, "Please select the sales office", Toast.LENGTH_SHORT).show();
        } else if (binding.routeTitle.getVisibility() == View.VISIBLE && binding.routeTitle.getText().toString().contains("*") && (TextUtils.isEmpty(routeNameStr) || TextUtils.isEmpty(routeCodeStr))) {
            Toast.makeText(context, "Please select the route", Toast.LENGTH_SHORT).show();
        } else if (binding.channelTitle.getVisibility() == View.VISIBLE && binding.channelTitle.getText().toString().contains("*") && (TextUtils.isEmpty(channelStr) || TextUtils.isEmpty(channelIDStr))) {
            Toast.makeText(context, "Please select the channel", Toast.LENGTH_SHORT).show();
        } else if (binding.subChannelTitle.getVisibility() == View.VISIBLE && binding.subChannelTitle.getText().toString().contains("*") && (TextUtils.isEmpty(subChannelIDStr) || TextUtils.isEmpty(subChannelNameStr))) {
            Toast.makeText(context, "Please select the sub channel", Toast.LENGTH_SHORT).show();
        } else if (binding.verticalsTitle.getVisibility() == View.VISIBLE && binding.verticalsTitle.getText().toString().contains("*") && (ReportingVerticalsID.isEmpty() || ReportingVerticalsStr.isEmpty())) {
            Toast.makeText(context, "Please select the verticals", Toast.LENGTH_SHORT).show();
        } else if (binding.cityTitle.getVisibility() == View.VISIBLE && binding.cityTitle.getText().toString().contains("*") && TextUtils.isEmpty(cityStr)) {
            Toast.makeText(context, "Please select the city", Toast.LENGTH_SHORT).show();
        } else if (binding.businessNameTitle.getVisibility() == View.VISIBLE && binding.businessNameTitle.getText().toString().contains("*") && TextUtils.isEmpty(customerNameStr)) {
            Toast.makeText(context, "Please enter the business name", Toast.LENGTH_SHORT).show();
        } else if (binding.ownerNameTitle.getVisibility() == View.VISIBLE && binding.ownerNameTitle.getText().toString().contains("*") && TextUtils.isEmpty(ownerNameStr)) {
            Toast.makeText(context, "Please enter the owner name", Toast.LENGTH_SHORT).show();
        } else if (binding.businessAddressLL.getVisibility() == View.VISIBLE && binding.businessAddressTitle.getText().toString().contains("*") && (TextUtils.isEmpty(businessAddressNoStr) || TextUtils.isEmpty(businessAddressCityStr) || TextUtils.isEmpty(businessAddressPincodeStr))) {
            Toast.makeText(context, "Please enter the business address", Toast.LENGTH_SHORT).show();
        } else if (binding.businessAddressLL.getVisibility() == View.VISIBLE && binding.businessAddressTitle.getText().toString().contains("*") && (TextUtils.isEmpty(pincodeStr) || pincodeStr.length() != 6)) {
            Toast.makeText(context, "Please enter 6 digit pincode", Toast.LENGTH_SHORT).show();
        } else if (binding.ownerAddressLL.getVisibility() == View.VISIBLE && binding.ownerAddressTitle.getText().toString().contains("*") && (TextUtils.isEmpty(ownerAddressNoStr) || TextUtils.isEmpty(ownerAddressCityStr) || TextUtils.isEmpty(ownerAddressPincodeStr))) {
            Toast.makeText(context, "Please enter the owner address", Toast.LENGTH_SHORT).show();
        } else if (binding.ownerAddressLL.getVisibility() == View.VISIBLE && binding.ownerAddressTitle.getText().toString().contains("*") && ownerAddressPincodeStr.length() != 6) {
            Toast.makeText(context, "Please enter 6 digit pincode", Toast.LENGTH_SHORT).show();
        } else if (binding.mobileTitle.getVisibility() == View.VISIBLE && binding.mobileTitle.getText().toString().contains("*") && (TextUtils.isEmpty(mobileNumberStr) || mobileNumberStr.length() != 10)) {
            Toast.makeText(context, "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
        } else if (binding.emailTitle.getVisibility() == View.VISIBLE && binding.emailTitle.getText().toString().contains("*") && TextUtils.isEmpty(emailAddressStr)) {
            Toast.makeText(context, "Please enter email address", Toast.LENGTH_SHORT).show();
        } else if (binding.salesExecutiveNameTitle.getVisibility() == View.VISIBLE && binding.salesExecutiveNameTitle.getText().toString().contains("*") && TextUtils.isEmpty(executiveNameStr)) {
            Toast.makeText(context, "Please enter the sales executive name", Toast.LENGTH_SHORT).show();
        } else if (binding.salesExecutiveIDTitle.getVisibility() == View.VISIBLE && binding.salesExecutiveIDTitle.getText().toString().contains("*") && TextUtils.isEmpty(employeeIdStr)) {
            Toast.makeText(context, "Please enter the sales executive ID", Toast.LENGTH_SHORT).show();
        } else if (binding.uidTypeTitle.getVisibility() == View.VISIBLE && binding.uidTypeTitle.getText().toString().contains("*") && TextUtils.isEmpty(UIDType)) {
            Toast.makeText(context, "Please select UID type", Toast.LENGTH_SHORT).show();
        } else if (binding.aadhaarLL.getVisibility() == View.VISIBLE && binding.aadhaarTitle.getText().toString().contains("*") && UIDType.equals("Aadhaar") && TextUtils.isEmpty(aadhaarStr)) {
            Toast.makeText(context, "Please enter the aadhaar number", Toast.LENGTH_SHORT).show();
        } else if (binding.aadhaarLL.getVisibility() == View.VISIBLE && binding.aadhaarTitle.getText().toString().contains("*") && UIDType.equals("Aadhaar") && aadhaarStr.length() != 12) {
            Toast.makeText(context, "Please enter 12 digit aadhaar number", Toast.LENGTH_SHORT).show();
        } else if (binding.aadhaarLL.getVisibility() == View.VISIBLE && binding.aadhaarTitle.getText().toString().contains("*") && UIDType.equals("Aadhaar") && AadhaarImgList.isEmpty()) {
            Toast.makeText(context, "Please capture the aadhaar photo", Toast.LENGTH_SHORT).show();
        } else if (binding.panNumberLL.getVisibility() == View.VISIBLE && binding.panNumberTitle.getText().toString().contains("*") && TextUtils.isEmpty(PANStr)) {
            Toast.makeText(context, "Please enter the PAN number", Toast.LENGTH_SHORT).show();
        } else if (binding.panNumberLL.getVisibility() == View.VISIBLE && binding.panNumberTitle.getText().toString().contains("*") && PANStr.length() != 10) {
            Toast.makeText(context, "Please enter 10 digit PAN number", Toast.LENGTH_SHORT).show();
        } else if (binding.panNumberLL.getVisibility() == View.VISIBLE && binding.panNumberTitle.getText().toString().contains("*") && !PANStr.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
            Toast.makeText(context, "Please enter valid PAN number", Toast.LENGTH_SHORT).show();
        } else if (binding.panNumberLL.getVisibility() == View.VISIBLE && binding.panNumberTitle.getText().toString().contains("*") && PanImgList.isEmpty()) {
            Toast.makeText(context, "Please capture the PAN photo", Toast.LENGTH_SHORT).show();
        } else if (binding.panNameTitle.getVisibility() == View.VISIBLE && binding.panNameTitle.getText().toString().contains("*") && TextUtils.isEmpty(PANName)) {
            Toast.makeText(context, "Please enter the PAN name", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(bankDetailsStr) && BankImgList.isEmpty()) {
            Toast.makeText(context, "Please capture the bank details photo", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(bankDetailsStr) && !BankImgList.isEmpty()) {
            Toast.makeText(context, "Please enter the bank details", Toast.LENGTH_SHORT).show();
        } else if (binding.bankLL.getVisibility() == View.VISIBLE && binding.bankTitle.getText().toString().contains("*") && (TextUtils.isEmpty(bankDetailsStr) || BankImgList.isEmpty())) {
            Toast.makeText(context, "Please enter the bank details", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(FSSAIDetailsStr) && FSSAIDetailsStr.length() != 14 && fssaiSwitch.isChecked()) {
            Toast.makeText(context, "Please enter 14 digit FSSAI number", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(FSSAIDetailsStr) && FSSAIDetailsStr.length() == 14 && fssaiSwitch.isChecked() && FSSAIList.isEmpty()) {
            Toast.makeText(context, "Please capture FSSAI license certificate", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isEmpty(FSSAIDetailsStr) && FSSAIDetailsStr.length() == 14 && fssaiSwitch.isChecked() && (fssaiFromStr.isEmpty() || fssaitoStr.isEmpty())) {
            Toast.makeText(context, "Please select FSSAI valid dates", Toast.LENGTH_SHORT).show();
        } else if (binding.fssaiMasterLL.getVisibility() == View.VISIBLE && binding.fssaiNumberTitle.getText().toString().contains("*") && !fssaiSwitch.isChecked() && FSSAIDeclarationImgList.isEmpty()) {
            Toast.makeText(context, "Please capture FSSAI declaration", Toast.LENGTH_SHORT).show();
        } else if (binding.fssaiMasterLL.getVisibility() == View.VISIBLE && binding.fssaiNumberTitle.getText().toString().contains("*") && fssaiSwitch.isChecked() && (FSSAIDetailsStr.isEmpty() || FSSAIList.isEmpty() || fssaiFromStr.isEmpty() || fssaitoStr.isEmpty())) {
            Toast.makeText(context, "Please provide valid FSSAI details", Toast.LENGTH_SHORT).show();
        } else if (binding.gstMasterLL.getVisibility() == View.VISIBLE && binding.gstTitle.getText().toString().contains("*") && gstSwitch.isChecked() && GSTDetailsStr.length() != 15) {
            Toast.makeText(context, "Please enter 15 digit GST number", Toast.LENGTH_SHORT).show();
        } else if (binding.gstMasterLL.getVisibility() == View.VISIBLE && binding.gstTitle.getText().toString().contains("*") && gstSwitch.isChecked() && (!GSTDetailsStr.matches("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[0-9]{1}[A-Z]{1}[0-9A-Z]{1}$"))) {
            Toast.makeText(context, "Please enter valid GST number", Toast.LENGTH_SHORT).show();
        } else if (binding.gstMasterLL.getVisibility() == View.VISIBLE && binding.gstTitle.getText().toString().contains("*") && gstSwitch.isChecked() && (GSTList.isEmpty())) {
            Toast.makeText(context, "Please capture the GST certificate", Toast.LENGTH_SHORT).show();
        } else if (binding.gstMasterLL.getVisibility() == View.VISIBLE && binding.gstTitle.getText().toString().contains("*") && !gstSwitch.isChecked() && GSTDeclarationImgList.isEmpty()) {
            Toast.makeText(context, "Please capture the GST declaration certificate", Toast.LENGTH_SHORT).show();
        } else if ((Lat == 0) || (Long == 0)) {
            Toast.makeText(context, "Location can't be fetched", Toast.LENGTH_SHORT).show();
        } else if (binding.tcsMasterLL.getVisibility() == View.VISIBLE && binding.tcsTitle.getText().toString().contains("*") && !tcsSwitch.isChecked() && TCSDeclarationImgList.isEmpty()) {
            Toast.makeText(context, "Please capture the TCS declaration", Toast.LENGTH_SHORT).show();
        } else if (binding.agreementLL.getVisibility() == View.VISIBLE && binding.agreementTitle.getText().toString().contains("*") && agreementDetailsStr.isEmpty()) {
            Toast.makeText(context, "Please select agreement copy", Toast.LENGTH_SHORT).show();
        } else if (binding.agreementLL.getVisibility() == View.VISIBLE && binding.agreementTitle.getText().toString().contains("*") && AgreementImgList.isEmpty()) {
            Toast.makeText(context, "Please capture agreement copy", Toast.LENGTH_SHORT).show();
        } else if (agreementDetailsStr.isEmpty() && !AgreementImgList.isEmpty()) {
            Toast.makeText(context, "Please select agreement copy", Toast.LENGTH_SHORT).show();
        } else if (!agreementDetailsStr.isEmpty() && AgreementImgList.isEmpty()) {
            Toast.makeText(context, "Please capture the agreement copy", Toast.LENGTH_SHORT).show();
        } else if (binding.purchaseTypeTitle.getVisibility() == View.VISIBLE && binding.purchaseTypeTitle.getText().toString().contains("*") && (purchaseTypeID.isEmpty() || purchaseTypeName.isEmpty())) {
            Toast.makeText(context, "Please select purchase type", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to submit?");
            builder.setCancelable(true);
            builder.setPositiveButton("YES", (dialog, which) -> SubmitForm());
            builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        }
    }

    private void SubmitForm() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Submitting...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JSONArray data = new JSONArray();
        JSONObject object = new JSONObject();

        try {
            object.put("sfCode", UserDetails.getString("Sfcode", ""));

            object.put("customer_photo_name", customer_photo_name);
            object.put("Ukey", mUkey);
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

            StringBuilder AadhaarImgBuilder = new StringBuilder();
            for (String name : AadhaarImgList) {
                AadhaarImgBuilder.append(",").append(name);
            }
            String aadhaarImageName = AadhaarImgBuilder.toString();
            if (!aadhaarImageName.isEmpty()) {
                aadhaarImageName = aadhaarImageName.substring(1);
            }
            object.put("aadhaarImage", aadhaarImageName);

            object.put("PANStr", PANStr);

            StringBuilder panImgBuilder = new StringBuilder();
            for (String name : PanImgList) {
                panImgBuilder.append(",").append(name);
            }
            String panImageName = panImgBuilder.toString();
            if (!panImageName.isEmpty()) {
                panImageName = panImageName.substring(1);
            }
            object.put("panImage", panImageName);

            object.put("PANName", PANName);

            object.put("bankDetailsStr", bankDetailsStr);

            StringBuilder bankImgBuilder = new StringBuilder();
            for (String name : BankImgList) {
                bankImgBuilder.append(",").append(name);
            }
            String bankImageName = bankImgBuilder.toString();
            if (!bankImageName.isEmpty()) {
                bankImageName = bankImageName.substring(1);
            }
            object.put("bankImageName", bankImageName);

            object.put("fssaiStatus", fssaiSwitch.isChecked() ? "1" : "0");
            object.put("FSSAIDetailsStr", FSSAIDetailsStr);

            StringBuilder FSSAIImageName = new StringBuilder();
            for (String name : FSSAIList) {
                FSSAIImageName.append(",").append(name);
            }
            String fssaiImageName = FSSAIImageName.toString();
            if (!fssaiImageName.isEmpty()) {
                fssaiImageName = fssaiImageName.substring(1);
            }
            object.put("FSSAIImageName", fssaiImageName);
            object.put("fssaiFromStr", fssaiFromStr);
            object.put("fssaitoStr", fssaitoStr);

            StringBuilder fssaiDecImgBuilder = new StringBuilder();
            for (String name : FSSAIDeclarationImgList) {
                fssaiDecImgBuilder.append(",").append(name);
            }
            String fssaiDecImageName = fssaiDecImgBuilder.toString();
            if (!fssaiDecImageName.isEmpty()) {
                fssaiDecImageName = fssaiDecImageName.substring(1);
            }
            object.put("FSSAIDeclarationImageName", fssaiDecImageName);
            object.put("gstStatus", gstSwitch.isChecked() ? "1" : "0");
            object.put("GSTDetailsStr", GSTDetailsStr);

            StringBuilder GSTImageName = new StringBuilder();
            for (String name : GSTList) {
                GSTImageName.append(",").append(name);
            }
            String gstImageName = GSTImageName.toString();
            if (!gstImageName.isEmpty()) {
                gstImageName = gstImageName.substring(1);
            }
            object.put("GSTImageName", gstImageName);

            StringBuilder gstDecImgBuilder = new StringBuilder();
            for (String name : GSTDeclarationImgList) {
                gstDecImgBuilder.append(",").append(name);
            }
            String gstDecImageName = gstDecImgBuilder.toString();
            if (!gstDecImageName.isEmpty()) {
                gstDecImageName = gstDecImageName.substring(1);
            }
            object.put("gstDeclarationImageName", gstDecImageName);
            object.put("tcsStatus", tcsSwitch.isChecked() ? "0" : "1");

            StringBuilder tcsDecImgBuilder = new StringBuilder();
            for (String name : TCSDeclarationImgList) {
                tcsDecImgBuilder.append(",").append(name);
            }
            String tcsDecImageName = tcsDecImgBuilder.toString();
            if (!tcsDecImageName.isEmpty()) {
                tcsDecImageName = tcsDecImageName.substring(1);
            }
            object.put("tcsDeclarationImageName", tcsDecImageName);

            object.put("agreementDetailsStr", agreementDetailsStr);

            StringBuilder agreementImgBuilder = new StringBuilder();
            for (String name : AgreementImgList) {
                agreementImgBuilder.append(",").append(name);
            }
            String agreementImageName = agreementImgBuilder.toString();
            if (!agreementImageName.isEmpty()) {
                agreementImageName = agreementImageName.substring(1);
            }
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
        params.put("axn", "save/customer_on_boarding");
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
                        if (line1.endsWith(", ")) line1 = line1.substring(0, line1.length() - 3);
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