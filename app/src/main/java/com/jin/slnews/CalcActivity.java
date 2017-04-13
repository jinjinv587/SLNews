package com.jin.slnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jin.adpter.MyAdpter;
import com.jin.adpter.MyAdpter.ViewHolder;
import com.jin.domain.ScoreSearchInfo;
import com.jin.utils.ViewUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CalcActivity extends Activity {
    /**
     * 得到的成绩信息集合
     */
    List<ScoreSearchInfo> list;
    /**
     * 显示成绩的ListView
     */
    private ListView lv;
    /**
     * 用于显示选中的条目数量
     */
    private TextView tv_show;
    /**
     * 全选按钮
     */
    private Button bt_selectall;
    /**
     * 计算学位课绩点
     */
    private Button bt_calc;
    private MyAdpter mAdapter;
    /**
     * 记录选中的条目数量
     */
    int checkNum = 0;

    float xuefen = 0;
    int xueweikesize;
    BigDecimal sums = new BigDecimal(0);

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_jidan);
        ViewUtils.editStatusBarColor(this);
        // 设置背景透明度
        findViewById(R.id.jd).getBackground().setAlpha(170);
        Intent intent = getIntent();
        list = (List<ScoreSearchInfo>) intent.getSerializableExtra("list");
        xueweikesize = intent.getIntExtra("xueweikesize", 100);
        lv = (ListView) findViewById(R.id.lv);
        tv_show = (TextView) findViewById(R.id.tv_show);
        bt_selectall = (Button) findViewById(R.id.bt_selectall);
        bt_calc = (Button) findViewById(R.id.bt_calc);
        // 绑定listView的监听器
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int postion, long arg3) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                ViewHolder holder = (ViewHolder) view.getTag();
                // 改变CheckBox的状态
                holder.cb.toggle();
                // 将CheckBox的选中状况记录下来
                MyAdpter.getIsSelected().put(postion, holder.cb.isChecked());
                // 调整选定条目
                if (holder.cb.isChecked()) {
                    sums = sums.add(new BigDecimal(list.get(postion).getCredit()).multiply(new BigDecimal(list.get(postion).getGPA())));
                    xuefen += list.get(postion).getCredit();
                    checkNum++;
                } else {
                    sums = sums.subtract(new BigDecimal(list.get(postion).getCredit()).multiply(new BigDecimal(list.get(postion).getGPA())));
                    xuefen -= list.get(postion).getCredit();
                    checkNum--;
                }
                if (checkNum == 0) {
                    tv_show.setText("未选中任何科目");
                } else {
                    tv_show.setText("已选平均绩点(共" + checkNum + "科):" + sums.divide(new BigDecimal(xuefen), 5, BigDecimal.ROUND_HALF_EVEN));
                }
                // 刷新listview
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter = new MyAdpter((ArrayList<ScoreSearchInfo>) list, this);
        lv.setAdapter(mAdapter);
        // 全选按钮
        bt_selectall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遍历list的长度，将MyAdapter中的map值全部设为true
                for (int i = 0; i < list.size(); i++) {
                    MyAdpter.getIsSelected().put(i, true);
                }
                // 数量设为list的长度
                // checkNum = list.size();
                sums = new BigDecimal(0);
                xuefen = 0;
                checkNum = 0;
                for (int i = 0; i < list.size(); i++) {
                    sums = sums.add(new BigDecimal(list.get(i).getCredit()).multiply(new BigDecimal(list.get(i).getGPA())));

                    xuefen += list.get(i).getCredit();
                    MyAdpter.getIsSelected().put(i, true);
                    checkNum++;
                }
                if (xuefen != 0) {
                    tv_show.setText("总平均绩点(共" + checkNum + "科):" + sums.divide(new BigDecimal(xuefen), 5, BigDecimal.ROUND_HALF_EVEN));
                }
                // 刷新listview
                mAdapter.notifyDataSetChanged();
            }
        });
        bt_calc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遍历list的长度，将MyAdapter中的map值全部设为true
                checkNum = 0;
                sums = new BigDecimal(0);
                xuefen = 0;
                for (int i = 0; i < list.size(); i++) {
                    MyAdpter.getIsSelected().put(i, list.get(i).isXueWei());//所有学位课都是true，包括重修
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isXueWei() && MyAdpter.getIsSelected().get(i)) {//过滤所有学位课
                        String name = list.get(i).getName();
                        for (int n = i + 1; n < list.size(); n++) {//后面
                            String tempName = list.get(n).getName();
                            if (list.get(n).isXueWei() && name.equals(tempName)) {
                                if (list.get(n).getGPA() >= list.get(i).getGPA() && list.get(n).getGPA() > 0) {
                                    MyAdpter.getIsSelected().put(n, true);
                                    MyAdpter.getIsSelected().put(i, false);
                                } else {
                                    MyAdpter.getIsSelected().put(n, false);
                                }
                            }
                        }


//                        for (int n = 0; n < i; n++) {//前面
//                            if (list.get(n).isXueWei()) {//过滤所有学位课
//                                //只要有比他大的，就设置false
//                                if (list.get(i).getName().equals(list.get(n).getName()) && list.get(n).getGPA() > list.get(i).getGPA()) {
//                                    MyAdpter.getIsSelected().put(i, false);
//                                }
//                            }
//
//                        }
//                        for (int n = i + 1; n < list.size(); n++) {//后面
//                            if (list.get(n).isXueWei()) {//过滤所有学位课
//                                //只要有比他大的，就设置false
//                                if (list.get(i).getName().equals(list.get(n).getName()) && list.get(n).getGPA() > list.get(i).getGPA()) {
//                                    MyAdpter.getIsSelected().put(i, false);
//                                }
//                            }
//
//                        }
                    }
                }

                int x = 0;
                sums = new BigDecimal(0);
                xuefen = 0;
                for (int n = 0; n < list.size(); n++) {
                    if (MyAdpter.getIsSelected().get(n)) {
                        x++;
                        sums = sums.add(new BigDecimal(list.get(n).getCredit()).multiply(new BigDecimal(list.get(n).getGPA())));
                        xuefen += list.get(n).getCredit();
                    }
                }
                checkNum = x;
                if (checkNum == 0) {
                    Toast.makeText(CalcActivity.this, "学位课信息有误！请返回重新加载！", Toast.LENGTH_LONG).show();
                } else {
                    tv_show.setText("学位课绩点(共" + x + "科):" + sums.divide(new BigDecimal(xuefen), 5, BigDecimal.ROUND_HALF_EVEN));
                }
                // 刷新listview和TextView的显示
                mAdapter.notifyDataSetChanged();
            }
        });
    }


}
