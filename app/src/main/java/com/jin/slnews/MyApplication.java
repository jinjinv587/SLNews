package com.jin.slnews;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
	private String xh = "1203030224";
	private String pw = "123456";

	@Override
	public void onCreate() {
		super.onCreate();
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
	}

}
