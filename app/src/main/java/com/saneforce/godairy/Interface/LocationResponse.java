package com.saneforce.godairy.Interface;

public interface LocationResponse {
    void onSuccess(double lat, double lng);
    void onFailure();
}
