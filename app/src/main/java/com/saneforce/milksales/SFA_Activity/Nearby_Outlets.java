package com.saneforce.milksales.SFA_Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity_Hap.AddNewRetailer;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.LocationEvents;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.Interface.UpdateResponseUI;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.RetailerNearByADP;
import com.saneforce.milksales.SFA_Model_Class.Dashboard_View_Model;
import com.saneforce.milksales.SFA_Model_Class.Retailer_Modal_List;
import com.saneforce.milksales.adapters.ExploreMapAdapter;
import com.saneforce.milksales.common.DatabaseHandler;
import com.saneforce.milksales.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Nearby_Outlets extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, OnMapReadyCallback, UpdateResponseUI, Master_Interface {
    List<Dashboard_View_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView, rclRetail;
    Type userType;
    Common_Class common_class;
    TextView Createoutlet, latitude, longitude, availableoutlets, btnNearme, btnExplore, cAddress;
    EditText txSearchRet;
    public static Shared_Common_Pref shared_common_pref;
    SharedPreferences UserDetails, CheckInDetails;
    MapView mapView;
    GoogleMap map;
    Boolean rev = false;
    ArrayList<Marker> mark = new ArrayList<>();

    StringBuilder sb, place;
    static String googlePlacesData;
    double laty = 0.0, lngy = 0.0;
    JSONArray resData;
    RelativeLayout vwRetails, tabExplore;
    private int _xDelta;
    private int _yDelta, ht;

    String TAG = "Nearby_Outlets", CheckInfo = "CheckInDetail", UserInfo = "MyPrefs";

    ImageView ivFilterKeysMenu, ivNearMe, ivExplore;

    List<Common_Model> mapKeyList = new ArrayList<>();

    String nextPageToken = "";

    public static Nearby_Outlets nearby_outlets;

    ExploreMapAdapter explore;

    String mapKey;
    DatabaseHandler db;
    LinearLayout llNearMe, llExplore;
    private Polyline mPolyline;
    private String dest_lat;
    private String dest_lng;
    private String dest_name;
    private JSONArray oldData;
    private Marker marker;
    JsonArray jOutlets;
    private JsonObject editRetailJsonObj;
    Switch swFreezerOutlet, swNoFreezerOutlet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nearby__outlets);
            db = new DatabaseHandler(this);
            nearby_outlets = this;
            shared_common_pref = new Shared_Common_Pref(this);

            CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
            UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

            init();
            setClickListener();


            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
            vwRetails.setOnTouchListener(this);
            RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, 635);
            rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            vwRetails.setLayoutParams(rel_btn);
            Log.d("Height:", String.valueOf(vwRetails.getHeight()));

            common_class = new Common_Class(this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(linearLayoutManager);
            rclRetail.setLayoutManager(new LinearLayoutManager(this));
            gson = new Gson();
            userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
            }.getType();
            txSearchRet.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    SearchRetailers();
                }
            });
            latitude.setText("Locating Please Wait...");
            new LocationFinder(getApplication(), new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {

                    if (location == null) {
                        availableoutlets.setText("Location Not Detacted.");
                        Toast.makeText(Nearby_Outlets.this, "Location Not Detacted. Please Try Again.", Toast.LENGTH_LONG).show();
                        return;
                    } else {

                        showNearbyData(location);
                    }
                }
            });


            Createoutlet.setOnClickListener(this);
            ImageView backView = findViewById(R.id.imag_back);

            backView.setOnClickListener(this);


            if (getArrayList(Constants.MAP_KEYLIST) == null || getArrayList(Constants.MAP_KEYLIST).size() == 0) {
                mapKeyList.add(new Common_Model("Shop"));
                shared_common_pref.save(Constants.MAP_KEYLIST, gson.toJson(mapKeyList));
                shared_common_pref.save(Constants.MAP_KEY, "Shop");
            } else {
                mapKeyList = getArrayList(Constants.MAP_KEYLIST);
                mapKey = shared_common_pref.getvalue(Constants.MAP_KEY);
            }


            rclRetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1)) {
                        if (common_class.isNetworkAvailable(Nearby_Outlets.this)) {
                            common_class.ProgressdialogShow(1, "");
                            getExploreDr(true);
                        } else {
                            Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);


            swFreezerOutlet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        swNoFreezerOutlet.setChecked(false);


                    }

                    SearchRetailers();

                }
            });

            swNoFreezerOutlet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        swFreezerOutlet.setChecked(false);
                    }
                    SearchRetailers();

                }
            });


            if (getIntent().getStringExtra("menu") != null) {
                findViewById(R.id.llMenuParent).setVisibility(View.GONE);
                Createoutlet.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            Log.e(TAG, " onCreate: " + e.getMessage());

        }
    }

    public void navigateEditRetailerScreen(JsonObject jItem, boolean isSameDist) {
        editRetailJsonObj = jItem;

        //  shared_common_pref.save(Constants.Distributor_phone, myDataset.get(position).getPhone());
        Shared_Common_Pref.Outlet_Info_Flag = "1";
        Shared_Common_Pref.Editoutletflag = "1";
        Shared_Common_Pref.Outler_AddFlag = "0";
        Shared_Common_Pref.FromActivity = "Outlets";
        Shared_Common_Pref.OutletCode = jItem.get("Code").getAsString();
        if (isSameDist) {
            callRetailUpdateScreen(jItem);
        } else {
            shared_common_pref.save(Constants.Distributor_Id, jItem.get("DistCode").getAsString());
            shared_common_pref.save(Constants.Distributor_name, jItem.get("Distributor").getAsString());
            shared_common_pref.save(Constants.Route_name, "");
            shared_common_pref.save(Constants.Route_Id, "");
            shared_common_pref.save(Constants.DistributorERP, jItem.get("StkERPCode").getAsString());
            shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, jItem.get("DistCode").getAsString());
            common_class.getDataFromApi(Constants.Retailer_OutletList, Nearby_Outlets.nearby_outlets, false);
        }

    }

    void callRetailUpdateScreen(JsonObject jItem) {
        Intent intent = new Intent(this, AddNewRetailer.class);
        intent.putExtra("OutletCode", jItem.get("Code").getAsString());
        intent.putExtra("OutletName", jItem.get("Name").getAsString());
        intent.putExtra("OutletAddress", jItem.get("Add1").getAsString());
        intent.putExtra("OutletMobile", jItem.get("Mobile").getAsString());
        intent.putExtra("OutletRoute", "");
        startActivity(intent);
    }

    void showNearbyData(Location location) {
        Shared_Common_Pref.Outletlat = location.getLatitude();
        Shared_Common_Pref.Outletlong = location.getLongitude();
        latitude.setText("Lat : " + location.getLatitude());
        longitude.setText("Lng : " + location.getLongitude());
        getCompleteAddressString(location.getLatitude(), location.getLongitude());
        JSONObject jsonObject = new JSONObject();
        try {
            common_class.ProgressdialogShow(1, "");

            jsonObject.put("SF", UserDetails.getString("Sfcode", ""));
            jsonObject.put("Div", UserDetails.getString("Divcode", ""));
            jsonObject.put("Lat", location.getLatitude());
            jsonObject.put("Lng", location.getLongitude());
//            jsonObject.put("Lat", "11.5237379");
//            jsonObject.put("Lng", "79.323381");
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            service.getDataArrayList("get/fencedOutlet", jsonObject.toString()).enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    try {
                        common_class.ProgressdialogShow(0, "");

                        jOutlets = response.body();

                        if (shared_common_pref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
                            JsonArray srhOutlets = new JsonArray();

                            for (int sr = 0; sr < jOutlets.size(); sr++) {
                                JsonObject jItm = jOutlets.get(sr).getAsJsonObject();
                                if ((shared_common_pref.getvalue(Constants.LOGIN_TYPE).equals(Constants.CHECKIN_TYPE)
                                        || (jItm.get("DistCode").getAsString().equalsIgnoreCase(shared_common_pref.getvalue(Constants.Distributor_Id))))) {
                                    srhOutlets.add(jItm);
                                }
                            }

                            jOutlets = srhOutlets;
                        }
                        availableoutlets.setText("Available Outlets :" + "\t" + jOutlets.size());
                        setOutletsAdapter(jOutlets);
                    } catch (Exception e) {
                        common_class.ProgressdialogShow(0, "");

                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    common_class.ProgressdialogShow(0, "");

                    Log.d("Fence", t.getMessage());
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

        map.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(Nearby_Outlets.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Nearby_Outlets.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("LocationError", "Need Location Permission");
            return;
        }
        laty = location.getLatitude();
        lngy = location.getLongitude();
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(laty, lngy)));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(laty, lngy), 15));
        getExploreDr(true);
    }


    public void removeMarkedPlaces() {

        try {
            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                JSONObject HeadItem = new JSONObject();

                HeadItem.put("lat", laty);
                HeadItem.put("lng", lngy);
                HeadItem.put("date", Common_Class.GetDate());
                HeadItem.put("divCode", Shared_Common_Pref.Div_Code);

                Call<ResponseBody> call = service.getMarkedData(HeadItem.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }

                                JSONObject jsonObject = new JSONObject(is.toString());

                                if (jsonObject.getBoolean("success")) {


                                    String placeIds = jsonObject.getString("Data");


                                    Log.v("markedPlace:", "success: " + resData.length());
                                    if (explore == null) {


                                        resData = removeDuplicateItem(resData, placeIds);


                                        Log.v("markedPlace:", "success:filter " + resData.length());

                                        explore = new ExploreMapAdapter(Nearby_Outlets.this, resData, String.valueOf(Shared_Common_Pref.Outletlat), String.valueOf(Shared_Common_Pref.Outletlong));
                                        rclRetail.setAdapter(explore);
                                        explore.notifyDataSetChanged();

                                    } else {

                                        if (oldData != null) {
                                            for (int i = 0; i < resData.length(); i++) {
                                                oldData.put(resData.get(i));

                                            }
                                        }


                                        resData = oldData;

                                        resData = removeDuplicateItem(resData, placeIds);

                                        Log.v("markedPlace:2", "success: " + resData.length());

                                        Log.v("markedPlace:2", "filter: " + resData.length());

                                        explore.notifyData(resData);

                                    }

                                    common_class.ProgressdialogShow(0, "");


                                } else {
                                    drDetail();
                                }
                            }


                        } catch (Exception e) {

                            Log.v("fail>>", e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>", t.toString());


                    }
                });
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }

    }

    private void SearchRetailers() {
        JsonArray srhOutlets = new JsonArray();

        String freezerType = swFreezerOutlet.isChecked() ? "yes" : swNoFreezerOutlet.isChecked() ? "No" : "";

        for (int sr = 0; sr < jOutlets.size(); sr++) {
            JsonObject jItm = jOutlets.get(sr).getAsJsonObject();
            String itmname = jItm.get("Name").getAsString().toUpperCase();
            String sSchText = txSearchRet.getText().toString().toUpperCase();
            if (((";" + itmname).indexOf(";" + sSchText) > -1) && (shared_common_pref.getvalue(Constants.LOGIN_TYPE).equals(Constants.CHECKIN_TYPE)
                    || (jItm.get("DistCode").getAsString().equalsIgnoreCase(shared_common_pref.getvalue(Constants.Distributor_Id)))) &&
                    (Common_Class.isNullOrEmpty(freezerType) ||
                            jItm.get("freezer_required").getAsString().equalsIgnoreCase(freezerType))) {
                srhOutlets.add(jItm);
            }
        }
        availableoutlets.setText("Available Outlets :" + "\t" + srhOutlets.size());
        setOutletsAdapter(srhOutlets);
    }


    void setOutletsAdapter(JsonArray jOutlets) {
        recyclerView.setAdapter(new RetailerNearByADP(jOutlets, R.layout.route_dashboard_recyclerview,
                Nearby_Outlets.this, new AdapterOnClick() {
            @Override
            public void onIntentClick(int position) {
                try {
                    editRetailJsonObj = null;
                    JsonObject jItm = jOutlets.get(position).getAsJsonObject();
                    Shared_Common_Pref.Outler_AddFlag = "0";
                    Shared_Common_Pref.OutletName = jItm.get("Name").getAsString().toUpperCase();
                    Shared_Common_Pref.OutletCode = jItm.get("Code").getAsString();
                    shared_common_pref.save(Constants.Retailor_Address, jItm.get("Add2").getAsString());
                    shared_common_pref.save(Constants.Retailor_ERP_Code, jItm.get("ERP").getAsString());
                    shared_common_pref.save(Constants.Retailor_Name_ERP_Code, jItm.get("Name").getAsString().toUpperCase() + "~" + jItm.get("ERP").getAsString());

                    Shared_Common_Pref.Freezer_Required = jItm.get("freezer_required").getAsString();
                    if (!shared_common_pref.getvalue(Constants.Distributor_Id).equalsIgnoreCase(jItm.get("DistCode").getAsString())) {
                        shared_common_pref.save(Constants.Distributor_Id, jItm.get("DistCode").getAsString());
                        shared_common_pref.save(Constants.Distributor_name, jItm.get("Distributor").getAsString());
                        shared_common_pref.save(Constants.Route_name, "");
                        shared_common_pref.save(Constants.Route_Id, "");
                        shared_common_pref.save(Constants.DistributorERP, jItm.get("StkERPCode").getAsString());
                        shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, jItm.get("DistCode").getAsString());
                        common_class.getDataFromApi(Constants.Retailer_OutletList, Nearby_Outlets.nearby_outlets, false);
                    } else {

                        common_class.CommonIntentwithoutFinish(Invoice_History.class);
                    }

                } catch (Exception e) {
                    Log.v(TAG + "nearbyOut:", e.getMessage());
                }
            }
        }));
    }

    private void setClickListener() {
        ivFilterKeysMenu.setOnClickListener(this);
        btnNearme.setOnClickListener(this);

        btnExplore.setOnClickListener(this);

        llNearMe.setOnClickListener(this);
        llExplore.setOnClickListener(this);
    }

    void init() {
        recyclerView = findViewById(R.id.outletrecyclerview);
        rclRetail = findViewById(R.id.rclRetail);

        Createoutlet = findViewById(R.id.Createoutlet);
        availableoutlets = findViewById(R.id.availableoutlets);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        cAddress = findViewById(R.id.cAddress);
        txSearchRet = findViewById(R.id.txSearchRet);

        vwRetails = findViewById(R.id.vwRetails);
        tabExplore = findViewById(R.id.tabExplore);
        btnNearme = findViewById(R.id.btnNearme);
        btnExplore = findViewById(R.id.btnExplore);
        mapView = (MapView) findViewById(R.id.mapview);
        ivFilterKeysMenu = (findViewById(R.id.ivFilterKeysMenu));

        llNearMe = (LinearLayout) findViewById(R.id.llNearMe);
        llExplore = (LinearLayout) findViewById(R.id.llExplore);
        ivExplore = (ImageView) findViewById(R.id.ivExplore);
        ivNearMe = (ImageView) findViewById(R.id.ivNearMe);
        swFreezerOutlet = findViewById(R.id.swFreezerOutlet);
        swNoFreezerOutlet = findViewById(R.id.swNofreezerOutlet);


    }

    public ArrayList<Common_Model> getArrayList(String key) {
        Gson gson = new Gson();
        String json = shared_common_pref.getvalue(key);
        Type type = new TypeToken<ArrayList<Common_Model>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Createoutlet:
                Shared_Common_Pref.OutletAvail = "";
                Shared_Common_Pref.OutletUniv = "";
                Shared_Common_Pref.Outler_AddFlag = "1";
                Shared_Common_Pref.Editoutletflag = "0";
                Shared_Common_Pref.OutletAvail = "";
                Shared_Common_Pref.OutletUniv = "";
                Shared_Common_Pref.Outlet_Info_Flag = "0";
                common_class.CommonIntentwithFinish(AddNewRetailer.class);
                //startActivity (Nearby_Outlets.this, AddNewRetailer.class);
                break;
            case R.id.llNearMe:
                llExplore.setBackgroundColor(Color.TRANSPARENT);
                btnExplore.setTextColor(Color.BLACK);
                ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);

                llNearMe.setBackgroundResource(R.drawable.blue_bg);
                btnNearme.setTextColor(Color.WHITE);
                ivNearMe.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                tabExplore.setVisibility(View.GONE);
                ivFilterKeysMenu.setVisibility(View.GONE);
                break;
            case R.id.llExplore:
                llNearMe.setBackgroundColor(Color.TRANSPARENT);
                btnNearme.setTextColor(Color.BLACK);
                ivNearMe.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);

                llExplore.setBackgroundResource(R.drawable.blue_bg);
                btnExplore.setTextColor(Color.WHITE);
                ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                tabExplore.setVisibility(View.VISIBLE);
                ivFilterKeysMenu.setVisibility(View.VISIBLE);


                break;


            case R.id.btnNearme:
                llExplore.setBackgroundColor(Color.TRANSPARENT);
                btnExplore.setTextColor(Color.BLACK);
                ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);

                llNearMe.setBackgroundResource(R.drawable.blue_bg);
                btnNearme.setTextColor(Color.WHITE);
                ivNearMe.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                tabExplore.setVisibility(View.GONE);
                ivFilterKeysMenu.setVisibility(View.GONE);
                break;
            case R.id.btnExplore:
                llNearMe.setBackgroundColor(Color.TRANSPARENT);
                btnNearme.setTextColor(Color.BLACK);
                ivNearMe.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);

                llExplore.setBackgroundResource(R.drawable.blue_bg);
                btnExplore.setTextColor(Color.WHITE);
                ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                tabExplore.setVisibility(View.VISIBLE);
                ivFilterKeysMenu.setVisibility(View.VISIBLE);


                break;

            case R.id.ivFilterKeysMenu:
                common_class.showCommonDialog(mapKeyList, 1000, this);
                break;


            case R.id.imag_back:
                finish();
                break;
        }
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
                cAddress.setText(strReturnedAddress.toString());
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            map = googleMap;
            map.getUiSettings().setMyLocationButtonEnabled(false);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("LocationError", "Need Location Permission");
                return;
            }
            laty = Shared_Common_Pref.Outletlat;
            lngy = Shared_Common_Pref.Outletlong;
            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(laty, lngy)));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(laty, lngy), 15));
            //  getExploreDr(true);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void getExploreDr(boolean boolNextPage) {
        sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + laty + "," + lngy);
        sb.append("&radius=500");
        //  sb.append("&types=cafe|store|shop");
        //sb.append("&keyword=tea shop|juice");

        mapKeyList = getArrayList(Constants.MAP_KEYLIST);

