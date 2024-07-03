package com.saneforce.godairy.procurement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.saneforce.godairy.databinding.ActivityImageViewBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {
    private ActivityImageViewBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadImage();
        onClick();
    }

    private void onClick() {
        binding.backButton.setOnClickListener(view -> finish());
    }

    private void loadImage() {

        String accessId = getIntent().getStringExtra("access_id");
        String image = "";

      if (accessId == null){
          accessId = "0";
      }

        String eventName = getIntent().getStringExtra("event_name");

        if (eventName != null){
            binding.eventName.setText(eventName);
        }

      if (Integer.parseInt(accessId) == 0){
          image = getIntent().getStringExtra("uri");
          String fileName = image.substring(image.lastIndexOf('/')+1, image.length());
          File fileFormersMeeting = new File(getExternalFilesDir(null), "/procurement/" +fileName);
          Bitmap bitmap = BitmapFactory.decodeFile(fileFormersMeeting.getAbsolutePath());
          Glide.with(this)
                  .load(bitmap)
                  .into(binding.imageView);
          return;
      }

        if (Integer.parseInt(accessId) == 1){
            // received intent has url
            image = getIntent().getStringExtra("url");
        }

        if (Integer.parseInt(accessId) == 2)
        {
            image = getIntent().getStringExtra("s3"); // for aws s3

            Bitmap bitmap = BitmapFactory.decodeFile(image);
            binding.imageView.setImageBitmap(bitmap);
        }

        Glide.with(this)
                .load(image)
                .into(binding.imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}