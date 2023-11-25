package com.saneforce.godairy.Activity_Hap;

import static com.saneforce.godairy.Common_Class.Constants.CUSTOMER_DATA;
import static com.saneforce.godairy.Common_Class.Constants.Freezer_Status;
import static com.saneforce.godairy.Common_Class.Constants.Freezer_capacity;
import static com.saneforce.godairy.Common_Class.Constants.OUTLET_CATEGORY;
import static com.saneforce.godairy.Common_Class.Constants.Rout_List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
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
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.LocationEvents;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Interface.OnImagePickListener;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.common.LocationFinder;
import com.saneforce.godairy.databinding.ActivityAddNewRetailerBinding;
import com.saneforce.godairy.universal.UniversalDropDownAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class AddNewRetailer extends AppCompatActivity implements OnMapReadyCallback, Master_Interface, UpdateResponseUI {
    ActivityAddNewRetailerBinding binding;

    String categoryID = "", categoryTITLE = "", subCategoryID = "", subCategoryTITLE = "", outletStatusID = "", outletStatusTITLE = "", visiCoolerAvailableID = "", visiCoolerAvailableTITLE = "", iceCreamFreezerID = "", iceCreamFreezerTITLE = "", lactalisFreezerRequirementID = "", lactalisFreezerRequirementTITLE = "", deliveryTypeID = "", deliveryTypeTITLE = "", stateID = "", stateTITLE = "", shopImageName = "", shopImageFullPath = "", distGrpERP = "", distributorERP = "",  divERP = "", routeId = "", routeName = "", customer_code = "";
    double lat = 0, lng = 0;
    JSONArray fieldDetailsArray, outletTypeArray, categoryListArray, subCategoryListArray, filteredSubCategoryListArray, yesNoArray, deliveryTypeArray, stateListArray;

    UniversalDropDownAdapter adapter;

    List<Common_Model> FRoute_Master = new ArrayList<>();
    Common_Model Model_Pojo;
    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;
    Context context = this;
    GoogleMap mGoogleMap;

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
        yesNoArray = new JSONArray();
        deliveryTypeArray = new JSONArray();
        stateListArray = new JSONArray();

        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);
        distGrpERP = shared_common_pref.getvalue(Constants.CusSubGrpErp);
        distributorERP = shared_common_pref.getvalue(Constants.DistributorERP);
        divERP = shared_common_pref.getvalue(Constants.DivERP);

        binding.btnRefLoc.setOnClickListener(v -> {
            binding.btnRefLoc.startAnimation();
            setLocation();
        });
        binding.ivShopPhoto.setOnClickListener(v -> {
            AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                @Override
                public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                    shopImageName = FileName;
                    shopImageFullPath = fullPath;
                    binding.ivShopPhoto.setImageBitmap(image);
                    com.saneforce.godairy.Common_Class.Common_Class.uploadToS3Bucket(context, fullPath, FileName, "outlet_info");
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
        binding.rlDistributor.setOnClickListener(v -> {
            common_class.showCommonDialog(common_class.getDistList(), 2, this);
        });
        binding.rlRoute.setOnClickListener(v -> {
            if (FRoute_Master != null && FRoute_Master.size() > 1) {
                common_class.showCommonDialog(FRoute_Master, 3, this);
            }
        });
        binding.btnDistEnter.setOnClickListener(v -> {
            if (Shared_Common_Pref.Outler_AddFlag != null && !Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                AlertDialogBox.showDialog(com.saneforce.godairy.Activity_Hap.AddNewRetailer.this, HAPApp.Title, "Are You Sure Want to Update the Franchise Code?", "OK", "Cancel", false, new AlertBox() {
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
        });
        binding.cbFranchise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    binding.llFranchiseCode.setVisibility(View.VISIBLE);
                else
                    binding.llFranchiseCode.setVisibility(View.GONE);
            }
        });
        binding.selectOutletCategory.setOnClickListener(v -> ShowDropdown("Select Outlet Category", categoryListArray));
        binding.selectOutletSubCategory.setOnClickListener(v -> ShowDropdown("Select Outlet Sub Category", filteredSubCategoryListArray));
        binding.txOutletType.setOnClickListener(v -> ShowDropdown("Select Outlet Status", outletTypeArray));
        binding.selectVisiCoolerAvailable.setOnClickListener(v -> ShowDropdown("Visi Cooler Available", yesNoArray));
        binding.selectIceCreamFreezer.setOnClickListener(v -> ShowDropdown("Ice Cream Freezer Available", yesNoArray));
        binding.selectLactalisFreezerRequirement.setOnClickListener(v -> ShowDropdown("Select Lactalis Freezer Requirement", yesNoArray));
        binding.rlDelvryType.setOnClickListener(v -> ShowDropdown("Select Delivery Type", deliveryTypeArray)); // txDelvryType
        binding.rlState.setOnClickListener(v -> ShowDropdown("Select State", stateListArray)); // tvState

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.route_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        setLocation();
        getDropdowns();

        if (Shared_Common_Pref.Outler_AddFlag.equals("1")) {
            common_class.getDb_310Data(Rout_List, this);
        }
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
            binding.edtNewAddress.setText(strReturnedAddress.toString());
            binding.edtNewCity.setText(address.getLocality());
            binding.edtPinCode.setText(address.getPostalCode());

            centreMapOnLocation("Your Location");
        } catch (Exception e) {
            new Handler().postDelayed(this::setAddress, 5000);
        }
    }

    private void ShowDropdown(String dropdownTitle, JSONArray array) {
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
            switch (dropdownTitle) {
                case "Select Outlet Category":
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
                case "Select Outlet Sub Category":
                    try {
                        subCategoryID = arrayList.getJSONObject(position).getString("id");
                        subCategoryTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.selectOutletSubCategory.setText(subCategoryTITLE);
                    } catch (JSONException ignored) {
                    }
                    break;
                case "Select Outlet Status":
                    try {
                        outletStatusID = arrayList.getJSONObject(position).getString("id");
                        outletStatusTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.txOutletType.setText(outletStatusTITLE);
                    } catch (JSONException ignored) {
                    }
                    break;
                case "Visi Cooler Available":
                    try {
                        visiCoolerAvailableID = arrayList.getJSONObject(position).getString("id");
                        visiCoolerAvailableTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.selectVisiCoolerAvailable.setText(visiCoolerAvailableTITLE);
                        if (visiCoolerAvailableID.equals("1")) {
                            binding.visiCoolerCompanyLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.visiCoolerCompanyLL.setVisibility(View.GONE);
                        }
                    } catch (JSONException ignored) {
                    }
                    break;
                case "Ice Cream Freezer Available":
                    try {
                        iceCreamFreezerID = arrayList.getJSONObject(position).getString("id");
                        iceCreamFreezerTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.selectIceCreamFreezer.setText(iceCreamFreezerTITLE);
                        if (iceCreamFreezerID.equals("1")) {
                            binding.iceCreamFreezerCompanyLL.setVisibility(View.VISIBLE);
                        } else {
                            binding.iceCreamFreezerCompanyLL.setVisibility(View.GONE);
                        }
                    } catch (JSONException ignored) {
                    }
                    break;
                case "Select Lactalis Freezer Requirement":
                    try {
                        lactalisFreezerRequirementID = arrayList.getJSONObject(position).getString("id");
                        lactalisFreezerRequirementTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.selectLactalisFreezerRequirement.setText(lactalisFreezerRequirementTITLE);
                    } catch (JSONException ignored) {
                    }
                    break;
                case "Select Delivery Type":
                    try {
                        deliveryTypeID = arrayList.getJSONObject(position).getString("id");
                        deliveryTypeTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.txDelvryType.setText(deliveryTypeTITLE);
                    } catch (JSONException ignored) {
                    }
                    break;
                case "Select State":
                    try {
                        stateID = arrayList.getJSONObject(position).getString("id");
                        stateTITLE = arrayList.getJSONObject(position).getString("title");
                        binding.tvState.setText(stateTITLE);
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
        } catch (Resources.NotFoundException ignored) { }
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

                        JSONObject object;
                        object = new JSONObject();
                        object.put("id", "1");
                        object.put("title", "Yes");
                        yesNoArray.put(object);
                        object = new JSONObject();
                        object.put("id", "2");
                        object.put("title", "No");
                        yesNoArray.put(object);

                    } catch (Exception ignored) { }
                });
            }

            @Override
            public void onFailure(String error) { }
        });
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        switch (type) {
            case 2:
                binding.retailerType.setText("");
                routeId = "";
                routeName = "";
                distGrpERP = myDataset.get(position).getCusSubGrpErp();
                binding.distributorText.setText(myDataset.get(position).getName());
                findViewById(R.id.rl_route).setVisibility(View.VISIBLE);
                shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, myDataset.get(position).getId());
                divERP = myDataset.get(position).getDivERP();
                distributorERP = myDataset.get(position).getCont();
                common_class.getDb_310Data(Constants.Rout_List, this);
                break;
            case 3:
                routeId = myDataset.get(position).getId();
                routeName = myDataset.get(position).getName();
                binding.retailerType.setText(myDataset.get(position).getName());
                break;
        }
    }

    private void checkValidity() {
        if (Common_Class.isNullOrEmpty(binding.edtDistCode.getText().toString()))
            common_class.showMsg(this, "Enter Customer Code");
        else {
            JsonObject data = new JsonObject();
            data.addProperty("customer_code", binding.edtDistCode.getText().toString().trim());
            data.addProperty("ERP_Code", distributorERP);
            common_class.getDb_310Data(Constants.CUSTOMER_DATA, this, data);
        }
    }

    public void loadroute(String id) {
        if (Common_Class.isNullOrEmpty(String.valueOf(id))) {
            Toast.makeText(this, "Select Franchise", Toast.LENGTH_SHORT).show();
        }
        if (FRoute_Master.size() == 1) {
            binding.ivRouteSpinner.setVisibility(View.INVISIBLE);
            routeId = FRoute_Master.get(0).getId();
            routeName = FRoute_Master.get(0).getName();
            binding.retailerType.setText(routeName);
        } else {
            binding.ivRouteSpinner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {
                switch (key) {
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
                        loadroute(shared_common_pref.getvalue(Constants.TEMP_DISTRIBUTOR_ID));
                        break;
                    case CUSTOMER_DATA:
                        JSONObject cusObj = new JSONObject(apiDataResponse);
                        if (cusObj.getBoolean("success")) {
                            JSONArray arr = cusObj.getJSONArray("Data");
                            JSONObject obj = arr.getJSONObject(0);
                            binding.edtRetailerFssai.setText("" + obj.getString("Fssai_No"));
                            binding.edtNewPhone.setText("" + obj.getString("Mobile"));
                            binding.edtNewName.setText("" + obj.getString("Name"));
                            binding.edtNewAddress.setText(obj.getString("Address"));
                            binding.edtGst.setText("" + obj.getString("gstn"));
                            binding.btnDistEnter.setText("Valid Code");
                            customer_code = binding.edtDistCode.getText().toString();
                        } else {
                            binding.btnDistEnter.setText("Invalid Code");
                            common_class.showMsg(this, cusObj.getString("Msg"));
                        }
                        break;
                }
            }
        } catch (Exception e) { }
    }
}
