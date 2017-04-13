package com.jin.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	public static List<String> getList(String xueweike) {
		// 创建一个list集合
		List<String> list = new ArrayList<String>();
		// 定义一个String类型的s数组来接收 s字符串用"。"分割以后的字符 串
		//xueweike = xueweike.replaceAll("all", "");
		String[] s = xueweike.split("。");
		for (int i = 0; i < s.length; i++) {
			
			list.add(s[i]);
			
		}
		
		// 用
		// for (int i = 0; i < s.length; i++) {
		// String temp = "";
		// for (int j = i + 1; j < s.length; j++) {
		// if (Integer.parseInt(s[i].substring(0, 6)) > Integer
		// .parseInt(s[j].substring(0, 6))) {
		// temp = s[j];
		// s[j] = s[i];
		// s[i] = temp;
		//
		// }
		// }
		// }
		List<String> list1 = new ArrayList<String>();
		for (int i = 0; i < s.length; i++) {
			list1.add(s[i]);
		}
		// System.out.println(list1);
		return list1;
	}
}
