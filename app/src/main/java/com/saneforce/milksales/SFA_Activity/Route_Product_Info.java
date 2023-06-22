package com.saneforce.milksales.SFA_Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity_Hap.AddNewRetailer;
import com.saneforce.milksales.Activity_Hap.CustomListViewDialog;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.Category_Universe_Modal;
import com.saneforce.milksales.SFA_Model_Class.Retailer_Modal_List;
import com.saneforce.milksales.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Route_Product_Info extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    GridView Categorygrid, availablitygrid;
    Switch availswitch, cateswitch;
    EditText reason_availablity, reason_category;
    LinearLayout more_info;
    Shared_Common_Pref sharedCommonPref;
    Common_Class common_class;
    List<Category_Universe_Modal> Category_univ_Modal = new ArrayList<>();
    TextView takeorder, Nextadd, Compititorname, Editoutlet;
    List<Category_Universe_Modal> Availlistt;
    List<Category_Universe_Modal> Universelistt;
    LinearLayout reason_categoryLin;
    List<Common_Model> Compititor_List = new ArrayList<>();
    CustomListViewDialog customDialog;
    String CompIDServer = "";
    Gson gson;
    Type userType;
    List<Retailer_Modal_List> Retailer_Modal_List;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_route__product__info);
            db = new DatabaseHandler(this);
            sharedCommonPref = new Shared_Common_Pref(this);
            common_class = new Common_Class(this);
            common_class.getDataFromApi(Constants.Competitor_List, this, false);

            gson = new Gson();
            Availlistt = new ArrayList<>();
            Universelistt = new ArrayList<>();
            Categorygrid = findViewById(R.id.category);
            takeorder = findViewById(R.id.takeorder);
            reason_category = findViewById(R.id.reason_category);
            Editoutlet = findViewById(R.id.Editoutlet);
            reason_categoryLin = findViewById(R.id.reason_categoryLin);
            more_info = findViewById(R.id.more_info);
            Nextadd = findViewById(R.id.Nextadd);
            Compititorname = findViewById(R.id.Compititorname);
            availablitygrid = findViewById(R.id.availablitygrid);
            availswitch = findViewById(R.id.availswitch);
            cateswitch = findViewById(R.id.cateswitch);
            reason_availablity = findViewById(R.id.reason_availablity);
            cateswitch.setTextColor(getResources().getColor(R.color.green));
            availswitch.setTextColor(getResources().getColor(R.color.green));
            availswitch.setChecked(true);
            cateswitch.setChecked(true);
            takeorder.setOnClickListener(this);
            Editoutlet.setOnClickListener(this);
            cateswitch.setOnClickListener(this);
            Nextadd.setOnClickListener(this);
            availswitch.setOnClickListener(this);
            more_info.setOnClickListener(this);
            Compititorname.setOnClickListener(this);
            //category_universe("1");
            // category_universe("2");
            userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
            }.getType();
