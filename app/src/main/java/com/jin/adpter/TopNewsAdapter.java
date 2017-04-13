package com.jin.adpter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.jin.domain.TabData;
import com.jin.domain.TabData.TopNewsData;
import com.jin.slnews.NewsDetailActivity;
import com.jin.slnews.R;
import com.jin.utils.Constant;
import com.lidroid.xutils.BitmapUtils;

/**
 * 头条新闻适配器
 * 
 * @author 王金强
 * 
 */
public class TopNewsAdapter extends PagerAdapter {
	
	private BitmapUtils utils;
	private Context mActivity;
	private TabData mTabDetailData;
	private ArrayList<TopNewsData> mTopNewsList;
	
	public TopNewsAdapter(Context mActivity, TabData mTabDetailData) {
		this.mTabDetailData = mTabDetailData;
		
		this.mActivity = mActivity;
		utils = new BitmapUtils(mActivity);
		utils.configDefaultLoadingImage(R.drawable.topnews_item_default);// 设置默认图片
	}
	
	@Override
	public int getCount() {
		return mTabDetailData.data.topnews.size();
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		ImageView image = new ImageView(mActivity);
		image.setScaleType(ScaleType.FIT_XY);// 基于控件大小填充图片
		image.setClickable(true);
		mTopNewsList = mTabDetailData.data.topnews;
		TopNewsData topNewsData = mTopNewsList.get(position);
		if (topNewsData.topimage.startsWith("http:")) {
			utils.display(image, topNewsData.topimage);// 传递imagView对象和图片地址
		} else {
			utils.display(image, Constant.BASE_URL + topNewsData.topimage);// 传递imagView对象和图片地址
		}
		
		container.addView(image);
		
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// 跳转新闻详情页
				Intent intent = new Intent(mActivity, NewsDetailActivity.class);
				intent.putExtra("url", mTopNewsList.get(position).url);
				intent.putExtra("share_content",
						mTopNewsList.get(position).title);
				mActivity.startActivity(intent);
				
			}
		});
		
		return image;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}