package com.yusion.shanghai.yusion.ui.upload;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.glide.StatusImageRel;
import com.yusion.shanghai.yusion.utils.GlideUtil;

public class PreviewActivity extends BaseActivity {
    private String imageUrl;
    private StatusImageRel statusImageRel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        statusImageRel = (StatusImageRel) findViewById(R.id.image_preview);
        statusImageRel.getSourceImg().setScaleType(ImageView.ScaleType.FIT_CENTER);
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("PreviewImg");
        GlideUtil.loadImg(this, statusImageRel, imageUrl);
        statusImageRel.setOnClickListener(v -> finish());
    }
}
