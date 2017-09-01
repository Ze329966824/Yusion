package com.yusion.shanghai.yusion.ui.apply;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yusion.shanghai.yusion.R;

public class PreviewActivity extends AppCompatActivity {
    private String imageUrl;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        imageView = (ImageView) findViewById(R.id.image_preview);
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("PreviewImg");
        Glide.with(this).load(imageUrl).into(imageView);
        imageView.setOnClickListener(v -> finish());
    }
}