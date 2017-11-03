package com.yusion.shanghai.yusion.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.yusion.shanghai.yusion.YusionApp;

import cn.jpush.android.api.JPushInterface;
import io.sentry.Sentry;


public class JpushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.e("推送-----------------", "");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Intent i = new Intent(context, JpushDialogActivity.class);

            String string = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (TextUtils.isEmpty(string)) {
                return;
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                if (YusionApp.isForeground) {
                    int notificationID = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                    String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                    JPushInterface.clearNotificationById(context, notificationID);
                }
            }
            Log.e("EXTRA_EXTRA", string);
            Sentry.capture(string);
            i.putExtra("jsonObject", string);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
