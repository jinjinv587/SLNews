package com.jin.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jin.slnews.R;
import com.jin.views.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 自动更新的工具类，需要传入上下文，以及一个json的url(访问方式是get且不需要穿任何参数)。
 * 网络json需要4个字段，名字、大小和类型写必须如下:String versionName,int versionCode,String
 * versionCode,String downLoadUrl。 例如:{"versionName"
 * :"2.0","versionCode":2,"versionCode":"新增功能，赶紧更新吧！！！","downLoadUrl"
 * :"http://java666.com/slhelper_update/slhelper2.0.apk"}
 * 用到的第三方jar包有:gson-2.3.1和xUtils-2.6.14
 *
 * @author 王金强
 *         <p/>
 *         2015-12-15
 */
public class AutoUpdate {

    private static UpdateInfo updateInfo;

//    /**
//     * @param mActivity 上下文
//     * @param jsonUrl   json的url
//     */
//    public static void check(final Activity mActivity, String jsonUrl) {
//        System.out.println("正在检查更新...");
//
//        HttpUtils httpUtils = new HttpUtils();
//        httpUtils.send(HttpMethod.GET, jsonUrl, new RequestCallBack<String>() {
//
//            @Override
//            public void onFailure(HttpException arg0, String arg1) {
//                System.out.println("检查更新失败:" + arg1);
//            }
//
//            @Override
//            public void onSuccess(ResponseInfo<String> reslut) {
//                String json = reslut.result;
//                //System.out.println("检查更新网络返回:" + json);
//                try {
//                    JSONObject object = new JSONObject(json);
//                    updateInfo = new UpdateInfo();
//                    updateInfo.versionCode = object.getInt("versionCode");
//                    updateInfo.versionName = object.getString("versionName");
//                    updateInfo.description = object.getString("description");
//                    updateInfo.downLoadUrl = object.getString("downLoadUrl");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    updateInfo = new UpdateInfo();
//                    updateInfo.versionCode = 1;
//                    updateInfo.versionName = "";
//                    updateInfo.description = "";
//                    updateInfo.downLoadUrl = "";
//                }
//
//
//                if (updateInfo.versionCode > getVersionCode(mActivity)) {// 判断是否有更新
//                    // 服务器的VersionCode大于本地的VersionCode
//                    // 说明有更新, 弹出升级对话框
//                    showUpdateDailog(mActivity);
//                }
//            }
//        });
//    }

    public static void check(final Context mActivity, String json) {//根据推送过来的，检测更新

        if (TextUtils.isEmpty(json)) {
            return;
        }
        //System.out.println("检查更新网络返回:" + json);
        try {
            JSONObject object = new JSONObject(json);
            updateInfo = new UpdateInfo();
            updateInfo.versionCode = object.getInt("versionCode");
            updateInfo.versionName = object.getString("versionName");
            updateInfo.description = object.getString("description");
            updateInfo.downLoadUrl = object.getString("downLoadUrl");

        } catch (JSONException e) {
            e.printStackTrace();
            updateInfo = new UpdateInfo();
            updateInfo.versionCode = 1;
            updateInfo.versionName = "";
            updateInfo.description = "";
            updateInfo.downLoadUrl = "";
        }


        if (updateInfo.versionCode > getVersionCode(mActivity)) {// 判断是否有更新
            // 服务器的VersionCode大于本地的VersionCode
            // 说明有更新, 弹出升级对话框
            showUpdateDailog1(mActivity);
        }
    }

    private static CustomDialog dialog;

    /**
     * 升级对话框
     */
    protected static void showUpdateDailog(final Activity mActivity) {
        dialog = new CustomDialog(mActivity,
                R.style.CustomDialog, "最新版本:" + updateInfo.versionName, updateInfo.description, "更新", "取消",
                new CustomDialog.CustomDialogListener() {
                    @Override
                    public void OnClick(View view) {
                        if (view.getId() == R.id.Dlg_Yes) {
                            dialog.dismiss();// 使对话框消失

                        } else {
                            dialog.dismiss();
                        }
                    }
                });
        dialog.show();
    }

    /**
     * 升级对话框
     */
    protected static void showUpdateDailog1(final Context mActivity) {
        dialog = new CustomDialog(mActivity,
                R.style.CustomDialog, "最新版本:" + updateInfo.versionName, updateInfo.description, "去下载", "取消",
                new CustomDialog.CustomDialogListener() {
                    @Override
                    public void OnClick(View view) {
                        if (view.getId() == R.id.Dlg_Yes) {
                            dialog.dismiss();// 使对话框消失
                            //跳转浏览器下载
                            String url = updateInfo.downLoadUrl; // web address
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            mActivity.startActivity(intent);
                        } else {
                            dialog.dismiss();
                        }
                    }
                });
        dialog.show();
    }


    /**
     * 获取本地app的版本号
     *
     * @return
     */
    public static int getVersionCode(Context mActivity) {
        PackageManager packageManager = mActivity.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mActivity.getPackageName(), 0);// 获取包的信息

            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (NameNotFoundException e) {
            // 没有找到包名的时候会走此异常
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 获取本地app的版本名
     *
     * @return
     */
    public static String getVersionName(Activity mActivity) {
        PackageManager packageManager = mActivity.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mActivity.getPackageName(), 0);// 获取包的信息
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            // 没有找到包名的时候会走此异常
            e.printStackTrace();
        }

        return "";
    }
}

class UpdateInfo {
    public String versionName;// 版本名

    public int versionCode;// 版本号

    public String description;// 版本描述

    public String downLoadUrl;// 下载地址

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", description='" + description + '\'' +
                ", downLoadUrl='" + downLoadUrl + '\'' +
                '}';
    }
}