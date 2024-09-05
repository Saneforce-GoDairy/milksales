package com.saneforce.godairy.adapters;

import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Activity.TAApprovalActivity;
import com.saneforce.godairy.Activity_Hap.TACumulativeApproval;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AlertDialogClickListener;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.onPayslipItemClick;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.assistantClass.AssistantClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TAApprListItem extends RecyclerView.Adapter<TAApprListItem.ViewHolder> {
    private static final String TAG = "ShiftList";
    private JSONArray mlist = new JSONArray();
    private Context mContext;
    static onPayslipItemClick payClick;
    NumberFormat formatter = new DecimalFormat("##0.00");
    Shared_Common_Pref sharedCommonPref;
    Common_Class common_class;
    AssistantClass assistantClass;


    public TAApprListItem(JSONArray mlist, Context mContext) {
        this.mlist = mlist;
        this.mContext = mContext;
        sharedCommonPref = new Shared_Common_Pref(mContext);
        common_class = new Common_Class(mContext);
        assistantClass = new AssistantClass(mContext);
    }

    @NonNull
    @Override
    public TAApprListItem.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ta_appr, parent, false);
        TAApprListItem.ViewHolder holder = new TAApprListItem.ViewHolder(view);
        return holder;

    }

    public static void SetPayOnClickListener(onPayslipItemClick mPayClick) {
        payClick = mPayClick;
    }

    @Override
    public void onBindViewHolder(@NonNull TAApprListItem.ViewHolder holder, int position) {
        JSONObject itm = null;
        try {
            itm = mlist.getJSONObject(position);
            holder.txEMPNm.setText(itm.getString("EmployeeName"));
            holder.txEMPDesig.setText(itm.getString("Designation"));
            holder.tvTotAmt.setText(CurrencySymbol+" " + formatter.format(itm.getDouble("Total")));
            holder.tvPeriod.setText(itm.getString("FromDate") + " - " + itm.getString("ToDate"));
            holder.tvDailyAlow.setText(CurrencySymbol+" " + formatter.format(itm.getDouble("DailyAllowance")));
            holder.tvfuelAlow.setText(CurrencySymbol+" " + formatter.format(itm.getDouble("FuelAllowance")));
            holder.tvTrvlExp.setText(CurrencySymbol+" "+ formatter.format(itm.getDouble("TravelExpense")));
            holder.tvOtherExp.setText(CurrencySymbol+" " + formatter.format(itm.getDouble("OtherExpense")));
            holder.tvLocConv.setText(CurrencySymbol+" " + formatter.format(itm.getDouble("LocalConveyance")));
            holder.tvLodgingAlow.setText(CurrencySymbol+" " + formatter.format(itm.getDouble("LodgingAllowance")));


            holder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(mContext, TAApprovalActivity.class);
                        intent.putExtra("view_id", mlist.getJSONObject(position).getString("Sf_Code"));
                        intent.putExtra("name", mlist.getJSONObject(position).getString("EmployeeName"));
                        mContext.startActivity(intent);
                    } catch (Exception e) {

                    }
                }
            });
            holder.btnApprov.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assistantClass.showAlertDialog("", "Are you sure you want to Approve?", true, "Yes", "No", new AlertDialogClickListener() {
                        @Override
                        public void onPositiveButtonClick(DialogInterface dialog) {
                            try {
                                SendtpApproval(1, mlist.getJSONObject(position), "");
                            } catch (Exception e) {

                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegativeButtonClick(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            holder.btnRjct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assistantClass.showAlertDialog("", "Are you sure you want to Reject?", true, "Yes", "No", new AlertDialogClickListener() {
                        @Override
                        public void onPositiveButtonClick(DialogInterface dialog) {
                            try {
                                showAlertDialogButtonClicked(2, mlist.getJSONObject(position));
                            } catch (Exception e) {

                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegativeButtonClick(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    void SendtpApproval(int flag, JSONObject obj, String reason) {
        JSONObject taReq = new JSONObject();

        try {
            taReq.put("login_sfCode", sharedCommonPref.getvalue(Shared_Common_Pref.Sf_Code));
            taReq.put("emp_sfCode", obj.getString("Sf_Code"));

            taReq.put("Flag", flag);
            taReq.put("TDate", obj.getString("TDate"));
            taReq.put("FDate", obj.getString("FDate"));
            taReq.put("Date", Common_Class.GetDate());
            taReq.put("Reason", reason);


            Log.v("TA_REQ", taReq.toString());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> mCall = apiInterface.taCumulativeApprove(taReq.toString());

            mCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    // locationList=response.body();
                    try {
                       // mContext.startActivity(new Intent(mContext, TACumulativeApproval.class));
                        TACumulativeApproval.taCumulativeApproval.finish();
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        Log.v("Res>>", response.body().toString());
                        if (flag == 1) {
                            Toast.makeText(mContext, "TA Approved Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "TA Rejected  Successfully", Toast.LENGTH_SHORT).show();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showAlertDialogButtonClicked(int flag, JSONObject obj) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(mContext);
        builder.setTitle(HAPApp.Title);
        builder.setMessage("Reject Reason?");

        // set the custom layout
        final View customLayout
                = TACumulativeApproval.taCumulativeApproval.getLayoutInflater()
                .inflate(
                        R.layout.ta_reject_popup,
                        null);
        builder.setView(customLayout);

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        // add a button
        builder
                .setPositiveButton(
                        "REJECT",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                EditText editText
                                        = customLayout
                                        .findViewById(
                                                R.id.editText);


                                if (editText.getText().toString().equalsIgnoreCase("")) {
                                    common_class.showMsg(TACumulativeApproval.taCumulativeApproval, "Please Enter the Reason");
                                } else {
                                    dialog.dismiss();
                                    SendtpApproval(flag, obj, editText.getText().toString());
                                }


                            }
                        });


        AlertDialog dialog
                = builder.create();
        dialog.show();
    }


    @Override
    public int getItemCount() {

        return mlist.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txEMPNm, txEMPDesig, tvTotAmt, tvPeriod, tvDailyAlow, tvfuelAlow, tvLodgingAlow, tvTrvlExp, tvOtherExp, tvLocConv;
        LinearLayout parentLayout;
        Button btnView, btnApprov, btnRjct;
        //CardView secondarylayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txEMPNm = itemView.findViewById(R.id.txEmpName);
            txEMPDesig = itemView.findViewById(R.id.txDesgName);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            tvTotAmt = itemView.findViewById(R.id.tvTotalAmount);
            tvDailyAlow = itemView.findViewById(R.id.tvDailyAlowAmt);
            tvPeriod = itemView.findViewById(R.id.tvPeriod);
            tvfuelAlow = itemView.findViewById(R.id.tvFuelAlowAmt);
            tvLodgingAlow = itemView.findViewById(R.id.tvLodgingAlowAmt);
            tvTrvlExp = itemView.findViewById(R.id.tvTrvlExpAmt);
            tvOtherExp = itemView.findViewById(R.id.tvOtherExpAmt);
            tvLocConv = itemView.findViewById(R.id.tvLocConveyanceAmt);
            btnView = itemView.findViewById(R.id.btn_View);
            btnApprov = itemView.findViewById(R.id.btn_approve);
            btnRjct = itemView.findViewById(R.id.btn_reject);


            //secondarylayout=itemView.findViewById(R.id.secondary_layout);
        }
    }
}
