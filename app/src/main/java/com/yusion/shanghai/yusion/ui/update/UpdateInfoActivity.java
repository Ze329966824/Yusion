package com.yusion.shanghai.yusion.ui.update;

import android.app.AlertDialog;
import android.appwidget.AppWidgetProvider;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;

import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.ubt.UBT;

/**
 * Created by LX on 2017/10/12.
 */

public class UpdateInfoActivity extends BaseActivity {
    public void toCommitActivity(String clt_id, String role, String title, String state) {
        String dialogMsg = "";
        switch (title) {
            case "个人影像件资料":
                dialogMsg = "个人";
                break;
            case "个人配偶影像件资料":
                dialogMsg = "配偶";
                break;
            case "担保人影像件资料":
                dialogMsg = "担保人";
                break;
            case "担保人配偶影像件资料":
                dialogMsg = "担保人配偶";
                break;
        }
        new AlertDialog.Builder(this)
                .setMessage("确认要更改" + dialogMsg + "信息？")
                .setCancelable(false)
                .setPositiveButton("确认更改", (dialog, which) -> {
                    //              UBT.addEvent(this, "onclick",new Button(this), getClass().getSimpleName());
//                    UBT.sendAllUBTEvents(this);
                    Intent intent = new Intent(UpdateInfoActivity.this, CommitActivity.class);
                    intent.putExtra("clt_id", clt_id);
                    intent.putExtra("role", role);
                    intent.putExtra("title", title);
                    intent.putExtra("commit_state", state);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("放弃更改", (dialog, which) -> dialog.dismiss())
                .show();
    }

}
