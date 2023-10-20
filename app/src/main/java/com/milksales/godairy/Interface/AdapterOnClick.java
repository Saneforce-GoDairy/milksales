package com.milksales.godairy.Interface;

import com.google.gson.JsonObject;
import com.milksales.godairy.Activity.Util.SelectionModel;

import org.json.JSONObject;

public interface AdapterOnClick {

    default void onIntentClick(JsonObject item,int Name){

    };

    default void onIntentClick(JSONObject item, int Name){

    };
    default void onIntentClick(int Name){

    };
    default void CallMobile(String MobileNo){

    };

    default void onIntentClick(SelectionModel selectionModel){

    };

}
