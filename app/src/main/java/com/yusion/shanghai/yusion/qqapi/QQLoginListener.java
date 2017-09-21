package com.yusion.shanghai.yusion.qqapi;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.opensdk.utils.Log;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.yusion.shanghai.yusion.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zz on 2017/9/21.
 */

public class QQLoginListener extends BaseActivity implements IUiListener {
    private UserInfo mInfo;
    @Override
    public void onComplete(Object o) {
        //TODO  同样都有QQ登录和分享的回调，这个可以分开写。
        //QQ登录先初始化openId 和 Token
        initOpenidAndToken(o);
        //再获取用户信息
        getUserInfo();

    }


    private void getUserInfo() {
        QQToken token = tencent.getQQToken();
        mInfo = new UserInfo(QQLoginListener.this, token);
        mInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object object) {
                JSONObject jb = (JSONObject) object;
                try {
//                    name = jb.getString("nickname");
//                    figureurl = jb.getString("figureurl_qq_2");  //头像图片的url
//                    nickName.setText(name);
//                    Picasso.with(QQLoginListener.this).load(figureurl).into(figure);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void initOpenidAndToken(Object object) {
        JSONObject jb = (JSONObject) object;
        try {
            String openID = jb.getString("openid"); //openid用户唯一标识
            String access_token = jb.getString("access_token");
            String expires = jb.getString("expires_in");

            tencent.setOpenId(openID);
            tencent.setAccessToken(access_token, expires);

            Log.e("qqlogin    ","openid："+openID);
            Log.e("qqlogin    ","access_token："+access_token);
            Log.e("qqlogin    ","expires："+expires);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
}
