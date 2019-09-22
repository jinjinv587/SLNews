package com.jin.slnews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioGroup;

import com.jin.fragment.ChengJiFragment;
import com.jin.fragment.ShouYeFragment;
import com.jin.fragment.WoDeFragment;
import com.jin.utils.AutoUpdate;
import com.jin.utils.SharedPreferencesUtils;
import com.jin.utils.ViewUtils;

public class HomeActivity extends FragmentActivity {
    private Fragment[] fragments;
    private int index;
    // 当前fragment的index
    private int currentTabIndex = 2;
    private RadioGroup rg_bottom;
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
        // 检查更新
//        if ((Boolean) SharedPreferencesUtils.getParam(this, "isAutoUpdate", true)) {
//            AutoUpdate.check(this, Constant.UPDATE_JSON_URL);
//        }

        AutoUpdate.check(this, SharedPreferencesUtils.getVersion(this));
        rg_bottom = findViewById(R.id.rg_bottom);
        rg_bottom.check(R.id.rb_chengji);// 默认勾选成绩

        ShouYeFragment shouYeFragment = new ShouYeFragment();
        ChengJiFragment chengJiFragment = new ChengJiFragment();
        WoDeFragment woDeFragment = new WoDeFragment();
        fragments = new Fragment[]{shouYeFragment,  chengJiFragment,  woDeFragment};
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, chengJiFragment)
                //.add(R.id.fl_content, shouYeFragment).hide(shouYeFragment)
                //.add(R.id.fl_content, biaoBaiFragment).hide(biaoBaiFragment)
                // .add(R.id.fl_content, woDeFragment).hide(woDeFragment)
                // .add(R.id.fl_content, zhaoLingFragment).hide(zhaoLingFragment)
                .commit();

//        rg_bottom.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(RadioGroup arg0, int arg1) {
//                switch (arg1) {
//                    case R.id.rb_shouye:
//                        //Toast.makeText(HomeActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
//                        //index = 0;
//                        break;
//                    case R.id.rb_zhaoling:
//                        index = 1;
//                        break;
//                    case R.id.rb_chengji:
//                        index = 2;
//                        break;
//                    case R.id.rb_geren:
//                        index = 4;
//                        break;
//                    case R.id.rb_biaobai:
//                        //Toast.makeText(HomeActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
//                        index = 3;
//                        break;
//                }
//                if (currentTabIndex != index) {
//                    FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
//                    trx.hide(fragments[currentTabIndex]);
//                    trx.show(fragments[index]).commit();
//                }
//                currentTabIndex = index;
//
//            }
//        });
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
