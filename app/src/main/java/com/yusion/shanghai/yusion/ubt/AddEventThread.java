package com.yusion.shanghai.yusion.ubt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.yusion.shanghai.yusion.retrofit.api.UBTApi;
import com.yusion.shanghai.yusion.ubt.bean.UBTData;
import com.yusion.shanghai.yusion.ubt.sql.SqlLiteUtil;
import com.yusion.shanghai.yusion.ubt.sql.UBTEvent;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

public class AddEventThread implements Runnable {
    private String action;
    private View view;
    private String pageName;
    private String action_value;

    //page
    private boolean isPageEvent;
    private String object;
    private Context context;

    public AddEventThread(Context context, String action, View view, String pageName, String action_value) {
        this.context = context;
        this.action = action;
        this.view = view;
        this.pageName = pageName;
        this.action_value = action_value;
    }

    public AddEventThread(Context context, String action, String object, String pageName) {
        this.context = context;
        this.action = action;
        this.pageName = pageName;
        this.object = object;
        isPageEvent = true;
    }

    @Override
    public void run() {
        ContentValues values = new ContentValues();
        if (isPageEvent) {
            values.put("object", object);
        } else {
            values.put("object", view.getClass().getSimpleName());
        }
        values.put("action", action);
        values.put("page", pageName);
        values.put("ts", new Date().getTime());
        SqlLiteUtil.insert(values);
        Log.e("TAG", "run: 插入成功");

        Cursor cursor = SqlLiteUtil.query(null, null, null, null);
        int count = cursor.getCount();
        if (count > UBT.LIMIT) {
            Log.e("TAG", "run:共有 " + count);
            Cursor query = SqlLiteUtil.query(null, null, null, String.valueOf(UBT.LIMIT));
            Log.e("TAG", "run:要删除的 " + query.getCount());
            query.moveToFirst();
            List<Long> tss = new ArrayList<>();
            List<UBTEvent> data = new ArrayList<>();
            while (query.moveToNext()) {
                tss.add(query.getLong(query.getColumnIndex("ts")));
                UBTEvent ubtEvent = new UBTEvent();
                ubtEvent.object = query.getString(query.getColumnIndex("object"));
                ubtEvent.action = query.getString(query.getColumnIndex("action"));
                ubtEvent.page = query.getString(query.getColumnIndex("page"));
                ubtEvent.ts = query.getLong(query.getColumnIndex("ts"));
                data.add(ubtEvent);
            }
            Log.e("TAG", "run: " + tss);

            //发送
            UBTData req = new UBTData();
            req.data = data;
            req.token = SharedPrefsUtil.getInstance(context).getValue("token", "");
            req.mobile = SharedPrefsUtil.getInstance(context).getValue("mobile", "");
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            req.imei = tm.getDeviceId();

            Log.e("TAG", "run: 发送");
            try {
                if (UBTApi.getUBTService().postUBTData(req).execute().isSuccessful()) {
                    Log.e("TAG", "run: 发送成功");

                    for (Long aLong : tss) {
                        SqlLiteUtil.delete("ts = ?", new String[]{String.valueOf(aLong)});
                    }
                }
            } catch (IOException e) {
                Log.e("TAG", "run: " + e);
                e.printStackTrace();
            }

        }

    }

    @Override
    public String toString() {
        return "AddEventThread{" +
                "action='" + action + '\'' +
                ", view=" + view +
                ", pageName='" + pageName + '\'' +
                ", action_value='" + action_value + '\'' +
                '}';
    }
}
