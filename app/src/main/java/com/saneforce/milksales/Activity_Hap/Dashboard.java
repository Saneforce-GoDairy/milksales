package com.saneforce.milksales.Activity_Hap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Activity.AllowanceActivity;
import com.saneforce.milksales.Activity.AllowanceActivityTwo;
import com.saneforce.milksales.Activity.TAClaimActivity;
import com.saneforce.milksales.Common_Class.AlertDialogBox;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.HAPApp;
import com.saneforce.milksales.SFA_Activity.MapDirectionActivity;
import com.saneforce.milksales.common.DatabaseHandler;
import com.saneforce.milksales.common.SANGPSTracker;
import com.saneforce.milksales.databinding.ActivityDashboardBinding;
import com.saneforce.milksales.fragments.GateInOutFragment;
import com.saneforce.milksales.fragments.MonthlyFragment;
import com.saneforce.milksales.fragments.TodayFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    private ActivityDashboardBinding binding;
    private Context context = this;

    public static final String CheckInDetail = "CheckInDetail";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String mypreference = "mypref";
    private static String Tag = "HAP_Check-In";
    private String imageProfile = "", sSFType = "";
    private String onDuty = "", ClosingDate = "";

    private Integer ClosingKm = 0;
    private Integer type, OTFlg = 0;

    private RelativeLayout mRelApproval;
    private ImageView profilePic, btMyQR;
    private TextView lblUserName, head_quarters, lblEmail;
    private TextView approvalcount;
    LinearLayout approval;
    private Button linMyday, linCheckin, linApprovals, linRequstStaus, linReport, linOnDuty, linSFA;
    private Button linTourPlan, linExit, lin_check_in, linHolidayWorking, linReCheck, linTaClaim, linExtShift;

    private SharedPreferences.Editor editors;
    private SharedPreferences CheckInDetails, UserDetails, sharedpreferences;
    private Common_Class common_class;
    private Shared_Common_Pref shared_common_pref;
    private com.saneforce.milksales.Activity_Hap.Common_Class DT = new com.saneforce.milksales.Activity_Hap.Common_Class();
    private DatabaseHandler db;

    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view  = binding.getRoot();
        setContentView(view);

        onClick2();
        loadFragment();

        db = new DatabaseHandler(context);
        //username = findViewById(R.id.username);
        lblUserName = (TextView) findViewById(R.id.user_name);
        head_quarters = (TextView) findViewById(R.id.head_quarters);
        lblEmail = (TextView) findViewById(R.id.lblEmail);
        profilePic = findViewById(R.id.image_view_user_profile);
        linReCheck = findViewById(R.id.lin_RecheckIn);


        linCheckin = findViewById(R.id.lin_check_in);
        linMyday = findViewById(R.id.lin_myday_plan);

        linHolidayWorking = findViewById(R.id.lin_holiday_working);

        CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Get_MydayPlan(1, "check/mydayplan");
        getHapLocations();
        getHAPWorkTypes();
        shared_common_pref = new Shared_Common_Pref(context);

        type = (UserDetails.getInt("CheckCount", 0));

        common_class = new Common_Class(context);

        String eMail = UserDetails.getString("email", "");
        String sSFName = UserDetails.getString("SfName", "");
        String SFDesig = UserDetails.getString("SFDesig", "");
        sSFType = UserDetails.getString("Sf_Type", "");
        OTFlg = UserDetails.getInt("OTFlg", 0);
        String mProfileImage = UserDetails.getString("Profile", "");

        mRelApproval = findViewById(R.id.rel_app);

        imageProfile = UserDetails.getString("url", "");

        lblUserName.setText(sSFName);
        head_quarters.setText(SFDesig);
        lblEmail.setText(eMail);

        int mCount = 0;

        for (int i = 0; i<mProfileImage.length(); i++){
            if(mProfileImage.charAt(i) != ' ')
                mCount++;
        }



        /*
        if profile pic not available setImage Placeholder
              -->  else set the valid profile pic url

                Not valid url : https://lactalisindia.salesjump.in/SalesForce_Profile_Img/
                Valid url : https://lactalisindia.salesjump.in/SalesForce_Profile_Img/MGR0008_1694517133.jpg
         */
        if (mCount == 58){
            Glide.with(this.context)
                    .load(R.drawable.person_placeholder_0)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profilePic);
        }else { // Valid image url Example
            Glide.with(this.context)
                    .load(mProfileImage)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profilePic);
        }

        profilePic.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
            intent.putExtra("ImageUrl", mProfileImage);
            startActivity(intent);

        });

