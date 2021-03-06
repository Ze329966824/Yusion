package com.yusion.shanghai.yusion.retrofit.callback;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.yusion.shanghai.yusion.base.BaseResult;
import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.ui.entrance.LoginActivity;

import java.net.UnknownHostException;
import java.util.Locale;

import io.sentry.Sentry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ice on 2017/8/3.
 */

public abstract class CustomCallBack<T> implements Callback<BaseResult<T>> {
    private Dialog dialog;
    private Context context;

    public CustomCallBack(Context context) {
        this(context, null);
    }

    public CustomCallBack(Context context, Dialog dialog) {
        this.context = context;
        this.dialog = dialog;
        if (this.dialog != null) {
            this.dialog.show();
        }
    }

    public abstract void onCustomResponse(T data);

    @Override
    public void onResponse(Call<BaseResult<T>> call, Response<BaseResult<T>> response) {
        if (dialog != null) {
            dialog.dismiss();
        }

        BaseResult<T> body = response.body();

        if (body == null) {
            Toast.makeText(context, "服务器出现错误,请稍后再试...", Toast.LENGTH_LONG).show();
            return;
        }

        Log.e(Api.getTag(call.request()), "onResponse: " + body);

        if (body.code < 0) {
            if (Settings.isOnline) {
                Toast.makeText(context, body.msg, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, String.format(Locale.CHINA, "code = %d and msg = %s", body.code, body.msg), Toast.LENGTH_LONG).show();
            }
            if (body.code == -1) {
                //token过期
                context.startActivity(new Intent(context, LoginActivity.class));
                return;
            }
        }
        //body.data()可能为空
        onCustomResponse(body.data);
    }

    @Override
    public void onFailure(Call<BaseResult<T>> call, Throwable t) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (t instanceof UnknownHostException) {
            Toast.makeText(context, "网络繁忙,请检查网络", Toast.LENGTH_SHORT).show();
        } else if (Settings.isOnline) {
            Toast.makeText(context, "接口调用失败,请稍后再试...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
        }
        Sentry.capture(t);
    }
}
