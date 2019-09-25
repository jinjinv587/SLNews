package com.jin.utils;

import org.xutils.common.Callback;

public abstract class MyHttpCallBack implements Callback.CommonCallback<String> {
    private static final String TAG = "MyHttpCallBack";

    @Override
    public void onSuccess(String result) {
        onHttpSuccess(result);
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ex.printStackTrace();
        LogUtil.e(TAG, "onError:" + ex.getMessage());
        onHttpFail(500, ex.getMessage());
    }

    @Override
    public void onCancelled(CancelledException cex) {
        LogUtil.e(TAG, "onCancelled");
    }

    @Override
    public void onFinished() {
        LogUtil.e(TAG, "onFinish");
    }

    public abstract void onHttpSuccess(String json);

    public abstract void onHttpFail(int code, String msg);

}