//        try {
//            Uri Profile = Uri.parse(shared_common_pref.getvalue(Shared_Common_Pref.Profile));
//            Glide.with(context).load(Profile).into(profilePic);
//        } catch (Exception e) {
//        }
//        Glide.with(context).load(Uri.parse((UserDetails.getString("url", "")))).into(profilePic);

        //profilePic.setImageURI(Uri.parse((UserDetails.getString("url", ""))));

        btMyQR = findViewById(R.id.myQR);
        linMyday.setVisibility(View.GONE);

        Log.e("Dashboard", "sSFType :" + sSFType);


        if (sSFType.equals("1")) {
            linMyday.setVisibility(View.VISIBLE);
            linHolidayWorking.setVisibility(View.GONE);
            linCheckin.setVisibility(View.GONE);
        }


        linRequstStaus = (findViewById(R.id.lin_request_status));
        linReport = (findViewById(R.id.lin_report));
        linOnDuty = (findViewById(R.id.lin_onduty));
        linSFA = findViewById(R.id.lin_sfa);

        linSFA.setVisibility(View.GONE);

        linOnDuty.setVisibility(View.GONE);
        if (sSFType.equals("0"))
            linOnDuty.setVisibility(View.VISIBLE);
        else {
            linSFA.setVisibility(View.VISIBLE);
            linReCheck.setVisibility(View.VISIBLE);
        }

        if (linOnDuty.getVisibility() == View.VISIBLE) {
            linCheckin.setVisibility(View.VISIBLE);
            linHolidayWorking.setVisibility(View.VISIBLE);
        } else {
            linCheckin.setVisibility(View.GONE);
        }

        approval = findViewById(R.id.approval);
        linTaClaim = (findViewById(R.id.lin_ta_claim));
        linExtShift = (findViewById(R.id.lin_extenden_shift));
        linExtShift.setVisibility(View.GONE);
        if (OTFlg == 1) linExtShift.setVisibility(View.VISIBLE);
        linTourPlan = (findViewById(R.id.lin_tour_plan));
        linTourPlan.setVisibility(View.GONE);
        if (sSFType.equals("1")) linTourPlan.setVisibility(View.VISIBLE);
        linExit = (findViewById(R.id.lin_exit));
        approvalcount = findViewById(R.id.approvalcount);

        if (UserDetails.getInt("CheckCount", 0) <= 0) {
            approval.setVisibility(View.INVISIBLE);
//            approvalcount.setVisibility(View.GONE);
        } else {
            approval.setVisibility(View.VISIBLE);
        }

        btMyQR.setOnClickListener(v -> {
            Intent intent = new Intent(context, CateenToken.class);
            startActivity(intent);
        });

        linMyday.setOnClickListener(this);
        linCheckin.setOnClickListener(this);
        linRequstStaus.setOnClickListener(this);
        linReport.setOnClickListener(this);
        linOnDuty.setOnClickListener(this);
        approval.setOnClickListener(this);
        linTaClaim.setOnClickListener(this);
        linExtShift.setOnClickListener(this);
        linTourPlan.setOnClickListener(this);
        linHolidayWorking.setOnClickListener(this);
        linExit.setOnClickListener(this);
        linReCheck.setOnClickListener(this);
        linSFA.setOnClickListener(this);
        getcountdetails();
        updateFlxlayout();

        Log.v("wrkType:",shared_common_pref.getvalue("worktype", ""));
        if (shared_common_pref.getvalue("worktype", "").equalsIgnoreCase("43")) {
            linCheckin.setVisibility(View.GONE);
            linReCheck.setVisibility(View.GONE);
        }
    }

    private void onClick2() {
        binding.linCheckIn.setOnClickListener(v -> {
            int val = UserDetails.getInt("checkRadius", 0);
            Log.v("CHECKIN:", "" + val);
            if (/*sSFType.equals("0")*/UserDetails.getInt("checkRadius", 0) == 1) {
                String[] latlongs = UserDetails.getString("HOLocation", "").split(":");
                //  String[] latlongs = "13.0299326:80.2414088".split(":");

                Intent intent = new Intent(context, MapDirectionActivity.class);
                intent.putExtra(Constants.DEST_LAT, latlongs[0]);
                intent.putExtra(Constants.DEST_LNG, latlongs[1]);
                intent.putExtra(Constants.DEST_NAME, "HOLocation");
                intent.putExtra(Constants.NEW_OUTLET, "checkin");
                startActivity(intent);
            } else {

                String ETime = CheckInDetails.getString("CINEnd", "");
                if (!ETime.equalsIgnoreCase("")) {
                    String CutOFFDt = CheckInDetails.getString("ShiftCutOff", "0");
                    String SftId = CheckInDetails.getString("Shift_Selected_Id", "0");
                    if (DT.GetCurrDateTime(context).getTime() >= DT.getDate(CutOFFDt).getTime() || SftId == "0") {
                        ETime = "";
                    }
                }
                if (!ETime.equalsIgnoreCase("")) {
                    Intent takePhoto = new Intent(context, CameraxActivity.class);
                    takePhoto.putExtra("Mode", "CIN");
                    takePhoto.putExtra("ShiftId", CheckInDetails.getString("Shift_Selected_Id", ""));
                    takePhoto.putExtra("ShiftName", CheckInDetails.getString("Shift_Name", ""));
                    takePhoto.putExtra("On_Duty_Flag", CheckInDetails.getString("On_Duty_Flag", "0"));
                    takePhoto.putExtra("On_Duty_Flag", CheckInDetails.getString("On_Duty_Flag", "0"));
                    takePhoto.putExtra("ShiftStart", CheckInDetails.getString("ShiftStart", "0"));
                    takePhoto.putExtra("ShiftEnd", CheckInDetails.getString("ShiftEnd", "0"));
                    takePhoto.putExtra("ShiftCutOff", CheckInDetails.getString("ShiftCutOff", "0"));
                    startActivity(takePhoto);
                } else {
                    Intent i = new Intent(context, Checkin.class);
                    startActivity(i);
                }

            }
        });
        binding.linMydayPlan.setOnClickListener(v -> {
            if (ClosingKm == 1) {
                Intent closingIntet = new Intent(context, AllowanceActivityTwo.class);
                closingIntet.putExtra("Cls_con", "cls");
                closingIntet.putExtra("Cls_dte", ClosingDate);
                startActivity(closingIntet);
                finish();
            } else {
                startActivity(new Intent(context, Mydayplan_Activity.class));
            }
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(binding.tabLayout.getTabAt(position)).select();
            }
        });

        binding.leaveRequestStatus.setOnClickListener(v -> startActivity(new Intent(context, Leave_Dashboard.class)));
        binding.taClaim.setOnClickListener(v -> {
            Shared_Common_Pref.TravelAllowance = 0;
            startActivity(new Intent(context, TAClaimActivity.class)); //Travel_Allowance
        });
        binding.sfa.setOnClickListener(v -> {
            startActivity(new Intent(context, SFA_Activity.class));
        });
        /*binding.canteenScan.setOnClickListener(v -> {
            Intent intent = new Intent(context, CateenToken.class);
            startActivity(intent);
        });*/

        binding.gateIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, QRCodeScanner.class);
            intent.putExtra("Name", "GateIn");
            startActivity(intent);
        });

        binding.gateOut.setOnClickListener(v -> {
            Intent intent = new Intent(this, QRCodeScanner.class);
            intent.putExtra("Name", "GateOut");
            startActivity(intent);
        });

        binding.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show();
            }
        });

