package com.saneforce.milksales.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Activity_Hap.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.databinding.FragmentTodayBinding;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private Gson gson;
    private int LoadingCnt = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTodayBinding.inflate(inflater, container, false);

        gson = new Gson();
        initSharedPref();
        loadTodayCheckInReport();

        return binding.getRoot();
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
            Log.e("dev__", divisionCode);
        }else {
            Log.e("dev__", "h");
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
                LoadingCnt++;
            }
            JsonObject fItm = res.get(0).getAsJsonObject();

            Log.e("check_in_time", fItm.get("GeoIn").getAsString());
            binding.checkInTime.setText(fItm.get("AttTm").getAsString());
            binding.geoIn.setText(fItm.get("GeoIn").getAsString());

            binding.checkOutTime.setText(fItm.get("ET").getAsString());
            binding.geoOut.setText(fItm.get("GeoOut").getAsString());
        }catch (Exception ignored) {

        }
    }
}