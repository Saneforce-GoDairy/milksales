package com.saneforce.godairy.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.adapters.GateAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GateInOutFragment extends Fragment {
    RecyclerView mRecyclerView;
    Context context;
    Shared_Common_Pref shared_common_pref;
    GateAdapter gateAdap;
    ProgressBar progressbar;
    TextView info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gate_in_out_settings, container, false);

        context = getContext();
        mRecyclerView = view.findViewById(R.id.gate_recycle);
        progressbar = view.findViewById(R.id.progressbar);
        info = view.findViewById(R.id.info);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);

        shared_common_pref = new Shared_Common_Pref(getActivity());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> Callto = apiInterface.gteDta(Shared_Common_Pref.Sf_Code, Common_Class.GetDateOnly());
        Callto.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                try {
                    JsonArray jsonArray = response.body();
                    if (jsonArray != null && jsonArray.size() > 0) {
                        gateAdap = new GateAdapter(context, jsonArray);
                        mRecyclerView.setAdapter(gateAdap);
                    } else {
                        info.setVisibility(View.VISIBLE);
                    }
                    progressbar.setVisibility(View.GONE);
                } catch (Exception ignored) {
                    progressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
                info.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
            }
        });

        return view;
    }
}