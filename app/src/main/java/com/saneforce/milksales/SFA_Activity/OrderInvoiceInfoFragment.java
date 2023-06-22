package com.saneforce.milksales.SFA_Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.HistoryOrderInvInfoAdapter;
import com.saneforce.milksales.SFA_Model_Class.OutletReport_View_Modal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderInvoiceInfoFragment extends Fragment {
    String mTabName = "";
    RecyclerView recyclerView;

    HistoryOrderInvInfoAdapter historyInfoAdapter;
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();

    Shared_Common_Pref shared_common_pref;

    public OrderInvoiceInfoFragment(String TabName) {
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
        view.findViewById(R.id.llOrderInvHeader).setVisibility(View.VISIBLE);

        shared_common_pref = new Shared_Common_Pref(getActivity());


        setAdapter();
        return view;
    }

    void setAdapter() {
        try {
            FilterOrderList.clear();
            String strHistory = shared_common_pref.getvalue(Constants.HistoryData);


            JSONObject ordInvObj = new JSONObject(strHistory);


            //if (ordInvObj.getBoolean("success")) {
            JSONArray jsonArray = ordInvObj.getJSONArray("Orders");

            if (jsonArray != null && jsonArray.length() > 0) {
                for (int pm = 0; pm < jsonArray.length(); pm++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(pm);

                    String items = "";

                    if (!jsonObject1.getString("Status").equals("No Order")) {
                        JSONArray detailsArray = jsonObject1.getJSONArray("Details");


                        for (int da = 0; da < detailsArray.length(); da++) {

                            JSONObject daObj = detailsArray.getJSONObject(da);

                            items = items + (daObj.getString("Product_Name")) + " x " + daObj.getString("Quantity") + ",";

                        }

                        if (!Common_Class.isNullOrEmpty(items))
                            items += items.substring(0, items.length() - 1);
                        FilterOrderList.add(new OutletReport_View_Modal(items, jsonObject1.getString("OrderID"), "",
                                jsonObject1.getString("OutletName"),
                                jsonObject1.getString("Date"), (jsonObject1.getDouble("Order_Value")),
                                jsonObject1.getString("Status")));


                    }
                }

            }


            for (int i = 0; i < FilterOrderList.size(); i++) {

                JSONArray jsonInvArray = ordInvObj.getJSONArray("Invoice");

                if (jsonInvArray != null && jsonInvArray.length() > 0) {
                    boolean isHave = false;
                    for (int pm = 0; pm < jsonInvArray.length(); pm++) {
                        JSONObject jsonObject1 = jsonInvArray.getJSONObject(pm);


                        if (FilterOrderList.get(i).getTransSlNo().equals(jsonObject1.getString("OrderID"))) {
                            isHave = true;
                            FilterOrderList.get(i).setInvoiceID(jsonObject1.getString("InvoiceID"));
                            FilterOrderList.get(i).setInvoiceDate(jsonObject1.getString("Date"));
                            FilterOrderList.get(i).setInvoiceStatus(jsonObject1.getString("Status"));
                            FilterOrderList.get(i).setInvoiceAmount(jsonObject1.getString("Order_Value"));

                            JSONArray detailsArray = jsonObject1.getJSONArray("Details");

                            String items = "";
                            for (int da = 0; da < detailsArray.length(); da++) {

                                JSONObject daObj = detailsArray.getJSONObject(da);

                                items = items + (daObj.getString("Product_Name")) + " x " + daObj.getString("Quantity") + ",";

                            }
                            if (!Common_Class.isNullOrEmpty(items))
                                items += items.substring(0, items.length() - 1);

                            FilterOrderList.get(i).setInvoiceItems(items);

                            Log.v("OrderInvValues: ", String.valueOf(FilterOrderList.get(i)));

                        }
                    }

                    if (!isHave) {
                        FilterOrderList.get(i).setInvoiceID("");
                        FilterOrderList.get(i).setInvoiceDate("");
                        FilterOrderList.get(i).setInvoiceStatus("");
                        FilterOrderList.get(i).setInvoiceAmount("");
                        FilterOrderList.get(i).setInvoiceItems("");

                    }


                }
            }
            //  }

            historyInfoAdapter = new HistoryOrderInvInfoAdapter(getActivity(), FilterOrderList, R.layout.history_orderinvoice_adapter_layout, 3);

            recyclerView.setAdapter(historyInfoAdapter);
        } catch (Exception e) {

            Log.e("OrderInvFrag: ", e.getMessage());

        }
    }

}