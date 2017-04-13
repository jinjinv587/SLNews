package com.jin.slnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jin.adpter.NewsAdpter;
import com.jin.adpter.TopNewsAdapter;
import com.jin.domain.NewsData.NewsTabData;
import com.jin.domain.TabData;
import com.jin.domain.TabData.TabNewsData;
import com.jin.domain.TabData.TopNewsData;
import com.jin.utils.CacheUtils;
import com.jin.utils.Constant;
import com.jin.views.RefreshListView;
import com.jin.views.RefreshListView.OnRefreshListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 页签详情页
 */
public class TabDetailPager implements OnPageChangeListener {
	public Activity mActivity;
	
	public View mRootView;// 根布局对象
	NewsTabData mTabData;// 页签数据
	
	private String mUrl;
	private TabData mTabDetailData;
	
	@ViewInject(R.id.vp_news)
	private ViewPager mViewPager;
	
	@ViewInject(R.id.tv_top_title)
	private TextView tv_top_title;// 头条新闻的标题
	
	private ArrayList<TopNewsData> mTopNewsList;// 头条新闻数据集合
	
	@ViewInject(R.id.lv_list)
	private RefreshListView lvList;// 新闻列表
	private ArrayList<TabNewsData> mNewsList; // 新闻数据集合
	private NewsAdpter mNewsAdpter;
	private String mMoreUrl;// 更多页面的地址
	private boolean shua;
	private Handler mHandler;
	
	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		mActivity = activity;
		mTabData = newsTabData;
		mUrl = Constant.BASE_URL + mTabData.url;
		mRootView = initViews();
		initData();
	}
	
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
		// 加载头布局
		ViewUtils.inject(this, view);
		View headerView = View.inflate(mActivity, R.layout.list_header_topnews,
				null);
		
		ViewUtils.inject(this, headerView);
		
		// 将头条新闻以头布局的形式加给listview
		lvList.addHeaderView(headerView);
		
		// 设置下拉刷新监听
		lvList.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				shua = true;
				getDataFromServer();
			}
			
			@Override
			public void onLoadMore() {
				if (mMoreUrl != null) {
					getMoreDataFromServer();
				} else {
					Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_SHORT)
							.show();
					lvList.onRefreshComplete(false);// 收起加载更多的布局
				}
			}
		});
		
		lvList.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("被点击:" + position);
				// 35311,34221,34234,34342
				// 在本地记录已读状态
				// String ids = PrefUtils.getString(mActivity, "read_ids", "");
				// String readId = mNewsList.get(position).id;
				// if (!ids.contains(readId)) {
				// ids = ids + readId + ",";
				// PrefUtils.setString(mActivity, "read_ids", ids);
				// }
				
				// mNewsAdpter.notifyDataSetChanged();
				// changeReadState(view);// 实现局部界面刷新, 这个view就是被点击的item布局对象
				
				// 跳转新闻详情页
				Intent intent = new Intent();
				intent.setClass(mActivity, NewsDetailActivity.class);
				intent.putExtra("url", mNewsList.get(position).url);
				 
				intent.putExtra("share_content", mNewsList.get(position).title);
				mActivity.startActivity(intent);
			}
		});
		
		return view;
	}
	
	// /**
	// * 改变已读新闻的颜色
	// */
	// private void changeReadState(View view) {
	// TextView tvTitle = (TextView) view.findViewById(R.id.tv_newstitle);
	// tvTitle.setTextColor(Color.GRAY);
	// }
	
	public void initData() {
		String cache = CacheUtils.getCache(mUrl, mActivity);
		
		if (!TextUtils.isEmpty(cache)) {
			parseData(cache, false);
		}
		
		getDataFromServer();
	}
	
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result =  responseInfo.result;
				// System.out.println("页签详情页返回结果:" + result);
				if (shua) {
					Toast.makeText(mActivity, "刷新成功！", Toast.LENGTH_SHORT)
							.show();
					shua = false;
				}
				parseData(result, false);
				lvList.onRefreshComplete(true);
				
				// 设置缓存
				CacheUtils.setCache(mUrl, result, mActivity);
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				if (shua) {
					Toast.makeText(mActivity, "刷新失败！", Toast.LENGTH_SHORT)
							.show();
					shua = false;
				}
				lvList.onRefreshComplete(false);
			}
		});
	}
	
	/**
	 * 加载下一页数据
	 */
	private void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				
				parseData(result, true);
				
				lvList.onRefreshComplete(true);
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				lvList.onRefreshComplete(false);
			}
		});
	}
	
	protected void parseData(String result, boolean isMore) {
		Gson gson = new Gson();
		mTabDetailData = gson.fromJson(result, TabData.class);
		// System.out.println("页签详情解析:" + mTabDetailData);
		
		// 处理下一页链接
		String more = mTabDetailData.data.more;
		if (!TextUtils.isEmpty(more)) {
			mMoreUrl = Constant.BASE_URL + more;
		} else {
			mMoreUrl = null;
		}
		
		if (!isMore) {
			mTopNewsList = mTabDetailData.data.topnews;
			
			mNewsList = mTabDetailData.data.news;
			
			if (mTopNewsList != null) {
				mViewPager.setAdapter(new TopNewsAdapter(mActivity,
						mTabDetailData));
				mViewPager.setOnPageChangeListener(this);//头条新闻设置滑动监听
				 
				tv_top_title.setText(mTopNewsList.get(0).title);
			}
			
			if (mNewsList != null) {
				mNewsAdpter = new NewsAdpter(mActivity, mNewsList);
				lvList.setAdapter(mNewsAdpter);
			}
			
			// 自动轮播条显示
			if (mHandler == null) {
				mHandler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						int currentItem = mViewPager.getCurrentItem();
						
						if (currentItem < mTopNewsList.size() - 1) {
							currentItem++;
							mViewPager.setCurrentItem(currentItem);// 切换到下一个页面
						} else {
							currentItem = 0;
							mViewPager.setCurrentItem(currentItem, false);// 切换到0,取消滑动效果
						}
						
						mHandler.sendEmptyMessageDelayed(0, 3000);// 在处理完后，继续延时3秒发消息,
																	// 形成循环
					};
				};
				
				mHandler.sendEmptyMessageDelayed(0, 3000);// 延时3秒后发消息
			}
			
		} else {// 如果是加载下一页,需要将数据追加给原来的集合
			ArrayList<TabNewsData> news = mTabDetailData.data.news;
			mNewsList.addAll(news);
			mNewsAdpter.notifyDataSetChanged();
		}
	}
	
	// /**
	// * 头条新闻的触摸监听
	// *
	// *
	// */
	// class TopNewsTouchListener implements OnTouchListener {
	//
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// switch (event.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// System.out.println("按下");
	// mHandler.removeCallbacksAndMessages(null);// 删除Handler中的所有消息
	// // mHandler.postDelayed(new Runnable() {
	// //
	// // @Override
	// // public void run() {
	// //
	// // }
	// // }, 3000);
	// break;
	// case MotionEvent.ACTION_CANCEL:
	// System.out.println("事件取消");
	// mHandler.sendEmptyMessageDelayed(0, 3000);
	// break;
	// case MotionEvent.ACTION_UP:
	// System.out.println("抬起");
	// mHandler.sendEmptyMessageDelayed(0, 3000);
	// break;
	//
	// default:
	// break;
	// }
	//
	// return true;
	// }
	//
	// }
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	
	@Override
	public void onPageSelected(int arg0) {
		TopNewsData topNewsData = mTopNewsList.get(arg0);
		tv_top_title.setText(topNewsData.title);
	}
}
