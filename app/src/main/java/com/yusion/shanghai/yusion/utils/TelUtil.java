package com.yusion.shanghai.yusion.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by ice on 2017/12/12.
 */

public class TelUtil {

    public static void test(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        Log.e("TAG", "test: " + tm.getLine1Number());
//        Log.e("TAG", "test: " + tm.getPhoneCount());
//        Log.e("TAG", "test: " + SubscriptionManager.from(context).getActiveSubscriptionInfoCount());

    }

    /**
     * 判断卡槽是否有卡
     *
     * @param slotId  1，2
     * @param context
     * @return 返回-1表示该卡槽没插卡
     */
    public static int getSubId(int slotId, Context context) {
        Uri uri = Uri.parse("content://telephony/siminfo");
        Cursor cursor = null;
        ContentResolver contentResolver = context.getContentResolver();
        try {
            cursor = contentResolver.query(uri, new String[]{"_id", "sim_id"}, "sim_id = ?", new String[]{String.valueOf(slotId)}, null);

//            //打印
//            JSONArray jsonArray = new JSONArray();
//            while (cursor != null && cursor.moveToNext()) {
//                String[] strings = cursor.getColumnNames();
//                Log.e("TAG", "getSubId: " + strings);
//                JSONObject jsonObject = new JSONObject();
//                for (int i = 0, size = strings.length; i < size; i++) {
//                    int type = cursor.getType(cursor.getColumnIndex(strings[i]));
//                    if (Cursor.FIELD_TYPE_BLOB == type) {
//                        jsonObject.put(strings[i], cursor.getBlob(cursor.getColumnIndex(strings[i])) == null ? "" : cursor.getBlob(cursor.getColumnIndex(strings[i])));
//                    } else {
//                        jsonObject.put(strings[i], cursor.getString(cursor.getColumnIndex(strings[i])) == null ? "" : cursor.getString(cursor.getColumnIndex(strings[i])));
//                    }
//                }
//                jsonArray.put(jsonObject);
//            }
//            Log.e("TAG", "getSubId: " + jsonArray);


            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    return cursor.getInt(cursor.getColumnIndex("_id"));
                }
            }
        } catch (Exception ignored) {
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return -1;
    }

}