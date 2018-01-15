package com.kang.Dlife;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kang.Dlife.sever.GPSService;
import com.kang.Dlife.tb_page1.Page1;
import com.kang.Dlife.tb_page2.Page2;
import com.kang.Dlife.tb_page3.Page3;
import com.kang.Dlife.tb_page4.Page4;

public class TabActivity extends FragmentActivity implements OnClickListener {
    // 宣告Linearlayout
    private LinearLayout ll_home;
    private LinearLayout ll_address;
    private LinearLayout ll_friend;
    private LinearLayout ll_setting;

    // 宣告ImageView
    private ImageView iv_home;
    private ImageView iv_wall;
    private ImageView iv_friend;
    private ImageView iv_setting;

    // 宣告Fragment
    private Fragment homeFragment;
    private Fragment addressFragment;
    private Fragment friendFragment;
    private Fragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_activity);
        // 初始化控制元件
        initView();
        // 初始tab按鈕事件
        initEvent();
        // 初始化並設定Fragment
        initFragment(0);
        //啟動GPS service
        Intent intent = new Intent( this, GPSService.class);
        startService(intent);

    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = TabActivity.this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewpage, fragment);
        fragmentTransaction.commit();
    }

    private void initFragment(int index) {
        switch (index) {
            case 0:
                    homeFragment = new Page1();
                    switchFragment(homeFragment);
                break;

            case 1:
                    addressFragment = new Page2();
                    switchFragment(addressFragment);
                break;
            case 2:
                    friendFragment = new Page3();
                    switchFragment(friendFragment);
                break;
            case 3:
                    settingFragment = new Page4();
                    switchFragment(settingFragment);
                break;

            default:
                break;
        }

    }


    private void initEvent() {
        // 建立按鈕偵測
        ll_home.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        ll_friend.setOnClickListener(this);
        ll_setting.setOnClickListener(this);

    }

    private void initView() {

        // 底部tab的Linearlayout
        this.ll_home = (LinearLayout) findViewById(R.id.TabBook);
        this.ll_address = (LinearLayout) findViewById(R.id.TabWall);
        this.ll_friend = (LinearLayout) findViewById(R.id.TabFriend);
        this.ll_setting = (LinearLayout) findViewById(R.id.TabSetting);

        // 底部tab的ImageView
        this.iv_home = (ImageView) findViewById(R.id.TabBookImg);
        this.iv_wall = (ImageView) findViewById(R.id.TabWallImg);
        this.iv_friend = (ImageView) findViewById(R.id.TabFriendImg);
        this.iv_setting = (ImageView) findViewById(R.id.TabSettingImg);

    }

    @Override
    public void onClick(View v) {

        restartBotton();
        // 當按到選擇的按鈕會換成灰色的圖片
        switch (v.getId()) {
            case R.id.TabBook:
                iv_home.setImageResource(R.drawable.book_onclick);
                initFragment(0);
                break;
            case R.id.TabWall:
                iv_wall.setImageResource(R.drawable.wall_onclick);
                initFragment(1);
                break;
            case R.id.TabFriend:
                iv_friend.setImageResource(R.drawable.friend_onclick);
                initFragment(2);
                break;
            case R.id.TabSetting:
                iv_setting.setImageResource(R.drawable.setting_onclick);
                initFragment(3);
                break;

            default:
                break;
        }

    }

    private void restartBotton() {
        // 把按鈕圖片再用回成白色
        iv_home.setImageResource(R.drawable.book);
        iv_wall.setImageResource(R.drawable.wall);
        iv_friend.setImageResource(R.drawable.friend);
        iv_setting.setImageResource(R.drawable.setting);
    }

}