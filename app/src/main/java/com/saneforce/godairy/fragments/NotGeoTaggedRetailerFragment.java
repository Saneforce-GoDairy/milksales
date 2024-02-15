package com.saneforce.godairy.fragments;

import static com.saneforce.godairy.SFA_Activity.RetailerGeoTaggingActivity.version;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saneforce.godairy.SFA_Activity.RetailerGeoTaggingActivity;
import com.saneforce.godairy.SFA_Adapter.OutletGeoTagInfoAdapter;
import com.saneforce.godairy.assistantClass.AssistantClass;

import org.json.JSONArray;
import org.json.JSONException;

public class NotGeoTaggedRetailerFragment extends Fragment implements RetailerGeoTaggingActivity.NotTaggedOutletsListener {
    com.saneforce.godairy.databinding.FragmentGeoTaggedRetailerBinding binding;
    AssistantClass assistantClass;
    OutletGeoTagInfoAdapter adapter;
    int myVersion = 0;

    public NotGeoTaggedRetailerFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = com.saneforce.godairy.databinding.FragmentGeoTaggedRetailerBinding.inflate(inflater, container, false);
        assistantClass = new AssistantClass(getContext());
        binding.filterET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (adapter != null) {
                    adapter.getFilter().filter(editable.toString());
                }
            }
        });
        myVersion = 0;
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RetailerGeoTaggingActivity) {
            ((RetailerGeoTaggingActivity) context).setListener2(this);
        }
    }

    @Override
    public void onLoadNotTaggedOutlets() {
        UpdateData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (version != myVersion) {
            UpdateData();
            myVersion = version;
        }
    }

    private void UpdateData() {
        JSONArray array = new JSONArray();
        binding.filterET.setText("");
        try {
            array = new JSONArray(assistantClass.getStringFromLocal("listWithoutGeo"));
        } catch (JSONException ignored) {
        }
        assistantClass.log("UpdateData (Not Tagged): " + array);
        adapter = new OutletGeoTagInfoAdapter(getContext(), array);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }
}