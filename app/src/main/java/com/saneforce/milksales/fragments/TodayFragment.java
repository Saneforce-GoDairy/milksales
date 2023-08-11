package com.saneforce.milksales.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.saneforce.milksales.databinding.FragmentTodayBinding;

public class TodayFragment extends Fragment {
    private FragmentTodayBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentTodayBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}