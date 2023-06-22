package com.saneforce.milksales.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.saneforce.milksales.Activity.PdfViewerActivity;
import com.saneforce.milksales.Activity_Hap.FlightBookingApproval;
import com.saneforce.milksales.Common_Class.AlertDialogBox;
import com.saneforce.milksales.Interface.AlertBox;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.HAPApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightBooking_Approval_Adapter extends RecyclerView.Adapter<FlightBooking_Approval_Adapter.MyViewHolder> {
    private JSONArray mArr;

    private Context context;
    private String sSF;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvStatus, tvNoOfTraveler, tvbookedBy, tvViewSta,
        txFrmPlc,txToPlc,txTrvDate,txRetTrvDate,txRetFrmPlc,txRetToPlc,txRet;
        Button btnApproval,btnReject,btnCancel,btnSvReject,btncancelRej;
        EditText RejectRmk;
        public View vwReject;
        LinearLayout tvVwPassg;

        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvStatus = view.findViewById(R.id.tvFBStatus);
            tvNoOfTraveler = view.findViewById(R.id.tvTravelerCount);
            tvVwPassg = view.findViewById(R.id.tvVwPassg);
            tvbookedBy = view.findViewById(R.id.tvBookedBy);
            tvViewSta = view.findViewById(R.id.tvViewSta);
            txFrmPlc = view.findViewById(R.id.tvFrom);
            txToPlc = view.findViewById(R.id.tvTo);
            txRetFrmPlc = view.findViewById(R.id.tvRetFrom);
            txRetToPlc = view.findViewById(R.id.tvRetTo);
            txRet = view.findViewById(R.id.tvReturn);
            vwReject = view.findViewById(R.id.vwReject);
            txTrvDate = view.findViewById(R.id.tvTrvDate);
            txRetTrvDate = view.findViewById(R.id.tvRetTrvDate);
            RejectRmk = view.findViewById(R.id.edtRejectRmk);

            btnApproval= view.findViewById(R.id.Approvetkt);
            btnReject= view.findViewById(R.id.rejecttkt);
            btnSvReject= view.findViewById(R.id.btnSvReject);
            btncancelRej= view.findViewById(R.id.btncancelRej);
            btnCancel= view.findViewById(R.id.canceltkt);
        }
    }


    public FlightBooking_Approval_Adapter(JSONArray arr, Context context, String mSF) {
        this.mArr = arr;
        this.context = context;
        this.sSF=mSF;
    }

    @Override
    public FlightBooking_Approval_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.flight_booking_status_listitem, null, false);
        return new FlightBooking_Approval_Adapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(FlightBooking_Approval_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            JSONObject obj = mArr.getJSONObject(position);
            holder.tvDate.setText("" + obj.getString("RequestDate"));
            holder.tvStatus.setText("" + obj.getString("BookingStatus"));
            holder.tvNoOfTraveler.setText("" + obj.getString("Travellers"));
            holder.tvbookedBy.setText("" + obj.getString("RequestedBy"));

            holder.txFrmPlc.setText("" + obj.getString("BookFR_Plc"));
            holder.txToPlc.setText("" + obj.getString("BookTo_Plc"));
            holder.txRet.setText("" + obj.getString("Ret"));

            holder.txRetFrmPlc.setText("" + obj.getString("BookRetFR_Plc"));
            holder.txRetToPlc.setText("" + obj.getString("BookRetTo_Plc"));
            holder.txTrvDate.setText("" + obj.getString("BookDt") + " - "+ obj.getString("BookSes"));
            holder.txRetTrvDate.setText("" + obj.getString("BookRetDt") + " - "+ obj.getString("BookRetSes"));

            holder.btnApproval.setVisibility(View.VISIBLE);
            holder.btnReject.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.GONE);
            holder.tvViewSta.setVisibility(View.GONE);
            if (obj.getString("BookingStatus").equalsIgnoreCase("Booked")) {
                holder.tvStatus.setBackgroundResource(R.drawable.button_green);
            } else {
                holder.tvStatus.setBackgroundResource(R.drawable.button_yellows);
            }

            holder.vwReject.setVisibility(View.GONE);
            holder.tvStatus.setPadding(20, 5, 20, 5);

            holder.btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        holder.vwReject.setVisibility(View.VISIBLE);
                        holder.btnApproval.setVisibility(View.GONE);
                        holder.btnReject.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }
                }
            });
            holder.btncancelRej.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        holder.vwReject.setVisibility(View.GONE);
                        holder.btnApproval.setVisibility(View.VISIBLE);
                        holder.btnReject.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                    }
                }
            });
            holder.tvVwPassg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        FlightBookingApproval.activity.showTravelersDialog(mArr.getJSONObject(position).getJSONArray("TrvDetails"));
                    } catch (Exception e) {
                    }
                }
            });
            holder.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialogBox.showDialog(context, HAPApp.Title, String.valueOf(Html.fromHtml("Do You Submit Flight Booking Request.")), "Yes", "No", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                            JSONObject jObj=new JSONObject();
                            try {
                                jObj.put("SF",sSF);
                                jObj.put("BookID",obj.getString("BookID"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            apiInterface.JsonSave("cancel/flightbook",jObj.toString()).enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    JsonObject Res=response.body();
                                    String Msg= Res.get("Msg").getAsString();
                                    if(!Msg.equalsIgnoreCase("")){
                                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                                .setTitle(HAPApp.Title)
                                                .setMessage(Html.fromHtml(Msg))
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                })
                                                .show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                }
                            });


                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });



                }
            });
            holder.btnApproval.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialogBox.showDialog(context, HAPApp.Title, String.valueOf(Html.fromHtml("Are you confirm to  <span style=\"color:#cc2311\"><b>Approve</b></span> this booking request.")), "Yes", "No", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            try {
                                ApproveFlightBooking(sSF ,obj.getString("BookID"),"",1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            holder.btnSvReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.RejectRmk.getText().equals("")){
                        Toast.makeText(context,"Enter the Reject Reason",Toast.LENGTH_LONG).show();
                        return;
                    }
                    AlertDialogBox.showDialog(context, HAPApp.Title, String.valueOf(Html.fromHtml("Are you confirm to  <span style=\"color:#cc2311\"><b>Reject</b></span> this booking request.")), "Yes", "No", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            try {
                                ApproveFlightBooking(sSF ,obj.getString("BookID"),holder.RejectRmk.getText().toString(),4);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                }
            });

            holder.tvViewSta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent stat = new Intent(context, PdfViewerActivity.class);
                        stat.putExtra("PDF_ONE", mArr.getJSONObject(position).getString("Attachment").replaceAll("http:", "https:"));
                        stat.putExtra("PDF_FILE", "Web");
                        context.startActivity(stat);
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {

        }
    }
public void ApproveFlightBooking(String SF,String Bookid,String Rmks,int flag){
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    JSONObject jObj=new JSONObject();
    try {
        jObj.put("SF",SF);
        jObj.put("BookID",Bookid);
        jObj.put("Rmks",Rmks);
        jObj.put("flag",flag);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    apiInterface.JsonSave("apprv/flightbook",jObj.toString()).enqueue(new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            JsonObject Res=response.body();
            String Msg= Res.get("Msg").getAsString();
            if(!Msg.equalsIgnoreCase("")){
                FlightBookingApproval.activity.refreshData();
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle(HAPApp.Title)
                        .setMessage(Html.fromHtml(Msg))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {

        }
    });
}
    @Override
    public int getItemCount() {
        return mArr.length();
    }
}