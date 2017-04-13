package com.jin.slnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.jin.utils.AutoUpdate;
import com.jin.utils.ViewUtils;

public class SheZhiActivity extends Activity {
	TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_shezhi);
		ViewUtils.editStatusBarColor(this);
		title = (TextView) findViewById(R.id.tv_title);
		Intent intent = getIntent();
		int id = intent.getIntExtra("id", 0);

		switch (id) {
		case R.id.gnjs:
			title.setText("功能介绍");
			break;
		case R.id.lxwm:
			setContentView(R.layout.activity_shezhi_lxwm);
			break;
		case R.id.gy:
			setContentView(R.layout.activity_shezhi_gy);
			TextView tv = (TextView) findViewById(R.id.tv_show);
			String name = AutoUpdate.getVersionName(this);
			tv.setText("版本:" + name + "\n作者:小强");
			break;

		default:
			break;
		}
	}
}
