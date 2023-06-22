package com.saneforce.milksales.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saneforce.milksales.Activity.Util.ImageFilePath;
import com.saneforce.milksales.Activity.Util.ModelDynamicView;
import com.saneforce.milksales.Activity.Util.SelectionModel;
import com.saneforce.milksales.Activity.Util.UpdateUi;
import com.saneforce.milksales.Activity_Hap.SFA_Activity;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.AdapterForDynamicView;
import com.saneforce.milksales.adapters.AdapterForSelectionList;
import com.saneforce.milksales.adapters.FilterDemoAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewActivity extends AppCompatActivity/* implements/* AdapterForSelectionList.OnSelectItemClick */ {

    String filePathing = "", btnShow = "V", MyPREFERENCES = "MyPrefs", SF_code = "", fab_value = "0", frm_id, mCreationId = "";
    public static String key = "", header = "";
    SimpleDateFormat sdf, sdf_or;
    boolean isEmpty = false;
    DatePickerDialog fromDatePickerDialog;
    TimePickerDialog timePickerDialog;
    ArrayList<ModelDynamicView> array_view = new ArrayList<>();
    ArrayList<SelectionModel> filterList = new ArrayList<>();
    ArrayList<SelectionModel> selectedFilter = new ArrayList<>();
    ApiInterface apiService;
    AdapterForDynamicView adp_view;
    FilterDemoAdapter adpt;
    ProgressDialog progressDialog = null;
    ListView list;
    int pos_upload_file = 0, CAMERA_REQUEST = 12, value = 0, arrayViewPos;
    Uri outputFileUri;
    FloatingActionButton fab;
    RecyclerView relist_view;
    Bundle extra;
    SharedPreferences UserDetails, share, shareKey;
    ImageView iv_dwnldmaster_back;
    TextView tool_header;
    Button btn_save;
    Shared_Common_Pref sharedCommonPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        sharedCommonPref = new Shared_Common_Pref(this);
        extra = getIntent().getExtras();
        frm_id = extra.getString("frmid");
        value = Integer.parseInt(extra.getString("btn_need"));
        share = getSharedPreferences("existing", 0);
        shareKey = getSharedPreferences("key", 0);
        progressDialog = createProgressDialog(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        fab = findViewById(R.id.fab);
        list = findViewById(R.id.list_view);
        relist_view = findViewById(R.id.relist_view);
        iv_dwnldmaster_back = findViewById(R.id.iv_dwnldmaster_back);
        tool_header = findViewById(R.id.tool_header);
        btn_save = findViewById(R.id.btn_save);
        Log.v("printing_frm_id", frm_id + "");
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf_or = new SimpleDateFormat("dd-MM-yyyy");
        if (value > 0)
            fab.setVisibility(View.VISIBLE);
        else
            fab.setVisibility(View.GONE);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        iv_dwnldmaster_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = share.edit();
                edit.putString("fab", "0");
                edit.commit();
                try {
                    JSONArray jjj = new JSONArray(shareKey.getString("keys", ""));
                    if (jjj.length() == 0) {
                        edit = shareKey.edit();
                        JSONObject jkey1 = new JSONObject();
                        JSONArray jar = new JSONArray();
                        JSONObject js = new JSONObject();
                        js.put("PK_ID", ViewActivity.key);
                        js.put("FK_ID", "");
                        js.put("SF", SF_code);
                        js.put("date", Common_Class.GetDate());
                        jkey1.put(ViewActivity.header, js);
                        jar.put(jkey1);
                        Log.v("printing_shareing23", jar.toString());
                        edit.putString("keys", jar.toString());
                        edit.putString("pk", ViewActivity.key);
                        edit.commit();
                    } else {
                        edit = shareKey.edit();
                        JSONObject jkey1 = new JSONObject();
                        JSONObject js = new JSONObject();
                        js.put("PK_ID", ViewActivity.key);
                        js.put("FK_ID", shareKey.getString("pk", ""));
                        js.put("SF", SF_code);
                        js.put("date", Common_Class.GetDate());
                        jkey1.put(ViewActivity.header, js);
                        jjj.put(jkey1);
                        Log.v("printing_shareing", jjj.toString());
                        edit.putString("keys", jjj.toString());
                        edit.putString("pk", ViewActivity.key);
                        edit.commit();
                    }
                } catch (Exception e) {
                }
                Intent i = new Intent(ViewActivity.this, ViewActivity.class);
                i.putExtra("frmid", String.valueOf(value));
                i.putExtra("frmname", "New Farmer");
                i.putExtra("btn_need", "0");
                startActivity(i);
            }
        });
        Log.v("printing_share_key", shareKey.getString("keys", "") + " hello ");
        callDynamicViewList();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationOfField()) {
                    SharedPreferences.Editor edit = shareKey.edit();
                    try {
                        JSONArray jjj = new JSONArray(shareKey.getString("keys", ""));
                        if (jjj.length() == 0) {
                            JSONObject jkey1 = new JSONObject();
                            JSONArray jar = new JSONArray();
                            JSONObject js = new JSONObject();
                            js.put("PK_ID", ViewActivity.key);
                            js.put("FK_ID", "");
                            js.put("SF", SF_code);
                            js.put("date", Common_Class.GetDate());
                            JSONArray jAA = new JSONArray();
                            for (int i = 0; i < array_view.size(); i++) {
                                JSONObject jk = new JSONObject();
                                if (!array_view.get(i).getViewid().equalsIgnoreCase("19") && !array_view.get(i).getViewid().equalsIgnoreCase("22")) {
                                    String col = array_view.get(i).getField_Col();
                                    jk.put("id", array_view.get(i).getViewid());
                                    if (array_view.get(i).getViewid().equalsIgnoreCase("8")) {
                                        Date d = (Date) sdf_or.parse(array_view.get(i).getValue());
                                        jk.put("value", sdf.format(d));
                                    } else if (array_view.get(i).getViewid().equalsIgnoreCase("15") || array_view.get(i).getViewid().equalsIgnoreCase("16")
                                            || array_view.get(i).getViewid().equalsIgnoreCase("17")) {
                                        String pic = "";
                                        String[] picVal = array_view.get(i).getValue().split(",");
                                        for (int k = 0; k < picVal.length; k++) {
                                            Log.v("picVal_entry", picVal[k]);
                                            getMulipart(picVal[k], 0);
                                            pic = pic + picVal[k].substring(picVal[k].lastIndexOf("/"));
                                        }
                                        jk.put("value", pic);
                                    } else if (array_view.get(i).getViewid().equalsIgnoreCase("3") || array_view.get(i).getViewid().equalsIgnoreCase("18")) {
                                        if (TextUtils.isEmpty(array_view.get(i).getValue()))
                                            jk.put("value", "0");
                                        else
                                            jk.put("value", array_view.get(i).getValue());
                                    } else
                                        jk.put("value", array_view.get(i).getValue());
                                    jk.put("col", col);
                                    jAA.put(jk);
                                }
                            }
                            js.put("ctrl", jAA);
                            jkey1.put(ViewActivity.header, js);
                            jar.put(jkey1);
                            Log.v("printing_shareing23", jar.toString());
                            edit.putString("keys", jar.toString());
                            edit.putString("pk", ViewActivity.key);
                            edit.commit();
                        } else {
                            JSONObject jkey1 = new JSONObject();
                            JSONObject js = new JSONObject();
                            js.put("PK_ID", ViewActivity.key);
                            js.put("FK_ID", shareKey.getString("pk", ""));
                            js.put("SF", SF_code);
                            js.put("date", Common_Class.GetDate());
                            JSONArray jAA = new JSONArray();
                            for (int i = 0; i < array_view.size(); i++) {
                                JSONObject jk = new JSONObject();
                                if (!array_view.get(i).getViewid().equalsIgnoreCase("19") && !array_view.get(i).getViewid().equalsIgnoreCase("22")) {
                                    String col = array_view.get(i).getField_Col();
                                    jk.put("id", array_view.get(i).getViewid());
                                    if (array_view.get(i).getViewid().equalsIgnoreCase("8")) {
                                        Date d = (Date) sdf_or.parse(array_view.get(i).getValue());
                                        jk.put("value", sdf.format(d));
                                    } else if (array_view.get(i).getViewid().equalsIgnoreCase("15") || array_view.get(i).getViewid().equalsIgnoreCase("16")
                                            || array_view.get(i).getViewid().equalsIgnoreCase("17")) {
                                        String pic = "";
                                        String[] picVal = array_view.get(i).getValue().split(",");
                                        for (int k = 0; k < picVal.length; k++) {
                                            Log.v("picVal_entry", picVal[k]);
                                            getMulipart(picVal[k], 0);
                                            pic = pic + picVal[k].substring(picVal[k].lastIndexOf("/") + 1) + ",";
                                        }
                                        jk.put("value", pic);
                                    } else if (array_view.get(i).getViewid().equalsIgnoreCase("3") || array_view.get(i).getViewid().equalsIgnoreCase("18")) {
                                        if (TextUtils.isEmpty(array_view.get(i).getValue()))
                                            jk.put("value", "0");
                                        else
                                            jk.put("value", array_view.get(i).getValue());
                                    } else
                                        jk.put("value", array_view.get(i).getValue());
                                    jk.put("col", col);
                                    jk.put("SF", SF_code);
                                    jk.put("date", Common_Class.GetDate());
                                    jAA.put(jk);
                                }
                            }
                            js.put("ctrl", jAA);
                            jkey1.put(ViewActivity.header, js);
                            jjj.put(jkey1);
                            Log.v("printing_shareing", jjj.toString());
                            edit.putString("keys", jjj.toString());
                            edit.putString("pk", ViewActivity.key);
                            edit.commit();
                        }
                        saveDynamicList();

                    } catch (Exception e) {
                    }
                } else
                    Toast.makeText(ViewActivity.this, "Please fill the mandatory fields", Toast.LENGTH_SHORT).show();
            }
        });
