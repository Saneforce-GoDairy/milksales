package com.saneforce.godairy.Interface;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Constant URLs
    public static final String CONFIG_URL = "https://lactalisindia.salesjump.in/server/milk_url_config.json";
    public static final String DEFAULT_BASE_URL = "https://lactalisindia.salesjump.in/";

    // Base URL
    public static String BASE_URL = "https://lactalisindia.salesjump.in/server/";
    private static Retrofit retrofit = null;
    private static Retrofit retrofit1 = null;

    // Initialize Retrofit
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientThirumala() {
        if (retrofit1 == null) {
            retrofit1 = new Retrofit.Builder()
                    .baseUrl("https://thirumala.salesjump.in/server/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .build();
        }
        return retrofit1;
    }

    // Re-Initialize Retrofit
    public static void ChangeBaseURL(String BaseURL) {
        BASE_URL = BaseURL + "server/";
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
    }
}
