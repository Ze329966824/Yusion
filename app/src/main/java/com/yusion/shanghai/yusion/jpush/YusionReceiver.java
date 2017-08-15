package com.yusion.shanghai.yusion.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.yusion.shanghai.yusion.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by LX on 2017/8/14.
 */

public class YusionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Intent i = new Intent(context, JpushDialogActivity.class);

                String string = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (TextUtils.isEmpty(string))return;
                i.putExtra("jsonObject",string);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

        }
    }
}
