package com.xonepay.commonlibrary.utils;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by wjq on 2017/6/19.
 */

public class HttpUtil {
    private static final String TAG = "HttpUtil";


    public interface HttpCallBack<String> {
        void onSuccess(String result);

        void onError(Throwable ex, boolean isOnCallback);
    }

    public static void get(RequestParams params, final HttpCallBack<String> callBack) {
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callBack.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callBack.onError(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public static void post(RequestParams params, final HttpCallBack callBack) {
        LogUtil.d(TAG, "--------------------------分割线--------------------------");
        List<KeyValue> list = params.getStringParams();
        LogUtil.d(TAG, "调用" + params.getUri() + "所传参数：");
        LogUtil.d(TAG, params.getStringParams().toString());
        LogUtil.d(TAG, "--------------------------分割线--------------------------");

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callBack.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callBack.onError(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
}
