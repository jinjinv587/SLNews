package com.jin.adpter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jin.domain.Results.BiaoBaiItem;
import com.jin.slnews.R;
import com.jin.utils.CacheUtils;

public class BiaoBaiAdpter extends BaseAdapter {
	static Context mActivity;
	static ArrayList<BiaoBaiItem> list;
	ZanListener zanListener;

	@SuppressLint("UseSparseArrays")
	public BiaoBaiAdpter(Context mActivity, ArrayList<BiaoBaiItem> list) {
		BiaoBaiAdpter.mActivity = mActivity;
		BiaoBaiAdpter.list = list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final String cahe = CacheUtils.getCache("zan", mActivity);
		ViewHolder holder;
		if (convertView == null) {
			// 获得ViewHolder对象
			holder = new ViewHolder();
			// 导入布局并赋值给convertview
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_biaobai, null);
			holder.id = (TextView) convertView.findViewById(R.id.tv_id);
			holder.person = (TextView) convertView.findViewById(R.id.tv_persion);
			holder.content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.iv_zan = (ImageView) convertView.findViewById(R.id.iv_zan);
			holder.zan = (TextView) convertView.findViewById(R.id.tv_zan);
			holder.author = (TextView) convertView.findViewById(R.id.tv_author);
			// 为view设置标签
			convertView.setTag(holder);
		} else {
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}
		holder.iv_zan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (cahe.contains(list.get(position).id + "")) {
					zanListener.zan(list.get(position).id, true);
				} else {
					zanListener.zan(list.get(position).id, false);
				}

			}
		});
		holder.id.setText(list.get(position).id + "");
		holder.person.setText(list.get(position).person);
		holder.content.setText(list.get(position).content);
		holder.zan.setText(list.get(position).zan + "");
		holder.author.setText(list.get(position).author);
		if (cahe.contains(list.get(position).id + "") && list.get(position).zan > 0) {
			holder.iv_zan.setImageResource(R.drawable.x2);
		} else {
			holder.iv_zan.setImageResource(R.drawable.x1);
		}
		return convertView;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	public void setOnZanListener(ZanListener zanListener) {
		this.zanListener = zanListener;
	}

	public interface ZanListener {
		void zan(int position, boolean flag);
	}

	public static void setIsLiked(int id) {
		// String cahe = CacheUtils.getCache("zan", mActivity);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).id == id) {
				list.get(i).zan++;
				break;
			}
		}

	}

	static class ViewHolder {
		public TextView id;
		public TextView person;
		public TextView content;
		public ImageView iv_zan;
		public TextView zan;
		public TextView author;

	}
}
