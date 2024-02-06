package com.saneforce.godairy.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.AlertDialogClickListener;
import com.saneforce.godairy.Interface.LocationResponse;
import com.saneforce.godairy.R;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.ActivityTravelPunchLocationBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ai.nextbillion.maps.annotations.MarkerOptions;
import ai.nextbillion.maps.camera.CameraPosition;
import ai.nextbillion.maps.camera.CameraUpdateFactory;
import ai.nextbillion.maps.core.NextbillionMap;
import ai.nextbillion.maps.core.OnMapReadyCallback;
import ai.nextbillion.maps.geometry.LatLng;

public class TravelPunchLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    ActivityTravelPunchLocationBinding binding;

    Common_Class common_class;
    AssistantClass assistantClass;
    Context context = this;
    NextbillionMap map;

    double lat = 0, lng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTravelPunchLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.map.onCreate(savedInstanceState);
        binding.map.getMapAsync(this);

        common_class = new Common_Class(this);
        assistantClass = new AssistantClass(context);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Travel Punch Location");
        binding.toolbar.home.setImageResource(R.drawable.ic_history);
        binding.toolbar.home.setOnClickListener(v -> startActivity(new Intent(context, TravelPunchHistoryActivity.class)));

        binding.refreshLocation.setOnClickListener(v -> GetLocation());
        binding.punchLocation.setOnClickListener(v -> PunchLocation());

        GetLocation();
    }

    private void PunchLocation() {
        String remarks = binding.enterRemarks.getText().toString().trim();
        String address = binding.address.getText().toString().trim();

        if (lat == 0 || lng == 0 || address.isEmpty()) {
            Toast.makeText(context, "Please capture location...", Toast.LENGTH_SHORT).show();
        } else {
            assistantClass.showAlertDialog("", "Are you sure you want to submit?", true, "Yes", "No", new AlertDialogClickListener() {
                @Override
                public void onPositiveButtonClick(DialogInterface dialog) {
                    dialog.dismiss();
                    assistantClass.showProgressDialog("Please wait...", false);
                    Map<String, String> params = new HashMap<>();
                    params.put("axn", "save_punch_location");
                    params.put("lat", String.valueOf(lat));
                    params.put("lng", String.valueOf(lng));
                    params.put("address", address);
                    params.put("remarks", remarks);
                    assistantClass.makeApiCall(params, "", new APIResult() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
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

                @Override
                public void onNegativeButtonClick(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void GetLocation() {
        assistantClass.showProgressDialog("Getting Location...", false);
        assistantClass.getLocation(new LocationResponse() {
            @Override
            public void onSuccess(double _lat, double _lng) {
                assistantClass.dismissProgressDialog();
                lat = _lat;
                lng = _lng;
                getLocationAddress();
            }

            @Override
            public void onFailure() {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithDismiss("Can't fetch location...");
            }
        });
    }

    private void getLocationAddress() {
        try {
            assistantClass.showProgressDialog("Getting Address...", false);
            Address address = Common_Class.getAddressFromLatLong(context, lat, lng);
            StringBuilder strReturnedAddress = new StringBuilder();
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(address.getAddressLine(i)).append("\n");
            }
            assistantClass.dismissProgressDialog();
            binding.address.setText(strReturnedAddress.toString().trim());
            centreMapOnLocation();
        } catch (Exception e) {
            assistantClass.dismissProgressDialog();
            assistantClass.showAlertDialog("", e.getLocalizedMessage(), false, "Retry", "", new AlertDialogClickListener() {
                @Override
                public void onPositiveButtonClick(DialogInterface dialog) {
                    dialog.dismiss();
                    getLocationAddress();
                }

                @Override
                public void onNegativeButtonClick(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onMapReady(@NonNull NextbillionMap nextbillionMap) {
        map = nextbillionMap;
        map.getStyle(style -> {
        });
        centreMapOnLocation();
    }

    public void centreMapOnLocation() {
        LatLng latlng = new LatLng(lat, lng);

        map.clear();
        MarkerOptions marker = new MarkerOptions().position(latlng);
        map.addMarker(marker);

        int millisecondSpeed = 300;
        CameraPosition position = new CameraPosition.Builder().target(latlng).zoom(16).tilt(20).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(position), millisecondSpeed);
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.map.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.map.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.map.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.map.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.map.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.map.onLowMemory();
    }
}