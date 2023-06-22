package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saneforce.milksales.Model_Class.DashboardParticulars;
import com.saneforce.milksales.R;

import java.util.ArrayList;

public class UpcomingFollow extends BaseAdapter {
    Context context;
    ArrayList<DashboardParticulars> array = new ArrayList<>();

    public UpcomingFollow(Context context, ArrayList<DashboardParticulars> array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.row_item_follow_up, viewGroup, false);
        TextView txt_name = view.findViewById(R.id.txt_name);
        TextView txt_this_month = view.findViewById(R.id.txt_this_month);
        TextView txt_last_month = view.findViewById(R.id.txt_last_month);
        TextView txt_percentage = view.findViewById(R.id.txt_percentage);
        DashboardParticulars mm = array.get(i);
        txt_name.setText(mm.getName());
        if(mm.getSl_NO().equals("3")){
            txt_this_month.setText("This Month :" + mm.getThisMonth());
            txt_last_month.setText("Last Month :" + mm.getLastmonth());
        }else{
            txt_this_month.setText("Target :" + mm.getThisMonth());
            txt_last_month.setText("Achieve :" + mm.getLastmonth());
        }
        txt_percentage.setText(mm.getPercentage() + "%");
        return view;
    }
}
