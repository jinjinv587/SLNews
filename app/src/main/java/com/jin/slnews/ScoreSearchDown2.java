package com.jin.slnews;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.jin.domain.ScoreSearchInfo;
import com.jin.fragment.ChengJiFragment;
import com.jin.utils.CacheUtils;
import com.jin.utils.HttpUtil;
import com.jin.utils.MyHttpCallBack;
import com.jin.utils.SharedPreferencesUtils;
import com.jin.utils.ToastUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成绩查询网络连接线程类，用于开辟一个子线程去访问网络，请求教学网，得到成绩信息。
 * 请求成功后，向Handler发送一个List<ScoreSearchInfo>对象。
 */
public class ScoreSearchDown2 {
    private static final String TAG = "ScoreSearchDown";
    /**
     * 学号
     */
    String xh;
    /**
     * 密码
     */
    final String password;
    /**
     * 消息处理器
     */
    Handler handler;
    String __VIEWSTATE;
    /**
     * 分数集合
     */
    List<ScoreSearchInfo> infos = new ArrayList<>();
    /**
     * 科目对象
     */
    ScoreSearchInfo info;

    Context context;

    /**
     * 构造方法
     *
     * @param user    学号
     * @param pswd    密码
     * @param handler
     */
    public ScoreSearchDown2(String user, String pswd, Handler handler, Context context) {
        this.xh = user;
        this.password = pswd;
        this.handler = handler;
        this.context = context;
    }

    String uriAPI = "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/default2.aspx";

    public void getSocre() {
        login();

        if (1 == 1) {
            return;
        }

        new Thread() {
            public void run() {
                try {
                    // 登录

//                    Log.e(TAG, "run: " + loginResult);
                    // 解析返回来的网页信息
                    Document logindoc = Jsoup.parse("");
                    // 解析得到 标题 元素
                    Elements loginele = logindoc.select("title");

                    // 判断是否登录成功，如果登陆失败，网页标题应是"登陆"字样
                    if (loginele.text().equals("登录")) {// 登陆失败
                        Log.e(TAG, "登录失败");
                        Message msg = handler.obtainMessage();
                        msg.obj = "登陆失败！请检查学号和密码！";
                        msg.what = ChengJiFragment.SHOW_TEXT;
                        handler.sendMessage(msg);
                    } else {// 登陆成功，然后点击成绩个人成绩查询
                        Log.e(TAG, "登录成功");
                        String sp_userxh = (String) SharedPreferencesUtils.getParam(context, "userxh", "");
                        String sp_password = (String) SharedPreferencesUtils.getParam(context, "password", "");
                        // 如果SharedPreferences中无数据，或者数据跟刚刚输入的数据不一致，则保存数据
                        if (!sp_userxh.equals(xh) || !sp_password.equals(password)) {// 提示用户保存成功
                            // 保存学号密码
                            SharedPreferencesUtils.setParam(context, "userxh", xh);
                            SharedPreferencesUtils.setParam(context, "password", password);
                            Message msg = new Message();
                            msg.obj = "保存学号密码成功！";
                            msg.what = ChengJiFragment.SHOW_TEXT;
                            handler.sendMessage(msg);

                        }
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Referer", "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/xs_main.aspx?xh=" + xh);
                        String getResult = HttpUtil.get("http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/xscjcx.aspx?xh=" + xh, headers);
                        RequestParams requestParams = new RequestParams("http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/xscjcx.aspx?xh=" + xh);
                        requestParams.setCharset("GB2312");
                        requestParams.addHeader("Referer", "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/xs_main.aspx?xh=" + xh);
                        x.http().get(requestParams, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Log.e(TAG, "onSuccess: " + result);
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                ex.printStackTrace();
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });


//                        Log.e(TAG, "个人成绩查询: " + getResult);
                        Document document = Jsoup.parse(getResult);
                        __VIEWSTATE = document.select("input[name=__VIEWSTATE]").get(0).attr("value");
//                        Log.e(TAG, "__VIEWSTATE=" + __VIEWSTATE);
                        Log.e(TAG, "点击个人成绩查询成功");

//-------------------
                        Map<String, Object> paramMap = new HashMap<>();
                        paramMap.put("__EVENTARGUMENT", "");
                        paramMap.put("__EVENTTARGET", "");
                        paramMap.put("ddl_kcxz", "");
                        paramMap.put("ddlXN", "");
                        paramMap.put("ddlXQ", "");
                        paramMap.put("hidLanguage", "");
                        paramMap.put("btn_zcj", "历年成绩");
                        paramMap.put("__VIEWSTATE", __VIEWSTATE);
                        headers = new HashMap<>();
                        headers.put("Referer", "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/xscjcx.aspx?xh=" + xh);
                        String post = HttpUtil.post("http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/xscjcx.aspx?xh=" + xh, paramMap, headers);
//                        Log.e(TAG, "历年成绩: " + post);
                        Log.e(TAG, "点击历年成绩成功");

//                        // 缓存
                        CacheUtils.setCache(xh, post, context);
                        Document doc = Jsoup.parse(post); // 把HTML代码加载到doc中
                        __VIEWSTATE = doc.select("input[name=__VIEWSTATE]").get(0).attr("value");
//                        Log.e(TAG, "__VIEWSTATE=" + __VIEWSTATE);

                        Elements ele = doc.select("table[class=datelist]"); // table
                        Elements elee = ele.select("tr");
                        // Log.i("TAG", "---elee.size()=" + elee.size());
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
                            infos.add(info);
                        }

                        String listsize = CacheUtils.getCache("listsize", context);
                        if (listsize.equals("")) { // 16
                            CacheUtils.setCache("listsize", infos.size() + "", context);
                        } else if (!listsize.equals(infos.size() + "")) {
                            CacheUtils.setCache("listsize", infos.size() + "", context);
                            // 存在并且数量不一致
                            Message msg = handler.obtainMessage();
                            msg.obj = "有最新成绩，请注意查看！";
                            msg.what = 2;
                            handler.sendMessage(msg);
                        } else if (listsize.equals(infos.size() + "")) {
                            // 存在并且数量一致
                            Message msg = handler.obtainMessage();
                            msg.obj = "暂无新成绩！";
//								System.out.println("暂无新成绩！");
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }

                        Message msg = handler.obtainMessage();
                        msg.obj = infos;
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    Message msg = handler.obtainMessage();
                    msg.obj = "查询失败！";
                    msg.what = 2;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }

        }.start();

    }

