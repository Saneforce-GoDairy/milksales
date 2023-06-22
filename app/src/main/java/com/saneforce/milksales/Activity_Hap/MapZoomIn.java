package com.saneforce.milksales.Activity_Hap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapZoomIn extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {


    Shared_Common_Pref mShared_common_pref;
    ApiInterface apiInterface;
    GoogleMap mGoogleMap;
    ArrayList<LatLng> latLonArray = new ArrayList<>();
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    String dateVal = "";
    Polyline polyline1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_map_zoom_in);
        mShared_common_pref = new Shared_Common_Pref(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.route_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        dateVal = String.valueOf(getIntent().getSerializableExtra("date"));
        Log.e("dateValue", dateVal);
        Log.e("Share_SF", mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
        if (!dateVal.equals("")) {
            callMap(dateVal);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    public void CloseActivity(View v) {
        finish();
    }


    /*Showing Map based on Map*/

    public void callMap(String date) {


        Log.v("Map_Date", date + " 00:00:00");
        date = date.replaceAll("^[\"']+|[\"']+$", "");
        Log.v("Map_Date", date);


        Call<ResponseBody> Callto = apiInterface.getMap(mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), date + " 00:00:00");
        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String jsonData = null;
                try {
                    jsonData = response.body().string();


                    if (!jsonData.equals("")) {
                        try {
                            JSONArray jsonArray = new JSONArray(jsonData);
                            Double lat = 0.0, lon = 0.0, lat1 = 0.0, lon1 = 0.0;
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (int i = 0; i < jsonArray.length() - 1; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String strLat = jsonObject.getString("lat");
                                String strLon = jsonObject.getString("lng");
                                lat = Double.parseDouble(strLat);
                                lon = Double.parseDouble(strLon);
                                LatLng loc = new LatLng(lat, lon);
                                builder.include(loc);
                                latLonArray.add(loc);
                            }

                            if (latLonArray.size() != 0) {
                                polyline1 = mGoogleMap.addPolyline(new PolylineOptions()
                                        .clickable(true)
                                        .addAll(latLonArray));

                                polyline1.setTag("A");
                                polyline1.setColor(COLOR_ORANGE_ARGB);
                                LatLngBounds bounds = builder.build();
                                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    @Override
    public void onClick(View v) {

    }
}