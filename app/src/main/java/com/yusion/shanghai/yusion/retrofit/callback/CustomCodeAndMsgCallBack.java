package com.yusion.shanghai.yusion.retrofit.callback;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pgyersdk.crash.PgyCrashManager;
import com.yusion.shanghai.yusion.base.BaseResult;
import com.yusion.shanghai.yusion.settings.Settings;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class CustomCodeAndMsgCallBack implements Callback<BaseResult> {

    private ProgressDialog dialog;
    private Context context;

    public CustomCodeAndMsgCallBack(Context context) {
        this(context, null);
    }

    public CustomCodeAndMsgCallBack(Context context, ProgressDialog dialog) {
        this.context = context;
        this.dialog = dialog;
        if (this.dialog != null) {
            this.dialog.show();
        }
    }

    @Override
    public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
        BaseResult body = response.body();
        Log.e("API", "onResponse: " + body);
        if (body.code < 0) {
            if (Settings.isOnline) {
                Toast.makeText(context, body.msg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, String.format(Locale.CHINA, "code = %d and msg = %s", body.code, body.msg), Toast.LENGTH_SHORT).show();
            }
        }
        onCustomResponse(body.code,body.msg);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onFailure(Call<BaseResult> call, Throwable t) {
        if (Settings.isOnline) {
            Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
            PgyCrashManager.reportCaughtException(context, ((Exception) t));
        } else {
            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
        }
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public abstract void onCustomResponse(int code, String msg);
}