    private void login() {
        RequestParams requestParams = new RequestParams(uriAPI);
        requestParams.setCharset("GB2312");
        requestParams.addBodyParameter("RadioButtonList1", "学生");// 设置类别
        requestParams.addBodyParameter("TextBox1", xh);// 设置学号
        requestParams.addBodyParameter("TextBox2", password);// 设置密码
        requestParams.addBodyParameter("__VIEWSTATE", "dDwxODI0OTM5NjI1Ozs+ErNwwEBfve9YGjMA8xEN6zdawEw=");
        requestParams.addBodyParameter("Button1", "");
        x.http().post(requestParams, new MyHttpCallBack() {
            @Override
            public void onHttpSuccess(String loginResult) {
                Document logindoc = Jsoup.parse(loginResult);
                // 解析得到 标题 元素
                Elements loginele = logindoc.select("title");
                if (loginele.text().equals("登录")) {// 登陆失败
                    Log.e(TAG, "登录失败");
                    ToastUtils.show(context, "登陆失败！请检查学号和密码！");

                } else {// 登陆成功，然后点击成绩个人成绩查询
                    Log.e(TAG, "登录成功");
                    String sp_userxh = (String) SharedPreferencesUtils.getParam(context, "userxh", "");
                    String sp_password = (String) SharedPreferencesUtils.getParam(context, "password", "");
                    // 如果SharedPreferences中无数据，或者数据跟刚刚输入的数据不一致，则保存数据
                    if (!sp_userxh.equals(xh) || !sp_password.equals(password)) {// 提示用户保存成功
                        // 保存学号密码
                        SharedPreferencesUtils.setParam(context, "userxh", xh);
                        SharedPreferencesUtils.setParam(context, "password", password);
                        ToastUtils.show(context, "保存学号密码成功！");

                    }
                }
            }

            @Override
            public void onHttpFail(int code, String msg) {

            }

        });

    }
}
