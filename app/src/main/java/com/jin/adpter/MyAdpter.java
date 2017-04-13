package com.jin.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jin.domain.ScoreSearchInfo;
import com.jin.slnews.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdpter extends BaseAdapter {
    // 填充数据的list  
    private ArrayList<ScoreSearchInfo> list;
    // 用来控制CheckBox的选中状况  
    private static HashMap<Integer, Boolean> isSelected;
    // 上下文  
    @SuppressWarnings("unused")
    private Context context;
    // 用来导入布局  
    private LayoutInflater inflater = null;

    // 构造器  
    @SuppressLint("UseSparseArrays")
    public MyAdpter(ArrayList<ScoreSearchInfo> list, Context context) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据  
        initDate();
    }

    // 初始化isSelected的数据  
    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            // 获得ViewHolder对象  
            holder = new ViewHolder();
            // 导入布局并赋值给convertview  
            convertView = inflater.inflate(R.layout.list_itme2, null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.body = (TextView) convertView.findViewById(R.id.body);
            holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
            // 为view设置标签  
            convertView.setTag(holder);
        } else {
            // 取出holder  
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置list中TextView的显示  
        String title = list.get(position).getName();
        String body = "学期:" + list.get(position).getXueqi() + ",学分:"
                + list.get(position).getCredit() + ",绩点:"
                + list.get(position).getGPA() + ",是否学位课:"
                + (list.get(position).isXueWei() ? "是" : "否");

        holder.title.setText(title);
        holder.body.setText(body);
        if (list.get(position).getGPA() < 0.1) {
            holder.title.setTextColor(Color.RED);
            holder.body.setTextColor(Color.RED);

        } else {
            holder.title.setTextColor(Color.BLACK);
            holder.body.setTextColor(Color.BLACK);

        }
        // 根据isSelected来设置checkbox的选中状况  
        holder.cb.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(int position) {
        MyAdpter.isSelected.put(position, true);
    }

    public static class ViewHolder {
        public TextView title;
        public TextView body;
        public CheckBox cb;
    }
}  