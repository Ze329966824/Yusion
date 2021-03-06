package com.yusion.shanghai.yusion.ubt.annotate;

import android.support.annotation.IdRes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
public @interface BindView {
    @IdRes int id();

    String widgetName();

    String onClick() default "onClick";

    String onFocusChange() default "onFocusChange";

    String onCheckedChanged() default "onCheckedChanged";

    String onTouch() default "onTouch";
}

