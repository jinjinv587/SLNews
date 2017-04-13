package com.jin.fragment;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.jin.slnews.R;
import com.jin.slnews.SheZhiActivity;
import com.jin.utils.AutoUpdate;
import com.jin.utils.Constant;
import com.jin.utils.SharedPreferencesUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class WoDeFragment extends BaseFragment {
	@ViewInject(R.id.cb_autoupdate)
	CheckBox cb_autoupdate;
	
	@ViewInject(R.id.gnjs)
	RelativeLayout gnjs;
	
	@ViewInject(R.id.lxwm)
	RelativeLayout lxwm;
	
	@ViewInject(R.id.gy)
	RelativeLayout gy;
	
	MyListener listener = new MyListener();
	
	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_geren, null);
		ViewUtils.inject(this, view); // 注入view和事件
		
		return view;
	}
	
	@Override
	public void initData() {
		
		System.out.println("初始化个人数据！");
		
		gnjs.setOnClickListener(listener);
		lxwm.setOnClickListener(listener);
		gy.setOnClickListener(listener);
		cb_autoupdate.setChecked((Boolean) SharedPreferencesUtils.getParam(
				mActivity, "isAutoUpdate", true));
		cb_autoupdate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// 保存用户设置
				SharedPreferencesUtils.setParam(mActivity, "isAutoUpdate",
						isChecked);
				
				if (isChecked) {// 如果状态由未勾选到勾选，则立即检查更新
					AutoUpdate.check(mActivity, Constant.UPDATE_JSON_URL);
				}
			}
		});
		
	}
	
	class MyListener implements OnClickListener {
		Intent intent;
		
		@Override
		public void onClick(View v) {
			intent = new Intent(mActivity, SheZhiActivity.class);
			
			intent.putExtra("id", v.getId());
			
			startActivity(intent);
		}
		
	}
	
}
