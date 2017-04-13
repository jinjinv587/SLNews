package com.jin.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.jin.slnews.R;

public class ViewUtils {
	/**
	 * 功能描述：编辑状态的颜色
	 * 
	 * @param context
	 *            activity
	 */
	public static void editStatusBarColor(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 状态栏透明 需要在创建SystemBarTintManager 之前调用。
			setTranslucentStatus((Activity) context, true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager((Activity) context);
		tintManager.setStatusBarTintEnabled(true);
		// 使StatusBarTintView 和 actionbar的颜色保持一致，风格统一。
		tintManager.setStatusBarTintResource(R.color.top_bar_normal_bg);
	}

	@TargetApi(19)
	public static void setTranslucentStatus(Activity activity, boolean on) {

		Window win = activity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);

	}

}
