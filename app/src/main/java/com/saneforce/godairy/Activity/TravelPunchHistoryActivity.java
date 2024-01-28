package com.saneforce.godairy.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
            int label = 1;
            for (int i = 0; i < array.length(); i++) {
                try {
                    polygonOptions.add(new LatLng(array.optJSONObject(i).optDouble("lat"), array.optJSONObject(i).optDouble("lng")));
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(array.optJSONObject(i).optDouble("lat"), array.optJSONObject(i).optDouble("lng")))
                            .title(array.optJSONObject(i).optString("title"))
                            .icon(getCustomMarkerIcon(label)));
                    label++;
                } catch (Exception e) {
                    assistantClass.log(e.getLocalizedMessage());
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

    private BitmapDescriptor getCustomMarkerIcon(int label) {
        View markerLayout = getLayoutInflater().inflate(R.layout.custom_icon_for_travel_punch, null);
        TextView markerLabel = markerLayout.findViewById(R.id.caption);
        markerLabel.setText(String.valueOf(label));
        Bitmap image = createDrawableFromView(context, markerLayout);
        return BitmapDescriptorFactory.fromBitmap(image);
    }

    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}