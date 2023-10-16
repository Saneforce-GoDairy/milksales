package com.saneforce.milksales.fragments;

import static com.saneforce.milksales.Activity_Hap.Login.MyPREFERENCES;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity_Hap.Common_Class;
import com.saneforce.milksales.Activity_Hap.Dashboard_Two;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.Status_Activity.View_All_Status_Activity;
import com.saneforce.milksales.adapters.HomeRptRecyler;
import com.saneforce.milksales.databinding.FragmentMonthlyBinding;

import java.lang.reflect.Type;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthlyFragment extends Fragment {
    private FragmentMonthlyBinding binding;
    int cModMnth = 1;
    private Common_Class DT = new Common_Class();
    private Shared_Common_Pref mShared_common_pref;
    private final String[] mns = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private SharedPreferences CheckInDetails, UserDetails, sharedpreferences;
    private Gson gson;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMonthlyBinding.inflate(inflater, container, false);

        context = getContext();
        UserDetails = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mShared_common_pref = new Shared_Common_Pref(getActivity());
        gson = new Gson();
        getMnthReports(0);

        binding.viewAll.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), View_All_Status_Activity.class);
            intent.putExtra("Priod", 0);
            intent.putExtra("Status", "");
            intent.putExtra("name", "View All Status");
            startActivity(intent);
        });

        binding.permissionLL.setOnClickListener(v -> {
            Intent intent = new Intent(context, View_All_Status_Activity.class);
            intent.putExtra("Priod", 0);
            intent.putExtra("Status", "Permission");
            intent.putExtra("name", "View Permission Status");
            context.startActivity(intent);
        });

        binding.LeaveLL.setOnClickListener(v -> {
            Intent intent = new Intent(context, View_All_Status_Activity.class);
            intent.putExtra("Priod", 0);
            intent.putExtra("Status", "Leave");
            intent.putExtra("name", "View Leave Status");
            context.startActivity(intent);
        });

        binding.lateLL.setOnClickListener(v -> {
            Intent intent = new Intent(context, View_All_Status_Activity.class);
            intent.putExtra("Priod", 0);
            intent.putExtra("Status", "Late");
            intent.putExtra("name", "View Late Status");
            context.startActivity(intent);
        });

        binding.onTimeLL.setOnClickListener(v -> {
            Intent intent = new Intent(context, View_All_Status_Activity.class);
            intent.putExtra("Priod", 0);
            intent.putExtra("Status", "On-Time");
            intent.putExtra("name", "View On-Time Status");
            context.startActivity(intent);
        });

        binding.missedPunchLL.setOnClickListener(v -> {
            Intent intent = new Intent(context, View_All_Status_Activity.class);
            intent.putExtra("Priod", 0);
            intent.putExtra("Status", "Missed Punch");
            intent.putExtra("name", "View Missed Punch Status");
            context.startActivity(intent);
        });

        binding.weeklyOffLL.setOnClickListener(v -> {
            Intent intent = new Intent(context, View_All_Status_Activity.class);
            intent.putExtra("Priod", 0);
            intent.putExtra("Status", "Weekly off");
            intent.putExtra("name", "View Weekly off Status");
            context.startActivity(intent);
        });

        return binding.getRoot();
    }
    private void getMnthReports(int m) {
        /*Common_Class Dt = new Common_Class();
        String sDt = Dt.GetDateTime(getContext(), "yyyy-MM-dd HH:mm:ss");
        Date dt = Dt.getDate(sDt);
        if (m == -1) {
            sDt = Dt.AddMonths(sDt, -1, "yyyy-MM-dd HH:mm:ss");
        }
        if (Dt.getDay(sDt) < 23) {
            sDt = Dt.AddMonths(sDt, -1, "yyyy-MM-dd HH:mm:ss");
        }
        int fmn = Dt.getMonth(sDt);
        if (m == -1) {
            mShared_common_pref.clear_pref(Constants.DB_TWO_GET_MREPORTS+"_"+mns[fmn - 1]);
        }
        sDt = Dt.AddMonths(Dt.getYear(sDt) + "-" + Dt.getMonth(sDt) + "-22 00:00:00", 1, "yyyy-MM-dd HH:mm:ss");
        int tmn = Dt.getMonth(sDt);
        Log.d("Tag", sDt + "-" + String.valueOf(fmn) + "-" + String.valueOf(tmn));*/
      //  TextView txUserName = findViewById(R.id.txtMnth);
      //  txUserName.setText("23," + mns[fmn - 1] + " - 22," + mns[tmn - 1]);

        // appendDS = appendDS + "&divisionCode=" + userData.divisionCode + "&sfCode=" + sSF + "&rSF=" + userData.sfCode + "&State_Code=" + userData.State_Code;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> rptMnCall = apiInterface.getDataObjectList("get/AttndMn", m,
                UserDetails.getString("Divcode", ""),
                UserDetails.getString("Sfcode", ""), UserDetails.getString("Sfcode", ""), "", "", null);
        rptMnCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                assignMnthReports(response.body(), m);
//                mShared_common_pref.save(Constants.DB_TWO_GET_MREPORTS+"_"+mns[fmn - 1], gson.toJson(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("Tag", String.valueOf(t));
            }
        });
    }

    private void assignMnthReports(JsonObject res, int m) {
        try {
            Log.e("ryrh", "" + res);

            int permissionCount = res.get("Permission").getAsInt();
            int leaveCount = res.get("leave").getAsInt();
            int lateCount = res.get("late").getAsInt();
            int onTimeCount = res.get("onTime").getAsInt();
            int missedPunchCount = res.get("missedPunch").getAsInt();
            int weeklyOffCount = res.get("weeklyOff").getAsInt();

            binding.permissionCount.setText(String.valueOf(permissionCount));
            binding.leaveCount.setText(String.valueOf(leaveCount));
            binding.lateCount.setText(String.valueOf(lateCount));
            binding.onTimeCount.setText(String.valueOf(onTimeCount));
            binding.missedPunchCount.setText(String.valueOf(missedPunchCount));
            binding.weeklyOffCount.setText(String.valueOf(weeklyOffCount));

        } catch (Exception e) {
            Log.e("ryrh", "Error: " + e.getMessage());
        }
    }
}