package com.saneforce.milksales.Interface;

import android.util.Log;

public interface UpdateResponseUI {
    void onLoadDataUpdateUI(String apiDataResponse, String key);
    default void onErrorData(String msg){
        Log.v("errorMsg123",msg);
    }
}