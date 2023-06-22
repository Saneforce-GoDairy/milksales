package com.saneforce.milksales.SFA_Activity;

import static com.saneforce.milksales.Common_Class.Common_Class.addquote;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dist_Locations extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    LinearLayout selectdistributor;
    TextView distilatitude, distilongitude, distributor_Name, capturelatlong, submit_button;
    Gson gson;
    Shared_Common_Pref sharedCommonPref;
    String Distributor_Id = "";
    ProgressBar progressbar;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    DatabaseHandler db;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dist__locations);
            db = new DatabaseHandler(this);

            selectdistributor = findViewById(R.id.selectdistributor);
            progressbar = findViewById(R.id.progressbar);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            submit_button = findViewById(R.id.submit_button);
            distilatitude = findViewById(R.id.distilatitude);
            distilongitude = findViewById(R.id.distilongitude);
            capturelatlong = findViewById(R.id.capturelatlong);
            distributor_Name = findViewById(R.id.distributor_Name);
            sharedCommonPref = new Shared_Common_Pref(Dist_Locations.this);
            submit_button.setOnClickListener(this);
            capturelatlong.setOnClickListener(this);

            gson = new Gson();
            selectdistributor.setOnClickListener(this);
            ImageView backView = findViewById(R.id.imag_back);
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            common_class = new Common_Class(this);

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);

            if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
                selectdistributor.setEnabled(false);
                distributor_Name.setText(sharedCommonPref.getvalue(Constants.Distributor_name));

                findViewById(R.id.ivDistSpinner).setVisibility(View.GONE);


            }

        } catch (Exception e) {

        }
    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        if (type == 2) {
            distributor_Name.setText(myDataset.get(position).getName());
            Distributor_Id = myDataset.get(position).getId();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectdistributor:
                common_class.showCommonDialog(common_class.getDistList(), 2, this);
                break;
            case R.id.submit_button:
                if (distributor_Name.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Select Franchise", Toast.LENGTH_SHORT).show();
                } else if (distilatitude.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Capture The Lat and Long", Toast.LENGTH_SHORT).show();
                } else {
                    SaveLatLong();
                }
                break;
            case R.id.capturelatlong:
                fetchLocation();
                break;
        }
    }

    public void SaveLatLong() {
        progressbar.setVisibility(View.VISIBLE);
        Calendar c = Calendar.getInstance();
        String Dcr_Dste = new SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(new Date());
        JSONArray jsonarr = new JSONArray();
        JSONObject jsonarrplan = new JSONObject();
        try {
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("Distributor_Id", addquote(Distributor_Id));
            jsonobj.put("Latitude", addquote(distilatitude.getText().toString()));
            jsonobj.put("Longitude", addquote(distilongitude.getText().toString()));
            jsonobj.put("Created_Date", addquote(Common_Class.GetDate()));
            jsonarrplan.put("saveDistiLatLong", jsonobj);
            jsonarr.put(jsonarrplan);
            Log.d("Distributor_QS", jsonarr.toString());
            Map<String, String> QueryString = new HashMap<>();
            QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
            QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
            QueryString.put("State_Code", Shared_Common_Pref.StateCode);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<Object> Callto = apiInterface.Tb_Mydayplan(QueryString, jsonarr.toString());
            Callto.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Log.e("RESPONSE_FROM_SERVER", response.body().toString());
                    Log.d("QueryString", String.valueOf(QueryString));
                    progressbar.setVisibility(View.GONE);
                    if (response.code() == 200 || response.code() == 201) {
                        /*common_class.CommonIntentwithFinish(Dashboard.class);*/
                        Toast.makeText(Dist_Locations.this, "Latitude and Longitude Updated Successfully", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    progressbar.setVisibility(View.GONE);
                    Log.e("Reponse TAG", "onFailure : " + t.toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void fetchLocation() {
        progressbar.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    distilatitude.setText("" + currentLocation.getLatitude());
                    distilongitude.setText("" + currentLocation.getLongitude());
                    //Shared_Common_Pref.OutletAddress = getCompleteAddressString(currentLocation.getLatitude(), currentLocation.getLongitude());
                    progressbar.setVisibility(View.GONE);
                }
            }
        });
    }
}