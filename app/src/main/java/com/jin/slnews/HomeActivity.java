package com.jin.slnews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;

import com.jin.fragment.ChengJiFragment;
import com.jin.utils.AutoUpdate;
import com.jin.utils.SharedPreferencesUtils;
import com.jin.utils.ViewUtils;

public class HomeActivity extends FragmentActivity {

    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        ViewUtils.editStatusBarColor(this);
        init();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                AutoUpdate.check(HomeActivity.this, SharedPreferencesUtils.getVersion(HomeActivity.this));
            }
        };
    }

    /**
     * 初始化
     */
    private void init() {

        ChengJiFragment chengJiFragment = new ChengJiFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, chengJiFragment).commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);

    }

}
