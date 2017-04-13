package com.jin.slnews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.jin.utils.SharedPreferencesUtils;

import cn.jpush.android.api.JPushInterface;

public class JGReceiver extends BroadcastReceiver {

    private static final String TAG = "onReceive";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
//			String content = SharedPreferencesUtils.getParam(context,
//					"content", "")
//					+ "\n\n"
//					+ bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            System.out.println("收到了自定义消息。消息内容是 ：" + content);
            SharedPreferencesUtils.setVersion(context, content);
            if (HomeActivity.handler != null) {
                Message msg = new Message();
                msg.obj = content;
                HomeActivity.handler.sendMessage(msg);
            }

            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
//			Intent i = new Intent(context, MainActivity.class); // 自定义打开的界面
//			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			i.putExtra("text", bundle.getString(JPushInterface.EXTRA_EXTRA));
//			context.startActivity(i);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }

    }

}
