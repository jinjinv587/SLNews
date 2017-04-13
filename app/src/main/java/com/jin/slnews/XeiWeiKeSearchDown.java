package com.jin.slnews;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

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
 * 学位课查询网络连接线程类
 */
public class XeiWeiKeSearchDown {
    String user;// 学号
    final String pswd;// 密码
    Handler handler;
    String __VIEWSTATE;
    Context mActivity;

    /**
     * 构造方法
     *
     * @param user    学号
     * @param pswd    密码
     * @param handler
     */
    public XeiWeiKeSearchDown(Context mActivity, String user, String pswd, Handler handler) {
        this.mActivity = mActivity;
        this.user = user;
        this.pswd = pswd;
        this.handler = handler;

    }

    /**
     * @param flag handler是否发送消息
     */
    public void getSocre(final boolean flag) {
        new Thread() {
            public void run() {
                // ChengJiFragment.isLoading = true;
                // 查成绩的网址
                String uriAPI = "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/default2.aspx";
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
                HttpPost httpPost = new HttpPost(uriAPI);
                HttpResponse Responselogin;
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("RadioButtonList1", "学生"));
                params.add(new BasicNameValuePair("TextBox1", user));// 设置学号
                params.add(new BasicNameValuePair("TextBox2", pswd));// 设置密码
                params.add(new BasicNameValuePair("__VIEWSTATE", "dDwxODI0OTM5NjI1Ozs+ErNwwEBfve9YGjMA8xEN6zdawEw="));
                params.add(new BasicNameValuePair("Button1", ""));
                params.add(new BasicNameValuePair("lbLanguage", ""));

                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, "GB2312"));
                    Responselogin = client.execute(httpPost);
                    // 看是否请求成功
                    if (Responselogin.getStatusLine().getStatusCode() == 200) {
                        String loginResult = EntityUtils.toString(Responselogin.getEntity(), "GB2312");
                        // 用于调试查看登录结果
                        // Log.i("TAG", "loginResult-->" + loginResult);
                        // 解析返回来的网页信息
                        Document logindoc = Jsoup.parse(loginResult);
                        // 解析得到 标题 元素
                        Elements loginele = logindoc.select("title");

                        // 判断是否登录成功，如果登陆失败，网页标题应是"登陆"字样
                        if (loginele.text().equals("登录")) {// 登陆失败
//                            Message msg = handler.obtainMessage();
//                            msg.obj = "查询学位课失败！请检查学号和密码！";
//                            msg.what = 2;
//                            handler.sendMessage(msg);
                            //System.out.println("查询学位课失败！");
                        } else {

                            // 去教学计划查询所有学位课
                            client = new DefaultHttpClient();
                            HttpGet httpGet1 = new HttpGet("http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/pyjh.aspx?xh=" + user);
                            // 设置请求头
                            httpGet1.setHeader("Referer", "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/xs_main.aspx?xh=" + user);
                            HttpResponse httpGetResponse1;
                            // 通过请求得到Response
                            httpGetResponse1 = client.execute(httpGet1);
                            if (httpGetResponse1.getStatusLine().getStatusCode() == 200) {// 请求成功
                                // 将返回的数据解析成String类型
                                String getResult = EntityUtils.toString(httpGetResponse1.getEntity(), "GB2312");
                                // 通过日志打印查看请求返回的数据
                                // Log.i("TAG", "httpGetResult-->" + getResult);
                                // 解析返回来的网页信息
                                Document document = Jsoup.parse(getResult);
                                __VIEWSTATE = document.select("input[name=__VIEWSTATE]").get(0).attr("value");

                                // 去点1
                                HttpResponse ResponseResult;
                                HttpPost getScore = new HttpPost("http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/pyjh.aspx?xh=" + user);
                                getScore.setHeader("Referer", "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/pyjh.aspx?xh=" + user);
                                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                                params1.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
                                params1.add(new BasicNameValuePair("__EVENTTARGET", "DBGrid:_ctl24:_ctl0"));
                                params1.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
                                params1.add(new BasicNameValuePair("xq", "全部"));
                                params1.add(new BasicNameValuePair("kcxz", "全部"));
                                getScore.setEntity(new UrlEncodedFormEntity(params1, "GB2312"));
                                ResponseResult = client.execute(getScore);

                                String PostResult = EntityUtils.toString(ResponseResult.getEntity(), "GB2312");
                                // Log.i("TAG", "PostResult->" +
                                // PostResult.toString());
                                // //System.out.println( "PostResult->" +
                                // PostResult.toString());
                                /**
                                 * 使用JSuop解析html数据
                                 */
                                Document doc = Jsoup.parse(PostResult); // 把HTML代码加载到doc中
                                Elements ele = doc.select("table[id=DBGrid]");
                                Elements elee = ele.select("tr");
                                String xueWeike = "";
                                for (int i = 1; i < elee.size() - 1; i++) {
                                    Elements result = elee.get(i).select("td");
                                    if (result.get(16).text().equals("是")) {
                                        xueWeike += result.get(1).text() + ",";

                                    }
                                }

                                String xueweike = (String) SharedPreferencesUtils.getParam(mActivity, user.substring(0, 6), "");
                                // 判断得到的学位课信息是否包含返回来的数据，如果不包含，就增加，如果包含则不变
                                if (!xueweike.contains(xueWeike)) {
                                    xueweike += xueWeike;
                                    SharedPreferencesUtils.setParam(mActivity, user.substring(0, 6), xueweike);
                                }
                                getScore = new HttpPost("http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/pyjh.aspx?xh=" + user);
                                getScore.setHeader("Referer", "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/pyjh.aspx?xh=" + user);
                                params1 = new ArrayList<NameValuePair>();
                                params1.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
                                params1.add(new BasicNameValuePair("__EVENTTARGET", "DBGrid:_ctl24:_ctl1"));
                                params1.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
                                params1.add(new BasicNameValuePair("xq", "全部"));
                                params1.add(new BasicNameValuePair("kcxz", "全部"));
                                getScore.setEntity(new UrlEncodedFormEntity(params1, "GB2312"));
                                ResponseResult = client.execute(getScore);

                                PostResult = EntityUtils.toString(ResponseResult.getEntity(), "GB2312");
                                // Log.i("TAG", "PostResult->" +
                                // PostResult.toString());
                                // //System.out.println( "PostResult->" +
                                // PostResult.toString());
                                /**
                                 * 使用JSuop解析html数据
                                 */
                                doc = Jsoup.parse(PostResult); // 把HTML代码加载到doc中
                                ele = doc.select("table[id=DBGrid]");
                                elee = ele.select("tr");
                                xueWeike = "";
                                for (int i = 1; i < elee.size() - 1; i++) {
                                    Elements result = elee.get(i).select("td");
                                    if (result.get(16).text().equals("是")) {
                                        xueWeike += result.get(1).text() + ",";
                                    }
                                }
                                xueweike = (String) SharedPreferencesUtils.getParam(mActivity, user.substring(0, 6), "");
                                // 判断得到的学位课信息是否包含返回来的数据，如果不包含，就增加，如果包含则不变
                                if (!xueweike.contains(xueWeike)) {
                                    xueweike += xueWeike;
                                    SharedPreferencesUtils.setParam(mActivity, user.substring(0, 6), xueweike);
                                }
                                // 去点2
                                getScore = new HttpPost("http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/pyjh.aspx?xh=" + user);
                                getScore.setHeader("Referer", "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/pyjh.aspx?xh=" + user);
                                params1 = new ArrayList<NameValuePair>();
                                params1.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
                                params1.add(new BasicNameValuePair("__EVENTTARGET", "DBGrid:_ctl24:_ctl2"));
                                params1.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
                                params1.add(new BasicNameValuePair("xq", "全部"));
                                params1.add(new BasicNameValuePair("kcxz", "全部"));
                                getScore.setEntity(new UrlEncodedFormEntity(params1, "GB2312"));
                                ResponseResult = client.execute(getScore);

                                PostResult = EntityUtils.toString(ResponseResult.getEntity(), "GB2312");
                                // Log.i("TAG", "PostResult->" +
                                // PostResult.toString());
                                // //System.out.println( "PostResult->" +
                                // PostResult.toString());
                                /**
                                 * 使用JSuop解析html数据
                                 */
                                doc = Jsoup.parse(PostResult); // 把HTML代码加载到doc中
                                ele = doc.select("table[id=DBGrid]");
                                elee = ele.select("tr");
                                xueWeike = "";
                                for (int i = 1; i < elee.size() - 1; i++) {
                                    Elements result = elee.get(i).select("td");
                                    if (result.get(16).text().equals("是")) {
                                        xueWeike += result.get(1).text() + ",";

                                    }
                                }
                                xueweike = (String) SharedPreferencesUtils.getParam(mActivity, user.substring(0, 6), "");
                                // 判断得到的学位课信息是否包含返回来的数据，如果不包含，就增加，如果包含则不变
                                if (!xueweike.contains(xueWeike)) {
                                    xueweike += xueWeike;
                                    SharedPreferencesUtils.setParam(mActivity, user.substring(0, 6), xueweike);
                                }
                                getScore = new HttpPost("http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/pyjh.aspx?xh=" + user);
                                getScore.setHeader("Referer", "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/pyjh.aspx?xh=" + user);
                                params1 = new ArrayList<NameValuePair>();
                                params1.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
                                params1.add(new BasicNameValuePair("__EVENTTARGET", "DBGrid:_ctl24:_ctl3"));
                                params1.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
                                params1.add(new BasicNameValuePair("xq", "全部"));
                                params1.add(new BasicNameValuePair("kcxz", "全部"));
                                getScore.setEntity(new UrlEncodedFormEntity(params1, "GB2312"));
                                ResponseResult = client.execute(getScore);

                                PostResult = EntityUtils.toString(ResponseResult.getEntity(), "GB2312");
                                // Log.i("TAG", "PostResult->" +
                                // PostResult.toString());
                                // //System.out.println( "PostResult->" +
                                // PostResult.toString());
                                /**
                                 * 使用JSuop解析html数据
                                 */
                                doc = Jsoup.parse(PostResult); // 把HTML代码加载到doc中
                                ele = doc.select("table[id=DBGrid]");
                                elee = ele.select("tr");
                                xueWeike = "";
                                for (int i = 1; i < elee.size() - 1; i++) {
                                    Elements result = elee.get(i).select("td");
                                    if (result.get(16).text().equals("是")) {
                                        xueWeike += result.get(1).text() + ",";

                                    }
                                }
                                xueweike = (String) SharedPreferencesUtils.getParam(mActivity, user.substring(0, 6), "");
                                // 判断得到的学位课信息是否包含返回来的数据，如果不包含，就增加，如果包含则不变
                                if (!xueweike.contains(xueWeike)) {
                                    xueweike += xueWeike;
                                    SharedPreferencesUtils.setParam(mActivity, user.substring(0, 6), xueweike);
                                }

                                // 去点5
                                getScore = new HttpPost("http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/pyjh.aspx?xh=" + user);
                                getScore.setHeader("Referer", "http://218.25.35.27:8080/(hkghmi32gs4dmratfknduq55)/pyjh.aspx?xh=" + user);
                                params1 = new ArrayList<NameValuePair>();
                                params1.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
                                params1.add(new BasicNameValuePair("__EVENTTARGET", "DBGrid:_ctl24:_ctl4"));
                                params1.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
                                params1.add(new BasicNameValuePair("xq", "全部"));
                                params1.add(new BasicNameValuePair("kcxz", "全部"));
                                getScore.setEntity(new UrlEncodedFormEntity(params1, "GB2312"));
                                ResponseResult = client.execute(getScore);

                                PostResult = EntityUtils.toString(ResponseResult.getEntity(), "GB2312");
                                // Log.i("TAG", "PostResult->" +
                                // PostResult.toString());
                                // //System.out.println( "PostResult->" +
                                // PostResult.toString());
                                /**
                                 * 使用JSuop解析html数据
                                 */
                                doc = Jsoup.parse(PostResult); // 把HTML代码加载到doc中
                                ele = doc.select("table[id=DBGrid]");
                                elee = ele.select("tr");
                                xueWeike = "";
                                for (int i = 1; i < elee.size() - 1; i++) {
                                    Elements result = elee.get(i).select("td");
                                    if (result.get(16).text().equals("是")) {
                                        xueWeike += result.get(1).text() + ",";
                                    }
                                }
                                xueweike = (String) SharedPreferencesUtils.getParam(mActivity, user.substring(0, 6), "");
                                //System.out.println("xueweike=" + xueweike);
                                // 判断得到的学位课信息是否包含返回来的数据，如果不包含，就增加，如果包含则不变
                                if (!xueweike.contains(xueWeike)) {
                                    xueweike += xueWeike;
                                }
                                if (!xueweike.contains("all")) {
                                    xueweike += "all";
                                }
                                SharedPreferencesUtils.setParam(mActivity, user.substring(0, 6), xueweike);
                                Message msg = handler.obtainMessage();
                                msg.obj = xueWeike;
                                msg.what = 4;
                                handler.sendMessage(msg);


                            }
                        }

                    }

                } catch (ConnectTimeoutException e) {
                    if (flag) {
                        Message msg = handler.obtainMessage();
                        msg.obj = "请求超时！请重新加载！";
                        msg.what = 1;
                        handler.sendMessage(msg);
                        //ChengJiFragment.isLoading = false;
                    }
                } catch (Exception e) {
                    if (flag) {
                        Message msg = handler.obtainMessage();
                        msg.obj = "查询教学计划失败，请重新加载！";
                        msg.what = 1;
                        handler.sendMessage(msg);
                        //ChengJiFragment.isLoading = false;
                    }
                }

            }

        }.start();

    }

}