//        binding.menuBar.setOnClickListener(v -> {
//            PopupMenu popup = new PopupMenu(context, binding.menuBar);
//            popup.inflate(R.menu.month_plan);
//            popup.setOnMenuItemClickListener(item -> {
//                switch (item.getItemId()) {
//                    case R.id.tour_plan:
//                        Intent intent = new Intent(context, Tp_Month_Select.class);
//                        startActivity(intent);
//                        break;
//
//                    case R.id.logout:
//                        common_class.clearLocData(Dashboard.this);
//                        shared_common_pref.clear_pref(Constants.DB_TWO_GET_MREPORTS);
//                        shared_common_pref.clear_pref(Constants.DB_TWO_GET_DYREPORTS);
//                        shared_common_pref.clear_pref(Constants.DB_TWO_GET_NOTIFY);
//                        shared_common_pref.clear_pref(Constants.LOGIN_DATA);
//                        finishAffinity();
//
//                        Intent Dashboard = new Intent(context, Login.class);
//                        startActivity(Dashboard);
//                        break;
//                }
//                return false;
//            });
//            popup.show();
//        });

        binding.pjp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Tp_Calander.class);
                startActivity(intent);
            }
        });
    }

    private void loadFragment() {
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        binding.viewPager.setAdapter(myViewPagerAdapter);
    }

    public static class MyViewPagerAdapter  extends FragmentStateAdapter {
        public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return new TodayFragment();
                case 1:
                    return new MonthlyFragment();
                case 2:
                    return new GateInOutFragment();
                default:
                    return new TodayFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(context, "There is no back action", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        Intent I;
        switch (view.getId()) {

            case R.id.lin_check_in:
                /*
                      Dashboard --> Checkin Btn --> Map --> Selfie --> Submit checkin

                 */
                   /*
                      PREVIOUS CODE - OLD DESIGN

                int val = UserDetails.getInt("checkRadius", 0);
                Log.v("CHECKIN:", "" + val);
                if (UserDetails.getInt("checkRadius", 0) == 1) {



                        String[] latlongs = UserDetails.getString("HOLocation", "").split(":");

                    Intent intent = new Intent(Dashboard.this, MapDirectionActivity.class);
                    intent.putExtra(Constants.DEST_LAT, latlongs[0]);
                    intent.putExtra(Constants.DEST_LNG, latlongs[1]);
                    intent.putExtra(Constants.DEST_NAME, "HOLocation");
                    intent.putExtra(Constants.NEW_OUTLET, "checkin");
                    startActivity(intent);
                } else {

                    String ETime = CheckInDetails.getString("CINEnd", "");
                    if (!ETime.equalsIgnoreCase("")) {
                        String CutOFFDt = CheckInDetails.getString("ShiftCutOff", "0");
                        String SftId = CheckInDetails.getString("Shift_Selected_Id", "0");
                        if (DT.GetCurrDateTime(this).getTime() >= DT.getDate(CutOFFDt).getTime() || SftId == "0") {
                            ETime = "";
                        }
                    }
                    if (!ETime.equalsIgnoreCase("")) {
                        Intent takePhoto = new Intent(this, CameraxActivity.class);
                        takePhoto.putExtra("Mode", "CIN");
                        takePhoto.putExtra("ShiftId", CheckInDetails.getString("Shift_Selected_Id", ""));
                        takePhoto.putExtra("ShiftName", CheckInDetails.getString("Shift_Name", ""));
                        takePhoto.putExtra("On_Duty_Flag", CheckInDetails.getString("On_Duty_Flag", "0"));
                        takePhoto.putExtra("ShiftStart", CheckInDetails.getString("ShiftStart", "0"));
                        takePhoto.putExtra("ShiftEnd", CheckInDetails.getString("ShiftEnd", "0"));
                        takePhoto.putExtra("ShiftCutOff", CheckInDetails.getString("ShiftCutOff", "0"));
                        startActivity(takePhoto);


                    String[] latlongs = UserDetails.getString("HOLocation", "").split(":");

                    Intent intent = new Intent(Dashboard.this, MapDirectionActivity.class);
                    intent.putExtra(Constants.DEST_LAT, latlongs[0]);
                    intent.putExtra(Constants.DEST_LNG, latlongs[1]);
                    intent.putExtra(Constants.DEST_NAME, "HOLocation");
                    intent.putExtra(Constants.NEW_OUTLET, "checkin");
                    startActivity(intent);


                } else {

                    String ETime = CheckInDetails.getString("CINEnd", "");
                    if (!ETime.equalsIgnoreCase("")) {
                        String CutOFFDt = CheckInDetails.getString("ShiftCutOff", "0");
                        String SftId = CheckInDetails.getString("Shift_Selected_Id", "0");
                        if (DT.GetCurrDateTime(this).getTime() >= DT.getDate(CutOFFDt).getTime() || SftId == "0") {
                            ETime = "";
                        }
                    }


                    if (!ETime.equalsIgnoreCase("")) {
                        Intent takePhoto = new Intent(this, CameraxActivity.class);
                        takePhoto.putExtra("Mode", "CIN");
                        takePhoto.putExtra("ShiftId", CheckInDetails.getString("Shift_Selected_Id", ""));
                        takePhoto.putExtra("ShiftName", CheckInDetails.getString("Shift_Name", ""));
                        takePhoto.putExtra("On_Duty_Flag", CheckInDetails.getString("On_Duty_Flag", "0"));
                        takePhoto.putExtra("ShiftStart", CheckInDetails.getString("ShiftStart", "0"));
                        takePhoto.putExtra("ShiftEnd", CheckInDetails.getString("ShiftEnd", "0"));
                        takePhoto.putExtra("ShiftCutOff", CheckInDetails.getString("ShiftCutOff", "0"));
                        startActivity(takePhoto);
                    } else {
                        Intent i = new Intent(this, Checkin.class);
                        startActivity(i);
                    }

                     */

                    /*
                            Select shift time. per day only once.
                            Dashboard -->My day plan Btn --> Select shift time --> Dashboard
                            if above done :-
                                   VISIBLE : Checkin Btn;
                                   GONE : My day plan Btn;
                       */

                  String[] latlongs = UserDetails.getString("HOLocation", "").split(":");
                     Intent intent = new Intent(context, MapDirectionActivity.class);
                     intent.putExtra(Constants.DEST_LAT, latlongs[0]);
                     intent.putExtra(Constants.DEST_LNG, latlongs[1]);
                     intent.putExtra(Constants.DEST_NAME, "HOLocation");
                     intent.putExtra(Constants.NEW_OUTLET, "checkin");

                     intent.putExtra("Mode", "CIN");
                     intent.putExtra("ShiftId", CheckInDetails.getString("Shift_Selected_Id", ""));
                     intent.putExtra("ShiftName", CheckInDetails.getString("Shift_Name", ""));
                     intent.putExtra("On_Duty_Flag", CheckInDetails.getString("On_Duty_Flag", "0"));
                     intent.putExtra("ShiftStart", CheckInDetails.getString("ShiftStart", "0"));
                     intent.putExtra("ShiftEnd", CheckInDetails.getString("ShiftEnd", "0"));
                     intent.putExtra("ShiftCutOff", CheckInDetails.getString("ShiftCutOff", "0"));
                  startActivity(intent);
                break;

            case R.id.lin_request_status:
                startActivity(new Intent(context, Leave_Dashboard.class));
                break;

            case R.id.lin_ta_claim:
                Shared_Common_Pref.TravelAllowance = 0;
                startActivity(new Intent(context, TAClaimActivity.class)); //Travel_Allowance
                break;

            case R.id.lin_report:
                Intent Dashboard = new Intent(context, Dashboard_Two.class);
                Dashboard.putExtra("Mode", "RPT");
                startActivity(Dashboard);
                break;

            case R.id.approval:
                Shared_Common_Pref.TravelAllowance = 1;
                startActivity(new Intent(context, Approvals.class));
                break;
            case R.id.lin_myday_plan:
                if (ClosingKm == 1) {
                    Intent closingIntet = new Intent(context, AllowanceActivityTwo.class);
                    closingIntet.putExtra("Cls_con", "cls");
                    closingIntet.putExtra("Cls_dte", ClosingDate);
                    startActivity(closingIntet);
                    finish();
                } else {
                    startActivity(new Intent(context, Mydayplan_Activity.class));
                }
                break;

            case R.id.lin_RecheckIn:
                Intent recall = new Intent(context, AllowanceActivity.class);
                recall.putExtra("Recall", "Recall");
                startActivity(recall);
                break;


            case R.id.lin_tour_plan:
                Shared_Common_Pref.Tp_Approvalflag = "0";
                startActivity(new Intent(context, Tp_Month_Select.class));

                break;
            case R.id.lin_holiday_working:
                AlertDialogBox.showDialog(context, HAPApp.Title, "Are you sure want to Check-in with Hoilday Entry", "YES", "NO", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        common_class.CommonIntentwithoutFinishputextra(Checkin.class, "Mode", "holidayentry");
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.lin_onduty:

                // startActivity(new Intent(this, On_Duty_Activity.class));
                Intent oDutyInt = new Intent(context, On_Duty_Activity.class);
                oDutyInt.putExtra("Onduty", onDuty);
                startActivity(oDutyInt);

                break;

            case R.id.lin_sfa:
                startActivity(new Intent(context, SFA_Activity.class));
                break;
            case R.id.lin_exit:
                shared_common_pref.clear_pref(Constants.LOGIN_DATA);
                SharedPreferences.Editor editor = UserDetails.edit();
                editor.putBoolean("Login", false);
                editor.apply();
                CheckInDetails.edit().clear().commit();
                Intent playIntent = new Intent(context, SANGPSTracker.class);
                stopService(playIntent);
                finishAffinity();

                break;
            case R.id.lin_extenden_shift:
                validateExtened("ValidateExtended");
                break;
            default:
                break;
        }

    }

    public void updateFlxlayout() {
        FlexboxLayout flexboxLayout = findViewById(R.id.flxlayut);
        View flxlastChild = null;
        int flg = 0;
        Log.d("TagName_FlexCount", String.valueOf(flexboxLayout.getChildCount()));
        for (int il = 0; il < flexboxLayout.getChildCount(); il++) {
            if (flexboxLayout.getChildAt(il).getVisibility() == View.VISIBLE) {
                flxlastChild = flexboxLayout.getChildAt(il);
                if (flg == 1)
                    flg = 0;
                else
                    flg = 1;
                FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) flxlastChild.getLayoutParams();
                Log.d("TagName", flxlastChild.toString() + " - " + lp.getFlexBasisPercent() + "-" + flg);
                lp.setFlexBasisPercent(0.47f);
                flxlastChild.setLayoutParams(lp);
            }
        }
        if (flg == 1) {
            FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) flxlastChild.getLayoutParams();
            lp.setFlexBasisPercent(100);
            flxlastChild.setLayoutParams(lp);
        }
    }

    private void validateExtened(String Name) {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", Name);
        QueryString.put("Sf_code", UserDetails.getString("Sfcode", ""));
        QueryString.put("Date", common_class.GetDate());
        QueryString.put("divisionCode", UserDetails.getString("Divcode", ""));
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    // Log.e("GettodayResult", "response Tp_View: " + jsonObject.getString("success"));

                    String success = jsonObject.getString("success");
                    String Msg = jsonObject.getString("msg");
                    if (!Msg.equals("")) {
                        AlertDialogBox.showDialog(context, HAPApp.Title, Msg, "OK", "", false, new AlertBox() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }

                            @Override
                            public void NegativeMethod(DialogInterface dialog, int id) {

                            }
                        });
                    } else {
                        AlertDialogBox.showDialog(context, HAPApp.Title, "Do you want to check-in with Extended Shift?", "YES", "NO", false, new AlertBox() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                common_class.CommonIntentwithoutFinishputextra(Checkin.class, "Mode", "extended");
                                /*Intent intent = new Intent(getApplicationContext(), Checkin.class);
                                Bundle extras = new Bundle();
                                extras.putString("Extended_Flag", "extended");
                                startActivity(intent);*/
                            }

                            @Override
                            public void NegativeMethod(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        // Toast.makeText(Dashboard.this, "Send To Checkin", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void getHAPWorkTypes() {

        JSONObject jParam = new JSONObject();
        try {
            jParam.put("SF", UserDetails.getString("Sfcode", ""));
            jParam.put("div", UserDetails.getString("Divcode", ""));
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            service.getDataArrayList("get/worktypes", jParam.toString()).enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    db.deleteMasterData("HAPWorkTypes");
                    db.addMasterData("HAPWorkTypes", response.body());
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getHapLocations() {
        String commonLeaveType = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> GetHAPLocation = service.GetHAPLocation(UserDetails.getString("Divcode", ""), UserDetails.getString("Sfcode", ""), commonLeaveType);
        GetHAPLocation.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                db.deleteMasterData("HAPLocations");
                db.addMasterData("HAPLocations", response.body());
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
    }

    private void Get_MydayPlan(int flag, String Name) {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", Name);
        QueryString.put("Sf_code", UserDetails.getString("Sfcode", ""));
        QueryString.put("Date", common_class.GetDate());
        QueryString.put("divisionCode", UserDetails.getString("Divcode", ""));
        QueryString.put("desig", "MGR");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, "[]");

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                    Log.v("GET_MYDAY_PLAN", jsonObject.toString());
                    Integer MotCount = Integer.valueOf(jsonObject.getString("checkMOT"));

                    ClosingKm = Integer.valueOf(jsonObject.getString("CheckEndKM"));
                    ClosingDate = jsonObject.getString("CheckEndDT");
                    /* *********  Missing KM Auto Asking ******* */
                    if (ClosingKm == 1) {
                        Intent closingIntet = new Intent(context, AllowanceActivityTwo.class);
                        closingIntet.putExtra("Cls_con", "cls");
                        closingIntet.putExtra("Cls_dte", ClosingDate);
                        startActivity(closingIntet);
                        finish();
                        return;
                    }


                    Log.v("MOT_COUNT", String.valueOf(MotCount));

//                    if (MotCount > 0)
//                        linReCheck.setVisibility(View.VISIBLE);

                    onDuty = jsonObject.getString("CheckOnduty");
                    Log.v("ONDUTY_RESPONSE", jsonObject.getString("CheckOnduty"));

                    sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                    editors = sharedpreferences.edit();
                    editors.putString("Onduty", onDuty);
                    editors.putString("ShiftDuty", jsonObject.getString("Todaycheckin_Flag"));
                    editors.commit();

                    linCheckin.setVisibility(View.VISIBLE);
                    linHolidayWorking.setVisibility(View.VISIBLE);
                    if (flag == 1 && sSFType.equals("1")) {
                        JSONArray jsoncc = jsonObject.getJSONArray("Checkdayplan");
                        if (jsoncc.length() > 0) {

                            if (jsoncc.getJSONObject(0).getInt("Cnt") < 1) {
                                Intent intent = new Intent(context, AllowanceActivity.class);
                                intent.putExtra("My_Day_Plan", "One");
                                startActivity(intent);
                            } else {
                                linMyday.setVisibility(View.GONE);

                                if (jsoncc.getJSONObject(0).getString("wtype").equalsIgnoreCase("43")) {
                                    linCheckin.setVisibility(View.GONE);
                                    linReCheck.setVisibility(View.GONE);

                                } else {
                                    linCheckin.setVisibility(View.VISIBLE);
                                }
                                linHolidayWorking.setVisibility(View.VISIBLE);
                                updateFlxlayout();
                            }
                        } else {
                            linCheckin.setVisibility(View.GONE);
                            linHolidayWorking.setVisibility(View.GONE);
                            linMyday.setVisibility(View.VISIBLE);
                            updateFlxlayout();
                        }

//                        Log.v("wrkType:",shared_common_pref.getvalue("worktype", ""));
//                        if (shared_common_pref.getvalue("worktype", "").equalsIgnoreCase("43")) {
//                            linCheckin.setVisibility(View.GONE);
//                            linReCheck.setVisibility(View.GONE);
//                        }

                    } else {
                        String success = jsonObject.getString("success");
                        String Msg = jsonObject.getString("msg");
                        if (!Msg.equals("")) {
                            AlertDialogBox.showDialog(context, HAPApp.Title, Msg, "OK", "", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {

                                }
                            });
                        } else {
                            AlertDialogBox.showDialog(context, HAPApp.Title, Msg, "YES", "NO", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    common_class.CommonIntentwithoutFinishputextra(Checkin.class, "Mode", "extended");
                                    /*Intent intent = new Intent(getApplicationContext(), Checkin.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("Extended_Flag", "extended");
                                    startActivity(intent);*/
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            // Toast.makeText(Dashboard.this, "Send To Checkin", Toast.LENGTH_SHORT).show();
                        }
                    }
                    updateFlxlayout();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("MDPError", t.getMessage());
            }
        });
    }


    public void getcountdetails() {

        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "ViewAllCount");
        QueryString.put("sfCode", UserDetails.getString("Sfcode", ""));
        QueryString.put("State_Code", UserDetails.getString("State_Code", ""));
        QueryString.put("divisionCode", UserDetails.getString("Divcode", ""));
        QueryString.put("rSF", UserDetails.getString("Sfcode", ""));
        QueryString.put("desig", "MGR");
        String commonworktype = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, commonworktype);

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                Log.e("TAG_TP_RESPONSEcount", "response Tp_View: " + new Gson().toJson(response.body()));
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    // int TC=Integer.parseInt(jsonObject.getString("leave")) + Integer.parseInt(jsonObject.getString("Permission")) + Integer.parseInt(jsonObject.getString("vwOnduty")) + Integer.parseInt(jsonObject.getString("vwmissedpunch")) + Integer.parseInt(jsonObject.getString("TountPlanCount")) + Integer.parseInt(jsonObject.getString("vwExtended"));
                    //jsonObject.getString("leave"))
                    Log.e("TOTAl_COUNT", String.valueOf(Integer.parseInt(jsonObject.getString("leave")) + Integer.parseInt(jsonObject.getString("Permission")) + Integer.parseInt(jsonObject.getString("vwOnduty")) + Integer.parseInt(jsonObject.getString("vwmissedpunch")) + Integer.parseInt(jsonObject.getString("TountPlanCount")) + Integer.parseInt(jsonObject.getString("vwExtended"))));
                    //count = count +
                    Shared_Common_Pref.TotalCountApproval = jsonObject.getInt("leave") + jsonObject.getInt("Permission") +
                            jsonObject.getInt("vwOnduty") + jsonObject.getInt("vwmissedpunch") +
                            jsonObject.getInt("vwExtended") + jsonObject.getInt("TountPlanCount") +
                            jsonObject.getInt("FlightAppr") +
                            jsonObject.getInt("HolidayCount") + jsonObject.getInt("DeviationC") +
                            jsonObject.getInt("CancelLeave") + jsonObject.getInt("ExpList");
                    approvalcount.setText(String.valueOf(Shared_Common_Pref.TotalCountApproval));
                    approvalcount.setVisibility(View.GONE);
                    if(Shared_Common_Pref.TotalCountApproval>0) approvalcount.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                common_class.ProgressdialogShow(2, "");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        if (CheckIn == true) {
            Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
            Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
            Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
            Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");

            String ActStarted = shared_common_pref.getvalue("ActivityStart");
            if (ActStarted.equalsIgnoreCase("true")) {
                Intent aIntent;
                String sDeptType = UserDetails.getString("DeptType", "");
                if (sDeptType.equalsIgnoreCase("1")) {
                    //   aIntent = new Intent(Dashboard.this, ProcurementDashboardActivity.class);
                    aIntent = (new Intent(getApplicationContext(), SFA_Activity.class));

                } else {
                    Shared_Common_Pref.Sync_Flag = "0";
                    aIntent = new Intent(context, SFA_Activity.class);
                }
                startActivity(aIntent);
                finish();
            } else {
                Intent Dashboard = new Intent(context, Dashboard_Two.class);
                Dashboard.putExtra("Mode", "CIN");
                startActivity(Dashboard);
                finish();
            }
        }
    }
}
