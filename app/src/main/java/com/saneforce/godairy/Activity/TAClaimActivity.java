package com.saneforce.godairy.Activity;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Activity.Util.ImageFilePath;
import com.saneforce.godairy.Activity.Util.SelectionModel;
import com.saneforce.godairy.Activity_Hap.AllowancCapture;
import com.saneforce.godairy.Activity_Hap.AttachementActivity;
import com.saneforce.godairy.Activity_Hap.CustomListViewDialog;
import com.saneforce.godairy.Activity_Hap.Dashboard;
import com.saneforce.godairy.Activity_Hap.Dashboard_Two;
import com.saneforce.godairy.Activity_Hap.MapZoomIn;
import com.saneforce.godairy.Activity_Hap.TaFuelEdit;
import com.saneforce.godairy.Activity_Hap.ViewTAStatus;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.CameraPermission;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.CtrlsListModel;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Common_Class.Util;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.DistanceMeterWatcher;
import com.saneforce.godairy.Interface.LocationEvents;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Interface.OnAttachmentDelete;
import com.saneforce.godairy.Interface.OnImagePickListener;
import com.saneforce.godairy.Model_Class.ModeOfTravel;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.adapters.FuelListAdapter;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.common.DatabaseHandler;
import com.saneforce.godairy.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TAClaimActivity extends AppCompatActivity implements Master_Interface,
        OnMapReadyCallback {
    public static final String mypreference = "mypref";
    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";
    public static final String SKM = "Started_km";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String CheckInfo = "CheckInDetail";
    String lodgingAllowType = "";
    static Util util;
    static TransferUtility transferUtility;
    final Handler handler = new Handler();
    private final OnBackPressedDispatcher mOnBackPressedDispatcher = new OnBackPressedDispatcher(new Runnable() {
        @Override
        public void run() {
            TAClaimActivity.super.onBackPressed();
        }
    });
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    Shared_Common_Pref mShared_common_pref;
    SharedPreferences TaSharedPrefernce;
    Common_Model mCommon_model_spinner;
    Common_Model Model_Pojo;
    Common_Class mCommon_class;
    GoogleMap mGoogleMap;
    ApiInterface apiInterface;
    AssistantClass assistantClass;
    DatabaseHandler db;
    Uri outputFileUri;
    FuelListAdapter fuelListAdapter;
    String allowType = "", lodgingStayTypeId = "", billAmts = "";
    boolean isJointWork = false, isFullPay = false, isSavedBill = false;
    LinearLayout Dynamicallowance, OtherExpense, localTotal, otherExpenseLayout, linAll, linRemarks,
            linFareAmount, ldg_typ_sp, linLocalSpinner, linOtherSpinner, ldg_StylocSpinner,ldg_DrvStylocSpinner, DA_TypSpinner, DA_locSpinner, lodgCont, lodgContvw, ldg_stayloc, ldg_stayDt,
            lodgJoin, ldgEAra, ldgMyEAra, JNLdgEAra, drvldgEAra, jointLodging, vwBoarding, vwDrvBoarding, linAddplaces,
            linAddAllowance, diverAllowanceLinear, LDailyAllowance, LOtherExpense, LLocalConve, LinearOtherAllowance,
            linlocalCon, linBusMode, linBikeMode, linMode, travelDynamicLoaction, travelPlaces, linDailyAllowance, lin,
            linImgPrv, TotalDays, stayDays, linEarly, linLate, linContinueStay, linCheckOut, vwldgBillAmt, linearConView;
    LinearLayout viewContinue, viewContinueTotal, ViewData, driverStayLocLayout, ldgGstLayout, lcGstLayout, oEGstLayout, tvGstLayout;
    RelativeLayout lnChangePlace,lnRetVehicle;
    CardView card_date, TravelBike, crdDynamicLocation, ldg_ara, cardTrvPlcs;
    TextView txt_date, txt_ldg_type, TxtStartedKm, TxtClosingKm, modeTextView, travelTypeMode,
            TotalTravelledKm, txtBusFrom, txtBusTo, txtTaClaim, PersonalTextKM, PersonalKiloMeter,
            txtDailyAllowance, editText, ldg_cin, ldg_cout, ldg_coutDt, txtJNName, txtJNDesig, txtJNDept, txtJNHQ, txtJNMob,
            lblHdBill, lblHdBln, ldgWOBBal, ldgAdd, txtJNMyEli, txtMyEligi, txtDrivEligi, lbl_ldg_eligi, txt_totDA,
            fuelAmount, TextTotalAmount, editTexts, oeEditext, localText, OeText, grandTotal, txtallamt, txt_BrdAmt,
            txt_DrvBrdAmt, txtJointAdd, txtJNEligi, txtTAamt, txtDesig, txtDept, txtEmpId, txtName, oeTxtUKey, oeTxtUKeys,
            lcTxtUKey, lcTxtUKeys, tvTxtUKey, tvTxtUKeys, txtMaxKm, txtDrvrBrod, txtStyDays, txtLodgUKey,
            txt_Styloc,txt_drvStyloc, txt_DAStyloc, txt_DATyp, txtAllwType, txtCAllwType, txEligDt, NoofNight, txldgTdyAmt,
            edtRwID;
    EditText enterMode, enterFrom, enterTo, enterFare, etrTaFr, etrTaTo, editTextRemarks, editLaFare, edtOE, edt, edt1, edt_ldg_JnEmp,
            edt_ldg_bill, edtLcFare, lodgStyLocation, drvStyLocation, earCheckIn, earCheckOut, latCheckIn, latCheckOut, edtEarBill, edtLateBill, txDAOthName,
            edtFAFrom,edtFAStartKm,edtFATo,edtFACloseKm,edtFAPersonalKm,FAtravelledkm,
            edtLdgGstNum,edtLdgGstAmt, edtLdgGstBillNo, edtTVGstNum, edtTVGstAmt, edtTVGstBillNo, edtOEGstNum, edtOEGstAmt,edtOEGstBillNo, edtLCGstNum, edtLCGstAmt, edtLCGstBillNo;
    ImageView deleteButton, previewss, taAttach, lcAttach, oeAttach, lcPreview, oePreview, endkmimage, startkmimage,
            img_lodg_prvw, img_lodg_atta, mapZoomIn, imgEdtPlace, btnDAclose,btnVRetclose;
    String SF_code = "", div = "", State_Code = "", StartedKm = "", ClosingKm = "", ModeOfTravel = "", PersonalKm = "",
            DriverNeed = "", DateForAPi = "", DateTime = "", shortName = "", Exp_Name = "", Id = "", userEnter = "",
            attachment = "", maxAllowonce = "", strRetriveType = "", StrToEnd = "", StrBus = "", StrTo = "", StrDaName = "",
            OEdynamicLabel = "", strFuelAmount = "", StrModeValue = "", dynamicLabel = "", StrDailyAllowance = "", ldgEmpName = "",
            witOutBill = "", ValCd = "", fullPath = "", filePath = "", editMode = "", editModeId = "", allowanceAmt = "", myldgEliAmt = "", myBrdEliAmt = "",
            drvldgEliAmt = "", drvBrdEliAmt = "", strGT = "", totLodgAmt = "", start_Image = "", End_Imge = "", finalPath = "",
            attach_Count = "", ImageURl = "", keyEk = "EK", oeEditCnt = "", lcEditcnt = "", lodEditcnt = "", tvEditcnt = "", OeUKey = "",
            LcUKey = "", TlUKey = "", lcUKey = "", oeUKey = "", ImageUKey = "", taAmt = "", stayTotal = "", lodUKey = "", gstNo="",
            DATE = "", lodgEarly = "", lodgLate = "", tominYear = "", tominMonth = "", sty_date = "", tominDay = "", ConStay = "", ErlyStay = "", LteStay = "", ErlyChecIn = "", ErlyChecOut = "", ErlyAmt = "", LteAmt = "", LteChecIn = "", LteChecOut = "",
            sLocId = "",sDrvLocId="", sLocName = "",sDrvLocName="", sDALocId = "", sDALocName = "", sDALType, CInDate = "", COutDate = "", Alw_Eligibilty = "";
    Integer totalkm = 0, totalPersonalKm = 0, Pva, C = 0, S = 0, editTextPositionss,
            oePosCnt = 0, lcPosCnt = 0, tvSize = 0, ttLod = 0, cnSty = 0, erlSty = 0, lteSty = 0;
    int size = 0, lcSize = 0, OeSize = 0, daysBetween = 0, OnlyNight = 0, transferflg = 0, TWMax_Km = 300, FWMax_Km = 1000,VHRetFlag=0;
    long styDate = 0, nofNght = 0;
    ScrollView scrlMain;
    Double tofuel = 0.0, ldgEliAmt = 0.0, ldgDrvEligi = 0.0,ldgDrvAlw = 0.0, gTotal = 0.0, TotLdging = 0.0,
            GrandTotalAllowance = 0.0, fAmount = 0.0, doubleAmount = 0.0, myBrdAmt = 0.0, drvBrdAmt = 0.0,
            otherExp = 0.0, localCov = 0.0, sum = 0.0, sumsTotss = 0.0, sumsTot = 0.0, BusAmount = 0.0;
    double TotDA = 0.0, sTotal = 0.0, sums = 0.0, sumsTa = 0.0, tTotAmt = 0.0, stayEgTotal = 0.0;

    //ArrayList<CtrlsListModel> uLCItems,uOEItems;
    float tJointAmt = 0;
    boolean changeStay = false;
    Button btnDAChange,btnVehiRet,btnFASubmit;
    CircularProgressButton btn_sub, buttonSave;
    int countLoding = 0;

    /**/
    HashSet<String> hashSet = new HashSet<>();
    /**/


    ArrayList<SelectionModel> array = new ArrayList<>();
    ArrayList<String> DA = new ArrayList<>();
    ArrayList<String> OE = new ArrayList<>();
    ArrayList<String> LC = new ArrayList<>();
    ArrayList<String> temaplateList;
    ArrayList<String> OEdynamicList, dynamicLabelList, attachCountList;
    ArrayList<String> lodgArrLst = new ArrayList<>();
    List<Common_Model> listAllwType = new ArrayList<>();
    List<Common_Model> listOrderType = new ArrayList<>();
    List<Common_Model> modelRetailDetails = new ArrayList<>();
    List<Common_Model> OtherExpenseList = new ArrayList<>();
    List<Common_Model> ldgModes = new ArrayList<>();
    List<Common_Model> ldgLocations = new ArrayList<>();
    List<Common_Model> modelTravelType = new ArrayList<>();
    List<ModeOfTravel> modelOfTravel;
    List<EditText> newEdt = new ArrayList<>();
    Map<String, CtrlsListModel> uTAItems = new HashMap<String, CtrlsListModel>();
    Map<String, CtrlsListModel> uLCItems = new HashMap<String, CtrlsListModel>();
    Map<String, CtrlsListModel> uOEItems = new HashMap<String, CtrlsListModel>();
    Map<String, String> AttachmentImg = new HashMap<String, String>();
    CustomListViewDialog customDialog;
    JSONObject jsonDailyAllowance = new JSONObject();
    Type userType;
    Gson gson;
    Dialog dialog;
    DatePickerDialog picker;
    Switch ldgNeeded;
    View viw, viewBilling;
    ScrollView mLc;
    CheckBox mChckCont, mChckEarly, mChckLate,chkDrvAlw;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editors;
    JsonArray jsonArray = null, ExpSetup = null, trvPlcsArray = null, jsonFuelAllowance = null, jsonExpHead = null, lcDraftArray = null, oeDraftArray = null,
            trvldArray = null, ldArray = null, travelDetails = null, LodingCon = null, StayDate = null;
    JSONArray jLCitems, jOEitems, jArrAttach;
    RecyclerView mFuelRecycler;
    double continueStay = 0.0;
    Double fuelAmt = 0.0;
    TextView TextCheckInDate;
    LinearLayout LinearCheckInDate;
    Location clocation = null;
    com.saneforce.godairy.Activity_Hap.Common_Class DT = new com.saneforce.godairy.Activity_Hap.Common_Class();
    private ShimmerFrameLayout mShimmerViewContainer;
    private String Ukey = "";
    String StartFrom,EndTo,StartKM,EndKM,PersonalKM,TravelledKM;
    Context context = this;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_a_claim);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmer();

        assistantClass = new AssistantClass(context);
        mCommon_class = new Common_Class(this);
        util = new Util();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.route_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        mShared_common_pref = new Shared_Common_Pref(this);
        getToolbar();
        gson = new Gson();
        temaplateList = new ArrayList<>();
        dynamicLabelList = new ArrayList<>();
        OEdynamicList = new ArrayList<>();
        attachCountList = new ArrayList<>();
        jArrAttach = new JSONArray();


        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");
        State_Code = UserDetails.getString("State_Code", "");

        new LocationFinder(getApplication(), new LocationEvents() {
            @Override
            public void OnLocationRecived(Location location) {
                clocation = location;
            }
        });

        ldgGstLayout = findViewById(R.id.ldg_gstLayout);


        txt_date = findViewById(R.id.txt_date);
        card_date = findViewById(R.id.card_date);
        btn_sub = findViewById(R.id.btn_sub);
        linAddAllowance = findViewById(R.id.lin_travel_loaction);
        linAddplaces = findViewById(R.id.lin_travel_places);
        vwBoarding = findViewById(R.id.vwBoarding);
        vwDrvBoarding = findViewById(R.id.vwDrvBoarding);
        TravelBike = findViewById(R.id.linear_bike);
        TxtStartedKm = findViewById(R.id.txt_started_km);
        TxtClosingKm = findViewById(R.id.txt_ended_km);
        travelTypeMode = findViewById(R.id.txt_type_travel);
        PersonalTextKM = findViewById(R.id.personal_km_text);
        endkmimage = findViewById(R.id.endkmimage);
        startkmimage = findViewById(R.id.startkmimage);
        jointLodging = findViewById(R.id.lin_join_person);
        txtJointAdd = findViewById(R.id.txt_joint_add);
        TotalTravelledKm = findViewById(R.id.total_km);
        PersonalKiloMeter = findViewById(R.id.pers_kilo_meter);
        editTextRemarks = findViewById(R.id.edt_rmk);
        LDailyAllowance = findViewById(R.id.lin_daily_allowance);
        LOtherExpense = findViewById(R.id.lin_other_allowance);
        LLocalConve = findViewById(R.id.lin_local_con);
        linBusMode = findViewById(R.id.linear_bus_mode);
        linBikeMode = findViewById(R.id.linear_bike_mode);
        linMode = findViewById(R.id.linear_mode);
        localTotal = findViewById(R.id.lin_total_loca);
        otherExpenseLayout = findViewById(R.id.lin_total_other);
        lodgStyLocation = findViewById(R.id.edt_stay_loc);
        drvStyLocation = findViewById(R.id.edt_drvstay_loc);
        txtallamt = findViewById(R.id.txt_mode_amount);
        txt_BrdAmt = findViewById(R.id.txt_BrdAmt);
        txt_DrvBrdAmt = findViewById(R.id.txt_DrvBrdAmt);
        txtBusFrom = findViewById(R.id.txt_bus_from);
        txtBusTo = findViewById(R.id.txt_bus_to);
        txtTaClaim = findViewById(R.id.mode_name);
        txtDailyAllowance = findViewById(R.id.txt_daily_allowance_mode);
        travelDynamicLoaction = findViewById(R.id.lin_travel_dynamic_location);
        crdDynamicLocation = findViewById(R.id.card_travel_loaction);
        cardTrvPlcs = findViewById(R.id.card_travel_places);
        travelPlaces = findViewById(R.id.lin_travel_dynamic_place);
        linDailyAllowance = findViewById(R.id.lin_da_type);
        linlocalCon = findViewById(R.id.lin_dyn_local_con);
        fuelAmount = findViewById(R.id.fuel_amount);
        TextTotalAmount = findViewById(R.id.txt_total_amt);
        LinearOtherAllowance = findViewById(R.id.lin_dyn_other_Expense);
        grandTotal = findViewById(R.id.grand_total);
        localText = findViewById(R.id.txt_local);
        OeText = findViewById(R.id.txt_oe);
        linAll = findViewById(R.id.lin_alo_view);
        linRemarks = findViewById(R.id.lin_remarks);
        linFareAmount = findViewById(R.id.lin_fare_amount);
        ldgNeeded = findViewById(R.id.sw_ldgNeed);
        ldg_ara = findViewById(R.id.linear_loadge);
        ldg_typ_sp = findViewById(R.id.ldg_typ_spiner);
        ldg_StylocSpinner = findViewById(R.id.ldg_StylocSpinner);
        ldg_DrvStylocSpinner = findViewById(R.id.ldg_drvStylocSpinner);
        txDAOthName = findViewById(R.id.txDAOthName);
        DA_locSpinner = findViewById(R.id.DA_locSpinner);
        DA_TypSpinner = findViewById(R.id.DA_TypSpinner);
        lnChangePlace = findViewById(R.id.lnChangePlace);
        lnRetVehicle = findViewById(R.id.lnRetVehicle);
        imgEdtPlace = findViewById(R.id.img_edit);
        scrlMain = findViewById(R.id.scrlMain);

        lodgCont = findViewById(R.id.lodgCont);
        lodgContvw = findViewById(R.id.lodgContvw);
        txt_ldg_type = findViewById(R.id.ldg_typ);
        ldg_stayloc = findViewById(R.id.ldg_stayloc);
        ldg_stayDt = findViewById(R.id.ldg_stayDt);
        lodgJoin = findViewById(R.id.lodgJoin);
        ldgEAra = findViewById(R.id.ldgEAra);
        ldgMyEAra = findViewById(R.id.ldgMyEAra);
        JNLdgEAra = findViewById(R.id.JNLdgEAra);
        drvldgEAra = findViewById(R.id.drvldgEAra);
        ldg_cin = findViewById(R.id.from_picker);
        ldg_cout = findViewById(R.id.to_picker);
        ldg_coutDt = findViewById(R.id.toDt_picker);
        NoofNight = findViewById(R.id.NoofNight);
        txt_totDA = findViewById(R.id.txt_totDA);
        txtMyEligi = findViewById(R.id.txtMyEligi);
        txtDrivEligi = findViewById(R.id.txtDrvLgd);
        driverStayLocLayout=findViewById(R.id.ldg_drvstayloc);
        chkDrvAlw = findViewById(R.id.chk_Drv_alw);
        lbl_ldg_eligi = findViewById(R.id.lbl_ldg_eligi);
        lblHdBill = findViewById(R.id.lblHdBill);
        lblHdBln = findViewById(R.id.lblHdBln);
        ldgWOBBal = findViewById(R.id.ldgWOBBal);
        edt_ldg_bill = findViewById(R.id.edt_ldg_bill);
        img_lodg_prvw = findViewById(R.id.lodg_preview);
        img_lodg_atta = findViewById(R.id.lodg_attach);
        txtJNEligi = findViewById(R.id.txtJNEligi);
        ldgAdd = findViewById(R.id.ldg_Add);
        mapZoomIn = findViewById(R.id.map_zoom);
        buttonSave = findViewById(R.id.save_button);
        txtTAamt = findViewById(R.id.txt_trav_loca);
        txtName = findViewById(R.id.txt_Name);
        txtDesig = findViewById(R.id.txt_desg);
        txtDept = findViewById(R.id.txt_dep);
        txtEmpId = findViewById(R.id.txt_emp_id);
        txtName.setText(Shared_Common_Pref.Sf_Name);
        txtDesig.setText(mShared_common_pref.getvalue(Shared_Common_Pref.SF_DESIG));
        txtDept.setText(mShared_common_pref.getvalue(Shared_Common_Pref.SF_DEPT));
        txtEmpId.setText("(" + mShared_common_pref.getvalue(Shared_Common_Pref.SF_EMP_ID) + ")");
        viw = findViewById(R.id.vw_tl);
        lin = findViewById(R.id.lin_tl);
        txtMaxKm = findViewById(R.id.max_km);
        txtDrvrBrod = findViewById(R.id.txt_driver_boarding);
        txtStyDays = findViewById(R.id.txt_stay_total);
        TotalDays = findViewById(R.id.total_days);
        stayDays = findViewById(R.id.lin_stay_view);
        txtLodgUKey = findViewById(R.id.log_ukey);
        txt_Styloc = findViewById(R.id.txt_Styloc);
        txt_drvStyloc = findViewById(R.id.txt_drvStyloc);
        txt_DATyp = findViewById(R.id.txt_DATyp);
        txtAllwType = findViewById(R.id.txtAllwType);
        txtCAllwType = findViewById(R.id.cAllwType);
        txEligDt = findViewById(R.id.txEligDt);
        txldgTdyAmt = findViewById(R.id.txldgTdyAmt);

        txt_DAStyloc = findViewById(R.id.txt_DAloc);
        earCheckIn = findViewById(R.id.early_check_in);
        earCheckOut = findViewById(R.id.early_check_out);
        latCheckIn = findViewById(R.id.late_check_in);
        latCheckOut = findViewById(R.id.late_check_out);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        mLc = findViewById(R.id.lc_srcoll);
        linImgPrv = findViewById(R.id.lin_img_prv);
        viewBilling = findViewById(R.id.bill_view);
        linEarly = findViewById(R.id.lin_early);
        linLate = findViewById(R.id.lin_late);
        linContinueStay = findViewById(R.id.lin_indep_sty);
        linCheckOut = findViewById(R.id.lin_chck_out);
        mChckCont = findViewById(R.id.chk_cnt_sty);
        mChckEarly = findViewById(R.id.chk_early);
        mChckLate = findViewById(R.id.chk_late);
        edtEarBill = findViewById(R.id.edt_earl_bil);
        edtLateBill = findViewById(R.id.lat_lod_bil);
        linearConView = findViewById(R.id.linear_con);
        viewContinue = (LinearLayout) findViewById(R.id.lin_con_sty);
        viewContinueTotal = (LinearLayout) findViewById(R.id.lin_con_sty_amt);
        ViewData = findViewById(R.id.data);
        TextCheckInDate = findViewById(R.id.txt_hotel_date);
        btnDAChange = findViewById(R.id.btnDAChange);
        btnVehiRet = findViewById(R.id.btnVehiRet);
        btnVRetclose = findViewById(R.id.btnVRetclose);
        btnDAclose = findViewById(R.id.btnDAclose);

        mFuelRecycler = findViewById(R.id.recycler_fuel);
        mFuelRecycler.setLayoutManager(new LinearLayoutManager(this));
        mFuelRecycler.setNestedScrollingEnabled(false);

        vwldgBillAmt = findViewById(R.id.vwldgBillAmt);

        /*--- Fuel Allowance - Add Vehicle Returns ---*/

        edtFACloseKm=findViewById(R.id.edt_FAendkm);
        edtFAFrom=findViewById(R.id.edt_placeFAFrom);
        edtFAStartKm=findViewById(R.id.edt_FAstartKm);
        edtFATo=findViewById(R.id.edt_placeFATo);
        edtFAPersonalKm=findViewById(R.id.edt_FApersonalkm);
        FAtravelledkm=findViewById(R.id.FATravelledkm);
        btnFASubmit=findViewById(R.id.btnFASubmit);

        edtLdgGstNum = findViewById(R.id.edt_ldg_gst);
        edtLdgGstAmt = findViewById(R.id.edt_ldg_gst_amt);
        edtLdgGstBillNo = findViewById(R.id.edt_ldg_gst_bno);




        TextWatcher textWatcher =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtFACloseKm.getText().toString().equals("") && !edtFAStartKm.getText().toString().equals("")) {
                    int temp1 = Integer.parseInt(edtFACloseKm.getText().toString());
                    int temp2 = Integer.parseInt(edtFAStartKm.getText().toString());
                    FAtravelledkm.setText(String.valueOf(temp1 - temp2));
                    Log.v("FA-travelledKm",String.valueOf(temp1 - temp2));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        btnFASubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FASubmit();
            }
        });

        edtFACloseKm.addTextChangedListener(textWatcher);
        edtFAStartKm.addTextChangedListener(textWatcher);

        /*---------*/

        ldgLocations.clear();
        loadLocations();
        dynamicDate();
        if (ldgLocations.size() < 2) {
            getHapLocations();
        }
        mChckCont.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContinueStayChecked(isChecked);
            }
        });
        mChckEarly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linEarly.setVisibility(View.VISIBLE);
                    erlSty = 1;
                } else {
                    erlSty = 0;
                    earCheckIn.setText("");
                    earCheckOut.setText("");
                    linEarly.setVisibility(View.GONE);
                }
            }
        });
        mChckLate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lteSty = 1;
                    linLate.setVisibility(View.VISIBLE);
                } else {
                    lteSty = 0;
                    latCheckIn.setText("");
                    latCheckOut.setText("");
                    linLate.setVisibility(View.GONE);
                }
            }
        });
        chkDrvAlw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ldgDrvEligi=ldgDrvAlw;
                    drvldgEAra.setVisibility(View.VISIBLE);
                    driverStayLocLayout.setVisibility(View.VISIBLE);
//                    txtDrivEligi.setVisibility(View.VISIBLE);
                } else {
                    ldgDrvEligi=0.0;
//                    txtDrivEligi.setVisibility(View.GONE);
                    drvldgEAra.setVisibility(View.GONE);
                    driverStayLocLayout.setVisibility(View.GONE);

                }
                txtDrivEligi.setText("₹" + new DecimalFormat("##0.00").format(ldgDrvEligi));
                SumOFLodging(0);
            }
        });
        img_lodg_atta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());
                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                    Log.v("PERMISSION", "NO");
                } else {
                    Log.v("PERMISSION", "Yes");

                    popupCapture(143);

                }
            }
        });
        img_lodg_prvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttachementActivity.setOnAttachmentDeleteListener(new OnAttachmentDelete() {
                    @Override
                    public void OnImageDelete(String Mode, int ImgCount) {
                        if (ImgCount < 1) {
                            txtLodgUKey.setText("");
                        }
                    }
                });
                DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
                Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                //TA claim
                stat.putExtra("position", txtLodgUKey.getText().toString());
                stat.putExtra("headTravel", "LOD");
                stat.putExtra("mode", "Room");
                stat.putExtra("date", DateTime);
                startActivity(stat);
            }
        });

