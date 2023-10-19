package com.saneforce.godairy.Interface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JioMoneyClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://pp-apig.jiomoney.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
