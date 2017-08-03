package com.yusion.shanghai.yusion.retrofit.callback;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.pgyersdk.crash.PgyCrashManager;
import com.yusion.shanghai.yusion.base.BaseResult;
import com.yusion.shanghai.yusion.settings.Settings;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ice on 2017/8/3.
 */

public abstract class CustomCallBack<T> implements Callback<BaseResult<T>> {
    private ProgressDialog dialog;
    private Context context;

    public CustomCallBack(Context context) {
        this(context, null);
    }

    public CustomCallBack(Context context, ProgressDialog dialog) {
        this.context = context;
        this.dialog = dialog;
    }

    public abstract void onCustomResponse(T data);

    @Override
    public void onResponse(Call<BaseResult<T>> call, Response<BaseResult<T>> response) {
        onCustomResponse(response.body().data);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onFailure(Call<BaseResult<T>> call, Throwable t) {
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
}