//        drvldgEAra.setVisibility(View.VISIBLE);
        lnChangePlace.setVisibility(View.GONE);
        lnRetVehicle.setVisibility(View.GONE);

        mCommon_model_spinner = new Common_Model("HQ", "HQ", "flag");
        listAllwType.add(mCommon_model_spinner);
        mCommon_model_spinner = new Common_Model("EXQ", "EXQ", "flag");
        listAllwType.add(mCommon_model_spinner);
        mCommon_model_spinner = new Common_Model("Out Station", "Out Station", "flag");
        listAllwType.add(mCommon_model_spinner);
        mCommon_model_spinner = new Common_Model("None", "None", "flag");
        listAllwType.add(mCommon_model_spinner);


        ldgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ldgEliAmt = 0.0;

                drvldgEAra.setVisibility(View.GONE);
                if (ldgAdd.getText().equals("+ Add")) {
                    ldgAdd.setText("- Remove");
                    lodgContvw.setVisibility(View.VISIBLE);


                    lodgCont.setVisibility(View.GONE);
                    JNLdgEAra.setVisibility(View.GONE);
                    TotalDays.setVisibility(View.GONE);
                    txt_Styloc.setText("");
                    txt_drvStyloc.setText("");
                    lodgStyLocation.setText("");
                    drvStyLocation.setText("");
                    txt_ldg_type.setText("");
                    lodgJoin.setVisibility(View.GONE);
                    TotalDays.setVisibility(View.GONE);

                } else {
                    ldgAdd.setText("+ Add");
                    lodgContvw.setVisibility(View.GONE);
                    txtMyEligi.setText("₹" + new DecimalFormat("##0.00").format(Double.valueOf("0.00")));
                    totLodgAmt = String.valueOf(0);

                }
                SumOFLodging(0);
            }
        });
        ldg_typ_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LDGType();
            }
        });
        btnDAChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject data = new JSONObject();
                try {
                    if (txt_DATyp.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(TAClaimActivity.this, "Select the DA Type", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (sDALocId.equalsIgnoreCase("")) {
                        Toast.makeText(TAClaimActivity.this, "Select the DA Type", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (sDALocId.equalsIgnoreCase("-1")) {
                        if (txDAOthName.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(TAClaimActivity.this, "Enter the Other Location", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    data.put("SF", UserDetails.getString("Sfcode", ""));
                    data.put("ExpDt", DateTime);
                    data.put("DAType", txt_DATyp.getText().toString().equalsIgnoreCase("NONE") ? "NA" : txt_DATyp.getText().toString());
                    data.put("DALocId", sDALocId);
                    sDALocName = txt_DAStyloc.getText().toString();
                    if (sDALocId.equalsIgnoreCase("-1")) {
                        sDALocName = txDAOthName.getText().toString();
                    }
                    data.put("DALoc", sDALocName);

                    apiInterface.getAndUpdate("update/DA", data.toString()).enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            JsonArray DADets = response.body();
                            JsonObject jsonObject = DADets.get(0).getAsJsonObject();

                            StrTo = jsonObject.get("To_Place").getAsString();
                            StrDailyAllowance = jsonObject.get("dailyAllowance").getAsString();
                            allowanceAmt = jsonObject.get("Allowance_Value").getAsString();
                            myBrdEliAmt = jsonObject.get("myBrdAmt").getAsString();
                            drvBrdEliAmt = jsonObject.get("drvBrdAmt").getAsString();
                            DriverNeed = jsonObject.get("driverAllowance").getAsString();

                            txtDailyAllowance.setText(StrDailyAllowance + " - " + StrTo);

                            myBrdAmt = 0.0;
                            //drvBrdAmt = 0.0;
                            vwDrvBoarding.setVisibility(View.GONE);
                            vwBoarding.setVisibility(View.VISIBLE);
                            if (StrDailyAllowance.equals("Out Station")) {
                                doubleAmount = 0.0;
                                txtallamt.setText("");
                                myBrdEliAmt = myBrdEliAmt.replaceAll("^[\"']+|[\"']+$", "");
                                myBrdAmt = Double.valueOf(myBrdEliAmt);
                                txt_BrdAmt.setText("₹" + new DecimalFormat("##0.00").format(myBrdAmt));
                                if (DriverNeed.equalsIgnoreCase("true")) {
                                    drvBrdEliAmt = drvBrdEliAmt.replaceAll("^[\"']+|[\"']+$", "");
                                    drvBrdAmt = Double.valueOf(drvBrdEliAmt);
                                    txt_DrvBrdAmt.setText("₹" + new DecimalFormat("##0.00").format(drvBrdAmt));

                                    vwDrvBoarding.setVisibility(View.VISIBLE);
                                    txtDrvrBrod.setText("Driver Allowance Boarding");
                                } else {
                                    txt_DrvBrdAmt.setText("");
                                }

                                vwBoarding.setVisibility(View.VISIBLE);
                                SumOFDAAmount();
                            } else {
                                allowanceAmt = allowanceAmt.replaceAll("^[\"']+|[\"']+$", "");
                                doubleAmount = Double.valueOf(allowanceAmt);
                                myBrdAmt = 0.0;
                                txtallamt.setText("₹" + new DecimalFormat("##0.00").format(doubleAmount));
                                txt_BrdAmt.setText("₹0.00");

                                if (DriverNeed.equalsIgnoreCase("true")) {
                                    drvBrdEliAmt = drvBrdEliAmt.replaceAll("^[\"']+|[\"']+$", "");
                                    drvBrdAmt = Double.valueOf(drvBrdEliAmt);
                                    txt_DrvBrdAmt.setText("₹" + new DecimalFormat("##0.00").format(drvBrdAmt));

                                    vwDrvBoarding.setVisibility(View.VISIBLE);
                                    txtDrvrBrod.setText("Driver Allowance Boarding");
                                } else {
                                    txt_DrvBrdAmt.setText("");
                                }
                                vwBoarding.setVisibility(View.GONE);
                                SumOFDAAmount();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            Log.d("DACliam", "Error : " + t.getMessage());
                        }
                    });

                    lnChangePlace.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnVehiRet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lnRetVehicle.setVisibility(View.VISIBLE);
                edtFAFrom.setText("");
                edtFATo.setText("");
                edtFAStartKm.setText("");
                edtFACloseKm.setText("");
                edtFAPersonalKm.setText("");
                FAtravelledkm.setText("");
            }
        });
        btnVRetclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnRetVehicle.setVisibility(View.GONE);

            }
        });
        imgEdtPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnChangePlace.setVisibility(View.VISIBLE);
            }
        });
        btnDAclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnChangePlace.setVisibility(View.GONE);
            }
        });
        DA_locSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog = new CustomListViewDialog(TAClaimActivity.this, ldgLocations, 13);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });
        DA_TypSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog = new CustomListViewDialog(TAClaimActivity.this, listAllwType, 14);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });

        ldg_StylocSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customDialog = new CustomListViewDialog(TAClaimActivity.this, ldgLocations, 12);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });
        ldg_DrvStylocSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(TAClaimActivity.this, ldgLocations, 15);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });
        TextCheckInDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDatePicker("LIN");
            }
        });
        ldg_cin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker("Lod_Check_In");
            }
        });
        ldg_cout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker("Lod_Check_Out");
            }
        });
        ldg_coutDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDatePicker("LCO");
            }
        });

        earCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker("Ear_Check_In");
            }
        });
        earCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker("Ear_Check_Out");
            }
        });
        latCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker("Lat_Check_In");
            }
        });
        latCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker("Lat_Check_Out");
            }
        });


        edt_ldg_bill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SumWOBLodging();
            }
        });
        //Not Needed below code
        edtEarBill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SumWOBLodging();
            }
        });
        edtEarBill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SumOFLodging(0);
            }
        });
        edtLateBill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SumOFLodging(0);
            }
        });
        //end comment

        strRetriveType = String.valueOf(getIntent().getSerializableExtra("Retrive_Type"));
        if (strRetriveType.equals("Daily Allowance")) {
            jsonDailyAllowance = (JSONObject) getIntent().getSerializableExtra("Retrive_Ta_List");
        }

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });


        strGT = grandTotal.getText().toString();


        LOtherExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                otherExpenseLayout.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(15, 15, 15, 15);
                final View rowView = inflater.inflate(R.layout.activity_other_expense, null);
                oEGstLayout = findViewById(R.id.otherExp_gstLayout);
                edtOEGstNum = findViewById(R.id.edt_otherExp_gst);
                edtOEGstAmt = findViewById(R.id.edt_otherExp_gst_amt);
                edtOEGstBillNo = findViewById(R.id.edt_otherExp_gst_bno);

                LinearOtherAllowance.addView(rowView, layoutParams);
                oePosCnt = LinearOtherAllowance.indexOfChild(rowView);
                View views = LinearOtherAllowance.getChildAt(oePosCnt);
                otherExpenseLayout.setVisibility(View.VISIBLE);
                oeEditext = (TextView) (views.findViewById(R.id.other_enter_mode));
                edtOE = (EditText) (views.findViewById(R.id.oe_fre_amt));
                oeAttach = (ImageView) (views.findViewById(R.id.oe_attach_img));
                oePreview = (ImageView) (views.findViewById(R.id.img_prvw_oe));
                oeTxtUKey = (TextView) (views.findViewById(R.id.txt_oe_ukey));
                OtherExpense = (LinearLayout) views.findViewById(R.id.lin_other_expense_dynamic);
                linOtherSpinner = (LinearLayout) (views.findViewById(R.id.lin_othr_spiner));

                oeAttach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());

                        if (!cameraPermission.checkPermission()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                cameraPermission.requestPermission();
                            }
                            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                        } else {
                            Log.v("PERMISSION", "PERMISSION");
                            Integer valuedfd = LinearOtherAllowance.indexOfChild(rowView);
                            View view = LinearOtherAllowance.getChildAt(valuedfd);
                            oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
                            oeTxtUKeys = (TextView) (view.findViewById(R.id.txt_oe_ukey));
                            editMode = oeEditext.getText().toString();

                            popupCapture(99);

                        }

                    }
                });
                oePreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Integer valuedfd = LinearOtherAllowance.indexOfChild(rowView);
                        View view = LinearOtherAllowance.getChildAt(valuedfd);
                        oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
                        oeTxtUKeys = (TextView) (view.findViewById(R.id.txt_oe_ukey));
                        editMode = oeEditext.getText().toString();
                        OeUKey = oeTxtUKeys.getText().toString();

                        AttachementActivity.setOnAttachmentDeleteListener(new OnAttachmentDelete() {
                            @Override
                            public void OnImageDelete(String Mode, int ImgCount) {
                                if (ImgCount < 1) {
                                    oeTxtUKeys.setText("");
                                }
                            }
                        });

                        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
                        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                        stat.putExtra("position", OeUKey);
                        stat.putExtra("headTravel", "OE");
                        stat.putExtra("mode", editMode);
                        stat.putExtra("date", DateTime);
                        startActivity(stat);
                    }
                });
                edtOE.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        SumOFOTAmount();
                    }
                });

                linOtherSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OtherExpenseList.clear();
                        Integer valuedf = LinearOtherAllowance.indexOfChild(rowView);
                        OtherExpenseMode(valuedf);

                    }
                });

            }
        });
        LLocalConve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);

                final View rowView = inflater.inflate(R.layout.activity_local_convenyance, null);
                lcGstLayout = findViewById(R.id.lcConv_gstLayout);

                edtLCGstNum = findViewById(R.id.edt_lcConv_gst);
                edtLCGstAmt = findViewById(R.id.edt_lcConv_gst_amt);
                edtLCGstBillNo = findViewById(R.id.edt_lcConv_gst_bno);

                linlocalCon.addView(rowView, layoutParams);
                localText.setVisibility(View.VISIBLE);

                Integer localCount = linlocalCon.getChildCount();
                lcPosCnt = linlocalCon.indexOfChild(rowView);
                LayoutTransition transition = new LayoutTransition();
                linlocalCon.setLayoutTransition(transition);

                View LcchildView = linlocalCon.getChildAt(lcPosCnt);
                scroll();

                editTexts = (TextView) (LcchildView.findViewById(R.id.local_enter_mode));
                linLocalSpinner = (LinearLayout) LcchildView.findViewById(R.id.lin_loc_spiner);
                lcAttach = (ImageView) (LcchildView.findViewById(R.id.la_attach_iamg));
                lcPreview = (ImageView) (LcchildView.findViewById(R.id.img_prvw_lc));
                lcTxtUKey = (TextView) (LcchildView.findViewById(R.id.txt_lc_ukey));
                editLaFare = (EditText) (LcchildView.findViewById(R.id.edt_la_fare));
                editLaFare.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        SumOFLCAmount();
                    }
                });


                Dynamicallowance = (LinearLayout) LcchildView.findViewById(R.id.lin_allowance_dynamic);

                linLocalSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listOrderType.clear();
                        Integer lcPosCntS = linlocalCon.indexOfChild(rowView);
                        dynamicModeType(lcPosCntS);
                    }
                });

                lcAttach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());

                        if (!cameraPermission.checkPermission()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                cameraPermission.requestPermission();
                            }
                            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                        } else {
                            Log.v("PERMISSION", "PERMISSION");


                            Integer lcPosCntS = linlocalCon.indexOfChild(rowView);
                            View view = linlocalCon.getChildAt(lcPosCntS);
                            editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
                            lcTxtUKeys = (TextView) (view.findViewById(R.id.txt_lc_ukey));
                            editMode = editTexts.getText().toString();

                            popupCapture(786);

                        }

                    }
                });

                lcPreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Integer valuelc = linlocalCon.indexOfChild(rowView);
                        View view = linlocalCon.getChildAt(valuelc);
                        editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
                        lcTxtUKeys = (TextView) (view.findViewById(R.id.txt_lc_ukey));
                        editMode = editTexts.getText().toString();
                        LcUKey = lcTxtUKeys.getText().toString();
                        AttachementActivity.setOnAttachmentDeleteListener(new OnAttachmentDelete() {
                            @Override
                            public void OnImageDelete(String Mode, int ImgCount) {
                                if (ImgCount < 1) {
                                    lcTxtUKeys.setText("");
                                }
                            }
                        });
                        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

                        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                        stat.putExtra("position", LcUKey);
                        stat.putExtra("headTravel", "LC");
                        stat.putExtra("mode", editMode);
                        stat.putExtra("date", DateTime);
                        startActivity(stat);
                    }
                });
            }
        });
        linAddplaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(15, 15, 15, 15);

                final View rowView = inflater.inflate(R.layout.travel_allowance_dynamic_one, null);
                tvGstLayout = findViewById(R.id.trvAll_gstLayout);

                edtTVGstNum = findViewById(R.id.edt_trvAll_gst);
                edtTVGstAmt = findViewById(R.id.edt_trvAll_gst_amt);
                edtTVGstBillNo = findViewById(R.id.edt_trvAll_gst_bno);

                travelPlaces.addView(rowView, layoutParams);

                cardTrvPlcs.setVisibility(View.VISIBLE);
                size = travelPlaces.getChildCount();
                for (int c = 0; c < size; c++) {
                    View views = travelPlaces.getChildAt(c);
                    deleteButton = views.findViewById(R.id.delete_button);
                    etrTaFr = (EditText) views.findViewById(R.id.ta_edt_from);
                    etrTaTo = (EditText) views.findViewById(R.id.ta_edt_to);
                }
            }
        });
        linAddAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(15, 15, 15, 15);

                crdDynamicLocation.setVisibility(View.VISIBLE);
                final View rowView = inflater.inflate(R.layout.travel_allowance_dynamic, null);
                travelDynamicLoaction.addView(rowView, layoutParams);
                deleteButton = findViewById(R.id.delete_button);

                tvSize = travelDynamicLoaction.indexOfChild(rowView);
                View tvchildView = travelDynamicLoaction.getChildAt(tvSize);
                viw.setVisibility(View.VISIBLE);
                lin.setVisibility(View.VISIBLE);

                editText = (TextView) (tvchildView.findViewById(R.id.enter_mode));
                enterFare = (EditText) tvchildView.findViewById(R.id.enter_fare);
                tvGstLayout=(LinearLayout) (tvchildView.findViewById(R.id.trvAll_gstLayout));
                taAttach = (ImageView) (tvchildView.findViewById(R.id.image_attach));
                previewss = (ImageView) (tvchildView.findViewById(R.id.image_preview));
                tvTxtUKey = (TextView) (tvchildView.findViewById(R.id.txt_tv_ukey));

                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modelTravelType.clear();
                        Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                        localCon(tvSizes);
                    }
                });
                enterFare.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        SumOFTAAmount();
                    }
                });
                previewss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                        View view = travelDynamicLoaction.getChildAt(tvSizes);
                        editText = (TextView) (view.findViewById(R.id.enter_mode));
                        enterFare = (EditText) view.findViewById(R.id.enter_fare);
                        tvTxtUKeys = (TextView) (view.findViewById(R.id.txt_tv_ukey));
                        editMode = editText.getText().toString();
                        TlUKey = tvTxtUKeys.getText().toString();
                        AttachementActivity.setOnAttachmentDeleteListener(new OnAttachmentDelete() {
                            @Override
                            public void OnImageDelete(String Mode, int ImgCount) {
                                if (ImgCount < 1) {
                                    tvTxtUKeys.setText("");
                                }
                            }
                        });
                        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

                        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                        stat.putExtra("position", TlUKey);
                        stat.putExtra("headTravel", "TL");
                        stat.putExtra("mode", editMode);
                        stat.putExtra("date", DateTime);
                        startActivity(stat);
                    }
                });
                taAttach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());

                        if (!cameraPermission.checkPermission()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                cameraPermission.requestPermission();
                            }
                            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                        } else {
                            Log.v("PERMISSION", "PERMISSION");

                            Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                            View view = travelDynamicLoaction.getChildAt(tvSizes);
                            editText = (TextView) (view.findViewById(R.id.enter_mode));
                            enterFare = (EditText) view.findViewById(R.id.enter_fare);
                            tvTxtUKeys = (TextView) (view.findViewById(R.id.txt_tv_ukey));
                            editMode = editText.getText().toString();

                            //hide for file attach mandatory validation
//                            if (tvTxtUKeys.getText().toString().equals("")) {
//                                DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                                Calendar calobjw = Calendar.getInstance();
//                                tvEditcnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
//                                tvTxtUKeys.setText(tvEditcnt);
//                            }
//                            TlUKey = tvTxtUKeys.getText().toString();
                            if (editText.getText().toString().equalsIgnoreCase(""))
                                mCommon_class.showMsg(TAClaimActivity.this, "Select the Mode");
                            else
                                popupCapture(123);

                        }

                    }
                });

            }
        });
        txtJointAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lodingView();
            }
        });


        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String date = Common_Class.GetDate();

        DateForAPi = date.substring(0, date.indexOf(" "));
        callApi(DateForAPi, "");

        TaSharedPrefernce = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        if (TaSharedPrefernce.contains(SKM)) {
            StartedKm = TaSharedPrefernce.getString(SKM, "");
        }
        if (TaSharedPrefernce.contains(MOT)) {
            ModeOfTravel = TaSharedPrefernce.getString(MOT, "");
        }

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_sub.startAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (txt_date.getText().toString().matches("")) {
                            Toast.makeText(TAClaimActivity.this, "Please choose Date", Toast.LENGTH_SHORT).show();
                            ResetSubmitBtn(0, btn_sub);
                            return;
                        } else {
                            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String currentDateandTime = sdf.format(new Date());
                            if(DateTime.equalsIgnoreCase(currentDateandTime)){
                                Toast.makeText(TAClaimActivity.this, "Can't Send Approval on Same day", Toast.LENGTH_SHORT).show();
                                return;
                                //btn_sub.setVisibility(View.GONE);
                            }*/
                            if (!validate()) {
                                ResetSubmitBtn(0, btn_sub);
                                return;
                            }

                            AlertDialogBox.showDialog(TAClaimActivity.this, HAPApp.Title, String.valueOf(Html.fromHtml("Do You submit your claim to Approval.")), "Yes", "No", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    if (clocation != null) {
                                        submitData("SubmitForApp", btn_sub);
                                    } else {
                                        new LocationFinder(getApplication(), new LocationEvents() {
                                            @Override
                                            public void OnLocationRecived(Location location) {
                                                clocation = location;
                                                submitData("SubmitForApp", btn_sub);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {
                                    ResetSubmitBtn(0, btn_sub);
                                    dialog.dismiss();
                                }
                            });
                        }

                    }
                }, 100);
            }
        });
        card_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog = new CustomListViewDialog(TAClaimActivity.this, modelRetailDetails, 10);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });
        mapZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
                Intent mapZoom = new Intent(getApplicationContext(), MapZoomIn.class);
                mapZoom.putExtra("date", DateTime);

                startActivity(mapZoom);

                overridePendingTransition(R.anim.fade,
                        R.anim.fade);

            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSave.startAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!validate()) {
                            ResetSubmitBtn(0, buttonSave);
                            return;
                        }
                        AlertDialogBox.showDialog(TAClaimActivity.this, HAPApp.Title, String.valueOf(Html.fromHtml("Do You Save your claim as Draft.")), "Yes", "No", false, new AlertBox() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                if (clocation != null) {
                                    submitData("Save", buttonSave);
                                } else {
                                    new LocationFinder(getApplication(), new LocationEvents() {
                                        @Override
                                        public void OnLocationRecived(Location location) {
                                            clocation = location;
                                            submitData("Save", buttonSave);
                                        }
                                    });
                                }

                            }

                            @Override
                            public void NegativeMethod(DialogInterface dialog, int id) {
                                ResetSubmitBtn(0, buttonSave);
                                dialog.dismiss();
                            }
                        });
                    }
                }, 100);
            }
        });

        TaFuelEdit.onDistanceMeterWatcher(new DistanceMeterWatcher() {
            @Override
            public void onKilometerChange(JSONObject KMDetails) {
                Toast.makeText(TAClaimActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                changeDate(DateTime);
                SumOFTAAmount();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShowRequestPermissionRationale("true");
        }
    }

    private void ContinueStayChecked(boolean isChecked) {
        if (isChecked) {
            //linCheckOut.setVisibility(View.INVISIBLE);
            vwldgBillAmt.setVisibility(View.GONE);
            ldgGstLayout.setVisibility(View.GONE);
            COutDate = DT.AddDays(DateTime + " 00:00:00", 2, "yyyy-MM-dd");
            ldg_coutDt.setText(DT.AddDays(DateTime + " 00:00:00", 2, "yyyy-MM-dd"));
            cnSty = 1;
            countLoding = 1;
            SumOFLodging(1);
        } else {
            COutDate = DT.AddDays(DateTime + " 00:00:00", 1, "yyyy-MM-dd");
            ldg_coutDt.setText(DT.AddDays(DateTime + " 00:00:00", 1, "yyyy-MM-dd"));
            cnSty = 0;
            countLoding = 0;
            SumOFLodging(0);
            if (!isFullPay) {
                vwldgBillAmt.setVisibility(View.VISIBLE);
                ldgGstLayout.setVisibility(View.VISIBLE);
            }
            linCheckOut.setVisibility(View.VISIBLE);
        }
        nofNght = DT.Daybetween(DateTime + " 00:00:00", COutDate + " 00:00:00");
        NoofNight.setText(" - " + nofNght + " Nights - ");
        linContinueStay.setVisibility(View.VISIBLE);
        getStayAllow();
    }

    private void loadLDGTypes() {
        assistantClass.showProgressDialog("Loading Lodging types...", false);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "getLodgingAllowanceTypes");
        params.put("date", DateTime);
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                ldgModes.clear();
                JSONArray array1 = jsonObject.optJSONArray("response");
                if (array1 == null) {
                    array1 = new JSONArray();
                }
                for (int i = 0; i < array1.length(); i++) {
                    ldgModes.add(new Common_Model(array1.optJSONObject(i).optString("logCode"), array1.optJSONObject(i).optString("logName"), array1.optJSONObject(i).optString("jointWork"), array1.optJSONObject(i).optString("fullPay")));
                }
                assistantClass.dismissProgressDialog();
            }

            @Override
            public void onFailure(String error) {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithDismiss(error);
            }
        });
    }


    private void FASubmit() {

        if (edtFAFrom.getText().toString().length()==0 || edtFATo.getText().toString().length()==0 || edtFAStartKm.getText().toString().length()==0 || edtFACloseKm.getText().toString().length()==0){
            Toast.makeText(TAClaimActivity.this,"Enter all the Mandatory fields to Submit",Toast.LENGTH_SHORT).show();
            return;
        }

        String sVhPkm=edtFAPersonalKm.getText().toString();if(sVhPkm.equalsIgnoreCase("")) sVhPkm="0";
        String sVhTkm=FAtravelledkm.getText().toString();if(sVhTkm.equalsIgnoreCase("")) sVhTkm="0";
        double vhPkm=Double.parseDouble(sVhPkm);
        double vhTkm=Double.parseDouble(sVhTkm);
        if((vhTkm-vhPkm)<0) {
            Toast.makeText(TAClaimActivity.this,"Please Enter valid KM Details",Toast.LENGTH_SHORT).show();
            return;
        }
        if ((vhTkm-vhPkm)>FWMax_Km){
            Toast.makeText(TAClaimActivity.this,"KM Limit is Exceeded",Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject jObj = new JSONObject();
        try {

            jObj.put("SFCode",SF_code);
            jObj.put("dateTime",DateTime);
            jObj.put("StartFrom", edtFAFrom.getText().toString());
            jObj.put("EndTo", edtFATo.getText().toString());
            jObj.put("StartKM", edtFAStartKm.getText().toString());
            jObj.put("CloseKM", edtFACloseKm.getText().toString());
            jObj.put("PersonalKM", edtFAPersonalKm.getText().toString());
            jObj.put("TravelledKM", FAtravelledkm.getText().toString());
            Log.d("hjj","ghkj"+jObj.toString());


            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            service.getDataArrayList("Add/VHRet", jObj.toString()).enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                    Toast.makeText(TAClaimActivity.this, "Vehicle Returns Submitted Successfully", Toast.LENGTH_SHORT).show();
                    lnRetVehicle.setVisibility(View.GONE);
                    btnVehiRet.setVisibility(View.GONE);
                    RefreshFare();


                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Toast.makeText(TAClaimActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

//    @Override
//    public void onRestart()
//    {
//        super.onRestart();
//        finish();
//        startActivity(getIntent());
//    }

    public void getHapLocations() {
        String commonLeaveType = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> GetHAPLocation = service.GetHAPLocation(UserDetails.getString("Divcode", ""), UserDetails.getString("Sfcode", ""), commonLeaveType);
        GetHAPLocation.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                db.deleteMasterData("HAPLocations");
                db.addMasterData("HAPLocations", response.body());
                loadLocations();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
    }

    public void clearAll() {
        StartedKm = "";
        ClosingKm = "";
        ModeOfTravel = "";
        PersonalKm = "";
        DriverNeed = "";
        DateForAPi = "";
        shortName = "";
        Exp_Name = "";
        Id = "";
        ldgAdd.setText("+ Add");
        userEnter = "";
        attachment = "";
        maxAllowonce = "";
        strRetriveType = "";
        StrToEnd = "";
        StrBus = "";
        StrTo = "";
        StrDaName = "";
        OEdynamicLabel = "";
        strFuelAmount = "";
        StrModeValue = "";
        dynamicLabel = "";
        StrDailyAllowance = "";
        ldgEmpName = "";
        witOutBill = "";
        ValCd = "";
        isJointWork = false;
        isFullPay = false;
        isSavedBill = false;
        fullPath = "";
        filePath = "";
        editMode = "";
        allowanceAmt = "";
        myldgEliAmt = "";
        myBrdEliAmt = "";
        drvldgEliAmt = "";
        drvBrdEliAmt = "";
        strGT = "";
        totLodgAmt = "";
        start_Image = "";
        End_Imge = "";
        finalPath = "";
        attach_Count = "";
        ImageURl = "";
        keyEk = "EK";
        oeEditCnt = "";
        lcEditcnt = "";
        lodEditcnt = "";
        tvEditcnt = "";
        OeUKey = "";
        LcUKey = "";
        TlUKey = "";
        lcUKey = "";
        oeUKey = "";
        ImageUKey = "";
        taAmt = "";
        stayTotal = "";
        lodUKey = "";
        DATE = "";
        lodgEarly = "";
        lodgLate = "";
        tominYear = "";
        tominMonth = "";
        sty_date = "";
        tominDay = "";
        ConStay = "";
        ErlyStay = "";
        LteStay = "";
        ErlyChecIn = "";
        ErlyChecOut = "";
        ErlyAmt = "";
        LteAmt = "";
        LteChecIn = "";
        LteChecOut = "";
        sLocId = "";
        sLocName = "";
        sDrvLocId ="";
        sDrvLocName="";
        gstNo="";

        sDALocId = "";
        sDALocName = "";
        sDALType = "";
        totalkm = 0;
        totalPersonalKm = 0;
        Pva = 0;
        C = 0;
        S = 0;
        editTextPositionss = 0;
        oePosCnt = 0;
        lcPosCnt = 0;
        tvSize = 0;
        ttLod = 0;
        cnSty = 0;
        erlSty = 0;
        lteSty = 0;


        size = 0;
        lcSize = 0;
        OeSize = 0;
        daysBetween = 0;
        styDate = 0;

        tofuel = 0.0;
        ldgEliAmt = 0.0;
        ldgDrvEligi = 0.0;
        gTotal = 0.0;
        TotLdging = 0.0;
        GrandTotalAllowance = 0.0;
        fAmount = 0.0;
        doubleAmount = 0.0;
        myBrdAmt = 0.0;
        drvBrdAmt = 0.0;
        otherExp = 0.0;
        localCov = 0.0;
        sum = 0.0;
        BusAmount = 0.0;
        sumsTot = 0.0;

        TotDA = 0.0;
        sTotal = 0.0;
        sumsTa = 0.0;
        tTotAmt = 0.0;
        stayEgTotal = 0.0;
        tJointAmt = 0;

        changeStay = false;
        countLoding = 0;

        lodgArrLst = new ArrayList<>();


        jsonArray = null;
        ExpSetup = null;
        trvPlcsArray = null;
        jsonFuelAllowance = null;
        jsonExpHead = null;
        lcDraftArray = null;
        oeDraftArray = null;
        trvldArray = null;
        ldArray = null;
        travelDetails = null;
        LodingCon = null;
        StayDate = null;

        continueStay = 0.0;
        fuelAmt = 0.0;

        ValCd = "";
        txt_ldg_type.setText("");
        stayDays.setVisibility(View.GONE);
        lodgCont.setVisibility(View.GONE);
        ldg_stayloc.setVisibility(View.GONE);
        ldg_stayDt.setVisibility(View.GONE);
        lodgJoin.setVisibility(View.GONE);
        JNLdgEAra.setVisibility(View.GONE);
        //linImgPrv.setVisibility(View.GONE);

        if (myldgEliAmt.equalsIgnoreCase("")) myldgEliAmt = "0.0";

        myldgEliAmt = myldgEliAmt.replaceAll("^[\"']+|[\"']+$", "");
        jointLodging.removeAllViews();
        ldgEliAmt = Double.valueOf(myldgEliAmt);
        txtMyEligi.setText("₹" + new DecimalFormat("##0.00").format(ldgEliAmt));

        mChckCont.setChecked(false);
        mChckLate.setChecked(false);
        mChckEarly.setChecked(false);
        ttLod = 1;

        txtMyEligi.setText("₹" + new DecimalFormat("##0.00").format(ldgEliAmt));
        ldgWOBBal.setText("₹" + new DecimalFormat("##0.00").format(ldgEliAmt));
        lbl_ldg_eligi.setText("₹" + new DecimalFormat("##0.00").format(ldgEliAmt));
        img_lodg_prvw.setVisibility(View.VISIBLE);
        ldg_cout.setText("");
        ldg_coutDt.setText("");
        TotalDays.setVisibility(View.GONE);

        // edt_ldg_bill.setText("");

        SumOFJointLodging();
        SumOFLodging(0);

    }

    public void LateImage(View v) {
        CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());

        if (!cameraPermission.checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cameraPermission.requestPermission();
            }
            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
        } else {
            Log.v("PERMISSION", "PERMISSION");
            popupCapture(343);
            DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar calobjw = Calendar.getInstance();
            lodgLate = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();

        }
    }

    public void LateAttach(View v) {
        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
        stat.putExtra("position", lodgLate);
        stat.putExtra("headTravel", "LOD");
        stat.putExtra("mode", "LateMode");
        stat.putExtra("date", DateTime);
        startActivity(stat);
    }

    public void EarImage(View v) {
        CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());

        if (!cameraPermission.checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cameraPermission.requestPermission();
            }
            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
        } else {
            Log.v("PERMISSION", "PERMISSION");
            popupCapture(405);
            DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar calobjw = Calendar.getInstance();
            lodgEarly = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();

        }
    }

    public void EarAttach(View v) {
        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
        stat.putExtra("position", lodgEarly);
        stat.putExtra("headTravel", "LOD");
        stat.putExtra("mode", "EarlyMode");
        stat.putExtra("date", DateTime);
        startActivity(stat);
    }

    public void ShowTimePicker(String str) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(TAClaimActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String hour = String.format("%02d", (selectedHour));
                String min = String.format("%02d", (selectedMinute));

                if (str.equalsIgnoreCase("Lod_Check_In")) {
                    ldg_cin.setText(hour + ":" + min);
                    if (ldg_cout.getText().toString().equalsIgnoreCase(""))
                        ldg_cout.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Lod_Check_Out")) {
                    ldg_cout.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Ear_Check_In")) {
                    earCheckIn.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Ear_Check_Out")) {
                    earCheckOut.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Lat_Check_In")) {
                    latCheckIn.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Lat_Check_Out")) {
                    latCheckOut.setText(hour + ":" + min);
                }

                if (!ldg_cin.getText().toString().equalsIgnoreCase("") && !ldg_cout.getText().toString().equalsIgnoreCase("")) {
                    difference("3");
                }

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void ShowDatePicker(String str) {
        Calendar mcurrentTime = Calendar.getInstance();
        Log.d("SelDt", DateTime);
        String[] sDtPart = DateTime.split("-");
        int day = Integer.parseInt(sDtPart[2]);
        int mnth = Integer.parseInt(sDtPart[1]) - 1;
        int yr = Integer.parseInt(sDtPart[0]);
        picker = new DatePickerDialog(TAClaimActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ldg_coutDt.setText(year + "-" + ((monthOfYear < 9) ? "0" : "") + (monthOfYear + 1) + "-" + ((dayOfMonth < 10) ? "0" : "") + dayOfMonth);//(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        COutDate = year + "-" + ((monthOfYear < 9) ? "0" : "") + (monthOfYear + 1) + "-" + ((dayOfMonth < 10) ? "0" : "") + dayOfMonth;
                        nofNght = DT.Daybetween(DateTime + " 00:00:00", year + "-" + ((monthOfYear < 9) ? "0" : "") + (monthOfYear + 1) + "-" + ((dayOfMonth < 10) ? "0" : "") + dayOfMonth + " 00:00:00");
                        //if(nofNght==0) nofNght=1;
                        NoofNight.setText(" - " + nofNght + " Nights - ");
                        linContinueStay.setVisibility(View.VISIBLE);
                        // if (DT.Daybetween(DateTime + " 00:00:00", ldg_coutDt.getText().toString() + " 00:00:00") < 1)
                        //    linContinueStay.setVisibility(View.GONE);
                        getStayAllow();
                    }
                }, yr, mnth, day);
        Calendar calendarmin = Calendar.getInstance();
        Log.d("MINMonth", String.valueOf(mnth));
        calendarmin.set(yr, mnth, day);
        picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
        calendarmin.add(Calendar.DAY_OF_MONTH, 15);
        picker.getDatePicker().setMaxDate(calendarmin.getTimeInMillis());

        picker.show();

        /*DatePickerDialog mDtPicker;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mDtPicker=new DatePickerDialog(TAClaimActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    ldg_coutDt.setText(year + "-" + month+"-"+dayOfMonth);
                }
            })
        }
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(TAClaimActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String hour = String.format("%02d", (selectedHour));
                String min = String.format("%02d", (selectedMinute));

                if (str.equalsIgnoreCase("Lod_Check_In")) {
                    ldg_cin.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Lod_Check_Out")) {
                    ldg_coutDt.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Ear_Check_In")) {
                    earCheckIn.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Ear_Check_Out")) {
                    earCheckOut.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Lat_Check_In")) {
                    latCheckIn.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Lat_Check_Out")) {
                    latCheckOut.setText(hour + ":" + min);
                }

                if (!ldg_cin.getText().toString().equalsIgnoreCase("") && !ldg_cout.getText().toString().equalsIgnoreCase("")) {
                    difference("3");
                }

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();*/
    }

    public void onTPlsDelete(View v) {
        int size = travelPlaces.getChildCount();
        travelPlaces.removeView((View) v.getParent());

        LayoutTransition transition = new LayoutTransition();
        travelPlaces.setLayoutTransition(transition);
    }

    public void onTADelete(View v) {
        int size = travelDynamicLoaction.getChildCount();
        travelDynamicLoaction.removeView((View) v.getParent());

        LayoutTransition transition = new LayoutTransition();
        travelDynamicLoaction.setLayoutTransition(transition);

        if (size == 0) {
            crdDynamicLocation.setVisibility(View.GONE);
        }
        SumOFTAAmount();
//        if (StrToEnd.equals("0")) {
//            SumOFTAAmount();
//        } else {
//
//        }
    }

    public void onLCDelete(View v) {
        LinearLayout pv = (LinearLayout) v.getParent().getParent();

        edtRwID = pv.findViewById(R.id.lcRwID);
        if (!edtRwID.getText().toString().equalsIgnoreCase(""))
            uLCItems.remove(edtRwID.getText().toString());

        linlocalCon.removeView(pv);
        LayoutTransition transition = new LayoutTransition();
        linlocalCon.setLayoutTransition(transition);

        if (linlocalCon.getChildCount() == 0) {
            localTotal.setVisibility(View.GONE);
        }
        SumOFLCAmount();

    }

    public void onOEDelete(View v) {
        LinearLayout pv = (LinearLayout) v.getParent().getParent();

        edtRwID = pv.findViewById(R.id.oeRwID);
        if (!edtRwID.getText().toString().equalsIgnoreCase(""))
            uOEItems.remove(edtRwID.getText().toString());

        LinearOtherAllowance.removeView(pv);
        LayoutTransition transition = new LayoutTransition();
        LinearOtherAllowance.setLayoutTransition(transition);

        if (LinearOtherAllowance.getChildCount() == 0) {
            otherExpenseLayout.setVisibility(View.GONE);
        }

        SumOFOTAmount();
    }

    public void openFuleEntry(View v) {
        startActivity(new Intent(getApplicationContext(), FuleEntryActivity.class));
    }

    public void viewStaus(View v) {
        startActivity(new Intent(getApplicationContext(), ViewTAStatus.class));
    }

    public void ImagePdf(View v) {
        pdfViewList();
    }

    public void lodingView() {
        jointLodging.setVisibility(View.VISIBLE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(15, 15, 15, 15);
        rowView = inflater.inflate(R.layout.activity_loding_layout, null);
        jointLodging.addView(rowView, layoutParams);
        for (int i = 0; i < jointLodging.getChildCount(); i++) {
            View childView = jointLodging.getChildAt(i);
            ImageView deleteLod = (ImageView) childView.findViewById(R.id.ldg_delete);
            /*if (i == 0) {
                deleteLod.setVisibility(View.GONE);
            } else {
                deleteLod.setVisibility(View.VISIBLE);
            }*/
        }
    }

    public void onLodingDelete(View v) {
        if (jointLodging.getChildCount() > 1) {
            jointLodging.removeView((View) v.getParent());
        } else {

            View pv = jointLodging.getChildAt(0);
            txtJNName = pv.findViewById(R.id.txtJNName);
            txtJNDesig = pv.findViewById(R.id.txtJNDesig);
            txtJNDept = pv.findViewById(R.id.txtJNDept);
            txtJNHQ = pv.findViewById(R.id.txtJNHQ);
            txtJNMob = pv.findViewById(R.id.txtJNMob);
            txtJNMyEli = pv.findViewById(R.id.txtJNMyEli);

            txtJNName.setText("");
            txtJNDesig.setText("");
            txtJNDept.setText("");
            txtJNHQ.setText("");
            txtJNMob.setText("");
            float sum = 0;
            Log.v("Allowance_Amount", String.valueOf(sum));
            ldgEmpName = String.valueOf(sum);

            txtJNMyEli.setText("₹" + new DecimalFormat("##0.00").format(sum));
        }
        SumOFJointLodging();
    }

    public void onGetEmpDetails(View v) {
        View pv = (View) v.getParent().getParent();
        edt_ldg_JnEmp = pv.findViewById(R.id.edt_ldg_JnEmp);
        String sEmpID = String.valueOf(edt_ldg_JnEmp.getText());
        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
        if (sLocId.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Select the Stay Location !", Toast.LENGTH_LONG).show();
            return;
        }
        if (edt_ldg_JnEmp.getText().toString().equals("")) {

        } else {
            assistantClass.showProgressDialog("Getting employee details...", false);
            Map<String, String> params = new HashMap<>();
            params.put("axn", "get/EmpByID");
            params.put("logCode", lodgingStayTypeId);
            params.put("allowanceType", allowType);
            params.put("empId", sEmpID);
            params.put("date", DateTime);
            assistantClass.makeApiCall(params, "", new APIResult() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    txtJNName = pv.findViewById(R.id.txtJNName);
                    txtJNDesig = pv.findViewById(R.id.txtJNDesig);
                    txtJNDept = pv.findViewById(R.id.txtJNDept);
                    txtJNHQ = pv.findViewById(R.id.txtJNHQ);
                    txtJNMob = pv.findViewById(R.id.txtJNMob);
                    txtJNMyEli = pv.findViewById(R.id.txtJNMyEli);

                    JSONObject object = jsonObject.optJSONArray("response").optJSONObject(0);
                    txtJNName.setText(object.optString("Name"));
                    txtJNDesig.setText(object.optString("Desig"));
                    txtJNDept.setText(object.optString("DeptName"));
                    txtJNHQ.setText(object.optString("HQ"));
                    txtJNMob.setText(object.optString("Mob"));
                    double sum = jsonObject.optDouble("amount");
                    Log.v("Allowance_Amount", String.valueOf(sum));
                    ldgEmpName = String.valueOf(sum);

                    txtJNMyEli.setText("₹" + new DecimalFormat("##0.00").format(sum));
                    SumOFJointLodging();

                    assistantClass.dismissProgressDialog();
                }

                @Override
                public void onFailure(String error) {
                    assistantClass.dismissProgressDialog();
                    assistantClass.showAlertDialogWithDismiss(error);
                }
            });


            Call<JsonArray> Callto = apiInterface.getDataArrayListA("get/EmpByID",
                    UserDetails.getString("Divcode", ""),
                    UserDetails.getString("Sfcode", ""), sEmpID, sLocId, DateTime, null);
            Callto.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    JsonArray res = response.body();
                    if (res.size() < 1) {
                        Toast.makeText(getApplicationContext(), "Emp Code Not Found !", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JsonObject EmpDet = res.get(0).getAsJsonObject();
                    if (EmpDet.has("Msg")) {
                        if (!EmpDet.get("Msg").getAsString().equalsIgnoreCase("")) {
                            Toast.makeText(getApplicationContext(), EmpDet.get("Msg").getAsString(), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    txtJNName = pv.findViewById(R.id.txtJNName);
                    txtJNDesig = pv.findViewById(R.id.txtJNDesig);
                    txtJNDept = pv.findViewById(R.id.txtJNDept);
                    txtJNHQ = pv.findViewById(R.id.txtJNHQ);
                    txtJNMob = pv.findViewById(R.id.txtJNMob);
                    txtJNMyEli = pv.findViewById(R.id.txtJNMyEli);

                    txtJNName.setText(EmpDet.get("Name").getAsString());
                    txtJNDesig.setText(EmpDet.get("Desig").getAsString());
                    txtJNDept.setText(EmpDet.get("DeptName").getAsString());
                    txtJNHQ.setText(EmpDet.get("HQ").getAsString());
                    txtJNMob.setText(EmpDet.get("Mob").getAsString());
                    float sum = EmpDet.get("ldgAllow").getAsFloat();
                    Log.v("Allowance_Amount", String.valueOf(sum));
                    ldgEmpName = String.valueOf(sum);

                    txtJNMyEli.setText("₹" + new DecimalFormat("##0.00").format(sum));
                    SumOFJointLodging();
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Log.d("Error:", "Some Error" + t.getMessage());
                }
            });
        }
    }

    public void SumOFTAAmount() {
        BusAmount = 0.0;
        int lcSize = travelDynamicLoaction.getChildCount();
        for (int k = 0; k < lcSize; k++) {
            View cv = travelDynamicLoaction.getChildAt(k);
            enterFare = cv.findViewById(R.id.enter_fare);
            String str = enterFare.getText().toString();
            if (str.matches("")) str = "0";
            BusAmount = BusAmount + Double.parseDouble(str);
        }
        sumsTa = GrandTotalAllowance + BusAmount;
        txtTAamt.setText("₹" + new DecimalFormat("##0.00").format(BusAmount));

        calOverAllTotal(localCov, otherExp, tTotAmt);
    }

    public void SumOFLCAmount() {
        sum = 0.0;
        int lcSize = linlocalCon.getChildCount();
        for (int k = 0; k < lcSize; k++) {
            View cv = linlocalCon.getChildAt(k);
            editLaFare = (EditText) (cv.findViewById(R.id.edt_la_fare));
            String str = editLaFare.getText().toString();
            if (str.matches("")) str = "0";
            sum = sum + Double.parseDouble(str);
        }
        localText.setText("₹" + new DecimalFormat("##0.00").format(sum));
        localCov = sum;

        localTotal.setVisibility(View.VISIBLE);
        if (linlocalCon.getChildCount() == 0) {
            localTotal.setVisibility(View.GONE);
        }
        calOverAllTotal(localCov, otherExp, tTotAmt);
    }

    public void SumOFOTAmount() {
        sumsTot = 0.0;
        int OeSize = LinearOtherAllowance.getChildCount();
        for (int k = 0; k < OeSize; k++) {
            View cv = LinearOtherAllowance.getChildAt(k);
            edtOE = (EditText) (cv.findViewById(R.id.oe_fre_amt));
            String strs = edtOE.getText().toString();
            if (strs.matches("")) strs = "0";
            sumsTot = sumsTot + Double.parseDouble(strs);
            //sTotal = GrandTotalAllowance + sumsTot;

        }
        OeText.setText("₹" + new DecimalFormat("##0.00").format(sumsTot));

        otherExp = sumsTot;
        calOverAllTotal(localCov, otherExp, tTotAmt);
    }

    public void SumOFDAAmount() {
        String sAmt = txtallamt.getText().toString().replaceAll("₹", "");
        String sBrdAmt = txt_BrdAmt.getText().toString().replaceAll("₹", "");
        String sDrvBrdAmt = txt_DrvBrdAmt.getText().toString().replaceAll("₹", "");
        Log.e("STRDAILY", StrDailyAllowance);
        if (sAmt.equalsIgnoreCase("")) sAmt = "0.00";
        if (sBrdAmt.equalsIgnoreCase("")) sBrdAmt = "0.00";
        if (sDrvBrdAmt.equalsIgnoreCase("")) sDrvBrdAmt = "0.00";
        TotDA = Double.parseDouble(sAmt) + Double.parseDouble(sBrdAmt) + Double.parseDouble(sDrvBrdAmt);
        txt_totDA.setText("₹" + new DecimalFormat("##0.00").format(TotDA));
        calOverAllTotal(localCov, otherExp, tTotAmt);
    }

    public void SumOFJointLodging() {
        tJointAmt = 0;
        for (int i = 0; i < jointLodging.getChildCount(); i++) {
            View childView = jointLodging.getChildAt(i);
            TextView jLdgEli = (TextView) childView.findViewById(R.id.txtJNMyEli);
            String sAmt = jLdgEli.getText().toString().replaceAll("₹", "");
            sAmt = sAmt.replaceAll("Rs.", "").trim();
            if (sAmt.equals("")) {
                sAmt = "0.0";
            }
            tJointAmt = tJointAmt + Float.parseFloat(sAmt);
        }
        txtJNEligi.setText("₹" + new DecimalFormat("##0.00").format(tJointAmt));
        SumOFLodging(0);
    }

    public void SumOFLodging(Integer count) {

        String sMyAmt = txtMyEligi.getText().toString().replaceAll("₹", "");
        String sJnAmt = txtJNEligi.getText().toString().replaceAll("₹", "");
        String sDrivAmt = txtDrivEligi.getText().toString().replaceAll("₹", "");
        String sErlyAmt = edtEarBill.getText().toString().replaceAll("₹", "");
        String sLateAmt = edtLateBill.getText().toString().replaceAll("₹", "");

        if (sErlyAmt.equalsIgnoreCase("")) sErlyAmt = "0";
        if (sLateAmt.equalsIgnoreCase("")) sLateAmt = "0";
        tTotAmt = Double.parseDouble(sMyAmt) + ldgDrvEligi + Float.parseFloat(sJnAmt) + Double.parseDouble(sErlyAmt) + Double.parseDouble(sLateAmt);

        //ldgWOBBal.setText("₹" + new DecimalFormat("##0.00").format(ldgEliAmt));

        if (!mChckCont.isChecked())
            tTotAmt = continueStay + Double.parseDouble(sMyAmt) + ldgDrvEligi + Float.parseFloat(sJnAmt) + Double.parseDouble(sErlyAmt) + Double.parseDouble(sLateAmt);

        int IntValue = (int) tTotAmt;

        edt_ldg_bill.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, IntValue)});
        edtEarBill.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, IntValue)});


        String sBillAmt = edt_ldg_bill.getText().toString().replaceAll("₹", "");
        if (sBillAmt.isEmpty()) sBillAmt = "0";
        if ((nofNght < 1 && OnlyNight == 1) || transferflg == 1)
            tTotAmt = Float.parseFloat(sBillAmt);
        totLodgAmt = String.valueOf(tTotAmt);
        //  lbl_ldg_eligi.setText("₹" + new DecimalFormat("##0.00").format(tTotAmt));

        //tTotAmt = Double.parseDouble(sMyAmt) + ldgDrvEligi + Float.parseFloat(sJnAmt)+Double.parseDouble(sErlyAmt) +Double.parseDouble(sLateAmt) ;

        lbl_ldg_eligi.setText("₹" + new DecimalFormat("##0.00").format(tTotAmt));
        if ((nofNght < 1 && OnlyNight == 1) || transferflg == 1)
            txldgTdyAmt.setText("₹" + new DecimalFormat("##0.00").format(tTotAmt));


        Log.v("COunt_stay", String.valueOf(count));
        /*if (mChckCont.isChecked())//if (count == 1)
        {
            tTotAmt = 0;
            calOverAllTotal(localCov, otherExp, tTotAmt);
        } else {
            Log.v("TOTAL_continueStay", String.valueOf(continueStay));
            calOverAllTotal(localCov, otherExp, tTotAmt);
        }*/
        SumWOBLodging();

    }

    public void SumWOBLodging() {
        String sMyAmt = txtMyEligi.getText().toString().replaceAll("₹", "");
        String sJnAmt = txtJNEligi.getText().toString().replaceAll("₹", "");
        String sldgAmt = txtDrivEligi.getText().toString().replaceAll("₹", "");

        if (sMyAmt.isEmpty()) {
            sMyAmt = "0";
        }
        if (sJnAmt.isEmpty()) {
            sJnAmt = "0";
        }
        if (sldgAmt.isEmpty()) {
            sldgAmt = "0";
        }

        Double totalBill = Double.parseDouble(sMyAmt) + Double.parseDouble(sJnAmt) + Double.parseDouble(sldgAmt);

        if (isSavedBill) {
            isSavedBill = false;
            edt_ldg_bill.setText(billAmts);
        }
        String bill = edt_ldg_bill.getText().toString().replaceAll("₹", "");

        if (bill.isEmpty()) {
            bill = "0";
        };

        tTotAmt = Float.parseFloat(bill);
        if (isFullPay) {
            tTotAmt = totalBill;
        }

        if (mChckCont.isChecked()) {
            tTotAmt = 0;
        }
        txldgTdyAmt.setText("₹" + new DecimalFormat("##0.00").format(tTotAmt));
        double tBalAmt = totalBill - tTotAmt;

        if (!mChckCont.isChecked()) {
            tTotAmt = continueStay + tTotAmt;
        }
        lbl_ldg_eligi.setText("₹" + new DecimalFormat("##0.00").format(tTotAmt));

        if ((nofNght < 1 && OnlyNight == 1) || transferflg == 1) {
            tBalAmt = 0;
        }
        witOutBill = String.valueOf(tBalAmt);
        ldgWOBBal.setText("₹" + new DecimalFormat("##0.00").format(tBalAmt));

        calOverAllTotal(localCov, otherExp, tTotAmt);
        // }

    }

    private void calOverAllTotal(Double localCov, Double otherExp, double tTotAmt) {
        Log.v("tTotAmt", String.valueOf(tTotAmt));

        String strldgTotal = lbl_ldg_eligi.getText().toString().substring(lbl_ldg_eligi.getText().toString().indexOf(".") + 1).trim();
        String separators = ".";
        int intldgTotal = strldgTotal.lastIndexOf(separators);

        gTotal = localCov + TotDA + otherExp + BusAmount + tofuel + tTotAmt;
        //if (tTotAmt == 0.0) {
        /*} else {
            if (LodingCon.size() != 0) {
                mChckEarly.setVisibility(View.GONE);
                Integer ValueTotal = LodingCon.size() + 1;
                Double TotalVal = ValueTotal * Double.valueOf(strldgTotal.substring(0, intldgTotal));
            }
            gTotal = localCov + myBrdAmt + drvBrdAmt + otherExp + GrandTotalAllowance + Double.valueOf(tTotAmt);
                // gTotal = localCov + myBrdAmt + drvBrdAmt + otherExp + GrandTotalAllowance + Double.valueOf(strldgTotal.substring(0, intldgTotal));
        }*/

        // gTotal= Double.valueOf(Math.round(gTotal));
        grandTotal.setText("₹" + new DecimalFormat("##0.00").format(gTotal));

    }

    /*Toolbar*/
    public void getToolbar() {


        ImageView imgHome = findViewById(R.id.toolbar_home);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });
    }

    public void openHome() {
        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
        Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
        Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
        Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");

        if (CheckIn == true) {
            Intent Dashboard = new Intent(TAClaimActivity.this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }

    /*Choosing Dynamic date*/
    public void dynamicDate() {
        JSONObject jj = new JSONObject();
        try {
            jj.put("sfCode", SF_code);
            jj.put("divisionCode", div);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.getTADate(jj.toString());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    String id = String.valueOf(jsonObject.get("id"));
                    String name = String.valueOf(jsonObject.get("Datewithname"));
                    Log.e("getTADate", name);
                    name = name.replaceAll("^[\"']+|[\"']+$", "");
                    mCommon_model_spinner = new Common_Model(id, name, "");
                    modelRetailDetails.add(mCommon_model_spinner);
                }
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
                scrlMain.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
    }

    /*Display Mode of travel View based on the choosed Date*/
    public void displayTravelMode(String ChoosedDate) {
        try {

            ChoosedDate = ChoosedDate.replaceAll("^[\"']+|[\"']+$", "");
            String[] sadt = ChoosedDate.split("-");
            txEligDt.setText(sadt[2] + "/" + sadt[1] + "/" + sadt[0]);

            SimpleDateFormat dt1 = new SimpleDateFormat("dd MMMM yyyy");


            TextCheckInDate.setText(ChoosedDate);
            ldg_coutDt.setText(ChoosedDate);
            DateTime = ChoosedDate;
            CInDate = ChoosedDate;
            COutDate = ChoosedDate;

            JSONObject jj = new JSONObject();
            try {
                jj.put("sfCode", mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
                jj.put("divisionCode", mShared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
                jj.put("Selectdate", ChoosedDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("Json_date_fomrat", jj.toString());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> call = apiInterface.getTAdateDetails(jj.toString()); // Todo: getTAdateDetails
            final String finalChoosedDate = ChoosedDate;
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    fuelAmt = 0.0;
                    JsonObject jsonObjects = response.body();

                    Log.v("JSON_TRAVEL_DETAILS", jsonObjects.toString());
                    ExpSetup = jsonObjects.getAsJsonArray("Settings");

                    jsonFuelAllowance = jsonObjects.getAsJsonArray("FuelAllowance");
                    jsonArray = jsonObjects.getAsJsonArray("TodayStart_Details");
                    lcDraftArray = jsonObjects.getAsJsonArray("Additional_ExpenseLC");
                    oeDraftArray = jsonObjects.getAsJsonArray("Additional_ExpenseOE");
                    trvldArray = jsonObjects.getAsJsonArray("Travelled_Loc");
                    trvPlcsArray = jsonObjects.getAsJsonArray("Travelled_Plcs");
                    ldArray = jsonObjects.getAsJsonArray("Lodging_Head");
                    travelDetails = jsonObjects.getAsJsonArray("Travelled_Details");
                    LodingCon = jsonObjects.getAsJsonArray("LodDtlist"); // Todo: LodingConArray
                    StayDate = jsonObjects.getAsJsonArray("Stay_Date_time"); // Todo: StayDateArray
                    jsonExpHead = jsonObjects.getAsJsonArray("Expense_Head");

                    Log.v("jsonFuelAllowance", jsonFuelAllowance.toString());

                    btn_sub.setVisibility(View.VISIBLE);
                    buttonSave.setVisibility(View.VISIBLE);

                    TravelBike.setVisibility(View.GONE);
                    linMode.setVisibility(View.GONE);
                    linBusMode.setVisibility(View.GONE);
                    linBikeMode.setVisibility(View.GONE);
                    JsonObject itmSetup = ExpSetup.get(0).getAsJsonObject();
                    OnlyNight = itmSetup.get("NgtOnlyFlag").getAsInt();
                    transferflg = itmSetup.get("TRFlag").getAsInt();
                    TWMax_Km = itmSetup.get("TWMax_Km").getAsInt();
                    FWMax_Km = itmSetup.get("FWMax_Km").getAsInt();
                    VHRetFlag=itmSetup.get("VHRetNeeded").getAsInt();

                    if(VHRetFlag==1){
                        btnVehiRet.setVisibility(View.VISIBLE);
                    }
                    else if(VHRetFlag==0){
                        btnVehiRet.setVisibility(View.GONE);
                    }

                    if (jsonFuelAllowance != null || jsonFuelAllowance.size() != 0) {
                        Log.v("jsonFuelAllowance_IN", jsonFuelAllowance.toString());
                        fuelListAdapter = new FuelListAdapter(getApplicationContext(), jsonFuelAllowance, TWMax_Km, FWMax_Km);
                        mFuelRecycler.setAdapter(fuelListAdapter);
                        fuelListAdapter.notifyDataSetChanged();
                        JsonObject jsFuel;
                        linMode.setVisibility(View.VISIBLE);
                        TravelBike.setVisibility(View.VISIBLE);
                        linBikeMode.setVisibility(View.VISIBLE);



                        if (jsonFuelAllowance.size() < 1) {
                            TravelBike.setVisibility(View.GONE);
                            linBikeMode.setVisibility(View.GONE);
                        }
                        for (int jf = 0; jf < jsonFuelAllowance.size(); jf++) {
                            jsFuel = jsonFuelAllowance.get(jf).getAsJsonObject();
                            if (!jsFuel.get("End_Km").getAsString().equalsIgnoreCase("")) {
                                Integer start = Integer.valueOf(Common_Class.isNullOrEmpty(jsFuel.get("Start_Km").getAsString()) ? "0" :
                                        jsFuel.get("Start_Km").getAsString());
                                Integer end = Integer.valueOf(Common_Class.isNullOrEmpty(jsFuel.get("End_Km").getAsString()) ? "0" : jsFuel.get("End_Km").getAsString());
                                Shared_Common_Pref.MaxKm = jsFuel.get("Maxkm").getAsInt();

                                if (end != 0) {
                                    String total = String.valueOf(end - start);
                                    Integer Total = Integer.valueOf(total);

                                    if (jsFuel.get("MOT_Name").getAsString().equals("Two Wheeler")) {
                                        if (Total >= TWMax_Km) Total = TWMax_Km;
                                    } else if (jsFuel.get("MOT_Name").getAsString().equals("Four Wheeler")) {
                                        if (Total >= FWMax_Km) Total = FWMax_Km;
                                    }





//                                    if (jsFuel.get("MOT_Name").getAsString().contains("Ret Vehicle") || jsFuel.get("MOT_Name").getAsString().contains("Two Wheeler")){
//                                        btnVehiRet.setVisibility(View.GONE);
//                                    }
//
//                                    if (jsFuel.get("MOT_Name").getAsString().contains("Four Wheeler")){
//                                        btnVehiRet.setVisibility(View.VISIBLE);
//                                    }





                                    Integer Personal = Integer.valueOf(Common_Class.isNullOrEmpty(jsFuel.get("Personal_Km").getAsString()) ? "0" : jsFuel.get("Personal_Km").getAsString());
                                    String TotalPersonal = String.valueOf(Total - Personal);
                                    Double q = Double.valueOf(TotalPersonal);
                                    Double z = Double.valueOf(jsFuel.get("FuelAmt").getAsString());
                                    String qz = String.valueOf(q * z);
                                    Log.v("TA_FUEL_TOTAL", qz);

                                    fuelAmt = fuelAmt + (q * z);
                                    fuelAmount.setText("₹" + fuelAmt);

                                    TextTotalAmount.setText("₹" + new DecimalFormat("##0.00").format(fuelAmt));
                                }
                                if (start != 0 && end == 0) {
                                    btn_sub.setVisibility(View.GONE);
                                    buttonSave.setVisibility(View.GONE);

                                }
                            }
                        }
                    }
                    JsonObject jRremarks = null;
                    for (int i = 0; i < jsonExpHead.size(); i++) {
                        jRremarks = jsonExpHead.get(i).getAsJsonObject();
                        String strRemarks = jRremarks.get("Reason").getAsString();
                        editTextRemarks.setText(strRemarks);
                    }

                    if (jsonArray != null || jsonArray.size() != 0) {
                        JsonObject jsonObject = null;
                        for (int i = 0; i < jsonArray.size(); i++) {
                            int finalC = i;

                            Log.e("JsonArray", String.valueOf(jsonArray.size()));
                            //jsonObject = (JSONObject) jsonArray.get(i);

                            jsonObject = jsonArray.get(i).getAsJsonObject();
                            linDailyAllowance.setVisibility(View.VISIBLE);

                            StartedKm = jsonObject.get("Start_Km").getAsString();
                            StrToEnd = (jsonObject.get("StEndNeed").getAsString());
                            StrBus = jsonObject.get("From_Place").getAsString();
                            StrTo = jsonObject.get("To_Place").getAsString();
                            StrDaName = jsonObject.get("MOT_Name").getAsString();
                            Alw_Eligibilty = jsonObject.get("Alw_Eligibilty").getAsString();
                            StrDailyAllowance = jsonObject.get("dailyAllowance").getAsString();
                            strFuelAmount = jsonObject.get("FuelAmt").getAsString();
                            allowanceAmt = jsonObject.get("Allowance_Value").getAsString();
                            myldgEliAmt = jsonObject.get("myLdgAmt").getAsString();
                            myBrdEliAmt = jsonObject.get("myBrdAmt").getAsString();
                            drvldgEliAmt = jsonObject.get("drvLdgAmt").getAsString();
                            sDALocId = jsonObject.get("To_Place").getAsString();
                            sDALocName = jsonObject.get("To_Place_Id").getAsString();
                            String AttaFlg = jsonObject.get("AttaFL").getAsString();

                            drvBrdEliAmt = jsonObject.get("drvBrdAmt").getAsString();
                            start_Image = jsonObject.get("start_Photo").getAsString();
                            End_Imge = jsonObject.get("End_photo").getAsString();
                            ClosingKm = jsonObject.get("End_Km").getAsString();
                            PersonalKm = jsonObject.get("Personal_Km").getAsString();
                            DriverNeed = jsonObject.get("driverAllowance").getAsString();


                            Log.v("ELIGIBLE_AMOUNT", myldgEliAmt);

                            JsonObject js = null;
                            for (int l = 0; l < travelDetails.size(); l++) {
                                if (js != null) {
                                    js = (JsonObject) travelDetails.get(l);
                                    taAmt = js.get("ta_total_amount").getAsString();
                                    txtTAamt.setText("₹" + taAmt + ".00");
                                }
                            }


/*
                            Log.v("DRIVER", ClosingKm);
                            Glide.with(getApplicationContext())
                                    .load(start_Image.replaceAll("^[\"']+|[\"']+$", ""))
                                    .into(startkmimage);
                            Glide.with(getApplicationContext())
                                    .load(End_Imge.replaceAll("^[\"']+|[\"']+$", ""))
                                    .into(endkmimage);

                            if (!start_Image.matches("")) {
                                startkmimage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                                        intent.putExtra("ImageUrl", start_Image);

                                        startActivity(intent);
                                    }
                                });
                            }
                            if (!End_Imge.matches("")) {
                                endkmimage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                                        intent.putExtra("ImageUrl", End_Imge);
                                        startActivity(intent);
                                    }
                                });
                            }

                            if (StartedKm != null && !StartedKm.isEmpty() && !StartedKm.equals("null") && !StartedKm.equals("")) {
                                S = Integer.valueOf(StartedKm);
                                TxtStartedKm.setText(StartedKm);
                                fAmount = Double.valueOf(strFuelAmount);
                                fuelAmount.setText("₹" + new DecimalFormat("##0.00").format(fAmount) + " / KM ");

                                btn_sub.setVisibility(View.GONE);
                                buttonSave.setVisibility(View.GONE);

                                TravelBike.setVisibility(View.VISIBLE);
                                linMode.setVisibility(View.VISIBLE);
                                linBikeMode.setVisibility(View.VISIBLE);
                            }
                            if (!ClosingKm.equals("0")) {
                                StartedKm = StartedKm.replaceAll("^[\"']+|[\"']+$", "");
                                PersonalKm = PersonalKm.replaceAll("^[\"']+|[\"']+$", "");

                                if (PersonalKm.equals("null")) PersonalKm="0";
                                PersonalKiloMeter.setText(PersonalKm);

                                if (ClosingKm != null && !ClosingKm.isEmpty() && !ClosingKm.equals("null") && !ClosingKm.equals("")) {
                                    ClosingKm = ClosingKm.replaceAll("^[\"']+|[\"']+$", "");
                                    TxtClosingKm.setText(ClosingKm);
                                    C = Integer.valueOf(ClosingKm);
                                    totalkm = C - S;
                                    TotalTravelledKm.setText(String.valueOf(totalkm));
                                }

                                if (PersonalKm != null && !PersonalKm.isEmpty() && !PersonalKm.equals("null") && !PersonalKm.equals("")) {
                                    PersonalKm = PersonalKm.replaceAll("^[\"']+|[\"']+$", "");
                                    Pva = Integer.valueOf(PersonalKm);
                                    totalPersonalKm = totalkm - Pva;
                                }

                                Integer TtrvKm=totalPersonalKm;
                                if (TtrvKm > 200 && StrDaName.equals("Two Wheeler")) TtrvKm=200;
                                if (TtrvKm > 500 && StrDaName.equals("Four Wheeler")) TtrvKm=500;
                                PersonalTextKM.setText(String.valueOf(TtrvKm));
                                Double totalAmount = Double.valueOf(strFuelAmount);
                                tofuel = TtrvKm * totalAmount;
                                txtMaxKm.setVisibility(View.GONE);

                                btn_sub.setVisibility(View.VISIBLE);
                                buttonSave.setVisibility(View.VISIBLE);

                            }*/
                            txtTaClaim.setText(StrDaName);
                            if (jsonObject.get("HQ_Type").isJsonNull() == false) {
                                txtAllwType.setText(jsonObject.get("HQ_Type").getAsString());
                            }
                            txtDailyAllowance.setText(StrDailyAllowance + " - " + StrTo);
                            myBrdAmt = 0.0;
                            drvBrdAmt = 0.0;
                            vwDrvBoarding.setVisibility(View.GONE);
                            if (StrDailyAllowance.equals("Out Station")) {
                                doubleAmount = 0.0;
                                txtallamt.setText("");
                                myBrdEliAmt = myBrdEliAmt.replaceAll("^[\"']+|[\"']+$", "");
                                myBrdAmt = Double.valueOf(myBrdEliAmt);
                                txt_BrdAmt.setText("₹" + new DecimalFormat("##0.00").format(myBrdAmt));
                                if (DriverNeed.equalsIgnoreCase("true")) {
                                    drvBrdEliAmt = drvBrdEliAmt.replaceAll("^[\"']+|[\"']+$", "");
                                    drvBrdAmt = Double.valueOf(drvBrdEliAmt);
                                    txt_DrvBrdAmt.setText("₹" + new DecimalFormat("##0.00").format(drvBrdAmt));

                                    vwDrvBoarding.setVisibility(View.VISIBLE);
                                    txtDrvrBrod.setText("Driver Allowance Boarding");
                                } else {
                                    txt_DrvBrdAmt.setText("");
                                }

                                vwBoarding.setVisibility(View.VISIBLE);
                                SumOFDAAmount();
                            } else {
                                allowanceAmt = allowanceAmt.replaceAll("^[\"']+|[\"']+$", "");
                                doubleAmount = Double.valueOf(allowanceAmt);
                                myBrdAmt = 0.0;
                                txtallamt.setText("₹" + new DecimalFormat("##0.00").format(doubleAmount));
                                txt_BrdAmt.setText("₹0.00");

                                if (DriverNeed.equalsIgnoreCase("true")) {
                                    drvBrdEliAmt = drvBrdEliAmt.replaceAll("^[\"']+|[\"']+$", "");
                                    drvBrdAmt = Double.valueOf(drvBrdEliAmt);
                                    txt_DrvBrdAmt.setText("₹" + new DecimalFormat("##0.00").format(drvBrdAmt));

                                    vwDrvBoarding.setVisibility(View.VISIBLE);
                                    txtDrvrBrod.setText("Driver Allowance Boarding");
                                } else {
                                    txt_DrvBrdAmt.setText("");
                                }
                                vwBoarding.setVisibility(View.GONE);
                                SumOFDAAmount();
                            }

                            viw.setVisibility(View.GONE);
                            lin.setVisibility(View.GONE);
                            if (StrToEnd.equals("0") && trvldArray.size() < 1) {
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                layoutParams.setMargins(15, 15, 15, 15);
                                final View rowView = inflater.inflate(R.layout.travel_allowance_dynamic, null);

                                travelDynamicLoaction.addView(rowView, layoutParams);
                                tvSize = travelDynamicLoaction.indexOfChild(rowView);

                                View tvchildView = travelDynamicLoaction.getChildAt(tvSize);
                                viw.setVisibility(View.VISIBLE);
                                lin.setVisibility(View.VISIBLE);

                                LinearLayout lad = (LinearLayout) tvchildView.findViewById(R.id.linear_row_ad);
                                editText = (TextView) tvchildView.findViewById(R.id.enter_mode);
                                enterFrom = tvchildView.findViewById(R.id.enter_from);
                                enterTo = tvchildView.findViewById(R.id.enter_to);
                                enterFare = tvchildView.findViewById(R.id.enter_fare);
                                tvTxtUKey = (TextView) (tvchildView.findViewById(R.id.txt_tv_ukey));

                                tvGstLayout= tvchildView.findViewById(R.id.trvAll_gstLayout);

                                TextView txRwID = tvchildView.findViewById(R.id.TARwID);

                                editText.setText(StrDaName);
                                enterFrom.setText(StrBus);
                                enterTo.setText(StrTo);

                                if (Alw_Eligibilty.equalsIgnoreCase("0")) {
                                    enterFare.setText("0");
                                    enterFare.setEnabled(false);
                                } else {
                                    //   enterFare.setText(tldraftJson.get("Fare").getAsString());
                                    enterFare.setEnabled(true);
                                }
                                enterFare.setVisibility(View.VISIBLE);
                                if (StrDaName.equalsIgnoreCase("NONE")) {
                                    enterFare.setText("0");
                                    enterFare.setVisibility(View.GONE);
                                    tvGstLayout.setVisibility(View.GONE);
                                }

                                deleteButton = tvchildView.findViewById(R.id.delete_button);
                                taAttach = (ImageView) tvchildView.findViewById(R.id.image_attach);
                                previewss = (ImageView) tvchildView.findViewById(R.id.image_preview);
                                enterFare.setVisibility(View.VISIBLE);
                                taAttach.setVisibility(View.VISIBLE);
                                previewss.setVisibility(View.VISIBLE);
                                tvGstLayout.setVisibility(View.VISIBLE);


                                String sRWID = StrDaName + "_" + System.nanoTime();
                                txRwID.setText(sRWID);

                                List<CtrlsListModel.Ctrls> users = new ArrayList<>();
                                CtrlsListModel.Ctrls Ctrl = new CtrlsListModel.Ctrls("From", enterFrom);
                                users.add(Ctrl);
                                Ctrl = new CtrlsListModel.Ctrls("To", enterTo);
                                users.add(Ctrl);
                                CtrlsListModel UTAItem = new CtrlsListModel(users, AttaFlg);
                                uTAItems.put(sRWID, UTAItem);
                                if(AttaFlg.equals("0")){
                                    enterFare.setVisibility(View.GONE);
                                    taAttach.setVisibility(View.GONE);
                                    previewss.setVisibility(View.GONE);
                                    tvGstLayout.setVisibility(View.GONE);
                                }
                                enterFare.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        SumOFTAAmount();
                                    }
                                });
                                taAttach.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());

                                        if (!cameraPermission.checkPermission()) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                cameraPermission.requestPermission();
                                            }
                                            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                                        } else {
                                            Log.v("PERMISSION", "PERMISSION");

                                            Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                                            View view = travelDynamicLoaction.getChildAt(tvSizes);
                                            editText = (TextView) (view.findViewById(R.id.enter_mode));
                                            enterFare = (EditText) view.findViewById(R.id.enter_fare);
                                            tvTxtUKeys = (TextView) (view.findViewById(R.id.txt_tv_ukey));
                                            editMode = editText.getText().toString();

                                            Log.v("Travel_Location_imagw", editMode);

                                            /*if (tvTxtUKeys.getText().toString().equals("")) {
                                                DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                                Calendar calobjw = Calendar.getInstance();
                                                tvEditcnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                                                tvTxtUKeys.setText(tvEditcnt);
                                            }
                                            TlUKey = tvTxtUKeys.getText().toString();*/
                                            if (editText.getText().toString().equalsIgnoreCase(""))
                                                mCommon_class.showMsg(TAClaimActivity.this, "Select the Mode");
                                            else popupCapture(123);


                                        }
                                    }
                                });
                                previewss.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                                        View view = travelDynamicLoaction.getChildAt(tvSizes);
                                        editText = (TextView) (view.findViewById(R.id.enter_mode));
                                        enterFare = (EditText) view.findViewById(R.id.enter_fare);
                                        tvTxtUKeys = (TextView) (view.findViewById(R.id.txt_tv_ukey));
                                        editMode = editText.getText().toString();
                                        TlUKey = tvTxtUKeys.getText().toString();
                                        Log.v("Travel_Location_preview", editMode);
                                        AttachementActivity.setOnAttachmentDeleteListener(new OnAttachmentDelete() {
                                            @Override
                                            public void OnImageDelete(String Mode, int ImgCount) {
                                                if (ImgCount < 1) {
                                                    tvTxtUKeys.setText("");
                                                }
                                            }
                                        });
                                        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
                                        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                                        stat.putExtra("position", TlUKey);
                                        stat.putExtra("headTravel", "TL");
                                        stat.putExtra("mode", editMode);
                                        stat.putExtra("date", DateTime);
                                        startActivity(stat);

                                    }
                                });
                            }
                        }
                    }
                    double ofare = 0.0;
                    if (trvldArray.size() > 0) {
                        StrBus = StrBus.replaceAll("^[\"']+|[\"']+$", "");
                        StrTo = StrTo.replaceAll("^[\"']+|[\"']+$", "");
                        for (int j = 0; j < trvldArray.size(); j++) {
                            Integer finc = j;
                            JsonObject tldraftJson = (JsonObject) trvldArray.get(j);
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            layoutParams.setMargins(15, 15, 15, 15);
                            final View rowView = inflater.inflate(R.layout.travel_allowance_dynamic, null);

                            travelDynamicLoaction.addView(rowView, layoutParams);
                            tvSize = travelDynamicLoaction.indexOfChild(rowView);

                            View tvchildView = travelDynamicLoaction.getChildAt(tvSize);
                            viw.setVisibility(View.VISIBLE);
                            lin.setVisibility(View.VISIBLE);

                            LinearLayout lad = (LinearLayout) tvchildView.findViewById(R.id.linear_row_ad);
                            editText = (TextView) tvchildView.findViewById(R.id.enter_mode);
                            enterFrom = tvchildView.findViewById(R.id.enter_from);
                            enterTo = tvchildView.findViewById(R.id.enter_to);
                            enterFare = tvchildView.findViewById(R.id.enter_fare);
                            tvTxtUKey = (TextView) (tvchildView.findViewById(R.id.txt_tv_ukey));
                            tvGstLayout = tvchildView.findViewById(R.id.trvAll_gstLayout);
                            edtTVGstNum = tvchildView.findViewById(R.id.edt_trvAll_gst);
                            edtTVGstAmt = tvchildView.findViewById(R.id.edt_trvAll_gst_amt);
                            edtTVGstBillNo = tvchildView.findViewById(R.id.edt_trvAll_gst_bno);


                            TextView txRwID = tvchildView.findViewById(R.id.TARwID);
                            ImageView imgAtt = tvchildView.findViewById(R.id.image_attach);
                            ImageView imgPrv = tvchildView.findViewById(R.id.image_preview);

                            editText.setText("" + tldraftJson.get("Mode").getAsString());
                            enterFrom.setText(tldraftJson.get("From_P").getAsString());
                            enterTo.setText(tldraftJson.get("To_P").getAsString());

                            edtTVGstNum.setText(tldraftJson.get("GSTNo").getAsString());
                            edtTVGstAmt.setText(tldraftJson.get("GSTAmt").getAsString());
                            edtTVGstBillNo.setText(tldraftJson.get("GSTBNo").getAsString());

                            if (tldraftJson.get("Alw_Eligibilty").getAsString().equalsIgnoreCase("0")) {
                                enterFare.setText("0");
                                enterFare.setEnabled(false);
                            } else {
                                enterFare.setText(tldraftJson.get("Fare").getAsString());
                                enterFare.setEnabled(true);
                            }
                            enterFare.setVisibility(View.VISIBLE);
                            if (editText.getText().toString().equalsIgnoreCase("NONE")) {
                                enterFare.setText("0");
                                enterFare.setVisibility(View.GONE);
                                tvGstLayout.setVisibility(View.GONE);
                            }


                            String sRWID = tldraftJson.get("Mode").getAsString() + "_" + System.nanoTime();
                            txRwID.setText(sRWID);

                            String AttFlg = tldraftJson.get("Attachments").getAsString();
                            int maxVal = tldraftJson.get("Max_Allowance").getAsInt();
                            if (maxVal == 0) maxVal = 50000;

                            enterFare.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, maxVal)});

                            List<CtrlsListModel.Ctrls> users = new ArrayList<>();
                            CtrlsListModel.Ctrls Ctrl = new CtrlsListModel.Ctrls("From", enterFrom);
                            users.add(Ctrl);
                            Ctrl = new CtrlsListModel.Ctrls("To", enterTo);
                            users.add(Ctrl);
                            CtrlsListModel UTAItem = new CtrlsListModel(users, AttFlg);
                            uTAItems.put(sRWID, UTAItem);
                            if (AttFlg.equals("1")) {
                                imgAtt.setVisibility(View.VISIBLE);
                                imgPrv.setVisibility(View.VISIBLE);
                                tvGstLayout.setVisibility(View.VISIBLE);

                            } else {
                                imgAtt.setVisibility(View.GONE);
                                imgPrv.setVisibility(View.GONE);
                                tvGstLayout.setVisibility(View.GONE);

                            }


                            if (!tldraftJson.get("Ukey").getAsString().equals("") &&
                                    !tldraftJson.get("Ukey").getAsString().isEmpty() &&
                                    tldraftJson.get("Ukey").getAsString() != null) {
                                tvTxtUKey.setText(tldraftJson.get("Ukey").getAsString());
                            }

                            editMode = editText.getText().toString();

                            Log.v("Travel_Location", editMode);

                            tvGstLayout = tvchildView.findViewById(R.id.trvAll_gstLayout);

                            deleteButton = tvchildView.findViewById(R.id.delete_button);
                            taAttach = (ImageView) tvchildView.findViewById(R.id.image_attach);
                            previewss = (ImageView) tvchildView.findViewById(R.id.image_preview);
                            enterFare.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    SumOFTAAmount();
                                }
                            });
                            taAttach.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());

                                    if (!cameraPermission.checkPermission()) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            cameraPermission.requestPermission();
                                        }
                                        Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                                    } else {
                                        Log.v("PERMISSION", "PERMISSION");

                                        Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                                        View view = travelDynamicLoaction.getChildAt(tvSizes);
                                        editText = (TextView) (view.findViewById(R.id.enter_mode));
                                        enterFare = (EditText) view.findViewById(R.id.enter_fare);
                                        tvTxtUKeys = (TextView) (view.findViewById(R.id.txt_tv_ukey));
                                        editMode = editText.getText().toString();

                                        Log.v("Travel_Location_imagw", editMode);

                                        if (editText.getText().toString().equalsIgnoreCase(""))
                                            mCommon_class.showMsg(TAClaimActivity.this, "Select the Mode");
                                        else popupCapture(123);


                                    }
                                }
                            });
                            previewss.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                                    View view = travelDynamicLoaction.getChildAt(tvSizes);
                                    editText = (TextView) (view.findViewById(R.id.enter_mode));
                                    enterFare = (EditText) view.findViewById(R.id.enter_fare);
                                    tvTxtUKeys = (TextView) (view.findViewById(R.id.txt_tv_ukey));
                                    editMode = editText.getText().toString();
                                    TlUKey = tvTxtUKeys.getText().toString();
                                    Log.v("Travel_Location_preview", editMode);
                                    AttachementActivity.setOnAttachmentDeleteListener(new OnAttachmentDelete() {
                                        @Override
                                        public void OnImageDelete(String Mode, int ImgCount) {
                                            if (ImgCount < 1) {
                                                tvTxtUKeys.setText("");
                                            }
                                        }
                                    });
                                    DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
                                    Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                                    stat.putExtra("position", TlUKey);
                                    stat.putExtra("headTravel", "TL");
                                    stat.putExtra("mode", editMode);
                                    stat.putExtra("date", DateTime);
                                    startActivity(stat);

                                }
                            });
                            ofare = ofare + tldraftJson.get("Fare").getAsFloat();
                            SumOFTAAmount();
                            if (j == 0) {
                                deleteButton.setVisibility(View.GONE);
                                lad.setOnClickListener(null);
                                editText.setOnClickListener(null);
                                editText.setClickable(false);
                                enterFrom.setEnabled(false);
                                if (tldraftJson.get("To_P").getAsString().equals("")) {

                                } else {
                                    enterTo.setEnabled(false);
                                }
                            }
                        }
                    }
                    tofuel = fuelAmt;
                    BusAmount = ofare; //+ fuelAmt;
                    /*Local Convenyance*/
                    if (lcDraftArray != null || lcDraftArray.size() != 0) {
                        localConDraft(lcDraftArray);
                    }
                    if (oeDraftArray != null || oeDraftArray.size() != 0) OeDraft(oeDraftArray);

                   /*if(.size()>0){
                        StrBus = StrBus.replaceAll("^[\"']+|[\"']+$", "");
                        StrTo = StrTo.replaceAll("^[\"']+|[\"']+$", "");
                        for (int j = 0; j < trvPlcsArray.size(); j++) {
                            Integer finc = j;
                            JsonObject tldraftJson = (JsonObject) trvPlcsArray.get(j);
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            layoutParams.setMargins(15, 15, 15, 15);
                            final View rowView = inflater.inflate(R.layout.travel_allowance_dynamic_one, null);

                            travelPlaces.addView(rowView, layoutParams);
                            tvSize = travelPlaces.indexOfChild(rowView);

                            View tvchildView = travelPlaces.getChildAt(tvSize);
                            viw.setVisibility(View.VISIBLE);
                            lin.setVisibility(View.VISIBLE);

                            LinearLayout lad = (LinearLayout) tvchildView.findViewById(R.id.linear_row_ad);
                            //editText = (TextView) tvchildView.findViewById(R.id.enter_mode);
                            enterFrom = tvchildView.findViewById(R.id.enter_from);
                            enterTo = tvchildView.findViewById(R.id.enter_to);

                            //editText.setText("" + tldraftJson.get("Mode").getAsString());
                            enterFrom.setText(tldraftJson.get("From_P").getAsString());
                            enterTo.setText(tldraftJson.get("To_P").getAsString());

                            deleteButton = tvchildView.findViewById(R.id.delete_button);
                        }
                    }*/
                    if (trvPlcsArray != null || trvPlcsArray.size() != 0)
                        trvldLocation(trvPlcsArray);

                    JsonObject eachData;

                    double elibs = 0.0;
                    JsonArray jsonAddition = null;
                    JsonObject ldraft;

                    ldg_cin.setText("");
                    ldg_cout.setText("");
                    //ldg_coutDt.setText("");
                    for (int i = 0; i < ldArray.size(); i++) {
                        // ldgAdd.setText("- Remove");
                        ldraft = (JsonObject) ldArray.get(i);
                        elibs = Integer.valueOf(Common_Class.isNullOrEmpty(ldraft.get("Eligible").getAsString()) ? "0" : ldraft.get("Eligible").getAsString());
                        txtMyEligi.setText("₹" + new DecimalFormat("##0.00").format(elibs));
                        TextCheckInDate.setText(ldraft.get("Tadate").getAsString());
                        // changes chk
                        ldg_coutDt.setText(ldraft.get("Tadate").getAsString());

                    }

                    lodgStyLocation.setText("");
                    drvStyLocation.setText("");
                    txt_Styloc.setText("");
                    txt_drvStyloc.setText("");
                    ldg_StylocSpinner.setClickable(true);
                    ldg_DrvStylocSpinner.setClickable(true);
                    viewContinue.removeAllViews();
                    viewContinueTotal.removeAllViews();

                    lodgContvw.setVisibility(View.GONE);
                    linContinueStay.setVisibility(View.GONE);
                    linearConView.setVisibility(View.GONE);

                    ldg_cout.setText("");
                    //ldg_coutDt.setText("");
                    if (StayDate.size() > 0) {

                        lodgContvw.setVisibility(View.VISIBLE);
                        linContinueStay.setVisibility(View.VISIBLE);
                        linearConView.setVisibility(View.VISIBLE);

                        CInDate = StayDate.get(0).getAsJsonObject().get("Stay_Date_time").getAsString();
                        COutDate = StayDate.get(0).getAsJsonObject().get("COutDt").getAsString();

                        TextCheckInDate.setText(StayDate.get(0).getAsJsonObject().get("CInDate").getAsString());
                        ldg_cin.setText(StayDate.get(0).getAsJsonObject().get("CInTime").getAsString());

                        ldg_cout.setText(StayDate.get(0).getAsJsonObject().get("COutTm").getAsString());
                        ldg_coutDt.setText(StayDate.get(0).getAsJsonObject().get("uCOutDate").getAsString());

                        nofNght = DT.Daybetween(CInDate + " 00:00:00", COutDate + " 00:00:00");
                        //if(nofNght==0) nofNght=1;
                        NoofNight.setText(" - " + nofNght + " Nights - ");

                        sLocId = StayDate.get(0).getAsJsonObject().get("LocId").getAsString();
                        sLocName = StayDate.get(0).getAsJsonObject().get("StayLoc").getAsString();
                        sDrvLocId = StayDate.get(0).getAsJsonObject().get("DrvLocId").getAsString();
                        sDrvLocName = StayDate.get(0).getAsJsonObject().get("DrvStayLoc").getAsString();

                        txt_ldg_type.setText(StayDate.get(0).getAsJsonObject().get("Lodging_Type").getAsString());
                        lodgingStayTypeId = StayDate.get(0).getAsJsonObject().get("lodgingStayTypeId").getAsString();
                        allowType = StayDate.get(0).getAsJsonObject().get("allowType").getAsString();
                        isJointWork = StayDate.get(0).getAsJsonObject().get("isJointWork").getAsString().equals("1");
                        isFullPay = StayDate.get(0).getAsJsonObject().get("isFullPay").getAsString().equals("1");

                        lodgStyLocation.setText(sLocName);
                        drvStyLocation.setText(sDrvLocName);

                        Log.v("driverSpinner",sDrvLocName);

                        if (sLocId.equalsIgnoreCase("-1"))
                            sLocName = "Other Location";

                        if (sDrvLocId.equalsIgnoreCase("-1"))
                            sDrvLocName = "Other Location";

                        txt_Styloc.setText(sLocName);
                        txt_drvStyloc.setText(sDrvLocName);
                        getStayAllow();
                    }
                    if (LodingCon.size() != 0) {
                        if (StayDate.size() > 0) {
                            changeStay = true;
                            ldg_typ_sp.setClickable(false); // Todo: ldg_typ_sp.setClickable(false);
                            ldg_StylocSpinner.setClickable(false); // Todo: ldg_StylocSpinner.setClickable(false);
                        }
                        viewContinue.removeAllViews();
                        viewContinueTotal.removeAllViews();
                        LinearLayout linearLayout = findViewById(R.id.prm_linear_orders);
                        linearLayout.setOnClickListener(null);

                        ldg_cin.setOnClickListener(null);
                        TextCheckInDate.setOnClickListener(null);
                        countLoding = 1;
                        for (int i = 0; i < LodingCon.size(); i++) {
                            eachData = (JsonObject) LodingCon.get(i);
                            lodgContvw.setVisibility(View.VISIBLE);
                            linContinueStay.setVisibility(View.VISIBLE);
                            linearConView.setVisibility(View.VISIBLE);

                            TextView customOptionsName = new TextView(TAClaimActivity.this);
                            customOptionsName.setPadding(0, 15, 0, 15);
                            customOptionsName.setText(eachData.get("fdt").getAsString());
                            viewContinue.addView(customOptionsName);

                            TextView customOptionsNames = new TextView(TAClaimActivity.this);
                            customOptionsNames.setPadding(0, 15, 0, 15);
                            customOptionsNames.setText("₹" + eachData.get("Amt").getAsString() + ".00");
                            viewContinueTotal.addView(customOptionsNames);
                            continueStay = continueStay + Double.parseDouble(eachData.get("Amt").getAsString());
                            Log.v("TOTAL_DATE", String.valueOf(continueStay));
                        }
                    }

                    //continueStay = continueStay + elibs;
                    // lbl_ldg_eligi.setText("₹" + continueStay);
                    Log.v("TOTAL_DATE_Outer", String.valueOf(continueStay));
                    Log.v("LODGING_ARRAY", String.valueOf(ldArray.size()));
                    if (ldArray != null || ldArray.size() != 0) {
                        Log.v("LODGING_ARRAY_IF", String.valueOf(ldArray.size()));
                        if (ldgAdd.getText().equals("+ Add")) {
                            //ldgAdd.setText("- Remove");
                            lodgContvw.setVisibility(View.GONE);
                            lodingDraft(ldArray, LodingCon);

                            Log.v("TO_DA_GA_PA", txt_ldg_type.getText().toString());
                        } else {
                            //ldgAdd.setText("+ Add");

                            lodgContvw.setVisibility(View.VISIBLE);
                            txtMyEligi.setText("₹" + 0.00);
                            ldgWOBBal.setText("₹" + 0.00);
                            //  lbl_ldg_eligi.setText("₹" + 0.00);
                            edt_ldg_bill.setText("");
                            edtLdgGstNum.setText("");
                            edtLdgGstAmt.setText("");
                            edtLdgGstBillNo.setText("");
                            txt_ldg_type.setText("");
                            TotalDays.setVisibility(View.GONE);
                            earCheckIn.setText("");
                            earCheckOut.setText("");
                            latCheckIn.setText("");
                            latCheckOut.setText("");
                            edtEarBill.setText("");
                            edtLateBill.setText("");
                            mChckCont.setChecked(false);
                            mChckEarly.setChecked(false);
                            mChckLate.setChecked(false);
                        }

                        if (ldArray.size() != 0)
                            ldgAdd.setText("- Remove");

                    } else {
                        //lodgContvw.setVisibility(View.GONE);
                        Log.v("LODGING_ARRAY_ELSE", String.valueOf(ldArray.size()));
//                        jointLodging.setVisibility(View.GONE);
                        ldg_cin.setText("");
                        ldg_cout.setText("");
                        //ldg_coutDt.setText("");
                        txtMyEligi.setText("₹" + 0.00);
                        ldgWOBBal.setText("₹" + 0.00);
                        edt_ldg_bill.setText("");
                        txt_ldg_type.setText("");

                        edtLdgGstNum.setText("");
                        edtLdgGstAmt.setText("");
                        edtLdgGstBillNo.setText("");


                        TotalDays.setVisibility(View.GONE);
                        lodgContvw.setVisibility(View.GONE);
                    }
                    SumOFLodging(0);
                    linContinueStay.setVisibility(View.VISIBLE);
                    //   if (DT.Daybetween(COutDate + " 00:00:00", DateTime + " 00:00:00") < 1)
                    //       linContinueStay.setVisibility(View.GONE);

                    /*
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDateandTime = sdf.format(new Date());
                    if(finalChoosedDate.equalsIgnoreCase(currentDateandTime)){
                        btn_sub.setVisibility(View.GONE);
                    }*/


                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
            loadLDGTypes();

        } catch (Exception exception) {
        }
    }

    @SuppressLint("SetTextI18n")
    public void lodingDraft(JsonArray lodingDraft, JsonArray ContSty) { // Todo: lodingDraft
        JsonArray jsonAddition = null;
        JsonObject ldraft;
        if (lodingDraft.size() > 0 || ContSty.size() > 0) {

            lodgContvw.setVisibility(View.VISIBLE);
            lodgCont.setVisibility(View.VISIBLE);
            ldg_stayloc.setVisibility(View.VISIBLE);
            ldg_stayDt.setVisibility(View.VISIBLE);
            linContinueStay.setVisibility(View.VISIBLE);

            lodgJoin.setVisibility(View.GONE);
            JNLdgEAra.setVisibility(View.GONE);


            if (isFullPay) {
                linContinueStay.setVisibility(View.GONE);
                edt_ldg_bill.setVisibility(View.GONE);
                lblHdBill.setVisibility(View.GONE);
                linImgPrv.setVisibility(View.GONE);
                viewBilling.setVisibility(View.GONE);
                lblHdBln.setVisibility(View.GONE);
                ldgWOBBal.setVisibility(View.GONE);
                drvldgEAra.setVisibility(View.GONE);
                ldgGstLayout.setVisibility(View.GONE);
            }

            if (isJointWork) {
                lodgJoin.setVisibility(View.VISIBLE);
                JNLdgEAra.setVisibility(View.VISIBLE);
            }

            if (mChckCont.isChecked()) {
                vwldgBillAmt.setVisibility(View.GONE);
                ldgGstLayout.setVisibility(View.GONE);
                cnSty = 1;
                countLoding = 1;
            } else {
                cnSty = 0;
                countLoding = 0;
                if (!isFullPay) {
                    edt_ldg_bill.setVisibility(View.VISIBLE);
                    lblHdBill.setVisibility(View.VISIBLE);
                    linImgPrv.setVisibility(View.VISIBLE);
                    viewBilling.setVisibility(View.VISIBLE);
                    ldgGstLayout.setVisibility(View.VISIBLE);
                    vwldgBillAmt.setVisibility(View.VISIBLE);
                }
                linCheckOut.setVisibility(View.VISIBLE);
            }

            stayDays.setVisibility(View.VISIBLE);

            SumOFLodging(0);
        }
        for (int i = 0; i < lodingDraft.size(); i++) {
            ldraft = (JsonObject) lodingDraft.get(i);
            jsonAddition = ldraft.getAsJsonArray("Additional");

            // ldg_cin.setText(ldraft.get("Stay_Date").getAsString());
            if (ContSty.size() < 1) {
                sLocId = ldraft.get("LocId").getAsString();
                sLocName = ldraft.get("Ldg_Stay_Loc").getAsString();
                sDrvLocId=ldraft.get("DrvLocId").getAsString();
                sDrvLocName = ldraft.get("DrvStayLoc").getAsString();


                lodgStyLocation.setText(sLocName);
                drvStyLocation.setText(sDrvLocName);
                Log.v("spinner",drvStyLocation.toString());

                if (sLocId.equalsIgnoreCase("-1"))
                    sLocName = "Other Location";
                txt_Styloc.setText(sLocName);
                txt_drvStyloc.setText(sDrvLocName);
            }
            Double drvAmt = Double.valueOf(ldraft.get("Driver_Ldg_Amount").getAsString());
            txtDrivEligi.setVisibility(View.GONE);
            chkDrvAlw.setVisibility(View.GONE);
            if (drvAmt != 0) {
                txtDrivEligi.setVisibility(View.VISIBLE);
                chkDrvAlw.setVisibility(View.VISIBLE);
                chkDrvAlw.setChecked(true);
                txtDrivEligi.setText("₹" + new DecimalFormat("##0.00").format(drvAmt));
                ldgDrvEligi = drvAmt;
                ldgDrvAlw=drvAmt;
                drvldgEAra.setVisibility(View.VISIBLE);
                driverStayLocLayout.setVisibility(View.VISIBLE);
            }else{
                chkDrvAlw.setChecked(false);
                ldgDrvEligi = 0.0;
            }
            ConStay = ldraft.get("Continuous_Stay").getAsString();
            ErlyStay = ldraft.get("Early_Checkin").getAsString();
            LteStay = ldraft.get("Late_Checkout").getAsString();

            ErlyChecIn = ldraft.get("Erly_Check_in").getAsString();
            ErlyChecOut = ldraft.get("Erly_Check_out").getAsString();
            ErlyAmt = ldraft.get("Ear_bill_amt").getAsString();

            LteChecIn = ldraft.get("lat_chec_in").getAsString();
            LteChecOut = ldraft.get("lat_check_out").getAsString();
            LteAmt = ldraft.get("lat_bill_amt").getAsString();


            earCheckIn.setText(ErlyChecIn);
            earCheckOut.setText(ErlyChecOut);
            latCheckIn.setText(LteChecIn);
            latCheckOut.setText(LteChecOut);
            edtEarBill.setText(ErlyAmt);
            edtLateBill.setText(LteAmt);

            if (ConStay.equalsIgnoreCase("1")) mChckCont.setChecked(true);

            lodgJoin.setVisibility(View.GONE);
            JNLdgEAra.setVisibility(View.GONE);

            edt_ldg_bill.setVisibility(View.VISIBLE);
            lblHdBill.setVisibility(View.VISIBLE);
            linImgPrv.setVisibility(View.VISIBLE);
            viewBilling.setVisibility(View.VISIBLE);
            ldgGstLayout.setVisibility(View.VISIBLE);

            try {
                lodgingStayTypeId = ldraft.get("lodgingStayTypeId").getAsString();
                allowType = ldraft.get("allowType").getAsString();
                isJointWork = ldraft.get("isJointWork").getAsString().equals("1");
                isFullPay = ldraft.get("isFullPay").getAsString().equals("1");
                billAmts = ldraft.get("Bill_Amt").getAsString();
                isSavedBill = true;
            } catch (Exception e) { }

            if (isFullPay) {
                linContinueStay.setVisibility(View.GONE);
                lodgJoin.setVisibility(View.GONE);
                JNLdgEAra.setVisibility(View.GONE);
                edt_ldg_bill.setVisibility(View.GONE);
                lblHdBill.setVisibility(View.GONE);
                linImgPrv.setVisibility(View.GONE);
                viewBilling.setVisibility(View.GONE);
                lblHdBln.setVisibility(View.GONE);
                ldgWOBBal.setVisibility(View.GONE);
                drvldgEAra.setVisibility(View.GONE);
                ldgGstLayout.setVisibility(View.GONE);
            }

            if (isJointWork) {
                lodgJoin.setVisibility(View.VISIBLE);
                JNLdgEAra.setVisibility(View.VISIBLE);
            }

            if (mChckCont.isChecked()) {
                vwldgBillAmt.setVisibility(View.GONE);
                ldgGstLayout.setVisibility(View.GONE);
                cnSty = 1;
                countLoding = 1;
            } else {
                cnSty = 0;
                countLoding = 0;
                if (!isFullPay) {
                    edt_ldg_bill.setVisibility(View.VISIBLE);
                    lblHdBill.setVisibility(View.VISIBLE);
                    linImgPrv.setVisibility(View.VISIBLE);
                    viewBilling.setVisibility(View.VISIBLE);
                    ldgGstLayout.setVisibility(View.VISIBLE);
                    vwldgBillAmt.setVisibility(View.VISIBLE);
                }
                linCheckOut.setVisibility(View.VISIBLE);
            }

            stayDays.setVisibility(View.VISIBLE);
//            Double totlLdgAmt = Double.valueOf(ldraft.get("Total_Ldg_Amount").getAsString());
//            Integer noday = Integer.valueOf(ldraft.get("NO_Of_Days").getAsString());

            txtLodgUKey.setText(ldraft.get("Ukey").getAsString());

            double elibs = Integer.valueOf(Common_Class.isNullOrEmpty(ldraft.get("Eligible").getAsString()) ? "0" : ldraft.get("Eligible").getAsString());


            txtMyEligi.setText("₹" + new DecimalFormat("##0.00").format(elibs));

            double srtjdgAmt = Integer.valueOf(Common_Class.isNullOrEmpty(ldraft.get("Joining_Ldg_Amount").getAsString()) ? "0" : ldraft.get("Joining_Ldg_Amount").getAsString());
            txtJNEligi.setText("₹" + new DecimalFormat("##0.00").format(srtjdgAmt));
            Double wobal = Double.valueOf(Common_Class.isNullOrEmpty(ldraft.get("WOB_Amt").getAsString()) ? "0" : ldraft.get("WOB_Amt").getAsString());

            Log.v("ldgWOBBal", String.valueOf(wobal));
            ldgWOBBal.setText("₹" + new DecimalFormat("##0.00").format(wobal));
            Log.v("ldgWOBBal_______", ldgWOBBal.getText().toString());

            txtStyDays.setText(ldraft.get("NO_Of_Days").getAsString());
            ldg_cin.setText(ldraft.get("Stay_Date").getAsString());
            ldg_cout.setText(ldraft.get("To_Date").getAsString());
            edtLdgGstNum.setText(ldraft.get("GSTNo").getAsString());
            edtLdgGstAmt.setText(ldraft.get("GSTAmt").getAsString());
            edtLdgGstBillNo.setText(ldraft.get("GSTBNo").getAsString());

            txt_ldg_type.setText(ldraft.get("Lodging_Type").getAsString());

            JsonObject jsonObjectAdd = null;
            if (jsonAddition.size() > 0) {
                for (int l = 0; l < jsonAddition.size(); l++) {
                    Log.e("LOCTAION_LRD", String.valueOf(jsonAddition.size()));
                    jsonObjectAdd = (JsonObject) jsonAddition.get(l);

                    jointLodging.setVisibility(View.VISIBLE);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    layoutParams.setMargins(15, 15, 15, 15);
                    View rowView = inflater.inflate(R.layout.activity_loding_layout, null);
                    jointLodging.addView(rowView, layoutParams);

                    Integer jfd = jointLodging.indexOfChild(rowView);

                    View jdV = jointLodging.getChildAt(jfd);
                    edt_ldg_JnEmp = (EditText) jdV.findViewById(R.id.edt_ldg_JnEmp);
                    txtJNName = (TextView) jdV.findViewById(R.id.txtJNName);
                    txtJNDesig = (TextView) jdV.findViewById(R.id.txtJNDesig);
                    txtJNDept = (TextView) jdV.findViewById(R.id.txtJNDept);
                    txtJNHQ = (TextView) jdV.findViewById(R.id.txtJNHQ);
                    txtJNMob = (TextView) jdV.findViewById(R.id.txtJNMob);
                    txtJNMyEli = (TextView) jdV.findViewById(R.id.txtJNMyEli);

                    edt_ldg_JnEmp.setText(jsonObjectAdd.get("Emp_Code").getAsString());
                    txtJNName.setText(jsonObjectAdd.get("Sf_Name").getAsString());
                    txtJNDesig.setText(jsonObjectAdd.get("Desig").getAsString());
                    txtJNDept.setText(jsonObjectAdd.get("Dept").getAsString());
                    txtJNHQ.setText(jsonObjectAdd.get("Sf_Hq").getAsString());
                    txtJNMob.setText(jsonObjectAdd.get("Sf_Mobile").getAsString());

                    float sum = jsonObjectAdd.get("Ldg_Amount").getAsFloat();
                    txtJNMyEli.setText("₹" + new DecimalFormat("##0.00").format(sum));
                }
            }
            SumOFLodging(0);
        }

    }

    public void localConDraft(JsonArray lcDraft) {
        JsonArray jsonAddition = null;
        JsonObject lcdraftJson = null;
        for (int i = 0; i < lcDraft.size(); i++) {
            lcdraftJson = (JsonObject) lcDraft.get(i);

            jsonAddition = lcdraftJson.getAsJsonArray("Additional");
            String expCode = String.valueOf(lcdraftJson.get("Exp_Code"));
            String expFare = lcdraftJson.get("Exp_Amt").getAsString();
            lcUKey = lcdraftJson.get("Ukey").getAsString();

            Log.v("EXP_AMOUNT", expFare);

            expCode = expCode.replaceAll("^[\"']+|[\"']+$", "");
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(15, 15, 15, 15);

            final View rowView = inflater.inflate(R.layout.activity_local_convenyance, null);
            linlocalCon.addView(rowView, layoutParams);
            localText.setVisibility(View.VISIBLE);
            localTotal.setVisibility(View.VISIBLE);

            lcSize = linlocalCon.indexOfChild(rowView);

            View LcchildView = linlocalCon.getChildAt(lcSize);
            localTotal.setVisibility(View.VISIBLE);

            edtRwID = LcchildView.findViewById(R.id.lcRwID);
            editTexts = (TextView) (LcchildView.findViewById(R.id.local_enter_mode));
            editLaFare = (EditText) (LcchildView.findViewById(R.id.edt_la_fare));
            lcGstLayout = (LinearLayout) (LcchildView.findViewById(R.id.lcConv_gstLayout));
            linLocalSpinner = (LinearLayout) LcchildView.findViewById(R.id.lin_loc_spiner);
            lcAttach = (ImageView) (LcchildView.findViewById(R.id.la_attach_iamg));
            lcPreview = (ImageView) (LcchildView.findViewById(R.id.img_prvw_lc));
            Dynamicallowance = (LinearLayout) LcchildView.findViewById(R.id.lin_allowance_dynamic);
            lcTxtUKey = (TextView) (LcchildView.findViewById(R.id.txt_lc_ukey));

            edtLCGstNum = (EditText) (LcchildView.findViewById(R.id.edt_lcConv_gst));
            edtLCGstAmt = (EditText) (LcchildView.findViewById(R.id.edt_lcConv_gst_amt));
            edtLCGstBillNo = (EditText) (LcchildView.findViewById(R.id.edt_lcConv_gst_bno));

            String sRWID = expCode + "_" + System.nanoTime();
            edtRwID.setText(sRWID);

            editTexts.setText(expCode);
            editLaFare.setText(expFare);
            edtLCGstNum.setText(lcdraftJson.get("GSTNo").getAsString());
            edtLCGstAmt.setText(lcdraftJson.get("GSTAmt").getAsString());
            edtLCGstBillNo.setText(lcdraftJson.get("GSTBNo").getAsString());
            if (!lcUKey.equals("")) {
                lcTxtUKey.setText(lcUKey);
            }


            if (lcdraftJson.get("Attachments").getAsString().equals("1")) {
                lcAttach.setVisibility(View.VISIBLE);
                lcPreview.setVisibility(View.VISIBLE);
                lcGstLayout.setVisibility(View.VISIBLE);
            } else {
                lcAttach.setVisibility(View.GONE);
                lcPreview.setVisibility(View.GONE);
                lcGstLayout.setVisibility(View.GONE);

            }
            int maxVal = lcdraftJson.get("Max_Allowance").getAsInt();
            editLaFare.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, maxVal)});
            editLaFare.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    SumOFLCAmount();
                }
            });
            linLocalSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listOrderType.clear();
                    Integer lcPosCntS = linlocalCon.indexOfChild(rowView);
                    dynamicModeType(lcPosCntS);
                }
            });
            lcAttach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());
                    if (!cameraPermission.checkPermission()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                            cameraPermission.requestPermission();
                        }
                        Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                    } else {
                        Log.v("PERMISSION", "PERMISSION");


                        Integer lcPosCntS = linlocalCon.indexOfChild(rowView);
                        View view = linlocalCon.getChildAt(lcPosCntS);
                        editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
                        lcTxtUKey = (TextView) (view.findViewById(R.id.txt_lc_ukey));
                        editMode = editTexts.getText().toString();
/*
                        if (lcTxtUKey.getText().toString().equals("")) {
                            DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            Calendar calobjw = Calendar.getInstance();
                            lcEditcnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                            lcTxtUKey.setText(lcEditcnt);

                        } else {
                        }
                        LcUKey = lcTxtUKey.getText().toString();*/
                        popupCapture(786);
                    }

                }
            });
            lcPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer valuelc = linlocalCon.indexOfChild(rowView);
                    View view = linlocalCon.getChildAt(valuelc);

                    editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
                    lcTxtUKeys = (TextView) (view.findViewById(R.id.txt_lc_ukey));
                    editMode = editTexts.getText().toString();
                    LcUKey = lcTxtUKeys.getText().toString();
                    AttachementActivity.setOnAttachmentDeleteListener(new OnAttachmentDelete() {
                        @Override
                        public void OnImageDelete(String Mode, int ImgCount) {
                            if (ImgCount < 1) {
                                lcTxtUKeys.setText("");
                            }
                        }
                    });
                    DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

                    Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                    stat.putExtra("position", LcUKey);
                    stat.putExtra("headTravel", "LC");
                    stat.putExtra("mode", editMode);
                    stat.putExtra("date", DateTime);
                    startActivity(stat);
                }
            });
            localConDisplay(sRWID, jsonAddition, lcSize, lcdraftJson.get("Attachments").getAsString());


        }
        SumOFLCAmount();
    }

    public void OeDraft(JsonArray oEDraft) {
        JsonArray jsonAddition = null;
        JsonObject lcdraftJson = null;
        for (int i = 0; i < oEDraft.size(); i++) {
            lcdraftJson = (JsonObject) oEDraft.get(i);
            jsonAddition = lcdraftJson.getAsJsonArray("Additional");
            String expCode = lcdraftJson.get("Exp_Code").getAsString();
            String expFare = lcdraftJson.get("Exp_Amt").getAsString();
            oeUKey = lcdraftJson.get("Ukey").getAsString();

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            otherExpenseLayout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(15, 15, 15, 15);

            final View rowView = inflater.inflate(R.layout.activity_other_expense, null);
            LinearOtherAllowance.addView(rowView, layoutParams);
            OeSize = LinearOtherAllowance.getChildCount();

            oePosCnt = LinearOtherAllowance.indexOfChild(rowView);
            View childView = LinearOtherAllowance.getChildAt(oePosCnt);
            otherExpenseLayout.setVisibility(View.VISIBLE);

            edtRwID = childView.findViewById(R.id.oeRwID);
            oeEditext = (TextView) (childView.findViewById(R.id.other_enter_mode));
            edtOE = (EditText) (childView.findViewById(R.id.oe_fre_amt));
            oeAttach = (ImageView) (childView.findViewById(R.id.oe_attach_img));
            oEGstLayout =(LinearLayout) (childView.findViewById(R.id.otherExp_gstLayout));
            oePreview = (ImageView) (childView.findViewById(R.id.img_prvw_oe));
            linOtherSpinner = (LinearLayout) (childView.findViewById(R.id.lin_othr_spiner));
            oeTxtUKey = (TextView) (childView.findViewById(R.id.txt_oe_ukey));

            edtOEGstNum = (EditText) (childView.findViewById(R.id.edt_otherExp_gst));
            edtOEGstAmt = (EditText) (childView.findViewById(R.id.edt_otherExp_gst_amt));
            edtOEGstBillNo = (EditText) (childView.findViewById(R.id.edt_otherExp_gst_bno));

            String sRWID = expCode + "_" + System.nanoTime();
            edtRwID.setText(sRWID);
            String AttFlg = lcdraftJson.get("Attachments").getAsString();
            int maxVal = lcdraftJson.get("Max_Allowance").getAsInt();
            if (AttFlg.equals("1")) {
                oeAttach.setVisibility(View.VISIBLE);
                oePreview.setVisibility(View.VISIBLE);
                oEGstLayout.setVisibility(View.VISIBLE);
            } else {
                oeAttach.setVisibility(View.GONE);
                oePreview.setVisibility(View.GONE);
                oEGstLayout.setVisibility(View.GONE);

            }

            oeEditext.setText(expCode);
            edtOE.setText(expFare);

            edtOEGstNum.setText(lcdraftJson.get("GSTNo").getAsString());
            edtOEGstAmt.setText(lcdraftJson.get("GSTAmt").getAsString());
            edtOEGstBillNo.setText(lcdraftJson.get("GSTBNo").getAsString());

            if (!oeUKey.equals("") && oeUKey != "" && !oeUKey.isEmpty() && oeUKey != null) {
                oeTxtUKey.setText(oeUKey);
            }
            oeAttach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());
                    if (!cameraPermission.checkPermission()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            cameraPermission.requestPermission();
                        }
                        Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                    } else {
                        Log.v("PERMISSION", "PERMISSION");

                        Integer valuedfd = LinearOtherAllowance.indexOfChild(rowView);
                        View view = LinearOtherAllowance.getChildAt(valuedfd);
                        oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
                        oeTxtUKeys = (TextView) (view.findViewById(R.id.txt_oe_ukey));
                        editMode = oeEditext.getText().toString();

                        /*if (oeTxtUKeys.getText().toString().equals("")) {
                            DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            Calendar calobjw = Calendar.getInstance();
                            oeEditCnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                            oeTxtUKeys.setText(oeEditCnt);
                        }
                        OeUKey = oeTxtUKeys.getText().toString();*/
                        popupCapture(99);
                    }
                }
            });
            oePreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Integer valuedfd = LinearOtherAllowance.indexOfChild(rowView);
                    View view = LinearOtherAllowance.getChildAt(valuedfd);
                    oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
                    oeTxtUKeys = (TextView) (view.findViewById(R.id.txt_oe_ukey));
                    editMode = oeEditext.getText().toString();

                    OeUKey = oeTxtUKeys.getText().toString();
                    DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

                    Log.v("OE_UKEY", OeUKey);
                    Log.v("OE_mode", editMode);
                    Log.v("OE_date", DateTime);
                    AttachementActivity.setOnAttachmentDeleteListener(new OnAttachmentDelete() {
                        @Override
                        public void OnImageDelete(String Mode, int ImgCount) {
                            if (ImgCount < 1) {
                                oeTxtUKeys.setText("");
                            }
                        }

                    });
                    Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                    stat.putExtra("position", OeUKey);
                    stat.putExtra("headTravel", "OE");
                    stat.putExtra("mode", editMode);
                    stat.putExtra("date", DateTime);
                    startActivity(stat);

                }
            });

            edtOE.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, maxVal)});
            edtOE.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {

                    SumOFOTAmount();

                }
            });
            OtherExpense = (LinearLayout) childView.findViewById(R.id.lin_other_expense_dynamic);
            linOtherSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OtherExpenseList.clear();
                    Integer valuedf = LinearOtherAllowance.indexOfChild(rowView);
                    OtherExpenseMode(valuedf);

                }
            });
            OtherexpAdDisplay(sRWID, jsonAddition, oePosCnt, AttFlg);


        }
        SumOFOTAmount();
    }

    public void trvldLocation(JsonArray traveldLoc) {
        try {
            JsonObject tldraftJson = null;
            for (int i = 0; i < traveldLoc.size(); i++) {
                tldraftJson = (JsonObject) traveldLoc.get(i);

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = null;

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);

                rowView = inflater.inflate(R.layout.travel_allowance_dynamic_one, null);

                viw.setVisibility(View.GONE);
                lin.setVisibility(View.GONE);

                travelPlaces.addView(rowView, layoutParams);
                View views = travelPlaces.getChildAt(i);
                deleteButton = views.findViewById(R.id.delete_button);
                etrTaFr = (EditText) views.findViewById(R.id.ta_edt_from);
                etrTaTo = (EditText) views.findViewById(R.id.ta_edt_to);

                if (!tldraftJson.get("From_P").getAsString().isEmpty() && !tldraftJson.get("From_P").getAsString().equals("")) {
                    etrTaFr.setText(tldraftJson.get("From_P").getAsString());
                }

                if (!tldraftJson.get("To_P").getAsString().isEmpty() && !tldraftJson.get("To_P").getAsString().equals("")) {
                    etrTaTo.setText(tldraftJson.get("To_P").getAsString());
                }
            }
        } catch (Exception e) {
        }
    }

    public void OtherexpAdDisplay(String modeName, JsonArray jsonAddition, int position, String flag) {

        JsonObject jsonObjectAdd = null;
        List<CtrlsListModel.Ctrls> users = new ArrayList<>();
        for (int l = 0; l < jsonAddition.size(); l++) {
            jsonObjectAdd = (JsonObject) jsonAddition.get(l);
            String edtValueb = String.valueOf(jsonObjectAdd.get("Ref_Value"));
            String valHint = String.valueOf(jsonObjectAdd.get("Ref_Code"));
            RelativeLayout childRel = new RelativeLayout(getApplicationContext());
            RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_START);
            layoutparams_3.setMargins(20, -10, 0, 0);

            edt1 = new EditText(getApplicationContext());
            edt1.setLayoutParams(layoutparams_3);
            edt1.setText(edtValueb.replaceAll("^[\"']+|[\"']+$", ""));
            edt1.setTextSize(13);
            childRel.addView(edt1);

            OEdynamicList.add(valHint);

            CtrlsListModel.Ctrls Ctrl = new CtrlsListModel.Ctrls(valHint, edt1);
            users.add(Ctrl);

            View view = LinearOtherAllowance.getChildAt(position);
            OtherExpense = (LinearLayout) view.findViewById(R.id.lin_other_expense_dynamic);
            OtherExpense.addView(childRel);

        }
        CtrlsListModel UOEItem = new CtrlsListModel(users, flag);
        uOEItems.put(modeName, UOEItem);
    }

    public void localConDisplay(String modeName, JsonArray jsonAddition, int position, String flag) {

        JsonObject jsonObjectAdd = null;
        List<CtrlsListModel.Ctrls> users = new ArrayList<>();
        for (int l = 0; l < jsonAddition.size(); l++) {
            jsonObjectAdd = (JsonObject) jsonAddition.get(l);
            String edtValueb = String.valueOf(jsonObjectAdd.get("Ref_Value"));
            String valHint = String.valueOf(jsonObjectAdd.get("Ref_Code"));

            RelativeLayout childRel = new RelativeLayout(getApplicationContext());
            RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_START);
            layoutparams_3.setMargins(20, -10, 0, 0);
            edt1 = new EditText(getApplicationContext());
            edt1.setLayoutParams(layoutparams_3);
            edt1.setText(edtValueb.replaceAll("^[\"']+|[\"']+$", ""));
            edt1.setTextSize(13);
            edt1.setHint(valHint);
            childRel.addView(edt1);

            CtrlsListModel.Ctrls Ctrl = new CtrlsListModel.Ctrls(valHint, edt1);
            users.add(Ctrl);

            View view = linlocalCon.getChildAt(position);
            Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);
            Dynamicallowance.addView(childRel);

            dynamicLabelList.add(jsonObjectAdd.get("Ref_Code").getAsString());
        }


        CtrlsListModel ULCItem = new CtrlsListModel(users, flag);
        uLCItems.put(modeName, ULCItem);
        //usersByCountry.put(modeName, users);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data != null) {
                String sMode = "";
                long nano_startTime = System.nanoTime();
                ImageUKey = keyEk + UserDetails.getString("Sfcode", "") + nano_startTime;

                Shared_Common_Pref.ImageUKey = ImageUKey;
                if (requestCode == 144) {
                    if (txtLodgUKey.getText().toString().equals("")) {
                        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Calendar calobjw = Calendar.getInstance();
                        lodEditcnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();

                        lodUKey = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                        // txtLodgUKey.setText(lodUKey);
                    }


                    //  lodUKey = txtLodgUKey.getText().toString();

                    sMode = "LOD;" + DateTime + ";" + lodUKey + ";Room;" + ImageUKey;
                }
                if (requestCode == 124) {
                    if (tvTxtUKeys.getText().toString().equals("")) {
                        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Calendar calobjw = Calendar.getInstance();
                        tvEditcnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                        TlUKey = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                        // tvTxtUKeys.setText(tvEditcnt);
                    }
                    // TlUKey = tvTxtUKeys.getText().toString();
                    sMode = "TL;" + DateTime + ";" + TlUKey + ";" + editMode + ";" + ImageUKey;
                }
                if (requestCode == 100) {
                    if (oeTxtUKeys.getText().toString().equals("")) {
                        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Calendar calobjw = Calendar.getInstance();
                        oeEditCnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                        OeUKey = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();

                        //  oeTxtUKeys.setText(oeEditCnt);
                    }

                    // OeUKey = oeTxtUKeys.getText().toString();
                    sMode = "OE;" + DateTime + ";" + OeUKey + ";" + editMode + ";" + ImageUKey;
                }
                if (requestCode == 787) {
                    if (lcTxtUKeys.getText().toString().equals("")) {
                        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Calendar calobjw = Calendar.getInstance();
                        lcEditcnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                        LcUKey = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();

                        // lcTxtUKeys.setText(lcEditcnt);
                    }
                    // LcUKey = lcTxtUKeys.getText().toString();
                    sMode = "LC;" + DateTime + ";" + LcUKey + ";" + editMode + ";" + ImageUKey;
                }


                if (requestCode == 144) {
                    if (resultCode == RESULT_OK) {
                        if (requestCode == 144) {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    //display your images
                                    ImageFilePath filepath = new ImageFilePath();
                                    fullPath = ImageFilePath.getPath(TAClaimActivity.this, mClipData.getItemAt(i).getUri());
                                    lodgArrLst.add(fullPath);
                                    getMulipart(lodUKey, fullPath, "LOD", ImageUKey, "Room", "", "");


                                }
                            } else if (data.getData() != null) {
                                Uri item = data.getData();

                                ImageFilePath filepath = new ImageFilePath();
                                fullPath = ImageFilePath.getPath(TAClaimActivity.this, item);
                                lodgArrLst.add(fullPath);
//                            Util util = new Util();
//                            String fileName = ImageUKey + "." + util.getFileExtension(this, item);
//                            final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//                                    "/" + fileName);
//                            util.createFile(getApplicationContext(), item, file);
//                            fullPath = file.getPath();
                                getMulipart(lodUKey, fullPath, "LOD", ImageUKey, "Room", "", "");

                            }
                        }
                    }
                } else if (requestCode == 124) {
                    if (resultCode == RESULT_OK) {
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                // display your images
                                ImageFilePath filepath = new ImageFilePath();
                                fullPath = ImageFilePath.getPath(TAClaimActivity.this, mClipData.getItemAt(i).getUri());

                                getMulipart(TlUKey, fullPath, "TL", ImageUKey, editMode, "", "");

                            }
                        } else if (data.getData() != null) {
                            Uri item = data.getData();
                            ImageFilePath filepath = new ImageFilePath();
                            fullPath = ImageFilePath.getPath(TAClaimActivity.this, item);


                            getMulipart(TlUKey, fullPath, "TL", ImageUKey, editMode, "", "");

                        }
                    }

                } else if (requestCode == 100) {
                    if (resultCode == RESULT_OK) {
                        if (requestCode == 100) {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    ImageFilePath filepath = new ImageFilePath();
                                    fullPath = ImageFilePath.getPath(TAClaimActivity.this, mClipData.getItemAt(i).getUri());

                                    getMulipart(OeUKey, fullPath, "OE", ImageUKey, editMode, "", "");


                                }
                            } else if (data.getData() != null) {

                                Uri item = data.getData();
                                ImageFilePath filepath = new ImageFilePath();
                                fullPath = ImageFilePath.getPath(TAClaimActivity.this, item);
                                getMulipart(OeUKey, fullPath, "OE", ImageUKey, editMode, "", "");

                            }
                        }
                    }
                } else if (requestCode == 787) {
                    if (resultCode == RESULT_OK) {
                        if (requestCode == 787) {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    // display your images
                                    ImageFilePath filepath = new ImageFilePath();
                                    fullPath = ImageFilePath.getPath(TAClaimActivity.this, mClipData.getItemAt(i).getUri());

                                    getMulipart(LcUKey, fullPath, "LC", ImageUKey, editMode, "", "");

                                }
                            } else if (data.getData() != null) {

                                Uri item = data.getData();
                                ImageFilePath filepath = new ImageFilePath();
                                fullPath = ImageFilePath.getPath(TAClaimActivity.this, item);

                                getMulipart(LcUKey, fullPath, "LC", ImageUKey, editMode, "", "");

                            }
                        }
                    }
                } else if (requestCode == 344) {
                    if (resultCode == RESULT_OK) {
                        if (requestCode == 344) {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    // display your images
                                    ImageFilePath filepath = new ImageFilePath();
                                    fullPath = ImageFilePath.getPath(TAClaimActivity.this, mClipData.getItemAt(i).getUri());

                                    getMulipart(lodgLate, fullPath, "LOD", ImageUKey, "LateMode", "", "");

                                }
                            } else if (data.getData() != null) {

                                Uri item = data.getData();
                                ImageFilePath filepath = new ImageFilePath();
                                fullPath = ImageFilePath.getPath(TAClaimActivity.this, item);

                                getMulipart(lodgLate, fullPath, "LOD", ImageUKey, "LateMode", "", "");

                            }
                        }
                    }
                } else if (requestCode == 406) {
                    if (resultCode == RESULT_OK) {
                        if (requestCode == 406) {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    // display your images
                                    ImageFilePath filepath = new ImageFilePath();
                                    fullPath = ImageFilePath.getPath(TAClaimActivity.this, mClipData.getItemAt(i).getUri());

                                    getMulipart(lodgEarly, fullPath, "LOD", ImageUKey, "EarlyMode", "", "");

                                }
                            } else if (data.getData() != null) {

                                Uri item = data.getData();
                                ImageFilePath filepath = new ImageFilePath();
                                fullPath = ImageFilePath.getPath(TAClaimActivity.this, item);

                                getMulipart(lodgEarly, fullPath, "LOD", ImageUKey, "EarlyMode", "", "");

                            }
                        }
                    }
                } else if (requestCode == 143 && resultCode == Activity.RESULT_OK) {
                    finalPath = "/storage/emulated/0";
                    filePath = outputFileUri.getPath();
                    filePath = filePath.substring(1);
                    filePath = finalPath + filePath.substring(filePath.indexOf("/"));

                    getMulipart(lodUKey, filePath, "LOD", "", "Room", "", "");

                } else if (requestCode == 343 && resultCode == Activity.RESULT_OK) {

                    finalPath = "/storage/emulated/0";
                    filePath = outputFileUri.getPath();
                    filePath = filePath.substring(1);
                    filePath = finalPath + filePath.substring(filePath.indexOf("/"));

                    getMulipart(lodgLate, filePath, "LOD", "", "LateMode", "", "");

                } else if (requestCode == 405 && resultCode == Activity.RESULT_OK) {

                    finalPath = "/storage/emulated/0";
                    filePath = outputFileUri.getPath();
                    filePath = filePath.substring(1);
                    filePath = finalPath + filePath.substring(filePath.indexOf("/"));

                    getMulipart(lodgEarly, filePath, "LOD", "", "EarlyMode", "", "");

                } else if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
                    finalPath = "/storage/emulated/0";
                    filePath = outputFileUri.getPath();
                    filePath = filePath.substring(1);
                    filePath = finalPath + filePath.substring(filePath.indexOf("/"));

                    getMulipart(TlUKey, filePath, "TL", "", editMode, "", "");

                } else if (requestCode == 99 && resultCode == Activity.RESULT_OK) {

                    finalPath = "/storage/emulated/0";
                    filePath = outputFileUri.getPath();
                    filePath = filePath.substring(1);
                    filePath = finalPath + filePath.substring(filePath.indexOf("/"));

                    getMulipart(OeUKey, filePath, "OE", "", editMode, "", "");

                } else if (requestCode == 786 && resultCode == Activity.RESULT_OK) {

                    finalPath = "/storage/emulated/0";
                    filePath = outputFileUri.getPath();
                    filePath = filePath.substring(1);
                    filePath = finalPath + filePath.substring(filePath.indexOf("/"));

                    getMulipart(LcUKey, filePath, "LC", "", editMode, "", "");
                }


                Log.v("FileName:Load1", filePath);
                Log.v("FileName:Load2", fullPath);

            }
        } catch (Exception e) {
            Log.v("TAClaimActivity:mulImg:", e.getMessage());
        }
    }

    private void UploadPhoto(String path, String SF, String FileName, String Mode, String type) {
        try {
            mCommon_class.ProgressdialogShow(1, "");

            MultipartBody.Part imgg;
            if (path != null && (path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".pdf"))) {
                imgg = convertimg("file", path);

            } else {
                mCommon_class.ProgressdialogShow(0, "");
                mCommon_class.showMsg(this, "Image and Pdf file only supported");
                return;
            }


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseBody> mCall = apiInterface.onTAFileUpload(SF, FileName, Mode, imgg);

            Log.e("SEND_IMAGE_SERVER", mCall.request().toString());

            mCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful()) {

                            String jsonData = null;
                            jsonData = response.body().string();
                            JSONObject js = new JSONObject(jsonData);

                            if (js.getBoolean("success")) {

                                switch (type) {
                                    case "LOD":
                                        if (txtLodgUKey.getText().toString().equals("")) {
                                            txtLodgUKey.setText(lodEditcnt);
                                        }
                                        lodUKey = txtLodgUKey.getText().toString();

                                        break;
                                    case "OE":
                                        if (oeTxtUKeys.getText().toString().equals("")) {
                                            oeTxtUKeys.setText(oeEditCnt);
                                        }

                                        OeUKey = oeTxtUKeys.getText().toString();
                                        break;
                                    case "LC":
                                        if (lcTxtUKeys.getText().toString().equals("")) {
                                            lcTxtUKeys.setText(lcEditcnt);
                                        }
                                        LcUKey = lcTxtUKeys.getText().toString();

                                        break;
                                    case "TL":
                                        if (tvTxtUKeys.getText().toString().equals("")) {
                                            tvTxtUKeys.setText(tvEditcnt);
                                        }
                                        TlUKey = tvTxtUKeys.getText().toString();

                                        break;
                                }

                                mCommon_class.ProgressdialogShow(0, "");

                                mCommon_class.showMsg(TAClaimActivity.this, "File uploading successful ");
                            } else {
                                mCommon_class.ProgressdialogShow(0, "");
                                mCommon_class.showMsg(TAClaimActivity.this, "Failed.Try Again...");
                            }
                        } else {

                            mCommon_class.ProgressdialogShow(0, "");
                            mCommon_class.showMsg(TAClaimActivity.this, "Failed.Try Again...");

                        }

                    } catch (Exception e) {
                        mCommon_class.ProgressdialogShow(0, "");
                        mCommon_class.showMsg(TAClaimActivity.this, "Failed.Try Again...");

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mCommon_class.ProgressdialogShow(0, "");
                    mCommon_class.showMsg(TAClaimActivity.this, "Failed.Try Again...");

                    Log.e("SEND_IMAGE_Response", "ERROR");
                }
            });

       /*     TransferObserver uploadObserver =
                    transferUtility.upload("happic", Mode + "/" + FileName, file);

            uploadObserver.setTransferListener(new TransferListener() {

                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        Toast.makeText(getApplicationContext(), "Upload Completed!", Toast.LENGTH_SHORT).show();
                    } else if (TransferState.FAILED == state) {

                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                }

                @Override
                public void onError(int id, Exception ex) {
                    ex.printStackTrace();
                }

            });*/
        } catch (Exception e) {
            Log.e("TAClaim:", e.getMessage());
        }
    }

    public void pdfViewList() {
        dialog = new Dialog(TAClaimActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.row_pdf_viewer_list);
        dialog.show();
        LinearLayout pdf1 = dialog.findViewById(R.id.lin_pdf);
        LinearLayout pdf2 = dialog.findViewById(R.id.lin_pdf2);
        LinearLayout pdf3 = dialog.findViewById(R.id.lin_pdf3);
        pdf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stat = new Intent(getApplicationContext(), PdfViewerActivity.class);
                stat.putExtra("PDF_ONE", "https://hap.sanfmcg.com/Travel%20and%20Daily%20Allowance%20Policy%20-%20Domestic%20Travel%20Annexure%20C-1.pdf");
                stat.putExtra("PDF_FILE", "Web");
                startActivity(stat);

                dialog.dismiss();
            }
        });
        pdf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stat = new Intent(getApplicationContext(), PdfViewerActivity.class);
                stat.putExtra("PDF_ONE", "https://hap.sanfmcg.com/HAP_ANNEXURE_C.pdf");
                stat.putExtra("PDF_FILE", "Web");
                startActivity(stat);

                dialog.dismiss();
            }
        });
        pdf3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stat = new Intent(getApplicationContext(), PdfViewerActivity.class);
                stat.putExtra("PDF_ONE", "https://hap.sanfmcg.com/HAP_LODGE_POLICY_NOV_2022.pdf");
                stat.putExtra("PDF_FILE", "Web");
                startActivity(stat);

                dialog.dismiss();
            }
        });
    }

    public void popupCapture(Integer attachName) {
        dialog = new Dialog(TAClaimActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_capture);
        dialog.show();
        TextView upload = dialog.findViewById(R.id.upload);
        TextView camera = dialog.findViewById(R.id.camera);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMultiImage(attachName);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureFile(attachName);
            }
        });
    }

    public void selectMultiImage(Integer attachName) {
        dialog.dismiss();
//        Intent intent = new Intent();
//        intent.setType("*/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), attachName + 1);
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), attachName + 1);
    }

    public void captureFile(Integer reqCode) {
        dialog.dismiss();

        AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
            @Override
            public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                String sMode = "";
                String type = "";
                long nano_startTime = System.nanoTime();
                ImageUKey = keyEk + UserDetails.getString("Sfcode", "") + nano_startTime;
                if (reqCode == 143) {
                    if (txtLodgUKey.getText().toString().equals("")) {
                        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Calendar calobjw = Calendar.getInstance();
                        lodEditcnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                        lodUKey = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                        //  txtLodgUKey.setText(lodUKey);
                    }
                    // lodUKey = txtLodgUKey.getText().toString();
                    sMode = "LOD;" + DateTime + ";" + lodUKey + ";Room;" + ImageUKey;
                    type = "LOD";
                }
                if (reqCode == 123) {
                    if (tvTxtUKeys.getText().toString().equals("")) {
                        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Calendar calobjw = Calendar.getInstance();
                        tvEditcnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                        // tvTxtUKeys.setText(tvEditcnt);
                        TlUKey = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                    }
                    // TlUKey = tvTxtUKeys.getText().toString();
                    sMode = "TL;" + DateTime + ";" + TlUKey + ";" + editMode + ";" + ImageUKey;
                    type = "TL";
                }
                if (reqCode == 99) {
                    if (oeTxtUKeys.getText().toString().equals("")) {
                        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Calendar calobjw = Calendar.getInstance();
                        oeEditCnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                        OeUKey = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();

                        //  oeTxtUKeys.setText(oeEditCnt);
                    }

                    //  OeUKey = oeTxtUKeys.getText().toString();
                    sMode = "OE;" + DateTime + ";" + OeUKey + ";" + editMode + ";" + ImageUKey;
                    type = "OE";
                }
                if (reqCode == 786) {
                    if (lcTxtUKeys.getText().toString().equals("")) {
                        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Calendar calobjw = Calendar.getInstance();
                        lcEditcnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                        LcUKey = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();

                        // lcTxtUKeys.setText(lcEditcnt);
                    }
                    // LcUKey = lcTxtUKeys.getText().toString();
                    sMode = "LC;" + DateTime + ";" + LcUKey + ";" + editMode + ";" + ImageUKey;
                    type = "LC";
                }

//                Intent mIntent = new Intent(TAClaimActivity.this, FileUploadService.class);
//                mIntent.putExtra("mFilePath", fullPath);
//                mIntent.putExtra("SF", UserDetails.getString("Sfcode", ""));
//                mIntent.putExtra("FileName", FileName);
//                mIntent.putExtra("Mode", "ExpClaim;" + sMode);
//                FileUploadService.enqueueWork(TAClaimActivity.this, mIntent);


                UploadPhoto(fullPath, UserDetails.getString("Sfcode", ""), FileName, "ExpClaim;" + sMode, type);

                Log.v("FileName:capture", FileName);

            }
        });
        Intent intent = new Intent(TAClaimActivity.this, AllowancCapture.class);
        intent.putExtra("allowance", "TAClaim");
        startActivity(intent);

    }

    public boolean validate() {
        String sMsg = "";
        if (lodgContvw.getVisibility() == View.VISIBLE) { /// && mChckCont.isChecked()==false

            if (txt_ldg_type.getText().toString().equalsIgnoreCase("")) {
                sMsg = "Select the Lodging Type";
            } else if (sLocId.equalsIgnoreCase("")) {
                sMsg = "Select the Stay Location";
            } else if (sLocId.equalsIgnoreCase("-1") && lodgStyLocation.getText().toString().equalsIgnoreCase("")) {
                sMsg = "Enter the Stay Location";
            } else if (ldg_cin.getText().toString().equalsIgnoreCase("") && !txt_ldg_type.getText().toString().equalsIgnoreCase("Stay At Relative's House")) {
                sMsg = "Select the Check-In Time";
            } else if (ldg_coutDt.getText().toString().equalsIgnoreCase("") && !txt_ldg_type.getText().toString().equalsIgnoreCase("Stay At Relative's House")) {
                sMsg = "Select the Check-Out Date";
            } else if (ldg_cout.getText().toString().equalsIgnoreCase("") && !txt_ldg_type.getText().toString().equalsIgnoreCase("Stay At Relative's House")) {
                sMsg = "Select the Check-Out Time";
            } else if (nofNght > 0 && DT.getDate(ldg_coutDt.getText() + "00:00:00").getTime() <= DT.getDate(DateTime + " 00:00:00").getTime()) {
                sMsg = "Lodging Can't Applicable for the  " + DateTime;
            } else if (!mChckCont.isChecked() && !txt_ldg_type.getText().toString().equalsIgnoreCase("Stay At Relative's House")) {
                String lBillAmt = edt_ldg_bill.getText().toString();
                if (edt_ldg_bill.getVisibility() == View.VISIBLE && (lBillAmt.equalsIgnoreCase("") || lBillAmt.equalsIgnoreCase("0"))) {
                    sMsg = "Enter the Lodging Bill Amount";
                } else if (txtLodgUKey.getVisibility() == View.VISIBLE && txtLodgUKey.getText().toString().equals("")) {
                    sMsg = "Kindly Attach Lodging Bill";
                }
            }
        }
        if (!sMsg.equalsIgnoreCase("")) {
            Toast.makeText(TAClaimActivity.this, sMsg, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void submitData(String responseVal, CircularProgressButton btnAnim) {


        if (edtEarBill.getText().toString().equalsIgnoreCase("")) edtEarBill.setText("0");
        if (edtLateBill.getText().toString().equalsIgnoreCase("")) edtLateBill.setText("0");
        if (earCheckIn.getText().toString().equalsIgnoreCase("00:00:00")) earCheckIn.setText("");
        if (earCheckOut.getText().toString().equalsIgnoreCase("00:00:00")) earCheckOut.setText("");
        if (latCheckIn.getText().toString().equalsIgnoreCase("00:00:00")) latCheckIn.setText("");
        if (latCheckOut.getText().toString().equalsIgnoreCase("00:00:00")) latCheckOut.setText("");

        JsonObject ldraft;
        sty_date = "";
        for (int i = 0; i < StayDate.size(); i++) {
            ldraft = (JsonObject) StayDate.get(i);
            sty_date = ldraft.get("Stay_Date_time").getAsString();
        }
        JSONArray transHead = new JSONArray();
        JSONObject transJson = new JSONObject();
        JSONObject jsonData = new JSONObject();
        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
        StrBus = StrBus.replaceAll("^[\"']+|[\"']+$", "");
        StrTo = StrTo.replaceAll("^[\"']+|[\"']+$", "");
        if (sty_date.equalsIgnoreCase("")) sty_date = DateTime;

        try {
            /*Head Json*/
            jsonData.put("UKey", Ukey);

            jsonData.put("SF_Code", SF_code);
            jsonData.put("exp_date", DateTime);
            jsonData.put("da_mode", StrDailyAllowance);
            jsonData.put("latLong", clocation.getLatitude() + ":" + clocation.getLongitude());
            jsonData.put("al_type", "");
            jsonData.put("from_place", StrBus);
            jsonData.put("to_place", StrTo);
            jsonData.put("al_amount", "0");
            jsonData.put("da_amount", TotDA);
            jsonData.put("lc_amount", sum);
            jsonData.put("oe_amount", sumsTot);
            jsonData.put("total_ldg_amt", totLodgAmt);
            jsonData.put("ta_total_amount", tofuel);
            jsonData.put("drvBrdAmt", drvBrdAmt);
            jsonData.put("trv_lc_amt", sumsTa);
            jsonData.put("gr_total", gTotal);
            jsonData.put("edt_remark", editTextRemarks.getText().toString());

            /*Daily Allowance*/
            JSONObject daAll = new JSONObject();
            daAll.put("all_name", txtDailyAllowance.getText());
            daAll.put("brd_amt", myBrdAmt);
            daAll.put("drvBrdAmt", drvBrdAmt);

            /*Lodging Save*/
            JSONObject ldgSave = new JSONObject();

            ldgSave.put("lodgingStayTypeId", lodgingStayTypeId);
            ldgSave.put("isJointWork", isJointWork);
            ldgSave.put("isFullPay", isFullPay);
            ldgSave.put("allowType", allowType);

            ldgSave.put("ldg_type", lodgContvw.getVisibility() == View.VISIBLE ? txt_ldg_type.getText().toString() : "");
            ldgSave.put("sty_dte", lodgContvw.getVisibility() == View.VISIBLE ? sty_date + " " + ldg_cin.getText().toString() : "");
            ldgSave.put("to_dte", lodgContvw.getVisibility() == View.VISIBLE ? ldg_cout.getText().toString() : "");

            String toDate = ldg_coutDt.getText().toString().replaceAll("/", "-");
            toDate = toDate + " " + ldg_cout.getText().toString();

            ldgSave.put("toout_dte", lodgContvw.getVisibility() == View.VISIBLE ?/* ldg_coutDt.getText().toString()*/toDate : "");
            ldgSave.put("elgble", lodgContvw.getVisibility() == View.VISIBLE ?
                    txtMyEligi.getText().toString().replaceAll("₹", "") : "");
            ldgSave.put("LocID", lodgContvw.getVisibility() == View.VISIBLE ? sLocId : "");
            if (sLocId.equalsIgnoreCase("-1")) {
                ldgSave.put("ldg_type_sty", lodgContvw.getVisibility() == View.VISIBLE ? lodgStyLocation.getText().toString() : "");

            } else {
                ldgSave.put("ldg_type_sty", lodgContvw.getVisibility() == View.VISIBLE ? sLocName : "");
            }

            ldgSave.put("ldg_GstNo", edtLdgGstNum.getText().toString());
            ldgSave.put("ldg_GstAmt", edtLdgGstAmt.getText().toString());
            ldgSave.put("ldg_GstBillNo", edtLdgGstBillNo.getText().toString());

            ldgSave.put("ldg_drv_Styid",sDrvLocId);
            ldgSave.put("ldg_drv_StyNm",sDrvLocName);
            ldgSave.put("ldg_drv_need",((chkDrvAlw.isChecked()==true)?1:0));
            ldgSave.put("ldg_drv_Styid",sDrvLocId);
            ldgSave.put("ldg_drv_StyNm",sDrvLocName);
            ldgSave.put("ldg_drv_need",((chkDrvAlw.isChecked()==true)?1:0));

            ldgSave.put("noOfDays", "");
            ldgSave.put("bil_amt", lodgContvw.getVisibility() == View.VISIBLE ? edt_ldg_bill.getText().toString() : "");
            ldgSave.put("con_sty", lodgContvw.getVisibility() == View.VISIBLE ? cnSty : "");
            ldgSave.put("Erly_sty", lodgContvw.getVisibility() == View.VISIBLE ? erlSty : "");
            ldgSave.put("lte_sty", lodgContvw.getVisibility() == View.VISIBLE ? lteSty : "");
            ldgSave.put("Ear_chec_in", lodgContvw.getVisibility() == View.VISIBLE ? earCheckIn.getText().toString() : "");
            ldgSave.put("Ear_check_out", lodgContvw.getVisibility() == View.VISIBLE ? earCheckOut.getText().toString() : "");
            ldgSave.put("Ear_bill_amt", lodgContvw.getVisibility() == View.VISIBLE ? edtEarBill.getText().toString() : "");
            ldgSave.put("lat_chec_in", lodgContvw.getVisibility() == View.VISIBLE ? latCheckIn.getText().toString() : "");
            ldgSave.put("lat_check_out", lodgContvw.getVisibility() == View.VISIBLE ? latCheckOut.getText().toString() : "");
            ldgSave.put("lat_bill_amt", lodgContvw.getVisibility() == View.VISIBLE ? edtLateBill.getText().toString() : "");
            ldgSave.put("wob_amt", lodgContvw.getVisibility() == View.VISIBLE ?
                    ldgWOBBal.getText().toString().replaceAll("₹", "") : "");
            ldgSave.put("drv_ldg_amt", lodgContvw.getVisibility() == View.VISIBLE ?
                    txtDrivEligi.getText().toString().replaceAll("₹", "") : "");
            ldgSave.put("jnt_ldg_amt", lodgContvw.getVisibility() == View.VISIBLE ?
                    txtJNEligi.getText().toString().replaceAll("₹", "") : "");
            ldgSave.put("total_ldg_amt", lodgContvw.getVisibility() == View.VISIBLE ?
                    lbl_ldg_eligi.getText().toString().replaceAll("₹", "") : "");
            ldgSave.put("attch_bill", "");
            ldgSave.put("u_key", lodgContvw.getVisibility() == View.VISIBLE ? txtLodgUKey.getText().toString() : "");

            JSONArray ldgArySve = new JSONArray();


            if (lodgContvw.getVisibility() == View.VISIBLE) {
                for (int jd = 0; jd < jointLodging.getChildCount(); jd++) {
                    View jdV = jointLodging.getChildAt(jd);
                    edt_ldg_JnEmp = (EditText) jdV.findViewById(R.id.edt_ldg_JnEmp);
                    txtJNName = (TextView) jdV.findViewById(R.id.txtJNName);
                    txtJNDesig = (TextView) jdV.findViewById(R.id.txtJNDesig);
                    txtJNDept = (TextView) jdV.findViewById(R.id.txtJNDept);
                    txtJNHQ = (TextView) jdV.findViewById(R.id.txtJNHQ);
                    txtJNMob = (TextView) jdV.findViewById(R.id.txtJNMob);
                    txtJNMyEli = (TextView) jdV.findViewById(R.id.txtJNMyEli);
                    String strJNMyEli = txtJNMyEli.getText().toString().substring(txtJNMyEli.getText().toString().indexOf(".") + 1).trim();
                    String separator5 = ".";
                    int intJNMyEli = strJNMyEli.lastIndexOf(separator5);
                    JSONObject jsnLdgSve = new JSONObject();
                    jsnLdgSve.put("emp_cde", edt_ldg_JnEmp.getText().toString());
                    jsnLdgSve.put("emp_Name", txtJNName.getText().toString());
                    jsnLdgSve.put("emp_Desig", txtJNDesig.getText().toString());
                    jsnLdgSve.put("emp_Dept", txtJNDept.getText().toString());
                    jsnLdgSve.put("emp_HQ", txtJNHQ.getText().toString());
                    jsnLdgSve.put("emp_Mob", txtJNMob.getText().toString());
                    //  jsnLdgSve.put("emp_ldg_amt", strJNMyEli.substring(0, intJNMyEli));
                    jsnLdgSve.put("emp_ldg_amt", txtJNMyEli.getText().toString().replace("₹", ""));

                    ldgArySve.put(jsnLdgSve);
                }
            }


            ldgSave.put("Loding_Emp", ldgArySve);

            Log.v("Lodging_Save", ldgSave.toString());
            /*Travel Mode Json*/

            JSONObject trDet = new JSONObject();
            trDet.put("MOT", StrDaName);
            trDet.put("Start_Km", StartedKm);
            trDet.put("End_Km", ClosingKm);
            trDet.put("Tr_km", totalkm);
            trDet.put("Pr_km", PersonalKm);
            trDet.put("total_claim", totalPersonalKm);
            trDet.put("fuel_cha", strFuelAmount);
            trDet.put("fuel_amt", tofuel);
            trDet.put("st_km_img", "");
            trDet.put("ed_km_img", "");
            trDet.put("fuel_amount", strFuelAmount);
            trDet.put("ta_total_amount", BusAmount);
            JSONArray trvLoc = new JSONArray();
            int travelBike = travelDynamicLoaction.getChildCount();

            for (int i = 0; i < travelBike; i++) {
                JSONObject jsonTrLoc = new JSONObject();
                View views = travelDynamicLoaction.getChildAt(i);
                editText = views.findViewById(R.id.enter_mode);
                enterFrom = views.findViewById(R.id.enter_from);
                enterTo = views.findViewById(R.id.enter_to);
                enterFare = views.findViewById(R.id.enter_fare);
                edtTVGstNum = views.findViewById(R.id.edt_trvAll_gst);
                edtTVGstAmt = views.findViewById(R.id.edt_trvAll_gst_amt);
                edtTVGstBillNo = views.findViewById(R.id.edt_trvAll_gst_bno);
                deleteButton = views.findViewById(R.id.delete_button);
                tvTxtUKeys = views.findViewById(R.id.txt_tv_ukey);
                editMode = editText.getText().toString();

                edtRwID = views.findViewById(R.id.TARwID);
                editModeId = edtRwID.getText().toString();


                CtrlsListModel UTAItem = uTAItems.get(editModeId);
                if (editMode.equalsIgnoreCase("")) {
                    Toast.makeText(TAClaimActivity.this, "Select the Travel Mode", Toast.LENGTH_LONG).show();
                    ResetSubmitBtn(0, btnAnim);
                    return;
                }
                if (enterFrom.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(TAClaimActivity.this, "Enter the From", Toast.LENGTH_LONG).show();
                    ResetSubmitBtn(0, btnAnim);
                    enterFrom.requestFocus();
                    return;
                }
                if (enterTo.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(TAClaimActivity.this, "Enter the To", Toast.LENGTH_LONG).show();
                    ResetSubmitBtn(0, btnAnim);
                    enterTo.requestFocus();
                    return;
                }
                if (!editMode.equalsIgnoreCase("NONE") && enterFare.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(TAClaimActivity.this, "Enter the " + editMode + " Amount", Toast.LENGTH_LONG).show();
                    ResetSubmitBtn(0, btnAnim);
                    enterFare.requestFocus();
                    return;
                }


                String val = tvTxtUKeys.getText().toString();
                Log.v("TravelExpensesubmit:", val);
                if ((UTAItem == null || UTAItem.getAttachNeed().equalsIgnoreCase("1")) &&
                        tvTxtUKeys.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(TAClaimActivity.this, "Please attach supporting files for " + editMode, Toast.LENGTH_LONG).show();
                    ResetSubmitBtn(0, btnAnim);
                    return;
                }

                jsonTrLoc.put("mode", editText.getText().toString());
                jsonTrLoc.put("from", enterFrom.getText().toString());
                jsonTrLoc.put("to", enterTo.getText().toString());
                jsonTrLoc.put("fare", enterFare.getText().toString());
                jsonTrLoc.put("tv_gstNo",edtTVGstNum.getText().toString());
                jsonTrLoc.put("tv_gstAmt",edtTVGstAmt.getText().toString());
                jsonTrLoc.put("tv_gstBillNo",edtTVGstBillNo.getText().toString());
                jsonTrLoc.put("u_key", tvTxtUKeys.getText().toString());
                jsonTrLoc.put("attach_count", AttachmentImg.get(editMode));
                trvLoc.put(jsonTrLoc);
            }
            Log.v("TRAVEl_LOCATION", "0");

            travelBike = travelPlaces.getChildCount();
            for (int i = 0; i < travelBike; i++) {
                JSONObject jsonTrLoc = new JSONObject();
                View views = travelPlaces.getChildAt(i);
                etrTaFr = (EditText) views.findViewById(R.id.ta_edt_from);
                etrTaTo = (EditText) views.findViewById(R.id.ta_edt_to);
                jsonTrLoc.put("mode", "0");
                jsonTrLoc.put("from", etrTaFr.getText().toString());
                jsonTrLoc.put("to", etrTaTo.getText().toString());
                trvLoc.put(jsonTrLoc);
            }
            trDet.put("trv_loca", trvLoc);
            /*Local Convenyance*/
            JSONArray addExp = new JSONArray();
            int addExpSize = linlocalCon.getChildCount();

            for (int lc = 0; lc < addExpSize; lc++) {
                View view = linlocalCon.getChildAt(lc);
                edtRwID = view.findViewById(R.id.lcRwID);
                editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
                editLaFare = (EditText) (view.findViewById(R.id.edt_la_fare));
                lcGstLayout= (LinearLayout) (view.findViewById(R.id.lcConv_gstLayout));
                edtLCGstNum = (EditText) (view.findViewById(R.id.edt_lcConv_gst));
                edtLCGstAmt = (EditText) (view.findViewById(R.id.edt_lcConv_gst_amt));
                edtLCGstBillNo = (EditText) (view.findViewById(R.id.edt_lcConv_gst_bno));
                Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);
                lcTxtUKeys = (TextView) (view.findViewById(R.id.txt_lc_ukey));
                editMode = editTexts.getText().toString();
                editModeId = edtRwID.getText().toString();

                if (editMode.equalsIgnoreCase("")) {
                    Toast.makeText(TAClaimActivity.this, "Select the Convenyance", Toast.LENGTH_LONG).show();
                    ResetSubmitBtn(0, btnAnim);
                    return;
                }
                CtrlsListModel ULCItem = uLCItems.get(editModeId);
                JSONArray lcModeRef = new JSONArray();
                JSONObject lcMode = new JSONObject();
                if (ULCItem != null) {
                    lcMode.put("type", editMode);
                    lcMode.put("attach_count", ULCItem.getAttachNeed());
                    lcMode.put("lc_gstNo",edtLCGstNum.getText().toString());
                    lcMode.put("lc_gstAmt",edtLCGstAmt.getText().toString());
                    lcMode.put("lc_gstBillNo",edtLCGstBillNo.getText().toString());

                    lcMode.put("total_amount", editLaFare.getText().toString());
                    lcMode.put("u_key", lcTxtUKeys.getText().toString());
                    lcMode.put("exp_type", "LC");

                    List<CtrlsListModel.Ctrls> lstCtrl = ULCItem.getCtrlsList();
                    for (int da = 0; da < lstCtrl.size(); da++) {
                        String lblDetCap = lstCtrl.get(da).getTxtLabel();
                        EditText txlcDet = lstCtrl.get(da).getTxtValue();
                        if (txlcDet.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(TAClaimActivity.this, "Enter the " + lblDetCap, Toast.LENGTH_LONG).show();
                            ResetSubmitBtn(0, btnAnim);
                            txlcDet.requestFocus();
                            return;
                        }
                        JSONObject AditionallLocalConvenyance = new JSONObject();
                        AditionallLocalConvenyance.put("KEY", lblDetCap);
                        AditionallLocalConvenyance.put("VALUE", txlcDet.getText().toString());
                        lcModeRef.put(AditionallLocalConvenyance);
                    }

                    if (!editMode.equalsIgnoreCase("NONE") && editLaFare.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(TAClaimActivity.this, "Enter the " + editMode + " Amount", Toast.LENGTH_LONG).show();
                        ResetSubmitBtn(0, btnAnim);
                        editLaFare.requestFocus();
                        return;
                    }
                    if ((ULCItem == null || ULCItem.getAttachNeed().equalsIgnoreCase("1")) && lcTxtUKeys.getText().toString().equalsIgnoreCase("")) {
                        ResetSubmitBtn(0, btnAnim);
                        Toast.makeText(TAClaimActivity.this, "Please attach supporting files for " + editMode, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                lcMode.put("ad_exp", lcModeRef);
                addExp.put(lcMode);
            }

            /*Other Expensive*/
            JSONArray othrExp = new JSONArray();
            int addOtherExp = LinearOtherAllowance.getChildCount();

            for (int OC = 0; OC < addOtherExp; OC++) {
                View view = LinearOtherAllowance.getChildAt(OC);

                edtRwID = view.findViewById(R.id.oeRwID);
                oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
                edtOE = (EditText) (view.findViewById(R.id.oe_fre_amt));
                edtOEGstNum = (EditText) (view.findViewById(R.id.edt_otherExp_gst));
                edtOEGstAmt = (EditText) (view.findViewById(R.id.edt_otherExp_gst_amt));
                edtOEGstBillNo = (EditText) (view.findViewById(R.id.edt_otherExp_gst_bno));
                OtherExpense = (LinearLayout) view.findViewById(R.id.lin_other_expense_dynamic);
                editMode = oeEditext.getText().toString();
                oeTxtUKey = (TextView) (view.findViewById(R.id.txt_oe_ukey));
                editModeId = edtRwID.getText().toString();

                if (editMode.equalsIgnoreCase("")) {
                    Toast.makeText(TAClaimActivity.this, "Select the Other Expense", Toast.LENGTH_LONG).show();
                    ResetSubmitBtn(0, btnAnim);
                    return;
                }
                CtrlsListModel UOEItem = uOEItems.get(editModeId);
                JSONArray lcModeRef1 = new JSONArray();
                JSONObject lcModes2 = new JSONObject();
                if (UOEItem != null) {
                    lcModes2.put("type", editMode);
                    lcModes2.put("attach_count", UOEItem.getAttachNeed());
                    lcModes2.put("otherExp_gstNo",edtOEGstNum.getText().toString());
                    lcModes2.put("otherExp_gstAmt",edtOEGstAmt.getText().toString());
                    lcModes2.put("otherExp_gstBillNo",edtOEGstBillNo.getText().toString());
                    lcModes2.put("total_amount", edtOE.getText().toString());
                    lcModes2.put("u_key", oeTxtUKey.getText().toString());
                    lcModes2.put("exp_type", "OE");

                    List<CtrlsListModel.Ctrls> lstCtrl = UOEItem.getCtrlsList();
                    for (int da = 0; da < lstCtrl.size(); da++) {
                        String lblDetCap = lstCtrl.get(da).getTxtLabel();
                        EditText txoeDet = lstCtrl.get(da).getTxtValue();
                        if (txoeDet.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(TAClaimActivity.this, "Enter the " + lblDetCap, Toast.LENGTH_LONG).show();
                            txoeDet.requestFocus();
                            ResetSubmitBtn(0, btnAnim);
                            return;
                        }
                        JSONObject AditionallLocalConvenyance = new JSONObject();
                        AditionallLocalConvenyance.put("KEY", lblDetCap);
                        AditionallLocalConvenyance.put("VALUE", txoeDet.getText().toString());

                        lcModeRef1.put(AditionallLocalConvenyance);
                    }
                    if (!editMode.equalsIgnoreCase("NONE") && edtOE.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(TAClaimActivity.this, "Enter the " + editMode + " Amount", Toast.LENGTH_LONG).show();
                        edtOE.requestFocus();
                        ResetSubmitBtn(0, btnAnim);
                        return;
                    }
                    if ((UOEItem == null || UOEItem.getAttachNeed().equalsIgnoreCase("1")) && oeTxtUKey.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(TAClaimActivity.this, "Please attach supporting files for " + editMode, Toast.LENGTH_LONG).show();
                        ResetSubmitBtn(0, btnAnim);
                        return;
                    }
                }

                lcModes2.put("ad_exp", lcModeRef1);
                othrExp.put(lcModes2);
                Log.v("OE_EXPENSE", lcModes2.toString());

            }

            jsonData.put("Add_Exp", addExp);
            jsonData.put("Other_Exp", othrExp);
            jsonData.put("Trv_details", trDet);
            jsonData.put("Lodg_details", ldgSave);
            jsonData.put("Da_Claim", daAll);
            transHead.put(jsonData);

            Log.v("TA_datalist",transHead.toString());

            /*ImageStore();*/


            Call<ResponseBody> submit;
            if (responseVal.equals("Save")) {
                submit = apiInterface.saveDailyAllowance(jsonData.toString());
            } else {
                submit = apiInterface.submitOfApp(jsonData.toString());
            }


            Log.v("TA_REQUEST", submit.request().toString());
            submit.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    Log.v("TA_Response", response.body().toString());
                    //startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    // openHome();
                    if (responseVal.equals("Save")) {
                        Toast.makeText(TAClaimActivity.this, "Saved Successfully ", Toast.LENGTH_SHORT).show();
                        openHome();
                    } else {
                        Toast.makeText(TAClaimActivity.this, "Submitted Successfully ", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(TAClaimActivity.this, ViewTAStatus.class));

                    }
                    ResetSubmitBtn(1, btnAnim);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ResetSubmitBtn(2, btnAnim);
                }
            });

        } catch (Exception e) {
            ResetSubmitBtn(0, btnAnim);
            AlertDialogBox.showDialog(TAClaimActivity.this, HAPApp.Title, String.valueOf(Html.fromHtml("Can't submit your claim. <br> " + e.getMessage())), "OK", "", false, new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {

                }
            });
            Log.e("TOTAL_JSON_OUT", e.toString());
        }
    }

    public void ResetSubmitBtn(int resetMode, CircularProgressButton btnAnim) {
        long dely = 10;
        if (resetMode != 0) dely = 1000;
        if (resetMode == 1) {
            btnAnim.doneLoadingAnimation(getResources().getColor(R.color.green), BitmapFactory.decodeResource(getResources(), R.drawable.done));
        } else {
            btnAnim.doneLoadingAnimation(getResources().getColor(R.color.color_red), BitmapFactory.decodeResource(getResources(), R.drawable.ic_wrong));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnAnim.stopAnimation();
                btnAnim.revertAnimation();
                btnAnim.setBackground(getDrawable(R.drawable.button_blueg));
            }
        }, dely);

    }

    public void getMulipart(String count, String path, String x, String imageKEY, String mode, String from, String to) {
        Log.v("PATH_IMAGE", "" + path);

        if (path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".pdf")) {
            MultipartBody.Part imgg = convertimg("file", path);
            Log.v("PATH_IMAGE_imgg", String.valueOf(imgg));
            sendImageToServer(count, x, mode, from, to, imgg);

        } else
            mCommon_class.showMsg(this, "Image and Pdf file only supported");
    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
        try {
            if (!TextUtils.isEmpty(path)) {

                File file = new File(path);
                if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
                    file = new Compressor(getApplicationContext()).compressToFile(new File(path));
                else
                    file = new File(path);
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
                yy = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);
            }
        } catch (Exception e) {
        }
        return yy;
    }

    public void callApi(String date, String OS) {

        ArrayList listValue = new ArrayList();
        jLCitems = new JSONArray();
        jOEitems = new JSONArray();
        try {
            JSONObject jj = new JSONObject();
            jj.put("Ta_Date", date);
            jj.put("div", div);
            jj.put("sf", SF_code);
            jj.put("rSF", SF_code);
            jj.put("State_Code", State_Code);
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.getDailyAllowance(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData);
                            array = new ArrayList<>();

                            JSONObject js = new JSONObject(jsonData);
                            JSONArray jsnArValue = js.getJSONArray("ExpenseWeb");
                            for (int i = 0; i < jsnArValue.length(); i++) {
                                JSONObject json_oo = jsnArValue.getJSONObject(i);

                                Exp_Name = json_oo.getString("Name");
                                shortName = json_oo.getString("Short_Name");
                                Id = String.valueOf(json_oo.get("ID"));
                                userEnter = json_oo.getString("user_enter");
                                attachment = json_oo.getString("Attachemnt");
                                maxAllowonce = json_oo.getString("Max_Allowance");
                                if (shortName.equals("Other Expense")) {
                                    OE.add(Exp_Name);
                                    jOEitems.put(json_oo);
                                }
                                if (shortName.equals("Local Conveyance")) {
                                    LC.add(Exp_Name);
                                    jLCitems.put(json_oo);
                                }
                                Log.v("Response_Count", shortName + " : " + attachment + " : " + Exp_Name);

                                listValue.add(shortName);
                                AttachmentImg.put(Exp_Name, attachment);

                                if (shortName.equals("Daily Allowance")) {
                                    DA.add(Exp_Name);
                                }

                                /*if (shortName.equals("Other Expense")) {
                                    OE.add(Exp_Name);
                                    jLCitems=null,jOTitems
                                }
                                if (shortName.equals("Local Conveyance")) {
                                    LC.add(Exp_Name);
                                    jLCitems=null,jOTitems
                                }*/
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

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 9) {
            stayDays.setVisibility(View.VISIBLE);
            isSavedBill = false;

            isJointWork = myDataset.get(position).getFlag().equals("1");
            isFullPay = myDataset.get(position).getCheckouttime().equals("1");

            ValCd = myDataset.get(position).getId();
            lodgingStayTypeId = myDataset.get(position).getId();
            String Valname = myDataset.get(position).getName();

            if (LodingCon.size() != 0) {
                mChckEarly.setVisibility(View.GONE);
            }
            txt_ldg_type.setText(Valname);
            lodgCont.setVisibility(View.VISIBLE);
            ldg_stayloc.setVisibility(View.GONE);
            ldg_stayDt.setVisibility(View.GONE);
            lodgJoin.setVisibility(View.GONE);
            JNLdgEAra.setVisibility(View.GONE);
            edt_ldg_bill.setVisibility(View.GONE);
            ldgGstLayout.setVisibility(View.GONE);
            lblHdBill.setVisibility(View.GONE);
            linImgPrv.setVisibility(View.GONE);
            viewBilling.setVisibility(View.GONE);
            lblHdBln.setVisibility(View.GONE);
            ldgWOBBal.setVisibility(View.GONE);
            linContinueStay.setVisibility(View.VISIBLE);
            ldg_stayDt.setVisibility(View.VISIBLE);

            if (myldgEliAmt.equalsIgnoreCase("")) myldgEliAmt = "0.0";

            myldgEliAmt = myldgEliAmt.replaceAll("^[\"']+|[\"']+$", "");
            jointLodging.removeAllViews();
            ldgEliAmt = Double.valueOf(myldgEliAmt);
            txtMyEligi.setText("₹" + new DecimalFormat("##0.00").format(ldgEliAmt));

            mChckCont.setChecked(false);
            mChckLate.setChecked(false);
            mChckEarly.setChecked(false);
            viewBilling.setVisibility(View.VISIBLE);

            if (isJointWork) {
                linContinueStay.setVisibility(View.VISIBLE);
                lodgJoin.setVisibility(View.VISIBLE);
                JNLdgEAra.setVisibility(View.VISIBLE);
                img_lodg_prvw.setVisibility(View.VISIBLE);
                linImgPrv.setVisibility(View.VISIBLE);
                viewBilling.setVisibility(View.VISIBLE);
                /*tTotAmt = 0;*/
                ttLod = 1;
                txtMyEligi.setText("₹" + new DecimalFormat("##0.00").format(ldgEliAmt));
                lodingView();
            }

            if (isFullPay) {
                viewBilling.setVisibility(View.GONE);
                linImgPrv.setVisibility(View.GONE);
            } else {
                lblHdBill.setVisibility(View.VISIBLE);
                edt_ldg_bill.setVisibility(View.VISIBLE);
                ldgGstLayout.setVisibility(View.VISIBLE);
                lblHdBln.setVisibility(View.VISIBLE);
                ldgWOBBal.setVisibility(View.VISIBLE);
                linImgPrv.setVisibility(View.VISIBLE);
                viewBilling.setVisibility(View.VISIBLE);

                ttLod = 1;
                /*tTotAmt = 0;*/

                txtMyEligi.setText("₹" + new DecimalFormat("##0.00").format(ldgEliAmt));
                ldgWOBBal.setText("₹" + new DecimalFormat("##0.00").format(ldgEliAmt));
                lbl_ldg_eligi.setText("₹" + new DecimalFormat("##0.00").format(ldgEliAmt));
                img_lodg_prvw.setVisibility(View.VISIBLE);
            }

            ldg_stayloc.setVisibility(View.VISIBLE);
            //ldg_cout.setText("");
            //ldg_coutDt.setText("");
            if (ldg_cin.getText().toString().equals("") || ldg_coutDt.getText().toString().equals("")) {
                TotalDays.setVisibility(View.GONE);
            } else {
                TotalDays.setVisibility(View.VISIBLE);
                if (DT.Daybetween(DateTime + " 00:00:00", COutDate + " 00:00:00") < 1)
                    linContinueStay.setVisibility(View.GONE);
            }

            edt_ldg_bill.setText("");
            edtLdgGstNum.setText("");
            edtLdgGstAmt.setText("");
            edtLdgGstBillNo.setText("");

            sLocId = "";
            sLocName = "";
            sDrvLocName = "";
            allowType = "";
            txt_Styloc.setText("");
            txt_drvStyloc.setText("");
            lodgStyLocation.setText("");
            drvStyLocation.setText("");
            ldgEliAmt = 0.0;
            txtMyEligi.setText("₹" + new DecimalFormat("##0.00").format(ldgEliAmt));

            getStayAllow();
            SumOFJointLodging();
        }
        if (type == 12) {
            boolean sameDrvldg=false;
            if (sDrvLocId=="" || sDrvLocId==sLocId) sameDrvldg=true;
            sLocId = myDataset.get(position).getId();
            sLocName = myDataset.get(position).getName();
            sDrvLocName = myDataset.get(position).getName();
            allowType = myDataset.get(position).getJSONObject().optString("HQ_Type");

            if(sameDrvldg==true){
                sDrvLocId = myDataset.get(position).getId();
                sDrvLocName = myDataset.get(position).getName();

            }
            txt_Styloc.setText(sLocName);
            txt_drvStyloc.setText(sDrvLocName);
            lodgStyLocation.setText(sLocName);
            drvStyLocation.setText(sDrvLocName);

            lodgStyLocation.setVisibility(View.GONE);
            if (sLocId.equalsIgnoreCase("-1")) {
                lodgStyLocation.setVisibility(View.VISIBLE);
                lodgStyLocation.setText("");
            }
            getStayAllow();
        }
        if (type == 15) {
            sDrvLocId = myDataset.get(position).getId();
            sDrvLocName = myDataset.get(position).getName();
            txt_drvStyloc.setText(sDrvLocName);
            getStayAllow();
        }

        if (type == 13) {
            sDALocId = myDataset.get(position).getId();
            sDALocName = myDataset.get(position).getName();
            JSONObject itm = myDataset.get(position).getJSONObject();
            txDAOthName.setVisibility(View.GONE);
            if (sDALocId.equalsIgnoreCase("-1")) {
                txDAOthName.setVisibility(View.VISIBLE);
            }

            txt_DAStyloc.setText(sDALocName);
            try {
                if (sDALocId.equalsIgnoreCase("-1")) {
                    txtAllwType.setText("Others");
                    txtCAllwType.setText("Allowance : Others");
                } else {
                    txtAllwType.setText(itm.getString("HQ_Type"));
                    txtCAllwType.setText("Allowance : " + itm.getString("HQ_Type"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (type == 14) {
            sDALType = myDataset.get(position).getName();
            txt_DATyp.setText(sDALType);

            if (myDataset.get(position).getName().equalsIgnoreCase("NONE")) {
                findViewById(R.id.llAllowLocParent).setVisibility(View.GONE);
            } else {
                findViewById(R.id.llAllowLocParent).setVisibility(View.VISIBLE);
            }
        }
        if (type == 10) {
            clearAll();

            txt_date.setText(myDataset.get(position).getName());
            Log.d("JSON_VALUE", myDataset.get(position).getId());
            DateTime = myDataset.get(position).getId();
            DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
            changeDate(DateTime);
            /*
            txt_date.setText(myDataset.get(position).getName());
            Log.d("JSON_VALUE", myDataset.get(position).getId());
            DateTime = myDataset.get(position).getId();
            DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
            displayTravelMode(myDataset.get(position).getId());
            travelDynamicLoaction.removeAllViews();
            linAddAllowance.setVisibility(View.VISIBLE);
            linlocalCon.removeAllViews();
            LinearOtherAllowance.removeAllViews();
            localText.setText("");
            OeText.setText("");
            localTotal.setVisibility(View.GONE);
            otherExpenseLayout.setVisibility(View.GONE);
            linAll.setVisibility(View.VISIBLE);
            linRemarks.setVisibility(View.VISIBLE);
            linFareAmount.setVisibility(View.VISIBLE);
            linback.setVisibility(View.GONE);*/

        }
        else if (type == 11) {
            modeTextView.setText(myDataset.get(position).getName());
        }
        else if (type == 8) {
            Integer editTextPosition = myDataset.get(position).getPho();
            View view = travelDynamicLoaction.getChildAt(editTextPosition);
            editText = (TextView) (view.findViewById(R.id.enter_mode));
            EditText txtTAFrom = (EditText) (view.findViewById(R.id.enter_from));
            EditText txtTATo = (EditText) (view.findViewById(R.id.enter_to));
            EditText txtTAFare = (EditText) (view.findViewById(R.id.enter_fare));

            ImageView imgAtt = view.findViewById(R.id.image_attach);
            ImageView imgPrv = view.findViewById(R.id.image_preview);
            editText.setText(myDataset.get(position).getName());

            TextView txRwID = view.findViewById(R.id.TARwID);
            if (!txRwID.getText().toString().equalsIgnoreCase(""))
                uTAItems.remove(txRwID.getText().toString());

            String sRWID = myDataset.get(position).getName() + "_" + System.nanoTime();
            txRwID.setText(sRWID);

            JSONObject Selitem = myDataset.get(position).getJSONObject();
            int maxVal = 50000;
            String AttFlg = "0";

            String Alw_Eligibilty = "";
            try {
                AttFlg = Selitem.getString("Attachemnt");
                maxVal = Selitem.getInt("Max_Allowance");
                if (maxVal == 0) maxVal = 50000;

                Alw_Eligibilty = Selitem.getString("Alw_Eligibilty");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            txtTAFare.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, maxVal)});

            List<CtrlsListModel.Ctrls> users = new ArrayList<>();
            CtrlsListModel.Ctrls Ctrl = new CtrlsListModel.Ctrls("From", txtTAFrom);
            users.add(Ctrl);
            Ctrl = new CtrlsListModel.Ctrls("To", txtTATo);
            users.add(Ctrl);
            CtrlsListModel UTAItem = new CtrlsListModel(users, AttFlg);
            uTAItems.put(sRWID, UTAItem);
            if (AttFlg.equals("1")) {
                imgAtt.setVisibility(View.VISIBLE);
                imgPrv.setVisibility(View.VISIBLE);
                tvGstLayout.setVisibility(View.VISIBLE);

            } else {
                imgAtt.setVisibility(View.GONE);
                imgPrv.setVisibility(View.GONE);
                tvGstLayout.setVisibility(View.GONE);
            }

            if (Alw_Eligibilty.equalsIgnoreCase("0")) {
                txtTAFare.setText("0");
                txtTAFare.setEnabled(false);
            } else {
                txtTAFare.setEnabled(true);
            }

            if (myDataset.get(position).getName().equalsIgnoreCase("NONE")) {
                txtTAFare.setText("0");
                txtTAFare.setVisibility(View.GONE);
            } else {
                txtTAFare.setVisibility(View.VISIBLE);
            }
        }
        else if (type == 80) {
            editTextPositionss = myDataset.get(position).getPho();
            View view = linlocalCon.getChildAt(editTextPositionss);

            edtRwID = view.findViewById(R.id.lcRwID);
            editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
            edtLcFare = (EditText) (view.findViewById(R.id.edt_la_fare));
            lcAttach = (ImageView) (view.findViewById(R.id.la_attach_iamg));
            lcGstLayout=(LinearLayout)(view.findViewById(R.id.lcConv_gstLayout));
            lcPreview = (ImageView) (view.findViewById(R.id.img_prvw_lc));

            edtLcFare.setText("");

            if (!edtRwID.getText().toString().equalsIgnoreCase(""))
                uLCItems.remove(edtRwID.getText().toString());

            String sRWID = myDataset.get(position).getName() + "_" + System.nanoTime();
            edtRwID.setText(sRWID);

            editTexts.setText(myDataset.get(position).getName());
            StrModeValue = myDataset.get(position).getName();
            Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);
            Dynamicallowance.removeAllViews();

            JSONObject Selitem = myDataset.get(position).getJSONObject();
            JSONArray AddFlds = null;
            int maxVal = 1000;
            String AttFlg = "0";
            try {
                AddFlds = Selitem.getJSONArray("value");
                AttFlg = Selitem.getString("Attachemnt");
                maxVal = Selitem.getInt("Max_Allowance");
                if (maxVal == 0) maxVal = 1000;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            edtLcFare.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, maxVal)});

            LocalConvenyanceApi(sRWID, AddFlds, AttFlg);

            if (AttFlg.equals("1")) {
                lcAttach.setVisibility(View.VISIBLE);
                lcPreview.setVisibility(View.VISIBLE);
                lcGstLayout.setVisibility(View.VISIBLE);
            } else {
                lcAttach.setVisibility(View.GONE);
                lcPreview.setVisibility(View.GONE);
                lcGstLayout.setVisibility(View.GONE);
            }

        }
        else if (type == 90) {

            editTextPositionss = myDataset.get(position).getPho();
            View view = LinearOtherAllowance.getChildAt(editTextPositionss);

            edtRwID = view.findViewById(R.id.oeRwID);
            oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
            edtOE = (EditText) (view.findViewById(R.id.oe_fre_amt));
            oEGstLayout =(LinearLayout) (view.findViewById(R.id.otherExp_gstLayout));

            oeAttach = (ImageView) (view.findViewById(R.id.oe_attach_img));
            oePreview = (ImageView) (view.findViewById(R.id.img_prvw_oe));
            OtherExpense = (LinearLayout) view.findViewById(R.id.lin_other_expense_dynamic);
            oeEditext.setText(myDataset.get(position).getName());
            StrModeValue = myDataset.get(position).getName();

            if (!edtRwID.getText().toString().equalsIgnoreCase(""))
                uOEItems.remove(edtRwID.getText().toString());

            String sRWID = myDataset.get(position).getName() + "_" + System.nanoTime();
            edtRwID.setText(sRWID);

            OtherExpense.removeAllViews();

            JSONObject Selitem = myDataset.get(position).getJSONObject();
            JSONArray AddFlds = null;
            int maxVal = 1000;
            String AttFlg = "0";
            try {
                AddFlds = Selitem.getJSONArray("value");
                AttFlg = Selitem.getString("Attachemnt");
                maxVal = Selitem.getInt("Max_Allowance");
                if (maxVal == 0) maxVal = 1000;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            edtOE.setText("");
            edtOE.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, maxVal)});
            OtherExpenseApi(sRWID, AddFlds, AttFlg);

            attachCountList.add(AttachmentImg.get(StrModeValue));

            if (AttFlg.equals("1")) {
                oeAttach.setVisibility(View.VISIBLE);
                oePreview.setVisibility(View.VISIBLE);
                oEGstLayout.setVisibility(View.VISIBLE);
            } else {
                oeAttach.setVisibility(View.GONE);
                oePreview.setVisibility(View.GONE);
                oEGstLayout.setVisibility(View.GONE);
            }


        }
        else if (type == 100) {
            String TrTyp = myDataset.get(position).getName();
            travelTypeMode.setText(TrTyp);

            if (TrTyp.equalsIgnoreCase("OS") == true) {
                callApi(DateForAPi, "OS");
                diverAllowanceLinear.setVisibility(View.VISIBLE);
            } else {
                callApi(DateForAPi, "");
                diverAllowanceLinear.setVisibility(View.GONE);
            }

        }
        else if (type == 1) {
            enterMode.setText(myDataset.get(position).getName());
        }

    }

    private void getLodgingAllowanceEligibleAmt() {
        if (lodgingStayTypeId.isEmpty()) {
            return;
        }
        ldgDrvEligi = 0.0;
        chkDrvAlw.setVisibility(View.GONE);
        txtDrivEligi.setText("₹" + new DecimalFormat("##0.00").format(ldgDrvEligi));
        assistantClass.showProgressDialog("Loading Lodging allowance eligible amount...", false);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "getLodgingAllowanceEligibleAmt");
        params.put("logCode", lodgingStayTypeId);
        params.put("allowanceType", allowType);
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                ldgEliAmt = jsonObject.optDouble("amount");
                txtMyEligi.setText("₹" + new DecimalFormat("##0.00").format(ldgEliAmt));
                SumOFLodging(0);
                assistantClass.dismissProgressDialog();
            }

            @Override
            public void onFailure(String error) {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithDismiss(error);
            }
        });
    }

    public void changeDate(String chooseDate) {
        Ukey = Common_Class.GetEkey();
        displayTravelMode(chooseDate);
        travelDynamicLoaction.removeAllViews();
        linAddAllowance.setVisibility(View.VISIBLE);
        linlocalCon.removeAllViews();
        LinearOtherAllowance.removeAllViews();
        localText.setText("");
        OeText.setText("");
        localTotal.setVisibility(View.GONE);
        otherExpenseLayout.setVisibility(View.GONE);
        linAll.setVisibility(View.VISIBLE);
        linRemarks.setVisibility(View.VISIBLE);
        linFareAmount.setVisibility(View.VISIBLE);
    }

    public void getStayAllow() {
        getLodgingAllowanceEligibleAmt();

        /*JSONObject item = new JSONObject();
        try {
            item.put("sfCode", UserDetails.getString("Sfcode", ""));
            item.put("HQID", sLocId);
            item.put("DHQID", sDrvLocId);
            item.put("ExpDt", DateTime);
        } catch (JSONException e) {

        }

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = service.getLDGAllowance("get/StayAllw", item.toString());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray AlwDets = response.body();
                drvldgEAra.setVisibility(View.GONE);
                ldgDrvEligi = 0.0;
                chkDrvAlw.setVisibility(View.GONE);
                txtDrivEligi.setText("₹" + new DecimalFormat("##0.00").format(ldgDrvEligi));
                if (AlwDets.size() > 0) {
                    JsonObject item = AlwDets.get(0).getAsJsonObject();
                    ldgEliAmt = item.get("myLdgAmt").getAsDouble();
                    txtMyEligi.setText("₹" + new DecimalFormat("##0.00").format(ldgEliAmt));
                    if (DriverNeed.equalsIgnoreCase("true")) {
                        drvldgEAra.setVisibility(View.VISIBLE);
                        chkDrvAlw.setVisibility(View.VISIBLE);
                        chkDrvAlw.setChecked(true);
                        ldgDrvEligi = item.get("DrvLdgAmt").getAsDouble();
                        ldgDrvAlw=ldgDrvEligi;
                        txtDrivEligi.setText("₹" + new DecimalFormat("##0.00").format(ldgDrvEligi));
                    }
                    SumOFLodging(0);
                    Log.d("Alwance:", String.valueOf(AlwDets));
                } else {
                    Toast.makeText(TAClaimActivity.this, "No Allowance fixed for this Place", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                Log.d("Error:", "Alwance Error");
            }
        });*/

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    public void localCon(Integer countPosition) {

        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        String commonLeaveType = "{\"tableName\":\"getmodeoftravel\",\"coloumns\":\"[\\\"id\\\",\\\"name\\\",\\\"Leave_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GetRouteObjects(QueryString, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                userType = new TypeToken<ArrayList<com.saneforce.godairy.Model_Class.ModeOfTravel>>() {
                }.getType();
                modelOfTravel = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < modelOfTravel.size(); i++) {
                    String id = String.valueOf(modelOfTravel.get(i).getStEndNeed());
                    String name = modelOfTravel.get(i).getName();
                    String modeId = String.valueOf(modelOfTravel.get(i).getId());
                    String driverMode = String.valueOf(modelOfTravel.get(i).getDriverNeed());
                    JSONObject item = new JSONObject();
                    try {
                        item.put("id", id);
                        item.put("name", name);
                        item.put("modeId", modeId);
                        item.put("Attachemnt", modelOfTravel.get(i).getAttachemnt());
                        item.put("Max_Allowance", modelOfTravel.get(i).getMax_Allowance());
                        item.put("Alw_Eligibilty", modelOfTravel.get(i).getAlw_Eligibilty());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Model_Pojo = new Common_Model(id, name, item, countPosition);
                    //Model_Pojo = new Common_Model(name, id, modeId, "", countPosition);
                    if (id.equals("0")) {
                        modelTravelType.add(Model_Pojo);
                    }
                }

                customDialog = new CustomListViewDialog(TAClaimActivity.this, modelTravelType, 8);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }

    public void loadLocations() {
        db = new DatabaseHandler(this);
        try {
            JSONArray HAPLoca = db.getMasterData("HAPLocations");
            if (HAPLoca != null) {
                for (int li = 0; li < HAPLoca.length(); li++) {
                    JSONObject jItem = HAPLoca.getJSONObject(li);
                    Common_Model item = new Common_Model(jItem.getString("id"), jItem.getString("name"), jItem);
                    ldgLocations.add(item);
                }
            }
            Common_Model itemOth = new Common_Model("-1", "Other Location", "");
            ldgLocations.add(itemOth);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        JSONObject jj = new JSONObject();
        try {
            jj.put("sfCode", Shared_Common_Pref.Sf_Code);
            jj.put("divisionCode", Shared_Common_Pref.Div_Code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.getBusTo(jj.toString());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    String id = String.valueOf(jsonObject.get("id"));
                    String name = String.valueOf(jsonObject.get("name"));
                    name = name.replaceAll("^[\"']+|[\"']+$", "");
                    id = id.replaceAll("^[\"']+|[\"']+$", "");
                    mCommon_model_spinner = new Common_Model(id, name, "");
                    ldgLocations.add(mCommon_model_spinner);
                }
                mCommon_model_spinner = new Common_Model("-1","Other Location","");
                ldgLocations.add(mCommon_model_spinner);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });*/

    }

    public void LDGType() {
        customDialog = new CustomListViewDialog(TAClaimActivity.this, ldgModes, 9);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }

    public void dynamicModeType(Integer poisition) {
        for (int i = 0; i < jLCitems.length(); i++) {
            try {
                JSONObject lcItem = jLCitems.getJSONObject(i);
                String name = lcItem.getString("Name");
                mCommon_model_spinner = new Common_Model(name, name, lcItem, poisition);
                listOrderType.add(mCommon_model_spinner);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        customDialog = new CustomListViewDialog(TAClaimActivity.this, listOrderType, 80);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }

    public void OtherExpenseMode(Integer poisition) {

        for (int i = 0; i < jOEitems.length(); i++) {
            try {
                JSONObject oeItem = jOEitems.getJSONObject(i);
                String name = oeItem.getString("Name");
                mCommon_model_spinner = new Common_Model(name, name, oeItem, poisition);
                OtherExpenseList.add(mCommon_model_spinner);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        customDialog = new CustomListViewDialog(TAClaimActivity.this, OtherExpenseList, 90);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }
    /*public void LocalConvenyanceApi(String sss) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        try {

            JSONObject jj = new JSONObject();
            jj.put("Ta_Date", "");
            jj.put("div", div);
            jj.put("sf", SF_code);
            jj.put("rSF", SF_code);
            jj.put("State_Code", State_Code);
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.getDailyAllowance(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @SuppressLint({"ResourceType", "NewApi"})
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {

                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData);
                            JSONObject js = new JSONObject(jsonData);
                            JSONArray jsnArValue = js.getJSONArray("ExpenseWeb");

                            for (int i = 0; i < jsnArValue.length(); i++) {
                                JSONObject jsonHeaderObject = jsnArValue.getJSONObject(i);
                                String Exp_Name = jsonHeaderObject.getString("Name");
                                if (Exp_Name.equals(sss)) {
                                    JSONArray additionArray = null;
                                    additionArray = jsonHeaderObject.getJSONArray("value");

                                    List<EditText> users = new ArrayList<>();
                                    for (int l = 0; l <= additionArray.length(); l++) {
                                        JSONObject json_in = additionArray.getJSONObject(l);
                                        dynamicLabel = json_in.getString("Ad_Fld_Name");
                                        dynamicLabelList.add(dynamicLabel);
                                        RelativeLayout childRel = new RelativeLayout(getApplicationContext());
                                        RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_START);
                                        layoutparams_3.setMargins(20, -10, 0, 0);
                                        edt = new EditText(getApplicationContext());
                                        edt.setLayoutParams(layoutparams_3);
                                        edt.setHint(dynamicLabel);
                                        edt.setId(12345);
                                        edt.setTextSize(13);
                                        edt.setTextColor(R.color.grey_500);
                                        childRel.addView(edt);
                                        users.add(edt);

                                        if (l == additionArray.length() - 1) {
                                            usersByCountry.put(sss, users);
                                        }
                                        View view = linlocalCon.getChildAt(editTextPositionss);
                                        Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);
                                        Dynamicallowance.addView(childRel);
                                    }
                                }

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
    }*/
    /*public void OtherExpenseApi(String sss) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        try {
            JSONObject jj = new JSONObject();
            jj.put("Ta_Date", "");
            jj.put("div", div);
            jj.put("sf", SF_code);
            jj.put("rSF", SF_code);
            jj.put("State_Code", State_Code);
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.getDailyAllowance(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @SuppressLint({"ResourceType", "NewApi"})
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            Log.v("print_upload_file_true", "ggg" + response);
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData);
                            JSONObject js = new JSONObject(jsonData);
                            JSONArray jsnArValue = js.getJSONArray("ExpenseWeb");

                            for (int i = 0; i < jsnArValue.length(); i++) {
                                JSONObject jsonHeaderObject = jsnArValue.getJSONObject(i);
                                String Exp_Name = jsonHeaderObject.getString("Name");

                                if (Exp_Name.equals(sss)) {
                                    JSONArray additionArray = null;
                                    additionArray = jsonHeaderObject.getJSONArray("value");
                                    List<EditText> otherExpenseEdit = new ArrayList<>();
                                    for (int l = 0; l <= additionArray.length(); l++) {
                                        JSONObject json_in = additionArray.getJSONObject(l);
                                        OEdynamicLabel = json_in.getString("Ad_Fld_Name");
                                        OEdynamicList.add(OEdynamicLabel);

                                        RelativeLayout childRel = new RelativeLayout(getApplicationContext());
                                        RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_END);
                                        layoutparams_3.setMargins(20, 10, 0, 0);
                                        edt = new EditText(getApplicationContext());
                                        edt.setLayoutParams(layoutparams_3);
                                        for (int da = 0; da < OEdynamicList.size(); da++) {
                                            edt.setHint(OEdynamicList.get(da));
                                        }
                                        edt.setId(12345);
                                        edt.setTextSize(13);
                                        edt.setTextColor(R.color.grey_500);
                                        edt.setBackgroundResource(R.drawable.textbox_bg);
                                        childRel.addView(edt);
                                        otherExpenseEdit.add(edt);
                                        if (l == additionArray.length() - 1) {
                                            userOtherExpense.put(sss, otherExpenseEdit);
                                        }
                                        View view = LinearOtherAllowance.getChildAt(editTextPositionss);
                                        OtherExpense = (LinearLayout) view.findViewById(R.id.lin_other_expense_dynamic);
                                        OtherExpense.addView(childRel);
                                    }
                                }
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
    }*/

    @SuppressLint("ResourceType")
    public void LocalConvenyanceApi(String Exp_Name, JSONArray additionArray, String flag) {
        try {
            List<CtrlsListModel.Ctrls> users = new ArrayList<>();
            for (int l = 0; l < additionArray.length(); l++) {
                JSONObject json_in = additionArray.getJSONObject(l);
                dynamicLabel = json_in.getString("Ad_Fld_Name");
                dynamicLabelList.add(dynamicLabel);
                RelativeLayout childRel = new RelativeLayout(getApplicationContext());
                RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_START);
                layoutparams_3.setMargins(12, 10, 12, 0);
                edt = new EditText(getApplicationContext());
                edt.setLayoutParams(layoutparams_3);
                edt.setHint(dynamicLabel);
                edt.setId(12345);
                edt.setTextSize(13);

                edt.setTextColor(getResources().getColor(R.color.black));
                childRel.addView(edt);

                CtrlsListModel.Ctrls Ctrl = new CtrlsListModel.Ctrls(dynamicLabel, edt);
                users.add(Ctrl);

                View view = linlocalCon.getChildAt(editTextPositionss);
                Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);
                Dynamicallowance.addView(childRel);
            }
            CtrlsListModel ULCItem = new CtrlsListModel(users, flag);
            uLCItems.put(Exp_Name, ULCItem);
            //usersByCountry.put(Exp_Name, users);
        } catch (Exception e) {

        }
    }

    @SuppressLint("ResourceType")
    public void OtherExpenseApi(String Exp_Name, JSONArray additionArray, String flag) {
        try {
            List<CtrlsListModel.Ctrls> otherExpenseEdit = new ArrayList<>();
            for (int l = 0; l < additionArray.length(); l++) {
                JSONObject json_in = additionArray.getJSONObject(l);
                OEdynamicLabel = json_in.getString("Ad_Fld_Name");
                OEdynamicList.add(OEdynamicLabel);

                RelativeLayout childRel = new RelativeLayout(getApplicationContext());
                RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_END);
                layoutparams_3.setMargins(0, 10, 0, 10);
                edt = new EditText(getApplicationContext());
                edt.setLayoutParams(layoutparams_3);

                edt.setHint(OEdynamicLabel);
                /*int sizeInDp=15;
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
                edt.setPadding(dpAsPixels,dpAsPixels,dpAsPixels,dpAsPixels);*/
                edt.setId(12345);
                edt.setTextSize(13);
                edt.setTextColor(getResources().getColor(R.color.black));
                //edt.setBackgroundResource(R.drawable.item_border);
                childRel.addView(edt);

                CtrlsListModel.Ctrls Ctrl = new CtrlsListModel.Ctrls(OEdynamicLabel, edt);
                otherExpenseEdit.add(Ctrl);

                View view = LinearOtherAllowance.getChildAt(editTextPositionss);
                OtherExpense = (LinearLayout) view.findViewById(R.id.lin_other_expense_dynamic);
                OtherExpense.addView(childRel);
            }

            CtrlsListModel ULCItem = new CtrlsListModel(otherExpenseEdit, flag);
            uOEItems.put(Exp_Name, ULCItem);
        } catch (Exception e) {
        }
    }

    /*Imageview */
    private void sendImageToServer(String count, String HeadTravel, String Mode, String from, String To, MultipartBody.Part imgg) {
        mCommon_class.ProgressdialogShow(1, "");

        long nano_startTime = System.nanoTime();
        Log.e("headTravel:multi:", HeadTravel);
        ImageUKey = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + nano_startTime;

        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> mCall = apiInterface.taImage(ImageUKey, count, HeadTravel, Mode, DateTime,
                mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), from, To, imgg);

        Log.e("SEND_IMAGE_SERVER", mCall.request().toString());

        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        mCommon_class.ProgressdialogShow(0, "");


                        String jsonData = null;
                        jsonData = response.body().string();
                        JSONObject js = new JSONObject(jsonData);

                        if (js.getBoolean("success")) {
                            mCommon_class.showMsg(TAClaimActivity.this, "File uploading successful ");

                            switch (HeadTravel) {
                                case "LOD":
                                    if (txtLodgUKey.getText().toString().equals("")) {
                                        txtLodgUKey.setText(lodEditcnt);
                                    }
                                    lodUKey = txtLodgUKey.getText().toString();

                                    break;
                                case "OE":
                                    if (oeTxtUKeys.getText().toString().equals("")) {
                                        oeTxtUKeys.setText(oeEditCnt);
                                    }

                                    OeUKey = oeTxtUKeys.getText().toString();
                                    break;
                                case "LC":
                                    if (lcTxtUKeys.getText().toString().equals("")) {
                                        lcTxtUKeys.setText(lcEditcnt);
                                    }
                                    LcUKey = lcTxtUKeys.getText().toString();

                                    break;
                                case "TL":
                                    if (tvTxtUKeys.getText().toString().equals("")) {
                                        tvTxtUKeys.setText(tvEditcnt);
                                    }
                                    TlUKey = tvTxtUKeys.getText().toString();

                                    break;
                            }
                        } else {
                            mCommon_class.showMsg(TAClaimActivity.this, "Failed.Try Again...");
                        }
                    } else {
                        mCommon_class.ProgressdialogShow(0, "");
                        mCommon_class.showMsg(TAClaimActivity.this, "Failed.Try Again...");

                    }

                } catch (Exception e) {
                    mCommon_class.ProgressdialogShow(0, "");
                    mCommon_class.showMsg(TAClaimActivity.this, "Failed.Try Again...");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mCommon_class.ProgressdialogShow(0, "");
                mCommon_class.showMsg(TAClaimActivity.this, "Failed.Try Again...");

                Log.e("SEND_IMAGE_Response", "ERROR");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshFare();

    }
    public void RefreshFare(){
    try {

        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

        JSONObject jj = new JSONObject();
        try {
            jj.put("sfCode", mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jj.put("divisionCode", mShared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
            jj.put("Selectdate", DateTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("Json_date_fomrat", jj.toString());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiInterface.getTAdateDetails(jj.toString());
        String finalChoosedDate = DateTime;
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                fuelAmt = 0.0;
                JsonObject jsonObjects = response.body();

                Log.v("JSON_TRAVEL_DETAILS", jsonObjects.toString());

                jsonFuelAllowance = jsonObjects.getAsJsonArray("FuelAllowance");



                Log.v("jsonFuelAllowance", jsonFuelAllowance.toString());

                if (jsonFuelAllowance != null || jsonFuelAllowance.size() != 0) {
                    Log.v("jsonFuelAllowance_IN", jsonFuelAllowance.toString());
                    fuelListAdapter = new FuelListAdapter(getApplicationContext(), jsonFuelAllowance, TWMax_Km, FWMax_Km);
                    mFuelRecycler.setAdapter(fuelListAdapter);
                    JsonObject jsFuel;

                    for (int jf = 0; jf < jsonFuelAllowance.size(); jf++) {
                        jsFuel = jsonFuelAllowance.get(jf).getAsJsonObject();



                        if (!jsFuel.get("End_Km").getAsString().equalsIgnoreCase("")) {
                            Integer start = Integer.valueOf(Common_Class.isNullOrEmpty(jsFuel.get("Start_Km").getAsString()) ? "0" : jsFuel.get("Start_Km").getAsString());
                            Integer end = Integer.valueOf(Common_Class.isNullOrEmpty(jsFuel.get("End_Km").getAsString()) ? "0" : jsFuel.get("End_Km").getAsString());
                            Shared_Common_Pref.MaxKm = jsFuel.get("Maxkm").getAsInt();
                            if (end != 0) {
                                String total = String.valueOf(end - start);
                                Integer Total = Integer.valueOf(total);
                                if (jsFuel.get("MOT_Name").getAsString().equals("Two Wheeler") && Total >= TWMax_Km)
                                    Total = TWMax_Km;
                                if (jsFuel.get("MOT_Name").getAsString().equals("Four Wheeler") && Total >= FWMax_Km)
                                    Total = FWMax_Km;


                                Integer Personal = Integer.valueOf(Common_Class.isNullOrEmpty(jsFuel.get("Personal_Km").getAsString()) ? "0" : jsFuel.get("Personal_Km").getAsString());
                                String TotalPersonal = String.valueOf(Total - Personal);
                                Double q = Double.valueOf(TotalPersonal);
                                Double z = Double.valueOf(Common_Class.isNullOrEmpty(jsFuel.get("FuelAmt").getAsString()) ? "0" : jsFuel.get("FuelAmt").getAsString());
                                String qz = String.valueOf(q * z);
                                Log.v("TA_FUEL_TOTAL", qz);
                                fuelAmt = fuelAmt + (q * z);
                                fuelAmount.setText("₹" + fuelAmt);

                                TextTotalAmount.setText("₹" + new DecimalFormat("##0.00").format(fuelAmt));
                            }
                        }
                    }
                }
                tofuel = fuelAmt;
                calOverAllTotal(localCov, otherExp, tTotAmt);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

        } catch (Exception exception) {
        }

    }

    public void MaxMinDateTo(String strMinDate) {
        Log.e("MAX_DATE_TWO", " " + strMinDate);

        String[] separated1 = strMinDate.split("-");
        separated1[0] = separated1[0].trim();
        separated1[1] = separated1[1].trim();
        separated1[2] = separated1[2].trim();

        tominYear = separated1[0];
        tominMonth = separated1[1];
        tominDay = separated1[2];

    }

    public void difference(String str) {

        stayEgTotal = 1 * ldgEliAmt;

        txtMyEligi.setText("₹" + new DecimalFormat("##0.00").format(stayEgTotal));
        ldgWOBBal.setText("₹" + new DecimalFormat("##0.00").format(stayEgTotal));

        SumOFLodging(0);

    }

    public void scroll() {

        mLc.post(new Runnable() {
            @Override
            public void run() {
                mLc.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


}