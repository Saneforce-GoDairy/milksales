package com.saneforce.godairy.Activity_Hap;

import static com.saneforce.godairy.Common_Class.Common_Class.UserDetail;
import static com.saneforce.godairy.Common_Class.Constants.CUSTOMER_DATA;
import static com.saneforce.godairy.Common_Class.Constants.Retailer_OutletList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.google.gson.JsonObject;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.MyAlertDialog;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Interface.OnImagePickListener;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.common.LocationFinder;
import com.saneforce.godairy.databinding.ActivityAddNewRetailerBinding;
import com.saneforce.godairy.universal.UniversalDropDownAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class AddNewRetailer extends AppCompatActivity implements OnMapReadyCallback, Master_Interface, UpdateResponseUI {
    ActivityAddNewRetailerBinding binding;

    String categoryID = "", categoryTITLE = "", subCategoryID = "", subCategoryTITLE = "", outletStatusID = "", outletStatusTITLE = "", deliveryTypeID = "", deliveryTypeTITLE = "", stateID = "", stateTITLE = "", shopImageName = "", shopImageFullPath = "", distGrpERP = "", distributorERP = "", divERP = "", customer_code = "", visiCoolerCompanyID = "", visiCoolerCompanyTITLE = "", routeID = "", routeTITLE = "", distributorID = "", distributorTITLE = "", uniqueKey = "", outletCode = "", flag = "";
    double lat = 0, lng = 0;
    JSONArray fieldDetailsArray, outletTypeArray, categoryListArray, subCategoryListArray, filteredSubCategoryListArray, visiCoolerCompanyArray, deliveryTypeArray, stateListArray, routeListArray;

    UniversalDropDownAdapter adapter;
    AssistantClass assistantClass;

    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;
    SharedPreferences userDetails;
    Context context = this;
    GoogleMap mGoogleMap;
    boolean isPhotoMandatory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewRetailerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fieldDetailsArray = new JSONArray();
        outletTypeArray = new JSONArray();
        categoryListArray = new JSONArray();
        subCategoryListArray = new JSONArray();
        filteredSubCategoryListArray = new JSONArray();
        visiCoolerCompanyArray = new JSONArray();
        deliveryTypeArray = new JSONArray();
        stateListArray = new JSONArray();
        routeListArray = new JSONArray();

        assistantClass = new AssistantClass(context);
        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);
        distGrpERP = shared_common_pref.getvalue(Constants.CusSubGrpErp);
        distributorERP = shared_common_pref.getvalue(Constants.DistributorERP);
        divERP = shared_common_pref.getvalue(Constants.DivERP);
        userDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        common_class.gotoHomeScreen(context, binding.toolbar.toolbarHome);

        binding.btnRefLoc.setOnClickListener(v -> {
            binding.btnRefLoc.startAnimation();
            setLocation();
        });

        binding.ivShopPhoto.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "outlet_info", new Common_Class.ImageUploadListener() {
                        @Override
                        public void onSuccess() {
                            shopImageName = FileName;
                            shopImageFullPath = fullPath;
                            binding.ivShopPhoto.setImageBitmap(image);
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

        binding.ivProfileView.setOnClickListener(v -> {
            if (!shopImageFullPath.isEmpty()) {
                Intent intent = new Intent(context, ProductImageView.class);
                intent.putExtra("ImageUrl", Uri.fromFile(new File(shopImageFullPath)).toString());
                startActivity(intent);
            } else {
                Toast.makeText(context, "Please capture shop photo", Toast.LENGTH_SHORT).show();
            }
        });

        binding.selectDistributor.setOnClickListener(v -> {
            common_class.showCommonDialog(common_class.getDistList(), 2, this);
        });

        binding.selectOutletStatus.setOnClickListener(v -> ShowDropdown(1, prepareTitle(binding.outletStatusTitle.getText().toString()), outletTypeArray));

        binding.selectRoute.setOnClickListener(v -> ShowDropdown(2, prepareTitle(binding.routeTitle.getText().toString()), routeListArray));

        binding.selectState.setOnClickListener(v -> ShowDropdown(3, prepareTitle(binding.stateTitle.getText().toString()), stateListArray));

        binding.selectDeliveryType.setOnClickListener(v -> ShowDropdown(4, prepareTitle(binding.deliveryTypeTitle.getText().toString()), deliveryTypeArray));

        binding.selectOutletCategory.setOnClickListener(v -> ShowDropdown(5, prepareTitle(binding.outletCategoryTitle.getText().toString()), categoryListArray));

        binding.selectOutletSubCategory.setOnClickListener(v -> {
            if (binding.selectOutletCategory.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, "Please " + binding.selectOutletCategory.getHint().toString().trim() + " First", Toast.LENGTH_SHORT).show();
            } else {
                ShowDropdown(6, prepareTitle(binding.outletSubCategoryTitle.getText().toString()), filteredSubCategoryListArray);
            }
        });

        binding.selectVisiCoolerCompany.setOnClickListener(v -> ShowDropdown(7, prepareTitle(binding.visiCoolerCompanyTitle.getText().toString()), visiCoolerCompanyArray));

        binding.switchVisiCoolerAvailable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.visiCoolerCompanyLL.setVisibility(View.VISIBLE);
            } else {
                binding.visiCoolerCompanyLL.setVisibility(View.GONE);
                visiCoolerCompanyID = "";
                visiCoolerCompanyTITLE = "";
                binding.selectVisiCoolerCompany.setText(visiCoolerCompanyTITLE);
            }
        });

        binding.switchIceCreamFreezerAvailable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.iceCreamFreezerCompanyLL.setVisibility(View.VISIBLE);
            } else {
                binding.iceCreamFreezerCompanyLL.setVisibility(View.GONE);
                binding.enterIceCreamFreezerCompany.setText("");
            }
        });

        binding.switchVisiCoolerAvailable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.visiCoolerCompanyLL.setVisibility(View.VISIBLE);
            } else {
                binding.visiCoolerCompanyLL.setVisibility(View.GONE);
                visiCoolerCompanyID = "";
                visiCoolerCompanyTITLE = "";
                binding.selectVisiCoolerCompany.setText(visiCoolerCompanyTITLE);
            }
        });

        binding.submitButton.setOnClickListener(v -> ValidateFields());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        uniqueKey = Common_Class.GetEkey();

        if (getIntent().hasExtra("outletCode")) {
            outletCode = getIntent().getStringExtra("outletCode");
            flag = getIntent().getStringExtra("flag"); // 1 - View, 2 - Edit
            if (flag.equals("1")) {
                binding.headtext.setText("Outlet Info");
                makeEditable(false);
            } else if (flag.equals("2")) {
                binding.headtext.setText("Edit Outlet");
                binding.submitButton.setText("Update");
                binding.submitButton.setVisibility(View.VISIBLE);
                makeEditable(true);
            }
            getOutletInfo();
        } else {
            binding.submitButton.setVisibility(View.VISIBLE);
            setLocation();
        }

        binding.distributorTitle.setText(Html.fromHtml("Distributor " + "<span style=\"color:#E53935\">*</span>"));
        getDropdowns();
    }

    private void makeEditable(boolean b) {
        binding.enterOutletName.setEnabled(b);
        binding.enterOwnerName.setEnabled(b);
        binding.enterAddress.setEnabled(b);
        binding.enterLocation.setEnabled(b);
        binding.enterDistrict.setEnabled(b);
        binding.enterGST.setEnabled(b);
        binding.enterMobileNumber.setEnabled(b);
        binding.enterSecMobileNumber.setEnabled(b);
        binding.enterEmail.setEnabled(b);
        binding.enterFSSAINumber.setEnabled(b);
        binding.enterPANNumber.setEnabled(b);
        binding.enterOutstandingAmount.setEnabled(b);
        binding.enterPincode.setEnabled(b);
        binding.enterReasonForClosed.setEnabled(b);
        binding.enterWhatsappNumber.setEnabled(b);
        binding.enterIceCreamFreezerCompany.setEnabled(b);
        binding.switchVisiCoolerAvailable.setEnabled(b);
        binding.switchIceCreamFreezerAvailable.setEnabled(b);
        binding.switchLactalisFreezerRequirement.setEnabled(b);
        binding.selectDistributor.setEnabled(b);
        binding.selectOutletStatus.setEnabled(b);
        binding.selectRoute.setEnabled(b);
        binding.selectState.setEnabled(b);
        binding.selectDeliveryType.setEnabled(b);
        binding.selectOutletCategory.setEnabled(b);
        binding.selectOutletSubCategory.setEnabled(b);
        binding.selectVisiCoolerCompany.setEnabled(b);
        binding.ivShopPhoto.setEnabled(b);
        binding.btnRefLoc.setEnabled(b);
    }

    private void getOutletInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_outlet_info");
        params.put("outletCode", outletCode);
        Common_Class.makeApiCall(context, params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                AssignOutletDetails(jsonObject);
            }

            @Override
            public void onFailure(String error) {
                MyAlertDialog.show(context, "", error, false, "Dismiss", "", new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        finish();
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void AssignOutletDetails(JSONObject object) {
        Log.e("AssignOutletDetails", "AssignOutletDetails: " + object);
        try {
            JSONObject jsonObject = object.optJSONObject("response");
            outletCode = jsonObject.optString("ListedDrCode");

            binding.enterOutletName.setText(jsonObject.optString("ListedDr_Name"));
            binding.enterOwnerName.setText(jsonObject.optString("Owner_Name"));
            binding.enterAddress.setText(jsonObject.optString("ListedDr_Address1"));
            binding.enterLocation.setText(jsonObject.optString("cityname"));
            binding.enterDistrict.setText(jsonObject.optString("SDP"));
            binding.enterGST.setText(jsonObject.optString("GST"));
            binding.enterMobileNumber.setText(jsonObject.optString("ListedDr_Mobile"));
            binding.enterSecMobileNumber.setText(jsonObject.optString("ListedDr_Phone"));
            binding.enterEmail.setText(jsonObject.optString("ListedDr_Email"));
            binding.enterFSSAINumber.setText(jsonObject.optString("FssiNo"));
            binding.enterPANNumber.setText(jsonObject.optString("Pan_No"));
            binding.enterOutstandingAmount.setText(jsonObject.optString("Outstanding"));
            binding.enterPincode.setText(jsonObject.optString("pin_code"));
            binding.enterReasonForClosed.setText(jsonObject.optString("closedReason"));
            binding.enterWhatsappNumber.setText(jsonObject.optString("whatsapp"));
            binding.enterIceCreamFreezerCompany.setText(jsonObject.optString("freezerCompany"));

            binding.switchVisiCoolerAvailable.setChecked(jsonObject.optString("visiCoolerAvailability").equals("1"));
            binding.switchIceCreamFreezerAvailable.setChecked(jsonObject.optString("iceCreamFreezerAvailability").equals("1"));
            binding.switchLactalisFreezerRequirement.setChecked(jsonObject.optString("lactalisFreezerRequired").equals("1"));

            distributorID = jsonObject.optString("Dist_name");
            distributorTITLE = jsonObject.optString("distName");
            binding.selectDistributor.setText(distributorTITLE);

            outletStatusID = jsonObject.optString("Outlet_Type");
            outletStatusTITLE = jsonObject.optString("outletStatus");
            binding.selectOutletStatus.setText(outletStatusTITLE);
            if (outletStatusTITLE.contains("Closed")) {
                binding.enterReasonForClosed.setVisibility(View.VISIBLE);
            } else {
                binding.enterReasonForClosed.setVisibility(View.GONE);
            }

            routeID = jsonObject.optString("Territory_Code");
            routeTITLE = jsonObject.optString("route");
            binding.selectRoute.setText(routeTITLE);

            stateID = jsonObject.optString("State_Code");
            stateTITLE = jsonObject.optString("State_Name");
            binding.selectState.setText(stateTITLE);

            deliveryTypeID = jsonObject.optString("deliveryTypeId");
            deliveryTypeTITLE = jsonObject.optString("Allowance_Type");
            binding.selectDeliveryType.setText(deliveryTypeTITLE);

            categoryID = jsonObject.optString("categoryId");
            categoryTITLE = jsonObject.optString("Category_Universe_Name");
            binding.selectOutletCategory.setText(categoryTITLE);

            subCategoryID = jsonObject.optString("subCategoryId");
            subCategoryTITLE = jsonObject.optString("Category_Universe_Remarks");
            binding.selectOutletSubCategory.setText(subCategoryTITLE);

            visiCoolerCompanyID = jsonObject.optString("visiCoolerCompanyId");
            visiCoolerCompanyTITLE = jsonObject.optString("visiCoolerCompany");
            binding.selectVisiCoolerCompany.setText(visiCoolerCompanyTITLE);

            lat = jsonObject.optDouble("ListedDr_Class_Patients");
            lng = jsonObject.optDouble("ListedDr_Consultation_Fee");
            centreMapOnLocation("Your Location");

            shopImageName = jsonObject.optString("ListedDr_Pin");
            common_class.getImageFromS3Bucket(context, shopImageName, "outlet_info", (bmp, path) -> {
                binding.ivShopPhoto.setImageBitmap(bmp);
                shopImageFullPath = path;
            });
        } catch (Exception ignored) {

        }
    }

    private String prepareTitle(String s) {
        return "Select " + s.split("\\*")[0].trim();
    }

    private void ValidateFields() {
        String outletName = binding.enterOutletName.getText().toString().trim();
        String reasonForClose = binding.enterReasonForClosed.getText().toString().trim();
        String ownerName = binding.enterOwnerName.getText().toString().trim();
        String outletAddress = binding.enterAddress.getText().toString().trim();
        String location = binding.enterLocation.getText().toString().trim();
        String district = binding.enterDistrict.getText().toString().trim();
        String pincode = binding.enterPincode.getText().toString().trim();
        String gst = binding.enterGST.getText().toString().trim();
        String mobile = binding.enterMobileNumber.getText().toString().trim();
        String secMobile = binding.enterSecMobileNumber.getText().toString().trim();
        String whatsapp = binding.enterWhatsappNumber.getText().toString().trim();
        String email = binding.enterEmail.getText().toString().trim();
        String freezerCompany = binding.enterIceCreamFreezerCompany.getText().toString().trim();
        String fssai = binding.enterFSSAINumber.getText().toString().trim();
        String pan = binding.enterPANNumber.getText().toString().trim();
        String outstandingAmt = binding.enterOutstandingAmount.getText().toString().trim();

        if (binding.distributorLL.getVisibility() == View.VISIBLE && binding.distributorTitle.getText().toString().contains("*") && (distributorID.isEmpty() || distributorTITLE.isEmpty())) {
            Toast.makeText(context, "Please " + binding.selectDistributor.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.selectDistributor.requestFocus();
        } else if (binding.outletNameLL.getVisibility() == View.VISIBLE && binding.outletNameTitle.getText().toString().contains("*") && outletName.isEmpty()) {
            Toast.makeText(context, "Please " + binding.enterOutletName.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.enterOutletName.requestFocus();
        } else if (binding.outletStatusLL.getVisibility() == View.VISIBLE && binding.outletStatusTitle.getText().toString().contains("*") && outletStatusTITLE.isEmpty()) {
            Toast.makeText(context, "Please " + binding.selectOutletStatus.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.selectOutletStatus.requestFocus();
        } else if (binding.reasonForClosedLL.getVisibility() == View.VISIBLE && binding.reasonForClosedTitle.getText().toString().contains("*") && reasonForClose.isEmpty()) {
            Toast.makeText(context, "Please " + binding.enterReasonForClosed.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.enterReasonForClosed.requestFocus();
        } else if (binding.routeLL.getVisibility() == View.VISIBLE && binding.routeTitle.getText().toString().contains("*") && routeTITLE.isEmpty()) {
            Toast.makeText(context, "Please " + binding.selectRoute.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.selectRoute.requestFocus();
        } else if (binding.ownerNameLL.getVisibility() == View.VISIBLE && binding.ownerNameTitle.getText().toString().contains("*") && ownerName.isEmpty()) {
            Toast.makeText(context, "Please " + binding.enterOwnerName.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.enterOwnerName.requestFocus();
        } else if (binding.photoLL.getVisibility() == View.VISIBLE && isPhotoMandatory && shopImageName.isEmpty()) {
            Toast.makeText(context, "Please Capture Shop Photo", Toast.LENGTH_SHORT).show();
            binding.ivShopPhoto.requestFocus();
        } else if (binding.addressLL.getVisibility() == View.VISIBLE && binding.addressTitle.getText().toString().contains("*") && outletAddress.isEmpty()) {
            Toast.makeText(context, "Please " + binding.enterAddress.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.enterAddress.requestFocus();
        } else if (binding.stateLL.getVisibility() == View.VISIBLE && binding.stateTitle.getText().toString().contains("*") && stateTITLE.isEmpty()) {
            Toast.makeText(context, "Please " + binding.selectState.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.selectState.requestFocus();
        } else if (binding.locationLL.getVisibility() == View.VISIBLE && binding.locationTitle.getText().toString().contains("*") && location.isEmpty()) {
            Toast.makeText(context, "Please " + binding.enterLocation.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.enterLocation.requestFocus();
        } else if (binding.districtLL.getVisibility() == View.VISIBLE && binding.districtTitle.getText().toString().contains("*") && district.isEmpty()) {
            Toast.makeText(context, "Please " + binding.enterDistrict.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.enterDistrict.requestFocus();
        } else if (binding.pincodeLL.getVisibility() == View.VISIBLE && binding.pincodeTitle.getText().toString().contains("*") && pincode.isEmpty()) {
            Toast.makeText(context, "Please " + binding.enterPincode.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.enterPincode.requestFocus();
        } else if (binding.gstLL.getVisibility() == View.VISIBLE && binding.gstTitle.getText().toString().contains("*") && gst.length() != 15) {
            Toast.makeText(context, "Please " + binding.enterGST.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.enterGST.requestFocus();
        } else if (binding.mobileNumberLL.getVisibility() == View.VISIBLE && binding.mobileNumberTitle.getText().toString().contains("*") && mobile.length() != 10) {
            Toast.makeText(context, "Please " + binding.enterMobileNumber.getHint().toString().trim() + " (10 Digits)", Toast.LENGTH_SHORT).show();
            binding.enterMobileNumber.requestFocus();
        } else if (binding.secMobileNumberLL.getVisibility() == View.VISIBLE && binding.secMobileNumberTitle.getText().toString().contains("*") && secMobile.length() != 10) {
            Toast.makeText(context, "Please " + binding.enterSecMobileNumber.getHint().toString().trim() + " (10 Digits)", Toast.LENGTH_SHORT).show();
            binding.enterSecMobileNumber.requestFocus();
        } else if (binding.whatsappNumberLL.getVisibility() == View.VISIBLE && binding.whatsappNumberTitle.getText().toString().contains("*") && whatsapp.length() != 10) {
            Toast.makeText(context, "Please " + binding.enterWhatsappNumber.getHint().toString().trim() + " (10 Digits)", Toast.LENGTH_SHORT).show();
            binding.enterWhatsappNumber.requestFocus();
        } else if (binding.emailLL.getVisibility() == View.VISIBLE && binding.emailTitle.getText().toString().contains("*") && email.isEmpty()) {
            Toast.makeText(context, "Please " + binding.enterEmail.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.enterEmail.requestFocus();
        } else if (binding.deliveryTypeLL.getVisibility() == View.VISIBLE && binding.deliveryTypeTitle.getText().toString().contains("*") && deliveryTypeTITLE.isEmpty()) {
            Toast.makeText(context, "Please " + binding.selectDeliveryType.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.selectDeliveryType.requestFocus();
        } else if (binding.outletCategoryLL.getVisibility() == View.VISIBLE && binding.outletCategoryTitle.getText().toString().contains("*") && categoryTITLE.isEmpty()) {
            Toast.makeText(context, "Please " + binding.selectOutletCategory.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.selectOutletCategory.requestFocus();
        } else if (binding.outletSubCategoryLL.getVisibility() == View.VISIBLE && binding.outletSubCategoryTitle.getText().toString().contains("*") && subCategoryTITLE.isEmpty()) {
            Toast.makeText(context, "Please " + binding.selectOutletSubCategory.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.selectOutletSubCategory.requestFocus();
        } else if (binding.switchVisiCoolerAvailable.isChecked() && binding.visiCoolerCompanyLL.getVisibility() == View.VISIBLE && binding.visiCoolerCompanyTitle.getText().toString().contains("*") && visiCoolerCompanyTITLE.isEmpty()) {
            Toast.makeText(context, "Please " + binding.selectVisiCoolerCompany.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.selectVisiCoolerCompany.requestFocus();
        } else if (binding.switchIceCreamFreezerAvailable.isChecked() && binding.iceCreamFreezerCompanyLL.getVisibility() == View.VISIBLE && binding.iceCreamFreezerCompanyTitle.getText().toString().contains("*") && freezerCompany.isEmpty()) {
            Toast.makeText(context, "Please " + binding.enterIceCreamFreezerCompany.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.enterIceCreamFreezerCompany.requestFocus();
        } else if (binding.fssaiNumberLL.getVisibility() == View.VISIBLE && binding.fssaiNumberTitle.getText().toString().contains("*") && fssai.length() != 14) {
            Toast.makeText(context, "Please " + binding.enterFSSAINumber.getHint().toString().trim() + " (14 Digits)", Toast.LENGTH_SHORT).show();
            binding.enterFSSAINumber.requestFocus();
        } else if (binding.panNumberLL.getVisibility() == View.VISIBLE && binding.panNumberTitle.getText().toString().contains("*") && pan.length() != 10) {
            Toast.makeText(context, "Please " + binding.enterPANNumber.getHint().toString().trim() + " (10 Digits)", Toast.LENGTH_SHORT).show();
            binding.enterPANNumber.requestFocus();
        } else if (binding.outstandingAmountLL.getVisibility() == View.VISIBLE && binding.outstandingAmountTitle.getText().toString().contains("*") && outstandingAmt.isEmpty()) {
            Toast.makeText(context, "Please " + binding.enterOutstandingAmount.getHint().toString().trim(), Toast.LENGTH_SHORT).show();
            binding.enterOutstandingAmount.requestFocus();
        } else {
            MyAlertDialog.show(context, "", "Are you sure you want to " + (flag.equals("2") ? "update?" : "submit?"), true, "Yes", "No", new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {
                    JSONObject object = new JSONObject();
                    try {
                        if (flag.equals("2")) {
                            object.put("outletCode", outletCode);
                        }
                        object.put("distId", distributorID);
                        object.put("distName", distributorTITLE);
                        object.put("outletName", outletName);
                        object.put("outletStatusId", outletStatusID);
                        object.put("outletStatus", outletStatusTITLE);
                        object.put("closedReason", reasonForClose);
                        object.put("routeId", routeID);
                        object.put("route", routeTITLE);
                        object.put("ownerName", ownerName);
                        object.put("shopImage", shopImageName);
                        object.put("address", outletAddress);
                        object.put("stateId", stateID);
                        object.put("state", stateTITLE);
                        object.put("location", location);
                        object.put("district", district);
                        object.put("pincode", pincode);
                        object.put("gst", gst);
                        object.put("mobile", mobile);
                        object.put("secMobile", secMobile);
                        object.put("whatsapp", whatsapp);
                        object.put("email", email);
                        object.put("deliveryTypeId", deliveryTypeID);
                        object.put("deliveryType", deliveryTypeTITLE);
                        object.put("categoryId", categoryID);
                        object.put("category", categoryTITLE);
                        object.put("subCategoryId", subCategoryID);
                        object.put("subCategory", subCategoryTITLE);
                        object.put("visiCoolerAvailability", binding.switchVisiCoolerAvailable.isChecked() ? "1" : "0");
                        object.put("visiCoolerCompanyId", visiCoolerCompanyID);
                        object.put("visiCoolerCompany", visiCoolerCompanyTITLE);
                        object.put("iceCreamFreezerAvailability", binding.switchIceCreamFreezerAvailable.isChecked() ? "1" : "0");
                        object.put("freezerCompany", freezerCompany);
                        object.put("lactalisFreezerRequired", binding.switchLactalisFreezerRequirement.isChecked() ? "1" : "0");
                        object.put("fssai", fssai);
                        object.put("pan", pan);
                        object.put("outstandingAmt", outstandingAmt);
                        object.put("uniqueKey", uniqueKey);
                        object.put("loginType", shared_common_pref.getvalue(Constants.LOGIN_TYPE));
                        object.put("lat", String.valueOf(lat));
                        object.put("lng", String.valueOf(lng));
                        object.put("sfCode", userDetails.getString("Sfcode", ""));
                        object.put("divisionCode", userDetails.getString("Divcode", "").split(",")[0]);
                    } catch (JSONException ignored) {
                    }
                    SaveOutlet(object);
                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void SaveOutlet(JSONObject object) {
        assistantClass.showProgressDialog("Saving...", false);
        Log.e("SaveOutlet", "SaveOutlet: " + object.toString());
        Map<String, String> params = new HashMap<>();
        if (flag.equals("2")) {
            params.put("axn", "update_retailer");
            params.put("outletCode", outletCode);
        } else {
            params.put("axn", "save_retailer");
        }
        Common_Class.makeApiCall(context, params, object.toString(), new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                common_class.getDataFromApi(Retailer_OutletList, AddNewRetailer.this, false);
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithFinish(jsonObject.optString("msg"));
            }

            @Override
            public void onFailure(String error) {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithDismiss(error);
            }
        });
    }

    private void setLocation() {
        new LocationFinder(context, location -> {
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                if (lat != 0 && lng != 0) {
                    setAddress();
                } else {
                    setLocation();
                }
            } else {
                setLocation();
            }
        });
    }

    private void setAddress() {
        try {
            Address address = Common_Class.getAddressFromLatLong(context, lat, lng);
            StringBuilder strReturnedAddress = new StringBuilder();
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(address.getAddressLine(i)).append("\n");
            }
            binding.enterAddress.setText(strReturnedAddress.toString().trim());
            binding.enterLocation.setText(address.getLocality());
            binding.enterPincode.setText(address.getPostalCode());

            centreMapOnLocation("Your Location");
        } catch (Exception e) {
            new Handler().postDelayed(this::setAddress, 5000);
        }
    }

    private void ShowDropdown(int which, String dropdownTitle, JSONArray array) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv_and_filter, null, false);
        builder.setView(view);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        TextView title = view.findViewById(R.id.title);
        title.setText(dropdownTitle);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        adapter = new UniversalDropDownAdapter(context, array);
        TextView close = view.findViewById(R.id.close);
        close.setOnClickListener(v1 -> dialog.dismiss());
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
                adapter.getFilter().filter(s.toString());
            }
        });
        adapter.setOnItemClick((position, arrayList) -> {
            switch (which) {
                case 1:
                    try {
                        outletStatusID = arrayList.getJSONObject(position).getString("id");
                        outletStatusTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.selectOutletStatus.setText(outletStatusTITLE);
                        if (outletStatusID.equals("3")) {
                            binding.reasonForClosedLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.reasonForClosedLL.setVisibility(View.GONE);
                            binding.enterReasonForClosed.setText("");
                        }
                    } catch (JSONException ignored) {
                    }
                    break;
                case 2:
                    try {
                        routeID = arrayList.getJSONObject(position).getString("id");
                        routeTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.selectRoute.setText(routeTITLE);
                    } catch (JSONException ignored) {
                    }
                    break;
                case 3:
                    try {
                        stateID = arrayList.getJSONObject(position).getString("id");
                        stateTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.selectState.setText(stateTITLE);
                    } catch (JSONException ignored) {
                    }
                    break;
                case 4:
                    try {
                        deliveryTypeID = arrayList.getJSONObject(position).getString("id");
                        deliveryTypeTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.selectDeliveryType.setText(deliveryTypeTITLE);
                    } catch (JSONException ignored) {
                    }
                    break;
                case 5:
                    try {
                        categoryID = arrayList.getJSONObject(position).getString("id");
                        categoryTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.selectOutletCategory.setText(categoryTITLE);
                        subCategoryID = "";
                        subCategoryTITLE = "";
                        binding.selectOutletSubCategory.setText(subCategoryTITLE);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            filteredSubCategoryListArray = new JSONArray();
                            try {
                                for (int i = 0; i < subCategoryListArray.length(); i++) {
                                    String BiggerValue = subCategoryListArray.getJSONObject(i).getString("CategoryCode");
                                    if (BiggerValue.contains("," + categoryID + ",")) {
                                        filteredSubCategoryListArray.put(subCategoryListArray.getJSONObject(i));
                                    }
                                }
                            } catch (JSONException ignored) {
                            }
                        });
                    } catch (JSONException ignored) {
                    }
                    break;
                case 6:
                    try {
                        subCategoryID = arrayList.getJSONObject(position).getString("id");
                        subCategoryTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.selectOutletSubCategory.setText(subCategoryTITLE);
                    } catch (JSONException ignored) {
                    }
                    break;
                case 7:
                    try {
                        visiCoolerCompanyID = arrayList.getJSONObject(position).getString("id");
                        visiCoolerCompanyTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.selectVisiCoolerCompany.setText(visiCoolerCompanyTITLE);
                    } catch (JSONException ignored) {
                    }
                    break;
            }
            dialog.dismiss();
        });
        recyclerView.setAdapter(adapter);
        dialog.show();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        centreMapOnLocation("Your Location");
    }

    public void centreMapOnLocation(String title) {
        LatLng userLocation = new LatLng(lat, lng);
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions().position(userLocation).title(title));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));

        try {
            binding.btnRefLoc.doneLoadingAnimation(getResources().getColor(R.color.green), BitmapFactory.decodeResource(getResources(), R.drawable.done));
            binding.btnRefLoc.stopAnimation();
            binding.btnRefLoc.revertAnimation();
            binding.btnRefLoc.setBackgroundResource((R.drawable.button_blueg));
        } catch (Resources.NotFoundException ignored) {
        }
    }

    private void getDropdowns() {
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_outet_creation_dropdowns");
        Common_Class.makeApiCall(context, params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        fieldDetailsArray = jsonObject.getJSONArray("fieldDetails");
                        outletTypeArray = jsonObject.getJSONArray("outletType");
                        categoryListArray = jsonObject.getJSONArray("categoryList");
                        subCategoryListArray = jsonObject.getJSONArray("subCategoryList");
                        deliveryTypeArray = jsonObject.getJSONArray("deliveryType");
                        stateListArray = jsonObject.getJSONArray("stateList");
                        visiCoolerCompanyArray = jsonObject.getJSONArray("visiCoolerCompany");
                        runOnUiThread(() -> prepareViews());
                    } catch (Exception ignored) {
                    }
                });
            }

            @Override
            public void onFailure(String error) {
            }
        });
    }

    private void prepareViews() {
        for (int i = 0; i < fieldDetailsArray.length(); i++) {
            try {
                JSONObject object = fieldDetailsArray.getJSONObject(i);
                String title = object.optString("title");
                String sName = object.optString("sName");
                String visibility = object.optString("visibility");
                String mandatory = object.optString("mandatory");
                switch (sName) {
                    case "category":
                        if (visibility.equals("0")) {
                            binding.outletCategoryLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.outletCategoryTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.outletCategoryTitle.setText(title);
                        }
                        binding.selectOutletCategory.setHint("Select " + title);
                        break;
                    case "deliveryType":
                        if (visibility.equals("0")) {
                            binding.deliveryTypeLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.deliveryTypeTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.deliveryTypeTitle.setText(title);
                        }
                        binding.selectDeliveryType.setHint("Select " + title);
                        break;
                    case "district":
                        if (visibility.equals("0")) {
                            binding.districtLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.districtTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.districtTitle.setText(title);
                        }
                        binding.enterDistrict.setHint("Enter " + title);
                        break;
                    case "email":
                        if (visibility.equals("0")) {
                            binding.emailLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.emailTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.emailTitle.setText(title);
                        }
                        binding.enterEmail.setHint("Enter " + title);
                        break;
                    case "fssai":
                        if (visibility.equals("0")) {
                            binding.fssaiNumberLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.fssaiNumberTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.fssaiNumberTitle.setText(title);
                        }
                        binding.enterFSSAINumber.setHint("Enter " + title);
                        break;
                    case "gst":
                        if (visibility.equals("0")) {
                            binding.gstLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.gstTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.gstTitle.setText(title);
                        }
                        binding.enterGST.setHint("Enter " + title);
                        break;
                    case "iceCreamAvail":
                        if (visibility.equals("0")) {
                            binding.iceCreamFreezerAvailableLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.iceCreamFreezerAvailableTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                            binding.switchIceCreamFreezerAvailable.setChecked(true);
                            binding.switchIceCreamFreezerAvailable.setEnabled(false);
                        } else {
                            binding.iceCreamFreezerAvailableTitle.setText(title);
                        }
                        break;
                    case "iceCreamComp":
                        if (visibility.equals("0")) {
                            binding.iceCreamFreezerCompanyLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.iceCreamFreezerCompanyTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.iceCreamFreezerCompanyTitle.setText(title);
                        }
                        binding.enterIceCreamFreezerCompany.setHint("Enter " + title);
                        break;
                    case "lacFreezReq":
                        if (visibility.equals("0")) {
                            binding.lactalisFreezerRequirementLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.lactalisFreezerRequirementTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                            binding.switchLactalisFreezerRequirement.setChecked(true);
                            binding.switchLactalisFreezerRequirement.setEnabled(false);
                        } else {
                            binding.lactalisFreezerRequirementTitle.setText(title);
                        }
                        break;
                    case "location":
                        if (visibility.equals("0")) {
                            binding.locationLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.locationTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.locationTitle.setText(title);
                        }
                        binding.enterLocation.setHint("Enter " + title);
                        break;
                    case "mobile":
                        if (visibility.equals("0")) {
                            binding.mobileNumberLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.mobileNumberTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.mobileNumberTitle.setText(title);
                        }
                        binding.enterMobileNumber.setHint("Enter " + title);
                        break;
                    case "outletName":
                        if (visibility.equals("0")) {
                            binding.outletNameLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.outletNameTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.outletNameTitle.setText(title);
                        }
                        binding.enterOutletName.setHint("Enter " + title);
                        break;
                    case "outletType":
                        if (visibility.equals("0")) {
                            binding.outletStatusLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.outletStatusTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.outletStatusTitle.setText(title);
                        }
                        binding.selectOutletStatus.setHint("Select " + title);
                        break;
                    case "outstandingAmount":
                        if (visibility.equals("0")) {
                            binding.outstandingAmountLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.outstandingAmountTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.outstandingAmountTitle.setText(title);
                        }
                        binding.enterOutstandingAmount.setHint("Enter " + title);
                        break;
                    case "pan":
                        if (visibility.equals("0")) {
                            binding.panNumberLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.panNumberTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.panNumberTitle.setText(title);
                        }
                        binding.enterPANNumber.setHint("Enter " + title);
                        break;
                    case "pincode":
                        if (visibility.equals("0")) {
                            binding.pincodeLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.pincodeTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.pincodeTitle.setText(title);
                        }
                        binding.enterPincode.setHint("Enter " + title);
                        break;
                    case "state":
                        if (visibility.equals("0")) {
                            binding.stateLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.stateTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.stateTitle.setText(title);
                        }
                        binding.selectState.setHint("Select " + title);
                        break;
                    case "subCategory":
                        if (visibility.equals("0")) {
                            binding.outletSubCategoryLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.outletSubCategoryTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.outletSubCategoryTitle.setText(title);
                        }
                        binding.selectOutletSubCategory.setHint("Select " + title);
                        break;
                    case "visiCoolAvail":
                        if (visibility.equals("0")) {
                            binding.visiCoolerAvailableLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.visiCoolerAvailableTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                            binding.switchVisiCoolerAvailable.setChecked(true);
                            binding.switchVisiCoolerAvailable.setEnabled(false);
                        } else {
                            binding.visiCoolerAvailableTitle.setText(title);
                        }
                        break;
                    case "visiCoolComp":
                        if (visibility.equals("0")) {
                            binding.visiCoolerCompanyLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.visiCoolerCompanyTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.visiCoolerCompanyTitle.setText(title);
                        }
                        binding.selectVisiCoolerCompany.setHint("Select " + title);
                        break;
                    case "whatsapp":
                        if (visibility.equals("0")) {
                            binding.whatsappNumberLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.whatsappNumberTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.whatsappNumberTitle.setText(title);
                        }
                        binding.enterWhatsappNumber.setHint("Enter " + title);
                        break;
                    case "outletAddress":
                        if (visibility.equals("0")) {
                            binding.addressLL.setVisibility(View.GONE);
                            binding.mapLL.setVisibility(View.GONE);
                            binding.btnRefLoc.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.addressTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.addressTitle.setText(title);
                        }
                        binding.enterAddress.setHint("Enter " + title);
                        break;
                    case "ownerName":
                        if (visibility.equals("0")) {
                            binding.ownerNameLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.ownerNameTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.ownerNameTitle.setText(title);
                        }
                        binding.enterOwnerName.setHint("Enter " + title);
                        break;
                    case "route":
                        if (visibility.equals("0")) {
                            binding.routeLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.routeTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.routeTitle.setText(title);
                        }
                        binding.selectRoute.setHint("Select " + title);
                        break;
                    case "secMobile":
                        if (visibility.equals("0")) {
                            binding.secMobileNumberLL.setVisibility(View.GONE);
                        }
                        if (mandatory.equals("1")) {
                            binding.secMobileNumberTitle.setText(Html.fromHtml(title + " " + "<span style=\"color:#E53935\">*</span>"));
                        } else {
                            binding.secMobileNumberTitle.setText(title);
                        }
                        binding.enterSecMobileNumber.setHint("Enter " + title);
                        break;
                    case "outletPhoto":
                        if (visibility.equals("0")) {
                            binding.photoLL.setVisibility(View.GONE);
                        }
                        isPhotoMandatory = mandatory.equals("1");
                        break;
                }
            } catch (JSONException ignored) {
            }
        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        switch (type) {
            case 2:
                binding.enterOwnerName.setText("");
                routeID = "";
                routeTITLE = "";
                binding.selectRoute.setText(routeTITLE);
                distributorID = myDataset.get(position).getId();
                distributorTITLE = myDataset.get(position).getName();
                binding.selectDistributor.setText(distributorTITLE);
                shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, distributorID);
                distGrpERP = myDataset.get(position).getCusSubGrpErp();
                divERP = myDataset.get(position).getDivERP();
                distributorERP = myDataset.get(position).getCont();
                getRouteList();
                break;
        }
    }

    private void getRouteList() {
        routeListArray = new JSONArray();
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_route_list");
        params.put("distCode", distributorID);
        Common_Class.makeApiCall(context, params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        routeListArray = jsonObject.getJSONArray("routeList");
                    } catch (Exception ignored) {
                    }
                });
            }

            @Override
            public void onFailure(String error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        MyAlertDialog.show(context, "", "Are you sure you want to go back?", true, "Yes", "No", new AlertBox() {
            @Override
            public void PositiveMethod(DialogInterface dialog, int id) {
                finish();
            }

            @Override
            public void NegativeMethod(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {

    }
}
