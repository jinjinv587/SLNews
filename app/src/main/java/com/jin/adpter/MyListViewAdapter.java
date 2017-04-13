package com.jin.adpter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jin.domain.ScoreSearchInfo;
import com.jin.slnews.R;

import java.util.ArrayList;

public class MyListViewAdapter extends BaseAdapter {
    private ArrayList<ScoreSearchInfo> list;
    private Activity mActivity;

    public MyListViewAdapter(ArrayList<ScoreSearchInfo> list, Activity mActivity) {
        this.list = list;
        this.mActivity = mActivity;
    }

    public void setData(ArrayList<ScoreSearchInfo> list) {
        this.list = list;
    }

    // 控制listview里面一共有多少条数据
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_itme, null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.body = (TextView) convertView.findViewById(R.id.body);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String title = list.get(position).getName();
        String body = "学期:" + list.get(position).getXueqi() + ",学分:"
                + list.get(position).getCredit() + ",平时:"
                + list.get(position).getUsusal() + "\n期末成绩:"
                + list.get(position).getTerminal() + ",成绩:"
                + list.get(position).getResult() + ",绩点:"
                + list.get(position).getGPA();
        // 绩点是0的，变成红颜色
        if (list.get(position).getGPA() < 0.1) {
            holder.title.setTextColor(Color.RED);
            holder.body.setTextColor(Color.RED);

        } else {
            holder.title.setTextColor(Color.BLACK);
            holder.body.setTextColor(Color.BLACK);

        }
        holder.title.setText(title);
        holder.body.setText(body);
        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView body;
    }
}
