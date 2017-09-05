package com.yusion.shanghai.yusion.ui.apply;


import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;

public class PreviewActivity extends BaseActivity {
    private String imageUrl = "";
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        imageView = (ImageView) findViewById(R.id.image_preview);
        imageUrl = getIntent().getStringExtra("PreviewImg");
        Glide.with(this).load(imageUrl).into(imageView);
        imageView.setOnClickListener(v -> finish());
    }
}
