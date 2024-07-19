package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_AGENT;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityAgentReportBinding;
import com.saneforce.godairy.procurement.AgentCreatActivity;
import com.saneforce.godairy.procurement.adapter.AgentListAdapter;
import com.saneforce.godairy.procurement.reports.model.Agent;
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

public class AgentReportActivity extends AppCompatActivity {
    private ActivityAgentReportBinding binding;
    private final Context context = this;
    private List<Agent> agentList;
    private AgentListAdapter agentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgentReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchToolbar.setTitle("Search Agent");
        binding.searchToolbar.setTitleTextColor(getResources().getColor(R.color.grey_500));
        setSupportActionBar(binding.searchToolbar);

        agentList = new ArrayList<>();
        loadList();

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
        ArrayList<Agent> filteredlist = new ArrayList<>();

        // running a for loop to compare elements
        for (Agent item : agentList) {
            // checking if the entered string matches any item of our recycler view
            if (item.getAgent_name().toLowerCase().contains(text.toLowerCase())) {
                // adding matched item to the filtered list
                filteredlist.add(item);
            }
        }

        if (filteredlist.isEmpty()) {
            // displaying a toast message if no data found
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // passing the filtered list to the adapter class
            agentListAdapter.filterList(filteredlist);
        }
    }

    private void onClick() {
        binding.fab.setOnClickListener(v -> startActivity(new Intent(context, AgentCreatActivity.class)));
        binding.fabText.setOnClickListener(v -> startActivity(new Intent(context, AgentCreatActivity.class)));
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getAgentReport(PROCUREMENT_GET_AGENT);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    binding.shimmerLayout.setVisibility(GONE);
                    String  aitList;
                    try {
                        aitList = response.body().string();

                        if (aitList.isEmpty()){
                            showError();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(aitList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords) {
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                Agent agent = new Agent();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                agent.setAgent_name(object.getString("agent_name"));
                                agent.setAgentImage(object.getString("agent_img"));

                                agent.setState(object.getString("state"));
                                agent.setDistrict(object.getString("district"));
                                agent.setTown(object.getString("town"));
                                agent.setColl_center(object.getString("coll_center"));

                                agent.setAgent_category(object.getString("ag_category"));
                                agent.setCompany(object.getString("company"));
                                agent.setAddress(object.getString("addr"));
                                agent.setPin_code(object.getString("pin_code"));

                                agent.setCity(object.getString("city"));
                                agent.setMobile(object.getString("mobile_no"));
                                agent.setIncentive(object.getString("email"));
                                agent.setCartage(object.getString("incentive_amt"));
                                agent.setEmail(object.getString("cartage_amt"));
                                agentList.add(agent);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            agentListAdapter = new AgentListAdapter(agentList, context);
                            binding.recyclerView.setAdapter(agentListAdapter);
                            agentListAdapter.notifyDataSetChanged();
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