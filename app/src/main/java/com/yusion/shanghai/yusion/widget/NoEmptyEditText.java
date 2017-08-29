package com.yusion.shanghai.yusion.widget;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

/**
 * Created by ice on 2017/8/29.
 */

public class NoEmptyEditText extends android.support.v7.widget.AppCompatEditText {
    public NoEmptyEditText(Context context) {
//        this(context, null);
        super(context);
        init();
    }

    public NoEmptyEditText(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
        super(context,attrs);
        init();
    }

    public NoEmptyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
        super(context,attrs,defStyleAttr);
        init();
    }

    private void init() {
        setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n"))
                    return "";
                else
                    return null;
            }
        }});
    }
}
