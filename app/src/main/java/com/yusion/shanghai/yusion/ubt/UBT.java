package com.yusion.shanghai.yusion.ubt;
/*
 * 类描述：
 * 伟大的创建人：ice   
 * 不可相信的创建时间：17/6/13 上午9:41 
 * 爱信不信的修改时间：17/6/13 上午9:41 
 * @version
 */

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.ubt.annotate.BindView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
}

