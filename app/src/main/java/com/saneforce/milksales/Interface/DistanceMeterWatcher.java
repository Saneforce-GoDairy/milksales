package com.saneforce.milksales.Interface;

import org.json.JSONObject;

public interface DistanceMeterWatcher {
    void onKilometerChange(JSONObject KMDetails);
}
