package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.procurement.AppConstants.GET_MILK_COLL;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_FARMER;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityMilkCollectionReportBinding;
import com.saneforce.godairy.procurement.AgentCreatActivity;
import com.saneforce.godairy.procurement.MilkCollEntryActivity;
import com.saneforce.godairy.procurement.adapter.FarmerListAdapter;
import com.saneforce.godairy.procurement.adapter.MilkCollListAdapter;
import com.saneforce.godairy.procurement.adapter.MilkCollectionAdapter;
import com.saneforce.godairy.procurement.reports.model.Farmer;
import com.saneforce.godairy.procurement.reports.model.MilkCollection;

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

public class MilkCollectionReport extends AppCompatActivity {
    private ActivityMilkCollectionReportBinding binding;
    private final Context context = this;
    private List<MilkCollection> milkCollectionList;
    private MilkCollListAdapter adapter;
    private MilkCollectionAdapter milkCollectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMilkCollectionReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchToolbar.setTitle("Search Milk Collection");
        binding.searchToolbar.setTitleTextColor(getResources().getColor(R.color.grey));
        setSupportActionBar(binding.searchToolbar);

        onClick();
    }

    private void loadMilkCollection() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getMilkColl(GET_MILK_COLL);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout.setVisibility(GONE);
                    String milkCollectionList1;
                    try {
                        milkCollectionList1 = response.body().string();

                        if (milkCollectionList1.isEmpty()){
                            showError();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(milkCollectionList1);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords) {
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                MilkCollection milkCollection = new MilkCollection();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                milkCollection.setCustomerName(object.getString("customer_name"));
                                milkCollection.setCustomerNo(object.getString("customer_no"));
                                milkCollection.setDate(object.getString("date"));
                                milkCollection.setSession(object.getString("session"));
                                milkCollection.setMilkType(object.getString("milk_type"));
                                milkCollection.setNoOfCans(object.getString("cans"));
                                milkCollection.setMilkWeight(object.getString("milk_weight"));
                                milkCollection.setMilkTotalQty(object.getString("total_milk_qty"));
                                milkCollection.setMilkSampleNo(object.getString("milk_sample_no"));
                                milkCollection.setMilkFat(object.getString("fat"));
                                milkCollection.setMilkSnf(object.getString("snf"));
                                milkCollection.setMilkClr(object.getString("clr"));
                                milkCollection.setMilkRate(object.getString("milk_rate"));
                                milkCollection.setTotalAmount(object.getString("total_milk_amt"));

                                milkCollectionList.add(milkCollection);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            milkCollectionAdapter = new MilkCollectionAdapter(milkCollectionList, context);
                            binding.recyclerView.setAdapter(milkCollectionAdapter);
                            milkCollectionAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void onClick() {
        binding.fab.setOnClickListener(v -> startActivity(new Intent(context, MilkCollEntryActivity.class)));
        binding.fabText.setOnClickListener(v -> startActivity(new Intent(context, MilkCollEntryActivity.class)));
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
        ArrayList<MilkCollection> filteredlist = new ArrayList<>();

        // running a for loop to compare elements
        for (MilkCollection item : milkCollectionList) {
            // checking if the entered string matches any item of our recycler view
            if (item.getCustomerName().toLowerCase().contains(text.toLowerCase())) {
                // adding matched item to the filtered list
                filteredlist.add(item);
            }
        }

        if (filteredlist.isEmpty()) {
            // displaying a toast message if no data found
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // passing the filtered list to the adapter class
            milkCollectionAdapter.filterList(filteredlist);
        }
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

        if (milkCollectionList != null){
            milkCollectionList.clear();
        }
        milkCollectionList = new ArrayList<>();
        loadMilkCollection();
    }
}