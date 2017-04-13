package com.jin.adpter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jin.domain.TabData.TabNewsData;
import com.jin.slnews.R;
import com.jin.utils.Constant;
import com.jin.utils.PrefUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * 新闻列表的适配器
 * 
 * @author 王金强
 * 
 */
public class NewsAdpter extends BaseAdapter {
	
	private BitmapUtils utils;
	private Context mActivity;
	private ArrayList<TabNewsData> mNewsList;
	
	// private MyBitmapUtils utils;
	
	public NewsAdpter(Context mActivity, ArrayList<TabNewsData> mNewsList) {
		this.mActivity = mActivity;
		this.mNewsList = mNewsList;
		
		utils = new BitmapUtils(mActivity);
		utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		// utils = new MyBitmapUtils();
	}
	
	@Override
	public int getCount() {
		return mNewsList.size();
	}
	
	@Override
	public TabNewsData getItem(int position) {
		return mNewsList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View
					.inflate(mActivity, R.layout.list_news_item, null);
			holder = new ViewHolder();
			holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_newstitle);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		TabNewsData item = getItem(position);
		
		holder.tvTitle.setText(item.title);
		holder.tvDate.setText(item.pubdate);
		if (item.listimage.startsWith("http:")) {
			utils.display(holder.ivPic, item.listimage);
			
		} else {
			utils.display(holder.ivPic, Constant.BASE_URL + item.listimage);
			
		}
		// String ids = PrefUtils.getString(mActivity, "read_ids", "");
		// if (ids.contains(getItem(position).id)) {
		// holder.tvTitle.setTextColor(Color.GRAY);
		// } else {
		// holder.tvTitle.setTextColor(Color.BLACK);
		// }
		
		return convertView;
	}
	
	static class ViewHolder {
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView ivPic;
	}
	
}
