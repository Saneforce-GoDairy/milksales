package com.saneforce.milksales.Activity_Hap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cuneytayyildiz.gestureimageview.GestureImageView;
import com.saneforce.milksales.R;

import java.io.InputStream;
import java.net.URL;

public class ProductImageView extends Activity {

    GestureImageView ProductZoomImage;
    String ImageUrl = "",ImageUrl1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_image_view);

        ImageUrl = getIntent().getStringExtra("ImageUrl");
        //ImageUrl1 = getIntent().getStringExtra("ImageUrl1");
        ProductZoomImage = findViewById(R.id.product_image);
       // ProductZoomImage.setRotation(90);
        if(ImageUrl.length()>4){
            if(ImageUrl.substring(0,1).equalsIgnoreCase("/")){
                ImageUrl="file://"+ImageUrl;
            }
            new DownLoadImageTask(ProductZoomImage).execute(ImageUrl);
        }
        /*if(ImageUrl1.length()>4){
            if(ImageUrl1.substring(0,1).equalsIgnoreCase("/")){
                ImageUrl1="file://"+ImageUrl1;
            }
            new DownLoadImageTask(ProductZoomImage).execute(ImageUrl1);
        }*/
    }

    public void CloseActivity(View v) {
        finish();
    }


    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        ProgressDialog dialog;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
            dialog = new ProgressDialog(ProductImageView.this);
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) { // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }


        protected void onPostExecute(Bitmap result) {


            super.onPostExecute(result);
            if (dialog.isShowing()) {
                imageView.setImageBitmap(result);
                dialog.dismiss();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("please wait.");
            dialog.show();

        }
    }
}

