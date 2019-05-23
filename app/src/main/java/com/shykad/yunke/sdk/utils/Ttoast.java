package com.shykad.yunke.sdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Create by hanweiwei on 11/07/2018
 * @author 38302
 */
public final class Ttoast {
    private static Toast sToast;

    public static void show(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String msg, int duration) {
        Toast toast = getToast(context);
        if (toast != null) {
            toast.setDuration(duration);
            toast.setText(String.valueOf(msg));
            toast.show();
        } else {
            LogUtils.i("Ttoast", "toast msg: " + String.valueOf(msg));
        }
    }

    @SuppressLint("ShowToast")
    private static Toast getToast(Context context) {
        if (context == null) {
            return sToast;
        }
        if (sToast == null) {
            synchronized (Ttoast.class) {
                if (sToast == null) {
                    sToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
                }
            }
        }
        return sToast;
    }

}
