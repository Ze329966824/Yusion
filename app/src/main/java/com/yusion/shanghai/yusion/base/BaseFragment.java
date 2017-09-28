package com.yusion.shanghai.yusion.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.ubt.UBT;
import com.yusion.shanghai.yusion.widget.TitleBar;

public abstract class BaseFragment extends Fragment {

    public Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public void onPause() {
        super.onPause();
        UBT.addPageEvent(getContext(), "page_hidden", "fragment", getClass().getSimpleName());
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }


    @Override
    public void onResume() {
        super.onResume();
        UBT.addPageEvent(getContext(), "page_show", "fragment", getClass().getSimpleName());
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    public TitleBar initTitleBar(final View view, String title) {
        TitleBar titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        titleBar.setBackgroundResource(R.color.system_color);
        titleBar.setImmersive(false);
        titleBar.setTitle(title);
        titleBar.setTitleColor(view.getResources().getColor(R.color.title_bar_text_color));
        titleBar.setDividerColor(view.getResources().getColor(R.color.system_color));
        return titleBar;
    }
}
