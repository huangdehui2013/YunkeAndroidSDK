package com.shykad.yunke.sdk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.shykad.yunke.sdk.R;

/**
 * Create by wanghong.he on 2019/3/20.
 * description：信息流广告
 */
public class InfoFlowActivity extends PermissionActivity {

    private Button listBtn,recycleBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yunke_activity_infoflow_ad);
        init();
    }

    private void init() {
        listBtn = findViewById(R.id.infoflow_listview_btn);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adJump(InfoFlowListViewActivity.class);
            }
        });
        recycleBtn = findViewById(R.id.infoflow_recycleview_btn);
        recycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adJump(InfoFlowRecycleViewActivity.class);
            }
        });
    }

    private void adJump(Class<?> cls){

        startActivity(new Intent(InfoFlowActivity.this, cls));
    }
}
