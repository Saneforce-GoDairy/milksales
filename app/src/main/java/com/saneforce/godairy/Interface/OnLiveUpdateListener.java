package com.saneforce.godairy.Interface;

public interface OnLiveUpdateListener {
    default void onUpdate(String mode){};
    default void onError(String msg){};
}
