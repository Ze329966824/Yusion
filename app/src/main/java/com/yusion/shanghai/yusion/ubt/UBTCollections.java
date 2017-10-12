package com.yusion.shanghai.yusion.ubt;

import android.util.Pair;

import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity;
import com.yusion.shanghai.yusion.ui.apply.ApplyActivity;
import com.yusion.shanghai.yusion.ui.apply.AutonymCertifyFragment;
import com.yusion.shanghai.yusion.ui.apply.PersonalInfoFragment;
import com.yusion.shanghai.yusion.ui.apply.SpouseInfoFragment;
import com.yusion.shanghai.yusion.ui.apply.guarantor.AddGuarantorActivity;
import com.yusion.shanghai.yusion.ui.apply.guarantor.DocmtActivity;
import com.yusion.shanghai.yusion.ui.apply.guarantor.DocumentFromLabelListActivity;
import com.yusion.shanghai.yusion.ui.apply.guarantor.GuarantorCreditInfoFragment;
import com.yusion.shanghai.yusion.ui.apply.guarantor.GuarantorInfoFragment;
import com.yusion.shanghai.yusion.ui.apply.guarantor.GuarantorSpouseInfoFragment;
import com.yusion.shanghai.yusion.ui.entrance.AgreeMentActivity;
import com.yusion.shanghai.yusion.ui.entrance.LaunchActivity;
import com.yusion.shanghai.yusion.ui.entrance.LoginActivity;
import com.yusion.shanghai.yusion.ui.entrance.MainActivity;
import com.yusion.shanghai.yusion.ui.entrance.WebViewActivity;
import com.yusion.shanghai.yusion.ui.main.HomeFragment;
import com.yusion.shanghai.yusion.ui.main.MineFragment;
import com.yusion.shanghai.yusion.ui.main.MyOrderFragment;
import com.yusion.shanghai.yusion.ui.main.mine.SettingsActivity;
import com.yusion.shanghai.yusion.ui.order.FinancePlanActivity;
import com.yusion.shanghai.yusion.ui.order.OrderDetailActivity;
import com.yusion.shanghai.yusion.ui.update.CommitActivity;
import com.yusion.shanghai.yusion.ui.update.ImgsListFragment;
import com.yusion.shanghai.yusion.ui.update.InfoListActivity;
import com.yusion.shanghai.yusion.ui.update.InfoListFragment;
import com.yusion.shanghai.yusion.ui.update.UpdateGuarantorInfoActivity;
import com.yusion.shanghai.yusion.ui.update.UpdateGuarantorSpouseInfoActivity;
import com.yusion.shanghai.yusion.ui.update.UpdatePersonalInfoActivity;
import com.yusion.shanghai.yusion.ui.update.UpdateSpouseInfoActivity;
import com.yusion.shanghai.yusion.ui.upload.OnlyReadUploadListActivity;
import com.yusion.shanghai.yusion.ui.upload.PreviewActivity;
import com.yusion.shanghai.yusion.ui.upload.UploadLabelListActivity;
import com.yusion.shanghai.yusion.ui.upload.UploadListActivity;

import java.util.HashMap;

/**
 * Created by ice on 2017/9/26.
 */

public class UBTCollections {
    public static HashMap<String, Pair<String, String>> pageNameMaps = new HashMap<>();

