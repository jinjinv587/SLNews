package com.jin.domain;

import java.util.ArrayList;

/**
 * 网络分类信息的封装：国内、国际等(新闻页面下12个子页签的数据对象)
 * 
 * 字段名字必须和服务器返回的字段名一致,方便gson解析
 * 
 */
public class NewsData {
	
	public ArrayList<NewsTabData> news;
	
	// 数据对象
	public class NewsTabData {
		public String id;
		public String title;
		public int type;
		public String url;
		
		@Override
		public String toString() {
			return "NewsMenuData [id=" + id + ", title=" + title + ", type="
					+ type + ", url=" + url + "]";
		}
		
	}
	
	@Override
	public String toString() {
		return "NewsData [news=" + news + "]";
	}
	
}
