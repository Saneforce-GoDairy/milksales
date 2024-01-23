package com.saneforce.godairy.Activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.R;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.ActivityTravelPunchHistoryBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TravelPunchHistoryActivity extends AppCompatActivity implements OnMapReadyCallback {
    ActivityTravelPunchHistoryBinding binding;

    Common_Class common_class;
    AssistantClass assistantClass;
    Context context = this;
    GoogleMap mGoogleMap;
    JSONArray array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTravelPunchHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        common_class = new Common_Class(this);
        assistantClass = new AssistantClass(context);
        array = new JSONArray();

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Travel Punch History");
        common_class.gotoHomeScreen(context, binding.toolbar.home);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        GetLocation();
    }

    private void GetLocation() {
        assistantClass.showProgressDialog("Please wait...", false);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_punched_locations");
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                assistantClass.dismissProgressDialog();
                array = jsonObject.optJSONArray("response");
                centreMapOnLocation();
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
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 16));
    }

    public void centreMapOnLocation() {
        mGoogleMap.clear();
        if (array != null && array.length() > 0) {
            PolygonOptions polygonOptions = new PolygonOptions();
            for (int i = 0; i < array.length(); i++) {
                try {
                    mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(array.optJSONObject(i).optDouble("lat"), array.optJSONObject(i).optDouble("lng"))).title(array.optJSONObject(i).optString("title")));
                    polygonOptions.add(new LatLng(array.optJSONObject(i).optDouble("lat"), array.optJSONObject(i).optDouble("lng")));
                } catch (Exception ignored) {
                }
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng point : polygonOptions.getPoints()) {
                builder.include(point);
            }
            LatLngBounds bounds = builder.build();
            mGoogleMap.addPolygon(polygonOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }
}