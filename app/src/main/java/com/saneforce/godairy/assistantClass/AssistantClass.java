package com.saneforce.godairy.assistantClass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.AlertDialogClickListener;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.DatePickerResult;
import com.saneforce.godairy.Interface.DropdownSelectListener;
import com.saneforce.godairy.Interface.LocationResponse;
import com.saneforce.godairy.R;
import com.saneforce.godairy.universal.UniversalDropDownAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssistantClass extends AppCompatActivity {
    Context context;
    SharedPreferences adminInfo;
    ProgressDialog progressDialog;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    boolean isLocationFound = false;

    public AssistantClass(Context context) {
        this.context = context;
        this.adminInfo = context.getSharedPreferences("user_info", MODE_PRIVATE);
        this.progressDialog = new ProgressDialog(context);
    }

    public static void clearAppData(Context context) {
        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear " + context.getPackageName());
        } catch (Exception ignored) {
        }
    }

    public void saveToLocal(String key, String value) {
        adminInfo.edit().putString(key, value).apply();
    }

    public void saveToLocal(String key, int value) {
        adminInfo.edit().putInt(key, value).apply();
    }

    public void saveToLocal(String key, boolean value) {
        adminInfo.edit().putBoolean(key, value).apply();
    }

    public String getStringFromLocal(String key) {
        return adminInfo.getString(key, "");
    }

    public int getIntFromLocal(String key) {
        return adminInfo.getInt(key, -1);
    }

    public boolean getBooleanFromLocal(String key) {
        return adminInfo.getBoolean(key, false);
    }

    public void makeApiCall(Map<String, String> params, String data, APIResult result) {
        if (!isConnected()) {
            dismissProgressDialog();
            showAlertDialog("", "Internet connection required...", false, "Turn on", "", new AlertDialogClickListener() {
                @Override
                public void onPositiveButtonClick(DialogInterface dialog) {
                    Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    dialog.dismiss();
                }

                @Override
                public void onNegativeButtonClick(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            return;
        }
        SharedPreferences UserDetails = ((Activity) context).getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        params.put("sfc", UserDetails.getString("Sfcode", ""));
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getUniversalData(params, data);
        log("Request: " + call.request());
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try (ResponseBody responseBody = response.body()) {
                        if (responseBody != null) {
                            JSONObject object = new JSONObject(responseBody.string());
                            log("Response: " + object);
                            if (object.optBoolean("success")) {
                                result.onSuccess(object);
                            } else {
                                result.onFailure(object.optString("msg"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        result.onFailure(e.getLocalizedMessage());
                    }
                } else {
                    result.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                result.onFailure(t.getLocalizedMessage());
            }
        });
    }

    public void clearLocalData() {
        adminInfo.edit().clear().apply();
    }

    @SuppressLint("SimpleDateFormat")
    public String getTime() {
        return new SimpleDateFormat("yyMMddHHmmssSSS").format(Calendar.getInstance().getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public String getTime(String yyMMddHHmmssSSSaa) {
        return new SimpleDateFormat(yyMMddHHmmssSSSaa).format(Calendar.getInstance().getTime());
    }

    public void copyToClipboard(String data) {
        android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(android.content.ClipData.newPlainText("text", data));
    }

    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnectedOrConnecting());
    }

    public boolean isRooted() {
        String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/", "/su/bin/"};
        for (String x : places) {
            if (new File(x + "su").exists()) {
                return true;
            }
        }
        return false;
    }

    public void showAlertDialog(String title, String message, boolean cancelable, String positive, String negative, AlertDialogClickListener dialogClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!title.isEmpty()) {
            builder.setTitle(title);
        }
        if (!message.isEmpty()) {
            builder.setMessage(message);
        }
        builder.setCancelable(cancelable);
        if (!positive.isEmpty()) {
            builder.setPositiveButton(positive, (dialog, which) -> dialogClickListener.onPositiveButtonClick(dialog));
        }
        if (!negative.isEmpty()) {
            builder.setNegativeButton(negative, (dialog, which) -> dialogClickListener.onNegativeButtonClick(dialog));
        }
        try {
            builder.create().show();
        } catch (Exception ignored) {
        }
    }

    public void showAlertDialogWithFinish(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!message.isEmpty()) {
            builder.setMessage(message);
        }
        builder.setCancelable(false);
        builder.setPositiveButton("Close", (dialog, which) -> ((Activity) context).finish());
        try {
            builder.create().show();
        } catch (Exception ignored) {
        }
    }

    public void showAlertDialogWithDismiss(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!message.isEmpty()) {
            builder.setMessage(message);
        }
        builder.setCancelable(false);
        builder.setPositiveButton("Dismiss", (dialog, which) -> dialog.dismiss());
        try {
            builder.create().show();
        } catch (Exception ignored) {
        }
    }

    public void showProgressDialog(String msg, boolean isCancelable) {
        if (!isNullOrEmpty(msg)) {
            progressDialog.setMessage(msg);
        }
        progressDialog.setCancelable(isCancelable);
        try {
            progressDialog.show();
        } catch (Exception ignored) {
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public boolean isNullOrEmpty(String string) {
        if (string == null) {
            return true;
        } else return string.isEmpty();
    }

    public boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isLocationPermissionGranted() {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    public boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) (context.getSystemService(Context.LOCATION_SERVICE));
        if (locationManager != null) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        return false;
    }

    public boolean isNotificationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    public void requestPermission(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, 9366);
    }

    public void showNotification(String notificationChannelId, String notificationChannelName, String subject, String body) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(notificationChannelId, notificationChannelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationChannelId).setSmallIcon(R.drawable.godairy_logo).setContentTitle(subject).setContentText(body).setPriority(NotificationCompat.PRIORITY_HIGH).setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        notificationManager.notify(getRandomNumber(100, 1000), builder.build());
    }

    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public void showDatePickerDialog(long minDate, long maxDate, DatePickerResult result) {
        final Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DAY = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(context, (view, year, month, day) -> {
            String _YEAR = new DecimalFormat("0000").format(year);
            String _MONTH = new DecimalFormat("00").format(month + 1);
            String _DAY = new DecimalFormat("00").format(day);
            String date = _DAY + "/" + _MONTH + "/" + _YEAR;
            String dateForDB = _YEAR + "/" + _MONTH + "/" + _DAY;
            result.onPick(date, dateForDB);
        }, YEAR, MONTH, DAY);
        if (minDate != 0) {
            dialog.getDatePicker().setMinDate(minDate);
        }
        if (maxDate != 0) {
            dialog.getDatePicker().setMaxDate(maxDate);
        }
        dialog.show();
    }

    @SuppressLint("MissingPermission")
    public void getLocation(LocationResponse result) {
        if (!isLocationPermissionGranted()) {
            if (progressDialog != null && progressDialog.isShowing()) {
                dismissProgressDialog();
            }
            showAlertDialogWithDismiss("Please grant location permission...");
        } else if (!isGpsEnabled()) {
            if (progressDialog != null && progressDialog.isShowing()) {
                dismissProgressDialog();
            }
            showAlertDialogWithDismiss("Please turn on location...");
        } else {
            isLocationFound = false;
            LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(5000).setFastestInterval(2500);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            isLocationFound = true;
                            stopLocationUpdates();
                            if (location.isFromMockProvider()) {
                                dismissProgressDialog();
                                showAlertDialogWithDismiss("Please disable mock location provider...");
                            } else {
                                result.onSuccess(location.getLatitude(), location.getLongitude());
                            }
                            break;
                        }
                    }
                }
            };
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient((Activity) context);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            new Handler().postDelayed(() -> {
                if (!isLocationFound) {
                    result.onFailure();
                    stopLocationUpdates();
                }
            }, 30000);
        }
    }

    private void stopLocationUpdates() {
        if (fusedLocationProviderClient != null && locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            fusedLocationProviderClient = null;
            locationCallback = null;
        }
    }

    public String formatDateToDB(String ddMMyyyy) {
        String formattedDate = "";
        try {
            Date fromDATE = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(ddMMyyyy);
            if (fromDATE != null) {
                formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(fromDATE);
            }
        } catch (Exception ignored) {
        }
        return formattedDate;
    }

    public String formatDateToLocal(String yyyyMMdd) {
        String formattedDate = "";
        try {
            Date fromDATE = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(yyyyMMdd);
            if (fromDATE != null) {
                formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(fromDATE);
            }
        } catch (Exception ignored) {
        }
        return formattedDate;
    }

    public void log(String toString) {
        Log.e("checkMyLog", toString);
    }

    public void showDropdown(String dropdownTitle, JSONArray array, DropdownSelectListener listener) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv_and_filter, null, false);
        builder.setView(view);
        builder.setCancelable(false);
        android.app.AlertDialog dialog = builder.create();
        TextView title = view.findViewById(R.id.title);
        title.setText(dropdownTitle);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        UniversalDropDownAdapter adapter = new UniversalDropDownAdapter(context, array);
        TextView close = view.findViewById(R.id.close);
        close.setOnClickListener(v1 -> dialog.dismiss());
        EditText eT_Filter = view.findViewById(R.id.eT_Filter);
        eT_Filter.setImeOptions(EditorInfo.IME_ACTION_DONE);
        eT_Filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        });
        adapter.setOnItemClick((position, arrayList) -> {
            listener.onSelect(arrayList.optJSONObject(position));
            dialog.dismiss();
        });
        recyclerView.setAdapter(adapter);
        dialog.show();
    }
}
