package com.yusion.shanghai.yusion.glide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion.R;

/**
 * Created by ice on 2017/9/11.
 */

public class StatusImageView extends RelativeLayout {
    private TextView progressTv;
    private ImageView sourceImg;

    public StatusImageView(Context context) {
        this(context, null);
    }

    public StatusImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.status_img_rel, this, true);
        progressTv = ((TextView) contentView.findViewById(R.id.status_img_progress_tv));
        sourceImg = ((ImageView) contentView.findViewById(R.id.status_img_source_img));
    }

    public TextView getProgressTv() {
        return progressTv;
    }

    public ImageView getSourceImg() {
        return sourceImg;
    }
}
