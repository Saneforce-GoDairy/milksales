package com.saneforce.milksales.SFA_Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.saneforce.milksales.Activity_Hap.AllowancCapture;
import com.saneforce.milksales.Activity_Hap.ProductImageView;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
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

public class AddNewDistributor extends AppCompatActivity implements OnMapReadyCallback, UpdateResponseUI {

    TextView select_region, select_sales_office_name, select_route_name, select_channel, select_state, date_of_creation,
            select_mode_of_payment, submit, select_bank_details, select_agreement_copy;
    ImageView refreshLocation, display_customer_photo, capture_customer_photo, display_shop_photo, capture_shop_photo, display_bank_details, capture_bank_details, display_fssai, capture_fssai,
            display_gst, capture_gst, display_agreement_copy, capture_agreement_copy, display_deposit, capture_deposit, home, display_aadhaar_number, capture_aadhaar_number,
            display_pan_number, capture_pan_number;
    EditText type_city, type_pincode, type_name_of_the_customer, type_name_of_the_owner, type_address_of_the_shop, type_residence_address, type_mobile_number, type_email_id,
            type_sales_executive_name, type_sales_executive_employee_id, type_aadhaar_number, type_pan_number, type_gst, type_deposit, type_fssai;

    Context context = this;
    Common_Class common_class;
    Shared_Common_Pref pref;
    SharedPreferences UserDetails;

    CommonAdapterForDropdown adapter;
    RegionAdapter regionAdapter;
    CommonAdapterForDropdownWithFilter filterAdapter;
    ArrayList<CommonModelForDropDown> ChannelList, stateList, BankList, AgreementList, MOPList;
    ArrayList<CommonModelWithThreeString> regionList, regionFilteredList;
    ArrayList<CommonModelWithFourString> officeList, filteredOfficeList, tempOfficeList, routeList, filteredRouteList, tempRouteList;

    String customer_photo_url = "", customer_photo_name = "", shop_photo_url = "", shop_photo_name = "", regionStr = "", regionCodeStr = "", officeCodeStr = "", officeNameStr = "", routeCodeStr = "",
            routeNameStr = "", channelStr = "", channelIDStr = "", cityStr = "", pincodeStr = "", stateCodeStr = "", stateNameStr = "", customerNameStr = "", ownerNameStr = "", shopAddressStr = "",
            residenceAddressStr = "", mobileNumberStr = "", emailAddressStr = "", executiveNameStr = "", employeeIdStr = "", creationDateStr = "", aadhaarStr = "",
            PANStr = "", bankDetailsStr = "", bankImageName = "", bankImageFullPath = "", FSSAIDetailsStr = "", FSSAIImageName = "", FSSAIImageFullPath = "",
            GSTDetailsStr = "", GSTImageName = "", GSTImageFullPath = "", agreementDetailsStr = "", agreementImageName = "", agreementImageFullPath = "", modeOfPaymentStr = "",
            depositDetailsStr = "", depositImageName = "", depositImageFullPath = "", aadhaarImageName = "", aadhaarImageFullPath = "", panImageName = "", panImageFullPath = "";

