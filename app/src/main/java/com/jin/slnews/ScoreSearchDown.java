package com.jin.slnews;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.jin.domain.ScoreSearchInfo;
import com.jin.fragment.ChengJiFragment;
import com.jin.utils.CacheUtils;
import com.jin.utils.SharedPreferencesUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 成绩查询网络连接线程类，用于开辟一个子线程去访问网络，请求教学网，得到成绩信息。
 * 请求成功后，向Handler发送一个List<ScoreSearchInfo>对象。
 */
public class ScoreSearchDown {
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
    public ScoreSearchDown(String user, String pswd, Handler handler, Context context) {
        this.xh = user;
        this.password = pswd;
        this.handler = handler;
        this.context = context;
    }

    public void getSocre() {
        new Thread() {
            public void run() {
                // 查成绩的网址
                String uriAPI = "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/default2.aspx";
                // 创建HttpClient对象
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
                HttpPost httpPost = new HttpPost(uriAPI);
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                // 根据表单设置对应的参数
                params.add(new BasicNameValuePair("RadioButtonList1", "学生"));// 设置类别
                params.add(new BasicNameValuePair("TextBox1", xh));// 设置学号
                params.add(new BasicNameValuePair("TextBox2", password));// 设置密码
                params.add(new BasicNameValuePair("__VIEWSTATE", "dDwxODI0OTM5NjI1Ozs+ErNwwEBfve9YGjMA8xEN6zdawEw="));
                params.add(new BasicNameValuePair("Button1", ""));
                params.add(new BasicNameValuePair("lbLanguage", ""));
                HttpResponse responselogin;
                try {
                    UrlEncodedFormEntity par = new UrlEncodedFormEntity(params, "GB2312");
                    System.out.println("par=" + par);
                    httpPost.setEntity(new UrlEncodedFormEntity(params, "GB2312"));
                    responselogin = client.execute(httpPost);

                    // 看是否请求成功
                    if (responselogin.getStatusLine().getStatusCode() == 200) {
                        String loginResult = EntityUtils.toString(responselogin.getEntity(), "GB2312");

                        // 用于调试查看登录结果
                        // Log.i("TAG", "loginResult-->" + loginResult);
                        // 解析返回来的网页信息
                        Document logindoc = Jsoup.parse(loginResult);
                        // 解析得到 标题 元素
                        Elements loginele = logindoc.select("title");

                        // 判断是否登录成功，如果登陆失败，网页标题应是"登陆"字样
                        if (loginele.text().equals("登录")) {// 登陆失败
                            Message msg = handler.obtainMessage();
                            msg.obj = "登陆失败！请检查学号和密码！";
                            msg.what = 2;
                            handler.sendMessage(msg);
                        } else { // 登陆成功，然后点击成绩个人成绩查询
                            String sp_userxh = (String) SharedPreferencesUtils.getParam(context, "userxh", "");
                            String sp_password = (String) SharedPreferencesUtils.getParam(context, "password", "");
                            // String xwk = (String) SharedPreferencesUtils.getParam(context, SharedPreferencesUtils.getParam(context, "userxh", "1203030224").toString().trim().substring(2, 6), "");

//                            if (!ChengJiFragment.isLoading) {// 如果没有加载中，则更新
//                                System.out.println("开始自动更新学位课信息");
//                                new XeiWeiKeSearchDown(context, sp_userxh, sp_password, handler).getSocre(false);
//                            }
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
                            // 点击成绩个人成绩查询
                            client = new DefaultHttpClient();
                            HttpGet httpGet = new HttpGet("http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/xscjcx.aspx?xh=" + xh);
                            // 设置请求头
                            httpGet.setHeader("Referer", "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/xs_main.aspx?xh=" + xh);
                            HttpResponse httpGetResponse;
                            // 通过请求得到Response
                            httpGetResponse = client.execute(httpGet);
                            // 通过状态码判断是否请求成功
                            if (httpGetResponse.getStatusLine().getStatusCode() == 200) {// 请求成功
                                // 将返回的数据解析成String类型
                                String getResult = EntityUtils.toString(httpGetResponse.getEntity(), "GB2312");
                                // 通过日志打印查看请求返回的数据
                                // Log.i("TAG", "httpGetResult-->" + getResult);
                                // 解析返回来的网页信息
                                Document document = Jsoup.parse(getResult);
                                __VIEWSTATE = document.select("input[name=__VIEWSTATE]").get(0).attr("value");

                                // System.out.println("__VIEWSTATE=" +
                                // __VIEWSTATE);
                            }

                            HttpResponse ResponseResult;
                            HttpPost getScore = new HttpPost("http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/xscjcx.aspx?xh=" + xh);
                            getScore.setHeader("Referer", "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/xscjcx.aspx?xh=" + xh);
                            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                            params1.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
                            params1.add(new BasicNameValuePair("__EVENTTARGET", ""));
                            params1.add(new BasicNameValuePair("ddl_kcxz", ""));

                            params1.add(new BasicNameValuePair("ddlXN", ""));
                            params1.add(new BasicNameValuePair("ddlXQ", ""));
                            params1.add(new BasicNameValuePair("hidLanguage", ""));
                            params1.add(new BasicNameValuePair("btn_zcj", "历年成绩"));
                            params1.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
                            getScore.setEntity(new UrlEncodedFormEntity(params1, "GB2312"));

                            ResponseResult = client.execute(getScore);

                            String sss = EntityUtils.toString(ResponseResult.getEntity(), "GB2312");
                            // Log.i("TAG", "ssss->" + sss.toString());

                            // 缓存
                            CacheUtils.setCache(xh, sss.toString(), context);

                            /**
                             * 使用JSuop解析html数据
                             */
                            Document doc = Jsoup.parse(sss); // 把HTML代码加载到doc中

                            Elements ele = doc.select("table[class=datelist]"); // table
                            // class="formlist"
                            // Log.i("TAG", "---Result" +
                            // ele.text().toString());
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

                    }

                } catch (ConnectTimeoutException e) {
                    Message msg = handler.obtainMessage();
                    msg.obj = "请求超时！请确认教学网是否可访问并检查网络是否可用！";
                    msg.what = 2;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    Message msg = handler.obtainMessage();
                    msg.obj = "查询失败！";
                    msg.what = 2;
                    handler.sendMessage(msg);
                }

            }

            ;
        }.start();

    }
}
