package com.saneforce.milksales.apiRequest;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRequest {
    Context context;
    Common_Class common_class;

    ApiRequestListener listener;

    public ApiRequest(Context context, Map<String, String> params, String data) {
        this.context = context;
        this.common_class = new Common_Class(context);
        CallApi(params, data);
    }

    public void setListener(ApiRequestListener listener) {
        this.listener = listener;
    }

    private void CallApi(Map<String, String> params, String data) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        params.put("outletCode", Shared_Common_Pref.OutletCode);
        params.put("sfCode", Shared_Common_Pref.Sf_Code);
        params.put("stateCode", Shared_Common_Pref.StateCode);
        params.put("divCode", Shared_Common_Pref.Div_Code);
        params.put("currentTime", Common_Class.GetDatemonthyearTimeformat());
        params.put("distId", new Shared_Common_Pref(context).getvalue(Constants.Distributor_Id));

        Log.e("ApiRequest", "params: " + params);
        Log.e("ApiRequest", "data: " + data);

        Call<ResponseBody> call = apiInterface.universalAPIRequest(params, data);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("success")) {
                            Log.e("ApiRequest", jsonObject.toString());
                            if (listener != null) {
                                listener.onSuccess(jsonObject);
                            }
                        } else {
                            Log.e("ApiRequest", "success: false");
                            if (listener != null) {
                                listener.onError("");
                            }
                        }
                    } catch (Exception e) {
                        Log.e("ApiRequest", e.getMessage());
                        if (listener != null) {
                            listener.onError(e.getMessage());
                        }
                    }
                } else {
                    Log.e("ApiRequest", "Response not success or null");
                    if (listener != null) {
                        listener.onError("");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("ApiRequest", t.getMessage());
                if (listener != null) {
                    listener.onError(t.getMessage());
                }
            }
        });
    }

    public interface ApiRequestListener {
        void onSuccess(JSONObject object);

        void onError(String errorMsg);

        void onFailure(String errorMsg);
    }
}
