package com.yusion.shanghai.yusion.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.widget.TitleBar;

public abstract class BaseFragment extends Fragment {

    public Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
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
