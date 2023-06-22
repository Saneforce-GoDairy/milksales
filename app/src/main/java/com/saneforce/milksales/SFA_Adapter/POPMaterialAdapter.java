package com.saneforce.milksales.SFA_Adapter;

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
import com.saneforce.milksales.SFA_Activity.POPActivity;
import com.saneforce.milksales.common.FileUploadService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POPMaterialAdapter extends RecyclerView.Adapter<POPMaterialAdapter.MyViewHolder> {
    Context context;


    Shared_Common_Pref shared_common_pref;
    List<QPS_Modal> mData = new ArrayList<>();

    FilesAdapter filesAdapter;
    ArrayList<List<String>> fileList = new ArrayList<>();

    Common_Class common_class;

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public POPMaterialAdapter(Context context, List<QPS_Modal> mData) {
        this.context = context;
        this.mData = mData;
        shared_common_pref = new Shared_Common_Pref(context);
        common_class = new Common_Class(context);

        try {
            for (int i = 0; i < mData.size(); i++) {

                String images = mData.get(i).getFileName();
                List<String> items = new ArrayList<>();
                String[] res = images.split("[,]", 0);
                for (String myStr : res) {
                    if (!Common_Class.isNullOrEmpty(myStr)) {
                       // items.add(myStr);
                        String sname = ApiClient.BASE_URL +myStr;
                        sname = sname.replaceAll("server/", "");
                        items.add(sname);

                    }

                }

                fileList.add(items);
            }
        } catch (Exception e) {

        }


    }

    @NonNull
    @Override
    public POPMaterialAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_popmaterial_layout, null, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(POPMaterialAdapter.MyViewHolder holder, int position) {
        try {

            holder.receivedDate.setText("" + mData.get(position).getReceivedDate());
            holder.status.setText("" + mData.get(position).getStatus());
            holder.materialName.setText("" + mData.get(position).getName());

            if (mData.get(position).getStatus().equalsIgnoreCase("Approved")) {
                holder.btnComplete.setVisibility(View.GONE);
                holder.ivCaptureImg.setVisibility(View.GONE);
                holder.ivAttachImg.setVisibility(View.GONE);
            } else {
                holder.btnComplete.setVisibility(View.VISIBLE);
                holder.ivCaptureImg.setVisibility(View.VISIBLE);
            }


            holder.ivCaptureImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        if (mData.get(holder.getBindingAdapterPosition()).getFileUrls() != null && mData.get(holder.getBindingAdapterPosition()).getFileUrls().size() >= 3) {
                            Toast.makeText(context, "Limit Exceed...", Toast.LENGTH_SHORT).show();

                        } else {
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
                        }
                    } catch (Exception e) {

                    }


                }
            });


            //working
            holder.btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        SaveOrder(mData.get(position).getsNo(), mData.get(position).getP_id(), position);
                        for (int i = 0; i < mData.get(position).getFileUrls().size(); i++) {

                            String filePath = mData.get(position).getFileUrls().get(i).replaceAll("file:/", "");
                            File file = new File(filePath);

                            Intent mIntent = new Intent(context, FileUploadService.class);
                            mIntent.putExtra("mFilePath", filePath);
                            mIntent.putExtra("SF", Shared_Common_Pref.Sf_Code);
                            mIntent.putExtra("FileName", file.getName());
                            //   mIntent.putExtra("Mode", "ExpClaim;" + qpsModalList.get(i).getFileKey());
                            mIntent.putExtra("Mode", "POP");
                            FileUploadService.enqueueWork(context, mIntent);


                        }

                    } catch (Exception e) {

                    }
                }
            });


            if (fileList != null && fileList.size() > position && fileList.get(position).size() > 0 && mData.get(position).getStatus().equalsIgnoreCase("Approved")) {
                filesAdapter = new FilesAdapter(fileList.get(position), R.layout.adapter_qps_files_layout, context);
                holder.rvFile.setAdapter(filesAdapter);
            }
        } catch (Exception e) {
            Log.e("POPMAterialAdaptr:", e.getMessage());
        }

    }


    private void SaveOrder(String transNo, String popId, int pos) {

        JSONArray data = new JSONArray();
        JSONObject ActivityData = new JSONObject();


        try {
            JSONObject HeadItem = new JSONObject();
            HeadItem.put("divisionCode", Shared_Common_Pref.Div_Code);
            HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
            HeadItem.put("retailorCode", Shared_Common_Pref.OutletCode);

            HeadItem.put("distributorcode", shared_common_pref.getvalue(Constants.Distributor_Id));

            HeadItem.put("date", Common_Class.GetDatewothouttime());

            HeadItem.put("pop_reqId", popId);
            HeadItem.put("pop_TransNo", transNo);


            ActivityData.put("POP_Header", HeadItem);
            JSONArray Order_Details = new JSONArray();
            for (int z = 0; z < mData.get(pos).getFileUrls().size(); z++) {
                File file = new File(mData.get(pos).getFileUrls().get(z));

                JSONObject ProdItem = new JSONObject();
                ProdItem.put("pop_filename", file.getName());

                Order_Details.put(ProdItem);

            }
            ActivityData.put("file_Details", Order_Details);
            data.put(ActivityData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> responseBodyCall = apiInterface.approvePOPEntry(data.toString());
        responseBodyCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonObjects = new JSONObject(response.body().toString());

                        if (jsonObjects.getBoolean("success")) {
                            common_class.getDb_310Data(Constants.POP_ENTRY_STATUS, POPActivity.popActivity);
                        }
                        Toast.makeText(context, jsonObjects.getString("Msg"), Toast.LENGTH_SHORT).show();

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
        TextView requestNo, receivedDate, status, materialName;
        Button btnComplete;
        ImageView ivCaptureImg, ivAttachImg;
        RecyclerView rvFile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            receivedDate = itemView.findViewById(R.id.tvReceivedDate);
            status = itemView.findViewById(R.id.tvStatus);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            ivCaptureImg = itemView.findViewById(R.id.ivQPSCaptureImg);
            ivAttachImg = itemView.findViewById(R.id.ivQPSPreviewImg);
            rvFile = itemView.findViewById(R.id.rvFiles);
            materialName = itemView.findViewById(R.id.tvPOPMaterial);

        }
    }
}