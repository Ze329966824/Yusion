package com.yusion.shanghai.yusion.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yusion.shanghai.yusion.YusionApp;

/**
 * Created by ice on 2017/8/3.
 */

public class BaseActivity extends AppCompatActivity {

    protected YusionApp myApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = ((YusionApp) getApplication());
    }
}
