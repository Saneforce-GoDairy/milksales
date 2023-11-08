package com.saneforce.godairy.SFA_Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.SalesReturnInvoiceAdapter;
import com.saneforce.godairy.SFA_Adapter.SalesReturnProductAdapter;
import com.saneforce.godairy.SFA_Adapter.SalesReturnProductAdapterSubmit;
import com.saneforce.godairy.SFA_Model_Class.SalesReturnInvoiceModel;
import com.saneforce.godairy.SFA_Model_Class.SalesReturnProductModel;
import com.saneforce.godairy.SFA_Model_Class.TaxModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesReturnActivity extends AppCompatActivity {
    ImageView home;
    TextView todayDate, outletName, outletAddress, selectedInvoice, back, submit, viewHistory, totalTV, quantitiesTV;
    RecyclerView recyclerView;
    RelativeLayout rlInvoice, submitLayout;
    LinearLayout returnTypeLayout, ProductsFinal;
    RadioGroup returnTypeRG;
    RadioButton full, partial;
    Context context;
    Common_Class common_class;
    ArrayList<SalesReturnInvoiceModel> invoiceList;
    ArrayList<SalesReturnProductModel> productList;
    SalesReturnProductAdapter salesReturnProductAdapter;
    String selectedInvoiceNumber = "";
    String submissionResult;
    boolean isOnSubmitScreen = false;
    int itemsCount, qtyCount;
    double retInvTotal;
    Shared_Common_Pref sharedCommonPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_return);
        home = findViewById(R.id.toolbar_home);
        todayDate = findViewById(R.id.tv_date);
        outletName = findViewById(R.id.outletName);
        outletAddress = findViewById(R.id.outletAddress);
        selectedInvoice = findViewById(R.id.invoice_status);
        recyclerView = findViewById(R.id.rv_products_list);
        rlInvoice = findViewById(R.id.rl_reference_invoice_number);
        back = findViewById(R.id.back);
        submit = findViewById(R.id.submit);
        returnTypeLayout = findViewById(R.id.returnTypeLayout);
        submitLayout = findViewById(R.id.submitLayout);
        full = findViewById(R.id.full);
        partial = findViewById(R.id.partial);
        returnTypeRG = findViewById(R.id.returnTypeRG);
        ProductsFinal = findViewById(R.id.ProductsFinal);
        viewHistory = findViewById(R.id.viewHistory);
        totalTV = findViewById(R.id.total);
        quantitiesTV = findViewById(R.id.quantities);
        context = this;
        common_class = new Common_Class(context);
        invoiceList = new ArrayList<>();
        productList = new ArrayList<>();
        sharedCommonPref = new Shared_Common_Pref(context);
        common_class.gotoHomeScreen(context, home);
        todayDate.setText("Date: " + new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
        outletName.setText(Shared_Common_Pref.OutletName.toUpperCase());
        outletAddress.setText(Shared_Common_Pref.OutletAddress);
        rlInvoice.setEnabled(false);
        rlInvoice.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_complementary_invoice, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView_complementary_invoice);
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            SalesReturnInvoiceAdapter adapter = new SalesReturnInvoiceAdapter(context, invoiceList);
            recyclerView1.setAdapter(adapter);
            TextView close = view.findViewById(R.id.close);
            AlertDialog dialog = builder.create();
            adapter.setItemSelect(invoiceNumber -> {
                selectedInvoiceNumber = invoiceNumber;
                returnTypeRG.clearCheck();
                recyclerView.setAdapter(null);
                recyclerView.setVisibility(View.INVISIBLE);
                submitLayout.setVisibility(View.INVISIBLE);
                selectedInvoice.setText(selectedInvoiceNumber);
                dialog.dismiss();
            });
            close.setOnClickListener(v1 -> dialog.dismiss());
            if (invoiceList.size() > 0) {
                dialog.show();
            } else {
                Toast.makeText(context, "No Invoices made Today", Toast.LENGTH_SHORT).show();
            }
        });
        selectedInvoice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LoadProductDetails(s.toString());
            }
        });

        back.setOnClickListener(v -> NavigateToProductScreen());

        submit.setOnClickListener(v -> {
            if (full.isChecked()) {
                SubmitSalesReturn("full");
            } else if (partial.isChecked() && isOnSubmitScreen) {
                SubmitSalesReturn("partial");
            } else if (partial.isChecked() && !isOnSubmitScreen) {
                NavigateToSubmitScreen();
            }
        });

        full.setOnClickListener(v -> {
            for (SalesReturnProductModel model : productList) {
                model.setRetQty(model.getInvQty());
                model.setRetAmount(model.getInvAmount());
                model.setRetTax(model.getInvTax());
            }
            submit.setText("Submit");
            recyclerView.setAdapter(null);
            recyclerView.setVisibility(View.INVISIBLE);
            submitLayout.setVisibility(View.VISIBLE);
            CalculateTotal();
        });

        partial.setOnClickListener(v -> {
            for (SalesReturnProductModel model : productList) {
                model.setRetQty(0);
                model.setRetAmount(0);
                model.setRetTax(0);
            }
            submit.setText("Proceed");
            submitLayout.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            salesReturnProductAdapter = new SalesReturnProductAdapter(context, productList);
            recyclerView.setAdapter(salesReturnProductAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            salesReturnProductAdapter.setCalculateTotal(this::CalculateTotal);
            CalculateTotal();
        });

        viewHistory.setOnClickListener(v -> startActivity(new Intent(context, SalesReturnHistoryActivity.class)));

        getCustomerInvoices();
    }

    private void CalculateTotal() {
        itemsCount = 0;
        qtyCount = 0;
        retInvTotal = 0;
        double mrp = 0;
        if (full.isChecked()) {
            for (SalesReturnProductModel model : productList) {
                itemsCount += 1;
                qtyCount += model.getRetQty();
                retInvTotal += model.getRetAmount();
            }
        } else if (partial.isChecked()) {
            for (SalesReturnProductModel model : productList) {
                if (model.getRetQty() > 0) {
                    itemsCount += 1;
                    qtyCount += model.getRetQty();
                    retInvTotal += model.getRetAmount();
                }
            }
        }
        String quantity = "Items: " + itemsCount + "     Qty: " + qtyCount;
        String total = HAPApp.CurrencySymbol + " " + new DecimalFormat("0.00").format(retInvTotal);
        quantitiesTV.setText(quantity);
        totalTV.setText(total);
    }

    private void NavigateToProductScreen() {
        ProductsFinal.setVisibility(View.GONE);
        rlInvoice.setEnabled(true);
        selectedInvoice.setEnabled(true);
        full.setEnabled(true);
        partial.setEnabled(true);
        submit.setText("Proceed");
        isOnSubmitScreen = false;
        if (partial.isChecked()) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            salesReturnProductAdapter = new SalesReturnProductAdapter(context, productList);
            recyclerView.setAdapter(salesReturnProductAdapter);
            salesReturnProductAdapter.setCalculateTotal(this::CalculateTotal);
        } else {
            recyclerView.setAdapter(null);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        back.setVisibility(View.GONE);
    }

    private void NavigateToSubmitScreen() {
        ArrayList<SalesReturnProductModel> NewList = new ArrayList<>();
        for (SalesReturnProductModel model : productList) {
            if (model.getRetQty() != 0) {
                NewList.add(model);
            }
        }
        if (!NewList.isEmpty()) {
            ProductsFinal.setVisibility(View.VISIBLE);
            isOnSubmitScreen = true;
            submit.setText("Submit");
            rlInvoice.setEnabled(false);
            full.setEnabled(false);
            selectedInvoice.setEnabled(false);
            partial.setEnabled(false);
            back.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            SalesReturnProductAdapterSubmit adapter = new SalesReturnProductAdapterSubmit(context, NewList);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(context, "No items selected to return", Toast.LENGTH_SHORT).show();
        }
    }

    private void SubmitSalesReturn(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to return " + type + " invoice?");
        builder.setCancelable(false);
        builder.setPositiveButton("SUBMIT", (dialog, which) -> SubmitToServer(type));
        builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void SubmitToServer(String type) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Submitting Sales Return...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            // Preparing Activity_Report_Head
            JSONObject Activity_Report_Head = new JSONObject();
            Activity_Report_Head.put("SF", Shared_Common_Pref.Sf_Code);
            Activity_Report_Head.put("dcr_activity_date", Common_Class.GetDate());

            // Preparing Activity_Doctor_Report
            JSONObject Activity_Doctor_Report = new JSONObject();
            Activity_Doctor_Report.put("NetAmount", retInvTotal); // Total
            Activity_Doctor_Report.put("stockist_code", sharedCommonPref.getvalue(Constants.Distributor_Id));
            Activity_Doctor_Report.put("doctor_code", Shared_Common_Pref.OutletCode);
            Activity_Doctor_Report.put("ref_invoice", selectedInvoiceNumber);
            Activity_Doctor_Report.put("orderValue", retInvTotal); // Subtotal
            Activity_Doctor_Report.put("No_Of_items", itemsCount);
            JSONArray TOT_TAX_details = new JSONArray();
            for (SalesReturnProductModel model : productList) {
                if (model.getRetQty() > 0) {
                    JSONObject tax_details_object = new JSONObject();
                    tax_details_object.put("Tax_Amt", model.getRetTax());
                    TOT_TAX_details.put(tax_details_object);
                }
            }
            Activity_Doctor_Report.put("TOT_TAX_details", TOT_TAX_details);

            // Preparing Order_Details Array
            JSONArray Order_Details = new JSONArray();
            for (SalesReturnProductModel model : productList) {
                if (model.getRetQty() > 0) {
                    String product_code = model.getProduct_Code();
                    int Product_Total_Qty = model.getRetQty();
                    double Rate = model.getPrice();
                    JSONArray taxDetails = new JSONArray();
                    for (TaxModel taxModel : model.getTaxList()) {
                        JSONObject object = new JSONObject();
                        object.put("Tax_Amt", taxModel.getTaxAmt());
                        object.put("Tax_Id", taxModel.getTaxCode());
                        object.put("Tax_Type", taxModel.getTaxName());
                        object.put("Tax_Val", taxModel.getTaxVal());
                        taxDetails.put(object);
                    }
                    JSONObject Order_Details_Object = new JSONObject();
                    Order_Details_Object.put("product_code", product_code);
                    Order_Details_Object.put("Product_Total_Qty", Product_Total_Qty);
                    Order_Details_Object.put("Rate", Rate);
                    Order_Details_Object.put("MRP", model.getMRP());
                    Order_Details_Object.put("UOM", model.getUOM());
                    Order_Details_Object.put("conv_fac", model.getCon_Fac());
                    Order_Details_Object.put("Product_Amount", model.getRetAmount());
                    Order_Details_Object.put("tax_Amount", model.getRetTax());
                    Order_Details_Object.put("TAX_details", taxDetails);
                    Order_Details.put(Order_Details_Object);
                }
            }

            // Preparing file_Details Array
            JSONArray file_Details = new JSONArray();
            JSONObject file_Details_Object = new JSONObject();
            file_Details_Object.put("SalesReturnImg", "");
            file_Details.put(file_Details_Object);

            // Preparing Master Object
            JSONObject masterObject = new JSONObject();
            masterObject.put("Activity_Report_Head", Activity_Report_Head);
            masterObject.put("Activity_Doctor_Report", Activity_Doctor_Report);
            masterObject.put("Order_Details", Order_Details);
            masterObject.put("file_Details", file_Details);

            // Preparing Data Array
            JSONArray data = new JSONArray();
            data.put(masterObject);

            submissionResult = "Sales return submission failed...";
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Map<String, String> params = new HashMap<>();
            params.put("axn", "save/sales_return");
            params.put("divisionCode", Shared_Common_Pref.Div_Code);
            params.put("Sf_code", Shared_Common_Pref.Sf_Code);
            Call<ResponseBody> call = apiInterface.getUniversalData(params , data.toString());
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            if (response.body() == null) {
                                Toast.makeText(context, "Response is Null", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String results = response.body().string();
                            JSONObject jsonObject = new JSONObject(results);
                            if (jsonObject.getBoolean("success")) {
                                submissionResult = "Sales return submitted successfully...";
                                progressDialog.dismiss();
                                ShowFinalResult();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "Error while parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            ShowFinalResult();
                        }
                    } else {
                        Toast.makeText(context, "Response Not Success", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        ShowFinalResult();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    ShowFinalResult();
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            ShowFinalResult();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ShowFinalResult() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(submissionResult);
        builder.setCancelable(false);
        builder.setPositiveButton("Dismiss", (dialog, which) -> {
            dialog.dismiss();
            finish();
        });
        builder.create().show();
    }

    private void LoadProductDetails(String invoice) {
        productList.clear();
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading products...");
        dialog.setCancelable(false);
        dialog.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_product_list_for_invoice");
        params.put("invoice", invoice);
        Call<ResponseBody> call = apiInterface.getUniversalData(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() == null) {
                            dialog.dismiss();
                            Toast.makeText(context, "Response is Null", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("isAlreadyRegistered")) {
                            returnTypeLayout.setVisibility(View.GONE);
                            submitLayout.setVisibility(View.GONE);
                            dialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Sales Return already submitted for the selected invoice. Please select any another invoice! ");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Dismiss", (dialog1, which) -> dialog1.dismiss());
                            builder.create().show();
                            return;
                        }
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            Log.e("status", "Request Result: \n" + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String Product_Code = jsonArray.getJSONObject(i).getString("Product_Code");
                                String Product_Name = jsonArray.getJSONObject(i).getString("Product_Name");
                                String Sale_Erp_Code = jsonArray.getJSONObject(i).getString("Sale_Erp_Code");
                                String UOM = jsonArray.getJSONObject(i).getString("Unit");
                                int invQty = jsonArray.getJSONObject(i).getInt("qty");
                                int Con_Fac = jsonArray.getJSONObject(i).getInt("Con_Fac");
                                int retQty = 0;
                                double MRP = jsonArray.getJSONObject(i).getDouble("MRP");
                                double Price = jsonArray.getJSONObject(i).getDouble("Price");
                                double invAmount = jsonArray.getJSONObject(i).getDouble("Amount");
                                double invTax = jsonArray.getJSONObject(i).getDouble("Tax");
                                double Margin = jsonArray.getJSONObject(i).getDouble("Margin");
                                double retAmount = 0;
                                double retTax = 0;
                                JSONArray taxArray = jsonArray.getJSONObject(i).getJSONArray("taxList");
                                ArrayList<TaxModel> taxList = new ArrayList<>();
                                for (int j = 0; j < taxArray.length(); j++) {
                                    String taxName = taxArray.getJSONObject(j).getString("Tax_Name");
                                    String taxCode = taxArray.getJSONObject(j).getString("Tax_Code");
                                    double taxAmt = taxArray.getJSONObject(j).getDouble("Tax_Amt");
                                    double taxVal = taxArray.getJSONObject(j).getDouble("Tax_Val");
                                    taxList.add(new TaxModel(taxName, taxCode, taxAmt, taxVal));
                                }
                                productList.add(new SalesReturnProductModel(Product_Code, Product_Name, Sale_Erp_Code, UOM, MRP, Price, invAmount, invTax, Margin, retAmount, retTax, invQty, Con_Fac, retQty, taxList));
                            }
                            returnTypeLayout.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(context, "Request does not reached the server", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error while parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Response Not Success", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void getCustomerInvoices() {
        invoiceList.clear();
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading products...");
        dialog.setCancelable(false);
        dialog.show();
        String outletCode = Shared_Common_Pref.OutletCode;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
//        params.put("axn", "get_latest_invoice_of_outlet"); // Enable this line to get last 30 days's invoices...
        params.put("axn", "get_today_invoice_of_outlet"); // Enable this line to get only today's invoices...
        params.put("outletCode", outletCode);
        Call<ResponseBody> call = apiInterface.getUniversalData(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() == null) {
                            Toast.makeText(context, "Response is Null", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray array = jsonObject.getJSONArray("response");
                            Log.e("invoiceList", array.toString());
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String invoiceNumber = object.getString("Trans_Inv_Slno");
                                String date = object.getJSONObject("Invoice_Date").getString("date");
                                String value = object.getString("Total");
                                invoiceList.add(new SalesReturnInvoiceModel(invoiceNumber, date, value));
                            }
                            rlInvoice.setEnabled(true);
                        } else {
                            Toast.makeText(context, "Request does not reached the server", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error while parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Response Not Success", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isOnSubmitScreen) {
            NavigateToProductScreen();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to go back?");
            builder.setCancelable(false);
            builder.setPositiveButton("YES", (dialog, which) -> super.onBackPressed());
            builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}