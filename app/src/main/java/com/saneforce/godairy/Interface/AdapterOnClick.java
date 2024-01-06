package com.saneforce.godairy.Interface;

import com.google.gson.JsonObject;
import com.saneforce.godairy.Activity.Util.SelectionModel;

import org.json.JSONObject;

public interface AdapterOnClick {

    default void onIntentClick(JsonObject item,int Name){

    };

    default void onIntentClick(JSONObject item, int Name){

    };
    default void onIntentClick(int Name){

    };

    default void onEditOrder(String orderNo, String cutoff_time, String categoryType){

    };
    default void CallMobile(String MobileNo){

    };

    default void onIntentClick(SelectionModel selectionModel){

    };

}
