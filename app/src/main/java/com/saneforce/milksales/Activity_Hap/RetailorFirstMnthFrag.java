package com.saneforce.milksales.Activity_Hap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.saneforce.milksales.R;

public class RetailorFirstMnthFrag extends Fragment {
    String mTabName = "MARCH";
    View view;
    Context mContext;

    public RetailorFirstMnthFrag(Context context, String TabName) {
        // Required empty public constructor
        this.mContext = context;
        this.mTabName = TabName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for mContext fragment
        view = inflater.inflate(R.layout.fragment_firstmnth_info, container, false);

        Toast.makeText(getActivity(),mTabName,Toast.LENGTH_SHORT).show();

        return view;
    }


}