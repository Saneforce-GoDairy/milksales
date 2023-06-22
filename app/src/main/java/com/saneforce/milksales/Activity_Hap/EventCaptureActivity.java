package com.saneforce.milksales.Activity_Hap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.AlertDialogBox;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.On_ItemCLick_Listner;
import com.saneforce.milksales.Model_Class.EventCapture;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.EventCaptureAdapter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventCaptureActivity extends AppCompatActivity {


    Button TakeEventPicture;
    TextView txtRetailerName, txtRoute, txtDistributorName;
    int count = 0,countInt = 0;
    ArrayList<EventCapture> eventCapture;
    RecyclerView mEventCapture;
    EventCaptureAdapter mEventCaptureAdapter;
    ApiInterface apiInterface;
    EventCapture taskOne = new EventCapture();
    Shared_Common_Pref mShaeShared_common_pref;
    SharedPreferences sp,sharedPreferences;
    EditText editextTitle, editTextDescrption;
    List<EventCapture> taskList;
    int eventDbCount = 0;
    EventCapture task;
    String RetailerChannel = "", Retailerclass = "", OrderAmount = "", LastVisited = "", Remarks = "", textMobile = "",
            PhoneNumber = "", selectOrder = "", RetailerNames = "",eventListStr, EventFileName,keyEk = "EK", KeyDate,
            keyCodeValue,RoomDataBase = "",RetailerName, RouteName, DistributorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_capute);
        initialize();
    }

    private void initialize() {


        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        getTasks();
        sp = getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        mShaeShared_common_pref = new Shared_Common_Pref(this);
        DistributorName = mShaeShared_common_pref.getvalue("distributor_name");
        RouteName = mShaeShared_common_pref.getvalue("route_name");
        RetailerName = mShaeShared_common_pref.getvalue("Retailer_name");

        selectOrder = mShaeShared_common_pref.getvalue("Phone_order_type");
        Log.e("STRING_VALUE", selectOrder);

        eventCapture = new ArrayList<>();
        TakeEventPicture = findViewById(R.id.btn_take_photo);
        txtRetailerName = findViewById(R.id.txt_reatiler_name);
        txtRoute = findViewById(R.id.txt_route);
        txtDistributorName = findViewById(R.id.txt_distributor_name);

        txtRetailerName.setText(RetailerName);

        txtRoute.setText(RouteName);
        txtDistributorName.setText(DistributorName);

        editextTitle = findViewById(R.id.editTextDesc);
        editTextDescrption = findViewById(R.id.editTextFinishBy);

        Log.e("NEW_COUNT_VALULE", "Heeeee");

        mEventCapture = findViewById(R.id.event_capture_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEventCapture.setLayoutManager(layoutManager);
        mEventCapture.setNestedScrollingEnabled(false);

        RoomDataBase = mShaeShared_common_pref.getvalue("Event_Capture");
        // intent.putExtra("count",eventDb);
        Intent intnet = getIntent();
        eventDbCount = intnet.getIntExtra("count", 0);
        RetailerChannel = intnet.getStringExtra("RetailerChannel");
        Retailerclass = intnet.getStringExtra("Retailerclass");
        OrderAmount = intnet.getStringExtra("OrderAmount");
        LastVisited = intnet.getStringExtra("LastVisited");
        Remarks = intnet.getStringExtra("Remarks");
        textMobile = intnet.getStringExtra("textMobile");
        PhoneNumber = intnet.getStringExtra("PhoneNumber");
        RetailerNames = intnet.getStringExtra("RetailerName");
        // selectOrder = intnet.getStringExtra("selectOrder");



        if (eventDbCount == 1) {
            delete();
        }


        TakeEventPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Calendar calobjw = Calendar.getInstance();
                KeyDate = mShaeShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code);
                keyCodeValue = keyEk + KeyDate + dfw.format(calobjw.getTime()).hashCode();

                countInt = sharedPreferences.getInt("age", 0);
                if (countInt != 0) {
                    Log.e("Count_Value", String.valueOf(countInt));
                } else {
                    Log.e("Count_Value", String.valueOf(countInt));
                }

                taskOne.addToQuantity();
                count = taskOne.getmQuantity() + countInt;
                EventFileName = "EventCapture" + keyCodeValue + ".jpeg";
                Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(getExternalCacheDir().getPath(), EventFileName);
                Uri uri = FileProvider.getUriForFile(EventCaptureActivity.this, getApplicationContext().getPackageName() + ".provider", file);
                m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(m_intent, 2);

            }
        });

        task = (EventCapture) getIntent().getSerializableExtra("task");

        loadTask(task);

    }

    private void loadTask(EventCapture task) {
        editextTitle.setText("task.getDesc()");
        editTextDescrption.setText("task.getFinishBy()");
    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<EventCapture>> {

            @Override
            protected List<EventCapture> doInBackground(Void... voids) {
                taskList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().getAll();


                return taskList;
            }

            @Override
            protected void onPostExecute(List<EventCapture> tasks) {
                super.onPostExecute(tasks);
                mEventCaptureAdapter = new EventCaptureAdapter(tasks, EventCaptureActivity.this, new On_ItemCLick_Listner() {
                    @Override
                    public void onIntentClick(int Name) {
                        Log.e("Delete_Position", String.valueOf(tasks.get(Name)));

                        AlertDialogBox.showDialog(EventCaptureActivity.this, "", "Are you surely want to delete?", "Yes", "No", false, new AlertBox() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                deleteTask(tasks.get(Name));
                            }

                            @Override
                            public void NegativeMethod(DialogInterface dialog, int id) {
                            }
                        });
                    }

                });
                mEventCapture.setAdapter(mEventCaptureAdapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {

                    EventFileName = "EventCapture" + keyCodeValue + ".jpeg";
                    File file = new File(getExternalCacheDir().getPath(), EventFileName);
                    Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                    eventListStr = String.valueOf(uri);
                    Log.e("EVENT_LIST_STR", String.valueOf(EventFileName));
                    Log.e("EVENT_LIST_STR_SHared", String.valueOf(keyCodeValue));

                    SharedPreferences.Editor myEdit
                            = sharedPreferences.edit();
                    myEdit.putInt("age", count);
                    myEdit.commit();

                    saveTask(eventListStr);
                    getMulipart(eventListStr, 0);
                }
                break;
        }
    }


    private void saveTask(String sTask) {

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                taskOne.setTask(sTask);
                taskOne.setDesc("");
                taskOne.setFinishBy("");

                //adding to database

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().insert(taskOne);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }


    /*Call Api Image*/
    public void getMulipart(String path, int x) {
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field("MR0417");
        CallApiImage(values, imgg, x);
    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
        Log.v("full_profile", path);
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
        Log.v("full_profile", yy + "");
        return yy;
    }

    public HashMap<String, RequestBody> field(String val) {
        HashMap<String, RequestBody> xx = new HashMap<String, RequestBody>();
        xx.put("data", createFromString(val));

        return xx;

    }

    private RequestBody createFromString(String txt) {
        return RequestBody.create(MultipartBody.FORM, txt);
    }


    public void CallApiImage(HashMap<String, RequestBody> values, MultipartBody.Part imgg, final int x) {
        Call<ResponseBody> Callto;

        Callto = apiInterface.uploadProcPic(values, imgg);

        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.v("print_upload_file", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (response.isSuccessful()) {
                        Log.v("print_upload_file_true", "ggg" + response);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("print_failure", "ggg" + t.getMessage());
            }
        });
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(EventCaptureActivity.this, SecondaryOrderActivity.class);
                    intent.putExtra("RetailerChannel", RetailerChannel);
                    intent.putExtra("Retailerclass", Retailerclass);
                    intent.putExtra("OrderAmount", OrderAmount);
                    intent.putExtra("LastVisited", LastVisited);
                    intent.putExtra("Remarks", Remarks);
                    intent.putExtra("textMobile", textMobile);
                    intent.putExtra("PhoneNumber", PhoneNumber);
                    intent.putExtra("RetailerName", RetailerName);
                    intent.putExtra("selectOrder", selectOrder);
                    intent.putExtra("EventCapCount", "123");
                    startActivity(intent);
                }
            });


    @Override
    public void onBackPressed() {

    }


    private void delete() {

        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().clearAllTables();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        DeleteTask st = new DeleteTask();
        st.execute();
    }


    private void deleteTask(EventCapture task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                recreate();
                Toast.makeText(EventCaptureActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getTasks();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getTasks();
    }
}

