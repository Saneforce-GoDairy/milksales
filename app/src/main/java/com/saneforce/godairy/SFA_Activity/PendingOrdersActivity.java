package com.saneforce.godairy.SFA_Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.OnLiveUpdateListener;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.AdapterPendingOrder;
import com.saneforce.godairy.SFA_Adapter.CancelOrderAdapter;
import com.saneforce.godairy.SFA_Model_Class.CancelOrderModel;
import com.saneforce.godairy.SFA_Model_Class.ModelPendingOrder;
import com.saneforce.godairy.SFA_Model_Class.Product_Details_Modal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingOrdersActivity extends AppCompatActivity implements UpdateResponseUI {
    ImageView home;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView headText, canceledOrders, clscnclw, cancelMessage, confirmCancel;
    RelativeLayout rlCnclOrd;

    public static boolean CometoPending = false;
    String selectedInvoice;

    Context context = this;

    ArrayList<ModelPendingOrder> list = new ArrayList<>();
    ArrayList<CancelOrderModel> cancelOrderModels = new ArrayList<>();
    AdapterPendingOrder adapter;

    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        home = findViewById(R.id.toolbar_home);
        rlCnclOrd = findViewById(R.id.rlCnclOrd);
        clscnclw = findViewById(R.id.clscnclw);
        recyclerView = findViewById(R.id.rvDashboard);
        progressBar = findViewById(R.id.progressBar_pending_orders);
        headText = findViewById(R.id.headtext);
        cancelMessage = findViewById(R.id.cancelMessage);
        confirmCancel = findViewById(R.id.confirmCancel);
        canceledOrders = findViewById(R.id.canceledOrders);

        shared_common_pref = new Shared_Common_Pref(context);
        common_class = new Common_Class(PendingOrdersActivity.this);
        common_class.gotoHomeScreen(context, home);

        cancelOrderModels = new ArrayList<>();

        loadData();
        clscnclw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelMessage.setText("");
                rlCnclOrd.setVisibility(View.GONE);
            }
        });

        canceledOrders.setOnClickListener(v -> startActivity(new Intent(context, CancelledOrdersActivity.class)));
        
        loadCancelRemarks();
    }

    private void loadCancelRemarks() {
        cancelOrderModels.clear();
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_order_cancel_remarks");
        params.put("currentTime", Common_Class.GetDate());
        params.put("sfCode", Shared_Common_Pref.Sf_Code);
        params.put("State_Code", Shared_Common_Pref.StateCode);
        params.put("divisionCode", Shared_Common_Pref.Div_Code);
        params.put("distributorId", shared_common_pref.getvalue(Constants.Distributor_Id));
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getUniversalData(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() == null) {
                            Toast.makeText(context, "Error 1: Response is Null", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String result = response.body().string();
                        JSONObject myJsonObject = new JSONObject(result);
                        if (myJsonObject.getBoolean("success")) {
                            JSONArray array = myJsonObject.getJSONArray("response");
                            for (int i = 0; i < array.length(); i++) {
                                int id = array.getJSONObject(i).getInt("id");
                                String remarks = array.getJSONObject(i).getString("remarks");
                                cancelOrderModels.add(new CancelOrderModel(id, remarks));
                            }
                        } else {
                            Toast.makeText(context, "Error 2: Response Not Success", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error 3: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Error 4: Response Not Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error 5: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String StockistCode = shared_common_pref.getvalue(Constants.Distributor_Id);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_pending_orders_list");
        params.put("stockistCode", StockistCode);
        Call<ResponseBody> call = apiInterface.getPendingOrdersCount(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("success")) {
                            JSONArray array = object.getJSONArray("response");
                            for (int i = 0; i < array.length(); i++) {
                                Log.e("status", "\n" + array.getJSONObject(i).toString() + "\n");
                                String title = array.getJSONObject(i).getString("FranchiseName").toUpperCase();
                                String address = array.getJSONObject(i).getString("Address");
                                String mobile = array.getJSONObject(i).getString("Mobile");
                                String title2 = array.getJSONObject(i).getString("OutletName").toUpperCase();
                                String OutletCd = array.getJSONObject(i).getString("OutletId").toUpperCase();
                                String orderID = array.getJSONObject(i).getString("TransactionNo");
                                String date = array.getJSONObject(i).getString("Date_Time");
                                String total = array.getJSONObject(i).getString("TransactionAmt");
                                JSONArray jsonArray = array.getJSONObject(i).getJSONArray("Products");
                                StringBuilder builder = new StringBuilder();
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    builder.append(jsonArray.getJSONObject(j).getString("Product_Name"));
                                    builder.append(" x ");
                                    builder.append(jsonArray.getJSONObject(j).getInt("Quantity"));
                                    if (!((j+1) == jsonArray.length())) {
                                        builder.append(", ");
                                    }
                                }
                                String products = builder.toString();
                                list.add(new ModelPendingOrder(OutletCd,title, address, mobile, title2, orderID, date, products, total));
                            }
                            adapter = new AdapterPendingOrder(context, list);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(adapter);

                            adapter.setViewClicked((model, position) -> {
                                CometoPending = true;
                                Shared_Common_Pref.TransSlNo = model.getOrderID();
                                Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(context);
                                Shared_Common_Pref.Invoicetoorder = "1";
                                shared_common_pref.save(Constants.FLAG, "ORDER");
                                shared_common_pref.OutletCode = model.getOutletCode();
                                shared_common_pref.OutletName = model.getTitle2();
                                shared_common_pref.save(Constants.Retailor_Name_ERP_Code, model.getTitle2());
                                shared_common_pref.save(Constants.Retailor_PHNo, model.getMobile());
                                shared_common_pref.save(Constants.Retailor_Address, model.getAddress());
                                common_class.getDb_310Data(Constants.FreeSchemeDiscList, PendingOrdersActivity.this);
                                common_class.getDb_310Data(Constants.TAXList, PendingOrdersActivity.this);
                                common_class.ProgressdialogShow(1, "Loading Matrial Details");
                                common_class.getProductDetails(PendingOrdersActivity.this, new OnLiveUpdateListener() {
                                    @Override
                                    public void onUpdate(String mode) {
                                        common_class.ProgressdialogShow(0, "");
                                        Intent intent = new Intent(context, Print_Invoice_Activity.class);
                                        startActivity(intent);
                                    }
                                });
                            });
                            adapter.setCancelClicked((model, position) -> {
                                selectedInvoice = model.getOrderID();
                                AlertDialogBox.showDialog(PendingOrdersActivity.this, HAPApp.Title, "Do you want to cancel this order?" ,
                                        "Yes", "No", false, new AlertBox() {
                                            @Override
                                            public void PositiveMethod(DialogInterface dialog, int id) {
                                                rlCnclOrd.setVisibility(View.VISIBLE);
                                                cancelMessage.setOnClickListener(v -> {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                    View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
                                                    builder.setView(view);
                                                    builder.setCancelable(false);

                                                    TextView title = view.findViewById(R.id.title);
                                                    RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView);
                                                    TextView close = view.findViewById(R.id.close);

                                                    title.setText("Select Reason");
                                                    recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                                                    CancelOrderAdapter adapter = new CancelOrderAdapter(cancelOrderModels, context);
                                                    recyclerView1.setAdapter(adapter);
                                                    AlertDialog dialog1 = builder.create();
                                                    adapter.setItemSelected((model1, position1) -> {
                                                        cancelMessage.setText(model1.getRemark());
                                                        dialog1.dismiss();
                                                    });
                                                    close.setOnClickListener(v1 -> dialog1.dismiss());
                                                    if (cancelOrderModels.size() > 0) {
                                                        dialog1.show();
                                                    } else {
                                                        Toast.makeText(context, "No templates found", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                confirmCancel.setOnClickListener(v -> {
                                                    String message = cancelMessage.getText().toString().trim();
                                                    if (TextUtils.isEmpty(message)) {
                                                        Toast.makeText(context, "Please Select a Reason", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        cancelOrder(message, position);
                                                    }
                                                });
                                            }

                                            @Override
                                            public void NegativeMethod(DialogInterface dialog, int id) {

                                            }
                                        });
                            });
                        }
                    } catch (Exception e) {
                        Log.e("error", "Failed1: " + e.getMessage());
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("error", "Failed2: Response Failed");
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error", "Failed3: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelOrder(String message, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to cancel this order?");
        builder.setCancelable(false);
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setPositiveButton("Yes", (dialog, which) -> {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Map<String, String> params = new HashMap<>();
            params.put("axn", "update_order_cancel");
            params.put("remarks", message);
            params.put("orderId", selectedInvoice);
            params.put("currentTime", Common_Class.GetDate());
            params.put("sfCode", Shared_Common_Pref.Sf_Code);
            params.put("State_Code", Shared_Common_Pref.StateCode);
            params.put("divisionCode", Shared_Common_Pref.Div_Code);
            params.put("distributorId", shared_common_pref.getvalue(Constants.Distributor_Id));
            Call<ResponseBody> call = apiInterface.getUniversalData(params);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            if (response.body() == null) {
                                Toast.makeText(context, "Error 1: Response is Null", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String result = response.body().string();
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("success")) {
                                cancelMessage.setText("");
                                rlCnclOrd.setVisibility(View.GONE);
                                Toast.makeText(context, "Order cancelled successfully", Toast.LENGTH_SHORT).show();
                                list.remove(position);
                                adapter.notifyItemRemoved(position);
                            } else {
                                Toast.makeText(context, "Error 2: Response Not Success", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "Error 3: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Error 4: Response Not Success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Error 5: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CometoPending = false;
    }
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null && !apiDataResponse.equals("")) {

                switch (key) {
                    case Constants.FreeSchemeDiscList:
                        JSONObject jsonObject = new JSONObject(apiDataResponse);
                        if (jsonObject.getBoolean("success")) {
                            Gson gson = new Gson();
                            List<Product_Details_Modal> product_details_modalArrayList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");

                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    product_details_modalArrayList.add(new Product_Details_Modal(jsonObject1.getString("Product_Code"),
                                            jsonObject1.getString("Scheme"), jsonObject1.getString("Free"),
                                            Double.valueOf(jsonObject1.getString("Discount")), jsonObject1.getString("Discount_Type"),
                                            jsonObject1.getString("Package"), 0, jsonObject1.getString("Offer_Product"),
                                            jsonObject1.getString("Offer_Product_Name"), jsonObject1.getString("offer_product_unit")));
                                }
                            }
                            shared_common_pref.save(Constants.FreeSchemeDiscList, gson.toJson(product_details_modalArrayList));
                        } else {
                            shared_common_pref.clear_pref(Constants.FreeSchemeDiscList);
                        }
                        break;
                    case Constants.TAXList:
                        JSONObject jsonObjectTax = new JSONObject(apiDataResponse);
                        Log.v("TAX_PRIMARY:", apiDataResponse);

                        if (jsonObjectTax.getBoolean("success")) {
                            shared_common_pref.save(Constants.TAXList, apiDataResponse);

                        } else {
                            shared_common_pref.clear_pref(Constants.TAXList);

                        }
                        break;
                }

            }
        } catch (Exception e) {
            Log.v("Invoice History: ", e.getMessage());

        }
    }

}