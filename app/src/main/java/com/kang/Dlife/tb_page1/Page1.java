package com.kang.Dlife.tb_page1;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kang.Dlife.R;


//不是Activity就一定要宣告View
public class Page1 extends Fragment implements View.OnClickListener {
    private RelativeLayout rlNoUpload;
    private RelativeLayout rlAlreadyUpload;

    private ImageView ivNoUpload;
    private ImageView ivAlreadyUpload;

    private TextView tvNoUpload;
    private TextView tvAlreadyUpload;

    private Fragment NoUpload;
    private Fragment AlreadyUpload;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        //因為有宣告view, 所以之後可以在這頁裡面找下面之後要用到的id
        View view = inflater.inflate(R.layout.page1, container, false);
        //因為是Fragment所以要把this改成getActivity
        initView(view);
        // 初始tab按鈕事件
        initEvent();
        // 初始化並設定Fragment
        initFragment(0);
        return view;
    }


    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewover, fragment);
        fragmentTransaction.commit();
    }

    private void initFragment(int index) {
        switch (index) {
            case 0:
                NoUpload = new Page1_NoUpload();
                switchFragment(NoUpload);
                break;

            case 1:
                AlreadyUpload = new Page1_AlreadyUpload();
                switchFragment(AlreadyUpload);
                break;

            default:
                break;
        }

    }


    private void initEvent() {
        // 建立按鈕偵測
        rlNoUpload.setOnClickListener(this);
        rlAlreadyUpload.setOnClickListener(this);

    }

    private void initView(View view) {

        this.rlNoUpload = (RelativeLayout) view.findViewById(R.id.rlNoUpload);
        this.rlAlreadyUpload = (RelativeLayout) view.findViewById(R.id.rlAlreadyUpload);

        this.ivNoUpload = (ImageView) view.findViewById(R.id.ivNoUpload);
        this.ivAlreadyUpload = (ImageView) view.findViewById(R.id.ivAlreadyUpload);

        this.tvNoUpload = (TextView) view.findViewById(R.id.tvNoUpload);
        this.tvAlreadyUpload = (TextView) view.findViewById(R.id.tvAlreadyUpload);


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {

        restartBotton();
        // 當按到選擇的按鈕會換成灰色的圖片
        switch (v.getId()) {
            case R.id.rlNoUpload:
                ivNoUpload.setImageResource(R.drawable.select_left_onclick);
                tvNoUpload.setTextColor(Color.parseColor("#f5f5f6"));
                initFragment(0);
                break;
            case R.id.rlAlreadyUpload:
                ivAlreadyUpload.setImageResource(R.drawable.select_right_onclick);
                tvAlreadyUpload.setTextColor(Color.parseColor("#f5f5f6"));
                initFragment(1);
                break;
            default:
                break;
        }

    }

    @SuppressLint("ResourceAsColor")
    private void restartBotton() {
        // 把按鈕圖片再用回成白色
        ivNoUpload.setImageResource(R.drawable.select_left);
        tvNoUpload.setTextColor(Color.parseColor("#48A28C"));
        ivAlreadyUpload.setImageResource(R.drawable.select_right);
        tvAlreadyUpload.setTextColor(Color.parseColor("#48A28C"));
    }

}