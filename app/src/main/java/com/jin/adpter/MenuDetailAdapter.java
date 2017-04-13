package com.jin.adpter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jin.domain.NewsData.NewsTabData;
import com.jin.domain.TabData;
import com.jin.slnews.NewsDetailActivity;
import com.jin.slnews.TabDetailPager;
import com.jin.utils.CacheUtils;
import com.jin.views.RefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MenuDetailAdapter extends PagerAdapter {
	private List<NewsTabData> mTabList;
	private Context mActivity;
	private boolean shua_flag = false;
	
	private ArrayList<TabDetailPager> mPagerList;
	
	public MenuDetailAdapter(Context mActivity, List<NewsTabData> mTabList,
			ArrayList<TabDetailPager> mPagerList) {
		
		this.mActivity = mActivity;
		this.mTabList = mTabList;
		this.mPagerList = mPagerList;
	}
	
	/**
	 * 重写此方法,返回页面标题,用于viewpagerIndicator的页签显示
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		return mTabList.get(position).title;
	}
	
	@Override
	public int getCount() {
		return mTabList.size();
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		TabDetailPager pager = mPagerList.get(position);
		container.addView(pager.mRootView);
		pager.initData();
		return pager.mRootView;
		
		// final RefreshListView refreshListView = (RefreshListView)
		// viewList.get(
		// position).findViewById(R.id.lv_list);
		// final ViewPager topNewsVP = (ViewPager) viewList.get(position)
		// .findViewById(R.id.vp_news);
		// final TextView tv_title = (TextView) viewList.get(position)
		// .findViewById(R.id.tv_top_title);
		// final String url = Constant.BASE_URL + mPagerList.get(position).url;
		// // 设置下拉刷新监听
		// refreshListView.setOnRefreshListener(new OnRefreshListener() {
		// @Override
		// public void onRefresh() {
		// shua_flag = true;
		// getData(url, refreshListView, topNewsVP, tv_title);
		// }
		//
		// @Override
		// public void onLoadMore() {
		// }
		// });
		// // System.out.println(refreshListView.getHeaderViewsCount());
		// String cache = CacheUtils.getCache(url, mActivity);
		// if (!TextUtils.isEmpty(cache)) {
		// parseData(cache, refreshListView, topNewsVP, tv_title);
		// }
		// getData(url, refreshListView, topNewsVP, tv_title);
		// container.addView(viewList.get(position));
		// // TabDetailPager pager = mPagerList.get(position);
		// // container.addView(pager.mRootView);
		// // pager.initData();
		// // return pager.mRootView;
		// return viewList.get(position);
	}
	
//	private void getData(final String url,
//			final RefreshListView refreshListView, final ViewPager topNewsVP,
//			final TextView tv_title) {
//		HttpUtils utils = new HttpUtils();
//		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
//			
//			// 访问成功, 在主线程运行
//			@Override
//			public void onSuccess(ResponseInfo<String> responseInfo) {
//				String result = (String) responseInfo.result;
//				// System.out.println("getData返回结果:" + result);
//				// 刷新完成调用次方法
//				if (shua_flag) {
//					refreshListView.onRefreshComplete(true);
//					shua_flag = false;
//					Toast.makeText(mActivity, "刷新成功！", Toast.LENGTH_LONG)
//							.show();
//				}
//				parseData(result, refreshListView, topNewsVP, tv_title);
//				
//				// 设置缓存
//				CacheUtils.setCache(url, result, mActivity);
//			}
//			
//			// 访问失败, 在主线程运行
//			@Override
//			public void onFailure(HttpException error, String msg) {
//				error.printStackTrace();
//				if (shua_flag) {
//					refreshListView.onRefreshComplete(false);
//					shua_flag = false;
//					Toast.makeText(mActivity, "刷新失败！", Toast.LENGTH_LONG)
//							.show();
//				}
//			}
//			
//		});
//	}
//	
//	private void parseData(String result, RefreshListView refreshListView,
//			ViewPager topNewsVP, final TextView tv_title) {
//		Gson gson = new Gson();
//		final TabData tabData = gson.fromJson(result, TabData.class);
//		// 给listview设置适配器
//		
//		tv_title.setText(tabData.data.topnews.get(0).title);
//		refreshListView
//				.setAdapter(new NewsAdapter(mActivity, tabData.data.news));
//		topNewsVP.setAdapter(new TopNewsAdapter(mActivity, tabData));
//		topNewsVP.setOnPageChangeListener(new OnPageChangeListener() {
//			
//			@Override
//			public void onPageSelected(int arg0) {
//				String title = tabData.data.topnews.get(arg0).title;
//				tv_title.setText(title);
//			}
//			
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//			}
//			
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//			}
//		});
//		refreshListView.setOnItemClickListener(new OnItemClickListener() {
//			
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// System.out.println("被点击:" + position);
//				//
//				// // 跳转新闻详情页
//				Intent intent = new Intent();
//				intent.setClass(mActivity, NewsDetailActivity.class);
//				intent.putExtra("url", tabData.data.news.get(position).url);
//				mActivity.startActivity(intent);
//			}
//		});
//	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
