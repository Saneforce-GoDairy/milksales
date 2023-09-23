package com.saneforce.milksales.fragments;

import static com.saneforce.milksales.Activity_Hap.Login.MyPREFERENCES;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
            // Todo: Leave
        });

        binding.lateLL.setOnClickListener(v -> {
            Intent intent = new Intent(context, View_All_Status_Activity.class);
            intent.putExtra("Priod", 0);
            intent.putExtra("Status", "Late");
            intent.putExtra("name", "View Late Status");
            context.startActivity(intent);
        });

        binding.earlyArrivalLL.setOnClickListener(v -> {
            // Todo: Early Arrival
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
        if (cModMnth == m) return;
        Common_Class Dt = new Common_Class();
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
        Log.d("Tag", sDt + "-" + String.valueOf(fmn) + "-" + String.valueOf(tmn));
      //  TextView txUserName = findViewById(R.id.txtMnth);
      //  txUserName.setText("23," + mns[fmn - 1] + " - 22," + mns[tmn - 1]);

        // appendDS = appendDS + "&divisionCode=" + userData.divisionCode + "&sfCode=" + sSF + "&rSF=" + userData.sfCode + "&State_Code=" + userData.State_Code;
        if (Common_Class.isNullOrEmpty(mShared_common_pref.getvalue(Constants.DB_TWO_GET_MREPORTS+"_"+mns[fmn - 1]))) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonArray> rptMnCall = apiInterface.getDataArrayList("get/AttndMn", m,
                    UserDetails.getString("Divcode", ""),
                    UserDetails.getString("Sfcode", ""), UserDetails.getString("Sfcode", ""), "", "", null);
            rptMnCall.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    assignMnthReports(response.body(), m);
                    mShared_common_pref.save(Constants.DB_TWO_GET_MREPORTS+"_"+mns[fmn - 1], gson.toJson(response.body()));
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Log.d("Tag", String.valueOf(t));
                //    LoadingCnt++;
                  //  hideShimmer();
                }
            });
        } else {
            Type userType = new TypeToken<JsonArray>() {
            }.getType();
            JsonArray arr = (gson.fromJson(mShared_common_pref.getvalue(Constants.DB_TWO_GET_MREPORTS+"_"+mns[fmn - 1]), userType));
            assignMnthReports(arr, m);
        }
    }

    private void assignMnthReports(JsonArray res, int m) {
        try {
            JsonArray dyRpt = new JsonArray();
            for (int il = 0; il < res.size(); il++) {
                JsonObject Itm = res.get(il).getAsJsonObject();
                JsonObject newItem = new JsonObject();
                newItem.addProperty("name", Itm.get("Status").getAsString());
                newItem.addProperty("value", Itm.get("StatusCnt").getAsString());
                newItem.addProperty("Link", true);
                newItem.addProperty("Priod", m);
                newItem.addProperty("color", Itm.get("StusClr").getAsString().replace(" !important", ""));

                JsonObject jsonObject0 = res.get(0).getAsJsonObject();
                JsonObject jsonObject1 = res.get(1).getAsJsonObject();
                JsonObject jsonObject2 = res.get(2).getAsJsonObject();

                binding.late.setText(jsonObject1.get("StatusCnt").getAsString());

                binding.weeklyOffText.setText(jsonObject0.get("Status").getAsString());
                binding.weeklyOff.setText(jsonObject0.get("StatusCnt").getAsString());

                binding.missedPunch.setText(jsonObject2.get("StatusCnt").getAsString());

                dyRpt.add(newItem);
            }

           // recyclerView = findViewById(R.id.Rv_MnRpt);
         //   mAdapter = new HomeRptRecyler(dyRpt, Dashboard_Two.this);

//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.setAdapter(mAdapter);
//            LoadingCnt++;
//            hideShimmer();
        } catch (Exception ignored) {
        }
    }
}