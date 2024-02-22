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
import com.saneforce.godairy.Interface.DropdownSelectListener;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.ActivityRetailerGeoTaggingBinding;
import com.saneforce.godairy.fragments.GeoTaggedRetailerFragment;
import com.saneforce.godairy.fragments.NotGeoTaggedRetailerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetailerGeoTaggingActivity extends AppCompatActivity {
    ActivityRetailerGeoTaggingBinding binding;
    AssistantClass assistantClass;
    Common_Class common_class;
    Context context = this;

    String title = "GEO TAG INFO", distributorID = "", distributorTITLE = "", masterId = "";
    public static int modifiedVersion = 0;
    public static double radius = 0;
    public static boolean isUpdated;
    JSONArray retailerArray, stockistArray, listWithGeo, listWithoutGeo, masterArray;
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
        retailerArray = new JSONArray();
        stockistArray = new JSONArray();
        listWithGeo = new JSONArray();
        listWithoutGeo = new JSONArray();
        masterArray = new JSONArray();

        try {
            masterArray.put(new JSONObject().put("id", "1").put("title", "Customers"));
            masterArray.put(new JSONObject().put("id", "2").put("title", "Outlets"));
        } catch (JSONException ignored) { }

        modifiedVersion = 0;
        isUpdated = false;

        binding.toolbar.title.setText(title);
        binding.toolbar.back.setOnClickListener(view -> onBackPressed());
        binding.toolbar.home.setVisibility(View.GONE);
        binding.toolbar.dropDown.setVisibility(View.VISIBLE);
        binding.toolbar.dropDown.setHint("Select Distributor");

        binding.toolbar.dropDown.setOnClickListener(v -> assistantClass.showDropdown("Select List Type", masterArray, object -> {
            masterId = object.optString("id");
            binding.toolbar.dropDown.setText(object.optString("title"));
            refreshMaster();
        }));

        binding.selectCustomer.setOnClickListener(view -> assistantClass.showDropdown("Select Customer", stockistArray, new DropdownSelectListener() {
            @Override
            public void onSelect(JSONObject object) {
                distributorID = object.optString("id");
                distributorTITLE = object.optString("title");
                binding.selectCustomer.setText(distributorTITLE);
                getOutletGeoTagInfo();
            }
        }));

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

        masterId = "1";
        binding.toolbar.dropDown.setText("Customers");
        refreshMaster();
    }

    private void refreshMaster() {
        distributorID = "";
        distributorTITLE = "";
        if (masterId.equalsIgnoreCase("1")) {
            binding.masterLL.setVisibility(View.GONE);
            getStockistGeoTagInfo();
        } else {
            binding.masterLL.setVisibility(View.VISIBLE);
            if (stockistArray.length() > 0) {
                distributorID = stockistArray.optJSONObject(0).optString("id");
                distributorTITLE = stockistArray.optJSONObject(0).optString("title");
                getOutletGeoTagInfo();
            }
            binding.selectCustomer.setText(distributorTITLE);
        }
    }

    private void getOutletGeoTagInfo() {
        assistantClass.showProgressDialog("Fetching outlet list...", false);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "getOutletGeoTagInfo");
        params.put("stockistCode", distributorID);
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                assistantClass.dismissProgressDialog();
                retailerArray = jsonObject.optJSONArray("response");
                assignResult(retailerArray);
            }

            @Override
            public void onFailure(String error) {
                assistantClass.dismissProgressDialog();
                assignResult(null);
            }
        });
    }

    private void getStockistGeoTagInfo() {
        assistantClass.showProgressDialog("Fetching stockist list...", false);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "getStockistGeoTagInfo");
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                assistantClass.dismissProgressDialog();
                stockistArray = jsonObject.optJSONArray("response");
                radius = jsonObject.optDouble("radius");
                assignResult(stockistArray);
            }

            @Override
            public void onFailure(String error) {
                assistantClass.dismissProgressDialog();
                assignResult(null);
            }
        });
    }

    private void assignResult(JSONArray myArray) {
        if (myArray == null) {
            myArray = new JSONArray();
        }
        listWithGeo = new JSONArray();
        listWithoutGeo = new JSONArray();
        for (int i = 0; i < myArray.length(); i++) {
            String lat = myArray.optJSONObject(i).optString("lat");
            String lng = myArray.optJSONObject(i).optString("lng");
            if (lat.trim().isEmpty() || lng.trim().isEmpty() || lat.trim().equals("0") || lng.trim().equals("0")) {
                listWithoutGeo.put(myArray.optJSONObject(i));
            } else {
                listWithGeo.put(myArray.optJSONObject(i));
            }
        }
        assistantClass.saveToLocal("listWithGeo", listWithGeo.toString());
        assistantClass.saveToLocal("listWithoutGeo", listWithoutGeo.toString());
        modifiedVersion ++;
        if (listener1 != null) {
            listener1.onLoadTaggedList();
        }
        if (listener2 != null) {
            listener2.onLoadNotTaggedList();
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
        default void onLoadTaggedList() {};
    }

    public interface NotTaggedOutletsListener {
        default void onLoadNotTaggedList() {};
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isUpdated) {
            if (masterId.equals("1")) {
                getStockistGeoTagInfo();
            } else {
                getOutletGeoTagInfo();
            }
            isUpdated = false;
        }
    }
}