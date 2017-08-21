package com.yusion.shanghai.yusion.ui.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseFragment;

/**
 * Created by aa on 2017/8/21.
 */

public class UpdateSpouseInfoFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_spouse_info, container, false);
    }
}
