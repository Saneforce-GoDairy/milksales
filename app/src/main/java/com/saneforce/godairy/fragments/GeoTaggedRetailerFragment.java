package com.saneforce.godairy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.SFA_Activity.RetailerGeoTaggingActivity;
import com.saneforce.godairy.SFA_Adapter.OutletGeoTagInfoAdapter;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.FragmentGeoTaggedRetailerBinding;

public class GeoTaggedRetailerFragment extends Fragment implements UpdateResponseUI {
    FragmentGeoTaggedRetailerBinding binding;
    AssistantClass assistantClass;
    OutletGeoTagInfoAdapter adapter;

    public GeoTaggedRetailerFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGeoTaggedRetailerBinding.inflate(inflater, container, false);
        assistantClass = new AssistantClass(getContext());
        return binding.getRoot();
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        adapter = new OutletGeoTagInfoAdapter(getContext(), RetailerGeoTaggingActivity.listWithGeo);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RetailerGeoTaggingActivity) {
            ((RetailerGeoTaggingActivity) context).setListener(this);
        }
    }
}