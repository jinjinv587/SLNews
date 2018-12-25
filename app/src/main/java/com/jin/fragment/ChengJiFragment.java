package com.jin.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jin.adpter.MyListViewAdapter;
import com.jin.domain.ScoreSearchInfo;
import com.jin.slnews.BuildConfig;
import com.jin.slnews.CalcActivity;
import com.jin.slnews.R;
import com.jin.slnews.ScoreSearchDown;
import com.jin.slnews.XeiWeiKeSearchDown;
import com.jin.utils.CacheUtils;
import com.jin.utils.SharedPreferencesUtils;
import com.jin.utils.Utils;
import com.jin.views.CustomDialog;
import com.lidroid.xutils.ViewUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChengJiFragment extends BaseFragment {
    /**
     * 访问出错标志
     */
    protected static final int ERROR = 1;
    /**
     * Toast提示信息标志
     */
    public static final int SHOW_TEXT = 2;
    /**
     * 成功标志
     */
    protected static final int SUCCESSED = 3;
    /**
     * 计算绩点准备标志
     */
    protected static final int OK = 4;
    /**
     * 显示成绩的ListView
     */
    private ListView lv;
    /**
     * 学号输入框
     */
    private EditText et_xh;
    /**
     * 密码输入框
     */
    private EditText et_pw;
    /**
     * 得到的成绩信息集合
     */
    private ArrayList<ScoreSearchInfo> list = new ArrayList();
    /**
     * 学位课
     */
    private String xueweike = "";
    /**
     * 消息机制
     */
    private Button calc;
    /**
     * 访问教学计划进度，最大是5
     */
    //private int i = 0;

    /**
     * 跳转标志
     */
    boolean tiao = false;
    /**
     * 是否正在加载学位课信息标志
     */
    public static boolean isLoading = false;
    /**
     * 进度条
     */
    private ProgressDialog progressDialog = null;
    Intent intent;

    private CheckBox show = null;
    MyListViewAdapter myListViewAdapter = null;
    public static Handler handler;

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_chengji, null);
        ViewUtils.inject(this, view); // 注入view和事件
        view.findViewById(R.id.tv_kefu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkApkExist(mActivity, "com.tencent.mobileqq")) {
                    mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=913214983&version=1")));
                } else {
                    Toast.makeText(mActivity, "检查到您手机没有安装QQ，请安装后使用该功能", Toast.LENGTH_LONG).show();
//                    DialogUtils.alertInfo(mActivity, "检查到您手机没有安装QQ，请安装后使用该功能");
                }
            }
        });
        return view;
    }

    @Override
    public void initData() {
        initHandler();
        System.out.println("初始化成绩");
        if (((String) SharedPreferencesUtils.getParam(mActivity, "120303", "")).isEmpty()) {
            System.out.println("SP无数据");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String allxueweike = //"120303=C语言程序设计B,大学英语1,高等数学B1,大学英语2,高等数学B2,线性代数D,电路,模拟电子技术A,大学英语3,数字电子技术A,信号与系统,高频电子电路,大学英语4,数字信号处理,通信系统,单片机原理与接口技术,数字图象处理,计算机网络与通信,DSP技术,DSP技术综合设计,all。" +
                            "120902=大学英语1,高等数学A1,C语言程序设计,大学英语2,高等数学A2,基础物理学1,现代应用光学,大学英语3,数学物理方法,基础物理学2,大学英语4,物理光学,电子技术基础,光电子学,量子力学,激光原理,光电检测原理与技术,光信息处理,光信息技术实验1,激光器件与技术,光信息技术实验2,all。"
                                    + "130501=工程制图A1,大学英语1,高等数学D1,工程制图A2,大学英语2,高等数学D2,线性代数D,大学物理C,大学英语3,理论力学B,材料力学性能,金属学与热处理,大学英语4,材料成型原理1,材料成型原理2,铸造工艺,铸造合金与熔炼,铸造设备,塑料成型工艺与模具设计,冲压工艺与模具设计,热成型工艺与模具设计,金属焊接原理与焊接性,焊接方法与设备,焊接结构,砂型铸造工艺课程设计,冲压模具课程设计,焊接工艺课程设计,all。"
                                    + "120302=C语言程序设计B,大学英语1,高等数学B1,大学英语2,高等数学B2,线性代数D,电路,模拟电子技术A,大学英语3,概率论与数理统计C,数字电子技术A,信号与系统（基础）,大学英语4,传感器与检测技术,单片机原理与接口技术,自动控制原理（基础）,电子测量技术,智能仪器仪表,测控系统综合课程设计,all。"
                                    + "120101=工程制图B1,大学英语1,高等数学B1,大学英语2,高等数学B2,线性代数D,大学英语3,理论力学B,机械设计A1,电工与电子技术A,大学英语4,材料力学B,机械设计A2,机电传动控制,单片机原理及应用,机械控制工程基础,有限元法,机械系统控制技术,all。"
                                    + "120901=大学英语1,数学分析1,高等代数1,空间解析几何,大学英语2,数学分析2,高等代数2,C语言程序设计,大学英语3,面向对象程序设计（Java）,数学分析3,大学英语4,概率论与数理统计,常微分方程,数据结构,数据库原理,软件工程,数值分析,算法设计与分析,算法设计与分析课程设计,数学模型,all。"
                                    + "120501=工程制图A1,大学英语1,高等数学D1,工程制图A2,大学英语2,高等数学D2,线性代数D,大学物理C,大学英语3,理论力学B,材料力学性能,金属学与热处理,大学英语4,材料力学C,材料成型原理1,材料成型原理2,铸造工艺,铸造合金与熔炼,铸造设备,塑料成型工艺与模具设计,冲压工艺与模具设计,热成型工艺与模具设计,金属焊接原理与焊接性,焊接方法与设备,焊接结构,砂型铸造工艺课程设计,冲压模具课程设计,焊接工艺课程设计,all。"
                                    + "120904=大学英语1,高等数学A1,C语言程序设计,大学英语2,高等数学A2,基础物理学1,现代应用光学,大学英语3,基础物理学2,模拟电路,大学英语4,数学物理方法,物理光学,数字逻辑设计及应用,光信息处理,光电子学,半导体物理与器件,数字图像处理技术,信息显示与光电技术实验1,信息显示与技术,光电成像原理与技术,信息显示与光电技术实验2,all。"
                                    + "121104=工程制图D,大学英语1,高等数学C1,计算机程序设计,大学英语2,高等数学C2,大学英语3,理论力学B,大学英语4,材料力学C,机械设计B,有限元法及其应用,枪炮内弹道学,武器制造工艺学,气体动力学,火炮测试技术,火炮设计理论,自动武器原理与构造,火炮设计课程设计,all。"
                                    + "120307=C语言程序设计A,大学英语1,高等数学B1,大学英语2,线性代数D,电路,模拟电子技术A,面向对象程序设计,大学英语3,数字电子技术A,微机原理与汇编语言,大学英语4,数据库原理与应用,单片机,数字系统与VHDL,计算机网络,电子系统设计,专业方向综合实训2,all。";

                    List<String> list = Utils.getList(allxueweike);
                    for (int i = 0; i < list.size(); i++) {
                        SharedPreferencesUtils.setParam(mActivity, list.get(i).substring(0, 6), list.get(i).substring(7, list.get(i).length()));

                    }
                }
            }).start();
        } else {
            System.out.println("SP已有数据");
        }
        // 设置背景透明度
        mActivity.findViewById(R.id.bg).getBackground().setAlpha(80);
        // 通过id找到界面中的计算按钮
        calc = (Button) mActivity.findViewById(R.id.calc);
        Button bt = (Button) mActivity.findViewById(R.id.bt);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLoading) {
                    tiao = true;
                    progressDialog = ProgressDialog.show(mActivity, "请稍等...", "正在加载学位课信息，首次加载会比较慢...", true);
                    return;
                }
                intent = new Intent(mActivity, CalcActivity.class);
                intent.putExtra("list", list);
                int xueweikesize = 0;
                String xwk = (String) SharedPreferencesUtils.getParam(mActivity, SharedPreferencesUtils.getParam(mActivity, "userxh", "1203030224").toString().trim().substring(0, 6), "");
                // 为每个学科添加学位课标志
                for (ScoreSearchInfo s : list) {
                    if (xwk.contains(s.getName())) {
                        s.setIsXueWei(true);
                        xueweikesize++;
                    } else {
                        s.setIsXueWei(false);
                    }
                }
                intent.putExtra("xueweikesize", xueweikesize);

                dialog();

            }
        });
        calc.setVisibility(View.INVISIBLE);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /**
                 * 得到输入框里面的学号
                 */
                final String xh = et_xh.getText().toString().trim();
                /**
                 * 得到输入框里面的密码
                 */
                final String pw = et_pw.getText().toString().trim();
                // 判断得到的学号和密码是否为空
                if (TextUtils.isEmpty(xh) || TextUtils.isEmpty(pw)) {
                    // 如果为空，则弹出提示并返回
                    Toast.makeText(mActivity, "学号或密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // 如果不为空，则立即拿此学号和密码去登陆，查询成绩，并保存学号密码到手机
                    new ScoreSearchDown(xh, pw, handler, mActivity).getSocre();
                    String xuewei = (String) SharedPreferencesUtils.getParam(mActivity, et_xh.getText().toString().trim().substring(0, 6), "");
                    if (xuewei.equals("") || xuewei.length() == 0) {
                        // 查询所有学位课
                        new XeiWeiKeSearchDown(mActivity, xh, pw, handler).getSocre(true);
                        isLoading = true;
                        // progressDialog = ProgressDialog.show(mActivity,
                        // "请稍等...", "获取数据中...", true);
                        Toast.makeText(mActivity, "正在加载数据，首次加载学位课会很慢，请稍等...", Toast.LENGTH_LONG).show();
                    }
                    // 提示用户正在查询
                    Toast.makeText(mActivity, "查询中，请稍等...", Toast.LENGTH_LONG).show();

                }
            }

        });
        // 通过id找到界面中的listview
        lv = (ListView) mActivity.findViewById(R.id.lv);
        // 通过id找到界面中的学号输入框EditText
        et_xh = (EditText) mActivity.findViewById(R.id.et_xh);
        // 通过id找到界面中的密码输入框EditText
        et_pw = (EditText) mActivity.findViewById(R.id.et_pw);
        // 得到SharedPreferences中的学号和密码
        String sp_userxh = (String) SharedPreferencesUtils.getParam(mActivity, "userxh", "");
        String sp_password = (String) SharedPreferencesUtils.getParam(mActivity, "password", "");
        show = (CheckBox) mActivity.findViewById(R.id.login_switchBtn);
        show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (show.isChecked()) {
                    // 设置为明文显示
                    et_pw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // 设置为秘闻显示
                    et_pw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }

        });
        // 如果sp里面有数据就读取
        if (sp_userxh != null && sp_password != null && sp_password != "" && sp_userxh != "") {
            // 并自动填充到学号密码的输入框
            et_xh.setText(sp_userxh);
            et_pw.setText(sp_password);
            // 为了增加用户体验，如果SharedPreferences中有学号和密码，则立即查询，减少用户等待时间
            new ScoreSearchDown(sp_userxh, sp_password, handler, mActivity).getSocre();
            String xuewei = (String) SharedPreferencesUtils.getParam(mActivity, et_xh.getText().toString().trim().substring(0, 6), "");
            if ("".equals(xuewei) || xuewei.length() == 0) {
                // 查询所有学位课
                new XeiWeiKeSearchDown(mActivity, sp_userxh, sp_password, handler).getSocre(true);
            } else {
//				System.out.println("SharedPreferences里面的学位课信息:" + xuewei);
            }
            String sss = CacheUtils.getCache(sp_userxh, mActivity);

            /**
             * 使用JSuop解析html数据
             */
            Document doc = Jsoup.parse(sss); // 把HTML代码加载到doc中

            Elements ele = doc.select("table[class=datelist]"); // table
            // class="formlist"
            // Log.i("TAG", "---Result" +
            // ele.text().toString());
            Elements elee = ele.select("tr");
            Log.i("TAG", "---elee.size()=" + elee.size());
            ScoreSearchInfo info;
            for (int i = 1; i < elee.size(); i++) {
                info = new ScoreSearchInfo();
                // Log.i("TAG", "---Result"
                // + elee.get(i).text().toString());
                //
                Elements result = elee.get(i).select("td");
                info.setPapermakeupinfo(result.get(15).text());
                info.setPapermakeupinfo(result.get(14).text());
                info.setRebuildsocre(result.get(16).text());
                info.setName(result.get(3).text());
                info.setXueqi(result.get(1).text());
                String xuefen = result.get(6).text().trim();
                if (TextUtils.isEmpty(xuefen)) {
                    info.setCredit(0);
                } else {
                    info.setCredit(Float.parseFloat(xuefen));
                }
                String gPA = result.get(7).text().trim();
                try {
                    info.setGPA(Float.parseFloat(gPA));
                } catch (Exception e) {
                    info.setGPA(0);
                }
                String Ususal = result.get(8).text().trim();
                try {
                    info.setUsusal(Float.parseFloat(Ususal));
                } catch (Exception e) {
                    info.setUsusal(0);
                }
                String midterm = result.get(9).text().trim();
                try {
                    info.setMidterm(Float.parseFloat(midterm));
                } catch (Exception e) {
                    info.setMidterm(0);
                }
                String Terminal = result.get(10).text().trim();
                try {
                    info.setTerminal(Float.parseFloat(Terminal));
                } catch (Exception e) {
                    info.setTerminal(0);
                }
                String experiment = result.get(11).text().trim();
                try {
                    info.setExperiment(Float.parseFloat(experiment));
                } catch (Exception e) {
                    info.setExperiment(0);
                }
                String Result = result.get(12).text().trim();
                try {
                    info.setResult(Float.parseFloat(Result));
                } catch (Exception e) {
                    info.setResult(0);
                }
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(info);
            }
            Message msg = handler.obtainMessage();
            msg.obj = list;
            msg.what = 3;
            handler.sendMessage(msg);

        }
    }

    private void initHandler() {
        handler = new Handler() {
            @SuppressWarnings("unchecked")
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ERROR: // 访问网络过程中抛出异常
                        // 取消加载标志
                        isLoading = false;
                        // 取消跳转
                        tiao = false;
                        // 取消显示进度条
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        String text = (String) msg.obj;
                        // 提示用户失败
                        if (text != null && text.length() >= 1) {
                            Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case SHOW_TEXT: // 用于显示 提示信息
                        Toast.makeText(mActivity, (String) msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESSED: // 加载成绩信息完毕
                        // 接收返回来的list集合
                        list.clear();
                        list.addAll((ArrayList<ScoreSearchInfo>) msg.obj);
                        // 给listview设置适配器
                        if (myListViewAdapter == null) {
                            if (list != null && list.size() > 0) {
                                Collections.reverse(list);
                                myListViewAdapter = new MyListViewAdapter(list, mActivity);
                                lv.setAdapter(myListViewAdapter);
                                calc.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (list != null && list.size() > 0) {
                                Collections.reverse(list);
                                myListViewAdapter.setData(list);
                                myListViewAdapter.notifyDataSetChanged();
                                calc.setVisibility(View.VISIBLE);
                            }

                        }

                        break;

                    case OK:// 学位课信息获取结束
                        // 根据学号的6位去SharedPreferences里面去得到有本专业的学位课信息，如果没有，则新建
                        xueweike = (String) SharedPreferencesUtils.getParam(mActivity, ((String) SharedPreferencesUtils.getParam(mActivity, "userxh", "1203030224")).substring(0, 6).trim(), "");
                        // 取消加载标志位
                        isLoading = false;
                        // 取消进度条显示
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        //System.out.println("xueweike"+xueweike);
                        // 提示用户学位课加载完成
                        // Toast.makeText(mActivity, "获取学位课信息成功，当前可计算学位课绩点！",
                        // Toast.LENGTH_LONG).show();
                        int xueweikesize = 0;
                        if (list != null && list.size() >= 1) {
                            // 如果有跳转标志，则跳转
                            if (tiao) {
                                intent = new Intent(mActivity, CalcActivity.class);
                                intent.putExtra("list", list);
                                // 为每个学科添加学位课标志
                                for (ScoreSearchInfo s : list) {
                                    if (xueweike.contains(s.getName())) {
                                        xueweikesize++;
                                        s.setIsXueWei(true);
                                    } else {
                                        s.setIsXueWei(false);
                                    }
                                }
                                intent.putExtra("xueweikesize", xueweikesize);
                                startActivity(intent);
                            }
                        }


                        break;


                    default:
                        break;
                }
            }

            ;
        };
    }

    private CustomDialog dialog;

    protected void dialog() {
        dialog = new CustomDialog(getActivity(),
                R.style.CustomDialog, "提示", "重新加载学位课信息(学位课无误则不用重新加载)？", "确定", "取消",
                new CustomDialog.CustomDialogListener() {
                    @Override
                    public void OnClick(View view) {
                        if (view.getId() == R.id.Dlg_Yes) {
                            dialog.dismiss();// 使对话框消失
                            String sp_userxh = (String) SharedPreferencesUtils.getParam(mActivity, "userxh", "");
                            String sp_password = (String) SharedPreferencesUtils.getParam(mActivity, "password", "");
                            new XeiWeiKeSearchDown(mActivity, sp_userxh, sp_password, handler).getSocre(true);
                            progressDialog = ProgressDialog.show(mActivity, "请稍等...", "正在获取学位课数据...", true);
                            Toast.makeText(mActivity, "获取中，请稍候！", Toast.LENGTH_LONG).show();
                            tiao = true;
                        } else {
                            dialog.dismiss();
                            if (intent != null) {
                                startActivity(intent);
                            }

                        }
                    }
                });
        dialog.show();
    }
}
