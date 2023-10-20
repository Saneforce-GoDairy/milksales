package com.milksales.godairy.Interface;

import org.json.JSONObject;

public interface DistanceMeterWatcher {
    void onKilometerChange(JSONObject KMDetails);
}