//            String OrdersTable = sharedCommonPref.getvalue(Shared_Common_Pref.Outlet_List);
//            String Category_List = sharedCommonPref.getvalue(Shared_Common_Pref.Category_List);
//            String Compititor_List = sharedCommonPref.getvalue(Shared_Common_Pref.Compititor_List);

            //  String OrdersTable = String.valueOf(db.getMasterData(Constants.Retailer_OutletList));
            String OrdersTable = sharedCommonPref.getvalue(Constants.Retailer_OutletList);

            String Category_List = String.valueOf(db.getMasterData(Constants.Category_List));
            String Compititor_List = String.valueOf(db.getMasterData(Constants.Competitor_List));


            Retailer_Modal_List = gson.fromJson(OrdersTable, userType);

            Log.e("CATEGORY_LIST", Category_List);
            Log.e("Compititor_List", Compititor_List);
            GetJsonData(Category_List, "1");
            GetJsonData(Compititor_List, "2");
            if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                Nextadd.setVisibility(View.VISIBLE);
                takeorder.setVisibility(View.GONE);
                Editoutlet.setVisibility(View.GONE);
            } else {
                String val = Retailer_Modal_List.get(getOutletPosition()).getCompititor_Name();
                if (!val.equals("null"))
                    Compititorname.setText("" + val);
                CompIDServer = "" + Retailer_Modal_List.get(getOutletPosition()).getCompititor_Id();
                if (Retailer_Modal_List.get(getOutletPosition()).getHatsanavail_Switch().equals("1")) {
                    availablitygrid.setVisibility(View.GONE);
                    reason_availablity.setVisibility(View.VISIBLE);
                    reason_availablity.setText("" + Retailer_Modal_List.get(getOutletPosition()).getReason_category());
                }
                Editoutlet.setVisibility(View.VISIBLE);
                Nextadd.setVisibility(View.GONE);
                takeorder.setVisibility(View.VISIBLE);
            }

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cateswitch:
                if (cateswitch.isChecked()) {
                    cateswitch.setChecked(true);
                    cateswitch.setText("YES");
                    cateswitch.setTextColor(getResources().getColor(R.color.green));
                    if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                        Categorygrid.setVisibility(View.VISIBLE);
                        reason_categoryLin.setVisibility(View.GONE);
                    }

                } else {
                    cateswitch.setChecked(false);
                    cateswitch.setText("NO");
                    cateswitch.setTextColor(getResources().getColor(R.color.color_red));
                    if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                        Categorygrid.setVisibility(View.GONE);
                        reason_categoryLin.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.availswitch:
                if (availswitch.isChecked()) {
                    availswitch.setChecked(true);
                    availswitch.setText("YES");
                    availswitch.setTextColor(getResources().getColor(R.color.green));
                    availablitygrid.setVisibility(View.VISIBLE);
                    reason_availablity.setVisibility(View.GONE);
                } else {
                    availswitch.setChecked(false);
                    availswitch.setText("NO");
                    availswitch.setTextColor(getResources().getColor(R.color.color_red));
                    availablitygrid.setVisibility(View.GONE);
                    reason_availablity.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.more_info:
                //common_class.CommonIntentwithFinish(More_Info_Activity.class);
                break;
            case R.id.Editoutlet:

                boolean checkavail = false;
                for (Common_Model fil : Compititor_List) {
                    if (fil.isSelected() == true) {
                        checkavail = true;
                    }
                }
                StringBuilder CatUniverIds = new StringBuilder();
                for (Category_Universe_Modal ListUniv : Universelistt) {
                    if (ListUniv.getColorFlag().equals("1")) {
                        CatUniverIds.append("," + ListUniv.getId());
                    }
                }
                StringBuilder AvailCats = new StringBuilder();
                for (Category_Universe_Modal ListUniv : Availlistt) {
                    if (ListUniv.getColorFlag().equals("1")) {
                        AvailCats.append("," + ListUniv.getId());
                    }
                }
                String HatsunAVailswitch = "";
                if (availswitch.isChecked()) {
                    HatsunAVailswitch = "0";
                } else {
                    HatsunAVailswitch = "1";
                }
                String categoryuniverseswitch = "";
                if (cateswitch.isChecked()) {
                    categoryuniverseswitch = "0";
                } else {
                    categoryuniverseswitch = "1";
                }
                Intent intent = new Intent(getBaseContext(), AddNewRetailer.class);
                intent.putExtra("HatsunAvailswitch", HatsunAVailswitch);
                intent.putExtra("categoryuniverseswitch", categoryuniverseswitch);
                intent.putExtra("Compititor_Id", CompIDServer);
                intent.putExtra("Compititor_Name", Compititorname.getText().toString());
                intent.putExtra("CatUniverSelectId", CatUniverIds.toString());
                intent.putExtra("AvailUniverSelectId", AvailCats.toString());
                intent.putExtra("reason_category", reason_availablity.getText().toString());
                Shared_Common_Pref.Editoutletflag = "1";
                startActivity(intent);
                break;

            case R.id.takeorder:
                common_class.CommonIntentwithoutFinish(Invoice_History.class);
                break;
            case R.id.Nextadd:
                boolean checkavaill = false;
                for (Common_Model fil : Compititor_List) {
                    if (fil.isSelected() == true) {
                        checkavaill = true;
                    }
                }
                /*if (checkavaill == false) {
                    Toast.makeText(this, "Select The Other Brand", Toast.LENGTH_SHORT).show();
                } else {*/
                StringBuilder CatUniverId = new StringBuilder();
                for (Category_Universe_Modal ListUniv : Universelistt) {
                    if (ListUniv.getColorFlag().equals("1")) {

                        Log.e("Universe_selection", ListUniv.getId());
                        CatUniverId.append("," + ListUniv.getId());
                    }
                }
                StringBuilder AvailCat = new StringBuilder();


                for (Category_Universe_Modal ListUnivv : Availlistt) {
                    if (ListUnivv.getColorFlag().equals("1")) {
                        // Log.e("Avail_selection", ListUnivv.getId());
                        AvailCat.append("," + ListUnivv.getId());
                    }
                }
                String HatsunAVailswitchs = "";
                if (availswitch.isChecked()) {
                    HatsunAVailswitchs = "0";
                } else {
                    HatsunAVailswitchs = "1";
                }
                String categoryuniverseswitchs = "";
                if (cateswitch.isChecked()) {
                    categoryuniverseswitchs = "0";
                } else {
                    categoryuniverseswitchs = "1";
                }
                Log.e("Category_Universe", CatUniverId.toString());
                Log.e("Avail_Universe", AvailCat.toString());
                Intent intents = new Intent(getBaseContext(), AddNewRetailer.class);
                intents.putExtra("Compititor_Id", CompIDServer);
                intents.putExtra("HatsunAvailswitch", HatsunAVailswitchs);
                intents.putExtra("categoryuniverseswitch", categoryuniverseswitchs);
                intents.putExtra("Compititor_Name", Compititorname.getText().toString());
                intents.putExtra("CatUniverSelectId", CatUniverId.toString());
                intents.putExtra("AvailUniverSelectId", AvailCat.toString());
                intents.putExtra("reason_category", reason_availablity.getText().toString());
                startActivity(intents);

                break;

            case R.id.Compititorname:
                customDialog = new CustomListViewDialog(Route_Product_Info.this, Compititor_List, -1);
                Window chillwindow = customDialog.getWindow();
                chillwindow.setGravity(Gravity.CENTER);
                chillwindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;

        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        if (type != -1) {
            if (type == 1) {
                Compititor_List.get(Compititor_List.indexOf(myDataset.get(position))).setSelected(true);
            } else {
                Compititor_List.get(Compititor_List.indexOf(myDataset.get(position))).setSelected(false);
            }
            StringBuilder Compname = new StringBuilder();
            StringBuilder CompId = new StringBuilder();
            for (Common_Model fil : Compititor_List) {
                if (fil.isSelected() == true) {
                    Compname.append("," + fil.getName());
                    CompId.append("," + fil.getId());
                }
            }
            Log.e("IndexofPOSITION", String.valueOf(Compititor_List.indexOf(myDataset.get(position))));
            if (Compname != null)
                Compititorname.setText("" + Compname);
            CompIDServer = CompId.toString();
            Log.e("GOOGLEPOSITION", String.valueOf(type));
        }
    }


    public class CustomAdapteravailablity extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        List<Category_Universe_Modal> Availlistt;

        public CustomAdapteravailablity(Context applicationContext, List<Category_Universe_Modal> listt) {
            this.context = applicationContext;
            Availlistt = listt;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return Availlistt.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.category_universe_gridview, null); // inflate the layout
            TextView name = (TextView) view.findViewById(R.id.textView);
            LinearLayout gridcolor = view.findViewById(R.id.gridcolor);
            name.setText(Availlistt.get(i).getName());
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("BACKROUND_COLOR", Availlistt.get(i).getColorFlag());

                    if (Availlistt.get(i).getColorFlag().equals("0")) {
                        Availlistt.get(i).setColorFlag("1");
                        gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround));
                        ShowCatUniverse(i, "1");
                    } else {
                        Availlistt.get(i).setColorFlag("0");
                        gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround_red));
                        ShowCatUniverse(i, "0");
                    }
                }
            });
            if (Availlistt.get(i).getColorFlag().equals("0")) {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround_red));
            } else if ((Availlistt.get(i).getColorFlag().equals("1"))) {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround));
            }
            return view;
        }
    }

    public class CustomCategoryUniverseAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        List<Category_Universe_Modal> Universelistt;

        public CustomCategoryUniverseAdapter(Context applicationContext, List<Category_Universe_Modal> listt) {
            this.context = applicationContext;
            this.Universelistt = listt;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return Universelistt.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.category_universe_gridview, null); // inflate the layout
            TextView name = (TextView) view.findViewById(R.id.textView);
            LinearLayout gridcolor = view.findViewById(R.id.gridcolor);
            name.setText(Universelistt.get(i).getName()); // set logo images
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("THIRUMALAI", String.valueOf(Retailer_Modal_List.get(getOutletPosition()).getCategory_Universe_Id().indexOf(Universelistt.get(i).getId())));
                    Log.e("CAT_UNIVERSE_CUrrent", Universelistt.get(i).getId());
                    Log.e("CAT_UNIVERSE", Retailer_Modal_List.get(getOutletPosition()).getCategory_Universe_Id());
                    if ((Integer.valueOf(Retailer_Modal_List.get(getOutletPosition()).getCategory_Universe_Id().indexOf(Universelistt.get(i).getId())) == -1) || Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                        if (Universelistt.get(i).getColorFlag().equals("0")) {
                            Universelistt.get(i).setColorFlag("1");
                            gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround));
                        } else {
                            Universelistt.get(i).setColorFlag("0");
                            gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround_red));
                        }
                    } else {
                        Toast.makeText(context, "You cannot deselect the category universe.!Please contact your administrator to change.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            Log.e("CAT_UNIVERSE_Outside", String.valueOf(Integer.valueOf(Retailer_Modal_List.get(getOutletPosition()).getCategory_Universe_Id().indexOf(Universelistt.get(i).getId()))));
            /*if (Universelistt.get(i).getColorFlag().equals("0")) {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround_red));
            } else if (Universelistt.get(i).getColorFlag().equals("1")) {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround));
            }*/
            if ((Universelistt.get(i).getColorFlag().equals("0") && Integer.valueOf(Retailer_Modal_List.get(getOutletPosition()).getCategory_Universe_Id().indexOf(Universelistt.get(i).getId())) == -1) || (Universelistt.get(i).getColorFlag().equals("0") && Shared_Common_Pref.Outler_AddFlag.equals("1"))) {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround_red));
            } else {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround));
            }
            return view;
        }
    }

    private void GetJsonData(String jsonResponse, String type) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                if (type.equals("1")) {
                    String Division_Code = jsonObject1.optString("Division_Code");
                    String Cat_Image = jsonObject1.optString("Cat_Image");
                    String sampleQty = jsonObject1.optString("sampleQty");
                    String colorflag = jsonObject1.optString("colorflag");
                    Availlistt.add(new Category_Universe_Modal(id, name, Division_Code, Cat_Image, sampleQty, colorflag));
                    Universelistt.add(new Category_Universe_Modal(id, name, Division_Code, Cat_Image, sampleQty, "0"));
                    //  Category_univ_Modal.add(new Category_Universe_Modal(id, name, Division_Code, Cat_Image, sampleQty, colorflag));
                } else {
                    Compititor_List.add(new Common_Model(name, id, false));

                }
            }
            if (type.equals("1")) {
                int index = 0;
                for (Category_Universe_Modal cuv : Universelistt) {
                    System.out.println("Outlet_Univ" + Shared_Common_Pref.OutletUniv);
                    System.out.println("Outlet_getId" + cuv.getId());
                    if (String.valueOf(Shared_Common_Pref.OutletUniv).indexOf(cuv.getId()) > -1) {
                        Universelistt.get(index).setColorFlag("1");


                    }
                    index++;
                }
                for (int k = 0; Universelistt.size() > k; k++) {
                    if (Universelistt.get(k).getColorFlag().equals("1")) {
                        Log.e("COLOR_FLAG_COde", Universelistt.get(k).getId());
                    }
                }

                CustomCategoryUniverseAdapter customAdapter = new CustomCategoryUniverseAdapter(getApplicationContext(), Universelistt);
                Categorygrid.setAdapter(customAdapter);
                int indexx = 0;
                for (Category_Universe_Modal cuv : Availlistt) {
                    System.out.println("Outlet_Avail" + Shared_Common_Pref.OutletAvail);
                    System.out.println("Outlet_getId" + cuv.getId());
                    if (String.valueOf(Shared_Common_Pref.OutletAvail).indexOf(cuv.getId()) > -1) {
                        Availlistt.get(indexx).setColorFlag("1");
                    }
                    indexx++;
                }
                CustomAdapteravailablity customAdapteravail = new CustomAdapteravailablity(getApplicationContext(), Availlistt);
                availablitygrid.setAdapter(customAdapteravail);
            }
            System.out.println("ThiruLIST" + String.valueOf(Compititor_List.size()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ShowCatUniverse(int position, String colorflag) {
        Log.e("AVAIL_CLOICK", String.valueOf(position));
        Universelistt.get(position).setColorFlag(colorflag);
        CustomCategoryUniverseAdapter customAdapter = new CustomCategoryUniverseAdapter(getApplicationContext(), Universelistt);
        Categorygrid.setAdapter(customAdapter);
    }

    public int getOutletPosition() {
        if (Shared_Common_Pref.Outler_AddFlag.equals("0")) {
            for (int i = 0; Retailer_Modal_List.size() > i; i++) {
                if (Retailer_Modal_List.get(i).getId().equals(Shared_Common_Pref.OutletCode)) {
                    return i;
                }
            }
        }
        return 0;
    }
}