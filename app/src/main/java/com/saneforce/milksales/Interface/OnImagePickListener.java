package com.saneforce.milksales.Interface;

import android.graphics.Bitmap;

public interface OnImagePickListener {
    default void OnImagePick(Bitmap image, String FileName) {

    }
    default void OnImageURIPick(Bitmap image,String FileName,String fullPath) {
    }
}
