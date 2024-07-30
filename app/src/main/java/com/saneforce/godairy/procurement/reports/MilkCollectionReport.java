package com.saneforce.godairy.procurement.reports;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityMilkCollectionReportBinding;
import com.saneforce.godairy.procurement.AgentCreatActivity;
import com.saneforce.godairy.procurement.MilkCollEntryActivity;
import com.saneforce.godairy.procurement.adapter.MilkCollListAdapter;
import com.saneforce.godairy.procurement.reports.model.Farmer;
import com.saneforce.godairy.procurement.reports.model.MilkCollection;

import java.util.ArrayList;
import java.util.List;

public class MilkCollectionReport extends AppCompatActivity {
    private ActivityMilkCollectionReportBinding binding;
    private final Context context = this;
    private List<MilkCollection> milkCollectionList;
    private MilkCollListAdapter adapter;


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
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                // adding matched item to the filtered list
                filteredlist.add(item);
            }
        }

        if (filteredlist.isEmpty()) {
            // displaying a toast message if no data found
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // passing the filtered list to the adapter class
            adapter.filterList(filteredlist);
        }
    }
}