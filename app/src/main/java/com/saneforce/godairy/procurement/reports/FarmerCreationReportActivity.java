package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_FARMER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcFarmerCreaReport;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityFarmerCreationReportBinding;
import com.saneforce.godairy.procurement.FarmerCreationActivity;
import com.saneforce.godairy.procurement.adapter.FarmerListAdapter;
import com.saneforce.godairy.procurement.reports.model.Agent;
import com.saneforce.godairy.procurement.reports.model.Farmer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmerCreationReportActivity extends AppCompatActivity {
    private ActivityFarmerCreationReportBinding binding;
    private final Context context = this;
    private List<Farmer> farmerList;
    private FarmerListAdapter farmerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFarmerCreationReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchToolbar.setTitle("Search Farmer");
        binding.searchToolbar.setTitleTextColor(getResources().getColor(R.color.grey_500));
        setSupportActionBar(binding.searchToolbar);

        onClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_toolbar, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        // set the on query text listener for the SearchView
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // call a method to filter your RecyclerView
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        // creating a new array list to filter data
        ArrayList<Farmer> filteredlist = new ArrayList<>();

        // running a for loop to compare elements
        for (Farmer item : farmerList) {
            // checking if the entered string matches any item of our recycler view
            if (item.getFarmer_name().toLowerCase().contains(text.toLowerCase())) {
                // adding matched item to the filtered list
                filteredlist.add(item);
            }
        }

        if (filteredlist.isEmpty()) {
            // displaying a toast message if no data found
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // passing the filtered list to the adapter class
            farmerListAdapter.filterList(filteredlist);
        }
    }

    private void onClick() {
        binding.fab.setOnClickListener(v -> startActivity(new Intent(context, FarmerCreationActivity.class)));
        binding.fabText.setOnClickListener(v -> startActivity(new Intent(context, FarmerCreationActivity.class)));
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getFarmerCreationReport(PROCUREMENT_GET_FARMER);

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout.setVisibility(GONE);
                    String farmerCreationList;
                    try {
                        farmerCreationList = response.body().string();

                        if (farmerCreationList.isEmpty()){
                            showError();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(farmerCreationList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords) {
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                Farmer farmer = new Farmer();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                farmer.setId(object.getString("id"));
                                farmer.setFarmer_name(object.getString("farmer_name"));
                                farmer.setFarmer_mobile(object.getString("mobile_no"));
                                farmer.setFarmer_photo(object.getString("farmer_img"));
                                farmer.setState(object.getString("state"));
                                farmer.setDistrict(object.getString("district"));
                                farmer.setTown(object.getString("town"));
                                farmer.setColl_center(object.getString("coll_center"));
                                farmer.setFarmerCategory(object.getString("fa_category"));
                                farmer.setAddress(object.getString("addr"));
                                farmer.setPincode(object.getString("pin_code"));
                                farmer.setCity(object.getString("city"));
                                farmer.setEmail(object.getString("email"));
                                farmer.setIncentive_amt(object.getString("incentive_amt"));
                                farmer.setCartage_amt(object.getString("cartage_amt"));

                                farmerList.add(farmer);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            farmerListAdapter = new FarmerListAdapter(context, farmerList);
                            binding.recyclerView.setAdapter(farmerListAdapter);
                            farmerListAdapter.notifyDataSetChanged();
                            return;
                        }
                        binding.shimmerLayout.setVisibility(GONE);
                        binding.recyclerView.setVisibility(GONE);
                        binding.noRecords.setVisibility(View.VISIBLE);
                    } catch (IOException | JSONException e) {
                        //  throw new RuntimeException(e);
                        showError();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                showError();
            }
        });
    }

    private void showError() {
        binding.shimmerLayout.setVisibility(GONE);
        binding.recyclerView.setVisibility(GONE);
        binding.nullError.setVisibility(View.VISIBLE);
        binding.message.setText("Something went wrong!");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (farmerList != null){
            farmerList.clear();
        }
        farmerList = new ArrayList<>();
        loadList();
    }
}


        /*
       Don't delete this code.
       Farmer creation form ( 12/12/2023 )
       Works fine. Name : Prasanth SEF295
       Hide sprint 6

    public class FarmerCreationReportActivity extends AppCompatActivity {
    private ActivityFarmerCreationReportBinding binding;
    private final Context context = this;


    private List<ProcFarmerCreaReport> farmerCreaReportList;
    private FarmerCreaReportAdapter farmerCreaReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFarmerCreationReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        farmerCreaReportList = new ArrayList<>();
        loadList();

        onClick();
    }

    private void onClick() {
        binding.fab.setOnClickListener(v -> startActivity(new Intent(context, FarmerCreationActivity.class)));
        binding.fabText.setOnClickListener(v -> startActivity(new Intent(context, FarmerCreationActivity.class)));
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getFarmerCreationReport(PROCUREMENT_GET_FARMER_CREATION_REPORT);

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout.setVisibility(GONE);
                    String farmerCreationList;
                    try {
                        farmerCreationList = response.body().string();

                        if (farmerCreationList.isEmpty()){
                            showError();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(farmerCreationList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords) {
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                ProcFarmerCreaReport farmerCreaReport = new ProcFarmerCreaReport();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                farmerCreaReport.setCenter(object.getString("center_name"));
                                farmerCreaReport.setFarmer_category(object.getString("farm_gategory"));
                                farmerCreaReport.setFarmer_name(object.getString("farmer_name"));
                                farmerCreaReport.setFarmer_img(object.getString("farmer_image"));
                                farmerCreaReport.setAddress(object.getString("farmer_addr"));
                                farmerCreaReport.setPhone_number(object.getString("phone_no"));
                                farmerCreaReport.setPin_code(object.getString("pin_code"));
                                farmerCreaReport.setNo_of_ani_cow(object.getString("cow_total"));
                                farmerCreaReport.setNo_of_ani_buffalo(object.getString("buf_total"));
                                farmerCreaReport.setMilk_avail_lttr_cow(object.getString("cow_milk_avail_ltrs"));
                                farmerCreaReport.setMilk_avail_lttr_buffalo(object.getString("buf_milk_avail_ltrs"));
                                farmerCreaReport.setMilk_supply_company(object.getString("milk_sup_company"));
                                farmerCreaReport.setInterested_for_supply(object.getString("intrstd_supply"));
                                farmerCreaReport.setCreated_dt(object.getString("created_dt"));
                                farmerCreaReportList.add(farmerCreaReport);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            farmerCreaReportAdapter = new FarmerCreaReportAdapter(context, farmerCreaReportList);
                            binding.recyclerView.setAdapter(farmerCreaReportAdapter);
                            farmerCreaReportAdapter.notifyDataSetChanged();
                            return;
                        }
                        binding.shimmerLayout.setVisibility(GONE);
                        binding.recyclerView.setVisibility(GONE);
                        binding.noRecords.setVisibility(View.VISIBLE);
                    } catch (IOException | JSONException e) {
                        //  throw new RuntimeException(e);
                        showError();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                showError();
            }
        });
    }
    private void showError() {
        binding.shimmerLayout.setVisibility(GONE);
        binding.recyclerView.setVisibility(GONE);
        binding.nullError.setVisibility(View.VISIBLE);
        binding.message.setText("Something went wrong!");
    }
}
     */