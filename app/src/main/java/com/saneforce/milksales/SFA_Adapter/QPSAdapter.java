package com.saneforce.milksales.SFA_Adapter;

import static com.saneforce.milksales.SFA_Activity.QPSActivity.qpsActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.saneforce.milksales.Activity_Hap.AllowancCapture;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.OnImagePickListener;
import com.saneforce.milksales.R;
import com.saneforce.milksales.common.FileUploadService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QPSAdapter extends RecyclerView.Adapter<QPSAdapter.MyViewHolder> {
    Context context;
    List<QPS_Modal> mData;
    Shared_Common_Pref shared_common_pref;
    FilesAdapter filesAdapter;

    public QPSAdapter(Context context, List<QPS_Modal> mData) {
        this.context = context;
        this.mData = mData;
        shared_common_pref = new Shared_Common_Pref(context);

    }

    @NonNull
    @Override
    public QPSAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_qps_layout, null, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(QPSAdapter.MyViewHolder holder, int position) {
        try {
            holder.sNo.setText("" + mData.get(position).getsNo());
            holder.requestNo.setText("" + mData.get(position).getRequestNo());
            holder.gift.setText("" + mData.get(position).getGift());
            holder.bookingDate.setText("" + mData.get(position).getBookingDate());
            holder.duration.setText("" + mData.get(position).getDuration());
            holder.receivedDate.setText("" + mData.get(position).getReceivedDate());
            holder.status.setText("" + mData.get(position).getStatus());

            filesAdapter = new FilesAdapter(mData.get(position).getFileUrls(), R.layout.adapter_qps_files_layout, context);
            holder.rvFile.setAdapter(filesAdapter);


            if (mData.get(position).getStatus().equalsIgnoreCase("Approved")) {
                holder.btnComplete.setVisibility(View.GONE);
                holder.ivCaptureImg.setVisibility(View.GONE);
                holder.ivAttachImg.setVisibility(View.GONE);
            } else {
                holder.btnComplete.setVisibility(View.VISIBLE);
                holder.ivCaptureImg.setVisibility(View.VISIBLE);
                //holder.ivAttachImg.setVisibility(View.VISIBLE);
            }


            holder.ivCaptureImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mData.get(holder.getBindingAdapterPosition()).getFileUrls() == null || mData.get(holder.getBindingAdapterPosition()).getFileUrls().size() < 3) {
                        AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                            @Override
                            public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {

                                List<String> list = new ArrayList<>();
                                File file = new File(fullPath);
                                Uri contentUri = Uri.fromFile(file);

                                if (mData.get(holder.getBindingAdapterPosition()).getFileUrls() != null && mData.get(holder.getBindingAdapterPosition()).getFileUrls().size() > 0)
                                    list = (mData.get(position).getFileUrls());
                                list.add(contentUri.toString());
                                mData.get(holder.getBindingAdapterPosition()).setFileUrls(list);

                                filesAdapter = new FilesAdapter(mData.get(position).getFileUrls(), R.layout.adapter_local_files_layout, context);
                                holder.rvFile.setAdapter(filesAdapter);

                            }
                        });
                        Intent intent = new Intent(context, AllowancCapture.class);
                        intent.putExtra("allowance", "TAClaim");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Limit Exceed...", Toast.LENGTH_SHORT).show();
                    }


                }
            });

//            holder.ivAttachImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    AttachementActivity.setOnAttachmentDeleteListener(new OnAttachmentDelete() {
//                        @Override
//                        public void OnImageDelete(String Mode, int ImgCount) {
//
//                        }
//                    });
//
//                    Intent stat = new Intent(context, AttachementActivity.class);
//                    stat.putExtra("qps_localData", mData.get(position).getsNo() + "~key");
//                    context.startActivity(stat);
//                }
//            });
            //working
            holder.btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    SaveOrder(holder.requestNo.getText().toString(), position);

                    if (mData.get(position).getFileUrls() != null) {
                        for (int i = 0; i < mData.get(position).getFileUrls().size(); i++) {
                            String filePath = mData.get(position).getFileUrls().get(i).replaceAll("file:/", "");
                            File file = new File(filePath);

                            Intent mIntent = new Intent(context, FileUploadService.class);
                            mIntent.putExtra("mFilePath", filePath);
                            mIntent.putExtra("SF", Shared_Common_Pref.Sf_Code);
                            mIntent.putExtra("FileName", file.getName());
                            //   mIntent.putExtra("Mode", "ExpClaim;" + qpsModalList.get(i).getFileKey());
                            mIntent.putExtra("Mode", "QPS");
                            FileUploadService.enqueueWork(context, mIntent);


                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.e("QPSAdapter:", e.getMessage());
        }

    }


    private void SaveOrder(String reqNo, int pos) {

        JSONArray data = new JSONArray();
        JSONObject ActivityData = new JSONObject();
        try {
            JSONObject HeadItem = new JSONObject();
            HeadItem.put("divisionCode", Shared_Common_Pref.Div_Code);
            HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
            HeadItem.put("retailorCode", Shared_Common_Pref.OutletCode);

            HeadItem.put("distributorcode", shared_common_pref.getvalue(Constants.Distributor_Id));

            HeadItem.put("date", Common_Class.GetDatewothouttime());

            ActivityData.put("QPS_Header", HeadItem);
            JSONArray Order_Details = new JSONArray();
            if (mData.get(pos).getFileUrls() != null) {
                for (int z = 0; z < mData.get(pos).getFileUrls().size(); z++) {

                    JSONObject ProdItem = new JSONObject();
                    ProdItem.put("qps_filename", mData.get(pos).getFileUrls().get(z));
                    ProdItem.put("qps_reqNo", reqNo);
                    Order_Details.put(ProdItem);

                }
            }
            ActivityData.put("file_Details", Order_Details);
            data.put(ActivityData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> responseBodyCall = apiInterface.approveQPSEntry(data.toString());
        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObjects = new JSONObject(response.body().toString());
                        String san = jsonObjects.getString("success");
                        Log.e("Success_Message", san);

                        if (jsonObjects.getBoolean("success")) {
                            Toast.makeText(context, jsonObjects.getString("Msg"), Toast.LENGTH_SHORT).show();
                            qpsActivity.common_class.getDb_310Data(Constants.QPS_STATUS, qpsActivity);
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("SUBMIT_VALUE", "ERROR");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sNo, requestNo, gift, bookingDate, duration, receivedDate, status;
        Button btnComplete;
        ImageView ivCaptureImg, ivAttachImg;
        RecyclerView rvFile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sNo = itemView.findViewById(R.id.tvQpsSno);
            requestNo = itemView.findViewById(R.id.tvQPSReqNo);
            gift = itemView.findViewById(R.id.tvQPSGift);
            bookingDate = itemView.findViewById(R.id.tvQPSBookDate);
            duration = itemView.findViewById(R.id.tvQPSDuration);
            receivedDate = itemView.findViewById(R.id.tvQPSReceivedDate);
            status = itemView.findViewById(R.id.tvStatus);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            ivCaptureImg = itemView.findViewById(R.id.ivQPSCaptureImg);
            ivAttachImg = itemView.findViewById(R.id.ivQPSPreviewImg);
            rvFile = itemView.findViewById(R.id.rvFiles);

        }
    }
}