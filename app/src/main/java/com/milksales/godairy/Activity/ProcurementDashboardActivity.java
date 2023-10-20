package com.milksales.godairy.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.milksales.godairy.Activity.Util.ListModel;
import com.milksales.godairy.Activity_Hap.Dashboard_Two;
import com.milksales.godairy.Interface.ApiClient;
import com.milksales.godairy.Interface.ApiInterface;
import com.milksales.godairy.Model_Class.DashboardParticulars;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Activity.HAPApp;
import com.milksales.godairy.adapters.UpcomingFollow;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcurementDashboardActivity extends AppCompatActivity {
    ApiInterface apiService;
    ArrayList<ListModel> array_nav = new ArrayList<>();
    AdapterForNavigation nav_adapt;
    ListView list_nav;
    ImageView iv_nav, iv_checkout;
    SharedPreferences share;
    SharedPreferences shareKey;
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    TextView txt_name, txt_mail, txt_head_name;
    String sf_code, div;
    ListView list_follow;
    ArrayList<DashboardParticulars> array_follow = new ArrayList<>();
    UpcomingFollow upcomingFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurement_dashboard);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        list_nav = findViewById(R.id.list_nav);
        iv_nav = findViewById(R.id.iv_nav);

        iv_checkout = findViewById(R.id.iv_checkout);
        txt_name = findViewById(R.id.txt_name);
        txt_mail = findViewById(R.id.txt_mail);
        txt_head_name = findViewById(R.id.txt_head_name);
        list_follow = findViewById(R.id.list_follow);
        share = getSharedPreferences("existing", 0);
        SharedPreferences.Editor edit = share.edit();
        edit.putString("exist", "N");
        edit.putString("fab", "1");
        edit.commit();
        shareKey = getSharedPreferences("key", 0);
        SharedPreferences.Editor edit1 = shareKey.edit();
        edit1.putString("keys", "[]");
        edit1.putString("pk", "");
        edit1.commit();
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sf_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");
        iv_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        ImageView ibtLogout = findViewById(R.id.ibtLogout);
        ibtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ProcurementDashboardActivity.this)
                        .setTitle(HAPApp.Title)
                        .setMessage(Html.fromHtml("Are you sure to <b><span style='color:#FF0000'>Stop</span></b> your Today Activity Now ?"))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent aIntent;
                                aIntent = new Intent(getApplicationContext(), Dashboard_Two.class);
                                aIntent.putExtra("Mode", "CIN");
                                startActivity(aIntent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });
        txt_name.setText(UserDetails.getString("SfName", ""));
        txt_mail.setText(UserDetails.getString("email", ""));
        txt_head_name.setText(UserDetails.getString("SfName", ""));
        iv_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTravelMode();
            }
        });

        callDashboardParticulars();
        callDynamicmenu();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "There is no back action", Toast.LENGTH_LONG).show();
    }

    public class AdapterForNavigation extends BaseAdapter {
        Context context;
        ArrayList<ListModel> arr = new ArrayList<>();

        public AdapterForNavigation(Context context, ArrayList<ListModel> arr) {
            this.context = context;
            this.arr = arr;
        }

        @Override
        public int getCount() {
            return arr.size();
        }

        @Override
        public Object getItem(int i) {
            return arr.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.row_item_dash, viewGroup, false);
            RelativeLayout lay_row = view.findViewById(R.id.lay_row);
            TextView txt_name = view.findViewById(R.id.txt_name);
            txt_name.setText(arr.get(i).getFormName());
            lay_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                        //super.onBackPressed();
                    }
                    //if(arr.get(i).getFormType().equalsIgnoreCase("V")){
                    Intent ii = new Intent(ProcurementDashboardActivity.this, ViewActivity.class);
                    ii.putExtra("btn_need", arr.get(i).getTargetForm());
                    ii.putExtra("frmid", arr.get(i).getFormid());
                    startActivity(ii);
                    //}
                }
            });
            return view;
        }
    }

    public void callDashboardParticulars() {
        JSONObject json = new JSONObject();
        try {
            json.put("div", div);

            Log.v("printing_sf_code", json.toString());
            Call<ResponseBody> approval = apiService.getDasboardParticulars(json.toString());

            approval.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track_dash", response.body().byteStream() + "");
                        JSONObject jsonObject = null;
                        String jsonData = null;

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }

                            Log.v("printing_dashboard_menu", is.toString());
                            JSONArray js = new JSONArray(is.toString());
                            for (int i = 0; i < js.length(); i++) {
                                JSONObject jj = js.getJSONObject(i);
                                array_follow.add(new DashboardParticulars(jj.getString("Sl_NO"),jj.getString("Particulars"), jj.getString("This_Month"),
                                        jj.getString("Last_Month"), jj.getString("LPercentage")));
                            }
                            upcomingFollow = new UpcomingFollow(ProcurementDashboardActivity.this, array_follow);
                            list_follow.setAdapter(upcomingFollow);

                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (Exception e) {
        }
    }

    public void callDynamicmenu() {
        JSONObject json = new JSONObject();
        try {
            json.put("div", div);

            Log.v("printing_sf_code", json.toString());
            Call<ResponseBody> approval = apiService.getMenu(json.toString());

            approval.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track", response.body().byteStream() + "");
                        JSONObject jsonObject = null;
                        String jsonData = null;

                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);

                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                            }

                            Log.v("printing_dynamic_menu", is.toString());
                            JSONArray js = new JSONArray(is.toString());
                            for (int i = 0; i < js.length(); i++) {
                                JSONObject jj = js.getJSONObject(i);
                                array_nav.add(new ListModel(jj.getString("Frm_ID"), jj.getString("Frm_Name"), jj.getString("Frm_Table"), jj.getString("Targt_Frm"), jj.getString("Frm_Type"),0));
                            }
                            nav_adapt = new AdapterForNavigation(ProcurementDashboardActivity.this, array_nav);
                            list_nav.setAdapter(nav_adapt);
                            nav_adapt.notifyDataSetChanged();
                            //commonFun();

                        } catch (Exception e) {
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (Exception e) {
        }
    }

    public void getTravelMode() {
        try {
            JSONObject jj = new JSONObject();

            jj.put("sf", sf_code);
            jj.put("div", div);
            Log.v("printing_allow", jj.toString());
            Call<ResponseBody> Callto = apiService.getTravelMode(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {

                            Log.v("print_upload_file_true", "ggg" + response);
                            JSONObject jb = null;
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("get_mode_Res", jsonData);
                            JSONArray ja = new JSONArray(jsonData);
                            if (ja.length() != 0) {
                                JSONObject js_ob = ja.getJSONObject(0);
                                if (js_ob.getString("MOT").equalsIgnoreCase("12")) {
                                    Intent i = new Intent(ProcurementDashboardActivity.this, AllowanceActivity.class);
                                    startActivity(i);
                                } else {

                                }
                            } else {

                            }
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
        }
    }
}