package com.shykad.yunke.sdk.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.shykad.yunke.sdk.R;

/**
 * Create by wanghong.he on 2019/3/20.
 * description：listview 信息流广告
 */
public class InfoFlowListViewActivity extends PermissionActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yunke_activity_infoflow_list_ad);
    }
}
