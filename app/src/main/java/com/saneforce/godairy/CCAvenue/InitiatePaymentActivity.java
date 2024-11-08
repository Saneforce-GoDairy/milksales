package com.saneforce.godairy.CCAvenue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class InitiatePaymentActivity extends AppCompatActivity {
    Map<String, ArrayList<CardTypeDTO>> cardsList = new LinkedHashMap<>();
    ArrayList<PaymentOptionDTO> payOptionList = new ArrayList<>();
    ArrayList<EMIOptionDTO> emiOptionList = new ArrayList<>();
    String selectedPaymentOption;
    CardTypeDTO selectedCardType;
    int counter;
    Context context = this;
    Activity activity = this;
    String vJsonStr;
    ImageView toolbar_home;
    Common_Class common_class;
    private EditText billingName, billingAddress, billingCountry, billingState, billingCity, billingZip, billingTel, billingEmail,
            deliveryName, deliveryAddress, deliveryCountry, deliveryState, deliveryCity, deliveryZip,
            deliveryTel, cardNumber, cardCvv, expiryMonth, expiryYear, issuingBank, vCardCVV;
    private CheckBox saveCard;
    private Map<String, String> paymentOptions = new LinkedHashMap<>();
    private TextView orderId;
    private JSONObject jsonRespObj;
    private String emiPlanId, emiTenureId, amount, currency, cardName, allowedBins;
    String totalValues, Trans_Sl_No;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate_payment);

        billingName = findViewById(R.id.billingName);
        billingAddress = findViewById(R.id.billingAddress);
        billingCountry = findViewById(R.id.billingCountry);
        billingState = findViewById(R.id.billingState);
        billingCity = findViewById(R.id.billingCity);
        billingZip = findViewById(R.id.billingZip);
        billingTel = findViewById(R.id.billingTel);
        billingEmail = findViewById(R.id.billingEmail);
        deliveryName = findViewById(R.id.deliveryName);
        deliveryAddress = findViewById(R.id.deliveryAddress);
        deliveryCountry = findViewById(R.id.deliveryCountry);
        deliveryState = findViewById(R.id.deliveryState);
        deliveryCity = findViewById(R.id.deliveryCity);
        deliveryZip = findViewById(R.id.deliveryZip);
        deliveryTel = findViewById(R.id.deliveryTel);
        orderId = findViewById(R.id.orderId);
        cardNumber = findViewById(R.id.cardNumber);
        cardCvv = findViewById(R.id.cardCVV);
        expiryYear = findViewById(R.id.expiryYear);
        expiryMonth = findViewById(R.id.expiryMonth);
        issuingBank = findViewById(R.id.issuingBank);
        saveCard = findViewById(R.id.saveCard);
        vCardCVV = findViewById(R.id.vCardCVV);
        toolbar_home = findViewById(R.id.toolbar_home);

        common_class = new Common_Class(this);
        common_class.gotoHomeScreen(this, toolbar_home);

