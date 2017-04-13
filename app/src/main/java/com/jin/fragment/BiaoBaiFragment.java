package com.jin.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jin.adpter.BiaoBaiAdpter;
import com.jin.adpter.BiaoBaiAdpter.ZanListener;
import com.jin.domain.Results;
import com.jin.domain.Results.BiaoBaiItem;
import com.jin.slnews.BiaoBaiActivity;
import com.jin.slnews.R;
import com.jin.utils.CacheUtils;
import com.jin.utils.Constant;
import com.jin.views.RefreshListView;
import com.jin.views.RefreshListView.OnRefreshListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

public class BiaoBaiFragment extends BaseFragment {
    @ViewInject(R.id.lv_biaobai)
    RefreshListView lv_biaobai;
    @ViewInject(R.id.btn_biaobai)
    Button btn_biaobai;

    private ArrayList<BiaoBaiItem> list;
    private HttpUtils httpUtils;
    private BiaoBaiAdpter biaoBaiAdpter;
    private boolean shua_flag = false;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_biaopbai, null);
        ViewUtils.inject(this, view); // 注入view和事件

        return view;
    }

    @Override
    public void initData() {
        System.out.println("初始化表白墙");
        list = new ArrayList<>();
        String cache = CacheUtils.getCache(Constant.BIAOBAI_LISTS_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache);
        }
        getDataFromServer();
        btn_biaobai.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, BiaoBaiActivity.class);
                startActivity(intent);
            }
        });
        lv_biaobai.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                shua_flag = true;
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {// 不用实现此方法，因为已经直接加载所有数据
                lv_biaobai.onRefreshComplete(true);
            }

        });

    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        httpUtils = new HttpUtils("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
        httpUtils.configHttpCacheSize(0);

        httpUtils.send(HttpMethod.GET, Constant.BIAOBAI_LISTS_URL, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                 System.out.println("表白网络result：" +
                 responseInfo.result);
                if (responseInfo.result.contains("list")) {
                    parseData(responseInfo.result);
                    CacheUtils.setCache(Constant.BIAOBAI_LISTS_URL, responseInfo.result, mActivity);
                }
                //else {
//					System.out.println("表白网络result：" + responseInfo.result);
                //}

                // 刷新完成调用次方法
                if (shua_flag) {
                    lv_biaobai.onRefreshComplete(true);
                    shua_flag = false;
                    Toast.makeText(mActivity, "刷新成功！", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
				System.out.println("biaobaiwell错误：" + msg);
                if (shua_flag) {
                    lv_biaobai.onRefreshComplete(false);
                    shua_flag = false;
                    Toast.makeText(mActivity, "刷新失败！", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromServer();
    }

    protected void parseData(String json) {
        //System.out.println("json=" + json);
        Results results = new Gson().fromJson(json, Results.class);
        list = results.list;
		//System.out.println("解析结果：" + list.toString());
        CacheUtils.setCache(Constant.BIAOBAI_LISTS_URL, json, mActivity);

        biaoBaiAdpter = new BiaoBaiAdpter(mActivity, list);
        lv_biaobai.setAdapter(biaoBaiAdpter);
        biaoBaiAdpter.setOnZanListener(new ZanListener() {

            @Override
            public void zan(final int id, boolean flag) {

                if (flag) {
                    Toast.makeText(mActivity, "你已经点过赞啦！", Toast.LENGTH_SHORT).show();
                } else {
                    httpUtils = new HttpUtils();
                    RequestParams requestParams = new RequestParams();
                    requestParams.addQueryStringParameter("id", id + "");
                    httpUtils.send(HttpMethod.GET, Constant.ZAN_URL, requestParams, new RequestCallBack<String>() {

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            if (responseInfo.result.equals("true")) {
                                // 点赞成功后
                                Toast.makeText(mActivity, "点赞成功！", Toast.LENGTH_SHORT).show();
                                String cahe = CacheUtils.getCache("zan", mActivity);
                                CacheUtils.setCache("zan", cahe + id + ",", mActivity);
                                BiaoBaiAdpter.setIsLiked(id);
                                biaoBaiAdpter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(mActivity, "点赞失败！", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(mActivity, "点赞失败！请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}
