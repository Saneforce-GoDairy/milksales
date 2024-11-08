package com.saneforce.godairy.Activity;

import static com.saneforce.godairy.SFA_Activity.RetailerGeoTaggingActivity.isUpdated;
import static com.saneforce.godairy.SFA_Activity.RetailerGeoTaggingActivity.radius;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.AlertDialogClickListener;
import com.saneforce.godairy.Interface.LocationResponse;
import com.saneforce.godairy.R;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.ActivityGeoTagBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GeoTagActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    ActivityGeoTagBinding binding;

    Common_Class common_class;
    AssistantClass assistantClass;
    Context context = this;
    GoogleMap mGoogleMap;
    double lat = 0, lng = 0;
    String id = "", title = "", address = "", type = "";
    boolean isViewMode = false;
    LatLng selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGeoTagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        common_class = new Common_Class(this);
        assistantClass = new AssistantClass(context);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        common_class.gotoHomeScreen(context, binding.toolbar.home);

        id = getIntent().getStringExtra("outletId");
        title = getIntent().getStringExtra("outletName");
        address = getIntent().getStringExtra("outletAddress");
        type = getIntent().getStringExtra("type");

        try {
            lat = Double.parseDouble(getIntent().getStringExtra("outletLat"));
            lng = Double.parseDouble(getIntent().getStringExtra("outletLng"));
        } catch (Exception ignored) {
            lat = 0;
            lng = 0;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (lat == 0 || lng == 0) {
            binding.toolbar.title.setText("Pick Geo-Location");
        } else {
            isViewMode = true;
            binding.toolbar.title.setText("View Geo-Location");
            binding.submit.setVisibility(View.GONE);
            binding.address.setText(address);
        }

        binding.submit.setOnClickListener(view -> {
            if (selectedLocation == null) {
                assistantClass.showAlertDialogWithDismiss("Please select location...");
                return;
            }
            assistantClass.showAlertDialog("", "Are you sure you want to save location?", true, "Yes", "No", new AlertDialogClickListener() {
                @Override
                public void onPositiveButtonClick(DialogInterface dialog) {
                    dialog.dismiss();
                    lat = selectedLocation.latitude;
                    lng = selectedLocation.longitude;
                    SaveLocation();
                }

                @Override
                public void onNegativeButtonClick(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
        });

    }

    private void SaveLocation() {
        assistantClass.showProgressDialog("Please wait...", false);
        Map<String, String> params = new HashMap<>();
        if (type.equalsIgnoreCase("outlet")) {
            params.put("axn", "GeoTagOutlet");
            params.put("outletCode", id);
        } else {
            params.put("axn", "GeoTagStockist");
            params.put("stockistCode", id);
        }
        params.put("lat", String.valueOf(lat));
        params.put("lng", String.valueOf(lng));
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithFinish(jsonObject.optString("msg"));
                isUpdated = true;
            }

            @Override
            public void onFailure(String error) {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithDismiss(error);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (isViewMode) {
            centreMapOnLocation();
        } else {
            mGoogleMap.setOnMapClickListener(GeoTagActivity.this);
            assistantClass.showProgressDialog("Getting location...", false);
            assistantClass.getLocation(new LocationResponse() {
                @Override
                public void onSuccess(double lat, double lng) {
                    assistantClass.dismissProgressDialog();
                    selectedLocation = new LatLng(lat, lng);
                    onMapClick(selectedLocation);
                }

                @Override
                public void onFailure() {
                    assistantClass.dismissProgressDialog();
                }
            });
        }
    }


    @SuppressLint("PotentialBehaviorOverride")
    public void centreMapOnLocation() {
        if (isViewMode) {
            LatLng userLocation = new LatLng(lat, lng);
            mGoogleMap.clear();
            mGoogleMap.addMarker(new MarkerOptions().position(userLocation).title(title));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));
            CircleOptions circleOptions = new CircleOptions()
                    .center(userLocation)
                    .radius(radius * 1000)
                    .strokeWidth(2)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.argb(50, 0, 0, 255));
            mGoogleMap.addCircle(circleOptions);
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if (selectedLocation != null) {
            mGoogleMap.clear();
        }
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        selectedLocation = latLng;
        try {
            binding.address.setText(Common_Class.getAddressFromLatLong(context, selectedLocation.latitude, selectedLocation.longitude).getAddressLine(0));
        } catch (Exception ignored) {
        }
    }
}