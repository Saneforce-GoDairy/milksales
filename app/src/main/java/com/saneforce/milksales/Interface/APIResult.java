package com.saneforce.milksales.Interface;

import org.json.JSONObject;

public interface APIResult {
    void onSuccess(JSONObject jsonObject);
    void onFailure(String error);
}
