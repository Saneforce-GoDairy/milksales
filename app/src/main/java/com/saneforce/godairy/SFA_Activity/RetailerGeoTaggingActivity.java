package com.saneforce.godairy.SFA_Activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.SFA_Model_Class.OutletGeoTagInfoModel;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.ActivityRetailerGeoTaggingBinding;
import com.saneforce.godairy.fragments.GeoTaggedRetailerFragment;
import com.saneforce.godairy.fragments.NotGeoTaggedRetailerFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RetailerGeoTaggingActivity extends AppCompatActivity {
    ActivityRetailerGeoTaggingBinding binding;
    AssistantClass assistantClass;
    Common_Class common_class;
    Context context = this;

    String title = "OUTLET GEO TAG INFO";
    JSONArray array;
    public static ArrayList<OutletGeoTagInfoModel> listWithGeo;
    public static ArrayList<OutletGeoTagInfoModel> listWithoutGeo;
    UpdateResponseUI listener;

    public void setListener(UpdateResponseUI listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRetailerGeoTaggingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        assistantClass = new AssistantClass(context);
        common_class = new Common_Class(this);
        array = new JSONArray();
        listWithGeo = new ArrayList<>();
        listWithoutGeo = new ArrayList<>();

        binding.toolbar.title.setText(title);
        binding.toolbar.back.setOnClickListener(view -> onBackPressed());
        common_class.gotoHomeScreen(context, binding.toolbar.home);

        MyPagerAdapter adapter = new MyPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Tagged");
                    break;
                case 1:
                    tab.setText("Not Tagged");
                    break;
            }
        }).attach();

        getOutletGeoTagInfo();
    }

    private void getOutletGeoTagInfo() {
        assistantClass.showProgressDialog("Fetching outlets...", false);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "getOutletGeoTagInfo");
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                assistantClass.dismissProgressDialog();
                array = jsonObject.optJSONArray("response");
                assignResult();
            }

            @Override
            public void onFailure(String error) {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithFinish(error);
            }
        });
    }

    private void assignResult() {
        if (array == null) {
            array = new JSONArray();
        }
        listWithGeo.clear();
        listWithoutGeo.clear();
        for (int i = 0; i < array.length(); i++) {
            String code = array.optJSONObject(i).optString("code");
            String name = array.optJSONObject(i).optString("name");
            String address = array.optJSONObject(i).optString("address");
            String lat = array.optJSONObject(i).optString("lat");
            String lng = array.optJSONObject(i).optString("lng");
            String mobile = array.optJSONObject(i).optString("mobile");
            if (lat.trim().isEmpty() || lng.trim().isEmpty()) {
                listWithoutGeo.add(new OutletGeoTagInfoModel(code, name, address, lat, lng, mobile));
            } else {
                listWithGeo.add(new OutletGeoTagInfoModel(code, name, address, lat, lng, mobile));
            }
        }
        if (listener != null) {
            listener.onLoadDataUpdateUI("", "OutletInfo");
        }
    }

    public static class MyPagerAdapter extends FragmentStateAdapter {

        public MyPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 1) {
                return new NotGeoTaggedRetailerFragment();
            }
            return new GeoTaggedRetailerFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listWithGeo.clear();
        listWithoutGeo.clear();
    }
}