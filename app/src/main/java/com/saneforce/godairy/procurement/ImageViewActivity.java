package com.saneforce.godairy.procurement;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.saneforce.godairy.databinding.ActivityImageViewBinding;

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

        if (Integer.parseInt(accessId) == 1){
            // received intent has url
            image = getIntent().getStringExtra("url");
        }else {
            image = getIntent().getStringExtra("uri");
        }

        String eventName = getIntent().getStringExtra("event_name");

        Glide.with(this)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageView);

        if (eventName != null){
            binding.eventName.setText(eventName);
        }
    }
}