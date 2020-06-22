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
import android.widget.TextView;
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
import com.jin.views.CustomDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;

public class ChengJiFragment extends BaseFragment {
    private static final String TAG = "ChengJiFragment";
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
    private TextView tv_version;
    /**
     * 得到的成绩信息集合
     */
    private ArrayList<ScoreSearchInfo> list = new ArrayList();
    /**
     * 学位课
     */
    private String xueweike = "";

    private Button calc;

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
        x.view().inject(this, view); // 注入view和事件
        view.findViewById(R.id.tv_kefu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkApkExist(mActivity, "com.tencent.mobileqq")) {
                    mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=913214983&version=1")));
                } else {
                    Toast.makeText(mActivity, "检查到您手机没有安装QQ，请安装后使用该功能", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    @Override
    public void initData() {
        initHandler();
        Log.e(TAG, "初始化成绩");

        // 设置背景透明度
        mActivity.findViewById(R.id.bg).getBackground().setAlpha(80);
        // 通过id找到界面中的计算按钮
        calc = mActivity.findViewById(R.id.calc);
        Button bt = mActivity.findViewById(R.id.bt);
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
                final String xh = et_xh.getText().toString().trim();
                final String pw = et_pw.getText().toString().trim();
                // 判断得到的学号和密码是否为空
                if (TextUtils.isEmpty(xh) || TextUtils.isEmpty(pw)) {
                    // 如果为空，则弹出提示并返回
                    Toast.makeText(mActivity, "学号或密码不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    if (xh.length() < 10) {
                        Toast.makeText(mActivity, "请输入正确的学号", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 如果不为空，则立即拿此学号和密码去登陆，查询成绩，并保存学号密码到手机
                    new ScoreSearchDown(xh, pw, handler, mActivity).getSocre();
                    String xuewei = (String) SharedPreferencesUtils.getParam(mActivity, et_xh.getText().toString().trim().substring(0, 6), "");
                    if (TextUtils.isEmpty(xuewei)) {
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
        lv = mActivity.findViewById(R.id.lv);
        // 通过id找到界面中的学号输入框EditText
        et_xh = mActivity.findViewById(R.id.et_xh);
        // 通过id找到界面中的密码输入框EditText
        et_pw = mActivity.findViewById(R.id.et_pw);
        tv_version = mActivity.findViewById(R.id.tv_version);
        tv_version.setText("V" + BuildConfig.VERSION_NAME);
        // 得到SharedPreferences中的学号和密码
        String sp_userxh = (String) SharedPreferencesUtils.getParam(mActivity, "userxh", "");
        String sp_password = (String) SharedPreferencesUtils.getParam(mActivity, "password", "");
        show = mActivity.findViewById(R.id.login_switchBtn);
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
                System.out.println("SP里面的学位课信息:" + xuewei);
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
