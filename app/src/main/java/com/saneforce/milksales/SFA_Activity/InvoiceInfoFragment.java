package com.saneforce.milksales.SFA_Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.HistoryInfoAdapter;
import com.saneforce.milksales.SFA_Model_Class.OutletReport_View_Modal;
import com.saneforce.milksales.SFA_Model_Class.Product_Details_Modal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InvoiceInfoFragment extends Fragment {
    String mTabName = "";
    RecyclerView recyclerView;

    Shared_Common_Pref shared_common_pref;
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();

    HistoryInfoAdapter historyInfoAdapter;
    Common_Class common_class;

    public InvoiceInfoFragment(String TabName) {
        // Required empty public constructor
        this.mTabName = TabName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.history_more_info_layout, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        shared_common_pref = new Shared_Common_Pref(getActivity());

        common_class = new Common_Class(getActivity());
        setAdapter();

        return view;
    }

    void setAdapter() {
        try {
            FilterOrderList.clear();
            String strHistory = shared_common_pref.getvalue(Constants.HistoryData);


            JSONObject invoiceObj = new JSONObject(strHistory);


            //  if (invoiceObj.getBoolean("success")) {
            JSONArray jsonArray = invoiceObj.getJSONArray("Invoice");

            if (jsonArray != null && jsonArray.length() > 0) {
                for (int pm = 0; pm < jsonArray.length(); pm++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(pm);


                    List<Product_Details_Modal> product_details_modalArrayList = new ArrayList<>();


                    if (!jsonObject1.getString("Status").equals("No Order")) {
                        JSONArray detailsArray = jsonObject1.getJSONArray("Details");


                        for (int da = 0; da < detailsArray.length(); da++) {

                            JSONObject daObj = detailsArray.getJSONObject(da);


                            product_details_modalArrayList.add(new Product_Details_Modal(daObj.getString("Product_Code"),
                                    daObj.getString("Product_Name"), "", Integer.parseInt(daObj.getString("Quantity")), ""));

                        }
                    }
                    FilterOrderList.add(new OutletReport_View_Modal(0, jsonObject1.getString("InvoiceID"), "",
                            jsonObject1.getString("ListedDr_Name"),
                            jsonObject1.getString("Date"), (jsonObject1.getDouble("Order_Value")),
                            jsonObject1.getString("Status"), product_details_modalArrayList));


                }


            }


            historyInfoAdapter = new HistoryInfoAdapter(getActivity(), FilterOrderList, R.layout.history_info_adapter_layout, 2, new AdapterOnClick() {
                @Override
                public void onIntentClick(int pos) {


                }
            });

            recyclerView.setAdapter(historyInfoAdapter);
        } catch (Exception e) {

        }
    }


}