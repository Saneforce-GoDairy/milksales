package com.saneforce.godairy.SFA_Activity;

import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.saneforce.godairy.Activity_Hap.AllowancCapture;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.OnImagePickListener;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.FilesAdapter;
import com.saneforce.godairy.SFA_Adapter.QPS_Modal;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OtherBrandAdapter extends RecyclerView.Adapter<OtherBrandAdapter.MyViewHolder> {
    private List<QPS_Modal> mData;
    private int rowLayout;
    private Context context;
    AlertDialog.Builder builder;
    Gson gson;
    Shared_Common_Pref shared_common_pref;
    public static String TAG = "OtherBrandAdapter";
    private FilesAdapter filesAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productname, Rate, Amount, Disc, tvPos;
        EditText Qty, etPrice, Free, sku;
        RelativeLayout rlOtherBrand;
        ImageView rlDeleteBrand, ivCapture;
        RecyclerView rvFiles;

        public MyViewHolder(View view) {
            super(view);
            productname = view.findViewById(R.id.productname);
            Qty = view.findViewById(R.id.Qty);
            etPrice = view.findViewById(R.id.etPrice);
            Amount = view.findViewById(R.id.Amount);
            Free = view.findViewById(R.id.Free);
            sku = view.findViewById(R.id.sku);
            rlDeleteBrand = view.findViewById(R.id.rlDeleteBrand);
            rlOtherBrand = view.findViewById(R.id.rlOtherBrand);
            tvPos = view.findViewById(R.id.tvOBPos);
            ivCapture = view.findViewById(R.id.ivOBCapture);
            rvFiles = view.findViewById(R.id.rvOBFiles);

        }
    }


    public OtherBrandAdapter(List<QPS_Modal> mData, int rowLayout, Context context) {
        this.mData = mData;
        this.rowLayout = rowLayout;
        this.context = context;
        gson = new Gson();
        shared_common_pref = new Shared_Common_Pref(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MyViewHolder(view);
    }


    public void notifyData(List<QPS_Modal> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            builder = new AlertDialog.Builder(context);
            QPS_Modal Product_Details_Modal = mData.get(position);


            holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());
            holder.Amount.setText(CurrencySymbol+" " + Product_Details_Modal.getAmount());
            holder.etPrice.setText("" + Product_Details_Modal.getPrice());
            holder.Free.setText("" + Product_Details_Modal.getScheme());
            holder.sku.setText("" + Product_Details_Modal.getSku());
            holder.Qty.setText("" + Product_Details_Modal.getQty());
            holder.tvPos.setText("" + (position + 1));


            holder.Qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence charSequence, int start,
                                          int before, int count) {
                    try {
                        double price = holder.etPrice.getText().toString().equals("") ? 0 : Double.parseDouble(holder.etPrice.getText().toString());
                        int qty = charSequence.toString().equals("") ? 0 : Integer.parseInt(charSequence.toString());
                        holder.Amount.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(qty *
                                price));

                        mData.get(holder.getBindingAdapterPosition()).setQty(qty);
                        mData.get(holder.getBindingAdapterPosition()).setAmount(qty * price);
                    } catch (Exception e) {
                        Log.v(TAG + " qty:", e.getMessage());
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            holder.rlOtherBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        OtherBrandActivity.otherBrandActivity.showBrandDialog(position);


                    } catch (Exception e) {
                        Log.e("otherbrandAdapter: ", e.getMessage());
                    }
                }
            });

            holder.etPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    try {
                        int qty = holder.Qty.getText().toString().equals("") ? 0 : Integer.parseInt(holder.Qty.getText().toString());
                        double price = charSequence.toString().equals("") ? 0 : Double.parseDouble(charSequence.toString());

                        holder.Amount.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(qty * price));

                        mData.get(holder.getBindingAdapterPosition()).setAmount(qty * price);
                        mData.get(holder.getBindingAdapterPosition()).setPrice(price);
                    } catch (Exception e) {
                        Log.v(TAG + " :price: ", e.getMessage());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            holder.Free.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                    mData.get(holder.getBindingAdapterPosition()).setScheme((charSequence.toString()));


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            holder.sku.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                    if (charSequence.toString().equals("")) {

                    } else {
                        mData.get(holder.getBindingAdapterPosition()).setSku((charSequence.toString()));
                    }


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            holder.rlDeleteBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(holder.getBindingAdapterPosition());

                }
            });

            holder.ivCapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                        @Override
                        public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                            try {

                                List<String> list = new ArrayList<>();
                                File file = new File(fullPath);
                                Uri contentUri = Uri.fromFile(file);

                                if (mData.get(holder.getBindingAdapterPosition()).getFileUrls() != null && mData.get(holder.getBindingAdapterPosition()).getFileUrls().size() > 0)
                                    list = (mData.get(position).getFileUrls());
                                list.add(contentUri.toString());
                                mData.get(holder.getBindingAdapterPosition()).setFileUrls(list);

                                filesAdapter = new FilesAdapter(mData.get(position).getFileUrls(), R.layout.adapter_local_files_layout, context);
                                holder.rvFiles.setAdapter(filesAdapter);

                            } catch (Exception e) {
                                Log.v(TAG + ":capture:", e.getMessage());
                            }

                        }
                    });
                    Intent intent = new Intent(context, AllowancCapture.class);
                    intent.putExtra("allowance", "TAClaim");
                    context.startActivity(intent);
                }
            });


            filesAdapter = new FilesAdapter(mData.get(position).getFileUrls(), R.layout.adapter_local_files_layout, context);
            holder.rvFiles.setAdapter(filesAdapter);

        } catch (Exception e) {
            Log.e(TAG + "OTHERBRAND_Adapter ", e.getMessage());
        }
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void deleteItem(int pos) {

        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage("").setTitle("");

        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to delete this brand ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mData.remove(pos);
                       // removeFiles(qpsModalList, "" + pos + "ob~key");

                        if (mData.size() == 0) {
                            mData.add(new QPS_Modal("", "", "", 0, 0, 0, ""));
                        }

                        notifyDataSetChanged();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        //alert.setTitle("AlertDialogExample");
        alert.show();
    }
}

