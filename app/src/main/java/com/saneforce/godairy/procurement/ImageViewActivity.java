package com.saneforce.godairy.procurement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityImageViewBinding;

public class ImageViewActivity extends AppCompatActivity {
    private ActivityImageViewBinding binding;
    private final Context context = this;


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
        String uri = getIntent().getStringExtra("uri");
        String eventName = getIntent().getStringExtra("event_name");

        Glide.with(this)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageView);

        if (eventName != null){
            binding.eventName.setText(eventName);
        }
    }
}