package com.jin.domain;

import java.util.ArrayList;

/**
 * 表白数据
 * 
 * @author 王金强
 * 
 */
public class Results {
	public ArrayList<BiaoBaiItem> list = new ArrayList<BiaoBaiItem>();
	
	@Override
	public String toString() {
		return "Results [list=" + list + "]";
	}
	
	public class BiaoBaiItem {
		public int id;
		public String person;
		public String content;
		public int zan;
		public String author;
		
		public BiaoBaiItem(int id, String person, String content, int zan,
				String author) {
			super();
			this.id = id;
			this.person = person;
			this.content = content;
			this.zan = zan;
			this.author = author;
		}
		
		@Override
		public String toString() {
			return "Item [id=" + id + ", person=" + person + ", content="
					+ content + ", zan=" + zan + ", author=" + author + "]";
		}
		
	}
}