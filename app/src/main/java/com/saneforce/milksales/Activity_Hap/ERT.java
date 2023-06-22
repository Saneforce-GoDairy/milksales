package com.saneforce.milksales.Activity_Hap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Model_Class.ERTChild;
import com.saneforce.milksales.Model_Class.ERTParent;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.ERTMainAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ERT extends AppCompatActivity {
    Shared_Common_Pref shared_common_pref;
    private RecyclerView menuRecycler;
    private List<ERTParent> cacheMenuRes;
    private ERTMainAdapter mainMenuAdapter;
    ImageView closeERT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_r_t);
        shared_common_pref = new Shared_Common_Pref(this);

        menuRecycler = findViewById(R.id.recylerview_ert);
        cacheMenuRes = new ArrayList<>();

        cacheMenuRes = new ArrayList<>();
        mainMenuAdapter = new ERTMainAdapter(getApplicationContext(), cacheMenuRes);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        menuRecycler.setLayoutManager(layoutManager);
        menuRecycler.setItemViewCacheSize(20);
        menuRecycler.setAdapter(mainMenuAdapter);

        closeERT = findViewById(R.id.close_ert);
        closeERT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ERTDETAILS();
    }


    public void ERTDETAILS() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiInterface.ERTDetails(shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                String str = response.body().toString();
                Log.e("ERT_DETAILS", str);

                final HashMap<String, List<ERTChild>> map = new HashMap<>();
                try {

                    JSONObject jsonObjectq = new JSONObject(str);


                    JSONArray jsonArray = jsonObjectq.getJSONArray("data");
                    Log.e("String_Json", jsonArray.getJSONObject(0).getString("ERTCatNm"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (map.containsKey(jsonObject.getString("ERTCatNm"))) {
                            List<ERTChild> menu = map.get(jsonObject.getString("ERTCatNm"));
                            menu.add(new ERTChild(jsonObject.getString("Desig"), jsonObject.getString("SF_Mobile"), jsonObject.getString("name"), jsonObject.getString("SF_Mobile"), jsonObject.getString("Profile_Pic")));
                            map.put(jsonObject.getString("ERTCatNm"), menu);
                        } else {
                            List<ERTChild> menus = new ArrayList<>();
                            menus.add(new ERTChild(jsonObject.getString("Desig"), jsonObject.getString("SF_Mobile"), jsonObject.getString("name"), jsonObject.getString("SF_Mobile"), jsonObject.getString("Profile_Pic")));
                            map.put(jsonObject.getString("ERTCatNm"), menus);
                        }

                    }
                    for (String catname : map.keySet()) {
                        cacheMenuRes.add(new ERTParent(catname, map.get(catname)));
                    }

                    mainMenuAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });


    }
}