//        totalValues = String.valueOf(getIntent().getDoubleExtra("totalValues", 1.00));
//        Trans_Sl_No = getIntent().getStringExtra("Trans_Sl_No");

        totalValues = "1.00";
        Trans_Sl_No = String.valueOf(ServiceUtility.randInt(111111111, 999999999));

        orderId.setText(Trans_Sl_No);

        fetch_payment_details();
    }

    public void onClick(View view) {
        // Mandatory parameters. Other parameters can be added if required.
        String vRsaKeyUrl = ServiceUtility.chkNull(Constants.RSA_URL).toString().trim();
        if (selectedCardType != null && selectedPaymentOption != null && !Trans_Sl_No.equals("") && !vRsaKeyUrl.equals("")) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(AvenuesParams.ORDER_ID, Trans_Sl_No.trim());
            intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(Constants.ACCESS_CODE_PAYMENT).toString().trim());
            intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(Constants.MERCHANT_ID_PAYMENT).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_NAME, ServiceUtility.chkNull(billingName.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_ADDRESS, ServiceUtility.chkNull(billingAddress.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_COUNTRY, ServiceUtility.chkNull(billingCountry.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_STATE, ServiceUtility.chkNull(billingState.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_CITY, ServiceUtility.chkNull(billingCity.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_ZIP, ServiceUtility.chkNull(billingZip.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_TEL, ServiceUtility.chkNull(billingTel.getText()).toString().trim());
            intent.putExtra(AvenuesParams.BILLING_EMAIL, ServiceUtility.chkNull(billingEmail.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_NAME, ServiceUtility.chkNull(deliveryName.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, ServiceUtility.chkNull(deliveryAddress.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, ServiceUtility.chkNull(deliveryCountry.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_STATE, ServiceUtility.chkNull(deliveryState.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_CITY, ServiceUtility.chkNull(deliveryCity.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_ZIP, ServiceUtility.chkNull(deliveryZip.getText()).toString().trim());
            intent.putExtra(AvenuesParams.DELIVERY_TEL, ServiceUtility.chkNull(deliveryTel.getText()).toString().trim());

            String cardCVV = ServiceUtility.chkNull(cardCvv.getText()).toString().trim();
            if (((LinearLayout) findViewById(R.id.vCardCVVCont)).getVisibility() == View.VISIBLE && vCardCVV.getVisibility() == View.VISIBLE) {
                cardCVV = ServiceUtility.chkNull(vCardCVV.getText()).toString().trim();
            }
            intent.putExtra(AvenuesParams.CVV, cardCVV);
            intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(Constants.REDIRECT_URL).toString().trim());
            intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(Constants.CANCEL_URL).toString().trim());
            intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(Constants.RSA_URL).toString().trim());
            intent.putExtra(AvenuesParams.PAYMENT_OPTION, selectedPaymentOption);
            intent.putExtra(AvenuesParams.CARD_NUMBER, ServiceUtility.chkNull(cardNumber.getText()).toString().trim());
            intent.putExtra(AvenuesParams.EXPIRY_YEAR, ServiceUtility.chkNull(expiryYear.getText()).toString().trim());
            intent.putExtra(AvenuesParams.EXPIRY_MONTH, ServiceUtility.chkNull(expiryMonth.getText()).toString().trim());
            intent.putExtra(AvenuesParams.ISSUING_BANK, ServiceUtility.chkNull(issuingBank.getText()).toString().trim());
            if (selectedPaymentOption.equals("OPTEMI")) {
                if (ServiceUtility.chkNull(cardNumber.getText()).toString().trim().equals("")) {
                    showToast("Card Number is mandatory for EMI payments");
                    return;
                } else if (ServiceUtility.chkNull(cardCvv.getText()).toString().trim().equals("")) {
                    showToast("Card CVV is mandatory for EMI payments");
                    return;
                } else if (ServiceUtility.chkNull(expiryMonth.getText()).toString().trim().equals("")) {
                    showToast("Expiry month is mandatory for EMI payments");
                    return;
                } else if (ServiceUtility.chkNull(expiryYear.getText()).toString().trim().equals("")) {
                    showToast("Expiry year is mandatory for EMI payments");
                    return;
                }
                /* validation for bin nos */
                if (!ServiceUtility.chkNull(allowedBins).equals("") && !ServiceUtility.chkNull(allowedBins).equals("allcards")) {
                    String cardBin = cardNumber.getText().toString().substring(0, 6);
                    boolean valid = false;
                    String[] bins = allowedBins.split(" ");
                    for (int i = 0; i < bins.length; i++) {
                        if (bins[i].equals(cardBin)) {
                            valid = true;
                            break;
                        }
                    }
                    if (!valid) {
                        showToast("This card is not allowed for the selected EMI option");
                        return;
                    }
                }
                intent.putExtra(AvenuesParams.EMI_PLAN_ID, ServiceUtility.chkNull(emiPlanId).toString().trim());
                intent.putExtra(AvenuesParams.EMI_TENURE_ID, ServiceUtility.chkNull(emiTenureId).toString().trim());
                intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency).toString().trim());
                intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(amount).toString().trim());
                intent.putExtra(AvenuesParams.CARD_TYPE, "CRDC");
                intent.putExtra(AvenuesParams.CARD_NAME, cardName);
            } else {
                intent.putExtra(AvenuesParams.CARD_TYPE, selectedCardType.getCardType());
                intent.putExtra(AvenuesParams.CARD_NAME, selectedCardType.getCardName());
                intent.putExtra(AvenuesParams.DATA_ACCEPTED_AT, selectedCardType.getDataAcceptedAt() != null ? (selectedCardType.getDataAcceptedAt().equals("CCAvenue") ? "Y" : "N") : null);
                intent.putExtra(AvenuesParams.CUSTOMER_IDENTIFIER, Constants.CUSTOMER_ID_PAYMENT);
                intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(Constants.CURRENCY_PAYMENT).toString().trim());
                intent.putExtra(AvenuesParams.AMOUNT, totalValues);
            }
            if (saveCard.isChecked())
                intent.putExtra(AvenuesParams.SAVE_CARD, "Y");
            startActivity(intent);
            finish();
        } else {
            showToast("Amount/Currency/Access code/Merchant Id & RSA key Url are mandatory."); //More validations can be added as per requirement.
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    public void fetch_payment_details() {
        LoadingDialog.showLoadingDialog(context, "Loading...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(WebViewActivity.this,response,Toast.LENGTH_LONG).show();
                        LoadingDialog.cancelLoading();
                        // vResponse = response;
                        // new RenderView().execute();

                        if (response != null && !response.equals("")) {
                            vJsonStr = response;
                            new GetData().execute();

                        } else {
                            Log.e("ServiceHandler", "Couldn't get any data from the url");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LoadingDialog.cancelLoading();
                        //Toast.makeText(WebViewActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AvenuesParams.COMMAND, Constants.COMMAND);
                params.put(AvenuesParams.ACCESS_CODE, Constants.ACCESS_CODE_PAYMENT);
                params.put(AvenuesParams.CURRENCY, Constants.CURRENCY_PAYMENT);
                params.put(AvenuesParams.AMOUNT, totalValues);
                params.put(AvenuesParams.CUSTOMER_IDENTIFIER, Constants.CUSTOMER_ID_PAYMENT);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            LoadingDialog.showLoadingDialog(context, "Loading...");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance


            // Making a request to url and getting response


            Log.d("Response: ", "> " + vJsonStr);

            if (vJsonStr != null && !vJsonStr.equals("")) {
                try {
                    jsonRespObj = new JSONObject(vJsonStr);
                    if (jsonRespObj != null) {
                        if (jsonRespObj.getString("payOptions") != null) {
                            JSONArray vPayOptsArr = new JSONArray(jsonRespObj.getString("payOptions"));
                            for (int i = 0; i < vPayOptsArr.length(); i++) {
                                JSONObject vPaymentOption = vPayOptsArr.getJSONObject(i);
                                if (vPaymentOption.getString("payOpt").equals("OPTIVRS")) continue;
                                payOptionList.add(new PaymentOptionDTO(vPaymentOption.getString("payOpt"), vPaymentOption.getString("payOptDesc").toString()));//Add payment option only if it includes any card
                                paymentOptions.put(vPaymentOption.getString("payOpt"), vPaymentOption.getString("payOptDesc"));
                                try {
                                    JSONArray vCardArr = new JSONArray(vPaymentOption.getString("cardsList"));
                                    if (vCardArr.length() > 0) {
                                        cardsList.put(vPaymentOption.getString("payOpt"), new ArrayList<CardTypeDTO>()); //Add a new Arraylist
                                        for (int j = 0; j < vCardArr.length(); j++) {
                                            JSONObject card = vCardArr.getJSONObject(j);
                                            try {
                                                CardTypeDTO cardTypeDTO = new CardTypeDTO();
                                                cardTypeDTO.setCardName(card.getString("cardName"));
                                                cardTypeDTO.setCardType(card.getString("cardType"));
                                                cardTypeDTO.setPayOptType(card.getString("payOptType"));
                                                cardTypeDTO.setDataAcceptedAt(card.getString("dataAcceptedAt"));
                                                cardTypeDTO.setStatus(card.getString("status"));

                                                cardsList.get(vPaymentOption.getString("payOpt")).add(cardTypeDTO);
                                            } catch (Exception e) {
                                                Log.e("ServiceHandler", "Error parsing cardType", e);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e("ServiceHandler", "Error parsing payment option", e);
                                }
                            }
                        }
                        if ((jsonRespObj.getString("EmiBanks") != null && jsonRespObj.getString("EmiBanks").length() > 0) &&
                                (jsonRespObj.getString("EmiPlans") != null && jsonRespObj.getString("EmiPlans").length() > 0)) {
                            paymentOptions.put("OPTEMI", "Credit Card EMI");
                            payOptionList.add(new PaymentOptionDTO("OPTEMI", "Credit Card EMI"));
                        }
                    }
                } catch (JSONException e) {
                    Log.e("ServiceHandler", "Error fetching data from server", e);
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            LoadingDialog.cancelLoading();

            try {
                // bind adapter to spinner
                final Spinner payOpt = (Spinner) findViewById(R.id.payopt);
                PayOptAdapter payOptAdapter = new PayOptAdapter(activity, R.layout.item_spinner, payOptionList);
                payOpt.setAdapter(payOptAdapter);

                //set a listener for selected items in the spinner
                payOpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView parent, View view, int position, long id) {
                        ((LinearLayout) findViewById(R.id.vCardCVVCont)).setVisibility(View.GONE);

                        selectedPaymentOption = payOptionList.get(position).getPayOptId();
                        String vCustPayments = null;
                        try {
                            vCustPayments = jsonRespObj.getString("CustPayments");
                        } catch (Exception e) {
                        }

                        if (counter != 0 || vCustPayments == null) {
                            LinearLayout ll = (LinearLayout) findViewById(R.id.cardDetails);
                            if (selectedPaymentOption.equals("OPTDBCRD") ||
                                    selectedPaymentOption.equals("OPTCRDC")) {
                                ll.setVisibility(View.VISIBLE);
                            } else {
                                ll.setVisibility(View.GONE);
                            }
                        }


                        if (selectedPaymentOption.equals("OPTEMI")) {
                            ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.VISIBLE);
                            ((CheckBox) findViewById(R.id.saveCard)).setVisibility(View.GONE);
                            if (((LinearLayout) findViewById(R.id.vaultCont)) != null)
                                ((LinearLayout) findViewById(R.id.vaultCont)).setVisibility(View.GONE);

                            ((Spinner) findViewById(R.id.cardtype)).setVisibility(View.GONE);
                            ((TextView) findViewById(R.id.cardtypetv)).setVisibility(View.GONE);

                            ((LinearLayout) findViewById(R.id.emiDetails)).removeAllViews();

                            ((LinearLayout) findViewById(R.id.emiOptions)).setVisibility(View.VISIBLE);
                            try {
                                JSONArray vEmiBankArr = new JSONArray(jsonRespObj.getString("EmiBanks"));
                                for (int i = 0; i < vEmiBankArr.length(); i++) {
                                    JSONObject vEmiBank = vEmiBankArr.getJSONObject(i);

                                    EMIOptionDTO vEmiOptionDTO = new EMIOptionDTO();
                                    vEmiOptionDTO.setGtwId(vEmiBank.getString("gtwId"));
                                    vEmiOptionDTO.setGtwName(vEmiBank.getString("gtwName"));
                                    vEmiOptionDTO.setSubventionPaidBy(vEmiBank.getString("subventionPaidBy"));
                                    vEmiOptionDTO.setTenureMonths(vEmiBank.getString("tenureMonths"));
                                    vEmiOptionDTO.setProcessingFeeFlat(vEmiBank.getString("processingFeeFlat"));
                                    vEmiOptionDTO.setProcessingFeePercent(vEmiBank.getString("processingFeePercent"));
                                    vEmiOptionDTO.setCcAvenueFeeFlat(vEmiBank.getString("ccAvenueFeeFlat"));
                                    vEmiOptionDTO.setCcAvenueFeePercent(vEmiBank.getString("ccAvenueFeePercent"));
                                    vEmiOptionDTO.setTenureData(vEmiBank.getString("tenureData"));
                                    vEmiOptionDTO.setPlanId(vEmiBank.getString("planId"));
                                    vEmiOptionDTO.setAccountCurrName(vEmiBank.getString("accountCurrName"));
                                    vEmiOptionDTO.setEmiPlanId(vEmiBank.getString("emiPlanId"));
                                    vEmiOptionDTO.setMidProcesses(vEmiBank.getString("midProcesses"));
                                    vEmiOptionDTO.setBins(vEmiBank.getString("BINs"));

                                    JSONArray vEmiPlanArr = new JSONArray(jsonRespObj.getString("EmiPlans"));
                                    for (int j = 0; j < vEmiPlanArr.length(); j++) {
                                        JSONObject vEmiPlan = vEmiPlanArr.getJSONObject(j);

                                        if (vEmiBank.getString("planId").equals(vEmiPlan.getString("planId"))) {
                                            EMIPlansDTO vEmiPlansDTO = new EMIPlansDTO();
                                            vEmiPlansDTO.setGtwId(vEmiPlan.getString("gtwId"));
                                            vEmiPlansDTO.setGtwName(vEmiPlan.getString("gtwName"));
                                            vEmiPlansDTO.setSubventionPaidBy(vEmiBank.getString("subventionPaidBy"));
                                            vEmiPlansDTO.setTenureMonths(vEmiPlan.getString("tenureMonths"));
                                            vEmiPlansDTO.setProcessingFeeFlat(vEmiPlan.getString("processingFeeFlat"));
                                            vEmiPlansDTO.setProcessingFeePercent(vEmiPlan.getString("processingFeePercent"));
                                            vEmiPlansDTO.setCcAvenueFeeFlat(vEmiPlan.getString("ccAvenueFeeFlat"));
                                            vEmiPlansDTO.setCcAvenueFeePercent(vEmiPlan.getString("ccAvenueFeePercent"));
                                            vEmiPlansDTO.setTenureData(vEmiPlan.getString("tenureData"));
                                            vEmiPlansDTO.setPlanId(vEmiPlan.getString("planId"));
                                            vEmiPlansDTO.setAccountCurrName(vEmiPlan.getString("accountCurrName"));
                                            vEmiPlansDTO.setEmiPlanId(vEmiPlan.getString("emiPlanId"));
                                            vEmiPlansDTO.setTenureId(vEmiPlan.getString("tenureId"));
                                            vEmiPlansDTO.setMidProcesses(vEmiPlan.getString("midProcesses"));
                                            vEmiPlansDTO.setEmiAmount(vEmiPlan.getString("emiAmount"));
                                            vEmiPlansDTO.setTotal(vEmiPlan.getString("total"));
                                            vEmiPlansDTO.setEmiProcessingFee(vEmiPlan.getString("emiProcessingFee"));
                                            vEmiPlansDTO.setTenureAmtGreaterThan(vEmiPlan.getString("tenureAmtGreaterThan"));
                                            vEmiPlansDTO.setCurrency(vEmiPlan.getString("currency"));

                                            vEmiOptionDTO.getEmiPlansDTO().add(vEmiPlansDTO);
                                        }
                                    }
                                    emiOptionList.add(vEmiOptionDTO);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Spinner emiOption = (Spinner) findViewById(R.id.emiBanks);
                            EMIAdapter emiAdapter = new EMIAdapter(activity, R.layout.item_spinner, emiOptionList);
                            emiOption.setAdapter(emiAdapter);

                            emiOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                    EMIOptionDTO vEmiOptionDTO = (EMIOptionDTO) emiOptionList.get(position);

                                    emiPlanId = vEmiOptionDTO.getPlanId();
                                    allowedBins = vEmiOptionDTO.getBins();

                                    String[] midProcessCards = vEmiOptionDTO.getMidProcesses().split("\\|");
                                    final ArrayList<String> cardNameList = new ArrayList<String>();
                                    for (int i = 0; i < midProcessCards.length; i++)
                                        cardNameList.add(midProcessCards[i]);
                                    Spinner emiCardName = (Spinner) findViewById(R.id.emiCardName);
                                    CardNameAdapter cardNameAdapter = new CardNameAdapter(activity, R.layout.item_spinner, cardNameList);
                                    emiCardName.setAdapter(cardNameAdapter);

                                    emiCardName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                            cardName = cardNameList.get(position);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });

                                    final LinearLayout vEmiDetailsCont = (LinearLayout) findViewById(R.id.emiDetails);
                                    vEmiDetailsCont.removeAllViews();

                                    RadioGroup rg = new RadioGroup(context);
                                    rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                            try {
                                                RadioButton rb = (RadioButton) findViewById(checkedId);

                                                EMIPlansDTO vEmiPlanDTO = (EMIPlansDTO) rb.getTag();

                                                emiTenureId = vEmiPlanDTO.getTenureId();
                                                amount = vEmiPlanDTO.getEmiAmount();
                                                currency = vEmiPlanDTO.getCurrency();

                                                TextView vProcFee = new TextView(context);
                                                vProcFee.setId(R.id.procFee);
                                                if (ServiceUtility.chkNull(vEmiPlanDTO.getSubventionPaidBy()).equals("Customer")) {
                                                    if ((TextView) findViewById(R.id.procFee) != null)
                                                        vEmiDetailsCont.removeView((TextView) findViewById(R.id.procFee));

                                                    vProcFee.setText("Processing Fee: " + vEmiPlanDTO.getCurrency() + " " + vEmiPlanDTO.getEmiProcessingFee() + "(Processing fee will be charged only on the first EMI.)");
                                                    vEmiDetailsCont.addView(vProcFee);
                                                } else {
                                                    vEmiDetailsCont.removeView((TextView) findViewById(R.id.procFee));
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    Iterator<EMIPlansDTO> vEmiPlanIt = vEmiOptionDTO.getEmiPlansDTO().iterator();
                                    while (vEmiPlanIt.hasNext()) {
                                        EMIPlansDTO vEmiPlansDTO = vEmiPlanIt.next();

                                        RadioButton rb = new RadioButton(context);

                                        String processingFee = !ServiceUtility.chkNull(vEmiPlansDTO.getProcessingFeePercent()).equals("") ?
                                                (vEmiPlansDTO.getProcessingFeePercent() + "% p.a.") : (vEmiPlansDTO.getProcessingFeeFlat() + " flat p.a.");
                                        rb.setText(vEmiPlansDTO.getTenureMonths() + " EMIs.@ " + processingFee + " - " + vEmiPlansDTO.getCurrency()
                                                + " " + (Math.round(Double.parseDouble(vEmiPlansDTO.getEmiAmount()) * 100.0) / 100.0) + " (Total: " +
                                                vEmiPlansDTO.getCurrency() + " " + (Math.round(Double.parseDouble(vEmiPlansDTO.getTotal()) * 100.0) / 100.0) + ")");
                                        rb.setTag(vEmiPlansDTO);
                                        rg.addView(rb);
                                    }
                                    vEmiDetailsCont.addView(rg);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } else {
                            if (!selectedPaymentOption.equalsIgnoreCase("OPTUPI")) {
                                ((Spinner) findViewById(R.id.cardtype)).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.cardtypetv)).setVisibility(View.VISIBLE);
                                ((CheckBox) findViewById(R.id.saveCard)).setVisibility(View.VISIBLE);
                                ((LinearLayout) findViewById(R.id.emiOptions)).setVisibility(View.GONE);

                                Spinner cardType = (Spinner) findViewById(R.id.cardtype);
                                ArrayList<CardTypeDTO> data = cardsList.get(selectedPaymentOption);
                                if (data != null) {
                                    CardAdapter cardTypeAdapter = new CardAdapter(activity, R.layout.item_spinner, data);
                                    cardType.setAdapter(cardTypeAdapter);

                                    cardType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                            ((LinearLayout) findViewById(R.id.vCardCVVCont)).setVisibility(View.GONE);
                                            selectedCardType = cardsList.get(selectedPaymentOption).get(position);
                                            if (ServiceUtility.chkNull(selectedPaymentOption).equals("OPTCRDC")
                                                    || ServiceUtility.chkNull(selectedPaymentOption).equals("OPTDBCRD")) {
                                                if (!ServiceUtility.chkNull(selectedCardType.getDataAcceptedAt()).equals("CCAvenue")) {
                                                    ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.GONE);
                                                    cardNumber.setText("");
                                                    expiryMonth.setText("");
                                                    expiryYear.setText("");
                                                    cardCvv.setText("");
                                                    issuingBank.setText("");
                                                } else {
                                                    //Setting default values here
                                                    cardNumber.setText("4111111111111111");
                                                    expiryMonth.setText("07");
                                                    expiryYear.setText("2027");
                                                    cardCvv.setText("328");
                                                    issuingBank.setText("State Bank of India");
                                                    ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });
                                } else {
                                    Toast.makeText(context, "No Payments available for the selected Payment Option...", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                ((Spinner) findViewById(R.id.cardtype)).setVisibility(View.GONE);
                                ((TextView) findViewById(R.id.cardtypetv)).setVisibility(View.GONE);
                                ((CheckBox) findViewById(R.id.saveCard)).setVisibility(View.GONE);
                                ((LinearLayout) findViewById(R.id.emiOptions)).setVisibility(View.GONE);
                            }
                        }
                        counter++;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                try {
                    if (jsonRespObj != null) {
                        if (jsonRespObj.getString("CustPayments") != null) {
                            final JSONArray vJsonArr = new JSONArray(jsonRespObj.getString("CustPayments"));
                            if (vJsonArr.length() > 0) {
                                ((LinearLayout) findViewById(R.id.payOptions)).setVisibility(View.GONE);
                                ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.GONE);

                                LinearLayout vDataContainer = (LinearLayout) findViewById(R.id.linDataCont);

                                final LinearLayout vVaultOptionsCont = new LinearLayout(context);
                                vVaultOptionsCont.setId(R.id.vaultCont);
                                vVaultOptionsCont.setOrientation(LinearLayout.VERTICAL);
                                TextView tv = new TextView(context);
                                tv.setText("Vault Options");
                                vVaultOptionsCont.addView(tv);

                                RadioGroup rg = new RadioGroup(context);
                                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        try {
                                            for (int i = 0; i < vJsonArr.length(); i++) {
                                                JSONObject vVaultOpt = vJsonArr.getJSONObject(i);

                                                if (checkedId == Integer.parseInt(vVaultOpt.getString("payOptId"))) {
                                                    selectedCardType = new CardTypeDTO();
                                                    selectedCardType.setCardName(vVaultOpt.getString("payCardName"));
                                                    selectedCardType.setCardType(vVaultOpt.getString("payCardType"));
                                                    selectedCardType.setPayOptType(vVaultOpt.getString("payOption"));

                                                    selectedPaymentOption = vVaultOpt.getString("payOption");

                                                    if (selectedPaymentOption.equals("OPTCRDC") || selectedPaymentOption.equals("OPTDBCRD"))
                                                        ((LinearLayout) findViewById(R.id.vCardCVVCont)).setVisibility(View.VISIBLE);
                                                    else
                                                        ((LinearLayout) findViewById(R.id.vCardCVVCont)).setVisibility(View.GONE);

                                                    String vCardStr = "";
                                                    try {
                                                        vCardStr = vVaultOpt.getString("payCardNo") != null ? vVaultOpt.getString("payCardNo") : cardNumber.getText().toString();
                                                    } catch (Exception e) {
                                                    }

                                                    cardNumber.setText(vCardStr);
                                                }
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                });
                                for (int i = 0; i < vJsonArr.length(); i++) {
                                    JSONObject vVaultOpt = vJsonArr.getJSONObject(i);

                                    String vCardStr = "";
                                    try {
                                        vCardStr = vVaultOpt.getString("payCardNo") != null ? " - XXXX XXXX XXXX " + vVaultOpt.getString("payCardNo") : "";
                                    } catch (Exception e) {
                                    }

                                    //Radio Button
                                    String vLblText = paymentOptions.get(vVaultOpt.getString("payOption"))
                                            + " - " + vVaultOpt.getString("payCardName") + vCardStr;
                                    RadioButton rb = new RadioButton(context);
                                    rb.setId(Integer.parseInt(vVaultOpt.getString("payOptId")));
                                    rb.setText(vLblText);
                                    rb.setTextSize(11);

                                    rg.addView(rb);
                                }
                                vVaultOptionsCont.addView(rg);

                                vDataContainer.addView(vVaultOptionsCont);

                                final CheckBox vChb = new CheckBox(context);
                                vChb.setText("Pay using other payment option");
                                vChb.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (vChb.isChecked()) {
                                            ((LinearLayout) findViewById(R.id.vCardCVVCont)).setVisibility(View.GONE);
                                            selectedPaymentOption = ((PaymentOptionDTO) payOpt.getItemAtPosition(payOpt.getSelectedItemPosition())).getPayOptId();
                                            ((LinearLayout) findViewById(R.id.payOptions)).setVisibility(View.VISIBLE);
                                            if (selectedPaymentOption.equals("OPTDBCRD")
                                                    || selectedPaymentOption.equals("OPTCRDC"))
                                                ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.VISIBLE);
                                            else if (selectedPaymentOption.equals("OPTEMI")) {
                                                ((LinearLayout) findViewById(R.id.emiOptions)).setVisibility(View.VISIBLE);
                                                ((LinearLayout) findViewById(R.id.emiDetails)).setVisibility(View.VISIBLE);
                                                ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.VISIBLE);
                                            } else
                                                ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.GONE);
                                            ((CheckBox) findViewById(R.id.saveCard)).setVisibility(View.VISIBLE);
                                            vVaultOptionsCont.setVisibility(View.GONE);
                                        } else {
                                            ((LinearLayout) findViewById(R.id.payOptions)).setVisibility(View.GONE);
                                            ((LinearLayout) findViewById(R.id.cardDetails)).setVisibility(View.GONE);
                                            ((LinearLayout) findViewById(R.id.emiOptions)).setVisibility(View.GONE);
                                            ((CheckBox) findViewById(R.id.saveCard)).setVisibility(View.GONE);
                                            vVaultOptionsCont.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                                vDataContainer.addView(vChb);
                            } else {
                                ((LinearLayout) findViewById(R.id.payOptions)).setVisibility(View.VISIBLE);
                            }
                        } else {
                            LinearLayout ll = (LinearLayout) findViewById(R.id.cardDetails);
                            if (selectedPaymentOption.equals("OPTDBCRD") ||
                                    selectedPaymentOption.equals("OPTCRDC")) {
                                ll.setVisibility(View.VISIBLE);
                            } else {
                                ll.setVisibility(View.GONE);
                            }
                            counter++;
                        }
                    }
                } catch (Exception e) {
                }
            } catch (Exception e) {
                showToast("Error loading payment options");
            }
        }
    }
}