package com.yusion.shanghai.yusion.widget.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ice on 2017/8/15.
 */

/**
 * 通过检测 上滑下滑 操作来显示 FAB 控件
 */
public class AutoInOutBehavior extends FloatingActionButton.Behavior {

    public AutoInOutBehavior(Context context, AttributeSet attrs) {
        super();
    }

    private boolean mIsAnimatingOut = false;
    private boolean mIsAnimatingIn = false;

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        boolean b = nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
        Log.e("TAG", "onStartNestedScroll: " + b);
        return b;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyConsumed > 0 && dyUnconsumed == 0) {
            System.out.println("上滑中。。。");
            if (!mIsAnimatingOut) {
                animateOut(child);
            }
        }
        if (dyConsumed == 0 && dyUnconsumed > 0) {
            System.out.println("到边界了还在上滑。。。");
            if (!mIsAnimatingOut) {
                animateOut(child);
            }
        }
        if (dyConsumed < 0 && dyUnconsumed == 0) {
            System.out.println("下滑中。。。");
            if (!mIsAnimatingIn) {
                animateIn(child);
            }
        }
        if (dyConsumed == 0 && dyUnconsumed < 0) {
            System.out.println("到边界了，还在下滑。。。");
            if (!mIsAnimatingIn) {
                animateIn(child);
            }
        }
        Log.e("TAG", "onNestedScroll() called with: coordinatorLayout = [" + coordinatorLayout + "], child = [" + child + "], target = [" + target + "], dxConsumed = [" + dxConsumed + "], dyConsumed = [" + dyConsumed + "], dxUnconsumed = [" + dxUnconsumed + "], dyUnconsumed = [" + dyUnconsumed + "]");
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    private void animateOut(final FloatingActionButton button) {
        ViewCompat.animate(button).translationY(button.getHeight() + getMarginBottom(button))
                .setListener(new ViewPropertyAnimatorListener() {
                    public void onAnimationStart(View view) {
                        mIsAnimatingOut = true;
                    }
                    public void onAnimationCancel(View view) {
                        mIsAnimatingOut = false;
                    }
                    public void onAnimationEnd(View view) {
                        mIsAnimatingOut = false;
                    }
                }).start();
    }

    private void animateIn(FloatingActionButton button) {
        button.setVisibility(View.VISIBLE);
        ViewCompat.animate(button).translationY(0)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        mIsAnimatingIn = true;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        mIsAnimatingIn = false;
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        mIsAnimatingIn = false;
                    }
                })
                .start();
    }


    private int getMarginBottom(View v) {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }
}