//        String keyVal = "";
//        for (int i = 0; i < mapKeyList.size(); i++) {
//            keyVal = keyVal + mapKeyList.get(i).getName() + "|";
//        }

        // keyVal = "&keyword=" + keyVal;

        sb.append("&keyword=" + shared_common_pref.getvalue(Constants.MAP_KEY));

        sb.append("&key=" + "AIzaSyAER5hPywUW-5DRlyKJZEfsqgZlaqytxoU");
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&
        // keyword=milk|juice&key=AIzaSyAER5hPywUW-5DRlyKJZEfsqgZlaqytxoU


        // https://maps.googleapis.com/maps/api/place/nearbysearch/xml?location=42.9825,-81.254&radius=50000&name=Medical%22Clinic&sensor=false&
        // key=[KEY GOES HERE]&pagetoken=[NEXT PAGE TOKEN GOES HERE]

        //  https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&keyword=milk|juice&key=AIzaSyAER5hPywUW-5DRlyKJZEfsqgZlaqytxoU


        //next page token:Aap_uECXTWAZEAaw417hZSGDM6er_hQeOHMqMn6M3tW_yOBnr1PprfHabmN7Bq-E_z7oyt4B3-vEn9wIvxVV19WJuDMHyyTNhU8SvRGcENx2czJ_GOdWhbrTgJvqDPwZ7RyPxXI8XXc0Kcas6M8C5CjVkYbMYEP7t3uO7AFNBC1nxLIKirdy1OCZe5LNX7IZca-6zG7xcJ1mCXaXKM6rorkao0BWjs9Foibzs38M6fEwPxsEBk0n8747BwVKzzsOq1QnmH3iMncK0uPcT-PbOvNt_KL9sxLTwu6-MXVi9A96HlnwnztvoEZd-DUMien7nxtlp7eveYmNfHXI7nffHFtKDb4QyLOfrsX-WxP3DekLszOEC0S37gMBI317dyTVxLmaEsW9hU-jI7uOxi1M2z7SA9LMTYenI3otwebmJzT27RIYMfgIY1RbssqOcl7RJKrmcrRia9vyucN6b-mFAlvx9dxbSYXyFuHZsouyVujWZyARnYFpKS9dLFxxNr1tPTJV6RvHO_0GAr-WdGDLo1gqGxiCb8gELZ947iik6sbu_Jb80US2Z0DdeCnmDL7NqaX4g0xwB2g7yV4tLVFmgCR9ORQUQpeCZC8r18RepZIJ3ZYqqAq6OtQkJvRERZrOP0TkW2X3nQl9ubc5squAlhCEv1Ou6B16hc85Op7KBcW4QUmHqrCl1xWSz1UJPw-ASim8VYBx09QSBPi30wSCdeAOJzXsN5MKQO1Wg4G4OqgB95Y2gOIy8mNiMsZeDxie3VZ34kiIsDMI0cpauTLJWLzky14FBVKvjq9aM7pB-TcihT1VBgda34RECiDj9p98mDUZ-7_bBfZBc-OEqIg01Vd0OWZZN71WuizykO3u08upH_TI4nHjt6SC6yJK7ZgH6PW33Nd-04YcumLebzyc

        if (nextPageToken.length() > 0 && boolNextPage)
            sb.append("&pagetoken=" + nextPageToken);

        Log.v(TAG + " Doctor_detail_print", sb.toString());

        if (!boolNextPage) {
            oldData = null;
            resData = null;
            explore = null;

            if (marker != null && map != null) {
                for (int i = 0; i < mark.size(); i++) {
                    mark.get(i).remove();
                    //  marker.remove();
                }
            }
        }


        if (common_class.isNetworkAvailable(this))
            new findDrDetail().execute();

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _yDelta = Y;
                if (Y < 500) {
                    ht = vwRetails.getHeight();
                    rev = true;
                } else {
                    rev = false;
                }
                Log.d("Y:", String.valueOf(Y) + " _yDelta:" + String.valueOf(_yDelta));
                break;
            case MotionEvent.ACTION_UP:
                Log.d("Height:", String.valueOf(vwRetails.getHeight()) + "=" + String.valueOf(mapView.getHeight()));
                int Hight = 635;
                if (rev == false) {
                    Hight = 635;
                    if (vwRetails.getHeight() > 700) {
                        Hight = mapView.getHeight();
                    }
                }
                if (rev == true) {
                    Hight = mapView.getHeight();
                    if (vwRetails.getHeight() < (mapView.getHeight() - 100)) {
                        Hight = 635;
                    }
                }
                RelativeLayout.LayoutParams vwlist = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, Hight);
                vwlist.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                vwRetails.setLayoutParams(vwlist);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                int incHight = 0;
                if (rev == false) {
                    incHight = (_yDelta - Y) + 635;
                } else {
                    incHight = ht - (Y - _yDelta);
                }
                if (incHight < 0) incHight = 0;
                Log.d("Y:", String.valueOf(Y) + " _yDelta:" + String.valueOf(_yDelta) + "=" + (Y - _yDelta) + " inc:" + String.valueOf(incHight));
                if (incHight > 634) {
                    RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, incHight);
                    rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    vwRetails.setLayoutParams(rel_btn);
                    Log.d("=>Height:", String.valueOf(vwRetails.getHeight()) + "=" + String.valueOf(mapView.getHeight()));

                }
               /* RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vwRetails.getLayoutParams();
                layoutParams.topMargin = Y - _yDelta;*/
                // vwRetails.setLayoutParams(layoutParams);
                break;
        }
        //_root.invalidate();
        return true;
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public void getPlaceIdValues(int position) {
        try {

            JSONObject jsonObject = resData.getJSONObject(position).getJSONObject("geometry");

            JSONObject jsonLatLongObj = jsonObject.getJSONObject("location");

            dest_lat = jsonLatLongObj.getString("lat");
            dest_lng = jsonLatLongObj.getString("lng");
            dest_name = resData.getJSONObject(position).getString("name");


            String dest = jsonLatLongObj.getString("lat") + "," + jsonLatLongObj.getString("lng");

            drawRoute(dest);
            sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");

            try {
                sb.append("place_id=" + resData.getJSONObject(position).getString("place_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //   sb.append("&fields=name,rating,formatted_phone_number,vicinity,formatted_address");
            // sb.append("&sensor=false&mode=walking&alternatives=true");

            sb.append("&key=AIzaSyAER5hPywUW-5DRlyKJZEfsqgZlaqytxoU");

            shared_common_pref.save(Constants.PLACE_ID_URL, sb.toString());

        } catch (Exception e) {
            shared_common_pref.save(Constants.PLACE_ID_URL, "");

        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            switch (key) {
                case Constants.Retailer_OutletList:
                    if (editRetailJsonObj != null)
                        callRetailUpdateScreen(editRetailJsonObj);
                    else
                        common_class.CommonIntentwithoutFinish(Invoice_History.class);

                    break;
            }

        } catch (Exception e) {

        }

    }

    class findDrDetail extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            DownloadUrl downloadUrl = new DownloadUrl();
            try {
                googlePlacesData = downloadUrl.readUrl(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG + " doInBackground: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.v("get_dr_detttt", "" + googlePlacesData);
            getDrDetail(googlePlacesData);
        }
    }


    public void getDrDetail(String placesdata) {
        try {
            if (resData != null)
                oldData = resData;


            resData = new JSONArray();


            JSONObject jsonObject = new JSONObject(placesdata);
            resData = jsonObject.getJSONArray("results");
            nextPageToken = jsonObject.optString("next_page_token");
            Log.e(TAG, "nextPageToken:" + nextPageToken);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();


            for (int i = 0; i < resData.length(); i++) {
                JSONObject json = resData.getJSONObject(i);
                String lat = json.getJSONObject("geometry").getJSONObject("location").getString("lat");
                String lng = json.getJSONObject("geometry").getJSONObject("location").getString("lng");
                String name = json.getString("name");
                String place_id = json.getString("place_id");
                String vicinity = json.getString("vicinity");
                String photoo = "", reference = "", height = "", width = "";

                try {
                    JSONArray jsonA = json.getJSONArray("photos");
                    JSONObject jo = jsonA.getJSONObject(0);
                    JSONArray ja = jo.getJSONArray("html_attributions");
                    //JSONObject jo1=ja.getJSONObject(0);
                    photoo = ja.getString(0);
                    reference = jo.getString("photo_reference");
                    height = jo.getString("height");
                    width = jo.getString("width");
                    Log.v("direction_latt", name + "phototss" + photoo);

                } catch (JSONException e) {
                }


                Log.v("direction_latt", name + "phototss");
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                marker = map.addMarker(new MarkerOptions().position(latLng)
                        .title((name)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mark.add(marker);

                builder.include(latLng);

            }
          //  map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));

            //  drDetail();
            removeMarkedPlaces();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG + ":getDrDetail: ", e.getMessage());
        }
    }


    JSONArray removeDuplicateItem(JSONArray yourJSONArray, String placeIds) {
        try {
            Set<String> stationCodes = new HashSet<String>();
            JSONArray tempArray = new JSONArray();


            for (int i = 0; i < yourJSONArray.length(); i++) {
                //  String stationCode = yourJSONArray.getJSONObject(i).getString("name");
                String placeId = yourJSONArray.getJSONObject(i).getString("place_id");


                if (stationCodes.contains(placeId) || placeIds.indexOf(placeId) > 0) {
                    continue;
                } else {

                    stationCodes.add(placeId);
                    tempArray.put(yourJSONArray.getJSONObject(i));

                    Log.v("placeId_current:" + i, placeId + " RES:" + placeIds + " rslt: " + placeIds.indexOf(placeId));

                }

            }

            Log.e(TAG, " total size:" + yourJSONArray.length() + " FilterSize: " + tempArray.length());

            yourJSONArray = tempArray; //assign temp to original
            return yourJSONArray;

        } catch (Exception e) {

        }
        return null;
    }


    public class DownloadUrl {

        public String readUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer("");

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                Log.d("downloadUrl", data.toString());
                br.close();

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

    }


    public void drDetail() {
        try {
            if (explore == null) {

                explore = new ExploreMapAdapter(Nearby_Outlets.this, resData, String.valueOf(Shared_Common_Pref.Outletlat), String.valueOf(Shared_Common_Pref.Outletlong));
                rclRetail.setAdapter(explore);
                explore.notifyDataSetChanged();

            } else {

                if (oldData != null) {
                    for (int i = 0; i < resData.length(); i++) {
                        oldData.put(resData.get(i));

                    }
                }


                resData = oldData;

                resData = removeDuplicateItem(resData, "");

                explore.notifyData(resData);

            }

            common_class.ProgressdialogShow(0, "");
        } catch (Exception e) {

        }

    }


    @Override
    public void onResume() {
        mapView.onResume();

        super.onResume();

    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    //draw route
    private void drawRoute(String mDestination) {
        Intent intent = new Intent(getApplicationContext(), MapDirectionActivity.class);
        intent.putExtra(Constants.DEST_LAT, dest_lat);
        intent.putExtra(Constants.DEST_LNG, dest_lng);
        intent.putExtra(Constants.DEST_NAME, dest_name);
        intent.putExtra(Constants.NEW_OUTLET, "new");
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            new LocationFinder(this, new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {
                    try {
                        showNearbyData(location);
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {

        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

}