package com.yusion.shanghai.yusion.ubt;
/*
 * 类描述：
 * 伟大的创建人：ice   
 * 不可相信的创建时间：17/6/13 上午9:41 
 * 爱信不信的修改时间：17/6/13 上午9:41 
 * @version
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.retrofit.api.UBTApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.ubt.annotate.BindView;
import com.yusion.shanghai.yusion.ubt.bean.UBTData;
import com.yusion.shanghai.yusion.ubt.sql.SqlLiteUtil;
import com.yusion.shanghai.yusion.ubt.sql.UBTEvent;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UBT {

    public static int LIMIT;

    static {
        if (Settings.isOnline) {
            LIMIT = 50;
        } else {
            LIMIT = 10;
        }
    }

    private static ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();


    public static void sendAllUBTEvents(Context context) {
        sendUBTEvents(context, 0, null);
    }

    public static void sendAllUBTEvents(Context context, OnVoidCallBack callBack) {
        sendUBTEvents(context, 0, callBack);
    }

    public static void sendUBTEvents(Context context, int limit) {
        sendUBTEvents(context, limit, null);
    }

    /**
     * @param context
     * @param limit   limit为0时发送所有数据 不为0时若数据数量大于limit条则发送limit条
     */
    public static void sendUBTEvents(Context context, int limit, OnVoidCallBack callBack) {
        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                //没有token和mobile的数据暂不发送
                if (TextUtils.isEmpty(SharedPrefsUtil.getInstance(context).getValue("mobile", ""))
                        || TextUtils.isEmpty(SharedPrefsUtil.getInstance(context).getValue("token", "")))
                    return;
                String TAG = "UBT";
                Cursor cursor = SqlLiteUtil.query(null, null, null, null);
                int count = cursor.getCount();
                if (count > limit) {
                    Log.e(TAG, "run:共有 " + count);
                    Cursor query;
                    if (limit == 0) {
                        query = SqlLiteUtil.query(null, null, null, null);
                    } else {
                        query = SqlLiteUtil.query(null, null, null, String.valueOf(UBT.LIMIT));
                    }
                    Log.e(TAG, "run:要删除的 " + query.getCount());
                    query.moveToFirst();
                    List<Long> tss = new ArrayList<>();
                    List<UBTEvent> data = new ArrayList<>();
                    while (query.moveToNext()) {
                        tss.add(query.getLong(query.getColumnIndex("ts")));
                        UBTEvent ubtEvent = new UBTEvent();
                        ubtEvent.object = query.getString(query.getColumnIndex("object"));
                        ubtEvent.action = query.getString(query.getColumnIndex("action"));
                        ubtEvent.page = query.getString(query.getColumnIndex("page"));
                        ubtEvent.page_cn = query.getString(query.getColumnIndex("page_cn"));
                        ubtEvent.ts = query.getLong(query.getColumnIndex("ts"));
                        data.add(ubtEvent);
                    }
                    Log.e(TAG, "run: " + tss);

                    //发送
                    UBTData req = new UBTData(context);
                    UBTData.DataBean dataBean = new UBTData.DataBean();
                    dataBean.category = "ubt";
                    dataBean.mobile = SharedPrefsUtil.getInstance(context).getValue("mobile", "");
                    dataBean.ubt_list = data;
                    req.data.add(dataBean);
                    Log.e(TAG, "run: 正在发送");
                    try {
                        if (UBTApi.getUBTService().postUBTData(req).execute().isSuccessful()) {
                            Log.e(TAG, "run: 发送成功");
                            for (Long aLong : tss) {
                                SqlLiteUtil.delete("ts = ?", new String[]{String.valueOf(aLong)});
                            }
                            if (callBack != null) {
                                callBack.callBack();
                            }
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "run: " + e);
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void bind(final Object object, View sourceView, String pageName) {
        for (Field field : object.getClass().getDeclaredFields()) {
            Annotation[] annotations = field.getAnnotations();
            if (annotations.length == 0) {
                continue;
            }
            for (Annotation annotation : annotations) {
                if (annotation instanceof BindView) {
                    BindView viewAnnotation = (BindView) annotation;
                    View view = sourceView.findViewById(viewAnnotation.id());
                    view.setTag(R.id.UBT_OBJ_NAME, ((BindView) annotation).objectName());
                    try {
                        field.setAccessible(true);
                        field.set(object, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    processorOnClick(object, viewAnnotation.onClick(), view, pageName);
                    processorOnFocusChange(object, viewAnnotation.onFocusChange(), view, pageName);
                    if (view instanceof CompoundButton) {
                        processorOnCheckedChange(object, viewAnnotation.onCheckedChanged(), (CompoundButton) view, pageName);
                    } else if (view instanceof TextView) {
                        processorOnTextChange((TextView) view, pageName);
                    }
                    processorOnTouch(object, viewAnnotation.onTouch(), view, pageName);
                }
            }
        }
    }

    public static void bind(AppCompatActivity activity) {
        bind(activity, activity.getWindow().getDecorView(), activity.getClass().getSimpleName());
    }

    private static void processorOnClick(final Object object, final String methodName, final View view, final String pageName) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addEvent(view.getContext(), "onClick", view, pageName);
                    try {
                        final Method method = object.getClass().getDeclaredMethod(methodName, View.class);
                        method.setAccessible(true);
                        method.invoke(object, view);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void processorOnFocusChange(final Object object, final String methodName, final View view, final String pageName) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    addEvent(view.getContext(), "onFocusChange", view, pageName, hasFocus ? "onFocus" : "onBlur");
                    try {
                        final Method method = object.getClass().getDeclaredMethod(methodName, View.class);
                        method.setAccessible(true);
                        method.invoke(object, view);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static void processorOnCheckedChange(final Object object, final String methodName, final CompoundButton view, final String pageName) {
        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    addEvent(view.getContext(), "onCheckedChange", view, pageName, isChecked ? "checked" : "unchecked");
                    try {
                        final Method method = object.getClass().getDeclaredMethod(methodName, View.class);
                        method.setAccessible(true);
                        method.invoke(object, view);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void processorOnTextChange(final TextView view, final String pageName) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                addEvent(view.getContext(), "onTextChanged", view, pageName, s.toString());
            }
        });
    }

    private static void processorOnTouch(final Object object, final String methodName, final View view, final String pageName) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String operation;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    operation = "down";
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    operation = "up";
                } else {
                    try {
                        final Method method = object.getClass().getDeclaredMethod(methodName, View.class);
                        method.setAccessible(true);
                        return (boolean) method.invoke(object, view);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
                addEvent(view.getContext(), "onTouch", view, pageName, String.format(Locale.CHINA, "{\"operation\":\"%s\",\"x\":\"%s\",\"y\":\"%s\"}", operation, event.getX(), event.getY()));
                try {
                    final Method method = object.getClass().getDeclaredMethod(methodName, View.class);
                    method.setAccessible(true);
                    return (boolean) method.invoke(object, view);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    private static void addEvent(Context context, String action, View view, final String pageName, String action_value) {
        singleThreadPool.execute(new AddEventThread(context, action, view, pageName, action_value));
    }

    private static void addEvent(Context context, String action, View view, final String pageName) {
        addEvent(context, action, view, pageName, null);
    }

    public static void addPageEvent(Context context, String action, String object, final String pageName) {
        singleThreadPool.execute(new AddEventThread(context, action, object, pageName));
    }

    public static void addAppEvent(Context context, String action) {
        singleThreadPool.execute(new AddEventThread(context, action));
    }
}

