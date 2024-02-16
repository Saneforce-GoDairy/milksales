package com.saneforce.godairy.SFA_Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.ActivityRetailerGeoTaggingBinding;
import com.saneforce.godairy.fragments.GeoTaggedRetailerFragment;
import com.saneforce.godairy.fragments.NotGeoTaggedRetailerFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetailerGeoTaggingActivity extends AppCompatActivity implements Master_Interface {
    ActivityRetailerGeoTaggingBinding binding;
    AssistantClass assistantClass;
    Common_Class common_class;
    Context context = this;

    String title = "OUTLET GEO TAG INFO", distributorID = "", distributorTITLE = "";
    public static int version = 0;
    public static double radius = 0;
    public static boolean isUpdated = false;
    JSONArray array, listWithGeo, listWithoutGeo;
    TaggedOutletsListener listener1;
    NotTaggedOutletsListener listener2;

    public void setListener1(TaggedOutletsListener listener1) {
        this.listener1 = listener1;
    }
    public void setListener2(NotTaggedOutletsListener listener2) {
        this.listener2 = listener2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRetailerGeoTaggingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        assistantClass = new AssistantClass(context);
        common_class = new Common_Class(this);
        array = new JSONArray();
        listWithGeo = new JSONArray();
        listWithoutGeo = new JSONArray();
        version = 0;
        isUpdated = false;

        binding.toolbar.title.setText(title);
        binding.toolbar.back.setOnClickListener(view -> onBackPressed());
        binding.toolbar.home.setVisibility(View.GONE);
        binding.toolbar.dropDown.setVisibility(View.VISIBLE);
        binding.toolbar.dropDown.setHint("Select Distributor");

        binding.toolbar.dropDown.setOnClickListener(view -> common_class.showCommonDialog(common_class.getDistList(), 2, this));

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
    }

    private void getOutletGeoTagInfo() {
        assistantClass.showProgressDialog("Fetching outlets...", false);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "getOutletGeoTagInfo");
        params.put("stockistCode", distributorID);
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                assistantClass.dismissProgressDialog();
                array = jsonObject.optJSONArray("response");
                radius = jsonObject.optDouble("radius");
                assignResult();
            }

            @Override
            public void onFailure(String error) {
                assistantClass.dismissProgressDialog();
                array = new JSONArray();
                assignResult();
            }
        });
    }

    private void assignResult() {
        if (array == null) {
            array = new JSONArray();
        }
        listWithGeo = new JSONArray();
        listWithoutGeo = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            String lat = array.optJSONObject(i).optString("lat");
            String lng = array.optJSONObject(i).optString("lng");
            if (lat.trim().isEmpty() || lng.trim().isEmpty() || lat.trim().equals("0") || lng.trim().equals("0")) {
                listWithoutGeo.put(array.optJSONObject(i));
            } else {
                listWithGeo.put(array.optJSONObject(i));
            }
        }
        assistantClass.saveToLocal("listWithGeo", listWithGeo.toString());
        assistantClass.saveToLocal("listWithoutGeo", listWithoutGeo.toString());
        version ++;
        if (listener1 != null) {
            listener1.onLoadTaggedOutlets();
        }
        if (listener2 != null) {
            listener2.onLoadNotTaggedOutlets();
        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        if (type == 2) {
            distributorID = myDataset.get(position).getId();
            distributorTITLE = myDataset.get(position).getName();
            binding.toolbar.dropDown.setText(distributorTITLE);
            getOutletGeoTagInfo();
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

    public interface TaggedOutletsListener {
        default void onLoadTaggedOutlets() {};
    }

    public interface NotTaggedOutletsListener {
        default void onLoadNotTaggedOutlets() {};
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isUpdated) {
            getOutletGeoTagInfo();
            isUpdated = false;
        }
    }
}