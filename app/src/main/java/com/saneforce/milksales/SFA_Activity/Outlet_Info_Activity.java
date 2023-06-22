package com.saneforce.milksales.SFA_Activity;

import static com.saneforce.milksales.Common_Class.Constants.DELIVERY_SEQUENCE;
import static com.saneforce.milksales.Common_Class.Constants.Retailer_OutletList;
import static com.saneforce.milksales.Common_Class.Constants.Rout_List;
import static com.saneforce.milksales.Common_Class.Constants.Route_Id;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity_Hap.AddNewRetailer;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.Interface.UpdateResponseUI;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.Outlet_Info_Adapter;
import com.saneforce.milksales.SFA_Model_Class.Retailer_Modal_List;
import com.saneforce.milksales.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Outlet_Info_Activity extends AppCompatActivity implements View.OnClickListener, Master_Interface, UpdateResponseUI {
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    TextView route_text, todayoutlets, TotalOutlets, reachedoutlets;
    List<Retailer_Modal_List> Retailer_Modal_List = new ArrayList<>();
    List<Retailer_Modal_List> Retailer_Modal_ListFilter = new ArrayList<>();
    Shared_Common_Pref sharedCommonPref;
    Common_Model Model_Pojo;
    EditText txSearchRet;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    DatabaseHandler db;
    String TAG = "OUTLET_INFO_Activity:", viewType = "-1";
    private TextView distributor_text;
    Switch swACOutlet, swOTHOutlet, swUpdOutlet, swUpdNoOutlet, swFreezerOutlet, swNoFreezerOutlet;
    int CountUR = 0, CountSR = 0, CountCls = 0;
    TextView txSrvOtlt, txUniOtlt, txClsOtlt, txAllOtlt, txSrvOtltCnt, txUniOtltCnt, txClsOtltCnt, tvApprovalSta;
    LinearLayout btSrvOtlt, btUniOtlt, btClsOtlt, undrUni, undrCls, undrServ;
    public static Outlet_Info_Activity outlet_info_activity;
    public static int retailerSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_outlet__info_);
            outlet_info_activity = this;
            db = new DatabaseHandler(this);
            common_class = new Common_Class(this);
            common_class.getDataFromApi(Constants.Todaydayplanresult, this, false);
            sharedCommonPref = new Shared_Common_Pref(Outlet_Info_Activity.this);
            recyclerView = findViewById(R.id.outletrecyclerview);
            route_text = findViewById(R.id.route_text);
            reachedoutlets = findViewById(R.id.reachedoutlets);
            todayoutlets = findViewById(R.id.todayoutlets);
            TotalOutlets = findViewById(R.id.TotalOutlets);
            distributor_text = findViewById(R.id.distributor_text);

            txSrvOtlt = findViewById(R.id.txSrvOtlt);
            txUniOtlt = findViewById(R.id.txUniOtlt);
            txClsOtlt = findViewById(R.id.txClsOtlt);
            txSrvOtltCnt = findViewById(R.id.txSrvOtltCnt);
            txUniOtltCnt = findViewById(R.id.txUniOtltCnt);
            txClsOtltCnt = findViewById(R.id.txClsOtltCnt);
            btSrvOtlt = findViewById(R.id.btSrvOtlt);
            btUniOtlt = findViewById(R.id.btUniOtlt);
            btClsOtlt = findViewById(R.id.btClsOtlt);

            txAllOtlt = findViewById(R.id.txAllOtlt);

            undrServ = findViewById(R.id.undrServ);
            undrUni = findViewById(R.id.undrUni);
            undrCls = findViewById(R.id.undrCls);

            txSearchRet = findViewById(R.id.txSearchRet);
            swACOutlet = findViewById(R.id.swACOutlet);
            swOTHOutlet = findViewById(R.id.swOTHOutlet);

            swUpdOutlet = findViewById(R.id.swUpdOutlet);
            swUpdNoOutlet = findViewById(R.id.swUpdNoOutlet);
            tvApprovalSta = findViewById(R.id.tvApprovSta);
            swFreezerOutlet = findViewById(R.id.swFreezerOutlet);
            swNoFreezerOutlet = findViewById(R.id.swNofreezerOutlet);

            tvApprovalSta.setVisibility(View.GONE);
            tvApprovalSta.setOnClickListener(this);

            route_text.setOnClickListener(this);
            reachedoutlets.setOnClickListener(this);
            distributor_text.setOnClickListener(this);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            gson = new Gson();
            userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
            }.getType();


            ImageView backView = findViewById(R.id.imag_back);
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
            }.getType();
            reloadData();
            // GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "2");
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
                    reloadData();
                }
            });

            swUpdOutlet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swUpdNoOutlet.setChecked(false);
                }
            });
            swUpdOutlet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    reloadData();
                }
            });
            swUpdNoOutlet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swUpdOutlet.setChecked(false);
                }
            });
            swUpdNoOutlet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    reloadData();
                }
            });
            swACOutlet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swOTHOutlet.setChecked(false);
                }
            });
            swACOutlet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    reloadData();
                }
            });
            swOTHOutlet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swACOutlet.setChecked(false);
                }
            });
            swOTHOutlet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    reloadData();
                }
            });


            swFreezerOutlet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swNoFreezerOutlet.setChecked(false);
                }
            });
            swFreezerOutlet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    reloadData();
                }
            });
            swNoFreezerOutlet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swFreezerOutlet.setChecked(false);
                }
            });
            swNoFreezerOutlet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    reloadData();
                }
            });


            btSrvOtlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewType = "1";
                    txSrvOtlt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txSrvOtlt.setTypeface(null, Typeface.BOLD);

                    undrServ.setVisibility(View.VISIBLE);
                    undrUni.setVisibility(View.INVISIBLE);
                    undrCls.setVisibility(View.INVISIBLE);

                    txUniOtlt.setTypeface(null, Typeface.NORMAL);
                    txUniOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                    txClsOtlt.setTypeface(null, Typeface.NORMAL);
                    txClsOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                    txAllOtlt.setTypeface(null, Typeface.NORMAL);
                    txAllOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                    reloadData();
                    // SearchRetailers();
                }
            });
            btUniOtlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewType = "0";
                    txUniOtlt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txUniOtlt.setTypeface(null, Typeface.BOLD);
                    undrUni.setVisibility(View.VISIBLE);
                    undrServ.setVisibility(View.INVISIBLE);
                    undrCls.setVisibility(View.INVISIBLE);
                    txSrvOtlt.setTypeface(null, Typeface.NORMAL);
                    txSrvOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                    txClsOtlt.setTypeface(null, Typeface.NORMAL);
                    txClsOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                    txAllOtlt.setTypeface(null, Typeface.NORMAL);
                    txAllOtlt.setTextColor(getResources().getColor(R.color.grey_900));

                    reloadData();
                }
            });
            btClsOtlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewType = "2";
                    txClsOtlt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txClsOtlt.setTypeface(null, Typeface.BOLD);
                    undrCls.setVisibility(View.VISIBLE);
                    undrUni.setVisibility(View.INVISIBLE);
                    undrServ.setVisibility(View.INVISIBLE);

                    txSrvOtlt.setTypeface(null, Typeface.NORMAL);
                    txSrvOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                    txUniOtlt.setTypeface(null, Typeface.NORMAL);
                    txUniOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                    txAllOtlt.setTypeface(null, Typeface.NORMAL);
                    txAllOtlt.setTextColor(getResources().getColor(R.color.grey_900));

                    reloadData();
                }
            });

            txAllOtlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewType = "-1";
                    txAllOtlt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txAllOtlt.setTypeface(null, Typeface.BOLD);
                    undrCls.setVisibility(View.INVISIBLE);
                    undrUni.setVisibility(View.INVISIBLE);
                    undrServ.setVisibility(View.INVISIBLE);

                    txSrvOtlt.setTypeface(null, Typeface.NORMAL);
                    txSrvOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                    txUniOtlt.setTypeface(null, Typeface.NORMAL);
                    txUniOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                    txClsOtlt.setTypeface(null, Typeface.NORMAL);
                    txClsOtlt.setTextColor(getResources().getColor(R.color.grey_900));

                    reloadData();
                }
            });

            txAllOtlt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            txAllOtlt.setTypeface(null, Typeface.BOLD);
            undrCls.setVisibility(View.INVISIBLE);
            undrUni.setVisibility(View.INVISIBLE);
            undrServ.setVisibility(View.INVISIBLE);

            txSrvOtlt.setTypeface(null, Typeface.NORMAL);
            txSrvOtlt.setTextColor(getResources().getColor(R.color.grey_900));
            txUniOtlt.setTypeface(null, Typeface.NORMAL);
            txUniOtlt.setTextColor(getResources().getColor(R.color.grey_900));
            txClsOtlt.setTypeface(null, Typeface.NORMAL);
            txClsOtlt.setTextColor(getResources().getColor(R.color.grey_900));
            if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
                distributor_text.setEnabled(false);
                findViewById(R.id.ivDistSpinner).setVisibility(View.GONE);
            }


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void reloadData() {
        try {
            Retailer_Modal_ListFilter.clear();
            if (sharedCommonPref.getvalue(Constants.Distributor_Id).equals("")) {
                Toast.makeText(this, "Select Franchise", Toast.LENGTH_SHORT).show();
                return;
            }
            String OrdersTable = sharedCommonPref.getvalue(Constants.Retailer_OutletList);
            Retailer_Modal_List = gson.fromJson(OrdersTable, userType);
            String routeId = sharedCommonPref.getvalue(Route_Id);


            CountUR = 0;
            CountSR = 0;
            CountCls = 0;
            if (Retailer_Modal_List != null) {
                retailerSize = Retailer_Modal_List.size();

                for (int sr = 0; sr < Retailer_Modal_List.size(); sr++) {
                    String itmname = Retailer_Modal_List.get(sr).getName().toUpperCase();
                    String sSchText = txSearchRet.getText().toString().toUpperCase();
                    boolean UpdTrue = false;
                    if (swUpdOutlet.isChecked()) {
                        if (Retailer_Modal_List.get(sr).getLastUpdt_Date() != null && !Retailer_Modal_List.get(sr).getLastUpdt_Date().equalsIgnoreCase(""))
                            UpdTrue = true;
                    } else if (swUpdNoOutlet.isChecked()) {
                        if (!(Retailer_Modal_List.get(sr).getLastUpdt_Date() != null && !Retailer_Modal_List.get(sr).getLastUpdt_Date().equalsIgnoreCase("")))
                            UpdTrue = true;
                    } else {
                        UpdTrue = true;
                    }
                    boolean ACTrue = false;
                    if (swACOutlet.isChecked()) {
                        if (Retailer_Modal_List.get(sr).getDelivType() != null && Retailer_Modal_List.get(sr).getDelivType().equalsIgnoreCase("AC"))
                            ACTrue = true;
                    } else if (swOTHOutlet.isChecked()) {
                        if (!(Retailer_Modal_List.get(sr).getDelivType() != null && Retailer_Modal_List.get(sr).getDelivType().equalsIgnoreCase("AC")))
                            ACTrue = true;
                    } else {
                        ACTrue = true;
                    }

                    boolean FreezerTrue = false;

                    Log.v("freezer:", "" + swFreezerOutlet.isChecked() + " :nofree:" + swNoFreezerOutlet.isChecked());
                    if (swFreezerOutlet.isChecked()) {
                        if (Retailer_Modal_List.get(sr).getFreezer_required() != null && Retailer_Modal_List.get(sr).getFreezer_required().equalsIgnoreCase("Yes"))
                            FreezerTrue = true;
                    } else if (swNoFreezerOutlet.isChecked()) {
                        if ((Retailer_Modal_List.get(sr).getFreezer_required() != null && Retailer_Modal_List.get(sr).getFreezer_required().equalsIgnoreCase("No")))
                            FreezerTrue = true;
                    } else {
                        FreezerTrue = true;
                    }


                    boolean FiltrType = false;
                    String outletType = Retailer_Modal_List.get(sr).getType() == null ? "0" : Retailer_Modal_List.get(sr).getType();
                    if (viewType.equalsIgnoreCase("-1"))
                        FiltrType = true;
                    else if (outletType.equalsIgnoreCase(viewType))
                        FiltrType = true;

                    if (UpdTrue && ACTrue && FiltrType && FreezerTrue && ((";" + itmname).indexOf(";" + sSchText) > -1 && (routeId.equals("") || (Retailer_Modal_List.get(sr).getTownCode().equals(routeId))))) {
                        Retailer_Modal_ListFilter.add(Retailer_Modal_List.get(sr));
                    }
                    if (UpdTrue && ACTrue && FreezerTrue && ((";" + itmname).indexOf(";" + sSchText) > -1 && (routeId.equals("") || (Retailer_Modal_List.get(sr).getTownCode().equals(routeId))))) {
                        if (Retailer_Modal_List.get(sr).getType() == null)
                            Retailer_Modal_List.get(sr).setType("0");
                        if (Retailer_Modal_List.get(sr).getType().equalsIgnoreCase("0")) CountUR++;
                        if (Retailer_Modal_List.get(sr).getType().equalsIgnoreCase("1")) CountSR++;
                        if (Retailer_Modal_List.get(sr).getType().equalsIgnoreCase("2")) CountCls++;
                    }
                }
            }
            TotalOutlets.setText(String.valueOf(Retailer_Modal_ListFilter.size()));
            txUniOtltCnt.setText(String.valueOf(CountUR));
            txSrvOtltCnt.setText(String.valueOf(CountSR));
            txClsOtltCnt.setText(String.valueOf(CountCls));

            if (Retailer_Modal_ListFilter != null) {

                recyclerView.setAdapter(new Outlet_Info_Adapter(Retailer_Modal_ListFilter, R.layout.outlet_info_recyclerview, this, "Outlets", new AdapterOnClick() {
                    @Override
                    public void onIntentClick(int position) {
                        try {
                            Intent intent = new Intent(getApplicationContext(), AddNewRetailer.class);
                            Shared_Common_Pref.Outlet_Info_Flag = "1";
                            Shared_Common_Pref.Editoutletflag = "1";
                            Shared_Common_Pref.Outler_AddFlag = "0";
                            Shared_Common_Pref.FromActivity = "Outlets";
                            Shared_Common_Pref.OutletCode = String.valueOf(Retailer_Modal_ListFilter.get(position).getId());
                            intent.putExtra("OutletCode", String.valueOf(Retailer_Modal_ListFilter.get(position).getId()));
                            intent.putExtra("OutletName", Retailer_Modal_ListFilter.get(position).getName());
                            intent.putExtra("OutletAddress", Retailer_Modal_ListFilter.get(position).getListedDrAddress1());
                            intent.putExtra("OutletMobile", Retailer_Modal_ListFilter.get(position).getPrimary_No());
                            intent.putExtra("OutletRoute", Retailer_Modal_ListFilter.get(position).getTownName());

                            startActivity(intent);
                            // finish();
                        } catch (Exception e) {

                        }

                    }
                }));

            }
        } catch (Exception e) {
        }
    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        try {
            common_class.dismissCommonDialog(type);
            if (type == 2) {
                route_text.setText("");
                sharedCommonPref.save(Constants.Route_Id, "");
                distributor_text.setText(myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Distributor_name, myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Distributor_Id, myDataset.get(position).getId());
                sharedCommonPref.save(Constants.DistributorERP, myDataset.get(position).getCont());
                sharedCommonPref.save(Constants.TEMP_DISTRIBUTOR_ID, myDataset.get(position).getId());
                sharedCommonPref.save(Constants.Distributor_phone, myDataset.get(position).getPhone());
                sharedCommonPref.save(Constants.CusSubGrpErp, myDataset.get(position).getCusSubGrpErp());

                findViewById(R.id.btnCmbRoute).setVisibility(View.VISIBLE);
                common_class.getDataFromApi(Retailer_OutletList, this, false);
                common_class.getDb_310Data(Rout_List, this);
                sharedCommonPref.save(Constants.DivERP, myDataset.get(position).getDivERP());

            } else if (type == 3) {
                route_text.setText(myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Route_name, myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Route_Id, myDataset.get(position).getId());
                reloadData();
            }
        } catch (Exception e) {

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
            case R.id.tvApprovSta:
                common_class.CommonIntentwithoutFinish(OutletApprovListActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.reachedoutlets:
                common_class.CommonIntentwithoutFinish(Nearby_Outlets.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.route_text:
                if (FRoute_Master != null && FRoute_Master.size() > 1)
                    common_class.showCommonDialog(FRoute_Master, 3, this);

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

                    case DELIVERY_SEQUENCE:
                        JSONObject obj = new JSONObject(apiDataResponse);
                        common_class.showMsg(this, obj.getString("Msg"));
                        if (obj.getBoolean("success"))
                            common_class.getDataFromApi(Retailer_OutletList, this, false);
                        break;
                    case Retailer_OutletList:
                        reloadData();
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

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }

    public void submitSeqNo(int sequence, String outletId) {
        JsonObject data = new JsonObject();
        data.addProperty("RetailerID", outletId);
        data.addProperty("SlNo", String.valueOf(sequence));
        common_class.getDb_310Data(Constants.DELIVERY_SEQUENCE, this, data);
    }
}
