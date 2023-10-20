package com.milksales.godairy.JioMoney;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Constants;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.ApiClient;
import com.milksales.godairy.Interface.ApiInterface;
import com.milksales.godairy.Interface.JioMoneyClient;
import com.milksales.godairy.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentWebView extends AppCompatActivity {
    public static final String UserDetail = "MyPrefs";
    WebView webview;
    ProgressBar progressBar;
    boolean canGoBack = false;
    String uniqueKey, Trans_Sl_No;
    double totalValues;
    Common_Class common_class;
    SharedPreferences UserDetails;
    Context context = this;
    ProgressDialog progressDialog;
    Shared_Common_Pref shared_common_pref;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_view);

        webview = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        common_class = new Common_Class(this);
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        shared_common_pref = new Shared_Common_Pref(this);
        uniqueKey = shared_common_pref.getvalue("uniqueKey");

        Trans_Sl_No = getIntent().getStringExtra("Trans_Sl_No");
        totalValues = getIntent().getDoubleExtra("totalValues", 0.00);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        MakePayment();
    }

    private void MakePayment() {
        try {
            progressDialog.setMessage("Connecting to Payment Gateway...");
            progressDialog.show();
            JSONObject object = new JSONObject();
            JSONObject application = new JSONObject();
            application.put("clientId", Constants.clientId);
            object.put("application", application);
            JSONObject blank = new JSONObject();
            blank.put("mode", 22);
            blank.put("value", Constants.secretCode);
            JSONArray authenticateList = new JSONArray();
            authenticateList.put(blank);
            object.put("authenticateList", authenticateList);
            object.put("scope", "SESSION");
            object.put("purpose", 2);
            ApiInterface apiInterface = JioMoneyClient.getClient().create(ApiInterface.class);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), String.valueOf(object));
            apiInterface.authenticate(uniqueKey, requestBody).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.body() != null) {
                        try {
                            String res = response.body().string();
                            JSONObject jsonObject = new JSONObject(res);
                            if (jsonObject.optString("status").equalsIgnoreCase("SUCCESS")) {
                                JSONObject session = jsonObject.optJSONObject("session");
                                if (session != null) {
                                    JSONObject accessToken = session.optJSONObject("accessToken");
                                    String tokenValue = accessToken.optString("tokenValue");
                                    String appIdentifierToken = session.optString("appIdentifierToken");
                                    MakeTransaction(tokenValue, appIdentifierToken);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ignored) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception t) {
            progressDialog.dismiss();
            Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void MakeTransaction(String tokenValue, String appIdentifierToken) {
        progressDialog.setMessage("Initiating Transaction...");
        Random random = new Random();
        int idempotentKey = random.nextInt(99999999 - 11111111 + 1) + 11111111;
        JSONObject mainObject = new JSONObject();
        try {
            JSONObject transaction = new JSONObject();
            transaction.put("idempotentKey", String.valueOf(idempotentKey));
            transaction.put("invoice", Trans_Sl_No);
            transaction.put("initiatingEntityTimestamp", (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(Calendar.getInstance().getTime()) + "Z"));
            JSONObject initiatingEntity = new JSONObject();
            initiatingEntity.put("returnUrl", Constants.returnUrl);
            transaction.put("initiatingEntity", initiatingEntity);
            String CHECKOUT = "{\n" +
                    "      \"template\": {\n" +
                    "        \"id\": \"100\"\n" +
                    "      },\n" +
                    "      \"allowed\": [\n" +
                    "        {\n" +
                    "          \"rank\": \"1\",\n" +
                    "          \"methodType\": \"110\",\n" +
                    "          \"methodSubType\": \"582\",\n" +
                    "          \"cardType\": [\n" +
                    "            110,\n" +
                    "            130,\n" +
                    "            131\n" +
                    "          ]\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"rank\": \"2\",\n" +
                    "          \"methodType\": \"212\",\n" +
                    "          \"methodSubType\": \"580\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"rank\": \"3\",\n" +
                    "          \"methodType\": \"110\",\n" +
                    "          \"methodSubType\": \"566\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"rank\": \"4\",\n" +
                    "          \"methodType\": \"110\",\n" +
                    "          \"methodSubType\": \"581\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }";
            JSONObject checkout = new JSONObject(CHECKOUT);
            transaction.put("checkout", checkout);
            JSONObject payer = new JSONObject();
            payer.put("externalId", UserDetails.getString("Sfcode", new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime())));
            payer.put("name", UserDetails.getString("SfName", "Customer Name"));
            payer.put("email", "email@gmail.com");
            JSONObject mobile = new JSONObject();
            mobile.put("number", shared_common_pref.getvalue(Constants.Distributor_phone));
            mobile.put("countryCode", "91");
            payer.put("mobile", mobile);
            transaction.put("payer", payer);
            mainObject.put("transaction", transaction);
            JSONObject amount = new JSONObject();
            amount.put("netAmount", String.valueOf(totalValues));
            mainObject.put("amount", amount);
            JSONObject payee = new JSONObject();
            payee.put("merchantId", Constants.merchantId);
            mainObject.put("payee", payee);
        } catch (Exception ignored) {
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), String.valueOf(mainObject));
        Map<String, String> headers = new HashMap<>();
        headers.put("x-trace-id", uniqueKey);
        headers.put("x-app-access-token", tokenValue);
        headers.put("x-appid-token", appIdentifierToken);
        ApiInterface apiInterface = JioMoneyClient.getClient().create(ApiInterface.class);
        apiInterface.MakeTransaction(headers, requestBody).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String res = response.body().string();
                        JSONObject main = new JSONObject(res);
                        JSONObject transaction = main.optJSONObject("transaction");
                        if (transaction != null) {
                            if (transaction.optString("status").equalsIgnoreCase("INITIATED")) {
                                String id = transaction.optString("id");
                                JSONObject metadata = transaction.optJSONObject("metadata");
                                String appidToken = metadata.optString("x-appid-token");
                                String appAccessToken = metadata.optString("x-app-access-token");
                                StorePaymentInfoToDB(uniqueKey, appAccessToken, appidToken, id);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void StorePaymentInfoToDB(String uniqueKey, String appAccessToken, String appIdToken, String id) {
        progressDialog.setMessage("Saving Payment Details...");
        Map<String, String> params = new HashMap<>();
        params.put("axn", "save_payment_info");
        params.put("tid", id);
        params.put("orderValue", String.valueOf(totalValues));
        params.put("Trans_Sl_No", Trans_Sl_No);
        params.put("uniqueKey", uniqueKey);
        params.put("appAccessToken", appAccessToken);
        params.put("appIdToken", appIdToken);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        // Todo: Saving Transaction info to DB
        apiInterface.universalAPIRequest(params, "").enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String res = response.body().string();
                        JSONObject object = new JSONObject(res);
                        if (object.getBoolean("success")) {
                            String postData = "<form name=\"payment\" method=\"POST\" action=\"https://pp-checkout.jiopay.com:8443\"\n" +
                                    "enctype=\"application/x-www-form-urlencoded\">\n" +
                                    "<input type='hidden' name=\"mid\" value=\"100001000293397\"/>\n" +
                                    "<input type='hidden' name=\"appidtoken\" value=\"%s\"/>\n" +
                                    "<input type='hidden' name=\"appaccesstoken\" value=\"%s\"/>\n" +
                                    "<input type='hidden' name=\"intentid\" value=\"%s\"/>\n" +
                                    "<input type='hidden' id='brandColor' name='brandColor' value='#1997CE'/>\n" +
                                    "<input type='hidden' id='bodyBgColor' name='bodyBgColor' value='#FFFFFF'/>\n" +
                                    "<input type='hidden' id='bodyTextColor' name='bodyTextColor' value='#000000'/>\n" +
                                    "<input type='hidden' id='headingText' name='headingText' value='#FFFFFF'/>\n" +
                                    "<center><img style=\"margin-top: 50px; width: 110px; height: 100px;\" src=\"https://www.ppro.com/wp-content/uploads/2021/06/JioMoney.png\" alt=\"Jio Money\"></center>\n" +
                                    "<center><input style=\"margin-top: 50px;\" type=\"submit\" value=\"Continue to Jio Pay\"/></center>\n" +
                                    "</form>";
                            String Data = String.format(postData, appIdToken, appAccessToken, id).replaceAll("\\n", "");
                            webview.loadDataWithBaseURL(null, Data, "text/html", "UTF-8", null);
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (canGoBack) {
            finish();
        } else {
            canGoBack = true;
            Toast.makeText(this, "Press back button again to go back", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> canGoBack = false, 500);
        }
    }
}