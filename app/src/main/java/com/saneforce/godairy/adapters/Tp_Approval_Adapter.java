package com.saneforce.godairy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.saneforce.godairy.Activity_Hap.TP_Approval_Details;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.Tp_Approval_FF_Modal;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tp_Approval_Adapter extends RecyclerView.Adapter<Tp_Approval_Adapter.MyViewHolder> {

    private List<Tp_Approval_FF_Modal> Tp_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    String SF_code = "",currentDate="",reportingSF="";
    Common_Class common_class;
    Shared_Common_Pref sharedCommonPref;
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    Intent i;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView month, date, worktype, remarks, approve, reject;
        public LinearLayout open;


        public MyViewHolder(View view) {
            super(view);
            month = (TextView) view.findViewById(R.id.tpApprovalMonth);
            date = (TextView) view.findViewById(R.id.tpApprovalDate);
            remarks = (TextView) view.findViewById(R.id.tpApprovalRemarks);
            worktype = (TextView) view.findViewById(R.id.tpApprovalWorkType);
            open = (LinearLayout) view.findViewById(R.id.detailsLay);
            approve =(TextView) view.findViewById(R.id.approve);
            reject =(Button) view.findViewById(R.id.reject);

        }
    }


    public Tp_Approval_Adapter(List<Tp_Approval_FF_Modal> Tp_Approval_ModelsList, int rowLayout, Context context
            , AdapterOnClick mAdapterOnClick)
    {
        this.Tp_Approval_ModelsList = Tp_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;
        common_class = new Common_Class(context);
        sharedCommonPref = new Shared_Common_Pref(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tp_Approval_FF_Modal Tp_Approval_Model = Tp_Approval_ModelsList.get(position);
//        Intent intent = new Intent();
//        SF_code=intent.getStringExtra("sfCode");

        UserDetails = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
//        reportingSF = holder.i.getExtras().getString("Sf_Code");

        Log.v("rsfCode",SF_code);
        holder.month.setText(Tp_Approval_Model.getTourMonth());
        holder.date.setText(Tp_Approval_Model.getDate());
        holder.remarks.setText(Tp_Approval_Model.getRemarks());
        holder.worktype.setText(Tp_Approval_Model.getWorktypeName());

        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapterOnClick.onIntentClick(position);
            }
        });

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SendPjpApproval(0,"");

                } catch (Exception e) {

                }
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AppCompatActivity activity=(AppCompatActivity)v.getContext();
                final AlertDialog alertDialog=new AlertDialog.Builder(activity).create();
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(HAPApp.Title);
                builder.setMessage("Do you confirm to Reject PJP Approval?");
                final View customLayout
                        = TP_Approval_Details.tpDetails.getLayoutInflater().inflate(R.layout.ta_reject_popup, null);
                builder.setView(customLayout);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        EditText editText
                                = customLayout
                                .findViewById(
                                        R.id.editText);


                        if (editText.getText().toString().equalsIgnoreCase("")) {
                            common_class.showMsg(TP_Approval_Details.tpDetails, "Please Enter the Reason");
//                            Toast.makeText(context,"Enter the REason",Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            SendPjpApproval(1, editText.getText().toString());
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                builder.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return Tp_Approval_ModelsList.size();
    }

    void SendPjpApproval(int flag, String reason) {
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("rSF", UserDetails.getString("Sfcode", ""));
//            jObj.put("sfCode",SF_code);
            jObj.put("Confirmed_Date",currentDate);
            JSONArray jArr = new JSONArray();
            for (int i = 0; i < 1; i++) {
                JSONObject obj1 = new JSONObject();
                obj1.put("Date", Tp_Approval_ModelsList.get(i).getDate());
                obj1.put("Confirmed", flag);
                obj1.put("sfcode", Tp_Approval_ModelsList.get(i).getSF_Code());
//                obj1.put("FieldForceName", filteredList.get(i).getFieldForceName());
//                obj1.put("HQ_Name", filteredList.get(i).getHQName());
//                obj1.put("Designation", filteredList.get(i).getDesignation());
//                obj1.put("Remarks", filteredList.get(i).getRemarks());
//                obj1.put("Work_Type", filteredList.get(i).getWorktypeName());
                obj1.put("Rejection_Reason", reason);
                jArr.put(obj1);
            }
            jObj.accumulate("PjpApprovedData" , jArr);

            Log.d("savePjpApproval", "ghkj" + jObj.toString());

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> responseBodyCall = apiInterface.pjpApprove(Shared_Common_Pref.Sf_Code,Shared_Common_Pref.Sf_Code,currentDate, jObj.toString());
            responseBodyCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            Log.e("JSON_VALUES", response.body().toString());
                            JSONObject jsonObjects = new JSONObject(response.body().toString());
                           // TP_Approval_Details.tpDetails.finish();

                            if (flag == 0) {
                                Toast.makeText(context, "PJP Approved Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "PJP Rejected  Successfully", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Log.e("errorMsg", e.toString());
                        }
                    } else {
                        Log.d("SUBMIT_VALUE", "ERROR"+response);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("SUBMIT_VALUE", "ERROR");

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}