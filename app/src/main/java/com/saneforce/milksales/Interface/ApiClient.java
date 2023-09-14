package com.saneforce.milksales.Interface;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String CONFIG_URL = "https://lactalisindia.salesjump.in/server/milk_url_config.json";
    public static String DEFAULT_BASE_URL = "https://lactalisindia.salesjump.in/";
    public static String BASE_URL = "https://lactalisindia.salesjump.in/server/";
    private static Retrofit retrofit = null;

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

    public static void ChangeBaseURL(String BaseURL) {
        BASE_URL = BaseURL + "server/";
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
    }
}
