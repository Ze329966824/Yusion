package com.yusion.shanghai.yusion.ui.info;

/**
 * Created by Administrator on 2017/8/4.
 */

//public class UpdateUserInfoActivity extends BaseActivity implements View.OnClickListener {
//
//    private String[] mTabTitle = {"个人信息", "配偶信息", "征信信息", "补充资料"};
//    private UploadLabelListFragment uploadLabelListFragment1;
//    private UploadLabelListFragment uploadLabelListFragment2;
//    private Dialog mUploadFileDialog;
//    private UpdatePersonalInfoFragment mPersonalInfoFragment;
//    private UserInfoBean mData;
//    private UpdateSpouseInfoFragment mSpouseInfoFragment;
//    public static List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlBeanList = new ArrayList<>();
//    private List<UploadLabelItemBean> pager1List;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_update_user_info);
//        uploadFileUrlBeanList.clear();
//        initTitleBar(this, "个人资料").setLeftClickListener(v -> showDoubleCheckForExit()).setRightTextColor(Color.WHITE).setRightText("提交").setRightClickListener(v -> updateUserInfo());
//        initView();
//    }
//
//    private void updateUserInfo() {
//        mPersonalInfoFragment.requestUpdateUserInfoBean(new OnVoidCallBack() {
//            @Override
//            public void callBack() {
//                mSpouseInfoFragment.requestUpdateUserInfoBean(new OnVoidCallBack() {
//                    @Override
//                    public void callBack() {
//                        ProductApi.updateUserInfo(UpdateUserInfoActivity.this, mData, new OnItemDataCallBack<UserInfoBean>() {
//                            @Override
//                            public void onItemDataCallBack(UserInfoBean data) {
//                                if (data == null) {
//                                    return;
//                                }
//                                Toast.makeText(UpdateUserInfoActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
//
//                                if (mUploadFileDialog == null) {
//                                    mUploadFileDialog = LoadingUtils.createLoadingDialog(UpdateUserInfoActivity.this);
//                                    mUploadFileDialog.setCancelable(false);
//                                }
//                                mUploadFileDialog.show();
//                                UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
////                                uploadFilesUrlReq.clt_id = mData.clt_id;
//                                uploadFilesUrlReq.files = uploadFileUrlBeanList;
//                                uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(UpdateUserInfoActivity.this).getValue("region", "");
//                                uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(UpdateUserInfoActivity.this).getValue("bucket", "");
//                                UploadApi.uploadFileUrl(UpdateUserInfoActivity.this, uploadFilesUrlReq, (code, msg) -> {
//                                    mUploadFileDialog.dismiss();
//                                    finish();
//                                });
//                            }
//                        });
//                    }
//                });
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        showDoubleCheckForExit();
//    }
//
//    private void showDoubleCheckForExit() {
//        new AlertDialog.Builder(this).setMessage("退出时未提交的更新将会被舍弃")
//                .setPositiveButton("确定该操作", (dialog, which) -> {
//                    dialog.dismiss();
//                    finish();
//                })
//                .setNegativeButton("取消该操作", (dialog, which) -> dialog.dismiss()).show();
//    }
//
//    private void initView() {
//        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
//        ArrayList<Fragment> mFragments = new ArrayList<>();
//        mPersonalInfoFragment = new UpdatePersonalInfoFragment();
//        mSpouseInfoFragment = new UpdateSpouseInfoFragment();
//        uploadLabelListFragment1 = null;
//        uploadLabelListFragment2 = null;
//        try {
//            JSONArray jsonArray = new JSONArray(YusionApp.CONFIG_RESP.client_material);
//            pager1List = new ArrayList<>();
//            List<UploadLabelItemBean> pager2List = new ArrayList<>();
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                UploadLabelItemBean uploadLabelItemBean = new Gson().fromJson(jsonObject.toString(), UploadLabelItemBean.class);
//                if (uploadLabelItemBean.value.equals("lender")) {
//                    pager1List.addAll(uploadLabelItemBean.label_list);
//                    uploadLabelListFragment1 = UploadLabelListFragment.newInstance(pager1List, "page1");
//                    continue;
//                }
//                pager2List.add(uploadLabelItemBean);
//            }
//            uploadLabelListFragment2 = UploadLabelListFragment.newInstance(pager2List, "page2");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        mFragments.add(mPersonalInfoFragment);
//        mFragments.add(mSpouseInfoFragment);
//        mFragments.add(uploadLabelListFragment1);
//        mFragments.add(uploadLabelListFragment2);
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.setCurrentItem(2);
//        viewPager.setAdapter(new InfoViewPagerAdapter(getSupportFragmentManager(), mFragments));
//
//        MagicIndicator mMagicIndicator = (MagicIndicator) findViewById(R.id.tab_layout);
//        CommonNavigator commonNavigator = new CommonNavigator(this);
//        commonNavigator.setAdjustMode(true);
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//            @Override
//            public int getCount() {
//                return mTabTitle == null ? 0 : mTabTitle.length;
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, final int index) {
//                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
//                colorTransitionPagerTitleView.setNormalColor(0xFF999999);
//                colorTransitionPagerTitleView.setSelectedColor(0xFF06B7A3);
//                colorTransitionPagerTitleView.setText(mTabTitle[index]);
//                colorTransitionPagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
//                return colorTransitionPagerTitleView;
//            }
//
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setColors(getResources().getColor(R.color.system_color));
//                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
//                return indicator;
//            }
//        });
//        mMagicIndicator.setNavigator(commonNavigator);
//
//        ViewPagerHelper.bind(mMagicIndicator, viewPager);
//
//        initData();
//    }
//
//    private void initData() {
//        ProductApi.getUserInfo(this, new GetClientInfoReq(), new OnItemDataCallBack<UserInfoBean>() {
//            @Override
//            public void onItemDataCallBack(UserInfoBean data) {
//                mData = data;
//                mPersonalInfoFragment.onRefresh(data);
//                mSpouseInfoFragment.onRefresh(data);
//
//                ListLabelsErrorReq req = new ListLabelsErrorReq();
//                req.clt_id = mData.clt_id;
//                ArrayList<String> labelsList = new ArrayList<>();
//                for (UploadLabelItemBean itemBean : pager1List) {
//                    labelsList.add(itemBean.value);
//                }
//                req.label_list = labelsList;
//                UploadApi.listLabelsError(UpdateUserInfoActivity.this, req, new OnItemDataCallBack<ListLabelsErrorResp>() {
//                    @Override
//                    public void onItemDataCallBack(ListLabelsErrorResp resp) {
//                        uploadLabelListFragment1.refresh(resp);
//                        uploadLabelListFragment2.refresh(resp);
//                    }
//                });
//            }
//        });
//    }
//
//    private class InfoViewPagerAdapter extends FragmentPagerAdapter {
//
//        private final List<Fragment> mFragments;
//
//        InfoViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
//            super(fm);
//            mFragments = fragments;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragments.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragments == null ? 0 : mFragments.size();
//        }
//
//    }
//
//    public UserInfoBean getUserInfoBean() {
//        return mData;
//    }
//}