    double Lat = 0, Long = 0;

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_distributor);

        select_region = findViewById(R.id.select_region);
        select_sales_office_name = findViewById(R.id.select_sales_office_name);
        select_route_name = findViewById(R.id.select_route_name);
        select_channel = findViewById(R.id.select_channel);
        select_state = findViewById(R.id.select_state);
        date_of_creation = findViewById(R.id.date_of_creation);
        select_mode_of_payment = findViewById(R.id.select_mode_of_payment);
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
        type_deposit = findViewById(R.id.type_deposit);
        display_agreement_copy = findViewById(R.id.display_agreement_copy);
        capture_agreement_copy = findViewById(R.id.capture_agreement_copy);
        display_deposit = findViewById(R.id.display_deposit);
        capture_deposit = findViewById(R.id.capture_deposit);
        home = findViewById(R.id.toolbar_home);
        type_city = findViewById(R.id.type_city);
        type_pincode = findViewById(R.id.type_pincode);
        type_name_of_the_customer = findViewById(R.id.type_name_of_the_customer);
        type_name_of_the_owner = findViewById(R.id.type_name_of_the_owner);
        type_address_of_the_shop = findViewById(R.id.type_address_of_the_shop);
        type_residence_address = findViewById(R.id.type_residence_address);
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

        common_class = new Common_Class(this);
        pref = new Shared_Common_Pref(this);
        UserDetails = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        common_class.gotoHomeScreen(context, home);

        regionList = new ArrayList<>();
        regionFilteredList = new ArrayList<>();
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
        capture_deposit.setOnClickListener(v -> {
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
        display_deposit.setOnClickListener(v -> {
            display_deposit.setEnabled(false);
            new Handler().postDelayed(() -> display_deposit.setEnabled(true), 1500);
            showImage(depositImageFullPath);
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
            if (stateList.isEmpty()) {
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
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            adapter = new CommonAdapterForDropdown(stateList, context);
            recyclerView1.setAdapter(adapter);
            AlertDialog dialog = builder.create();
            adapter.setSelectItem((model, position) -> {
                regionFilteredList.clear();
                for (CommonModelWithThreeString modelForDropDown : regionList) {
                    if (modelForDropDown.getReference().equalsIgnoreCase(model.getId())) {
                        regionFilteredList.add(modelForDropDown);
                    }
                }
                select_state.setText(model.getTitle());
                stateCodeStr = model.getId();
                select_region.setText("");
                regionCodeStr = "";
                dialog.dismiss();
            });
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });
        select_region.setOnClickListener(v -> {
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
        });
        select_sales_office_name.setOnClickListener(v -> {
            if (TextUtils.isEmpty(select_region.getText().toString().trim())) {
                Toast.makeText(context, "Please Select Region", Toast.LENGTH_SHORT).show();
                return;
            } else if (filteredOfficeList.isEmpty()) {
                Toast.makeText(context, "No Offices found for the selected Region", Toast.LENGTH_SHORT).show();
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
            title.setText("Select Sales Office Name");
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
            title.setText("Select Route Name");
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
                dialog.dismiss();
            });
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
        select_mode_of_payment.setOnClickListener(v -> {
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
        });
        refreshLocation.setOnClickListener(v -> getLocation());
        submit.setOnClickListener(v -> ValidateFields());

        getLocation();

        if (Common_Class.isNullOrEmpty(pref.getvalue(Constants.STATE_LIST))) {
            common_class.getDb_310Data(Constants.STATE_LIST, this);
        } else {
            PrepareStateList();
        }

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
    }

    private void PrepareStateList() {
        try {
            stateList.clear();
            JSONObject object = new JSONObject(pref.getvalue(Constants.STATE_LIST));
            JSONArray array = object.getJSONArray("Data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String id = jsonObject.getString("State_Code");
                String title = jsonObject.getString("StateName");
                stateList.add(new CommonModelForDropDown(id, title));
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocation() {
        new LocationFinder(this, location -> {
            try {
                Lat = location.getLatitude();
                Long = location.getLongitude();
                getCompleteAddressString(Lat, Long);
                SetMap();
            } catch (Exception ignored) { }
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
                                String regionReference = officeResponse.getJSONObject(i).getString("RegionId");
                                String officeReference = officeResponse.getJSONObject(i).getString("PlantId");
                                Log.e("ksjdhksd", "officeResponse: " + id + ", " + title + ", " + regionReference + ", " + officeReference);
                                officeList.add(new CommonModelWithFourString(id, title, regionReference, officeReference));
                            }
                            JSONArray routeResponse = object.getJSONArray("routeResponse");
                            for (int i = 0; i < routeResponse.length(); i++) {
                                String id = routeResponse.getJSONObject(i).getString("RouteCode");
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
        regionStr = select_region.getText().toString().trim();
        officeNameStr = select_sales_office_name.getText().toString().trim();
        routeNameStr = select_route_name.getText().toString().trim();
        channelStr = select_channel.getText().toString().trim();
        cityStr = type_city.getText().toString().trim();
        pincodeStr = type_pincode.getText().toString().trim();
        stateNameStr = select_state.getText().toString().trim();
        customerNameStr = type_name_of_the_customer.getText().toString().trim();
        ownerNameStr = type_name_of_the_owner.getText().toString().trim();
        shopAddressStr = type_address_of_the_shop.getText().toString().trim();
        residenceAddressStr = type_residence_address.getText().toString().trim();
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
        modeOfPaymentStr = select_mode_of_payment.getText().toString().trim();
        depositDetailsStr = type_deposit.getText().toString().trim();

        if (TextUtils.isEmpty(customer_photo_name) || TextUtils.isEmpty(customer_photo_url)) {
            Toast.makeText(context, "Please Capture Customer Photo", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(shop_photo_name) || TextUtils.isEmpty(shop_photo_url)) {
            Toast.makeText(context, "Please Capture Shop Photo", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(regionStr) || TextUtils.isEmpty(regionCodeStr)) {
            Toast.makeText(context, "Please Select the Region", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(officeNameStr) || TextUtils.isEmpty(officeCodeStr)) {
            Toast.makeText(context, "Please Select the Sales Office Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(routeNameStr) || TextUtils.isEmpty(routeCodeStr)) {
            Toast.makeText(context, "Please Select the Route Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(channelStr) || TextUtils.isEmpty(channelIDStr)) {
            Toast.makeText(context, "Please Select the Channel", Toast.LENGTH_SHORT).show();
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
        } else if (TextUtils.isEmpty(shopAddressStr)) {
            Toast.makeText(context, "Please Enter the Shop Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(residenceAddressStr)) {
            Toast.makeText(context, "Please Enter the Residence Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mobileNumberStr) || mobileNumberStr.length() != 10) {
            Toast.makeText(context, "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(executiveNameStr)) {
            Toast.makeText(context, "Please Enter the Sales Executive Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(employeeIdStr)) {
            Toast.makeText(context, "Please Enter the Sales Executive - Employee ID", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(creationDateStr)) {
            Toast.makeText(context, "Creation Date Can't be Fetched", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(aadhaarStr)) {
            Toast.makeText(context, "Please Enter the Aadhaar Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(aadhaarImageName) || TextUtils.isEmpty(aadhaarImageFullPath)) {
            Toast.makeText(context, "Please Capture the Aadhaar Image", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(PANStr)) {
            Toast.makeText(context, "Please Enter the PAN Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(panImageName) || TextUtils.isEmpty(panImageFullPath)) {
            Toast.makeText(context, "Please Capture the PAN Image", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(GSTDetailsStr)) {
            Toast.makeText(context, "Please Enter the GST Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(GSTImageName) || TextUtils.isEmpty(GSTImageFullPath)) {
            Toast.makeText(context, "Please Capture the GST Certificate", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(modeOfPaymentStr)) {
            Toast.makeText(context, "Please Select the Mode of Payment", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(depositDetailsStr)) {
            Toast.makeText(context, "Please Enter the Deposit Details", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(depositImageName) || TextUtils.isEmpty(depositImageFullPath)) {
            Toast.makeText(context, "Please Capture the Deposit Details", Toast.LENGTH_SHORT).show();
        } else if ((Lat == 0) || (Long == 0)) {
            Toast.makeText(context, "Location can't be fetched", Toast.LENGTH_SHORT).show();
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
            object.put("regionCodeStr", regionCodeStr);
            object.put("regionStr", regionStr);
            object.put("officeCodeStr", officeCodeStr);
            object.put("officeNameStr", officeNameStr);
            object.put("routeCodeStr", routeCodeStr);
            object.put("routeNameStr", routeNameStr);
            object.put("channelIDStr", channelIDStr);
            object.put("channelStr", channelStr);
            object.put("cityStr", cityStr);
            object.put("pincodeStr", pincodeStr);
            object.put("stateCodeStr", stateCodeStr);
            object.put("stateNameStr", stateNameStr);
            object.put("customerNameStr", customerNameStr);
            object.put("ownerNameStr", ownerNameStr);
            object.put("shopAddressStr", shopAddressStr);
            object.put("residenceAddressStr", residenceAddressStr);
            object.put("mobileNumberStr", mobileNumberStr);
            object.put("emailAddressStr", emailAddressStr);
            object.put("executiveNameStr", executiveNameStr);
            object.put("employeeIdStr", employeeIdStr);
            object.put("creationDateStr", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime()));
            object.put("aadhaarStr", aadhaarStr);
            object.put("aadhaarImage", aadhaarImageName);
            object.put("PANStr", PANStr);
            object.put("panImage", panImageName);
            object.put("bankDetailsStr", bankDetailsStr);
            object.put("bankImageName", bankImageName);
            object.put("FSSAIDetailsStr", FSSAIDetailsStr);
            object.put("FSSAIImageName", FSSAIImageName);
            object.put("GSTDetailsStr", GSTDetailsStr);
            object.put("GSTImageName", GSTImageName);
            object.put("agreementDetailsStr", agreementDetailsStr);
            object.put("agreementImageName", agreementImageName);
            object.put("modeOfPaymentStr", modeOfPaymentStr);
            object.put("depositDetailsStr", depositDetailsStr);
            object.put("depositImageName", depositImageName);
            object.put("lat", Lat);
            object.put("long", Long);
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
                            Toast.makeText(context, jsonObject.getString("response"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Request does not reached the server", Toast.LENGTH_SHORT).show();
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
                type_address_of_the_shop.setText(strReturnedAddress.toString());
                type_city.setText(returnedAddress.getLocality());
                type_pincode.setText(returnedAddress.getPostalCode());
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
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        if (key.equals(Constants.STATE_LIST)) {
            PrepareStateList();
        }
    }
}