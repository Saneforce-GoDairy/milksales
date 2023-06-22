package com.saneforce.milksales.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saneforce.milksales.Activity.Util.SelectionModel;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;

import java.util.ArrayList;
import java.util.List;


public class AdapterForSelectionList extends BaseAdapter {
    Context context;
    ArrayList<SelectionModel> array = new ArrayList<>();
    ArrayList<SelectionModel> dummyList = new ArrayList<>();
    int type = 0;
    AdapterOnClick adapterOnClick;

    public AdapterForSelectionList(Context context, ArrayList<SelectionModel> array, int type, AdapterOnClick adapterOnClick) {
        this.context = context;
        this.array = array;
        this.type = type;
        this.dummyList.addAll(array);
        this.adapterOnClick = adapterOnClick;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_selection, parent, false);
        RelativeLayout lay_row = (RelativeLayout) view.findViewById(R.id.lay_row);
        TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
        final CheckBox check = (CheckBox) view.findViewById(R.id.check);
        check.setChecked(array.get(i).isClick());
        txt_name.setText(array.get(i).getTxt());

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (type == 0) {
                    for (int k = 0; k < array.size(); k++) {
                        if (i != k) {
                            array.get(k).setClick(false);
                        } else {

                           /* AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                            animation1.setDuration(500);
                            animation1.setStartOffset(200);
                            //animation1.setFillAfter(true);
                            check.startAnimation(animation1);*/
                            array.get(k).setClick(true);
                            check.setChecked(true);
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    if (array.size() != 0) {
                        if (array.get(i).isClick()) {
                            check.setChecked(false);
                            array.get(i).setClick(false);
                        } else {

                            check.setChecked(true);
                            /*AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                            animation1.setDuration(200);
                            animation1.setStartOffset(200);
                            //animation1.setFillAfter(true);
                            check.startAnimation(animation1);*/
                            array.get(i).setClick(true);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });

        lay_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 adapterOnClick.onIntentClick(array.get(i));

              //  onSelectItemClick.itemClick(dummyList.get(i));

                if (type == 0) {
                    for (int k = 0; k < array.size(); k++) {
                        if (i != k) {
                            array.get(k).setClick(false);
                        } else {

                           /* AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                            animation1.setDuration(500);
                            animation1.setStartOffset(200);
                            //animation1.setFillAfter(true);
                            check.startAnimation(animation1);*/
                            array.get(k).setClick(true);
                            check.setChecked(true);
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    if (array.size() != 0) {
                        if (array.get(i).isClick()) {
                            check.setChecked(false);
                            array.get(i).setClick(false);
                        } else {

                            check.setChecked(true);
                            /*AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
                            animation1.setDuration(200);
                            animation1.setStartOffset(200);
                            //animation1.setFillAfter(true);
                            check.startAnimation(animation1);*/
                            array.get(i).setClick(true);
                        }
                    }
                    notifyDataSetChanged();
                }

            }
        });
        return view;
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {


                FilterResults filterResults = new FilterResults();
                if (charSequence != null && charSequence.length() > 0) {
                    List<SelectionModel> filteredList = new ArrayList<>();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        dummyList = array;
                    } else {


                        filteredList.clear();
                        for (SelectionModel row : dummyList) {
                            if (row.getTxt().toLowerCase().contains(charString.toLowerCase()) || row.getTxt().contains(charSequence)) {
                                Log.v("lowercase_filter", row.getTxt());
                                filteredList.add(row);
                            }

                        }
                      /*  if (filteredList.size() == 0) {
                            filteredList.addAll(DrListFiltered);
                        }*/

                        //DrListFiltered = filteredList;
                    }


                    filterResults.values = filteredList;
                } else {
                    // results.count=filterList.size();
                    filterResults.values = dummyList;

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                // Log.e("frr",DrListFiltered.get(0).getmDoctorName());
                array = (ArrayList<SelectionModel>) filterResults.values;
                //row=DrListFiltered;
                notifyDataSetChanged();
            }
        };
    }


}
