package com.saneforce.milksales.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.saneforce.milksales.databinding.FragmentTodayBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TodayFragment extends Fragment {
    private FragmentTodayBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentTodayBinding.inflate(inflater, container, false);

       String todayDate = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date());

       binding.date.setText(todayDate);
       return binding.getRoot();
    }
}