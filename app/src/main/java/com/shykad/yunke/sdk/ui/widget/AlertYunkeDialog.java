package com.shykad.yunke.sdk.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shykad.yunke.sdk.R;

import java.util.List;

/**
 *
 * @author WanghongHe
 * @date 2018/10/17 11:37
 */

public class AlertYunkeDialog extends Dialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayoutBg;
    private TextView txtTitle;
    private TextView txtMsg;
    private Button btnNeg;
    private Button btnPos;
    private ImageView imgLine;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;

    public AlertYunkeDialog(Context context) {
        super(context);
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(false);
    }

    public AlertYunkeDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.yunke_view_alertdialog, null);

        // 获取自定义Dialog布局中的控件
        lLayoutBg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtTitle.setVisibility(View.GONE);
        txtMsg = (TextView) view.findViewById(R.id.txt_msg);
        txtMsg.setVisibility(View.GONE);
        btnNeg = (Button) view.findViewById(R.id.btn_neg);
        btnNeg.setVisibility(View.GONE);
        btnPos = (Button) view.findViewById(R.id.btn_pos);
        btnPos.setVisibility(View.GONE);
        imgLine = (ImageView) view.findViewById(R.id.img_line);
        imgLine.setVisibility(View.GONE);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayoutBg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), FrameLayout.LayoutParams.WRAP_CONTENT));

        return this;
    }

    public AlertYunkeDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txtTitle.setText("标题");
        } else {
            txtTitle.setText(title);
        }
        return this;
    }

    public AlertYunkeDialog setCancelOutside(boolean able){
        setCancelable(able);
        return this;
    }

    public AlertYunkeDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txtMsg.setText("内容");
        } else {
            txtMsg.setText(msg);
        }
        return this;
    }

    /**
     * 慎用，待dialog build以后调用
     */
    @Override
    public void setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
    }

    public AlertYunkeDialog setPositiveButton(String text, final View.OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btnPos.setText("确定");
        } else {
            btnPos.setText(text);
        }
        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                listener.onClick(v);
            }
        });
        return this;
    }

    public AlertYunkeDialog setNegativeButton(String text, final View.OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btnNeg.setText("取消");
        } else {
            btnNeg.setText(text);
        }
        btnNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClick(v);

            }
        });
        return this;
    }

    private void setLayout() {
        if (!showTitle && !showMsg) {
            txtTitle.setText("提示");
            txtTitle.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txtTitle.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            txtMsg.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btnPos.setText("确定");
            btnPos.setVisibility(View.VISIBLE);
            btnPos.setBackgroundResource(R.drawable.yunke_alertdialog_single_selector);
            btnPos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btnPos.setVisibility(View.VISIBLE);
            btnPos.setBackgroundResource(R.drawable.yunke_alertdialog_right_selector);
            btnNeg.setVisibility(View.VISIBLE);
            btnNeg.setBackgroundResource(R.drawable.yunke_alertdialog_left_selector);
            imgLine.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btnPos.setVisibility(View.VISIBLE);
            btnPos.setBackgroundResource(R.drawable.yunke_alertdialog_single_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btnNeg.setVisibility(View.VISIBLE);
            btnNeg.setBackgroundResource(R.drawable.yunke_alertdialog_single_selector);
        }
    }

    @Override
    public void show() {
        setLayout();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public boolean isShow() {
        if (dialog != null && dialog.isShowing()) {
            return true;
        }
        return false;
    }

    @Override
    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
