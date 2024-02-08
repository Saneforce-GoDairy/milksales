package com.saneforce.godairy.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class TravelPunchLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    ActivityTravelPunchLocationBinding binding;

    Common_Class common_class;
    AssistantClass assistantClass;
    Context context = this;
    GoogleMap mGoogleMap;

    double lat = 0, lng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTravelPunchLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        common_class = new Common_Class(this);
        assistantClass = new AssistantClass(context);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Travel Punch Location");
        binding.toolbar.home.setImageResource(R.drawable.ic_history);
        binding.toolbar.home.setOnClickListener(v -> startActivity(new Intent(context, TravelPunchHistoryActivity.class)));

        binding.refreshLocation.setOnClickListener(v -> GetLocation());
        binding.punchLocation.setOnClickListener(v -> PunchLocation());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        centreMapOnLocation();
    }

    public void centreMapOnLocation() {
        LatLng userLocation = new LatLng(lat, lng);
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions().position(userLocation).title("Your are here"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
    }
}