//click function added for field label (spinner)
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (array_view.size() == 0) {
//
//                } else {
//                    if (array_view.get(i).getViewid().equalsIgnoreCase("4") || array_view.get(i).getViewid().equalsIgnoreCase("5")) {
//                        popupSpinner(0, array_view.get(i).getA_list(), i, array_view.get(i).getCreation_id());
//                    } else if (array_view.get(i).getViewid().equalsIgnoreCase("8")) {
//                        datePick(i, 8);
//                    } else if (array_view.get(i).getViewid().equalsIgnoreCase("6") || array_view.get(i).getViewid().equalsIgnoreCase("7")) {
//                        popupSpinner(1, array_view.get(i).getA_list(), i, array_view.get(i).getCreation_id());
//
//
//                    }
//                }
//
//            }
//        });

        AdapterForDynamicView.bindListernerForDateRange(new UpdateUi() {
            @Override
            public void update(int value, int pos) {
                switch (value) {
                    case 15:
                        pos_upload_file = pos;
                        uploadFile();
                        break;
                    case 16:
                        pos_upload_file = pos;
                        captureFile();
                        break;
                    case 17:
                        pos_upload_file = pos;
                        popupCapture();
                        break;
                    case 4:
                        popupSpinner(0, array_view.get(pos).getA_list(), pos, array_view.get(pos).getCreation_id());
                        break;
                    case 5:
                        popupSpinner(0, array_view.get(pos).getA_list(), pos, array_view.get(pos).getCreation_id());
                        break;
                    case 6:
                        popupSpinner(1, array_view.get(pos).getA_list(), pos, array_view.get(pos).getCreation_id());
                        break;
                    case 7:
                        popupSpinner(1, array_view.get(pos).getA_list(), pos, array_view.get(pos).getCreation_id());
                        break;
                    case 8:
                        datePick(pos, value);
                        break;
                    case 9:
                        datePick(pos, value);
                        break;
                    default:
                        timePicker(pos, value);
                        break;
                }


//                if (value == 15) {
//                    pos_upload_file = pos;
//                    uploadFile();
//                } else if (value == 16) {
//                    pos_upload_file = pos;
//                    captureFile();
//                } else if (value == 17) {
//                    pos_upload_file = pos;
//                    popupCapture();
//                } else if (value == 4) {
//                    popupSpinner(0, array_view.get(pos).getA_list(), pos, array_view.get(pos).getCreation_id());
//                } else if (value == 5) {
//                    popupSpinner(0, array_view.get(pos).getA_list(), pos, array_view.get(pos).getCreation_id());
//                } else if (value == 6) {
//                    popupSpinner(1, array_view.get(pos).getA_list(), pos, array_view.get(pos).getCreation_id());
//                } else if (value == 7) {
//                    popupSpinner(1, array_view.get(pos).getA_list(), pos, array_view.get(pos).getCreation_id());
//                } else if (value > 5 && value < 10) {
//                    datePick(pos, value);
//                } else
//                    timePicker(pos, value);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        filePathing = "";
        try {
            // When an Image is picked
            if (requestCode == 2 && resultCode == RESULT_OK
                    && null != data && null != data.getClipData()) {

                ClipData mClipData = data.getClipData();
                ModelDynamicView mm = array_view.get(pos_upload_file);
                if (!TextUtils.isEmpty(mm.getValue()))
                    filePathing = mm.getValue();
                Toast.makeText(ViewActivity.this, "You picked " +
                                (mClipData.getItemCount() > 1 ? mClipData.getItemCount() + "Images" :
                                        mClipData.getItemCount() + "Image"),
                        Toast.LENGTH_LONG).show();

                //pickedImageContainer.removeAllViews();

                int pickedImageCount;

                for (pickedImageCount = 0; pickedImageCount < mClipData.getItemCount();
                     pickedImageCount++) {
                    Log.v("picked_image_value", mClipData.getItemAt(pickedImageCount).getUri() + "");
                    ImageFilePath filepath = new ImageFilePath();
                    String fullPath = filepath.getPath(ViewActivity.this, mClipData.getItemAt(pickedImageCount).getUri());
                    Log.v("picked_image_value_path", fullPath + "");

                    filePathing = filePathing + fullPath + ",";
                }

                mm.setValue(filePathing);
                adp_view.notifyDataSetChanged();

            } else if (requestCode == 7 && resultCode == RESULT_OK) {
                if (resultCode == RESULT_OK) {
                    ModelDynamicView mm = array_view.get(pos_upload_file);
                    if (!TextUtils.isEmpty(mm.getValue()))
                        filePathing = mm.getValue();
                    Uri uri = data.getData();
                    ImageFilePath filepath = new ImageFilePath();
                    String fullPath = filepath.getPath(ViewActivity.this, uri);
                    Log.v("file_path_are", fullPath + "print");
                    String PathHolder = data.getData().getPath();
                    filePathing = filePathing + fullPath + ",";

                    if (fullPath == null)
                        Toast.makeText(ViewActivity.this, "This file format not supported", Toast.LENGTH_LONG).show();
                    else {
                        mm.setValue(filePathing);
                        adp_view.notifyDataSetChanged();
                    }
                }

            } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                ModelDynamicView mm = array_view.get(pos_upload_file);
                if (!TextUtils.isEmpty(mm.getValue()))
                    filePathing = mm.getValue();
                String finalPath = "/storage/emulated/0";
                String filePath = outputFileUri.getPath();
                filePath = filePath.substring(1);
                filePath = finalPath + filePath.substring(filePath.indexOf("/"));
                Log.v("printing__file_path", filePath);
                filePathing = filePathing + filePath + ",";
                if (filePath == null)
                    Toast.makeText(ViewActivity.this, "This file format not supported", Toast.LENGTH_LONG).show();
                else {
                    mm.setValue(filePathing);
                    adp_view.notifyDataSetChanged();
                }

            } else {
                Toast.makeText(this, "You haven't picked any Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: Something went wrong " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void selectMultiImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }

    public void uploadFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 7);
    }

    public void captureFile() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputFileUri = FileProvider.getUriForFile(ViewActivity.this, getApplicationContext().getPackageName() + ".provider", new File(getExternalCacheDir().getPath(), "pickImageResult" + System.currentTimeMillis() + ".jpeg"));
        Log.v("priniting_uri", outputFileUri.toString() + " output " + outputFileUri.getPath() + " raw_msg " + getExternalCacheDir().getPath());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    public void popupSpinner(int type, final ArrayList<SelectionModel> array_selection, final int pos, String creationId) {
        try {

            Dialog dialog = new Dialog(ViewActivity.this, R.style.AlertDialogCustom);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.popup_dynamic_view1);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            ListView popup_list = (ListView) dialog.findViewById(R.id.popup_list);
            TextView tv_todayplan_popup_head = (TextView) dialog.findViewById(R.id.tv_todayplan_popup_head);
            tv_todayplan_popup_head.setText(array_view.get(pos).getFieldname());
            Button iv_close_popup = (Button) dialog.findViewById(R.id.btnClose);
            Button ok = (Button) dialog.findViewById(R.id.ok);

            if (array_selection.contains(new SelectionModel(true))) {
                isEmpty = false;
            } else
                isEmpty = true;
            arrayViewPos = pos;
            mCreationId = creationId;

            AdapterForSelectionList adapt = new AdapterForSelectionList(ViewActivity.this, array_selection, type, new AdapterOnClick() {
                @Override
                public void onIntentClick(SelectionModel selectionModel) {

                    array_view.get(pos).setValue(selectionModel.getTxt());
                    if (filterList.size() != 0) {
                        for (int j = 0; j < filterList.size(); j++) {
                            if (filterList.get(j).getTxt().equals(creationId)) {
                                selectedFilter.add(new SelectionModel(filterList.get(j).getCode(), selectionModel.getTxt()));
                            }
                        }
                        Log.e("ListValues_fil", filterList.toString());
                        Log.e("ListValues_select", selectedFilter.toString());
                    }
                    adp_view.notifyDataSetChanged();

                    dialog.dismiss();
                    commonFun();
                }


            });
            popup_list.setAdapter(adapt);
            final SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
            search_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search_view.setIconified(false);
                    InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                    im.showSoftInput(search_view, 0);
                }
            });
            search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    Log.v("search_view_str", s);
                    adapt.getFilter().filter(s);
                    return false;
                }
            });

            iv_close_popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEmpty) {
                        if (array_selection.contains(new SelectionModel(true))) {
                            for (int i = 0; i < array_selection.size(); i++) {
                                SelectionModel m = array_selection.get(i);
                                m.setClick(false);
                            }
                        }
                    }
                    dialog.dismiss();
                    commonFun();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (array_selection.contains(new SelectionModel(true))) {
                        for (int i = 0; i < array_selection.size(); i++) {
                            SelectionModel m = array_selection.get(i);
                            if (m.isClick()) {
                                array_view.get(pos).setValue(m.getTxt());
                                i = array_selection.size();
                                if (filterList.size() != 0) {
                                    for (int j = 0; j < filterList.size(); j++) {
                                        if (filterList.get(j).getTxt().equals(creationId)) {
                                            selectedFilter.add(new SelectionModel(filterList.get(j).getCode(), m.getTxt()));
                                        }
                                    }
                                    Log.e("ListValues_fil", filterList.toString());
                                    Log.e("ListValues_select", selectedFilter.toString());
                                }
                                adp_view.notifyDataSetChanged();
                                break;
                            }
                        }

                    } else {
                        array_view.get(pos).setValue("");
                        adp_view.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                    commonFun();
                }
            });
        } catch (Exception e) {
            Log.v("ViewActivity:POP:", e.getMessage());
        }
    }


    public void datePick(final int pos, final int value) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(ViewActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ModelDynamicView mm = array_view.get(pos);
                int mnth = monthOfYear + 1;
                if (mm.getViewid().equalsIgnoreCase("9")) {
                    if (value == 8) {
                        if (TextUtils.isEmpty(mm.getTvalue()))
                            mm.setValue(dayOfMonth + "-" + mnth + "-" + year);
                        else {
                            String val = dayOfMonth + "-" + mnth + "-" + year;
                            if (dateDifference(val, mm.getTvalue()) < 0)
                                Toast.makeText(ViewActivity.this, "From date should be lesser", Toast.LENGTH_SHORT).show();
                            else
                                mm.setValue(dayOfMonth + "-" + mnth + "-" + year);
                        }
                    } else {
                        if (TextUtils.isEmpty(mm.getValue()))
                            Toast.makeText(ViewActivity.this, "Fill from date", Toast.LENGTH_SHORT).show();
                        else {
                            String val = dayOfMonth + "-" + mnth + "-" + year;
                            if (dateDifference(mm.getValue(), val) < 0)
                                Toast.makeText(ViewActivity.this, "To date should be greater", Toast.LENGTH_SHORT).show();
                            else
                                mm.setTvalue(dayOfMonth + "-" + mnth + "-" + year);
                        }


                    }
                } else
                    mm.setValue(dayOfMonth + "-" + mnth + "-" + year);

                adp_view.notifyDataSetChanged();
                commonFun();
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }

    public void timePicker(final int pos, final int value) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(ViewActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                ModelDynamicView mm = array_view.get(pos);
                if (mm.getViewid().equalsIgnoreCase("12")) {
                    if (value == 12) {
                        if (TextUtils.isEmpty(mm.getTvalue()))
                            mm.setValue(selectedHour + ":" + selectedMinute);
                        else {
                            int thr = spiltTime(mm.getTvalue());
                            int tmin = spiltMin(mm.getTvalue());
                            if (thr == selectedHour) {
                                if (selectedMinute < tmin) {
                                    mm.setValue(selectedHour + ":" + selectedMinute);
                                } else
                                    Toast.makeText(ViewActivity.this, "From time should be lesser", Toast.LENGTH_SHORT).show();
                            } else if (thr > selectedHour) {
                                mm.setValue(selectedHour + ":" + selectedMinute);
                            } else
                                Toast.makeText(ViewActivity.this, "From time should be lesser", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        if (TextUtils.isEmpty(mm.getValue()))
                            Toast.makeText(ViewActivity.this, "Fill the from  time", Toast.LENGTH_SHORT).show();
                        else {
                            int fhr = spiltTime(mm.getValue());
                            int fmin = spiltMin(mm.getValue());
                            if (fhr == selectedHour) {
                                if (selectedMinute > fmin) {
                                    mm.setTvalue(selectedHour + ":" + selectedMinute);
                                } else
                                    Toast.makeText(ViewActivity.this, "To time should be greater", Toast.LENGTH_SHORT).show();
                            } else if (fhr < selectedHour) {
                                mm.setTvalue(selectedHour + ":" + selectedMinute);
                            } else
                                Toast.makeText(ViewActivity.this, "To time should be greater", Toast.LENGTH_SHORT).show();

                        }
                    }

                } else
                    mm.setValue(selectedHour + ":" + selectedMinute);


                adp_view.notifyDataSetChanged();
                commonFun();

            }
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public int spiltTime(String val) {
        String v = val.substring(0, val.indexOf(":"));
        return Integer.parseInt(v);
    }

    public int spiltMin(String val) {
        String v = val.substring(val.indexOf(":") + 1);
        return Integer.parseInt(v);
    }

    public long dateDifference(String d1, String d2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        try {
            Date date1 = simpleDateFormat.parse(d1 + " 00:00:00");
            Date date2 = simpleDateFormat.parse(d2 + " 00:00:00");

            long different = date2.getTime() - date1.getTime();
            Log.v("priting_date_diffence", different + "");
            return different;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void callDynamicViewList() {


        try {
            try {
                Log.v("FORM_NAME:", getIntent().getStringExtra("frmname"));
            } catch (Exception e) {

            }

            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(getIntent().getStringExtra("frmname")))) {
                Log.v("FORM_ID:", frm_id);
                JSONObject json = new JSONObject();
                json.put("slno", frm_id);

                Log.v("printing_sf_code", json.toString());
                Call<ResponseBody> approval = apiService.getView(json.toString());

                approval.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Log.v("printing_res_track_dV", response.body().byteStream() + "");
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

                                sharedCommonPref.save(getIntent().getStringExtra("frmname"), is.toString());
                                createDynamicView(is.toString());


                            } catch (Exception e) {

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            } else {
                createDynamicView(sharedCommonPref.getvalue(getIntent().getStringExtra("frmname")));
            }

        } catch (Exception e) {
        }

    }


    void createDynamicView(String data) {
        try {

            array_view.clear();
            Log.v("printing_dynamic_view", data);
            JSONArray jsonArray = new JSONArray(data);

            if (jsonArray.length() == 0) {
                Toast.makeText(ViewActivity.this, "No controls available for this form ", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {
                JSONObject jsonjk = jsonArray.getJSONObject(0);
                if (jsonjk.getString("Control_id").equalsIgnoreCase("19")) {
                    callDynamicViewListView();
                } else {
                    if (share.getString("exist", "").equalsIgnoreCase("E") && share.getString("fab", "").equalsIgnoreCase("1")) {
                        ArrayList<SelectionModel> arr = new ArrayList<>();
                        array_view.add(new ModelDynamicView("19", share.getString("arr", ""), "", share.getString("value", ""), arr, "", "", "0", "", "", ""));

                        SharedPreferences.Editor edit = share.edit();
                        edit.putString("exist", "");
                        edit.putString("fab", "");
                        edit.putString("value", "");
                        edit.putString("arr", "");
                        edit.commit();

                    }
                    for (int i = 0; i < jsonArray.length(); i++) {

                        ArrayList<SelectionModel> arr = new ArrayList<>();
                        JSONObject json = jsonArray.getJSONObject(i);
                        JSONArray jarray = null;
                        try {
                            jarray = json.getJSONArray("input");
                            String filterText = json.getString("Filter_Text");
                            String filterValue = json.getString("Filter_Value");
                            if (!filterText.equals("")) {
                                if (filterText.contains(",")) {
                                    String[] txtArray = filterText.split(",");
                                    String[] valueArray = filterValue.split(",");
                                    for (int j = 0; j < txtArray.length; j++) {
                                        filterList.add(new SelectionModel(txtArray[j], valueArray[j]));
                                    }
                                } else {
                                    filterList.add(new SelectionModel(filterText, filterValue));
                                }
                                //   Log.e("ListValues_fil", filterList.toString());
                            }
                        } catch (Exception e) {
                            // Log.v("NO_INPUT:0", e.getMessage());
                        }

                        //  Log.v("Printing_ctrl_id", json.getString("Control_id"));
                        if (json.getString("Control_id").equalsIgnoreCase("23")) {
                            try {
                                //   Log.v("NO_INPUT:1", "In:" + i);
                                String gettingfield = json.getString("Fld_Src_Field");
                                array_view.add(new ModelDynamicView(json.getString("Control_id"), gettingfield, json.getString("Fld_Name"), "", arr, json.getString("Fld_Length"), json.getString("Fld_ID"), json.getString("Frm_ID"), "", json.getString("Fld_Mandatory"), json.getString("Field_Col")));
                                fab_value = json.getString("type");
                                btnShow = json.getString("target");
                                tool_header.setText(json.getString("header"));
                                header = json.getString("header");
                                //  Log.v("NO_INPUT:1", "OUT:" + i);
                            } catch (Exception e) {
                                // Log.v("NO_INPUT:1", e.getMessage());
                            }
                        } else if (json.getString("Control_id").equalsIgnoreCase("19")) {
                            try {
                                //Log.v("NO_INPUT:2", "In:" + i);
                                String gettingfield = json.getString("Fld_Src_Field");
                                array_view.add(new ModelDynamicView("19", jarray.toString(), "", gettingfield, arr, "", "", json.getString("Target_Form"), "", "", ""));
                                fab_value = json.getString("type");
                                btnShow = json.getString("target");
                                tool_header.setText(json.getString("header"));
                                // Log.v("FORM_NAME:", json.getString("header"));

                                header = json.getString("header");
                                // Log.v("NO_INPUT:2", "Out:" + i);

                            } catch (Exception e) {
                                //  Log.v("NO_INPUT:2", e.getMessage());
                            }
                        } else {
                            try {
                                if (jarray != null && jarray.length() != 0) {
                                    for (int m = 0; m < jarray.length(); m++) {
                                        //  Log.v("NO_INPUT:3", "InArr:" + m + ":com:" + i);

                                        JSONObject jjss = jarray.getJSONObject(m);
                                        //  Log.v("json_input_iss", jjss.getString(json.getString("code")));
                                        arr.add(new SelectionModel(jjss.getString(json.getString("name")), false, jjss.getString(json.getString("code"))));
                                        //Log.v("NO_INPUT:3", "OUTArr:" + m + ":com:" + i);
                                    }
                                }
                                // Log.v("NO_INPUT:3", "In:" + i);

                                fab_value = json.getString("type");
                                btnShow = json.getString("target");
                                tool_header.setText(json.getString("header"));
                                header = json.getString("header");
                                //Log.v("NO_INPUT:3", "Out:1" + i);

                                array_view.add(new ModelDynamicView(json.getString("Control_id"), "", json.getString("Fld_Name"), "",
                                        arr, json.getString("Fld_Length"), json.getString("Fld_ID"), json.getString("Frm_ID"), "",
                                        json.getString("Fld_Mandatory"), json.getString("Field_Col")));
                                // Log.v("NO_INPUT:3", "Out:2" + i);
                            } catch (Exception e) {
                                // Log.v("NO_INPUT:3", e.getMessage());
                            }
                        }
                    }

                    adp_view = new AdapterForDynamicView(ViewActivity.this, array_view);
                    list.setAdapter(adp_view);
                    adp_view.notifyDataSetChanged();
                    if (Integer.parseInt(fab_value) > 0) {
                        fab.setVisibility(View.VISIBLE);
                        value = Integer.parseInt(fab_value);
                    }
                    if (btnShow.equalsIgnoreCase("T"))
                        btn_save.setVisibility(View.VISIBLE);
                    else
                        btn_save.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    key = SF_code + "_" + frm_id + "_" + System.currentTimeMillis();

                }
                //   Log.v("Printing_arr_view", array_view.size() + "");
            }

        } catch (Exception e) {
            //  Log.v("NO_INPUT:Ex", e.getMessage());
            progressDialog.dismiss();
            adp_view = new AdapterForDynamicView(ViewActivity.this, array_view);
            list.setAdapter(adp_view);
            adp_view.notifyDataSetChanged();

        }
    }

    public void saveDynamicList() {
        JSONObject json = new JSONObject();
        try {
            JSONArray ja = new JSONArray(shareKey.getString("keys", ""));

            json.put("data", ja);

            Log.v("save_printing_sf_code", ja.toString());
            Call<ResponseBody> approval = apiService.saveView(ja.toString());

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
                            Log.v("printing_save_tp", is.toString());
                            JSONObject jj = new JSONObject(is.toString());
                            if (jj.getString("success").equalsIgnoreCase("true")) {
                                //bommu
                                //Intent i = new Intent(ViewActivity.this, ProcurementDashboardActivity.class);
                                Intent i = new Intent(ViewActivity.this, SFA_Activity.class);
                                startActivity(i);
                            }

                        } catch (Exception e) {
                            Log.v("Exception_fmcg", e.getMessage());
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

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_progress);
        return dialog;
    }

    public void commonFun() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void popupCapture() {
        final Dialog dialog = new Dialog(ViewActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_capture);
        dialog.show();
        TextView upload = dialog.findViewById(R.id.upload);
        TextView camera = dialog.findViewById(R.id.camera);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMultiImage();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureFile();
            }
        });
    }

    public boolean validationOfField() {
        boolean val = true;
        for (int i = 0; i < array_view.size(); i++) {
            ModelDynamicView mm = array_view.get(i);
            if (mm.getMandatory().equalsIgnoreCase("0") && (!mm.getViewid().equalsIgnoreCase("22")) && (!mm.getViewid().equalsIgnoreCase("23")) && (!mm.getViewid().equalsIgnoreCase("19"))) {
                if (mm.getViewid().equalsIgnoreCase("12") || mm.getViewid().equalsIgnoreCase("9")) {
                    if (TextUtils.isEmpty(mm.getTvalue()) || TextUtils.isEmpty(mm.getValue()))
                        return false;
                } else {
                    if (TextUtils.isEmpty(mm.getValue()))
                        return false;
                }
            }
        }
        return val;
    }

    public void callDynamicViewListView() {
        JSONObject json = new JSONObject();
        try {
            json.put("slno", frm_id);

            Log.v("printing_sf_code_LV", json.toString());
            Call<ResponseBody> approval = apiService.getView(json.toString());

            approval.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.v("printing_res_track_LV", response.body().byteStream() + "");
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
                            // array_view.clear();
                            Log.v("printing_dynamic_LV", is.toString());
                            JSONArray jsonArray = new JSONArray(is.toString());

                            ArrayList<SelectionModel> arr = new ArrayList<>();
                            JSONObject json = jsonArray.getJSONObject(0);
                            String gettingfield = json.getString("Fld_Src_Field");
                            String[] splitbyComma = gettingfield.split(",");

                            JSONArray jarray = json.getJSONArray("input");

                            adpt = new FilterDemoAdapter(ViewActivity.this, jarray, gettingfield, 0, json.getString("Target_Form"));
                            list.setAdapter(adpt);
                            fab_value = json.getString("type");
                            btnShow = json.getString("target");
                            tool_header.setText(json.getString("header"));
                            header = json.getString("header");
                            key = SF_code + "_" + frm_id + "_" + System.currentTimeMillis();
                            if (Integer.parseInt(fab_value) > 0) {
                                fab.setVisibility(View.VISIBLE);
                                value = Integer.parseInt(fab_value);
                            }
                            if (btnShow.equalsIgnoreCase("T"))
                                btn_save.setVisibility(View.VISIBLE);
                            else
                                btn_save.setVisibility(View.GONE);
                            progressDialog.dismiss();

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

    public void getMulipart(String path, int x) {
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field(Shared_Common_Pref.Sf_Code);
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

        Callto = apiService.uploadProcPic(values, imgg);

        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("print_upload_file", "ggg" + response.isSuccessful() + response.body());

                try {
                    if (response.isSuccessful()) {


                        Log.v("print_upload_file_true", "ggg" + response);
                        JSONObject jb = null;
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("request_data_upload", String.valueOf(jsonData));
                        JSONObject js = new JSONObject(jsonData);
                        if (js.getString("success").equalsIgnoreCase("true")) {
                            Log.v("printing_dynamic_cou", js.getString("url"));

                        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences.Editor edit = share.edit();
        edit.putString("fab", "1");
        edit.commit();

        try {
            JSONArray jjj = new JSONArray(shareKey.getString("keys", "").toString());
            if (jjj.length() != 0) {

                Log.v("printing_json_je", jjj.toString());
                JSONArray ja = new JSONArray();
                for (int i = 0; i < jjj.length(); i++) {
                    if (i != jjj.length() - 1) {
                        JSONObject js = jjj.getJSONObject(i);
                        Log.v("printing_json_ke", js.toString());
                        ja.put(js);
                    }
                }
                edit = shareKey.edit();
                Log.v("printing_json_fi", ja.toString());
                edit.putString("keys", ja.toString());
                edit.commit();
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        header = tool_header.getText().toString();
    }

//    @Override
//    public void itemClick(SelectionModel selectionModel) {
//        try {
//            array_view.get(arrayViewPos).setValue(selectionModel.getTxt());
//            if (filterList.size() != 0) {
//                for (int j = 0; j < filterList.size(); j++) {
//                    if (filterList.get(j).getTxt().equals(mCreationId)) {
//                        selectedFilter.add(new SelectionModel(filterList.get(j).getCode(), selectionModel.getTxt()));
//                    }
//                }
//                Log.e("ListValues_fil", filterList.toString());
//                Log.e("ListValues_select", selectedFilter.toString());
//            }
//            adp_view.notifyDataSetChanged();
//
//            dialog.dismiss();
//            commonFun();
//        } catch (Exception e) {
//            Log.v("itemClick:", e.getMessage());
//        }
//
//    }
}