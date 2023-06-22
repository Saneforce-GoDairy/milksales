package com.saneforce.milksales.SFA_Activity;

import static com.saneforce.milksales.Common_Class.Constants.Retailer_OutletList;
import static com.saneforce.milksales.Common_Class.Constants.Rout_List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.Interface.UpdateResponseUI;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.Lead_Adapter;
import com.saneforce.milksales.SFA_Model_Class.Retailer_Modal_List;
import com.saneforce.milksales.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Lead_Activity extends AppCompatActivity implements View.OnClickListener, Master_Interface, UpdateResponseUI {
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    TextView route_text, todayoutlets, TotalOutlets, reachedoutlets;
    List<Retailer_Modal_List> Retailer_Modal_List = new ArrayList<>();
    List<Retailer_Modal_List> Retailer_Modal_ListFilter = new ArrayList<>();
    Shared_Common_Pref sharedCommonPref;
    Common_Model Model_Pojo;
    String Route_id;
    EditText txSearchRet;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    DatabaseHandler db;
    String TAG = "Lead_Activity:";
    private TextView distributor_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lead_);
            db = new DatabaseHandler(this);
            common_class = new Common_Class(this);
            common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);
            sharedCommonPref = new Shared_Common_Pref(Lead_Activity.this);
            recyclerView = findViewById(R.id.outletrecyclerview);
            route_text = findViewById(R.id.route_text);
            reachedoutlets = findViewById(R.id.reachedoutlets);
            todayoutlets = findViewById(R.id.todayoutlets);
            TotalOutlets = findViewById(R.id.TotalOutlets);
            distributor_text = findViewById(R.id.distributor_text);

            txSearchRet = findViewById(R.id.txSearchRet);

            route_text.setOnClickListener(this);
            reachedoutlets.setOnClickListener(this);
            distributor_text.setOnClickListener(this);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            gson = new Gson();

            ImageView backView = findViewById(R.id.imag_back);
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
            }.getType();


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);

            distributor_text.setText(sharedCommonPref.getvalue(Constants.Distributor_name));

            route_text.setText(sharedCommonPref.getvalue(Constants.Route_name));


            if (!sharedCommonPref.getvalue(Constants.Distributor_Id).equals("")) {
                findViewById(R.id.btnCmbRoute).setVisibility(View.VISIBLE);
                common_class.getDb_310Data(Rout_List, this);
            } else {
                findViewById(R.id.btnCmbRoute).setVisibility(View.GONE);
            }

            txSearchRet.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    setAdapter();
                }
            });

            setAdapter();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        if (type == 2) {
            route_text.setText("");
            sharedCommonPref.save(Constants.Route_Id, "");
            distributor_text.setText(myDataset.get(position).getName());
            sharedCommonPref.save(Constants.Distributor_name, myDataset.get(position).getName());
            sharedCommonPref.save(Constants.Distributor_Id, myDataset.get(position).getId());
            sharedCommonPref.save(Constants.TEMP_DISTRIBUTOR_ID, myDataset.get(position).getId());
            sharedCommonPref.save(Constants.Distributor_phone, myDataset.get(position).getPhone());
            findViewById(R.id.btnCmbRoute).setVisibility(View.VISIBLE);
            common_class.getDataFromApi(Retailer_OutletList, this, false);
            common_class.getDb_310Data(Constants.Rout_List, this);

        } else if (type == 3) {
            Route_id = myDataset.get(position).getId();
            route_text.setText(myDataset.get(position).getName());
            sharedCommonPref.save(Constants.Route_name, myDataset.get(position).getName());
            sharedCommonPref.save(Constants.Route_Id, myDataset.get(position).getId());
            setAdapter();

        }
    }

    public void loadroute() {

        if (FRoute_Master.size() == 1) {
            route_text.setText(FRoute_Master.get(0).getName());
            sharedCommonPref.save(Constants.Route_name, FRoute_Master.get(0).getName());
            sharedCommonPref.save(Constants.Route_Id, FRoute_Master.get(0).getId());
            findViewById(R.id.ivRouteSpinner).setVisibility(View.INVISIBLE);

        } else {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reachedoutlets:
                common_class.CommonIntentwithoutFinish(Nearby_Outlets.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.route_text:
                if (FRoute_Master != null && FRoute_Master.size() > 1) {
                    common_class.showCommonDialog(FRoute_Master, 3, this);
                }
                break;

            case R.id.distributor_text:
                common_class.showCommonDialog(common_class.getDistList(), 2, this);
                break;
        }
    }


    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {
                switch (key) {
                    case Retailer_OutletList:
                        setAdapter();
                        break;
                    case Rout_List:
                        JSONArray routeArr = new JSONArray(apiDataResponse);
                        FRoute_Master.clear();
                        for (int i = 0; i < routeArr.length(); i++) {
                            JSONObject jsonObject1 = routeArr.getJSONObject(i);
                            String id = String.valueOf(jsonObject1.optInt("id"));
                            String name = jsonObject1.optString("name");
                            String flag = jsonObject1.optString("FWFlg");
                            Model_Pojo = new Common_Model(id, name, flag);
                            Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                            FRoute_Master.add(Model_Pojo);

                        }
                        loadroute();
                        break;

                }
            }
        } catch (Exception e) {

        }

    }

    void setAdapter() {
        String OrdersTable = sharedCommonPref.getvalue(Constants.Retailer_OutletList);

        Retailer_Modal_List = gson.fromJson(OrdersTable, userType);
        if (Retailer_Modal_List != null) {
            Retailer_Modal_ListFilter.clear();
            //            int todaycount = 0;
//            for (Retailer_Modal_List lm : Retailer_Modal_List) {
//                if (lm.getLastUpdt_Date() != null && lm.getLastUpdt_Date().equals(Common_Class.GetDatewothouttime())) {
//                    todaycount++;
//                }
//            }
//            todayoutlets.setText("Today Outlets:" + "\t" + todaycount);

            String routeId = sharedCommonPref.getvalue(Constants.Route_Id);
            for (int sr = 0; sr < Retailer_Modal_List.size(); sr++) {
                String itmname = Retailer_Modal_List.get(sr).getName().toUpperCase();
                String sSchText = txSearchRet.getText().toString().toUpperCase();
                if ((";" + itmname).indexOf(";" + sSchText) > -1 && (routeId.equals("") || (Retailer_Modal_List.get(sr).getTownCode().equals(routeId)))) {
                    Retailer_Modal_ListFilter.add(Retailer_Modal_List.get(sr));
                }
            }
            TotalOutlets.setText(String.valueOf(Retailer_Modal_ListFilter.size()));
            recyclerView.setAdapter(new Lead_Adapter(Retailer_Modal_ListFilter, R.layout.lead_recyclerview, getApplicationContext()));

        }


    }
}
