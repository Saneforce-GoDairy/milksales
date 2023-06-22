package com.saneforce.milksales.Activity_Hap;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.AlertDialogBox;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.GateEntryQREvents;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.HAPApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRCodeScanner extends AppCompatActivity {
    SurfaceView surfaceView;
    TextView txtBarcodeValue,txScanMod;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    static GateEntryQREvents mGateEntryQREvents;
    Button btnAction;
    String[] arrSplit;
    String latlon = "", NameValue = "", intentData = "";
    Boolean readFlag = false;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code_scanner);
        NameValue = String.valueOf(getIntent().getSerializableExtra("Name"));
        initViews();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.v("Loaction", String.valueOf(location.getLatitude()));
                            Log.v("Loaction", String.valueOf(location.getLongitude()));
                            latlon = location.getLatitude() + "," + location.getLongitude();
                        }
                    }
                });

        initialiseDetectorsAndSources();

    }

    private void initViews() {

        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        txScanMod = findViewById(R.id.txtBarHead);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        txScanMod.setVisibility(View.VISIBLE);
        if(NameValue.equalsIgnoreCase("GateIn"))
            txScanMod.setText("Gate - IN");
        else if(NameValue.equalsIgnoreCase("GateOut"))
            txScanMod.setText("Gate - OUT");
        else
            txScanMod.setVisibility(View.GONE);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }

    private void initialiseDetectorsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(QRCodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(QRCodeScanner.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0 && readFlag == false) {
                    surfaceView.post(new Runnable() {

                        @Override
                        public void run() {
                            if (readFlag == false) {
                                intentData = barcodes.valueAt(0).displayValue.replace("|", ",");
                                cameraSource.release();
                                readFlag = true;
                                if (getIntent().getStringExtra("scan") != null) {
                                    Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(QRCodeScanner.this);
                                    shared_common_pref.save(Constants.SCAN_DATA, intentData.toString());
                                   // Toast.makeText(getApplicationContext(), intentData.toString(), Toast.LENGTH_SHORT).show();

                                    finish();
                                } else if (!intentData.equals("")) {
                                    arrSplit = intentData.split(",");
                                    if (String.valueOf(arrSplit.length).equals("5")) {
                                        if ((NameValue.equalsIgnoreCase("gatein") && arrSplit[3].equalsIgnoreCase("IN")) || (NameValue.equalsIgnoreCase("gateout") && arrSplit[3].equalsIgnoreCase("Out"))) {

                                            Log.d("BarCodeDetact", "Called 2 " + intentData);
                                            GateIn(NameValue, arrSplit, 1);
                                            // Toast.makeText(QRCodeScanner.this, "Code is successfull", Toast.LENGTH_SHORT).show();
                                        } else {
                                            AlertDialogBox.showDialog(QRCodeScanner.this, HAPApp.Title, String.valueOf(Html.fromHtml("Provide a Valid QR Code<br>Scan Detail<br>Mode:"+arrSplit[3]+"<br>Data : <br> "+intentData)), "OK", "", false, new AlertBox() {
                                                @Override
                                                public void PositiveMethod(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                    QRCodeScanner.this.finish();
                                                }

                                                @Override
                                                public void NegativeMethod(DialogInterface dialog, int id) {

                                                }
                                            });
                                        }
                                    } else {
                                        AlertDialogBox.showDialog(QRCodeScanner.this, HAPApp.Title, String.valueOf(Html.fromHtml("Provide a Valid QR Code<br>Need Update QR Data<br>Scan Detail<br>Mode:"+arrSplit[3]+"<br>Data : <br> "+intentData)), "OK", "", false, new AlertBox() {
                                            @Override
                                            public void PositiveMethod(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                QRCodeScanner.this.finish();
                                            }

                                            @Override
                                            public void NegativeMethod(DialogInterface dialog, int id) {

                                            }
                                        });

                                    }
                                }
                            }
                        }
                    });


                }
            }
        });
    }


    private void GateIn(String Name, String[] arrSplit, int flag) {
        if (!arrSplit[0].equals("") || !arrSplit[1].equals("") || !arrSplit[2].equals("") || !arrSplit[3].equals("") || !arrSplit[4].equals("")) {
            Calendar calendar;
            SimpleDateFormat dateFormat, dateTime, time;
            String date, dateTi, ti;

            calendar = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            time = new SimpleDateFormat("hh:mm:ss");
            date = dateFormat.format(calendar.getTime());
            dateTi = dateTime.format(calendar.getTime());
            ti = time.format(calendar.getTime());
            Log.v("DATE_ONLY", date);
            Log.v("DATE_ONLY", dateTi);
            Log.v("DATE_ONLY", ti);

            //GateIn,GateOut
            Map<String, String> QueryString = new HashMap<>();
            QueryString.put("axn", "dcr/save");
            QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
            QueryString.put("State_Code", Shared_Common_Pref.Div_Code);
            QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
            //  QueryString.put("duty_id", duty_id);

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            JSONObject sp = new JSONObject();
            try {
                sp.put("HQLoc", arrSplit[0]);
                sp.put("HQLocID", arrSplit[1]);
                sp.put("Location", arrSplit[2]);
                sp.put("MajourType", arrSplit[4]);
                sp.put("latLng", latlon);
                sp.put("mode", arrSplit[3]);
                sp.put("time", date);
                sp.put("eDate", dateTi);
                sp.put("eTime", ti);
                jsonObject.put(Name, sp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());
            mCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    // locationList=response.body();
                    Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                    try {
                        //JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        JsonObject jsonObject = response.body();
                        String Msg = jsonObject.get("Msg").getAsString();
                        if (Msg.equalsIgnoreCase(""))
                            Msg = "Submitted Successfully";
                        AlertDialogBox.showDialog(QRCodeScanner.this, HAPApp.Title, Msg, "OK", "", false, new AlertBox() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                mGateEntryQREvents.RefreshGateEntrys();
                                QRCodeScanner.this.finish();
                            }

                            @Override
                            public void NegativeMethod(DialogInterface dialog, int id) {

                            }
                        });

                   /* if (flag == 1) {
                        // common_class.ProgressdialogShow(2, "");
                        Toast.makeText(getApplicationContext(), "Holiday  Approved Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        //  common_class.ProgressdialogShow(2, "");
                        Toast.makeText(getApplicationContext(), "Holiday Rejected  Successfully", Toast.LENGTH_SHORT).show();

                    }*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    //  common_class.ProgressdialogShow(2, "");
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();

        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    public static void bindEvents(GateEntryQREvents gateEntryQREvents) {
        mGateEntryQREvents = gateEntryQREvents;
    }
}