package com.jin.fragment;

import android.view.View;

import com.jin.slnews.R;

public class ShouYeFragment extends BaseFragment {

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_shouye, null);

		return view;
	}

	@Override
	public void initData() {
		System.out.println("初始化首页数据！");
	}

}
