package com.saneforce.godairy.SFA_Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.atom.atompaynetzsdk.PayActivity;
import com.google.gson.JsonObject;
import com.saneforce.godairy.CCAvenue.InitiatePaymentActivity;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.AdapterTwoClickListener;
import com.saneforce.godairy.Interface.AlertDialogClickListener;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.LocationEvents;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.JioMoney.PaymentWebView;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.PrimaryOrder_History_Adapter;
import com.saneforce.godairy.SFA_Model_Class.ModelPaymentCollection;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.common.LocationFinder;
import com.saneforce.godairy.databinding.PaymentCollectionBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentCollection extends AppCompatActivity implements UpdateResponseUI {
    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";
    public static final String Tag = "Pending Invoice";
    PaymentCollectionBinding binding;
    Shared_Common_Pref shared_common_pref;
    RecyclerView rvBillDets;
    TextView tvACBal, outlet_name, tvDistId;
    ArrayList<ModelPaymentCollection> list;
    AssistantClass assistantClass;
    Context context = this;
    boolean isMultipleSelected;
    Bills_Adapter adapter;
    Common_Class common_class;
    String PaymentMode;
    String NTTDATAMerchantId = "", NTTDATAPassword = "", NTTDATAReqHashKey = "", encSaltRequest = "", encSaltResponse = "", NTTDATAResHashKey = "", NTTDATAProdID = "";
    boolean isLive = false;
    private SharedPreferences CheckInDetails, UserDetails;
    String invoiceList = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PaymentCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

        common_class = new Common_Class(context);
        isMultipleSelected = false;
        list = new ArrayList<>();
        assistantClass = new AssistantClass(context);
        shared_common_pref = new Shared_Common_Pref(this);
        rvBillDets = findViewById(R.id.rvBillDets);
        tvACBal = findViewById(R.id.tvACBal);
        outlet_name = findViewById(R.id.outlet_name);
        tvDistId = findViewById(R.id.tvDistId);
        outlet_name.setText(shared_common_pref.getvalue(Constants.Distributor_name, ""));
        tvDistId.setText("" + shared_common_pref.getvalue(Constants.DistributorERP));
        getPndBills();
        common_class.gotoHomeScreen(context, binding.toolbarHome);
        binding.historyLL.setOnClickListener(v -> {
            Intent intent = new Intent(context, PaymentHistory.class);
            startActivity(intent);
        });
        binding.pay.setOnClickListener(v -> {
            double amt = 0;
            StringBuilder builder = new StringBuilder();
            for (ModelPaymentCollection item : list) {
                if (item.isChecked()) {
                    amt += item.getInvoicePAmt();
                    builder.append(item.getInvoice());
                    builder.append(",");
                }
            }
            invoiceList = builder.toString();
            if (amt > 0) {
                MakeOnlinePaymment(amt);
            } else {
                assistantClass.showAlertDialogWithDismiss("Amount should be bigger than Rs.0");
            }
        });
        common_class.getDb_310Data(Constants.PaymentMethod, this);

    }

    private void getPndBills() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> rptCall = apiInterface.getData310List("get/pripndbills",
                UserDetails.getString("Divcode", ""),
                shared_common_pref.getvalue(Constants.Distributor_Id), "", "", "", "");
        rptCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject res = new JSONObject(String.valueOf(response.body()));
                    Log.d("Pending Bills", String.valueOf(res));
                    JSONArray PndBills = res.getJSONArray("response");
                    double totAmt = 0.0;
                    list = new ArrayList<>();
                    for (int li = 0; li < PndBills.length(); li++) {
                        JSONObject item = PndBills.getJSONObject(li);
                        String InvoiceNo = item.optString("InvoiceNo");
                        String InvDate = item.optString("InvDate");
                        double BillAmount = item.optDouble("BillAmount");
                        double PendAmt = item.optDouble("PendAmt");
                        list.add(new ModelPaymentCollection(InvoiceNo, InvDate, BillAmount, PendAmt, false));
                        totAmt += item.getDouble("PendAmt");
                    }
                    tvACBal.setText(new DecimalFormat("##0.00").format(totAmt));
                    adapter = new Bills_Adapter(list, R.layout.ada_payprimarybills, PaymentCollection.this, new AdapterTwoClickListener() {
                        @Override
                        public void onClickOne(int position) {
                            String invoice = PndBills.optJSONObject(position).optString("InvoiceNo");
                            assistantClass.showAlertDialog(invoice, "Select a payment mode", true, "Online", "Offline", new AlertDialogClickListener() {
                                @Override
                                public void onPositiveButtonClick(DialogInterface dialog) {
                                    invoiceList = list.get(position).getInvoice();
                                    double amt = list.get(position).getInvoicePAmt();
                                    if (amt > 0) {
                                        MakeOnlinePaymment(amt);
                                    } else {
                                        assistantClass.showAlertDialogWithDismiss("Amount should be bigger than Rs.0");
                                    }
                                }

                                @Override
                                public void onNegativeButtonClick(DialogInterface dialog) {
                                    AlertDialog.Builder builders = new AlertDialog.Builder(context);
                                    builders.setCancelable(true);
                                    builders.setMessage("In offline payment, a challan will be created. You can make payment using this challan at your nearest Axis bank. Do you want to create a challan?");
                                    builders.setPositiveButton("Yes", (dialog1, which1) -> CreateChallan(invoice));
                                    builders.setNegativeButton("No", (dialog1, which1) -> dialog.dismiss());
                                    builders.create().show();
                                }
                            });
                        }

                        @Override
                        public void onClickTwo(int position) {
                            int checked = 0;
                            double amt = 0;
                            for (ModelPaymentCollection item : list) {
                                if (item.isChecked()) {
                                    checked++;
                                    amt += item.getInvoicePAmt();
                                }
                            }
                            isMultipleSelected = checked > 1;
                            binding.pay.setEnabled(isMultipleSelected);
                            if (isMultipleSelected) {
                                binding.billLayout.setVisibility(View.VISIBLE);
                                binding.noOfBills.setText(checked + " Bills selected");
                                binding.billAmt.setText(common_class.formatCurrency(amt));
                            } else {
                                binding.billLayout.setVisibility(View.GONE);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                    rvBillDets.setAdapter(adapter);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(Tag, String.valueOf(t));
            }
        });
    }

    private void CreateChallan(String invoice) {
        assistantClass.showProgressDialog("Please wait...", false);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "save_new_offline_trans");
        params.put("invoice", invoice);
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                assistantClass.dismissProgressDialog();
                Intent intent = new Intent(context, ChallanActivity.class);
                intent.putExtra("invoice", invoice);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(String error) {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithDismiss(error);
            }
        });
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (Constants.PaymentMethod.equals(key)) {
                JSONObject myObject = new JSONObject(apiDataResponse);
                JSONObject me = myObject.optJSONObject("response");
                PaymentMode = me.optString("PaymentGateway");
                if (PaymentMode.equalsIgnoreCase("nd")) {
                    getNTTDataCredentials();
                }
            }
        } catch (Exception ignored) {

        }
    }

    private void MakeOnlinePaymment(double amount) {
        if (PaymentMode == null) {
            Toast.makeText(this, "Can't get the payment mode", Toast.LENGTH_SHORT).show();
        } else if (PaymentMode.equalsIgnoreCase("CC")) {
            Intent intent = new Intent(this, InitiatePaymentActivity.class);
            intent.putExtra("Trans_Sl_No", Common_Class.GetDatemonthyearformat());
            intent.putExtra("totalValues", amount);
            startActivity(intent);
            finish();
        } else if (PaymentMode.equalsIgnoreCase("JM")) {
            Intent intent = new Intent(this, PaymentWebView.class);
            intent.putExtra("Trans_Sl_No", Common_Class.GetDatemonthyearformat());
            intent.putExtra("totalValues", amount);
            startActivity(intent);
            finish();
        } else if (PaymentMode.equalsIgnoreCase("nd")) {
            if (NTTDATAMerchantId.isEmpty()) {
                Toast.makeText(context, "Invalid NTTData Credentials...", Toast.LENGTH_SHORT).show();
            } else {
                StartNTTDataPayment(String.valueOf(amount));
            }
        }
    }

    private void StartNTTDataPayment(String amount) {
        if (invoiceList.isEmpty()) {
            Toast.makeText(context, "Invalid invoice number", Toast.LENGTH_SHORT).show();
        } else if (amount.equals("")) {
            Toast.makeText(context, "Invalid invoice amount", Toast.LENGTH_SHORT).show();
        } else {
            savePaymentInfo(amount);
        }
    }

    private void savePaymentInfo(String amount) {
        assistantClass.showProgressDialog("Initating...", false);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "saveNTTDATATransaction");
        params.put("invoice", invoiceList);
        params.put("invoiceAmt", amount);
        params.put("stockistCode", shared_common_pref.getvalue(Constants.Distributor_Id));
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                assistantClass.dismissProgressDialog();
                String intentId = jsonObject.optString("intentId");
                if (intentId.isEmpty()) {
                    assistantClass.showAlertDialogWithDismiss("Can't initiate transaction. Please try again later...");
                    return;
                }
                Intent newPayIntent = new Intent(PaymentCollection.this, PayActivity.class);
                newPayIntent.putExtra("merchantId", NTTDATAMerchantId);
                newPayIntent.putExtra("password", NTTDATAPassword);
                newPayIntent.putExtra("signature_request", NTTDATAReqHashKey);
                newPayIntent.putExtra("signature_response", NTTDATAResHashKey);
                newPayIntent.putExtra("prodid", NTTDATAProdID);
                newPayIntent.putExtra("enc_request", encSaltRequest);
                newPayIntent.putExtra("enc_response", encSaltResponse);
                newPayIntent.putExtra("salt_request", encSaltRequest);
                newPayIntent.putExtra("salt_response", encSaltResponse);
                newPayIntent.putExtra("isLive", isLive);
                newPayIntent.putExtra("amt", amount);

                newPayIntent.putExtra("txnid", intentId);
                newPayIntent.putExtra("custFirstName", shared_common_pref.getvalue(Constants.Distributor_name));
                newPayIntent.putExtra("customerMobileNo", shared_common_pref.getvalue(Constants.Distributor_phone));
                newPayIntent.putExtra("customerEmailID", "myemail@gmail.com");
                newPayIntent.putExtra("txncurr", "INR");
                newPayIntent.putExtra("custacc", "100000036600");
                newPayIntent.putExtra("udf1", "");
                newPayIntent.putExtra("udf2", "");
                newPayIntent.putExtra("udf3", "");
                newPayIntent.putExtra("udf4", "");
                newPayIntent.putExtra("udf5", "");
                startActivityForResult(newPayIntent, 1);
            }

            @Override
            public void onFailure(String error) {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithDismiss(error);
            }
        });
    }

    private void getNTTDataCredentials() {
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_nttdata_credentials");
        Common_Class.makeApiCall(context, params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Log.e("getNTTDataCredentials", "getNTTDataCredentials: " + jsonObject);
                    JSONObject object = jsonObject.getJSONObject("response");
                    NTTDATAMerchantId = object.optString("merchantID");
                    NTTDATAPassword = object.optString("password");
                    NTTDATAReqHashKey = object.optString("requestHashKey");
                    NTTDATAResHashKey = object.optString("responseHashKey");
                    encSaltRequest = object.optString("encSaltRequest");
                    encSaltResponse = object.optString("encSaltResponse");
                    NTTDATAProdID = object.optString("prodID");
                    isLive = object.optBoolean("isLive");
                } catch (JSONException ignored) {
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    public class Bills_Adapter extends RecyclerView.Adapter<Bills_Adapter.MyViewHolder> {
        private final int rowLayout;
        Context context;
        ArrayList<ModelPaymentCollection> list;
        AdapterTwoClickListener listener;

        public Bills_Adapter(ArrayList<ModelPaymentCollection> list, int rowLayout, Context context, AdapterTwoClickListener listener) {
            this.list = list;
            this.rowLayout = rowLayout;
            this.context = context;
            this.listener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {
                ModelPaymentCollection item = list.get(holder.getBindingAdapterPosition());
                holder.txInvNo.setText(item.getInvoice());
                holder.payNow.setEnabled(!isMultipleSelected);
                holder.txInvDate.setText(item.getInvoiceDate());
                holder.txInvAmt.setText("" + item.getInvoiceAmt());
                holder.txInvPAmt.setText("" + item.getInvoicePAmt());
                holder.payNow.setOnClickListener(v -> listener.onClickOne(holder.getBindingAdapterPosition()));
                holder.cbPndBill.setChecked(item.isChecked());
                holder.cbPndBill.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    item.setChecked(isChecked);
                    listener.onClickTwo(holder.getBindingAdapterPosition());
                });
            } catch (Exception e) {
                Log.e("Pri.Payment", "adapterProduct: " + e.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView txInvNo, txInvAmt, txInvDate, txInvPAmt, payNow;
            public CheckBox cbPndBill;

            public MyViewHolder(View view) {
                super(view);
                txInvNo = view.findViewById(R.id.txInvNo);
                txInvAmt = view.findViewById(R.id.txInvAmt);
                txInvDate = view.findViewById(R.id.txInvDate);
                txInvPAmt = view.findViewById(R.id.txInvPAmt);
                cbPndBill = view.findViewById(R.id.cbPndBill);
                payNow = view.findViewById(R.id.payNow);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", "requestCode: " + requestCode);
        Log.e("resultCode", "resultCode: " + resultCode);
        if (requestCode == 1) {
            try {
                Log.e("nttdataTransaction", data.getExtras().getString("response"));
            } catch (Exception ignored) {}
            if (resultCode == 2) {
                Toast.makeText(context, "Transaction cancelled by user...", Toast.LENGTH_SHORT).show();
            } else if (resultCode == 1) {
                JSONObject object = PreparePaymentSave(data);
                Map<String, String> params = new HashMap<>();
                params.put("axn", "save_nttdata_transaction");
                Common_Class.makeApiCall(context, params, object.toString(), new APIResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        ShowPaymentResult(data);
                    }

                    @Override
                    public void onFailure(String error) {
                        ShowPaymentResult(data);
                    }
                });
            } else {
                Toast.makeText(context, "Transaction failed...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ShowPaymentResult(Intent data) {
        try {
            JSONObject jsonObject = new JSONObject(data.getExtras().getString("response"));
            String merchTxnId = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("merchDetails")
                    .optString("merchTxnId");

            String totalAmount = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("payDetails")
                    .optString("totalAmount");

            String txnCompleteDate = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("payDetails")
                    .optString("txnCompleteDate");

            String message = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("responseDetails")
                    .optString("message");

            Intent intent = new Intent(context, PaymentResult.class);
            intent.putExtra("status", message);
            intent.putExtra("transactionId", merchTxnId);
            intent.putExtra("transactionAmount", totalAmount);
            intent.putExtra("transactionDate", txnCompleteDate);
            startActivity(intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject PreparePaymentSave(Intent data) {
        JSONObject requestBody = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(data.getExtras().getString("response"));
            String merchId = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("merchDetails")
                    .optString("merchId");

            String merchTxnId = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("merchDetails")
                    .optString("merchTxnId");

            String merchTxnDate = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("merchDetails")
                    .optString("merchTxnDate");

            String atomTxnId = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("payDetails")
                    .optString("atomTxnId");

            String totalAmount = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("payDetails")
                    .optString("totalAmount");

            String custAccNo = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("payDetails")
                    .optString("custAccNo");

            String txnInitDate = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("payDetails")
                    .optString("txnInitDate");

            String txnCompleteDate = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("payDetails")
                    .optString("txnCompleteDate");

            String otsBankId = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("payModeSpecificData")
                    .getJSONObject("bankDetails")
                    .optString("otsBankId");

            String otsBankName = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("payModeSpecificData")
                    .getJSONObject("bankDetails")
                    .optString("otsBankName");

            String bankTxnId = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("payModeSpecificData")
                    .getJSONObject("bankDetails")
                    .optString("bankTxnId");

            String cardType = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("payModeSpecificData")
                    .getJSONObject("bankDetails")
                    .optString("cardType");

            String cardMaskNumber = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("payModeSpecificData")
                    .getJSONObject("bankDetails")
                    .optString("cardMaskNumber");

            String statusCode = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("responseDetails")
                    .optString("statusCode");

            String message = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("responseDetails")
                    .optString("message");

            String description = jsonObject.getJSONObject("payInstrument")
                    .getJSONObject("responseDetails")
                    .optString("description");

            requestBody.put("stockistCode", shared_common_pref.getvalue(Constants.Distributor_Id));
            requestBody.put("stockistName", shared_common_pref.getvalue(Constants.Distributor_name));
            requestBody.put("merchId", merchId);
            requestBody.put("merchTxnId", merchTxnId);
            requestBody.put("merchTxnDate", merchTxnDate);
            requestBody.put("atomTxnId", atomTxnId);
            requestBody.put("totalAmount", totalAmount);
            requestBody.put("custAccNo", custAccNo);
            requestBody.put("txnInitDate", txnInitDate);
            requestBody.put("txnCompleteDate", txnCompleteDate);
            requestBody.put("otsBankId", otsBankId);
            requestBody.put("otsBankName", otsBankName);
            requestBody.put("bankTxnId", bankTxnId);
            requestBody.put("cardType", cardType);
            requestBody.put("cardMaskNumber", cardMaskNumber);
            requestBody.put("statusCode", statusCode);
            requestBody.put("message", message);
            requestBody.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestBody;
    }
}