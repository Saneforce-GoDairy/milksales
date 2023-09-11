package com.saneforce.milksales.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Activity_Hap.Common_Class;
import com.saneforce.milksales.Activity_Hap.Dashboard_Two;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.MapDirectionActivity;
import com.saneforce.milksales.adapters.HomeRptRecyler;
import com.saneforce.milksales.databinding.FragmentTodayBinding;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayFragment extends Fragment {
    private FragmentTodayBinding binding;
    private Shared_Common_Pref SHARED_COMMON_PREF;
    private SharedPreferences USER_DETAILS;
    public static final String KEY_USER_DETAIL = "MyPrefs";
    private static final String TAG = "TodayFragment";
    private Gson gson;
    JsonObject fItm;
    private Shared_Common_Pref mShared_common_pref;
    private String timerTime,timerDate;
    private SharedPreferences UserDetails;
    public static final String UserDetail = "MyPrefs";
    private String viewMode = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTodayBinding.inflate(inflater, container, false);

        gson = new Gson();
        mShared_common_pref = new Shared_Common_Pref(getActivity());
        UserDetails = getActivity().getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

        initSharedPref();
        loadTodayCheckInReport();
        onClick();

        return binding.getRoot();
    }

    private void onClick() {
        binding.viewGeoIn.setOnClickListener(v -> openMap());
        binding.viewGeoOut.setOnClickListener(v -> openMap());
    }

    private void openMap() {
        mShared_common_pref.save(Constants.LOGIN_DATE, com.saneforce.milksales.Common_Class.Common_Class.GetDatewothouttime());
        JsonArray dyRpt = new JsonArray();
        JsonObject newItem = new JsonObject();

        timerTime = fItm.get("AttTm").getAsString();
        timerDate = fItm.get("AttDate").getAsString().replaceAll("/", "-");
        newItem.addProperty("name", "Shift");
        newItem.addProperty("value", fItm.get("SFT_Name").getAsString());
        newItem.addProperty("Link", false);
        newItem.addProperty("color", "#333333");
        dyRpt.add(newItem);
        newItem = new JsonObject();
        newItem.addProperty("name", "Status");
        newItem.addProperty("value", fItm.get("DayStatus").getAsString());
        newItem.addProperty("color", fItm.get("StaColor").getAsString());
        dyRpt.add(newItem);

        if (!fItm.get("HQNm").getAsString().equalsIgnoreCase("")) {
            newItem = new JsonObject();
            newItem.addProperty("name", "Location");
            newItem.addProperty("value", fItm.get("HQNm").getAsString());
            newItem.addProperty("color", fItm.get("StaColor").getAsString());
            newItem.addProperty("type", "geo");
            dyRpt.add(newItem);
        }
        newItem = new JsonObject();
        newItem.addProperty("name", "Check-In");
        newItem.addProperty("value", fItm.get("AttTm").getAsString());
        newItem.addProperty("color", "#333333");
        dyRpt.add(newItem);
        if (!fItm.get("ET").isJsonNull()) {
            newItem = new JsonObject();
            newItem.addProperty("name", "Last Check-Out");
            newItem.addProperty("value", fItm.get("ET").getAsString());
            newItem.addProperty("color", "#333333");
            dyRpt.add(newItem);
        }
        newItem = new JsonObject();
        newItem.addProperty("name", "Geo In");
        newItem.addProperty("value", fItm.get("GeoIn").getAsString());
        newItem.addProperty("color", "#333333");
        newItem.addProperty("type", "geo");
        dyRpt.add(newItem);

        newItem = new JsonObject();
        newItem.addProperty("name", "Geo Out");
        newItem.addProperty("value", fItm.get("GeoOut").getAsString());//"<a href=\"https://www.google.com/maps?q="+fItm.get("GeoOut").getAsString()+"\">"+fItm.get("GeoOut").getAsString()+"</a>");
        newItem.addProperty("color", "#333333");
        newItem.addProperty("type", "geo");
        dyRpt.add(newItem);

        Integer OTFlg = UserDetails.getInt("OTFlg", 0);
        if (OTFlg==1 && viewMode.equalsIgnoreCase("extended")) {
            newItem = new JsonObject();
            newItem.addProperty("name", "Extended Start");
            newItem.addProperty("value", fItm.get("ExtStartTtime").getAsString());//"<a href=\"https://www.google.com/maps?q="+fItm.get("GeoOut").getAsString()+"\">"+fItm.get("GeoOut").getAsString()+"</a>");
            newItem.addProperty("color", "#333333");
            /*newItem.addProperty("type", "geo");*/
            dyRpt.add(newItem);

            newItem = new JsonObject();
            newItem.addProperty("name", "Extended End");
            newItem.addProperty("value", fItm.get("ExtEndtime").getAsString());//"<a href=\"https://www.google.com/maps?q="+fItm.get("GeoOut").getAsString()+"\">"+fItm.get("GeoOut").getAsString()+"</a>");
            newItem.addProperty("color", "#333333");
            /*newItem.addProperty("type", "geo");*/
            dyRpt.add(newItem);

            newItem = new JsonObject();
            newItem.addProperty("name", "Ext.Geo In");
            newItem.addProperty("value", fItm.get("Extin").getAsString());
            newItem.addProperty("color", "#333333");
            newItem.addProperty("type", "geo");
            dyRpt.add(newItem);

            newItem = new JsonObject();
            newItem.addProperty("name", "Ext.Geo Out");
            newItem.addProperty("value", fItm.get("Extout").getAsString());//"<a href=\"https://www.google.com/maps?q="+fItm.get("GeoOut").getAsString()+"\">"+fItm.get("GeoOut").getAsString()+"</a>");
            newItem.addProperty("color", "#333333");
            newItem.addProperty("type", "geo");
            dyRpt.add(newItem);
        }

        JsonObject jsonObject = dyRpt.get(4).getAsJsonObject();
        String tag = jsonObject.get("name").getAsString();
        String value = jsonObject.get("value").getAsString();
        String[] latlongs = value.split(",");

        Intent intent = new Intent(getContext(), MapDirectionActivity.class);
        intent.putExtra(Constants.DEST_LAT, latlongs[0]);
        intent.putExtra(Constants.DEST_LNG, latlongs[1]);

        intent.putExtra(Constants.DEST_NAME, tag);
        intent.putExtra(Constants.NEW_OUTLET, "GEO");
        startActivity(intent);
    }

    private void initSharedPref() {
        SHARED_COMMON_PREF = new Shared_Common_Pref(getActivity());
        SHARED_COMMON_PREF.save("Dashboard", "one");

        USER_DETAILS = requireActivity().getSharedPreferences(KEY_USER_DETAIL, Context.MODE_PRIVATE);
    }

    private void loadTodayCheckInReport() {
        String todayDate = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date());
        binding.date.setText(todayDate);

        if (Common_Class.isNullOrEmpty(SHARED_COMMON_PREF.getvalue(Constants.DB_TWO_GET_DYREPORTS))) {
            String divisionCode = USER_DETAILS.getString("Divcode", "");
            Log.e(TAG, "Division code : " + divisionCode);

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonArray> rptCall = apiInterface.getDataArrayList("get/AttnDySty",
                    USER_DETAILS.getString("Divcode", ""),
                    USER_DETAILS.getString("Sfcode", ""), "", "", null);
            Log.v(TAG, "View Request :" + rptCall.request().toString());
            rptCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    try {
                        assignDyReports(response.body());
                        SHARED_COMMON_PREF.save(Constants.DB_TWO_GET_DYREPORTS, gson.toJson(response.body()));
                    } catch (Exception ignored) {
                    }

                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Log.d(TAG, "Error : " + t);
                }
            });
        }else {
            Type userType = new TypeToken<JsonArray>() {
            }.getType();
            JsonArray arr = (gson.fromJson(SHARED_COMMON_PREF.getvalue(Constants.DB_TWO_GET_DYREPORTS), userType));
            assignDyReports(arr);
        }
    }

    private void assignDyReports(JsonArray res) {
        try {
            if (res.size() < 1){
                Toast.makeText(getContext(), "No Records Today", Toast.LENGTH_LONG).show();
            }
            fItm = res.get(0).getAsJsonObject();

            Log.e("check_in_time", fItm.get("GeoIn").getAsString());
            binding.checkInTime.setText(fItm.get("AttTm").getAsString());
            binding.geoIn.setText(fItm.get("GeoIn").getAsString());

            binding.checkOutTime.setText(fItm.get("ET").getAsString());
            binding.geoOut.setText(fItm.get("GeoOut").getAsString());

          String  checkInUrl =  fItm.get("ImgName").getAsString();
                    Glide.with(requireContext())
                    .load(ApiClient.BASE_URL.replaceAll("server/", "") + fItm.get("EImgName").getAsString())
                            .apply(RequestOptions.circleCropTransform())
                    .into(binding.userProfile);
        }catch (Exception ignored) {

        }
    }
}