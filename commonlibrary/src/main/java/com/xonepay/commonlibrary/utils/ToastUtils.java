package com.xonepay.commonlibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wjq on 2017年4月14日19:47:53.
 */
public class ToastUtils {
    private static Toast mToast = null;

    public static void show(Context context, String info) {
        if (mToast == null) {
            mToast = Toast.makeText(context, info, Toast.LENGTH_LONG);
        } else {
            mToast.setText(info);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public static void showShort(Context context, String info) {
        if (mToast == null) {
            mToast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(info);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
