package com.saneforce.godairy.procurement;

import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_PLANT;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.saneforce.godairy.Activity_Hap.MainActivity;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityNewCollectionCenterBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCollectionCenter extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private ActivityNewCollectionCenterBinding binding;
    private final Context context = this;
    private final String TAG = "NewCollectionCenter_";
    private Bitmap bitmap;
    private String mCenterName, mState, mDistrict, mPlant, mAddr1, mAddr2, mAddr3;
    private String mOwnerName, mOwnerAddr1, mOwnerPinCode, mMobile, mEmail;
    private final List<String> list = new ArrayList<>();
    Toolbar mToolbar;
    ArrayAdapter<String> mAdapter;
    ListView mListView;
    TextView mEmptyView;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewCollectionCenterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.google_map);

        mapFragment.getMapAsync(this);

        updateLocation();

        binding.edPlant.setFocusable(false);

        binding.edPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.formCon.setVisibility(View.GONE);
                binding.plantCon.setVisibility(View.VISIBLE);
            }
        });

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mListView = findViewById(R.id.list);
        mEmptyView = findViewById(R.id.emptyView);

        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
            // Toast.makeText(AgronomistFormActivity.this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
            binding.edPlant.setText(adapterView.getItemAtPosition(i).toString());
            binding.plantCon.setVisibility(View.GONE);
            binding.formCon.setVisibility(View.VISIBLE);
        });

        mListView.setEmptyView(mEmptyView);

        onClick();
        loadPlant();
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Missing location permission");
            return;
        }
        LocationServices.getFusedLocationProviderClient(NewCollectionCenter.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
             //   Toast.makeText(NewCollectionCenter.this, "Location Updated", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "LastLocation: " + (location == null ? "NO LastLocation" : location.toString()));
                if (location != null) {
                    //  binding.latText.setText("" + location.getLatitude());
                    //  binding.langText.setText("" + location.getLongitude());

                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("title"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
                    TextView textView = findViewById(R.id.ed_address);

                      binding.edAddr1.setText("" + getCompleteAddressString(location.getLatitude(), location.getLongitude()));
                }
            }
        });
    }

    private String getCompleteAddressString(double latitude, double longitude) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            //  e.printStackTrace();
            Log.e(TAG, "Error! " + e.getMessage());
        }
        return strAdd;
    }

    private void loadPlant() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getProcPlant2(PROCUREMENT_GET_PLANT, "ALL");

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String plantList;
                    try {
                        plantList = response.body().string();

                        JSONArray jsonArray = new JSONArray(plantList);

                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String plantName = object.optString("plant_name");

                            list.add(plantName);
                        }
                        mAdapter = new ArrayAdapter<>(context,
                                android.R.layout.simple_list_item_1,
                                list);

                        mListView.setAdapter(mAdapter);
                        // updatePlant();
                    } catch (IOException | JSONException e) {
                        //  throw new RuntimeException(e);
                        Toast.makeText(context, "Plant list load error! :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClick() {

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    saveNow();
                }
            }
        });

        binding.cameraCenter.setOnClickListener(v -> {
            binding.txtImgCenterNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Center Photo");
            intent.putExtra("camera_id", "25");
            startActivity(intent);
        });

        binding.imageViewCenterLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "CENP_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Center Photo");
            startActivity(intent);
        });
    }

    private void saveNow() {
        String mActiveFlag = "1";
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("center_name", mCenterName);
        serviceIntent.putExtra("state", mState);
        serviceIntent.putExtra("district", mDistrict);
        serviceIntent.putExtra("plant", mPlant);
        serviceIntent.putExtra("addr1", mAddr1);
        serviceIntent.putExtra("addr2", mAddr2);
        serviceIntent.putExtra("addr3", mAddr3);
        serviceIntent.putExtra("owner_name", mOwnerName);
        serviceIntent.putExtra("owner_addr1", mOwnerAddr1);
        serviceIntent.putExtra("owner_pincode", mOwnerPinCode);
        serviceIntent.putExtra("mobile", mMobile);
        serviceIntent.putExtra("email", mEmail);
        serviceIntent.putExtra("active_flag", mActiveFlag);
        serviceIntent.putExtra("upload_service_id", "19");
        ContextCompat.startForegroundService(this, serviceIntent);
     //   finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs() {
        mCenterName = binding.edCenterName.getText().toString();
        mState = binding.edState.getText().toString();
        mDistrict = binding.edDistrict.getText().toString();
        mPlant = binding.edPlant.getText().toString();
        mAddr1 = binding.edAddr1.getText().toString();
        mAddr2 = binding.edAddr2.getText().toString();
        mAddr3 = binding.edAddr3.getText().toString();
        mOwnerName = binding.edOwnerName.getText().toString();
        mOwnerAddr1 = binding.edOwnerAddr1.getText().toString();
        mOwnerPinCode = binding.edOwnerPincode.getText().toString();
        mMobile = binding.edMobileNo.getText().toString();
        mEmail = binding.edEmail.getText().toString();

        if ("".equals(mCenterName)){
            binding.edCenterName.setError("Empty field!");
            binding.edCenterName.requestFocus();
            return false;
        }
        if ("".equals(mState)){
            binding.edState.setError("Empty field!");
            binding.edState.requestFocus();
            return false;
        }
        if ("".equals(mDistrict)){
            binding.edDistrict.setError("Empty field!");
            binding.edDistrict.requestFocus();
            return false;
        }
        if (bitmap == null){
            binding.txtImgCenterNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mPlant)){
            binding.edPlant.setError("Empty field!");
            binding.edPlant.requestFocus();
            return false;
        }
        if ("".equals(mAddr1)){
            binding.edAddr1.setError("Empty field!");
            binding.edAddr1.requestFocus();
            return false;
        }
        if ("".equals(mAddr2)){
            binding.edAddr2.setError("Empty field!");
            binding.edAddr2.requestFocus();
            return false;
        }
        if ("".equals(mAddr3)){
            binding.edAddr3.setError("Empty field!");
            binding.edAddr3.requestFocus();
            return false;
        }
        if ("".equals(mOwnerName)){
            binding.edOwnerName.setError("Empty field!");
            binding.edOwnerName.requestFocus();
            return false;
        }
        if ("".equals(mOwnerAddr1)){
            binding.edOwnerAddr1.setError("Empty field!");
            binding.edOwnerAddr1.requestFocus();
            return false;
        }
        if ("".equals(mOwnerPinCode)){
            binding.edOwnerPincode.setError("Empty field!");
            binding.edOwnerPincode.requestFocus();
            return false;
        }
        if ("".equals(mMobile)){
            binding.edMobileNo.setError("Empty field!");
            binding.edMobileNo.requestFocus();
            return false;
        }
        if ("".equals(mEmail)){
            binding.edEmail.setError("Empty field!");
            binding.edEmail.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        File fileFat = new File(getExternalFilesDir(null), "/procurement/" + "CENP_123.jpg");
        bitmap = BitmapFactory.decodeFile(fileFat.getAbsolutePath());

        if (bitmap != null){
            binding.imageViewCenterLayout.setVisibility(View.VISIBLE);
            binding.imageCenter.setImageBitmap(bitmap);
            binding.txtImgCenterNotValid.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //  buildGoogleApiClient();
                mMap.setMyLocationEnabled(false);
            }
        } else {
            // buildGoogleApiClient();
            mMap.setMyLocationEnabled(false);
        }
    }
}