package com.milksales.godairy.SFA_Activity;

import static android.Manifest.permission.CALL_PHONE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Common_Model;
import com.milksales.godairy.Common_Class.Constants;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.AdapterOnClick;
import com.milksales.godairy.Interface.LocationEvents;
import com.milksales.godairy.Interface.Master_Interface;
import com.milksales.godairy.Interface.UpdateResponseUI;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Adapter.MyTeamCategoryAdapter;
import com.milksales.godairy.SFA_Adapter.MyTeamMapAdapter;
import com.milksales.godairy.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyTeamActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, UpdateResponseUI, Master_Interface, View.OnTouchListener {


    public Common_Class common_class;

    public static Shared_Common_Pref shared_common_pref;
    SharedPreferences UserDetails, CheckInDetails;
    MapView mapView;
    GoogleMap map;

    ArrayList<Marker> mark = new ArrayList<>();
    Boolean rev = false;

    double laty = 0.0, lngy = 0.0;

    private int _yDelta, ht;
    String TAG = "MyTeamActivity", CheckInfo = "CheckInDetail", UserInfo = "MyPrefs";
    public static MyTeamActivity myTeamActivity;

    private Marker marker;
    RecyclerView rvCategory, rvTeamDetail;
    MyTeamCategoryAdapter adapter;

    public static int selectedPos;
    RelativeLayout vwRetails;
    MyTeamMapAdapter mapAdapter;
    EditText txSearchRet;

    public String mType = "";
    private int locPos;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_myteam_layout);

            myTeamActivity = this;
            shared_common_pref = new Shared_Common_Pref(this);

            CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
            UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

            init();

            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
            vwRetails.setOnTouchListener(this);


            RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, 435);

            rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            vwRetails.setLayoutParams(rel_btn);


            RelativeLayout.LayoutParams mapLayout = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            mapLayout.setMargins(0, 0, 0, 435);

            mapView.setLayoutParams(mapLayout);


            common_class = new Common_Class(this);

            txSearchRet.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    setAdapter(shared_common_pref.getvalue(Constants.MYTEAM_LOCATION));
                }
            });
            new LocationFinder(getApplication(), new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {

                    if (location == null) {

                        Toast.makeText(MyTeamActivity.this, "Location Not Detacted. Please Try Again.", Toast.LENGTH_LONG).show();
                        return;
                    } else {

                        showNearbyData(location);
                    }
                }
            });


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);


        } catch (Exception e) {
            Log.e(TAG, " onCreate: " + e.getMessage());

        }
    }

    void showNearbyData(Location location) {
        Shared_Common_Pref.Outletlat = location.getLatitude();
        Shared_Common_Pref.Outletlong = location.getLongitude();
//        latitude.setText("Lat : " + location.getLatitude());
//        longitude.setText("Lng : " + location.getLongitude());
        getCompleteAddressString(location.getLatitude(), location.getLongitude());
        map.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(MyTeamActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyTeamActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("LocationError", "Need Location Permission");
            return;
        }
        laty = location.getLatitude();
        lngy = location.getLongitude();
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(laty, lngy)));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(laty, lngy), 15));

        getTeamLoc("ALL");
    }

    public void getTeamLoc(String type) {
        if (mType.equalsIgnoreCase("")) {
            mType = type;
            JsonObject data = new JsonObject();
            data.addProperty("sfcode", Shared_Common_Pref.Sf_Code);
            data.addProperty("date", Common_Class.GetDatewothouttime());
            data.addProperty("type", type);
            data.addProperty("lat", "" + laty);
            data.addProperty("lng", "" + lngy);

            common_class.getDb_310Data(Constants.MYTEAM_LOCATION, this, data);
        } else {
            mType = type;
            setAdapter(shared_common_pref.getvalue(Constants.MYTEAM_LOCATION));
        }


    }

    void setAdapter(String apiDataResponse) {
        try {
            JSONObject jsonObject = new JSONObject(apiDataResponse);
            if (jsonObject.getBoolean("success")) {
                if (mark != null && map != null) {
                    for (int i = 0; i < mark.size(); i++) {
                        mark.get(i).remove();
                    }
                }
                JSONArray arr = jsonObject.getJSONArray("Data");

                JSONArray arr1 = new JSONArray();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject arrObj = arr.getJSONObject(i);
                    boolean addItmflg = false;
                    if (txSearchRet.getText().toString().equalsIgnoreCase("") ||
                            (";" + arrObj.getString("Sf_Name").toLowerCase()).indexOf(";" + txSearchRet.getText().toString().toLowerCase()) > -1) {
                        addItmflg = true;
                    }

                    if (addItmflg && (mType.equalsIgnoreCase(arrObj.getString("shortname")) || mType.equalsIgnoreCase("ALL"))) {
                        LatLng latLng = new LatLng(Double.parseDouble(arrObj.getString("Lat")),
                                Double.parseDouble(arrObj.getString("Lon")));
                        BitmapDescriptor markerIcon;

                        switch (arrObj.getString("shortname")) {
                            case "FRANCHISE":
                                markerIcon = getMarkerIconFromDrawable(getResources().getDrawable
                                        (R.drawable.ic_franchise));

                                marker = map.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title((arrObj.getString("Sf_Name")))
                                        .icon(markerIcon)
                                );


                                break;
                            case "OUTLET":
                                markerIcon = getMarkerIconFromDrawable(getResources().getDrawable
                                        (R.drawable.ic_baseline_storefront_24));

                                marker = map.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title((arrObj.getString("Sf_Name")))
                                        .icon(markerIcon)
                                );


                                break;
                            default:
                                marker = map.addMarker(new MarkerOptions().position(latLng)
                                        .title((arrObj.getString("Sf_Name"))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                break;
                        }


                        mark.add(marker);


                        arr1.put(arr.getJSONObject(i));

                        builder.include(latLng);
                    }
                }

                mapAdapter = new MyTeamMapAdapter(this, arr1, String.valueOf(laty), String.valueOf(lngy),mType, new AdapterOnClick() {
                    @Override
                    public void onIntentClick(JsonObject item, int Name) {

                    }

                    @Override
                    public void CallMobile(String MobileNo) {
                        Log.d("Event", "CAll Mobile");
                        int readReq = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
                        if (readReq != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(HAPApp.activeActivity, new String[]{CALL_PHONE}, REQUEST_PERMISSIONS_REQUEST_CODE);
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + MobileNo));//change the number
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callIntent);
                        }
                    }
                });
                rvTeamDetail.setAdapter(mapAdapter);


                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker mMark) {
                        showDialog(mMark.getPosition(), arr1);
                        return false;
                    }
                });

                map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));


            }
        } catch (Exception e) {

        }
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, 50, 50);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    void init() {
        mapView = (MapView) findViewById(R.id.mapview);
        rvCategory = findViewById(R.id.rvTeamCategory);
        rvTeamDetail = findViewById(R.id.rvTeamDetail);
        vwRetails = findViewById(R.id.vwRetails);
        txSearchRet = findViewById(R.id.txSearchRet);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


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
                //  cAddress.setText(strReturnedAddress.toString());
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


            getTeamLoc("ALL");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {
                switch (key) {
                    case Constants.MYTEAM_LOCATION:
                        JSONObject jsonObject = new JSONObject(apiDataResponse);
                        if (jsonObject.getBoolean("success")) {
                            String Desgs = "[\"ALL\"," + jsonObject.getJSONArray("Designation").join(",") + "]";
                            // Log.v(TAG,"loc list:")
                            JSONArray arr = new JSONArray(Desgs);//jsonObject.getJSONArray("Designation");
                            adapter = new MyTeamCategoryAdapter(arr, R.layout.myteam_category_adapter_layout, this);
                            rvCategory.setAdapter(adapter);
                            selectedPos = 0;
                            setAdapter(apiDataResponse);
                        }
                        break;
                }
            }

        } catch (Exception e) {

        }
    }

    private void showDialog(LatLng markPos, JSONArray array) {
        try {
            locPos = -1;
            LayoutInflater inflater = LayoutInflater.from(this);

            final View view = inflater.inflate(R.layout.team_loc_detail_layout, null);
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            //alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LinearLayout llDir = (LinearLayout) view.findViewById(R.id.llDirection);
            LinearLayout llCall=view.findViewById(R.id.btnCallMob);
            TextView tvSfName = (TextView) view.findViewById(R.id.tvSfName);
            TextView tvDesig = (TextView) view.findViewById(R.id.tvDesig);
            TextView tvMobile = (TextView) view.findViewById(R.id.txMobile);
            TextView txDtTm = (TextView) view.findViewById(R.id.txDtTm);
            TextView txHQ = view.findViewById(R.id.tvHQ);


            for (int i = 0; i < array.length(); i++) {
                JSONObject arrObj = array.getJSONObject(i);
                LatLng latLng = new LatLng(Double.parseDouble(arrObj.getString("Lat")),
                        Double.parseDouble(arrObj.getString("Lon")));

                if (latLng.equals(markPos)) {
                    locPos = i;
                }

            }

            JSONObject obj = array.getJSONObject(locPos);
            tvSfName.setText("" + obj.getString("Sf_Name"));
            tvDesig.setText("" + obj.getString("Designation_Name"));
            tvMobile.setText("" + obj.getString("SF_Mobile"));
            txDtTm.setText("" + obj.getString("dttm"));
            txHQ.setText("" + obj.getString("HQ_Name"));

            if(Common_Class.isNullOrEmpty(obj.getString("SF_Mobile")))
                view.findViewById(R.id.btnCallMob).setVisibility(View.GONE);

            llCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    common_class.showCalDialog(MyTeamActivity.this, "Do you want to Call this number?", tvMobile.getText().toString().replaceAll(",", ""));
                }
            });


            llDir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        alertDialog.dismiss();
                        Intent intent = new Intent(MyTeamActivity.this, MapDirectionActivity.class);
                        intent.putExtra(Constants.DEST_LAT, obj.getString("Lat"));
                        intent.putExtra(Constants.DEST_LNG, obj.getString("Lon"));
                        intent.putExtra(Constants.DEST_NAME, obj.getString("HQ_Name"));
                        intent.putExtra(Constants.NEW_OUTLET, "new");
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                }
            });

            alertDialog.setView(view);
            alertDialog.show();

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

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                _yDelta = Y;
                if (Y < 200) {
                    ht = vwRetails.getHeight();
                    rev = true;
                } else {
                    rev = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                int Hight = 435;
                if (rev == false) {
                    Hight = 435;
                    if (vwRetails.getHeight() > 500) {
                        Hight = mapView.getHeight() + 435;
                    }
                }
                if (rev == true) {
                    Hight = mapView.getHeight() + 435;
                    if (vwRetails.getHeight() < ((mapView.getHeight() + 435) - 100)) {
                        Hight = 435;
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
                    incHight = (_yDelta - Y) + 435;
                } else {
                    incHight = ht - (Y - _yDelta);
                }
                if (incHight < 0) incHight = 0;
                Log.d("Y:", String.valueOf(Y) + " _yDelta:" + String.valueOf(_yDelta) + "=" + (Y - _yDelta) + " inc:" + String.valueOf(incHight));
                if (incHight > 434) {
                    RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, incHight);
                    rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    vwRetails.setLayoutParams(rel_btn);

                }

                break;
        }
        //_root.invalidate();
        return true;
    }

}