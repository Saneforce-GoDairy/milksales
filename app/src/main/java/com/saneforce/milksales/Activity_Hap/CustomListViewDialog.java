package com.saneforce.milksales.Activity_Hap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.saneforce.milksales.Activity.Util.UpdateUi;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.Nearby_Outlets;
import com.saneforce.milksales.adapters.DataAdapter;

import java.util.List;

public class CustomListViewDialog extends Dialog implements View.OnClickListener, UpdateUi {
    public CustomListViewDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomListViewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public Activity activity;
    public Context mContext;
    public Dialog dialog;
    public Button mBtnSave, no;
    EditText searchView;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    //RecyclerView.Adapter adapter;
    DataAdapter da;
    int type;
    List<Common_Model> mDataset;
    ImageView mIvAddMapKey;
    EditText etAddKey;
    TextView tvtitle;
    Master_Interface updateUi;


    public CustomListViewDialog(Activity a, List<Common_Model> wk, int type) {
        super(a);
        this.activity = a;
        this.type = type;
        this.mDataset = wk;
        this.da = new DataAdapter(mDataset, activity, type);
        setupLayout();
    }

    private void setupLayout() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_layout);
        no = (Button) findViewById(R.id.no);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recycler_view);
        mIvAddMapKey = (findViewById(R.id.ivAddKey));
        mBtnSave = findViewById(R.id.btn_save);
        etAddKey = findViewById(R.id.et_addMapKey);
        tvtitle = findViewById(R.id.txt);

        if (type == 1000) {
            searchView.setVisibility(View.GONE);
            mIvAddMapKey.setVisibility(View.VISIBLE);
            tvtitle.setText("Map KeyList");

        }
        if (type == 500) {
            searchView.setVisibility(View.GONE);
        }

        if (type == 501) {
            mIvAddMapKey.setVisibility(View.VISIBLE);
            updateUi = ((Master_Interface) activity);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(da);

        no.setOnClickListener(this);
        //da.notifyDataSetChanged();
        searchView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                da.getFilter().filter(s.toString());
            }
        });

        mIvAddMapKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 501) {
                    showDialog();
                } else {
                    etAddKey.setVisibility(View.VISIBLE);
                    mBtnSave.setVisibility(View.VISIBLE);
                }
            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (etAddKey.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Empty key is not allowed", Toast.LENGTH_SHORT).show();
                    } else {
                        mDataset.add(new Common_Model(etAddKey.getText().toString()));
                        etAddKey.setText("");
                        Gson gson = new Gson();
//                        if (type == 501) {
//                            da.notifyDataSetChanged();
//                            updateUi.OnclickMasterType(mDataset, mDataset.size() - 1, type);
//                        } else {
                        Nearby_Outlets.shared_common_pref.save(Constants.MAP_KEYLIST, gson.toJson(mDataset));
                        da.notifyDataSetChanged();
                        // }

                    }

                } catch (Exception e) {
                    Log.v("CustomDialog: ", e.getMessage());
                }
            }
        });


    }

    private void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(activity);

        final View view = inflater.inflate(R.layout.edittext_dialog, null);
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setCancelable(false);

        final EditText etComments = (EditText) view.findViewById(R.id.et_addItem);
        Button btnSave = (Button) view.findViewById(R.id.btn_save);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etComments.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Empty key is not allowed", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.dismiss();
                    mDataset.add(new Common_Model(etComments.getText().toString()));
                    etComments.setText("");
                    da.notifyDataSetChanged();
                    updateUi.OnclickMasterType(mDataset, mDataset.size() - 1, type);

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.setView(view);
        alertDialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.no:
                dismiss();
                break;
            default:
                break;
        }

    }


    @Override
    public void update(int value, int pos) {
        Log.e("Custom_Dialog_Calling", "");
        dismiss();
    }


}