    static {
        pageNameMaps.put(LaunchActivity.class.getSimpleName(), new Pair<>("launch", "启动页面"));
        pageNameMaps.put(LoginActivity.class.getSimpleName(), new Pair<>("login", "登陆页面"));
        pageNameMaps.put(WebViewActivity.class.getSimpleName(), new Pair<>("webview", "H5页面"));
        pageNameMaps.put(AgreeMentActivity.class.getSimpleName(), new Pair<>("agreement", "协议页面"));
        pageNameMaps.put(MainActivity.class.getSimpleName(), new Pair<>("home_order_mine", "首页-订单-我的页面"));

        pageNameMaps.put(HomeFragment.class.getSimpleName(), new Pair<>("home", "首页"));
        pageNameMaps.put(MyOrderFragment.class.getSimpleName(), new Pair<>("order_list", "订单列表页面"));
        pageNameMaps.put(MineFragment.class.getSimpleName(), new Pair<>("mine", "个人信息页面"));

        pageNameMaps.put(SettingsActivity.class.getSimpleName(), new Pair<>("settings", "设置页面"));

        pageNameMaps.put(FinancePlanActivity.class.getSimpleName(), new Pair<>("finance_plan", "金融方案页面"));
        pageNameMaps.put(OrderDetailActivity.class.getSimpleName(), new Pair<>("order_detail", "订单详情页面"));

        pageNameMaps.put(CommitActivity.class.getSimpleName(), new Pair<>("commit_apply_success", "提交申请成功页面"));
        pageNameMaps.put(ImgsListFragment.class.getSimpleName(), new Pair<>("info_img", "资料列表-影像件页面"));
        pageNameMaps.put(InfoListActivity.class.getSimpleName(), new Pair<>("info", "资料列表页面"));
        pageNameMaps.put(InfoListFragment.class.getSimpleName(), new Pair<>("info_info", "资料列表-资料页面"));

        pageNameMaps.put(UpdateGuarantorInfoActivity.class.getSimpleName(), new Pair<>("update_guarantor", "修改担保人资料页面"));
        pageNameMaps.put(UpdateGuarantorSpouseInfoActivity.class.getSimpleName(), new Pair<>("update_guarantor_spouse", "修改担保人配偶资料页面"));
        pageNameMaps.put(UpdatePersonalInfoActivity.class.getSimpleName(), new Pair<>("update_lender", "修改个人资料页面"));
        pageNameMaps.put(UpdateSpouseInfoActivity.class.getSimpleName(), new Pair<>("update_lender_spouse", "修改个人配偶资料页面"));

        pageNameMaps.put(ApplyActivity.class.getSimpleName(), new Pair<>("lender_apply", "主贷人申请页面"));
        pageNameMaps.put(AutonymCertifyFragment.class.getSimpleName(), new Pair<>("lender_apply_credit", "主贷人-征信信息页面"));
        pageNameMaps.put(PersonalInfoFragment.class.getSimpleName(), new Pair<>("lender_apply_personal", "主贷人-个人信息页面"));
        pageNameMaps.put(SpouseInfoFragment.class.getSimpleName(), new Pair<>("lender_apply_spouse", "主贷人-配偶页面"));
        pageNameMaps.put(AddGuarantorActivity.class.getSimpleName(), new Pair<>("guarantor_apply", "担保人申请页面"));
        pageNameMaps.put(GuarantorCreditInfoFragment.class.getSimpleName(), new Pair<>("guarantor_apply_personal", "担保人-征信信息页面"));
        pageNameMaps.put(GuarantorInfoFragment.class.getSimpleName(), new Pair<>("guarantor_apply_personal", "担保人-个人信息页面"));
        pageNameMaps.put(GuarantorSpouseInfoFragment.class.getSimpleName(), new Pair<>("guarantor_apply_personal", "担保人-配偶页面"));
        pageNameMaps.put(AMapPoiListActivity.class.getSimpleName(), new Pair<>("poi_address", "高德地图POI检索界面"));

        pageNameMaps.put(DocmtActivity.class.getSimpleName(), new Pair<>("single_img_upload", "单张图片上传界面(身份证正反面,驾照)"));
        pageNameMaps.put(DocumentFromLabelListActivity.class.getSimpleName(), new Pair<>("multi_img_upload", "多张图片上传界面"));

        pageNameMaps.put(PreviewActivity.class.getSimpleName(), new Pair<>("img_preview", "图片预览界面"));
        pageNameMaps.put(OnlyReadUploadListActivity.class.getSimpleName(), new Pair<>("img_authorization_book", "授权书查看页面"));
        pageNameMaps.put(UploadLabelListActivity.class.getSimpleName(), new Pair<>("upload_label_list", "上传影像件标签列表页面"));
        pageNameMaps.put(UploadListActivity.class.getSimpleName(), new Pair<>("upload_img_list", "上传影像件列表页面"));
    }

    public static String getPageNm(String key) {
        Pair<String, String> pair = pageNameMaps.get(key);
        if (pair == null) {
            return "";
        } else {
            return pair.first;
        }
    }

    public static String getPageNmCn(String key) {
        Pair<String, String> pair = pageNameMaps.get(key);
        if (pair == null) {
            return "";
        } else {
            return pair.second;
        }
    }
}